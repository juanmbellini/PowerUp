
define(['trademygame', '../services/authService', 'directives/equalFields', 'directives/personalInfo', 'directives/userReviews', 'directives/tradeRequests', 'directives/tradeOffers', 'directives/userHistory', 'directives/userOffers', 'directives/likedUsers', 'directives/productInterests', 'directives/settings'], function(trademygame) {

  'use strict';
  trademygame.controller('user', function($scope, $routeParams, $location, $window, $sce, Restangular, authService, toastr, $translate) {

    $scope.changepassform = {"userId":$routeParams.id, "actualPass":"", "newPass1":"", "newPass2":""};
    $scope.userReviewForm = {"writerId":$routeParams.id, "title":"", "review":"", "reviewedId":"", "tradeId":"", "rating":""};

    $scope.loading = true;

    Restangular.one('users', $routeParams.id).get().then(function(data) {
      $scope.loading = false;
      $scope.user = data.plain();
      $scope.username = $scope.user.username;
      $scope.iconId = $scope.user.iconId;
      $scope.newIconForm = {"iconId": $scope.iconId};
    },function (data) {
        $scope.loading = false;

    });

    $scope.id = $routeParams.id;


    $scope.loggedUserId  = authService.loggedId();

    $scope.offers = null;

    $scope.userReviews = null;

    $scope.tradesByMe = null;

    $scope.tradesToMe = null;

    $scope.doneTrades = null;

    $scope.userLikes = null;

    $scope.productInterests = null;

    $scope.deleteOffer = function(index){
      var offerId = $scope.offers[index].offerId;
      $("#deleteOfferModal" + offerId).modal('hide');
      $('body').removeClass('modal-open');
      $('.modal-backdrop').remove();
      Restangular.one('offers', offerId).remove().then(function(data){
        $scope.offers.splice(index,1);
      });
    }

    $scope.cancelTrade = function(trades, index){
      var tradeId = trades[index].tradeId;
      Restangular.one('trades', tradeId).one('state/cancel').put().then(function(data){
        $scope.loadDoneTrades();
          $("#tabHistory").click();
        if($scope.doneTrades != null){
          $scope.doneTrades.push(data.plain());
        }
      });
    }

    $scope.cancelAcceptedTrade = function(trades,index){
      var tradeId = trades[index].tradeId;
      Restangular.one('trades', tradeId).one('state/cancelaccepted').put().then(function(data){
          $scope.loadDoneTrades();
          $("#tabHistory").click();
        if($scope.doneTrades != null){
          $scope.doneTrades.push(data.plain());
        }
      });
    }

    $scope.acceptTrade = function(trades, index){
      var tradeId = trades[index].tradeId;
      Restangular.one('trades', tradeId).one('state/accept').put().then(function(data){
        trades[index] = data.plain();
        Restangular.one('trades/offerer', $routeParams.id).getList().then(function(data) {
          $scope.tradesToMe = data.plain();
        });
      });
    }

    $scope.rejectTrade = function(index){
      var tradeId = $scope.tradesToMe[index].tradeId;
      Restangular.one('trades', tradeId).one('state/reject').put().then(function(data){
          $scope.loadDoneTrades();
          $("#tabHistory").click();
        if($scope.doneTrades != null){
          $scope.doneTrades.push(data.plain());
        }
      });
    }

    $scope.completeTrade = function(trades, index){
      var tradeId = trades[index].tradeId;
      Restangular.one('trades', tradeId).one('state/complete').put().then(function(data){
          $scope.loadDoneTrades();
          $("#tabHistory").click();
        if($scope.doneTrades != null){
          $scope.doneTrades.push(data.plain());
        }
      });
    }


    $scope.loadingOffer = true;
    $scope.loadUserOffers = function(){
      Restangular.all('offers').getList({userId: $routeParams.id}).then(function(data) {
        $scope.offers = data.plain();
        $scope.loadingOffer = false;
      }, function (data) {
          $scope.loadingOffer = false;
      });
    }

      $scope.loadingUserReviews = true;
      $scope.loadUserReviews = function(){
      Restangular.one('users', $routeParams.id).one('reviews').getList().then(function(data) {
        $scope.userReviews = data.plain();
          $scope.loadingUserReviews = false;
      }, function (data) {
          $scope.loadingUserReviews = false;
      });
    }

    $scope.loadingTradesByMe = true;
    $scope.loadTradesByMe = function(){
      Restangular.one('trades/interested', $routeParams.id).getList().then(function(data) {
        $scope.tradesByMe = data.plain();
          $scope.loadingTradesByMe = false;

      }, function (data) {
          $scope.loadingTradesByMe = false;
      });
    }

    $scope.loadingTradesToMe = true;
    $scope.loadTradesToMe = function(){
      Restangular.one('trades/offerer', $routeParams.id).getList().then(function(data) {
        $scope.tradesToMe = data.plain();
          $scope.loadingTradesToMe = false;
      }, function (data) {
          $scope.loadingTradesToMe = false;

      });
    }

    $scope.loadingDoneTrades = true;
    $scope.loadDoneTrades = function(){
      Restangular.one('trades', $routeParams.id).one('done').getList().then(function(data) {
        $scope.doneTrades = data.plain();
          $scope.loadingDoneTrades = false;

      }, function (data) {
          $scope.loadingDoneTrades = false;
      });
    }

    $scope.loadingUserLikes = true;
    $scope.loadUserLikes = function(){
        Restangular.all('users/likes/liker').getList({likerid: $routeParams.id}).then(function(data){

          var likes = data.plain();
          var matrix = [];
          var i,k;

          for(i=0, k=-1; i<likes.length; i++){
            if( i%4 ==0){
              k++;
              matrix[k]=[];
            }
            matrix[k].push(Restangular.stripRestangular(likes[i]));
          }
          $scope.userLikes = matrix;
          $scope.loadingUserLikes = false;

        }, function (data) {
            $scope.loadingUserLikes = false;
        })
      }

    $scope.loadingProductInterests = true;
    $scope.loadProductInterests = function(){
      Restangular.one('products/interests', $routeParams.id).getList().then(function(data){
        var interests = data.plain();
        var matrix = [];
        var i,k;

        for(i=0, k=-1; i<interests.length; i++){
          if( i%4 ==0){
            k++;
            matrix[k]=[];
          }
          matrix[k].push(Restangular.stripRestangular(interests[i]));
        }
        $scope.productInterests = matrix;
        $scope.loadingProductInterests = false;
      }, function (data) {
          $scope.loadingProductInterests = false;
      })
    }

	$scope.notOldPass = false;

	$scope.passwordPattern = /^([A-Z]|[a-z]|[0-9]|(á|é|í|ó|ú|ñ|Á|É|Í|Ó|Ú|Ñ))+$/;

    $scope.changePassword = function(){
	  $scope.notOldPass = false;
      Restangular.one('users', $routeParams.id).one('password').customPUT($scope.changepassform).then(function(data){
        $("#changePassModal").modal('hide');
		    toastr.success($translate.instant('userpage_passchanged'),$translate.instant('success'));
      }, function(response){
        var errors = response.data.errors;
		    if(errors != undefined){
		    for(var i = 0; i < errors.length; i++){
		        if(errors[i].property == "changePassword.arg1"){
		            $scope.notOldPass = true;
		        }
		    }
		}
		else {
        	toastr.error($translate.instant('userpage_toast_changepass_error'),$translate.instant('error_error'));
		}
      });
    }

    $scope.reviewUser = function(index){
      $scope.userReviewForm.tradeId = $scope.doneTrades[index].tradeId;
      if($scope.user.userid == $scope.doneTrades[index].offererId){
        $scope.userReviewForm.reviewedId = $scope.doneTrades[index].interestedId;
      }else{
        $scope.userReviewForm.reviewedId = $scope.doneTrades[index].offererId;
      }

      var user = Restangular.one('users', $routeParams.id);
      user.all('reviews').post($scope.userReviewForm).then(function(data){
        $('#reviewModal').modal('hide');
        $scope.doneTrades[index].rating = $scope.userReviewForm.rating;
      }, function(response){
          toastr.error($translate.instant('userpage_toast_createreview_error'),$translate.instant('error_error'));
      });
    }

    var userLikeId = undefined;

    if($scope.loggedUserId == null || $scope.loggedUserId == $routeParams.id){
      $scope.alreadyLiked = false;
    }else {
      Restangular.one('users', $routeParams.id).one('likes').get().then(function(data){
        userLikeId = data.userLikeId;
        $scope.alreadyLiked = true;
      }, function(response){
        $scope.alreadyLiked = false;
      })
    }

    $scope.like = function(){
      Restangular.one('users', $routeParams.id).one('likes').post().then(function(data){
        $scope.alreadyLiked = true;
        userLikeId = data.userLikeId;
        toastr.info($translate.instant('userpage_like_user', {'user': $scope.user.username}));
      });
    }

    $scope.unlike = function(){
      Restangular.one('users/likes', userLikeId).remove().then(function(data){
        $scope.alreadyLiked = false;
        toastr.info($translate.instant('userpage_unlike_user', {'user': $scope.user.username}));
      });
    }

      $scope.unlikeOther = function(row,col){
          var selectedLike = $scope.userLikes[row][col];
          Restangular.one('users/likes', selectedLike.userLikeId).remove().then(function(data){
              $scope.loadUserLikes();
              toastr.info($translate.instant('userpage_unlike_user', {'user': selectedLike.likedUsername}));
          });
      };

    $scope.unfav = function(userLikes, i){
      var ulid = userLikes[i].userLikeId;
      Restangular.one('users/likes', ulid).remove().then(function(data){
        $scope.userLikes.splice(i,1);
      });
    }

    $scope.uninterest = function(row,col){
      var selectedProductInterest = $scope.productInterests[row][col];
      var piid = selectedProductInterest.productInterestId;
      Restangular.one('products/interests', piid).remove().then(function(data){
        $scope.loadProductInterests();
        toastr.info($translate.instant('userpage_uninterest', {'game': selectedProductInterest.prodName}));
      });
    }

    $scope.selectIconModal = function(num){
      $("#iconModal" + $scope.newIconForm.iconId).css({'border-width':'0px'});
      $scope.newIconForm.iconId = num;
      $("#iconModal" + $scope.newIconForm.iconId).css({'border-color':'black', 'border-style': 'solid', 'border-width':'5px'});

    }

    $scope.changeIcon = function(){
      $("#exampleModalLong").modal('hide');
      if($scope.user.iconId != $scope.newIconForm.iconId){
        Restangular.one('users', $routeParams.id).one('icon').customPUT($scope.newIconForm).then(function(data){
          $scope.user.iconId = $scope.newIconForm.iconId;
        }, function(response){
          toastr.error($translate.instant('userpage_toast_iconchange_error'),$translate.instant('error_error'));

        });

      }
    }

    angular.element(document).ready(function(){
        var loc = window.location.href;
        loc = loc.split("?")[1];
        switch (loc){
            case "offers":
                $("#tabOffers").click();
                break;
            case "tradeoffers":
                $("#tabTradeOffers").click();
                break;
            case "history":
                $("#tabHistory").click();
                break;
            case "settings":
                $("#tabSettings").click();
                break;
            case "traderequests":
                $("#tabTradeRequests").click();
                break;
        }
    });


    // Settings
    $scope.settings = null;
    $scope.loadingSettings = true;
    $scope.loadSettings = function(){
      Restangular.one('users/settings').get().then(function(data) {
        $scope.settings = data.plain();
        $scope.loadingSettings = false;
        returnToOriginalSettings();
      }, function (data) {
        $scope.loadingSettings = false;
      });
    }

    $scope.editSettings = function(){
      enableCheckboxes();
      document.getElementById('applyCancelRow').style.display = "";
      document.getElementById('editRow').style.display = "none";
    }

    $scope.cancelEdit = function(){
      returnToOriginalSettings();
      disableCheckboxes();
      document.getElementById('applyCancelRow').style.display = "none";
      document.getElementById('editRow').style.display = "";
    }

    $scope.applySettingChanges = function(){
      $scope.settings.acceptedMail = document.getElementById('accepted').checked;
      $scope.settings.cancelledMail = document.getElementById('canceled').checked;
      $scope.settings.completedMail = document.getElementById('completed').checked;
      $scope.settings.newTradeMail = document.getElementById('newtrade').checked;
      $scope.settings.rejectedMail = document.getElementById('rejected').checked;

      Restangular.one('users/settings').customPUT($scope.settings).then(function(data){
        toastr.success($translate.instant('userpage_settings_toast'));
      });

      disableCheckboxes();
      document.getElementById('applyCancelRow').style.display = "none";
      document.getElementById('editRow').style.display = "";
    }

    function enableCheckboxes(){
      document.getElementById('accepted').disabled = "";
      document.getElementById('canceled').disabled = "";
      document.getElementById('completed').disabled = "";
      document.getElementById('newtrade').disabled = "";
      document.getElementById('rejected').disabled = "";
    }

    function disableCheckboxes(){
      document.getElementById('accepted').disabled = "disabled";
      document.getElementById('canceled').disabled = "disabled";
      document.getElementById('completed').disabled = "disabled";
      document.getElementById('newtrade').disabled = "disabled";
      document.getElementById('rejected').disabled = "disabled";
    }

    function returnToOriginalSettings(){
      document.getElementById('accepted').checked = $scope.settings.acceptedMail;
      document.getElementById('canceled').checked = $scope.settings.cancelledMail;
      document.getElementById('completed').checked = $scope.settings.completedMail;
      document.getElementById('newtrade').checked = $scope.settings.newTradeMail;
      document.getElementById('rejected').checked = $scope.settings.rejectedMail;
    }



  });



});
