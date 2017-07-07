'use strict';
define(['powerUp', 'authService', 'loadingCircle'], function(powerUp) {

    powerUp.controller('ThreadsCtrl', ['$scope', '$location', '$log', 'Restangular', 'AuthService', function($scope, $location, $log, Restangular, AuthService) {

        // Restangular.setFullResponse(false);

        $scope.threads = null;
        $scope.order = $location.search().order || 'hot';
        $scope.isLoggedIn = AuthService.isLoggedIn();
        $scope.currentUser = AuthService.getCurrentUser();

        var disabledThreadButtons = [];

        $scope.getThreads = function() {
            Restangular.all('threads').getList({orderBy: $scope.order}).then(function(response) {
                $scope.threads = response;
            }, function(error) {
                $log.error('Error getting threads: ', error);
                $scope.threads = [];
            });
        };

        // TODO use API for this
        $scope.isLikedByCurrentUser = function(thread) {
            if(!$scope.isLoggedIn || !thread.hasOwnProperty("likedByCurrentUser")) {
                return false;
            }
            return thread.likedByCurrentUser;
        };

        $scope.likeThread = function(thread) {
            var threadId = thread.id;
            if (!threadId || disabledThreadButtons.indexOf(threadId) !== -1) {
                return;
            }
            // Disable this like button
            disabledThreadButtons.push(threadId);

            Restangular.one('threads', threadId).one('likes').put().then(function(response) {
                // Update like count
                thread.likeCount++;
                setLikedByCurrentUser(thread, true);
                // Re-enable button
                disabledThreadButtons.splice(disabledThreadButtons.indexOf(threadId), 1);
            }, function(error) {
                $log.error('Error liking thread #', threadId, ': ', error);
                // Re-enable button
                disabledThreadButtons.splice(disabledThreadButtons.indexOf(threadId), 1);
            });
        };

        $scope.unlikeThread = function(thread) {
            var threadId = thread.id;
            if (!threadId || disabledThreadButtons.indexOf(threadId) !== -1) {
                return;
            }
            // Disable this unlike button
            disabledThreadButtons.push(threadId);

            Restangular.one('threads', threadId).one('likes').remove().then(function(response) {
                // Update like count
                thread.likeCount--;
                setLikedByCurrentUser(thread, false);
                // Re-enable button
                disabledThreadButtons.splice(disabledThreadButtons.indexOf(threadId), 1);
            }, function(error) {
                $log.error('Error unliking thread #', threadId, ': ', error);
                // Re-enable button
                disabledThreadButtons.splice(disabledThreadButtons.indexOf(threadId), 1);
            });
        };

        /* *****************************
         *       PRIVATE FUNCTIONS
         * ****************************/
        // TODO use API for this
        function setLikedByCurrentUser(thread, isLiked) {
            if(!thread || typeof thread !== 'object' || typeof isLiked === 'undefined') {
                return;
            }
            thread.likedByCurrentUser = isLiked;
            return thread;
        }
    }]);
});
