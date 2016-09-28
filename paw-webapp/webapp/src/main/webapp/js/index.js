$(function() {
    $("#search-form").on("submit", function(event) {
        event.preventDefault();
        var searchTerm = $(this).find("input[name='search']").val();
            window.location = "/search?name=" + encodeURIComponent(searchTerm);
    });
});