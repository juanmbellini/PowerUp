define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.controller('filter', function($scope, $routeParams, Restangular, $location, $window, $http) {

      $scope.query_name = $routeParams.name;

      Restangular.all('platforms').getList().then(function(data) {
          $scope.platforms = data.plain();
      });

      Restangular.all('products/mpsp').getList().then(function(data) {
          $scope.mpsps = data.plain();
      });

      Restangular.all('products/genre').getList().then(function(data) {
          $scope.genres = data.plain();
      });

      Restangular.all('products/esrb').getList().then(function(data) {
          $scope.esrbs = data.plain();
      });

      $scope.searchPlatform;
      $scope.searchGenre;
      $scope.searchMpsp;
      $scope.searchEsrb;

      $scope.performSearch = function(){
        var location = '/#/search?page=1';

        if($scope.query_name != undefined){
          location += "&name=" + encodeURIComponent($scope.query_name);
        }
        if($scope.searchPlatform != undefined){
          location += "&platform=" + encodeURIComponent($scope.searchPlatform);
        }
        if($scope.searchGenre != undefined){
          location += "&genre=" + encodeURIComponent($scope.searchGenre);
        }
        if($scope.searchEsrb != undefined){
          location += "&esrb=" + encodeURIComponent($scope.searchEsrb);
        }
        if($scope.searchMpsp != undefined){
          location += "&mpsp=" + encodeURIComponent($scope.searchMpsp);
        }

        $window.location.href = location;
      }

    });

});
