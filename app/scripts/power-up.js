'use strict';
define(['routes',
	'services/dependencyResolverFor',
	'i18n/i18nLoader!',
	'angular',
	'angular-route',
	'bootstrap',
	'angular-translate'],
	function(config, dependencyResolverFor, i18n) {
		var power-up = angular.module('power-up', [
			'ngRoute',
			'pascalprecht.translate'
		]);
		power-up
			.config(
				['$routeProvider',
				'$controllerProvider',
				'$compileProvider',
				'$filterProvider',
				'$provide',
				'$translateProvider',
				function($routeProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $translateProvider) {

					power-up.controller = $controllerProvider.register;
					power-up.directive = $compileProvider.directive;
					power-up.filter = $filterProvider.register;
					power-up.factory = $provide.factory;
					power-up.service = $provide.service;

					if (config.routes !== undefined) {
						angular.forEach(config.routes, function(route, path) {
							$routeProvider.when(path, {templateUrl: route.templateUrl, resolve: dependencyResolverFor(['controllers/' + route.controller]), controller: route.controller, gaPageTitle: route.gaPageTitle});
						});
					}
					if (config.defaultRoutePath !== undefined) {
						$routeProvider.otherwise({redirectTo: config.defaultRoutePath});
					}

					$translateProvider.translations('preferredLanguage', i18n);
					$translateProvider.preferredLanguage('preferredLanguage');
				}]);
		return power-up;
	}
);
