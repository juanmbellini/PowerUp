'use strict';
define(['powerUp', 'slick-carousel', 'onComplete'], function(powerUp) {

	powerUp.controller('HomeCtrl', function($scope, $location, Data, searchedTitleService, Restangular, SweetAlert, AuthService) {
	    // SweetAlert.swal("BAM!");
		Restangular.setFullResponse(false);
		$scope.homePageText = 'This is your homepage';
		$scope.data = Data;
		$scope.gameTitle = '';
		$scope.submitSearch = function() {
			searchedTitleService.setTitle($scope.gameTitle);
			$location.search({'name': $scope.gameTitle});
			$location.path('search');
      	};

      	if (AuthService.isLoggedIn()) {
			$scope.user = AuthService.getCurrentUser();
			Restangular.all('users').one('username',$scope.user.username).get().then(function (user) {
				Restangular.one('users',user.id).all('recommended-games').getList({}).then(function (recommendedGames) {
					$scope.recommendedGames = recommendedGames;
					$scope.recommendedMin = Math.min($scope.recommendedGames.length, 5);
				});
			});
		}

		$scope.$on('recommendedRendered', function(event) {
			angular.element('#recommended-carousel').slick({
				infinite: false,
				arrows: true
			});
			require(['lightbox2']); // TODO ensure requirejs doesn't load this twice
		});
	});
});
