'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('ReviewCtrl', function($scope, Restangular, $location, AuthService, $log, $route) {

        Restangular.setFullResponse(true);
        $scope.canWriteReview = false;
        $scope.pageSizes = [1,5,10,15];
        $scope.pageSize = parseInt($location.search().pageSize, 10);
        if (!$scope.pageSize || $scope.pageSize <= 0 || $scope.pageSize > 100) {
            $scope.pageSize = 5;
        }
        $scope.pageSizeSelected = $scope.pageSize;
        $scope.pageNumber = $location.search().pageNumber;
        if (!$scope.pageNumber || $scope.pageNumber <= 0) {
            $scope.pageNumber = 1;
        }
        $scope.userId = $location.search().userId;
        $scope.gameId = $location.search().gameId;
        if (!$scope.userId && !$scope.gameId) {
            console.log('No gameId nor userId');
            $location.search({});
            $location.path('');
        }
        $scope.updatePageSize = function (pageSizeSelected) {
            $scope.pageSize = pageSizeSelected;
            $location.search('pageSize', $scope.pageSize);
        };


        // TODO delete duplicated
        if ($scope.gameId) {
            Restangular.one('games', $scope.gameId).get().then(function(response) {
                var game = response.data;
                $scope.game = game;
                console.log('Game: ', game);
                if ($scope.game !== null) {
                    // $scope.$broadcast('gameFound');                                     // Game found, fire all secondary searches (e.g. reviews)
                } else {
                    // TODO show 'game not found'
                    $location.search({});
                    $location.path('');
                }
            }, function(response) {
                console.log('Error with status code', response.status); // TODO handle error
                $location.search({});
                $location.path('');
            });
        }
       if ($scope.userId) {
           Restangular.one('users', $scope.userId).get().then(function(response) {
               var user = response.data;
               $scope.user = user;
               console.log('User: ', user);
               if ($scope.user !== null) {
                   // $scope.$broadcast('gameFound');                                     // Game found, fire all secondary searches (e.g. reviews)
               } else {
                   // TODO show 'user not found'
                   $location.search({});
                   $location.path('');
               }
           }, function(response) {
               console.log('Error with status code', response.status); // TODO handle error
               $location.search({});
               $location.path('');
           });
       }

        /**
         * Calculates the overallReviewScore of a review and returns it
         * @param review
         * @returns {number}
         */
       $scope.overallReviewScore = function(review) {
            var fields = ['storyScore', 'graphicsScore', 'audioScore', 'controlsScore', 'funScore'];
            var result = 0;
            fields.forEach(function(field) {
                result += review[field] / fields.length;
            });
            return result;
        };
        $scope.canDeleteReview = function(review) {
            return AuthService.isLoggedIn() && AuthService.getCurrentUser().username === review.username;
        };

        /**
         * Check if the user is logged. If he is, check if he can write a review by checking if he has another review already written.
         * Set the variable $scope.canWriteReview to true if he can write a review and to false if he cannot.
         */
        $scope.checkCanWriteReview = function() {
            if ($scope.userId || !AuthService.isLoggedIn() || !$scope.gameId) {
                $scope.canWriteReview = false;
            } else {
                var currentUserUsername = AuthService.getCurrentUser().username;
                Restangular.all('reviews').getList({username: currentUserUsername, gameId: $scope.gameId}).then(function (response) {
                    var reviews = response.data;
                    if (reviews.length > 0) {
                        $scope.canWriteReview = false;
                    } else {
                        $scope.canWriteReview = true;
                    }
                }, function(response) {
                    console.log('There was an error getting reviews, ', response);
                    $scope.canWriteReview = false;
                });
            }
        };

        /**
         * Deletes the review
         * @param review
         */
        $scope.deleteReview = function(review) {
            review.remove().then(function(response) {
                var data = response.data;
                $log.info('Success: ', data);
                $route.reload();
                // $scope.reviews = $scope.reviews.filter(function(reviewToFilter) {
                //     return reviewToFilter.id !== review.id;
                // });
            },
            function(error) {
                $log.error('Error: ', error);
            },function () {
                    $scope.checkCanWriteReview();
            });
        };

        Restangular.all('reviews').getList({gameId: $scope.gameId, userId: $scope.userId, pageSize: $scope.pageSize, pageNumber: $scope.pageNumber}).then(function (response) {
            // TODO si el pageNumber se pasa, se tiene que retornar el numero de pagina maxima y si hay reviews para ese usuario.
            var reviews = response.data;
            $scope.reviews = reviews;
            console.log('foundReviews', reviews);
            $scope.headersPagination = response.headers();
            console.log($scope.headersPagination);
            $scope.checkCanWriteReview();
            $scope.updatePagination();
        }, function() {
            console.log('There was an error getting reviews');
            $location.search('pageNumber', 1);

        });

        /**
         * Changes the pageNumber query parameter using the newPageNumber
         * @param newPageNumber
         */
        $scope.changePageNumber = function(newPageNumber) {
            $scope.pageNumber = newPageNumber;
            $location.search('pageNumber', $scope.pageNumber);
        };

        /**
         * Updates pagination variables using the pagination headers
         */
       $scope.updatePagination = function() {
           $scope.pageNumber = parseInt($scope.headersPagination['x-page-number'], 10);
           $scope.totalPages = parseInt($scope.headersPagination['x-total-pages'], 10);
           // Show the fist ten
           $scope.paginationJustOne = ($scope.pageNumber - 4 <= 0) || ($scope.totalPages <= 10);
           // Show the last ten
           $scope.paginationNoMorePrev = ($scope.pageNumber + 5 > $scope.totalPages);

           $scope.paginationTheFirstOnes = ($scope.pageNumber + 5 < 10);
           $scope.paginationNoMoreNext = ($scope.pageNumber + 5 >= $scope.totalPages) || ($scope.totalPages < 10);

           if ($scope.paginationJustOne) {
               $scope.paginationBegin = 1;
           } else {
               $scope.paginationBegin = $scope.paginationNoMorePrev ? $scope.totalPages - 9 : $scope.pageNumber - 4;
           }
           if ($scope.paginationNoMoreNext) {
               $scope.paginationEnd = $scope.totalPages;
           } else {
               $scope.paginationEnd = $scope.paginationTheFirstOnes ? 10 : $scope.pageNumber + 5;
           }
           $scope.rangePagination = [];
           for (var i = $scope.paginationBegin; i <= $scope.paginationEnd; i++) {
               $scope.rangePagination.push(i);
           }
       };
    });
});
