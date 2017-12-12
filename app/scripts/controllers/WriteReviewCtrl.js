'use strict';
define(['powerUp', 'AuthService'], function(powerUp) {

    powerUp.controller('WriteReviewCtrl', ['$scope', '$location', '$log', 'AuthService', 'Restangular', '$timeout', '$anchorScroll', function($scope, $location, $log, AuthService, Restangular, $timeout, $anchorScroll) {
        Restangular.setFullResponse(false);
        $anchorScroll();
        $scope.gameId = $location.search().id;
        if (!$scope.gameId) {
            console.log('No gameId');
            $location.search({});
            $location.path('');
        }
        $scope.game = null;
        $scope.criteriaNames = ['story', 'graphics', 'audio', 'controls', 'fun'];
        $scope.criteria = {};
        $scope.scoreTest = 0;
        $scope.criteriaNames.forEach(function(criterionName) {
            $scope.criteria[criterionName] = {};
            $scope.criteria[criterionName].name = criterionName;
            $scope.criteria[criterionName].score = 5;
        });
        var errorHandler = function(error) {
            $log.error('Error: ', error);
            $location.path('');
        };

        // Get Game
        Restangular.one('games', $scope.gameId).get().then(function(game) {
            $scope.game = game;
            $log.debug('Writing review for ', game);
            if ($scope.game !== null) {
                // All good. TODO If no logic is needed here, only handle negative case.
            } else {
                errorHandler('No game found with ID ', gameId);
            }
        }, errorHandler);

        var reviewAlreadyExist = false;
        var oldReview;
        // Recover review if it already exist
        var currentUserUsername = AuthService.getCurrentUser().username;
        Restangular.all('reviews').getList({username: currentUserUsername, gameId: $scope.gameId}).then(function (reviews) {
            if (reviews.length > 0) {
                oldReview = reviews[0];
                $scope.review = oldReview.body;
                $scope.criteriaNames.forEach(function(criterionName) {
                    var criterion = $scope.criteria[criterionName];
                    criterion.score = oldReview[criterion.name + 'Score'];
                });
                reviewAlreadyExist = true;
            } else {
                reviewAlreadyExist = false;
            }
        }, function(response) {
            console.log('There was an error getting reviews, ', response);
        });

        // Submit review
        $scope.submitReview = function() {
            // TODO validate input?
            var review = {body: $scope.review, gameId: $scope.gameId};
            $scope.criteriaNames.forEach(function(criterionName) {
                var criterion = $scope.criteria[criterionName];
                review[criterion.name + 'Score'] = criterion.score;
            });
            console.log('Submiting Review', review);
            if (reviewAlreadyExist) {
                oldReview.remove().then(function(response) {
                    post(review);
                });
            } else {
                post(review);
            }
        };
        function post(review) {
            Restangular.all('reviews').post(review).then(function(response) {
                // $location.search('#game').search({id: $scope.gameId});
                if ($scope.writeReviewRedirect) {
                    var redirect = $scope.writeReviewRedirect;
                    $scope.writeReviewRedirect = null;
                    $location.url(redirect);
                } else {
                    $location.search({id: $scope.gameId});
                    $location.path('game');
                }
            }, function(response) {
                console.log('Error with status code', response.status); // TODO handle error
            });
        }

        angular.element('document').ready(function() {
            $('textarea').characterCounter();
        });


        // Form on top for changing values:

        var noPlayStatusString = 'no-play-status';

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
        var isLoadedShelves = false;
        Restangular.one('users',userId).all('shelves').getList().then(function (shelves) {
            $scope.shelves = shelves;
            if (isLoadedShelves) {
                $timeout(function () {
                    $('select').material_select();
                });
            } else {
                isLoadedShelves = true;
            }
        });
        Restangular.one('users',userId).all('shelves').getList({gameId: $scope.gameId}).then(function (shelvesWithGame) {
            $scope.shelvesWithGame = [];
            angular.forEach(shelvesWithGame, function (shelf) {
                $scope.shelvesWithGame.push(shelf.name);
            });
            $scope.oldShelvesWithGame = $scope.shelvesWithGame;
            if (isLoadedShelves) {
                $timeout(function () {
                    $('select').material_select();
                });
            } else {
                isLoadedShelves = true;
            }
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



    }]);


});