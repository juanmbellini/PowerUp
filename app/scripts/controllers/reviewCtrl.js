'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('ReviewCtrl', function($scope, Restangular, $location, AuthService) {

        $scope.canWriteReview = false;
        $scope.pageSizes = [5,10,15];
        $scope.pageSize = 5;

        $scope.userId = $location.search().userId;
        $scope.gameId = $location.search().gameId;
        if (!$scope.userId && !$scope.gameId) {
            console.log('No gameId nor userId');
            $location.search({});
            $location.path('');
        }


        // TODO delete duplicated
        if ($scope.gameId) {
            Restangular.one('games', $scope.gameId).get().then(function(game) {
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
           Restangular.one('users', $scope.userId).get().then(function(user) {
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
        $scope.deleteReview = function(review) {
            Restangular.one('reviews', review.id).remove().then(function(data) {
                    $log.info('Success: ', data);
                    $scope.reviews = $scope.reviews.filter(function(reviewToFilter) {
                        return reviewToFilter.id !== review.id;
                    });
                },
                function(error) {
                    $log.error('Error: ', error);
                },function () {
                    checkCanWriteReview();
                });

        };

        Restangular.all('reviews').getList({gameId: $scope.gameId, userId: $scope.userId}).then(function (reviews) {
            $scope.reviews = reviews;
            console.log('foundReviews', reviews);
            checkCanWriteReview();
        }, function() {
            console.log('There was an error getting reviews');
        });

        // TODO borrar y juntar
        $scope.checkCanWriteReview = function() {
            if ($scope.userId || !AuthService.isLoggedIn() || !$scope.gameId) {
                $scope.canWriteReview = false;
            } else {
                var currentUserUsername = AuthService.getCurrentUser().username;
                Restangular.all('reviews').getList({userName: currentUserUsername, gameId: $scope.gameId}).then(function (reviews) {
                    if (reviews.length > 0) {
                        $scope.canWriteReview = false;
                    } else {
                        $scope.canWriteReview = true;
                    }
                }, function(response) {
                    console.log('There was an error getting reviews, ', response);
                    $scope.canWriteReview = false;
                    return;
                });
            }
        };
    });

});
