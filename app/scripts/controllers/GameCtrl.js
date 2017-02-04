'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('GameCtrl', function($scope, $location, Restangular) {

        $scope.gameId = $location.search().id;

        Restangular.one('games',$scope.gameId).get().then(function(game) {
            $scope.game = game;
            console.log('Game: ', game);
            if ($scope.gameId > 0 && $scope.game !== null) {
                console.log('todo OK!');
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

    });

});