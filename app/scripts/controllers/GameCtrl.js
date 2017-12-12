'use strict';
define(['powerUp', 'slick-carousel', 'onComplete', 'loadingCircle', 'ratingStars', 'AuthService'], function(powerUp) {

    powerUp.controller('GameCtrl', ['$scope', '$location', '$log', 'Restangular', 'AuthService', '$timeout', '$anchorScroll', function($scope, $location, $log, Restangular, AuthService, $timeout, $anchorScroll) {

        $anchorScroll();
        Restangular.setFullResponse(true);
        $scope.gameId = $location.search().id;
        $scope.game = null;

        var noPlayStatusString = 'no-play-status';

        $scope.canWriteReview = false;

        $scope.findGame = function(gameId) {
            Restangular.one('games', gameId).get().then(function(response) {
                var game = response.data;
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
            Restangular.all('users').all('play-statuses').getList({}).then(function (response) {
                var playStatuses = response.data;
                $scope.playStatusOptions = playStatuses;
                $scope.playStatusOptions = $scope.playStatusOptions.filter(function(playStatusToFilter) {
                    return playStatusToFilter !== noPlayStatusString;
                });

            }, function (response) {
                $log.error('Could not get playStatuses', response);
            });
            Restangular.one('users', userId).one('play-status', $scope.gameId).get().then(function (response) {
                var playStatus = response.data;
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
                        $log.info('removed play status from game', response.data);
                        $scope.updatedStatus = true;
                        $scope.loadingStatus = false;
                    }, function (response) {
                        $scope.loadingStatus = false;
                        $log.error('Could not remove play status from game', response);
                    });
                } else {
                    Restangular.one('users', userId).all('play-status').post({gameId: $scope.gameId, status: $scope.gamePlayStatus}).then(function (response) {
                        $log.info('added play status to game', response.data);
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
            Restangular.one('users', userId).all('game-scores').getList({gameId: $scope.gameId}).then(function (response) {
                var gameScore = response;
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
                        $log.info('removed score from game', response.data);
                        $scope.updatedScore = true;
                        $scope.loadingScore = false;
                    }, function (response) {
                        $log.error('Could not remove score from game', response);
                        $scope.loadingScore = false;
                    });
                } else {
                    Restangular.one('users', userId).all('game-scores').post({gameId: $scope.gameId, score: $scope.gameScore}).then(function (response) {
                        $log.info('added score to game', response.data);
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
            Restangular.one('users',userId).all('shelves').getList().then(function (response) {
                var shelves = response.data;
                $scope.shelves = shelves;
                $timeout(function () {
                    $('select').material_select();
                },30);
            });
            Restangular.one('users',userId).all('shelves').getList({gameId: $scope.gameId}).then(function (response) {
                var shelvesWithGame = response.data;
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
        Restangular.one('games', $scope.gameId).all('related-games').getList({}).then(function(response) {
            var relatedGames = response.data;
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
      // (Re-)initialize Slick for game streams
      $scope.$on('streamsRendered', function(event) {
        angular.element('#streams-carousel').slick({
          infinite: false,
          arrows: true
        });
        require(['lightbox2']); // TODO ensure requirejs doesn't load this thrice
      });


      /* *****************************************
       *                 TWITCH
       * ****************************************/
      $scope.twitchStreams = null;
      $scope.$on('gameFound', function() {
          $scope.game.all('twitch').getList({}).then(function(response) {
            $scope.twitchStreams = response.data.slice(0, 4);    // Take at most 4 streams because the Twitch player is heavy TODO: Get a way to lazy load players to be able to show more?
            $scope.streamsMin = Math.min($scope.twitchStreams.length, 4);   // How many streams to show per carousel page
            $log.debug('First found Twitch stream: ', $scope.twitchStreams[0]);
          }, function(error) {
              $log.error('Error getting Twitch streams:', error);
          });
      });

        /* *****************************************
         *                 REVIEWS
         * ****************************************/
        $scope.reviews = null;
        $scope.numReviews = 0;
        $scope.$on('gameFound', function() {
            Restangular.all('reviews').getList({gameId: $scope.game.id, pageSize: 10}).then(function (response) {
                var reviews = response.data;
                $scope.numReviews = parseInt(response.headers()['x-overall-amount-of-elements'], 10),
                $scope.reviews = reviews;
                console.log('found review ', $scope.reviews);
                // Add scores
                angular.forEach(reviews, function (reviewRef, index, reviewArray) {
                    Restangular.one('users', reviewRef.userId).all('game-scores').getList({gameId: $scope.gameId}).then(function (response) {
                        var gameScore = response.data;
                        if (gameScore.length > 0) {
                            reviewArray[index].overallScore = gameScore[0].score;
                        }
                    });
                });
                // Add shelves
                angular.forEach(reviews, function (reviewRef, index, reviewArray) {
                    Restangular.one('users',reviewRef.userId).all('shelves').getList({gameId: $scope.gameId}).then(function (response) {
                        var shelvesWithGame = response.data;
                        reviewArray[index].shelves = shelvesWithGame;
                    });
                });
                // Add users
                angular.forEach(reviews, function (reviewRef, index, reviewArray) {
                    Restangular.one('users').one('username', reviewRef.username).get().then(function(response) {
                        var user = response.data;
                        reviewArray[index].user = user;
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
                $log.info('Success: ', data.data);
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


        /* *************************************************************************************************************
              FOLLOWS
       * ************************************************************************************************************/
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



    }]);
});
