'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('ProfileCtrl', function($scope, $location, Restangular) {

        $scope.user = 'default';

        $scope.userId = $location.search().userId;

        Restangular.one('users',$scope.userId).get().then(function(user) {
            // console.log('User id: ', $scope.userId);
            // $scope.user=user;
            // console.log(user);
        });

    });

});
