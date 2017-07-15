'use strict';
define(['trademygame', '../services/authService'], function(trademygame) {

	trademygame.controller('HomeCtrl', function($scope, authService, Restangular) {

		$scope.loggedId = authService.loggedId();

		if(authService.loggedId() != undefined){
			Restangular.one('offers/recommendations').get().then(function(data) {

					 var matrix = [];
					 var i,k;

					 for(i=0, k=-1; i<data.length;i++){
							 if (i % 3 === 0) {
									 k++;
									 matrix[k] = [];
							 }
							 matrix[k].push(Restangular.stripRestangular(data.plain()[i]));
					 }

					 $scope.recommendations = matrix;

			});
		}

		$scope.myInterval = 4000;

	});
});
