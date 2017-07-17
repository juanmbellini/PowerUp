'use strict';
define(['powerUp', 'slick-carousel', 'onComplete', 'feedService', 'likesService'], function(powerUp) {

	powerUp.controller('HomeCtrl', function($scope, $location, Data, searchedTitleService, Restangular, SweetAlert, AuthService, FeedService, LikesService, $log, $route) {
	    // SweetAlert.swal("BAM!");
		Restangular.setFullResponse(true);
		$scope.homePageText = 'This is your homepage';
		$scope.data = Data;
		$scope.gameTitle = '';
		$scope.submitSearch = function() {
			searchedTitleService.setTitle($scope.gameTitle);
			$location.search({'name': $scope.gameTitle});
			$location.path('search');
      	};

      	if (AuthService.isLoggedIn()) {
			$scope.user = AuthService.getCurrentUser();
			Restangular.all('users').one('username',$scope.user.username).get().then(function (response) {
				var user = response.data;
				Restangular.one('users',user.id).all('recommended-games').getList({}).then(function (response) {
					$scope.recommendedGames = response.data;
					$scope.recommendedMin = Math.min($scope.recommendedGames.length, 5);
				});
			});
		}

		$scope.$on('recommendedRendered', function(event) {
			angular.element('#recommended-carousel').slick({
				infinite: false,
				arrows: true
			});
			require(['lightbox2']); // TODO ensure requirejs doesn't load this twice
		});
		$scope.feed = [];
		var feedObj;
		if (AuthService.isLoggedIn()) {
			feedObj = FeedService.initialize(AuthService.getCurrentUser().id);
            $scope.loadingFeed = true;
		}

        $scope.needFeed = function() {
        	if (feedObj === null) {
        		return null;
        	}
        	return FeedService.isReady(feedObj) + '' + $scope.loadingFeed;
        };


		$scope.$watch('needFeed()', function () {
            if (AuthService.isLoggedIn() && feedObj !== null && FeedService.isReady(feedObj) && $scope.loadingFeed) {
                var feedArray = FeedService.getFeed(feedObj);
                feedArray.forEach(function (element, index, array) {
                    $scope.feed.push(array[index]);
                });
                $scope.thereAreMore = feedObj.thereAreMore;
                $scope.loadingFeed = false;
            }
		});

        $scope.loadMoreFeed = function () {
            $scope.loadingFeed = true;
            // if (AuthService.isLoggedIn() && feedObj !== null) {
            //     while ($scope.feedNeeded > 0 && FeedService.isReady(feedObj)) {
            //         $scope.feedNeeded--;
            //         var element = FeedService.getFeedElement(feedObj);
            //         if (element !== null) {
            //
            //         }
            //     }
            // }
        };




		// Threads
        $scope.isLikedByCurrentUser = function(thread) {
            if (!$scope.isLoggedIn || !thread.hasOwnProperty('likedByCurrentUser')) {
                return false;
            }
            return thread.likedByCurrentUser;
        };

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

        // reviews
        $scope.canDeleteReview = function(review) {
            return AuthService.isLoggedIn() && AuthService.getCurrentUser().username === review.username;
        };
        $scope.deleteReview = function(review) {
            review.remove().then(function(response) {
                    var data = response.data;
                    $log.info('Success: ', data);
                    $route.reload();
                // TODO solo el filtering
                    // $scope.reviews = $scope.reviews.filter(function(reviewToFilter) {
                    //     return reviewToFilter.id !== review.id;
                    // });
                },
                function(error) {
                    $log.error('Error: ', error);
                },function () {
                    // $scope.checkCanWriteReview();
                });
        };


        // Follow friend
        $scope.followFriend = function () {
			if ($scope.friendName === $scope.user.username) {
				$scope.followFriendError = true;
				$scope.followFriendErrorMsj = 'You can\'t follow yourself!';
				return;
			}
			$scope.followFriendErrorMsj = 'User doesn\'t exist';
            $scope.loadingFollowFriend = true;
            $scope.followFriendSend = true;
            Restangular.all('users').one('username',$scope.friendName).get().then(function (response) {
                var user = response.data;
                Restangular.one('users', user.id).all('follow').put().then(function (response) {
                    $scope.followFriendError = false;
                    $scope.loadingFollowFriend = false;
                }, function () {
                    $scope.followFriendError = true;
                    $scope.loadingFollowFriend = false;
                });
            }, function () {
                $scope.followFriendError = true;
                $scope.loadingFollowFriend = false;
            });
        };



	});
});
