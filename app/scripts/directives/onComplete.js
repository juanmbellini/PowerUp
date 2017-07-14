'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.directive('onComplete', function($timeout) {
        return {
            restrict: 'A',
            link: function (scope, element, attr) {
                if (scope.$last === true) {
                    $timeout(function () {
                        scope.$emit(attr.onComplete);
                    });
                }
            }
        };
    });
});
