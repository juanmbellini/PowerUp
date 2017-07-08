'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('RegisterCtrl', function($scope, $location, $log, AuthService, Restangular) {

        /**
         * Register User and returns true if it could be registered. False if not.
         * @param data
         */
        $scope.submitted = false;
        $scope.register = function(form) {
            $scope.submitted = true;
            console.log($scope.userToSubmit);
            if (validate($scope.userToSubmit)) {
                var user = {username: $scope.userToSubmit.username, password: $scope.userToSubmit.password, email: $scope.userToSubmit.email};
                Restangular.all('users').post(user).then(function (response) {
                    AuthService.authenticate(user.username, user.password, function() {
                            $location.search();
                            $location.path('');
                        },
                        function(error) {
                            // TODO do something more useful, e.g. show the error
                            $log.error('There was an error logging in: ', error);
                            $location.search();
                            $location.path('login');
                        });
                }, function (response) {
                    $log.error('There was an error registering user', response);
                    return false;
                });
            }
        };



        function validate(userToSubmit) {

            return (userToSubmit && userToSubmit.username && userToSubmit.email && userToSubmit.password && userToSubmit.repeatPassword
                && userToSubmit.repeatPassword === userToSubmit.password);
        }



    });

});
