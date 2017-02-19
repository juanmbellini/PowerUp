'use strict';
define(['powerUp'], function(powerUp) {

	powerUp.service('CsrfService', function() {
        var token = null;
        var tokenHeader = null;
        var tokenParam = null;

        return {
            getToken: function() {
                return token;
            },
            setToken: function(newToken) {
                token = newToken;
            },
            isTokenSet: function() {
                return token !== null;
            },

            getTokenHeader: function() {
                return tokenHeader;
            },
            setTokenHeader: function(newTokenHeader) {
                tokenHeader = newTokenHeader;
            },
            isTokenHeaderSet: function() {
                return tokenHeader !== null;
            },

            getTokenParam: function() {
                return tokenParam;
            },
            setTokenParam: function(newTokenParam) {
                tokenParam = newTokenParam;
            },
            isTokenParamSet: function() {
                return tokenParam !== null;
            }
        };
	});
});
