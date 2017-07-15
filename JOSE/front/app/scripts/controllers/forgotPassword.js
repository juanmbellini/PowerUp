define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.controller('forgotPassword', function($scope, toastr, Restangular, $window, $translate) {

        $scope.forgotPassForm = {
            "email": undefined
        }


        $scope.forgotPass = function(){

            $scope.emailExists = false;


            Restangular.all('users').one('forgotPassword').customPUT({}, ''  , {email: $scope.forgotPassForm.email}, {}).then(function(data){


                $window.location.href = "/#/";
                toastr.success($translate.instant('forgotPassword_message'),$translate.instant('success'));



            }, function(response){
                $scope.emailExists = true;
            });
        }


    });

});
