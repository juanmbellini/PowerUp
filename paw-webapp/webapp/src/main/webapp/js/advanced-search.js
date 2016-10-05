$(function () {
    $("#advanced-search-form").on("submit", function (event) {
        event.preventDefault();
        var params = {};

        var title = $("#title").val();

        var platforms = $(this).find("select[name='platform']").val();
        if (platforms) {
            var index = platforms.indexOf("");
            if(index !== -1) {
                platforms.splice(index, 1);
            }
            if(platforms.length) {
                params.platform = platforms;
            }
        }
        var genres = $(this).find("select[name='genre']").val();
        if (genres) {
            var index = genres.indexOf("");
            if(index !== -1) {
                genres.splice(index, 1);
            }
            if(genres.length) {
                params.genre = genres;
            }
        }
        var developers = $(this).find("select[name='developer']").val();
        if (developers) {
            var index = developers.indexOf("");
            if(index !== -1) {
                developers.splice(index, 1);
            }
            if(developers.length) {
                params.developer = developers;
            }
        }
        var publishers = $(this).find("select[name='publisher']").val();
        if (publishers) {
            var index = publishers.indexOf("");
            if(index !== -1) {
                publishers.splice(index, 1);
            }
            if(publishers.length) {
                params.publisher = publishers;
            }
        }

        if(!title && !params) {
            return;
        }

        //TODO do this through Java
        var URL = "/search?";
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