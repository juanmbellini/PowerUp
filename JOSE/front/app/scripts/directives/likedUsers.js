define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.directive('likedUsers', function() {
        return {
            restrict: 'E',
            scope: {
              userlikes: '=',
              userid: '=',
              loggeduserid: '=',
              username: '=',
              unfav: '&',
              unlikeOther: '&'
            },
            templateUrl: 'views/directives/liked-users.html'
        }

    });

});
