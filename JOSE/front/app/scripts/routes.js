'use strict';

define([], function() {
    return {
        defaultRoutePath: '/error404',
        routes: {
            '/': {
                templateUrl: '/views/home.html',
                controller: 'HomeCtrl'
            },
            '/error404': {
                templateUrl: '/views/error404.html',
                controller: 'error404'
            },
            '/about_us': {
                templateUrl: '/views/aboutUs.html',
                controller: 'aboutUs'
            },
            '/product/:id': {
                templateUrl: '/views/product/:id/product.html',
                controller: 'product'
            },
            '/offer/:id': {
                templateUrl: '/views/offer/:id/offer.html',
                controller: 'OfferCtrl'
            },
            '/filter': {
                templateUrl: '/views/filter.html',
                controller: 'filter'
            },
            '/product/:id/reviews': {
                templateUrl: '/views/product/:id/reviews/reviews.html',
                controller: 'reviews',
                reloadOnSearch: false
            },
            '/changePassword': {
                templateUrl: '/views/changePassword.html',
                controller: 'changePassword'
            },
            '/forgotPassword': {
                templateUrl: '/views/forgotPassword.html',
                controller: 'forgotPassword'
            },
            '/product/:id/offers': {
                templateUrl: '/views/product/:id/offers/offers.html',
                controller: 'offers',
                reloadOnSearch: false
            },
            '/newoffer': {
                templateUrl: '/views/newoffer/newoffer.html',
                controller: 'newoffer'
            },
            '/search': {
                templateUrl: '/views/search.html',
                controller: 'search'
            },
            '/accessdenied': {
                templateUrl: '/views/accessdenied.html',
                controller: 'accessdenied'
            },
            '/user/:id': {
                templateUrl: '/views/user/:id/user.html',
                controller: 'user',
                reloadOnSearch: false
            },
            '/register': {
                templateUrl: '/views/register.html',
                controller: 'register'
            },
            '/offer/:id/trade': {
                templateUrl: '/views/offer/:id/trade/trade.html',
                controller: 'trade'
            },
            '/confirmAccount': {
                templateUrl: '/views/home.html',
                controller: 'confirmAccount'
            }
            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});
