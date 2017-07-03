'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('ListsCtrl', function($scope, $location, Restangular, SweetAlert, $log) {

        // TODO replace this values with api calls. -- Droche 15/02/2017
        $scope.playStatuses = ['planToPlay','playing','played'];
        $scope.shelves = null;         // [{name: 'RPG',id: 1},{name: 'Racing',id: 2}];
        $scope.userId = $location.search().userId;
        $scope.username = $location.search().username;

        var userURL = null;
        if ($scope.userId) {
            userURL = Restangular.one('users',$scope.userId);
        } else if ($scope.username) {
            // userURL =  Restangular.all('users').one(username,$scope.username);
            // TODO username. Paja porque todo lo otro depende de la userURL que no podria armar. tendria que hacer que todo espere
        } else {
            $log.debug('Need id in url');
            // TODO search for logged user (?
            $location.search({});
            $location.path('');
        }


        userURL.get().then(function(user) {
            $scope.user = user;
        }, function(response) {
            $log.error('Could not get user or no user with that id. Error with status code', response.status); // TODO handle error
            $location.search({});
            $location.path('');
        });

        userURL.all('shelves').getList({}).then(function(shelves) {
            $scope.shelves = shelves;
        }, function(error) {
            $log.error('Could not get user shelves. Error with status code', error.status);
        });

        $scope.games = [];


        // TODO change games to real game list and recommended games too.
        var baseGames = Restangular.all('games');
        baseGames.getList({}).then(function(games) {
            $scope.games = games;
            $scope.recommendedGames = games;
        }, function(response) {
            $log.error('Error with status code', response.status);
        });



        $scope.deleteShelf = function(shelf) {
            SweetAlert.swal({
                    title: 'Are you sure?',
                    text: 'You are about to permanently delete shelf \"' + shelf.name + '\"',
                    type: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#DD6B55',
                    confirmButtonText: 'Delete',
                    closeOnConfirm: false
                },
                function () {
                    // //Disable submit button to prevent multiple submissions
                    // $('.confirm').attr('disabled', 'disabled');

                    userURL.one('shelves',shelf.name).remove().then(function(response) {
                        $log.debug('Now your shelf should be deleted');
                    },function(error) {

                        // $log.error('Couldn\'t delete shelf ', error);
                    });

              });
        };
        $scope.editShelf = function(shelf) {

            SweetAlert.swal({
                    title: 'Rename \"' + shelf.name + '\" to...',
                    type: 'input',
                    showCancelButton: true,
                    closeOnConfirm: false,
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
                        // TODO handle error. show no user found or something
                    });
                });
        };
        $scope.isCurrentUserLogged = function() {
            return true; // TODO ver auth
        };

        $scope.newShelfName = null;
        $scope.createShelf = function() {
            // TODO validate input?
            var shelf = {name: $scope.newShelfName};
            userURL.all('shelves').post(shelf).then(function(response) {
                $log.debug('Created Shelf');
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



    });

});
