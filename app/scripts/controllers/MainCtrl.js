'use strict';
define(['powerUp'], function (powerUp) {

  // 'Restangular' != 'restangular! http://stackoverflow.com/a/32904726/2333689
  powerUp.controller('MainCtrl', ['$scope', '$cookies', 'Restangular', function ($scope, $cookies, Restangular) {
    $scope.welcomeText = 'Welcome to your powerUp page';

    // Log in if not logged in
    if ($cookies.hasOwnProperty('JSESSIONID') && $cookies.JSESSIONID) {
      console.log('Already logged in as PAW');
      console.log('To clear session cookie, go to the "Application" tab in Chrome Dev tools, Storage => Cookies => localhost and delete JSESSIONID');
    } else {
      console.log("Logging in as PAW...");
      var auth = Restangular.all('auth/login');
      auth.post({'username': 'paw', 'password': 'paw'})
        .then(function (data) {
          console.log('Logged in as PAW, session cookie saved, future requests will be sent as PAW');
          console.log('To clear session cookie, go to the "Application" tab in Chrome Dev tools, Storage => Cookies => localhost and delete JSESSIONID');
        });
      // auth.customPOST({"username": "paw", "password": "paw"}, undefined, undefined, {"Content-Type": "application/json"});
    }

    // Get all users TODO change backend to return array instead of object
    // Restangular.all('users').getList()  // GET: /users
    // .then(function(users) {
    //   console.log('All users: ', users);
    // });

  }]);
});
