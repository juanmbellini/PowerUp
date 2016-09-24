$(function() {
    $("#search-form").on("submit", function(event) {
        event.preventDefault();
        var searchTerm = $(this).find("input[name='search']").val();
        if(searchTerm.length > 0) {
            window.location = "/search?name=" + encodeURIComponent(searchTerm);
        } else {
            Materialize.toast("Empty search param", 1000);
        }
    });
});