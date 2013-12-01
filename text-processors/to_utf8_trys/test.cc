 // "Copyright [year] <Copyright Owner>"  [legal/copyright]

#include <app-server-code/data_processor/sampler_uni_header.h>

namespace tmitter_web_service {
using std::string;
//using std::v ector;
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

//
//
void Sampler::StateProcessing() {
  newFailOccure_ = false;
  if (statusTmitterTgr_.IsChange()) {
    if (statusTmitterTgr_.D == 1) {
      newFailOccure_ = true;
    } else if (statusTmitterTgr_.D == 2) {
      newFailOccure_ = true;
    } else if (statusTmitterTgr_.D == 0) {
      newFailOccure_ = false;
    } else {
      //LOG_I("Error : unknown state transmitter");
    }
  }

  // Запертость
  if (tmitterLockTgr___.IsChange()) {
    unlockCounter_ = 0;  // в любом случае 
  } else if (TmitterIsUnlock() && !TimoutIsOver()) {
      ++unlockCounter_;  // отсчитываем интервал 
  } else if (!TmitterIsUnlock()) {
    //unlockCounter_ = 0;  // если заперт, то
  } else {
    // unused
  }

  // @TODO: <igor.a.lugansky@gmail.com> need refactoring
  if (powHalfModeTgr_.IsChange()) {
    if (powHalfModeTgr_.GetQQ() == 1) {
      // full mode
      if (powHalfModeTgr_.D == 0) {
        currentBounds___.iPowLow *= 2;
        currentBounds___.iPowMax *= 2;
      }
    } else if (powHalfModeTgr_.GetQQ() == 0) {
      if (powHalfModeTgr_.D == 1) {
        currentBounds___.iPowLow /= 2;
        currentBounds___.iPowMax /= 2;
      }
    }
  }
  
  failsTotalD_ += (uint)ibPAB;
  failsTotalD_ += (uint)ibVtv;
  failsTotalD_ += failBallast_;
  failsTotalD_ += failFRW_;
  failsTotalD_ += failCoolling_;

  // State:
  bool onAndUnlock = TmitterIsOnAndUnlock();
  bool powerBad = PowerIsBad(realPowerRepresent___);
  bool FRWBad = FRWIsBad(FRWValue___);
  bool result;
  bool timout = (unlockCounter_ >= kMaxUnlockCounter_);

  // Мощность
  // LATCH
  // если была ненорма, потом мощн. из и опять не норма, 
  //   то не покажет последнюю
  bool powerOkTTgr_save = powerOkTTgr_;
  needPrintOkPower_ = onAndUnlock && !powerBad && powerOkTTgr_save;
  if (needPrintOkPower_) powerOkTTgr_ = false;
  needPrintFullPower_ = onAndUnlock && powerBad && !powerOkTTgr_save && timout;
  if (needPrintFullPower_) powerOkTTgr_ = true;
  // LATCH

  // КБВ
  // LATCH
  // если была ненорма, потом мощн. из и опять не норма, 
  //   то не покажет последнюю
  bool FRWOkTTgr_save = FRWOkTTgr_;
  needPrintOkFRW_ = onAndUnlock && !FRWBad && FRWOkTTgr_save;
  if (needPrintOkFRW_) FRWOkTTgr_ = false;

//#ifdef IN_TESTS
  //realPowerRepresent___ = 0x01;
//#endif  // IN_TESTS

  needPrintFullFRW_ = onAndUnlock && FRWBad && 
      timout && (realPowerRepresent___ > 0) && !FRWOkTTgr_save;
  if (needPrintFullFRW_)FRWOkTTgr_ = true;
  // LATCH

  // ВТВ
  for (int number = 0; number < excitersTotal_; number++) {
    failsTotalD_ += (uint)ibVtvModStatus[number];
    failsTotalD_ += (uint)ibVtvUsStatus[number];
  }

  // БУМы
  lockPABTgr_.D = 0;
  countOnOffPABsTgr_.D = 0;
  uchar numOnPABs = 0;
  uchar numLockPABs = 0;
  for (int pabIndex = 0; pabIndex < PABTotal_; pabIndex++)  {
    {
      numOnPABs += PABOnOffTgr_[pabIndex].D;

      // включенность?
      bTurnoff_[pabIndex] = false;
      if (PABOnOffTgr_[pabIndex].IsChange()) {
        if (TmitterIsUnlock()) {
          if (PABOnOffTgr_[pabIndex].D == 0) {
            bTurnoff_[pabIndex] = true;
          }
        }
      }

      bool onAndUnlock = TmitterIsOn() && TmitterIsUnlock();
      bool lock = IsPABLock(pabIndex);
      bool change = PABOnOffTgr_[pabIndex].IsChange();
      bool tmitterNoOk = PowerIsBad(realPowerRepresent___) && timout;
      
      // Отперт/Заперт
      // LATCH
      bool lockEvSave = printPABLockEvent_[pabIndex];
      printPABUnlock_[pabIndex] = onAndUnlock && lockEvSave && !lock; 
      if (printPABUnlock_[pabIndex]) printPABLockEvent_[pabIndex] = false;
      
      printPABLock_[pabIndex] = onAndUnlock && tmitterNoOk && 
          lock && !lockEvSave;
      if (printPABLock_[pabIndex]) printPABLockEvent_[pabIndex] = true;
      // LATCH

      // выходная мощность
      // LATCH
      bool inPowerSave = printPABNoInPowerEvent_[pabIndex];
      printPABInPowerOk_[pabIndex] = inPowerSave && 
          onAndUnlock && (ibPABInPowerStatus[pabIndex] == 0);
      if (printPABInPowerOk_[pabIndex]) 
        printPABNoInPowerEvent_[pabIndex] = false;

      // Идет в Отказы!
      printPABNoInPower_[pabIndex] = CheckStatusAndAlrms() && 
          (ibPABInPowerStatus[pabIndex] != 0) && tmitterNoOk;
      if (printPABNoInPower_[pabIndex]) 
        printPABNoInPowerEvent_[pabIndex] = true;
      // LATCH

      // запертость
      numLockPABs += PABLockTgr_[pabIndex].D;

      //
      failsTotalD_ += (uint)ibPABInPowerStatus[pabIndex];

      // состояния Отказов бум
      failsTotalD_ += (uint)ibPABOutPowerStatus[pabIndex];
      failsTotalD_ += (uint)ibPABFRWStatus[pabIndex];
    }


    // предварительные?
    if (sizeBlockPreampParams_ > 0) {
      //RptPreAmpParams(pabIndex, ptrInSrcArray);
      failsTotalD_ += (uint)ibPreTemStaus[pabIndex];
      failsTotalD_ += (uint)ibPreMIP[pabIndex];
      UpdateMaxTemre(iPreTemValue[pabIndex], 
          ibPreTemStaus[pabIndex]);
    }
    
    // оконечные?
    if (sizeBlockTerminalAmpParams_ > 0)  {
      for (int terminalAmpIndex = 0; 
          terminalAmpIndex < preampPerPAB___; 
          terminalAmpIndex++) {
        //RptTerminalAmpliferParams(pabIndex, terminalAmpIndex, ptrInSrcArray);
        failsTotalD_ += (uint)ibPaVT12status[pabIndex][terminalAmpIndex];
        failsTotalD_ += (uint)ibPaMIP[pabIndex][terminalAmpIndex];
        failsTotalD_ += (uint)ibPaTemStaus[pabIndex][terminalAmpIndex];
      }
    }
  }
  countOnOffPABsTgr_.Clk(numOnPABs);
  lockPABTgr_.Clk(numLockPABs);

  // Нагрузки
  for (int BCNIndex = 0; BCNIndex < BCNTotal_; BCNIndex++) {
    failsTotalD_ += (uint)ibBCVstatus[BCNIndex];
    failsTotalD_ += ((uint)ibBCV_R1[BCNIndex] + (uint)ibBCV_TR1[BCNIndex]);
    failsTotalD_ += ((uint)ibBCV_R2[BCNIndex] + (uint)ibBCV_TR2[BCNIndex]);
    failsTotalD_ += ((uint)ibBCV_R3[BCNIndex] + (uint)ibBCV_TR3[BCNIndex]);
    failsTotalD_ += ((uint)ibBCV_R4[BCNIndex] + (uint)ibBCV_TR4[BCNIndex]);
    failsTotalD_ += ((uint)ibBCV_R5[BCNIndex] + (uint)ibBCV_TR5[BCNIndex]);
    failsTotalD_ += ((uint)ibBCV_R6[BCNIndex] + (uint)ibBCV_TR6[BCNIndex]);
    failsTotalD_ += ((uint)ibBCV_R7[BCNIndex] + (uint)ibBCV_TR7[BCNIndex]);
    failsTotalD_ += ((uint)ibBCV_R8[BCNIndex] + (uint)ibBCV_TR8[BCNIndex]);
  }

  // ДБ
  for (int k = 0; k < DBTotal_; k++) failsTotalD_ += (uint)ibBDstatus[k];
}

bool Sampler::TmitterIsOnAndUnlock() const {
  bool result = (tmitterOnTgr___.D == 1) && (tmitterLockTgr___.D == 0);
  return result;
}

bool Sampler::PowerIsBad( int value ) const {
  int nominal_power = nominalPower___;
  float fv = (static_cast<float>(nominal_power)/static_cast<float>(100));
  int low = static_cast<int>(floor(fv * (100 - currentBounds___.iPowLow)));
  int hig = static_cast<int>(floor(fv * (100 + currentBounds___.iPowMax)));
  bool result = (value < low) || (value > hig);
  return result;
}

bool Sampler::FRWIsBad( int value ) const {
  uchar nominal_frw = (FRW_VALUE_H);
  float fv = (static_cast<float>(nominal_frw)/static_cast<float>(100));
  int low = static_cast<int>(floor(fv * (currentBounds___.iFRWLow)));
  int hig = static_cast<int>(floor(fv * (currentBounds___.iFRWMax)));
  bool result = (value < low) || (value > hig);
  return result;
}

bool Sampler::CheckStatusAndAlrms() const {
  bool result = (0 != statusTmitterTgr_.D) && (newFailOccure_);
  return result;
}
}  // tmitter_web_service
