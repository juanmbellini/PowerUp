'use strict';
define(['powerUp', 'slick-carousel', 'onComplete'], function(powerUp) {

    powerUp.controller('ListsCtrl', function($scope, $location, Restangular, SweetAlert, $log, AuthService) {

        // TODO replace this values with api calls. -- Droche 15/02/2017
        $scope.playStatuses = ['planToPlay','playing','played'];

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

        $scope.getShelves = function() {
            userURL.all('shelves').getList({}).then(function(shelves) {
                $scope.shelves = shelves;
            }, function(error) {
                $log.error('Could not get user shelves. Error with status code', error.status);
            });
        };

        $scope.shelves = null;
        $scope.getShelves();

        $scope.games = [];
        // TODO change games to real game list and recommended games too.
        var baseGames = Restangular.all('games');


        baseGames.getList({}).then(function(games) {
            $scope.games = games;
            $scope.$broadcast('gamesLoaded');
        }, function(response) {
            $log.error('Error with status code', response.status);
        });

       baseGames.getList({}).then(function(games) {
            // userURL.all('shelves').all('recommendedGames').getList({shelvesFilter = {'shelf1','shelf2'}})
            $scope.recommendedGames = games;
            $scope.recommendedMin = Math.min($scope.recommendedGames.length, 5);
        }, function(response) {
            $log.error('Error with status code', response.status);
        });

        $scope.getGameShelves = function(game) {
            userURL.all('shelves').getList({gameId: game.id}).then(function(shelves) {
                return shelves;
            }, function (response) {
                $log.error('Couldn\'t get shelves, status code', response.status);
                return [];
            });
        };


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

        $scope.isCurrentUserLogged = false;

        $scope.checkCurrentUserLogged = function() { // TODO change name to isCurrentUserLoggedAndOwner
            $scope.isCurrentUserLogged = AuthService.isLoggedIn();
        };

        $scope.isUserLoggedOwner = false;
        $scope.checkUserLoggedOwner = function () {
            $scope.isUserLoggedOwner = $scope.user && $scope.currentUser && $scope.user.username === $scope.currentUser.username; // AuthService.isCurrentUser($scope.user);
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

        $scope.submitSearch = function() {
            // TODO deprecated pero guardo for now
            // $('#new-shelf-form').on('submit', function(event) {
            //     var name = $(this).find('input[type=text]').val();
            //     if(name.length === 0 || name.length > 25) {
            //         event.preventDefault();
            //     }
            // });
        };

        $scope.$on('recommendedRendered', function(event) {
            angular.element('#recommended-carousel').slick({
                infinite: false,
                arrows: true
            });
            require(['lightbox2']); // TODO ensure requirejs doesn't load this twice
        });


    });
});
