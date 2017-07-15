define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.directive('equalFields', function() {
      return {
      require: 'ngModel',
      link: function (scope, elem, attrs, ctrl) {
        var firstField = '#' + attrs.equalFields;
        elem.add(firstField).on('keyup', function () {
          scope.$apply(function () {
            var v = elem.val()===$(firstField).val();
            ctrl.$setValidity('fieldmatch', v);
          });
        });
      }
    }


    });

});
