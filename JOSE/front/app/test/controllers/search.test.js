'use strict';

define(['trademygame',
'angular-mocks',
'controllers/search'],
function() {
	describe('search', function() {
		beforeEach(module('trademygame'));

		var $controller, $rootScope, $window, controller, $routeParams, $httpBackend,
        Restangular, toastr, $translate, authServiceSpy, scope;

    beforeEach(function() {
			module(function($provide) {
		        $provide.value('$window', {
		            location: {href: ''}
		        });
		    });
		});

    var defaultResults = [
      {"available":true,"avgRating":5.0,"esrb":"MATURE17","genre":"RPG","mpsp":"SINGLEPLAYER","name":"Persona 5","nrRevs":1,"platform":{"company":"Sony","name":"PS4",
    "platformId":12},"prodId":59},{"available":true,
    "avgRating":4.5,"esrb":"TEEN","genre":"RPG","mpsp":"SINGLEPLAYER","name":"Persona 4 Golden","nrRevs":2,"platform":{"company":"Sony","name":"PS Vita","platformId":14},"prodId":3},{"available":true,"avgRating":4.5,
    "esrb":"EVERYONE","genre":"RPG","mpsp":"SINGLEPLAYER","name":"Pokemon Gold","nrRevs":6,"platform":{"company":"Nintendo","name":"GBC","platformId":5},"prodId":2},{"available":false,"avgRating":4.5,"esrb":"MATURE17",
    "genre":"ACTION","mpsp":"SINGLEPLAYER","name":"Yakuza 0","nrRevs":2,"platform":{"company":"Sony","name":"PS4","platformId":12},"prodId":65},{"available":true,"avgRating":4.3333335,"esrb":"EVERYONE","genre":"RACING",
    "mpsp":"BOTH","name":"Mario Kart 8 Deluxe","nrRevs":3,"platform":{"company":"Nintendo","name":"Switch","platformId":4257},"prodId":64},{"available":false,"avgRating":4.285714,"esrb":"EVERYONE","genre":"ADVENTURE",
    "mpsp":"SINGLEPLAYER","name":"Cory in the House","nrRevs":7,"platform":{"company":"Nintendo","name":"NDS","platformId":4},"prodId":1},{"available":true,"avgRating":4.25,"esrb":"ADULTS","genre":"SPORT",
    "mpsp":"SINGLEPLAYER","name":"Barto Tennis 2017","nrRevs":4,"platform":{"company":"Sony","name":"PS3","platformId":11},"prodId":43},{"available":false,"avgRating":4.0,"esrb":"TEEN","genre":"FIGHTING",
    "mpsp":"BOTH","name":"Dragon Ball Z: Budokai Tenkaichi 2","nrRevs":3,"platform":{"company":"Sony","name":"PS2","platformId":10},"prodId":52},{"available":true,"avgRating":4.0,"esrb":"EVERYONE","genre":"RPG",
    "mpsp":"SINGLEPLAYER","name":"Monster Rancher 2","nrRevs":1,"platform":{"company":"Sony","name":"PS1","platformId":9},"prodId":15},{"available":true,"avgRating":4.0,"esrb":"MATURE17","genre":"RPG","mpsp":"SINGLEPLAYER",
    "name":"Shin Megami Tensei IV","nrRevs":1,"platform":{"company":"Nintendo","name":"3DS","platformId":3},"prodId":10},{"available":true,"avgRating":4.0,"esrb":"EVERYONE","genre":"SPORT","mpsp":"BOTH","name":"Top Spin 2",
    "nrRevs":1,"platform":{"company":"Sony","name":"PS2","platformId":10},"prodId":23},{"available":false,"avgRating":3.3333333,"esrb":"EVERYONE10","genre":"VISUAL_NOVEL","mpsp":"SINGLEPLAYER","name":"Apollo Justice: Ace Attorney",
    "nrRevs":3,"platform":{"company":"Nintendo","name":"NDS","platformId":4},"prodId":16},{"available":true,"avgRating":3.2941177,"esrb":"TEEN","genre":"RPG","mpsp":"SINGLEPLAYER","name":"Persona 3 FES","nrRevs":85,
    "platform":{"company":"Sony","name":"PS2","platformId":10},"prodId":11},{"available":false,"avgRating":3.0,"esrb":"MATURE17","genre":"PUZZLE","mpsp":"SINGLEPLAYER","name":"Catherine",
    "nrRevs":1,"platform":{"company":"Sony","name":"PS3","platformId":11},"prodId":36},{"available":true,"avgRating":3.0,"esrb":"EVERYONE10","genre":"STRATEGY","mpsp":"SINGLEPLAYER","name":"Pikmin","nrRevs":1,
    "platform":{"company":"Nintendo","name":"GameCube","platformId":7},"prodId":18}];

    var platforms = [{"company":"Nintendo","name":"Wii","platformId":1},{"company":"Nintendo","name":"Wii U","platformId":2},{"company":"Nintendo","name":"3DS","platformId":3},{"company":"Nintendo","name":"NDS","platformId":4},{"company":"Nintendo","name":"GBC","platformId":5},{"company":"Nintendo","name":"GBA","platformId":6},{"company":"Nintendo","name":"GameCube","platformId":7},{"company":"Nintendo","name":"N64","platformId":8},{"company":"Sony","name":"PS1","platformId":9},{"company":"Sony","name":"PS2","platformId":10},{"company":"Sony","name":"PS3","platformId":11},{"company":"Sony","name":"PS4","platformId":12},{"company":"Sony","name":"PSP","platformId":13},{"company":"Sony","name":"PS Vita","platformId":14},{"company":"Microsoft","name":"XBox One","platformId":15},{"company":"Microsoft","name":"XBox 360","platformId":16},{"company":"Microsoft","name":"XBox","platformId":17},{"company":"PC","name":"PC","platformId":18},{"company":"Sega","name":"Sega Genesis","platformId":19},{"company":"Unknown","name":"Family Game","platformId":20},{"company":"Nintendo","name":"Switch","platformId":4257},{"company":"Nintendo","name":"NES","platformId":129},{"company":"Nintendo","name":"SNES","platformId":130}];

    var esrbs = [{"text":"EARLYCHILDHOOD"},{"text":"EVERYONE"},{"text":"EVERYONE10"},{"text":"TEEN"},{"text":"MATURE17"},{"text":"ADULTS"}];

    var genres = [{"text":"ACTION"},{"text":"ADVENTURE"},{"text":"EDUCATIONAL"},{"text":"FIGHTING"},{"text":"FPS"},{"text":"HORROR"},{"text":"LIFE_SIMULATOR"},{"text":"MMO"},{"text":"MUSIC"},{"text":"PARTY"},{"text":"PUZZLE"},{"text":"RACING"},{"text":"RPG"},{"text":"SPORT"},{"text":"STRATEGY"},{"text":"THREE_D_PLATFORM"},{"text":"TWO_D_PLATFORM"},{"text":"VISUAL_NOVEL"}];

    var mpsps = [{"text":"SINGLEPLAYER"},{"text":"MULTIPLAYER"},{"text":"BOTH"}];

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

      $routeParams.page = 1;
      $routeParams.name = "";

      $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/products/amount?name=&page=1')).respond(function(){
        return [200, {"amount":61,"pagesAmount":5}];
      });
      $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/products?name=&page=1')).respond(function(){
        return [200, defaultResults];
      });
      $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/platforms')).respond(function(){
        return [200, platforms];
      });
      $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/products/esrb')).respond(function(){
        return [200, esrbs];
      });
      $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/products/genre')).respond(function(){
        return [200, genres];
      });
      $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/products/mpsp')).respond(function(){
        return [200, mpsps];
      });

      $httpBackend.expectGET(Restangular.configuration.baseUrl.concat('/products/amount?name=&page=1'));
      $httpBackend.expectGET(Restangular.configuration.baseUrl.concat('/products?name=&page=1'));
      $httpBackend.expectGET(Restangular.configuration.baseUrl.concat('/platforms'));
      $httpBackend.expectGET(Restangular.configuration.baseUrl.concat('/products/esrb'));
      $httpBackend.expectGET(Restangular.configuration.baseUrl.concat('/products/genre'));
      $httpBackend.expectGET(Restangular.configuration.baseUrl.concat('/products/mpsp'));
      controller = $controller('search', {'$scope': scope});

    }));

    describe('loading default results', function(){
      beforeEach(function(){
        $httpBackend.flush();
      });

      it('should correctly load the default results matrix', function(){
        var matrix = [];
        var i,k;

        for(i=0, k=-1; i<defaultResults.length;i++){
            if (i % 3 === 0) {
                k++;
                matrix[k] = [];
            }
            matrix[k].push(defaultResults[i]);
        }
        expect(scope.products).toEqual(matrix);
      });

      it('should correctly load default pagination', function(){
        expect(scope.pagination).toEqual({"pagesAmount":5,"prodAmount":61,"maxSizePage":5});
      });

    });

    describe('loading results filtered by platform', function(){
      var ndsResults = [{"available":false,"avgRating":4.285714,"esrb":"EVERYONE","genre":"ADVENTURE","mpsp":"SINGLEPLAYER","name":"Cory in the House","nrRevs":7,"platform":{"company":"Nintendo","name":"NDS","platformId":4},"prodId":1},{"available":false,"avgRating":3.3333333,"esrb":"EVERYONE10","genre":"VISUAL_NOVEL","mpsp":"SINGLEPLAYER","name":"Apollo Justice: Ace Attorney","nrRevs":3,"platform":{"company":"Nintendo","name":"NDS","platformId":4},"prodId":16},{"available":false,"avgRating":0.0,"esrb":"TEEN","genre":"RPG","mpsp":"SINGLEPLAYER","name":"The World Ends With You","nrRevs":0,"platform":{"company":"Nintendo","name":"NDS","platformId":4},"prodId":24}];
      var ndsPagination = {"amount":3,"pagesAmount":1};
      beforeEach(function(){
        $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/products?page=1&platform=NDS')).respond(function(){
          return [200, ndsResults];
        });
        $httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/products/amount?page=1&platform=NDS')).respond(function(){
          return [200, ndsPagination];
        });
        controller = $controller('search', {'$scope': scope, $routeParams: {platform: 'NDS'}});
        $httpBackend.flush();
      });

      it('should correctly load the filtered results matrix', function(){
        var matrix = [];
        var i,k;

        for(i=0, k=-1; i<ndsResults.length;i++){
            if (i % 3 === 0) {
                k++;
                matrix[k] = [];
            }
            matrix[k].push(ndsResults[i]);
        }
        expect(scope.products).toEqual(matrix);
      });

      it('should correctly load  filtered pagination', function(){
        expect(scope.pagination).toEqual({"pagesAmount":1,"prodAmount":3,"maxSizePage":5});
      });

      it('should correctly load the filters', function(){
        expect(scope.name).toEqual(undefined);
        expect(scope.searchPlatform).toEqual("NDS");
        expect(scope.searchGenre).toEqual(undefined);
        expect(scope.searchEsrb).toEqual(undefined);
        expect(scope.searchMpsp).toEqual(undefined);
      })

    });

    describe('filter results', function(){
      beforeEach(function(){
        scope.searchPlatform = 'NDS';
        scope.performSearch();
        $httpBackend.flush();
      });

      it('should redirect correctly to the search with the corresponding filters', function(){
        expect($window.location.href).toEqual('/#/search?page=1&name=&platform=NDS');
      });

    })

    describe('remove filters', function(){
      beforeEach(function(){
        scope.searchPlatform = 'NDS';
        scope.performSearch();
        scope.removeFilters();
        $httpBackend.flush();
      });

      it('should redirect correctly to the search without filters', function(){
        expect($window.location.href).toEqual('/#/search?page=1&name=');
      });

    })

	});
});
