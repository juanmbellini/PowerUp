define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.directive('tradeRequests', function() {
        return {
            restrict: 'E',
            scope: {
              trades: '=',
              acceptTrade: '&',
              rejectTrade: '&',
              cancelAcceptedTrade: '&',
              completeTrade: '&'
            },
            templateUrl: 'views/directives/trade-requests.html'
        }

    });

});
