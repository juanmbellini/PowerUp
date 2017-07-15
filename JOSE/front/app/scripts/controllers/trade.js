define(['trademygame', '../services/authService'], function(trademygame) {

    'use strict';
    trademygame.controller('trade', function($scope, Restangular, $routeParams, $location, $window, authService, toastr, $translate) {

      $scope.userId = authService.loggedId();
      $scope.selectedOffer = undefined;

      Restangular.one('offers', $routeParams.id).get().then(function(data) {
        var offer = data.plain();
        $scope.offererId = offer.userId;
        Restangular.one('products', data.prodId).get().then(function(data) {
             $scope.prod = data.plain();
        });
      });

      Restangular.all('offers').getList({userId: $scope.userId}).then(function(data) {
        var offersArray = data.plain();
        var matrix = [];
        var i,k;

        for(i=0, k=-1; i<offersArray.length; i++){
          if( i%3 ==0){
            k++;
            matrix[k]=[];
          }
          matrix[k].push(Restangular.stripRestangular(offersArray[i]));
        }
          $scope.offers = matrix;
      });

      $scope.tradeForm = {
        "offererOfferId": $routeParams.id,
        "interestedOfferId": undefined
      }

      $scope.trade = function(){
        if($scope.tradeForm.interestedOfferId != undefined){
          Restangular.all('trades').post($scope.tradeForm).then(function(data){
            $window.location.href = "/#/user/" + $scope.userId + "?tradeoffers";
            toastr.success($translate.instant('trade_succesfulltrade'),$translate.instant('succes_succes'));
          },function(response){
            toastr.error($translate.instant('trade_failcreating'),$translate.instant('error_error'));
          });
        }
      }

      $scope.selectImage = function(id) {
        $("#offer" + $scope.tradeForm.interestedOfferId).css('border','dotted');
        $scope.tradeForm.interestedOfferId = id;
        $("#offer" + id).css({'border-color':'black', 'border-style': 'solid', 'border-width':'5px'});
      }

    });

});
