define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.directive('userReviews', function() {
        return {
            restrict: 'E',
            scope: {
              reviews: '=',
              user: '=',
              loggeduserid: '=',
            },
            templateUrl: 'views/directives/user-reviews.html'
        }
    });

});
