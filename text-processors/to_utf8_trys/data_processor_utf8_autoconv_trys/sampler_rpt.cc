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

//using ::stdext::hash_map;

using simple_type_processors::int2str;
using simple_type_processors::hl;
using simple_type_processors::uint8;

static bool g_onOn = true;
//
//
void Sampler::RptMainStateTmitter() {
  // порядок важен!
  // отображение типа управления
  if (statusTmitterTgr_.D == 2) {
    typeControl_ = "(Авт.)";
  } else {
    if (ctrlModeTgr_.D == 0) {
      typeControl_ = "(МУ)";
    } else {
      typeControl_ = "(ДУ)";
    }
  }
  if (g_onOn) {
    typeControl_ = "";
    g_onOn = false;
  }

  // Включенность
  if (tmitterOnTgr___.IsChange()) {
    WriteCurrentMsg("Передатчик - ", SNMP_CM7);
    if (tmitterOnTgr___.D == 0) {
      AppendToCurrentRecord("отключен"+typeControl_, kSnmpOk);
    } else {
      AppendToCurrentRecord("включен"+typeControl_, kSnmpOk);
    }
  } else {
    currentMWFCode_ |= kSnmpOk;
  }

  if (ctrlModeTgr_.IsChange()){
    WriteCurrentMsg("Режим управления передатчиком - ", SNMP_CM2);
    string typeControl;
    if (ctrlModeTgr_.D == 0) {
      typeControl = "(МУ)";
      AppendToCurrentRecord("местный"+typeControl, kSnmpWarning);
    } else {
      typeControl = "(ДУ)";
      AppendToCurrentRecord("дистанционный"+typeControl, kSnmpOk);
    }
  } else {
    // Это что?
    if (ctrlModeTgr_.D == 0) {
      currentMWFCode_ |= kSnmpWarning;
    } else {
      currentMWFCode_ |= kSnmpOk;
    }
  }

  // Запертость
  if (tmitterLockTgr___.IsChange()) {
    WriteCurrentMsg("Передатчик - ", SNMP_CM6);
    if (tmitterLockTgr___.D == 0) {
      AppendToCurrentRecord("отперт"+typeControl_, kSnmpOk);
    } else {
      AppendToCurrentRecord("заперт"+typeControl_, kSnmpOk);
    }
  } else {
    currentMWFCode_ |= kSnmpOk;
  }

  // зависят от включенности
  // далее порядок похоже не важен

  // Тип нагрузки
  if (typeLoadTgr_.IsChange()) {
    WriteCurrentMsg("Работа на ", SNMP_CM12);
    if (typeLoadTgr_.D == 0) {
      AppendToCurrentRecord("антенну", kSnmpOk);
    } else {
      AppendToCurrentRecord("нагрузку", kSnmpOk);
    }
  } else {
    currentMWFCode_ |= kSnmpOk;
  }

  if (istreamTgr_.IsChange() && TmitterIsOn()) {
    if (istreamTgr_.D == 1) {
      WriteCurrentMsg("Входной сигнал - ", SNMP_CM3);
      AppendToCurrentRecord("отсутствует", kSnmpWarning);
    }
  } else {
    if (istreamTgr_.D == 0) {
      currentMWFCode_ |= kSnmpOk;
    } else {
      currentMWFCode_ |= kSnmpWarning;
    }
  }

  if (outSynTgr_.IsChange() && TmitterIsOn()) {
    if (outSynTgr_.D == 1) {
      WriteCurrentMsg("Синхронизация - ", SNMP_CM4);
      AppendToCurrentRecord("нет", kSnmpWarning);
    }
  } else {
    if (outSynTgr_.D == 0) {
      currentMWFCode_ |= kSnmpOk;
    } else {
      currentMWFCode_ |= kSnmpWarning;
    }
  }

  //
  // пошли Отказы
  //
  // !! Attention !! alrms processing
  statusRecordIndex_ = 0;
  if (statusTmitterTgr_.IsChange()) {
    if (statusTmitterTgr_.D == 1) {
      fixedAlrmRecordIndex_ = currentQueryIndex_;
      //WriteCurrentMsg("<b><font color='Gold'>Неисправность передатчика:</font></b>", SNMP_CM5);
      WriteCurrentMsg("Неисправность передатчика:", SNMP_CM5);
      AppendToCurrentRecord("", kSnmpWarning);
    } else if (statusTmitterTgr_.D == 2) {
      fixedAlrmRecordIndex_ = currentQueryIndex_;
      //WriteCurrentMsg("<b><font color='red'>Отказ передатчика:</font></b>",
      WriteCurrentMsg("Отказ передатчика:",
          SNMP_CM5);
      AppendToCurrentRecord("", kSnmpFail);
    } else if (statusTmitterTgr_.D == 0) {
      //WriteCurrentMsg("<b><font color='LimeGreen'>Передатчик норма</font></b>",
      WriteCurrentMsg("Передатчик норма",
          SNMP_CM5);
      AppendToCurrentRecord("", kSnmpOk);
    } else {
      // no used
      //LOG_I("Error : unknown state transmitter");
    }

    // Указатель на строку с сообщение состояния в новом массиве
    // Получить текущий размер очереди
    // size() - число элементов, а номер будет добавлен следующей коммандой
    statusRecordIndex_ = msgsSetFromOnIteration_.size()-1;
  } else {
    if (statusTmitterTgr_.D == 1) {
      currentMWFCode_ |= kSnmpWarning;
    } else if (statusTmitterTgr_.D == 2) {
      currentMWFCode_ |= kSnmpFail;
    } else if (statusTmitterTgr_.D == 0)  {
      currentMWFCode_ |= kSnmpOk;
    } else {
      //LOG_I("Error : unknown state transmitter");
    }
  }

  if (DtTgr_FailNet_.ClkAndCheckAlrm()) {
    RptAlrm("Отказ сеть");
  } 
  if (DtTgr_FailNet_.ClkAndCheckNoAlrm()) {
    AlrmHided("Сеть норма");
  }

  if (DtTgr_FailBallast_.ClkAndCheckAlrm()) {
    RptAlrm("Отказ балласт");
  }
  if (DtTgr_FailBallast_.ClkAndCheckNoAlrm()) {
    AlrmHided("Балласт норма");
  }

  if (DtTgr_FailFRW_.ClkAndCheckAlrm()) {
    RptAlrm("Отказ КБВ");
  }
  if (DtTgr_FailFRW_.ClkAndCheckNoAlrm()) {
    AlrmHided("КБВ норма");
  }

  if (DtTgr_FailCoolling_.ClkAndCheckAlrm()) {
    RptAlrm("Отказ охлаждения");
  }
  if (DtTgr_FailCoolling_.ClkAndCheckNoAlrm()) {
    AlrmHided("Охлаждение норма");
  }

  for (int j = 0; j < PABTotal_; j++) {
    int alrmOccure = ((ibPAB >> j) & 0x01);
    DtTgr_PAB_[j].SetValue(alrmOccure);
    if (DtTgr_PAB_[j].ClkAndCheckAlrm()) {
      RptAlrm( "Отказ БУМ" + int2str(j+1));
    } 
    if (DtTgr_PAB_[j].ClkAndCheckNoAlrm()) {
      AlrmHided( "БУМ" + int2str(j+1) + " норма");
    }
  }

  for (int j = 0; j < excitersTotal_; j++) {
    int alrmOccure = ((ibVtv >> j) & 0x01);
    DtTgr_Vtv_[j].SetValue(alrmOccure);
    if (DtTgr_Vtv_[j].ClkAndCheckAlrm()) {
      RptAlrm( "Отказ ВТВ" + int2str(j+1));
    } 
    if (DtTgr_Vtv_[j].ClkAndCheckNoAlrm()){
      AlrmHided( "ВТВ" + int2str(j+1) + " норма");
    }
  }


  //
  // Обрабока аварий была завершена
  //
 
  // Мощность
  if (needPrintFullPower_) {
    WriteCurrentMsg("Внимание, выходная мощность = ", SNMP_CM9);
    AppendToCurrentRecord(int2str(realPowerRepresent___)+" Вт", kSnmpWarning);
  } 

  if (needPrintOkPower_) {
    WriteCurrentMsg("Выходная мощность норма", SNMP_CM9);
    AppendToCurrentRecord("", kSnmpOk);
  }

  // КБВ
  if (needPrintFullFRW_) {
    WriteCurrentMsg("Внимание, значение КБВ = ", SNMP_CM10);
    AppendToCurrentRecord(int2str(FRWValue___), kSnmpWarning);
  }

  if (needPrintOkFRW_) {
    WriteCurrentMsg("Значение КБВ норма", SNMP_CM10);
    AppendToCurrentRecord("", kSnmpOk);
  }
}
//
//
void Sampler::RptMainParamsExciter(const int number) {
  if (this->DtTgr_VtvMod_[number].ClkAndCheckAlrm()) {
    RptAlrm("Отказ мод. ВТВ"+int2str(number+1));
  }
  if (this->DtTgr_VtvMod_[number].ClkAndCheckNoAlrm()) {
    AlrmHided("Мод. ВТВ"+int2str(number+1)+" норма");
  }


  if (this->DtTgr_VtvUs_[number].ClkAndCheckAlrm()) {
    RptAlrm("Отказ УС ВТВ"+int2str(number+1));
  }
  if (this->DtTgr_VtvUs_[number].ClkAndCheckNoAlrm()) {
    AlrmHided("УС ВТВ"+int2str(number+1)+" норма");
  }
}

//
//
void Sampler::RptModParams(const int k) {
  if (exciterASI12Tgr_[k].IsChange()) {
    WriteCurrentMsg("ВТВ"+int2str(k+1)+" - ", SNMP_CM8);
    AppendToCurrentRecord("", kSnmpOk);
    if ((exciterASI12Tgr_[k].D & 0x0f) == 1) {
      AppendToCurrentRecord("ASI1-основной, ");
    } else if ((exciterASI12Tgr_[k].D & 0xf) == 2) {
      AppendToCurrentRecord("ASI1-резервный, ");
    } else {
      AppendToCurrentRecord("ASI1-состояние неизвестно, ");
    }

    if ((exciterASI12Tgr_[k].D & 0xf0) == 0x10) {
      AppendToCurrentRecord("ASI2-основной");
    } else if ((exciterASI12Tgr_[k].D & 0xf0) == 0x20) {
      AppendToCurrentRecord("ASI2-резервный");
    } else {
      AppendToCurrentRecord("ASI2-состояние неизвестно");
    }
  }
}

//
//
void Sampler::RptMainParamsPAB(int number) {
  if (PABOnOffTgr_[number].IsChange() && bTurnoff_[number] && 
      TimoutIsOver()) {
    WriteCurrentMsg("БУМ"+int2str(number+1)+" - отключен", SNMP_CM15);
    AppendToCurrentRecord("", kSnmpWarning);
  }

  // заперт-отперт
  if (printPABLock_[number]) {
    WriteCurrentMsg("БУМ"+int2str(number+1), SNMP_CM16);
    AppendToCurrentRecord(" - заперт");
    AppendToCurrentRecord("", kSnmpWarning);
  }

  if (printPABUnlock_[number]) {
    WriteCurrentMsg("БУМ"+int2str(number+1), SNMP_CM16);
    AppendToCurrentRecord(" - отперт");
    AppendToCurrentRecord("", kSnmpWarning);
  }

  // выходная мощность
  if (printPABNoInPower_[number]) {
    WriteCurrentMsg("Нет Pвх. БУМ"+int2str(number+1));
  }

  if (printPABInPowerOk_[number]) {
    WriteCurrentMsg("БУМ"+int2str(number+1), SNMP_CM16);
    AppendToCurrentRecord(" Рвх - норма");
    AppendToCurrentRecord("", kSnmpOk);
  }

  // done
  if (this->DtTgr_PABOutPower_[number].ClkAndCheckAlrm()) {
    RptAlrm("Отказ Pвых. БУМ"+int2str(number+1));
  }
  if (this->DtTgr_PABOutPower_[number].ClkAndCheckNoAlrm()) {
    AlrmHided("Pвых. БУМ"+int2str(number+1)+" норма");
  }

  if (this->DtTgr_PABFRWStatus_[number].ClkAndCheckAlrm()) {
    RptAlrm("Отказ КБВ БУМ"+int2str(number+1));
  }
  if (this->DtTgr_PABFRWStatus_[number].ClkAndCheckNoAlrm()) {
    AlrmHided("КБВ БУМ"+int2str(number+1)+" норма");
  }
}

//
//
void Sampler::RptPreAmpParams(int k) {
  if (this->DtTgr_PreVT12_[k].ClkAndCheckAlrm()) {
    if ((ibPreVT12status[k] & 0x01) == 1) {
      RptAlrm("Отказ УП VT1 БУМ"+int2str(k+1));
    }
    if ((ibPreVT12status[k] & 0x02) == 2) {
      RptAlrm("Отказ УП VT2 БУМ"+int2str(k+1));
    }
  }
  // @TODO_N: Исчезание пока не делаю

  if (this->DtTgr_PreTem_[k].ClkAndCheckAlrm()) {
    RptAlrm("Отказ Т БУМ"+int2str(k+1));
  }
  if (this->DtTgr_PreTem_[k].ClkAndCheckNoAlrm()) {
    AlrmHided("Т БУМ"+int2str(k+1)+" норма");
  }

  if (this->DtTgr_PreMIP_[k].ClkAndCheckAlrm()) {
    RptAlrm("Отказ МИП УП БУМ"+int2str(k+1));
  }
  if (this->DtTgr_PreMIP_[k].ClkAndCheckNoAlrm()) {
    AlrmHided("МИП УП БУМ"+int2str(k+1)+" норма");
  }
  
  // АРУ?
  // +-15 V
}

//
//
void Sampler::RptTerminalAmpliferParams(int k, int n) {
  if (this->DtTgr_PaVT12_[k][n].ClkAndCheckAlrm()) {
    if ((ibPreVT12status[k] & 0x1) == 1) {
      RptAlrm("Отказ VT1 БУМ"+int2str(k+1)+" УМ"+int2str(n+1)+", ");
    }
    if ((ibPaVT12status[k][n] & 0x2) == 2) {
      RptAlrm("Отказ VT2 БУМ"+int2str(k+1)+" УМ"+int2str(n+1)+", ");
    }
  }
  // @TODO_N: Исчезание пока не делаю

  if (this->DtTgr_PaTem_[k][n].ClkAndCheckAlrm()) {
    RptAlrm("Отказ Т БУМ"+int2str(k+1)+" УМ"+int2str(n+1));
  }
  if (this->DtTgr_PaTem_[k][n].ClkAndCheckNoAlrm()) {
    AlrmHided("Т БУМ"+int2str(k+1)+" УМ"+int2str(n+1)+" норма");
  }

  if (this->DtTgr_PaMIP_[k][n].ClkAndCheckAlrm()) {
    RptAlrm("Отказ МИП БУМ"+int2str(k+1)+" УМ"+int2str(n+1));
  }
  if (this->DtTgr_PaMIP_[k][n].ClkAndCheckNoAlrm()) {
    AlrmHided("МИП БУМ"+int2str(k+1)+" УМ"+int2str(n+1)+" норма");
  }
}

//
//
void Sampler::RptParamsBCL(int number) {
  if (this->DtTgr_BCL_[number].ClkAndCheckAlrm()) {
    RptAlrm("Отказ БКН"+int2str(number+1));
  }
  if (this->DtTgr_BCL_[number].ClkAndCheckNoAlrm()) {
    AlrmHided("БКН"+int2str(number+1)+" норма");
  }

  if (this->DtTgr_BCL_R_1[number].ClkAndCheckAlrm()) RptAlrm("Отказ Rб1");
  if (this->DtTgr_BCL_R_2[number].ClkAndCheckAlrm()) RptAlrm("Отказ Rб2");
  if (this->DtTgr_BCL_R_3[number].ClkAndCheckAlrm()) RptAlrm("Отказ Rб3");
  if (this->DtTgr_BCL_R_4[number].ClkAndCheckAlrm()) RptAlrm("Отказ Rб4");
  if (this->DtTgr_BCL_R_5[number].ClkAndCheckAlrm()) RptAlrm("Отказ Rб5");
  if (this->DtTgr_BCL_R_6[number].ClkAndCheckAlrm()) RptAlrm("Отказ Rб6");
  if (this->DtTgr_BCL_R_7[number].ClkAndCheckAlrm()) RptAlrm("Отказ Rб7");
  if (this->DtTgr_BCL_R_8[number].ClkAndCheckAlrm()) RptAlrm("Отказ Rб8");

  if (this->DtTgr_BCL_R_1[number].ClkAndCheckNoAlrm()) AlrmHided("Rб1 норма");
  if (this->DtTgr_BCL_R_2[number].ClkAndCheckNoAlrm()) AlrmHided("Rб2 норма");
  if (this->DtTgr_BCL_R_3[number].ClkAndCheckNoAlrm()) AlrmHided("Rб3 норма");
  if (this->DtTgr_BCL_R_4[number].ClkAndCheckNoAlrm()) AlrmHided("Rб4 норма");
  if (this->DtTgr_BCL_R_5[number].ClkAndCheckNoAlrm()) AlrmHided("Rб5 норма");
  if (this->DtTgr_BCL_R_6[number].ClkAndCheckNoAlrm()) AlrmHided("Rб6 норма");
  if (this->DtTgr_BCL_R_7[number].ClkAndCheckNoAlrm()) AlrmHided("Rб7 норма");
  if (this->DtTgr_BCL_R_8[number].ClkAndCheckNoAlrm()) AlrmHided("Rб8 норма");

  //bool t = DtTgr_BCL_TR_[0][number].ClkAndCheckAlrm();
  if (this->DtTgr_BCL_TR_1[number].ClkAndCheckAlrm()) 
    RptAlrm("Отказ Tб1");
  if (this->DtTgr_BCL_TR_2[number].ClkAndCheckAlrm()) RptAlrm("Отказ Tб2");
  if (this->DtTgr_BCL_TR_3[number].ClkAndCheckAlrm()) RptAlrm("Отказ Tб3");
  if (this->DtTgr_BCL_TR_4[number].ClkAndCheckAlrm()) RptAlrm("Отказ Tб4");
  if (this->DtTgr_BCL_TR_5[number].ClkAndCheckAlrm()) RptAlrm("Отказ Tб5");
  if (this->DtTgr_BCL_TR_6[number].ClkAndCheckAlrm()) 
    RptAlrm("Отказ Tб6");  
  if (this->DtTgr_BCL_TR_7[number].ClkAndCheckAlrm()) RptAlrm("Отказ Tб7");
  if (this->DtTgr_BCL_TR_8[number].ClkAndCheckAlrm()) RptAlrm("Отказ Tб8");

  if (this->DtTgr_BCL_TR_1[number].ClkAndCheckNoAlrm())
    AlrmHided("Tб1 норма");
  if (this->DtTgr_BCL_TR_2[number].ClkAndCheckNoAlrm()) AlrmHided("Tб2 норма");
  if (this->DtTgr_BCL_TR_3[number].ClkAndCheckNoAlrm()) AlrmHided("Tб3 норма");
  if (this->DtTgr_BCL_TR_4[number].ClkAndCheckNoAlrm()) AlrmHided("Tб4 норма");
  if (this->DtTgr_BCL_TR_5[number].ClkAndCheckNoAlrm()) AlrmHided("Tб5 норма");
  if (this->DtTgr_BCL_TR_6[number].ClkAndCheckNoAlrm()) AlrmHided("Tб6 норма");
  if (this->DtTgr_BCL_TR_7[number].ClkAndCheckNoAlrm()) AlrmHided("Tб7 норма");
  if (this->DtTgr_BCL_TR_8[number].ClkAndCheckNoAlrm()) AlrmHided("Tб8 норма");
}
//
//
void Sampler::RptParamsDB(int number) {
  if (this->DtTgr_BD_[number].ClkAndCheckAlrm()) {
    RptAlrm("Отказ БД"+int2str(number+1));
  }
  if (this->DtTgr_BD_[number].ClkAndCheckNoAlrm()) {
    AlrmHided("БД"+int2str(number+1)+" норма");
  }
}
} // namespace tmitter_web_service
