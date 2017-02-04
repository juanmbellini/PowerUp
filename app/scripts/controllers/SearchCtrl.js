'use strict';
define(['powerUp'], function(powerUp) {


    powerUp.controller('SearchCtrl', function($scope, searchedTitleService, $location, Restangular, $httpParamSerializer) {

        Restangular.setFullResponse(true);

        // TODO borrar, viejo.
        $scope.searchedTitle = searchedTitleService.getTitle;

        $scope.overAllAmountOfElements = 100;
        $scope.totalPages = 114;
        $scope.pageSize = 25;

        $scope.searchedName = $location.search().name;
        $scope.ascending = $location.search().ascending;
        $scope.pageNumber = $location.search().pageNumber;
        $scope.orderCategory = $location.search().orderCategory;
        $scope.developers = $location.search().developer;
        $scope.publishers = $location.search().publisher;
        $scope.genres = $location.search().genre;
        $scope.platforms = $location.search().platform;
        $scope.pageNumber = $location.search().pageNumber;
        if (angular.isUndefined($scope.pageNumber)) {
            $scope.pageNumber = 1;
        };
        if ($scope.ascending === undefined) {
            $scope.ascending = true;
        }
        // $scope.keywords = $location.search().keyword;

        $scope.changePageUrl = function(pageNumber) {
            return $httpParamSerializer({orderCategory: $scope.orderCategory, ascending: $scope.ascending, name: $scope.searchedName,
                publisher: $scope.publishers, genres: $scope.genres, platform: $scope.platforms, developer: $scope.developers,
                pageNumber: pageNumber});

        };

        $scope.submitSearch = function() {
            console.log($scope.searchedName);
            console.log('HOLA!');
            $location.search({'name': $scope.searchedName});
            $location.path('search');
        };

        $scope.getPagesNumbers = function () {
            return [1,2,3,4,5];
        };

        $scope.hasFilters = function() {
            return ($scope.developers !== undefined || $scope.publishers !== undefined || $scope.genres !== undefined || $scope.platforms !== undefined);
        };

        $scope.sortNameButton = function() {
            if ($scope.orderCategory === undefined || angular.equals($scope.orderCategory,'name')) {
                $scope.ascending = !$scope.ascending;
            }else {
                $scope.orderCategory = 'name';
                $scope.ascending = true;
            }
            console.log('orderCategory: ', $scope.orderCategory);
            console.log('ascending: ', $scope.ascending);
            $scope.reload();
        };

        $scope.sortRatingButton = function() {
            if (angular.equals($scope.orderCategory,'avg_score')) {
                $scope.ascending = !$scope.ascending;
            }else {
                $scope.orderCategory = 'avg_score';
                $scope.ascending = false;
            }
            console.log('orderCategory: ', $scope.orderCategory);
            console.log('ascending: ', $scope.ascending);
            $scope.reload();
        };

        $scope.sortReleaseButton = function() {
            if (angular.equals($scope.orderCategory,'release')) {
                $scope.ascending = !$scope.ascending;
            }else {
                $scope.orderCategory = 'release';
                $scope.ascending = false;
            }
            console.log('orderCategory: ', $scope.orderCategory);
            console.log('ascending: ', $scope.ascending);
            $scope.reload();
        };

        $scope.reload = function() {
            $location.search({orderCategory: $scope.orderCategory, ascending: $scope.ascending, name: $scope.searchedName,
                publisher: $scope.publishers, genres: $scope.genres, platform: $scope.platforms, developer: $scope.developers,
                pageNumber: $scope.pageNumber});
            $location.path('search');
        };

        var baseGames = Restangular.all('games');

        baseGames.getList({orderCategory: $scope.orderCategory, ascending: $scope.ascending, name: $scope.searchedName,
            publisher: $scope.publishers, genres: $scope.genres, platform: $scope.platforms, developer: $scope.developers,
            pageNumber: $scope.pageNumber})
            .then(function(response) {
                $scope.games = response.data;
                console.log('All ok2');
                console.log(response.data);
                console.log(response.headers());
        }, function(response) {
            console.log('Error with status code', response.status);
        });

    });

});
