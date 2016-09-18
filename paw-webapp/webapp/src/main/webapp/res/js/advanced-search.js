$(function() {
   $("#advanced-search-form").on("submit", function(event) {
       event.preventDefault();
       Materialize.toast("/search?" + $(this).serialize(), 5000);
   });
});