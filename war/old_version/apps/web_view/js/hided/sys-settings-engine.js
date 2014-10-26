// Хранить состояние настроек. При открытии страницы мы !!должны!! из заполнить
// затем работать с ветками настроек
var g_readedSettings = undefined;

// пространства имен
var CrossCuts = {  // TODO(zaqwes): Не видится если в другом файле
    //,

  // Общая сигнализация
  plot_call_stack : function (e) {
    stack_trace = e.stack;
    stack_trace = stack_trace.split('at ').join('<br>&nbsp&nbsp at ');
    return stack_trace;
  },
  
  // Коммуникации
  resend : function (function_name) {
    var _TIMEOUT_SEND = 2000;
    window.setTimeout(function_name+'()',_TIMEOUT_SEND);
  },
  
  // Шаблоны
  get_half_alert_template: function (type, id, head_msg) {
    return '<div id="'+
        id+'" class="alert alert-block alert-'+
        type+' fade in">'+
        '<button type="button" class="close" data-dismiss="alert">&times;</button>'+
        '<h4 class="alert-heading">'+head_msg+'</h4>'+
        '<div id="msgr"></div>';
  },
 
};

/// /// ///
///
/// Getters 
var GlobalMsgr = (function () { 
  // TODO(zaqwes):Автодополнитель Eclipse не видит в таких пространствах имен
  var g_alerts_windows_id = 'g_alerts';
  var g_info_placeholder_id = 'g_info_id';
  var bootstrap_alert = function() {};

  return {
    close_global_alert: function () {
      $("#"+g_alerts_windows_id+"").alert('close');
    },

    // Просто информация
    close_global_info: function () {
      $("#"+g_info_placeholder_id+"").alert('close');
    },
    warn_global: function (msg) {
      bootstrap_alert.warning = function(msg) {
        $('#global_alert_placeholder').html(
            CrossCuts.get_half_alert_template(
                'warning', g_alerts_windows_id, 
                'Предупреждение')+msg+'</div>');
      };
      bootstrap_alert.warning(msg);
    },
    err_global: function (msg) {
      bootstrap_alert.error = function(msg) {
        $('#global_alert_placeholder').html(
            CrossCuts.get_half_alert_template('error', g_alerts_windows_id,'Ошибка')+
            msg+'</div>');
      };

      bootstrap_alert.error(msg);
    },
    info_global: function (msg) {
      bootstrap_alert.info = function(msg) {
        $('#global_alert_placeholder').html(
            CrossCuts.get_half_alert_template('info', g_alerts_windows_id, 'Сообщение')+msg+'</div>'
          );
      };
      bootstrap_alert.info(msg);
    },
    info_global_info: function (msg) {
      bootstrap_alert.info_global = function(msg) {
        $('#g_info_placeholder').html(
            CrossCuts.get_half_alert_template('info', g_info_placeholder_id, 'Сообщение')+msg+'</div>'
              );
      };
      bootstrap_alert.info_global(msg);
    }
  };
})();



/** Getter */

function get_all_settings() {
  var fun_name = 'get_all_settings';
  var resend = CrossCuts.resend;
  
  var response_processor = function (data) {
    // Если битый, то оставить старый и отправить новый запрос
    try {
      g_readedSettings = $.parseJSON(data);  // При неверном формате выбр. искл. и не изменит настройки
      //
      GlobalMsgr.close_global_info();
      GlobalMsgr.close_global_alert();  // Если ошибка была, то убираем, так же сообщение ожидания. Ответ получен и он корректен
      
      // Размазываем результат по странице
      _fit_response(g_readedSettings);
    } catch (e) {
      // Ошибка в cgi-скрипте. Ломаная json строка.
      GlobalMsgr.err_global(
          'Запрос параметров закончился с ошибкой.'+
          ' Неправильный формат принятых данных.<br>'+
          'Запрос будет повторен.</i><br><br><b>Дополнительная информация:</b><br>'+
          data+'<br><br>Снимок стека:<br>'+
          CrossCuts.plot_call_stack(e));
      resend(fun_name);
      GlobalMsgr.close_global_info();
      return;
    }
  };

  var request_processor = '/cgi-bin/settings.py';
  GlobalMsgr.info_global_info("Подождите выполняется запрос...");

  // Запросчик
  var jqxhr = $.get(request_processor, {name: fun_name})
    .success(response_processor)  // колбэк должен быть известен
    .error(function(data) { 
      if (data.status == 404) {
        GlobalMsgr.close_global_info();
        GlobalMsgr.err_global("Запрос к несуществующей странице. Ошибка 404. Запрос будет повторен.");
        resend(fun_name);
      }
      if (data.status == 500) {
        GlobalMsgr.close_global_info();
        GlobalMsgr.err_global("Внутренняя ошибка сервера. Ошибка 500.");
        resend(fun_name);  // TODO(?):  Нужно ли повторять запрос
      }
      if (data.status == 0) {
        GlobalMsgr.close_global_info();
        GlobalMsgr.err_global("<b>Отсутствует связь с сервером, проверьте соединение</b>. Запрос будет повторен.");
        resend(fun_name);  // TODO(?):  Нужно ли повторять запрос
      }
    })
    .complete(function(data) { ; });
  
  var _fit_response = function (data_map) {
    
    for (var sets_branch in data_map) {
      if(data_map.hasOwnProperty(sets_branch) ) {
        var settings_mon_system = data_map[sets_branch];
        for (var item in settings_mon_system) {
          if(settings_mon_system.hasOwnProperty(item) ) {
            // Textboxes
            if (item[0] == 't') {
              document.getElementById(item).value = settings_mon_system[item];
            }
            
            // Comboboxes
            if (item[0] == 'o') {
              var recoder_map = {public:0, private:1};
              //var country = $('#'+item);
              var country = document.getElementById(item);
              try {
                var index = recoder_map[settings_mon_system[item]];
                country.options[index].selected = true;
              } catch (e) {
                alert('Error: key no found.');                
              }
            }
            
            // Checkboxes
            if (item[0] == 'c') {
              var recoder_map = {1:true, 0:false};
              var country = document.getElementById(item);
              try {
                var index = recoder_map[settings_mon_system[item]];
                country.checked = index;
              } catch (e) {
                alert('Error: key no found.');                
              }
            }
            
            // Radiobuttons
            if (item[0] == 'r') {
              if (item === 'rdhcp_on') {
                if (settings_mon_system[item] == '1') {
                  var country = document.getElementById(item);
                  country.checked = true;
                } else if (settings_mon_system[item] == '0') {
                  var country = document.getElementById('rdhcp_off');
                  country.checked = true;
                } else {
                  alert('Error: Неудалось определить параметер.')
                }
              }
              if (item === 'rauto_dns_on') {
                if (settings_mon_system[item] == '1') {
                  var country = document.getElementById(item);
                  country.checked = true;
                } else if (settings_mon_system[item] == '0') {
                  var country = document.getElementById('rauto_dns_off');
                  country.checked = true;
                } else {
                  alert('Error: Неудалось определить параметер.')
                }
              }
            }  // if (item[0] == 'r') {
          }
        }
      }
    } 
  }; // _fit_response
}
/// Getters
///
/// /// ///

/// /// ///
///
/// Setters API
var kTypeSetRequest = 'GET';
/** Обобщенные обработчкик сетторов */
var Setters = { 
    bootstrap_alert : function() {},  // TODO(zaqwes): Нельзя вызвать изнутри объекта, при рек. упаковке
    // Генераторы шаблонов  
    get_accept_template : function (id, type, head_msg, callback_txt) {
      return '<div id="'+
          id+'" class="alert alert-block alert-'+
          type+' fade in">'+
          '  <button type="button" class="close" data-dismiss="alert">&times;</button>'+
          '  '+
          '  <h4 class="alert-heading">'+head_msg+'</h4>'+
          '  <p align="right">'+
          '    <input type="button" onclick="'+
          callback_txt+'(0)" class="btn btn-small" value="Отмена" align="right"/>'+
          '&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp '+
          // Не получается подключить как нужно
          '    <input type="button" onclick="'+
          callback_txt+'(1)" class="btn  btn-small btn-warning" value='+
          '"&nbsp&nbsp&nbsp&nbsp&nbsp&nbspДа&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" '+
          'align="right"/>'+
          '  </p>'+
          '</div>';
    },

    get_accept_template_full : function (
        id, 
        type, 
        head_msg, 
        callback_txt,
        ok_btn_type) {
      return '<div id="'+
          id+'" class="alert alert-block alert-'+
          type+' fade in">'+
          '  <button type="button" class="close" data-dismiss="alert">&times;</button>'+
          '  '+
          '  <h4 class="alert-heading">'+head_msg+'</h4>'+
          '  <p align="right">'+
          '    <input type="button" onclick="'+
          callback_txt+'(0)" class="btn btn-small btn" value="Отмена" align="right"/>'+
          '&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp '+
          // Не получается подключить как нужно
          '    <input type="button" onclick="'+
          callback_txt+'(1)" class="btn btn-small btn-'+ok_btn_type+'" value='+
          '"&nbsp&nbsp&nbsp&nbsp&nbsp&nbspДа&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" '+
          'align="right"/>'+
          '  </p>'+
          '</div>';
    },
    
    /// Обобщенные сообщения 
    // @param type - 'info', 'error', 'warning'
    msg : function (placeholder, msg, type) {
      Setters.bootstrap_alert[placeholder] = function(msg) {
        var head = '';
        if (type == 'info') head = 'Сообщение';
        else if (type == 'error') head = 'Ошибка';
        else if (type == 'warning') head = 'Передупреждение';
        else {}
        
        // Рисуем
        var html_code = 
            CrossCuts.get_half_alert_template(type, placeholder+'_inner', head)+
            msg+'</div>';
        $('#'+placeholder).html(html_code);
      };
      Setters.bootstrap_alert[placeholder](msg);
    },
    
    accept : function (placeholder, type, callback_txt) {
      var head = 'Применить настройки?';
      Setters.bootstrap_alert[placeholder] = function() {
        var html_contant = Setters.get_accept_template(
            placeholder+'_inner', 
            type, head, 
            callback_txt);
        $('#'+placeholder).html(html_contant);
      };
      Setters.bootstrap_alert[placeholder]();
    },
    
    accept_msg : function (placeholder, type, callback_txt, text) {
      var head = text;
      Setters.bootstrap_alert[placeholder] = function() {
        var html_contant = Setters.get_accept_template(
            placeholder+'_inner', 
            type, head, 
            callback_txt);
        $('#'+placeholder).html(html_contant);
      };
      Setters.bootstrap_alert[placeholder]();
    },
    
    accept_full : function (placeholder, type, callback_txt, msg) {
      var head = msg;
      Setters.bootstrap_alert[placeholder] = function() {
        var html_contant = Setters.get_accept_template_full(
            placeholder+'_inner', 
            type, head, 
            callback_txt, 'danger');
        $('#'+placeholder).html(html_contant);
      };
      Setters.bootstrap_alert[placeholder]();
    },
    

    // Функции скрытия сообщений
    msg_hide : function (placeholder) {
      $("#"+placeholder+'_inner').alert('close');
    }, 
    
    hide_all : function (sets_branch) {
      Setters.msg_hide('accept_'+sets_branch);
      Setters.msg_hide('err_'+sets_branch);
      Setters.msg_hide('msg_'+sets_branch);
    },
    
    hide_no_err : function (sets_branch) {
      Setters.msg_hide('accept_'+sets_branch);
      Setters.msg_hide('msg_'+sets_branch);
    },
    
    set_err_handle : function (data, sets_branch, fun_name) {
      var err_action = function (msg) {
        Setters.hide_all(sets_branch);
        Setters.msg(
            'err_'+sets_branch, 
            msg, 
            'error');
        CrossCuts.resend(fun_name);
      };
      
      if (data.status == 404) {
        err_action(
            'Запрос к несуществующей странице. '+
            'Ошибка 404. Запрос будет повторен.');
      }
      if (data.status == 500) {
        err_action(
            'Внутренняя ошибка сервера. Ошибка 500. '+
            'Запрос будет повторен.');
      }
      if (data.status == 0) {
        err_action(
            '<b>Отсутствует связь с сервером, проверьте соединение.</b> '+
            'Запрос будет повторен.');
      }
    },
    
    // One branch processor
    check_and_accept : function (sets_branch, checker_fptr, name_processor) {
      Setters.hide_all(sets_branch);

      // Проверка правильности ввода
      var response = checker_fptr();
      if (response !== "") {
        Setters.msg(
            'err_'+sets_branch, 
            'В полях формы ошибка. Проверьте корректность вводимых данных.<br>'+
              response, 
            'error');
        return;
      }

      // Уверены? 
      Setters.accept(
          'accept_'+sets_branch, 
          'warning', 
          name_processor);
      // Можно отправлять, но нужно подтверждение. По его получению вызывается функция по имени указ. в accept выше
     
    },
    
    // One branch processor
    check_and_accept_w_msg : function (sets_branch, checker_fptr, name_processor, msg) {
      Setters.hide_all(sets_branch);

      // Проверка правильности ввода
      var response = checker_fptr();
      if (response !== "") {
        Setters.msg(
            'err_'+sets_branch, 
            'В полях формы ошибка. Проверьте корректность вводимых данных.<br>'+
              response, 
            'error');
        return;
      }

      // Уверены? 
      Setters.accept_msg(
          'accept_'+sets_branch, 
          'warning', 
          name_processor, msg);
      // Можно отправлять, но нужно подтверждение. По его получению вызывается функция по имени указ. в accept выше
     
    },
    
 // One branch processor
    check_and_accept_d_msg : function (sets_branch, checker_fptr, name_processor, msg) {
      Setters.hide_all(sets_branch);

      // Проверка правильности ввода
      var response = checker_fptr();
      if (response !== "") {
        Setters.msg(
            'err_'+sets_branch, 
            'В полях формы ошибка. Проверьте корректность вводимых данных.<br>'+
              response, 
            'error');
        return;
      }

      // Уверены? 
      Setters.accept_full(
          'accept_'+sets_branch, 
          'error', 
          name_processor, msg);
      // Можно отправлять, но нужно подтверждение. По его получению вызывается функция по имени указ. в accept выше
     
    },
    
    // One branch processor
    check_and_accept_danger : function (sets_branch, checker_fptr, name_processor, msg) {
      Setters.hide_all(sets_branch);

      // Проверка правильности ввода
      var response = checker_fptr();
      if (response !== "") {
        Setters.msg(
            'err_'+sets_branch, 
            'В полях формы ошибка. Проверьте корректность вводимых данных.<br>'+
              response, 
            'error');
        return;
      }

      // Уверены? 
      Setters.accept_full(
          'accept_'+sets_branch, 
          'error', 
          name_processor,
          "Внимание! Ошибка может сделать систему недоступной. Применть настройки?");
      // Можно отправлять, но нужно подтверждение. По его получению вызывается функция по имени указ. в accept выше
     
    },
    
    response_processor : function (data, sets_branch, resend_fun_name) {
      var resend = CrossCuts.resend;
      try {
        var response = $.parseJSON(data);  // При неверном формате выбр. искл. и не изменит настройки
        Setters.hide_all(sets_branch);
        
        if (response['err_code'] !== '0') {
          // error occure
          Setters.msg(
              'err_'+sets_branch, 
              'При установке параметров возникала ошибка. Попробуйте еще раз применить настройки.<br>'+
                '<br><b>Дополнительная информация:</b><br> '+response['err_msg'], 
              'error');
          return;
        }
        Setters.msg(
            'msg_'+sets_branch, 
            'Действие успешно выполнено'+
            '<br>',//+data, 
            'info');
        
        function hide_success(sets_branch) {
          Setters.msg_hide('msg_'+sets_branch);
        };
        setTimeout(hide_success, 800, sets_branch);
        return;  // Все норм, выводить сообщения не о чем
        
      } catch (e) {
        Setters.hide_no_err(sets_branch);
        
        // error occure
        Setters.msg(
            'err_'+sets_branch, 
            'Запрос параметров закончился с ошибкой. '+
              'Неправильный формат принятых данных.<br>'+
              'Запрос будет повторен.</i><br><br><b>Дополнительная информация:</b><br>'+
              data+
              '<br><br>Снимок стека:<br>'+
              CrossCuts.plot_call_stack(e),
            'error');
        resend(resend_fun_name);
        return;
      }
    },
    
    rpt_request_sended : function (sets_branch) {
      Setters.msg(
          'msg_'+sets_branch, 
          'Подождите. Отправлен запрос на выполняение комманды...', 
          'info');
    }
};
/// Setters API
///
/// /// ///

/// /// ///
///
/// Branches
// Branch: settings_mon_system
function get_settings_mon_system_name() {
  return 'settings_mon_system'; 
}
function set_settings_mon_system() {
  var sets_branch = get_settings_mon_system_name();  // TODO(zaqwes): плохо, но как устр. не ясно
  var name_processor = '_send_'+get_settings_mon_system_name();
  var checker_fptr = check_settings_mon_system;
  Setters.check_and_accept_w_msg(sets_branch, checker_fptr, name_processor, 
      "Применить настройки системы мониторинга?");
}

/**
 * Вызывается по нажатию Окей или отмена в окне Accept
 * */
function _send_settings_mon_system(ok) {
  var sets_branch = get_settings_mon_system_name();
  Setters.msg_hide('accept_'+sets_branch);  // скрыть окно-вопрос
  if (ok !== 1) {
    // Просто ничего не делаем
    return;
  }
  _resend_settings_mon_system();
}

// Нужно было разбить на разовый запрос и перезапросы
function _resend_settings_mon_system() {
  var sets_branch = get_settings_mon_system_name();
  var resend_fun_name = '_resend_'+get_settings_mon_system_name();
  var request_processor = '/cgi-bin/settings.py';
  var settings_mon_system = g_readedSettings[sets_branch];
  var response_branch = {branch_name: "set_"+sets_branch};
  
  // Собираем данные для отправки, предполагается что они валидыне
  for (var item in settings_mon_system) {
    if(settings_mon_system.hasOwnProperty(item)) {
      // TODO(zaqwes): Есть зависимость от типа поля
      // заполняем поля
      if (item[0] === 't') {
        response_branch[item] = document.getElementById(item).value;
      }   
    } 
  }
    
  // Запросчик
  Setters.rpt_request_sended(sets_branch);
  $.ajax({
    type: kTypeSetRequest,
    url: request_processor,
    data: response_branch//,
    //dataType: "json"  // TODO(zaqwes): Удобно тем, что в data Объект JS!
    //timeout :
  }).done(function(data) {
    Setters.response_processor(data, sets_branch, resend_fun_name);
  }).fail(function(data) {
    Setters.set_err_handle(data, sets_branch, resend_fun_name);
  });
}
//Branch: settings_mon_system




// Branch: snmp_settings
function get_snmp_settings_name() {
  return 'snmp_settings'; 
}
function set_snmp_settings() {
  var sets_branch = get_snmp_settings_name();  // TODO(zaqwes): плохо, но как устр. не ясно
  var name_processor = '_send_'+get_snmp_settings_name();
  var checker_fptr = check_settings_mon_system;//check_snmp_settings;
  Setters.check_and_accept_w_msg(sets_branch, checker_fptr, name_processor, 
      "Приминить настройки SNMP-агента?");
}

/**
 * Вызывается по нажатию Окей или отмена в окне Accept
 * */
function _send_snmp_settings(ok) {
  var sets_branch = get_snmp_settings_name();
  Setters.msg_hide('accept_'+sets_branch);  // скрыть окно-вопрос
  if (ok !== 1) {
    // Просто ничего не делаем
    return;
  }
  _resend_snmp_settings();
}

// Нужно было разбить на разовый запрос и перезапросы
function _resend_snmp_settings() {
  var sets_branch = get_snmp_settings_name();
  var resend_fun_name = '_resend_'+get_snmp_settings_name();
  var request_processor = '/cgi-bin/settings.py';
  var snmp_settings = g_readedSettings[sets_branch];
  var response_branch = {branch_name: "set_"+sets_branch};
  
  // Собираем данные для отправки, предполагается что они валидыне
  for (var item in snmp_settings) {
    if(snmp_settings.hasOwnProperty(item)) {
      // TODO(zaqwes): Есть зависимость от типа поля
      // заполняем поля
      if (item[0] === 't') {
        response_branch[item] = document.getElementById(item).value;
      }
      
      // Comboboxes
      if (item[0] == 'o') {
        var recoder_map = {0:'public', 1:'private'};
        var country = document.getElementById(item);
        
        if (country.options[0].selected) {
          response_branch[item] = recoder_map[0];
        }
        if (country.options[1].selected) {
          response_branch[item] = recoder_map[1];
        }
      }
      
      // Checkboxes
      if (item[0] == 'c') {
        if(document.getElementById(item).checked) {
          response_branch[item] = 1; 
        } else {
          response_branch[item] = 0; 
        }
      }
      
    } 
  }
    
  // Запросчик
  Setters.rpt_request_sended(sets_branch);
  $.ajax({
    type: kTypeSetRequest,
    url: request_processor,
    data: response_branch//,
    //dataType: "json"  // TODO(zaqwes): Удобно тем, что в data Объект JS!
    //timeout :
  }).done(function(data) {
    Setters.response_processor(data, sets_branch, resend_fun_name);
  }).fail(function(data) {
    Setters.set_err_handle(data, sets_branch, resend_fun_name);
  });
}
//Branch: snmp_settings

//Branch: access_settings
function get_access_settings_name() {
  return 'access_settings'; 
}
function set_access_settings() {
  var sets_branch = get_access_settings_name();  // TODO(zaqwes): плохо, но как устр. не ясно
  var name_processor = '_send_'+get_access_settings_name();
  var checker_fptr = check_settings_mon_system;//check_access_settings;
  Setters.check_and_accept_d_msg(sets_branch, checker_fptr, name_processor,
      "Внимание! Ошибка может сделать систему недоступной. Изменить пароль?"
    );
}

/**
 * Вызывается по нажатию Окей или отмена в окне Accept
 * */
function _send_access_settings(ok) {
  var sets_branch = get_access_settings_name();
  Setters.msg_hide('accept_'+sets_branch);  // скрыть окно-вопрос
  if (ok !== 1) {
    // Просто ничего не делаем
    return;
  }
  _resend_access_settings();
}

// Нужно было разбить на разовый запрос и перезапросы
function _resend_access_settings() {
  var sets_branch = get_access_settings_name();
  var resend_fun_name = '_resend_'+get_access_settings_name();
  var request_processor = '/cgi-bin/settings.py';
  var access_settings = g_readedSettings[sets_branch];
  var response_branch = {branch_name: "set_"+sets_branch};
  
  // Собираем данные для отправки, предполагается что они валидыне
  for (var item in access_settings) {
    if(access_settings.hasOwnProperty(item)) {
      // TODO(zaqwes): Есть зависимость от типа поля
      // заполняем поля
      if (item[0] === 't') {
        response_branch[item] = document.getElementById(item).value;
      }
    } 
  }
    
  // Запросчик
  Setters.rpt_request_sended(sets_branch);
  $.ajax({
    type: kTypeSetRequest,
    url: request_processor,
    data: response_branch//,
    //dataType: "json"  // TODO(zaqwes): Удобно тем, что в data Объект JS!
    //timeout :
  }).done(function(data) {
    Setters.response_processor(data, sets_branch, resend_fun_name);
  }).fail(function(data) {
    Setters.set_err_handle(data, sets_branch, resend_fun_name);
  });
}
//Branch: access_settings


//Branch: coupled_sys_settings
function get_coupled_sys_settings_name() {
  return 'coupled_sys_settings'; 
}
function set_coupled_sys_settings() {
var sets_branch = get_coupled_sys_settings_name();  // TODO(zaqwes): плохо, но как устр. не ясно
var name_processor = '_send_'+get_coupled_sys_settings_name();
var checker_fptr = check_settings_mon_system;//check_coupled_sys_settings;
Setters.check_and_accept_danger(sets_branch, checker_fptr, name_processor);
}

/**
* Вызывается по нажатию Окей или отмена в окне Accept
* */
function _send_coupled_sys_settings(ok) {
var sets_branch = get_coupled_sys_settings_name();
Setters.msg_hide('accept_'+sets_branch);  // скрыть окно-вопрос
if (ok !== 1) {
  // Просто ничего не делаем
  return;
}
_resend_coupled_sys_settings();
}

//Нужно было разбить на разовый запрос и перезапросы
function _resend_coupled_sys_settings() {
  var sets_branch = get_coupled_sys_settings_name();
  var resend_fun_name = '_resend_'+get_coupled_sys_settings_name();
  var request_processor = '/cgi-bin/settings.py';
  var coupled_sys_settings = g_readedSettings[sets_branch];
  var response_branch = {branch_name: "set_"+sets_branch};
  
  // Собираем данные для отправки, предполагается что они валидыне
  for (var item in coupled_sys_settings) {
    if(coupled_sys_settings.hasOwnProperty(item)) {
      // TODO(zaqwes): Есть зависимость от типа поля
      // заполняем поля
      if (item[0] === 't') {
        response_branch[item] = document.getElementById(item).value;
      }
    } 
  }
    
  // Запросчик
  Setters.rpt_request_sended(sets_branch);
  $.ajax({
    type: kTypeSetRequest,
    url: request_processor,
    data: response_branch//,
    //dataType: "json"  // TODO(zaqwes): Удобно тем, что в data Объект JS!
    //timeout :
  }).done(function(data) {
    Setters.response_processor(data, sets_branch, resend_fun_name);
  }).fail(function(data) {
    Setters.set_err_handle(data, sets_branch, resend_fun_name);
  });
}
//Branch: coupled_sys_settings



//Branch: light_settings
function get_light_settings_name() {
return 'light_settings'; 
}
function set_light_settings() {
var sets_branch = get_light_settings_name();  // TODO(zaqwes): плохо, но как устр. не ясно
var name_processor = '_send_'+get_light_settings_name();
var checker_fptr = check_settings_mon_system;//check_light_settings;
Setters.check_and_accept(sets_branch, checker_fptr, name_processor);
}

/**
* Вызывается по нажатию Окей или отмена в окне Accept
* */
function _send_light_settings(ok) {
var sets_branch = get_light_settings_name();
Setters.msg_hide('accept_'+sets_branch);  // скрыть окно-вопрос
if (ok !== 1) {
  // Просто ничего не делаем
  return;
}
_resend_light_settings();
}

//Нужно было разбить на разовый запрос и перезапросы
function _resend_light_settings() {
  var sets_branch = get_light_settings_name();
  var resend_fun_name = '_resend_'+get_light_settings_name();
  var request_processor = '/cgi-bin/settings.py';
  var light_settings = g_readedSettings[sets_branch];
  var response_branch = {branch_name: "set_"+sets_branch};
  
  // Собираем данные для отправки, предполагается что они валидыне
  for (var item in light_settings) {
    if(light_settings.hasOwnProperty(item)) {
      // TODO(zaqwes): Есть зависимость от типа поля
      // заполняем поля
      if (item[0] === 't') {
        response_branch[item] = document.getElementById(item).value;
      }
    } 
  }
    
  // Запросчик
  Setters.rpt_request_sended(sets_branch);
  $.ajax({
    type: kTypeSetRequest,
    url: request_processor,
    data: response_branch//,
    //dataType: "json"  // TODO(zaqwes): Удобно тем, что в data Объект JS!
    //timeout :
  }).done(function(data) {
    Setters.response_processor(data, sets_branch, resend_fun_name);
  }).fail(function(data) {
    Setters.set_err_handle(data, sets_branch, resend_fun_name);
  });
}
//Branch: light_settings

//Branch: network_adaptor_system
function get_network_adaptor_system_name() {
  return 'network_adaptor_system'; 
}
function set_network_adaptor_system() {
  var sets_branch = get_network_adaptor_system_name();  // TODO(zaqwes): плохо, но как устр. не ясно
  var name_processor = '_send_'+get_network_adaptor_system_name();
  var checker_fptr = check_settings_mon_system;//check_network_adaptor_system;
  Setters.check_and_accept_danger(sets_branch, checker_fptr, name_processor);
}

/**
* Вызывается по нажатию Окей или отмена в окне Accept
* */
function _send_network_adaptor_system(ok) {
  var sets_branch = get_network_adaptor_system_name();
  Setters.msg_hide('accept_'+sets_branch);  // скрыть окно-вопрос
  if (ok !== 1) {
    // Просто ничего не делаем
    return;
  }
  _resend_network_adaptor_system();
}

//Нужно было разбить на разовый запрос и перезапросы
function _resend_network_adaptor_system() {
  var sets_branch = get_network_adaptor_system_name();
  var resend_fun_name = '_resend_'+get_network_adaptor_system_name();
  var request_processor = '/cgi-bin/settings.py';
  var network_adaptor_system = g_readedSettings[sets_branch];
  var response_branch = {branch_name: "set_"+sets_branch};
  
  // Собираем данные для отправки, предполагается что они валидыне
  for (var item in network_adaptor_system) {
    if(network_adaptor_system.hasOwnProperty(item)) {
      // TODO(zaqwes): Есть зависимость от типа поля
      // заполняем поля
      if (item[0] === 't' && (item.indexOf('current') == -1)) {
        response_branch[item] = document.getElementById(item).value;
      }
      
      // Radiobuttons
      if (item[0] == 'r') {
        if (item === 'rdhcp_on') {
            var country = document.getElementById(item);
            if (country.checked) {
              response_branch[item] = "1";
            } else {
              response_branch[item] = "0";
            }
        }
        if (item === 'rauto_dns_on') {
          var country = document.getElementById(item);
          if (country.checked) {
            response_branch[item] = "1";
          } else {
            response_branch[item] = "0";
          }
        }
      }  // if (item[0] == 'r') {
    } 
  }
    
  // Запросчик
  Setters.rpt_request_sended(sets_branch);
  $.ajax({
    type: kTypeSetRequest,
    url: request_processor,
    data: response_branch//,
    //dataType: "json"  // TODO(zaqwes): Удобно тем, что в data Объект JS!
    //timeout :
  }).done(function(data) {
    Setters.response_processor(data, sets_branch, resend_fun_name);
  }).fail(function(data) {
    Setters.set_err_handle(data, sets_branch, resend_fun_name);
  });
}
//Branch: network_adaptor_system


// Actons
function fake_recall() {
  
  
}

/// Template 
//Branch: action
function restart_server() {
  var sets_branch = get_action_name();  // TODO(zaqwes): плохо, но как устр. не ясно
  var name_processor = '_send_'+get_action_name();
  var checker_fptr = check_settings_mon_system;//check_action;
  Setters.check_and_accept_w_msg(sets_branch, checker_fptr, name_processor,
      '<b>Перезагрузить сервер?</b> Сервер станет недоступным некоторое время.');
}

function get_action_name() {
  return 'action'; 
}

function _send_action(ok) {
  var sets_branch = get_action_name();
  var resend_fun_name = 'fake_recall';
  var request_processor = '/cgi-bin/settings.py';
  var sets_branch = get_action_name();
  
  Setters.msg_hide('accept_'+sets_branch);  // скрыть окно-вопрос
  if (ok !== 1) {
    // Просто ничего не делаем
    return;
  }
    
  // Запросчик
  Setters.rpt_request_sended(sets_branch);
  $.ajax({
    type: 'POST',
    url: request_processor,
    data: {action: 'restart'}//,
    //dataType: "json"  // TODO(zaqwes): Удобно тем, что в data Объект JS!
    //timeout :
  }).done(function(data) {
    Setters.response_processor(data, sets_branch, resend_fun_name);
  }).fail(function(data) {
    Setters.set_err_handle(data, sets_branch, resend_fun_name);
  });
}
//Branch: action

//Branch: shutdown
function shutdown_server() {
  var sets_branch = get_shutdown_name();  // TODO(zaqwes): плохо, но как устр. не ясно
  var name_processor = '_send_'+get_shutdown_name();
  var checker_fptr = check_settings_mon_system;//check_shutdown;
  Setters.check_and_accept_danger(sets_branch, checker_fptr, name_processor);
}

function get_shutdown_name() {
  return 'shutdown'; 
}

function _send_shutdown(ok) {
  var sets_branch = get_shutdown_name();
  var resend_fun_name = 'fake_recall';
  var request_processor = '/cgi-bin/settings.py';
  var sets_branch = get_shutdown_name();
  
  Setters.msg_hide('accept_'+sets_branch);  // скрыть окно-вопрос
  if (ok !== 1) {
    // Просто ничего не делаем
    return;
  }
    
  // Запросчик
  Setters.rpt_request_sended(sets_branch);
  $.ajax({
    type: 'POST',
    url: request_processor,
    data: {action: 'shutdown'}//,
    //dataType: "json"  // TODO(zaqwes): Удобно тем, что в data Объект JS!
    //timeout :
  }).done(function(data) {
    Setters.response_processor(data, sets_branch, resend_fun_name);
  }).fail(function(data) {
    Setters.set_err_handle(data, sets_branch, resend_fun_name);
  });
}
//Branch: shutdown
// Actons

/// Getters API
///
/// /// ///

function test_checkbox(value) {
  /*
  // Comboboxes
  var country = document.getElementById("osnmp_rd_access");
  country.options[1].selected = true;
  
  var recoder_map = {public:0, private:1};*/
  
  /*
  // Checkboxes 
  var checker = document.getElementById('cip_accessed_0');
  
  checker.checked = false;
  */
  //alert(value);
  
}















