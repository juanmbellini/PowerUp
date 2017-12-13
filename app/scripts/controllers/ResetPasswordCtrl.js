'use strict';
define(['powerUp', 'AuthService', 'sweetalert.angular', 'validator-js'], function (powerUp) {

  powerUp.controller('ResetPasswordCtrl', ['$scope', '$location', '$log', '$timeout', 'AuthService', 'Restangular', function ($scope, $location, $log, $timeout, AuthService, Restangular) {
    var validator = require('validator-js');

    var nonce = $location.search().nonce;
    if (!nonce || !nonce.length) {
      $log.warn('Reached password reset without a nonce, redirecting to home');
      $location.search({});
      $location.path('/');
    }

    $scope.submitted = false;
    $scope.resetting = false;
    $scope.model = {};

    $scope.resetPassword = function (form) {
      if ($scope.resetting) {
        return;
      }
      $scope.resetting = true;
      $scope.submitted = true;
      if (validate($scope.model)) {
        $log.debug('Attempting to reset password');
        var payload = {password: $scope.model.password};
        var params = {nonce: nonce};
        Restangular.all('users').all('password').post(payload, params).then(function (response) {
          $log.debug('Password successfully reset');
          if (AuthService.isLoggedIn()) {
            $log.debug('Forcing log out');
            AuthService.logOut(function () {
              onSuccessfulReset();
            }, function(error) {
              $log.error("Couldn't log out after password reset:", error);
              swal({
                title: 'Password reset! But...',
                text: "Your password was successfully reset, but we couldn't log you out. Please try logging out manually via the nav bar, and log in again.",
                type: 'error'
              });
            });
          } else {
            onSuccessfulReset();
          }
        }, function (error) {
          $log.error('There was an error resetting password:', error);
          $scope.resetting = false;
          swal({
            title: "Couldn't reset password",
            text: error.data.errors.map(function(e) {
              return e.message;
            }).join('<br />'),
            type: 'error'
          });
        });
      } else {
        $scope.resetting = false;
      }
    };

    /* *********************************************************
     *                        RESEND TOKEN
     * ********************************************************/
    var resendingToken = false;
    $scope.resendToken = function() {
      if (resendingToken) {
        return;
      }
      swal({
          title: 'Send new token',
          text: "Please enter your account email and we'll send you a new token",
          type: 'input',
          inputType: 'email',
          inputPlaceholder: 'Email',
          showCancelButton: true,
          closeOnConfirm: false,
          confirmButtonText: 'Get new token'
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

          resendingToken = true;
          var user = null;
          swal.disableButtons();
          Restangular.all('users').one('email', inputValue).get().then(function(response) {
            user = response.data || response;
            var returnUrl = $location.protocol() + '://' + $location.host() + ($location.port() !== 80 ? ':' + $location.port() : '') + '/#/reset-password?nonce={0}';
            Restangular.one('users', user.id).all('password').remove({template: returnUrl}).then(function(response) {
              resendingToken = false;
              swal({
                title: 'Token sent!',
                text: 'Please check your email for your new token. Redirecting you to home...',
                type: 'success',
                closeOnConfirm: false
              });
              $timeout(function() {
                // Redirect to home
                $location.search({});
                $location.path('/');
              }, 5000);
            }, function(error) {
              $log.error('Error re-resetting password:', error);
              swal.showInputError(error.data.errors.map(function(e) {
                return e.message;
              }).join('<br />'));
              resendingToken = false;
            });
          }, function(error) {
            if (error.status === 404) {
              swal.showInputError('No user with that email address');
            } else {
              $log.error('Error getting user by email: ', error);
              swal.showInputError(error.data.errors.map(function(e) {
                return e.message;
              }).join('<br />'));
            }
            swal.enableButtons();
            resendingToken = false;
          });
        });
    };

    function validate(model) {
      return model && model.password && model.repeatPassword && model.repeatPassword === model.password;
    }

    function onSuccessfulReset() {
      swal({
        title: 'Success',
        text: 'Password successfully reset! You will now be redirected to the log in page',
        type: 'success',
        closeOnConfirm: false
      });
      $timeout(function() {
        $location.search({});
        $location.path('/login');
      }, 5000);
    }
  }]);

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
    };
  });
});
