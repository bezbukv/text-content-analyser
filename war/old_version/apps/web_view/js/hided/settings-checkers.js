// Checkers Mon sys settings //
// branch: settings_mon_system
// Auto generate by: D:\home\lugansky-igor\tmitter-web-service-win\scripts\id_extractor.py
function check_settings_mon_system() {
  var correct = false;

  // checker
  var check_tfrw_low = function(value) {
    return true;
  }
  correct = check_tfrw_low(document.getElementById("tfrw_low").value);
  if (!correct) {
    return "Ошибка при вводе нижней границы КБВ.";
  }

  // checker
  var check_tdevice_id_snmp = function(value) {
    return true;
  }
  correct = check_tdevice_id_snmp(document.getElementById("tdevice_id_snmp").value);
  if (!correct) {
    return "msg";
  }

  // checker
  var check_tdevice_type_snmp = function(value) {
    return true;
  }
  correct = check_tdevice_type_snmp(document.getElementById("tdevice_type_snmp").value);
  if (!correct) {
    return "msg";
  }

  // checker
  var check_tfrw_high = function(value) {
    return true;
  }
  correct = check_tfrw_high(document.getElementById("tfrw_high").value);
  if (!correct) {
    return "msg";
  }

  // checker
  var check_trs_addr = function(value) {
    //if (value == '10') return true;
    //else return false;
    return true;
  }
  correct = check_trs_addr(document.getElementById("trs_addr").value);
  if (!correct) {
    return "Ошибка при вводе адреса передатчика.";
  }

  // checker
  var check_tterm_low = function(value) {
    return true;
  }
  correct = check_tterm_low(document.getElementById("tterm_low").value);
  if (!correct) {
    return "msg";
  }

  // checker
  var check_tpower_high = function(value) {
    return true;
  }
  correct = check_tpower_high(document.getElementById("tpower_high").value);
  if (!correct) {
    return "msg";
  }

  // checker
  var check_tchannal_idx = function(value) {
    return true;
  }
  correct = check_tchannal_idx(document.getElementById("tchannal_idx").value);
  if (!correct) {
    return "msg";
  }

  // checker
  var check_tpower_low = function(value) {
    return true;
  }
  correct = check_tpower_low(document.getElementById("tpower_low").value);
  if (!correct) {
    return "msg";
  }

  // checker
  var check_tterm_high = function(value) {
    return true;
  }
  correct = check_tterm_high(document.getElementById("tterm_high").value);
  if (!correct) {
    return "msg";
  }

  // checker
  var check_tperiod_snmp = function(value) {
    return true;
  }
  correct = check_tperiod_snmp(document.getElementById("tperiod_snmp").value);
  if (!correct) {
    return "msg";
  }
  return "";
}  // function check_settings_mon_system
// Checkers Mon sys settings //