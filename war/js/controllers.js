var phonecatApp = angular.module('phonecatApp', []);
 
phonecatApp.controller('PhoneListCtrl', function ($scope, $http) {
  $scope.translates = [];  // Пока не подключено

  $scope.showSamples = function() {
     $scope.contextSamplesHided = false;
     $scope.translateHided = true;
  };

  $scope.showTranslates = function() {
     $scope.contextSamplesHided = true;
     $scope.translateHided = false;
  };
  
  $scope.getNewWord = function() {
	  $http.get('/pkg').success(function(data) {
		  $scope.word = data.word;
		  $scope.samples = data.sentences;
		  $scope.yourName = '';
	  });
  };
  
  $scope.getNewWord();

  // Сперва контекст
  $scope.contextSamplesHided = false;
  $scope.translateHided = true; 
  
  $scope.orderProp = 'age';
});