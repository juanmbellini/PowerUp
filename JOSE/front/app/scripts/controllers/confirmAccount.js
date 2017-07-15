define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.controller('confirmAccount', function($scope, $routeParams, $location, Restangular, toastr, $translate) {

      $scope.token = $routeParams.token;

      Restangular.all('users').all('confirmAccount').customPOST({}, ''  , {token: $scope.token}, {}).then(function(data){
        toastr.success($translate.instant('confirmAccount_success'),$translate.instant('success'));
        $location.path("/");
      }, function(response){
        toastr.error($translate.instant('confirmAccount_error'),$translate.instant('error_error'));
        $location.path("/");
      });


    });
});
