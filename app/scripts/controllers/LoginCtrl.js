'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('LoginCtrl', function($scope, Restangular, LogInService, $location) {


        $scope.logIn = function(form) {
            var logInAccount = {username: $scope.username, password: $scope.password};
            console.log(logInAccount);
            Restangular.all('auth/login').post(logInAccount).then(function (data) {
                LogInService.setLoggedInStatus(true);
                $location.search();
                $location.path('');
                // LogInService.setLoggedUser
            }, function() {
                console.log('There was an error in logIn');
            });



        };

    });

});
