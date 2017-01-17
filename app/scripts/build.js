/* global paths */
'use strict';
require.config({
    baseUrl: '/scripts',
    paths: {
        angular: '../../bower_components/angular/angular',
        'angular-route': '../../bower_components/angular-route/angular-route',
        'angular-translate': '../../bower_components/angular-translate/angular-translate',
        'es5-shim': '../../bower_components/es5-shim/es5-shim',
        jquery: '../../bower_components/jquery/dist/jquery',
        json3: '../../bower_components/json3/lib/json3',
        moment: '../../bower_components/moment/moment',
        requirejs: '../../bower_components/requirejs/require',
        /*
         * The following are all the components for Materialize. Since it doesn't use AMD,
         * we have to manually define all its components and load them with Require.js
         */
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
        velocity: '../../bower_components/velocity/velocity'
    },
    shim: {
        angular: {
            deps: [
                'jquery'
            ]
        },
        'angular-route': {
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
        // Materialize components
        'materialize.global': {
          deps: [
            'jquery',
            'hammerjs',
            'velocity'
          ]
        },
        'materialize.animation': {
          deps: ['materialize.global']
        },
        'materialize.toasts': {
          deps: ['materialize.global']
        },
        'materialize.collapsible': {
          deps: ['materialize.global']
        },
        'materialize.dropdown': {
          deps: ['materialize.global']
        },
        'materialize.leanModal': {
          deps: ['materialize.global']
        },
        'materialize.materialbox': {
          deps: ['materialize.global']
        },
        'materialize.parallax': {
          deps: ['materialize.global']
        },
        'materialize.tabs': {
          deps: ['materialize.global']
        },
        'materialize.tooltip': {
          deps: ['materialize.global']
        },
        'materialize.sideNav': {
          deps: ['materialize.global']
        },
        'materialize.scrollspy': {
          deps: ['materialize.global']
        },
        'materialize.forms': {
          deps: ['materialize.global']
        },
        'materialize.slider': {
          deps: ['materialize.global']
        },
        'materialize.cards': {
          deps: ['materialize.global']
        },
        'materialize.pushpin': {
          deps: ['materialize.global']
        },
        'materialize.transitions': {
          deps: [
            'materialize.global',
            'materialize.scrollFire'
          ]
        },
        'materialize.scrollFire': {
          deps: ['materialize.global']
        },
        'materialize.waves': {
          deps: ['materialize.global']
        },
        'materialize.character_counter': {
          deps: ['materialize.global']
        },
        'materialize.picker': {
          deps: ['materialize.global']
        },
        'materialize.picker.date': {
          deps: ['materialize.picker']
        },
        'hammerjs': {
          deps: ['jquery']
        },
        'velocity': {
          deps: ['jquery']
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
        'controllers/IndexCtrl'
    ],
    function() {
        angular.bootstrap(document, ['powerUp']);
    }
);
