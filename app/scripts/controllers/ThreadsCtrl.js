'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('ThreadsCtrl', function($scope, Restangular, SessionService, $location) {

        Restangular.setFullResponse(false);
        $scope.threads = [{creator: {username: '<h1>Hello</h1>', likeCount: 3, id: '1'},id: 1,comments: [],initialComment: 'MI PRIMER COMENTARIO',title: 'EL TITULO', createdAt: '2016-1-13'}];
        $scope.order = $location.search().order || 'hot';
        $scope.isLoggedIn = SessionService.isLoggedIn;
        $scope.getCurrentUser = SessionService.getCurrentUser;

        $scope.isLikedByCurrentUser = function(thread) {
          return true;   // TODO
        };
        $scope.unlikeThread = function() {

        };
        $scope.likeThread = function() {

        };
    });
});
