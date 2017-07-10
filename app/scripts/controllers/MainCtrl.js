'use strict';
define(['powerUp', 'authService', 'angular-local-storage'], function (powerUp) {

    powerUp.factory('Data', function() {
        return {message: "I'm data from a service"};
    });

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
    powerUp.controller('MainCtrl', ['$scope', '$log', 'Restangular', 'AuthService', 'localStorageService', function($scope, $log, Restangular, AuthService, LocalStorageService) {
        Restangular.setFullResponse(false);

        AuthService.trackToken();

        $scope.logOut = AuthService.logOut;
        $scope.apiLocation = 'http://localhost:8080/api';
        $scope.isLoggedIn = AuthService.isLoggedIn;
        $scope.currentUser = AuthService.getCurrentUser();

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
         * reloaded the controller is lost. Main controller is always present so processing will continue even on page
         * changes.
         * TODO consider passing this to a service using local storage, it takes a long time to retrieve this from the server
         */
        var filterTypes = ['publisher', 'developer', 'genre', 'keyword', 'platform'];
        var remainingRequests = filterTypes.length;
        $scope.filters = {};
        $scope.filtersReady = false;

        if(LocalStorageService.get('filters')) {
            $log.debug('Loading filters from local storage');
            $scope.filters = LocalStorageService.get('filters');
            $scope.filtersReady = true;
        } else {
            $log.debug('Querying API for filters');
            filterTypes.forEach(function(filterType) {
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
