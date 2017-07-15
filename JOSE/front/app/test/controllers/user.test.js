'use strict';

define(['trademygame',
'angular-mocks',
'controllers/user'],
function() {
	describe('user', function() {
		beforeEach(module('trademygame'));

    var expectedParams = {
      id: 198
    };

		var $controller, $rootScope, $window, controller, $routeParams, $httpBackend,
        Restangular, toastr, $translate, authServiceSpy, scope;

    var user = {
      "avgRating":4.5,
      "iconId":6,
      "nrRevs":2,
      "userid":198,
      "username":"eltipodelaspapitas"
    };

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

      $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/users/198')).respond(function(){
        return [200, user];
      });
      $httpBackend.expectGET(Restangular.configuration.baseUrl.concat('/users/198'));

      controller = $controller('user', {$scope: scope, $routeParams: expectedParams});

    }));

    describe('loading user', function(){
      beforeEach(function(){
        $httpBackend.flush();
      });

      it('should correctly load the user', function(){
        expect(scope.user).toEqual(user);
      });

    });

    describe('user reviews tab', function(){
      var reviews = [{"date":"2017-07-04T00:00:00-03:00","rating":5,"review":"le puso papitas a TODO!","reviewedId":198,"title":"que capo es este chabon","tradeId":60,"writer":"testuser","writerIconId":3,"writerId":202},{"date":"2017-07-05T00:00:00-03:00","rating":4,"review":"ewvewvwevewvew","reviewedId":198,"title":"vfsvsv","tradeId":73,"writer":"leogarcia","writerIconId":8,"writerId":197}];

      beforeEach(function(){
        $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/users/198/reviews')).respond(function(){
          return [200, reviews];
        });
        scope.loadUserReviews();
        $httpBackend.flush();
      });

      it('should correctly load user reviews', function(){
        expect(scope.userReviews).toEqual(reviews);
      });

    });

    describe('user offers tab', function(){
      var offers = [{"available":true,"description":"khjghfgxdckj","nrRevs":2,"offerId":100,"offererIconId":6,"platform":"PS4","prodId":59,"prodName":"Persona 5","userId":198,"userRating":4.5,"username":"eltipodelaspapitas"},{"available":true,"description":"!!!!!!!!!!!!","nrRevs":2,"offerId":109,"offererIconId":6,"platform":"PS4","prodId":8,"prodName":"FIFA 17","userId":198,"userRating":4.5,"username":"eltipodelaspapitas"},{"available":true,"nrRevs":2,"offerId":110,"offererIconId":6,"platform":"NDS","prodId":1,"prodName":"Cory in the House","userId":198,"userRating":4.5,"username":"eltipodelaspapitas"}];
      var offersAfterDelete = [{"available":true,"description":"khjghfgxdckj","nrRevs":2,"offerId":100,"offererIconId":6,"platform":"PS4","prodId":59,"prodName":"Persona 5","userId":198,"userRating":4.5,"username":"eltipodelaspapitas"},{"available":true,"nrRevs":2,"offerId":110,"offererIconId":6,"platform":"NDS","prodId":1,"prodName":"Cory in the House","userId":198,"userRating":4.5,"username":"eltipodelaspapitas"}];

      beforeEach(function(){
        $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/offers?userId=198')).respond(function(){
          return [200, offers];
        });
        scope.loadUserOffers();
        $httpBackend.flush();
      });

      it('should correctly load user offers', function(){
        expect(scope.offers).toEqual(offers);
      });

      it('should correctly delete an offer', function(){
        $httpBackend.when('DELETE', Restangular.configuration.baseUrl.concat('/offers/109')).respond(function(){
          return [200];
        });
        scope.deleteOffer(1);
        $httpBackend.flush();
        expect(scope.offers).toEqual(offersAfterDelete);
      });

    });

    describe('favourite users tab', function(){
      var favUsers = [{"likedIconId":27,"likedId":201,"likedUsername":"FelixAyerza","likerId":198,"likerUsername":"eltipodelaspapitas","rating":0.0,"userLikeId":85},{"likedIconId":11,"likedId":199,"likedUsername":"josejose","likerId":198,"likerUsername":"eltipodelaspapitas","rating":2.0,"userLikeId":91},{"likedIconId":3,"likedId":202,"likedUsername":"testuser","likerId":198,"likerUsername":"eltipodelaspapitas","rating":0.0,"userLikeId":97},{"likedIconId":8,"likedId":197,"likedUsername":"leogarcia","likerId":198,"likerUsername":"eltipodelaspapitas","rating":5.0,"userLikeId":49}];
      var favUsersAfterDelete = [{"likedIconId":27,"likedId":201,"likedUsername":"FelixAyerza","likerId":198,"likerUsername":"eltipodelaspapitas","rating":0.0,"userLikeId":85},{"likedIconId":3,"likedId":202,"likedUsername":"testuser","likerId":198,"likerUsername":"eltipodelaspapitas","rating":0.0,"userLikeId":97},{"likedIconId":8,"likedId":197,"likedUsername":"leogarcia","likerId":198,"likerUsername":"eltipodelaspapitas","rating":5.0,"userLikeId":49}];

      beforeEach(function(){
        $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/users/likes/liker?likerid=198')).respond(function(){
          return [200, favUsers];
        });
        scope.loadUserLikes();
        $httpBackend.flush();
      });

      it('should correctly load favourite users matrix', function(){
        var matrix = [];
        var i,k;

        for(i=0, k=-1; i<favUsers.length; i++){
          if( i%4 ==0){
            k++;
            matrix[k]=[];
          }
          matrix[k].push(favUsers[i]);
        }
        expect(scope.userLikes).toEqual(matrix);
      });

      it('should correctly delete favourite user', function(){
        $httpBackend.when('DELETE', Restangular.configuration.baseUrl.concat('/users/likes/91')).respond(function(){
          return [200];
        });
        $httpBackend.expectGET(Restangular.configuration.baseUrl.concat('/users/likes/liker?likerid=198')).respond(200, favUsersAfterDelete);

        scope.unlikeOther(0,1);
        $httpBackend.flush();

        var matrix = [];
        var i,k;

        for(i=0, k=-1; i<favUsersAfterDelete.length; i++){
          if( i%4 ==0){
            k++;
            matrix[k]=[];
          }
          matrix[k].push(favUsersAfterDelete[i]);
        }
        expect(scope.userLikes).toEqual(matrix);
      });
    });

    describe('interested games tab', function(){
      var prodInterests = [{"esrb":"EVERYONE","genre":"RACING","mpsp":"BOTH","platform":"Switch","prodId":64,"prodName":"Mario Kart 8 Deluxe","productInterestId":159,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"EVERYONE","genre":"RPG","mpsp":"SINGLEPLAYER","platform":"PS1","prodId":15,"prodName":"Monster Rancher 2","productInterestId":92,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"ADULTS","genre":"SPORT","mpsp":"SINGLEPLAYER","platform":"PS3","prodId":43,"prodName":"Barto Tennis 2017","productInterestId":161,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"EVERYONE","genre":"ADVENTURE","mpsp":"SINGLEPLAYER","platform":"NDS","prodId":1,"prodName":"Cory in the House","productInterestId":162,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"MATURE17","genre":"RPG","mpsp":"SINGLEPLAYER","platform":"3DS","prodId":10,"prodName":"Shin Megami Tensei IV","productInterestId":163,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"EVERYONE","genre":"RPG","mpsp":"SINGLEPLAYER","platform":"GBC","prodId":25,"prodName":"Pokemon Red","productInterestId":165,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"TEEN","genre":"FIGHTING","mpsp":"BOTH","platform":"PS2","prodId":52,"prodName":"Dragon Ball Z: Budokai Tenkaichi 2","productInterestId":98,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"TEEN","genre":"RPG","mpsp":"SINGLEPLAYER","platform":"PS2","prodId":11,"prodName":"Persona 3 FES","productInterestId":168,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"MATURE17","genre":"ACTION","mpsp":"SINGLEPLAYER","platform":"PS4","prodId":65,"prodName":"Yakuza 0","productInterestId":169,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"MATURE17","genre":"PUZZLE","mpsp":"SINGLEPLAYER","platform":"PS3","prodId":36,"prodName":"Catherine","productInterestId":61,"userId":198,"username":"eltipodelaspapitas"}];
      var prodInterestsAfterDelete = [{"esrb":"EVERYONE","genre":"RACING","mpsp":"BOTH","platform":"Switch","prodId":64,"prodName":"Mario Kart 8 Deluxe","productInterestId":159,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"ADULTS","genre":"SPORT","mpsp":"SINGLEPLAYER","platform":"PS3","prodId":43,"prodName":"Barto Tennis 2017","productInterestId":161,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"EVERYONE","genre":"ADVENTURE","mpsp":"SINGLEPLAYER","platform":"NDS","prodId":1,"prodName":"Cory in the House","productInterestId":162,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"MATURE17","genre":"RPG","mpsp":"SINGLEPLAYER","platform":"3DS","prodId":10,"prodName":"Shin Megami Tensei IV","productInterestId":163,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"EVERYONE","genre":"RPG","mpsp":"SINGLEPLAYER","platform":"GBC","prodId":25,"prodName":"Pokemon Red","productInterestId":165,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"TEEN","genre":"FIGHTING","mpsp":"BOTH","platform":"PS2","prodId":52,"prodName":"Dragon Ball Z: Budokai Tenkaichi 2","productInterestId":98,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"TEEN","genre":"RPG","mpsp":"SINGLEPLAYER","platform":"PS2","prodId":11,"prodName":"Persona 3 FES","productInterestId":168,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"MATURE17","genre":"ACTION","mpsp":"SINGLEPLAYER","platform":"PS4","prodId":65,"prodName":"Yakuza 0","productInterestId":169,"userId":198,"username":"eltipodelaspapitas"},{"esrb":"MATURE17","genre":"PUZZLE","mpsp":"SINGLEPLAYER","platform":"PS3","prodId":36,"prodName":"Catherine","productInterestId":61,"userId":198,"username":"eltipodelaspapitas"}];

      beforeEach(function(){
        $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/products/interests/198')).respond(function(){
          return [200, prodInterests];
        });
        scope.loadProductInterests();
        $httpBackend.flush();
      });

      it('should correctly load product interests matrix', function(){
        var matrix = [];
        var i,k;

        for(i=0, k=-1; i<prodInterests.length; i++){
          if( i%4 ==0){
            k++;
            matrix[k]=[];
          }
          matrix[k].push(prodInterests[i]);
        }
        expect(scope.productInterests).toEqual(matrix);
      });

      it('should correctly delete product interest', function(){
        $httpBackend.when('DELETE', Restangular.configuration.baseUrl.concat('/products/interests/92')).respond(function(){
          return [200];
        });
        $httpBackend.expectGET(Restangular.configuration.baseUrl.concat('/products/interests/198')).respond(200, prodInterestsAfterDelete);

        scope.uninterest(0,1);
        $httpBackend.flush();

        var matrix = [];
        var i,k;

        for(i=0, k=-1; i<prodInterestsAfterDelete.length; i++){
          if( i%4 ==0){
            k++;
            matrix[k]=[];
          }
          matrix[k].push(prodInterestsAfterDelete[i]);
        }
        expect(scope.productInterests).toEqual(matrix);
      });
    });

    describe('settings tab', function(){
      var settings = {
        "acceptedMail":true,
        "cancelledMail":false,
        "completedMail":true,
        "newTradeMail":false,
        "rejectedMail":false
      };

      var newSettings = {
        "acceptedMail":true,
        "cancelledMail":true,
        "completedMail":true,
        "newTradeMail":true,
        "rejectedMail":true
      };

      beforeEach(function(){
        $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/users/settings')).respond(function(){
          return [200, settings];
        });

        spyOn(document, "getElementById").and.callFake(function(id) {
					if(id === 'accepted' || id === 'canceled' || id === 'completed' || id === 'newtrade' || id === 'rejected'){
						return {
              checked: true,
              disabled: "disabled"
            };
					} else if (id === 'applyCancelRow' || id === 'editRow'){
            return {
              style : {
                display : ""
              }
            };
          }
				});

        scope.loadSettings();
        $httpBackend.flush();
      });

      it('should correctly load settings', function(){
        expect(scope.settings).toEqual(settings);
      });

      it('should correctly apply changes', function(){
        scope.applySettingChanges();
        expect(scope.settings).toEqual(newSettings);
      });
    })

    describe('trades proposed by me tab', function(){
      var tradeProposals = [{"interestedId":198,"interestedOfferId":100,"interestedOfferName":"Persona 5","interestedUsername":"eltipodelaspapitas","lastStateChange":"2017-07-05T15:41:32.751-03:00","offererId":197,"offererOfferId":85,"offererOfferName":"Barto Tennis 2017","offererUsername":"leogarcia","state":"PENDING","tradeId":69},{"interestedEmail":"cdsvsvs@hotmail.com","interestedId":198,"interestedOfferId":106,"interestedOfferName":"Mario Golf","interestedPhone":"24132535466","interestedUsername":"eltipodelaspapitas","lastStateChange":"2017-07-05T15:58:57.736-03:00","offererEmail":"leogarcia@morrisey","offererId":197,"offererOfferId":77,"offererOfferName":"Arms","offererPhone":"32323443243","offererUsername":"leogarcia","state":"ACCEPTED","tradeId":72}];

      beforeEach(function(){

        $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/trades/interested/198')).respond(function(){
          return [200, tradeProposals];
        });
        scope.loadTradesByMe();
        $httpBackend.flush();
      });

      it('should correctly load trades proposed by me', function(){
        expect(scope.tradesByMe).toEqual(tradeProposals);
      });

    });

    describe('trades proposed to me tab', function(){
      var tradeProposals = [{"interestedId":204,"interestedOfferId":111,"interestedOfferName":"Catherine","interestedUsername":"fran","lastStateChange":"2017-07-08T17:17:01.258-03:00","offererId":198,"offererOfferId":100,"offererOfferName":"Persona 5","offererUsername":"eltipodelaspapitas","state":"PENDING","tradeId":74}];

      beforeEach(function(){

        $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/trades/offerer/198')).respond(function(){
          return [200, tradeProposals];
        });
        scope.loadTradesToMe();
        $httpBackend.flush();
      });

      it('should correctly load trades proposed by me', function(){
        expect(scope.tradesToMe).toEqual(tradeProposals);
      });

    });

    describe('history tab', function(){
      var trades = [{"interestedId":204,"interestedOfferId":111,"interestedOfferName":"Catherine","interestedUsername":"fran","lastStateChange":"2017-07-08T17:17:01.258-03:00","offererId":198,"offererOfferId":100,"offererOfferName":"Persona 5","offererUsername":"eltipodelaspapitas","state":"ACCEPTED_CANCELLED","tradeId":74}];

      beforeEach(function(){

        $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/trades/198/done')).respond(function(){
          return [200, trades];
        });
        scope.loadDoneTrades();
        $httpBackend.flush();
      });

      it('should correctly load historic trades', function(){
        expect(scope.doneTrades).toEqual(trades);
      });

    });



	});
});
