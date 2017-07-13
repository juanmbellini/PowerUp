'use strict';
define(['powerUp', 'authService', 'loadingCircle', 'loadingCircle-small', 'likesService', 'paginationService'], function(powerUp) {

    powerUp.controller('ThreadsCtrl', ['$scope', '$location', '$log', 'Restangular', 'AuthService', 'LikesService', 'PaginationService', function($scope, $location, $log, Restangular, AuthService, LikesService, PaginationService) {

        Restangular.setFullResponse(true);  // TODO set this to true in initial config and always use it as such

        $scope.threads = null;
        $scope.order = $location.search().order || 'hot';
        $scope.direction = $location.search().sortDirection;
        $scope.isLoggedIn = AuthService.isLoggedIn();
        $scope.currentUser = AuthService.getCurrentUser();

        // Hotfix
        if ($scope.order === 'best' && !$scope.direction) {
            $scope.direction = 'desc';
        }

        // DOM control
        $scope.pendingRequests = {
            threads: false
        };

        // Pagination control
        var threadsPaginator = null;
        $scope.hasMorePages = false;

        $scope.getThreads = function() {
            threadsPaginator = PaginationService.initialize(Restangular.all('threads'), undefined, 1, undefined, $scope.order, $scope.direction);
            PaginationService.get(threadsPaginator, function(response) {
                $scope.threads = response.data;
                $scope.hasMorePages = PaginationService.hasMorePages(threadsPaginator);
            }, function(error) {
                $log.error('Error getting threads: ', error);
                $scope.threads = [];
            });
        };

        $scope.getMoreThreads = function() {
            if ($scope.pendingRequests.threads) {
                return;
            }

            $scope.pendingRequests.threads = true;
            PaginationService.getNextPage(threadsPaginator, function(response) {
                $scope.threads = $scope.threads.concat(response.data);
                $scope.hasMorePages = PaginationService.hasMorePages(threadsPaginator);
                $scope.pendingRequests.threads = false;
            }, function(error) {
                $log.error('Error getting more threads: ', error);
                $scope.pendingRequests.threads = false;
            });
        };

        $scope.isLikedByCurrentUser = function(thread) {
            if (!$scope.isLoggedIn || !thread.hasOwnProperty('likedByCurrentUser')) {
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
