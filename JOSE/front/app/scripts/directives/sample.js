'use strict';
define(['trademygame'], function(trademygame) {

	trademygame.directive('sample', function() {
		return {
			restrict: 'E',
			template: '<span>Sample</span>'
		};
	});
});
