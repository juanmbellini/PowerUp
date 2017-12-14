'use strict';
define(['routes',
    'services/dependencyResolverFor',
    'i18n/i18nLoader!',
    'angular',
    'angular-route',
    'angular-translate',
    'angular-environment',
    'restangular',
    'sweetalert.angular',
    'angular-local-storage',
    'ng-file-upload'
    ],
  function (config, dependencyResolverFor, i18n) {
    var powerUp = angular.module('powerUp', [
      'ngRoute',
      'pascalprecht.translate',
      'environment',
      'restangular',
      'oitozero.ngSweetAlert',
      'LocalStorageModule',
      'ngFileUpload'
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
          '$sceDelegateProvider',
          'localStorageServiceProvider',
          'envServiceProvider',
          '$logProvider',
          function ($routeProvider, $locationProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $translateProvider, RestangularProvider, $sceDelegateProvider, localStorageServiceProvider, envServiceProvider, $logProvider) {

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

            // Whitelist URLs for interpolations ('self' = the application's domain, i.e. localhost or pawserver.itba.edu.ar, etc.)
            $sceDelegateProvider.resourceUrlWhitelist([
                'self',
                'https://www.youtube.com/**',
                'http://player.twitch.tv/**'
            ]);

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

            // Set environment-dependent configuration
            envServiceProvider.config({
              domains: {
                  development: ['localhost:9000'],
                  staging: ['localhost:8080', 'localhost:80', '192.168.1.42', 'lipusal.redirectme.net'],
                  production: ['pawserver.it.itba.edu.ar', 'pawserver.itba.edu.ar', '200.5.121.138']   // TODO check whether IP should stay, we don't know if it's static
              },
              vars: {
                  development: {
                      apiUrl: 'http://localhost:8080/api'
                  },
                  staging: {
                      apiUrl: '/paw-2016b-02/api',
                      prefix: '/paw-2016b-02'
                  },
                  production: {
                      apiUrl: 'http://pawserver.it.itba.edu.ar/paw-2016b-02/api',
                      prefix: '/paw-2016b-02'
                  },
                  defaults: {
                      // Defaults go here when no environment matches
                      apiURl: './api'
                  }
              }
            });
            // Set the current environment
            envServiceProvider.check();

            // Configure Restangular
            RestangularProvider.setBaseUrl(envServiceProvider.read('apiUrl'));
            if (!envServiceProvider.is('production')) {
                RestangularProvider.setDefaultHttpFields({
                    // To allow authentication via CORS
                    withCredentials: true
                });
            }
            RestangularProvider.setFullResponse(true);

            // Disable debug logs in production
            if (envServiceProvider.is('production')) {
              $logProvider.debugEnabled(false);
            } else {
              console.debug('Current environment is', envServiceProvider.get(), ', API url is', RestangularProvider.configuration.baseUrl);
            }

            // Configure local storage
            localStorageServiceProvider
                .setPrefix('paw')
                .setStorageType('localStorage')     // Use local storage (no expiration)
                .setDefaultToCookie(false)          // DON'T fall back to cookies for security, users won't get to use the page
                .setNotify(false, false);           // Don't broadcast setItem and removeItem events

          }]);
    return powerUp;
  }
);
