'use strict';
define(['powerUp', 'loadingCircle', 'sweetalert.angular', 'likesService'], function(powerUp) {

    powerUp.controller('ThreadCtrl', ['$scope', '$location', '$routeParams', '$log', 'Restangular', 'AuthService', 'LikesService', function($scope, $location, $routeParams, $log, Restangular, AuthService, LikesService) {
        $scope.threadId = $routeParams.threadId;
        $scope.thread = null;
        $scope.comments = null;
        $scope.isLoggedIn = AuthService.isLoggedIn();
        $scope.currentUser = AuthService.getCurrentUser();
        $scope.isCurrentUser = false;

        // DOM control
        $scope.changeThreadCommentFormVisible = false;
        var pendingRequests = {
            changeTitle: false,
            changeThreadComment: false,
            deleteThread: false,
            comments: {
                create: false
            }
        };

        // Get requested thread
        Restangular.one('threads', $scope.threadId).get().then(function(response) {
            $scope.thread = response;
            $scope.isCurrentUser = AuthService.isCurrentUser(response.creator.username);
            // Get thread top-level comments on success
            response.all('comments').getList().then(function(response) {
                $scope.comments = response;
            }, function(error) {
                $log.error('Error getting comments for thread #', $scope.threadId, ': ', error);
                $scope.thread = {};
            });
        }, function(error) {
            $log.error('Error getting thread #', $scope.threadId, ': ', error);
            $scope.thread = {};
        });

        $scope.isLikedByCurrentUser = LikesService.isLikedByCurrentUser;

        $scope.changeTitle = function() {
            swal({
                title: 'Change thread title',
                text: 'Change from "' + $scope.thread.title + '" to',
                type: 'input',
                inputType: 'text',
                showCancelButton: true,
                closeOnConfirm: false,
                inputPlaceholder: 'New title'
            },
            function(inputValue) {
                if (inputValue === false) {
                    return false;
                }

                if (inputValue === '' || inputValue.length > 50) {
                    swal.showInputError('Please write between 1 and 50 characters');
                    return false;
                }

                swal.disableButtons();

                if (!pendingRequests.changeTitle) {
                    pendingRequests.changeTitle = true;
                    putThread(function(response) {
                        // $scope.thread = response;
                        pendingRequests.changeTitle = false;
                        swal.close();
                    }, function(error) {
                        $log.error('Error updating thread #', $scope.threadId, ': ', error);
                        pendingRequests.changeTitle = false;
                        swal.enableButtons();
                    });
                }
            });
        };

        $scope.showChangeThreadCommentForm = function() {
            if (!$scope.changeThreadCommentFormVisible) {
                $scope.changeThreadCommentFormVisible = true;
            }
            var $textArea = $('#change-thread-comment-textarea');
            $textArea.focus();
        };

        $scope.changeThreadComment = function() {
            if (!pendingRequests.changeThreadComment) {
                pendingRequests.changeThreadComment = true;
                putThread(function(response) {
                    // $scope.thread = response;
                    $scope.changeThreadCommentFormVisible = false;
                    pendingRequests.changeThreadComment = false;
                }, function(error) {
                    $log.error('Error updating thread #', $scope.threadId, ': ', error);
                    pendingRequests.changeThreadComment = false;
                });
            }
        };

        $scope.deleteThread = function() {
            if($scope.thread === null || !$scope.thread.restangularized) {
                return;
            }
            swal({
                title: "Are you sure?",
                text: "This thread and all its comments will be permanently lost",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes",
                cancelButtonText: "No",
                closeOnConfirm: false
            },
            function() {
                swal.disableButtons();

                if(!pendingRequests.deleteThread) {
                    pendingRequests.deleteThread = true;
                    // $scope.thread is a Restangularized element, so it will know where to DELETE
                    $scope.thread.remove().then(function(response) {
                        // Redirect imminent, no need to close sweetAlert, set $scope.thread to NULL, etc.
                        $location.path('threads');
                    }, function(error) {
                        $log.error('Error deleting thread #', $scope.threadId, ': ', error);
                        swal.enableButtons();
                    });
                }
            });
        };

        /* ************************************************************
         *    THREAD LIKE/UNLIKE FUNCTIONS, adapted from ThreadsCtrl
         * ***********************************************************/
        $scope.likeThread = function(thread) {
            LikesService.like(thread, undefined, function() {

            }, function(error) {
                $log.error('Error liking thread #', thread.id, ': ', error);
            });
        };

        $scope.unlikeThread = function(thread) {
            LikesService.unlike(thread, undefined, function() {

             }, function(error) {
                $log.error('Error unliking thread #', thread.id, ': ', error);
             });
        };

        /* **************************************************
         *                  COMMENT FUNCTIONS
         * *************************************************/
        $scope.createComment = function(newComment) {
            if(!newComment || pendingRequests.comments.create) {
                return;
            }
            pendingRequests.comments.create = true;
            
        };

        $scope.likeComment = function(comment) {
            LikesService.like(Restangular.all('threads').one('comments', comment.id), comment, function() {

            }, function(error) {
                $log.error('Error liking comment #', comment.id, ': ', error);
            });
        };

        $scope.unlikeComment = function(comment) {
            LikesService.unlike(Restangular.all('threads').one('comments', comment.id), comment, function() {

            }, function(error) {
                $log.error('Error unliking comment #', comment.id, ': ', error);
            });
        };

        /* *************************************************
         *              PRIVATE FUNCTIONS
         * ************************************************/
        function putThread(successCallback, errorCallback) {
            if($scope.thread === null || !$scope.thread.restangularized) {
                return;
            }
            //$scope.thread is a Restangularized object so it will know where to PUT
            $scope.thread.put().then(function(response) {
                if (typeof successCallback !== 'undefined') {
                    successCallback(response);
                }
            }, function(error) {
                if (typeof errorCallback !== 'undefined') {
                    errorCallback(error);
                } else {
                    $log.error('Error updating thread #', $scope.threadId, ': ', error);
                }
            });
        }
    }]);
});
