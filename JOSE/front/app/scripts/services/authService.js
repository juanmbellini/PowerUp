define(['trademygame'], function(trademygame) {

    'use strict';
    trademygame.service('authService', function() {

        this.isLoggedIn = function(){
        	var token = localStorage.getItem("token");
        	if(token === null){
        	    return false;
            }

            var claims = atob(token.split(".")[1]);
            var json = JSON.parse(claims);

            var exp = json["exp"];
            exp = exp*1000;

            var expDate = new Date(exp);
            var nowDate = new Date();
            if(expDate < nowDate){
                localStorage.removeItem("token");
                return false;
            }
        	return true;
        };

        this.loggedUser = function(){
        	var token = localStorage.getItem("token");
        	if(token === null) return null;

        	var claims = atob(token.split(".")[1]);
        	var json = JSON.parse(claims);
        	return json["sub"];
        };

        this.loggedId = function(){
          var token = localStorage.getItem("token");
          if(token === null) return null;

          var claims = atob(token.split(".")[1]);
          var json = JSON.parse(claims);
          return json["jti"];
        }
    });

});
