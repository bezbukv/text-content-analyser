// Zoom:
//   - До определенного момента кружочки не должны выключаться. Например при zoom = 1, когда показываются все элементы.
$(function() {
  function showTooltip(x, y, contents) {
    $("<div id='tooltip'>" + contents + "</div>").css({
      position: "absolute",
      display: "none",
      top: y + 5,
      left: x + 5,
      border: "1px solid #fdd",
      padding: "2px",
      "background-color": "#fee",
      opacity: 0.80
    }).appendTo("body").fadeIn(200);
  }

  var previousPoint = null;
  $("#placeholder").bind("plothover", function (event, pos, item) {
    // Вывод подсказки по элементу
    if (item) {
      if (previousPoint != item.dataIndex) {

        previousPoint = item.dataIndex;

        $("#tooltip").remove();
        var x = item.datapoint[0].toFixed(2),
        y = item.datapoint[1].toFixed(2);

        showTooltip(item.pageX, item.pageY, g_map[Math.floor(x)]);
      }
    } else {
      $("#tooltip").remove();
      previousPoint = null;            
    }
  });
});

function get_data() {
  var request_processor = '/app';
  var response_branch = {'name':'get_axis'};
  var jqxhr = $.get(request_processor, response_branch)
    .success(function(data) {
      process_response(data);
    })
    .error(function(data) { 
      if (data.status == 404) {
            /*err_action(
                'Запрос к несуществующей странице. '+
                'Ошибка 404. Запрос будет повторен.');*/
          }
          if (data.status == 500) {
            /*err_action(
                'Внутренняя ошибка сервера. Ошибка 500. '+
                'Запрос будет повторен.');*/
          }
          if (data.status == 0) {
            /*err_action(
                'Отсутствует связь с сервером. '+
                'Запрос будет повторен.');*/
          }
    });
}

var g_map = {}

function process_response(data) {

  var getted_axises = $.parseJSON(data);
  
  // Нужно усреднять данные на отрезках, а через zoom увеличивать.
  var save = 0;
  var zoomed_data = [];
  var catch_every = 1;
  for(var i = 0; i < getted_axises.length; ++i) {
    ///if (i%catch_every === 0) {
      tmp = []
      for(var name in getted_axises[i])
      {
          if (getted_axises[i].hasOwnProperty(name))
          {
              tmp.push(i);
              tmp.push(getted_axises[i][name]);
          }
          g_map[tmp[0]] = 'Position : '+tmp[0]+'/'+name+'/'+tmp[1]
      }
      zoomed_data.push(tmp);

    //}
  }

  // Функция рисования
  var plot = $.plot("#placeholder", [{ data: zoomed_data, label: "uniform(x)"}], {
    series: {
      lines: {show: true},
      points: {show: false}
    },
    grid: {
      hoverable: true,
      clickable: true
    }
  });
}

function get_line_template(id, content) {
    return '<div class="one_line_container">'+
        '  <div class="first_one8"><div class="parent">'+
        '    <div id="'+id+'" class="child">'+content+'</div>'+
        '    <div class="helper"></div>'+
        '  </div></div>'+
        '</div>';
}

function get_list_items() {
    var list_names = ['IM', 'AA'];

    // Будет замкнута
    var i = 0;
    for (i = 0; i < 2 ; ++i) {
        // TODO():А что если имена одинаковые?
        $('#list_nodes_container').append(get_line_template(list_names[i], list_names[i]));


        // Соединяем с обработчиком нажатия
        // Нужно связать не на прямую, а на Item в линии - он шире
        var bind_obj = { click:
            (function(x) {
                return function() {
                    $('#'+list_names[x]).val("Clicked");

                    //alert($('#'+list_names[x]).text())

                    var request_processor = '/app';
                    var response_branch = {'name':'get_axis'};
                    $.ajax({
                            url: request_processor,
                            type: "GET",
                            data: response_branch})
                    .done(function(data) {
                        //process_response(data);
                        alert('Done');
                    })
                    .error(function(data) {
                        if (data.status == 404) {}
                        if (data.status == 500) {}
                        if (data.status == 0) {}
                    });
                }
            })(i)
        };
        $('#'+list_names[i]).parent(".parent").parent(".first_one8").bind(bind_obj);
    }
}
