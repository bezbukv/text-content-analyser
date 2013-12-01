/**
  file: tvct_lib.js
  
  thinks:
    для всех html файлов проета библ. избыточная, но при кэшировании может быть 
      это не очнь то и важно
      
    ! в подключаемых салбеках не должно быть this
      Because this is so easy to get wrong, limit its use to those places where it is required:
        in constructors
        in methods of objects (including in the creation of closures)
*/
var TR_STS = [ "норма","неисправность","отказ" ];
var TR_OO = [ "отключен","включен" ];
var TR_OO2 = [ "-","отключен","включен" ];
var TR_LC = [ "отперт", "заперт" ];
var TR_CNTMODE = [ "местный","дистанционный" ];
var TR_LOAD = [ "антенна","эквивалент" ];
var TR_AD = [ "аналоговый","цифровой" ];
var TR_RESERV = [ "основной","резервный" ];
var TR_RESERV2 = [ "-","основной","резервный" ];
var TR_CNTREADY = [ "не готов к управлению","готов к управлению" ];
var TR_READY = [ "не готов","готов" ];
var TR_INPSTR = [ "есть","ошибка" ];
var TR_OUTSYNC = [ "есть","нет" ];
var TR_OKFA = [ "норма","отказ" ];
var TR_OKFA2 = [ "норма","не норма" ];
var TR_WRK = [ "не работа","работа" ];
var TR_NET = [ "-","-","-","МЧС","ОЧС" ];
var TR_QAM = [ "-","QPSK","QAM-16","QAM-64" ];
var TR_RATE = [ "-","1/2","2/3","3/4","5/6","7/8" ];
var TR_GI = [ "-","1/4","1/8","1/16","1/32" ];
var TR_HEIR = [ "-","нет","H1","H2","H4" ];
var TR_CARNUM = [ "-","2K","4K","8K" ];
var TR_TEST = [ "-","выключен","Тон","ПСП","ПСП авто","СВИП-2" ];
var TRNAM = [ 
  "АЦТП 1/0,5 аналоговый",
  "АЦТП 2/1 аналоговый",
  "АЦТП 5/2,5 аналоговый",
  "АЦТП 10/5 аналоговый",
  "АЦТП 1/0,5 цифровой",
  "АЦТП 2/1 цифровой",
  "АЦТП 5/2,5 цифровой",
  "АЦТП 10/5 цифровой",
  /*"Нева-1 1000 Вт",
  "Нева-2 2000 Вт",
  "Нева-5 5000 Вт",*/
  
  "РТЦ-1",
  "РТЦ-2",
  "РТЦ-5",
  
  "Нева-10",
  "Нева-0,01",
  "Нева-0,1",
  "Нева-0,2",
  "Нева-0,5",
  "РТЦ-1",
  "РТЦ-2",
  "РТЦ-0,6",
  "Нева-Ц-1,0-Ж 1000 Вт",
  "Нева-Ц-2,5-Ж 2000 Вт",
  "Нева-Ц-5,0-Ж 5000 Вт",
  "АЦТП-Ж 5/2,5 аналоговый" ];
var TYPE_VTV = [ "-","ВТВ без контроля","ВТВ-аналоговый всеканальный",
  "ВТВ DVB-T","ВТВ DVB-T2","ВТВ DVB-T","ВТВ DVB-T2" ];

var MX_NPAB = 12;

var CgiPages = {
  // ВТВ
  ajx_vtvmod1: "/cgi-bin/ajx_vtvmod1.exe",
  ajx_vtvmod2: "/cgi-bin/ajx_vtvmod2.exe",
  ajx_vtv1: "/cgi-bin/ajx_vtv1.exe",
  ajx_vtv2: "/cgi-bin/ajx_vtv2.exe",
  // PowerAmp
  ajx_pab1: "/cgi-bin/ajx_pab1.exe",
  ajx_pab2: "/cgi-bin/ajx_pab2.exe",
  ajx_pab3: "/cgi-bin/ajx_pab3.exe",
  ajx_pab4: "/cgi-bin/ajx_pab4.exe",
  ajx_pab5: "/cgi-bin/ajx_pab5.exe",
  ajx_pab6: "/cgi-bin/ajx_pab6.exe",
  ajx_pab7: "/cgi-bin/ajx_pab7.exe",
  ajx_pab8: "/cgi-bin/ajx_pab8.exe",
  ajx_pab9: "/cgi-bin/ajx_pab9.exe",
  ajx_pab10: "/cgi-bin/ajx_pab10.exe",
  ajx_pab11: "/cgi-bin/ajx_pab11.exe",
  ajx_pab12: "/cgi-bin/ajx_pab12.exe",
  
  // 
  ajx_bcn1: "/cgi-bin/ajx_bcn1.exe",
  ajx_bcn2: "/cgi-bin/ajx_bcn2.exe",
  ajx_bcn: "/cgi-bin/ajx_bcn",
  
  //
  ajx_bd1: "/cgi-bin/ajx_bd1.exe",
  ajx_bd2: "/cgi-bin/ajx_bd2.exe",
  ajx_bd: "/cgi-bin/ajx_bd",
  
  //
  ajx_logstrnum: "/cgi-bin/ajx_logstrnum.exe",
  ajx_log: "/cgi-bin/ajx_log.exe",
  
  //
  ajx_lft: "/cgi-bin/ajx_lft.exe",
  
  //
  ajx_tmn: "/cgi-bin/ajx_tmn.exe",
  ajx_mtrctl:  "/cgi-bin/ajx_mtrctl.exe", //set predcor
  
  //
  ajx_sysonof:  "/cgi-bin/ajx_sysonof.exe",
  ajx_getsys:  "/cgi-bin/ajx_getsys.exe",
  ajx_getnet:  "/cgi-bin/ajx_getnet.exe",
  ajx_setsys:  "/cgi-bin/ajx_setsys.exe",
  ajx_setnet:  "/cgi-bin/ajx_setnet.exe",
  ajx_restrs:  "/cgi-bin/ajx_restrs.exe",
  ajx_passwd:  "/cgi-bin/ajx_passwd.exe",
  frc_rst:  "/cgi-bin/frc_rst.exe"
};

// кандидаты на включение в библиотеку
// по сути не используется
function flash_led( ) {
  //change led
  // on
  if ( pt === 0 ) {
    document.getElementById( "mod_er" ).src=led_mod.src;
    document.getElementById( "mdi_er" ).src=led_mdi.src;
    pt=1;
  }
  //off
  else {
    document.getElementById( "mod_er" ).src=led_of.src;
    document.getElementById( "mdi_er" ).src=led_of.src;
    pt=0;
  }

  window.setTimeout( "flash_led( )",1000 );
}

/*
function showit( ) {
  var bl = document.getElementById( "sys_mod" );
  bl.style.visibility = "visible";
  setTimeout( "hideit( )",1000 );
}

function hideit( ) {
  var bl = document.getElementById( "sys_mod" );
  bl.style.visibility = "hidden";
  setTimeout( "showit( )",500 );
}*/

// всплывающие подсказки
function showHint( id,s ) {
  var sdiv=document.getElementById( id );
  if( s ) sdiv.style.display='block';
  else sdiv.style.display='none';
}

function checkBRNG( ) {
  if ( document.forms[ 0 ].checkauto.checked ) { document.forms[ 0 ].brng.value = "1"; }
  else { document.forms[ 0 ].brng.value = "0"; }
}

/**
  abs.: общее для проекта пространстро имен
    лучше создавать использую библиотеки ( Closure, Dojo,... )
*/
var tvct = (function(){
/** @private */
  function _getStrValue( data, pos ) {
    var cur = data[ pos ].split( "=" );
    var value = cur[ 1 ];
    return value;
  }
    
  function _alertUser() {
    alert( 'private member' );
  }
  
  // to public
  function checkPW( fld ) {
    var inputStr = fld.value;
    for ( var i=0;i<inputStr.length;i++ ) {
      var oneChar = inputStr.charAt( i );
      if ( ( oneChar < "0" || oneChar > "9" ) ) {
        alert( "В это поле должны водиться только десятичные цифры" );
        fld.value="1000";
        return;
      }
    }
    if ( ( parseInt( inputStr,10 )<0 ) || ( parseInt( inputStr,10 )>1000000 ) ) {
      alert( "Установка значения ограничена величиной 1000000" );
      fld.value="1000";
    }
  }
  function isNumber( inputStr ) {
    for ( var i=0;i<inputStr.length;i++ ) {
      var oneChar = inputStr.charAt( i );
      if ( ( oneChar < "0" || oneChar > "9" ) && ( oneChar!="-" ) ) {
        alert( "В это поле должны водиться только десятичные цифры" );
        return false;
      }
    }
    return true;
  }
  function checkHex( fld ) {
    var inputStr = fld.value.toUpperCase( );
    if ( !isHex( inputStr ) ) {
      fld.value = "0";
    }
  }
  function isHex( inputStr ) {
    for ( var i=0;i<inputStr.length;i++ ) {
      var oneChar = inputStr.charAt( i );
      if ( ( oneChar < "0" || oneChar > "9" ) ) {
        if ( ( oneChar < "A" || oneChar > "F" ) ) {
          alert( "В это поле должны водиться только шестнадцатеричные цифры" );
          return false;
        }
      }
    }
    return true;
  }

/** @public  */
  return {
    // Ajax запросы и ответы
    getXMLHttpRequest: function() {
      var result = null;
      if ( window.XMLHttpRequest ) {
        try {
          result = new XMLHttpRequest( );
        }
        catch ( e ){}
      } else if ( window.ActiveXObject ) {
        try {
          result = new ActiveXObject( 'Msxml2.XMLHTTP' );
        }
        catch ( e ) {
          try {
            result = new ActiveXObject( 'Microsoft.XMLHTTP' );
          }
          catch ( e ){
            // alert( 'Caught Exception: ' + e.description );
            // В связи с багом XMLHttpRequest в Firefox приходится отлавливать ошибку
            // Bugzilla Bug 238559 XMLHttpRequest needs a way to report networking errors
            // https://bugzilla.mozilla.org/show_bug.cgi?id=238559
            alert( "Exception : tvct-lib.js file. Create Ajax object." );
          }
        }
      }
      return result;
    },
    
    // рисует кнопки
    buttonHovers: function ( namesButtons, imagesPath ) {
      preloads = new Object( );
      preloads[ 0 ] = new Image( ); preloads[ 0 ].src = imagesPath + "button_left_xon.gif";
      preloads[ 1 ] = new Image( ); preloads[ 1 ].src = imagesPath + "button_right_xon.gif";

      var buttons = [];
      var i;
      for ( i = 0; i < namesButtons.length; i++ ) {
        buttons[ i ] = document.getElementById( namesButtons[ i ] );
      }

      for ( i = 0; i < buttons.length; i++ ) {
        buttons[ i ].className = "buttonSubmit";
        
        var buttonLeft = document.createElement( 'img' );
        buttonLeft.src = imagesPath + "button_left.gif";
        buttonLeft.className = "buttonImg";
        buttons[ i ].parentNode.insertBefore( buttonLeft, buttons[ i ] );
        
        var buttonRight = document.createElement( 'img' );
        buttonRight.src = imagesPath + "button_right.gif";
        buttonRight.className = "buttonImg";
        if( buttons[ i ].nextSibling ) {
          buttons[ i ].parentNode.insertBefore( buttonRight, buttons[ i ].nextSibling );
        } else {
          buttons[ i ].parentNode.appendChild( buttonRight );
        }
        buttons[ i ].onmouseover = function( ) {
          this.className += "Hovered";
          this.previousSibling.src = imagesPath + "button_left_xon.gif";
          this.nextSibling.src = imagesPath + "button_right_xon.gif";
        };
        buttons[ i ].onmouseout = function( ) {
          this.className = this.className.replace( /Hovered/g, "" );
          this.previousSibling.src = imagesPath + "button_left.gif";
          this.nextSibling.src = imagesPath + "button_right.gif";
        };
      }
    },
    
    // получает массив значений типа [ name1=3, name2=,..] и возвращает значение поля по индексу
    getStrValue: function( data, pos ) {
      return _getStrValue( data, pos );
    },
    getIntValue: function( data, pos ) {
      var strValue = _getStrValue( data, pos );
      var intValue = parseInt( strValue, 10 );
      return intValue;
    }
  };
})();

/**
  thinks: 
    вот c этим нужно хорошо разобраться
    ! вызовы происходят путано !
    
  nfo: 
  
  use-cases :
    1. посторный опрос - запрос по таймеру
    2. разовый запрос но с попыткаи установки связи

*/
function bind(func, context) {
  return function() { 
    return func.apply(context, arguments); 
  };
}

function AjaxObj() { 
  // методы создаются в конструкторе
/** @private */
  // virtuals
  var self = this;  // сохранить this в замыкании
  
  // тестовый вывод
  var _mainContainerId = 'main';
  var _container = 'vtv';
  
  // 
  var _ajaxRequestGet = tvct.getXMLHttpRequest();
    var _urlToGet = '';
  var _ajaxRequestPost = tvct.getXMLHttpRequest();
    var _postPage = "";
  var _numTriesConnect = 0;  // только ответ сбрасывает в ноль
  var MAX_TRIES = 4;
  var _sendEna = true;  // Разрешение посылать запросы. Сперва разблокировано
  
  // можно их замкнуть
  var _appendToPanel = function ( what ) { $('#'+_container+'').append( what + '<br>' ); };
  var _addPanel = function() { 
    $('#'+_mainContainerId+'').append( '<p>Name:'+_container+'</p>'); 
    $('#'+_mainContainerId+'').append( '<div style="background: #E0FFE0;" id="'+_container+'"></div>'); 
  };
  
  //
  var _makeUrl = function ( page ) {
    var result = page+"?"+"r="+Math.random( );
    return result;
  };
  
  // processing GET change state
  var _processReqChange = function( ) {
    try {
      if ( _ajaxRequestGet.readyState == 4 ) {
        if ( _ajaxRequestGet.status == 200 ) {
          // плохо что два, но пусть пока так
          _numTriesConnect = 0;
          
          // запрещаем запросы
          _sendEna = false;
          
          //
          var result = _ajaxRequestGet.responseText;
          self.update( result );
        } else {
          // прочие коды ответа http-сервера
          self.noOkHttpCode( _ajaxRequestGet.status );
        }
      }
    }
    catch( e ) { 
      alert( "Exception : tvct-lib.js file. Ajax GET-response processing." );
    }
  };
  
  var _sendGetRequest = function () {
    _ajaxRequestGet = tvct.getXMLHttpRequest();
    if ( _ajaxRequestGet  ) {
      // создаем Url
      var url = _makeUrl( _urlToGet );

      // запрашиваем
      _ajaxRequestGet.open( "GET", url, true );
      _ajaxRequestGet.onreadystatechange = _processReqChange;
      _ajaxRequestGet.send( null );
    } else {
      alert( "Ошибка при создании XMLHttpRequest объекта" );
    }
    
    _numTriesConnect = _numTriesConnect+1;
    if ( _numTriesConnect >= MAX_TRIES ) {
      self.triesIsOver( );
    }
  };
  
  // processing POST change state
  var _processPostReqChange = function ( ) {
    try {
      if ( _ajaxRequestPost.readyState == 4 ) {
        if ( _ajaxRequestPost.status == 200 ) {
          var result = _ajaxRequestPost.responseText;
          self.splitRequest( result );
        } else {
          alert( "Не удалось выполнить" );
        }
      }
    }
    catch( e ) { alert( "Exception : tvct-lib.js file. Ajax POST-response processing." ); }
  };
  
  var _repeatIfNeed = function () {
    if ( _numTriesConnect > 0 ) {
      _sendGetRequest();
      
    }
  };
  
/** @public */
  // Virtual 
  // for GET
  this.update = function ( gettedStr ) {};  // обрабатывает полученный пакет
  this.noOkHttpCode = function ( codeState ) {
    /* прочие ответы http-сервера */
  };  // действия в случае ошибки ajax ( 4 and not 200 )
  // for POST
  this.triesIsOver = function () { };
  this.splitRequest = function ( gettedStr ) {};  
  
  // No virtual
  this.setMainContainerId = function ( id ) { _mainContainerId = id; };
  this.setContainerId = function ( id ) { _container = id; };
  
  // Предварительный просмотр
  this.preView = function ( gettedStr ) {
    var gettedDataPkg = gettedStr.split( "&" );
    _addPanel();
    var len = gettedDataPkg.length;
    for( var i = 0; i < len ; i++ ) {
      var oneElement = gettedDataPkg[ i ].split( "=" );
      _appendToPanel( oneElement[ 0 ]+': '+i+ ', // '+oneElement[ 1 ]+' ['+i+']' );
    }
    _appendToPanel( '' );
  };
  
  // обобщенная обработка
  this.processing = function ( gettedDataPkg, callbacksMap ) {
    // !! не гарантирован порядок обхода
    for( var key in callbacksMap ) if ( callbacksMap.hasOwnProperty( key ) ) {
      callbacksMap[ key ]( gettedDataPkg );
    }
  };

  // сделать GET-запрос
  // первый запрос серии
  this.getData = function( page ) {
    if ( _sendEna ) {
      _urlToGet = page;
      _sendGetRequest();
    }
  };

  // раздельный запрос - установка url отдельно, вызов - отдельно
  this.setPage = function ( url ) {
    _urlToGet = url;
  };
  this.doGetRequest = function(  ) {
    this.getData( _urlToGet );
  };

  // сдалать POST-запрос - no tested
  this.postData = function ( val, page ) {
    _ajaxRequestPost = tvct.getXMLHttpRequest();
    if ( _ajaxRequestPost ) {
      _ajaxRequestPost.open( "POST", page, true );
      try {
        _ajaxRequestPost.setRequestHeader( "Content-Type", "application/x-www-form-urlencoded" );
      }
      catch( e ){ 
        alert("Exception : tvct-lib.js file. Ajax POST-request send.");
      }

      var params;
      params = "cmd="+val.toString( );
      _ajaxRequestPost.onreadystatechange = _processPostReqChange;
      _ajaxRequestPost.send( params );
    }
  };
}

// pseudo for lib calls
var _getStrValue = tvct.getStrValue;
var _getIntValue = tvct.getIntValue;
  
// Const
var KV2 = 76;  // двухкиловатный

// получить метку времени
// to lib
// src : Mon Oct 08 2012 15:13:30 GMT+0400 (Московское время (лето))
function getTimeStamp() {
  /*var MonthMap = {
    Jan : "Января",  Feb : "Февраля", Mar : "Марта", Apr : "Апреля",
    May : "Мая", Jun : "Июня", Jul : "Июля", Aug : "Августа",
    Sep : "Сентября", Oct : "Октября", Nov : "Ноября", Dec : "Декабря"
  };*/
  var MonthMap = {
    0 : "Января",  1 : "Февраля", 2 : "Марта", 3 : "Апреля",
    4 : "Мая", 5 : "Июня", 6 : "Июля", 7 : "Августа",
    8 : "Сентября", 9 : "Октября", 10 : "Ноября", 11 : "Декабря"
  };

  var timestamp = new Date();
  var mins = '0'+timestamp.getUTCMinutes();  // 0xx или 0x
  var secs = '0'+timestamp.getUTCSeconds();  // 0xx или 0x
  var timeDay = (timestamp.getUTCHours()+4)+':'+
    mins[ mins.length-2]+mins[ mins.length-1]+':'+
    secs[ secs.length-2]+secs[ secs.length-1];
  var summary = [];
  summary.push( timeDay );  // время дня
  summary.push( timestamp.getUTCDate() );  // число
  summary.push( MonthMap[ timestamp.getUTCMonth() ] );  // месяц
  summary.push( timestamp.getUTCFullYear() );
  var result = summary.join( ' ' );
  return result;
}

function yel_fon(cur,curinput) {
  if (parseInt(cur[1],10)==1) curinput.style.backgroundColor="#ffff00";
  else curinput.style.backgroundColor="#FAFAFA";
}
function red_fon(cur,curinput) {
  if (parseInt(cur[1],10)==1)  curinput.style.backgroundColor="#ff3500";
  else curinput.style.backgroundColor="#FAFAFA";
}

function yel_green(cur,curinput) { 
  if (parseInt(cur[1],10)==1) curinput.style.backgroundColor="#ffff00";
  else curinput.style.backgroundColor="#33cc00";
}

function red_green(cur,curinput) {
  if (parseInt(cur[1],10)==1)  curinput.style.backgroundColor="#ff3500";
  else curinput.style.backgroundColor="#33cc00";
}

function red_green_bound(curinput,bh,bl) {
  if ((curinput.value <= bh)&&(curinput.value >= bl)) {
    curinput.style.backgroundColor="#33cc00";
  } else {
    curinput.style.backgroundColor="#ff0000";
  }
}

function red_fon_bound(curinput,bh,bl) {
  if ((curinput.value <= bh)&&(curinput.value >= bl)) {
    curinput.style.backgroundColor="#FAFAFA";
  } else {
    curinput.style.backgroundColor="#ff0000";
  }
}

function show_pa(objd) {
  var compstyle;
  var dst;
  if (objd.currentStyle) dst = objd.currentStyle.display;
  else {
     compstyle = document.defaultView.getComputedStyle(objd,"");
     dst = compstyle.getPropertyValue("display");
  }
  //objd.style.display = "block";
}

function show_r(cur_id,vw) {  

  var objd = document.getElementById(cur_id);
  //var objd = cur_span;
  var compstyle;
  var dst;
  if (objd.currentStyle) dst = objd.currentStyle.display;
  else {
    compstyle = document.defaultView.getComputedStyle(objd,"");
    dst = compstyle.getPropertyValue("display");
  }
  
  if(vw == 0) {
    objd.style.display = "none";
  }
  else //if(vw == 1)
  {
    objd.style.display = "block";
  }
  
}



