/* global paths */
'use strict';
require.config({
    baseUrl: '/scripts',
    paths: {
        angular: '../../bower_components/angular/angular',
        'angular-route': '../../bower_components/angular-route/angular-route',
        'angular-translate': '../../bower_components/angular-translate/angular-translate',
        'angular-cookies': '../../bower_components/angular-cookies/angular-cookies',
        'es5-shim': '../../bower_components/es5-shim/es5-shim',
        jquery: '../../bower_components/jquery/dist/jquery',
        json3: '../../bower_components/json3/lib/json3',
        moment: '../../bower_components/moment/moment',
        requirejs: '../../bower_components/requirejs/require',
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
        hammerjs: '../../bower_components/hammerjs/hammer',
        velocity: '../../bower_components/velocity/velocity',
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
        popover: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/popover',
        materialize: '../../bower_components/materialize/bin/materialize',
        'velocity.ui': '../../bower_components/velocity/velocity.ui',
        restangular: '../../bower_components/restangular/dist/restangular',
        lodash: '../../bower_components/lodash/dist/lodash'
    },
    shim: {
        angular: {
            // EXPORTS IS IMPORTANT! Libraries that depend on angular don't seem to see angular without this
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
        bootstrap: {
            deps: [
                'jquery',
                'modal'
            ]
        },
        modal: {
            deps: [
                'jquery'
            ]
        },
        tooltip: {
            deps: [
                'jquery'
            ]
        },
        'angular-translate': {
            deps: [
                'angular'
            ]
        },
        'materialize.global': {
            deps: [
                'jquery',
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

// Define the whole materialize package as a module, to be required later
define('materialize', [
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
  'materialize.picker.date'
], function() {
  // Do something when materialize is loaded
});


require([
        'angular',
        'powerUp',
        'materialize',
        'controllers/MainCtrl'
    ],
    function() {
        angular.bootstrap(document, ['powerUp']);
    }
);
