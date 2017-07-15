'use strict';

/*
 * ALWAYS require powerUp and angular-mocks, then require whatever else you need.
 *
 * What you require here should be EXACTLY as the keys for the "paths" object in test-main.js
 *
 * If you want to require something that is not in test-main.js, you need to add it there AND in karma.conf.js
 */
define(['powerUp', 'angular-mocks', 'authService', 'angular-local-storage'], function () {

    // what you are testing
    describe('AuthService', function () {

        // Necessary
        beforeEach(module('powerUp'));

        // Angular stuff that you will need
        var $httpBackend,   // Used to mock API calls
            $rootScope,     // Used to create a $scope
            $scope,
            AuthService,    // Actual AuthService
            LocalStorage;   // I will need this to mock local storage later

        // Other variables
        var user = {id: 1, username: "paw", email: "paw@paw.paw"};

        // Manually do all the angular injection
        beforeEach(inject(function (_$httpBackend_, _$rootScope_, _AuthService_, _localStorageService_) {
            $httpBackend = _$httpBackend_;
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();
            AuthService = _AuthService_;
            LocalStorage = _localStorageService_;
        }));

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
                beforeEach(mockCurrentUser);

                it('Should show logged in', function () {
                    expect(AuthService.isLoggedIn()).toBe(true);
                });
            });
        });

        // Same shit
        describe('#getCurrentUser', function () {

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
                beforeEach(mockCurrentUser);

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

        // TODO test other exported methods

        /**
         * Mocks user stored in local storage
         */
        function mockCurrentUser() {
            /*
             * spyOn lets you override or mock methods for an object, track how many times they've been called, or tell
             * them to return something. In here I'm mocking LocalStorage.get, and having LocalStorage.get('currentUser')
             * return the mocked user, null otherwise.
             * If you always want to return the same thing, use .and.returnValue(yourValue)
             */
            spyOn(LocalStorage, 'get').and.callFake(function (key) {
                if (key === 'currentUser') {
                    return user;
                } else {
                    return null;
                }
            });
        }
    });
});