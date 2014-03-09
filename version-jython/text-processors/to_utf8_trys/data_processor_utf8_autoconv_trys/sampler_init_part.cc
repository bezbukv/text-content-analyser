// encoding : utf8
// "Copyright [year] <Copyright Owner>"  [legal/copyright]

#include <app-server-code/data_processor/sampler_uni_header.h>



namespace tmitter_web_service {
using std::string;
using boost::shared_ptr;

#ifndef _CROSS_GCC
using service_journals::SNMPJournalInterface;
#endif  // _CROSS_GCC


void ResetSNMPJournalSettings(int IDDevice, 
                              std::string Ip, 
                              int DstPort) {

}
void Sampler::RstSamplerFSM() {
  // счетчики в машине состоянй
  unlockCounter_ = 0;
  powerOkTTgr_ = false;
  FRWOkTTgr_ = false;
  for (int i = 0; i < kMaxPABs; ++i ) {  
    printPABLock_[i] = false;
    printPABNoInPower_[i] = true;
    printPABUnlock_[i] = true;
    printPABInPowerOk_[i] = true;
    
    //
    printPABLockEvent_[i] = false;
    printPABNoInPowerEvent_[i] = false;

    bTurnoff_[i] = false;
  }

  // State
  ctrlModeTgr_.Q = 0xFF; 
  typeLoadTgr_.Q = 0xFF;
  tmitterLockTgr___.Q = 0xFF; 
  
  powHalfModeTgr_.Q = 0xFF;
  istreamTgr_.Q = 0xFF;
  outSynTgr_.Q = 0xFF;
  statusTmitterTgr_.Q = 0x00;
  lockPABTgr_.Q = 0;
  countOnOffPABsTgr_.Q = 0;
  typeControl_ = "";

  // флаги Отказов
  Tgr_FailNet_ = false;
  Tgr_FailBallast_ = false;
  Tgr_FailFRW_ = false;
  Tgr_FailCoolling_ = false;
  
  for (int i = 0; i < kMaxExciters; ++i) {
    Tgr_VtvMod_[i] = false;
    Tgr_VtvUs_[i] = false;
    Tgr_Vtv_[i] = false;
  }
  for (int i = 0; i < kMaxPABs; ++i) {
#ifndef IN_TESTS
    Tgr_PAB_[i] = false;
#else 
    Tgr_PAB_[i] = false;
#endif  // IN_TESTS
    Tgr_PABOutPower_[i] = false;
    Tgr_PreVT12_[i] = false;
    Tgr_PreTem_[i] = false;
    Tgr_PreMIP_[i] = false;
    for (int j = 0; j < kMaxTAsPerPAB; ++j) {
      Tgr_PaVT12_[i][j] = false;
      Tgr_PaTem_[i][j] = false;
      Tgr_PaMIP_[i][j] = false;
    }
  }
  // @TODO: <igor.a.lugansky@gmail.com> move const fom here
  const int kMaxRTRs = 7;
  for (int i = 0; i < kMaxBCLs; ++i) {
    Tgr_BCL_[i] = false;
    // Danger!! index inversion!!
    for (int j = 0; j < kMaxRTRs; ++j) {
      Tgr_BCL_R_[j][i] = false;
      Tgr_BCL_TR_[j][i] = false;
    }
  }
  for (int i = 0; i < kMaxDBs; ++i) {
    Tgr_BD_[i] = false;
  }

  failNet_=0;      
  failBallast_=0;    
  failFRW_=0;      
  failCoolling_=0;    
  ibPAB = 0;

  failsTotalD_=0;
  failsTotalQ_ = 7777777;
  failsTotalQQ_ = 7777777;

  currentItemInQuery_=0;
  hasMsgForSnmp_ = false;
  
  // состояние мощности
  //powerOk_ = false;  
  FRWOk_=true; 

  temperatureWasOk_=true;  
  newFailOccure_ = false;
}


Sampler::Sampler( void ) : journal(kPathToJournal_) {
    // чтобы перераспределения памяти не было
  msgsSetFromOnIteration_.reserve(kReserveSize);
    
  // это должно быть в конструкторе
  tmitterOnTgr___.Q = 1;  // Отключен! это важно
    
  RstSamplerFSM();
  
    // уничтожение хэндлов
#ifndef _CROSS_GCC
  for ( int i=0; i<10; i++ ) {
    externalEventsArray_[i]=0;
  }
#endif  // _CROSS_GCC
  
  for ( int i=0; i<(  kMaxExciters  ); i++ )
  {
    cVtvIP[i][0]=0;
    uiPort[i]=8000;
  }
  cExt[0]=0;
  cSNMP=0;
  iSNMPeriod=0;
  nominalPower___=0;
  numExcitersPack_=0;
  excitersTotal_=0;
  ibNumPAB=0;
  PABTotal_=0;
  ibNumPAinPAB=0;
  preampPerPAB___=0;
  ibNumBCV=0;
  BCNTotal_=0;
  ibSizeIBMod=0;
  sizeBlockModParams_=0;
  ibSizeIBPAPAB=0;  //size of inform block power amplif
  sizeBlockTerminalAmpParams_=0;
  ibSizeIBPrAPAB=0;  
  sizeBlockPreampParams_=0;  //size of inform block preamplif
  ibSizeIBBCV=0;
  sizeBlockBCNParams_=0;
  ibEventStringSize=0;
  sizeEventsString_=0;
  ibFailStringSize=0;
  sizeFailsString_=0;
  ibNUMDB=0;
  DBTotal_=0;
  ibSizeDB=0;
  sizeBlockDBParams_=0;
  transmitterID___=0;
  exciterType_=0;
  countReservedTransmitters_=0;
  
  current_ctrl_cmd___=0;
  transmitter_number_to_set_=0;
  
  connecton_state___=0;
  snmp_connecton_status___ =0;
  
  iUsedTime=0;    
  iUsedReserv=0;
  

  //
  fixedAlrmRecordIndex_=0;  
  currentMWFCode_=0;  
  currentAliveFaWa___=0;
  ibAnlgDigit_=0;    
  ibWork_=0;      
  exciterLock_=0;    
  ibPowAmpLock_=0;    
  ibVtv12OnOff_=0;    
  ibRadioMode_=0;    
  transmitterReady_=0;      

  ibRS485=0;    
  ibI2C=0;      
  ibVtv=0;      
  channalValuePacked_=0;  
  iChannelSetup=0;
  iChannel=0;      
  ibPower=0;      
  realPowerRepresent___=0;
  ibValueFRW=0;    
  FRWValue___=0;    
  transmitterAddress___=0;
  failBallast_Power=0;  
  iBallastPower=0;
  ibSoundLock = 0;  
  ibSU1St = 0; 
  ibSU2St=0;
  //initCounter_ = 0;
  //maxValueInitCounter_ = ( MAX_INIT_UNLOCK_CNT );
  iMaxTemre=0;
  iMaxStatus=0;

  //VTV  Main
  for (int i=0; i <(kMaxExciters); i++) {
    ibVtvCntrlMode[i]=0;    //1 - dist
    ibVtvRadioMode[i]=0;    //1 - reserv
    ibVtvAnlgDigit[i]=0;    //1-digit
    ibUsVtvLock[i]=0;      //1 - lock
    ibModVtvLock[i]=0;      //1 - lock
    ibOwnVtvLock[i]=0;      //1 - lock
    ibVtvReady[i]=0;      //1 - ready
    ibVtvWork[i]=0;        //1 - norm
    ibVtvStatus[i]=0;      //bits 1..0 
    ibVtvModStatus[i]=0;    //1 - fail 
    ibVtvUsStatus[i]=0;      //1 - fail
    exciterIstream_[i]=0;      //1 - fail
    ibVtvOutSync[i]=0;      //1 - fail
    exciterIstreamError_[i]=0;      //uchar
    ibVtvErrMod[i]=0;      //uchar
    ibVtvOutPower[i]=0;
    iVtvOutPower[i]=0;

    //VTV  Mod
    ibVtvNet[i]=0;        //bits 2..0
    ibVtvDVBTOnOf[i]=0;      //bits 1..0 -
    ibVtvMIPOnOf[i]=0;      //bits 1..0 -

    ibVtvQAM[i]=0;        //bits 2..0 -
    ibVtvCodRate[i]=0;      //bits 3..0 - 
    ibVtvGI[i]=0;        //bits 3..0 - 
    ibVtvHierar[i]=0;      //bits 3..0 - 
    ibVtvCarNum[i]=0;      //bits 2..0 - 
    ibVtvChannel[i]=0;      //uchar
    iVtvChannel[i]=0;    
    ibVtvFreq[i][5]=0;      //
    iVtvFreq[i]=0;
    ibVtvOutLevel[i]=0;      //
    iVtvOutLevel[i]=0;      //level*10
    ibVtvTransmNum[i]=0;    //
    iVtvTransmNum[i]=0;      //
    ibVtvIDCellNum[i]=0;    //
    iVtvIDCellNum[i]=0;      //
    ibVtvAdDelay[i]=0;    //
    iVtvAdDelay[i]=0;      //
    ibVtvPrecLineNum[i]=0;    //
    iVtvPrecLineNum[i]=0;      //
    ibVtvPrecLOnOff[i]=0;    //3..0
    ibVtvPrecNonLOnOff[i]=0;  //3..0
    ibVtvTestMode[i]=0;      //3..0
    ibVTVNoconnet[i]=0;

    exciterASI12Tgr_[i].Q = 0xFF;
    vtvSigOnOffTgrQ_[i]=0xFF;
    vtvOverModTgrQ_[i]=0xFF;

    ibVtvSKSStatus[i]=0;
    ibVtvSChMStatus[i]=0;
    ibVtvSPChStatus[i]=0;
    vtvSigOnOffTgrD_[i]=0;
    ibVtvVideoLock[i]=0;
    ibVtvPowerStatus[i]=0;
    vtvOverModTgrD_[i]=0;
    ibVtvSound[i]=0;

    ibVtvChanNumAnalog[i]=0;
    iVtvChanNumAnalog[i]=0;
  }

  //PAB main
  for ( int i=0; i < (kMaxPABs); i++ ) {
    PABLockTgr_[i].Q = 0xff;
    PABOnOffTgr_[i].Q = 0x00;

    ibPABAnlgDigit[i]=0;    //1 -dig
    ibPABInPowerStatus[i]=0;  //1 - invalid
    ibPABStatus[i]=0;      //1 - fail
    ibPABOutPowerStatus[i]=0;  //1 - invalid < Pmin
    ibPABFRWStatus[i]=0;    //1 - fail

    ibPABInPow[i]=0;      //uchar
    iPABInPow[i]=0;        
    ibPABOutPow[i]=0;      //2 uchar
    iPABOutPow[i]=0;      
    ibPABFRW[i]=0;        //uchar
    iPABFRW[i]=0;        //FRW * 100

    //PAB  pre amplif
    ibPreVT12status[i]=0;    //1..0;  1 - VT 2, 0 - VT 1
    ibPreTemStaus[i]=0;        //1 -fail
    ibPreMIP[i]=0;        //1 -fail
    ibPreAGC[i]=0;        //1 -fail
    ibPrePlus15[i]=0;      //1 -fail
    ibPreCurVT1[i]=0;
    iPreCurVT1[i]=0;      //cut*10
    ibPreCurVT2[i]=0;
    iPreCurVT2[i]=0;      //cur*10
    ibPreTemValue[i]=0;
    iPreTemValue[i]=0;

    ibPreMIPVoltage[i]=0;
    iPreMIPVoltage[i]=0;    //vol *10
    ibPreAttenVoltage[i]=0;
    iPreAttenVoltage[i]=0;    //Voltage*10
    ibPrePhaseVoltage[i]=0;
    iPreAPhaseVoltage[i]=0;    //Voltage*100
    ibPreReferVoltage[i]=0;
    iPreReferVoltage[i]=0;    //Voltage*10

      

    for ( int k=0; k < ( kMaxTAsPerPAB ); k++ )
    {
      //PAB  power amplif
      ibPaVT12status[i][k]=0;    //1..0;  1 - VT 2, 0 - VT 1
      ibPaTemStaus[i][k]=0;      //1 -fail
      ibPaMIP[i][k]=0;        //1 -fail
      ibPaCurVT1[i][k]=0;
      iPaCurVT1[i][k]=0;        //cut*10
      ibPaCurVT2[i][k]=0;
      iPaCurVT2[i][k]=0;        //cur*10
      ibPaTemValue[i][k]=0;
      iPaTemValue[i][k]=0;

      ibPaMIPVoltage[i][k]=0;
      iPaMIPVoltage[i][k]=0;
    }
  }

  for ( int i=0; i < ( kMaxBCLs ); i++ ) {
    ibBCVTrOnOf[i]=0;
    ibBCVTrLock[i]=0;
    ibBCVstatus[i]=0;
    ibBCVAnlgDigit[i]=0;
    ibBCVPowerStat[i]=0;

    ibBCV_R1[i]=0;
    ibBCV_TR1[i]=0;
    ibBCV_R2[i]=0;
    ibBCV_TR2[i]=0;
    ibBCV_R3[i]=0;
    ibBCV_TR3[i]=0;
    ibBCV_R4[i]=0;
    ibBCV_TR4[i]=0;
    ibBCV_R5[i]=0;
    ibBCV_TR5[i]=0;
    ibBCV_R6[i]=0;
    ibBCV_TR6[i]=0;
    ibBCV_R7[i]=0;
    ibBCV_TR7[i]=0;
    ibBCV_R8[i]=0;
    ibBCV_TR8[i]=0;

    iBCV_R1Tem[i]=0;
    iBCV_R1Pow[i]=0;
    iBCV_R2Tem[i]=0;
    iBCV_R2Pow[i]=0;
    iBCV_R3Tem[i]=0;
    iBCV_R3Pow[i]=0;
    iBCV_R4Tem[i]=0;
    iBCV_R4Pow[i]=0;
    iBCV_R5Tem[i]=0;
    iBCV_R5Pow[i]=0;
    iBCV_R6Tem[i]=0;
    iBCV_R6Pow[i]=0;
    iBCV_R7Tem[i]=0;
    iBCV_R7Pow[i]=0;
    iBCV_R8Tem[i]=0;
    iBCV_R8Pow[i]=0;
  }

  for ( int i=0; i < ( kMaxDBs ); i++ ) {
    ibBDTrOnOf[i]=0;
    ibBDTrLock[i]=0;
    ibBDstatus[i]=0;
    ibBDAnlgDigit[i]=0;
    ibBDChannel[i]=0;
    ibBDFactor[i]=0;
    
    iBDSoundPower[i]=0;
    iBDOutPower[i]=0;
    iBDFRW[i]=0;
  }
  
    currentQueryIndex_ = 0;
}

//
//
void Sampler::ResetParamForSNMP() {
  //bAfterStart_ = true;
  //bAfterStart2_ = true;
  
  ctrlModeTgr_.Q = 0xFF; 
  typeLoadTgr_.Q = 0xFF;
  tmitterLockTgr___.Q = 0xFF; 
  tmitterOnTgr___.Q = 0xFF;
  powHalfModeTgr_.Q = 0xFF;
  istreamTgr_.Q = 0xFF;
  outSynTgr_.Q = 0xFF;
  statusTmitterTgr_.Q = 0xFF;
  

  currentQueryIndex_ = 0;
  /*for (int i = 0; i < kQuerySize; i++) {
    snmpEventsQuery_[i] = 0;
    stringMsgsQuery_[i] = "";
    typesCodesInQuery_[i] = 0;
    MWFCodesQuery_[i] = 0;
  }*/

  failsTotalD_ = 0;
  failsTotalQ_ = 7777777;
  failsTotalQQ_ = 7777777;

  lockPABTgr_.Q = 0;
  countOnOffPABsTgr_.Q = 0;
  //printPowerNeed_ = false;

  currentItemInQuery_ = 0;
  hasMsgForSnmp_ = false;
  fixedAlrmRecordIndex_ = 0;
  currentMWFCode_ = 0;
  currentAliveFaWa___ = 0;

  for (int i = 0; i <(kMaxExciters); i++) {
    exciterASI12Tgr_[i].Q = 0xFF;

    vtvSigOnOffTgrQ_[i] = 0xFF;
    vtvOverModTgrQ_[i] = 0xFF;
  }
  for (int i = 0; i <(kMaxPABs); i++) {
    PABOnOffTgr_[i].Q = 0x00;
    PABLockTgr_[i].Q = 0xff;
  }


  //powerOk_ = true;
  FRWOk_ = true;
  temperatureWasOk_ = true;
  snmp_connecton_status___ = 0;
  //initCounter_ = 0;
  //maxValueInitCounter_ = (MAX_INIT_UNLOCK_CNT);
}
//
//
} // namespace tmitter_web_service