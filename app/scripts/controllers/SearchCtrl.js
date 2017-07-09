'use strict';
define(['powerUp', 'loadingCircle', 'paginationService'], function(powerUp) {


    powerUp.controller('SearchCtrl', ['$scope', '$timeout', '$location', '$log', 'Restangular', 'PaginationService', function($scope, $timeout, $location, $log, Restangular, PaginationService) {
        Restangular.setFullResponse(true);

        $scope.overAllAmountOfElements = 100;
        $scope.totalPages = 114;
        $scope.pageSize = 25;

        $scope.searchParams = {
            name: $location.search().name,
            publisher: $location.search().publisher,
            developer: $location.search().developer,
            genre: $location.search().genre,
            keyword: $location.search().keyword,
            platform: $location.search().platform,
            // Pagination
            orderBy: $location.search().orderBy || 'name',
            sortDirection: $location.search().sortDirection || 'asc',
            pageSize: $location.search().pageSize || 25,
            pageNumber: $location.search().pageNumber || 1
        };

        // Sanitize values
        for(var key in $scope.searchParams) {
            if($scope.searchParams.hasOwnProperty(key)) {
                if ($scope.searchParams[key] === true) {
                    delete $scope.searchParams[key];
                }
            }
        }

        $scope.games = null;

        $scope.submitSearch = function() {
            $log.debug('Reloading Search with specified parameters: ', $scope.searchParams);
            $location.search($scope.searchParams);
            $location.path('search');
        };

        /**
         * Gets the range of pages to include in the pagination links on the bottom of the page. Ensures that no link
         * will go out of range of total page count (either left or right).
         *
         * @return {Array}
         */
        $scope.getPageRange = function () {
            if(!$scope.gamesPaginator.pagination.totalPages) {
                return [];
            }
            var deltaPages = 2; // How many pages before and after to show
            var result = [];
            for(var i = -deltaPages; i <= deltaPages; i++) {
                var page = $scope.gamesPaginator.pagination.pageNumber + i;
                if(page >= 1 && page <= $scope.gamesPaginator.pagination.totalPages) {
                    result.push(page);
                }
            }
            return result;
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

        $scope.setPageNumber = function(number) {
            if(!$scope.gamesPaginator.pagination.totalPages) {
                return;
            }
            if(number >= 1 && number <= $scope.gamesPaginator.pagination.totalPages) {
                $scope.searchParams.pageNumber = number;
            }
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

        // Reload page on param changes
        $scope.$watch('searchParams', function(newVal, oldVal) {
            if(typeof oldVal === 'undefined' || angular.equals(newVal, oldVal)) {
                return; // Initial change, ignore
            }
            $scope.submitSearch();
        }, true);

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
