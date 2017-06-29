'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('ReviewCtrl', function($scope, Restangular, $location) {

        $scope.canWriteReview = function() {
            return true;
        };
        $scope.pageSizes = [5,10,15];
        $scope.pageSize = 5;

        $scope.userId = $location.search().userId;
        $scope.gameId = $location.search().gameId;
        if (!$scope.userId && !$scope.gameId) {
            console.log('No gameId nor userId');
            $location.search({});
            $location.path('');
        }


        // TODO delete duplicated
        if ($scope.gameId) {
            Restangular.one('games', $scope.gameId).get().then(function(game) {
                $scope.game = game;
                console.log('Game: ', game);
                if ($scope.game !== null) {
                    // $scope.$broadcast('gameFound');                                     // Game found, fire all secondary searches (e.g. reviews)
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
        }
       if ($scope.userId) {
           Restangular.one('users', $scope.userId).get().then(function(user) {
               $scope.user = user;
               console.log('User: ', user);
               if ($scope.user !== null) {
                   // $scope.$broadcast('gameFound');                                     // Game found, fire all secondary searches (e.g. reviews)
               } else {
                   // TODO show 'user not found'
                   $location.search({});
                   $location.path('');
               }
           }, function(response) {
               console.log('Error with status code', response.status); // TODO handle error
               $location.search({});
               $location.path('');
           });
       }
       $scope.overallReviewScore = function(review) {
            var fields = ['storyScore', 'graphicsScore', 'audioScore', 'controlsScore', 'funScore'];
            var result = 0;

            fields.forEach(function(field) {
                result += review[field] / fields.length;
            });
            return result;
        };
        $scope.canDeleteReview = function(review) {
            return AuthService.isLoggedIn() && AuthService.getCurrentUser().username === review.username;
        };


        Restangular.all('reviews').getList({gameId: $scope.gameId, userId: $scope.userId}).then(function (reviews) {
            $scope.reviews = reviews;
        }, function() {
            console.log('There was an error getting reviews');
        });

        // $scope.reviews = {data: [{game: {id: 7522, name: 'daGame', coverPictureUrl: 'https://res.cloudinary.com/igdb/image/upload/t_cover_big_2x/rwhafb8nebwrc84edjd1.jpg'}, review: 'ALTO JUEGASO', storyScore: 2, graphicsScore: 5, audioScore: 3, controlsScore: 4, funScore: 8, overallScore: 7, date: '2015-02-01', user: {id: 1, username: 'jorge'}}], totalPages: 1, pageNumber: 1, pageSize: 1, overAllAmountOfElements: 1};
    });

});
