/* global paths */
'use strict';
require.config({
    baseUrl: '/scripts',
    paths: {
        requirejs: '../../bower_components/requirejs/require',
        jquery: '../../bower_components/jquery/dist/jquery',
        json3: '../../bower_components/json3/lib/json3',
        angular: '../../bower_components/angular/angular',
        'angular-route': '../../bower_components/angular-route/angular-route',
        'angular-translate': '../../bower_components/angular-translate/angular-translate',
        'angular-cookies': '../../bower_components/angular-cookies/angular-cookies',
        'es5-shim': '../../bower_components/es5-shim/es5-shim',
        moment: '../../bower_components/moment/moment',
        materialize: '../../bower_components/materialize-amdified/materialize.amd',
        restangular: '../../bower_components/restangular/dist/restangular',
        lodash: '../../bower_components/lodash/dist/lodash',
        hammerjs: '../../bower_components/hammerjs/hammer'
    },
    shim: {
        angular: {
            exports: 'angular',
            deps: [
                'jquery'
            ]
        },
        'angular-route': {
            deps: [
                'angular'
            ]
        },
        'angular-cookies': {
            deps: [
                'angular'
            ]
        },
        'angular-translate': {
            deps: [
                'angular'
            ]
        },
        materialize: {
            deps: [
                'jquery'
            ]
        },
        restangular: {
            exports: 'Restangular',
            deps: [
                'angular',
                'lodash'
            ]
        }
    },
    packages: [

    ]
});

if (paths) {
    require.config({
        paths: paths
    });
}


require([
        'angular',
        'powerUp',
        'materialize',
        'restangular',
        'controllers/MainCtrl'
    ],
    function() {
        angular.bootstrap(document, ['powerUp']);
    }
);
