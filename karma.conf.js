// Karma configuration
// Generated on Fri Jul 14 2017 18:13:58 GMT-0300 (ART)

module.exports = function(config) {
  config.set({

    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: '',


    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['jasmine', 'requirejs'],

    plugins: [
        require("karma-jasmine"),
        require("karma-requirejs"),
        require("karma-ng-html2js-preprocessor"),
        require("karma-jasmine-html-reporter"),
        require("karma-chrome-launcher"),
        require("karma-phantomjs-launcher")
    ],

    // list of files / patterns to load in the browser
    files: [
      // Angular
      {pattern: 'bower_components/angular/angular.js', included: false},
      {pattern: 'bower_components/angular-local-storage/dist/angular-local-storage.js', included: false},
      {pattern: 'bower_components/angular-mocks/angular-mocks.js', included: false},
      {pattern: 'bower_components/angular-route/angular-route.js', included: false},
      {pattern: 'bower_components/angular-translate/angular-translate.js', included: false},
      // TODO remove angular cookies, we shouldn't be using cookies
      {pattern: 'bower_components/angular-cookies/angular-cookies.js', included: false},
      {pattern: 'bower_components/angular-route/angular-route.js', included: false},
      {pattern: 'bower_components/ng-file-upload/ng-file-upload.js', included: false},
      {pattern: 'bower_components/ngSweetAlert/SweetAlert.js', included: false},

      // Etc.
      {pattern: 'bower_components/es5-shim/es5-shim.js', included: false},
      {pattern: 'bower_components/requirejs/require.js', included: false},
      {pattern: 'bower_components/restangular/dist/restangular.js', included: false},
      {pattern: 'bower_components/lodash/dist/lodash.js', included: false},

      // UI
      {pattern: 'bower_components/jquery/dist/jquery.js', included: false},
      // TODO use custom jQuery-UI dist in deploy branch
      {pattern: 'bower_components/jquery-ui/jquery-ui.js', included: false},
      {pattern: 'bower_components/lightbox2/dist/js/lightbox.js', included: false},
      {pattern: 'bower_components/materialize/js/*.js', included: false},
      // See if Hammer is needed, included in Materialize
      // {pattern: 'bower_components/hammerjs/hammer.js', included: false},
      // {pattern: 'bower_components/velocity/velocity.js', included: false},
      {pattern: 'bower_components/moment/moment.js', included: false},
      {pattern: 'bower_components/slick-carousel/slick/slick.js', included: false},
      {pattern: 'bower_components/sweetalert/dist/sweetalert.min.js', included: false},

      // Controllers, views, tests, etc.
      {pattern: 'app/scripts/**/*.js', included: false},
      {pattern: 'app/views/**/*.html', included: false},
      {pattern: 'test-main.js', included: true},
      {pattern: 'app/test/**/*.js', included: false}
    ],


    // list of files to exclude
    exclude: [
    ],


    // preprocess matching files before serving them to the browser
    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
    preprocessors: {
      // TODO uncomment and see what it does
      // 'app/scripts/controllers/**/*.js': ['coverage'],
      // 'app/scripts/directives/*.js': ['coverage'],
      // 'app/scripts/services/!(dependencyResolverFor).js': ['coverage'],
      // 'app/views/**/*.html': ['ng-html2js']
    },

    // TODO maybe coverage reporter?


    // test results reporter to use
    // possible values: 'dots', 'progress'
    // available reporters: https://npmjs.org/browse/keyword/karma-reporter
    reporters: ['progress', 'kjhtml'/*, 'coverage'*/],


    // web server port
    port: 9876,


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    logLevel: config.LOG_DEBUG,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // start these browsers
    // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
    browsers: [/*'Chrome', */'PhantomJS'],


    // Continuous Integration mode
    // if true, Karma captures browsers, runs the tests and exits
    singleRun: false,

    // Concurrency level
    // how many browser should be started simultaneous
    concurrency: Infinity
  })
}
