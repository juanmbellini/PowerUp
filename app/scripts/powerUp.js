'use strict';
define(['routes',
    'services/dependencyResolverFor',
    'i18n/i18nLoader!',
    'angular',
    'angular-route',
    'angular-translate',
    'angular-cookies',
    'restangular'],
  function (config, dependencyResolverFor, i18n) {
    var powerUp = angular.module('powerUp', [
      'ngRoute',
      'pascalprecht.translate',
      'restangular',
      'ngCookies'
    ]);
    powerUp
      .config(
        ['$routeProvider',
          '$locationProvider',
          '$controllerProvider',
          '$compileProvider',
          '$filterProvider',
          '$provide',
          '$translateProvider',
          'RestangularProvider',
          function ($routeProvider, $locationProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $translateProvider, RestangularProvider) {

            powerUp.controller = $controllerProvider.register;
            powerUp.directive = $compileProvider.directive;
            powerUp.filter = $filterProvider.register;
            powerUp.factory = $provide.factory;
            powerUp.service = $provide.service;

            if (config.routes !== undefined) {
              angular.forEach(config.routes, function (route, path) {
                $routeProvider.when(path, {
                  templateUrl: route.templateUrl,
                  resolve: dependencyResolverFor(['controllers/' + route.controller]),
                  controller: route.controller,
                  gaPageTitle: route.gaPageTitle
                });
              });
            }
            if (config.defaultRoutePath !== undefined) {
              $routeProvider.otherwise({redirectTo: config.defaultRoutePath});
            }

            /*
             * Enabling HTML5 mode removes the # from the URLs, but it doesn't work as you might expect:
             * FROM HOME, vising /login for example works. But typing /login into the address bar will NOT work, since
             * it's angular from home who does the redirection. For that to work, we would need to do some server-side
             * configuration, which we can't do. For now this remains commented out.
             *
             * NOTE: If uncommenting this, remember to add <base href="/"> in index.html's <head> section. For production,
             * this needs to be something like /paw-2016b-02.
             */
            // $locationProvider.html5Mode(true);

            $translateProvider.translations('preferredLanguage', i18n);
            $translateProvider.preferredLanguage('preferredLanguage');

            RestangularProvider.setBaseUrl('http://localhost:8080/api');  // TODO change this on production
            RestangularProvider.setDefaultHttpFields({
              withCredentials: true                                       // To allow authentication via CORS
            });

          }]);
    return powerUp;
  }
);
