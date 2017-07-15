define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.controller('reviews', function($scope, $routeParams, $location, Restangular) {

      Restangular.one('products', $routeParams.id).one('reviews/amount').get().then(function(data) {
           var amounts = data;
           $scope.pagination = {
             pagesAmount: amounts.pagesAmount,
             reviewAmount: amounts.amount,
             maxSizePage: 5
           };
      });

      $scope.page = $routeParams.page;
      $scope.prodId = $routeParams.id;

      Restangular.one('products', $routeParams.id).one('reviews').getList().then(function(data) {
           $scope.reviews = data;
      });

      $scope.pageChanged = function(p) {
        $location.search('page', p);
      };

    });



});
