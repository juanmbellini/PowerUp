'use strict';
define(['power-up'], function(power-up) {

	power-up.directive('sample', function() {
		return {
			restrict: 'E',
			template: '<span>Sample</span>'
		};
	});
});
