'use strict';
define(['powerUp', 'AuthService', 'angular-local-storage'], function (powerUp) {

    // TODO BORRAR
    powerUp.factory('Data', function() {
        return {message: "I'm data from a service"};
    });

    // TODO BORRAR
    powerUp.service('searchedTitleService', function () {

        var searchedTitle = null;

        var setTitle = function(newTitle) {
            searchedTitle = newTitle;
        };

        var getTitle = function() {
            return searchedTitle;
        };

        return {
            setTitle: setTitle,
            getTitle: getTitle
        };
    });

    // 'Restangular' != 'restangular! http://stackoverflow.com/a/32904726/2333689
    powerUp.controller('MainCtrl', ['$scope', '$log', '$location', 'Restangular', 'AuthService', 'localStorageService', function($scope, $log, $location, Restangular, AuthService, LocalStorageService) {
        Restangular.setFullResponse(false);

        AuthService.trackToken();

        $scope.logOut = AuthService.logOut;
        $scope.apiLocation = 'http://localhost:8080/api';
        $scope.isLoggedIn = AuthService.isLoggedIn;
        $scope.currentUser = AuthService.getCurrentUser();

        // Track current page to redirect user back to where they were after logging in
        $scope.loginRedirect = '/';
        // Update current page URL on page change, except when in login page
        $scope.$on('$viewContentLoaded', function() {
            if($location.path() !== '/login') {
                $scope.loginRedirect = $location.url();
            }
        });

        // Watch the current user to always keep it updated
        $scope.$watch(
            AuthService.getCurrentUser,
            function(newVal) {
                $scope.currentUser = newVal;
            },
            true    // This is necessary because getCurrentUser returns a different object instance every time. With this as true, we check for value equality rather than instance equality
        );

        Waves.displayEffect();      // To get waves effects working, https://gist.github.com/stephenjang/123740713c0b0ab21c9a#gistcomment-1982064

        /*
         * Fetch possible game filters. Even though this is necessary only in Search, if the page changes or gets
         * reloaded the controller is lost. Main controller is always present so processing will continue loading
         * filters even on page changes.
         */
        $scope.filters = {};
        $scope.filterCategories = ['publisher', 'developer', 'genre', 'keyword', 'platform'];
        $scope.filtersReady = false;
        var remainingRequests = $scope.filterCategories.length;

        // Keep filters in local storage because it takes a long time to get these from the server
        if (LocalStorageService.get('filters')) {
            $log.debug('Loading filters from local storage');
            $scope.filters = LocalStorageService.get('filters');
            $scope.filtersReady = true;
        } else {
            $log.debug('Querying API for filters');
            $scope.filterCategories.forEach(function(filterType) {
                Restangular.all('games').all('filters').all(filterType).getList().then(function(response) {
                    $scope.filters[filterType] = response.data || response;    // TODO always use full response and response.data
                    remainingRequests--;
                    $log.debug('Done fetching filters for type ' + filterType + ', ' + remainingRequests + ' types remaining');
                    if (remainingRequests <= 0) {
                        $log.debug('Done fetching all filters, saving to local storage');
                        LocalStorageService.set('filters', $scope.filters);
                        $scope.filtersReady = true;
                    }
                }, function(error) {
                    $log.error('ERROR getting filters for type ' + filterType + ': ', error);
                });
            });
        }
	}]);
});
