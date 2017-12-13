'use strict';
define(['powerUp', 'AuthService', 'sweetalert.angular', 'validator-js'], function(powerUp) {

    powerUp.controller('LoginCtrl', ['$scope', '$location', '$log', 'Restangular', 'AuthService', function($scope, $location, $log, Restangular, AuthService) {
        var validator = require('validator-js');

        $scope.loginError = false;

        $scope.isLoggingIn = false;
        $scope.logIn = function(form) {
            if ($scope.isLoggingIn) {
                return;
            }
            $scope.isLoggingIn = true;
            var logInAccount = {username: $scope.username, password: $scope.password};
            AuthService.authenticate($scope.username, $scope.password,
                function() {
                    $scope.loginError = false;
                    var redirect = $scope.loginRedirect || '/';
                    $location.url(redirect);
                },
                function(error) {
                    // TODO do something more useful, e.g. show the error
                    $log.error('There was an error logging in: ', error);
                    $scope.loginError = true;
                    $scope.isLoggingIn = false;
                }
            );
        };

        var resettingPassword = false;
        $scope.resetPassword = function() {
            if (resettingPassword) {
                return;
            }
            swal({
                title: 'Password Reset',
                text: 'Please enter your account email',
                type: 'input',
                inputType: 'email',
                inputPlaceholder: 'Email',
                showCancelButton: true,
                closeOnConfirm: false,
                confirmButtonText: 'Reset Password'
            },
            function(inputValue) {
                if (inputValue === false) {
                    return false;
                } else if (inputValue === '') {
                    swal.showInputError('Please write your account email');
                    return false;
                } else if (!validator.isEmail(inputValue)) {
                    swal.showInputError('Please enter a valid email address');
                    return false;
                }

                resettingPassword = true;
                var user = null;
                swal.disableButtons();
                Restangular.all('users').one('email', inputValue).get().then(function(response) {
                    user = response.data || response;
                    Restangular.one('users', user.id).all('password').remove({template: $scope.resetPasswordUrl()}).then(function(response) {
                        swal('Password Reset!', 'Please check your email for reset instructions', 'success');
                        resettingPassword = false;
                    }, function(error) {
                        swal.showInputError('Server error, please try again');
                        resettingPassword = false;
                    });
                }, function(error) {
                    if (error.status === 404) {
                        swal.showInputError('No user with that email address');
                    } else {
                        swal.showInputError('Server error, please try again');
                        $log.error('Error getting user by email: ', error);
                    }
                    swal.enableButtons();
                    resettingPassword = false;
                });
            });
        };
    }]);
});
