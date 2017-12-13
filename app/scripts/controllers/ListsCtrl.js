'use strict';
define(['powerUp', 'slick-carousel', 'onComplete', 'sweetalert.angular', 'loadingCircle', 'ratingStars', 'PaginationService'], function(powerUp) {

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
            }, function(error) {
                $log.error('Error with status code', error.status);
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
        $scope.shelves = null;

        $scope.getShelves = function() {
            userURL.all('shelves').getList({}).then(function(response) {
                $scope.shelves = response.data;
            }, function(error) {
                $log.error('Could not get user shelves. Error with status code', error.status);
            });
        };
        $scope.getShelves();

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
                  // Remove shelf
                  $scope.shelves.splice($scope.shelves.findIndex(function(s) {
                    return s.id === shelf.id;
                  }), 1);

                  swal({
                    title: 'Success',
                    text: 'Shelf "' + shelf.name + '"successfully deleted',
                    type: 'success'
                  });
                }, function(error) {
                  swal({
                    title: "Oh no! Couldn't delete shelf",
                    text: error.data.errors.map(function(e) {
                      return e.message;
                    }).join('<br />'),
                    type: 'error'
                  });
                });
            });
        };

        /**
         *  function that deletes the game from your list. Removing everything you did.
         */
        $scope.deleteGame = function(item) {
            swal({
                    title: 'Are you sure?',
                    text: 'You are about to permanently delete your score, reviews and play status from \"' + item.game.name + '\" and remove it from all your shelves!',
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
                    var game = item.game;
                    var shelves = item.shelvesHolding;
                    $scope.games = $scope.games.filter(function(gameToFilter) {
                        return gameToFilter.game.id !== game.id;
                    });
                    // delete score:
                    Restangular.one('users', $scope.userId).one('game-scores',game.id).remove().then(function (response) {
                        $log.debug('removed score from game', response);
                    }, function (response) {
                        $log.error('Could not remove score from game', response);
                    });
                    // delete status
                    Restangular.one('users', $scope.userId).one('play-status',game.id).remove().then(function (response) {
                        $log.debug('removed play status from game', response);
                    }, function (response) {
                        $log.error('Could not remove play status from game', response);
                    });
                    // delete from shelves:
                    angular.forEach(shelves,function(shelf) {
                        Restangular.one('users',$scope.userId).one('shelves',shelf.name).one('games',game.id).remove().then(function () {
                        }, function (response) {
                            $log.error('could not remove game from shelf', response);

                        });
                    });
                    // delete review
                    Restangular.all('reviews').getList({gameId: game.id, userId: $scope.userId}).then(function (response) {
                        var reviews = response.data;
                        if (reviews.length > 0) {
                            reviews[0].remove().then(function(data) {
                                $log.debug('Success: ', data);
                            },
                            function(error) {
                                $log.error('Error: ', error);
                            });
                        }
                    }, function(error) {
                        $log.error('There was an error getting the review to delete:', error);
                    });
                    swal('Success', 'Game" ' + item.game.name + ' " successfully deleted from your list!', 'success');
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
                    if (!inputValue) {
                        swal.showInputError('Please write a new name');
                        return false;
                    }
                    var oldName = shelf.name;
                    shelf.name = inputValue;
                    userURL.one('shelves',oldName).customPUT(shelf).then(function (response) {
                        $log.debug('Updated Shelf. Now your shelf should be called ' + inputValue);
                        swal('Success', 'Shelf "' + oldName + '" renamed to "' + shelf.name + '"', 'success');
                    }, function(error) {
                        $log.error('Error renaming shelf:', error);
                        shelf.name = oldName;
                        swal.showInputError(error.data.errors.map(function(e) {
                          return e.message;
                        }).join('<br />'));
                    });
                });
        };

        $scope.newShelfName = null;
        $scope.creatingShelf = false;
        $scope.createShelf = function() {
          if ($scope.creatingShelf) {
            return;
          }
          $scope.creatingShelf = true;

          $log.debug('Attempting to create shelf "' + $scope.newShelfName + '"');
          var shelf = {name: $scope.newShelfName};
          userURL.all('shelves').post(shelf).then(function(data) {
            $log.debug('Shelf "' + shelf.name + '" successfully created');
            $scope.getShelves();
            $scope.creatingShelf = false;
            $scope.shelves.push(shelf);
            $scope.newShelfName = '';
            swal({
              title: 'Success',
              text: 'Shelf "' + shelf.name + '" successfully created',
              type: 'success'
            });
          }, function(error) {
            swal({
              title: "Oh no! Couldn't create shelf",
              text: error.data.errors.map(function(e) {
                return e.message;
              }).join('<br />'),
              type: 'error'
            });
            $scope.creatingShelf = false;
            $log.error('Error creating shelf:', error);
          });
        };

        /* ***************************************************************************
         *                          RECOMMENDED GAMES CONTROL
         * ***************************************************************************/
        // Recommended games
        $scope.hasSlick = false;
        $scope.loadingRecommended = false;

        $scope.recommendedText = function() {
            var result = $scope.isUserLoggedOwner ? 'you' : ($scope.user && $scope.user.username);
            if ($scope.selectedShelves.length > 0) {
                result += ' using selected shelves';
            }
            return result;
        };

        $scope.$on('recommendedReady', function(event) {
            $scope.loadingRecommended = false;
            if ($scope.hasSlick) {
                // Un-slick first
                angular.element('#recommended-carousel').slick('unslick');
                $scope.hasSlick = false;
            }
            if ($scope.recommendedGames.length) {
              angular.element('#recommended-carousel').slick({
                slidesToShow: $scope.recommendedMin,
                slidesToScroll: $scope.recommendedMin,
                infinite: false,
                arrows: true
              });
              $scope.hasSlick = true;
            }
        });

      /* ***************************************************************************
       *                          PAGINATION CONTROL
       * ***************************************************************************/
        $scope.pageSizes = [25, 50, 100];
        $scope.searchParams = {
            shelfName: undefined,
            status: undefined,
            pageNumber: 1,
            pageSize: $scope.pageSizes[0]
        };
        $scope.gamesPaginator = PaginationService.initialize(Restangular.one('users', $scope.userId).all('game-list'), undefined, $scope.searchParams.pageNumber, $scope.searchParams.pageSize);

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

        $scope.validPageSizes = function() {
            var result = [];
            var pagination = $scope.gamesPaginator.pagination;
            if (!pagination.totalElements) {
                return result;
            }
            $scope.pageSizes.forEach(function(pageSize, index, pageSizes) {
                if (pagination.totalElements >= pageSize || (index > 0 && pagination.totalElements > pageSizes[index - 1])) {
                    result.push(pageSize);
                }
            });
            var customPageSize = $scope.pageSizes.indexOf($scope.searchParams.pageSize) === -1 ? $scope.searchParams.pageSize : null;
            if (customPageSize) {
                result.push(customPageSize);
                result.sort(function(a, b) {
                    return a - b;
                });
            }
            return result;
        };


        /*
         *  Watchers; changes in these variables will refresh the game lists and possibly something else.
         */
        $scope.$watchCollection('selectedShelves', function () {
            if ($scope.ready) {
                // Update shelves
                $scope.searchParams.shelfName = $scope.selectedShelves;
                updateGameList(true);
            }

            // Update game suggestions
            $scope.loadingRecommended = true;
            $timeout(function() {
              if ($scope.hasSlick) {
                // Un-slick
                angular.element('#recommended-carousel').slick('unslick');
                $scope.recommendedGames = [];
                $scope.hasSlick = false;
              }
            });
            Restangular.one('users',$scope.userId).all('recommended-games').getList({shelves: $scope.selectedShelves}).then(function (response) {
                $scope.recommendedGames = response.data;
                $scope.recommendedMin = Math.min($scope.recommendedGames.length, 5);
                $timeout(function() {
                    $scope.$broadcast('recommendedReady');
                });
            }, function (response) {
              $log.error('Could not get recommended games', response);
              $scope.loadingRecommended = false;
              $scope.recommendedGames = [];
              $timeout(function() {
                $scope.$broadcast('recommendedReady');
              });
            });
        });

        $scope.$watchCollection('selectedPlayStatuses', function() {
            if ($scope.ready) {
                $scope.searchParams.status = $scope.selectedPlayStatuses;
                updateGameList(true);
            }
        });

        $scope.$watch('searchParams.pageNumber', function () {
            if ($scope.ready) {
                updateGameList(false);
            }
        });
        $scope.$watchCollection('searchParams.pageSize', function() {
            if ($scope.ready) {
                $scope.searchParams.pageSize = parseInt($scope.searchParams.pageSize, 10);
                updateGameList(false);
            }
        });

        // Now that everything is defined, trigger initial search!
        updateGameList(false);
    });
});
