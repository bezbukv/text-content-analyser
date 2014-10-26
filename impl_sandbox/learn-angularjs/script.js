// create the module and name it scotchApp
// also include ngRoute for all our routing needs
var scotchApp = angular.module('scotchApp', []);

// create the controller and inject Angular's $scope
scotchApp.controller('mainController', function($scope) {
  // create a message to display in our view
  $scope.message = 'Everyone come and see how good I look!';
  $scope.visiblePage = new PageData();

  $scope.changeName = function() {
    $scope.visiblePage.name = "hello";
  };
});


// About: Данные по странице. Могут доформировываться дополнительными запросами.
//   Фактически json данные.
function PageData() {
  var self = this;  // по-моему так лечятся некоторые проблемы с this
  this.id = 0;
  this.name = "noName";
  
  // Думается будут динамическими.
  this.currentWord = "";
  this.currentContentItems = [];
}

