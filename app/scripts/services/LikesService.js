'use strict';
define(['powerUp', 'AuthService'], function(powerUp) {

    powerUp.service('LikesService', ['$log', 'Restangular', 'AuthService', function ($log, Restangular, AuthService) {

        /* ********************************************
         *              Public functions
         * *******************************************/

        /**
         * Likes a given Restangularized object. Performs a PUT to [this object's URL]/likes
         * Also:
         *      - Sets a "likesDisabled" property on the object as true while the request is in progress, and sets it to
         *      false when done, regardless of result.
         *      - On success, sets the object's "likedByCurrentUser" property to true.
         *      - On success, increments the object's "likeCount" property if it has such property.
         *
         * @param restangularizedObject The Restangularized object to like. An object will be Restangularized if it was
         *                              previously obtained through Restangular.
         * @param likeableObject        (Optional) If the object that needs to be updated is not the restangularized object.
         * @param successCallback       (Optional) Callback function to execute on success.
         * @param failureCallback       (Optional) Callback function to execute on failure. Receives error object as parameter.
         */
        function like(restangularizedObject, likeableObject, successCallback, failureCallback) {
            if (!likeableObject) {
                likeableObject = restangularizedObject;
            }
            if (!likeableObject || !restangularizedObject.hasOwnProperty('restangularized') || !AuthService.isLoggedIn()) {
                return;
            }
            if (likeableObject.hasOwnProperty('likesDisabled') && likeableObject.likesDisabled) {
                return;
            }
            if (likeableObject.hasOwnProperty('likedByCurrentUser') && likeableObject.likedByCurrentUser === true) {
                return;
            }
            likeableObject.likesDisabled = true;

            restangularizedObject.one('likes').put().then(function() {
                $log.debug('Successfully liked ', likeableObject);

                likeableObject.likesDisabled = false;
                likeableObject.likedByCurrentUser = true;
                if (likeableObject.hasOwnProperty('likeCount')) {
                    likeableObject.likeCount++;
                }

                if (typeof successCallback !== 'undefined') {
                    successCallback();
                }
            }, function(error) {
                likeableObject.likesDisabled = false;

                if (typeof failureCallback !== 'undefined') {
                    failureCallback(error);
                } else {
                    $log.error('Error liking ', likeableObject, ': ', error);
                }
            });
        }

        /**
         * Removes a given Restangularized like object. Performs a DELETE to [this object's URL]
         * Also:
         *      - Sets a "likesDisabled" property on the object as true while the request is in progress, and sets it to false
         *      when done, regardless of result.
         *      - On success, sets the object's "likedByCurrentUser" property to false.
         *      - On success, decrements the object's "likeCount" property if it has such property.
         *
         * @param restangularizedObject The Restangularized like object to delete. An object will be Restangularized if it was
         *                              previously obtained through Restangular.
         * @param likeableObject        (Optional) If the object that needs to be updated is not the restangularized object.
         * @param successCallback       (Optional) Callback function to execute on success.
         * @param failureCallback       (Optional) Callback function to execute on failure. Receives error object as parameter.
         */
        function unlike(restangularizedObject, likeableObject, successCallback, failureCallback) {
            if (!likeableObject) {
                likeableObject = restangularizedObject;
            }
            if (!likeableObject || !restangularizedObject.hasOwnProperty('restangularized') || !AuthService.isLoggedIn()) {
                return;
            }
            if (likeableObject.hasOwnProperty('likesDisabled') && likeableObject.likesDisabled) {
                return;
            }
            likeableObject.likesDisabled = true;

            restangularizedObject.one('likes').remove().then(function() {
                $log.debug('Successfully unliked ', likeableObject);

                likeableObject.likesDisabled = false;
                likeableObject.likedByCurrentUser = false;
                if (likeableObject.hasOwnProperty('likeCount')) {
                    likeableObject.likeCount--;
                }

                if (typeof successCallback !== 'undefined') {
                    successCallback();
                }
            }, function(error) {
                likeableObject.likesDisabled = false;
                if (typeof failureCallback !== 'undefined') {
                    failureCallback(error);
                } else {
                    $log.error('Error unliking ', likeableObject, ': ', error);
                }
            });
        }

        /**
         * Returns whether a likeable object (e.g. thread, comment) is liked by the current user.
         *
         * @param {object} object   The object to check for likes.
         * @returns {boolean}       If not logged in or if the object does not have like information, always returns false.
         *                          Otherwise returns whether the object is liked by the current user.
         */
        function isLikedByCurrentUser(object) {
            if (!AuthService.isLoggedIn() || !object.hasOwnProperty('likedByCurrentUser')) {
                return false;
            }
            return object.likedByCurrentUser;
        }

        /**
         * Sets whether a likeable object is liked by the current user. Requires to be logged in.
         * TODO: Unused, remove?
         *
         * @param {object} object       The object on which to set liked. Not modified if not logged in.
         * @param {boolean} isLiked     Whether the object is liked by the current user.
         * @return {object}             The passed object.
         */
        function setLikedByCurrentUser(object, isLiked) {
            if (!object || typeof object !== 'object' || typeof isLiked !== 'boolean' || !AuthService.isLoggedIn()) {
                return object;
            }
            object.likedByCurrentUser = isLiked;
            return object;
        }

        /* ********************************************
         *              Private functions
         * *******************************************/


        // Public exported functions
        return {
            like: like,
            unlike: unlike,
            isLikedByCurrentUser: isLikedByCurrentUser
        };
    }]);
});
