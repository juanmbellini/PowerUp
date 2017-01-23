'use strict';
define(['powerUp'], function(powerUp) {


    powerUp.controller('SearchCtrl', function($scope, searchedTitleService) {

        $scope.searchedTitle = searchedTitleService.getTitle;
    });

});
