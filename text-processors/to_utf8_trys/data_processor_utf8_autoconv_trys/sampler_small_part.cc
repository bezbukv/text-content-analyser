// encoding : utf8
// "Copyright [year] <Copyright Owner>"  [legal/copyright]

#include <app-server-code/data_processor/sampler_uni_header.h>

namespace tmitter_web_service {
//} // namespace tmitter_web_service
using std::string;

void Sampler::SetUsedTime( bool bUsed ) {
  if ( bUsed )  iUsedTime =1;
  else    iUsedTime =0;
}

void Sampler::SetUsedReserv( bool bUsed ) {
  if ( bUsed )  iUsedReserv =1;
  else    iUsedReserv =0;
  lengthCfgAnswer = ( TR_LEN_CFG_ANS_M ) + iUsedReserv;
}

void Sampler::SetWarningBounds( sBounds  &sNewBounds ) {
  currentBounds___=sNewBounds;
}

void Sampler::SetDisplayChan( int &iNewChan ) {
  iChannelSetup = iNewChan;
}

void Sampler::SetVtvIP( char* cVTV1,uint uiP1,char* cVTV2,uint  uiP2 ) {
  strcpy( cVtvIP[0],cVTV1 );
  strcpy( cVtvIP[1],cVTV2 );
  uiPort[0]=uiP1;
  uiPort[1]=uiP2;
}

void Sampler::SetExtIP( char* cExtNew ) {
  strcpy( cExt,cExtNew );
}

// update maximum temperature
void Sampler::UpdateMaxTemre( int  iAddTemre,int iAddStatus ) {
  if ( iAddTemre > new_max_temperature_ ) {
     new_max_temperature_ = iAddTemre;
     iNewMaxStatus = iAddStatus;
  }
}

void Sampler::SetTransmitterNumber( int  new_number ) {
  transmitterAddress___ = new_number;
}
פûגאפג

void Sampler::PrepareForAllParam() {
  if (((*this).transmitterID___>=5)&&((*this).transmitterID___<23)) {
    lengthAllParams___ = (LEN_HED) + (LEN_IB_TR) +
      (*this).excitersTotal_*((LEN_VTV_MPAR)+(*this).sizeBlockModParams_) +
      (*this).PABTotal_*((LEN_PAB_MPAR)+(*this).sizeBlockPreampParams_ +
      (*this).sizeBlockTerminalAmpParams_ * (*this).preampPerPAB___)+
      (*this).BCNTotal_*(*this).sizeBlockBCNParams_+
      (*this).DBTotal_*(*this).sizeBlockDBParams_+
      (*this).iUsedTime*(LEN_TIME)+(LEN_CS);
  }
  //analog
  else {
    lengthAllParams___ = (LEN_HED) + (LEN_IB_TR) +
      (*this).excitersTotal_*((LEN_VTV_MPAR_AN)+(*this).sizeBlockModParams_) +
      (*this).PABTotal_*((LEN_PAB_MPAR)+(*this).sizeBlockPreampParams_ +
      (*this).sizeBlockTerminalAmpParams_ * (*this).preampPerPAB___)+
      (*this).BCNTotal_*(*this).sizeBlockBCNParams_+
      (*this).DBTotal_*(*this).sizeBlockDBParams_+
      (*this).iUsedTime*(LEN_TIME)+(LEN_CS);
  }
}

void Sampler::PrepareForAllParam_v1() {
  lengthAllParams___ = (LEN_HED_v1) + (LEN_IB_TR_v1) + (NUM_PAB_4KWATT)*(LEN_PAB_v1)+(LEN_CS);
}
} // namespace tmitter_web_service
