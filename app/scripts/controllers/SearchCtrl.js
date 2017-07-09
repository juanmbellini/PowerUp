'use strict';
define(['powerUp', 'loadingCircle', 'paginationService'], function(powerUp) {


    powerUp.controller('SearchCtrl', ['$scope', '$timeout', '$location', '$log', 'Restangular', 'PaginationService', function($scope, $timeout, $location, $log, Restangular, PaginationService) {
        Restangular.setFullResponse(true);

        $scope.overAllAmountOfElements = 100;
        $scope.totalPages = 114;
        $scope.pageSize = 25;

        var defaults = {
            name: undefined,
            publisher: undefined,
            developer: undefined,
            genre: undefined,
            keyword: undefined,
            platform: undefined,
            // Pagination
            orderBy: 'name',
            sortDirection: 'asc',
            pageSize: 25,
            pageNumber: 1
        };

        $scope.searchParams = {};

        // Safely set all defaults
        for(var key in defaults) {
            var defaultValue = defaults[key];
            var value = $location.search()[key];
            if(typeof value !== 'string') {
                $scope.searchParams[key] = defaultValue;
            } else {
                if(typeof defaultValue === 'number') {
                    value = parseInt(value, 10);
                }
                $scope.searchParams[key] = value;
            }
        }

        $scope.games = null;

        $scope.submitSearch = function() {
            $log.debug('Reloading Search with specified parameters');
            $location.search($scope.searchParams);
            $location.path('search');
        };

        $scope.getPagesNumbers = function () {
            return [1,2,3,4,5];
        };

        $scope.hasFilters = function() {
            return ($scope.searchParams.developers !== undefined || $scope.searchParams.publishers !== undefined || $scope.searchParams.genres !== undefined || $scope.searchParams.platforms !== undefined);
        };

        $scope.sortNameButton = function() {
            if ($scope.searchParams.orderBy === undefined || angular.equals($scope.searchParams.orderBy,'name')) {
                $scope.toggleSortDirection();
            }else {
                $scope.searchParams.orderBy = 'name';
                $scope.searchParams.sortDirection = 'asc';
            }
            $location.search('orderBy', $scope.searchParams.orderBy);
            $location.search('sortDirection', $scope.searchParams.sortDirection);
        };

        $scope.sortRatingButton = function() {
            if (angular.equals($scope.searchParams.orderBy,'avg_score')) {
                $scope.toggleSortDirection();
            }else {
                $scope.searchParams.orderBy = 'avg_score';
                $scope.searchParams.sortDirection = 'desc';
            }
            $location.search('orderBy', $scope.searchParams.orderBy);
            $location.search('sortDirection', $scope.searchParams.sortDirection);
        };

        $scope.sortReleaseButton = function() {
            if (angular.equals($scope.searchParams.orderBy,'release')) {
                $scope.toggleSortDirection();
            }else {
                $scope.searchParams.orderBy = 'release';
                $scope.searchParams.sortDirection = 'desc';
            }
            $location.search('orderBy', $scope.searchParams.orderBy);
            $location.search('sortDirection', $scope.searchParams.sortDirection);
        };

        $scope.toggleSortDirection = function() {
            if($scope.searchParams.sortDirection === 'asc') {
                $scope.searchParams.sortDirection = 'desc';
            } else if($scope.searchParams.sortDirection === 'desc') {
                $scope.searchParams.sortDirection = 'asc';
            }
            $log.warn('Called toogleSortDirection but sort direction is neither asc nor desc. Doing nothing.')
        };

        // Pagination control
        $scope.gamesPaginator = PaginationService.initialize(Restangular.all('games'), undefined, $scope.searchParams.pageNumber, $scope.searchParams.pageSize, $scope.searchParams.orderBy, $scope.searchParams.sortDirection);
        PaginationService.setRequestParams($scope.gamesPaginator, $scope.searchParams);
        PaginationService.get($scope.gamesPaginator, function(response) {
            $scope.games = response.data;
            $log.debug('Found ' + $scope.games.length + ' games');
        }, function(error) {
            // TODO do something more useful
            $log.error('Error performing search: ', error);
            $scope.games = [];
        });

        /* ********************************************
         *      MATERIALIZE INITIALIZATION
         * *******************************************/
        $timeout(function() {
            // Collapsible for filters
            angular.element('.collapsible').collapsible();

            // Tab for filter sections
            angular.element('ul.tabs').tabs();

            $log.debug('Fired Materialize initializers');
        }, 500);
    }]);
});
