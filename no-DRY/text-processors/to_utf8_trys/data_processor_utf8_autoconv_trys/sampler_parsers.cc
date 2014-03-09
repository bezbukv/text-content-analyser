// "Copyright [year] <Copyright Owner>"  [legal/copyright]

#include <app-server-code/data_processor/sampler_uni_header.h>

namespace tmitter_web_service {
using std::string;
using std::vector;
using std::size_t;
using std::copy;
using std::back_inserter;
using std::map;
using std::pair;
using std::cout;
using std::endl;

//using ::stdext::hash_map;

using simple_type_processors::int2str;
using simple_type_processors::hl;
using simple_type_processors::uint8;

void Sampler::ParseMainStateTmitter(const uint8* ptrSourceArray) {
  // 0
  uchar processedByte = ptrSourceArray[0];  

  // Triggers
  ctrlModeTgr_.Clk(processedByte & 0x01);
  typeLoadTgr_.Clk((processedByte & 0x02) >> 1);

  ibAnlgDigit_ = (processedByte & 0x04) >> 2;
  ibWork_ = (processedByte & 0x08) >> 3;
  exciterLock_ = (processedByte & 0x10) >> 4;
  ibPowAmpLock_ = (processedByte & 0x20) >> 5;

  tmitterLockTgr___.Clk((processedByte & 0x40) >> 6);
  tmitterOnTgr___.Clk((processedByte & 0x80) >> 7);
  //cout << (int)tmitterOnTgr___.Q << endl;

  // 1
  processedByte = ptrSourceArray[1];
  
  ibVtv12OnOff_ = processedByte & 0x03;  // 2 бита
  ibRadioMode_ = (processedByte & 0x04) >> 2;
  transmitterReady_ = (processedByte & 0x08) >> 3;
  
  // Triggers
  powHalfModeTgr_.Clk((processedByte & 0x20) >> 5);
  istreamTgr_.Clk((processedByte & 0x40) >> 6);
  outSynTgr_.Clk((processedByte & 0x80) >> 7);

#ifdef DONT_SHOW_NOSYNC
  outSynTgr_.Clk(0);
#endif

  // !! Attention !! alrms processing
  // 2
  processedByte = ptrSourceArray[2];
  statusTmitterTgr_.Clk(processedByte & 0x03);  // 2 бита
  failsTotalD_ = (uint)(statusTmitterTgr_.D);  // cpplint.py не обнаружил!
  // пошли прочие Отказы


  failNet_ = (processedByte & 0x10) >> 4;
  DtTgr_FailNet_.SetValue(failNet_);

  failBallast_ = (processedByte & 0x20) >> 5;
  DtTgr_FailBallast_.SetValue(failBallast_);

  failFRW_ = (processedByte & 0x40) >> 6;

  failCoolling_ = (processedByte & 0x80) >> 7;

  // 3
  ibPAB = ptrSourceArray[3];

  // 4
  processedByte = ptrSourceArray[4];
  ibPAB |= ((uint)(processedByte & 0x03)) << 8; // расширение до 10 бит

  ibRS485 = (processedByte & 0x04) >> 2;
  ibI2C = (processedByte & 0x08) >> 3;
  
  // = (processedByte & 0x10) >> 4;
  // = (processedByte & 0x20) >> 5;
  
  ibVtv = (processedByte & 0xC0) >> 6;  // 2 бита

  // Обрабока аварий была завершена

  // 5
  channalValuePacked_ = ptrSourceArray[5];
  iChannel = (channalValuePacked_ & 0x0f);
  iChannel += ((channalValuePacked_ & 0xf0) >> 4)*10;

  // читаем мощность
  // 6
  ibPower = ((uint)ptrSourceArray[6]);

  // 7
  ibPower |= (((uint)(ptrSourceArray[7])) << 8);

  // 8
  ibPower |= (((uint)(ptrSourceArray[8])) << 16);
  realPowerRepresent___ = (ibPower & 0x0f);
  realPowerRepresent___ += ((ibPower & 0xf0) >> 4)*10;
  realPowerRepresent___ += ((ibPower & 0xf00) >> 8)*100;
  realPowerRepresent___ += ((ibPower & 0xf000) >> 12)*1000;
  realPowerRepresent___ += ((ibPower & 0xf0000) >> 16)*10000;

  // 9
  uchar packedValueFRW = ptrSourceArray[9];
  FRWValue___ = (packedValueFRW & 0x0f);
  FRWValue___ += ((packedValueFRW & 0xf0) >> 4)*10;
}
//
//
void Sampler::ParseMainParamsExciter(const int number, 
                                    const uint8* ptrSourceArray) {
  // начинаем выборку
  const uchar kCodeErrIstream = 2;
  const uchar kCodeFailureMod = 3;
  const uchar kOutPowerMod = 4;

  uchar processedByte = ptrSourceArray[0];
  // разбок байта
  ibVtvCntrlMode[number] = processedByte & 0x01;        // 1 - dist
  ibVtvRadioMode[number] = (processedByte & 0x02) >> 1;    // 1 - reserv
  ibVtvAnlgDigit[number] = (processedByte & 0x04) >> 2;    // 1 - digit
  ibUsVtvLock[number] = (processedByte & 0x08) >> 3;    // 1 - lock
  ibModVtvLock[number] = (processedByte & 0x10) >> 4;    // 1 - lock
  ibOwnVtvLock[number] = (processedByte & 0x20) >> 5;    // 1 - lock
  ibVtvReady[number] = (processedByte & 0x40) >> 6;    // 1 - ready
  ibVtvWork[number] = (processedByte & 0x80) >> 7;    // 1 - norm

  // 1
  processedByte = ptrSourceArray[1];
  ibVtvStatus[number] = processedByte & 0x03;        // bits 1..0


  ibVtvModStatus[number] = (processedByte & 0x04) >> 2;    // 1 - fail
  ibVtvUsStatus[number] = (processedByte & 0x08) >> 3;      // 1 - fail

  this->DtTgr_VtvMod_[number].SetValue(ibVtvModStatus[number]);
  this->DtTgr_VtvUs_[number].SetValue(ibVtvUsStatus[number]);

  
  //[number] = (processedByte & 0x10) >> 4;      // 1 - fail
  //[number] = (processedByte & 0x20) >> 5;      // 1 - fail
  
  // состояние входного потока возбудителя
  exciterIstream_[number] = (processedByte & 0x40) >>  6;   // 1 - fail
  ibVtvOutSync[number] = (processedByte & 0x80) >> 7;      // 1 - fail
#ifdef DONT_SHOW_NOSYNC
  ibVtvOutSync[number] = 0;
#endif

  // вот тут признак отсутствия связи
  exciterIstreamError_[number] = ptrSourceArray[kCodeErrIstream];
  
  ibVtvErrMod[number] = ptrSourceArray[kCodeFailureMod];

  // Выходная мощность модулятора
  ibVtvOutPower[number] = ptrSourceArray[kOutPowerMod];
  iVtvOutPower[number] = (ibVtvOutPower[number] & 0x0f);
  iVtvOutPower[number] += ((ibVtvOutPower[number] & 0xf0) >> 4)*10;
}

//
//
void Sampler::ParseModParams(int number, const uchar* ptrSourceArray) {
  // 0
  uchar  processedByte = ptrSourceArray[0];
  ibVtvNet[number] = processedByte & 0x0f;    // bits 3..0
  ibVtvDVBTOnOf[number] = (processedByte & 0x30) >> 4;  // bits 5..4 -
  ibVtvMIPOnOf[number] = (processedByte & 0xC0) >> 6;  // bits 7..6 -

  // 1
  exciterASI12Tgr_[number].Clk(ptrSourceArray[1]);      // bits 3..0 - ASI1  7..4 - ASI2
  
  // 2
  processedByte = ptrSourceArray[2];
  ibVtvQAM[number] = (processedByte & 0xF0) >> 4;  // bits 7..4

  processedByte = ptrSourceArray[3];
  ibVtvCodRate[number] = (processedByte & 0xF0) >> 4;  // bits 7..4 -

  processedByte = ptrSourceArray[4];
  ibVtvGI[number] = (processedByte & 0xF0) >> 4;  // bits 7..4 -

  processedByte = ptrSourceArray[5];
  ibVtvHierar[number] = (processedByte & 0x07);    // bits 2..0 -

  processedByte = ptrSourceArray[6];
  ibVtvCarNum[number] = (processedByte & 0x07);    // bits 2..0 -

  ibVtvChannel[number] = ptrSourceArray[7];      // uchar
  iVtvChannel[number] = (ibVtvChannel[number] & 0x0f);
  iVtvChannel[number] += ((ibVtvChannel[number] & 0xf0) >> 4)*10;

  ibVtvFreq[number][0] = ptrSourceArray[8];
  ibVtvFreq[number][1] = ptrSourceArray[9];
  ibVtvFreq[number][2] = ptrSourceArray[10];
  ibVtvFreq[number][3] = ptrSourceArray[11];
  ibVtvFreq[number][4] = ptrSourceArray[12];

  iVtvFreq[number] = (ibVtvFreq[number][4] & 0x0f);
  iVtvFreq[number] += ((ibVtvFreq[number][4] & 0xf0) >> 4)*10;
  iVtvFreq[number] += ((ibVtvFreq[number][3] & 0x0f))*100;
  iVtvFreq[number] += ((ibVtvFreq[number][3] & 0xf0) >> 4)*1000;
  iVtvFreq[number] += ((ibVtvFreq[number][2] & 0x0f))*10000;
  iVtvFreq[number] += ((ibVtvFreq[number][2] & 0xf0) >> 4)*100000;
  iVtvFreq[number] += ((ibVtvFreq[number][1] & 0x0f))*1000000;
  iVtvFreq[number] += ((ibVtvFreq[number][1] & 0xf0) >> 4)*10000000;
  iVtvFreq[number] += ((ibVtvFreq[number][0] & 0x0f))*100000000;
  iVtvFreq[number] += ((ibVtvFreq[number][0] & 0xf0) >> 4)*1000000000;

  ibVtvOutLevel[number] = ((uint)ptrSourceArray[13]);    // bits 3..0
  ibVtvOutLevel[number] |= (((uint)ptrSourceArray[14]) << 8);
  iVtvOutLevel[number] = ((ibVtvOutLevel[number] & 0x0f0) >> 4);  // level*10
  iVtvOutLevel[number] += ((ibVtvOutLevel[number] & 0xf00) >> 8)*10;
  iVtvOutLevel[number] += (ibVtvOutLevel[number] & 0xf)*100;

  ibVtvTransmNum[number] = ((uint)ptrSourceArray[17]);
  ibVtvTransmNum[number] |= (((uint)ptrSourceArray[16]) << 8);
  ibVtvTransmNum[number] |= (((uint)ptrSourceArray[15]) << 16);
  iVtvTransmNum[number] = (ibVtvTransmNum[number] & 0xf);
  iVtvTransmNum[number] += ((ibVtvTransmNum[number] & 0xf0) >> 4)*10;
  iVtvTransmNum[number] += ((ibVtvTransmNum[number] & 0xf00) >> 8)*100;
  iVtvTransmNum[number] += ((ibVtvTransmNum[number] & 0xf000) >> 12)*1000;
  iVtvTransmNum[number] += ((ibVtvTransmNum[number] & 0xf0000) >> 16)*10000;

  ibVtvIDCellNum[number] = ((uint)ptrSourceArray[20]);
  ibVtvIDCellNum[number] |= (((uint)ptrSourceArray[19]) << 8);
  ibVtvIDCellNum[number] |= (((uint)ptrSourceArray[18]) << 16);

  iVtvIDCellNum[number] = (ibVtvIDCellNum[number] & 0xf);
  iVtvIDCellNum[number] += ((ibVtvIDCellNum[number] & 0xf0) >> 4)*10;
  iVtvIDCellNum[number] += ((ibVtvIDCellNum[number] & 0xf00) >> 8)*100;
  iVtvIDCellNum[number] += ((ibVtvIDCellNum[number] & 0xf000) >> 12)*1000;
  iVtvIDCellNum[number] += ((ibVtvIDCellNum[number] & 0xf0000) >> 16)*10000;

  ibVtvAdDelay[number] = ((uint)ptrSourceArray[24]);
  ibVtvAdDelay[number] |= (((uint)ptrSourceArray[23]) << 8);
  ibVtvAdDelay[number] |= (((uint)ptrSourceArray[22]) << 16);
  ibVtvAdDelay[number] |= (((uint)ptrSourceArray[21]) << 24);

  iVtvAdDelay[number] = (ibVtvAdDelay[number] & 0xf);
  iVtvAdDelay[number] += ((ibVtvAdDelay[number] & 0xf0) >> 4)*10;
  iVtvAdDelay[number] += ((ibVtvAdDelay[number] & 0xf00) >> 8)*100;
  iVtvAdDelay[number] += ((ibVtvAdDelay[number] & 0xf000) >> 12)*1000;
  iVtvAdDelay[number] += ((ibVtvAdDelay[number] & 0xf0000) >> 16)*10000;
  iVtvAdDelay[number] += ((ibVtvAdDelay[number] & 0xf00000) >> 20)*100000;
  iVtvAdDelay[number] += ((ibVtvAdDelay[number] & 0xf000000) >> 24)*1000000;

  processedByte = ptrSourceArray[21];
  // minus
  if (processedByte == 0x20) {
    iVtvAdDelay[number] *= -1;
  }

  ibVtvPrecLineNum[number] = ptrSourceArray[25];
  iVtvPrecLineNum[number] = (ibVtvPrecLineNum[number] & 0xf);
  iVtvPrecLineNum[number] += ((ibVtvPrecLineNum[number] & 0xf0) >> 4);

  processedByte = ptrSourceArray[26];
  ibVtvPrecLOnOff[number] = ((processedByte & 0x30) >> 4);    // 5..4
  ibVtvPrecNonLOnOff[number] = (processedByte & 0x3);  // 1..0
  ibVtvTestMode[number] = (ptrSourceArray[27] & 0x7);      // 2..0
}

#define CHECK_MAIN_PAR_1DARR_NL(name) \
  if (0 == (name)[number]) {\
    (name)[number] = 0x01;\
  } else {\
    (name)[number] = 0;\
  }

#define CHECK_MAIN_PAR_2DARR_NL(name) \
  if (0 == (name)[number][n]) {\
    (name)[number][n] = 0x01;\
  } else {\
    (name)[number][n] = 0;\
  }

#define CHECK_MAIN_PAR_1DARR_L(name)\
  (name)[number] = 0x01;

//
//
void Sampler::ParseMainParamsPAB(
    int number, 
    const simple_type_processors::uint8* ptrSourceArray) {
  // состояние
  const uchar kPABState = 0;
  uchar processedByte = ptrSourceArray[kPABState];
  PABOnOffTgr_[number].Clk(processedByte & 0x01);      // 1 -on
  PABLockTgr_[number].Clk((processedByte & 0x02) >> 1);    // 1 -lock
  
  ibPABAnlgDigit[number] = (processedByte & 0x08) >> 3;    // 1 -dig
  
  ibPABInPowerStatus[number] = (processedByte & 0x10) >> 4;  // 1 - invalid

  // состояния Отказов бум
  const uchar kPABStateFailure = 1;
  processedByte = ptrSourceArray[kPABStateFailure];
  
  // 
  ibPABStatus[number] = processedByte & 0x01;      // 1 - fail
//#ifndef IN_TESTS
  ibPABOutPowerStatus[number] = (processedByte & 0x02) >> 1;  // 1 - invalid < Pmin
//#else
  //CHECK_MAIN_PAR_1DARR_NL(ibPABOutPowerStatus);
//#endif
  this->DtTgr_PABOutPower_[number].SetValue(ibPABOutPowerStatus[number]);

  
//#ifndef IN_TESTS
  ibPABFRWStatus[number] = (processedByte & 0x04) >> 2;    // 1 - fail
//#else
  //CHECK_MAIN_PAR_1DARR_NL(ibPABFRWStatus);
//#endif
  this->DtTgr_PABFRWStatus_[number].SetValue(ibPABFRWStatus[number]);

  // состояние соединения
  PABNoConnect_[number] = mSH7(processedByte);

  // значение выходной мощности
  const uchar kPABOutPowerA = 2;
  ibPABInPow[number] = ptrSourceArray[kPABOutPowerA];      // uchar
  iPABInPow[number] = (ibPABInPow[number] & 0xf);
  iPABInPow[number] += (((ibPABInPow[number] & 0xf0) >> 4)*10);

  ibPABOutPow[number] = ptrSourceArray[3];
  ibPABOutPow[number] |= (((uint)ptrSourceArray[4]) << 8);
  iPABOutPow[number] = (ibPABOutPow[number] & 0xf);
  iPABOutPow[number] += (((ibPABOutPow[number] & 0xf0) >> 4)*10);
  iPABOutPow[number] += (((ibPABOutPow[number] & 0xf00) >> 8)*100);
  iPABOutPow[number] += (((ibPABOutPow[number] & 0xf000) >> 12)*1000);

  ibPABFRW[number] = ptrSourceArray[5];
  iPABFRW[number] = (ibPABFRW[number] & 0xf);  // FRW * 100
  iPABFRW[number] += (((ibPABFRW[number] & 0xf0) >> 4)*10);
}
//
//
void Sampler::ParsePreAmpParams(int number, const uchar* ptrSourceArray) {
  uchar  processedByte = ptrSourceArray[0];

// Исчезание на сделано
//#ifndef IN_TESTS
  ibPreVT12status[number] = processedByte & 0x03;        // 1..0;  1 - VT 2, 0 - VT 1
//#else
  //CHECK_MAIN_PAR_1DARR_NL(ibPreVT12status);
//#endif
  this->DtTgr_PreVT12_[number].SetValue(ibPreVT12status[number]);

//#ifndef IN_TESTS
  ibPreTemStaus[number] = (processedByte & 0x04) >> 2;   // 1 - fail
//#else
  //if (number == 4) {
    //CHECK_MAIN_PAR_1DARR_NL(ibPreTemStaus);
  //}
//#endif
  this->DtTgr_PreTem_[number].SetValue(ibPreTemStaus[number]);
  
//#ifndef IN_TESTS
  ibPreMIP[number] = (processedByte & 0x08) >> 3;        // 1 -fail
//#else
  //if (number == 4) {
    //CHECK_MAIN_PAR_1DARR_NL(ibPreMIP);
  //}
//#endif
  this->DtTgr_PreMIP_[number].SetValue(ibPreMIP[number]);

  ibPreAGC[number] = (processedByte & 0x10) >> 4;        // 1 -fail
  ibPrePlus15[number] = (processedByte & 0x20) >> 5;     // 1 -fail

  ibPreCurVT1[number] = ptrSourceArray[1];
  iPreCurVT1[number] = (ibPreCurVT1[number] & 0xf);  // cut*10
  iPreCurVT1[number] += (((ibPreCurVT1[number] & 0xf0) >> 4)*10);

  ibPreCurVT2[number] = ptrSourceArray[2];
  iPreCurVT2[number] = (ibPreCurVT2[number] & 0xf);  // cur*10
  iPreCurVT2[number] += (((ibPreCurVT2[number] & 0xf0) >> 4)*10);

  ibPreTemValue[number] = ptrSourceArray[3];
  iPreTemValue[number] = (ibPreTemValue[number] & 0xf);
  iPreTemValue[number] += (((ibPreTemValue[number] & 0xf0) >> 4)*10);

  ibPreMIPVoltage[number] = ptrSourceArray[4];
  ibPreMIPVoltage[number] |= (((uint)(ptrSourceArray[6] & 0xf)) << 8);
  iPreMIPVoltage[number] = (ibPreMIPVoltage[number] & 0xf);  // vol *10
  iPreMIPVoltage[number] += (((ibPreMIPVoltage[number] & 0xf0) >> 4)*10);
  iPreMIPVoltage[number] += (((ibPreMIPVoltage[number] & 0xf00) >> 8)*100);

  ibPreAttenVoltage[number] = ptrSourceArray[5];
  ibPreAttenVoltage[number] |= (((uint)(ptrSourceArray[6] & 0xf0)) << 4);
  iPreAttenVoltage[number] = (ibPreAttenVoltage[number] & 0xf);  // Voltage*10
  iPreAttenVoltage[number] +=  (((ibPreAttenVoltage[number] & 0xf0) >> 4)*10);
  iPreAttenVoltage[number] +=  (((ibPreAttenVoltage[number] & 0xf00) >> 8)*100);

  ibPrePhaseVoltage[number] = ptrSourceArray[7];
  ibPrePhaseVoltage[number] |= (((uint)ptrSourceArray[8]) << 8);
  iPreAPhaseVoltage[number] = (ibPrePhaseVoltage[number] & 0xf);    // Voltage*100
  iPreAPhaseVoltage[number] += (((ibPrePhaseVoltage[number] & 0xf0) >> 4)*10);
  iPreAPhaseVoltage[number] += (((ibPrePhaseVoltage[number] & 0xf00) >> 8)*100);
  iPreAPhaseVoltage[number] += (((ibPrePhaseVoltage[number] & 0xf000) >> 12)*1000);

  ibPreReferVoltage[number] = ptrSourceArray[9];
  iPreReferVoltage[number] = (ibPreReferVoltage[number] & 0xf);    // Voltage*10
  iPreReferVoltage[number] += (((ibPreReferVoltage[number] & 0xf0) >> 4)*10);
}
//
//
void Sampler::ParseTerminalAmpliferParams(
    int number, int n, 
    const uchar *ptrSourceArray) {
  uchar  processedByte = ptrSourceArray[0];

  // не перебрасывается пока
//#ifndef IN_TESTS
  ibPaVT12status[number][n] = processedByte & 0x03;        // 1..0;  1 - VT 2, 0 - VT 1
//#else
  //if (number == 4) {
    //CHECK_MAIN_PAR_2DARR_NL(ibPaVT12status);
  //}
//#endif
  this->DtTgr_PaVT12_[number][n].SetValue(ibPaVT12status[number][n]);

  // температура
//#ifndef IN_TESTS
  ibPaTemStaus[number][n] = (processedByte & 0x04) >> 2;    // 1 -fail
//#else
  //if (number == 4) {
    //CHECK_MAIN_PAR_2DARR_NL(ibPaTemStaus);
  //}
//#endif
  this->DtTgr_PaTem_[number][n].SetValue(ibPaTemStaus[number][n]);

  // МИП
//#ifndef IN_TESTS
    ibPaMIP[number][n] = (processedByte & 0x08) >> 3;    // 1 -fail
//#else
  //if (number == 4) {
    //CHECK_MAIN_PAR_2DARR_NL(ibPaMIP);
  //}
//#endif
  this->DtTgr_PaMIP_[number][n].SetValue(ibPaMIP[number][n]);

  ibPaCurVT1[number][n] = ptrSourceArray[1];
  ibPaCurVT1[number][n] |= (((uint)ptrSourceArray[3] & 0xf) << 8);
  iPaCurVT1[number][n] = (ibPaCurVT1[number][n] & 0xf);    // cut*10
  iPaCurVT1[number][n] += ((ibPaCurVT1[number][n] & 0xf0) >> 4)*10;
  iPaCurVT1[number][n] += ((ibPaCurVT1[number][n] & 0xf00) >> 8)*100;

  ibPaCurVT2[number][n] = ptrSourceArray[2];
  ibPaCurVT2[number][n] |= (((uint)ptrSourceArray[3] & 0xf0) << 4);
  iPaCurVT2[number][n] = (ibPaCurVT2[number][n] & 0xf);    // cur*10
  iPaCurVT2[number][n] += ((ibPaCurVT2[number][n] & 0xf0) >> 4)*10;
  iPaCurVT2[number][n] += ((ibPaCurVT2[number][n] & 0xf00) >> 8)*100;

  ibPaTemValue[number][n] = ptrSourceArray[4];
  iPaTemValue[number][n] = (ibPaTemValue[number][n] & 0xf);
  iPaTemValue[number][n] += ((ibPaTemValue[number][n] & 0xf0) >> 4)*10;

  UpdateMaxTemre(iPaTemValue[number][n], ibPaTemStaus[number][n]);

  ibPaMIPVoltage[number][n] = ptrSourceArray[5];
  ibPaMIPVoltage[number][n] |= (((uint)ptrSourceArray[6] & 0xf) << 8);
  iPaMIPVoltage[number][n] = (ibPaMIPVoltage[number][n] & 0xf);  // vol *10
  iPaMIPVoltage[number][n] += ((ibPaMIPVoltage[number][n] & 0xf0) >> 4)*10;
  iPaMIPVoltage[number][n] += ((ibPaMIPVoltage[number][n] & 0xf00) >> 8)*100;
}
//
//
void Sampler::ParseParamsBCL(
    int number, 
    const uchar *ptrSourceArray) {
  // Общие параметры
  const uchar UNION_PARAMS = 0;
  uchar  processedByte = ptrSourceArray[UNION_PARAMS];

  ibBCVTrOnOf[number] = (processedByte & 0x01);
  ibBCVTrLock[number] = (processedByte & 0x02) >> 1;

//#ifndef IN_TESTS
  ibBCVstatus[number] = (processedByte & 0x04) >> 2;
//#else
  //if (number == 4) {
  //CHECK_MAIN_PAR_1DARR_NL(ibBCVstatus);
  //}
//#endif
  this->DtTgr_BCL_[number].SetValue(ibBCVstatus[number]);

  ibBCVAnlgDigit[number] = (processedByte & 0x08) >> 3;
  ibBCVPowerStat[number] = (processedByte & 0x40) >> 6;

  // связь - пока сохраняем весь байт
  _bc_loads_uni[number] = processedByte;

  // про баласты - начало
  processedByte = ptrSourceArray[1];
//#ifndef IN_TESTS
  ibBCV_R1[number] = (processedByte & 0x1);
//#else
  //CHECK_MAIN_PAR_1DARR_L(ibBCV_R1);
//#endif
  this->DtTgr_BCL_R_1[number].SetValue(ibBCV_R1[number]);

  ibBCV_R2[number] = (processedByte & 0x2) >> 1;

//#ifndef IN_TESTS
  ibBCV_TR1[number] = (processedByte & 0x4) >> 2;
//#else
  //CHECK_MAIN_PAR_1DARR_NL(ibBCV_TR1);
//#endif

  ibBCV_TR2[number] = (processedByte & 0x8) >> 3;

  ibBCV_TR3[number] = (processedByte & 0x10) >> 4;
  ibBCV_TR4[number] = (processedByte & 0x20) >> 5;
  ibBCV_R3[number] = (processedByte & 0x40) >> 6;
  ibBCV_R4[number] = (processedByte & 0x80) >> 7;

  processedByte = ptrSourceArray[2];
  ibBCV_R5[number] = (processedByte & 0x1);
  ibBCV_R6[number] = (processedByte & 0x2) >> 1;
  ibBCV_TR5[number] = (processedByte & 0x4) >> 2;
  ibBCV_TR6[number] = (processedByte & 0x8) >> 3;

  ibBCV_TR7[number] = (processedByte & 0x10) >> 4;
  ibBCV_TR8[number] = (processedByte & 0x20) >> 5;

//#ifndef IN_TESTS
  ibBCV_R7[number] = (processedByte & 0x40) >> 6;
//#else
  //CHECK_MAIN_PAR_1DARR_L(ibBCV_R7);
//#endif
  this->DtTgr_BCL_R_7[number].SetValue(ibBCV_R7[number]);

//#ifndef IN_TESTS
  ibBCV_R8[number] = (processedByte & 0x80) >> 7;
//#else
  //CHECK_MAIN_PAR_1DARR_L(ibBCV_R8);
//#endif
  this->DtTgr_BCL_R_8[number].SetValue(ibBCV_R8[number]);
  

  uint icur;
  processedByte = ptrSourceArray[3];
  iBCV_R1Tem[number] = (processedByte & 0xf);
  iBCV_R1Tem[number] +=  (((processedByte & 0xf0) >> 4)*10);
  icur = ptrSourceArray[4];
  iBCV_R1Pow[number] = (icur & 0xf);
  iBCV_R1Pow[number] +=  (((icur & 0xf0) >> 4)*10);
  icur = ptrSourceArray[5];
  iBCV_R1Pow[number] +=  ((icur & 0xf)*100);
  iBCV_R1Pow[number] +=  (((icur & 0xf0) >> 4)*1000);

  processedByte = ptrSourceArray[6];
  iBCV_R2Tem[number] = (processedByte & 0xf);
  iBCV_R2Tem[number] +=  (((processedByte & 0xf0) >> 4)*10);
  icur = ptrSourceArray[7];
  iBCV_R2Pow[number] = (icur & 0xf);
  iBCV_R2Pow[number] +=  (((icur & 0xf0) >> 4)*10);
  icur = ptrSourceArray[8];
  iBCV_R2Pow[number] +=  ((icur & 0xf)*100);
  iBCV_R2Pow[number] +=  (((icur & 0xf0) >> 4)*1000);

  processedByte = ptrSourceArray[9];
  iBCV_R3Tem[number] = (processedByte & 0xf);
  iBCV_R3Tem[number] +=  (((processedByte & 0xf0) >> 4)*10);
  icur = ptrSourceArray[10];
  iBCV_R3Pow[number] = (icur & 0xf);
  iBCV_R3Pow[number] +=  (((icur & 0xf0) >> 4)*10);
  icur = ptrSourceArray[11];
  iBCV_R3Pow[number] +=  ((icur & 0xf)*100);
  iBCV_R3Pow[number] +=  (((icur & 0xf0) >> 4)*1000);

  processedByte = ptrSourceArray[12];
  iBCV_R4Tem[number] = (processedByte & 0xf);
  iBCV_R4Tem[number] +=  (((processedByte & 0xf0) >> 4)*10);
  icur = ptrSourceArray[13];
  iBCV_R4Pow[number] = (icur & 0xf);
  iBCV_R4Pow[number] +=  (((icur & 0xf0) >> 4)*10);
  icur = ptrSourceArray[14];
  iBCV_R4Pow[number] +=  ((icur & 0xf)*100);
  iBCV_R4Pow[number] +=  (((icur & 0xf0) >> 4)*1000);

  processedByte = ptrSourceArray[15];
  
  iBCV_R5Tem[number] = (processedByte & 0xf);
  iBCV_R5Tem[number] +=  (((processedByte & 0xf0) >> 4)*10);
  
  icur = ptrSourceArray[16];
  iBCV_R5Pow[number] = (icur & 0xf);
  iBCV_R5Pow[number] +=  (((icur & 0xf0) >> 4)*10);
  icur = ptrSourceArray[17];
  iBCV_R5Pow[number] +=  ((icur & 0xf)*100);
  iBCV_R5Pow[number] +=  (((icur & 0xf0) >> 4)*1000);

  processedByte = ptrSourceArray[18];
  iBCV_R6Tem[number] = (processedByte & 0xf);
  iBCV_R6Tem[number] +=  (((processedByte & 0xf0) >> 4)*10);
  icur = ptrSourceArray[19];
  iBCV_R6Pow[number] = (icur & 0xf);
  iBCV_R6Pow[number] +=  (((icur & 0xf0) >> 4)*10);
  icur = ptrSourceArray[20];
  iBCV_R6Pow[number] +=  ((icur & 0xf)*100);
  iBCV_R6Pow[number] +=  (((icur & 0xf0) >> 4)*1000);

  processedByte = ptrSourceArray[21];
  iBCV_R7Tem[number] = (processedByte & 0xf);
  iBCV_R7Tem[number] +=  (((processedByte & 0xf0) >> 4)*10);
  icur = ptrSourceArray[22];
  iBCV_R7Pow[number] = (icur & 0xf);
  iBCV_R7Pow[number] +=  (((icur & 0xf0) >> 4)*10);
  icur = ptrSourceArray[23];
  iBCV_R7Pow[number] +=  ((icur & 0xf)*100);
  iBCV_R7Pow[number] +=  (((icur & 0xf0) >> 4)*1000);

  processedByte = ptrSourceArray[24];
  iBCV_R8Tem[number] = (processedByte & 0xf);
  iBCV_R8Tem[number] +=  (((processedByte & 0xf0) >> 4)*10);
  icur = ptrSourceArray[25];
  iBCV_R8Pow[number] = (icur & 0xf);
  iBCV_R8Pow[number] +=  (((icur & 0xf0) >> 4)*10);
  icur = ptrSourceArray[26];
  iBCV_R8Pow[number] +=  ((icur & 0xf)*100);
  iBCV_R8Pow[number] +=  (((icur & 0xf0) >> 4)*1000);

  // Auto generate
  this->DtTgr_BCL_R_2[number].SetValue(ibBCV_R2[number]);
  this->DtTgr_BCL_R_3[number].SetValue(ibBCV_R3[number]);
  this->DtTgr_BCL_R_4[number].SetValue(ibBCV_R4[number]);
  this->DtTgr_BCL_R_5[number].SetValue(ibBCV_R5[number]);
  this->DtTgr_BCL_R_6[number].SetValue(ibBCV_R6[number]);
  
  //this->DtTgr_BC L_R_[6][number].SetValue(ibBCV_R7[number]);
  //this->DtTgr_BC L_R_[7][number].SetValue(ibBCV_R8[number]);

  this->DtTgr_BCL_TR_2[number].SetValue(ibBCV_TR2[number]);
  this->DtTgr_BCL_TR_3[number].SetValue(ibBCV_TR3[number]);
  this->DtTgr_BCL_TR_4[number].SetValue(ibBCV_TR4[number]);
  this->DtTgr_BCL_TR_5[number].SetValue(ibBCV_TR5[number]);
  this->DtTgr_BCL_TR_6[number].SetValue(ibBCV_TR6[number]);
  this->DtTgr_BCL_TR_7[number].SetValue(ibBCV_TR7[number]);
  this->DtTgr_BCL_TR_8[number].SetValue(ibBCV_TR8[number]);

  this->DtTgr_BCL_TR_1[number].SetValue(ibBCV_TR1[number]);
    //bool t = DtTgr_BCL_TR_[0][number].ClkAndCheckAlrm();
}
//
//

//
//
void Sampler::ParseParamsDB(int number, 
                                     const uchar* ptrSourceArray) {
  uchar* repeater_ptr = NULL;

  //
  const uchar UNI_PARAMS_DB = 0;
  uchar  processedByte = ptrSourceArray[UNI_PARAMS_DB];
  ibBDTrOnOf[number] = (processedByte & 0x01);
  ibBDTrLock[number] = (processedByte & 0x02) >> 1;

  ibBDstatus[number] = (processedByte & 0x04) >> 2;
  this->DtTgr_BD_[number].SetValue(ibBDstatus[number]);

  ibBDAnlgDigit[number] = (processedByte & 0x08) >> 3;
  
  ibBDChannel[number] = (processedByte & 0x40) >> 6;
  ibBDFactor[number] = (processedByte & 0x80) >> 7;

  // состояние связи - пока сохраняем весь содержащийся байт
  _uni_about_bl_complex_loads[number] = processedByte;

  uint icur;
  icur = ptrSourceArray[1];
  iBDSoundPower[number] = (icur & 0xf);
  iBDSoundPower[number] += (((icur & 0xf0) >> 4)*10);

  icur = ptrSourceArray[4];
  iBDSoundPower[number] += ((icur & 0x3)*100);

  icur = ptrSourceArray[2];
  iBDOutPower[number] = (icur & 0xf);
  iBDOutPower[number] += (((icur & 0xf0) >> 4)*10);

  icur = ptrSourceArray[3];
  iBDOutPower[number] += ((icur & 0xf)*100);
  iBDOutPower[number] += (((icur & 0x30) >> 4)*1000);

  icur = ptrSourceArray[5];
  iBDFRW[number] = (icur & 0xf);
  iBDFRW[number] += (((icur & 0xf0) >> 4)*10);
}
//
//
}  // tmitter_web_service
