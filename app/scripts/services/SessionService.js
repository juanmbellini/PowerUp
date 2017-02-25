'use strict';
define(['powerUp', 'angular-local-storage'], function(powerUp) {

    powerUp.service('SessionService', ['$log', '$location', 'Restangular', 'localStorageService', 'CsrfService', function ($log, $location, Restangular, LocalStorage, CsrfService) {
        var trackingToken = false;

        /* ********************************************
         *              Private functions
         * *******************************************/
        function setToken(newToken) {
            if (newToken === null) {
                LocalStorage.remove('sessionToken');
            } else {
                LocalStorage.set('sessionToken', newToken);
            }
        }

        function getToken() {
            return LocalStorage.get('sessionToken');
        }

        /* ********************************************
         *              Public functions
         * *******************************************/

        /**
         * Adds a Restangular response interceptor to keep track of the X-AUTH-TOKEN header on all responses. Also adds
         * a Restangular request interceptor to add the authentication token (if set) on all requests.
         */
        function trackToken() {
            if (trackingToken) {
                $log.warn('SessionService is already tracking the session token. Ignoring call to trackToken().');
                return;
            }
            trackingToken = true;
            // Response
            $log.debug('Adding session token RESPONSE interceptor');
            Restangular.addResponseInterceptor(function(data, operation, what, url, response, deferred) {
                var oldToken = getToken();
                var newToken = response.headers('x-auth-token');
                if (newToken !== null) {
                    if (newToken !== oldToken) {
                        $log.debug(oldToken === null ? 'Setting' : 'Updating', 'session token');
                    } else {
                        // $log.debug('No change in session token, overwriting anyway');
                    }
                    setToken(newToken);
                } else {
                    // $log.debug('No session token header present');
                }
                return data;
            });
            // Request
            $log.debug('Adding session token REQUEST interceptor');
            Restangular.addFullRequestInterceptor(function(element, operation, what, url, headers, params) {
                var token = getToken();
                if (token !== null) {
                    // $log.debug('Adding session token to request headers');
                    headers['X-AUTH-TOKEN'] = token;
                    return {
                        headers: headers
                    };
                } else {
                    // $log.debug('No saved session token, not adding to request headers');
                }
            });
        }

        function setCurrentUser(newUser) {
            if (newUser === null) {
                LocalStorage.remove('currentUser');
            } else {
                LocalStorage.set('currentUser', newUser);
            }
        }

        function getCurrentUser() {
            return LocalStorage.get('currentUser');
        }

        function isLoggedIn() {
            return getCurrentUser() !== null;
        }

        function logOut() {
            if (CsrfService.isTokenSet()) { // This should always be set, but just in case
                var csrfHeaders = {};
                csrfHeaders[CsrfService.getTokenHeader()] = CsrfService.getToken();
                Restangular.all('auth/logout').post(undefined, undefined, csrfHeaders).then(function (data) {
                    setCurrentUser(null);
                    setToken(null);
                    CsrfService.setToken(null);
                    CsrfService.setTokenHeader(null);
                    $location.search();
                    $location.path('');
                }, function(error) {
                    $log.error('There was an error logging out:', error);
                });
            } else {
                $log.debug('No CSRF token set, retrieving and retrying with token');
                CsrfService.requestToken(function() {
                    logOut(); // Try again with the CSRF token set
                });
            }
        }

        // Public exported functions
        return {
            trackToken: trackToken,
            isLoggedIn: isLoggedIn,
            setCurrentUser: setCurrentUser,
            getCurrentUser: getCurrentUser,
            logOut: logOut
        };
    }]);
});
