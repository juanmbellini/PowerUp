'use strict';
define(['powerUp', 'slick-carousel', 'loadingCircle', 'FeedService', 'LikesService'], function (powerUp) {

  powerUp.controller('HomeCtrl', ['$scope', '$location', '$log', '$route', '$timeout', 'searchedTitleService', 'Restangular', 'AuthService', 'FeedService', 'LikesService', function ($scope, $location, $log, $route, $timeout, searchedTitleService, Restangular, AuthService, FeedService, LikesService) {
    Restangular.setFullResponse(true);
    $scope.homePageText = 'This is your homepage';
    $scope.gameTitle = '';
    $scope.submitSearch = function () {
      searchedTitleService.setTitle($scope.gameTitle);
      $location.search({'name': $scope.gameTitle});
      $location.path('search');
    };

    $scope.loadingRecommended = false;
    if (AuthService.isLoggedIn()) {
      $scope.user = AuthService.getCurrentUser();
      Restangular.all('users').one('username', $scope.user.username).get().then(function (response) {
        var user = response.data;
        $scope.loadingRecommended = true;
        Restangular.one('users', user.id).all('recommended-games').getList({}).then(function (response) {
          $scope.recommendedGames = response.data;
          $scope.recommendedMin = Math.min($scope.recommendedGames.length, 5);
          $timeout(function () {
            $scope.$broadcast('recommendedReady');
          });
        });
      });
    }

    $scope.$on('recommendedReady', function (event) {
      $scope.loadingRecommended = false;
      angular.element('#recommended-carousel').slick({
        slidesToShow: $scope.recommendedMin,
        slidesToScroll: $scope.recommendedMin,
        infinite: false,
        arrows: true
      });
    });

    /* ***********************************************************************
     *                                FEED
     * ***********************************************************************/
    $scope.feed = [];
    var feedObj;
    if (AuthService.isLoggedIn()) {
      feedObj = FeedService.initialize(AuthService.getCurrentUser().id);
      $scope.loadingFeed = true;
    }

    $scope.needFeed = function () {
      if (feedObj === null) {
        return null;
      }
      return FeedService.isReady(feedObj) + '' + $scope.loadingFeed;
    };


    $scope.$watch('needFeed()', function () {
      if (AuthService.isLoggedIn() && feedObj !== null && FeedService.isReady(feedObj) && $scope.loadingFeed) {
        var feedArray = FeedService.getFeed(feedObj);
        $scope.feed = $scope.feed.concat(feedArray);
        // feedArray.forEach(function (element, index, array) {
        //     $scope.feed.push(array[index]);
        // });
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
    $scope.reloadFeed = function () {
      $scope.feed = [];
      if (AuthService.isLoggedIn()) {
        feedObj = FeedService.initialize(AuthService.getCurrentUser().id);
        $scope.loadingFeed = true;
      }
    };

    // Likes
    $scope.isLikedByCurrentUser = LikesService.isLikedByCurrentUser;
    $scope.likeReview = function(review) {
        LikesService.like(review, undefined, function() {

        }, function(error) {
            $log.error('Error liking review #', review.id, ': ', error);
        });
    };
    $scope.unlikeReview = function(review) {
        LikesService.unlike(review, undefined, function() {

        }, function(error) {
            $log.error('Error unliking review #', review.id, ': ', error);
        });
    };

    $scope.likeThread = function (thread) {
      LikesService.like(Restangular.one('threads', thread.id), thread, function () {
      }, function (error) {
        $log.error('Error liking thread #', thread.id, ': ', error);
      });
    };

    $scope.unlikeThread = function (thread) {
      LikesService.unlike(Restangular.one('threads', thread.id), thread, function () {
      }, function (error) {
        $log.error('Error unliking thread #', thread.id, ': ', error);
      });
    };

    // reviews
    $scope.canDeleteReview = function (review) {
      return AuthService.isLoggedIn() && AuthService.getCurrentUser().username === review.username;
    };
    $scope.deleteReview = function (review) {
      review.remove().then(function (response) {
          var data = response.data;
          $log.info('Success: ', data);
          $route.reload();
          // TODO solo el filtering
          // $scope.reviews = $scope.reviews.filter(function(reviewToFilter) {
          //     return reviewToFilter.id !== review.id;
          // });
        },
        function (error) {
          $log.error('Error: ', error);
        }, function () {
          // $scope.checkCanWriteReview();
        });
    };

    // functions:
    $scope.getReviewUserProfilePictureUrl = function(review) {
        return Restangular.one('users', review.userId).one('picture').getRequestedUrl();
    };


      /* *************************************************************************************************************
                   FOLLOWS
            * ************************************************************************************************************/
      // Follows for reviews
      $scope.followDisabled = false;

      $scope.canFollow = function (user) {
          return user && AuthService.isLoggedIn() && !AuthService.isCurrentUser(user);
      };

      $scope.updateFollow = function (user) {
          if ($scope.followDisabled) {
              return;
          }
          $scope.followDisabled = true;

          if (!user.social.followedByCurrentUser) {
              // Follow
              Restangular.one('users', user.id).one('followers').put().then(function () {
                  $scope.followDisabled = false;
                  user.social.followedByCurrentUser = true;
                  user.social.followersCount++;
              }, function () {
                  $scope.followDisabled = false;
              });
          } else {
              // Unfollow
              Restangular.one('users',user.id).one('followers').remove().then(function () {
                  $scope.followDisabled = false;
                  user.social.followedByCurrentUser = false;
                  user.social.followersCount--;
              }, function () {
                  $scope.followDisabled = false;
              });
          }
      };

    // Follow friend from feed
    $scope.followFriend = function () {
      if ($scope.friendName === $scope.user.username) {
        $scope.followFriendError = true;
        $scope.followFriendErrorMsj = 'You can\'t follow yourself!';
        return;
      }
      $scope.followFriendErrorMsj = 'User doesn\'t exist';
      $scope.loadingFollowFriend = true;
      $scope.followFriendSend = true;
      Restangular.all('users').one('username', $scope.friendName).get().then(function (response) {
        var user = response.data;
        Restangular.one('users', user.id).one('followers').put().then(function (response) {
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
  }]);
});
