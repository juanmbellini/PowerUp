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
        restangular: '../../bower_components/restangular/dist/restangular',
        lodash: '../../bower_components/lodash/dist/lodash',
        materialize: '../../bower_components/materialize/bin/materialize',
        'materialize-amdified': '../../bower_components/materialize-amdified/materialize.amd',
        hammerjs: '../../bower_components/hammerjs/hammer',
        velocity: '../../bower_components/velocity/velocity',
        'velocity.ui': '../../bower_components/velocity/velocity.ui',
        sweetalert: '../../bower_components/sweetalert/dist/sweetalert.min',
        'sweetalert.angular': '../../bower_components/ngSweetAlert/SweetAlert.min',
        ngSweetAlert: '../../bower_components/ngSweetAlert/SweetAlert',
        'slick-carousel': '../../bower_components/slick-carousel/slick/slick',
        lightbox2: '../../bower_components/lightbox2/dist/js/lightbox',
        onComplete: 'directives/on-complete',
        sessionService: 'services/sessionService',
        'csrf-service': 'services/csrfService',
        'angular-slick': '../../bower_components/angular-slick/dist/slick'
    },
    shim: {
        jquery: {
            exports: 'jQuery'
        },
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
        'materialize-amdified': {
            deps: [
                'jquery',
                'hammerjs',
                'velocity'
            ]
        },
        restangular: {
            exports: 'Restangular',
            deps: [
                'angular',
                'lodash'
            ]
        },
        sweetalert: {
            deps: [
                'jquery'
            ]
        },
        'sweetalert.angular': {
            deps: [
                'angular',
                'sweetalert'
            ]
        },
        'slick-carousel': {
            exports: 'slick',
            deps: [
                'jquery'
            ]
        },
        lightbox2: {
            exports: 'lightbox',
            deps: [
                'jquery'
            ]
        },
        onComplete: {
            deps: [
                'angular'
            ]
        },
        sessionService: {
            deps: [
                'angular'
            ]
        },
        'csrf-service': {
            deps: [
                'angular'
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
        'materialize-amdified',
        'restangular',
        'sweetalert',
        'controllers/MainCtrl'
    ],
    function() {
        angular.bootstrap(document, ['powerUp']);
    }
);
