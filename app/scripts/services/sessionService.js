'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.service('SessionService', ['$log', 'Restangular', function ($log, Restangular) {
        var currentUser = null;
        var trackingToken = false;
        var sessionToken = null;


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
                var newToken = response.headers('x-auth-token');
                if (newToken !== null) {
                    if (newToken !== sessionToken) {
                        $log.debug(sessionToken === null ? 'Setting' : 'Updating', 'session token');
                    } else {
                        // $log.debug('No change in session token, overwriting anyway');
                    }
                    sessionToken = newToken;
                } else {
                    // $log.debug('No session token header present');
                }
                return data;
            });
            // Request
            $log.debug('Adding session token REQUEST interceptor');
            Restangular.addFullRequestInterceptor(function(element, operation, what, url, headers, params) {
                if (sessionToken !== null) {
                    // $log.debug('Adding session token to request headers');
                    return {
                        headers: {'X-AUTH-TOKEN': sessionToken}
                    };
                } else {
                    // $log.debug('No saved session token, not adding to request headers');
                }
            });
        }

        function setCurrentUser(newUser) {
            currentUser = newUser;
        }

        function getCurrentUser() {
            return currentUser;
        }

        function isLoggedIn() {
            return currentUser !== null;
        }

        function logOut() {
            setCurrentUser(null);
            // TODO actually hit API
        }

        return {
            trackToken: trackToken,
            isLoggedIn: isLoggedIn,
            setCurrentUser: setCurrentUser,
            getCurrentUser: getCurrentUser,
            logOut: logOut
        };
    }]);
});
