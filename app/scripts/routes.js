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
            '/register': {
                templateUrl: '/views/login/register.html',
                controller: 'RegisterCtrl'
            },
            '/profile': {
                templateUrl: '/views/profile/profile.html',
                controller: 'ProfileCtrl'
            },
            '/threads': {
                templateUrl: '/views/threads/threads.html',
                controller: 'ThreadsCtrl'
            },
            '/thread': {
                templateUrl: '/views/threads/thread.html',
                controller: 'ThreadCtrl'
            },
            '/list': {
                templateUrl: '/views/lists/lists.html',
                controller: 'ListsCtrl'
            },
            '/search': {
                templateUrl: '/views/search/search.html',
                controller: 'SearchCtrl'
            },
            '/game': {
                templateUrl: '/views/games/game.html',
                controller: 'GameCtrl'
            }
            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});
