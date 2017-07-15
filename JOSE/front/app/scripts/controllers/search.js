requirejs(['filters']);

define(['trademygame','../services/productService'], function(trademygame) {

    'use strict';
    trademygame.controller('search', function($scope, $routeParams, $location, $window, $http, productService, Restangular) {

      $scope.page = $routeParams.page;
      $scope.name = $routeParams.name;
      $scope.searchPlatform = $routeParams.platform;
      $scope.searchGenre = $routeParams.genre;
      $scope.searchEsrb = $routeParams.esrb;
      $scope.searchMpsp = $routeParams.mpsp;
      $scope.doneSearch = false;
      $scope.pagination = {};

      if($scope.page == undefined){
        $scope.page = 1;
      }

      Restangular.one('products/amount').get({page: $scope.page, name: $routeParams.name,
          platform: $routeParams.platform,genre: $routeParams.genre,
          esrb: $routeParams.esrb, mpsp: $routeParams.mpsp}).then(function(data) {
           var amounts = data;
           $scope.pagination = {
             pagesAmount: amounts.pagesAmount,
             prodAmount: amounts.amount,
             maxSizePage: 5
           };
      });



      Restangular.all('products').getList({page: $scope.page, name: $routeParams.name,
            platform: $routeParams.platform,genre: $routeParams.genre,
            esrb: $routeParams.esrb, mpsp: $routeParams.mpsp}).then(function(data){
            var matrix = [];
            var i,k;

            for(i=0, k=-1; i<data.length;i++){
                if (i % 3 === 0) {
                    k++;
                    matrix[k] = [];
                }
                matrix[k].push(Restangular.stripRestangular(data[i]));
            }
            $scope.products = matrix;
            $scope.doneSearch = true;
        });


      Restangular.all('platforms').getList().then(function(data) {
        $scope.platforms = data;
      });

      Restangular.all('products/esrb').getList().then(function(data) {
        $scope.esrbs = data;
      });

      Restangular.all('products/genre').getList().then(function(data) {
        $scope.genres = data;
      });

      Restangular.all('products/mpsp').getList().then(function(data) {
        $scope.mpsps = data;
      });


      $scope.pageChanged = function(p) {
        $location.search('page', p);
      };

      $scope.performSearch = function(){
        var location = '/#/search?page=1';

        if($scope.name != undefined){
          location += "&name=" + $scope.name;
        }
        if($scope.searchPlatform != undefined){
          location += "&platform=" + $scope.searchPlatform;
        }
        if($scope.searchGenre != undefined){
          location += "&genre=" + $scope.searchGenre;
        }
        if($scope.searchEsrb != undefined){
          location += "&esrb=" + $scope.searchEsrb;
        }
        if($scope.searchMpsp != undefined){
          location += "&mpsp=" + $scope.searchMpsp;
        }

        $window.location.href = location;
      }

      $scope.removeFilters = function(){
        var location = '/#/search?page=1';

        if($scope.name != undefined){
          location += "&name=" + $scope.name;
        }

        $window.location.href = location;
      }

    });

});
