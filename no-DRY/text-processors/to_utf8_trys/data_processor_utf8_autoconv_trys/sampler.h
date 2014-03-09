// encoding : utf8
/**
  "Copyright [year] <Copyright Owner>"

  dict :
    mwf - messege or warning or failure
    frw - КБВ
  @TODO: <igor.a.lugansky@gmail.com> [optionally include text about more work to be done]
*/
#ifndef CC_HEADERS_WEB_SERVICE_SAMPLER_H_
#define CC_HEADERS_WEB_SERVICE_SAMPLER_H_
#include <map>
#include <vector>
//#include <deque>
#include <string>

// Other
#include <utils/g_abstractions.h>
#include <boost/foreach.hpp>
#include <boost/shared_ptr.hpp>

// App
#include <app-server-code/reuse/fsm_elements.h>
#include <app-server-code/app_specific/app_cfg.h>
#include <app-server-code/cross_cuttings/service_journal.h>  // it's aberration
#include <app-server-code/app_specific/app_types.h>

#define foreach_ BOOST_FOREACH
#define foreach_r_ BOOST_REVERSE_FOREACH

namespace tmitter_web_service {

#ifndef _CROSS_GCC
// '_itoa': This function or variable may be unsafe.
// Consider using _itoa_s instead. To disable deprecation, use 
// _CRT_SECURE_NO_WARNINGS. See online help for details.
#define CRT_SECURE_NO_WARNINGS
#endif  // _CROSS_GCC

struct sBounds {
  uchar iFRWMax;
  uchar iFRWLow;

  uchar iTemMax;
  uchar iTemLow;

  // в %!!!
  int iPowMax;
  int iPowLow;
};

// @TODO: <igor.a.lugansky@gmail.com> 
//  состояние нужно сохранять при каждой итерации на случай выкл.
//    так как при включении собъется.
static const std::string kPathToJournal_ = "journals/journal.txt";
class Sampler {
 public:
  void (*OnShutDown)(uint);  // TODO_new Для переносимости убрать отсюда
  Sampler();
  ~Sampler(void) {}

  int lengthAllParams___;

  void PrepareForAllParam();
  void PrepareForAllParam_v1();
  
  void RstSamplerFSM();
 
  std::string GetJournalContent(int& err);

  void ResetSNMPJournalSettings(int IDDevice, std::string Ip, int DstPort);

  // похоже на самый главный обработчик данных
  // похоже есть на вхоже декодер запроса
  int ProcessAnswer(uchar* cBuf, 
                    int iBufLen, 
                    int  &iCurTransmNum, 
                    uint type_protocol_);

  void SetTransmitterNumber(int  iNewTransmNum);

#ifndef _CROSS_GCC
  // Danger!!
  void PutExternalEventHandler(HANDLE *hNewCmdEvent, int iNumActiveEvent);
#endif  // _CROSS_GCC
 
  // Похоже тоже данные получают - нет это запросы - а где ответы?
  void RequestMainParams(uchar* cBuf, int buffer_size, int &wrote);
  void RequestAllParams(uchar* cBuf, int buffer_size, int &wrote);
  void RequestAllParams_v1(uchar* cBuf, int buffer_size, int &wrote);  // protocol version 1
  void RequestTransmitterCfg(uchar* cBuf, int buffer_size, int &wrote);  // configuration of Transmitter
 
  void SetVtvIP(char* cVTV1, uint uiP1, char* cVTV2, uint  uiP2);
  void SetExtIP(char* cExtNew);
  void UpdateMaxTemre(int  iAddTemre, int iAddStatus);  // update maximum temperature

  void FillBufferWithCmd(uchar* cBuf, int buffer_size, int &wrote, uint cmd);
  void FillBufferWithCmd_v1(uchar* cBuf, int buffer_size, int &wrote, uint cmd);

  // Заполняют строки для работы веб страницы
  void PackMainParamsTransmitter(std::string &s);
  void PasteAjaxCFGTransm(std::string &s); 
  void PasteAjaxVtvMod(std::string &s, int number);
  void AppendETVMainParamForWeb(std::string &s, int number);
  void PasteAjaxPAB_main_pre(std::string &s, int number);
  void PasteAjaxPAB_PA(std::string &s, int number, int m);
  void PasteAjaxgetbd(std::string &s, int number);
  void PasteAjaxgetbn(std::string &s, int number);
  void PasteAjaxOK(std::string &s);
  void PasteAjaxEr(std::string &s);
 
  bool RecodeAndRunCmdFromWeb(ParametersMap &p, uint type_protocol_);
  void ResetParamForSNMP();
  void ApplyMaxTemperature();
  void SetUsedTime(bool bUsed);
  void SetUsedReserv(bool bUsed);
  void SetWarningBounds(sBounds  &sNewBounds);
  void SetDisplayChan(int &iNewChan);

#ifdef _DEBUG
  int counterDebug___;
#endif
  void InserArrayPart(
     const simple_type_processors::uint8* ptr, 
     const int length,
     const std::string marker);
  // 

  // обрабатывает состояние
  ListLines Rpt(
    const uchar* localCopyBuffer, 
    uchar type_protocol, 
    int bufferLength);

  // меняет состяние
  void ParsePkgAndChangeState(
    /*const*/ uchar* localCopyBuffer, 
    uchar type_protocol,
    int bufferLength);
  void ParseCfgSystem(uchar* localCopyBuffer);
  MapDataArrays mapData_;

  // текстовой журнал
  service_journals::ServiceJournalC_IO journal;

 private:
  void StateProcessing();
  // trigger control mode 1 - distance  or 0 - local
  fsms_elements::Trigger ctrlModeTgr_;

  // МУ или ДУ
  // есть еще Авт. но как реализовать пока не ясно
  std::string typeControl_;  
    
  // trigger loading 1 - equivalent 0 - antenna
  fsms_elements::Trigger typeLoadTgr_;

 public:
  fsms_elements::Trigger tmitterLockTgr___;
  bool TmitterIsUnlock() {
    return (tmitterLockTgr___.D == 0);
  }

  // trigger transmitter on/off 1 - on 0 - off
  fsms_elements::Trigger tmitterOnTgr___;
  
  bool TmitterIsOn() {
    return (tmitterOnTgr___.D == 1);
  }

  int unlockCounter_;
  static const int kMaxUnlockCounter_ = 4;
  bool TimoutIsOver() {
    return (unlockCounter_ >= kMaxUnlockCounter_);
  }
 private:
  // trigger transmitter status
  fsms_elements::Trigger statusTmitterTgr_;

  bool failsReseted_;
 
  // trigger transmitter half mode 1 - half power mode, 0 - full power mode
  fsms_elements::Trigger powHalfModeTgr_;
  
  // trigger input stream
  // Input stream 1 - error
  fsms_elements::Trigger istreamTgr_;

  // trigger synch
  // Out synchronization
  fsms_elements::Trigger outSynTgr_;
  
  // PAB
  fsms_elements::Trigger PABOnOffTgr_[kMaxPABs];  // 1 -on
  bool bTurnoff_[kMaxPABs];
  fsms_elements::Trigger PABLockTgr_[kMaxPABs];    // 1 -lock
  bool IsPABLock(int number) {
    return (PABLockTgr_[number].D == 1);
  }

  bool printPABLock_[kMaxPABs];
  bool printPABLockEvent_[kMaxPABs];
  bool printPABNoInPower_[kMaxPABs];
  bool printPABNoInPowerEvent_[kMaxPABs];
  bool printPABUnlock_[kMaxPABs];
  bool printPABInPowerOk_[kMaxPABs];
  
  fsms_elements::Trigger countOnOffPABsTgr_;
  fsms_elements::Trigger lockPABTgr_;
  //uint countOnOffPABsTgrQ_;
  //uint countOnOffPABsTgrD_;
  //uint lockPABTgrQ_;
  //uint lockPABTgrD_;

  // VTV
  // ASI 1 2// bits 3..0 - ASI1  7..4 - ASI2
  fsms_elements::Trigger exciterASI12Tgr_[kMaxExciters];
  
  // изменилось ли состояние мощности
  //fsms_elements::Trigger powerTgr_;
  bool powerOkTTgr_;
  bool needPrintFullPower_;
  bool needPrintOkPower_;

  // изменилось ли состояние мощности
  //fsms_elements::Trigger FRWTgr_;
  bool FRWOkTTgr_;
  bool needPrintFullFRW_;
  bool needPrintOkFRW_;

  /* analog */
  // trigger Vtv signal on off
  uchar vtvSigOnOffTgrD_[kMaxExciters];     
  uchar vtvSigOnOffTgrQ_[kMaxExciters];
 
  // trigger overmodulation
  uchar vtvOverModTgrD_[kMaxExciters];
  uchar vtvOverModTgrQ_[kMaxExciters];
 
  // 
  uchar ibAnlgDigit_;      // 1-digital 0 - analog 
  uchar ibWork_;           // 1 - norm 0 - not norm
  uchar exciterLock_;     // Vtv 1 - lock 0 -unlock
  uchar ibPowAmpLock_;     // Power Amplifier // 1 - lock 0 -unlock
  uchar ibVtv12OnOff_;     // Vtv 1 or 2 1 - on
  uchar ibRadioMode_;      // radiation mode 1 -reserve 0 - main
  
  // Transmitter 1 - ready 0 -not ready
  // набор Отказов для вывода на веб-интерфейс?
  uchar transmitterReady_;

  //
  //
  uchar failNet_;   // 1 - fail
  uchar failBallast_;  // 1 -fail
  uchar failFRW_;      // FRW (or KBV in russion)1 - fail
  uchar failCoolling_; // 1 - fail
  uint ibPAB;       // power amplifier block (10..1) for bits 9..0, 1 - fail
  uchar ibVtv;      // bits 1..0 for Vtv2..Vtv1, 1 - fail
  uchar ibVtvModStatus[kMaxExciters];    // VTV modulator status// 1 - fail
  uchar ibVtvUsStatus[kMaxExciters];    // VTV US status // 1 - fail
  
  // PAB
  uchar ibPABOutPowerStatus[kMaxPABs];  // 1 - invalid < Pmin
  uchar ibPreVT12status[kMaxPABs];    // 1..0;  1 - VT 2, 0 - VT 1
  uchar ibPreTemStaus[kMaxPABs]; // temperature status  // 1 -fail
  uchar ibPreMIP[kMaxPABs]; // 1 -fail
  uchar ibPaVT12status[kMaxPABs][kMaxTAsPerPAB]; // 1..0;  1 - VT 2, 0 - VT 1
  uchar ibPaTemStaus[kMaxPABs][kMaxTAsPerPAB]; // 1 -fail
  uchar ibPaMIP[kMaxPABs][kMaxTAsPerPAB]; // 1 -fail
  
  // БКН
  uchar ibBCVstatus[kMaxBCLs];
  uchar ibBCV_R1[kMaxBCLs]; // R1 status
  uchar ibBCV_TR1[kMaxBCLs]; // Temperature R1 status
  uchar ibBCV_R2[kMaxBCLs];
  uchar ibBCV_TR2[kMaxBCLs];
  uchar ibBCV_R3[kMaxBCLs];
  uchar ibBCV_TR3[kMaxBCLs];
  uchar ibBCV_R4[kMaxBCLs];
  uchar ibBCV_TR4[kMaxBCLs];
  uchar ibBCV_R5[kMaxBCLs];
  uchar ibBCV_TR5[kMaxBCLs];
  uchar ibBCV_R6[kMaxBCLs];
  uchar ibBCV_TR6[kMaxBCLs];
  uchar ibBCV_R7[kMaxBCLs];
  uchar ibBCV_TR7[kMaxBCLs];
  uchar ibBCV_R8[kMaxBCLs];
  uchar ibBCV_TR8[kMaxBCLs];
  
  // БД
  uchar ibBDstatus[kMaxDBs];

  // Суммы
  uint failsTotalD_;    // total fails code
  uint failsTotalQ_;     // last value of fail code
  uint failsTotalQQ_;    // before last value
  
  // триггеры
  // по осн. парам.
  bool Tgr_FailNet_; 
  bool Tgr_FailBallast_;
  bool Tgr_FailFRW_;
  bool Tgr_FailCoolling_; 
  bool Tgr_PAB_[kMaxPABs]; 
  bool Tgr_Vtv_[kMaxExciters];

  //
  bool Tgr_VtvMod_[kMaxExciters];
  bool Tgr_VtvUs_[kMaxExciters];

  bool Tgr_PABOutPower_[kMaxPABs];
  bool Tgr_PreVT12_[kMaxPABs];
  bool Tgr_PreTem_[kMaxPABs]; 
  bool Tgr_PreMIP_[kMaxPABs]; 
  bool Tgr_PaVT12_[kMaxPABs][kMaxTAsPerPAB]; 
  bool Tgr_PaTem_[kMaxPABs][kMaxTAsPerPAB]; 
  bool Tgr_PaMIP_[kMaxPABs][kMaxTAsPerPAB]; 

  bool Tgr_BCL_[kMaxBCLs];
  bool Tgr_BCL_R_[7][kMaxBCLs]; 
  bool Tgr_BCL_TR_[7][kMaxBCLs]; 

  bool Tgr_BD_[kMaxDBs];

  // T-триггеры Отказов
  fsms_elements::DtTrigger DtTgr_FailNet_; 
  fsms_elements::DtTrigger DtTgr_FailBallast_;
  fsms_elements::DtTrigger DtTgr_FailFRW_;
  fsms_elements::DtTrigger DtTgr_FailCoolling_; 
  fsms_elements::DtTrigger DtTgr_PAB_[kMaxPABs]; 
  fsms_elements::DtTrigger DtTgr_Vtv_[kMaxExciters];

  // Продолжение - Отказы по пакетам
  fsms_elements::DtTrigger DtTgr_VtvMod_[kMaxExciters];
  fsms_elements::DtTrigger DtTgr_VtvUs_[kMaxExciters];

  fsms_elements::DtTrigger DtTgr_PABOutPower_[kMaxPABs];
  fsms_elements::DtTrigger DtTgr_PABFRWStatus_[kMaxPABs];
  
  fsms_elements::DtTrigger DtTgr_PreVT12_[kMaxPABs];
  fsms_elements::DtTrigger DtTgr_PreTem_[kMaxPABs]; 
  fsms_elements::DtTrigger DtTgr_PreMIP_[kMaxPABs]; 

  fsms_elements::DtTrigger DtTgr_PaVT12_[kMaxPABs][kMaxTAsPerPAB]; 
  fsms_elements::DtTrigger DtTgr_PaTem_[kMaxPABs][kMaxTAsPerPAB]; 
  fsms_elements::DtTrigger DtTgr_PaMIP_[kMaxPABs][kMaxTAsPerPAB]; 

  fsms_elements::DtTrigger DtTgr_BCL_[kMaxBCLs+10];
  fsms_elements::DtTrigger DtTgr_BCL_R_1[kMaxBCLs+10];
  fsms_elements::DtTrigger DtTgr_BCL_R_2[kMaxBCLs+10];
  fsms_elements::DtTrigger DtTgr_BCL_R_3[kMaxBCLs+10];
  fsms_elements::DtTrigger DtTgr_BCL_R_4[kMaxBCLs+10];
  fsms_elements::DtTrigger DtTgr_BCL_R_5[kMaxBCLs+10];
  fsms_elements::DtTrigger DtTgr_BCL_R_6[kMaxBCLs+10];
  fsms_elements::DtTrigger DtTgr_BCL_R_7[kMaxBCLs+10];
  fsms_elements::DtTrigger DtTgr_BCL_R_8[kMaxBCLs+10];

  fsms_elements::DtTrigger DtTgr_BCL_TR_1[kMaxBCLs+10];
  fsms_elements::DtTrigger DtTgr_BCL_TR_2[kMaxBCLs+10]; 
  fsms_elements::DtTrigger DtTgr_BCL_TR_3[kMaxBCLs+10]; 
  fsms_elements::DtTrigger DtTgr_BCL_TR_4[kMaxBCLs+10]; 
  fsms_elements::DtTrigger DtTgr_BCL_TR_5[kMaxBCLs+10]; 
  fsms_elements::DtTrigger DtTgr_BCL_TR_6[kMaxBCLs+10]; 
  fsms_elements::DtTrigger DtTgr_BCL_TR_7[kMaxBCLs+10];
  fsms_elements::DtTrigger DtTgr_BCL_TR_8[kMaxBCLs+10]; 


  fsms_elements::DtTrigger DtTgr_BD_[kMaxDBs];
  //
  //

 public:
  uchar ibRS485;    // 1 - fail
  uchar ibI2C;      // 1 - fail

  uint channalValuePacked_;
  int iChannel; // Transmitter// Channel number
  int iChannelSetup;
  uint ibPower;
  int realPowerRepresent___; // Transmitter// Channel power
  uint ibValueFRW;
  int FRWValue___; // Transmitter  // real FRW * 100 = iFRW
  uchar ibSoundLock;    // Transmitter // Sound lock
  uchar ibSU1St;    // SU1 (or US1  see protocol) 1 - fail
  uchar ibSU2St;
  uint failBallast_Power;
  int iBallastPower;

  int transmitterAddress___;    // Transmitter// Number
  int iMaxTemre;    // Transmitter // maximum tempreture
  int iMaxStatus;    // Transmitter temperature status
  int new_max_temperature_;
  int iNewMaxStatus;
  int connecton_state___;  // connection status
  int snmp_connecton_status___;  // snmp connection status
 
  // Набор ссылок на внешние "спусковые крючки"
private :
#ifndef _CROSS_GCC
  HANDLE externalEventsArray_[10];
#endif  // _CROSS_GCC

  static const uchar  kSendCmd_ = 0;
  static const uchar  kBoundUpdate_ = 1;
  static const uchar  kSendSnmp_ = 2;
 
  // Line 585:   sampler_.PutExternalEventHandler(&(transport_layer_events_set_[EV_SEND_BY_CONTROL_CMD]), 0);
  // Line 586:   sampler_.PutExternalEventHandler(&(hEvents[kSNMPSendByEventEvent_]), 1);
  // Line 587:   sampler_.PutExternalEventHandler(&(transport_layer_events_set_[kCentralEventBoundUpdate]), 2);
public :
 
  uint current_ctrl_cmd___;  // current control command (send coomand of transmitter)
  int transmitter_number_to_set_;
  char cVtvIP[kMaxExciters][30];
  uint uiPort[kMaxExciters];
  char cExt[30];    // external IP
   
  char* cSNMP;
  int* iSNMPeriod;
  
  // Msgs Query
  // 
 private:
  int currentQueryIndex_;    // number SNMP events in Queue
  int currentItemInQuery_;
  static const int kMaxSNMPQueue = kQuerySize;  // maximum number SNMP events in Queue
  
  // BYTE iSNMPQueue[QUEUE_SIZE];	//array of codes SNMP events
  // std::string stv[QUEUE_SIZE];	//string array for message
  // BYTE iFaWa[QUEUE_SIZE];		//Fail Ok or Warnig Code for Queue
  // BYTE iMWF[QUEUE_SIZE];		//flag of Event (Fail Ok or Warnig)
  
  // !Danger! То, что непонятно что и зачем используется
  uchar snmpEventsQuery_[kQuerySize];  // array of codes SNMP events
  
  // Msgs
  std::string stringMsgsQuery_[kQuerySize];  // string array for message
  // LLevel
  uchar typesCodesInQuery_[kQuerySize];  // Fail Ok or Warnig Code for Queue
  // HLevel
  uchar typeMsgsQuery_[kQuerySize];  // flag of Event (Fail Ok or Warnig)
  
 public:
  // Msgs
  std::vector<std::string> stringMsgsQuerySTL_;  // string array for message
  // LLevel
  std::vector<int> LLTypeCodesQuerySTL_;  // Fail Ok or Warnig Code for Queue
  // HLevel
  std::vector<int> HLTypeCodesQuerySTL_;  // flag of Event (Fail Ok or Warnig)

 private:
  uchar fixedAlrmRecordIndex_;                  // index in array
  bool hasMsgForSnmp_;   // 
  
  // igor's
  // Набор сообщений за обработку одного пакета - "все параметры".
  // Удаляется поселе записи в журнал и отправки по SNMP.
  // Позиция Отказов сохраняется, Отказы накапливаются.
  ListLines msgsSetFromOnIteration_;
  int statusRecordIndex_;
  static const int kReserveSize = 1024;
  // 
  // Msgs Query
  
  //bool bAfterStart_;
  //bool bAfterStart2_;

  bool newFailOccure_; // New Fail now
  uchar currentMWFCode_; // current Fail Warnig code
 public:
  uchar currentAliveFaWa___;
  
 private:
  //bool powerOk_;
  bool FRWOk_;
  bool temperatureWasOk_;
  int counterFailsAndWarns_; // counter for fails

 public:
  int iUsedTime;
  int iUsedReserv;
  int lengthCfgAnswer; // length of config answer

  sBounds currentBounds___;

  // cfg
  int nominalPower___;
 
  uint numExcitersPack_;     // Number VTV
 private:
  int excitersTotal_;
 public:
  uint ibNumPAB;     // Number PAB
 private:
  uint PABTotal_;
 public:
  uint ibNumPAinPAB;   // Number power amplifier in PAB
  uint preampPerPAB___;
  uint ibNumBCV;     // Number BCV
 private:
  uint BCNTotal_;
  uint ibNUMDB;     // Number BD
  uint DBTotal_;
 public:
  uint ibSizeIBMod; // size of information block for VTV modulator
  uint sizeBlockModParams_;
  uint ibSizeIBPAPAB;  // size of inform block for power amplif block
 private:
  uint sizeBlockTerminalAmpParams_;
 public:
  uint ibSizeIBPrAPAB;    // size of information block for preamplifier
  uint sizeBlockPreampParams_;  // size of inform block preamplif
  uint ibSizeIBBCV;    // size of information block for BCV
 private:
  uint sizeBlockBCNParams_;
 public:
  uint ibEventStringSize; 
  uint sizeEventsString_;
  uint ibFailStringSize;
  uint sizeFailsString_;

  uint ibSizeDB;
  uint sizeBlockDBParams_;

  uint transmitterID___;      // Transmitter Name
  uint exciterType_;              // VTV type
  uint countReservedTransmitters_;    // Number reserved tranmitters

  //int initCounter_;
  //int maxValueInitCounter_;
 

  // VTV  Main
  uchar ibVtvCntrlMode[kMaxExciters];    // Control mode // 1 - dist
  uchar ibVtvRadioMode[kMaxExciters];    // radiation mode // 1 - reserv
  uchar ibVtvAnlgDigit[kMaxExciters];    // analod or digital // 1-digit
  uchar ibUsVtvLock[kMaxExciters];      // US VTV // 1 - lock
  uchar ibModVtvLock[kMaxExciters];      // Modulator Lock // 1 - lock
  uchar ibOwnVtvLock[kMaxExciters];      // VTV lock // 1 - lock
  uchar ibVtvReady[kMaxExciters];      // VTV Ready // 1 - ready
  uchar ibVtvWork[kMaxExciters];      // VTV work// 1 - norm
 
  // пока идет только на Web
  // bits 1..0
  uchar ibVtvStatus[kMaxExciters];      
  
private :
  uchar exciterIstream_[kMaxExciters];      // VTV input stream// 1 - fail

public :
  uchar ibVtvOutSync[kMaxExciters];      // VTV out synchronization// 1 - fail

  uchar exciterIstreamError_[kMaxExciters];    // uchar
    static const uchar bNoConnecion = M1;
  uchar ibVtvErrMod[kMaxExciters];      // uchar
  uint ibVtvOutPower[kMaxExciters];    // vtv output power
  int iVtvOutPower[kMaxExciters];

  // analog
  uchar ibVtvSKSStatus[kMaxExciters];    // S K S
  uchar ibVtvSChMStatus[kMaxExciters];    // S Ch M
  uchar ibVtvSPChStatus[kMaxExciters];    // S P ch
 


  uchar ibVtvVideoLock[kMaxExciters];    // video lock unlock
  uchar ibVtvPowerStatus[kMaxExciters]; 
  uchar ibVtvSound[kMaxExciters];      // sound

  uchar ibVtvChanNumAnalog[kMaxExciters];
  int iVtvChanNumAnalog[kMaxExciters];

  // VTV  Mod
  uchar ibVtvNet[kMaxExciters];      // Net power // bits 2..0
  uchar ibVtvDVBTOnOf[kMaxExciters];  // DVB-H on off// bits 1..0 -
  uchar ibVtvMIPOnOf[kMaxExciters];    // MIP on off// bits 1..0 -
  uchar ibVtvQAM[kMaxExciters];      // bits 2..0 -
  uchar ibVtvCodRate[kMaxExciters];    // bits 3..0 -
  uchar ibVtvGI[kMaxExciters];      // guard interval// bits 3..0 -
  uchar ibVtvHierar[kMaxExciters];    // bits 3..0 -
  uchar ibVtvCarNum[kMaxExciters];    // carriers number// bits 2..0 -
  uint ibVtvChannel[kMaxExciters];    // channel// uchar
  int iVtvChannel[kMaxExciters];   
  uchar ibVtvFreq[kMaxExciters][5]; // frequency// 
  int iVtvFreq[kMaxExciters];
  uint ibVtvOutLevel[kMaxExciters]; // 
  int iVtvOutLevel[kMaxExciters]; // level*10
  uint ibVtvTransmNum[kMaxExciters];    // transmitter number
  int iVtvTransmNum[kMaxExciters]; // 
  uint ibVtvIDCellNum[kMaxExciters];    // ID Cell Number
  int iVtvIDCellNum[kMaxExciters]; // 
  uint ibVtvAdDelay[kMaxExciters];    // additional delay
  int iVtvAdDelay[kMaxExciters]; // 
  uchar ibVtvPrecLineNum[kMaxExciters];    // preccorection line number
  int iVtvPrecLineNum[kMaxExciters]; // 
  uchar ibVtvPrecLOnOff[kMaxExciters];    // 3..0
  uchar ibVtvPrecNonLOnOff[kMaxExciters];  // 3..0
  uchar ibVtvTestMode[kMaxExciters]; // 3..0
  uchar ibVTVNoconnet[kMaxExciters]; // 1 - no connect

  // PAB main
  uchar PABNoConnect_[kMaxPABs];
  uchar ibPABAnlgDigit[kMaxPABs];    // 1 - dig
  uchar ibPABInPowerStatus[kMaxPABs];  // 1 - invalid
  uchar ibPABStatus[kMaxPABs];      // 1 - fail
  uchar ibPABFRWStatus[kMaxPABs];    // 1 - fail

  uint ibPABInPow[kMaxPABs];      // Input power// uchar
  int iPABInPow[kMaxPABs];
  uint ibPABOutPow[kMaxPABs]; // /PAB output power/2 uchar
  int iPABOutPow[kMaxPABs];
  uint ibPABFRW[kMaxPABs]; // FRW value// uchar
  int iPABFRW[kMaxPABs]; // FRW * 100

  // PAB  pre amplif
  uchar ibPreAGC[kMaxPABs]; // auto gain control// 1 -fail
  uchar ibPrePlus15[kMaxPABs]; // +15// 1 -fail
  uint ibPreCurVT1[kMaxPABs]; // current value VT1
  int iPreCurVT1[kMaxPABs]; // cut*10
  uint ibPreCurVT2[kMaxPABs]; // current value VT2
  int iPreCurVT2[kMaxPABs]; // cur*10
  uint ibPreTemValue[kMaxPABs]; // temperature value
  int iPreTemValue[kMaxPABs];

  uint ibPreMIPVoltage[kMaxPABs];
  int iPreMIPVoltage[kMaxPABs];    // vol *10
  uint ibPreAttenVoltage[kMaxPABs]; // attenuator
  int iPreAttenVoltage[kMaxPABs];    // Voltage*10
  uint ibPrePhaseVoltage[kMaxPABs];    // phase setting Voltage
  int iPreAPhaseVoltage[kMaxPABs];    // Voltage*100
  uint ibPreReferVoltage[kMaxPABs];    // reference voltage
  int iPreReferVoltage[kMaxPABs];    // Voltage*10

  // PAB  power amplif - оконечные каскады
  uint ibPaCurVT1[kMaxPABs][kMaxTAsPerPAB]; // current value VT1
  int iPaCurVT1[kMaxPABs][kMaxTAsPerPAB]; // cut*10
  uint ibPaCurVT2[kMaxPABs][kMaxTAsPerPAB]; // current value VT2
  int iPaCurVT2[kMaxPABs][kMaxTAsPerPAB]; // cur*10
  uint ibPaTemValue[kMaxPABs][kMaxTAsPerPAB];
  int iPaTemValue[kMaxPABs][kMaxTAsPerPAB];

  uint ibPaMIPVoltage[kMaxPABs][kMaxTAsPerPAB];
  int iPaMIPVoltage[kMaxPABs][kMaxTAsPerPAB]; // vol *10
 
  // BCV kMaxBCLs
  uchar _bc_loads_uni[kMaxBCLs];
  uchar ibBCVTrOnOf[kMaxBCLs];
  uchar ibBCVTrLock[kMaxBCLs];
  uchar ibBCVAnlgDigit[kMaxBCLs];
  uchar ibBCVPowerStat[kMaxBCLs];

  int iBCV_R1Tem[kMaxBCLs];    // R1 temperature velue
  int iBCV_R1Pow[kMaxBCLs];    // R1 power velue
  int iBCV_R2Tem[kMaxBCLs];
  int iBCV_R2Pow[kMaxBCLs];
  int iBCV_R3Tem[kMaxBCLs];
  int iBCV_R3Pow[kMaxBCLs];
  int iBCV_R4Tem[kMaxBCLs];
  int iBCV_R4Pow[kMaxBCLs];
  int iBCV_R5Tem[kMaxBCLs];
  int iBCV_R5Pow[kMaxBCLs];
  int iBCV_R6Tem[kMaxBCLs];
  int iBCV_R6Pow[kMaxBCLs];
  int iBCV_R7Tem[kMaxBCLs];
  int iBCV_R7Pow[kMaxBCLs];
  int iBCV_R8Tem[kMaxBCLs];
  int iBCV_R8Pow[kMaxBCLs];

  // DB kMaxDBs
  uchar _uni_about_bl_complex_loads[kMaxDBs];
  uchar ibBDTrOnOf[kMaxDBs];
  uchar ibBDTrLock[kMaxDBs];
  uchar ibBDAnlgDigit[kMaxDBs];
  uchar ibBDChannel[kMaxDBs];
  uchar ibBDFactor[kMaxDBs];
 
  int iBDSoundPower[kMaxDBs];
  int iBDOutPower[kMaxDBs];
  uchar iBDFRW[kMaxDBs];

 private:
  // Danger!! длина блоков не передается
  // Похоже функции обработки принятого буффера
  void ProcessAllParamsResponse(uchar* localCopyBuffer, 
      uchar type_protocol, 
      int bufferLength);
  // [0-9] in TZ
  void RptMainStateTmitter();
  void ParseMainStateTmitter(const simple_type_processors::uint8* src);
 
  uchar* RptMainStateTmitter_v1(uchar *source_buffer);
  
  void RptMainParamsExciter(const int number);
  void ParseMainParamsExciter(const int number, 
      const simple_type_processors::uint8* ptrSourceArray);

  void RptModParams(const int number);
  void ParseModParams(int k, 
      const uchar* ptrSourceArray);

  void RptMainParamsPAB(int number);
  void ParseMainParamsPAB(int number, 
      const simple_type_processors::uint8* ptrSourceArray);

  uchar* RptMainParamsPAB_v1(int number, uchar *ptrSourceArray);

  void RptPreAmpParams(int number);
  void ParsePreAmpParams(int number, 
      const uchar *ptrSourceArray);

  void RptTerminalAmpliferParams(int number, int n);
  void ParseTerminalAmpliferParams(
      int number, int n, 
      const uchar *ptrSourceArray);

  void RptParamsBCL(int number);
  void ParseParamsBCL(int number, 
      const uchar *ptrSourceArray);

  void RptParamsDB(int number);
  void ParseParamsDB(int number, 
      const uchar *ptrSourceArray);

  uchar* ParseTransmitterEventsProto(uchar *ptrSourceArray);
  
  // 
  void AppendToCurrentRecord(std::string name, uchar status);
  void AppendToCurrentRecord(std::string name);
  void WriteCurrentMsg(std::string msg);
  void WriteCurrentMsg(std::string msg, uchar key);
  //void TickQueryPosition();
  
  // заносит сообщение об Отказе и подсвечивает
  void RptAlrm(std::string msg);
  // заносит сообщение о снятии Отказ и подсвечивает
  void AlrmHided(std::string msg);

  void ProcessExciterAnalog(uchar number, const uchar* ptrSourceArray);
    
  // Установка пре и пост состояния объекта, в время парсинга пакета 
  // праметров.
  bool TmitterIsOnAndUnlock() const;
  bool CheckStatusAndAlrms() const;
  bool PowerIsBad(int value) const;
  bool FRWIsBad(int value) const;
 public:
  void Preprocess();
  void Postprocess();
};
}

#endif  // CC_HEADERS_WEB_SERVICE_SAMPLER_H_