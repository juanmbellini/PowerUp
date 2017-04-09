'use strict';
define(['powerUp', 'csrf-service'], function(powerUp) {

    powerUp.controller('WriteReviewCtrl', ['CsrfService', function($scope, $log, CsrfService) {
        $scope.gameId = $location.search().id;
        $scope.game = null;
        $scope.criteria = ['story', 'graphics', 'audio', 'controls', 'fun'];

        var errorHandler = function(error) {
            $log.error('Error: ', error);
            $location.path('');
        };

        $scope.findGame = function(gameId) {
            if(!gameId) {
                errorHandler("Invalid game ID");
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
            //TODO validate input?

            if(CsrfService.isTokenSet()) {
                var review = { review: $scope.review };
                $scope.criteria.forEach(function (criterium) {
                    review[criterium + 'Score'] = $scope[criterium+'-score'];
                });
                Restangular.all('reviews').post(review, undefined, CsrfService.attachTokenHeader()).then(function(response) {
                    $location.search('#game').search({id: $scope.gameId});
                });
            } else {
                $log.debug('No CSRF token set, retrieving and retrying with token');
                CsrfService.requestToken(function() {
                    $scope.submitReview(); // Try again with the CSRF token set
                });
            }
        };

        angular.element('document').ready(function() {
            $('textarea').characterCounter();
        });
    }]);

});