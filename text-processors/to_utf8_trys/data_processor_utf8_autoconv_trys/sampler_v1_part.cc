// "Copyright [year] <Copyright Owner>"  [legal/copyright]

#include <app-server-code/data_processor/sampler_uni_header.h>

namespace tmitter_web_service {
//} // namespace tmitter_web_service
using std::string;
//
//
//
#ifndef NO_RTS
uchar* Sampler::RptMainStateTmitter_v1( uchar  *ptrSourceArray ) {
  uchar* repeater_ptr=NULL;

  // processing
  uchar  processedByte = ptrSourceArray[0];//cBuf[3];
  transmitterOnTgrD___ = ( processedByte & 0x01 );
  if ( transmitterOnTgrQ_ != transmitterOnTgrD___ ) //SNMP_CM7
  {
    transmitterOnTgrQ_=transmitterOnTgrD___;
    currentQueryIndex_++;
    hasMsgForSnmp_ = true;
    if ( kMaxSNMPQueue == currentQueryIndex_ ) {
      currentQueryIndex_ = 0;
    }
    snmpEventsQuery_[currentQueryIndex_] = ( SNMP_CM7 );
    stringMsgsQuery_[currentQueryIndex_] = "Передатчик-";
    if ( transmitterOnTgrQ_==0 ) 
    {
      stringMsgsQuery_[currentQueryIndex_] += "отключен";
      
      currentMWFCode_ |= kSnmpOk;
      typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
    }
    else
    {
      stringMsgsQuery_[currentQueryIndex_] += "включен";
      
      currentMWFCode_ |= kSnmpOk;
      typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
    }
    typeMsgsQuery_[currentQueryIndex_] = kSnmpOk;
  }
  else
  {
    if ( transmitterOnTgrD___==0 ) 
    {
      currentMWFCode_ |= kSnmpOk;
    }
    else
    {
      currentMWFCode_ |= kSnmpOk;
    }
  }

  ibSoundLock = ( processedByte & 0x02 ) >> 1;
  ibPowAmpLock_ = ( processedByte & 0x04 ) >> 2;

  transmitterLockTgrD___ = ( processedByte & 0x08 ) >> 3;
  
  if ( transmitterLockTgrQ_ != transmitterLockTgrD___ ) //SNMP_CM6
  {
    transmitterLockTgrQ_=transmitterLockTgrD___;
    currentQueryIndex_++;
    hasMsgForSnmp_ = true;
    if ( kMaxSNMPQueue == currentQueryIndex_ )
    {
      currentQueryIndex_ = 0;
    }
    snmpEventsQuery_[currentQueryIndex_] = ( SNMP_CM6 );
    stringMsgsQuery_[currentQueryIndex_] = "Передатчик-";
    if ( transmitterLockTgrQ_==0 ) {
      stringMsgsQuery_[currentQueryIndex_] += "отперт";
      currentMWFCode_ |= kSnmpOk;
      typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
      initCounter_ = 0;
    }
    else
    {
      stringMsgsQuery_[currentQueryIndex_] += "заперт";
      
      currentMWFCode_ |= kSnmpOk;
      typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
    }
    typeMsgsQuery_[currentQueryIndex_] = kSnmpOk;
  }
  else
  {
    if ( transmitterLockTgrD___==0 ) 
    {
      currentMWFCode_ |= kSnmpOk;
      if ( initCounter_ < maxValueInitCounter_ )
        initCounter_++;
    }
    else
    {
      currentMWFCode_ |= kSnmpOk;
    }
  }

  ibVtv12OnOff_ = ( processedByte & 0x10 ) >> 4;
  ibAnlgDigit_ = 1;
  typeLoadingTgrD_ = ( processedByte & 0x40 ) >> 6;
  if ( typeLoadingTgrQ_ != typeLoadingTgrD_ ) //SNMP_CM12
  {
    typeLoadingTgrQ_ = typeLoadingTgrD_;
    hasMsgForSnmp_ = true;
    currentQueryIndex_++;
    if ( kMaxSNMPQueue == currentQueryIndex_ )
    {
      currentQueryIndex_ = 0;
    }
    snmpEventsQuery_[currentQueryIndex_] = ( SNMP_CM12 );
    stringMsgsQuery_[currentQueryIndex_] = "Работа на ";
    if ( typeLoadingTgrQ_==0 ) 
    {
      stringMsgsQuery_[currentQueryIndex_] += "антенну";
      
      currentMWFCode_ |= kSnmpOk;
      typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
      typeMsgsQuery_[currentQueryIndex_] = kSnmpOk;
    }
    else
    {
      stringMsgsQuery_[currentQueryIndex_] += "нагрузку";
      
      currentMWFCode_ |= kSnmpOk;
      typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
      typeMsgsQuery_[currentQueryIndex_] = kSnmpOk;
    }
  }
  else
  {
    if ( typeLoadingTgrD_==0 ) 
    {
      currentMWFCode_ |= kSnmpOk;
    }
    else
    {
      currentMWFCode_ |= kSnmpOk;
    }
  }

  controlModeTgrD_ =  ( processedByte & 0x80 ) >> 7;

  
  if ( controlModeTgrQ_ != controlModeTgrD_ ) //SNMP_CM2
  {
    controlModeTgrQ_ = controlModeTgrD_;
    hasMsgForSnmp_ = true;
    currentQueryIndex_++;
    if ( kMaxSNMPQueue == currentQueryIndex_ )
    {
      currentQueryIndex_ = 0;
    }
    snmpEventsQuery_[currentQueryIndex_] = ( SNMP_CM2 );
    stringMsgsQuery_[currentQueryIndex_] = "Режим управления передатчиком-";
    if ( controlModeTgrQ_==0 ) 
    {
      stringMsgsQuery_[currentQueryIndex_] += "местный";
      
      currentMWFCode_ |= kSnmpWarning;
      typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
      typeMsgsQuery_[currentQueryIndex_] = kSnmpWarning;
    }
    else
    {
      stringMsgsQuery_[currentQueryIndex_] += "дистанционный";
      
      currentMWFCode_ |= kSnmpOk;
      typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
      typeMsgsQuery_[currentQueryIndex_] = kSnmpOk;
    }
  }
  else
  {
    if ( controlModeTgrD_==0 ) 
    {
      currentMWFCode_ |= kSnmpWarning;
    }
    else
    {
      currentMWFCode_ |= kSnmpOk;
    }
  }
  
  //1
  channalValuePacked_=ptrSourceArray[1];
  iChannel = iChannelSetup;

  //2
  processedByte=ptrSourceArray[2];

  transmitterStatusTgrD_ =  ( processedByte & 0x80 ) >> 7;
  
  bool failsReseted=false;
  
  if ( failsTotalQ_ != failsTotalQQ_ ) {
    failsReseted=true;
  }
  failsTotalQQ_ = failsTotalQ_;
  
  failsTotalD_ = ( uint )( transmitterStatusTgrD_ );
  

  if ( ( transmitterStatusTgrQ_ != transmitterStatusTgrD_ )||( failsReseted ) ) //SNMP_CM5
  {
    transmitterStatusTgrQ_=transmitterStatusTgrD_;
    currentQueryIndex_++;
    hasMsgForSnmp_ = true;
    if ( kMaxSNMPQueue == currentQueryIndex_ )
    {
      currentQueryIndex_ = 0;
    }
    snmpEventsQuery_[currentQueryIndex_] = ( SNMP_CM5 );
    
    // сохраняем позиция Отказа
    fixedAlrmRecordIndex_ = currentQueryIndex_;
    // statusRecordIndex_ = msgsSetFromOnIteration_
    newFailOccure_ = true;
    
    if ( transmitterStatusTgrQ_==1 ) 
    {
      stringMsgsQuery_[currentQueryIndex_] = "Отказ передатчика, ";
      
      currentMWFCode_ |= ( kSnmpFail );
      typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
      typeMsgsQuery_[currentQueryIndex_] = ( kSnmpFail );
    }
    else if ( transmitterStatusTgrQ_==0 ) 
    {
      stringMsgsQuery_[currentQueryIndex_] = "Передатчик норма";
      
      currentMWFCode_ |= kSnmpOk;
      typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
      typeMsgsQuery_[currentQueryIndex_] = kSnmpOk;
    }
  }
  else
  {
    if ( transmitterStatusTgrD_==1 ) 
    {
      currentMWFCode_ |= ( kSnmpFail );
    }
    else if ( transmitterStatusTgrD_==0 ) 
    {
      currentMWFCode_ |= kSnmpOk;
    }
  }

  failCoolling_ = ( processedByte & 0x02 ) >> 1;
  failsTotalD_ += ( uint )failCoolling_;

  if ( ( 0 != transmitterStatusTgrD_ )&&( failCoolling_==1 )&&( newFailOccure_ ) )
  {
    stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ охлаждение,";
  }
  
  ibSU1St =  ( processedByte & 0x04 ) >> 2; 
  ibSU2St =  ( processedByte & 0x08 ) >> 3;

  failFRW_ =  ( processedByte & 0x10 ) >> 4;
  failsTotalD_ += ( uint )failFRW_;
  if ( ( 0 != transmitterStatusTgrD_ )&&( failFRW_==1 )&&( newFailOccure_ ) )
  {
    stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ КБВ,";
  }
  
  failBallast_ =  ( processedByte & 0x20 ) >> 5;
  failsTotalD_ += ( uint )failBallast_;
  if ( ( 0 != transmitterStatusTgrD_ )&&( failBallast_==1 )&&( newFailOccure_ ) )
  {
    stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ балласт,";
  }

  failNet_ =  ( processedByte & 0x40 ) >> 6;
  failsTotalD_ += ( uint )failNet_;
  if ( ( 0 != transmitterStatusTgrD_ )&&( failNet_==1 )&&( newFailOccure_ ) )
  {
    stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ сеть,";
    
  }

  //3
  ibPAB=ptrSourceArray[3];
  
  //4
  processedByte=ptrSourceArray[4];
  ibPAB  |= ( ( uint )( processedByte & 0x03 ) )<<8;
  failsTotalD_ += ( uint )ibPAB;

  if ( ( 0 != transmitterStatusTgrD_ )&&( ibPAB != 0 )&&( newFailOccure_ ) )
  {
    for( int j=0; j <PABTotal_; j++ )
    {
      char chb[10];
      int ib = ( ( ibPAB >> j )&0x1 );
      if ( ib==1 )
      {
        stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ БУМ";
        _itoa( ( j+1 ),chb,10 );
        stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
        stringMsgsQuery_[fixedAlrmRecordIndex_]+=",";
      }
    }
  }

  ibI2C = ( processedByte & 0x04 ) >> 2;    //SPI or TWI
  ibRS485 = ( processedByte & 0x08 ) >> 3;

  ibVtv = ( processedByte & 0xC0 ) >> 6;
  
  failsTotalD_ += ( uint )ibVtv;

  if ( ( 0 != transmitterStatusTgrD_ )&&( ibVtv != 0 )&&( newFailOccure_ ) )
  {
    for( int j=0; j <excitersTotal_; j++ )
    {
      char chb[10];
      int ib = ( ( ibVtv >> j )&0x1 );
      if ( ib==1 )
      {
        stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ ВТВ";
        _itoa( ( j+1 ),chb,10 );
        stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
        stringMsgsQuery_[fixedAlrmRecordIndex_]+=",";
      }
    }
  }

  //6
  ibPower = ( ( uint )ptrSourceArray[5] );

  //7
  ibPower |= ( ( ( ( uint )( ptrSourceArray[6] ) )&0xf )<<8 );

  realPowerRepresent___ = ( ibPower&0x0f )*10;
  realPowerRepresent___ += ( ( ibPower&0xf0 ) >> 4 )*100;
  realPowerRepresent___ += ( ( ibPower&0xf00 ) >> 8 )*1000;
  

  int nominal_power = nominalPower___;
  float fv = ( ( float )nominal_power/( float )100 ); 
  int  low =  ( int )( floor( fv * ( 100 - currentBounds___.iPowLow ) ) );
  int  hig =  ( int )( floor( fv * ( 100 + currentBounds___.iPowMax ) ) );
  if ( ( transmitterOnTgrD___==1 )&&( transmitterLockTgrD___==0 ) )
  {
    if (   ( realPowerRepresent___<low )||( realPowerRepresent___ > hig )   ) //SNMP_CM9
    {
      
      if ( ( ( powerOk_ ) || ( printPowerNeed_ ) ) && ( initCounter_  >= maxValueInitCounter_ ) )
      {
        powerOk_=false;
        currentQueryIndex_++;
        hasMsgForSnmp_ = true;
        if ( kMaxSNMPQueue == currentQueryIndex_ )  {
          currentQueryIndex_ = 0;
        }
        snmpEventsQuery_[currentQueryIndex_] = ( SNMP_CM9 );
        stringMsgsQuery_[currentQueryIndex_] = "Внимание, выходная мощность = ";

        char chb[10];
        _itoa( realPowerRepresent___,chb,10 );
        stringMsgsQuery_[currentQueryIndex_] += chb; stringMsgsQuery_[currentQueryIndex_] += " Вт";
        currentMWFCode_ |= kSnmpWarning;
        typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
        typeMsgsQuery_[currentQueryIndex_] = kSnmpWarning;
      }
    }
    else
    {
      if ( !powerOk_ )
      {
        powerOk_=true;

        currentQueryIndex_++;
        hasMsgForSnmp_ = true;
        if ( kMaxSNMPQueue == currentQueryIndex_ )
        {
          currentQueryIndex_ = 0;
        }
        snmpEventsQuery_[currentQueryIndex_] = ( SNMP_CM9 );
        
        stringMsgsQuery_[currentQueryIndex_] = "Выходная мощность норма";

        currentMWFCode_ |= kSnmpOk;
        typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
        typeMsgsQuery_[currentQueryIndex_] = kSnmpOk;
      }
    }
  }

  //8
  ibValueFRW = ptrSourceArray[7];
  FRWValue___ =  ( ibValueFRW&0x0f );
  FRWValue___ += ( ( ibValueFRW&0xf0 ) >> 4 )*10;

  nominal_power = ( FRW_VALUE_H );
  fv = ( ( float )nominal_power/( float )100 ); 
  //low =  ( int )( floor( fv * ( FRW_VALUE_L ) ) );FRW_VALUE_H
  low =  ( int )( floor( fv * ( currentBounds___.iFRWLow ) ) );
  hig =  ( int )( floor( fv * ( currentBounds___.iFRWMax ) ) );
  if ( ( transmitterOnTgrD___==1 )&&( transmitterLockTgrD___==0 ) )
  {
    if (   ( FRWValue___<low )||( FRWValue___ > hig )   ) //SNMP_CM10
    {
      if ( ( FRWOk_ )&&( initCounter_  >= maxValueInitCounter_ ) )
      {
        FRWOk_ = false;

        currentQueryIndex_++;
        hasMsgForSnmp_ = true;
        if ( kMaxSNMPQueue == currentQueryIndex_ )
        {
          currentQueryIndex_ = 0;
        }
        snmpEventsQuery_[currentQueryIndex_] = ( SNMP_CM10 );
        
        stringMsgsQuery_[currentQueryIndex_] = "Внимание, значение КБВ = ";

        char chb[10];
        _itoa( FRWValue___,chb,10 );
        stringMsgsQuery_[currentQueryIndex_] += chb; 
          
        currentMWFCode_ |= kSnmpWarning;
        typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
        typeMsgsQuery_[currentQueryIndex_] = kSnmpWarning;
      }
    }
    else
    {
      if ( !FRWOk_ )
      {
        FRWOk_ =true;
        
        currentQueryIndex_++;
        hasMsgForSnmp_ = true;
        if ( kMaxSNMPQueue == currentQueryIndex_ )
        {
          currentQueryIndex_ = 0;
        }
        snmpEventsQuery_[currentQueryIndex_] = ( SNMP_CM10 );
        
        stringMsgsQuery_[currentQueryIndex_] = "Значение КБВ норма";

        currentMWFCode_ |= kSnmpOk;
        typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
        typeMsgsQuery_[currentQueryIndex_] = kSnmpOk;
      }
    }
  }

  failBallast_Power = ptrSourceArray[8];
  failBallast_Power |= ( ( ( ( uint )( ptrSourceArray[6] ) )&0xf0 )<<4 );  
  
  iBallastPower = ( failBallast_Power&0x0f )*10;
  iBallastPower += ( ( failBallast_Power&0xf0 ) >> 4 )*100;
  iBallastPower += ( ( failBallast_Power&0xf00 ) >> 8 )*1000;

  repeater_ptr = &ptrSourceArray[9];
  return repeater_ptr;
}

#endif  // NO_RTS
//
//
//

uchar* Sampler::RptMainParamsPAB_v1( int k,uchar*  ptrSourceArray )
{
#ifndef NO_RTS
  uchar* repeater_ptr=NULL;
  uchar  processedByte = ptrSourceArray[0];

  PABOnOffTgrD_[k] =  processedByte & 0x1;      //1 -on
    
  
  countOnOffPABsTgrQ_ += PABOnOffTgrD_[k];
  
  if ( PABOnOffTgrQ_[k] != PABOnOffTgrD_[k] ) //SNMP_CM15
  {
    bool bTurnoff = false;
    if ( transmitterLockTgrD___==0 )    //unlock
    {
      if ( PABOnOffTgrD_[k]==0 )    //1 - >  0 turnoff
      {
        bTurnoff=true;
      }
    }
    

    PABOnOffTgrQ_[k]=PABOnOffTgrD_[k];

    if ( ( bTurnoff )&&( initCounter_  >= maxValueInitCounter_ ) )
    {
      hasMsgForSnmp_ = true;
      currentQueryIndex_++;
      if ( kMaxSNMPQueue == currentQueryIndex_ )
      {
        currentQueryIndex_ = 0;
      }
      snmpEventsQuery_[currentQueryIndex_] = ( SNMP_CM15 );
      stringMsgsQuery_[currentQueryIndex_] = "БУМ";
      char chb[10];
      _itoa( ( k+1 ),chb,10 );
      stringMsgsQuery_[currentQueryIndex_] += chb;
      stringMsgsQuery_[currentQueryIndex_] += " - отключен";

      currentMWFCode_ |= kSnmpWarning;
      typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
      typeMsgsQuery_[currentQueryIndex_] = kSnmpWarning;

      //printPowerNeed_=true;
    }
  }
  
  

  PABLockTgrD_[k] =  ( processedByte & 0x2 ) >> 1;    //1 -lock
  lockPABTgrQ_ += PABLockTgrD_[k];
  if ( PABLockTgrQ_[k] != PABLockTgrD_[k] ) //SNMP_CM16
  {
    PABLockTgrQ_[k]=PABLockTgrD_[k];
    if ( ( transmitterLockTgrD___==0 ) )//&&( initCounter_  >= maxValueInitCounter_ ) )    //unlock
    {
      hasMsgForSnmp_ = true;
      currentQueryIndex_++;
      if ( kMaxSNMPQueue == currentQueryIndex_ )
      {
        currentQueryIndex_ = 0;
      }
      snmpEventsQuery_[currentQueryIndex_] = ( SNMP_CM16 );
      stringMsgsQuery_[currentQueryIndex_] = "БУМ";
      char chb[10];
      _itoa( ( k+1 ),chb,10 );
      stringMsgsQuery_[currentQueryIndex_] += chb;
      if ( PABLockTgrQ_[k]==0 )
        stringMsgsQuery_[currentQueryIndex_] += " - отперт";
      else stringMsgsQuery_[currentQueryIndex_] += " - заперт";

      currentMWFCode_ |= kSnmpWarning;
      typesCodesInQuery_[currentQueryIndex_] = currentMWFCode_;
      typeMsgsQuery_[currentQueryIndex_] = kSnmpWarning;

      //printPowerNeed_=true;
    }
    
    
  }
  
  ibPABStatus[k] =  ( processedByte & 0x4 ) >> 2;      //1 - fail
  ibPABAnlgDigit[k] =  ( processedByte & 0x8 ) >> 3;    //1 -dig

  processedByte = ptrSourceArray[1];
  ibPABInPowerStatus[k] =  ( processedByte & 0x1 );  //1 - invalid

  failsTotalD_ += ( uint )ibPABInPowerStatus[k];
  if ( ( 0 != transmitterStatusTgrD_ )&&( ibPABInPowerStatus[k] != 0 )&&( newFailOccure_ ) )
  {
    char chb[10];
    stringMsgsQuery_[fixedAlrmRecordIndex_]+="нет Pвх. БУМ";
    _itoa( ( k+1 ),chb,10 );
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=",";

  }

  
  
  ibPABOutPowerStatus[k] =  ( processedByte & 0x2 ) >> 1;  //1 - invalid < Pmin

  failsTotalD_ += ( uint )ibPABOutPowerStatus[k];
  if ( ( 0 != transmitterStatusTgrD_ )&&( ibPABOutPowerStatus[k] != 0 )&&( newFailOccure_ ) )
  {
    char chb[10];
    stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ Pвых. БУМ";
    _itoa( ( k+1 ),chb,10 );
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=",";

  }

  ibPABFRWStatus[k] =  ( processedByte & 0x4 ) >> 2;    //1 - fail
  failsTotalD_ += ( uint )ibPABFRWStatus[k];
  if ( ( 0 != transmitterStatusTgrD_ )&&( ibPABFRWStatus[k] != 0 )&&( newFailOccure_ ) )
  {
    char chb[10];
    stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ КБВ БУМ";
    _itoa( ( k+1 ),chb,10 );
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=",";

  }

  int n =0; //PA 1
  ibPaMIP[k][n] = ( processedByte & 0x8 ) >> 3;    //1 -fail
  failsTotalD_ += ( uint )ibPaMIP[k][n];
  if ( ( 0 != transmitterStatusTgrD_ )&&( ibPaMIP[k][n] != 0 )&&( newFailOccure_ ) )
  {
    char chb[10];
    stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ МИП БУМ";
    _itoa( ( k+1 ),chb,10 );
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=" УМ";
    _itoa( ( n+1 ),chb,10 );
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=",";

  }

  n =1; //PA 2
  ibPaMIP[k][n] = ( processedByte & 0x10 ) >> 4;    //1 -fail
  failsTotalD_ += ( uint )ibPaMIP[k][n];
  if ( ( 0 != transmitterStatusTgrD_ )&&( ibPaMIP[k][n] != 0 )&&( newFailOccure_ ) )
  {
    char chb[10];
    stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ МИП БУМ";
    _itoa( ( k+1 ),chb,10 );
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=" УМ";
    _itoa( ( n+1 ),chb,10 );
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=",";

  }

  ibPreMIP[k]  = ( processedByte & 0x20 ) >> 5;  //1 -fail
  failsTotalD_ += ( uint )ibPreMIP[k];
  if ( ( 0 != transmitterStatusTgrD_ )&&( ibPreMIP[k] != 0 )&&( newFailOccure_ ) )
  {
    char chb[10];
    stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ МИП УП БУМ";
    _itoa( ( k+1 ),chb,10 );
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=",";

  }

  processedByte = ptrSourceArray[2];
  
  n=0;
  ibPaVT12status[k][n] = processedByte & 0x3;        //1..0;  1 - VT 2, 0 - VT 1
  failsTotalD_ += ( uint )ibPaVT12status[k][n];

  if ( ( 0 != transmitterStatusTgrD_ )&&( ibPaVT12status[k][n] != 0 )&&( newFailOccure_ ) )
  {
    char chb[10];
    if ( ( ibPreVT12status[k]&0x1 )==1 )
    {
      stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ VT1 БУМ";
      _itoa( ( k+1 ),chb,10 );
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=" УМ";
      _itoa( ( n+1 ),chb,10 );
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=",";
    }
    if ( ( ibPaVT12status[k][n]&0x2 )==2 )
    {
      stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ VT2 БУМ";
      _itoa( ( k+1 ),chb,10 );
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=" УМ";
      _itoa( ( n+1 ),chb,10 );
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=",";
    }
  }

  n=1;
  ibPaVT12status[k][n] = ( processedByte & 0xC ) >> 2;        //1..0;  1 - VT 2, 0 - VT 1
  failsTotalD_ += ( uint )ibPaVT12status[k][n];

  if ( ( 0 != transmitterStatusTgrD_ )&&( ibPaVT12status[k][n] != 0 )&&( newFailOccure_ ) )
  {
    char chb[10];
    if ( ( ibPreVT12status[k]&0x1 )==1 )
    {
      stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ VT1 БУМ";
      _itoa( ( k+1 ),chb,10 );
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=" УМ";
      _itoa( ( n+1 ),chb,10 );
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=",";
    }
    if ( ( ibPaVT12status[k][n]&0x2 )==2 )
    {
      stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ VT2 БУМ";
      _itoa( ( k+1 ),chb,10 );
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=" УМ";
      _itoa( ( n+1 ),chb,10 );
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=",";
    }
  }


  ibPreVT12status[k] = ( processedByte & 0x30 ) >> 4;      //1..0;  1 - VT 2, 0 - VT 1
  failsTotalD_ += ( uint )ibPreVT12status[k];

  if ( ( 0 != transmitterStatusTgrD_ )&&( ibPreVT12status[k] != 0 )&&( newFailOccure_ ) )
  {
    char chb[10];
    if ( ( ibPreVT12status[k]&0x1 )==1 )
    {
      stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ УП VT1 БУМ";
      _itoa( ( k+1 ),chb,10 );
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=",";
    }
    if ( ( ibPreVT12status[k]&0x2 )==2 )
    {
      stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ УП VT2 БУМ";
      _itoa( ( k+1 ),chb,10 );
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
      stringMsgsQuery_[fixedAlrmRecordIndex_]+=",";
    }
  }

  processedByte = ptrSourceArray[3];
  ibPreTemStaus[k] = ( processedByte & 0x4 ) >> 2;  //1 -fail
  failsTotalD_ += ( uint )ibPreTemStaus[k];
  if ( ( 0 != transmitterStatusTgrD_ )&&( ibPreTemStaus[k] != 0 )&&( newFailOccure_ ) )
  {
    char chb[10];
    stringMsgsQuery_[fixedAlrmRecordIndex_]+="Отказ Т УП БУМ";
    _itoa( ( k+1 ),chb,10 );
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=chb;
    stringMsgsQuery_[fixedAlrmRecordIndex_]+=",";

  }

  ibPreAGC[k]  = ( processedByte & 0x8 ) >> 3;  //1 -fail

  ibPABInPow[k] =  ptrSourceArray[4];      //uchar
  iPABInPow[k] = ( ibPABInPow[k]&0xf );
  iPABInPow[k]      += ( ( ( ibPABInPow[k]&0xf0 ) >> 4 )*10 );


  ibPABOutPow[k] =  ptrSourceArray[5];
  ibPABOutPow[k]      |=  ( ( ( ( uint )ptrSourceArray[6] )&0xf )<<8 );
  iPABOutPow[k] = ( ibPABOutPow[k]&0xf );
  iPABOutPow[k]      += ( ( ( ibPABOutPow[k]&0xf0 ) >> 4 )*10 );
  iPABOutPow[k]      += ( ( ( ibPABOutPow[k]&0xf00 ) >> 8 )*100 );
  //iPABOutPow[k]      += ( ( ( ibPABOutPow[k]&0xf000 ) >> 12 )*1000 );


  ibPABFRW[k]  =  ptrSourceArray[7];        //uchar
  iPABFRW[k]  = ( ibPABFRW[k]&0xf );  //FRW * 100
  iPABFRW[k]        += ( ( ( ibPABFRW[k]&0xf0 ) >> 4 )*10 );
  
  n=0;
  ibPaMIPVoltage[k][n] = ptrSourceArray[8];
  ibPaMIPVoltage[k][n]  |= ( ( ( uint )ptrSourceArray[10]&0xf )<<8 );
  iPaMIPVoltage[k][n] = ( ibPaMIPVoltage[k][n]&0xf );  //vol *10
  iPaMIPVoltage[k][n]    += ( ( ibPaMIPVoltage[k][n]&0xf0 ) >> 4 )*10;
  iPaMIPVoltage[k][n]    += ( ( ibPaMIPVoltage[k][n]&0xf00 ) >> 8 )*100;

  n=1;
  ibPaMIPVoltage[k][n] = ptrSourceArray[9];
  ibPaMIPVoltage[k][n]  |= ( ( ( uint )ptrSourceArray[10]&0xf0 )<<4 );
  iPaMIPVoltage[k][n] = ( ibPaMIPVoltage[k][n]&0xf );  //vol *10
  iPaMIPVoltage[k][n]    += ( ( ibPaMIPVoltage[k][n]&0xf0 ) >> 4 )*10;
  iPaMIPVoltage[k][n]    += ( ( ibPaMIPVoltage[k][n]&0xf00 ) >> 8 )*100;

  ibPreMIPVoltage[k] = ptrSourceArray[11];
  ibPreMIPVoltage[k]    |= ( ( ( uint )( ptrSourceArray[12]&0xf ) )<<8 );
  iPreMIPVoltage[k] =   ( ibPreMIPVoltage[k]&0xf );  //vol *10
  iPreMIPVoltage[k]    += ( ( ( ibPreMIPVoltage[k]&0xf0 ) >> 4 )*10 );
  iPreMIPVoltage[k]    += ( ( ( ibPreMIPVoltage[k]&0xf00 ) >> 8 )*100 );

  ibPreAttenVoltage[k] = ptrSourceArray[24];
  ibPreAttenVoltage[k]  |= ( ( ( uint )( ptrSourceArray[12]&0xf0 ) )<<4 );
  iPreAttenVoltage[k] =  ( ibPreAttenVoltage[k]&0xf ); //Voltage*10
  iPreAttenVoltage[k]    +=  ( ( ( ibPreAttenVoltage[k]&0xf0 ) >> 4 )*10 );
  iPreAttenVoltage[k]    +=  ( ( ( ibPreAttenVoltage[k]&0xf00 ) >> 8 )*100 );

  ibPrePhaseVoltage[k] = ptrSourceArray[25];
  ibPrePhaseVoltage[k]  |= ( ( ( uint )ptrSourceArray[26] )<<8 );
  iPreAPhaseVoltage[k] = ( ibPrePhaseVoltage[k]&0xf );    //Voltage*100
  iPreAPhaseVoltage[k]  += ( ( ( ibPrePhaseVoltage[k]&0xf0 ) >> 4 )*10 );
  iPreAPhaseVoltage[k]  += ( ( ( ibPrePhaseVoltage[k]&0xf00 ) >> 8 )*100 );
  iPreAPhaseVoltage[k]  += ( ( ( ibPrePhaseVoltage[k]&0xf000 ) >> 12 )*1000 );

  n=0;
  ibPaCurVT1[k][n] = ptrSourceArray[13];
  ibPaCurVT1[k][n]    |= ( ( ( uint )ptrSourceArray[15]&0xf )<<8 );
  iPaCurVT1[k][n] = ( ibPaCurVT1[k][n]&0xf );    //cut*10
  iPaCurVT1[k][n]      += ( ( ibPaCurVT1[k][n]&0xf0 ) >> 4 )*10;
  iPaCurVT1[k][n]      += ( ( ibPaCurVT1[k][n]&0xf00 ) >> 8 )*100;

  ibPaCurVT2[k][n] = ptrSourceArray[14];
  ibPaCurVT2[k][n]    |= ( ( ( uint )ptrSourceArray[15]&0xf0 )<<4 );
  iPaCurVT2[k][n] = ( ibPaCurVT2[k][n]&0xf );    //cur*10
  iPaCurVT2[k][n]      += ( ( ibPaCurVT2[k][n]&0xf0 ) >> 4 )*10;
  iPaCurVT2[k][n]      += ( ( ibPaCurVT2[k][n]&0xf00 ) >> 8 )*100;

  n=1;
  ibPaCurVT1[k][n] = ptrSourceArray[16];
  ibPaCurVT1[k][n]    |= ( ( ( uint )ptrSourceArray[18]&0xf )<<8 );
  iPaCurVT1[k][n] = ( ibPaCurVT1[k][n]&0xf );    //cut*10
  iPaCurVT1[k][n]      += ( ( ibPaCurVT1[k][n]&0xf0 ) >> 4 )*10;
  iPaCurVT1[k][n]      += ( ( ibPaCurVT1[k][n]&0xf00 ) >> 8 )*100;

  ibPaCurVT2[k][n] = ptrSourceArray[17];
  ibPaCurVT2[k][n]    |= ( ( ( uint )ptrSourceArray[18]&0xf0 )<<4 );
  iPaCurVT2[k][n] = ( ibPaCurVT2[k][n]&0xf );    //cur*10
  iPaCurVT2[k][n]      += ( ( ibPaCurVT2[k][n]&0xf0 ) >> 4 )*10;
  iPaCurVT2[k][n]      += ( ( ibPaCurVT2[k][n]&0xf00 ) >> 8 )*100;

  ibPreCurVT1[k] = ptrSourceArray[19];
  iPreCurVT1[k] =   ( ibPreCurVT1[k]&0xf );  //cut*10
  iPreCurVT1[k]      += ( ( ( ibPreCurVT1[k]&0xf0 ) >> 4 )*10 );

  ibPreCurVT2[k] = ptrSourceArray[20];
  iPreCurVT2[k] =   ( ibPreCurVT2[k]&0xf );  //cur*10
  iPreCurVT2[k]      += ( ( ( ibPreCurVT2[k]&0xf0 ) >> 4 )*10 );

  n=0;
  ibPaTemValue[k][n] = ptrSourceArray[21];
  iPaTemValue[k][n] = ( ibPaTemValue[k][n]&0xf );
  iPaTemValue[k][n]    += ( ( ibPaTemValue[k][n]&0xf0 ) >> 4 )*10;

  UpdateMaxTemre( iPaTemValue[k][n],ibPaTemStaus[k][n] );

  n=1;
  ibPaTemValue[k][n] = ptrSourceArray[22];
  iPaTemValue[k][n] = ( ibPaTemValue[k][n]&0xf );
  iPaTemValue[k][n]    += ( ( ibPaTemValue[k][n]&0xf0 ) >> 4 )*10;

  UpdateMaxTemre( iPaTemValue[k][n],ibPaTemStaus[k][n] );

  ibPreTemValue[k] = ptrSourceArray[23];
  iPreTemValue[k] =   ( ibPreTemValue[k]&0xf );
  iPreTemValue[k]      += ( ( ( ibPreTemValue[k]&0xf0 ) >> 4 )*10 );

  UpdateMaxTemre( iPreTemValue[k],ibPreTemStaus[k] );
#endif  // NO_RTS
  //next block
  uchar* repeater_ptr = NULL;
  repeater_ptr = &( ptrSourceArray[27] );
  return repeater_ptr;
}

//
//
//
//protocol version 1
void Sampler::RequestAllParams_v1( uchar* cBuf,int  iBufSize,int &wrote ) {
  wrote = 3;
  if ( iBufSize < wrote ) {
     wrote =-1;
     return;
  }
  cBuf[0]=0xA1; 
  cBuf[1]=0x02;  
  cBuf[2]=( ( ( uint )( kCmdRequest_v1 ) )<<4 )|( kRequestAllParameters_v1 );
};

//
//
//
void Sampler::FillBufferWithCmd_v1(
    uchar* cBuf,
    int  iBufSize,
    int &wrote, 
    uint cmd ) 
  {
  cBuf[0]=0xA0; 
  cBuf[1]=0x2;   
  cBuf[2]=( cmd&0xff );
  wrote = 3;
}

} // namespace tmitter_web_service