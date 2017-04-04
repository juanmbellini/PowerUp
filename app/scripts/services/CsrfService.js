'use strict';
define(['powerUp', 'angular-local-storage'], function(powerUp) {

	powerUp.service('CsrfService', ['$log', 'Restangular', 'localStorageService', function($log, Restangular, LocalStorage) {
        var trackingToken = false;


        /**
         * Adds a Restangular response interceptor to keep track of the CSRF token on responses from state-changing
         * requests (i.e. POST, etc.), or when requesting a new token.
         *
         * NOTE: It is the programmer's responsibility to send the tracked CSRF token when necessary, the service does
         * NOT automatically add it to necessary requests.
         */
        function trackToken() {
            if (trackingToken) {
                $log.warn('CsrfService is already tracking the CSRF token. Ignoring call to trackToken().');
                return;
            }
            trackingToken = true;
            $log.debug('Adding CSRF response interceptor');
            Restangular.addResponseInterceptor(function(data, operation, what, url, response, deferred) {
                /*
                 * Track responses from state-changing requests (i.e. not GET, HEAD, etc.). GETLIST is here because
                 * the operation param is GET for get() calls, and GETLIST for getList() calls.
                 * Also track responses from the CSRF endpoint.
                 */
                if (['GET', 'GETLIST', 'HEAD', 'TRACE', 'OPTIONS'].indexOf(operation.toUpperCase()) === -1 || url.match(/.*\/api\/auth\/csrf$/) !== null) {
                    $log.debug('CSRF response interceptor active');
                    var csrfHeaderName = response.headers('X-CSRF-HEADER');
                    if (csrfHeaderName !== null) {
                        var newToken = response.headers(csrfHeaderName);
                        var oldToken = getToken();
                        if (newToken !== oldToken) {
                            $log.debug(oldToken === null ? 'Setting' : 'Updating', 'CSRF token');
                        } else {
                            $log.debug('No change in CSRF token, overwriting anyway');
                        }
                        setTokenHeader(csrfHeaderName);
                        setToken(newToken);
                    } else {
                        $log.warn('No CSRF header present on a CSRF-protected endpoint, make sure this is not a bug!');
                    }
                }
                return data;
            });
        }

        /**
         * @returns {String|null} The saved CSRF token, or null if not set.
         */
        function getToken() {
            return LocalStorage.get('csrfToken');
        }

        /**
         * Registers a new CSRF token in the service.
         *
         * @param newToken The new token.
         */
        function setToken(newToken) {
            if (newToken === null) {
                LocalStorage.remove('csrfToken');
            } else {
                LocalStorage.set('csrfToken', newToken);
            }
        }

        /**
         * @returns {boolean} Whether a token is registered in the CsrfService.
         */
        function isTokenSet() {
            return getToken() !== null;
        }

        /**
         * Requests a CSRF token from the API and registers it in the CsrfService.
         *
         * @param callback (Optional) Callback to run on success.
         */
        function requestToken(callback) {
            Restangular.all('auth/csrf').getList().then(function() {
                // Request interceptor already extracts CSRF header from response
                if (typeof callback !== 'undefined') {
                    callback();
                }
            });
        }



        /**
         * @returns {String|null} The HTTP header name under which the server passes the CSRF token, e.g.
         * 'X-CSRF-TOKEN', or null if not set.
         */
        function getTokenHeader() {
            return LocalStorage.get('csrfTokenHeader');
        }

        function setTokenHeader(newTokenHeader) {
            if (newTokenHeader === null) {
                LocalStorage.remove('csrfTokenHeader');
            } else {
                LocalStorage.set('csrfTokenHeader', newTokenHeader);
            }
        }

        function isTokenHeaderSet() {
            return getTokenHeader() !== null;
        }



        return {
            trackToken: trackToken,
            getToken: getToken,
            setToken: setToken,
            isTokenSet: isTokenSet,
            requestToken: requestToken,

            getTokenHeader: getTokenHeader,
            setTokenHeader: setTokenHeader,
            isTokenHeaderSet: isTokenHeaderSet
        };
	}]);
});
