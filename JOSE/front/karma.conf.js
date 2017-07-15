// Karma con:wq:figuration
// Generated on Sun Jul 02 2017 19:09:18 GMT-0300 (ART)
'use strict';
module.exports = function(config) {

  var fs = require('fs');
  fs.symlink('../bower_components', './app/bower_components', 'dir', function() {});


  config.set({

    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: 'app',

    plugins: [
      require('karma-jasmine'),
      require('karma-ng-html2js-preprocessor'),
      require('karma-coverage'),
      require('karma-requirejs'),
      require('karma-chrome-launcher'),
      require('karma-jasmine-html-reporter'),
      require('karma-phantomjs-launcher')
    ],

    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['jasmine', 'requirejs'],


    // list of files / patterns to load in the browser
    files: [
      { pattern: 'bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/affix.js', included: false},
      { pattern: 'bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/alert.js', included: false},
      { pattern: 'bower_components/angular/angular.js', included: false},
      { pattern: 'bower_components/angular-route/angular-route.js', included: false},
      { pattern: 'bower_components/angular-translate/angular-translate.js', included: false},
      { pattern: 'bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/button.js', included: false},
      { pattern: 'bower_components/bootstrap/dist/js/bootstrap.js', included: false},
      { pattern: 'bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/carousel.js', included: false},
      { pattern: 'bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/collapse.js', included: false},
      { pattern: 'bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/dropdown.js', included: false},
      { pattern: 'bower_components/es5-shim/es5-shim.js', included: false},
      { pattern: 'bower_components/jquery/dist/jquery.js', included: false},
      { pattern: 'bower_components/json3/lib/json3.js', included: false},
      { pattern: 'bower_components/moment/moment.js', included: false},
      { pattern: 'bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/popover.js', included: false},
      { pattern: 'bower_components/requirejs/require.js', included: false},
      { pattern: 'bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/scrollspy.js', included: false},
      { pattern: 'bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/tab.js', included: false},
      { pattern: 'bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/tooltip.js', included: false},
      { pattern: 'bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/transition.js', included: false},
      { pattern: 'bower_components/restangular/dist/restangular.js', included: false},
      { pattern: 'bower_components/lodash/dist/lodash.js', included: false},
      { pattern: 'bower_components/ng-country-select/dist/ng-country-select.js', included: false},
      { pattern: 'bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/modal.js', included: false},
      { pattern: 'bower_components/ng-country-select/dist/ng-country-select.js', included: false},
      { pattern: 'bower_components/ng-flow/dist/ng-flow-standalone.min.js', included: false},
      { pattern: 'bower_components/angular-toastr/dist/angular-toastr.js', included: false},
      { pattern: 'bower_components/ng-flow/dist/ng-flow.js', included: false},
	  { pattern: 'bower_components/angular-mocks/angular-mocks.js', included: false},
      { pattern: 'scripts/**/*.js', included: false },
	  { pattern: 'views/**/*.html', included: false},
      { pattern: 'test/test-main.js', included:true},
	  { pattern: 'test/**/*.js', included: false}
    ],


    // list of files to exclude
    exclude: [
    ],


    // preprocess matching files before serving them to the browser
    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
    preprocessors: {
      'scripts/controllers/**/*.js': ['coverage'],
      'scripts/directives/*.js': ['coverage'],
      'scripts/services/!(dependencyResolverFor).js': ['coverage'],
      'views/**/*.html': ['ng-html2js']
    },

	coverageReporter: {
      includeAllSources: true,
      reporters: [
        {type: 'html', dir: 'coverage/'},
        {type: 'lcov', dir: 'coverage/'}
      ],
      check: {
        global: {
          statements: 35
        }
      }
    },

    // test results reporter to use
    // possible values: 'dots', 'progress'
    // available reporters: https://npmjs.org/browse/keyword/karma-reporter
    reporters: ['progress', 'kjhtml', 'coverage'],





    // web server port
    port: 9876,


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: false,


    // start these browsers
    // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
    browsers: ['PhantomJS'],


    // Continuous Integration mode
    // if true, Karma captures browsers, runs the tests and exits
    singleRun: true,

    // Concurrency level
    // how many browser should be started simultaneous
    concurrency: Infinity
  })
}
