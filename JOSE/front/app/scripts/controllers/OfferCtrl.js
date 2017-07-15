define(['trademygame', '../services/authService'], function(trademygame) {

    'use strict';
    trademygame.controller('OfferCtrl', function($scope, $routeParams, Restangular, authService) {

        $scope.loadingOffers = true;
      Restangular.one('offers', $routeParams.id).get().then(function(data) {
           $scope.offer = data;
          $scope.loadingOffers = false;
          Restangular.one('products', $scope.offer.prodId).get().then(function(data) {
                $scope.product = data;
           });
      }, function (data) {
          $scope.loadingOffers = false;
      });

      $scope.loggedId = authService.loggedId();

      Restangular.one('offers', $routeParams.id).getList('questions').then(function(data) {
          $scope.questions = data;
      });


      $scope.offerId = $routeParams.id;

      $scope.questionForm = {
        "question": undefined
      }

      $scope.answerForm = {
        "answer": undefined,
        "questionId": ""
      }

      $scope.newQuestion = function(){
        //  /offers/{offerId}/questions POST
        Restangular.one("offers", $routeParams.id).all('questions').post($scope.questionForm).then(function(data){
          $scope.questions.unshift(data);
          $scope.questionForm.question = undefined;
        });
      }

      $scope.answerQuestion = function(i){
        $scope.answerForm.answer = document.getElementById("answer" + i).value;
        $scope.answerForm.questionId = document.getElementById("questionIdForm" + i).value;
        //  /offers/{offerId}/questions PUT
        Restangular.one("offers", $routeParams.id).one('questions').customPUT($scope.answerForm).then(function(data){
          $scope.questions[i] = data.plain();

        });
      }

      $scope.deleteQuestion = function(i){
        Restangular.one("offers/questions", $scope.questions[i].id).remove().then(function(data){
          document.getElementById("q" + i).remove();
          Restangular.one('offers', $routeParams.id).getList('questions').then(function(data) {
            $scope.questions = data;
          });
        });
      }

    });

});
