'use strict';

/*
 * ALWAYS require powerUp and angular-mocks, then require:
 *  - Everything else in the define() on the top of the file you're testing
 *  - Everything else required by whatever you are testing, unless it's an angular thing that starts with $
 *  - What you're actually testing
 *
 * What you require here should be typed EXACTLY as the keys for the "paths" object in test-main.js
 * If you want to require something that is not in test-main.js, you need to add it there AND in karma.conf.js if it's
 * not already there.
 */
define(['powerUp', 'angular-mocks', 'restangular', 'angular-local-storage', 'AuthService'], function () {

    // What you are testing
    describe('AuthService', function () {

        // Always necessary
        beforeEach(module('powerUp'));

        // All the dependencies for AuthService
        var $httpBackend,   // Used to mock API calls
            $log,           // Everything that the AuthService depends on
            $location,
            Restangular,
            LocalStorage,
            AuthService;    // Actual AuthService

        // Other variables
        var user = {id: 1, username: 'paw', email: 'paw@paw.paw'};
        var credentials = {username: 'paw', password: 'superSecretPassword'};
        var token = 'blah.blah._blah_-blah';

        // Have angular inject everything
        beforeEach(inject(function (_$httpBackend_, _$log_, _$location_, _Restangular_, _localStorageService_, _AuthService_) {
            $httpBackend = _$httpBackend_;
            $log = _$log_;
            $location = _$location_;
            Restangular = _Restangular_;
            LocalStorage = _localStorageService_;
            AuthService = _AuthService_;
        }));

        afterEach(function() {
            // Verify we made EXACTLY the number of network requests we expected - not more and not less
            $httpBackend.verifyNoOutstandingExpectation();
            $httpBackend.verifyNoOutstandingRequest();

            // Clear local storage
            LocalStorage.clearAll();
        });

        /*
         * Test the isLoggedIn function. You can organize your tests however you want (e.g. before I was testing when
         * the user was logged in and when the user was not logged in, but I thought it would be better organized if I
         * grouped by method), but each test group goes under a "describe" block.
         */
        describe('#isLoggedIn', function () {

            // Context for a test
            describe('When not logged in', function () {
                // What you're testing
                it('Should show not logged in', function () {
                    // Actual test, kind of like jUnit but prettier. See syntax at https://jasmine.github.io/api/2.6/
                    expect(AuthService.isLoggedIn()).toBe(false);
                });
            });

            describe('When logged in', function () {
                // Pass a function to run before each test in this "describe" block. Since I want the user to be logged
                // in, I need to mock the current user being stored in local storage.
                beforeEach(mockLoggedIn);

                it('Should show logged in', function () {
                    expect(AuthService.isLoggedIn()).toBe(true);
                });
            });
        });

        describe('#getCurrentUser', function () {

            describe('When not logged in', function () {
                it('Should return null', function () {
                    expect(AuthService.getCurrentUser()).toBeNull();
                });
            });

            describe('When logged in', function () {
                beforeEach(mockLoggedIn);

                it('Should NOT return null', function () {
                    expect(AuthService.getCurrentUser()).not.toBeNull();
                });

                it('Should match the current user', function () {
                    expect(AuthService.getCurrentUser()).toEqual(user);
                });
            });
        });

        describe('#isCurrentUser', function () {

            describe('When not logged in', function () {
                it('Should not match the current user by object', function () {
                    expect(AuthService.isCurrentUser(user)).toBe(false);
                });

                it('Should not match the current user by username', function () {
                    expect(AuthService.isCurrentUser(user.username)).toBe(false);
                });

                it('Should not match the current user by id', function () {
                    expect(AuthService.isCurrentUser(user.id)).toBe(false);
                });
            });

            describe('When logged in', function () {
                beforeEach(mockLoggedIn);

                it('Should match the current user by object', function () {
                    expect(AuthService.isCurrentUser(user)).toBe(true);
                });

                it('Should not match the current user by a different object', function () {
                    expect(AuthService.isCurrentUser({id: 4})).toBe(false);
                    expect(AuthService.isCurrentUser({username: 'not PAW'})).toBe(false);
                });

                it('Should match the current user by username', function () {
                    expect(AuthService.isCurrentUser(user.username)).toBe(true);
                });

                it('Should match the current user by id', function () {
                    expect(AuthService.isCurrentUser(user.id)).toBe(true);
                });
            });
        });

        describe('#trackToken', function() {
            var debugSpy, warningSpy;
            beforeEach(function() {
                debugSpy = spyOn($log, 'debug').and.callThrough();
                warningSpy = spyOn($log, 'warn').and.callThrough();
            });

            it('Should allow one call', function() {
                AuthService.trackToken();
                expect(debugSpy).toHaveBeenCalledTimes(2);
                expect(debugSpy).toHaveBeenCalledWith('Adding auth token RESPONSE interceptor');
                expect(debugSpy).toHaveBeenCalledWith('Adding auth token REQUEST interceptor');
            });

            it('Should disregard further calls', function() {
                AuthService.trackToken();
                AuthService.trackToken();
                expect(warningSpy).toHaveBeenCalled();
                expect(warningSpy).toHaveBeenCalledWith('AuthService is already tracking the auth token. Ignoring call to trackToken().');
            });
        });

        describe('#logOut', function() {

            describe('When not logged in', function() {
                it('Does nothing', function() {
                    // afterEach() will ensure no requests are made
                });
            });

            describe('When logged in', function() {
                beforeEach(function() {
                    mockLoggedIn();
                    AuthService.trackToken();
                    Restangular.setFullResponse(true);
                });

                // Extracted to variable because ESLint
                var hasAuthorizationHeader = function(headers) {
                    return headers.hasOwnProperty('Authorization');
                };

                it('Logs out on 204', function () {
                    // When POSTing to auth/logout with no data and authorization token, respond 204 No Content
                    $httpBackend.expectPOST(Restangular.configuration.baseUrl.concat('/auth/logout'), [], hasAuthorizationHeader).respond(204);

                    AuthService.logOut();

                    // Flush all mocked network requests. The afterEach() will make sure we made exactly the number of calls we expected - no more and no less.
                    $httpBackend.flush();

                    expect(AuthService.isLoggedIn()).toBe(false);
                });

                it('Logs out on 401', function() {
                    $httpBackend.expectPOST(Restangular.configuration.baseUrl.concat('/auth/logout'), [], hasAuthorizationHeader).respond(401);

                    AuthService.logOut();
                    $httpBackend.flush();

                    expect(AuthService.isLoggedIn()).toBe(false);
                });

                it('Does NOT log out on another 4xx', function() {
                    $httpBackend.expectPOST(Restangular.configuration.baseUrl.concat('/auth/logout'), [], hasAuthorizationHeader).respond(404);

                    AuthService.logOut();
                    $httpBackend.flush();

                    expect(AuthService.isLoggedIn()).toBe(true);
                });
            });
        });

        describe('#authenticate', function() {
            beforeEach(function() {
                AuthService.trackToken();
                Restangular.setFullResponse(true);
            });

            it('Logs in correctly', function () {
                $httpBackend.expectPOST(Restangular.configuration.baseUrl.concat('/auth/login'), credentials).respond(200, user);
                AuthService.authenticate(credentials.username, credentials.password);
                $httpBackend.flush();

                expect(AuthService.isLoggedIn()).toBe(true);
            });

            it('Sets the current user', function () {
                $httpBackend.expectPOST(Restangular.configuration.baseUrl.concat('/auth/login'), credentials).respond(200, user);
                AuthService.authenticate(credentials.username, credentials.password);
                $httpBackend.flush();

                expect(AuthService.getCurrentUser().id).toEqual(user.id);
                expect(AuthService.getCurrentUser().username).toEqual(user.username);
                expect(AuthService.getCurrentUser().email).toEqual(user.email);
            });

            it('Does NOT log in on 4xx', function() {
                $httpBackend.expectPOST(Restangular.configuration.baseUrl.concat('/auth/login'), credentials).respond(401);
                AuthService.authenticate(credentials.username, credentials.password);
                $httpBackend.flush();

                expect(AuthService.isLoggedIn()).toBe(false);
            });
        });

        /**
         * Mocks user and JWT stored in local storage
         */
        function mockLoggedIn() {
            LocalStorage.set('currentUser', user);
            LocalStorage.set('jwt', token);
        }
    });
});