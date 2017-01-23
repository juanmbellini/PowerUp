'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('ProfileCtrl', function($scope, $location, Restangular) {



        $scope.userId = $location.search().userId;

        // $scope.profilePicture = Restangular.one('users',$scope.userId)

        Restangular.one('users',$scope.userId).get().then(function(user) {
            if (user !== null){
                console.log('User id: ', $scope.userId);
                $scope.user = user;
                console.log(user);
            } else {
                // TODO check log-in
                $location.path("");
            }
        });

    });

});
