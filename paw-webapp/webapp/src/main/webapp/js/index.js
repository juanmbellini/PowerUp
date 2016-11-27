$(function() {
    $("#search-icon").on('click', function() {
        var $form = $("#search-form");
        $form.trigger('submit');
    });

    $('.slick-carousel').slick({
        infinite: false,
        slidesToShow: 4,
        slidesToScroll: 4,
        arrows: true,
        lazyload: 'ondemand'
    });
});