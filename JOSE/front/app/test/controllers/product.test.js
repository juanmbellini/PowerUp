'use strict';

define(['trademygame',
'angular-mocks',
'controllers/product'],
function() {
	describe('product', function() {
		beforeEach(module('trademygame'));

    var expectedParams = {
      id: 10
    };


		var $controller, $rootScope, $window, controller, $routeParams, $httpBackend,
        Restangular, toastr, $translate, authServiceSpy, scope;

    var prod = {
      "avgRating":4.0,
      "esrb":"MATURE17",
      "genre":"RPG",
      "mpsp":"SINGLEPLAYER",
      "name":"Shin Megami Tensei IV",
      "nrRevs":1,
      "platform":{
        "company":"Nintendo",
        "name":"3DS",
        "platformId":3
      },
      "prodId":10
    };

    var reviews = [
      {
        "date":"2016-10-15T00:00:00-03:00",
        "iconId":1,
        "prodId":10,
        "rating":4,
        "review":"me cop mucho",
        "title":"Re turbio",
        "username":"anonymous",
        "writerId":9
      }
    ];

    var newRev = {
      "date":"2016-10-15T00:00:00-03:00",
      "iconId":2,
      "prodId":10,
      "rating":1,
      "review":"nueva review",
      "title":"nuevo titulo",
      "username":"francisco",
      "writerId":8
    };

    var offers = [
      {
        "available":true,
        "description":"vdvdsvdvdvds peolita",
        "nrRevs":1,
        "offerId":108,
        "offererIconId":8,
        "platform":"3DS",
        "prodId":10,
        "prodName":"Shin Megami Tensei IV",
        "userId":197,
        "userRating":5.0,
        "username":"leogarcia"
      }
    ];

		beforeEach(inject(function(_$controller_, _$routeParams_, _$httpBackend_, _$rootScope_, _Restangular_, _$window_, _$translate_, _toastr_, authService) {

			authServiceSpy = authService;
			$controller = _$controller_;
      $routeParams = _$routeParams_;
			$rootScope = _$rootScope_;
      scope = $rootScope.$new();
			$translate = _$translate_;
			$window = _$window_;
      $httpBackend = _$httpBackend_;
      Restangular = _Restangular_;
			toastr = _toastr_;

      $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/products/10')).respond(function(){
        return [200, prod];
      });
      $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/products/10/reviews')).respond(function(){
        return [200, reviews];
      });
      $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/products/10/offers')).respond(function(){
        return [200, offers];
      });
      $httpBackend.expectGET(Restangular.configuration.baseUrl.concat('/products/10'));
      $httpBackend.expectGET(Restangular.configuration.baseUrl.concat('/products/10/reviews'));
      $httpBackend.expectGET(Restangular.configuration.baseUrl.concat('/products/10/offers'));

      controller = $controller('product', {$scope: scope, $routeParams: expectedParams});

    }));


    describe('updating product rating', function(){
      beforeEach(function(){
        $httpBackend.flush();
        scope.updateAvgRating(2);
      });

      it('should exist', function() {
        expect(scope.updateAvgRating).toBeDefined();
      });

      it('should correctly update product average rating', function(){
        expect(scope.prod.avgRating).toEqual(3);
      });

      it('should correctly increase nrRevs', function(){
        expect(scope.prod.nrRevs).toEqual(2);
      });

    });

    describe('writing a new review', function(){
      beforeEach(function(){
        $httpBackend.when('POST', Restangular.configuration.baseUrl.concat('/products/10/reviews')).respond(function(){
          scope.prodReviewForm.userId = newRev.writerId;
          scope.prodReviewForm.title = newRev.title;
          scope.prodReviewForm.review = newRev.review;
          scope.prodReviewForm.prodId = newRev.prodId;
          scope.prodReviewForm.rating = newRev.rating;
          return [200, newRev];
        });
        $httpBackend.expectPOST(Restangular.configuration.baseUrl.concat('/products/10/reviews'));
        scope.newProductReview();
        $httpBackend.flush();
      });

      it('should exist', function() {
        expect(scope.newProductReview).toBeDefined();
      });

      it('should correctly increase the amount of reviews', function(){
        expect(scope.reviews.length).toEqual(2);
      });

      it('should correctly update product average rating', function(){
        expect(scope.prod.avgRating).toEqual(2.5);
      });

      it('should correctly increase nrRevs', function(){
        expect(scope.prod.nrRevs).toEqual(2);
      });

      it('visibility should be none when already reviewed', function(){
        expect($('.post-review-box').is(':visible')).toBe(false);
      });
    });



	});
});
