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
        bootstrap: '../../bower_components/bootstrap/dist/js/bootstrap',
        affix: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/affix',
        alert: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/alert',
        button: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/button',
        carousel: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/carousel',
        collapse: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/collapse',
        dropdown: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/dropdown',
        tab: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/tab',
        transition: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/transition',
        scrollspy: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/scrollspy',
        modal: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/modal',
        tooltip: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/tooltip',
        popover: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/popover'
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
