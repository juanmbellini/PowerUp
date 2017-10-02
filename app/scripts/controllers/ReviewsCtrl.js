'use strict';
define(['powerUp', 'LikesService', 'AuthService', 'PaginationService'], function(powerUp) {

    powerUp.controller('ReviewsCtrl', function($scope, Restangular, $location, AuthService, $log, $route, LikesService, PaginationService) {

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
            $log.warn('No gameId nor userId, redirecting to home');
            $location.search({});
            $location.path('');
        }
        $scope.updatePageSize = function (pageSizeSelected) {
            $scope.pageSize = pageSizeSelected;
            $location.search('pageSize', $scope.pageSize);
        };

        $scope.isLoggedIn = AuthService.isLoggedIn();

        /*
         * TODO: Game/User and reviews are fetched independently, but we need both requests to complete before rendering
         * anything. Consider using a counter or something to show a loading circle until everything is ready.
         *
         * Also see if anything can be done about the 3N requests being fired to show extra data; consider adding this
         * information to the original reviews API call.
         */

        // TODO delete duplicated
        if ($scope.gameId) {
            Restangular.one('games', $scope.gameId).get().then(function(response) {
                var game = response.data;
                $scope.game = game;
                $log.debug('Game: ', game);
                if ($scope.game !== null) {
                    // $scope.$broadcast('gameFound');                                     // Game found, fire all secondary searches (e.g. review)
                } else {
                    // TODO show 'game not found'
                    $location.search({});
                    $location.path('');
                }
            }, function(response) {
                $log.error('Error with status code', response.status); // TODO handle error
                $location.search({});
                $location.path('');
            });
        }
       if ($scope.userId) {
            Restangular.one('users', $scope.userId).get().then(function(response) {
               var user = response.data;
               $scope.user = user;
                $log.debug('User: ', user);
               if ($scope.user !== null) {
                   // $scope.$broadcast('gameFound');                                     // Game found, fire all secondary searches (e.g. review)
               } else {
                   // TODO show 'user not found'
                   $location.search({});
                   $location.path('');
               }
           }, function(response) {
               $log.error('Error with status code', response.status); // TODO handle error
               $location.search({});
               $location.path('');
           });
       }

       // Pagination control
        $scope.reviewsPaginator = PaginationService.initialize(Restangular.all('reviews'), undefined, $scope.pageNumber, $scope.pageSize, $scope.orderBy, $scope.sortDirection);

        PaginationService.setRequestParams($scope.reviewsPaginator, {gameId: $scope.gameId, userId: $scope.userId});

        $scope.getPageRange = function(deltaPages) {
            return PaginationService.getPageRange($scope.reviewsPaginator, deltaPages);
        };

        // Automatically get requested reviews
        PaginationService.get($scope.reviewsPaginator, function(response) {
            // TODO si el pageNumber se pasa, se tiene que retornar el numero de pagina maxima y si hay review para ese usuario.
            $scope.reviews = response.data;
            $log.debug('Found reviews: ', $scope.reviews);
            $scope.headersPagination = response.headers();  // TODO remove, use $scope.reviewsPaginator.pagination

            $scope.checkCanWriteReview();
            // $scope.updatePagination();   // TODO remove or merge code with PaginationService.getPageRange()

            // Get extra info for each review
            angular.forEach($scope.reviews, function (review, i, array) {
                // The score that the creator left for the game
                // TODO include this in API response? Why may the API not return any data for this?
                Restangular.one('users', review.userId).all('game-scores').getList({gameId: review.gameId}).then(function (response) {
                    var gameScore = response.data;
                    if (gameScore.length > 0) {
                        array[i].overallScore = gameScore[0].score;
                    }
                }, function(error) {
                    $log.error("Couldn't get user #" + review.userId + "'s score for game #" + review.gameId + ': ', error);
                });

                // Whether the creator is followed by the current user
                // TODO include this in API response?
                if (AuthService.isLoggedIn()) {
                    Restangular.one('users', review.userId).get().then(function (response) {
                        var reviewCreator = response.data;
                        array[i].followedByCurrentUser = reviewCreator.social.followedByCurrentUser;
                    }, function(error) {
                        $log.error("Couldn't get user #" + review.userId + "'s social info for review #" + review.id + ': ', error);
                    });
                }

                // All the creator's shelves that the reviewed game belongs to
                Restangular.one('users', review.userId).all('shelves').getList({gameId: review.gameId}).then(function (response) {
                    array[i].shelves = response.data;
                });
            });
        }, function(error) {
            $log.error('There was an error getting reviews: ', error, '; setting page number to 1');
            $location.search('pageNumber', 1);
        });

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
         * Checks if logged in. If so, checks whether current user can write a review by checking if they have another
         * review already written. Sets result as boolean to $scope.canWriteReview.
         */
        $scope.checkCanWriteReview = function() {
            if ($scope.userId || !AuthService.isLoggedIn() || !$scope.gameId) {
                $scope.canWriteReview = false;
            } else {
                var currentUserUsername = AuthService.getCurrentUser().username;
                Restangular.all('reviews').getList({username: currentUserUsername, gameId: $scope.gameId}).then(function (response) {
                    var reviews = response.data;
                    $scope.canWriteReview = reviews.length === 0;
                }, function(response) {
                    $log.error('There was an error checking whether the current user can write a review: ', response, '; assuming false');
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
                // TODO revise this, reloading the page with one result less may result in an out-of-range error.
                // Consider resetting to page 1, or at least trying to go 1 page back if there's an error after the reload
                $route.reload();
                // $scope.review = $scope.review.filter(function(reviewToFilter) {
                //     return reviewToFilter.id !== review.id;
                // });
            },
            function(error) {
                $log.error('Error: ', error);
            },function () {
                    $scope.checkCanWriteReview();
            });
        };

        /**
         * Changes the pageNumber query parameter using the newPageNumber
         * @param newPageNumber
         */
        $scope.setPageNumber = function(newPageNumber) {
            if (!$scope.reviewsPaginator.pagination.totalPages) {
                return;
            }
            if (newPageNumber >= 1 && newPageNumber <= $scope.reviewsPaginator.pagination.totalPages) {
                $scope.pageNumber = newPageNumber;
                $location.search('pageNumber', $scope.pageNumber);
            }
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

        // Likes
        $scope.isLikedByCurrentUser = LikesService.isLikedByCurrentUser;
        $scope.likeReview = function(review) {
            LikesService.like(review, undefined, function() {

            }, function(error) {
                $log.error('Error liking review #', review.id, ': ', error);
            });
        };
        $scope.unlikeReview = function(review) {
            LikesService.unlike(review, undefined, function() {

            }, function(error) {
                $log.error('Error unliking review #', review.id, ': ', error);
            });
        };

        // Follows
        $scope.updateFollow = function (review) {
            $scope.followDisable = true;
            if (!review.followedByCurrentUser) {
                Restangular.one('users',review.userId).one('followed').put().then(function () {
                    $scope.followDisable = false;
                    review.followedByCurrentUser = true;
                }, function () {
                    $scope.followDisable = false;
                });
            } else {
                Restangular.one('users',review.userId).one('followed').remove().then(function () {
                    $scope.followDisable = false;
                    review.followedByCurrentUser = false;
                }, function () {
                    $scope.followDisable = false;
                });
            }
        };
    });
});
