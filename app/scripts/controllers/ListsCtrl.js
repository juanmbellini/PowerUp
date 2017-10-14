'use strict';
define(['powerUp', 'slick-carousel', 'onComplete', 'sweetalert.angular', 'loadingCircle', 'PaginationService'], function(powerUp) {

    powerUp.controller('ListsCtrl', function($scope, $location, Restangular, SweetAlert, $log, AuthService, $timeout, $anchorScroll, PaginationService) {

        Restangular.setFullResponse(true);


        // User
        $scope.checkCurrentUserLogged = function() { // TODO change name to isCurrentUserLoggedAndOwner
            $scope.isCurrentUserLogged = AuthService.isLoggedIn();
        };
        $scope.userId = $location.search().id;
        $scope.username = $location.search().username;
        $scope.shelfQuery = $location.search().shelf;
        $scope.currentUser = AuthService.getCurrentUser();
        var userURL = null;
        if ($scope.userId) {
            userURL = Restangular.one('users',$scope.userId);
            userURL.get().then(function(response) {
                var user = response.data;
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
        } else if (AuthService.isLoggedIn()) {
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
        $scope.ready = false;           // All data is loaded (games, statuses, etc. etc.) TODO this only contemplates games
        $scope.refreshingList = false;  // Refreshing list, only games section needs a spinner


        function updateGameList(resetPageNumber) {
            // Don't block new requests if others are running.
            // TODO cancel pending requests if any
            $scope.refreshingList = true;

            $anchorScroll();

            if (resetPageNumber === true) {
                $scope.searchParams.pageNumber = 1;
            }
            // Update paginator
            $scope.gamesPaginator.pagination.pageNumber = $scope.searchParams.pageNumber;
            $scope.gamesPaginator.pagination.pageSize = $scope.searchParams.pageSize;

            // Send only defined search params
            var params = {};
            for (var key in $scope.searchParams) {
                if ($scope.searchParams.hasOwnProperty(key) && typeof $scope.searchParams[key] !== 'undefined') {
                    params[key] = $scope.searchParams[key];
                }
            }
            PaginationService.setRequestParams($scope.gamesPaginator, params);

            // Fire request
            PaginationService.get($scope.gamesPaginator, function(response) {
                $scope.games = response.data;
                $scope.ready = true;
                $scope.refreshingList = false;
                // TODO delet dis
                $scope.headersPagination = response.headers();
                $scope.updatePagination();
            }, function(error) {
                $log.error('Error with status code', response.status);
                $scope.ready = true;
                $scope.refreshingList = false;
            });
        }
        // function addInformationToGame(gameRef, index, gameArray) {
        //     var game = gameArray[index];
        //     Restangular.one('users', $scope.userId).all('game-scores').getList({gameId: game.id}).then(function (gameScore) {
        //         if (gameScore.length > 0) {
        //             game.userScore = gameScore[0].score;
        //         }
        //     }, function (response) {
        //         $log.error('Could not get score from game', response);
        //     });
        //     Restangular.one('users',$scope.userId).all('shelves').getList({gameId: game.id}).then(function (shelvesWithGame) {
        //         game.shelves = shelvesWithGame;
        //     });
        //     Restangular.one('users', $scope.userId).one('play-status', game.id).get().then(function (playStatus) {
        //         if (playStatus.length > 0) {
        //             game.status = playStatus[0].status;
        //         }
        //     }, function (response) {
        //         $log.error('Could not get play status from game', response);
        //     });
        // }

        // PlayStatuses
        $scope.playStatuses = [];
        Restangular.all('users').all('play-statuses').getList({}).then(function (response) {
            $scope.playStatuses = response.data;
        }, function (response) {
            $log.error('Could not get playStatuses', response);
        });
        $scope.selectedPlayStatuses = [];
        $scope.toggleSelectionPlayStatus = function (playStatus) {
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
        if ($scope.shelfQuery) {
            $scope.selectedShelves.push($scope.shelfQuery);
        }
        $scope.toggleSelectionShelves = function (shelf) {
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
            userURL.all('shelves').getList({}).then(function(response) {
                $scope.shelves = response.data;
            }, function(error) {
                $log.error('Could not get user shelves. Error with status code', error.status);
            });
        };
        $scope.shelves = null;
        $scope.getShelves();
        $scope.$watchCollection('selectedShelves', function () {
            $scope.searchParams.shelfName = $scope.selectedShelves;
            updateGameList(true);
            Restangular.one('users',$scope.userId).all('recommended-games').getList({shelves: $scope.selectedShelves}).then(function (response) {
                $scope.recommendedGames = response.data;
                $scope.recommendedMin = Math.min($scope.recommendedGames.length, 5);
            }, function (response) {
                $log.error('Could not get recommended games', response);
                $scope.recommendedGames = [];
            });
        });
        $scope.deleteShelf = function(shelf) {
            swal({
                title: 'Are you sure?',
                text: 'You are about to permanently delete shelf \"' + shelf.name + '\"',
                type: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#DD6B55',
                confirmButtonText: 'Delete',
                closeOnConfirm: false
            },
            function (inputValue) {
                if (inputValue === false) {
                    return false;
                }
                swal.disableButtons();
                userURL.one('shelves',shelf.name).remove().then(function(response) {
                    swal('Success', 'Shelf "' + shelf.name + '" successfully deleted', 'success');
                },function(error) {
                    swal('Sever error', 'Sorry, please try again', 'error');
                });
            });
        };
        $scope.editShelf = function(shelf) {
            swal({
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
                    var oldName = shelf.name;
                    shelf.name = inputValue;
                    userURL.one('shelves',oldName).customPUT(shelf).then(function (response) {
                        $log.debug('Updated Shelf. Now your shelf should be called ' + inputValue);
                        swal('Success', 'Shelf "' + oldName + '" renamed to "' + shelf.name + '"', 'success');
                    }, function(response) {
                        $log.error('Error renaming shelf. Error with status code', response.status);
                        shelf.name = oldName;
                        swal.showInputError('Server error, please try again');
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
                $scope.newShelfName = '';
                // $scope.shelves.push(response);
            }, function(response) {
                $log.error('Error creating shelf. Error with status code', response.status); // TODO handle error
            });
        };

        // RecommendedGames
        $scope.hasSlick = false;
        $scope.first = true;
        $scope.$on('recommendedRendered', function(event) {
            $scope.first = false;
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
            $scope.searchParams.status = $scope.selectedPlayStatuses;
            updateGameList(true);
        });




        // Pagination control
        $scope.pageSizes = [25, 50, 100];
        $scope.searchParams = {
            shelfName: undefined,
            status: undefined,
            pageNumber: 1,
            pageSize: $scope.pageSizes[0]
        };
        $scope.gamesPaginator = PaginationService.initialize(Restangular.one('users', $scope.userId).all('game-list'), undefined, $scope.searchParams.pageNumber, $scope.searchParams.pageSize);

        // TODO delet dis
        $scope.pageSize = 3;
        $scope.pageSizeSelected = 3;
        $scope.updatePageSize = function (pageSizeSelected) {
            $scope.pageSize = pageSizeSelected;
            $scope.pageNumber = 1;
            // $location.search('pageSize', $scope.pageSize);
        };


        $scope.setPageNumber = function (number) {
            if (!$scope.gamesPaginator.pagination.totalPages) {
                return;
            }
            if (number >= 1 && number <= $scope.gamesPaginator.pagination.totalPages) {
                $scope.searchParams.pageNumber = number;
            }
        };

        $scope.getPageRange = function(deltaPages) {
            return PaginationService.getPageRange($scope.gamesPaginator, deltaPages);
        };




        /**
         * Changes the pageNumber query parameter using the newPageNumber
         * @param newPageNumber
         */
        $scope.changePageNumber = function(newPageNumber) {
            $scope.pageNumber = newPageNumber;
            // $location.search('pageNumber', $scope.pageNumber);
        };
        /**
         * Updates pagination variables using the pagination headers
         * TODO delet dis
         */
        $scope.updatePagination = function() {
            $scope.pageNumber = parseInt($scope.headersPagination['x-page-number'], 10);
            $scope.totalPages = parseInt($scope.headersPagination['x-total-pages'], 10);
            // Show the fist ten
            $scope.paginationJustOne = ($scope.pageNumber - 4 <= 0) || ($scope.totalPages <= 10);
            // Show the last ten
            $scope.paginationNoMorePrev = ($scope.pageNumber + 5 > $scope.totalPages);

            $scope.paginationTheFirstOnes = ($scope.pageNumber + 5 < 10);
            $scope.paginationNoMoreNext = ($scope.pageNumber + 5 >= $scope.totalPages) || ($scope.totalPages < 10);

            if ($scope.paginationJustOne) {
                $scope.paginationBegin = 1;
            } else {
                $scope.paginationBegin = $scope.paginationNoMorePrev ? $scope.totalPages - 9 : $scope.pageNumber - 4;
            }
            if ($scope.paginationNoMoreNext) {
                $scope.paginationEnd = $scope.totalPages;
            } else {
                $scope.paginationEnd = $scope.paginationTheFirstOnes ? 10 : $scope.pageNumber + 5;
            }
            $scope.rangePagination = [];
            for (var i = $scope.paginationBegin; i <= $scope.paginationEnd; i++) {
                $scope.rangePagination.push(i);
            }
        };
        $scope.$watch('searchParams.pageNumber', function () {
            updateGameList(false);
        });
        $scope.$watchCollection('searchParams.pageSize', function() {
            updateGameList(false);
        });
    });
});
