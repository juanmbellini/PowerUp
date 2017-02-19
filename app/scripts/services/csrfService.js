'use strict';
define(['powerUp'], function(powerUp) {

	powerUp.service('CsrfService', ['$log', 'Restangular', function($log, Restangular) {
        var token = null;
        var tokenHeader = null;
        var tokenParam = null;
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
                $log.warn('CsrfService is already tracking the CSRF token. Ignoring second call to trackToken().');
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
                        if (newToken !== getToken()) {
                            $log.debug(getToken() === null ? 'Setting' : 'Updating', 'CSRF token');
                        } else {
                            $log.debug('No change in CSRF token, overwriting anyway');
                        }
                        setTokenHeader(csrfHeaderName);
                        setToken(newToken);
                        setTokenParam(response.headers('X-CSRF-PARAM'));
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
            return token;
        }

        /**
         * Registers a new CSRF token in the service.
         *
         * @param newToken The new token.
         */
        function setToken(newToken) {
            token = newToken;
        }

        /**
         * @returns {boolean} Whether a token is registered in the CsrfService.
         */
        function isTokenSet() {
            return token !== null;
        }

        /**
         * Requests a CSRF token from the API and registers it in the CsrfService.
         *
         * @param callback Callback to run on success.
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
            return tokenHeader;
        }

        function setTokenHeader(newTokenHeader) {
            tokenHeader = newTokenHeader;
        }

        function isTokenHeaderSet() {
            return tokenHeader !== null;
        }



        // TODO I think we will just use HTTP headers for CSRF, no need for request params. Confirm and delete this section.
        /**
         * @returns {String|null} The HTTP request parameter name under which the server accepts the CSRF token for
         * state-changing requests, e.g. '_csrf'
         */
        function getTokenParam() {
            return tokenParam;
        }

        function setTokenParam(newTokenParam) {
            tokenParam = newTokenParam;
        }

        function isTokenParamSet() {
            return tokenParam !== null;
        }



        return {
            trackToken: trackToken,
            getToken: getToken,
            setToken: setToken,
            isTokenSet: isTokenSet,
            requestToken: requestToken,

            getTokenHeader: getTokenHeader,
            setTokenHeader: setTokenHeader,
            isTokenHeaderSet: isTokenHeaderSet,

            getTokenParam: getTokenParam,
            setTokenParam: setTokenParam,
            isTokenParamSet: isTokenParamSet
        };
	}]);
});
