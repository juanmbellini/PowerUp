'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.directive('ratingStars', function() {
        return {
            restrict: 'E',
            templateUrl: 'scripts/directives/ratingStars.html',
            scope: {
                rating: '='
            },
            link: function($scope, $element, attrs, controller, transcludeFn) {
                $scope.wholeStars = function() {
                  return new Array(Math.floor($scope.rating / 2));
                };

              $scope.halfStars = function() {
                return new Array(Math.floor($scope.rating % 2));
              };

              $scope.emptyStars = function() {
                return new Array(5 - $scope.wholeStars().length - $scope.halfStars().length);
              };
            }
        };
    });
});
