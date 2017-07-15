define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.controller('offers', function($scope, $routeParams, $location, Restangular) {

      Restangular.one('products', $routeParams.id).one('offers/amount').get().then(function(data) {
           var amounts = data;
           $scope.pagination = {
             pagesAmount: amounts.pagesAmount,
             offerAmount: amounts.amount,
             maxSizePage: 5
           };
      });
      $scope.prodId = $routeParams.id;


      $scope.page = $routeParams.page;
      $scope.prodId = $routeParams.id;

      Restangular.one('products', $routeParams.id).one('offers').getList().then(function(data) {
           $scope.offers = data;
      });


      $scope.getTimes=function(n){
        return new Array(n);
      };

      $scope.pageChanged = function(p) {
        $location.search('page', p);
      };


    });

});
