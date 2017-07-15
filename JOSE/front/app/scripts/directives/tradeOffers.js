define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.directive('tradeOffers', function() {
        return {
            restrict: 'E',
            scope: {
              trades: '=',
              cancelTrade: '&',
              cancelAcceptedTrade: '&',
              completeTrade: '&'
            },
            templateUrl: 'views/directives/trade-offers.html'
        }

    });

});
