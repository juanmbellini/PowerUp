'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('ListsCtrl', function($scope, $location, Restangular, SweetAlert) {

        $scope.playStatuses = ['planToPlay','playing','played'];
        $scope.shelves = [{name: 'RPG',id: 1},{name: 'Racing',id: 2}];
        $scope.games = [];
        $scope.userId = $location.search().userId;
        // TODO change to username or add both options ---droche 13/02/2017
        Restangular.one('users',$scope.userId).get().then(function(user) {
            $scope.user = user;
        }, function(response) {
            console.log('Error with status code', response.status); // TODO handle error
        });


        // TODO change games to real game list and recommended games too.
        var baseGames = Restangular.all('games');
        baseGames.getList({})
        .then(function(games) {
            $scope.games = games;
            $scope.recommendedGames = games;
        }, function(response) {
            console.log('Error with status code', response.status);
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
                    console.log('Now your shelf should be deleted');
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

                    inputValue = inputValue.replace(/,/g, ';');

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
                    console.log('Now your shelf should be called ' + inputValue);
                });
        };
        $scope.isCurrentUserLogged = function() {
            return true; // TODO
        };
        $scope.deleteGame = function(game) {
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
                    console.log('Now your game should be deleted');
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
