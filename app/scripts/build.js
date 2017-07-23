/* global paths */
'use strict';
require.config({
    baseUrl: 'scripts',
    paths: {
        requirejs: '../../bower_components/requirejs/require',
        jquery: '../../bower_components/jquery/dist/jquery',
        'jquery-ui': 'jquery-ui',
        json3: '../../bower_components/json3/lib/json3',
        angular: '../../bower_components/angular/angular',
        'angular-route': '../../bower_components/angular-route/angular-route',
        'angular-translate': '../../bower_components/angular-translate/angular-translate',
        'angular-cookies': '../../bower_components/angular-cookies/angular-cookies',
        'es5-shim': '../../bower_components/es5-shim/es5-shim',
        moment: '../../bower_components/moment/moment',
        restangular: '../../bower_components/restangular/dist/restangular',
        lodash: '../../bower_components/lodash/dist/lodash',
        hammerjs: '../../bower_components/hammerjs/hammer',
        velocity: '../../bower_components/velocity/velocity',
        'velocity.ui': '../../bower_components/velocity/velocity.ui',
        sweetalert: '../../bower_components/sweetalert/dist/sweetalert.min',
        'sweetalert.angular': '../../bower_components/ngSweetAlert/SweetAlert',
        'slick-carousel': '../../bower_components/slick-carousel/slick/slick',
        lightbox2: '../../bower_components/lightbox2/dist/js/lightbox',
        onComplete: 'directives/onComplete',
        AuthService: 'services/AuthService',
        feedService: 'services/FeedService',
        loadingCircle: 'directives/loadingCircle',
        loadingCircleSmall: 'directives/loadingCircleSmall',
        'angular-local-storage': '../../bower_components/angular-local-storage/dist/angular-local-storage',
        'ng-file-upload': '../../bower_components/ng-file-upload/ng-file-upload',
        ngSweetAlert: '../../bower_components/ngSweetAlert/SweetAlert',
        LikesService: 'services/LikesService',
        PaginationService: 'services/PaginationService',
        'materialize.global': '../../bower_components/materialize/js/global',
        'materialize.animation': '../../bower_components/materialize/js/animation',
        'materialize.toasts': '../../bower_components/materialize/js/toasts',
        'materialize.collapsible': '../../bower_components/materialize/js/collapsible',
        'materialize.dropdown': '../../bower_components/materialize/js/dropdown',
        'materialize.leanModal': '../../bower_components/materialize/js/modal',
        'materialize.materialbox': '../../bower_components/materialize/js/materialbox',
        'materialize.parallax': '../../bower_components/materialize/js/parallax',
        'materialize.tabs': '../../bower_components/materialize/js/tabs',
        'materialize.tooltip': '../../bower_components/materialize/js/tooltip',
        'materialize.sideNav': '../../bower_components/materialize/js/sideNav',
        'materialize.scrollspy': '../../bower_components/materialize/js/scrollspy',
        'materialize.forms': '../../bower_components/materialize/js/forms',
        'materialize.slider': '../../bower_components/materialize/js/slider',
        'materialize.cards': '../../bower_components/materialize/js/cards',
        'materialize.pushpin': '../../bower_components/materialize/js/pushpin',
        'materialize.transitions': '../../bower_components/materialize/js/transitions',
        'materialize.scrollFire': '../../bower_components/materialize/js/scrollFire',
        'materialize.waves': '../../bower_components/materialize/js/waves',
        'materialize.character_counter': '../../bower_components/materialize/js/character_counter',
        'materialize.picker': '../../bower_components/materialize/js/date_picker/picker',
        'materialize.picker.date': '../../bower_components/materialize/js/date_picker/picker.date',
        'materialize.chips': '../../bower_components/materialize/js/chips'
    },
    shim: {
        jquery: {
            exports: 'jQuery'
        },
        'jquery-ui': {
            deps: [
                'jquery'
            ]
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
        loadingCircle: {
            deps: [
                'angular'
            ]
        },
        'loadingCircle-small': {
            deps: [
                'angular'
            ]
        },
        AuthService: {
            deps: [
                'angular'
            ]
        },
        feedService: {
            deps: [
                'angular'
            ]
        },
        'angular-local-storage': {
            deps: [
                'angular'
            ]
        },
        'ng-file-upload': {
            deps: [
                'angular'
            ]
        },
        'materialize.global': {
            deps: [
                'jquery',
                'jquery-ui',
                'hammerjs',
                'velocity'
            ]
        },
        'materialize.animation': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.toasts': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.collapsible': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.dropdown': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.leanModal': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.materialbox': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.parallax': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.tabs': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.tooltip': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.sideNav': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.scrollspy': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.forms': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.slider': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.cards': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.pushpin': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.transitions': {
            deps: [
                'materialize.global',
                'materialize.scrollFire'
            ]
        },
        'materialize.scrollFire': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.waves': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.character_counter': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.picker': {
            deps: [
                'materialize.global'
            ]
        },
        'materialize.picker.date': {
            deps: [
                'materialize.picker'
            ]
        },
        'materialize.chips': {
            deps: [
                'materialize.global'
            ]
        },
        hammerjs: {
            deps: [
                'jquery'
            ]
        },
        velocity: {
            deps: [
                'jquery'
            ]
        },
        LikesService: {
            deps: [
                'angular'
            ]
        },
        PaginationService: {
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
        'restangular',
        'sweetalert',
        'controllers/MainCtrl',
        'jquery',
        'materialize.global',
        'materialize.animation',
        'materialize.toasts',
        'materialize.collapsible',
        'materialize.dropdown',
        'materialize.leanModal',
        'materialize.materialbox',
        'materialize.parallax',
        'materialize.tabs',
        'materialize.tooltip',
        'materialize.sideNav',
        'materialize.scrollspy',
        'materialize.forms',
        'materialize.slider',
        'materialize.cards',
        'materialize.pushpin',
        'materialize.transitions',
        'materialize.scrollFire',
        'materialize.waves',
        'materialize.character_counter',
        'materialize.picker',
        'materialize.picker.date',
        'materialize.chips'
    ],
    function() {
        angular.bootstrap(document, ['powerUp']);
    }
);
