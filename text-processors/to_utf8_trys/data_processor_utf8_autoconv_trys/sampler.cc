// encoding : utf8
// "Copyright [year] <Copyright Owner>"  [legal/copyright]
#include <app-server-code/data_processor/sampler_uni_header.h>

// где должна проходить граница простр. имен?
namespace tmitter_web_service {
  //} // namespace tmitter_web_service

using std::string;
using std::vector;
using std::size_t;

using simple_type_processors::int2str;
using simple_type_processors::hl;
using simple_type_processors::uint8;

//int НачальнаяОбрабокаПолучДанных()

// AA 55 позоже убраны
int Sampler::ProcessAnswer(
    uchar* input_buffer,
    int buffer_length,
    int &currentTransmitterIndex_,
    uint type_protocol) {
  uchar *localCopyBuffer = new uchar[buffer_length];
  if (type_protocol == 0) {
    // Danger!!! bad loop!!
    for (int i = 0, k = 0; i < buffer_length; i++, k++) {
      localCopyBuffer[k] = input_buffer[i];
      if (input_buffer[i] == 0xAA) {
        i++;
      }
    }
  } else if (type_protocol == 1) {
    for (int i = 0, k = 0; i < buffer_length; i++, k++)
      localCopyBuffer[k] = input_buffer[i];
  }

  int ret = 0;
  uint iLen = (((uint)localCopyBuffer[0]) << 8) | ((uint)localCopyBuffer[1]);

  if ((iLen > buffer_length) || (iLen < 2)) {
    delete [] localCopyBuffer;
    ret = -3;
    return ret;  // incorrect tag len or len buf
  }

  int typeTransmitter;  // не соответсвует названию!! Danger!!
  int codeTransmitter;
  if (type_protocol == 0) {
    currentTransmitterIndex_ = static_cast<int>(localCopyBuffer[2]);
    typeTransmitter = static_cast<int>(localCopyBuffer[3]);
    codeTransmitter = static_cast<int>(localCopyBuffer[4]);
  } else if (type_protocol == 1) {
    currentTransmitterIndex_ = 1;
    typeTransmitter = 0xC1;
    codeTransmitter = (kRequestAllParameters);
    nominalPower___ = 2000;
    excitersTotal_ = 2;
    PABTotal_ = 10;
    preampPerPAB___ = 2;
    BCNTotal_ = 0;
    sizeBlockModParams_ = 0;
    sizeBlockTerminalAmpParams_ = 0;
    sizeBlockPreampParams_ = 0;
    sizeBlockBCNParams_ = 0;
    sizeEventsString_ = 0;
    sizeFailsString_ = 0;
    DBTotal_ = 0;
    sizeBlockDBParams_ = 0;
    transmitterID___ = 77;
    exciterType_ = 0;
  } else {
    // no use
  }

  // check control sum
  uchar control_sum = 0;
  if (type_protocol == 0) {
    for (int i = 0; i < (iLen-1); i++)
      control_sum += localCopyBuffer[i];
  } else if (type_protocol == 1) {
    control_sum = 0xA1;
    for (int i = 0; i < (iLen-1); i++) {
      if (((uint)control_sum+(uint)localCopyBuffer[i]) >=  0x100) {
        control_sum += localCopyBuffer[i];
        control_sum += 1;
      } else {
        control_sum += localCopyBuffer[i];
      }
    }
  }
  if (control_sum != localCopyBuffer[iLen-1]) {
    delete [] localCopyBuffer;
    ret = -1;
    return ret;  // cs error
  }

  // @TODO: <igor.a.lugansky@gmail.com> ^ выделить бы в один метод-инициализатор
  
  // распределение по типу запроса?
  switch (typeTransmitter) {
    // 41 Transmitter
    case 0xC1:
      switch (codeTransmitter) {
        case kRequestMainParameters: {
          uchar *ptrSourceArray = &localCopyBuffer[5];
          ParseMainStateTmitter(ptrSourceArray);
        } break;

        // запрашивались все параметры?
        case kRequestAllParameters: {
          // Mayby Error!!
          uchar *ptrSourceArray = &localCopyBuffer[0];
          // Danger!! LOG_ARRAY(first, length, buffer_name, marker)
          // marker=5 для type_proto = 0
          //LOG_ARRAY(0, buffer_length, localCopyBuffer, 5)
          //int current_buffer_ptr_ = 0;
          ProcessAllParamsResponse(ptrSourceArray, type_protocol, buffer_length);
        } break;

        // еще запрос
        case kRequestProtocolEvent: {
          uchar *ptrSourceArray = &localCopyBuffer[5];
          ptrSourceArray = ParseTransmitterEventsProto(ptrSourceArray);
        } break;
        
        //
        default: break;
      }
      break;

    // 44 -
    case 0xC4:
      switch (codeTransmitter) {
        case kRequestCfgTransmitter:{
          uchar *ptrSourceArray = &localCopyBuffer[0];
          ParseCfgSystem(ptrSourceArray);
        } break;
        default: break;
      }
      break;
    // code type is undefined
    default: ret = -2;
  }
  delete [] localCopyBuffer;
  return ret;
}

void Sampler::ParseCfgSystem(uchar* localCopyBuffer) {
  // первые 5 наверное заголовок
  // Danger!! LOG_ARRAY(first, length, buffer_name, marker)
  string name = "cfg system";
  //LOG_ARRAY(0, 22, localCopyBuffer, 5, name)
  
  // мощность номинальная
  int current_nominal_power = localCopyBuffer[5];
  current_nominal_power |= (((uint)(localCopyBuffer[6])) << 8);
  nominalPower___ = (current_nominal_power & 0x0f);
  nominalPower___ += ((current_nominal_power & 0xf0) >> 4)*10;
  nominalPower___ += ((current_nominal_power & 0xf00) >> 8)*100;
  nominalPower___ += ((current_nominal_power & 0xf000) >> 12)*1000;

  // число возбудителей
  numExcitersPack_ = localCopyBuffer[7];
  excitersTotal_ = (numExcitersPack_ & 0x0f);
  excitersTotal_ += ((numExcitersPack_ & 0xf0) >> 4)*10;

  // число усилительных блоков
  ibNumPAB = localCopyBuffer[8];
  PABTotal_ = (ibNumPAB & 0x0f);
  PABTotal_ += ((ibNumPAB & 0xf0) >> 4)*10;

  // число УП в Бумах
  ibNumPAinPAB = localCopyBuffer[9];
  preampPerPAB___ = (ibNumPAinPAB & 0x0f);
  preampPerPAB___ += ((ibNumPAinPAB & 0xf0) >> 4)*10;

  // число комплексных нагрузок
  ibNumBCV = localCopyBuffer[10];
  BCNTotal_ = (ibNumBCV & 0x0f);
  BCNTotal_ += ((ibNumBCV & 0xf0) >> 4)*10;

  //
  // вот что это? размены информационных блоков
  ibSizeIBMod = localCopyBuffer[11];
  sizeBlockModParams_ = (ibSizeIBMod & 0x0f);
  sizeBlockModParams_ += ((ibSizeIBMod & 0xf0) >> 4)*10;

  // размер блока параметров предварителього усилителя
  ibSizeIBPAPAB = localCopyBuffer[12];
  sizeBlockTerminalAmpParams_ = (ibSizeIBPAPAB & 0x0f);
  sizeBlockTerminalAmpParams_ += ((ibSizeIBPAPAB & 0xf0) >> 4)*10;

  ibSizeIBPrAPAB = localCopyBuffer[13];
  sizeBlockPreampParams_ = (ibSizeIBPrAPAB & 0x0f);
  sizeBlockPreampParams_ += ((ibSizeIBPrAPAB & 0xf0) >> 4)*10;

  ibSizeIBBCV = localCopyBuffer[14];
  sizeBlockBCNParams_ = (ibSizeIBBCV & 0x0f);
  sizeBlockBCNParams_ += ((ibSizeIBBCV & 0xf0) >> 4)*10;
  
  ibEventStringSize = localCopyBuffer[15];
  sizeEventsString_ = (ibEventStringSize & 0x0f);
  sizeEventsString_ += ((ibEventStringSize & 0xf0) >> 4)*10;

  ibFailStringSize = localCopyBuffer[16];
  sizeFailsString_ = (ibFailStringSize & 0x0f);
  sizeFailsString_ += ((ibFailStringSize & 0xf0) >> 4)*10;
  //
  //
  
  // детектора
  ibNUMDB = localCopyBuffer[17];
  DBTotal_ = (ibNUMDB & 0x0f);
  DBTotal_ += ((ibNUMDB & 0xf0) >> 4)*10;

  ibSizeDB = localCopyBuffer[18];
  sizeBlockDBParams_ = (ibSizeDB & 0x0f);
  sizeBlockDBParams_ += ((ibSizeDB & 0xf0) >> 4)*10;

  // остальное
  transmitterID___ = localCopyBuffer[19];
  
  exciterType_ = localCopyBuffer[20];
  
  countReservedTransmitters_ = localCopyBuffer[21];
}

uchar* Sampler::ParseTransmitterEventsProto(uchar  *ptrSourceArray) {
  uchar* repeater_ptr = NULL;
  return repeater_ptr;
}

void Sampler::ApplyMaxTemperature() {
  iMaxTemre = new_max_temperature_;
  iMaxStatus = iNewMaxStatus;

  int low = (currentBounds___.iTemLow);
  int hig = (currentBounds___.iTemMax);

  if (tmitterOnTgr___.D == 1) {
    // SNMP_CM11
    if ((iMaxTemre<low) || (iMaxTemre > hig)) {
      if ((temperatureWasOk_) && TimoutIsOver()) {
        temperatureWasOk_ = false;
        //TickQueryPosition();
        WriteCurrentMsg("Внимание, максимальная температура "+int2str(iMaxTemre)+" C",
            SNMP_CM11);
        AppendToCurrentRecord("", kSnmpWarning);
      }
    } else {
      if (!temperatureWasOk_) {
        temperatureWasOk_ = true;
        //TickQueryPosition();
        WriteCurrentMsg("Максимальная температура норма", SNMP_CM11);
        AppendToCurrentRecord("", kSnmpOk);
        currentMWFCode_ |= kSnmpOk;
      }
    }
  }
}

// Похоже тут происходит перекачка буфера из rs в меструю базу данных
//
//
void Sampler::ProcessAllParamsResponse(uchar* localCopyBuffer, 
                                     uchar type_protocol,
                                     int bufferLength) {
  // настроим состояние
  Preprocess();

  // установка состояния
  ParsePkgAndChangeState(localCopyBuffer, type_protocol, bufferLength);

  // обработка
  ListLines msgSet = Rpt(
    localCopyBuffer, 
    type_protocol,
    bufferLength);

  // To log
  journal.PutSetRecords(msgSet);
  
  // Отправляем накопленное в журнал, жмем на спусковой крючок.
  const int kSNMPEventSend_ = 1;

#ifndef _CROSS_GCC
  SetEvent(externalEventsArray_[kSNMPEventSend_]);
#endif  // _CROSS_GCC

  // доработка - завершающий этап
  Postprocess();
}

void Sampler::Preprocess() { 
  new_max_temperature_ = -200;
  hasMsgForSnmp_ = false;
  currentMWFCode_ = 0;
  counterFailsAndWarns_ = currentQueryIndex_;
  
  //newFailOccure_ = false;
  statusRecordIndex_ = 0;
  
  // Очистка очередей для SNMP, нужно бы сделать копию
  stringMsgsQuerySTL_.clear();
  HLTypeCodesQuerySTL_.clear();
  LLTypeCodesQuerySTL_.clear();
}

void Sampler::Postprocess() {
  // прочая обработка
  ApplyMaxTemperature();
  
  // if (hasMsgForSnmp_) {
    // спусковой крючок - обновит границы? чего?
    //SetEvent(externalEventsArray_[kBoundUpdate_]);
  //}
  
  // Если в(ы)ключен, то нужно сбросить FSM
  if (tmitterOnTgr___.IsChange()) {
    if (tmitterOnTgr___.D == 0) {
      RstSamplerFSM();
    }
  }
  

  // пока просто очищаем
  msgsSetFromOnIteration_.clear();
}

string Sampler::GetJournalContent(int& err) {
  err = 0;
  vector<string> content = journal.GetContent();
  string result;
  result.reserve(4096);
  if (!content.empty()) {
    foreach_r_(string at, content) {
      result += at+kNewLineLog; 
    }
  }
  return result;
}

} // namespace tmitter_web_service

// Обобщенная обработка ответа?
// Ведет запись в буффер журнала, но только во вложенных вызовах
//
#define ERRORED_PICE
#ifndef ERRORED_PICE
int Sampler::ProcessAnswer(
    uchar* input_buffer,  // есть еще какой-то заголовок
    int buffer_length,
    int &currentTransmitterIndex_,
    uint type_protocol) {
  uchar *localCopyBuffer = new uchar[buffer_length];
  int typeTransmitter;  // не соотв. название, кажется!
  int codeTransmitter;
  int ret = 0;
  uint iLen = (((uint)localCopyBuffer[0]) << 8) | ((uint)localCopyBuffer[1]);
  uchar control_sum = 0;
  
  // разделение по протоколам
  if (type_protocol == 0) {
     // Danger!!! bad loop!!
     for (int i = 0, k = 0; i < buffer_length; i++, k++) {
       localCopyBuffer[k] = input_buffer[i];
       if (input_buffer[i] == 0xAA) {
         i++;
       }
     }

    if ((iLen > buffer_length) || (iLen < 2)) {
      delete [] localCopyBuffer;
      ret = -3;
      return ret;  // incorrect tag len or len buf
    }

    currentTransmitterIndex_ = static_cast<int>(localCopyBuffer[2]);
    typeTransmitter = static_cast<int>(localCopyBuffer[3]);
    codeTransmitter = static_cast<int>(localCopyBuffer[4]);

    // check control sum
    for (int i = 0; i < (iLen-1); i++)  control_sum += localCopyBuffer[i];
    
    if (control_sum != localCopyBuffer[iLen-1]) {
      delete [] localCopyBuffer;
      ret = -1;
      return ret;  // cs error
    }
    
// другой протокол
  } else if (type_protocol == 1) {
    for (int i = 0, k = 0; i < buffer_length; i++, k++)  localCopyBuffer[k] = input_buffer[i];

    if ((iLen > buffer_length) || (iLen < 2)) {
      delete [] localCopyBuffer;
      ret = -3;
      return ret;  // incorrect tag len or len buf
    }

    currentTransmitterIndex_ = 1;
    typeTransmitter = 0xC1;
    codeTransmitter = (kRequestAllParameters);
    nominalPower___ = 2000;
    excitersTotal_ = 2;
    PABTotal_ = 10;
    preampPerPAB___ = 2;
    BCNTotal_ = 0;
    sizeBlockModParams_ = 0;
    sizeBlockTerminalAmpParams_ = 0;
    sizeBlockPreampParams_ = 0;
    sizeBlockBCNParams_ = 0;
    sizeEventsString_ = 0;
    sizeFailsString_ = 0;
    DBTotal_ = 0;
    sizeBlockDBParams_ = 0;
    transmitterID___ = 77;
    exciterType_ = 0;

    // check control sum
    control_sum = 0xA1;
    for (int i = 0; i < (iLen-1); i++) {
      if (((uint)control_sum+(uint)localCopyBuffer[i]) >=  0x100) {
        control_sum += localCopyBuffer[i];
        control_sum += 1;
      } else {
        control_sum += localCopyBuffer[i];
      }
    }
    
    if (control_sum != localCopyBuffer[iLen-1]) {
      delete [] localCopyBuffer;
      ret = -1;
      return ret;  // cs error
    }
  } else {
    LOG_I("Protocol no supported");
    return -4;
  }
#endif  // ERRORED_PICE