define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.directive('userHistory', function() {
        return {
            restrict: 'E',
            scope: {
              donetrades: '=',
              userReviewForm: '=',
              user: '=',
              reviewUser: '&'
            },
            templateUrl: 'views/directives/user-history.html'
        }
    });

});
