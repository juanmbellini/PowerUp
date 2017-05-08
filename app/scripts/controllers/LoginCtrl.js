'use strict';
define(['powerUp', 'authService'], function(powerUp) {

    powerUp.controller('LoginCtrl', ['$scope', '$location', '$log', 'Restangular', 'AuthService', function($scope, $location, $log, Restangular, AuthService) {

        $scope.logIn = function(form) {
            var logInAccount = {username: $scope.username, password: $scope.password};
            // $log.debug('Logging in with', logInAccount);
            AuthService.authenticate($scope.username, $scope.password,
                function() {
                    $location.search();
                    $location.path('');
                },
                function(error) {
                    // TODO do something more useful, e.g. show the error
                    $log.error('There was an error logging in: ', error);
                }
            );
        };
    }]);
});
