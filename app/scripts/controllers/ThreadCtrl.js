'use strict';
define(['powerUp', 'loadingCircle', 'loadingCircle-small', 'sweetalert.angular', 'likesService', 'paginationService'], function(powerUp) {

    powerUp.controller('ThreadCtrl', ['$scope', '$location', '$routeParams', '$log', 'Restangular', 'AuthService', 'LikesService', 'PaginationService', function($scope, $location, $routeParams, $log, Restangular, AuthService, LikesService, PaginationService) {
        $scope.threadId = $routeParams.threadId;
        $scope.thread = null;
        $scope.comments = null;
        $scope.isLoggedIn = AuthService.isLoggedIn();
        $scope.currentUser = AuthService.getCurrentUser();
        $scope.isCurrentUser = false;

        // Pagination control
        var paginatedComments = null;

        // DOM control
        $scope.changeThreadBodyFormVisible = false;
        $scope.pendingRequests = {
            changeTitle: false,
            changeThreadBody: false,
            deleteThread: false,
            comments: {
                getTopLevel: false,
                create: false
            }
        };
        $scope.hasMoreComments = false;
        $scope.newComment = {};

        // Get requested thread
        Restangular.one('threads', $scope.threadId).get().then(function(response) {
            $scope.thread = response.data;
            $scope.isCurrentUser = AuthService.isCurrentUser($scope.thread.creator.username);

            // Get thread top-level comments on success, don't use getMoreTopLevelComments() because this is a special case
            paginatedComments = PaginationService.initialize($scope.thread, 'comments');
            PaginationService.get(paginatedComments, function(response) {
                $scope.comments = response.data;
                $scope.hasMoreComments = PaginationService.hasMorePages(paginatedComments);
                // $scope.comments.forEach(function(comment) {
                //     $scope.getCommentReplies(comment);
                // });
            }, function(error) {
                $log.error('Error getting comments for thread #', $scope.threadId, ': ', error);
                $scope.comments = [];
            });
        }, function(error) {
            $log.error('Error getting thread #', $scope.threadId, ': ', error);
            $scope.thread = {};
        });

        $scope.isLikedByCurrentUser = LikesService.isLikedByCurrentUser;

        $scope.hasMorePages = PaginationService.hasMorePages;

        $scope.changeThreadTitle = function() {
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

                if (!$scope.pendingRequests.changeTitle) {
                    $scope.thread.title = inputValue;
                    $scope.pendingRequests.changeTitle = true;
                    putThread(function(response) {
                        // $scope.thread = response;
                        $scope.pendingRequests.changeTitle = false;
                        swal.close();
                    }, function(error) {
                        $log.error('Error updating thread #', $scope.threadId, ': ', error);
                        $scope.pendingRequests.changeTitle = false;
                        swal.enableButtons();
                    });
                }
            });
        };

        $scope.showChangeThreadBodyForm = function() {
            if (!$scope.changeThreadBodyFormVisible) {
                $scope.changeThreadBodyFormVisible = true;
            }
            // TODO this probably isn't working because the element isn't visible here, the cycle has to digest
            var $textArea = $('#change-thread-comment-textarea');
            $textArea.focus();
        };

        $scope.changeThreadBody = function() {
            if (!$scope.pendingRequests.changeThreadBody) {
                $scope.pendingRequests.changeThreadBody = true;
                putThread(function(response) {
                    // $scope.thread = response;
                    $scope.changeThreadBodyFormVisible = false;
                    $scope.pendingRequests.changeThreadBody = false;
                }, function(error) {
                    $log.error('Error updating thread #', $scope.threadId, ': ', error);
                    $scope.pendingRequests.changeThreadBody = false;
                });
            }
        };

        $scope.deleteThread = function() {
            if ($scope.thread === null || !$scope.thread.restangularized) {
                return;
            }
            swal({
                title: 'Are you sure?',
                text: 'This thread and all its comments will be permanently lost',
                type: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#DD6B55',
                confirmButtonText: 'Yes',
                cancelButtonText: 'No',
                closeOnConfirm: false
            },
            function() {
                swal.disableButtons();

                if (!$scope.pendingRequests.deleteThread) {
                    $scope.pendingRequests.deleteThread = true;
                    // $scope.thread is a Restangularized element, so it will know where to DELETE
                    $scope.thread.remove().then(function(response) {
                        // Don't forget to close the sweet alert or it will follow the user to the next page!
                        swal.close();
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
        $scope.getMoreTopLevelComments = function() {
            if (paginatedComments === null || $scope.pendingRequests.comments.getTopLevel) {
                return;
            }
            $scope.pendingRequests.comments.getTopLevel = true;
            PaginationService.getNextPage(paginatedComments, function(response) {
                $scope.comments = $scope.comments.concat(response.data);
                $scope.hasMoreComments = PaginationService.hasMorePages(paginatedComments);
                $scope.pendingRequests.comments.getTopLevel = false;
            }, function(error) {
                $log.error('Error getting more comments: ', error);
                $scope.pendingRequests.comments.getTopLevel = false;
            });
        };

        $scope.getCommentReplies = function(comment) {
            if (!PaginationService.isInitialized(comment.paginatedReplies)) {
                comment.paginatedReplies = PaginationService.initialize(Restangular.all('threads').one('comments', comment.id), 'replies', 0);  // Initialize on page 0 so the first call to nextPage will get page 1
            }
            comment.repliesBusy = true;
            PaginationService.getNextPage(comment.paginatedReplies, function(response) {
                if (!comment.replies) {
                    comment.replies = [];
                }
                comment.replies = comment.replies.concat(response.data);
                comment.replies.hasMoreReplies = PaginationService.hasMorePages(comment.paginatedReplies);
                comment.repliesBusy = false;

                // comment.replies.forEach(function(comment) {
                //     $scope.getCommentReplies(comment);
                // });
            }, function(error) {
                $log.error('Error getting replies for comment #' + comment.id + ': ', error);
                comment.repliesBusy = false;
            });
        };

        $scope.createComment = function(newComment) {
            if (!newComment || $scope.pendingRequests.comments.create) {
                return;
            }
            $scope.pendingRequests.comments.create = true;

            $scope.thread.post('comments', newComment).then(function(response) {
                // Fetch newly created comment
                Restangular.oneUrl('routeName', response.headers('Location')).get().then(function(response) {
                    $scope.comments.push(response.data);
                    $scope.pendingRequests.comments.create = false;
                    $scope.newComment.body = '';
                });
            }, function(error) {
                $log.error('Error creating comment: ', error);
                $scope.pendingRequests.comments.create = false;
            });
        };

        $scope.replyToComment = function(parentComment) {
            if (!parentComment || !parentComment.newReply || parentComment.repliesDisabled) {
                return;
            }
            parentComment.repliesDisabled = true;

            Restangular.all('threads').one('comments', parentComment.id).post('replies', parentComment.newReply).then(function(response) {
                // Fetch newly created reply
                Restangular.oneUrl('routeName', response.headers('Location')).get().then(function(response) {
                    if(!parentComment.replies) {
                        parentComment.replies = [];
                    }
                    parentComment.replies.push(response.data);
                    // TODO also consider that this may throw off future results, because we have an element that is not
                    // being considered in pagination
                    parentComment.repliesDisabled = false;
                    parentComment.showReplyForm = false;
                    parentComment.newReply.body = '';
                });
            }, function(error) {
                $log.error('Error replying to comment #' + parentComment.id + ':', error);
                parentComment.repliesDisabled = false;
                parentComment.showReplyForm = false;
            });
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

        $scope.showChangeCommentForm = function(comment) {
            comment.showChangeForm = true;
            comment.showReplyForm = false;
        };

        $scope.showReplyToCommentForm = function(comment) {
            comment.showReplyForm = true;
            comment.showChangeForm = false;
        };

        $scope.changeComment = function(comment) {
            if(comment.editsDisabled) {
                return;
            }

            comment.editsDisabled = true;
            Restangular.all('threads').one('comments', comment.id).customPUT({body: comment.body}).then(function() {
                comment.editsDisabled = false;
                comment.showChangeForm = false;
            }, function(error) {
                $log.error('Error updating comment #' + comment.id + ': ', error);
                comment.editsDisabled = false;
            });
        };

        $scope.deleteComment = function(comment) {
            if(comment.deleteDisabled) {
                return;
            }

            swal({
                    title: 'Are you sure?',
                    text: 'This comment and all its replies will be permanently lost',
                    type: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#DD6B55',
                    confirmButtonText: 'Yes',
                    cancelButtonText: 'No',
                    closeOnConfirm: false
                },
                function() {
                    swal.disableButtons();

                    if (!comment.deleteDisabled) {
                        comment.deleteDisabled = true;
                        Restangular.all('threads').one('comments', comment.id).remove().then(function() {
                            comment.deleteDisabled = false;

                            // Mark comment tree as deleted from frontend
                            markDeleted(comment);
                            // TODO: The pagination of comments may now be inconsistent, since results will be off by 1 element for
                            // each element that gets deleted. Consider triggering a reload of comments, on next reload
                            // the comment will be gone.

                            swal.close();
                        }, function(error) {
                            $log.error('Error deleting comment #' + comment.id + ': ', error);
                            comment.deleteDisabled = false;
                            swal.enableButtons();
                        });
                    }
                });
        };

        /* *************************************************
         *              PRIVATE FUNCTIONS
         * ************************************************/
        function putThread(successCallback, errorCallback) {
            if ($scope.thread === null || !$scope.thread.restangularized) {
                return;
            }
            // $scope.thread is a Restangularized object so it will know where to PUT
            $scope.thread.customPUT({title: $scope.thread.title, body: $scope.thread.body}).then(function(response) {
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

        /**
         * Reecursively loops through the initial comment's replies and their replies to mark them all as logically
         * deleted.
         *
         * @param initialComment The initially deleted comment
         */
        function markDeleted(initialComment) {
            initialComment.deleted = true;
            if (initialComment.replies) {
                initialComment.replies.forEach(function(reply) {
                    markDeleted(reply);
                });
            }
        }
    }]);
});
