function _addToMainPanel( msg ) {
  $('#main').append( msg );
}
  
function show() {
  $.ajax({  
    //url: "../../cgi-bin/test.py?name=liq", 
    url: "../../cgi-bin/ajx_tmn.exe", 
    cache: false,  
    success: function(data){  
      _addToMainPanel('<br><br>'+data+'<br>');
      try  {
        var json_response = $.parseJSON(data);
        
        // форматированный вывод, только для alert? или console тоже?
        //var str = JSON.stringify(json_response, "", 4);
        //alert(str)
      }
      catch (e) {
        // возможно ошибка в py-скрипте
        _addToMainPanel('Error:<br>'+data+'<br>');
      }
      
    }  
  });  
  
  /* мешают друг-другу, нужна наверное очередь
  // если расомментить, то выводит в случ. порядке, а post, а может быть и get иногда пропускаются!
  $.post(
    '../../cgi-bin/test.py?name=log&pages=1', 
    //{  },  // это похоже что-то особое
    function(data) {
      try  {
        var json_response = $.parseJSON(data);
        var str = JSON.stringify(json_response, "", 4);

        _addToMainPanel( json_response.data );
      }
      catch (e) {
        // возможно ошибка в py-скрипте
        _addToMainPanel(data);
      }
    }
  );
  */
}

function add_row(type_msg) {
  var log = $("#journal").append('<tr class="'+type_msg+'">'+
    //'<td>'+
    //'       <input type="checkbox" class="checkbox app_checkbox">'+
    //'  </td>'+
    //' <td>TB - Fridy</td>'+
    ' <td></td>'+
    ' <td>01/04/2012</td></tr>');
}

function remove_row() {

}

function on_up() {
  // Добавление ряда
     
  // Поочередный обход строк
  var inArray = ['asdfasdf', 'asdfassgf0df', 'afdsasdfdasdfasddddddddddddddddddddddddddddddddd fdfdf'];
  var j = 0;
  $('#journal >tbody >tr').each(function(index, item){
    $(this).children("td").each(function(index) { 
      if (index == 0) {
        if (j < inArray.length) {
          $(this).text(inArray[j]);
        }
      }
      });
    j++;
  });
}

function on_down() {
  var err = $("td").css("font-size", "100%");
}

/*function init_view() {
  for (var i = 0; i < 10; i++) {
    if (i%2 == 0) {
      add_row('info');
    } else {
      add_row('');
    }
  }
}*/

function get_row_template(id) {

}

function _add_row() {
  var one_row_template = 
'<div class="one_line_container">'+
'    <div class="first_one4"><div class="parent">'+
'      <div class="child">'+
'        <span class="text_highlight">Граница</span> Норма мощности, (-/+) %'+
'      </div><div class="helper"></div>'+
'    </div></div>'+
'    '+
'    <div class="one1"><div class="parent">'+
'      <div class="child">'+
'        <input type="text" id="tpower_low" class="input_field"/>'+
'      </div><div class="helper"></div>'+
'    </div></div>'+
'    '+
'    <div class="one1"><div class="parent">'+
'      <div class="child">'+
'        <input type="text" id="tpower_high" class="input_field"/>'+
'      </div><div class="helper"></div>'+
'    </div></div>'+
'    </div>'

  $('#journal_tbl').append(one_row_template);
}