'use strict';
define(['routes',
	'services/dependencyResolverFor',
	'i18n/i18nLoader!',
	'angular',
	'angular-route',
	'bootstrap',
	'angular-translate',
	'angular-bootstrap',
	'countrySelect',
	'restangular',
	'flow',
	'toastr'],

	function(config, dependencyResolverFor, i18n) {
		var trademygame = angular.module('trademygame', [
			'ngRoute',
			'pascalprecht.translate',
			'ui.bootstrap',
			'countrySelect',
			'restangular',
			'flow',
			'toastr'
		]);


		trademygame
			.config(
				['$routeProvider',
				'$controllerProvider',
				'$compileProvider',
				'$filterProvider',
				'$provide',
				'$translateProvider',
				'RestangularProvider',
				'$httpProvider',
				'$locationProvider',
				'toastrConfig',
				function($routeProvider, $controllerProvider, $compileProvider, $filterProvider,
					$provide, $translateProvider, RestangularProvider, $httpProvider, $locationProvider, toastrConfig) {
					trademygame.controller = $controllerProvider.register;
					trademygame.directive = $compileProvider.directive;
					trademygame.filter = $filterProvider.register;
					trademygame.factory = $provide.factory;
					trademygame.service = $provide.service;

					angular.extend(toastrConfig,{
                        closeButton:true,
                        positionClass: 'toast-bottom-right',
                        preventOpenDuplicates:true
                    });

					if (config.routes !== undefined) {
						angular.forEach(config.routes, function(route, path) {
							$routeProvider.when(path, {templateUrl: route.templateUrl, resolve: dependencyResolverFor(['controllers/' + route.controller]), controller: route.controller, gaPageTitle: route.gaPageTitle});
						});
					}
					if (config.defaultRoutePath !== undefined) {
						$routeProvider.otherwise({redirectTo: config.defaultRoutePath});
					}

					$httpProvider.interceptors.push(function($q) {
					    return {
					     'request': function(config) {
					          config.headers['Authorization'] = localStorage.getItem("token");
					          return config;
					      }
					    };
					  });

					$translateProvider.translations('preferredLanguage', i18n);
					$translateProvider.preferredLanguage('preferredLanguage');

					RestangularProvider.setBaseUrl('/api');
					/*
					$locationProvider.html5Mode({
					           enabled: true,
					           requireBase: true,
					           rewriteLinks: false
					});*/

				}]);

        trademygame.run(['$rootScope', 'authService', '$location', 'toastr', '$translate', function ($rootScope, authService, $location, toastr, $translate) {
            $rootScope.$on('$routeChangeStart', function (event) {
            	var url = $location.url();

            	if(authService.isLoggedIn()){
            		switch(url){
						case "/register":
                            toastr.error($translate.instant('error_accessDenied'),$translate.instant('error_error'));
                            $location.path("/");
                            break;
					}

				}else{
					if(url == "/newoffer"){
                        toastr.error($translate.instant('error_accessDenied'),$translate.instant('error_error'));
                        $location.path("/");
					}else if(url.includes("/offer") && url.includes("/trade")){
						toastr.error($translate.instant('error_accessDenied'),$translate.instant('error_error'));
                        $location.path("/");
					}	
					
				}
            });


		}]);
		return trademygame;
	}
);
