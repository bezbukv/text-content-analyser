 

var yourNamespace = {

    foo: function() {
    },

    bar: function() {
    }
};

(function() {
    //private variable
    var ingredient = "Bacon Strips";
 
    //private function
    function fry() {
        console.log( "Frying " + ingredient );
    }
     
    fry();
}());

var skillet = (function() {
    var pub = {},
        //Private property
        amountOfGrease = "1 Cup";
 
    //Public property    
    pub.ingredient = "Bacon Strips";
 
    //Public method
    pub.fry = function() {
        console.log( "Frying " + pub.ingredient );
    };
 
    //Private method
    function privateWay() {
        //Do something...
    }
 
    //Return just the public parts
    return pub;
}());

(function( skillet, $, undefined ) {
    //Private Property
    var isHot = true;
 
    //Public Property
    skillet.ingredient = "Bacon Strips";
     
    //Public Method
    skillet.fry = function() {
        var oliveOil;
         
        addItem( "\t\n Butter \n\t" );
        addItem( oliveOil );
        console.log( "Frying " + skillet.ingredient );
    };
     
    //Private Method
    function addItem( item ) {
        if ( item !== undefined ) {
            console.log( "Adding " + $.trim(item) );
        }
    }    
}( window.skillet = window.skillet || {}, jQuery ));

var bOldrest = 0;
var bOldshut = 0;
var bStop =0;

//vspliv podskazki
function showHint(id,s)
{
  var sdiv=document.getElementById(id);
  if(s)
    sdiv.style.display='block';
  else
    sdiv.style.display='none';
}

//is number
function isNumber(inputStr)
{
    for (var i=0;i<inputStr.length;i++)
    {
        var oneChar = inputStr.charAt(i);
        if (oneChar < "0" || oneChar > "9")
        {
            alert("В это поле должны водиться только десятичные цифры");
            return false;
        }
    }
    return true;
}

function checkNumeric(fld)
{
    var inputStr = fld.value;
    if (!isNumber(inputStr))
    {
        fld.value = "0";
    }
}




//buttons
var buttons = new Array(); 
var imagesPath = "/img/";


function rollover()
{
    var par = document.getElementById("restspan");
    par.className = "overspan";
}

function outcheck()
{
    var par = document.getElementById("restspan");
    par.className = "outspan";
}
function rollover1()
{
    var par = document.getElementById("shutspan");
    par.className = "overspan";
}

function outcheck1()
{
    var par = document.getElementById("shutspan");
    par.className = "outspan";
}

function rollover2()
{
    var par = document.getElementById("modrspan");
    par.className = "overspan";
}

function outcheck2()
{
    var par = document.getElementById("modrspan");
    par.className = "outspan";
}
function rollover3()
{
    var par = document.getElementById("predspan");
    par.className = "overspan";
}

function outcheck3()
{
    var par = document.getElementById("predspan");
    par.className = "outspan";
}

function chRest()
{
    var restspan = document.getElementById("restspan");
    if (document.forms[0].rest.value=="0")
    {
        document.forms[0].rest.value="1";
        var lsttxt = restspan.lastChild;
        restspan.removeChild(lsttxt);
        var txt1;
        txt1 = document.createTextNode("Сервер будет перезагружен.(Нажмите чтобы отменить)");
        restspan.appendChild(txt1);
        
        document.forms[0].shut.value = "1";
        chShut();
//        document.forms[0].modr.value="1";
//        chModr();
//        document.forms[0].pred.value="1";
//        chPred();
        
        return;
    }
    if (document.forms[0].rest.value=="1")
    {
        document.forms[0].rest.value="0";
        var lsttxt = restspan.lastChild;
        restspan.removeChild(lsttxt);
        var txt1;
        txt1 = document.createTextNode("Перезагрузить сервер");
        restspan.appendChild(txt1);
    }
}
function chShut()
{
    var restspan = document.getElementById("shutspan");
    if (document.forms[0].shut.value=="0")
    {
        document.forms[0].shut.value="1";
        var lsttxt = restspan.lastChild;
        restspan.removeChild(lsttxt);
        var txt1;
        txt1 = document.createTextNode("Сервер будет выключен.(Нажмите чтобы отменить)");
        restspan.appendChild(txt1);
        
        document.forms[0].rest.value="1";
        chRest();
//        document.forms[0].modr.value="1";
//        chModr();
//        document.forms[0].pred.value="1";
//        chPred();
        return;
    }
    if (document.forms[0].shut.value=="1")
    {
        document.forms[0].shut.value="0";
        var lsttxt = restspan.lastChild;
        restspan.removeChild(lsttxt);
        var txt1;
        txt1 = document.createTextNode("Выключить сервер");
        restspan.appendChild(txt1);
    }
}

function chModr()
{
    var restspan = document.getElementById("modrspan");
    if (document.forms[0].modr.value=="0")
    {
        document.forms[0].modr.value="1";
        var lsttxt = restspan.lastChild;
        restspan.removeChild(lsttxt);
        var txt1;
        txt1 = document.createTextNode("Модулятор будет перезагружен.(Нажмите чтобы отменить)");
        restspan.appendChild(txt1);
        
        document.forms[0].rest.value="1";
        chRest();
        document.forms[0].shut.value="1";
        chShut();
        document.forms[0].pred.value="1";
        chPred();
        
        return;
    }
    if (document.forms[0].modr.value=="1")
    {
        document.forms[0].modr.value="0";
        var lsttxt = restspan.lastChild;
        restspan.removeChild(lsttxt);
        var txt1;
        txt1 = document.createTextNode("Перезагрузить модулятор");
        restspan.appendChild(txt1);
    }
}

function chPred()
{
    var restspan = document.getElementById("predspan");
    if (document.forms[0].pred.value=="0")
    {
        document.forms[0].pred.value="1";
        var lsttxt = restspan.lastChild;
        restspan.removeChild(lsttxt);
        var txt1;
        txt1 = document.createTextNode("Предкорректор будет перезагружен.(Нажмите чтобы отменить)");
        restspan.appendChild(txt1);
        
        document.forms[0].rest.value="1";
        chRest();
        document.forms[0].shut.value="1";
        chShut();
        document.forms[0].modr.value="1";
        chModr();
        
        return;
    }
    if (document.forms[0].pred.value=="1")
    {
        document.forms[0].pred.value="0";
        var lsttxt = restspan.lastChild;
        restspan.removeChild(lsttxt);
        var txt1;
        txt1 = document.createTextNode("Перезагрузить предкорректор");
        restspan.appendChild(txt1);
    }
}

//status header
ie4 = (document.all)? true:false

function showit() 
{
    if (ie4) {blinking.style.visibility = "visible"}
    setTimeout("hideit()",100)
}

function hideit() {
    if (ie4) {blinking.style.visibility = "hidden"}
    setTimeout("showit()",10)
}

function onloadcheck()
{
    buttonHovers();
    //start button
    bOldrest = document.forms[0].rest.value;
    bOldshut = document.forms[0].shut.value;
    document.getElementById("dhcp0").checked=true; ondhcp(1);
    onautodns(1);  document.getElementById("adns0").checked=true;
    getData(set_page);
    getData2(net_page);
}

function setState()
{
    postData(sys_page);
}

var req_pw,req,req2,req3;
var sys_page = "/cgi-bin/ajx_sysonof.exe";
var set_page = "/cgi-bin/ajx_getsys.exe";
var net_page = "/cgi-bin/ajx_getnet.exe";
var postset_page = "/cgi-bin/ajx_setsys.exe";
var postnet_page = "/cgi-bin/ajx_setnet.exe";
var restrs_page = "/cgi-bin/ajx_restrs.exe";
var passwd_page = "/cgi-bin/ajx_passwd.exe";
var force_page = "/cgi-bin/frc_rst.exe";

function aplnet()
{
    var sda = checknet();
    if (sda) { sda = confirm("Применить настройки? Для отмены нажмите ОТМЕНА.");
    if (sda) {postData(postnet_page);} }
}

function restrssnmp()
{
    sda = confirm("Перезапустить RS и SNMP? Для отмены нажмите ОТМЕНА.");
    if (sda)   {postData(restrs_page);}
}

function aplset()
{
    var sda = checkset();
    if (sda) { sda = confirm("Применить настройки? Для отмены нажмите ОТМЕНА.");
    if (sda) {postData(postset_page);}}
}

function chanpassword()
{
    var sda = checkpasswd();
    if (sda) {postData(passwd_page)};
    document.getElementById("newp1").value="";
    document.getElementById("newp2").value="";
    document.getElementById("curpass").value="";
}

function hotreset()
{
    sda = confirm("Перезагрузить сервер? Для отмены нажмите ОТМЕНА.");
    if (sda) {getData3(force_page);}
}

function checkpasswd()
{
    var pas1 = document.getElementById("newp1").value;
    var pas2 = document.getElementById("newp2").value;
    if (pas1!=pas2) 
    {
        alert("Ошибка. Пароль и подтверждение не совпадают.");
        return false;
    }
    if (pas1.length<8)
    {
        alert("Ошибка. Короткий пароль.");
        return false;
    }
    if (checkSybmol(pas1)==false)
    {
        alert("Ошибка. Недопустимые символы в пароле.");
        return false;
    }
    return true;
}

function checkSybmol(value)
{
    value=value.replace(/\s+/g,'');
    var Dig=false;
    var Let=false;
    var GoodS=false;
    for (var i=0;i<value.length;i++)
    {
        Dig=false;
        Let=false;
        GoodS=false;
        
        var oneChar = value.charAt(i);
        if ((oneChar >= "0" && oneChar <= "9"))
        {
            Dig=true;
        }
        if ((oneChar >= "A" && oneChar <= "Z"))
        {
            Let=true;
        }
        if ((oneChar >= "a" && oneChar <= "z"))
        {
            Let=true;
        }
        //=_-!*,.:;()
        //if (oneChar == "=" ) GoodS=true;
        if (oneChar == "_" ) GoodS=true;
        if (oneChar == "-" ) GoodS=true;
        if (oneChar == "." ) GoodS=true;
        if (oneChar == "!" ) GoodS=true;
        if (oneChar == "*" ) GoodS=true;
        if (oneChar == "," ) GoodS=true;
        if (oneChar == ";" ) GoodS=true;
        if (oneChar == ":" ) GoodS=true;
        if (oneChar == "(" ) GoodS=true;
        if (oneChar == ")" ) GoodS=true;
 
        
        if ((Dig==false)&&(Let==false)&&(GoodS==false))
        {
            return false;
        }
        
    }
    
    return true;
}

function checkSybmol2(value)
{
    value=value.replace(/\s+/g,'+');
    var Dig=false;
    var Let=false;
    var GoodS=false;
    for (var i=0;i<value.length;i++)
    {
        Dig=false;
        Let=false;
        GoodS=false;
        
        var oneChar = value.charAt(i);
        if ((oneChar >= "0" && oneChar <= "9"))
        {
            Dig=true;
        }
        if ((oneChar >= "A" && oneChar <= "Z"))
        {
            Let=true;
        }
        if ((oneChar >= "a" && oneChar <= "z"))
        {
            Let=true;
        }
        if ((oneChar >= "А" && oneChar <= "Я"))
        {
            Let=true;
        }
        if ((oneChar >= "а" && oneChar <= "я"))
        {
            Let=true;
        }
        
        //=_-!*,.:;()
        if (oneChar == "+" ) GoodS=true;
        //if (oneChar == "=" ) GoodS=true;
        if (oneChar == "_" ) GoodS=true;
        if (oneChar == "-" ) GoodS=true;
        if (oneChar == "." ) GoodS=true;
        if (oneChar == "!" ) GoodS=true;
        if (oneChar == "*" ) GoodS=true;
        if (oneChar == "," ) GoodS=true;
        if (oneChar == ";" ) GoodS=true;
        if (oneChar == ":" ) GoodS=true;
        if (oneChar == "(" ) GoodS=true;
        if (oneChar == ")" ) GoodS=true;
 
        
        if ((Dig==false)&&(Let==false)&&(GoodS==false))
        {
            return false;
        }
        
    }
    
    return true;
}

 
function postData(page3)
{
    req_pw = null;
    if (window.XMLHttpRequest) {
        try {
            req_pw = new XMLHttpRequest();
        } catch (e){}
    } else if (window.ActiveXObject) {
        try {
            req_pw = new ActiveXObject('Msxml2.XMLHTTP');
        } catch (e){
            try {
                req_pw = new ActiveXObject('Microsoft.XMLHTTP');
            } catch (e){}
        }
    }
    
    if (req_pw) 
    {       
        req_pw.open("POST", page3, true);
        
        
        try
        {
            req_pw.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        }
        catch(e){};
        var params;
        if (page3==sys_page)
        {
            req_pw.onreadystatechange = processReqChange2;
            params = "rest=" + document.getElementById("rest").value + "&shut=" + document.getElementById("shut").value;
        }
        if (page3==postnet_page)
        {
            req_pw.onreadystatechange = processReqChange4;
            params=get_net(params);
        }
        if (page3==restrs_page)
        {
            req_pw.onreadystatechange = processReqChange4;
            params = "rest=1";
        }
        
        if (page3==postset_page)
        {
            req_pw.onreadystatechange = processReqChange4;
            params=get_settings(params);
            
        }
        if (page3==passwd_page)
        {
            req_pw.onreadystatechange = processReqChange4;
            params = "pas0=" + document.getElementById("newp1").value +
             "&pas1=" + document.getElementById("newp2").value +
             "&curpas=" + document.getElementById("curpass").value;
        }
        req_pw.send(params);
    }
}
 
function processReqChange2()
{
  try { // Важно!
    // только при состоянии "complete"
    if (req_pw.readyState == 4) {
        // для статуса "OK"
        if (req_pw.status == 200) 
        {
            alert( "Действие будет выполнено ");
            window.location = "wait.htm";
        } 
        else 
        {
            alert("Не удалось получить данные:\n");// +
                //req.statusText);
        }
    }
  }
  catch( e ) {
      // alert('Caught Exception: ' + e.description);
      // В связи с багом XMLHttpRequest в Firefox приходится отлавливать ошибку
      // Bugzilla Bug 238559 XMLHttpRequest needs a way to report networking errors
      // https://bugzilla.mozilla.org/show_bug.cgi?id=238559
  }
}

function processReqChange5()
{
  try { // Важно!
    // только при состоянии "complete"
    if (req3.readyState == 4) {
        // для статуса "OK"
        if (req3.status == 200) 
        {
            var ret2=req3.responseText;
            var check = ret2.split(" ");
            if (check[0]=="The")
            {
                alert( "Действие будет выполнено ");
                window.location = "wait.htm";
            }
            else
            {
                alert( "Действие отменено. Перезагрузите штатно.");
            }
        } 
        else 
        {
            alert( "Действие будет выполнено ");
            window.location = "wait.htm";
        }
    }
  }
  catch( e ) {
      // alert('Caught Exception: ' + e.description);
      // В связи с багом XMLHttpRequest в Firefox приходится отлавливать ошибку
      // Bugzilla Bug 238559 XMLHttpRequest needs a way to report networking errors
      // https://bugzilla.mozilla.org/show_bug.cgi?id=238559
  }
}

function processReqChange4()
{
  try { // Важно!
    // только при состоянии "complete"
    if (req_pw.readyState == 4) {
        // для статуса "OK"
        if (req_pw.status == 200) 
        {
            split_req(req_pw.responseText);
           
        } 
        else 
        {
            alert("Не удалось получить данные:\n");// +
            clear_all();
                //req.statusText);
        }
    }
  }
  catch( e ) {
      // alert('Caught Exception: ' + e.description);
      // В связи с багом XMLHttpRequest в Firefox приходится отлавливать ошибку
      // Bugzilla Bug 238559 XMLHttpRequest needs a way to report networking errors
      // https://bugzilla.mozilla.org/show_bug.cgi?id=238559
  }
}
function split_req(value) // split modulator config
{
    var check = value.split(" ");
    if (check[0]=="The")
    {
        //clear_all();
        return;
    }
    else
    {
        value = value.substr(0,(value.length-2));
        var data=value.split("&");

        cur = data[2].split("=");
        if (cur[1]=="ОК"){alert("Действие будет выполнено")}
        else{ alert("Действие отменено")}
        
    }
}

function on_hideshow(cur_span) 
{    

    var objd = document.getElementById(cur_span);
    //var objd = cur_span;
    var compstyle;
    var dst;
    if (objd.currentStyle)
    {
        dst = objd.currentStyle.display;
    }
    else
    {
        compstyle = document.defaultView.getComputedStyle(objd,"");
        dst = compstyle.getPropertyValue("display");
    }
    if(dst == "")
    {
        objd.style.display = "none";
        
    }
    else if(dst != "none")
    {
        objd.style.display = "none";
    }
    else
    {
        objd.style.display = "block";
        
         //set default
    }
}




function getData(page)
{
    req = null;
    if (window.XMLHttpRequest) {
        try {
            req = new XMLHttpRequest();
        } catch (e){}
    } else if (window.ActiveXObject) {
        try {
            req = new ActiveXObject('Msxml2.XMLHTTP');
        } catch (e){
            try {
                req = new ActiveXObject('Microsoft.XMLHTTP');
            } catch (e){}
        }
    }
    var tmp = new Date(); 
    //tmp = "?"+tmp.getTime();
    tmp = "?"+"r="+Math.random();
    tmp = page + tmp;
    if (req) 
    {   
        req.open("GET", tmp, true);
        if (page == set_page)
        {
            req.onreadystatechange = processReqChange;
        }
        
        
        
        req.send(null);
    }
    
}

function getData2(page)
{
    req2 = null;
    if (window.XMLHttpRequest) {
        try {
            req2 = new XMLHttpRequest();
        } catch (e){}
    } else if (window.ActiveXObject) {
        try {
            req2 = new ActiveXObject('Msxml2.XMLHTTP');
        } catch (e){
            try {
                req2 = new ActiveXObject('Microsoft.XMLHTTP');
            } catch (e){}
        }
    }
    var tmp = new Date(); 
    //tmp = "?"+tmp.getTime();
    tmp = "?"+"r="+Math.random();
    tmp = page + tmp;
    if (req2) 
    {   
        req2.open("GET", tmp, true);
        if (page == net_page)
        {
            req2.onreadystatechange = processReqChange3;
        }
        
        req2.send(null);
    }
    
}

function getData3(page)
{
    req3 = null;
    if (window.XMLHttpRequest) {
        try {
            req3 = new XMLHttpRequest();
        } catch (e){}
    } else if (window.ActiveXObject) {
        try {
            req3 = new ActiveXObject('Msxml2.XMLHTTP');
        } catch (e){
            try {
                req3 = new ActiveXObject('Microsoft.XMLHTTP');
            } catch (e){}
        }
    }
    var tmp = new Date(); 
    //tmp = "?"+tmp.getTime();
    tmp = "?"+"r="+Math.random();
    tmp = page + tmp;
    if (req3) 
    {   
        req3.open("GET", tmp, true);
        if (page == force_page)
        {
            req3.onreadystatechange = processReqChange5;
        }
        
        req3.send(null);
    }
    
}
 
function processReqChange()
{
  try { // Важно!
    // только при состоянии "complete"
    if (req.readyState == 4) {
        // для статуса "OK"
        if (req.status == 200) 
        {
            var ret2=req.responseText;
            splitset(ret2);
        } 
        else 
        {
            clear_all();
           // alert("Не удалось получить данные:\n" +
           //     req.statusText);
        }
    }
  }
  catch( e ) {
      // alert('Caught Exception: ' + e.description);
      // В связи с багом XMLHttpRequest в Firefox приходится отлавливать ошибку
      // Bugzilla Bug 238559 XMLHttpRequest needs a way to report networking errors
      // https://bugzilla.mozilla.org/show_bug.cgi?id=238559
  }
}

function processReqChange3()
{
  try { // Важно!
    // только при состоянии "complete"
    if (req2.readyState == 4) {
        // для статуса "OK"
        if (req2.status == 200) 
        {
            var ret2=req2.responseText;
            splitnet(ret2);
        } 
        else 
        {
            clear_all();
           // alert("Не удалось получить данные:\n" +
           //     req.statusText);
        }
    }
  }
  catch( e ) {
      // alert('Caught Exception: ' + e.description);
      // В связи с багом XMLHttpRequest в Firefox приходится отлавливать ошибку
      // Bugzilla Bug 238559 XMLHttpRequest needs a way to report networking errors
      // https://bugzilla.mozilla.org/show_bug.cgi?id=238559
  }
}

function splitset(value)
{
    var check = value.split(" ");
    if (check[0]=="The")
    {
        clear_all();
        return;
    }
    else
    {
        value = value.substr(0,(value.length-2));
        var data=value.split("&");

        cur = data[2].split("=");
        document.getElementById("rs_adr").value = cur[1];

        cur = data[3].split("=");
        document.getElementById("ip_vtv1").value = cur[1];

        cur = data[4].split("=");
        document.getElementById("ip_vtv2").value = cur[1];

        cur = data[5].split("=");
        document.getElementById("ip_snmp").value = cur[1];

        cur = data[6].split("=");
        document.getElementById("port_snmp").value = cur[1];

        cur = data[7].split("=");
        document.getElementById("period_snmp").value = cur[1];

        cur = data[8].split("=");
        document.getElementById("devid_snmp").value = cur[1];

        cur = data[9].split("=");
        document.getElementById("devtype_snmp").value = cur[1];
        
        cur = data[10].split("=");
        document.getElementById("port_vtv1").value = cur[1];
        
        cur = data[11].split("=");
        document.getElementById("port_vtv2").value = cur[1];
        
        cur = data[12].split("=");
        document.getElementById("ip_ext").value = cur[1];
        
        cur = data[13].split("=");
        document.getElementById("flow").value = cur[1];
        cur = data[14].split("=");
        document.getElementById("fhig").value = cur[1];
            
        cur = data[15].split("=");
        document.getElementById("plow").value = cur[1];
        cur = data[16].split("=");
        document.getElementById("phig").value = cur[1];
    
        cur =  data[17].split("=");
        document.getElementById("tlow").value = cur[1];
        cur =  data[18].split("=");
        document.getElementById("thig").value = cur[1];
        
        cur =  data[19].split("=");
        document.getElementById("idischan").value = cur[1];
        
        cur =  data[20].split("=");
        document.getElementById("isinf0").value = cur[1];
        
        cur =  data[21].split("=");
        document.getElementById("isinf1").value = cur[1];
        
        
        
        cur =  data[22].split("=");
        document.getElementById("rHour").value = cur[1];
        
        cur =  data[23].split("=");
        document.getElementById("rMin").value = cur[1];
        
        cur =  data[24].split("=");
        document.getElementById("rp").value = cur[1];
        
        cur =  data[25].split("=");
        if (cur[1]=="0"){ document.getElementById("bRest1").checked=true; }
        else{document.getElementById("bRest0").checked=true; }
        
        cur =  data[26].split("=");
        var val=cur[1].replace(/-/g,'.');
        document.getElementById("rld").value = val;
           

        bStop = 1;
       
    }
}

var TR_OO =new Array("отключен","включен");
var net_data = Array ();
function splitnet(value)
{
    var check = value.split(" ");
    if (check[0]=="The")
    {
        clear_all();
        return;
    }
    else
    {
        value = value.substr(0,(value.length-2));
        var data=value.split("&");
        var bdhcp;
        cur = data[2].split("=");
        net_data[0]=cur[1];
        bdhcp = parseInt(cur[1],10);
        document.getElementById("rdhcp").value = TR_OO[bdhcp];
         
        cur = data[3].split("=");
        var dip = cur[1].split(",");
        var ipv4 ="";
        var ipv6 ="";
        var msk="";
        var pfx="";
        for(var i=0; i<dip.length; i++)
        {
            if (isIPv6(dip[i]))
            {
                if (ipv6.length>0) ipv6 +=",";
                ipv6 += dip[i];
            }
            else
            {
                if (ipv4.length>0) ipv4 +=",";
                ipv4 += dip[i];
            }
        }
        net_data[1]=ipv4;
        document.getElementById("rIP").value = cur[1];
        document.getElementById("ipv60").value = ipv6;
        
        cur = data[4].split("=");
        dip = cur[1].split(",");
        for(var i=0; i<dip.length; i++)
        {
            if (isPfx(dip[i]))
            {
                if (pfx.length>0) pfx +=",";
                pfx += dip[i];
            }
            else
            {
                if (msk.length>0) msk +=",";
                msk += dip[i];
            }
        }
        net_data[2]=msk;
        document.getElementById("rMask").value = cur[1];
        document.getElementById("pfx60").value = pfx;
        
        cur = data[5].split("=");
        net_data[3]=cur[1];
        document.getElementById("rgw").value = cur[1];
        
        cur = data[7].split("=");
        net_data[4]=cur[1];
        document.getElementById("rdnsip").value = cur[1];
        if (cur[1].length>0)
        {
             document.getElementById("rdns").value = TR_OO[0];
        }
        else 
        {
            if (bdhcp==1)
            {
                document.getElementById("rdns").value = TR_OO[1];
            }
            else 
            {
                document.getElementById("rdns").value = TR_OO[0];
            }
        }
        
        cur = data[8].split("=");
        net_data[5]=cur[1];
        var curinput = document.getElementById("rwins");
        curinput.value = cur[1];
        
        cur = data[9].split("=");
       
        if (cur[1].length>0)
        {
            curinput.value += ","; net_data[5]+=",";
            curinput.value += cur[1]; net_data[5]+=cur[1];
        }
        
        cur = data[10].split("=");
        net_data[6]=cur[1];
        document.getElementById("metgw").value = cur[1];
    
    }
}

function isIPv6(inputstr)
{
    var data = inputstr.split(":");
    if (data.length<2) return false;
    
    return true;
}

function isPfx(inputstr)
{
    var data = inputstr.split(".");
    if (data.length>1) return false;
    
    return true;
}
function clear_all()
{
    if (bStop==1)
    {
        window.location = "cgi_sys.htm";
        bStop = 0;
    }
}


function copynet()
{
    if (net_data[0]=="0"){ document.getElementById("dhcp1").checked=true; ondhcp(0);}
    else{document.getElementById("dhcp0").checked=true; ondhcp(1);}
    
    var data=net_data[1].split(",");
    var tmp;
    var max = 4;
    if (data.length < max) max = data.length;
    for (var i = 0; i < max; i++) 
    {
        tmp="ip"; tmp+=i;
        document.getElementById(tmp).value = data[i];
    }
    
    data=net_data[2].split(",");
    max = 4;
    if (data.length < max) max = data.length;
    for (var i = 0; i < max; i++) 
    {
        tmp="mask"; tmp+=i.toString(10);
        document.getElementById(tmp).value = data[i];
    }
    
    data=net_data[3].split(",");
    max = 2;
    if (data.length < max) max = data.length;
    for (var i = 0; i < max; i++) 
    {
        tmp="gw"; tmp+=i.toString(10);
        document.getElementById(tmp).value = data[i];
    }
    
    data=net_data[4].split(",");
    max = 3;
    if (net_data[0]=="0")
    {
        onautodns(0);
        document.getElementById("adns1").checked=true;
        if (data.length < max) max = data.length;
        for (var i = 0; i < max; i++) 
        {
            tmp="dns"; tmp+=i.toString(10);
            document.getElementById(tmp).value = data[i];
        }
    }
    else
    {
        if (data.length==0)
        {
             onautodns(1); document.getElementById("adns0").checked=true;
        }
        else
        {
            if ((data.length==1)&&(data[0].length==0))
            {
                onautodns(1);  document.getElementById("adns0").checked=true;
            }
            else
            {  onautodns(0);   document.getElementById("adns1").checked=true;
                if (data.length < max) max = data.length;
                for (var i = 0; i < max; i++) 
                { tmp="dns"; tmp+=i.toString(10);
                    document.getElementById(tmp).value = data[i];
                }
            }
        }
    }
    
    data=net_data[5].split(",");
    max = 2;
    if (data.length < max) max = data.length;
    for (var i = 0; i < max; i++) 
    {
        tmp="wins"; tmp+=i.toString(10);
        document.getElementById(tmp).value = data[i];
    }
    
   
    data=net_data[6].split(",");
    max = 2;
    if (data.length < max) max = data.length;
    for (var i = 0; i < max; i++) 
    {
        tmp="mgw"; tmp+=i.toString(10);
        document.getElementById(tmp).value = data[i];
    }
}

function ondhcp(val)
{
    if (val==0)
    {
        document.getElementById("ip0").disabled=false;
        document.getElementById("ip1").disabled=false;
        document.getElementById("ip2").disabled=false;
        document.getElementById("ip3").disabled=false;
        document.getElementById("mask0").disabled=false;
        document.getElementById("mask1").disabled=false;
        document.getElementById("mask2").disabled=false;
        document.getElementById("mask3").disabled=false;
        document.getElementById("mgw0").disabled=false;
        document.getElementById("mgw1").disabled=false;
        document.getElementById("gw0").disabled=false;
        document.getElementById("gw1").disabled=false;
        document.getElementById("adns0").disabled=true;
        document.getElementById("adns1").checked = true;
        
    }
    else if (val==1)
    {
        document.getElementById("ip0").disabled="disabled";
        document.getElementById("ip1").disabled="disabled";
        document.getElementById("ip2").disabled="disabled";
        document.getElementById("ip3").disabled="disabled";
        document.getElementById("mask0").disabled="disabled";
        document.getElementById("mask1").disabled="disabled";
        document.getElementById("mask2").disabled="disabled";
        document.getElementById("mask3").disabled="disabled";
        document.getElementById("mgw0").disabled="disabled";
        document.getElementById("mgw1").disabled="disabled";
        document.getElementById("gw0").disabled="disabled";
        document.getElementById("gw1").disabled="disabled";
        document.getElementById("adns0").disabled=false;
        document.getElementById("adns0").checked = true;
    }
}

function onautodns(val)
{
    if (val==0)
    {
        document.getElementById("dns0").disabled=false;
        document.getElementById("dns1").disabled=false;
        document.getElementById("dns2").disabled=false;
       
    }
    else if (val==1)
    {
        document.getElementById("dns0").disabled="disabled";
        document.getElementById("dns1").disabled="disabled";
        document.getElementById("dns2").disabled="disabled";
       
    }    
}


function   get_net(params)
{
    var tmp;
    params = "IPAddress="+ document.getElementById("ip0").value;
    tmp = document.getElementById("ip1").value;
    if (tmp.length>0)   {  params+=(","+tmp);  }
    tmp = document.getElementById("ip2").value;
    if (tmp.length>0)   {  params+=(","+tmp);  }
    tmp = document.getElementById("ip3").value;
    if (tmp.length>0)   {  params+=(","+tmp);  }
    tmp = document.getElementById("ipv60").value;
    if (tmp.length>0)   {  params+=(","+tmp);  }
    
    params += ("&IPSubnet="+ document.getElementById("mask0").value);
    tmp = document.getElementById("mask1").value;
    if (tmp.length>0)   {  params+=(","+tmp);  }
    tmp = document.getElementById("mask2").value;
    if (tmp.length>0)   {  params+=(","+tmp);  }
    tmp = document.getElementById("mask3").value;
    if (tmp.length>0)   {  params+=(","+tmp);  }
    tmp = document.getElementById("pfx60").value;
    if (tmp.length>0)   {  params+=(","+tmp);  }
    
    params += "&DHCPEnabled=";
    if (document.getElementById("dhcp1").checked==true)
    {
        params += "0";
    }
    else    params += "1";
    
    
    params += ("&DefaultIPGateway="+ document.getElementById("gw0").value);
    tmp = document.getElementById("gw1").value;
    if (tmp.length>0)   {  params+=(","+tmp);  }
    
    params += ("&GatewayCostMetric="+ document.getElementById("mgw0").value);
    tmp = document.getElementById("mgw1").value;
    if (tmp.length>0)   {  params+=(","+tmp);  }
    
    params += "&DNSServerSearchOrder=";
    if (document.getElementById("adns0").checked==true)
    {
        params += "";
    }
    else
    {
        params += document.getElementById("dns0").value;
        tmp = document.getElementById("dns1").value;
        if (tmp.length>0)   {  params+=(","+tmp);  }
        tmp = document.getElementById("dns2").value;
        if (tmp.length>0)   {  params+=(","+tmp);  }
    }
    
    params += ("&WINSPrimaryServer="+ document.getElementById("wins0").value);
    params += ("&WINSSecondaryServer="+ document.getElementById("wins1").value);
 
    return params;   
}

function   get_settings(params)
{
    var tmp;
    params = "TrNum=" + document.getElementById("rs_adr").value;
    params += "&ipVtv1=" + document.getElementById("ip_vtv1").value;
    params += "&ipVtv2=" + document.getElementById("ip_vtv2").value;
    params += "&PortVtv1=" + document.getElementById("port_vtv1").value;
    params += "&PortVtv2=" + document.getElementById("port_vtv2").value;
    params += "&ipSNMP=" + document.getElementById("ip_snmp").value;
    params += "&portSNMP=" + document.getElementById("port_snmp").value;
    params += "&perdSNMP=" + document.getElementById("period_snmp").value;
    params += "&DeviID=" + document.getElementById("devid_snmp").value;
    params += "&DTypID=" + document.getElementById("devtype_snmp").value;
    params += "&ipExt=" + document.getElementById("ip_ext").value;
    
    params += "&iFRWLow=" + document.getElementById("flow").value;
    params += "&iFRWMax=" + document.getElementById("fhig").value;
    params += "&iPowLow=" + document.getElementById("plow").value;
    params += "&iPowMax=" + document.getElementById("phig").value;
    params += "&iTemLow=" + document.getElementById("tlow").value;
    params += "&iTemMax=" + document.getElementById("thig").value;
    
    params += "&idc=" + document.getElementById("idischan").value;
    
    tmp = document.getElementById("isinf0").value;
    tmp=utf8_decode(tmp);
    
   
    params += "&isi0=" + tmp;
    
    tmp = document.getElementById("isinf1").value;
    tmp=utf8_decode(tmp);
    params += "&isi1=" + tmp;
    
    params += "&rh=" + document.getElementById("rHour").value;
    params += "&rm=" + document.getElementById("rMin").value;
    params += "&rp=" + document.getElementById("rp").value;
    params += "&rb=";
    if (document.getElementById("bRest1").checked==true)
    {
        params += "0";
    }
    else    params += "1";
    
    
 
    return params;   
}


function utf8_decode (aa) {
    var bb = '', c = 0;
    for (var i = 0; i < aa.length; i++) {
        c = aa.charCodeAt(i);
        if (c > 127) {
            if (c > 1024) {
                if (c == 1025) {
                    c = 1016;
                } else if (c == 1105) {
                    c = 1032;
                }
                bb += String.fromCharCode(c - 848);
            }
        } else {
            bb += aa.charAt(i);
        }
    }
    var tmp = bb;
    
    //to %hex
    var sss='',c;
    for (var i = 0; i < tmp.length; i++)
    {
       sss += "%";
       c =   parseInt(tmp.charCodeAt(i),10);
       sss += c.toString(16);
    }
    return sss;
}

function checknet()
{
    var tmp = "Неверный параметр:";
    var sda = checkip(document.getElementById("ip0").value);
    if (!sda){alert(tmp+"IP 0"); return false;}
    sda = checkip(document.getElementById("ip1").value);
    if (!sda){alert(tmp+"IP 1"); return false;}
    sda = checkip(document.getElementById("ip2").value);
    if (!sda){alert(tmp+"IP 2"); return false;}
    sda = checkip(document.getElementById("ip3").value);
    if (!sda){alert(tmp+"IP 3"); return false;}
    sda = checkmask(document.getElementById("mask0").value);
    if (!sda){alert(tmp+"Маска 0"); return false;}
    sda = checkmask(document.getElementById("mask1").value);
    if (!sda){alert(tmp+"Маска 1"); return false;}
    sda = checkmask(document.getElementById("mask2").value);
    if (!sda){alert(tmp+"Маска 2"); return false;}
    sda = checkmask(document.getElementById("mask3").value);
    if (!sda){alert(tmp+"Маска 3"); return false;}
    sda = checkip(document.getElementById("gw0").value);
    if (!sda){alert(tmp+"Шлюз 0"); return false;}
    sda = checkip(document.getElementById("gw1").value);
    if (!sda){alert(tmp+"Шлюз 1"); return false;}
    sda = checkip(document.getElementById("dns0").value);
    if (!sda){alert(tmp+"DNS 0"); return false;}
    sda = checkip(document.getElementById("dns1").value);
    if (!sda){alert(tmp+"DNS 1"); return false;}
    sda = checkip(document.getElementById("dns2").value);
    if (!sda){alert(tmp+"DNS 2"); return false;}
    sda = checkip(document.getElementById("wins0").value);
    if (!sda){alert(tmp+"WINS 0"); return false;}
    sda = checkip(document.getElementById("wins1").value);
    if (!sda){alert(tmp+"WINS 0"); return false;}
    
    var value=document.getElementById("mgw0").value;
    value=value.replace(/\s+/g,'');
    if (value.length>0) { sda = isNumber(); if (!sda){alert(tmp+"Метрика 0"); return false;}}
    
    value=document.getElementById("mgw1").value;
    value=value.replace(/\s+/g,'');
    if (value.length>0) { sda = isNumber(); if (!sda){alert(tmp+"Метрика 1"); return false;}}
       
   
    
    return true;
}

function checkset()
{
    var tmp = "Неверный параметр:";
    var sda = checkip(document.getElementById("ip_vtv1").value);
    if (!sda){alert(tmp+"IP ВТВ1"); return false;}
    sda = checkip(document.getElementById("ip_vtv2").value);
    if (!sda){alert(tmp+"IP ВТВ2"); return false;}
    sda = checkip(document.getElementById("ip_snmp").value);
    if (!sda){alert(tmp+"IP SNMP-менеджера"); return false;}
    
    sda = checkip(document.getElementById("ip_ext").value);
    if (!sda){alert(tmp+"IP Внешний"); return false;}
       
    sda = isNumber(document.getElementById("port_vtv1").value);
    if (document.getElementById("port_vtv1").value==0) return false;
    if (!sda){alert(tmp+"Порт ВТВ1"); return false;}
    
    sda = isNumber(document.getElementById("port_vtv2").value);
    if (document.getElementById("port_vtv2").value==0) return false;
    if (!sda){alert(tmp+"Порт ВТВ2"); return false;}
    
    sda = isNumber(document.getElementById("port_snmp").value);
    if (document.getElementById("port_snmp").value==0) return false;
    if (!sda){alert(tmp+"Порт SNMP-менеджера"); return false;}
    sda = isNumber(document.getElementById("period_snmp").value);
    if (document.getElementById("period_snmp").value==0) return false;
    if (!sda){alert(tmp+"Период для SNMP"); return false;}
    sda = isNumber(document.getElementById("devid_snmp").value);
    if (!sda){alert(tmp+"DeviceID SNMP"); return false;}
    sda = isNumber(document.getElementById("devtype_snmp").value);
    if (!sda){alert(tmp+"ParDeviceType SNMP"); return false;}
    sda = isNumber(document.getElementById("rs_adr").value);
    if (document.getElementById("rs_adr").value==0) return false;
    if (!sda){alert(tmp+"RS адрес передатчика"); return false;}
    
    sda = isNumber(document.getElementById("flow").value);
    if (!sda){alert(tmp+"КБВ LOW"); return false;}
    sda = isNumber(document.getElementById("fhig").value);
    if (!sda){alert(tmp+"КБВ HIGH"); return false;}
    if (parseInt(document.getElementById("flow").value,10) > parseInt(document.getElementById("fhig").value,10))
    {
        alert(tmp+"КБВ HIGH < КБВ LOW"); return false;
    }
    
    sda = isNumber(document.getElementById("plow").value);
    if (!sda){alert(tmp+"Мощность LOW"); return false;}
    sda = isNumber(document.getElementById("phig").value);
    if (!sda){alert(tmp+"Мощность HIGH"); return false;}
    if (parseInt(document.getElementById("plow").value,10) > parseInt(document.getElementById("phig").value,10))
    {
        alert(tmp+"Мощность HIGH < Мощность LOW"); return false;
    }
    
    sda = isNumber(document.getElementById("tlow").value);
    if (!sda){alert(tmp+"Температура LOW"); return false;}
    sda = isNumber(document.getElementById("thig").value);
    if (!sda){alert(tmp+"Температура HIGH"); return false;}
    if (parseInt(document.getElementById("tlow").value,10) > parseInt(document.getElementById("thig").value,10))
    {
        alert(tmp+"Температура HIGH < Температура LOW"); return false;
    }
    
    sda = isNumber(document.getElementById("idischan").value);
    if (!sda){alert(tmp+"Отображаемый номер канала"); return false;}
    
    var s1 = document.getElementById("isinf0").value;
    var s2 = document.getElementById("isinf1").value;
    
    if (checkSybmol2(s1)==false)
    {
        alert("Ошибка. Недопустимые символы в Инф0.");
        return false;
    }
    
    if (checkSybmol2(s2)==false)
    {
        alert("Ошибка. Недопустимые символы в Инф1.");
        return false;
    }
    
    s1 = document.getElementById("rHour").value;
    sda = isNumber(s1);
    if (!sda){alert(tmp+"Время часы"); return false;}
    if ((parseInt(s1,10)<0)||(parseInt(s1,10)>23))
    {alert(tmp+"Время часы"); return false;}
    
    
    s1 = document.getElementById("rMin").value;
    sda = isNumber(s1);
    if (!sda){alert(tmp+"Время минуты"); return false;}
    if ((parseInt(s1,10)<0)||(parseInt(s1,10)>59))
    {alert(tmp+"Время минуты"); return false;}
    
    s1 = document.getElementById("rp").value;
    sda = isNumber(s1);
    if (!sda){alert(tmp+"Период в днях"); return false;}
    if ((parseInt(s1,10)<1)||(parseInt(s1,10)>7))
    {alert(tmp+"Период в днях"); return false;}
    
    
    return true;
}

function checkip(value)
{
    value=value.replace(/\s+/g,'');
    if (value.length==0)
    {
        return true;
    }
    var data=value.split(".");
    if (data.length!=4)
    {
        data = value.split(":");
        if (data.length<2) return false;
        var data0 = value.split("::");
        if (data0.length>1)
        {
            data=new Array();
            var k=0,m=0;
            var data1 = data0[0].split(":");
            data[0]=data1[0];
            for (var i=1; i<data1.length; i++)
            {
                data[i]=data1[i];
            }
            k=data1.length;m=k;
            data1 = data0[1].split(":");
            var len = m+data1.length;
            for (var i=0; i<(8-len);i++)
            {
                data[m+i]="0";k++;
            }
            data[k]=data1[0];
            for (var i=0; i<data1.length; i++)
            {
                data[k+i]=data1[i]; 
            }
            
        }
        
        for (var i=0; i<data.length; i++)
        {
            var inputStr = data[i].toUpperCase();
            if (inputStr.length==0) return false;
            sda = isHex(inputStr);
            if (!sda) return false;
            else if ((parseInt(data[i],16)<0)||(parseInt(data[i],16)>65535))  return false;
        }
        
    }
    else
    {
        for (var i=0; i<data.length; i++)
        {
            if (data[i].length==0) return false;
            sda = isNumber(data[i]);
            if (!sda) return false;
            else if ((parseInt(data[i],10)<0)||(parseInt(data[i],10)>255))  return false;    
        }
       
    }
    
    return true;
}

function checkmask(value)
{
    value=value.replace(/\s+/g,'');
    if (value.length==0)
    {
        return true;
    }
    var data=value.split(".");
    if (data.length!=4)
    {
        if (data.length!=1) return false;
        sda = isNumber(data[0]);
        if (!sda) return false;
        else if ((parseInt(data[i],10)<0)||(parseInt(data[i],10)>128))  return false; 
    }
    else
    {
        for (var i=0; i<data.length; i++)
        {
            sda = isNumber(data[i]);
            if (!sda) return false;
            else if ((parseInt(data[i],10)<0)||(parseInt(data[i],10)>255))  return false;    
        }
       
    }
    
    return true;
}

function isHex(inputStr)
{
    for (var i=0;i<inputStr.length;i++)
    {
        var oneChar = inputStr.charAt(i);
        if ((oneChar < "0" || oneChar > "9"))
        {
            if ((oneChar < "A" || oneChar > "F"))
            {
                return false;
            }
        }
    }
    
   
    return true;
}

function checkHex(fld)
{
    var inputStr = fld.value.toUpperCase();
    if (!isHex(inputStr))
    {
        fld.value = "0";
    }
}