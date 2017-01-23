'use strict';
define(['powerUp'], function(powerUp) {

	powerUp.controller('MainCtrl', function($scope, Restangular) {
		$scope.welcomeText = 'Welcome to your powerUp page';

        powerUp.factory('Data', function(){
            return {message:"I'm data from a service"};
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

        Restangular.all('users').getList()  // GET: /users
          .then(function(users) {
            console.log('All users: ', users);
          });

            Restangular.one('users', 2).get().then(function(user) {
          console.log('User #2: ', user);
        });
	});
});
