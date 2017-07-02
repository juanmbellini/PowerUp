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

        $scope.isLikedByCurrentUser = function(thread) {
          return false;   // TODO
        };

        // Like threads on like button click
        angular.element('body').on('click', '.like-thread', function(event) {
            var $target = $(event.currentTarget);
            var threadId = $target.data('thread-id');
            if (!threadId || disabledThreadButtons.indexOf(threadId) !== -1) {
                return;
            }
            // Disable this like button
            disabledThreadButtons.push(threadId);
            Restangular.one('threads', threadId).post('like').then(function(response) {
                // Update like count
                var $likeCountElement = $target.closest('span').find('b');
                var likeCount = parseInt($likeCountElement.html(), 10);
                if (!isNaN(likeCount)) {
                    $likeCountElement.html(likeCount + 1);
                }
                // Change like button to unlike button
                var $thumb = $target.find('i');
                $target.removeClass('like-thread');
                $target.addClass('unlike-thread');
                $thumb.removeClass('black-text');
                $thumb.addClass('green-text');
                // Re-enable button
                disabledThreadButtons.splice(disabledThreadButtons.indexOf(threadId), 1);
            }, function(error) {
                $log.error('Error liking thread #', threadId, ': ', error);
                // Re-enable button
                disabledThreadButtons.splice(disabledThreadButtons.indexOf(threadId), 1);
            });
        });

        // Unlike threads on unlike button click
        angular.element('body').on('click', '.unlike-thread', function(event) {
            var $target = $(event.currentTarget);
            var threadId = $target.data('thread-id');
            if (!threadId || disabledThreadButtons.indexOf(threadId) !== -1) {
                return;
            }
            // Disable this unlike button
            disabledThreadButtons.push(threadId);
            Restangular.one('threads', threadId).post('unlike').then(function(response) {
                // Update like count
                var $likeCountElement = $target.closest('span').find('b');
                var likeCount = parseInt($likeCountElement.html(), 10);
                if (!isNaN(likeCount)) {
                    $likeCountElement.html(likeCount - 1);
                }
                // Change unlike button to like button
                var $thumb = $target.find('i');
                $target.removeClass('unlike-thread');
                $target.addClass('like-thread');
                $thumb.removeClass('green-text');
                $thumb.addClass('black-text');
                // Re-enable button
                disabledThreadButtons.splice(disabledThreadButtons.indexOf(threadId), 1);
            }, function(error) {
                $log.error('Error unliking thread #', threadId, ': ', error);
                // Re-enable button
                disabledThreadButtons.splice(disabledThreadButtons.indexOf(threadId), 1);
            });
        });
    }]);
});
