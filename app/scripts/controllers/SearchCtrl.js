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

        $scope.submitSearch = function() {
            console.log($scope.searchedName);
            console.log("HOLA!");
            $location.search({'name':$scope.searchedName});
            $location.path("search");
        };


        // TODO MAKE FUNCTION THAT RETURN PATH OR GO TO PATH WITH ALL INFORMATION.

        var baseGames = Restangular.all('games');

        baseGames.getList({orderCategory: $scope.orderCategory, Ascending:$scope.orderBoolean, name:$scope.searchedName,
            publisher:$scope.publishers, genres:$scope.genres, platform:$scope.platforms, developer:$scope.developers})
            .then(function(games) {
            $scope.games = games;
            console.log("All ok2");
            console.log(games);
        }, function(response) {
            console.log("Error with status code", response.status);
        });

    });

});
