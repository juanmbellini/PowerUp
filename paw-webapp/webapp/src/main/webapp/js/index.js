$(function() {
    $("#search-form").on("submit", function(event) {
        event.preventDefault();
        var searchTerm = $(this).find("input[name='search']").val();
        if(searchTerm) {
            window.location = "/search?name=" + encodeURIComponent(searchTerm);
        }
    });

    $("#search-icon").on('click', function() {
        $("#search-form").trigger('submit');
    });
});