'use strict';

define(['trademygame',
'services/authService',
'angular-mocks',
'controllers/OfferCtrl'],
function() {
	describe('OfferCtrl', function() {
		beforeEach(module('trademygame'));

		var $controller, $httpBackend, $rootScope,
			Restangular, authServiceSpy, $routeParams,
			$scope, controller;

		var prodId = 11;

		var offer = {
			"available":true,
			"description":"nr4",
			"nrRevs":0,
			"offerId":10,
			"offererIconId":1,
			"platform":"PS2",
			"prodId":11,
			"prodName":"Persona 3 FES",
			"userId":1,
			"userRating":0.0,
			"username":"incognito@hotmail.com"
		};

		var product =
		{
			"avgRating":3.2941177,
			"esrb":"TEEN",
			"genre":"RPG",
			"mpsp":"SINGLEPLAYER",
			"name":"Persona 3 FES",
			"nrRevs":85,
			"platform":
			{
				"company":"Sony",
				"name":"PS2",
				"platformId":10
			},
			"prodId":11
		};

		var questions =
		[
			{
				"id":100,
				"offerId":10,
				"question":"asi esta bien jose?",
				"questionDate":"2017-07-07T00:00:00-03:00",
				"writer":"leogarcia",
				"writerIconId":8,
				"writerId":197
			}
		];

		var questionWithAnswer =
		{
			"answer":"vfvdvsdd",
			"id":100,
			"offerId":10,
			"question":"asi esta bien jose?",
			"questionDate":"2017-07-07T00:00:00-03:00",
			"writer":"leogarcia",
			"writerIconId":8,
			"writerId":197,
			"answerDate":"2017-07-07T00:00:00-03:00"
		};



		beforeEach(inject(function(_$controller_, _$httpBackend_, _$rootScope_, _Restangular_, authService, _$routeParams_) {

			$httpBackend = _$httpBackend_;
			$controller = _$controller_;
			$rootScope = _$rootScope_;
			$scope = $rootScope.$new();
			$httpBackend = _$httpBackend_;
			Restangular = _Restangular_;
			$routeParams = _$routeParams_;
			authServiceSpy = authService;

			$routeParams.id = 10;

			$httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/offers/', $routeParams.id)).respond(function(){
				return [200, offer];
			});



			$httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/offers/', $routeParams.id, '/questions')).respond(function(){
				return [200, questions];
			});

			$httpBackend.when('GET', Restangular.configuration.baseUrl.concat('/products/', prodId)).respond(function(){
				return [200, product];
			});


			$httpBackend.expectGET(Restangular.configuration.baseUrl.concat('/offers/10'));
			$httpBackend.expectGET(Restangular.configuration.baseUrl.concat('/products/', prodId));


			controller = $controller('OfferCtrl', {'$scope': $scope, '$routeParams': $routeParams,
															'Restangular': Restangular, 'authServicce': authService});

			$httpBackend.flush();

		}));


		describe('writing a new question', function() {

			var questionSize;

			beforeEach(function() {

				$scope.questionForm = {
        			"question": 'lalalala'
      			};

				questionSize = $scope.questions.length;

				var question =
				{
					"id":101,
					"offerId":10,
					"question":$scope.questionForm,
					"questionDate":"2017-07-07T00:00:00-03:00",
					"writer":"leogarcia",
					"writerIconId":8,
					"writerId":197
				}

				$httpBackend.when('POST', Restangular.configuration.baseUrl.concat('/offers/', $routeParams.id, '/questions')).respond(function(){
					return [200, question];
				});
				$httpBackend.expectPOST(Restangular.configuration.baseUrl.concat('/offers/', $routeParams.id, '/questions'));
				$scope.newQuestion();
				$httpBackend.flush();
			});

			it('should set questionForm.question to undefined', function() {
				expect($scope.questionForm.question).toBeUndefined();
			});

			it('should add a question to questions', function() {
				expect($scope.questions.length).toBe(questionSize+1);
			});
		});

		describe('answering a question', function() {
			var questionNumber = 0;
			beforeEach(function() {

				spyOn(document, "getElementById").and.callFake(function(id) {
					if(id === 'answer'.concat(questionNumber)){
						return questionWithAnswer.answer;
					}
					else if(id == 'questionIdForm'.concat(questionNumber)){
						return 'q'.concat(questionNumber);
					}

				});


				$httpBackend.when('PUT', Restangular.configuration.baseUrl.concat('/offers/', $routeParams.id, '/questions')).respond(function(){
					return [200, questionWithAnswer];
				});
				$httpBackend.expectPUT(Restangular.configuration.baseUrl.concat('/offers/', $routeParams.id, '/questions'));
				$scope.answerQuestion(questionNumber);
				$httpBackend.flush();
			});

			it('should update the question', function() {
				expect($scope.questions[questionNumber].answer).toEqual(questionWithAnswer.answer);
			});
		});

		describe('deleting a question', function() {
			var questionNumber = 0;
			var questionSize;
			beforeEach(function() {
				questionSize = $scope.questions.length;
				spyOn(document, "getElementById").and.callFake(function(id) {
					if(id == 'q'.concat(questionNumber)){
						return $scope.questions[questionNumber];
					}
				});

				$httpBackend.when('DELETE', Restangular.configuration.baseUrl.concat('/offers/questions/', $scope.questions[questionNumber].id)).respond(function(){
					return [200];
				});

				$httpBackend.when('DELETE', Restangular.configuration.baseUrl.concat('/offers/', $scope.offer.offerId,'/questions/', $scope.questions[questionNumber].id)).respond(function(){
					return [200];
				});

				$httpBackend.expectDELETE(Restangular.configuration.baseUrl.concat('/offers/questions/', $scope.questions[questionNumber].id));

				$httpBackend.expectDELETE(Restangular.configuration.baseUrl.concat('/offers/', $scope.offer.offerId,'/questions/', $scope.questions[questionNumber].id));
				$httpBackend.expectGET(Restangular.configuration.baseUrl.concat('/offers/', $routeParams.id, '/questions')).respond(200, []);
				$scope.deleteQuestion(questionNumber);
				$httpBackend.flush();
			});

			it('should delete the question', function() {
				expect(document.getElementById).toHaveBeenCalledWith('q'.concat(questionNumber));
				expect($scope.questions.length).toBe(questionSize-1);
			});
		});
	});
});
