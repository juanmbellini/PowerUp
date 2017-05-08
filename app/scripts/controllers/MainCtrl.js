'use strict';
define(['powerUp', 'authService', 'csrf-service'], function (powerUp) {

    powerUp.factory('Data', function() {
        return {message: "I'm data from a service"};
    });

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

    // 'Restangular' != 'restangular! http://stackoverflow.com/a/32904726/2333689
    powerUp.controller('MainCtrl', function($scope, $cookies, Restangular, AuthService, CsrfService) {
        Restangular.setFullResponse(false);
        // powerUp.controller('MainCtrl', ['$scope', '$cookies', 'Restangular', function ($scope, $cookies, Restangular) {

        AuthService.trackToken();


        $scope.welcomeText = 'Welcome to your powerUp page';

        // // Log in if not logged in
        // if ($cookies.hasOwnProperty('JSESSIONID') && $cookies.JSESSIONID) {
        //     console.log('Already logged in as PAW');
        //     console.log('To clear session cookie, go to the "Application" tab in Chrome Dev tools, Storage => Cookies => localhost and delete JSESSIONID');
        // } else {
        //     console.log('Logging in as PAW...');
        //     var auth = Restangular.all('auth/login');
        //     auth.post({'username': 'paw', 'password': 'paw'})
        //         .then(function (data) {
        //             console.log('Logged in as PAW, session cookie saved, future requests will be sent as PAW');
        //             console.log('To clear session cookie, go to the "Application" tab in Chrome Dev tools, Storage => Cookies => localhost and delete JSESSIONID');
        //         });
        //     // auth.customPOST({'username': 'paw', 'password': 'paw'}, undefined, undefined, {'Content-Type': 'application/json'});
        // }

        // Restangular.setDefaultHeaders({'Access-Control-Allow-Headers': '*'});
        // Restangular.setDefaultHeaders({'Access-Control-Allow-Origin': '*'});
        // Restangular.setDefaultHeaders({'Access-Control-Expose-Headers': '*'});


        $scope.range = function(min, max, step) {
            step = step || 1;
            var input = [];
            for (var i = min; i <= max; i += step) {
                input.push(i);
            }
            return input;
        };

        $scope.logOut = AuthService.logOut;

        $scope.apiLocation = 'http://localhost:8080/api';

        $scope.isLoggedIn = AuthService.isLoggedIn;

        $scope.currentUser = AuthService.getCurrentUser();
        // Watch the current user to always keep it updated
        $scope.$watch(
            AuthService.getCurrentUser,
            function(newVal) {
                $scope.currentUser = newVal;
            },
            true    // This is necessary because getCurrentUser returns a different object instance every time. With this as true, we check for value equality rather than instance equality
        );

        // Restangular.all('users').getList()  // GET: /users
        //   .then(function(users) {
        //     console.log('All users: ', users);
        //   });
        //
        //     Restangular.one('users', 1).get().then(function(user) {
        //   console.log('User #2: ', user);
        // });
		// var scope = angular.element('[ng-controller=myController]').scope();
	});
});
