'use strict';
define(['powerUp', 'authService'], function(powerUp) {

    powerUp.controller('WriteReviewCtrl', ['$scope', '$location', '$log', 'AuthService', 'Restangular', function($scope, $location, $log, AuthService, Restangular) {
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
        Restangular.one('games', $scope.gameId).get().then(function(game) {
            $scope.game = game;
            $log.debug('Writing review for ', game);
            if ($scope.game !== null) {
                // All good. TODO If no logic is needed here, only handle negative case.
            } else {
                errorHandler('No game found with ID ', gameId);
            }
        }, errorHandler);


        $scope.submitReview = function() {
            // TODO validate input?

            var review = {body: $scope.review, gameId: $scope.gameId};
            $scope.criteriaNames.forEach(function(criterionName) {
                var criterion = $scope.criteria[criterionName];
                review[criterion.name + 'Score'] = criterion.score;
            });
            console.log('Submiting Review', review);
            Restangular.all('reviews').post(review).then(function(response) {
                // $location.search('#game').search({id: $scope.gameId});
                $location.search({id: $scope.gameId});
                $location.path('game');

            }, function(response) {
                console.log('Error with status code', response.status); // TODO handle error
            });
        };

        angular.element('document').ready(function() {
            $('textarea').characterCounter();
        });
        // $(document).ready(function() {
        //     $('select').material_select();
        // });

    }]);


});