'use strict';
define(['powerUp'], function(powerUp) {

	powerUp.controller('MainCtrl', function($scope, Restangular) {
		$scope.welcomeText = 'Welcome to your powerUp page';

        // Restangular.setDefaultHeaders({"Access-Control-Allow-Headers": "*"});
        // Restangular.setDefaultHeaders({'Access-Control-Allow-Origin': "*"});
        // Restangular.setDefaultHeaders({'Access-Control-Expose-Headers': "*"});

        powerUp.factory('Data', function(){
            return {message:"I'm data from a service"};
        });

        powerUp.service('LogInService', function () {

            var userLoggedIn = null;

            var isLoggedInStatus = false;

            var setLoggedInStatus = function(newLoggedInStatus) {
                isLoggedInStatus = newLoggedInStatus;
            };

            var setLoggedUser = function(newLoggedInUser) {
                userLoggedIn = newLoggedInUser;
            };

            var getLoggedUser = function(){
                return userLoggedIn;
            };

            var isLoggedIn = function(){
                return isLoggedInStatus;
            };

            return {
                isLoggedIn: isLoggedIn,
                setLoggedInStatus: setLoggedInStatus,
                setLoggedUser: setLoggedUser,
                getLoggedUser: getLoggedUser
            };
        });

        powerUp.service('searchedTitleService', function () {

            var searchedTitle = null;

            var setTitle = function(newTitle) {
                searchedTitle = newTitle;
            };

            var getTitle = function(){
                return searchedTitle;
            };

            return {
                setTitle: setTitle,
                getTitle: getTitle
            };
        });
        $scope.apiLocation = 'http://localhost:8080/api';

        // $scope.isLoggedIn = LogInService.
        // Restangular.all('users').getList()  // GET: /users
        //   .then(function(users) {
        //     console.log('All users: ', users);
        //   });
        //
        //     Restangular.one('users', 1).get().then(function(user) {
        //   console.log('User #2: ', user);
        // });
		console.log($scope); //	TODO borrar
		// var scope = angular.element('[ng-controller=myController]').scope();
        console.log("isLogIn: ", $scope.isLogIn);
	});
});
