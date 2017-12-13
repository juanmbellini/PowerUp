'use strict';

define([], function() {
    return {
        defaultRoutePath: '/',
        routes: {
            '/': {
                templateUrl: 'views/home.html',
                controller: 'HomeCtrl'
            },
            '/login': {
                templateUrl: 'views/login/login.html',
                controller: 'LoginCtrl'
            },
            '/register': {
                templateUrl: 'views/login/register.html',
                controller: 'RegisterCtrl'
            },
            '/reset-password': {
              templateUrl: 'views/login/reset-password.html',
              controller: 'ResetPasswordCtrl'
            },
            '/profile': {
                templateUrl: 'views/profile/profile.html',
                controller: 'ProfileCtrl'
            },
            '/threads': {
                templateUrl: 'views/threads/threads.html',
                controller: 'ThreadsCtrl'
            },
            '/thread/:threadId': {
                templateUrl: 'views/threads/thread.html',
                controller: 'ThreadCtrl'
            },
            '/threads/create': {
                templateUrl: 'views/threads/create.html',
                controller: 'CreateThreadCtrl'
            },
            '/list': {
                templateUrl: 'views/lists/lists.html',
                controller: 'ListsCtrl'
            },
            '/search': {
                templateUrl: 'views/search/search.html',
                controller: 'SearchCtrl'
            },
            '/game': {
                templateUrl: 'views/games/game.html',
                controller: 'GameCtrl'
            },
            '/write-review': {
                templateUrl: 'views/reviews/write-review.html',
                controller: 'WriteReviewCtrl'

            },
            '/reviews': {
                templateUrl: 'views/reviews/reviews.html',
                controller: 'ReviewsCtrl'
            }
            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});
