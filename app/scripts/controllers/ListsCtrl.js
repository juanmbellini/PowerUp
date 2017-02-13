'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('ListsCtrl', function($scope, $location, Restangular) {

        $scope.playStatuses = ['planToPlay','playing','played'];
        $scope.shelves = [{name: 'RPG',id: 1},{name: 'Racing',id: 2}];
        $scope.games = [];
        $scope.userId = $location.search().userId;
        // TODO change to username or add both options ---droche 13/02/2017
        Restangular.one('users',$scope.userId).get().then(function(user) {
            $scope.user = user;
        }, function(response) {
            console.log('Error with status code', response.status); // TODO handle error
        });


        // TODO change games to real game list.
        var baseGames = Restangular.all('games');

        baseGames.getList({})
        .then(function(games) {
            $scope.games = games;
        }, function(response) {
            console.log('Error with status code', response.status);
        });


        $scope.deleteShelf = function(shelf) {

        };
        $scope.editShelf = function(shelf) {

        };
        $scope.isCurrentUserLogged = function() {
            return true; // TODO
        };
        $scope.deleteGame = function(game) {

        };
    });

});
