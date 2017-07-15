define(['trademygame', 'directives/equalFields'], function(trademygame) {

    'use strict';
    trademygame.controller('changePassword', function($scope, $routeParams, Restangular, $window, toastr, $translate) {

        $scope.updatePassForm = {
            "tokenId": undefined,
            "password": undefined,
            "repeatPassword": undefined
        };


        $scope.token = $routeParams.token;

        $scope.updatePassForm.tokenId = $scope.token;

		$scope.passwordPattern = /^([A-Z]|[a-z]|[0-9]|(á|é|í|ó|ú|ñ|Á|É|Í|Ó|Ú|Ñ))+$/;


        Restangular.one('users').one('password').customGET(''  , {token: $scope.token}).then(function(){

        }, function(Response){
            $window.location.href = "/#/error404";
        });



        $scope.changePass = function(){

            Restangular.one('users').one('password').customPUT($scope.updatePassForm).then(function(data){

                $window.location.href = "/#/";
                toastr.success($translate.instant('changePassword_success'),$translate.instant('success'));

            }, function(response) {
                toastr.error($translate.instant('error_changing_pass'),$translate.instant('error_error'));
            });
        }



    });

});
