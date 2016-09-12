$(function() {
    $("#search-form").on("submit", function(event) {
        event.preventDefault();
        Materialize.toast("Searched for " + $("#search-form input[name='search']").val(), 1000);
    });
});