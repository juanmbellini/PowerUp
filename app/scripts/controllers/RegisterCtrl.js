'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('RegisterCtrl', function($scope) {

        $scope.register = function(form) {
            // var logInAccount = {username: $scope.username, password: $scope.password};
            // console.log(logInAccount);

            console.log($scope.userToSubmit);

            // Restangular.all('auth/login').post(logInAccount).then(function (data) {
            //     LogInService.setLoggedInStatus(true);
            //     // LogInService.setLoggedUser
            // }, function() {
            //     console.log('There was an error in logIn');
            // });



        };
    });

});