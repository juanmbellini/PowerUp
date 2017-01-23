'use strict';
define(['powerUp'], function(powerUp) {

	powerUp.controller('HomeCtrl', function($scope, $location, Data, searchedTitleService, Restangular) {
		$scope.homePageText = 'This is your homepage';
		$scope.data = Data;
		$scope.submitSearch = function() {
			console.log($scope.gameTitle);
			console.log($scope.data);
			searchedTitleService.setTitle($scope.gameTitle);
			$location.path("search");
      	};
	});
});
