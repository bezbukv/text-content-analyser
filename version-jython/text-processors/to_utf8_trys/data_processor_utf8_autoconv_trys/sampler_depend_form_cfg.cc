// encoding : utf8
// "Copyright [year] <Copyright Owner>"  [legal/copyright]

#include <app-server-code/data_processor/sampler_uni_header.h>

namespace tmitter_web_service {
//} // namespace tmitter_web_service
using std::string;
void Sampler::ProcessExciterAnalog(uchar number, const uchar* ptrSourceArray) {
#ifndef DANGER_ANALOG_NO_USED
  uchar processedByte = ptrSourceArray[0];
  // analog
  uint uiSum = 0;
  ibVtvSKSStatus[number] = processedByte & 0x01;      // 1 - dist
  uiSum += (uint)processedByte;
  failsTotalD_ += (uint)ibVtvSKSStatus[number];

  if ((0 != transmitterStatusTgrD_) && 
      (ibVtvSKSStatus[number] != 0) && 
      (newFailOccure_)) {
    
    stringMsgsQuery_[fixedAlrmRecordIndex_] += "Отказ СКС ВТВ";
    char chb[10];
    _itoa((number+1), chb, 10);
    stringMsgsQuery_[fixedAlrmRecordIndex_] += chb;
    stringMsgsQuery_[fixedAlrmRecordIndex_] += ", ";
  }
  ibVtvSChMStatus[number] = (processedByte & 0x02) >> 1;
  failsTotalD_ += (uint)ibVtvSChMStatus[number];

  if ((0 != transmitterStatusTgrD_) && (ibVtvSChMStatus[number] != 0) && 
      (newFailOccure_)) {
    char chb[10];
    stringMsgsQuery_[fixedAlrmRecordIndex_] += "Отказ CЧМ ВТВ";
    _itoa((number+1), chb, 10);
    stringMsgsQuery_[fixedAlrmRecordIndex_] += chb;
    stringMsgsQuery_[fixedAlrmRecordIndex_] += ", ";
  }
  ibVtvSPChStatus[number] = (processedByte & 0x04) >> 2;
  failsTotalD_ += (uint)ibVtvSPChStatus[number];

  if ((0 != transmitterStatusTgrD_) && (ibVtvSPChStatus[number] != 0) && 
      (newFailOccure_)) {
    char chb[10];
    stringMsgsQuery_[fixedAlrmRecordIndex_] += "Отказ CПЧ ВТВ";
    _itoa((number+1), chb, 10);
    stringMsgsQuery_[fixedAlrmRecordIndex_] += chb;
    stringMsgsQuery_[fixedAlrmRecordIndex_] += ", ";
  }
  vtvSigOnOffTgrD_[number] = (processedByte & 0x08) >> 3;
  // SNMP_CM14
  if (vtvSigOnOffTgrQ_[number] != vtvSigOnOffTgrD_[number]) {
    vtvSigOnOffTgrQ_[number] = vtvSigOnOffTgrD_[number];

    if (vtvSigOnOffTgrQ_[number] == 0) {
    } else {
      hasMsgForSnmp_ = true;
      currentQueryIndex_++;
      if (kMaxSNMPQueue == currentQueryIndex_) {
        currentQueryIndex_ = 0;
      }
      snmpEventsQuery_[currentQueryIndex_] = (SNMP_CM14);
      string tmp = "ВТВ";
      char chb[10];
      _itoa((number+1), chb, 10);
      tmp += chb;
      tmp += " - замещениe";
      stringMsgsQuery_[currentQueryIndex_] = tmp;
      currentMWFCode_ |= kSnmpWarning;
      typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
      typeMsgsQuery_[currentQueryIndex_] = kSnmpWarning;
    }
  } else {
    if (vtvSigOnOffTgrD_[number] == 0) {
      currentMWFCode_ |= kSnmpOk;
    } else {
      currentMWFCode_ |= kSnmpWarning;
    }
  }

  ibVtvVideoLock[number] = (processedByte & 0x10) >> 4;
  ibVtvPowerStatus[number] = (processedByte & 0x20) >> 5;
  failsTotalD_ += (uint)ibVtvPowerStatus[number];

  if ((0 != statusTmitterTgr_.D) && 
    (ibVtvPowerStatus[number] != 0) &&
      (newFailOccure_)) {
    char chb[10];
    if ((ibVtvSPChStatus[number]+
        ibVtvSChMStatus[number]+
        ibVtvSKSStatus[number]) > 0) {
      stringMsgsQuery_[fixedAlrmRecordIndex_] += "Отказ Pвых. ВТВ";
    } else {
      stringMsgsQuery_[fixedAlrmRecordIndex_] += "Отказ УС ВТВ";
    }
    _itoa((number+1), chb, 10);
    stringMsgsQuery_[fixedAlrmRecordIndex_] += chb;
    stringMsgsQuery_[fixedAlrmRecordIndex_] += ", ";
  }

  vtvOverModTgrD_[number] = (processedByte & 0x40) >> 6;
  // SNMP_CM13
  if (vtvOverModTgrQ_[number] != vtvOverModTgrD_[number]) {
    vtvOverModTgrQ_[number] = vtvOverModTgrD_[number];
    if (vtvOverModTgrQ_[number] == 1) {
    } else {
      hasMsgForSnmp_ = true;
      currentQueryIndex_++;
      if (kMaxSNMPQueue == currentQueryIndex_)
      {
        currentQueryIndex_ = 0;
      }
      snmpEventsQuery_[currentQueryIndex_] = (SNMP_CM13);
      stringMsgsQuery_[currentQueryIndex_] = "ВТВ";
      char chb[10];
      _itoa((number+1), chb, 10);
      stringMsgsQuery_[currentQueryIndex_] += chb;
      stringMsgsQuery_[currentQueryIndex_] += " - ";
      stringMsgsQuery_[currentQueryIndex_] += "перемодуляция";

      currentMWFCode_ |= kSnmpWarning;
      typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
      typeMsgsQuery_[currentQueryIndex_] = kSnmpWarning;
    }
  } else {
    if (vtvOverModTgrD_[number] == 1) {
      currentMWFCode_ |= kSnmpOk;
    } else {
      currentMWFCode_ |= kSnmpWarning;
    }
  }

  ibVtvSound[number] = (processedByte & 0x80) >> 7;
  ibVtvChanNumAnalog[number] = ptrSourceArray[1];
  uiSum += (uint)ptrSourceArray[1];
  iVtvChanNumAnalog[number] = (ibVtvChanNumAnalog[number] & 0x0f);
  iVtvChanNumAnalog[number] += ((ibVtvChanNumAnalog[number] & 0xf0) >> 4)*10;

  if (uiSum == 0)
    ibVTVNoconnet[number] = 1;
  else
    ibVTVNoconnet[number] = 0;
#endif  // DANGER_ANALOG_NO_USED
}
} // namespace tmitter_web_service