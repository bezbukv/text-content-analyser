
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

/*
<script type="text/javascript">
$(function() {
 var data, data1, options, chart;
   data1 = [[1, 4], [2,5], [3, 6], [4,9]];
   var data2 = [], data3 = [];

   data = [
       {data:data1, label:"eq", lines:{show:true}, points:{show:true}},
       data2, data3];

   options = {};

   // Просто сохранять страницы толку нет, т.к. она изменена js'ом
   $(document).ready(function() {
     chart1 = $.plot($("#placeholder"), data, options);
   });
});
</script>
* */


function get_line_template(id, content) {
    return '<div class="one_line_container">'+
        '  <div class="first_one8"><div class="parent">'+
        '    <div id="'+id+'" class="child">'+content+'</div>'+
        '    <div class="helper"></div>'+
        '  </div></div>'+
        '</div>';
}