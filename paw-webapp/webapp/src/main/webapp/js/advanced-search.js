$(function () {
    $("#advanced-search-form").on("submit", function (event) {
        event.preventDefault();
        var params = {};

        var title = $("#title").val();

        //Remove the "any" value from every filter. That way, if values are selected for a filter, only those values
        //will match. If no value is selected, that filter won't be taken into account.
        var anyIndex;
        var platforms = $(this).find("select[name='platform']").val();
        if (platforms) {
            anyIndex = platforms.indexOf("");
            if(anyIndex !== -1) {
                platforms.splice(anyIndex, 1);
            }
            if(platforms.length) {
                params.platform = platforms;
            }
        }
        var genres = $(this).find("select[name='genre']").val();
        if (genres) {
            anyIndex = genres.indexOf("");
            if(anyIndex !== -1) {
                genres.splice(anyIndex, 1);
            }
            if(genres.length) {
                params.genre = genres;
            }
        }
        var developers = $(this).find("select[name='developer']").val();
        if (developers) {
            anyIndex = developers.indexOf("");
            if(anyIndex !== -1) {
                developers.splice(anyIndex, 1);
            }
            if(developers.length) {
                params.developer = developers;
            }
        }
        var publishers = $(this).find("select[name='publisher']").val();
        if (publishers) {
            anyIndex = publishers.indexOf("");
            if(anyIndex !== -1) {
                publishers.splice(anyIndex, 1);
            }
            if(publishers.length) {
                params.publisher = publishers;
            }
        }

        if(!title && !params) {
            return;
        }

        //TODO do this through Java, this might not work on deployment
        var URL = "search?";
        if(title) {
            URL += "name=" + encodeURIComponent(title);
            if(params) {
                URL += "&";
            }
        }
        if(params) {
            URL += "filters=" + encodeURIComponent(JSON.stringify(params));
        }
        window.location = URL;
    });
});