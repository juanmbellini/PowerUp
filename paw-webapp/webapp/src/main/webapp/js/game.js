$(function () {
    $('.slick-carousel').slick({
        infinite: true,
        slidesToShow: 4,
        slidesToScroll: 4,
        arrows: true,
        lazyload: 'ondemand'
    });

    var $form = $("#rateAndStatusForm");
    var selectedScore = $("#score").val();
    var selectedStatus = $("#status").val();

    $form.on("submit", function(event) {
        var newScore = $("#score").val();
        var newStatus = $("#status").val();
        if(newScore === selectedScore && newStatus === selectedStatus) {
            event.preventDefault();
        } else {
            var $submit = $("#submit");
            $submit.attr("disabled", "disabled");
            $submit.addClass("disabled");
            $submit.html("Updating...");
        }
        selectedScore = newScore;
        selectedStatus = newStatus;
    });
});