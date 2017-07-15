'use strict';
define(['trademygame', '../services/authService'], function(trademygame) {

	trademygame.controller('IndexCtrl', function($scope, Restangular, authService, $location, $window, $route, toastr, $translate) {
		$scope.welcomeText = 'Welcome to your trademygame page';
		$scope.pageSize = 15;

		$scope.loggedIn = authService.isLoggedIn();
		$scope.loggedUser = authService.loggedUser();
		$scope.loggedUserId  = authService.loggedId();
		$scope.loginform = {"username":"", "password":""};

		$scope.loginSubmit = function(){
			var authLogin = Restangular.all('auth/login');
			authLogin.post($scope.loginform).then(function(data){
				localStorage.setItem("token", data);
				$scope.loggedIn = true;
				$scope.loggedUser = authService.loggedUser();
				$scope.loggedUserId  = authService.loggedId();

				var current_url = $location.url();
				if(current_url == "/register"){
                    $location.path("/");
				}else{
					$route.reload();
				}

			}, function (data) {
                toastr.error($translate.instant('header_badlogin'),$translate.instant('error_error'));

            });

		};

		$scope.logout = function(){
			localStorage.removeItem("token");
			$scope.loggedIn = false;
			$scope.loggedUser = null;
            $route.reload();
		};


		Restangular.all('platforms/nintendo').getList().then(function(data) {
				 $scope.nintendoPlatforms = data.plain();
		});

		Restangular.all('platforms/sony').getList().then(function(data) {
				 $scope.sonyPlatforms = data;
		});

		Restangular.all('platforms/microsoft').getList().then(function(data) {
				 $scope.microsoftPlatforms = data;
		});

		$scope.performSearch = function(){
			var location = '/#/search?page=1';

			if($scope.selected != undefined){
				location += "&name=" + encodeURIComponent($scope.selected);
			}

			$window.location.href = location;
		}

		$scope.onSelect = function ($item, $model, $label) {
		    $scope.selected = $item;
		    $scope.performSearch();
		};

		$scope.goToFilter = function(){
			var location = '/#/filter';
			if($scope.selected != undefined){
				location += "?name=" + encodeURIComponent($scope.selected);
			}
			$window.location.href = location;
		}

		$scope.selected = undefined;
		Restangular.all('products/names').getList().then(function(data){
			$scope.names = data;
		});

		$scope.closeDropdown = function(){
			$("#login-dp").click();
		}

	});
});
