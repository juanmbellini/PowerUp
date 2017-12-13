'use strict';
define(['powerUp', 'AuthService'], function(powerUp) {

    powerUp.controller('WriteReviewCtrl', ['$scope', '$location', '$log', 'AuthService', 'Restangular', '$anchorScroll', function($scope, $location, $log, AuthService, Restangular, $anchorScroll) {
        Restangular.setFullResponse(false);
        $anchorScroll();
        $scope.gameId = $location.search().id;
        if (!$scope.gameId) {
            console.log('No gameId');
            $location.search({});
            $location.path('');
        }
        $scope.game = null;

        $scope.scoreTest = 0;

        var errorHandler = function(error) {
            $log.error('Error: ', error);
            $location.path('');
        };

        // Range Score
        // TODO get from api
        $scope.rangeScore = [];
        for (var i = 1; i <= 10; i++) {
            $scope.rangeScore.push(i);
        }

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
            console.log('Submiting Review', review);
            if (reviewAlreadyExist) {
                Restangular.one('reviews',oldReview.id).customPUT(review).then(function (response) {
                    $log.debug('Updated Review');
                    $location.search({id: $scope.gameId});
                    $location.path('game');
                }, function(response) {
                    $log.error('Error updating review', response.status);
                });
            } else {
                post(review);
            }
        };
        function post(review) {
            Restangular.all('reviews').post(review).then(function(response) {
                // $location.search('#game').search({id: $scope.gameId});
                $location.search({id: $scope.gameId});
                $location.path('game');
            }, function(response) {
                console.log('Error with status code', response.status); // TODO handle error
            });
        }

        angular.element('document').ready(function() {
            $('textarea').characterCounter();
        });
        // $(document).ready(function() {
        //     $('select').material_select();
        // });



    }]);


});