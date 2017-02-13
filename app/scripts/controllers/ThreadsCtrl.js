'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('ThreadsCtrl', function($scope, Restangular, LogInService, $location) {

        Restangular.setFullResponse(false);
        $scope.threads = [{creator: {username: '<h1>Hello</h1>', likeCount: 3, id: '1'},id: 1,comments: [],initialComment: 'MI PRIMER COMENTARIO',title: 'EL TITULO', createdAt: '2016-1-13'}];
        $scope.order = $location.search().order;
        if (angular.isUndefined($scope.order)) {
            $scope.order = 'hot';
        };
        $scope.isLoggedIn = LogInService.isLoggedIn;
        $scope.getCurrentUser = LogInService.getLoggedUser;

        $scope.isLikedByCurrentUser = function(thread) {
          return true;   // TODO
        };
        $scope.unlikeThread = function() {

        };
        $scope.likeThread = function() {

        };
    });
});
