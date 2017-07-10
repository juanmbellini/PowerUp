'use strict';
define(['powerUp', 'slick-carousel', 'onComplete', 'loadingCircle', 'authService'], function(powerUp) {

    powerUp.controller('GameCtrl', ['$scope', '$location', '$log', 'Restangular', 'AuthService', function($scope, $location, $log, Restangular, AuthService) {

        Restangular.setFullResponse(false);
        $scope.gameId = $location.search().id;
        $scope.game = null;

        $scope.canWriteReview = false;

        $scope.rangeScore = [];
        for (var i = 1; i <= 10; i++) {
            $scope.rangeScore.push(i);
        }

        $scope.playStatusOptions = ['verdurita1', 'verdurita2']; // TODO get from api real playStatusOptions

        // TODO Pedir shelves del uusario al endpoint de shelves. Pedir las shelves en las cuales tiene este juego y hacer el metodo que se fija si esta en la shelf.

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

        $scope.findGame($scope.gameId);

        var userId = 1; // TODO change para que auth te de el user. Agregar la validacion de que este conectado

        // Shelves
        $scope.shelves = [];
        $scope.shelvesWithGame = [];
        Restangular.one('users',userId).all('shelves').getList().then(function (shelves) {
            $scope.shelvesWithGame = shelves;
        });
        Restangular.one('users',userId).all('shelves').getList({gameId: $scope.gameId}).then(function (shelves) {
            $scope.shelves = shelves;
        });
        /**
         * Returns if the game is in this shelf
         * @param shelf
         */
        $scope.isInShelf = function (shelf) {

        };
        /**
         * Changes value of game in this shelf (take game out of shelf or puts it
         * @param shelf
         */
        $scope.updateInShelf = function (shelf) {

        };


        // Related Games
        $scope.relatedGames = [];
        Restangular.one('games', $scope.gameId).all('related-games').getList({}).then(function(relatedGames) {
            $scope.relatedGames = relatedGames;
            $scope.relatedMin = Math.min($scope.relatedGames.length, 5);
        }, function (response) {
            console.log('Error with status code', response.status); // TODO handle error
        });
        $scope.$on('relatedRendered', function(event) {
            angular.element('#related-carousel').slick({
                infinite: false,
                arrows: true
            });
            require(['lightbox2']); // TODO ensure requirejs doesn't load this twice
        });

        // userURL.all('shelves').all('recommendedGames').getList({shelvesFilter = {'shelf1','shelf2'}})

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
            Restangular.all('reviews').getList({gameId: $scope.game.id, pageSize: 10}).then(function (reviews) {
                $scope.reviews = reviews;
                console.log('found review ', $scope.reviews);
                $scope.checkCanWriteReview();
            }, function() {
                console.log('There was an error getting reviews');
            });
        });

        $scope.checkCanWriteReview = function() {
            if (!AuthService.isLoggedIn()) {
                $scope.canWriteReview = false;
            } else {
                var currentUserUsername = AuthService.getCurrentUser().username;
                Restangular.all('reviews').getList({username: currentUserUsername, gameId: $scope.gameId}).then(function (reviews) {
                    if (reviews.length > 0) {
                        $scope.canWriteReview = false;
                    } else {
                        $scope.canWriteReview = true;
                    }
                }, function(response) {
                    console.log('There was an error getting reviews, ', response);
                    $scope.canWriteReview = false;
                    return;
                });
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
        };

        $scope.canDeleteReview = function(review) {
          return AuthService.isLoggedIn() && AuthService.getCurrentUser().username === review.username;
        };

        $scope.deleteReview = function(review) {
            review.remove().then(function(data) {
                $log.info('Success: ', data);
                $scope.reviews = $scope.reviews.filter(function(reviewToFilter) {
                    return reviewToFilter.id !== review.id;
                });
            },
            function(error) {
                $log.error('Error: ', error);
            },function () {
                    $scope.checkCanWriteReview();
            });
        };



    }]);
});
