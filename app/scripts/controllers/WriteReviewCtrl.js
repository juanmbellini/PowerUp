'use strict';
define(['powerUp', 'authService'], function(powerUp) {

    powerUp.controller('WriteReviewCtrl', ['$scope', '$location', '$log', 'AuthService', function($scope, $location, $log, AuthService) {
        $scope.gameId = $location.search().id;
        $scope.game = null;
        $scope.criteriaNames = ['story', 'graphics', 'audio', 'controls', 'fun'];
        $scope.criteria = {};
        $scope.scoreTest = 0;

        $scope.criteriaNames.forEach(function(criterion) {
            $scope.criteria[criterion] = {};
            $scope.criteria[criterion].name = criterion;
            $scope.criteria[criterion].score = 5;
        });



        var errorHandler = function(error) {
            $log.error('Error: ', error);
            $location.path('');
        };

        $scope.findGame = function(gameId) {
            if (!gameId) {
                errorHandler('Invalid game ID');
            }

            Restangular.one('games', gameId).get().then(function(game) {
                $scope.game = game;
                $scope.gameId = game.id;

                $log.debug('Writing review for ', game);
                if ($scope.gameId > 0 && $scope.game !== null) {
                    // All good. TODO If no logic is needed here, only handle negative case.
                } else {
                    errorHandler('No game found with ID ', gameId);
                }
            }, errorHandler);


        };

        $scope.submitReview = function() {
            // TODO validate input?

            var review = { review: $scope.review };
            $scope.criteria.forEach(function (criterium) {
                review[criterium + 'Score'] = $scope[criterium+'-score'];
            });
            Restangular.all('reviews').post(review).then(function(response) {
                $location.search('#game').search({id: $scope.gameId});
            });
        };

        angular.element('document').ready(function() {
            $('textarea').characterCounter();
        });
        $(document).ready(function() {
            $('select').material_select();
        });

    });


});