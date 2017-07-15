define(['trademygame', 'directives/equalFields'], function(trademygame) {

    'use strict';
    trademygame.controller('register', function($scope, Restangular, $location, $window, toastr, $translate) {

      $scope.registerForm = {
        "userName": undefined,
        "email": undefined,
        "password": undefined,
        "repeatPassword": undefined,
        "phone": undefined,
        "country": undefined,
        "iconId": 1,
      }


      $scope.usernamePattern = /^([A-Z]|[a-z]|[0-9]|(á|é|í|ó|ú|ñ|Á|É|Í|Ó|Ú|Ñ))+$/;

      //TODO: check this pattern
      //$scope.phonePattern = /(\(?(\+|00)?[0-9]{1,6}\))?([0-9]{1,4}-?)?([0-9]{3,6}-?[0-9]{3,6})/;


      $scope.phonePattern = /^(\(((\+|00)?[0-9]{1,6}\))|((\+|00)?[0-9]{1,6}))?([0-9]{1,4}-?)?([0-9]{3,6}-?[0-9]{3,6})$/;


      $scope.register = function(){
          $scope.registerForm.country = document.getElementById("selectedCountry").getAttribute("value");
          if($scope.registerForm.country == ""){
            $scope.registerForm.country = undefined;
          }

          $scope.userExists = false;
          $scope.emailExists = false;

        Restangular.all('users').post($scope.registerForm).then(function(data){

          toastr.success($translate.instant('confirmAccount_userCreated'),$translate.instant('success'));
          $window.location.href = "/#/";

        }, function(response){
          var errors = response.data.errors;
          if(errors != undefined){
            for(var i = 0; i < errors.length; i++){
              if(errors[i].property == "createUser.arg1.userName"){
                $scope.userExists = true;
              }
              if(errors[i].property == "createUser.arg1.email"){
                  $scope.emailExists = true;
              }
            }
          }
        });
      }
      $scope.selectIcon = function(num){
        $("#icon" + $scope.registerForm.iconId).css({'border-width':'0px'});
        $scope.registerForm.iconId = num;
        $("#icon" + $scope.registerForm.iconId).css({'border-color':'black', 'border-style': 'solid', 'border-width':'5px'});

      }
    });
});
