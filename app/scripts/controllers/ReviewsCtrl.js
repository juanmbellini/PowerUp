'use strict';
define(['powerUp', 'LikesService', 'ratingStars', 'AuthService', 'PaginationService'], function(powerUp) {

    powerUp.controller('ReviewsCtrl', function($scope, Restangular, $location, AuthService, $log, $route, LikesService, PaginationService) {

        Restangular.setFullResponse(true);

        // start copy
        $scope.searchParams = {
            userId: $location.search().userId,
            gameId: $location.search().gameId,
            // Pagination
            orderBy: $location.search().orderBy || 'best',
            sortDirection: $location.search().sortDirection || 'desc',
            pageSize: $location.search().pageSize || 25,
            pageNumber: $location.search().pageNumber || 1
        };

        // Search parameters which will trigger a new search when changed
        var paginationSearchParams = ['orderBy', 'sortDirection', 'pageSize', 'pageNumber'];

        var defaultSortDirections = {
            date: 'desc',  // eslint-disable-line camelcase
            best: 'desc'
        };

        // Sanitize search param values
        for (var key in $scope.searchParams) {
            if ($scope.searchParams.hasOwnProperty(key)) {
                if ($scope.searchParams[key] === true) {
                    delete $scope.searchParams[key];
                }
            }
        }

        // Sanitize numeric search param values
        if (isNaN(parseInt($scope.searchParams.pageSize, 10))) {
            $scope.searchParams.pageSize = 25;
        } else {
            $scope.searchParams.pageSize = parseInt($scope.searchParams.pageSize, 10);
        }
        if (isNaN(parseInt($scope.searchParams.pageNumber, 10))) {
            $scope.searchParams.pageNumber = 1;
        } else {
            $scope.searchParams.pageNumber = parseInt($scope.searchParams.pageNumber, 10);
        }

        $scope.submitSearch = function (resetPageNumber) {
            if (resetPageNumber === true) {
                $scope.searchParams.pageNumber = 1;
            }
            $log.debug('Reloading Search with specified parameters: ', $scope.searchParams);
            $location.search($scope.searchParams);
            $location.path('reviews');
        };

        $scope.getPageRange = function(deltaPages) {
            return PaginationService.getPageRange($scope.reviewsPaginator, deltaPages);
        };


        /**
         * Toggles sorting direction or sets new order-by criterion and default sort direction.  Always resets page
         * number to 1.
         *
         * @param orderBy The new criterion to order by.
         */
        $scope.changeOrderBy = function (orderBy) {
            if ($scope.searchParams.orderBy === orderBy) {
                $scope.toggleSortDirection();
            } else {
                $scope.searchParams.orderBy = orderBy;
                $scope.searchParams.sortDirection = defaultSortDirections[orderBy];
            }
            $scope.searchParams.pageNumber = 1;
        };

        $scope.toggleSortDirection = function () {
            if ($scope.searchParams.sortDirection === 'asc') {
                $scope.searchParams.sortDirection = 'desc';
            } else if ($scope.searchParams.sortDirection === 'desc') {
                $scope.searchParams.sortDirection = 'asc';
            }
            $log.warn('Called toogleSortDirection but sort direction is neither asc nor desc. Doing nothing.');
        };

        $scope.setPageNumber = function (number) {
            if (!$scope.reviewsPaginator.pagination.totalPages) {
                return;
            }
            if (number >= 1 && number <= $scope.reviewsPaginator.pagination.totalPages) {
                $scope.searchParams.pageNumber = number;
            }
        };

        // Pagination control
        $scope.pageSizes = [25, 50, 100];
        $scope.resetPageNumberOnSubmit = false;

        $scope.validPageSizes = function() {
            var result = [];
            var pagination = $scope.reviewsPaginator.pagination;
            if (!pagination.totalElements) {
                return result;
            }
            $scope.pageSizes.forEach(function(pageSize, index, pageSizes) {
                if (pagination.totalElements >= pageSize || (index > 0 && pagination.totalElements > pageSizes[index - 1])) {
                    result.push(pageSize);
                }
            });
            var customPageSize = $scope.pageSizes.indexOf($scope.searchParams.pageSize) === -1 ? $scope.searchParams.pageSize : null;
            if (customPageSize) {
                result.push(customPageSize);
                result.sort(function(a, b) {
                    return a - b;
                });
            }
            return result;
        };

        // Reload page on pagination changes
        $scope.$watchCollection(function () {
            return extractProperties(paginationSearchParams, $scope.searchParams);
        }, function (newVal, oldVal) {
            if (typeof oldVal === 'undefined' || angular.equals(newVal, oldVal)) {
                return; // Initial change, ignore
            }
            // If page number didn't change, then a different pagination param changed. Reset page number.
            var resetPageNumber = newVal.pageNumber === oldVal.pageNumber;
            $scope.submitSearch(resetPageNumber);
        });

        // end copy


        $scope.canWriteReview = false;
        $scope.pageSizes = [1,5,10,15];

        $scope.userId = $location.search().userId;
        $scope.gameId = $location.search().gameId;

        if (!$scope.userId && !$scope.gameId) {
            $log.warn('No gameId nor userId, redirecting to home');
            $location.search({});
            $location.path('');
        }
        $scope.isLoggedIn = AuthService.isLoggedIn();

        /*
         * TODO: Game/User and reviews are fetched independently, but we need both requests to complete before rendering
         * anything. Consider using a counter or something to show a loading circle until everything is ready.
         *
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

        $scope.reviewsPaginator = PaginationService.initialize(Restangular.all('reviews'), undefined, $scope.searchParams.pageNumber, $scope.searchParams.pageSize, $scope.searchParams.orderBy, $scope.searchParams.sortDirection);
        PaginationService.setRequestParams($scope.reviewsPaginator, $scope.searchParams);


       // Pagination control

        // $scope.getPageRange = function(deltaPages) {
        //     return PaginationService.getPageRange($scope.reviewsPaginator, deltaPages);
        // };

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

                if (AuthService.isLoggedIn()) {
                    Restangular.one('users', review.userId).get().then(function (response) {
                        var reviewCreator = response.data;
                        array[i].user = reviewCreator;
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
        $scope.followDisabled = false;

        $scope.canFollow = function (user) {
            return user && AuthService.isLoggedIn() && !AuthService.isCurrentUser(user);
        };

        $scope.updateFollow = function (user) {
            if ($scope.followDisabled) {
                return;
            }
            $scope.followDisabled = true;

            if (!user.social.followedByCurrentUser) {
                // Follow
                Restangular.one('users', user.id).one('followers').put().then(function () {
                    $scope.followDisabled = false;
                    user.social.followedByCurrentUser = true;
                    user.social.followersCount++;
                }, function () {
                    $scope.followDisabled = false;
                });
            } else {
                // Unfollow
                Restangular.one('users',user.id).one('followers').remove().then(function () {
                    $scope.followDisabled = false;
                    user.social.followedByCurrentUser = false;
                    user.social.followersCount--;
                }, function () {
                    $scope.followDisabled = false;
                });
            }
        };

        // functions:
        $scope.getReviewUserProfilePictureUrl = function(review) {
            return Restangular.one('users', review.userId).one('picture').getRequestedUrl();
        };


        /**
         * Extract a subset of a specified objet's properties.
         *
         * @return {object}     The sub-object.
         */
        function extractProperties(properties, object) {
            var result = {};
            properties.forEach(function (key) {
                if (object.hasOwnProperty(key)) {
                    result[key] = object[key];
                }
            });
            return result;
        }

    });
});
