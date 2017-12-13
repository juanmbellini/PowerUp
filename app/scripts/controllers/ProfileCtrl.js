'use strict';
define(['powerUp', 'AuthService', 'sweetalert.angular', 'loadingCircle', 'loadingCircleSmall', 'ratingStars'], function(powerUp) {

    powerUp.controller('ProfileCtrl', ['$scope', '$location', '$timeout', '$log', 'Restangular', 'AuthService', function($scope, $location, $timeout, $log, Restangular, AuthService) {
        Restangular.setFullResponse(true);
        $scope.Restangular = Restangular;
        $scope.AuthService = AuthService;

        $scope.isCurrentUser = AuthService.isCurrentUser;

        $scope.userId = $location.search().userId;
        $scope.username = $location.search().username;
        $scope.currentUser = AuthService.getCurrentUser();

        $scope.requestedUser = null;
        $scope.uploadingPicture = false;
        $scope.pictureUploaded = true;
        // All profile-specific info will go here
        $scope.profile = {
            gamesInList: 0,
            picture: {
                url: null,
                data: null,
                temp: null,
                canDelete: false
            }
        };

        // Form control variables
        $scope.pictureSubmitDisabled = true;
        var deleteProfilePictureDisabled = false;
        var resettingPassword = false;
        var DEFAULT_PROFILE_PICTURE_URL = 'http://res.cloudinary.com/dtbyr26w9/image/upload/v1476797451/default-cover-picture.png';

        Restangular.one('users').one('username', $scope.username).get().then(function(response) {
            var user = response.data;
            if (!user) {
                $log.warn('Received null user, redirecting to home');
                $location.search({});
                $location.path('');
            }
            $scope.requestedUser = user;
            Restangular.one('users', user.id).getList('game-list').then(function(response) {
                // TODO use paginator
                $scope.profile.gamesAmount = parseInt(response.headers()['x-overall-amount-of-elements'], 10);
                if (!$scope.profile.gamesAmount) {
                    $scope.profile.gamesAmount = 0;
                }
            });
            $scope.profile.picture.url = getProfilePictureUrl(user);
            $scope.profile.picture.canDelete = canDeleteProfilePicture($scope.profile.picture.url); // FIXME get this from user JSON
            getFollowerData();
            getShelvesData();
        }, function(response) {
            $log.error('Error retrieving user: ', response); // TODO handle error
            $location.search({});
            $location.path('');
        });

        $scope.parseProfilePicture = function(file) {
            if (!file) {
                return;
            }

            var r = new FileReader();
            $scope.pictureSubmitDisabled = true;
            r.onloadend = function(e) {
                var pictureBase64 = e.target.result;
                $timeout(function() {
                    $scope.pictureSubmitDisabled = false;
                    $scope.profile.picture.temp = pictureBase64;
                    $scope.pictureUploaded = false;
                });
            };
            r.readAsDataURL(file);
        };

        $scope.uploadProfilePicture = function() {
            var data = $scope.profile.picture.temp;
            if (!data || !$scope.canChangePicture()) {
                return;
            }

            $scope.pictureSubmitDisabled = true;
            $scope.uploadingPicture = true;
            Restangular.one('users').one('picture').customPUT(data).then(function (response) {
                // Don't re-enable submit button, user should select a different picture to re-submit

                $scope.uploadingPicture = false;
                $scope.profile.picture.canDelete = true;
                $scope.pictureUploaded = true;

                // Load the profile picture from own computer, no need to re-retrieve picture
                $scope.profile.picture.data = $scope.profile.picture.temp;
                $scope.profile.picture.temp = null;
                $scope.profile.picture.url = null;
            }, function (error) {
                $log.error('Profile picture upload error: ', error);
                $scope.pictureSubmitDisabled = false;
                $scope.uploadingPicture = false;
                swal({
                  title: 'Oh no!',
                  text: error.data.errors.map(function(e) {
                    return e.message;
                  }).join('<br />'),
                  type: 'error'
                });
            });
        };

        $scope.confirmDeleteProfilePicture = function() {
          swal({
            title: 'Delete profile picture?',
            text: 'Are you sure you want to delete your profile picture?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#DD6B55',
            closeOnConfirm: false
          }, function() {
            if (!$scope.profile.picture.canDelete || deleteProfilePictureDisabled) {
              return;
            }

            swal.disableButtons();
            $scope.pictureSubmitDisabled = false;
            deleteProfilePictureDisabled = true;
            Restangular.one('users').one('picture').remove().then(function(response) {
              // Reset profile picture URL to get default picture
              $scope.profile.picture.url = DEFAULT_PROFILE_PICTURE_URL;
              $scope.profile.picture.data = null;
              $scope.profile.picture.temp = null;
              $scope.profile.picture.canDelete = false;
              $scope.pictureSubmitDisabled = false;
              deleteProfilePictureDisabled = false;

              swal({
                title: 'Picture deleted!',
                type: 'success'
              });
            }, function(error) {
              $log.error('Couldn\'t delete profile picture ', error);
              $scope.pictureSubmitDisabled = false;
              deleteProfilePictureDisabled = false;

              swal({
                title: "Oh no! Couldn't delete profile picture",
                text: error.data.errors.map(function(e) {
                  return e.message;
                }).join('<br />'),
                type: 'error'
              });
            });
          });
        };

        $scope.canChangePicture = function() {
            return $scope.requestedUser && AuthService.isCurrentUser($scope.requestedUser);
        };

        /**
         * Shows the first SWAL for resetting password. Asks for new password.
         */
        $scope.changePassword = function() {
            swal({
                    title: 'New password',
                    type: 'input',
                    inputType: 'password',
                    showCancelButton: true,
                    closeOnConfirm: false
                },
                function(inputValue) {
                    if (inputValue === false) {
                        return false;
                    } else if (inputValue === '') {
                        swal.showInputError('Please write a new password');
                        return false;
                    }

                    secondPasswordSwal(inputValue);
                });
        };

        /* *************************************************************************************************************
               Shelves
        * ************************************************************************************************************/
        $scope.shelves = [];
        var getShelvesData = function() {
            var userId = $scope.requestedUser.id;
            Restangular.one('users', userId).all('shelves').getList({}).then(function (response) {
                $scope.shelves = response.data;
                // TODO get the number of elements in each shelf. users/:userId/shelves/:shelfName/games Or get it from back.
            });
        };




        /* *************************************************************************************************************
                FOLLOWS
         * ************************************************************************************************************/
        $scope.followDisabled = false;
        $scope.followers = [];
        $scope.following = [];

        var getFollowerData = function() {
            var userId = $scope.requestedUser.id;
            Restangular.one('users', userId).all('followers').getList({}).then(function (response) {
                $scope.followers = response.data;
            });
            Restangular.one('users', userId).all('following').getList({}).then(function (response) {
                $scope.following = response.data;
            });
        };

        $scope.canFollow = function () {
            return $scope.requestedUser && AuthService.isLoggedIn() && !AuthService.isCurrentUser($scope.requestedUser);
        };

        $scope.updateFollow = function () {
            if ($scope.followDisabled) {
                return;
            }
            $scope.followDisabled = true;

            if (!$scope.requestedUser.social.followedByCurrentUser) {
                // Follow
                Restangular.one('users', $scope.requestedUser.id).one('followers').put().then(function () {
                    $scope.followDisabled = false;
                    $scope.requestedUser.social.followedByCurrentUser = true;
                    $scope.requestedUser.social.followersCount++;
                }, function () {
                    $scope.followDisabled = false;
                });
            } else {
                // Unfollow
                Restangular.one('users',$scope.requestedUser.id).one('followers').remove().then(function () {
                    $scope.followDisabled = false;
                    $scope.requestedUser.social.followedByCurrentUser = false;
                    $scope.requestedUser.social.followersCount--;
                }, function () {
                    $scope.followDisabled = false;
                });
            }
        };





        /* ******************************************
         *              PRIVATE FUNCTIONS
         * *****************************************/

        function getProfilePictureUrl(user) {
          return Restangular.one('users', user.id).one('picture').getRequestedUrl();
        }

        function canDeleteProfilePicture(profilePictureUrl) {
            return profilePictureUrl !== DEFAULT_PROFILE_PICTURE_URL;
        }



        function getTopGames() {
            var userId = $scope.requestedUser.id;
            if (!userId) {
                return;
            }

            /* TODO sort by descending user score */
            Restangular.one('users', userId).getList('game-scores', {pageSize: 10}).then(function(response) {
                $scope.profile.top10 = response;
            });
        }

        /**
         * Shows the second password change SWAL. The input password must match the provided new password. On cancel,
         * goes back to the first SWAL. On confirm, hits API.
         *
         * @param newPassword   The new password, as input in {@link firstPasswordSwal()}. Must match that.
         */
        function secondPasswordSwal(newPassword) {
            if (resettingPassword) {
                return;
            }
            swal({
                title: 'Confirm password',
                type: 'input',
                inputType: 'password',
                showCancelButton: true,
                closeOnConfirm: false,
                closeOnCancel: false,
                cancelButtonText: 'Go Back'
            },
            function(inputValue) {
                if (inputValue === false) {
                    // Pressed cancel
                    $scope.changePassword();
                    return;
                }

                if (inputValue === '') {
                    swal.showInputError('Please write your new password again');
                    return false;
                } else if (inputValue !== newPassword) {
                    swal.showInputError('Passwords don\'t match');
                    return false;
                }

                // Confirmed, hit API
                swal.disableButtons();
                resettingPassword = true;

                Restangular.all('users').customPUT({password: inputValue}, 'password').then(function(response) {
                    swal('Password Changed!', undefined, 'success');
                    resettingPassword = false;
                }, function(error) {
                    swal.showInputError('Server error, please try again');
                    $log.error('Error resetting password: ', error);
                    resettingPassword = false;
                });
            });
        }
    }]);

});
