'use strict';

define(['trademygame',
'angular-mocks',
'services/authService'],
function() {
	describe('authService', function() {
		beforeEach(module('trademygame'));

		var $httpBackend, $rootScope, scope, authServiceService;

		var token = 'eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyMDQiLCJzdWIiOiJmcmFuIiwiZXhwIjoxNTAxOTU2NzgwLCJhdWQiOiIwNWVjMzI2Mi1mODM3LTRjZTEtODhkYi1hZGE1NWFiZDNmOTgifQ.r51jk4x5JV1LP5Kz7pao2LVOejvvKNGFOvs24hXBKNSwJH84CyYrw3flHikcL3pY6MrIJOJf22LXweXuogHQFw';


		beforeEach(inject(function(_$httpBackend_, _$rootScope_, authService) {
			$httpBackend =  _$httpBackend_;
			$rootScope = _$rootScope_;
			scope = $rootScope.$new();
			authServiceService = authService;
		}));

		describe('when user is not logged in', function() {
			beforeEach(function() {
				spyOn(localStorage, 'getItem').and.callFake(function(item){
						if(item == 'token'){
							return null;
						}
				});
			});

			describe('and isLoggedIn call is made', function() {
				var result;
				beforeEach(function() {
					result = authServiceService.isLoggedIn();
				});
				it('should return false', function() {
					expect(result).toBe(false);
				});
			});

			describe('and loggedUser call is made', function() {
				var result;
				beforeEach(function() {
					result = authServiceService.loggedUser();
				});
				it('should return null', function() {
					expect(result).toBe(null);
				});
			});

			describe('and loggedId call is made', function() {
				var result;
				beforeEach(function() {
					result = authServiceService.loggedId();
				});
				it('should return null', function() {
					expect(result).toBe(null);
				});
			});
		});



		describe('when user token has expired', function() {
			beforeEach(function() {
				spyOn(localStorage, 'getItem').and.callFake(function(item){
					if(item == 'token'){
						return token;
					}
				});

				var claims = atob(token.split(".")[1]);
		        var json = JSON.parse(claims);

		        var exp = json["exp"];
		        exp = exp*1000;

		        var expDate = new Date(exp);

				var baseTime = new Date(2018, 9, 23);
				spyOn(window, 'Date').and.callFake(function(exp) {
					if(exp === undefined){
						return baseTime;
					}
					return expDate;
				});
			});

			describe('and isLoggedIn call is made', function() {
				var result;
				beforeEach(function() {
					result = authServiceService.isLoggedIn();
				});
				it('should return false', function() {
					expect(result).toBe(false);
				});
			});

			describe('and loggedUser call is made', function() {
				var result;
				beforeEach(function() {
					result = authServiceService.loggedUser();
				});
				it('should return the username', function() {
					expect(result).toBe('fran');
				});
			});

			describe('and loggedId call is made', function() {
				var result;
				beforeEach(function() {
					result = authServiceService.loggedId();
				});
				it('should return the user id', function() {
					expect(result).toBe('204');
				});
			});
			
		});

		describe('when user is logged in', function() {
			beforeEach(function() {
				spyOn(localStorage, 'getItem').and.callFake(function(item){
					if(item == 'token'){
						return token;
					}
				});

				var claims = atob(token.split(".")[1]);
		        var json = JSON.parse(claims);

		        var exp = json["exp"];
		        exp = exp*1000;

		        var expDate = new Date(exp);

				var baseTime = new Date(2013, 9, 23);
				spyOn(window, 'Date').and.callFake(function(exp) {
					if(exp === undefined){
						return baseTime;
					}
					return expDate;
				});
			});

			describe('and isLoggedIn call is made', function() {
				var result;
				beforeEach(function() {
					result = authServiceService.isLoggedIn();
				});
				it('should return true', function() {
					expect(result).toBe(true);
				});
			});

			describe('and loggedUser call is made', function() {
				var result;
				beforeEach(function() {
					result = authServiceService.loggedUser();
				});
				it('should return the username', function() {
					expect(result).toBe('fran');
				});
			});

			describe('and loggedId call is made', function() {
				var result;
				beforeEach(function() {
					result = authServiceService.loggedId();
				});
				it('should return the user id', function() {
					expect(result).toBe('204');
				});
			});
		});

	});

});
