'use strict';
define(['powerUp', 'slick-carousel', 'onComplete'], function(powerUp) {

    powerUp.controller('GameCtrl', ['$scope', '$location', 'Restangular', function($scope, $location, Restangular) {

        Restangular.setFullResponse(false);
        $scope.gameId = $location.search().id;

        Restangular.one('games',$scope.gameId).get().then(function(game) {
            $scope.game = game;

            console.log('Game: ', game);
            if ($scope.gameId > 0 && $scope.game !== null) {
                $scope.videosMin = Math.min($scope.game.videoUrls.length, 4);       // How many videos to show per carousel page
                $scope.picturesMin = Math.min($scope.game.pictureUrls.length, 4);   // How many pictures to show per carousel page
            } else {
                // TODO show 'game not found'
                $location.search({});
                $location.path('');
            }
        }, function(response) {
            console.log('Error with status code', response.status); // TODO handle error
            $location.search({});
            $location.path('');
        });

        // Event receivers for ng-repeats, see http://stackoverflow.com/questions/15207788/calling-a-function-when-ng-repeat-has-finished
        $scope.$on('picturesRendered', function(event) {
            angular.element('#screenshots-carousel').slick({
                infinite: false,
                arrows: true,
                lazyload: 'ondemand'
            });
            require(['lightbox2']);
        });

        $scope.$on('videosRendered', function(event) {
            angular.element('#videos-carousel').slick({
                infinite: false,
                arrows: true
            });
            require(['lightbox2']); // TODO ensure requirejs doesn't load this twice
        });

    }]);
});
