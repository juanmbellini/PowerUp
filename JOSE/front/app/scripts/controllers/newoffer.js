define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.controller('newoffer', function($scope, Restangular, $filter, flowFactory, toastr, $translate, $routeParams) {

    	Restangular.all('platforms').getList().then(function(data) {
    	  $scope.platforms = data;
    	});

    	Restangular.all('products/esrb').getList().then(function(data) {
    	  $scope.esrbs = data;
    	});

    	Restangular.all('products/genre').getList().then(function(data) {
    	  $scope.genres = data;
    	});

    	Restangular.all('products/mpsp').getList().then(function(data) {
    	  $scope.mpsps = data;
    	});

		$scope.createdOffer = {};
		$scope.prodname = null;
		$scope.offerForm = {};
        $scope.noImage = true;

    	$scope.searchGame = function(){
    		
            if($scope.prodname !== null){
                $scope.fromProduct=false;
                $scope.offerForm = {};
                Restangular.all('products').getList({name: $scope.prodname}).then(function(data){
                    var result = data[0];
                    if (result === undefined){
                        $scope.offerForm.name = $scope.prodname;
                    }else{
                        $scope.offerForm.name = result.name;
                        $scope.offerForm.mpsp = result.mpsp;
                        $scope.offerForm.genre = result.genre;
                        $scope.offerForm.platform = result.platform.name;
                        $scope.offerForm.esrb = result.esrb;
                        $("#description").focus();
                    }
                }, function(){
                    $scope.offerForm = {};
                    $scope.offerForm.name = $scope.prodname;
                });
            }
            $scope.nextTab();
    	};

       
    	$scope.uploadOffer = function(){

    		Restangular.all('offers').post($scope.offerForm).then(function(data){
    			$scope.createdOffer = data;
                $("#1st_step").addClass('disabled');
                $("#2nd_step").addClass('disabled');
                $scope.nextTab();
    		}, function(){
                toastr.error($translate.instant('error_creatingOffer'),$translate.instant('error_error'));
    		});  
    	};

        $scope.uploadImage = function(param){
            param.opts.target = '/api/offers/'+$scope.createdOffer.offerId+'/image';
            param.opts.headers = {Authorization: localStorage.getItem("token")};
            param.upload();
        }

        $scope.successUpload = function($flow, $file, $message){
            $('#imgButton').removeClass('disabled');
            $("#3rd_step").addClass('disabled');
            $scope.noImage = false;
        }

        $scope.errorUpload = function($flow, $file, $message){
            toastr.error($translate.instant('error_uploadingImage'),$translate.instant('error_error'));
        }

        $scope.nextTab = function(){
            var $active = $('.wizard .nav-tabs li.active');
            $active.next().removeClass('disabled');
            $($active).next().find('a[data-toggle="tab"]').click();
        }

        $scope.previousTab = function(){
            var $active = $('.wizard .nav-tabs li.active');
            $($active).prev().find('a[data-toggle="tab"]').click();
        };

        Restangular.all('products/names').getList().then(function(data){
            $scope.autoCompleteNames = data;
        });

        $scope.fromProduct = false;
        angular.element(document).ready(function(){

            if($routeParams.name !== undefined &&
               $routeParams.platform !== undefined &&
               $routeParams.mpsp !== undefined &&
               $routeParams.genre !== undefined &&
               $routeParams.esrb !== undefined)
            {
                $scope.offerForm.name = $routeParams.name;
                $scope.offerForm.mpsp = $routeParams.mpsp;
                $scope.offerForm.genre = $routeParams.genre;
                $scope.offerForm.esrb = $routeParams.esrb;
                $scope.offerForm.platform = $routeParams.platform;

                $scope.fromProduct=true;
                $scope.nextTab();
                $("#description").focus();

            }

        });

    	
    });

});

$(document).ready(function () {
    //Initialize tooltips
    $('.nav-tabs > li a[title]').tooltip();
    
    //Wizard
    $('a[data-toggle="tab"]').on('show.bs.tab', function (e) {

        var $target = $(e.target);
    
        if ($target.parent().hasClass('disabled')) {
            return false;
        }
    });
});