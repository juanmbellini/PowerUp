'use strict';
define(['powerUp', 'slick-carousel', 'onComplete', 'loadingCircle', 'AuthService'], function(powerUp) {

    powerUp.controller('GameCtrl', ['$scope', '$location', '$log', 'Restangular', 'AuthService', '$timeout', '$anchorScroll', function($scope, $location, $log, Restangular, AuthService, $timeout, $anchorScroll) {

        $anchorScroll();
        Restangular.setFullResponse(false);
        $scope.gameId = $location.search().id;
        $scope.game = null;

        var noPlayStatusString = 'no-play-status';

        $scope.canWriteReview = false;

        $scope.findGame = function(gameId) {
            Restangular.one('games', gameId).get().then(function(game) {
                $scope.game = game;
                $scope.gameId = game.id;
                console.log('Game: ', game);
                if ($scope.gameId > 0 && $scope.game !== null) {
                    $scope.videosMin = Math.min($scope.game.videoUrls.length, 4);       // How many videos to show per carousel page
                    $scope.picturesMin = Math.min($scope.game.pictureUrls.length, 4);   // How many pictures to show per carousel page
                    $scope.$broadcast('gameFound');                                     // Game found, fire all secondary searches (e.g. reviews)
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

        };

        $scope.findGame($scope.gameId);


        if (AuthService.isLoggedIn()) {
            var userId = AuthService.getCurrentUser().id;

            // Play Status
            $scope.playStatusOptions = [];
            Restangular.all('users').all('play-statuses').getList({}).then(function (playStatuses) {
                $scope.playStatusOptions = playStatuses;
                $scope.playStatusOptions = $scope.playStatusOptions.filter(function(playStatusToFilter) {
                    return playStatusToFilter !== noPlayStatusString;
                });

            }, function (response) {
                $log.error('Could not get playStatuses', response);
            });
            Restangular.one('users', userId).one('play-status', $scope.gameId).get().then(function (playStatus) {
                if (playStatus.length > 0) {
                    $scope.gamePlayStatus = playStatus[0].status;
                } else {
                    $scope.gamePlayStatus = noPlayStatusString;
                }
            }, function (response) {
                $log.error('Could not get play status from game', response);
            });
            $scope.updatePlayStatus = function () {
                $scope.loadingStatus = true;
                if ($scope.gamePlayStatus === noPlayStatusString) {
                    Restangular.one('users', userId).one('play-status',$scope.gameId).remove().then(function (response) {
                        $log.info('removed play status from game', response);
                        $scope.updatedStatus = true;
                        $scope.loadingStatus = false;
                    }, function (response) {
                        $scope.loadingStatus = false;
                        $log.error('Could not remove play status from game', response);
                    });
                } else {
                    Restangular.one('users', userId).all('play-status').post({gameId: $scope.gameId, status: $scope.gamePlayStatus}).then(function (response) {
                        $log.info('added play status to game', response);
                        $scope.updatedStatus = true;
                        $scope.loadingStatus = false;
                    }, function (response) {
                        $scope.loadingStatus = false;
                        $log.error('Could not add play status to game', response);
                    });
                }
            };


            // Game Score
            $scope.rangeScore = [];
            for (var i = 1; i <= 10; i++) {
                $scope.rangeScore.push(i);
            }
            $scope.gameScore = null;
            Restangular.one('users', userId).all('game-scores').getList({gameId: $scope.gameId}).then(function (gameScore) {
                if (gameScore.length > 0) {
                    $scope.gameScore = gameScore[0].score;
                } else {
                    $scope.gameScore = 'delete';
                }
            }, function (response) {
                $log.error('Could not get score from game', response);
            });
            $scope.updateScore = function () {
                $scope.loadingScore = true;
                if ($scope.gameScore === 'delete') {
                    Restangular.one('users', userId).one('game-scores',$scope.gameId).remove().then(function (response) {
                        $log.info('removed score from game', response);
                        $scope.updatedScore = true;
                        $scope.loadingScore = false;
                    }, function (response) {
                        $log.error('Could not remove score from game', response);
                        $scope.loadingScore = false;
                    });
                } else {
                    Restangular.one('users', userId).all('game-scores').post({gameId: $scope.gameId, score: $scope.gameScore}).then(function (response) {
                        $log.info('added score to game', response);
                        $scope.updatedScore = true;
                        $scope.loadingScore = false;
                    }, function (response) {
                        $log.error('Could not add score to game', response);
                        $scope.loadingScore = false;
                    });
                }
            };

            // Shelves
            $scope.shelves = [];
            $scope.shelvesWithGame = []; // name array
            $scope.shelvesWithGameDirty = []; // name array
            Restangular.one('users',userId).all('shelves').getList().then(function (shelves) {
                $scope.shelves = shelves;
                $timeout(function () {
                    $('select').material_select();
                },30);
            });
            Restangular.one('users',userId).all('shelves').getList({gameId: $scope.gameId}).then(function (shelvesWithGame) {
                $scope.shelvesWithGame = [];
                angular.forEach(shelvesWithGame, function (shelf) {
                    $scope.shelvesWithGame.push(shelf.name);
                });
                $scope.oldShelvesWithGame = $scope.shelvesWithGame;
            });
            $scope.updateShelfSelect = function () {
                // console.log(shelfUpdated);
                $scope.loadingShelves = true;
                angular.forEach($scope.shelvesWithGame, function (shelfName) {
                    if ($scope.oldShelvesWithGame.indexOf(shelfName) === -1) {
                        // The game is not in the oldArray, then it was added
                        Restangular.one('users',userId).one('shelves',shelfName).all('games').post({gameId: $scope.gameId}).then(function () {
                            $scope.updatedShelves = true;
                            $scope.loadingShelves = false;
                        }, function (response) {
                            $log.error('could not add game to shelf', response);
                            $scope.loadingShelves = false;
                        });
                    }
                });
                angular.forEach($scope.oldShelvesWithGame, function (shelfName) {
                    if ($scope.shelvesWithGame.indexOf(shelfName) === -1) {
                        // The game is not in the newArray, then it was removed
                        Restangular.one('users',userId).one('shelves',shelfName).one('games',$scope.gameId).remove().then(function () {
                            $scope.updatedShelves = true;
                            $scope.loadingShelves = false;
                        }, function (response) {
                            $log.error('could not remove game from shelf', response);
                            $scope.loadingShelves = false;
                        });
                    }
                });
                $scope.oldShelvesWithGame = $scope.shelvesWithGame;
            };
            $scope.isInShelf = function(shelfName) {
                return $scope.shelvesWithGame.indexOf(shelfName) !== -1;
            };
        }

        // Related Games
        $scope.relatedGames = [];
        Restangular.one('games', $scope.gameId).all('related-games').getList({}).then(function(relatedGames) {
            $scope.relatedGames = relatedGames;
            $scope.relatedMin = Math.min($scope.relatedGames.length, 5);
        }, function (response) {
            console.log('Error with status code', response.status); // TODO handle error
        });
        $scope.$on('relatedRendered', function(event) {
            angular.element('#related-carousel').slick({
                infinite: false,
                arrows: true
            });
            require(['lightbox2']); // TODO ensure requirejs doesn't load this twice
        });

        // userURL.all('shelves').all('recommendedGames').getList({shelvesFilter = {'shelf1','shelf2'}})

        /* *******************************
         * Event receivers for ng-repeats, see http://stackoverflow.com/questions/15207788/calling-a-function-when-ng-repeat-has-finished
         * ******************************/
        // (Re-)initialize Slick for game pictures
        $scope.$on('picturesRendered', function(event) {
            angular.element('#screenshots-carousel').slick({
                infinite: false,
                arrows: true,
                lazyload: 'ondemand'
            });
            require(['lightbox2']);
        });
        // (Re-)initialize Slick for game videos
        $scope.$on('videosRendered', function(event) {
            angular.element('#videos-carousel').slick({
                infinite: false,
                arrows: true
            });
            require(['lightbox2']); // TODO ensure requirejs doesn't load this twice
        });


        /* *****************************************
         *                 REVIEWS
         * ****************************************/
        $scope.reviews = null;
        $scope.$on('gameFound', function() {
            Restangular.all('reviews').getList({gameId: $scope.game.id, pageSize: 10}).then(function (reviews) {
                $scope.reviews = reviews;
                console.log('found review ', $scope.reviews);
                angular.forEach(reviews, function (reviewRef, index, reviewArray) {
                    Restangular.one('users', reviewRef.userId).all('game-scores').getList({gameId: $scope.gameId}).then(function (response) {
                        var gameScore = response;
                        if (gameScore.length > 0) {
                            reviewArray[index].overallScore = gameScore[0].score;
                        }
                    });
                });
                angular.forEach(reviews, function (reviewRef, index, reviewArray) {
                    Restangular.one('users',reviewRef.userId).all('shelves').getList({gameId: $scope.gameId}).then(function (response) {
                        var shelvesWithGame = response;
                        reviewArray[index].shelves = shelvesWithGame;
                    });
                });
                $scope.checkCanWriteReview();
            }, function() {
                console.log('There was an error getting reviews');
            });
        });

        $scope.checkCanWriteReview = function() {
            if (!AuthService.isLoggedIn()) {
                $scope.canWriteReview = false;
            } else {
                var currentUserUsername = AuthService.getCurrentUser().username;
                Restangular.all('reviews').getList({username: currentUserUsername, gameId: $scope.gameId}).then(function (reviews) {
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

        $scope.overallReviewScore = function(review) {
            var fields = ['storyScore', 'graphicsScore', 'audioScore', 'controlsScore', 'funScore'];
            var result = 0;

            fields.forEach(function(field) {
                result += review[field] / fields.length;
            });
            return result;
        };

        $scope.getReviewUserProfilePictureUrl = function(review) {
            return Restangular.one('users', review.userId).one('picture').getRequestedUrl();
        };

        $scope.canDeleteReview = function(review) {
          return AuthService.isLoggedIn() && AuthService.getCurrentUser().username === review.username;
        };

        $scope.deleteReview = function(review) {
            review.remove().then(function(data) {
                $log.info('Success: ', data);
                $scope.reviews = $scope.reviews.filter(function(reviewToFilter) {
                    return reviewToFilter.id !== review.id;
                });
            },
            function(error) {
                $log.error('Error: ', error);
            },function () {
                    $scope.checkCanWriteReview();
            });
        };



    }]);
});
