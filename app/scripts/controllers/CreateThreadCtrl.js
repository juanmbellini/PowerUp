'use strict';
define(['powerUp', 'AuthService'], function(powerUp) {

    powerUp.controller('CreateThreadCtrl', ['$scope', '$location', '$log', 'Restangular', 'AuthService', function($scope, $location, $log, Restangular, AuthService) {

        Restangular.setFullResponse(true);

        $scope.thread = {body: '', title: ''};

        if (!AuthService.isLoggedIn()) {
            // Unauthorized, redirect
            $location.path('threads');
        }

        // DOM control
        $scope.pendingRequests = {
            thread: {
                create: false
            }
        };

        $scope.createThread = function() {
            if ($scope.pendingRequests.thread.create) {
                return;
            }
            $scope.pendingRequests.thread.create = true;
            Restangular.all('threads').post($scope.thread).then(function(response) {
                var location = response.headers('Location');
                location = (/\/(\d+)$/).exec(location)[1];
                // Extract new thread ID and redirect to it
                $location.path('/thread/' + location);
            }, function(error) {
                $log.error('Error creating thread: ', error);
                $scope.pendingRequests.thread.create = false;
            });
        };
    }]);
});
