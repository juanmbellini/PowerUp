define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.directive('settings', function() {
        return {
            restrict: 'E',
            scope: {
              acceptedmail: '=',
              canceledmail: '=',
              completedmail: '=',
              newtrademail: '=',
              rejectedmail: '=',
              editSettings: '&',
              applySettingChanges: '&',
              cancelEdit: '&'
            },
            templateUrl: 'views/directives/settings.html'
        }

    });

});
