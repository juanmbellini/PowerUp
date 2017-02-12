'use strict';
define(['powerUp'], function(powerUp) {


	powerUp.controller('HomeCtrl', function($scope, $location, Data, searchedTitleService, Restangular, SweetAlert) {
	    // SweetAlert.swal("BAM!");

		$scope.homePageText = 'This is your homepage';
		$scope.data = Data;
		$scope.gameTitle = '';
		$scope.submitSearch = function() {
			console.log($scope.gameTitle);
			console.log($scope.data);
			searchedTitleService.setTitle($scope.gameTitle);
			$location.search({'name': $scope.gameTitle});
			$location.path('search');
      	};
      	console.log('isLogIn: ', $scope.isLogIn);
      	$scope.isLogIn = true;
		console.log('isLogIn: ', $scope.isLogIn);
	});
});
