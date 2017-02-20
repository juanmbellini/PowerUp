'use strict';
define(['powerUp', 'sessionService', 'csrf-service'], function(powerUp) {

    powerUp.controller('LoginCtrl', ['$scope', '$location', '$log', 'Restangular', 'SessionService', 'CsrfService', function($scope, $location, $log, Restangular, SessionService, CsrfService) {

        $scope.logIn = function(form) {
            if (CsrfService.isTokenSet()) {
                var logInAccount = {username: $scope.username, password: $scope.password};
                var csrfHeaders = {};
                csrfHeaders[CsrfService.getTokenHeader()] = CsrfService.getToken(); // Dynamically set CSRF header since the header name is a variable
                $log.debug('Logging in with', logInAccount, 'and CSRF token');
                Restangular.all('auth/login').post(logInAccount, undefined, csrfHeaders).then(function (data) {
                    SessionService.setCurrentUser({username: $scope.username}); // TODO use actual user, or retrieve from API
                    $location.search();
                    $location.path('');
                }, function(error) {
                    $log.error('There was an error in logIn:', error);
                });
            } else {
                $log.debug('No CSRF token set, retrieving and retrying with token');
                CsrfService.requestToken(function() {
                    $scope.logIn(form); // Try again with the CSRF token set
                });
            }
        };
    }]);
});
