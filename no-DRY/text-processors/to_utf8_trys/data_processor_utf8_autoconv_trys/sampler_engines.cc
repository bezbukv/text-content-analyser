// encoding : utf8
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
void Sampler::ParsePkgAndChangeState(
    /*const*/ uchar* localCopyBuffer, 
    uchar type_protocol,
    int bufferLength) {
  msgsSetFromOnIteration_.clear();
  if(type_protocol == 0) {
    // копируем данные буффера, убрать не удастся, т.к. данные константные
    // и указатель создать не получится
    /*v ector<uint8> dataContainer;
    dataContainer.reserve(1024);
    for (int i = 0; i < bufferLength; i++) {
      dataContainer.push_back(localCopyBuffer[i]);
    }*/
    
    uint8* ptrInSrcArray = localCopyBuffer;//&dataContainer.front();
    const int kFirstInfoByte = 5;  // нумерация с нуля
    InserArrayPart(ptrInSrcArray, kFirstInfoByte, "HEAD");
    ptrInSrcArray = &ptrInSrcArray[kFirstInfoByte];
    
    // слепок
    mapData_.clear();
    const int kMainParamsBlockSize = 10;
    InserArrayPart(ptrInSrcArray, kMainParamsBlockSize, "main params tmitter");
    ParseMainStateTmitter(ptrInSrcArray);
    ptrInSrcArray = &ptrInSrcArray[kMainParamsBlockSize];
    
    // Жестко +5 +28
    // IB VTV from localCopyBuffer[14] - а не 15?
    for (int number = 0; number < excitersTotal_; number++) {
      if ((transmitterID___ >= 5) && (transmitterID___ < 23)) {
        const int kExcMainParamBlockSize = 5;
        InserArrayPart(ptrInSrcArray, 
            kExcMainParamBlockSize, "exciter main param");

        ParseMainParamsExciter(number, ptrInSrcArray);
        ptrInSrcArray = &(ptrInSrcArray[kExcMainParamBlockSize]);
      } else {
        ProcessExciterAnalog( number, ptrInSrcArray );
        ptrInSrcArray = &(ptrInSrcArray[2]);
      }
      
      // по модуляторам
      if (sizeBlockModParams_ > 0) {
        const int kExcModBlockSize = 28;
        InserArrayPart(ptrInSrcArray, kExcModBlockSize, "mod params");

        ParseModParams(number, ptrInSrcArray);
        ptrInSrcArray = &(ptrInSrcArray[kExcModBlockSize]);
      }
    }

    // PAB
    for (int pabIndex = 0; pabIndex < PABTotal_; pabIndex++)  {
      const int kPABMainPartBlockSize = 6;
      InserArrayPart(ptrInSrcArray, kPABMainPartBlockSize, "PAB main par");

      ParseMainParamsPAB(pabIndex, ptrInSrcArray);
      ptrInSrcArray = &(ptrInSrcArray[kPABMainPartBlockSize]);

      // предварительные?
      if (sizeBlockPreampParams_ > 0) {
        const int kPreampPartSize = 10;
        InserArrayPart(ptrInSrcArray, kPreampPartSize, "preamp params");

        ParsePreAmpParams(pabIndex, ptrInSrcArray);
        ptrInSrcArray = &(ptrInSrcArray[kPreampPartSize]);
      }
      
      // оконечные?
      if (sizeBlockTerminalAmpParams_ > 0)  {
        for (int terminalAmpIndex = 0; 
            terminalAmpIndex < preampPerPAB___; 
            terminalAmpIndex++) {
          const int kTAPartSize = 7;
          InserArrayPart(ptrInSrcArray, kTAPartSize, "TA info");

          ParseTerminalAmpliferParams(pabIndex, 
              terminalAmpIndex, 
              ptrInSrcArray);
          ptrInSrcArray = &(ptrInSrcArray[kTAPartSize]);
        }
      }
    }

    // БКН
    for (int k = 0; k < BCNTotal_; k++)  {
      if (sizeBlockBCNParams_ > 0) {
        const int kBCNPartSize = 27;
        InserArrayPart(ptrInSrcArray, kBCNPartSize, "bcn info");

        ParseParamsBCL(k, ptrInSrcArray);
        ptrInSrcArray = &(ptrInSrcArray[kBCNPartSize]);
      }
    }

    // ДБ
    for (int k = 0; k < DBTotal_; k++) {
      if (sizeBlockDBParams_ > 0) {
        const int kDBPartSize = 6;
        InserArrayPart(ptrInSrcArray, kDBPartSize, "DB info");

        ParseParamsDB(k, ptrInSrcArray);
        ptrInSrcArray = &(ptrInSrcArray[kDBPartSize]);
      }
    }
    
    // Доработка состояния
    StateProcessing();
// Другой протокол !!!
  } else if (type_protocol == 1){

#ifndef NO_RTS
    // создаем указатель на информационную часть?
    uchar *ptrInSrcArray = NULL;
    ptrInSrcArray = &localCopyBuffer[3];
    ptrInSrcArray = ParseMainParamsTmitter_v1(ptrInSrcArray);

    // инф. блоки PAB
    lockPABTgrQ_ = 0;
    countOnOffPABsTgrQ_ = 0;
    for (int k = 0; k < PABTotal_; k++)  {
      ptrInSrcArray = ParseMainParamsPAB_v1(k, ptrInSrcArray);
    }
#endif  // NO_RTS
  }
}

//
//
ListLines Sampler::Rpt(
    const uchar* localCopyBuffer, 
    uchar type_protocol,
    int bufferLength) {
  if(type_protocol == 0) {
    
    // состояние передатчика
    RptMainStateTmitter();
    
    // возбудитель
    for (int number = 0; number < excitersTotal_; number++) {
      {
        if ((transmitterID___ >= 5) && (transmitterID___ < 23)) {
          RptMainParamsExciter(number);
        } else {
#ifndef DANGER_ANALOG_NO_USED
          ProcessExciterAnalog( number, ptrInSrcArray );
#endif  // DANGER_ANALOG_NO_USED
        }
      }
      
      // по модуляторам
      if (sizeBlockModParams_ > 0) {
        RptModParams(number);
      }
    }

    // PAB
    for (int pabIndex = 0; pabIndex < PABTotal_; pabIndex++)  {
      {
        RptMainParamsPAB(pabIndex);
      }

      // предварительные?
      if (sizeBlockPreampParams_ > 0) {
        RptPreAmpParams(pabIndex);
      }
      
      // оконечные?
      if (sizeBlockTerminalAmpParams_ > 0)  {
        for (int terminalAmpIndex = 0; 
            terminalAmpIndex < preampPerPAB___; 
            terminalAmpIndex++) {
          RptTerminalAmpliferParams(pabIndex, terminalAmpIndex);
        }
      }
    }

    // БКН
    for (int k = 0; k < BCNTotal_; k++)  {
      if (sizeBlockBCNParams_ > 0) {
        RptParamsBCL(k);
      }
    }

    // ДБ
    for (int k = 0; k < DBTotal_; k++) {
      if (sizeBlockDBParams_ > 0) {
        RptParamsDB(k);
      }
    }
  } else {
    // другие протоколы
  }

  // общий выход
  statusRecordIndex_ = 0;
  return msgsSetFromOnIteration_;
}

}  // tmitter_web_service