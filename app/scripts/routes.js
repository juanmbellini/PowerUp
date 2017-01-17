'use strict';

define([], function() {
    return {
        defaultRoutePath: '/',
        routes: {
            '/': {
                templateUrl: '/views/home.html',
                controller: 'HomeCtrl'
            },
            '/login': {
                templateUrl: '/views/login/login.html',
                controller: 'LoginCtrl'
            },
            '/profile': {
                templateUrl: '/views/profile/profile.html',
                controller: 'ProfileCtrl'
            },
            '/threads': {
                templateUrl: '/views/threads/threads.html',
                controller: 'ThreadsCtrl'
            },
            '/lists': {
                templateUrl: '/views/lists/lists.html',
                controller: 'ListsCtrl'
            },
            '/search': {
                templateUrl: '/views/search/search.html',
                controller: 'SearchCtrl'
            }
            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});
