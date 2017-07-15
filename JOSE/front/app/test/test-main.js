'use strict';

var allTestFiles = [];
var TEST_REGEXP = /(spec|test)\.js$/i;

// Get a list of all the test files to include
Object.keys(window.__karma__.files).forEach(function (file) {
  if (TEST_REGEXP.test(file)) {
    allTestFiles.push(file);
  }
});

require.config({
  // Karma serves files under /base, which is the basePath from your config file
  baseUrl: '/base/scripts',

  paths: {
      'routes': 'routes',
	  'test-utils': '../test/testUtils',
      'services/dependencyResolverFor': 'services/dependencyResolverFor',
      'i18n/i18nLoader': 'i18n/i18nLoader',
      'i18n/translations.es': 'i18n/translations.es',
      'i18n/translations.en': 'i18n/translations.en',
      affix: '../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/affix',
      alert: '../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/alert',
      angular: '../bower_components/angular/angular',
      'angular-route': '../bower_components/angular-route/angular-route',
      'angular-translate': '../bower_components/angular-translate/angular-translate',
      button: '../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/button',
      bootstrap: '../bower_components/bootstrap/dist/js/bootstrap',
      carousel: '../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/carousel',
      collapse: '../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/collapse',
      dropdown: '../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/dropdown',
      'es5-shim': '../bower_components/es5-shim/es5-shim',
      jquery: '../bower_components/jquery/dist/jquery',
      'trademygame': 'trademygame',
      json3: '../bower_components/json3/lib/json3',
      moment: '../bower_components/moment/moment',
      popover: '../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/popover',
      requirejs: '../bower_components/requirejs/require',
      scrollspy: '../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/scrollspy',
      tab: '../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/tab',
      tooltip: '../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/tooltip',
      transition: '../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/transition',
      ratings: './lib/ratings',
      'angular-bootstrap': './lib/ui-bootstrap-tpls-0.12.1.min',
      filters: './lib/filters',
      restangular: '../bower_components/restangular/dist/restangular',
      lodash: '../bower_components/lodash/dist/lodash',
      countrySelect: '../bower_components/ng-country-select/dist/ng-country-select',
      personalInfo: 'directives/personalInfo',
      userOffers: 'directives/userOffers',
      userPage: './lib/userpage',
      tradeOffers: 'directives/tradeOffers',
      tradeRequests: 'directives/tradeRequests',
      userReviews: 'directives/userReviews',
      userHistory: 'directives/userHistory',
      userLikes: 'directives/likedUsers',
      settings: 'directives/settings',
      productInterests: 'directives/productInterests',
      modal: '../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/modal',
      'ng-country-select': '../bower_components/ng-country-select/dist/ng-country-select',
      flow: '../bower_components/ng-flow/dist/ng-flow-standalone.min',
      'ng-flow': '../bower_components/ng-flow/dist/ng-flow',
      equalFields: 'directives/equalFields',
      toastr: '../bower_components/angular-toastr/dist/angular-toastr',
      'angular-toastr': '../bower_components/angular-toastr/dist/angular-toastr',
	  'angular-mocks': '../bower_components/angular-mocks/angular-mocks'
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
      bootstrap: {
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
      'angular-bootstrap': {
          deps: [
              'angular'
          ]
      },
      restangular: {
          exports: 'Restangular',
          deps: [
              'angular',
              'lodash'
          ]
      },
      countrySelect: {
          exports: 'countrySelect',
          deps: [
              'angular'
          ]
      },
      flow: {
          exports: 'flow',
          deps: [
              'angular'
          ]
      },
      toastr: {
          exports: 'toastr',
          deps: [
              'angular'
          ]
      },
	  'angular-mocks': {
		  exports: 'angular-mocks',
		  deps: [
			'angular'
		  ]
	  }
  },
  packages: [

  ],


  // dynamically load all test files
  deps: allTestFiles,

  // we have to kickoff jasmine, as it is asynchronous
  //callback: window.__karma__.start
  callback: function() {
    console.log('load complete');
    window.__karma__.start();
  }

});
