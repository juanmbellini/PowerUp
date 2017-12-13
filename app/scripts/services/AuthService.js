'use strict';
define(['powerUp', 'angular-local-storage'], function(powerUp) {

    powerUp.service('AuthService', ['$log', '$location', 'Restangular', 'localStorageService', function ($log, $location, Restangular, LocalStorage) {
        var trackingToken = false;

        // Endpoints for which authentication is optional, and for which authentication will be sent
        // if the user is logged in
        var optionalAuthenticationEndpoints = [
            /.*/
        ];
        // /\/threads\/?/

        /* ********************************************
         *              Private functions
         * *******************************************/
        function setToken(newToken) {
            if (newToken === null) {
                LocalStorage.remove('jwt');
            } else {
                LocalStorage.set('jwt', newToken);
            }
        }

        function getToken() {
            return LocalStorage.get('jwt');
        }

        function setCurrentUser(newUser) {
            if (newUser === null) {
                LocalStorage.remove('currentUser');
            } else {
                LocalStorage.set('currentUser', newUser);
            }
        }

        function isOptionallyAuthenticatedEndpoint(url) {
            // Disregard protocol, domain and port; care only about path after '/api'
            var regex = /\/api(.*)/;
            if (!regex.test(url)) {
                return false;
            }
            url = regex.exec(url)[1];
            // Return true on the first element that matches, or false if no element matches.
            // Thanks to https://stackoverflow.com/a/2641374/2333689
            return optionalAuthenticationEndpoints.some(function(urlRegex) {
                return urlRegex.test(url);
            });
        }

        /* ********************************************
         *              Public functions
         * *******************************************/

        /**
         * Adds a Restangular response interceptor to keep track of the authentication token header on all responses.
         * Also adds a Restangular request interceptor to add the authentication token (if set) on all requests.
         */
        function trackToken() {
            if (trackingToken) {
                $log.warn('AuthService is already tracking the auth token. Ignoring call to trackToken().');
                return;
            }
            trackingToken = true;
            // Response
            $log.debug('Adding auth token RESPONSE interceptor');
            Restangular.addResponseInterceptor(function(data, operation, what, url, response, deferred) {
                var oldToken = getToken();
                var newToken = response.headers('X-TOKEN');
                if (newToken !== null) {
                    if (newToken !== oldToken) {
                        $log.debug(oldToken === null ? 'Setting' : 'Updating', 'auth token');
                    } else {
                        // $log.debug('No change in auth token, overwriting anyway');
                    }
                    setToken(newToken);
                } else {
                    // $log.debug('No auth token header present');
                }
                return data;
            });
            // Request
            $log.debug('Adding auth token REQUEST interceptor');
            Restangular.addFullRequestInterceptor(function(element, operation, what, url, headers, params) {
                // Only attach token to protected endpoints, or to optionally authenticated GET endpoints when logged in
                if (['post', 'put', 'remove'].indexOf(operation) !== -1 || (['get', 'getList'].indexOf(operation) !== -1 && isOptionallyAuthenticatedEndpoint(url)) && isLoggedIn()) {
                    var token = getToken();
                    if (token !== null) {
                        // $log.debug('Adding auth token to request headers');
                        headers.Authorization = 'Bearer ' + token;
                        return {
                            headers: headers
                        };
                    } else {
                        $log.warn('Attempted to add JWT to request headers but there is no saved auth token, doing nothing. Authentication may fail for this request.');
                    }
                }
            });
        }

        /**
         * Authenticates with the API. On success, saves authenticated user's info in local storage.
         *
         * @param username The username.
         * @param password The password.
         * @param successCallback (Optional) Function to call on successful authentication.
         * @param failureCallback (Optional) Function to call on authentication failure.
         */
        function authenticate(username, password, successCallback, failureCallback) {
            Restangular.all('auth/login').post({username: username, password: password})
                .then(function(response) {
                    var data = response.data || response;
                    setCurrentUser(data);   // TODO only copy fields to object? i.e. don't copy full Restangularized object
                    // TODO pass parameters to callback?
                    $log.info('Successfully authenticated as ', getCurrentUser().username);
                    if (typeof successCallback !== 'undefined') {
                        successCallback();
                    }
                })
                .catch(function(error) {
                    // TODO handle HTTP 403 etc. and network errors differently
                    if (typeof failureCallback !== 'undefined') {
                        failureCallback(error);
                    } else {
                        $log.error('Authentication error: ', error);
                    }
                });
        }

        /**
         * "Logs out" of the API by invalidating the authentication token received in {@link authenticate}. Removes user
         * info from local storage on success.
         *
         * @param successCallback (Optional) Function to call on success.
         * @param failureCallback (Optional) Function to call on failure.
         */
        function logOut(successCallback, failureCallback) {
            if (!isLoggedIn()) {
                return;
            }
            Restangular.all('auth/logout').post(undefined, undefined).then(function (response) {
                setCurrentUser(null);
                setToken(null);
                $log.info('Successfully logged out');
                if (typeof successCallback !== 'undefined') {
                    successCallback();
                } else {
                    $location.search({});
                    $location.path('');
                }
            }, function(error) {
                if (error.status === 401) {
                    // Either token expired or password changed, either way these credentials won't work anymore. Assume logged out successfully.
                    $log.warn('Credentials rejected when logging out, assuming successful logout.');
                    setCurrentUser(null);
                    setToken(null);
                    if (typeof successCallback !== 'undefined') {
                        successCallback();
                    } else {
                        $location.search({});
                        $location.path('');
                    }
                    return;
                }
                if (typeof failureCallback !== 'undefined') {
                    failureCallback(error);
                } else {
                    $log.error('There was an error logging out: ', error);
                }
            });
        }

        function getCurrentUser() {
            return LocalStorage.get('currentUser');
        }

        function isLoggedIn() {
            return getCurrentUser() !== null;
        }

        /**
         * Checks whether the specified data corresponds to the current user.
         *
         * @param data {object,string,number} Can be a user object with a username or id property, a username (string)
         * or a user ID (number). Each case is handled appropriately. If an unsupported data type is sent, always
         * returns false.
         * @returns {boolean} Whether the specified data corresponds to the current user.
         */
        function isCurrentUser(data) {
            if (!isLoggedIn()) {
                return false;
            }
            switch (typeof data) {
                case 'object':
                    return (data.hasOwnProperty('username') && data.username === getCurrentUser().username) ||
                        (data.hasOwnProperty('id') && data.id === getCurrentUser().id);
                case 'string':
                    return data === getCurrentUser().username;
                case 'number':
                    return data === getCurrentUser().id;
                default:
                    return false;
            }
        }

        // Public exported functions
        return {
            trackToken: trackToken,
            authenticate: authenticate,
            logOut: logOut,
            isLoggedIn: isLoggedIn,
            getCurrentUser: getCurrentUser,
            isCurrentUser: isCurrentUser
        };
    }]);
});
