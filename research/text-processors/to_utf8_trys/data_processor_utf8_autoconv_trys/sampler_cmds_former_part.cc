// encoding : utf8
// "Copyright [year] <Copyright Owner>"  [legal/copyright]

#include <app-server-code/data_processor/sampler_uni_header.h>

namespace tmitter_web_service {
using std::string;
//
//
// Cmds formers
// configuration of Transmitter
void Sampler::RequestAllParams(uchar* cBuf, int iBufSize, int &wrote) {
  wrote = 7;
  if (iBufSize < wrote) {
     wrote =-1;
     return;
  }
  cBuf[0]=0xAA;
  cBuf[1]=0x55; 
  cBuf[2]= kLengthRequestCmd ;
  cBuf[3]=(uchar)(transmitterAddress___&0xff);  // адрес передатчика
  cBuf[4]=(kCmdRequest);
  cBuf[5]=(kRequestAllParameters);
  cBuf[6]=0;
  for (int i = 0; i < kLengthRequestCmd-1; i++)
    cBuf[6]+=cBuf[i+2];

  uchar *cBufi = new uchar[wrote*2];
  int new_wrote = 0;
  for(int i=0; i<wrote; i++) {
     cBufi[new_wrote]=cBuf[i];
     if ((cBufi[i]==0xAA)&&(i > 1)) {
       new_wrote++;
       cBufi[new_wrote] = 0x00;
     }
     new_wrote++;
  }
  memcpy(cBuf,cBufi,new_wrote);
  delete [] cBufi;
};

void Sampler::RequestTransmitterCfg(uchar* cBuf,int  iBufSize,int &wrote) {
  wrote = 7;
  if (iBufSize < wrote) {
     wrote =-1;
     return;
  }
  cBuf[0] = 0xAA;
  cBuf[1] = 0x55;
  
  cBuf[2] = kLengthRequestCmd ;
  cBuf[3] = (uchar)(transmitterAddress___&0xff);
  cBuf[4] = kCfgRequest;  // код типа запроса
  cBuf[5] = kRequestCfgTransmitter;
  cBuf[6] = 0;
  for (int i = 0;i < kLengthRequestCmd-1; i++) {
    cBuf[6]+=cBuf[i+2];
  }

  uchar *cBufi = new uchar[wrote*2];
  int new_wrote = 0;
  for(int i=0; i<wrote; i++)
  {
    cBufi[new_wrote]=cBuf[i];
    if ((cBufi[i]==0xAA)&&(i > 1)) {
      new_wrote++;
      cBufi[new_wrote] = 0x00;
    }
    new_wrote++;
  }
  memcpy(cBuf,cBufi,new_wrote);
  delete [] cBufi;
};

// inl
void Sampler::RequestMainParams(uchar* cBuf,int  iBufSize,int &wrote) {
  wrote = 7;
  if (iBufSize < wrote) {
     wrote =-1;
     return;
  }
  cBuf[0]=0xAA;
  cBuf[1]=0x55; 
  cBuf[2]= kLengthRequestCmd ;
  cBuf[3]=(uchar)(transmitterAddress___&0xff);
  cBuf[4]=(kCmdRequest);  
  cBuf[5]=(kRequestMainParameters);
  cBuf[6]=0;
  for (int i=0;i<( kLengthRequestCmd -1); i++) 
    cBuf[6]+=cBuf[i+2];

  uchar *cBufi = new uchar[wrote*2];
  int new_wrote = 0;
  for(int i=0; i<wrote; i++) {
    cBufi[new_wrote]=cBuf[i];
    if ((cBufi[i]==0xAA)&&(i > 1)) {
      new_wrote++;
      cBufi[new_wrote] = 0x00;
    }
    new_wrote++;
  }
  memcpy(cBuf,cBufi,new_wrote);
  delete [] cBufi;
}

void Sampler::FillBufferWithCmd(uchar* cBuf,int  iBufSize,int &wrote, uint cmd) {
  int iNumEnd;
 
  cBuf[0]=0xAA;
  cBuf[1]=0x55;  
  cBuf[3]=(uchar)(transmitterAddress___&0xff);
  cBuf[4]=(TR_CMD_CTL);
  cBuf[5]=(cmd&0xff);

  if (cmd < (PROTO_TR_CTL_TR_SET_ADDR)){
    cBuf[2]= kLengthRequestCmd ;
  }
  if (cmd==(PROTO_TR_CTL_TR_SET_ADDR)){
    cBuf[2] = 6;
    cBuf[6] = transmitter_number_to_set_;
  }
  iNumEnd = cBuf[2]+1;
  wrote = iNumEnd+1;

  if (iBufSize < wrote) {
    wrote =-1;
    return;
  }

  cBuf[iNumEnd]=0;
  for (int i=2;i<iNumEnd; i++) cBuf[iNumEnd]+=cBuf[i];

  uchar *cBufi = new uchar[wrote*2];
  int    new_wrote = 0;
  for(int i=0; i<wrote; i++) {
    cBufi[new_wrote]=cBuf[i];
    if ((cBufi[i]==0xAA)&&(i > 1)) {
      new_wrote++;
      cBufi[new_wrote] = 0x00;
    }
    new_wrote++;
  }
  memcpy(cBuf,cBufi,new_wrote);
  delete [] cBufi;
}

bool Sampler::RecodeAndRunCmdFromWeb(ParametersMap &p, uint type_protocol) {
  int cmd = atoi(p["cmd"].c_str());
  if (type_protocol==0) {
    // что за комманда пришал по web-интерфейсу
    switch (cmd) {
      //
      case 1:  current_ctrl_cmd___ = PROTO_TR_CTL_TR_ON;  break;
      case 2:  current_ctrl_cmd___ = PROTO_TR_CTL_TR_OFF;  break;
      //
      case 3:  current_ctrl_cmd___ = PROTO_TR_CTL_TR_LOCK;  break;
      case 4:  current_ctrl_cmd___ = PROTO_TR_CTL_TR_UNLOCK;  break;
      //
      case 5:  current_ctrl_cmd___ = PROTO_TR_CTL_TR_RST_ERR;  break;
      case 7:
        current_ctrl_cmd___ = PROTO_TR_CTL_TR_SET_ADDR; 
        transmitter_number_to_set_ = atoi(p["trnum"].c_str());
        break;
       
      // переключение мощности
      // Danger !! еще перекодируются !
      case 8: current_ctrl_cmd___ = PROTO_TR_CTL_TR_SET_HALF;  break;
      case 9: current_ctrl_cmd___ = PROTO_TR_CTL_TR_SET_FULL;  break;
     
      // смена ВТВ
      case kSetExciterACodeCmdFromWeb:
        current_ctrl_cmd___ = kSetExciterACmd;
        break;
      case kSetExciterBCodeCmdFromWeb:
        current_ctrl_cmd___ = kSetExciterBCmd;
        break;
     
      //
      default: return false;
    }
  }
  else if (type_protocol==1) {
    switch (cmd) {
      case 1:  current_ctrl_cmd___ = PROTO_TR_CTL_TR_ON_v1;    break;
      case 2:  current_ctrl_cmd___ = PROTO_TR_CTL_TR_OFF_v1;    break;
      case 3:  current_ctrl_cmd___ = TR_CTL_VLOCK_v1;    break;
      case 4:  current_ctrl_cmd___ = TR_CTL_VUNLOCK_v1;  break;
      case 5:  current_ctrl_cmd___ = PROTO_TR_CTL_TR_RST_ERR_v1;  break;
      default: return false;
    } 
  }

  // спускаем крючок отправки комманды
#ifndef _CROSS_GCC
  SetEvent(externalEventsArray_[kSendCmd_]);
#endif  // _CROSS_GCC
  return true;
}

#ifndef _CROSS_GCC
// Danger!
void Sampler::PutExternalEventHandler(HANDLE *hNewCmdEvent,int iNumActiveEvent) {
  externalEventsArray_[iNumActiveEvent]=hNewCmdEvent[0];
}
#endif  // _CROSS_GCC

/*
void Sampler::rqStatus(uchar* cBuf) //0x01
{
  cBuf[0]=0xAA; cBuf[1]=0x55;  cBuf[2]= kLengthRequestCmd ; cBuf[3]=(uchar)(transmitterAddress___&0xff);
  cBuf[4]=(kCmdRequest); cBuf[5]=(TR_REQ_STATUS);
  cBuf[6]=0;
  for (int i=0;i<( kLengthRequestCmd -1); i++) cBuf[6]+=cBuf[i+2];
}

void Sampler::rqChannel(uchar* cBuf) //0x0A
{

  cBuf[0]=0xAA; cBuf[1]=0x55;  cBuf[2]= kLengthRequestCmd ; cBuf[3]=(uchar)(transmitterAddress___&0xff);
  cBuf[4]=(kCmdRequest); cBuf[5]=(TR_REQ_CHANNEL);
  cBuf[6]=0;
  for (int i=0;i<( kLengthRequestCmd -1); i++) cBuf[6]+=cBuf[i+2];
}

void Sampler::rqPower(uchar* cBuf) //0x0B
{

  cBuf[0]=0xAA; cBuf[1]=0x55;  cBuf[2]= kLengthRequestCmd ; cBuf[3]=(uchar)(transmitterAddress___&0xff);
  cBuf[4]=(kCmdRequest); cBuf[5]=(TR_REQ_POWER);
  cBuf[6]=0;
  for (int i=0;i<( kLengthRequestCmd -1); i++) cBuf[6]+=cBuf[i+2];
}

void Sampler::rqFRunWave(uchar* cBuf) //0x0C
{

  cBuf[0]=0xAA; cBuf[1]=0x55;  cBuf[2]= kLengthRequestCmd ; cBuf[3]=(uchar)(transmitterAddress___&0xff);
  cBuf[4]=(kCmdRequest); cBuf[5]=(TR_REQ_FRW);
  cBuf[6]=0;
  for (int i=0;i<( kLengthRequestCmd -1); i++) cBuf[6]+=cBuf[i+2];
}
*/

} // namespace tmitter_web_service