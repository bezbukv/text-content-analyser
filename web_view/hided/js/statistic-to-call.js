/**
  TODO : 
    Закрыть пространостом имен
*/
// Name - 
var _styleTD = 'bgcolor="#ffffff" style="border-style:solid; border-width:1px;margin:0px; border-color:#DEDEDE;"';
var cell25 = function (content) {
  return '<td align="center" width="35%" '+_styleTD+' >'+content+'</td>\r\n';
};
var idCell25 = function (id) {
  return '<td id="'+id+'" align="center" width="15%"  '+_styleTD+' ></td>\r\n';
};

var cellAny = function (content, proc) {
  return '<td align="center" width="'+proc+'%"  '+_styleTD+' >'+content+'</td>\r\n';
};
var cellOther = function (content) {
  return '<td align="center"  '+_styleTD+' >'+content+'</td>\r\n';
};

var idCellAny = function (id, proc) {
  return '<td id="'+id+'" align="center" width="'+proc+'%"  '+_styleTD+' ></td>\r\n';
};
var idCellOther = function (id) {
  return '<td id="'+id+'" align="center" '+_styleTD+' ></td>\r\n';
};
var fillRow = function(names, call3) {
  var result;
  result = '<tr style="margin:0px">\r\n'
  result += cell25(names[0]);
  result += idCell25(names[1]);
  result += call3(names[2]);
  result += idCell25(names[3]);
  result += '</tr>\r\n';
  return result;
};  

var fillRow4Any = function(names, procs) {
  var result;
  result = '<tr style="margin:0px">\r\n'
  result += cellAny(names[0], procs[0]);
  result += idCellAny(names[1], procs[1]);
  result += cellAny(names[2], procs[2]);
  result += idCellAny(names[3], procs[3]);
  result += '</tr>\r\n';
  return result;
};
var fillState4Any = function(names) {
  var idCellAny = function (id, proc) {
    return '<td id="'+id+'" align="center" width="'+proc+'%"  ></td>\r\n';
  };
  var result = '';
  var procs = [25,25,25,25];
  result += _tableHeadSpacedStateNoGrid;
  result += '<tr style="margin:0px">\r\n'
  result += '<td align="center" width="'+procs[0]+'%"  ><b>'+names[0]+'&nbsp;&nbsp;</b></td>\r\n'; 
  result += idCellAny(names[1], procs[1]);
  result += idCellAny(names[2], procs[2]);
  result += idCellAny(names[3], procs[3]);
  result += '</tr>\r\n';
  result += _tableBottom;
  return result;
};
var putHeadSubTable = function(head) {
  var result = '';
  result += _tableHeadSpacedStateNoGrid;
  result += '<tr style="margin:0px">\r\n'
  result += '<td colspan="4" align="left" width="'+100+'%"><b>&nbsp;&nbsp;'+head+':'+'&nbsp;&nbsp;</b></td>\r\n'; 
  result += '</tr>\r\n';
  result += _tableBottom;
  return result;
}

var fillNamedRow4Any = function(names, procs, id) {
  var result;
  result = '<tr style="margin:0px" id="'+id+'">\r\n'
  result += cellAny(names[0], procs[0]);
  result += idCellAny(names[1], procs[1]);
  result += cellAny(names[2], procs[2]);
  result += idCellAny(names[3], procs[3]);
  result += '</tr>\r\n';
  return result;
};

var fillLongRow = function(names, doFirst) {
  var result;
  result = '<tr style="margin:0px">\r\n'
  if (doFirst)
    result += cellAny(names[0],5);
  result += cellAny(names[1],12);
  result += idCellAny(names[2],7);
  result += cellAny(names[3],12);
  result += idCellAny(names[4],7);
  result += cellAny(names[5],20);
  result += idCellAny(names[6],12);
  result += cellAny(names[7],17);
  result += idCellAny(names[8],5);
  result += '</tr>\r\n';
  return result;
};  

var do4ColsInRow = function () {

}

var fillNamedLongRow = function(names, doFirst, id) {
  var result;
  result = '<tr style="margin:0px" id="'+id+'">\r\n'
  if (doFirst)
    result += cellAny(names[0],5);
  result += cellAny(names[1],12);
  result += idCellAny(names[2],7);
  result += cellAny(names[3],12);
  result += idCellAny(names[4],7);
  result += cellAny(names[5],25);
  result += idCellAny(names[6],7);
  result += cellAny(names[7],20);
  result += idCellAny(names[8],5);
  result += '</tr>\r\n';
  return result;
};


var fill4NamedRow = function(names, id) {
  var result;
  result = '<tr style="margin:0px" id="'+id+'">\r\n'
  result += cell25(names[0]);
  result += cell25(names[1]);
  result += cell25(names[2]);
  result += cell25(names[3]);
  result += '</tr>\r\n';
  return result;
};
var _tableHead = '<table width="100%" cellpadding="3" cellspacing="1" style="padding:0px;border-collapse: collapse;" >';
var _tableHeadSpaced = '<table width="100%" cellpadding="3" cellspacing="1" style="padding:0px;border-collapse: collapse;margin-bottom:8px;" >';
var _tableHeadSpacedState = '<table width="60%" align="left" cellpadding="3" cellspacing="1" style="padding:0px;border-collapse: collapse;margin-bottom:8px;" >';
var _tableHeadSpacedStateNoGrid = '<table width="60%" align="left" style="padding:0px;border-collapse: collapse;margin-bottom:8px;" >';
var _tableHeadNoId = '<table width="100%" cellpadding="3" cellspacing="1" style="padding:0px;border-collapse: collapse;" ';
var _tableBottom = '</table>';
function doHead(textHead) {
  return '<p style="margin-bottom:5px" ><b>'+textHead+'</b></p>'
}

//
function _appendToLeftPanel(what) { $('#leftData').append(what); }
function _addLeftPanel() {
  $('#main').append('<div id="leftData"></div>'); // style="background : #E0FFE0;"
}
function _addToMainPanel(subPanelName) {
  $('#main').append('<div style="background : #E0FFE0;" id="'+subPanelName+'"></div>');
}
function _addSubPanelToMainPanel(subPanelName) {
  $('#main').append('<div style="background : #E0FFE0;" id="'+subPanelName+'"></div>');
}
function _addAnyToMainPanel(valueInnerHtml) {  $('#main').append(valueInnerHtml);}
function _addToSubPanel(subPanelName, valueInnerHtml) { 
  $('#'+subPanelName).append(valueInnerHtml+'&nbsp;&nbsp');
}
var map_ajx_lft = {
  NPow: function (gettedData) {}, // 5000 [0]
  NuVt: function (gettedData) {}, // 2 [1]
  NuAB: function (gettedData) {}, // 9 [2]
  NuPA: function (gettedData) {}, // 4 [3]
  NuBC: function (gettedData) {}, // 1 [4]
  NuDB: function (gettedData) {}, // 1 [5]
  TrNa: function (gettedData) {
    // имя передатчика
    var trKeyName = _getIntValue(gettedData, 6)-1;
    if (trKeyName == KV2) { 
      _appendToLeftPanel("<center>Назвение передатчика : <b>"+"РТЦ-2 2000 Вт"+'</b></center>'); 
    }
    else { 
      _appendToLeftPanel("<center>Назвение передатчика : <b>"+TRNAM[ trKeyName ]+'</b></center>'); 
    }
  }, // 11 [6]
  VtTy: function (gettedData) {
    var trKeyName = _getIntValue(gettedData, 6)-1;
    // тип возбудителя
    var key = _getIntValue(gettedData, 7);
    
    // 2000W или нет
    var stylePrint = '';
    var type = '';
    if (trKeyName === 76) type = TYPE_VTV[3];
    else type = TYPE_VTV[ key ];
    _appendToLeftPanel("<center>Тип возбудителя : <b "+stylePrint+">"+type+'</b></center>');
  }, // 3 [7]
  Chan: function (gettedData) {
    var key = _getIntValue(gettedData, 8);
    _appendToLeftPanel("<br><center>Номер канала : <b >"+key+'</b></center>');

  }, // 67 [8]
  _Pow: function (gettedData) {
    var key = _getIntValue(gettedData, 9);
    _appendToLeftPanel("<center>Выходная мощность : <b >"+key+'</b></center>');
  }, // 0 [9]
  VFRW: function (gettedData) {
    var key = _getStrValue(gettedData, 10);
    _appendToLeftPanel("<center>КБВ : <b >"+key+'</b></center>');
  }, // 0.00 [10]
  TrOO: function (gettedData) {
    // включен/выключен
    var key = _getIntValue(gettedData, 11);
    var keyLock = _getIntValue(gettedData, 12);
    _appendToLeftPanel("<center>Передатчик : <b>"+TR_OO[ key ]+'</b> и <b>'+TR_LC[ keyLock ]+'</b></center>');
  }, // 0 [11]
  TrLc: function (gettedData) {
    // отперт/заперт, но обрабатывается не здесь
  }, // 1 [12]
  Stas: function (gettedData) {
    // статус передатчика
    var key = _getIntValue(gettedData, 13);
    var stylePrint = '';//"style='background-color:red;'"
    _appendToLeftPanel("<center>Состояние : <b "+stylePrint+">"+TR_STS[ key ]+'</b></center>');
  }, // 0 [13]
  MaxT: function (gettedData) {
    var key = _getStrValue(gettedData, 14);
    _appendToLeftPanel("<center>Максимальная Т, &deg;C : <b >"+key+'</b></center>');
  }, // 0 [14]
  MaxS: function (gettedData) {}, // 0 [15]
  _FRW: function (gettedData) {}, // 0 [16]
  conn: function (gettedData) {}, // 1 [17]
  cn_snmp: function (gettedData) {}, // 0 [18]
  iFRWLow: function (gettedData) {}, // 60 [19]
  iFRWMax: function (gettedData) {}, // 100 [20]
  iPowLow: function (gettedData) {}, // 10 [21]
  iPowMax: function (gettedData) {}, // 10 [22]
  iTemLow: function (gettedData) {}, // 0 [23]
  iTemMax: function (gettedData) {}, // 80 [24]
  is0: function (gettedData) {}, // [25]
  is1: function (gettedData) {}, // [26]
}

// Name - 
var map_ajx_tmn = {
  nonamed : function (gettedData) {}, // undefined [0]
  CnMo: function (gettedData) {}, // 0 [1]
  Load: function (gettedData) {}, // 1 [2]
  AnDi: function (gettedData) {}, // 1 [3]
  Work: function (gettedData) {}, // 0 [4]
  VtLc: function (gettedData) {}, // 0 [5]
  PALc: function (gettedData) {}, // 0 [6]
  TrLc: function (gettedData) {}, // 0 [7]
  TrOO: function (gettedData) {}, // 1 [8]
  NuVt: function (gettedData) {}, // 2 [9]
  NuAB: function (gettedData) {}, // 9 [10]
  NuPA: function (gettedData) {}, // 4 [11]
  NuBC: function (gettedData) {}, // 1 [12]
  NuDB: function (gettedData) {}, // 1 [13]
  V12O: function (gettedData) {}, // 2 [14]
  RadM: function (gettedData) {}, // 0 [15]
  Redy: function (gettedData) {}, // 1 [16]
  InSt: function (gettedData) {}, // 1 [17]
  OuSy: function (gettedData) {}, // 0 [18]
  Stas: function (gettedData) {}, // 0 [19]
  _Net: function (gettedData) {}, // 0 [20]
  Blst: function (gettedData) {}, // 0 [21]
  _FRW: function (gettedData) {}, // 0 [22]
  Cool: function (gettedData) {}, // 0 [23]
  _PAB: function (gettedData) {}, // 0 [24]
  R485: function (gettedData) {}, // 0 [25]
  _I2C: function (gettedData) {}, // 0 [26]
  iVtv: function (gettedData) {}, // 0 [27]
  Chan: function (gettedData) {}, // 67 [28]
  _Pow: function (gettedData) {}, // 0 [29]
  VFRW: function (gettedData) {}, // 0.00 [30]
  MaxT: function (gettedData) {}, // 0 [31]
  MaxS: function (gettedData) {}, // 0 [32]
  conn: function (gettedData) {}, // 1 [33]
  NPow: function (gettedData) {}, // 5000 [34]
  iFRWLow: function (gettedData) {}, // 60 [35]
  iFRWMax: function (gettedData) {}, // 100 [36]
  iPowLow: function (gettedData) {}, // 10 [37]
  iPowMax: function (gettedData) {}, // 10 [38]
  iTemLow: function (gettedData) {}, // 0 [39]
  iTemMax: function (gettedData) {}, // 80 [40]
  Bcvi: function (gettedData) {}, // 0 [41]
  Bdi: function (gettedData) {}, // 0 [42]
  TrNa: function (gettedData) {}, // 11 [43]
  SOOi: function (gettedData) {}, // 0 [44]
  Overi: function (gettedData) {}, // 0 [45]
  VTni: function (gettedData) {}, // 0 [46]
  VTni: function (gettedData) {}, // 0 [47]
  pBal: function (gettedData) {}, // 0 [48]
  pwH: function (gettedData) {}, // 0 [49]
}
// Name - 
var map_ajx_vtv = {
  nonamed : function (gettedData, number) {}, // undefined [0]
  conn: function (gettedData, number) {
    var cur = gettedData[3].split("=");
    document.getElementById("vtv"+number+"_ctl").innerHTML = TR_CNTMODE[parseInt(cur[1],10)];
    
    cur = gettedData[4].split("=");
    //document.getElementById("vtv"+number+"_reserv").innerHTML = TR_RESERV[parseInt(cur[1],10)];
    
    cur = gettedData[5].split("=");
    //document.getElementById("vtv"+number+"_da").innerHTML = TR_AD[parseInt(cur[1],10)];
    
    cur = gettedData[6].split("=");
    document.getElementById("vtv"+number+"_pa_lck").innerHTML = TR_LC[parseInt(cur[1],10)];
    
    cur = gettedData[7].split("=");
    document.getElementById("vtv"+number+"_mod_lck").innerHTML = TR_LC[parseInt(cur[1],10)];
    
    cur = gettedData[8].split("=");
    document.getElementById("vtv"+number+"_lck").innerHTML = TR_LC[parseInt(cur[1],10)]+'&nbsp;&nbsp;&nbsp;';
    cur = gettedData[9].split("=");
    document.getElementById("vtv"+number+"_ready").innerHTML = TR_READY[parseInt(cur[1],10)]+'&nbsp;&nbsp;&nbsp;';
    cur = gettedData[11].split("=");
    var curinput = document.getElementById("vtv"+number+"_sts");
    curinput.innerHTML = TR_STS[parseInt(cur[1],10)]+'&nbsp;&nbsp;&nbsp;';
    
    //if (parseInt(cur[1],10)==1) curinput.style.backgroundColor="#ffff00";
    //else if (parseInt(cur[1],10)==2) curinput.style.backgroundColor="#ff0000";
    //else curinput.style.backgroundColor="#FAFAFA";
    
    cur = gettedData[10].split("=");
    //document.getElementById("vtv"+number+"_work").innerHTML = TR_WRK[parseInt(cur[1],10)];
    
    cur = gettedData[12].split("=");
    curinput = document.getElementById("vtv"+number+"_mod_st");
    curinput.innerHTML = TR_OKFA[parseInt(cur[1],10)];
    //red_green(cur,curinput);
    
    cur = gettedData[13].split("=");
    curinput = document.getElementById("vtv"+number+"_pa_st");
    curinput.innerHTML = TR_OKFA[parseInt(cur[1],10)];
    //red_green(cur,curinput);
    
    cur = gettedData[14].split("=");
    curinput = document.getElementById("vtv"+number+"_instr");
    curinput.innerHTML = TR_OKFA[parseInt(cur[1],10)];
    //yel_fon(cur,curinput);
    
    cur = gettedData[15].split("=");
    curinput = document.getElementById("vtv"+number+"_outsync");
    curinput.innerHTML = TR_OKFA[parseInt(cur[1],10)];
    //yel_fon(cur,curinput);
    
    cur = gettedData[16].split("=");
    //document.getElementById("vtv"+number+"_ins_err").innerHTML = cur[1];
    
    cur = gettedData[17].split("=");
    document.getElementById("vtv"+number+"_mod_err").innerHTML = cur[1];
    
    cur = gettedData[18].split("=");
    document.getElementById("vtv"+number+"_outpow").innerHTML = cur[1];
    
    //cur = gettedData[19].split("=");
    //curinput = document.getElementById("mod"+number+"_ref");
    //curinput.setAttribute('href',"http://"+cur[1]);
  }, // 1 [1]
  Trn: function (gettedData, number) {}, // 11 [2]
  VCtm: function (gettedData, number) {}, // 0 [3]
  VRdm: function (gettedData, number) {}, // 0 [4]
  VADm: function (gettedData, number) {}, // 0 [5]
  UVLc: function (gettedData, number) {}, // 0 [6]
  MVLc: function (gettedData, number) {}, // 0 [7]
  VLck: function (gettedData, number) {}, // 0 [8]
  VRdy: function (gettedData, number) {}, // 0 [9]
  VWrk: function (gettedData, number) {}, // 0 [10]
  VtSt: function (gettedData, number) {}, // 0 [11]
  VMSt: function (gettedData, number) {}, // 0 [12]
  VUSt: function (gettedData, number) {}, // 0 [13]
  VtIn: function (gettedData, number) {}, // 0 [14]
  VOSy: function (gettedData, number) {}, // 0 [15]
  VErr: function (gettedData, number) {}, // 00 [16]
  VMEr: function (gettedData, number) {}, // 00 [17]
  VOPw: function (gettedData, number) {}, // 0 [18]
  VVIP: function (gettedData, number) {}, // 172.16.1.77-8082 [19]
}

function getVtvParHtmlCode(number) {
  var _makeTable = function () {
    // Сам процесс вычисления
    var names;
    
    var result = '';
    var mainParam = [['Состояние:', 'vtv'+number+'_lck', 'vtv'+number+'_sts','vtv'+number+'_ready']];
    result += fillState4Any(mainParam[0]);
    
    result += putHeadSubTable('Основные параметры');
        
    result += _tableHeadSpaced;
    result += '<tr><td></td></tr>'
    mainParam = [
      ["Режим управления", "vtv"+number+"_ctl", "Входной поток", "vtv"+number+"_instr"],
      ["Внешняя синхронизация", "vtv"+number+"_outsync","Выходная мощность, мВт", "vtv"+number+"_outpow" ]];
    for (var i = 0; i < mainParam.length; i++) {
      result += fillRow(mainParam[i], cell25);
    }
    result += _tableBottom;
    
    result += _tableHeadSpacedState;
    mainParam = [["Усилитель:", "vtv"+number+"_pa_lck", "vtv"+number+"_pa_st", "fix0"]];
    result += fillState4Any(mainParam[0]);
    result += _tableBottom;
    
    result += _tableHeadSpacedState;
    mainParam = [["Модулятор:", "vtv"+number+"_mod_lck", "vtv"+number+"_mod_st", "fix1"]];
    
    var fillState4Any_Mod = function(names) {
      var idCellAny = function (id, proc) {
        return '<td style="display:none" id="'+id+'" align="center" width="'+proc+'%"  ></td>\r\n';
      };
      var result = '';
      var procs = [25,25,25,25];
      var kTableHeadSpacedStateNoGrid = '<table width="60%" align="left" style="padding:0px;border-collapse: collapse;margin-bottom:8px;" >';
      result += kTableHeadSpacedStateNoGrid;
      result += '<tr style="margin:0px; display:none">\r\n'
      result += '<td style="display:none" align="center" width="'+procs[0]+'%"  ><b>'+
        names[0]+'&nbsp;&nbsp;</b></td>\r\n'; 
      result += idCellAny(names[1], procs[1]);
      result += idCellAny(names[2], procs[2]);
      result += idCellAny(names[3], procs[3]);
      result += '</tr>\r\n';
      result += _tableBottom;
      return result;
    };
    
    result += fillState4Any_Mod(mainParam[0]);
    result += _tableBottom;
    
    // Код отказа модулятора
    var _tableHeadSpaced1 = '<table width="50%" align="left" cellpadding="3"'+
      ' cellspacing="1" style="padding:0px;border-collapse: collapse;margin-bottom:8px;" >';
    result += _tableHeadSpaced1;
    var fillRow1 = function(names) {
      var result;
      result = '<tr style="margin:0px; ">\r\n'
      result += cellAny(names[0], 35);
      result += idCellAny(names[1], 15);
      result += '</tr>\r\n';
      return result;
    };  

    result += fillRow1(["Код отказа модулятора", "vtv"+number+"_mod_err","",""]);
    result += _tableBottom;
    
    return result;
  };


  var result = ''+_makeTable();
  return result;
}

// Name - 
var map_ajx_vtvmod = {
  nonamed : function (gettedData, number) {}, // undefined [0]
  conn: function (gettedData, number) {
    var cur = gettedData[2].split("=");
    document.getElementById("vtv"+number+"_net").innerHTML = TR_NET[parseInt(cur[1],10)];
    
    cur = gettedData[3].split("=");
    document.getElementById("vtv"+number+"_dvbon").innerHTML = TR_OO2[parseInt(cur[1],10)];
    
    cur = gettedData[4].split("=");
    document.getElementById("vtv"+number+"_mipon").innerHTML = TR_OO2[parseInt(cur[1],10)];
    
    cur = gettedData[5].split("=");
    document.getElementById("vtv"+number+"_asi1").innerHTML = TR_RESERV2[parseInt(cur[1],10)];
    
    cur = gettedData[6].split("=");
    document.getElementById("vtv"+number+"_asi2").innerHTML = TR_RESERV2[parseInt(cur[1],10)];
    
    cur = gettedData[7].split("=");
    document.getElementById("vtv"+number+"_qam").innerHTML = TR_QAM[parseInt(cur[1],10)];
    
    cur = gettedData[8].split("=");
    document.getElementById("vtv"+number+"_rate").innerHTML = TR_RATE[parseInt(cur[1],10)];
    
    cur = gettedData[9].split("=");
    document.getElementById("vtv"+number+"_gi").innerHTML = TR_GI[parseInt(cur[1],10)];
    
    cur = gettedData[10].split("=");
    document.getElementById("vtv"+number+"_heir").innerHTML = TR_HEIR[parseInt(cur[1],10)];
    
    cur = gettedData[11].split("=");
    document.getElementById("vtv"+number+"_carnum").innerHTML = TR_CARNUM[parseInt(cur[1],10)];
    
    cur = gettedData[12].split("=");
    document.getElementById("vtv"+number+"_chnum").innerHTML = cur[1];
    
    cur = gettedData[13].split("=");
    var cfr = cur[1];
    var ist = cfr.length - 6;
    document.getElementById("vtv"+number+"_freq").innerHTML = cfr.substr(0,ist)+"."+cfr.substr(ist,cfr.length);
    
    cur = gettedData[14].split("=");
    document.getElementById("vtv"+number+"_outlev").innerHTML = cur[1];
    
    cur = gettedData[15].split("=");
    document.getElementById("vtv"+number+"_trnum").innerHTML = cur[1];
    
    cur = gettedData[16].split("=");
    document.getElementById("vtv"+number+"_cellnum").innerHTML = cur[1];
    
    cur = gettedData[17].split("=");
    document.getElementById("vtv"+number+"_delay").innerHTML = cur[1];
    
    cur = gettedData[18].split("=");
    document.getElementById("vtv"+number+"_numberline").innerHTML = cur[1];
    
    cur = gettedData[19].split("=");
    document.getElementById("vtv"+number+"_line").innerHTML = TR_OO2[parseInt(cur[1],10)];
    
    cur = gettedData[20].split("=");
    document.getElementById("vtv"+number+"_nonline").innerHTML = TR_OO2[parseInt(cur[1],10)];
    
    cur = gettedData[21].split("=");
    document.getElementById("vtv"+number+"_test").innerHTML = TR_TEST[parseInt(cur[1],10)];
  }, // 1 [1]
  MNet: function (gettedData, number) {}, // 0 [2]
  MTOO: function (gettedData, number) {}, // 0 [3]
  MPOO: function (gettedData, number) {}, // 0 [4]
  MAS1: function (gettedData, number) {}, // 0 [5]
  MAS2: function (gettedData, number) {}, // 0 [6]
  MQAM: function (gettedData, number) {}, // 0 [7]
  MRAT: function (gettedData, number) {}, // 0 [8]
  MGdI: function (gettedData, number) {}, // 0 [9]
  MHie: function (gettedData, number) {}, // 0 [10]
  MNCr: function (gettedData, number) {}, // 0 [11]
  MChn: function (gettedData, number) {}, // 0 [12]
  MFrq: function (gettedData, number) {}, // 0 [13]
  MOLv: function (gettedData, number) {}, // 0.00 [14]
  MTRN: function (gettedData, number) {}, // 0 [15]
  MCLN: function (gettedData, number) {}, // 0 [16]
  MDel: function (gettedData, number) {}, // 0 [17]
  MLPr: function (gettedData, number) {}, // 0 [18]
  LIOO: function (gettedData, number) {}, // 0 [19]
  NLOO: function (gettedData, number) {}, // 0 [20]
  MTst: function (gettedData, number) {}, // 0 [21]
  VVIP: function (gettedData, number) {}, // 172.16.1.77-8082 [22]
}
function getVtvModHtmlCode(number) {
  var _makeTable = function () {
    var names;
    var result = _tableHeadSpaced;
    
    var mainParam = [
      ['Сеть', 'vtv'+number+'_net', 'DVB-H вкл/откл', 'vtv'+number+'_dvbon'],
      ['MIP вкл/откл', 'vtv'+number+'_mipon', 'ASI-1', 'vtv'+number+'_asi1'],
      ['ASI-2', 'vtv'+number+'_asi2', 'Тип модуляции', 'vtv'+number+'_qam'],
      ['Скорость кода', 'vtv'+number+'_rate', 'Защитный интервал', 'vtv'+number+'_gi'],
      ['Иерархический режим', 'vtv'+number+'_heir', 'Число несущих', 'vtv'+number+'_carnum'],
      ['Номер канала', 'vtv'+number+'_chnum', 'Частота, МГц', 'vtv'+number+'_freq'],
      ['Уровень выхода, дБ', 'vtv'+number+'_outlev', 'Номер передатчика', 'vtv'+number+'_trnum'],
      ['Номер ячейки, ID', 'vtv'+number+'_cellnum', 'Доп. задержка, x100 нс.', 'vtv'+number+'_delay'],
      ['Нелинейный предкорректор', 'vtv'+number+'_nonline', 'Линейный предкорректор', 'vtv'+number+'_line'],
      ['№ кривой нел. предкорректора', 'vtv'+number+'_numberline', 'Режим теста', 'vtv'+number+'_test']
    ];
    
    var fillKey = [ cell25 ];
    var fillRow_Mod = function(names, call3) {
      var result;
      result = '<tr style="margin:0px; display:none">\r\n'
      result += cell25(names[0]);
      result += idCell25(names[1]);
      result += call3(names[2]);
      result += idCell25(names[3]);
      result += '</tr>\r\n';
      return result;
    };

    for (var i = 0; i < mainParam.length; i++) result += fillRow_Mod(mainParam[i], fillKey[0]);
    
    //
    result += _tableBottom;
    return result;
  };
  var result = ''+_makeTable();
  
  return result;
}

// Name - 
var map_ajx_pab = {
  nonamed : function (gettedData) {}, // undefined [0]
  
  // другие пока не используются
  conn: function (gettedData, number) {
    // скрываем таблицы
    $('#'+'new'+number+'refer_div').hide(); 
    for(var taNumber = 1; taNumber < 4+1; taNumber++)
      $('#'+'new'+number+'pa'+taNumber).hide();
    
    // продолжаем обработку
  
    var FRW_H, FRW_L, TEMR_H, TEMR_L,  bOn, bUnLock;
    var cur = gettedData[2].split("=");
    document.getElementById("new"+number+"pab_onof").innerHTML = TR_OO[parseInt(cur[1],10)];
    bOn = parseInt(cur[1],10);
    
    cur = gettedData[3].split("=");
    var curinput = document.getElementById("new"+number+"pab_lck");
    curinput.innerHTML = TR_LC[parseInt(cur[1],10)];
    if (parseInt(cur[1],10)==1)
      bUnLock = 0;
    else bUnLock = 1;
    
    cur = gettedData[26].split("=");
    FRW_L = parseInt(cur[1],10);
    cur = gettedData[27].split("=");
    FRW_H = parseInt(cur[1],10);
    
    cur =  gettedData[28].split("=");
    TEMR_L=parseInt(cur[1],10);
    cur =  gettedData[29].split("=");
    TEMR_H=parseInt(cur[1],10);
    
    cur =  gettedData[30].split("=");
    var iTrNum = parseInt(cur[1],10)-1;
    
    if (iTrNum!=76)
    {
      $('#'+'new'+number+'refer_div').show(); 
    }
    
    cur = gettedData[4].split("=");
    document.getElementById("new"+number+"pab_da").innerHTML = TR_AD[parseInt(cur[1],10)];
    
    cur = gettedData[5].split("=");
    curinput = document.getElementById("new"+number+"inpow_val");
    
    cur = gettedData[6].split("=");
    curinput = document.getElementById("new"+number+"pab_st");
    curinput.innerHTML = TR_OKFA[parseInt(cur[1],10)];
    
    cur = gettedData[7].split("=");
    curinput = document.getElementById("new"+number+"outpow_val");

    cur = gettedData[8].split("=");
    
    var frw_st = parseInt(cur[1],10);
    
    cur = gettedData[11].split("=");
    curinput = document.getElementById("new"+number+"frw_val");
    curinput.innerHTML = cur[1];
    
    cur = gettedData[9].split("=");
    document.getElementById("new"+number+"inpow_val").innerHTML = cur[1];
    
    cur = gettedData[10].split("=");
    document.getElementById("new"+number+"outpow_val").innerHTML = cur[1];
    
    // Preamplefier
    cur = gettedData[12].split("=");
    curinput = document.getElementById("new"+number+"pre_vt1_val");
    
    cur = gettedData[13].split("=");
    curinput = document.getElementById("new"+number+"pre_vt2_val");
    
    cur = gettedData[14].split("=");
    curinput = document.getElementById("new"+number+"pp_temre");
    var temre_st = parseInt(cur[1],10);
    
    // подсветка температуры
    cur = gettedData[20].split("=");
    curinput = document.getElementById("new"+number+"pp_temre");
    curinput.innerHTML = cur[1];
    
    // подсветка напряжения МИП предварительного?
    cur = gettedData[15].split("=");
    curinput = document.getElementById("new"+number+"pre_mip_val");
    
    // обобщение по предварительному
    var varsList = ['pre_vt1_val', 'pre_vt2_val', 'mock', 'pre_mip_val', 'pre_att_val', 'pre_phs_val', 'pre_ref_val' ];
    var i;
    for(i = 0; i < varsList.length; i++) {
      cur = gettedData[18+i].split("=");
      // rlse : 
      if (i != 2) {
        document.getElementById("new"+number+ varsList[ i ]).innerHTML = cur[1];
      }
      //dbg : 
      //document.getElementById("new"+number+ varsList[ i ]).innerHTML = number;
    }
    // число оконечных каскадов
    cur = gettedData[25].split("=");
    var iNumPAinPAB = parseInt(cur[1],10);
    //var iNumPAinPAB = 1;
    var bFirst = 1;
    var pa;
    if (bFirst==1) {
      bFirst = 0;
      for(var taNumber = 1; taNumber < iNumPAinPAB+1; taNumber++) {
        $('#'+'new'+number+'pa'+taNumber).show();
      }
    }
    
    // заполняем планки для оконечных усилителей
    var ISH = 5;
    var pabProcessing = function (beginPos, gettedData, numberTA, numberPAB) {
      var cur = gettedData[beginPos+0+ISH].split("=");
      var curinput = document.getElementById("new"+numberPAB+"pa1_vt1_val");
      //red_fon(cur,curinput);
      
      cur = gettedData[beginPos+1+ISH].split("=");
      curinput = document.getElementById("new"+numberPAB+"pa"+numberTA+"_vt2_val");
      //red_fon(cur,curinput);
      
      cur = gettedData[beginPos+2+ISH].split("=");
      curinput = document.getElementById("new"+numberPAB+"pa"+numberTA+"_tem_val");
      //red_fon(cur,curinput);
      temre_st = parseInt(cur[1],10);
      
      cur = gettedData[beginPos+3+ISH].split("=");
      curinput = document.getElementById("new"+numberPAB+"pa"+numberTA+"_mip_val");
      //red_fon(cur,curinput);
      
      cur = gettedData[beginPos+4+ISH].split("=");
      document.getElementById("new"+numberPAB+"pa"+numberTA+"_vt1_val").innerHTML = cur[1];
      
      cur = gettedData[beginPos+5+ISH].split("=");
      document.getElementById("new"+numberPAB+"pa"+numberTA+"_vt2_val").innerHTML = cur[1];
      
      cur = gettedData[beginPos+6+ISH].split("=");
      curinput = document.getElementById("new"+numberPAB+"pa"+numberTA+"_tem_val");
      curinput.innerHTML = cur[1];
      //if (temre_st==0) if (bOn==1) red_fon_bound(curinput,TEMR_H,TEMR_L);
      
      cur = gettedData[beginPos+7+ISH].split("=");
      document.getElementById("new"+numberPAB+"pa"+numberTA+"_mip_val").innerHTML = cur[1];
      //dbg : document.getElementById("new"+numberPAB+"pa"+numberTA+"_mip_val").innerHTML = numberTA;
    }
    if (iNumPAinPAB>=1) {
      pabProcessing(26, gettedData, 1, number);
    }
    if (iNumPAinPAB>1) {
      pabProcessing(34, gettedData, 2, number);
    } 
    if (iNumPAinPAB>2) {
      pabProcessing(42, gettedData, 3, number);
    }
    if (iNumPAinPAB>3) {
      pabProcessing(50, gettedData, 4, number);
    }
  }, // 1 [1]
  
  PBOO: function (gettedData) {}, // 0 [2]
  PBLc: function (gettedData) {}, // 0 [3]
  PBAD: function (gettedData) {}, // 0 [4]
  PBIP: function (gettedData) {}, // 0 [5]
  PBSt: function (gettedData) {}, // 0 [6]
  PBOP: function (gettedData) {}, // 0 [7]
  PFRs: function (gettedData) {}, // 0 [8]
  PIPw: function (gettedData) {}, // 0 [9]
  POPw: function (gettedData) {}, // 0 [10]
  PFRW: function (gettedData) {}, // 0.00 [11]
  PVT1: function (gettedData) {}, // 0 [12]
  PVT2: function (gettedData) {}, // 0 [13]
  PTem: function (gettedData) {}, // 0 [14]
  PMIP: function (gettedData) {}, // 0 [15]
  PAGC: function (gettedData) {}, // 0 [16]
  PP15: function (gettedData) {}, // 0 [17]
  CVT1: function (gettedData) {}, // 0.0 [18]
  CVT2: function (gettedData) {}, // 0.0 [19]
  PTeV: function (gettedData) {}, // 0 [20]
  MIPV: function (gettedData) {}, // 0.0 [21]
  PAtV: function (gettedData) {}, // 0.0 [22]
  PPhV: function (gettedData) {}, // 0.00 [23]
  RefV: function (gettedData) {}, // 0.0 [24]
  NuPA: function (gettedData) {}, // 4 [25]
  iFRWLow: function (gettedData) {}, // 60 [26]
  iFRWMax: function (gettedData) {}, // 100 [27]
  iTemLow: function (gettedData) {}, // 0 [28]
  iTemMax: function (gettedData) {}, // 80 [29]
  Trn: function (gettedData) {}, // 11 [30]
  AVT1: function (gettedData) {}, // 0 [31]
  AVT2: function (gettedData) {}, // 0 [32]
  ATem: function (gettedData) {}, // 0 [33]
  AMIP: function (gettedData) {}, // 0 [34]
  IVT1: function (gettedData) {}, // 0.0 [35]
  IVT2: function (gettedData) {}, // 0.0 [36]
  ATeV: function (gettedData) {}, // 0 [37]
  AMiV: function (gettedData) {}, // 0.0 [38]
  AVT1: function (gettedData) {}, // 0 [39]
  AVT2: function (gettedData) {}, // 0 [40]
  ATem: function (gettedData) {}, // 0 [41]
  AMIP: function (gettedData) {}, // 0 [42]
  IVT1: function (gettedData) {}, // 0.0 [43]
  IVT2: function (gettedData) {}, // 0.0 [44]
  ATeV: function (gettedData) {}, // 0 [45]
  AMiV: function (gettedData) {}, // 0.0 [46]
  AVT1: function (gettedData) {}, // 0 [47]
  AVT2: function (gettedData) {}, // 0 [48]
  ATem: function (gettedData) {}, // 0 [49]
  AMIP: function (gettedData) {}, // 0 [50]
  IVT1: function (gettedData) {}, // 0.0 [51]
  IVT2: function (gettedData) {}, // 0.0 [52]
  ATeV: function (gettedData) {}, // 0 [53]
  AMiV: function (gettedData) {}, // 0.0 [54]
  AVT1: function (gettedData) {}, // 0 [55]
  AVT2: function (gettedData) {}, // 0 [56]
  ATem: function (gettedData) {}, // 0 [57]
  AMIP: function (gettedData) {}, // 0 [58]
  IVT1: function (gettedData) {}, // 0.0 [59]
  IVT2: function (gettedData) {}, // 0.0 [60]
  ATeV: function (gettedData) {}, // 0 [61]
  AMiV: function (gettedData) {}, // 0.0 [62]
}

function getPABHtmlCode(number) {
  var _makeTable = function () {
    var names;
    
    // Общие и состояние
    var result = _tableHeadSpacedState;
    var mainParam = [['Состояние:', 'new'+number+'pab_lck', 'new'+number+'pab_st', 'new'+number+'pab_onof']];
    result += fillState4Any(mainParam[0]);
    result += _tableBottom;
    
    result += putHeadSubTable('Основные параметры');
    
    result += _tableHeadSpaced;
    mainParam = [
      ['Входная мощность, мВт.', 'new'+number+'inpow_val', 'Выходная мощность, Вт', 'new'+number+'outpow_val'],
      ['КБВ, значение ', 'new'+number+'frw_val', 'Режим БУМ', 'new'+number+'pab_da']];
    for (var i = 0; i < mainParam.length; i++) 
      result += fillRow(mainParam[i], cell25);
    result += _tableBottom;

    

    // предварительный 
    result += putHeadSubTable('Предварительный усилитель');
    result += _tableHeadSpaced;
    mainParam = ['','Ток VT1, А', 'new'+number+'pre_vt1_val', 'Ток VT2, А', 'new'+number+'pre_vt2_val',
      'Напряжение МИП, В', 'new'+number+'pre_mip_val', 'Температура, C', 'new'+number+'pp_temre'];
    result += fillNamedLongRow(mainParam, true, '');
    result += _tableBottom;
    
    result += _tableHeadSpaced;
    mainParam = [[ 'Напряжение установки фазы, В', 'new'+number+'pre_phs_val', 'Напряжение аттенюатора, В', 'new'+number+'pre_att_val']];
    var procs = [30,7,30,7];
    result += fillRow4Any(mainParam[0], procs);
    
    // Напряжение на предвариетльном ! срывается зачем-то
    mainParam = [[ 'Напряжение опорное, В', 'new'+number+'pre_ref_val','','' ]];
    var fillNamedRow4Any = function(names, procs, id) {
      var result;
      result = '<tr style="margin:0px" id="'+id+'">\r\n'
      result += cellAny(names[0], procs[0]);
      result += idCellAny(names[1], procs[1]);
      result += '</tr>\r\n';
      return result;
    };

    result += fillNamedRow4Any(mainParam[0], procs, 'new'+number+'refer_div');
    result += _tableBottom;

    result += putHeadSubTable('Оконечные усилители');
    result +=  _tableHeadSpaced;
    for(var taNumber = 1; taNumber < 4+1; taNumber++) {
      mainParam = ['№'+taNumber, 
        'Ток VT1, А', 'new'+number+'pa'+taNumber+'_vt1_val', 
        'Ток VT2, А', 'new'+number+'pa'+taNumber+'_vt2_val',
        'Напряжение МИП, В', 'new'+number+'pa'+taNumber+'_mip_val', 
        'Температура, С', 'new'+number+'pa'+taNumber+'_tem_val'];
      result += fillNamedLongRow(mainParam, true, 'new'+number+'pa'+taNumber);
    }
    result += _tableBottom;
    return result;
};
  var result = _makeTable();
  return result;
}

// Name - 
var map_ajx_bcn = {
  nonamed : function (gettedData) {
  
  
  }, // undefined [0]
  conn: function (gettedData, number) {
    var numLoads = 8;
    for (var i = 3; i < numLoads+1; ++i)
      $('#new'+number+'r'+i).hide();
    
    //
    var show_load = function  (iNumTr) {
      var showId = function(id) {
        $('#'+id).show();
      }
    
      //r1 r2 always
      switch(iNumTr) {
      case 2://1,2,7,8
        showId('new'+number+'r7');
        showId('new'+number+'r8');
        break;
      case 3://1-8
        showId('new'+number+'r3');
        showId('new'+number+'r4');
        showId('new'+number+'r5');
        showId('new'+number+'r6');
        showId('new'+number+'r7');
        showId('new'+number+'r8');
        break;
      case 9://1,2,3
        showId('new'+number+'r3');
        break;
      case 10://1-8
        showId('new'+number+'r3');
        showId('new'+number+'r4');
        showId('new'+number+'r5');
        showId('new'+number+'r6');
        showId('new'+number+'r7');
        showId('new'+number+'r8');
        break;
      case 20://1,2,3
        showId('new'+number+'r3');
        break;
      case 21://1-8
        showId('new'+number+'r3');
        showId('new'+number+'r4');
        showId('new'+number+'r5');
        showId('new'+number+'r6');
        showId('new'+number+'r7');
        showId('new'+number+'r8');
        break;
      case 22://1,2,7,8
        showId('new'+number+'r7');
        showId('new'+number+'r8');
        break;
      }
    }

    var curinput;
    
    var cur = gettedData[2].split("=");
    var iNumTr = parseInt(cur[1],10)-1;
    
    var bFirst = 1;
    if (bFirst==1) {
      bFirst = 0;
      show_load(iNumTr);
    }
    
    //cur = gettedData[3].split("=");
    //document.getElementById("new"+number+"vtv_fail").innerHTML = TR_SKS[parseInt(cur[1],10)];
    
    cur = gettedData[4].split("=");
    curinput = document.getElementById("new"+number+"bcn_st");
    curinput.innerHTML = TR_OKFA[parseInt(cur[1],10)];
    //red_fon(cur,curinput);
    
    cur = gettedData[5].split("=");
    document.getElementById("new"+number+"bcn_da").innerHTML = TR_AD[parseInt(cur[1],10)];
    //1
    cur = gettedData[6].split("=");
    document.getElementById("new"+number+"st_r1").innerHTML = TR_OKFA[parseInt(cur[1],10)];
    //red_fon(cur,curinput);
    
    cur = gettedData[7].split("=");
    curinput = document.getElementById("new"+number+"t_r1");
    //red_fon(cur,curinput);
    
    cur = gettedData[8].split("=");
    document.getElementById("new"+number+"t_r1").innerHTML = cur[1];
    
    cur = gettedData[9].split("=");
    //document.getElementById("new"+number+"p_r1").innerHTML = cur[1];
    //2
    cur = gettedData[10].split("=");
    document.getElementById("new"+number+"st_r2").innerHTML = TR_OKFA[parseInt(cur[1],10)];
    //red_fon(cur,curinput);
    
    cur = gettedData[11].split("=");
    curinput = document.getElementById("new"+number+"t_r2");
    //red_fon(cur,curinput);
    
    cur = gettedData[12].split("=");
    document.getElementById("new"+number+"t_r2").innerHTML = cur[1];
    
    cur = gettedData[13].split("=");
    //document.getElementById("new"+number+"p_r2").innerHTML = cur[1];
    //3
    cur = gettedData[14].split("=");
    document.getElementById("new"+number+"st_r3").innerHTML = TR_OKFA[parseInt(cur[1],10)];
    //red_fon(cur,curinput);
    
    cur = gettedData[15].split("=");
    curinput = document.getElementById("new"+number+"t_r3");
    //red_fon(cur,curinput);
    
    cur = gettedData[16].split("=");
    document.getElementById("new"+number+"t_r3").innerHTML = cur[1];
    
    cur = gettedData[17].split("=");
    //document.getElementById("new"+number+"p_r3").innerHTML = cur[1];
    
    //4
    cur = gettedData[18].split("=");
    document.getElementById("new"+number+"st_r4").innerHTML = TR_OKFA[parseInt(cur[1],10)];
    //red_fon(cur,curinput);
    
    cur = gettedData[19].split("=");
    curinput = document.getElementById("new"+number+"t_r4");
    //red_fon(cur,curinput);
    
    cur = gettedData[20].split("=");
    document.getElementById("new"+number+"t_r4").innerHTML = cur[1];
    
    cur = gettedData[21].split("=");
    //document.getElementById("new"+number+"p_r4").innerHTML = cur[1];
    
    //5
    cur = gettedData[22].split("=");
    document.getElementById("new"+number+"st_r5").innerHTML = TR_OKFA[parseInt(cur[1],10)];
    //red_fon(cur,curinput);
    
    cur = gettedData[23].split("=");
    curinput = document.getElementById("new"+number+"t_r5");
    //red_fon(cur,curinput);
    
    cur = gettedData[24].split("=");
    document.getElementById("new"+number+"t_r5").innerHTML = cur[1];
    
    cur = gettedData[25].split("=");
    //document.getElementById("new"+number+"p_r5").innerHTML = cur[1];
    
    //6
    cur = gettedData[26].split("=");
    document.getElementById("new"+number+"st_r6").innerHTML = TR_OKFA[parseInt(cur[1],10)];
    //red_fon(cur,curinput);
    
    cur = gettedData[27].split("=");
    curinput = document.getElementById("new"+number+"t_r6");
    //red_fon(cur,curinput);
    
    cur = gettedData[28].split("=");
    document.getElementById("new"+number+"t_r6").innerHTML = cur[1];
    
    cur = gettedData[29].split("=");
    //document.getElementById("new"+number+"p_r6").innerHTML = cur[1];
    
     //7
    cur = gettedData[30].split("=");
    document.getElementById("new"+number+"st_r7").innerHTML = TR_OKFA[parseInt(cur[1],10)];
    //red_fon(cur,curinput);
    
    cur = gettedData[31].split("=");
    curinput = document.getElementById("new"+number+"t_r7");
    //red_fon(cur,curinput);
    
    cur = gettedData[32].split("=");
    document.getElementById("new"+number+"t_r7").innerHTML = cur[1];
    
    cur = gettedData[33].split("=");
    document.getElementById("new"+number+"p_r7").innerHTML = cur[1];
    
    //8
    cur = gettedData[34].split("=");
    document.getElementById("new"+number+"st_r8").innerHTML = TR_OKFA[parseInt(cur[1],10)];
    //red_fon(cur,curinput);
    
    cur = gettedData[35].split("=");
    curinput = document.getElementById("new"+number+"t_r8");
    //red_fon(cur,curinput);
    
    cur = gettedData[36].split("=");
    document.getElementById("new"+number+"t_r8").innerHTML = cur[1];
    
    cur = gettedData[37].split("=");
    document.getElementById("new"+number+"p_r8").innerHTML = cur[1];
  }, // 1 [1]
  Trn: function (gettedData) {}, // 11 [2]
  Lock: function (gettedData) {}, // 0 [3]
  Stat: function (gettedData) {}, // 0 [4]
  AnDi: function (gettedData) {}, // 0 [5]
  R1: function (gettedData) {}, // 0 [6]
  S1: function (gettedData) {}, // 0 [7]
  T1: function (gettedData) {}, // 0 [8]
  P1: function (gettedData) {}, // 0 [9]
  R2: function (gettedData) {}, // 0 [10]
  S2: function (gettedData) {}, // 0 [11]
  T2: function (gettedData) {}, // 0 [12]
  P2: function (gettedData) {}, // 0 [13]
  R3: function (gettedData) {}, // 0 [14]
  S3: function (gettedData) {}, // 0 [15]
  T3: function (gettedData) {}, // 0 [16]
  P3: function (gettedData) {}, // 0 [17]
  R4: function (gettedData) {}, // 0 [18]
  S4: function (gettedData) {}, // 0 [19]
  T4: function (gettedData) {}, // 0 [20]
  P4: function (gettedData) {}, // 0 [21]
  R5: function (gettedData) {}, // 0 [22]
  S5: function (gettedData) {}, // 0 [23]
  T5: function (gettedData) {}, // 0 [24]
  P5: function (gettedData) {}, // 0 [25]
  R6: function (gettedData) {}, // 0 [26]
  S6: function (gettedData) {}, // 0 [27]
  T6: function (gettedData) {}, // 0 [28]
  P6: function (gettedData) {}, // 0 [29]
  R7: function (gettedData) {}, // 0 [30]
  S7: function (gettedData) {}, // 0 [31]
  T7: function (gettedData) {}, // 0 [32]
  P7: function (gettedData) {}, // 0 [33]
  R8: function (gettedData) {}, // 0 [34]
  S8: function (gettedData) {}, // 0 [35]
  T8: function (gettedData) {}, // 0 [36]
  P8: function (gettedData) {}, // 0 [37]
}

function getBCNHtmlCode(number) {
  var _makeTable = function () {
    var _loadsPlace = [25, 15];
    var fillNamedRow1 = function(names, id) {
      var result;
      result = '<tr id='+id+' style="margin:0px">\r\n'
      result += cellAny(names[0], _loadsPlace[0]);
      result += idCellAny(names[1], _loadsPlace[1]);
      result += '</tr>\r\n';
      return result;
    };
    
    var _loadsPlace0 = [25, 20, 15, 20];
    var fillNamedRow = function(names, id) {
      var result;
      result = '<tr id='+id+' style="margin:0px">\r\n'
      result += cellAny(names[0], _loadsPlace0[0]);
      result += idCellAny(names[1], _loadsPlace0[1]);
      result += idCellAny(names[2], _loadsPlace0[2]);
      result += idCellAny(names[3], _loadsPlace0[3]);
      result += '</tr>\r\n';
      return result;
    };
    var fill4HeadRow = function(names) {
      var result;
      result = '<tr style="margin:0px">\r\n'
      result += cellAny(names[0], _loadsPlace0[0]);
      result += cellAny(names[1], _loadsPlace0[1]);
      result += cellAny(names[2], _loadsPlace0[2]);
      result += cellAny(names[3], _loadsPlace0[3]);
      result += '</tr>\r\n';
      return result;
    };
    // Сам процесс вычисления
    var names;
  
    var result = '';
    var mainParam = [['Состояние:', 'new'+number+'bcn_st', '', '']];
    result += fillState4Any(mainParam[0]);
    
    result += putHeadSubTable('Основные параметры');
    
    var _tableHeadSpaced1 = '<table width="45%" align="left" cellpadding="3"'+
      ' cellspacing="1" style="padding:0px;border-collapse: collapse;margin-bottom:8px;" >';
    result += _tableHeadSpaced1;
    mainParam = ['Режим', 'new'+number+'bcn_da', '', ''];
    result += fillNamedRow1(mainParam, 'new_dddd');
    result += _tableBottom+'<br>';
    
    result += putHeadSubTable('Нагрузки');

    //
    var numLoads = 8;
    result += _tableHeadSpaced;
    result += fill4HeadRow([  '', 'Состояние', 'T &deg;С', 'Мощность Pср, Вт']);
    for (var i = 1; i < numLoads+1; ++i) {
      mainParam =['Нагрузка '+i, 'new'+number+'st_r'+i,  'new'+number+'t_r'+i,  'new'+number+'p_r'+i ];
      result += fillNamedRow(mainParam, 'new'+number+'r'+i);
    }
    
    result += _tableBottom;
    //
    return result;
  };
  return _makeTable();
}
// Name - 
var map_ajx_bd = {
  nonamed : function (gettedData) {}, // undefined [0]
  conn: function (gettedData, number) {
    $('#new'+number+'pw_analog').hide();
    $('#new'+number+'fwr').hide();
    var show_block = function (iNumTr) {
      switch(number) {
        case 1://
          show_r('fwr',1);
          if (iNumTr<=3 || iNumTr>=22) 
            $("#new"+number+'pw_analog');
          break;
        case 2://1-8
          if (iNumTr<=3 || iNumTr>=22) 
            $("#new"+number+'pw_analog');
          break;
      }
    }

    var curinput;
    
    var cur = gettedData[2].split("=");
    var iNumTr = parseInt(cur[1],10)-1;
    
    var bFirst = 1;
    if (bFirst==1)
    {
      bFirst = 0;
      show_block(iNumTr);
    }
    
    //cur = gettedData[3].split("=");
    //document.getElementById("new"+number+"vtv_fail").innerHTML = TR_SKS[parseInt(cur[1],10)];
    
    cur = gettedData[4].split("=");
    curinput = document.getElementById("new"+number+"bd_st");
    curinput.innerHTML = TR_OKFA[parseInt(cur[1],10)];
    red_fon(cur,curinput);
    
    cur = gettedData[5].split("=");
    document.getElementById("new"+number+"bd_da").innerHTML = TR_AD[parseInt(cur[1],10)];
    
    cur = gettedData[6].split("=");
    document.getElementById("new"+number+"bd_pw").innerHTML = cur[1];
    
    cur = gettedData[7].split("=");
    document.getElementById("new"+number+"bd_frw").innerHTML = cur[1];
    
    cur = gettedData[8].split("=");
    document.getElementById("new"+number+"bd_pwsd").innerHTML = cur[1];
  }, // 1 [1]
  Trn: function (gettedData) {}, // 11 [2]
  Lock: function (gettedData) {}, // 0 [3]
  Stat: function (gettedData) {}, // 0 [4]
  AnDi: function (gettedData) {}, // 0 [5]
  Powr: function (gettedData) {}, // 0 [6]
  VFRW: function (gettedData) {}, // 0.00 [7]
  Psnd: function (gettedData) {}, // 0 [8]
}
function getBDHtmlCode(number) {
  var _makeTable = function () {
    var _loadsPlace = [30, 15];
    var fillNamedRow = function(names, id) {
      var result;
      result = '<tr id='+id+' style="margin:0px">\r\n'
      result += cellAny(names[0], _loadsPlace[0]);
      result += idCellAny(names[1], _loadsPlace[1]);
      result += '</tr>\r\n';
      return result;
    };

    // Сам процесс вычисления
    var names;
    var result = '';
    var mainParam = [['Состояние', 'new'+number+'bd_st', '', '']];
    result += fillState4Any(mainParam[0]);
    
    result += putHeadSubTable('Основные параметры');
    
    // данные
    var _tableHeadSpaced = '<table width="50%" align="left" cellpadding="3"'+
      ' cellspacing="1" style="padding:0px;border-collapse: collapse;margin-bottom:8px;" >';
    result += _tableHeadSpaced;
    var mainParam = ['Режим', 'new'+number+'bd_da', '', ''];
    result += fillNamedRow(mainParam, 'new_dddd');
    mainParam =['Мощность Pср, Вт', 'new'+number+'bd_pw',  '',  '' ];
    result += fillNamedRow(mainParam, 'new_dddd');
    mainParam =['Мощность звука, Вт', 'new'+number+'bd_pwsd',  '',  '' ];
    result += fillNamedRow(mainParam, 'new'+number+'pw_analog');
    mainParam =['КБВ', 'new'+number+'bd_frw',  '',  '' ];
    result += fillNamedRow(mainParam, 'new'+number+'fwr');
    result += _tableBottom;
    //
    return result;
  };
  return _makeTable();
}