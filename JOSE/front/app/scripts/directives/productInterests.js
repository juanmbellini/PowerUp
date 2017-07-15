define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.directive('productInterests', function() {
        return {
            restrict: 'E',
            scope: {
              productinterests: '=',
              user: '=',
              loggedid: '=',
              uninterest: '&'
            },
            templateUrl: 'views/directives/product-interests.html'
        }

    });

});
