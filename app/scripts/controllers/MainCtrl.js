'use strict';
define(['powerUp', 'AuthService', 'angular-local-storage', 'angular-environment', 'PaginationService'], function (powerUp) {

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
    powerUp.controller('MainCtrl', ['$scope', '$log', '$location', 'Restangular', 'AuthService', 'localStorageService', 'envService', 'PaginationService', function($scope, $log, $location, Restangular, AuthService, LocalStorageService, envService, PaginationService) {
        AuthService.trackToken();

        $scope.logOut = AuthService.logOut;
        $scope.apiLocation = envService.read('apiUrl');
        $scope.isLoggedIn = AuthService.isLoggedIn;
        $scope.currentUser = AuthService.getCurrentUser();

        // Track current page to redirect user back to where they were after logging in
        $scope.loginRedirect = '/';
        // Update current page URL on page change, except when in login page
        $scope.$on('$viewContentLoaded', function() {
            if (['/login', '/reset-password'].indexOf($location.path()) === -1) {
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

      /* *****************************************************************
       *                  PASSWORD RESET HELPER FUNCTION
       * *****************************************************************/
      $scope.resetPasswordUrl = function() {
        return $location.protocol()
          + '://' + $location.host()
          + ($location.port() !== 80 ? ':' + $location.port() : '')
          + (envService.read('prefix') || '') +
          '/#/reset-password?nonce={0}';
      };

      /* *****************************************************************
       *                  PROFILE PICTURE HELPER METHOD
       * *****************************************************************/
      var DEFAULT_PROFILE_PICTURE_URL = 'http://res.cloudinary.com/dtbyr26w9/image/upload/v1476797451/default-cover-picture.png';

      /**
       * Get the user's profile picture URL, falling back to a default profile picture.
       * @param url      The user whose profile picture URL to get
       * @return {string} The resulting profile picture URL
       */
      $scope.profilePictureUrl = function(url) {
        return url || DEFAULT_PROFILE_PICTURE_URL;
      };

        Waves.displayEffect();      // To get waves effects working, https://gist.github.com/stephenjang/123740713c0b0ab21c9a#gistcomment-1982064

        /* *************************************************************************************************************
         *                                          FILTER DOWNLOAD CONTROL
         * ************************************************************************************************************/
        /*
         * Fetch possible game filters. Even though this is necessary only in Search, if the page changes or gets
         * reloaded the controller is lost. Main controller is always present so processing will continue loading
         * filters even on page changes.
         */
        $scope.filters = {};
        $scope.filterCategories = ['genre', 'platform', 'publisher', 'developer'];
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
            $scope.filters[filterType] = [];
            var paginator = PaginationService.initialize(
              Restangular.all('games').all('filters').all(filterType),
              undefined,
              1,
              500
            );
            PaginationService.getAllPages(paginator, function(response) {
              $scope.filters[filterType] = $scope.filters[filterType].concat(response.data || response);
              $log.debug('Got page', paginator.pagination.pageNumber, '/', paginator.pagination.totalPages, 'of ' + filterType + 's');
              if (paginator.pagination.pageNumber >= paginator.pagination.totalPages) {
                $log.info('Done fetching filters for ' + filterType + ', ' + remainingRequests + ' remaining');
                if (--remainingRequests <= 0) {
                  $log.debug('Done fetching all filters, saving to local storage');
                  // TODO NOW treat each filter type independently (enable, save, etc.)
                  LocalStorageService.set('filters', $scope.filters);
                  $scope.filtersReady = true;
                }
              }
            }, function(error) {
              $log.error("Couldn't get filters for " + filterType, error);
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
