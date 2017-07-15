/**
 * Created by vitali on 9/28/16.
 */

var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

function performSearch(page){


    var url = window.location.href.split("?")[0] + "?";

        var query = getUrlParameter("query");

        var platform = $('input[name="platform"]:checked').val();

        var genre = $('input[name="genre"]:checked').val();

        var esrb = $('input[name="esrb"]:checked').val();

        var mpsp = $('input[name="mpsp"]:checked').val();

        if(query != undefined){
            url += "query=" + query + "&";
        }

        if(platform != undefined){
            url += "platform=" + platform + "&";
        }
        if(genre != undefined){
            url += "genre=" + genre + "&";
        }
        if(esrb != undefined){
            url += "esrb=" + esrb + "&";
        }
        if(mpsp != undefined){
            url += "mpsp=" + mpsp + "&";
        }
        if(page != undefined && page>=1 ){
            url += "page=" + page;
        }

    window.location.href = url;
}

function clearFilter(){


    var url = window.location.href.split("?")[0] + "?";

    var query = getUrlParameter("query");

    var platform = getUrlParameter("platform");

   if(query != undefined){
       url += "query=" + query;
   }else if (platform != undefined && platform != ""){
       url += "platform=" + platform;
   }

    window.location.href = url;

}


