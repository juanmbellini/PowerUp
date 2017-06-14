'use strict';
define(['powerUp', 'authService'], function(powerUp) {

    powerUp.controller('ProfileCtrl', ['$scope', '$location', '$timeout', '$log', 'Restangular', 'AuthService', function($scope, $location, $timeout, $log, Restangular, AuthService) {
        Restangular.setFullResponse(false);
        $scope.Restangular = Restangular;
        $scope.AuthService = AuthService;

        $scope.userId = $location.search().userId;
        $scope.username = $location.search().username;
        $scope.currentUser = AuthService.getCurrentUser();

        // All profile-specific info will go here
        $scope.profile = {
            playedCount: 0,
            playingCount: 0,
            planToPlayCount: 0,
            picture: {
                url: null,
                data: null,
                temp: null,
                canDelete: false
            }
        };

        // Form control variables
        $scope.pictureSubmitDisabled = false;
        var deleteProfilePictureDisabled = false;
        var DEFAULT_PROFILE_PICTURE_URL = "http://res.cloudinary.com/dtbyr26w9/image/upload/v1476797451/default-cover-picture.png";

        Restangular.one('users').one('username', $scope.username).get().then(function(user) {
            if (!user) {
                $log.warn('Received null user, redirecting to home');
                $location.search({});
                $location.path('');
            }
            $scope.requestedUser = user;
            $scope.profile.picture.url = getProfilePictureUrl(user.id);
            $scope.profile.picture.canDelete = canDeleteProfilePicture($scope.profile.picture.url);
            getPlayStatuses();
            getTopGames();
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
            Restangular.one('users').one('picture').customPUT(data).then(function (response) {
                $scope.pictureSubmitDisabled = false;
                $scope.profile.picture.canDelete = true;

                // Load the profile picture from own computer, no need to re-retrieve picture
                $scope.profile.picture.data = $scope.profile.picture.temp;
                $scope.profile.picture.temp = null;
                $scope.profile.picture.url = null;
            }, function (error) {
                $log.error('Profile picture upload error: ', error);
                $scope.pictureSubmitDisabled = false;
            });
        };

        $scope.deleteProfilePicture = function() {
            if (!$scope.profile.picture.canDelete || deleteProfilePictureDisabled) {
                return;
            }

            $scope.pictureSubmitDisabled = false;
            deleteProfilePictureDisabled = true;
            Restangular.one('users').one('picture').remove().then(function(response) {
                $timeout(function() {
                    // Reset profile picture URL to get default picture
                    $scope.profile.picture.url = DEFAULT_PROFILE_PICTURE_URL;
                    $scope.profile.picture.data = null;
                    $scope.profile.picture.temp = null;
                    $scope.profile.picture.canDelete = false;

                    $scope.pictureSubmitDisabled = false;
                    deleteProfilePictureDisabled = false;
                });
            }, function(error) {
                $log.error('Couldn\'t delete profile picture ', error);
                $scope.pictureSubmitDisabled = false;
                deleteProfilePictureDisabled = false;
            });
        };

        $scope.canChangePicture = function() {
            return $scope.requestedUser && AuthService.isCurrentUser($scope.requestedUser);
        };

        /* ******************************************
         *              PRIVATE FUNCTIONS
         * *****************************************/

        function getProfilePictureUrl(userId) {
            return Restangular.one('users', userId).one('picture').getRequestedUrl();
        }

        function canDeleteProfilePicture(profilePictureUrl) {
            return profilePictureUrl !== DEFAULT_PROFILE_PICTURE_URL;
        }

        function getPlayStatuses() {
            var userId = $scope.requestedUser.id;
            if (!userId) {
                return;
            }

            Restangular.one('users', userId).getList('play-statuses').then(function(response) {
                $scope.profile.playStatuses = response;
                angular.forEach(response, function(value, key) {
                    switch (value.status) {
                        case 'played':
                            $scope.profile.playedCount++;
                            break;
                        case 'playing':
                            $scope.profile.playingCount++;
                            break;
                        case 'plan to play':
                            $scope.profile.planToPlayCount++;
                            break;
                        default:
                            $log.warn('Unrecognized play status ', value.status, ', ignoring.');
                            break;
                    }
                });
            });
        }

        function getTopGames() {
            var userId = $scope.requestedUser.id;
            if (!userId) {
                return;
            }

            /* TODO sort by descending user score */
            Restangular.one('users', userId).getList('game-scores', {pageSize:10}).then(function(response) {
                $scope.profile.top10 = response;
            });
        }
    }]);

});
