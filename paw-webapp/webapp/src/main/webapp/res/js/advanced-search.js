$(function () {
    $("#advanced-search-form").on("submit", function (event) {
        event.preventDefault();
        var params = {};

        var title = $("#title").val();
        var platform = $(this).find("select[name='platform']").val();
        if (platform) {
            params.platform = [platform];
        }
        var genre = $(this).find("select[name='genre']").val();
        if (genre) {
            params.genre = [genre];
        }
        var developer = $(this).find("select[name='developer']").val();
        if (developer) {
            params.developer = [developer];
        }
        var publisher = $(this).find("select[name='publisher']").val();
        if (publisher) {
            params.publisher = [publisher];
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