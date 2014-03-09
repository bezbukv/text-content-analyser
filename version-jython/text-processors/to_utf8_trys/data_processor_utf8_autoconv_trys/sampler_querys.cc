// encoding : utf8
// "Copyright [year] <Copyright Owner>"  [legal/copyright]
#include <app-server-code/data_processor/sampler_uni_header.h>

namespace tmitter_web_service {
//} // namespace tmitter_web_service
using simple_type_processors::int2str;
using simple_type_processors::hl;
using std::string;
//using std::v ector;
using std::size_t;
using std::cout;
using std::endl;

/// Создание сообщения
// Create calls
void Sampler::WriteCurrentMsg(std::string msg, uchar key) {
  WriteCurrentMsg(msg);
  
  // !Danger! второй параметр использовался неясно зачем и был удален
}

void Sampler::WriteCurrentMsg(std::string msg) {
  // В файл
  msgsSetFromOnIteration_.push_back(msg);
  
  // на snmp менеджера
  stringMsgsQuerySTL_.push_back(msg);
}

/// Дополнение сообщения
// Append
void Sampler::AppendToCurrentRecord(std::string msg, uchar status) {
  // содержание сообщения
  if (msgsSetFromOnIteration_.empty()) {
    LOG_I("Ошибка работы с std::vector.");
  }
  msgsSetFromOnIteration_.back() += msg;
  
  // статус сообщения!
  if (stringMsgsQuerySTL_.empty()) {
    LOG_I("Ошибка работы с std::vector.");
  }
  stringMsgsQuerySTL_.back() += msg;
  
  LLTypeCodesQuerySTL_.push_back(currentMWFCode_ |= status);
  HLTypeCodesQuerySTL_.push_back(status);
}

void Sampler::AppendToCurrentRecord(std::string msg) {
  // добавка
  if (msgsSetFromOnIteration_.empty()) {
    LOG_I("Ошибка работы с std::vector.");
  }
  msgsSetFromOnIteration_.back() += msg;
  
  // на SNMP
  if (stringMsgsQuerySTL_.empty()) {
    LOG_I("Ошибка работы с std::vector.");
  }
  stringMsgsQuerySTL_.back() += msg;
}

/// Отказы
// Добавляет имена Отказов к сообщению!
void Sampler::RptAlrm(string msg) {
  WriteCurrentMsg(msg);
  
  // статус сообщения!
  AppendToCurrentRecord("", kSnmpFail);
}

// Добавляет имена Отказов к сообщению!
void Sampler::AlrmHided(string msg) {
  WriteCurrentMsg(msg);
  //WriteCurrentMsg("<font color='LimeGreen'>"+msg+"</font>");
  
  // статус сообщения!
  AppendToCurrentRecord("", kSnmpOk);
}
} // namespace tmitter_web_service