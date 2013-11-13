// encoding : utf8
// "Copyright [year] <Copyright Owner>"  [legal/copyright]

#include <app-server-code/data_processor/sampler_uni_header.h>

namespace tmitter_web_service {
  //} // namespace tmitter_web_service
using std::string;
//using std::v ector;
using simple_type_processors::double2str_dot_yy;
using simple_type_processors::double2str_dot_y;

void Sampler::PasteAjaxCFGTransm( std::string &s )  
{

  char tmp[20];
  
  s+="NPow=";   _itoa( nominalPower___,tmp,10 );  s+=tmp;
  s+="&NuVt=";  _itoa( excitersTotal_,tmp,10 );      s+=tmp;
  s+="&NuAB=";  _itoa( PABTotal_,tmp,10 );      s+=tmp;
  s+="&NuPA=";  _itoa( preampPerPAB___,tmp,10 );    s+=tmp;
  s+="&NuBC=";  _itoa( BCNTotal_,tmp,10 );      s+=tmp;
  s+="&NuDB=";  _itoa( DBTotal_,tmp,10 );      s+=tmp;
  s+="&TrNa=";  _itoa( transmitterID___,tmp,10 );    s+=tmp;
  s+="&VtTy=";  _itoa( exciterType_,tmp,10 );      s+=tmp;
  s+="&Chan=";  _itoa( iChannel,tmp,10 );  s+=tmp;
  s+="&_Pow=";  _itoa( realPowerRepresent___,tmp,10 );  s+=tmp;
  
  double dDif = ( ( double )FRWValue___ )/100.0;
  s+="&VFRW=";
  s+= double2str_dot_yy(dDif);

  //12
  s+="&TrOO=";  _itoa( this->tmitterOnTgr___.D,tmp,10 );  s+=tmp;
  s+="&TrLc=";  _itoa( this->tmitterLockTgr___.D, tmp,10 );  s+=tmp;
  s+="&Stas=";  _itoa( this->statusTmitterTgr_.D, tmp,10 );  s+=tmp;
  s+="&MaxT=";  _itoa( iMaxTemre,tmp,10 );  s+=tmp;
  s+="&MaxS=";  _itoa( iMaxStatus,tmp,10 );  s+=tmp;
  s+="&_FRW=";  _itoa( failFRW_,tmp,10 );  s+=tmp;
  s+="&conn=";  _itoa( connecton_state___,tmp,10 );  s+=tmp;
  s+="&cn_snmp=";  _itoa( snmp_connecton_status___,tmp,10 );  s+=tmp;
  s+="&iFRWLow=";  _itoa( currentBounds___.iFRWLow,tmp,10 );  s+=tmp;
  s+="&iFRWMax=";  _itoa( currentBounds___.iFRWMax,tmp,10 );  s+=tmp;
  s+="&iPowLow=";  _itoa( currentBounds___.iPowLow,tmp,10 );  s+=tmp;
  s+="&iPowMax=";  _itoa( currentBounds___.iPowMax,tmp,10 );  s+=tmp;
  s+="&iTemLow=";  _itoa( currentBounds___.iTemLow,tmp,10 );  s+=tmp;
  s+="&iTemMax=";  _itoa( currentBounds___.iTemMax,tmp,10 );  s+=tmp;
}

void Sampler::PackMainParamsTransmitter( std::string &s ) {
  char tmp[20];
  // Основыне параметры
  s+="&CnMo=";  _itoa( this->ctrlModeTgr_.D,tmp,10 );  s+=tmp;
  s+="&Load=";  _itoa( this->typeLoadTgr_.D,tmp,10 );  s+=tmp;

  s+="&AnDi=";  _itoa( ibAnlgDigit_,tmp,10 );  s+=tmp;
  s+="&Work=";  _itoa( ibWork_,tmp,10 );  s+=tmp;
  s+="&VtLc=";  _itoa( exciterLock_,tmp,10 );  s+=tmp;
  s+="&PALc=";  _itoa( ibPowAmpLock_,tmp,10 );  s+=tmp;
  s+="&TrLc=";  _itoa( this->tmitterLockTgr___.D, tmp,10 );  s+=tmp;
  s+="&TrOO=";  _itoa( this->tmitterOnTgr___.D, tmp,10 );  s+=tmp;
  
  // Конфигурация системы
  s+="&NuVt=";  _itoa( excitersTotal_,tmp,10 );  s+=tmp;
  s+="&NuAB=";  _itoa( PABTotal_,tmp,10 );  s+=tmp;
  s+="&NuPA=";  _itoa( preampPerPAB___,tmp,10 );  s+=tmp;
  s+="&NuBC=";  _itoa( BCNTotal_,tmp,10 );  s+=tmp;
  s+="&NuDB=";  _itoa( DBTotal_,tmp,10 );  s+=tmp;
  
  //?
  s+="&V12O=";  _itoa( ibVtv12OnOff_,tmp,10 );  s+=tmp;

  
  //10
  s+="&RadM=";  _itoa( ibRadioMode_,tmp,10 );  s+=tmp;
  s+="&Redy=";  _itoa( transmitterReady_,tmp,10 );  s+=tmp;
  s+="&InSt=";  _itoa( this->istreamTgr_.D, tmp,10 );  s+=tmp;
  s+="&OuSy=";  _itoa( this->outSynTgr_.D, tmp,10 );  s+=tmp;
  s+="&Stas=";  _itoa( this->statusTmitterTgr_.D, tmp,10 );  s+=tmp;
  s+="&_Net=";  _itoa( failNet_,tmp,10 );  s+=tmp;
  s+="&Blst=";  _itoa( failBallast_,tmp,10 );  s+=tmp;
  s+="&_FRW=";  _itoa( failFRW_,tmp,10 );  s+=tmp;
  s+="&Cool=";  _itoa( failCoolling_,tmp,10 );  s+=tmp;
  s+="&_PAB=";  _itoa( ibPAB,tmp,2 );  s+=tmp;
  
  //20
  s+="&R485=";  _itoa( ibRS485,tmp,10 );  s+=tmp;
  s+="&_I2C=";  _itoa( ibI2C,tmp,10 );  s+=tmp;
  s+="&iVtv=";  _itoa( ibVtv,tmp,2 );  s+=tmp;
  s+="&Chan=";  _itoa( iChannel,tmp,10 );  s+=tmp;
  s+="&_Pow=";  _itoa( realPowerRepresent___,tmp,10 );  s+=tmp;
  
  double  dDif = ( ( double )FRWValue___ )/100.0;
  s+="&VFRW=";  s+= double2str_dot_yy(dDif);
  
  s+="&MaxT=";  _itoa( iMaxTemre,tmp,10 );  s+=tmp;
  s+="&MaxS=";  _itoa( iMaxStatus,tmp,10 );  s+=tmp;
  s+="&conn=";  _itoa( connecton_state___,tmp,10 );  s+=tmp;
  s+="&NPow=";  _itoa( nominalPower___,tmp,10 );s+=tmp;
  s+="&iFRWLow=";  _itoa( currentBounds___.iFRWLow,tmp,10 );  s+=tmp;
  s+="&iFRWMax=";  _itoa( currentBounds___.iFRWMax,tmp,10 );  s+=tmp;
  s+="&iPowLow=";  _itoa( currentBounds___.iPowLow,tmp,10 );  s+=tmp;
  s+="&iPowMax=";  _itoa( currentBounds___.iPowMax,tmp,10 );  s+=tmp;
  s+="&iTemLow=";  _itoa( currentBounds___.iTemLow,tmp,10 );  s+=tmp;
  s+="&iTemMax=";  _itoa( currentBounds___.iTemMax,tmp,10 );  s+=tmp;

  for ( int i=0; i < BCNTotal_; i++ )  {
    s+="&Bcvi=";  _itoa( ibBCVstatus[i],tmp,10 );  s+=tmp;
  }

  for ( int i=0; i < DBTotal_; i++ ) {
    s+="&Bdi=";    _itoa( ibBDstatus[i],tmp,10 );    s+=tmp;
  }

  s+="&TrNa=";  _itoa( transmitterID___,tmp,10 );  s+=tmp;
  s+="&SOOi=";
  if ( ibVtv12OnOff_ > 0 )  {
    _itoa( vtvSigOnOffTgrD_[ibVtv12OnOff_-1],tmp,10 );
  }
  else {
    strcpy( tmp,"0" );
  }
  s+=tmp;

  s+="&Overi=";
  if ( ibVtv12OnOff_ > 0 ) {
    _itoa( vtvOverModTgrD_[ibVtv12OnOff_-1],tmp,10 );
  }
  else strcpy( tmp,"1" );
  s+=tmp;

  for ( int i=0; i < excitersTotal_; i++ ) {
    s+="&VTni="; _itoa( ibVTVNoconnet[i],tmp,10 );  s+=tmp;
  }

  s+="&pBal=";  _itoa( iBallastPower,tmp,10 );  s+=tmp;
  s+="&pwH=";  _itoa(this->powHalfModeTgr_.D, tmp,10 );  s+=tmp;

  // признаки связи связь с блоками
  /*
    excitersTotal_
    PABTotal_
    BCNTotal_
    DBTotal_
    !! Danger !! есть различия в расчете, но пока пусть будут!
  */
  // ВТВ * 2
  //if ( excitersTotal_ > 0 ) {
  //exciterIstreamError_[0] = 0x00;
  //exciterIstreamError_[1] = 0x02;
  s+="&exch_with_exciters=";
  for( int i = 0; i < excitersTotal_; ++i ) {
    uchar isConnected = mSH1( exciterIstreamError_[i] );
    if( 0 == isConnected ) s += "0";
    else s += "1";
  }
  //}
  // БУМ * 10
  //PABNoConnect_[4-1] = 0x01;
  //PABNoConnect_[1-1] = 0x01; 
  s+="&exch_with_pabs=";
  for( int i = 0; i < PABTotal_; ++i ) {
    if( 0 == PABNoConnect_[i]) s += "0";
    else s += "1";
  }
  // БКН * ?
  //_bc_loads_uni[0] = 0x80;
  s+="&exch_with_bcns=";
  for( int i = 0; i < BCNTotal_; ++i ) {
    uchar isConnected = mSH7( _bc_loads_uni[i] );
    if( 0 == isConnected ) s += "0";
    else s += "1";
  }
  // БВ * ?
  //_uni_about_bl_complex_loads[0] = 0x20;
  s+="&exch_with_dbs=";
  for( int i = 0; i < DBTotal_; ++i ) {
    uchar isConnected = mSH5( _uni_about_bl_complex_loads[i] );
    if( 0 == isConnected ) s += "0";
    else s += "1";
  }
}

void Sampler::AppendETVMainParamForWeb( std::string &s,int number ) {

  char tmp[20];
  s+="&conn=";  _itoa( connecton_state___,tmp,10 );  s+=tmp;
  s+="&Trn=";    _itoa( transmitterID___,tmp,10 );  s+=tmp;
  //Main param
  if ( ( transmitterID___ >=5 )&&( transmitterID___<23 ) )
  {
    s+="&VCtm=";  _itoa( ibVtvCntrlMode[number],tmp,10 );  s+=tmp;
    s+="&VRdm=";  _itoa( ibVtvRadioMode[number],tmp,10 );  s+=tmp;
    s+="&VADm=";  _itoa( ibVtvAnlgDigit[number],tmp,10 );  s+=tmp;
    s+="&UVLc=";  _itoa( ibUsVtvLock[number],tmp,10 );  s+=tmp;
    s+="&MVLc=";  _itoa( ibModVtvLock[number],tmp,10 );  s+=tmp;
    s+="&VLck=";  _itoa( ibOwnVtvLock[number],tmp,10 );  s+=tmp;
    s+="&VRdy=";  _itoa( ibVtvReady[number],tmp,10 );  s+=tmp;
    s+="&VWrk=";  _itoa( ibVtvWork[number],tmp,10 );  s+=tmp;
    //8
    s+="&VtSt=";  _itoa( ibVtvStatus[number],tmp,10 );  s+=tmp;
    s+="&VMSt=";  _itoa( ibVtvModStatus[number],tmp,10 );  s+=tmp;
    s+="&VUSt=";  _itoa( ibVtvUsStatus[number],tmp,10 );  s+=tmp;
    // входной поток возбудителя
    s+="&VtIn=";  _itoa( exciterIstream_[number],tmp,10 );  s+=tmp;
    // !!
    
    s+="&VOSy=";  _itoa( ibVtvOutSync[number],tmp,10 );  s+=tmp;
    s+="&VErr=";  _itoa( exciterIstreamError_[number],tmp,16 );  if ( strlen( tmp )<2 )  s+="0"; 
    s+=tmp;
    s+="&VMEr=";  _itoa( ibVtvErrMod[number],tmp,16 );  if ( strlen( tmp )<2 )  s+="0"; 
    s+=tmp;
    s+="&VOPw=";  _itoa( iVtvOutPower[number],tmp,10 );  s+=tmp;
    s+="&VVIP=";  s+=cExt; 
    s+=":";      _itoa( uiPort[number],tmp,10 );      s+=tmp;
  }
  else  //analog
  {
    s+="&SKS=";  _itoa( ibVtvSKSStatus[number],tmp,10 );  s+=tmp;

    s+="&SCM=";  _itoa( ibVtvSChMStatus[number],tmp,10 );  s+=tmp;

    s+="&SPC=";  _itoa( ibVtvSPChStatus[number],tmp,10 );  s+=tmp;

    s+="&SOO=";  _itoa( vtvSigOnOffTgrD_[number],tmp,10 );  s+=tmp;

    s+="&Vid=";  _itoa( ibVtvVideoLock[number],tmp,10 );  s+=tmp;

    s+="&Pow=";  _itoa( ibVtvPowerStatus[number],tmp,10 );  s+=tmp;

    s+="&OMO=";  _itoa( vtvOverModTgrD_[number],tmp,10 );  s+=tmp;

    s+="&Sou=";  _itoa( ibVtvSound[number],tmp,10 );  s+=tmp;

    s+="&VCN=";  _itoa( iVtvChanNumAnalog[number],tmp,10 );  s+=tmp;

    s+="&NON=";  _itoa( ibVTVNoconnet[number],tmp,10 );  s+=tmp;
  }
}

void Sampler::PasteAjaxVtvMod( std::string &s,int k ) {
  char tmp[20];

  s+="&conn=";  _itoa( connecton_state___,tmp,10 );  s+=tmp;
  
  s+="&MNet=";  _itoa( ibVtvNet[k],tmp,10 );  s+=tmp;

  s+="&MTOO=";  _itoa( ibVtvDVBTOnOf[k],tmp,10 );  s+=tmp;

  s+="&MPOO=";  _itoa( ibVtvMIPOnOf[k],tmp,10 );  s+=tmp;

  uchar  b1 = exciterASI12Tgr_[k].D&0xf;
  s+="&MAS1=";  _itoa( b1,tmp,10 );  s+=tmp;

  b1 = ( exciterASI12Tgr_[k].D&0xf0 ) >> 4;
  s+="&MAS2=";  _itoa( b1,tmp,10 );  s+=tmp;
  s+="&MQAM=";  _itoa( ibVtvQAM[k],tmp,10 );  s+=tmp;

  s+="&MRAT=";  _itoa( ibVtvCodRate[k],tmp,10 );  s+=tmp;

  s+="&MGdI=";  _itoa( ibVtvGI[k],tmp,10 );  s+=tmp;
  //8

  s+="&MHie=";  _itoa( ibVtvHierar[k],tmp,10 );  s+=tmp;

  s+="&MNCr=";  _itoa( ibVtvCarNum[k],tmp,10 );  s+=tmp;
  
  s+="&MChn=";  _itoa( iVtvChannel[k],tmp,10 );  s+=tmp;

  s+="&MFrq=";  _itoa( iVtvFreq[k],tmp,10 );  s+=tmp;
  
  double  dDif = ( ( double )iVtvOutLevel[k] )/10.0;
  s+="&MOLv=";  s+= double2str_dot_yy(dDif);

  s+="&MTRN=";  _itoa( iVtvTransmNum[k],tmp,10 );  s+=tmp;
  
  s+="&MCLN=";  _itoa( iVtvIDCellNum[k],tmp,10 );  s+=tmp;
  //16

  s+="&MDel=";  _itoa( iVtvAdDelay[k],tmp,10 );  s+=tmp;

  s+="&MLPr=";  _itoa( iVtvPrecLineNum[k],tmp,10 );  s+=tmp;
  
  s+="&LIOO=";  _itoa( ibVtvPrecLOnOff[k],tmp,10 );  s+=tmp;

  s+="&NLOO=";  _itoa( ibVtvPrecNonLOnOff[k],tmp,10 );  s+=tmp;

  s+="&MTst=";  _itoa( ibVtvTestMode[k],tmp,10 );  s+=tmp;
  
  s+="&VVIP=";  s+=cExt;
  s+=":";      _itoa( uiPort[k],tmp,10 );      s+=tmp;
}

void Sampler::PasteAjaxPAB_main_pre( std::string &s,int k )
{
  char tmp[20];

  s+="&conn=";  _itoa( connecton_state___,tmp,10 );  s+=tmp;
  
  s+="&PBOO=";  _itoa( PABOnOffTgr_[k].D, tmp,10 );  s+=tmp;

  s+="&PBLc=";    _itoa( PABLockTgr_[k].D,tmp,10 );  s+=tmp;

  s+="&PBAD=";    _itoa( ibPABAnlgDigit[k],tmp,10 );  s+=tmp;

  s+="&PBIP=";    _itoa( ibPABInPowerStatus[k],tmp,10 );  s+=tmp;
  
  s+="&PBSt=";    _itoa( ibPABStatus[k],tmp,10 );  s+=tmp;

  s+="&PBOP=";    _itoa( ibPABOutPowerStatus[k],tmp,10 );  s+=tmp;

  s+="&PFRs=";    _itoa( ibPABFRWStatus[k],tmp,10 );  s+=tmp;
  
  s+="&PIPw=";    _itoa( iPABInPow[k],tmp,10 );  s+=tmp;
  //8
  s+="&POPw=";    _itoa( iPABOutPow[k],tmp,10 );  s+=tmp;
  
  double  dDif = ( ( double )iPABFRW[k] )/100.0;
  s+="&PFRW=";    s+= double2str_dot_yy(dDif);
  
  //pre amplif
  s+="&PVT1=";    _itoa( ( ibPreVT12status[k]&0x1 ),tmp,10 );  s+=tmp;

  s+="&PVT2=";    _itoa( ( ( ibPreVT12status[k]&0x2 ) >> 1 ),tmp,10 );  s+=tmp;

  s+="&PTem=";    _itoa( ibPreTemStaus[k],tmp,10 );  s+=tmp;

  s+="&PMIP=";    _itoa( ibPreMIP[k],tmp,10 );  s+=tmp;
  
  s+="&PAGC=";    _itoa( ibPreAGC[k],tmp,10 );  s+=tmp;
  
  s+="&PP15=";    _itoa( ibPrePlus15[k],tmp,10 );  s+=tmp;
 
  //16
  dDif = ( ( double )iPreCurVT1[k] )/10.0;
  s+="&CVT1=";   s+= double2str_dot_y(dDif);
  
  dDif = ( ( double )iPreCurVT2[k] )/10.0;
  s+="&CVT2=";   s+= double2str_dot_y(dDif);
  
  s+="&PTeV=";    _itoa( iPreTemValue[k],tmp,10 );  s+=tmp;
  
  dDif = ( ( double )iPreMIPVoltage[k] )/10.0;
  s+="&MIPV=";    s+= double2str_dot_y(dDif);
  
  dDif = ( ( double )iPreAttenVoltage[k] )/10.0;
  s+="&PAtV=";    s+= double2str_dot_y(dDif);

  dDif = ( ( double )iPreAPhaseVoltage[k] )/100.0;
  s+="&PPhV=";    s+= double2str_dot_yy(dDif);

  dDif = ( ( double )iPreReferVoltage[k] )/10.0;
  s+="&RefV=";    s+= double2str_dot_y(dDif);
  
  s+="&NuPA=";  _itoa( preampPerPAB___,tmp,10 );  s+=tmp;

  s+="&iFRWLow=";  _itoa( currentBounds___.iFRWLow,tmp,10 );  s+=tmp;

  s+="&iFRWMax=";  _itoa( currentBounds___.iFRWMax,tmp,10 );  s+=tmp;

  s+="&iTemLow=";  _itoa( currentBounds___.iTemLow,tmp,10 );  s+=tmp;

  s+="&iTemMax=";  _itoa( currentBounds___.iTemMax,tmp,10 );  s+=tmp;

  s+="&Trn=";    _itoa( transmitterID___,tmp,10 );  s+=tmp;
  
}

void Sampler::PasteAjaxgetbd( std::string &s,int k ) {
  char tmp[20];

  s+="&conn=";  _itoa( connecton_state___,tmp,10 );  s+=tmp;

  s+="&Trn=";    _itoa( transmitterID___,tmp,10 );  s+=tmp;

  s+="&Lock=";  _itoa( ibBDTrLock[k],tmp,10 );  s+=tmp;

  s+="&Stat=";  _itoa( ibBDstatus[k],tmp,10 );  s+=tmp;

  s+="&AnDi=";  _itoa( ibBDAnlgDigit[k],tmp,10 );  s+=tmp;

  s+="&Powr=";  _itoa( iBDOutPower[k],tmp,10 );  s+=tmp;

  double  dDif = ( ( double )iBDFRW[k] )/100.0;
  s+="&VFRW="; s+= double2str_dot_yy(dDif);

  s+="&Psnd=";  _itoa( iBDSoundPower[k],tmp,10 );  s+=tmp;
}

void Sampler::PasteAjaxgetbn( std::string &s,int number ) {
  char tmp[20];

  s+="&conn=";  _itoa( connecton_state___,tmp,10 );  s+=tmp;

  s+="&Trn=";    _itoa( transmitterID___,tmp,10 );  s+=tmp;

  s+="&Lock=";  _itoa( ibBCVTrLock[number],tmp,10 );  s+=tmp;

  s+="&Stat=";  _itoa( ibBCVstatus[number],tmp,10 );  s+=tmp;

  s+="&AnDi=";  _itoa( ibBCVAnlgDigit[number],tmp,10 );  s+=tmp;

  s+="&R1=";  _itoa( ibBCV_R1[number],tmp,10 );  s+=tmp;

  s+="&S1=";  _itoa( ibBCV_TR1[number],tmp,10 );  s+=tmp;

  s+="&T1=";  _itoa( iBCV_R1Tem[number],tmp,10 );  s+=tmp;

  s+="&P1=";  _itoa( iBCV_R1Pow[number],tmp,10 );  s+=tmp;
  //
  s+="&R2=";  _itoa( ibBCV_R2[number],tmp,10 );  s+=tmp;

  s+="&S2=";  _itoa( ibBCV_TR2[number],tmp,10 );  s+=tmp;

  s+="&T2=";  _itoa( iBCV_R2Tem[number],tmp,10 );  s+=tmp;

  s+="&P2=";  _itoa( iBCV_R2Pow[number],tmp,10 );  s+=tmp;
  //
  s+="&R3=";  _itoa( ibBCV_R3[number],tmp,10 );  s+=tmp;

  s+="&S3=";  _itoa( ibBCV_TR3[number],tmp,10 );  s+=tmp;

  s+="&T3=";  _itoa( iBCV_R3Tem[number],tmp,10 );  s+=tmp;

  s+="&P3=";  _itoa( iBCV_R3Pow[number],tmp,10 );  s+=tmp;
  //
  s+="&R4=";  _itoa( ibBCV_R4[number],tmp,10 );  s+=tmp;

  s+="&S4=";  _itoa( ibBCV_TR4[number],tmp,10 );  s+=tmp;

  s+="&T4=";  _itoa( iBCV_R4Tem[number],tmp,10 );  s+=tmp;

  s+="&P4=";  _itoa( iBCV_R4Pow[number],tmp,10 );  s+=tmp;

  //
  s+="&R5=";  _itoa( ibBCV_R5[number],tmp,10 );  s+=tmp;

  s+="&S5=";  _itoa( ibBCV_TR5[number],tmp,10 );  s+=tmp;

  s+="&T5=";  _itoa( iBCV_R5Tem[number],tmp,10 );  s+=tmp;
  

  s+="&P5=";  _itoa( iBCV_R5Pow[number],tmp,10 );  s+=tmp;
  //
  s+="&R6=";  _itoa( ibBCV_R6[number],tmp,10 );  s+=tmp;

  s+="&S6=";  _itoa( ibBCV_TR6[number],tmp,10 );  s+=tmp;

  s+="&T6=";  _itoa( iBCV_R6Tem[number],tmp,10 );  s+=tmp;

  s+="&P6=";  _itoa( iBCV_R6Pow[number],tmp,10 );  s+=tmp;
  //
  s+="&R7=";  _itoa( ibBCV_R7[number],tmp,10 );  s+=tmp;

  s+="&S7=";  _itoa( ibBCV_TR7[number],tmp,10 );  s+=tmp;

  s+="&T7=";  _itoa( iBCV_R7Tem[number],tmp,10 );  s+=tmp;

  s+="&P7=";  _itoa( iBCV_R7Pow[number],tmp,10 );  s+=tmp;
  
  //
  s+="&R8=";  _itoa( ibBCV_R8[number],tmp,10 );  s+=tmp;

  s+="&S8=";  _itoa( ibBCV_TR8[number],tmp,10 );  s+=tmp;

  s+="&T8=";  _itoa( iBCV_R8Tem[number],tmp,10 );  s+=tmp;

  s+="&P8=";  _itoa( iBCV_R8Pow[number],tmp,10 );  s+=tmp;
}

void Sampler::PasteAjaxPAB_PA( std::string &s,int k,int m ) {
  char tmp[20];
  s+="&AVT1=";  _itoa( ( ibPaVT12status[k][m]&0x1 ),tmp,10 );  s+=tmp;
  s+="&AVT2=";  _itoa( ( ( ibPaVT12status[k][m]&0x2 ) >> 1 ),tmp,10 );  s+=tmp;
  s+="&ATem=";  _itoa( ibPaTemStaus[k][m],tmp,10 );  s+=tmp;
  s+="&AMIP=";  _itoa( ibPaMIP[k][m],tmp,10 );  s+=tmp;

  double  dDif = ( ( double )iPaCurVT1[k][m] )/10.0;
  s+="&IVT1=";    s+= double2str_dot_y(dDif);

  dDif = ( ( double )iPaCurVT2[k][m] )/10.0;
  s+="&IVT2=";    s+= double2str_dot_y(dDif);
  s+="&ATeV=";  _itoa( iPaTemValue[k][m],tmp,10 );  s+=tmp;

  dDif = ( ( double )iPaMIPVoltage[k][m] )/10.0;
  s+="&AMiV=";    s+= double2str_dot_y(dDif);
}

//
void Sampler::PasteAjaxOK( std::string &s )
{
  char tmp[20];
  s+= "&conn=";  _itoa( connecton_state___,tmp,10 );  s+=tmp;
  s+="&Res=ОК";
  //s+="&Res=О";
}

// Ошибка POST
void Sampler::PasteAjaxEr( std::string &s ) {
  char tmp[20];
  
  // статус соединения по rs
  s+="&conn=";  
  _itoa( connecton_state___,tmp,10 );  
  s+=tmp;
  
  // статус ответа
  s+="&Res=Err";
}
// Ошибка POST

} // namespace tmitter_web_service