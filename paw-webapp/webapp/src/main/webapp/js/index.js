$(function() {
    $("#search-icon").on('click', function() {
        var $form = $("#search-form");
        if($form.find("input[name='name']").val().length > 0) {
            $form.trigger('submit');
        }
    });
});