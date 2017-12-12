'use strict';
define(['powerUp', 'AuthService', 'angular-local-storage', 'angular-environment'], function (powerUp) {

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

 //   powerUp.config(function($routeProvider, $locationProvider) {
 //      $routeProvider
//            .when('/list', {
//                templateUrl: 'views/lists/lists.html',
 //               controller: 'controllers/ListsCtrl',
//                reloadOnSearch: false
//            });
//    });

    // 'Restangular' != 'restangular! http://stackoverflow.com/a/32904726/2333689
    powerUp.controller('MainCtrl', ['$scope', '$log', '$location', 'Restangular', 'AuthService', 'localStorageService', 'envService', function($scope, $log, $location, Restangular, AuthService, LocalStorageService, envService) {
        Restangular.setFullResponse(false);

        AuthService.trackToken();

        $scope.logOut = AuthService.logOut;
        $scope.apiLocation = envService.read('apiUrl');
        $scope.isLoggedIn = AuthService.isLoggedIn;
        $scope.currentUser = AuthService.getCurrentUser();

        // Track current page to redirect user back to where they were after logging in
        $scope.loginRedirect = '/';
        // Update current page URL on page change, except when in login page
        $scope.$on('$viewContentLoaded', function() {
            if ($location.path() !== '/login') {
                $scope.loginRedirect = $location.url();
            }
        });

        // Names of statuses to show
        $scope.namesOfStatuses = {'plan-to-play': 'Plan to play', 'no-play-status': 'No play status', 'playing': 'Playing', 'played': 'Played'};


        $scope.writeReviewRedirect = null;
        // Update current page URL on page change, except when in login page
        $scope.$on('$viewContentLoaded', function() {
            if ($location.path() !== '/write-review') {
                $scope.writeReviewRedirect = $location.url();
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
        } else if (envService.is('production')) {
          // FIXME optimize API call so we can use filters in production
          $log.warn('WARNING! Filters are not stored in local storage and querying the server will most likely bring it down. Aborting filter load.',
            'To get the filters, run the app on development or staging and copy-paste them from local storage (or ask Juen).');
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


      /* **************************************************
       *   Get a random game (used in various pages)
       * **************************************************/
      // TODO either use this in more pages (that's why it was added to MainCtrl) or move it to SearchCtrl
      $scope.randomGameLoading = false;
      $scope.randomGame = function(onSuccess, onError) {
        if ($scope.randomGameLoading) {
          return;
        }
        $scope.randomGameLoading = true;
        Restangular.all('games').one('random').get().then(function(response) {
          if (typeof onSuccess === 'function') {
            onSuccess(response);
          }
          $scope.randomGameLoading = false;
          // Redirect to appropriate game page
          $location.path('/game');
          $location.search({id: response.data.randomGameId});
        }, function(error) {
          if (typeof onError === 'function') {
            onError(error);
          } else {
            $log.error('Error getting a random game: ', error);
          }
          $scope.randomGameLoading = false;
        });
      };
	}]);
});
