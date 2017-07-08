'use strict';
define(['powerUp', 'authService', 'loadingCircle', 'likesService'], function(powerUp) {

    powerUp.controller('ThreadsCtrl', ['$scope', '$location', '$log', 'Restangular', 'AuthService', 'LikesService', function($scope, $location, $log, Restangular, AuthService, LikesService) {

        // Restangular.setFullResponse(false);

        $scope.threads = null;
        $scope.order = $location.search().order || 'hot';
        $scope.direction = $location.search().sortDirection;
        $scope.isLoggedIn = AuthService.isLoggedIn();
        $scope.currentUser = AuthService.getCurrentUser();

        $scope.getThreads = function() {
            var params = {orderBy: $scope.order};
            if ($scope.direction) {
                params.sortDirection = $scope.direction;
            }
            Restangular.all('threads').getList(params).then(function(response) {
                $scope.threads = response;
            }, function(error) {
                $log.error('Error getting threads: ', error);
                $scope.threads = [];
            });
        };

        $scope.isLikedByCurrentUser = function(thread) {
            if(!$scope.isLoggedIn || !thread.hasOwnProperty("likedByCurrentUser")) {
                return false;
            }
            return thread.likedByCurrentUser;
        };

        $scope.likeThread = function(thread) {
            LikesService.like(thread, undefined, function() {

            }, function(error) {
                $log.error('Error liking thread #', thread.id, ': ', error);
            });
        };

        $scope.unlikeThread = function(thread) {
            LikesService.unlike(thread, undefined, function() {

            }, function(error) {
                $log.error('Error unliking thread #', thread.id, ': ', error);
            });
        };

        /* *****************************
         *       PRIVATE FUNCTIONS
         * ****************************/

    }]);
});
