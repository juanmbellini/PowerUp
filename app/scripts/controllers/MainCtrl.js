'use strict';
define(['powerUp'], function(powerUp) {

	powerUp.controller('MainCtrl', function($scope, Restangular) {
		$scope.welcomeText = 'Welcome to your powerUp page';

    Restangular.all('users').getList()  // GET: /users
      .then(function(users) {
        console.log('All users: ', users);
      });

		Restangular.one('users', 2).get().then(function(user) {
      console.log('User #2: ', user);
    });
	});
});
