'use strict';
define(['powerUp', 'slick-carousel', 'onComplete', 'loadingCircle', 'authService'], function(powerUp) {

    powerUp.controller('GameCtrl', ['$scope', '$location', 'Restangular', 'AuthService', function($scope, $location, Restangular, AuthService) {

        Restangular.setFullResponse(false);
        $scope.gameId = $location.search().id;
        $scope.game = null;

        $scope.findGame = function(gameId) {
            Restangular.one('games', gameId).get().then(function(game) {
                $scope.game = game;
                $scope.gameId = game.id;

                console.log('Game: ', game);
                if ($scope.gameId > 0 && $scope.game !== null) {
                    $scope.videosMin = Math.min($scope.game.videoUrls.length, 4);       // How many videos to show per carousel page
                    $scope.picturesMin = Math.min($scope.game.pictureUrls.length, 4);   // How many pictures to show per carousel page
                    $scope.$broadcast('gameFound');                                     // Game found, fire all secondary searches (e.g. reviews)
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

        };

        /* *******************************
         * Event receivers for ng-repeats, see http://stackoverflow.com/questions/15207788/calling-a-function-when-ng-repeat-has-finished
         * ******************************/
        // (Re-)initialize Slick for game pictures
        $scope.$on('picturesRendered', function(event) {
            angular.element('#screenshots-carousel').slick({
                infinite: false,
                arrows: true,
                lazyload: 'ondemand'
            });
            require(['lightbox2']);
        });
        // (Re-)initialize Slick for game videos
        $scope.$on('videosRendered', function(event) {
            angular.element('#videos-carousel').slick({
                infinite: false,
                arrows: true
            });
            require(['lightbox2']); // TODO ensure requirejs doesn't load this twice
        });


        /* *****************************************
         *                 REVIEWS
         * ****************************************/
        $scope.reviews = null;

        $scope.$on('gameFound', function() {
            Restangular.all('reviews').getList({gameId: $scope.game.id}).then(function (reviews) {
                $scope.reviews = reviews;
            }, function() {
                console.log('There was an error getting reviews');
            });
        });

        $scope.canWriteReview = function() {
            if ($scope.reviews === null || !AuthService.isLoggedIn()) {
                return false;
            } else {
                // Can submit review if user hasn't made one already. For now we just check that the retrieved reviews don't have the current user as author.
                // TODO consider hitting API for this since reviews may be paginated
                var currentUserId = AuthService.getCurrentUser().id;
                $scope.reviews.forEach(function(review) {
                    if (currentUserId === review.user.id) {
                        return false;
                    }
                });
                return true;
            }
        };

        $scope.overallReviewScore = function(review) {
            var fields = ['storyScore', 'graphicsScore', 'audioScore', 'controlsScore', 'funScore'];
            var result = 0;

            fields.forEach(function(field) {
                result += review[field] / fields.length;
            });
            return result;
        };

        $scope.getReviewUserProfilePictureUrl = function(review) {
            return Restangular.one('users', review.userId).one('picture').getRequestedUrl();
        }
    }]);
});
