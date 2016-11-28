$(function () {
    $('#related-games-carousel').slick({
        infinite: true,
        slidesToShow: 4,
        slidesToScroll: 4,
        arrows: true,
        lazyload: 'ondemand'
    });

    $('#videos-carousel').slick({
        infinite: false,
        arrows: true
    });

    $('#screenshots-carousel').slick({
        infinite: false,
        arrows: true,
        lazyload: 'ondemand'
    });

    var $rateAndStatusForm = $("#rateAndStatusForm");
    var selectedScore = $("#score").val();
    var selectedStatus = $("#status").val();

    $rateAndStatusForm.on("submit", function(event) {
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

    // $('#shelves').on('change', function(event) {
    //     $(this).material_select();          //Y u do dis Materialize
    //     debugger;
    //     if($(this).val().indexOf("newShelf") !== -1) {
    //         //TODO add support for creating new shelves via AJAX
    //     }
    // });
});