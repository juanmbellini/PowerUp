define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.directive('userOffers', function() {
        return {
            restrict: 'E',
            scope: {
              offers: '=',
              userid: '=',
              loggedid: '=',
              username: '=',
              deleteOffer: '&'
            },
            templateUrl: 'views/directives/user-offers.html'
        }
    });

});
