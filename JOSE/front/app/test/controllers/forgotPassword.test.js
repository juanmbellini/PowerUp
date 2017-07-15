'use strict';

define(['trademygame',
'services/authService',
'angular-mocks',
'controllers/forgotPassword'],
function() {
	describe('forgotPassword', function() {
		beforeEach(module('trademygame'));

		var $controller, $rootScope, controller,
        Restangular, toastr, $translate, $httpBackend,
		authServiceSpy,
		scope, $window, $location;

		beforeEach(function() {
			module(function($provide) {
		        $provide.value('$window', {
		            location: {href: ''}
		        });
		    });
		});


		beforeEach(inject(function(_$controller_, _$httpBackend_, _$rootScope_, _Restangular_, _$translate_, _toastr_, authService, _$window_, _$location_) {

			$window = _$window_;
			$location = _$location_;
			authServiceSpy = authService;
			$controller = _$controller_;
			$rootScope = _$rootScope_;
			scope = $rootScope.$new();
			$translate = _$translate_;
			$httpBackend = _$httpBackend_;
			Restangular = _Restangular_;
			toastr = _toastr_;
			controller = $controller('forgotPassword', {'$scope': scope, 'toastr': toastr,
															'Restangular': Restangular, '$window': $window, '$translate': $translate});
			

			

			$rootScope.$apply();
		}));
		afterEach(function() {
		    $httpBackend.verifyNoOutstandingExpectation();
		    $httpBackend.verifyNoOutstandingRequest();
		});

		describe('asking for a password recovery', function() {


			describe('with existing email', function() {

				beforeEach(inject(function($httpBackend) {

					spyOn(toastr, 'success');



					$httpBackend.whenPUT(Restangular.configuration.baseUrl.concat('/users/forgotPassword?email=mail@mailaddress.com')).respond(function () {
					  return [200, 0, ''];
					});
				}));

				


				it('should redirect to /#/ and show success toastr', function() {

					// Set mail
					scope.forgotPassForm = {
						"email": 'mail@mailaddress.com'
					};

					// Call forgotPass
					$httpBackend.expectPUT(Restangular.configuration.baseUrl.concat('/users/forgotPassword?email=mail@mailaddress.com'));
					scope.forgotPass();
					$httpBackend.flush();
					expect(toastr.success).toHaveBeenCalled();
					expect($window.location.href).toEqual('/#/');
				});


			});


			describe('with nonexisting email', function() {

				beforeEach(inject(function($httpBackend) {
					$httpBackend.whenPUT(Restangular.configuration.baseUrl.concat('/users/forgotPassword?email=mail@mailaddress.com')).respond(function () {
					  return [404, 0, ''];
					});
				}));


				it('should set scope variable emailExists to true', function() {

					// Set mail
					scope.forgotPassForm = {
						"email": 'mail@mailaddress.com'
					};

					// Call forgotPass
					$httpBackend.expectPUT(Restangular.configuration.baseUrl.concat('/users/forgotPassword?email=mail@mailaddress.com'));
					scope.forgotPass();
					$httpBackend.flush();
					expect(scope.emailExists).toEqual(true);
				});


			});

			
		});
	});
});
