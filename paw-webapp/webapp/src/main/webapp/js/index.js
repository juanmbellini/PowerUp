$(function() {
    $("#search-icon").on('click', function() {
        var $form = $("#search-form");
        if($form.find("input[name='name']").val().length > 0) {
            $form.trigger('submit');
        }
    });

    $('.slick-carousel').slick({
        infinite: true,
        slidesToShow: 4,
        slidesToScroll: 4,
        arrows: true,
        lazyload: 'ondemand'
    });
});