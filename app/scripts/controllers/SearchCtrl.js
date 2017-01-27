'use strict';
define(['powerUp'], function(powerUp) {


    powerUp.controller('SearchCtrl', function($scope, searchedTitleService, $location, Restangular) {

        // TODO borrar, viejo.
        $scope.searchedTitle = searchedTitleService.getTitle;

        $scope.searchedName = $location.search().name;
        $scope.orderBoolean = $location.search().orderBoolean;
        $scope.pageNumber = $location.search().pageNumber;
        $scope.orderCategory = $location.search().orderCategory;
        $scope.developers = $location.search().developer;
        $scope.publishers = $location.search().publisher;
        $scope.genres = $location.search().genre;
        $scope.platforms = $location.search().platform;
        // $scope.keywords = $location.search().keyword;

        //

        var baseGames = Restangular.all('games');

        baseGames.getList({orderCategory: $scope.orderCategory, Ascending:$scope.orderBoolean, name:$scope.searchedName}).then(function(games) {
            $scope.games = games;
            console.log("All ok2");
            console.log(games);
        }, function(response) {
            console.log("Error with status code", response.status);
        });

    });

});
