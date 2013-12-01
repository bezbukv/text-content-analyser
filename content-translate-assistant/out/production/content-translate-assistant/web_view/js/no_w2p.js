function on_click() {
    var success = function(data) {
        alert('Clicked');
    }
    
    
    //var jqxhr = $.getJSON("http://127.0.0.1:8000/myapp/default/myvalues.json", function(data) {
    var jqxhr = $.getJSON("/myapp/default/myvalues.json", function(data) {
      alert(data['values']);
    })
    .success(function() { 
        //alert("second success"); 
     })
    .error(function(data) { 
        alert("error"); 
        //alert(data);
        })
    .complete(function() { 
        //alert("complete"); 
    });
}

function init() {
  $(document).ready(function(){
    $('#scrollbar1').tinyscrollbar();  
  });
}