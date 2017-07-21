'use strict';

// TODO do I need to require loading circles here? They're only used in the view
define(['powerUp', 'angular-mocks', 'AuthService', 'loadingCircle', 'loadingCircleSmall', 'LikesService', 'PaginationService', 'ThreadsCtrl'], function () {

    describe('ThreadsCtrl', function () {

        beforeEach(module('powerUp'));

        var $httpBackend,   // Used to mock API calls
            $rootScope,     // Used to create a $scope
            $scope,
            $location,
            $log,
            Restangular,
            AuthService,
            LikesService,
            PaginationService,
            ThreadsCtrl;

        // Other variables
        // var user = {id: 1, username: "paw", email: "paw@paw.paw"};

        // Injection
        beforeEach(inject(function (_$httpBackend_, _$rootScope_, _$log_, _$location_, _Restangular_, _AuthService_, _LikesService_, _PaginationService_, _$controller_) {
            $httpBackend = _$httpBackend_;
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();     // Actually create a $scope
            $location = _$location_;
            $log = _$log_;
            Restangular = _Restangular_;
            AuthService = _AuthService_;
            LikesService = _LikesService_;
            PaginationService = _PaginationService_;
            // Actually create the controller, passing all the dependencies to it
            ThreadsCtrl = _$controller_('ThreadsCtrl', {$scope: $scope, $location: $location, $log: $log, Restangular: Restangular, AuthService: AuthService, LikesService: LikesService, PaginationService: PaginationService});
        }));

        /*
         * Test the isLoggedIn function. You can organize your tests however you want (e.g. before I was testing when
         * the user was logged in and when the user was not logged in, but I thought it would be better organized if I
         * grouped by method), but each test group goes under a "describe" block.
         */
        describe('#isLikedByCurrentUser', function () {
            describe('When not logged in', function() {
                it('Should always return false', function () {
                    expect($scope.isLikedByCurrentUser()).toBe(false);
                    expect($scope.isLikedByCurrentUser(/*TODO pass a thread*/)).toBe(false);
                });
            });
        });

        // TODO test more functions

        // TODO test API stuff with $httpBackend
    });
});