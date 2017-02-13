'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('RegisterCtrl', function($scope) {

        console.log('hola from register');

        $scope.register = function(form) {

            console.log($scope.userToSubmit);


            // Referencia:
            // var logInAccount = {username: $scope.username, password: $scope.password};
            // console.log(logInAccount);
            // Restangular.all('auth/login').post(logInAccount).then(function (data) {
            //     LogInService.setLoggedInStatus(true);
            //     // LogInService.setLoggedUser
            // }, function() {
            //     console.log('There was an error in logIn');
            // });



        };
    });

});