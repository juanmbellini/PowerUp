'use strict';
define(['powerUp'], function(powerUp) {

	powerUp.directive('sample', function() {
		return {
			restrict: 'E',
			template: '<span>Sample</span>'
		};
	});
});
