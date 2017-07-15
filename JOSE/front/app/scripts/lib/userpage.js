/**
 * Created by natinavas on 10/8/16.
 */
(function(){
    'use strict';
    var $ = jQuery;
    $.fn.extend({
        filterTable: function(){
            return this.each(function(){
                $(this).on('keyup', function(e){
                    var $this = $(this), search = $this.val().toLowerCase(), target = $this.attr('data-filters'), $rows = $(target).find('tbody tr');
                    if(search == '') {
                        $rows.show();
                    } else {
                        $rows.each(function(){
                            var $this = $(this);
                            $this.text().toLowerCase().indexOf(search) === -1 ? $this.hide() : $this.show();
                        })
                    }
                });
            });
        }
    });
    $('[data-action="filter"]').filterTable();
})(jQuery);

$(function(){
    // attach table filter plugin to inputs
    $('[data-action="filter"]').filterTable();

    $('.container-fluid').on('click', '.panel-heading span.filter', function(e){
        var $this = $(this),
            $panel = $this.parents('.panel');

        $panel.find('.panel-body').slideToggle();
        if($this.css('display') != 'none') {
            $panel.find('.panel-body input').focus();
        }
    });
    $('[data-toggle="tooltip"]').tooltip();
})
/*
$(function(){
    $('.tabs').stickyTabs();
});


(function ( $ ) {
    $.fn.stickyTabs = function( options ) {
        var context = this

        var settings = $.extend({
            getHashCallback: function(hash, btn) { return hash },
            selectorAttribute: "href",
            backToTop: false,
            initialTab: $('li.active > a', context)
        }, options );

        // Show the tab corresponding with the hash in the URL, or the first tab.
        var showTabFromHash = function() {
            var hash = settings.selectorAttribute == "href" ? window.location.hash : window.location.hash.substring(1);
            if (hash != '') {
                var selector = hash ? 'a[' + settings.selectorAttribute +'="' + hash + '"]' : settings.initialTab;
                $(selector, context).tab('show');
                setTimeout(backToTop, 1);
            }
        }

        // We use pushState if it's available so the page won't jump, otherwise a shim.
        var changeHash = function(hash) {
            if (history && history.pushState) {
                history.pushState(null, null, window.location.pathname + window.location.search + '#' + hash);
            } else {
                scrollV = document.body.scrollTop;
                scrollH = document.body.scrollLeft;
                window.location.hash = hash;
                document.body.scrollTop = scrollV;
                document.body.scrollLeft = scrollH;
            }
        }

        var backToTop = function() {
            if (settings.backToTop === true) {
                window.scrollTo(0, 0);
            }
        }

        // Set the correct tab when the page loads
        showTabFromHash();

        // Set the correct tab when a user uses their back/forward button
        $(window).on('hashchange', showTabFromHash);

        // Change the URL when tabs are clicked
        $('a', context).on('click', function(e) {
            var hash = this.href.split('#')[1];
            if (typeof hash != 'undefined' && hash != '') {
                var adjustedhash = settings.getHashCallback(hash, this);
                changeHash(adjustedhash);
                setTimeout(backToTop, 1);
            }
        });

        return this;
    };
}( jQuery ));
*/
