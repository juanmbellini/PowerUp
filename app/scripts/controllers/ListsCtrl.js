'use strict';
define(['powerUp', 'slick-carousel', 'onComplete'], function(powerUp) {

    powerUp.controller('ListsCtrl', function($scope, $location, Restangular, SweetAlert, $log, AuthService, $timeout) {

        Restangular.setFullResponse(false);

        // User
        $scope.checkCurrentUserLogged = function() { // TODO change name to isCurrentUserLoggedAndOwner
            $scope.isCurrentUserLogged = AuthService.isLoggedIn();
        };
        $scope.userId = $location.search().id;
        $scope.username = $location.search().username;
        $scope.currentUser = AuthService.getCurrentUser();
        var userURL = null;
        if ($scope.userId) {
            userURL = Restangular.one('users',$scope.userId);
            userURL.get().then(function(user) {
                $scope.user = user;
                $scope.checkCurrentUserLogged();
                $scope.checkUserLoggedOwner();
                $log.debug('user ', user);
            }, function(response) {
                $log.error('Could not get user or no user with that id. Error with status code', response.status); // TODO handle error
                $location.search({});
                $location.path('');
            });
        // } else if ($scope.username) {
            // userURL =  Restangular.all('users').one(username,$scope.username);
            // TODO username. Paja porque todo lo otro depende de la userURL que no podria armar. tendria que hacer que todo espere
        } else if ($scope.currentUser !== null && $scope.currentUser.id) {
            $location.search({id: $scope.currentUser.id});
            $location.path('list');
        } else {
            $log.debug('Need id in url or be logged');
            $location.search({});
            $location.path('');
        }
        $scope.isCurrentUserLogged = false;
        $scope.isUserLoggedOwner = false;
        $scope.checkUserLoggedOwner = function () {
            $scope.isUserLoggedOwner = $scope.user && $scope.currentUser && $scope.user.username === $scope.currentUser.username; // AuthService.isCurrentUser($scope.user);
        };

        // Games
        $scope.games = [];
        function updateGameList() {
            // {shelves: $scope.selectedShelves, statuses: $scope.selectedPlayStatuses}
            userURL.all('game-list').getList().then(function(games) {
                angular.forEach(games, function (gameRef, index, gameArray) {
                    $scope.games = games;
                    Restangular.one('games', gameRef.gameId).get().then(function (game) {
                        gameArray[index] = game;
                        addInformationToGame(gameRef, index, gameArray);
                    });
                });
            }, function(response) {
                $log.error('Error with status code', response.status);
            });
        }
        function addInformationToGame(gameRef, index, gameArray) {
            var game = gameArray[index];
            Restangular.one('users', $scope.userId).all('game-scores').getList({gameId: game.id}).then(function (gameScore) {
                if (gameScore.length > 0) {
                    game.userScore = gameScore[0].score;
                }
            }, function (response) {
                $log.error('Could not get score from game', response);
            });
            Restangular.one('users',$scope.userId).all('shelves').getList({gameId: game.id}).then(function (shelvesWithGame) {
                game.shelves = shelvesWithGame;
            });
            Restangular.one('users', $scope.userId).one('play-status', game.id).get().then(function (playStatus) {
                if (playStatus.length > 0) {
                    game.status = playStatus[0].status;
                }
            }, function (response) {
                $log.error('Could not get play status from game', response);
            });
        }
        $scope.deleteGame = function(game) { // TODO re pensar si va a existir exto en el nuevo contexto
            SweetAlert.swal({
                    title: 'Are you sure?',
                    text: 'You are about to delete ' + game.name + ' from all your shelves.',
                    type: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#DD6B55',
                    confirmButtonText: 'Delete',
                    closeOnConfirm: false
                },
                function () {
                    $log.debug('Now your game should be deleted');
                });
        };

        // PlayStatuses
        $scope.playStatuses = [];
        Restangular.all('users').all('play-statuses').getList({}).then(function (playStatuses) {
            $scope.playStatuses = playStatuses;
        }, function (response) {
            $log.error('Could not get playStatuses', response);
        });
        $scope.selectedPlayStatuses = [];
        $scope.toggleSelectionPlayStatus = function toggleSelection(playStatus) {
            var idx = $scope.selectedPlayStatuses.indexOf(playStatus);
            if (idx > -1) {
                // Is currently selected
                $scope.selectedPlayStatuses.splice(idx, 1);
            } else {
                // Is newly selected
                $scope.selectedPlayStatuses.push(playStatus);
            }
            $log.info($scope.selectedPlayStatuses);
        };

        // Shelves
        $scope.selectedShelves = [];
        $scope.toggleSelectionShelves = function toggleSelection(shelf) {
            var idx = $scope.selectedShelves.indexOf(shelf);
            if (idx > -1) {
                // Is currently selected
                $scope.selectedShelves.splice(idx, 1);
            } else {
                // Is newly selected
                $scope.selectedShelves.push(shelf);
            }
            $log.info($scope.selectedShelves);
        };
        $scope.getShelves = function() {
            userURL.all('shelves').getList({}).then(function(shelves) {
                $scope.shelves = shelves;
            }, function(error) {
                $log.error('Could not get user shelves. Error with status code', error.status);
            });
        };
        $scope.shelves = null;
        $scope.getShelves();
        $scope.$watchCollection('selectedShelves', function () {
            updateGameList();
            Restangular.one('users',$scope.userId).all('recommended-games').getList({shelves: $scope.selectedShelves}).then(function (recommendedGames) {
                $scope.recommendedGames = recommendedGames;
                $scope.recommendedMin = Math.min($scope.recommendedGames.length, 5);
            }, function (response) {
                $log.error('Could not get recommended games', response);
                $scope.recommendedGames = [];
            });
        });
        $scope.deleteShelf = function(shelf) {
            SweetAlert.swal({
                    title: 'Are you sure?',
                    text: 'You are about to permanently delete shelf \"' + shelf.name + '\"',
                    type: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#DD6B55',
                    confirmButtonText: 'Delete',
                    closeOnConfirm: true
                },
                function () {
                    // //Disable submit button to prevent multiple submissions
                    // $('.confirm').attr('disabled', 'disabled');
                    userURL.one('shelves',shelf.name).remove().then(function(response) {
                        $log.debug('Now your shelf should be deleted');
                    },function(error) {
                        $log.error('Couldn\'t delete shelf ', error);
                    });
              });
        };
        $scope.editShelf = function(shelf) { // TODO minimo de longitud de shelf == 1
            SweetAlert.swal({
                    title: 'Rename \"' + shelf.name + '\" to...',
                    type: 'input',
                    showCancelButton: true,
                    closeOnConfirm: true,
                    confirmButtonText: 'Rename',
                    inputPlaceholder: 'New name'
                },
                function (inputValue) {
                    if (inputValue === false) {
                        return false;
                    }
                    // TODO delete if no errors later.
                    // inputValue = inputValue.replace(/,/g, ';');
                    if (inputValue === '' || inputValue.length > 25) {
                        swal.showInputError('Please write between 1 and 25 characters');
                        return false;
                    }
                    // Disable submit button to prevent multiple submissions
                    // $('.confirm').attr('disabled', 'disabled');
                    // Create an inline form and submit it to redirect with POST
                    // TODO viejo pero tal vez hay que seguir usandolo. Creo que era boludeces de backend que no van a servir más.
                    // escapeHtml(inputValue)
                    // var entityMap = {
                    //     '&': '&amp;',
                    //     '<': '&lt;',
                    //     '>': '&gt;',
                    //     '"': '&quot;',
                    //     "'": '&#39;',
                    //     '/': '&#x2F;'
                    // };
                    // function escapeHtml(string) {
                    //     return String(string).replace(/[&<>"'\/]/g, function (s) {
                    //         return entityMap[s];
                    //     });
                    // }
                    var oldName = shelf.name;
                    shelf.name = inputValue;
                    userURL.one('shelves',oldName).customPUT(shelf).then(function (response) {
                        $log.debug('Updated Shelf. Now your shelf should be called ' + inputValue);
                    }, function(response) {
                        $log.error('Error creating shelf. Error with status code', response.status);
                        shelf.name = oldName;
                        // TODO handle error. show error or something
                    });
                });
        };
        $scope.newShelfName = null;
        $scope.createShelf = function() {
            console.log($scope.newShelfName);
            // TODO validate input?
            var shelf = {name: $scope.newShelfName};
            userURL.all('shelves').post(shelf).then(function(response) {
                $log.debug('Created Shelf');
                $scope.getShelves();
            }, function(response) {
                $log.error('Error creating shelf. Error with status code', response.status); // TODO handle error
            });
        };

        // RecommendedGames
        $scope.hasSlick = false;
        $scope.$on('recommendedRendered', function(event) {
            if ($scope.hasSlick) {
                $scope.hasSlick = false;
                angular.element('#recommended-carousel').slick('unslick');
            }
             angular.element('#recommended-carousel').slick({
                infinite: false,
                arrows: true
            });
            $scope.hasSlick = true;
            require(['lightbox2']); // TODO ensure requirejs doesn't load this twice
        });

        $scope.$watchCollection('selectedPlayStatuses', function() {
            updateGameList();
        });
    });
});
