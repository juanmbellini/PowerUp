requirejs(['ratings']);

define(['trademygame', '../services/authService'], function(trademygame) {

    'use strict';
    trademygame.controller('product', function($scope, $routeParams, Restangular, authService, toastr, $translate, $window) {

      $scope.loadingProduct = true;

      this.getProduct = function(){
        Restangular.one('products', $routeParams.id).get().then(function(data) {
          $scope.prod = data.plain();
          $scope.loadingProduct = false;

        }, function (data) {
          $scope.loadingProduct = false;

        });
      }
      this.getProduct();

      $scope.loggedId = authService.loggedId();

      $scope.reviews_size = 5;

      $scope.offerProduct = function(){
          var location = '/#/newoffer';
          var data = "?name=" + encodeURIComponent($scope.prod.name);
          data += "&platform=" + encodeURIComponent($scope.prod.platform.name);
          data += "&mpsp=" + encodeURIComponent($scope.prod.mpsp);
          data += "&esrb=" + encodeURIComponent($scope.prod.esrb);
          data += "&genre=" + encodeURIComponent($scope.prod.genre);

          location+=data;
          console.log(location);
          $window.location = location;

      };

      Restangular.one('products', $routeParams.id).getList('reviews').then(function(data) {
           $scope.reviews = data.plain();

           var i;
           for(i=0; i<$scope.reviews.length; i++){
             if($scope.reviews[i].writerId == $scope.loggedId){
               $scope.alreadyReviewed = true;
             }
           }
      });

      $scope.getTimes=function(n){
        return new Array(n);
      };

      $scope.offers_size = 5;

      Restangular.one('products', $routeParams.id).getList('offers').then(function(data) {
           $scope.offers = data.plain();
      });

      $scope.prodReviewForm = {
        "userId" : $scope.loggedId,
        "title": "",
        "review": "",
        "prodId": $routeParams.id,
        "rating": undefined
      };

      $scope.newProductReview = function(){
        //  /products/{prodId}/reviews
        $scope.alreadyReviewed = false;
        Restangular.one("products", $routeParams.id).all('reviews').post($scope.prodReviewForm).then(function(data){
          $scope.reviews.unshift(data.plain());
          if($scope.reviews.length >= 6){
            $scope.reviews.pop();
          }
          $('#post-review-box').css('visibility', 'hidden');
          $scope.updateAvgRating($scope.prodReviewForm.rating);

          $scope.prodReviewForm.title = "";
          $scope.prodReviewForm.review = "";
          $scope.prodReviewForm.rating = undefined;

		      $scope.alreadyReviewed = true;

        }, function(response){
            var errors = response.data.errors;
            for(var i = 0; i < errors.length; i++){
                if(errors[i].property == "newProductReview.arg1"){
                    $scope.alreadyReviewed = true;
                }
            }
        });

  		};

      $scope.updateAvgRating = function(rating){
        $scope.prod.avgRating *= ($scope.prod.nrRevs);
        $scope.prod.avgRating += rating;
        $scope.prod.nrRevs += 1;
        $scope.prod.avgRating /= $scope.prod.nrRevs;
      }

      var productInterestId = undefined;

      if($scope.loggedId == null){
        $scope.alreadyInterested = false;
      }else {
        Restangular.one('products', $routeParams.id).one('interests').get().then(function(data){
          productInterestId = data.productInterestId;
          $scope.alreadyInterested = true;
        }, function(response){
          $scope.alreadyInterested = false;
        })
      }

      $scope.interest = function(){
        Restangular.one('products', $routeParams.id).one('interests').post().then(function(data){
          $scope.alreadyInterested = true;
          productInterestId = data.productInterestId;
          toastr.info($translate.instant('prodpage_interest',{'game': $scope.prod.name}));
        });
      }

      $scope.uninterest = function(){
        Restangular.one('products/interests', productInterestId).remove().then(function(data){
          $scope.alreadyInterested = false;
          toastr.info($translate.instant('prodpage_uninterest',{'game': $scope.prod.name}));
        });
      }

      $scope.displayform = function(){
        document.getElementById("post-review-box").style.display = "inline";
        document.getElementById("open-review-box").style.display = "none";
      }

      $scope.undisplayform = function(){
        document.getElementById("post-review-box").style.display = "none";
        document.getElementById("open-review-box").style.display = "inline";
      }

    });

});
