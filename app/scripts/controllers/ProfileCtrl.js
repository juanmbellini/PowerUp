'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.controller('ProfileCtrl', function($scope, $location, Restangular) {




        $scope.userId = $location.search().userId;
        console.log('User id: ', $scope.userId);
        Restangular.one('users',$scope.userId).get().then(function(user) {
            $scope.user = user;
            console.log('User: ', user);
            if ($scope.userId > 0 && $scope.user !== null) {
                console.log('todo OK!');
            } else {
                // TODO check log-in
                $location.search({});
                $location.path('');
            }
        }, function(response) {
            console.log('Error with status code', response.status); // TODO handle error
            $location.search({});
            $location.path('');
        });


        $scope.profilePicture = Restangular.one('users',$scope.userId).customGET('picture',{},{Accept: 'image/png'}).get();
        console.log($scope.profilePicture);
        console.log('isLogIn: ', $scope.isLogIn);
    });

});
