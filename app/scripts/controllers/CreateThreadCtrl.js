'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('CreateThreadCtrl', ['$scope', '$location', '$log', 'Restangular', 'AuthService', function($scope, $location, $routeParams, $log, Restangular, AuthService) {
        $scope.isLoggedIn = AuthService.isLoggedIn();
        $scope.currentUser = AuthService.getCurrentUser();
        $scope.thread = {body: "", title: ""};

        if(!$scope.isLoggedIn) {
            // Unauthorized, redirect
            $location.path('threads');
        }

        // DOM control
        var pendingRequests = {
            thread: {
                create: false
            }
        };

        $scope.createThread = function() {
            if(pendingRequests.thread.create) {
                return;
            }
            pendingRequests.thread.create = true;
            Restangular.all('threads').post($scope.thread).then(function(response) {
                // TODO redirect to created thread URL, should be in response as Location header
                $location.path(response.headers['Location']);
            }, function(error) {
                $log.error('Error creating thread: ', error);
                pendingRequests.thread.create = false;
            });
        };

        /* ************************************************************
         *    THREAD LIKE/UNLIKE FUNCTIONS, adapted from ThreadsCtrl
         * ***********************************************************/
        $scope.likeThread = function(thread) {
            var threadId = thread.id;
            if (!threadId || pendingRequests.likeOrUnlikeThread) {
                return;
            }
            pendingRequests.likeOrUnlikeThread = true;
            Restangular.one('threads', threadId).post('like').then(function(response) {
                // Update like count
                thread.likeCount++;
                // TODO make sure $scope.isLikedByCurrentUser(thread) now returns true
                pendingRequests.likeOrUnlikeThread = false;
            }, function(error) {
                $log.error('Error liking thread #', threadId, ': ', error);
                pendingRequests.likeOrUnlikeThread = false;
            });
        };

        $scope.unlikeThread = function(thread) {
            var threadId = thread.id;
            if (!threadId || pendingRequests.likeOrUnlikeThread) {
                return;
            }
            pendingRequests.likeOrUnlikeThread = true;
            Restangular.one('threads', threadId).post('unlike').then(function(response) {
                // Update like count
                thread.likeCount--;
                // TODO make sure $scope.isLikedByCurrentUser(thread) now returns false
                pendingRequests.likeOrUnlikeThread = false;
            }, function(error) {
                $log.error('Error unliking thread #', threadId, ': ', error);
                pendingRequests.likeOrUnlikeThread = false;
            });
        };

        /* **************************************************
         *                  COMMENT FUNCTIONS
         * *************************************************/
        $scope.createComment = function(newComment) {
            if(!newComment || pendingRequests.comments.create) {
                return;
            }
            pendingRequests.comments.create = true;
            
        };

        /* *************************************************
         *              PRIVATE FUNCTIONS
         * ************************************************/
        function putThread(successCallback, errorCallback) {
            if($scope.thread === null || !$scope.thread.restangularized) {
                return;
            }
            //$scope.thread is a Restangularized object so it will know where to PUT
            $scope.thread.put().then(function(response) {
                if (typeof successCallback !== 'undefined') {
                    successCallback(response);
                }
            }, function(error) {
                if (typeof errorCallback !== 'undefined') {
                    errorCallback(error);
                } else {
                    $log.error('Error updating thread #', $scope.threadId, ': ', error);
                }
            });
        }
    }]);
});
