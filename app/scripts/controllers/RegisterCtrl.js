'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('RegisterCtrl', function($scope, $location, $log, AuthService, Restangular) {

        /**
         * Register User and returns true if it could be registered. False if not.
         * @param data
         */
        $scope.submitted = false;
        $scope.register = function(form) {
            $scope.submitted = true;
            console.log($scope.userToSubmit);
            if (validate($scope.userToSubmit)) {
                var user = {username: $scope.userToSubmit.username, password: $scope.userToSubmit.password, email: $scope.userToSubmit.email};
                Restangular.all('users').post(user).then(function (response) {
                    AuthService.authenticate(user.username, user.password, function() {
                            $location.search();
                            $location.path('');
                        },
                        function(error) {
                            // TODO do something more useful, e.g. show the error
                            $log.error('There was an error logging in: ', error);
                            $location.search();
                            $location.path('login');
                        });
                }, function (response) {
                    $log.error('There was an error registering user', response);
                    return false;
                });
            }
        };
        function validate(userToSubmit) {
            return (userToSubmit && userToSubmit.username && userToSubmit.email && userToSubmit.password && userToSubmit.repeatPassword
                && userToSubmit.repeatPassword === userToSubmit.password);
        }
    });
    powerUp.directive('uniqueUsername', function(Restangular) {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, element, attrs, ngModel) {
                element.bind('blur', function (e) {
                    ngModel.$loading = true;
                    Restangular.all('users').one('username',element.val()).get().then(function () {
                        ngModel.$setValidity('unique', false);
                        ngModel.$loading = false;
                    }, function () {
                        ngModel.$setValidity('unique', true);
                        ngModel.$loading = false;
                    });
                });
            }
        };
    });
    powerUp.directive('uniqueEmail', function(Restangular) {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, element, attrs, ngModel) {
                element.bind('blur', function (e) {
                    ngModel.$loading = true;
                    Restangular.all('users').one('email',element.val()).get().then(function () {
                        ngModel.$setValidity('unique', false);
                        ngModel.$loading = false;
                    }, function () {
                        ngModel.$setValidity('unique', true);
                        ngModel.$loading = false;
                    });
                });
            }
        };
    });
    powerUp.directive('pwCheck', function () {
        return {
            require: 'ngModel',
            link: function (scope, elem, attrs, ctrl) {
                var firstPassword = '#' + attrs.pwCheck;
                elem.add(firstPassword).on('keyup', function () {
                    scope.$apply(function () {
                        var v = elem.val() === $(firstPassword).val();
                        ctrl.$setValidity('pwmatch', v);
                    });
                });
            }
        }
    });
});