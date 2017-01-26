'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('LoginCtrl', function($scope, Restangular) {


        $scope.logIn =  function(form) {
            var logInAccount = {username: $scope.username, password: $scope.password};
            console.log(logInAccount);
            Restangular.all('auth/login').post(logInAccount);
        };

    });

});
