define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.directive('personalInfo', function() {
        return {
            restrict: 'E',
            scope: {
              user: '=' ,
              changepassform: '=',
              changePassword: '&',
              selectIconModal: '&',
              changeIcon: '&',
              loggedUserId: '=',
              newIconForm: '=',
			        notOldPass: '=',
              alreadyLiked: '=',
              like:'&',
              unlike: '&',
              id: '=',
              passwordPattern: '='
            },
            templateUrl: 'views/directives/personal-info.html'
        }
    });



});
