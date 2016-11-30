<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>Search - PowerUp</title>
    <%--TODO change default title on all pages--%>

</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>
    <div class="container">
        <div class="row filters-row">
            <h3 class="header center orange-text">
                Search
            </h3>

            <div class="col s12">
                <form id="name-form">
                    <div class="input-field col s6 offset-s3">
                        <input placeholder="Title" type="text" name="name" value="${fn:length(searchedName) > 0 ? searchedName : ""}">
                    </div>
                </form>
            </div>

            <%--OLD SORTING--%>
            <%--<div class="col s6">--%>
                <%--<!-- Nothing here... may be used by future things -->--%>
            <%--</div>--%>
            <%--<div class="col s4">--%>
                <%--<div class="row select-title">--%>
                    <%--Order by--%>
                <%--</div>--%>
                <%--<div class="row">--%>
                    <%--<select class="col s5 select-drop-down" id="orderSelectId" onchange="changeOrderDropDown()">--%>
                        <%--<option value="name">Name</option>--%>
                        <%--<option value="release">Release date</option>--%>
                        <%--<option value="rating">Rating</option>--%>
                    <%--</select>--%>
                    <%--<div class="col s1">--%>

                    <%--</div>--%>
                    <%--<select class="col s5 select-drop-down" id="orderBooleanId" onchange="changeOrderDropDown()">--%>
                        <%--<option value="ascending">Ascending</option>--%>
                        <%--<option value="descending">Descending</option>--%>
                    <%--</select>--%>
                    <%--<div class="col s1">--%>

                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--<div class="col s2">--%>
                <%--<div class="row select-title">--%>
                    <%--Page size--%>
                <%--</div>--%>
                <%--<select class="row select-drop-down" id="pageSizeSelectId" onchange="changePageSize()">--%>
                    <%--<option value="25">25</option>--%>
                    <%--<option value="50">50</option>--%>
                    <%--<option value="100">100</option>--%>
                    <%--<c:if test="${page.pageSize != 25 && page.pageSize != 50 && page.pageSize != 100}">--%>
                        <%--<option value="${page.pageSize}">Other: ${page.pageSize}</option>--%>
                    <%--</c:if>--%>
                <%--</select>--%>
            <%--</div>--%>
            <%--END OLD SORTING--%>

        </div>

        <%--RESULTS--%>
        <div class="row">
            <c:choose>
                <%--"No results" notice--%>
                <c:when test="${ page.overAllAmountOfElements == 0 }">
                    <h3 class="center">No results</h3>
                </c:when>
                <c:otherwise>
                    <%--Total games count--%>
                    <ul class="row games-count">
                        <div class="col s6">
                            Games ${(page.pageNumber - 1) * page.pageSize + 1 } -
                                ${(page.pageNumber - 1) * page.pageSize + page.amountOfElements}
                            of ${page.overAllAmountOfElements}
                        </div>
                    </ul>
                </c:otherwise>
            </c:choose>

            <%--Filters collapsible--%>
            <button class="btn waves-effect waves-light right" id="submit-search" ${hasFilters ? "" : "disabled"}>Search <i class="material-icons right">send</i></button>
            <button class="btn waves-effect waves-light right" id="clear-filters" ${hasFilters ? "" : "disabled"}>Clear filters <i class="material-icons right">close</i></button>
            <ul class="collapsible" data-collapsible="expandable">
                <li>
                    <div class="collapsible-header ${hasFilters ? " active" : ""}"><i
                            class="material-icons">filter_list</i>Filters
                    </div>
                    <div class="collapsible-body">
                        <%--Filter categories--%>
                        <div class="row">
                            <div class="col s12">
                                <ul class="tabs">
                                    <li class="tab col s3"><a href="#platforms" class="active">Platforms</a></li>
                                    <li class="tab col s3"><a href="#genres">Genres</a></li>
                                    <li class="tab col s3"><a href="#developers">Developers</a></li>
                                    <li class="tab col s3"><a href="#publishers">Publishers</a></li>
                                </ul>
                            </div>
                            <%--Filter values--%>
                            <div id="platforms" class="col s12 filter">
                                <div class="input-field">
                                    <div class="autocomplete">
                                        <div class="ac-input col s6">
                                            <input type="text" id="platforms-ac-input" placeholder="Platform name" data-activates="platforms-ac-dropdown" data-beloworigin="true" autocomplete="off">
                                        </div>
                                        <div id="platforms-ac-appender" class="ac-appender col"></div>
                                        <ul id="platforms-ac-dropdown" class="dropdown-content ac-dropdown"></ul>
                                    </div>
                                </div>
                            </div>
                            <div id="genres" class="col s12 filter">
                                <div class="input-field">
                                    <div class="autocomplete">
                                        <div class="ac-input col s6">
                                            <input type="text" id="genres-ac-input" placeholder="Genre name" data-activates="genres-ac-dropdown" data-beloworigin="true" autocomplete="off">
                                        </div>
                                        <div id="genres-ac-appender" class="ac-appender col"></div>
                                        <ul id="genres-ac-dropdown" class="dropdown-content ac-dropdown"></ul>
                                    </div>
                                </div>
                            </div>
                            <div id="developers" class="col s12 filter">
                                <div class="input-field">
                                    <div class="autocomplete">
                                        <div class="ac-input col s6">
                                            <input type="text" id="developers-ac-input" placeholder="Developer name" data-activates="developers-ac-dropdown" data-beloworigin="true" autocomplete="off">
                                        </div>
                                        <div id="developers-ac-appender" class="ac-appender col"></div>
                                        <ul id="developers-ac-dropdown" class="dropdown-content ac-dropdown"></ul>
                                    </div>
                                </div>
                            </div>
                            <div id="publishers" class="col s12 filter">
                                <div class="input-field">
                                    <div class="autocomplete">
                                        <div class="ac-input col s6">
                                            <input type="text" id="publishers-ac-input" placeholder="Publisher name" data-activates="publishers-ac-dropdown" data-beloworigin="true" autocomplete="off">
                                        </div>
                                        <div id="publishers-ac-appender" class="ac-appender col"></div>
                                        <ul id="publishers-ac-dropdown" class="dropdown-content ac-dropdown"></ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </li>
            </ul>

            <%--Results, if any--%>
            <c:if test="${ fn:length(page.data) > 0 }">
                <%--Sorting--%>
                <c:if test="${ fn:length(page.data) > 1 }">
                    <div class="right-align" id="sorting">
                        <%--SORT BY NAME--%>
                        <c:choose>
                            <c:when test="${empty orderCategory || orderCategory == 'name'}">
                                <c:set var="nameSort" value="${empty orderBoolean ? 'ascending' : orderBoolean}" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="nameSort" value="ascending" />
                            </c:otherwise>
                        </c:choose>
                        <button class="btn waves-effect waves-light<c:if test="${not empty orderCategory && orderCategory != 'name'}"> inactive</c:if>" data-category="name" data-sort="${nameSort}">
                            Name <i class="material-icons right">arrow_drop_${nameSort == "ascending" ? "up" : "down"}</i>
                        </button>
                        <%--SORT BY RELEASE DATE--%>
                        <c:choose>
                            <c:when test="${orderCategory == 'release'}">
                                <c:set var="releaseSort" value="${orderBoolean}" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="releaseSort" value="descending" />
                            </c:otherwise>
                        </c:choose>
                        <button class="btn waves-effect waves-light<c:if test="${orderCategory != 'release'}"> inactive</c:if>" data-category="release" data-sort="${releaseSort}">
                            Release <i class="material-icons right">arrow_drop_${releaseSort == "ascending" ? "up" : "down"}</i>
                        </button>
                        <%--SORT BY AVERAGE RATING--%>
                        <c:choose>
                            <c:when test="${orderCategory == 'rating'}">
                                <c:set var="ratingSort" value="${orderBoolean}" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="ratingSort" value="descending" />
                            </c:otherwise>
                        </c:choose>
                        <button class="btn waves-effect waves-light<c:if test="${orderCategory != 'rating'}"> inactive</c:if>" data-category="rating" data-sort="${ratingSort}">
                            Rating <i class="material-icons right">arrow_drop_${ratingSort == "ascending" ? "up" : "down"}</i>
                        </button>
                    </div>
                </c:if>
                <%--End sorting--%>
                <%--TODO pagination--%>
                <%--Games list header--%>
                <div class="col s12 center" id="games-list-header">
                    <p class="col s2">Cover Picture</p>
                    <p class="col s7 left-align">Title and Summary</p>
                    <p class="col s1">Release</p>
                    <p class="col s2 right-align">Avg. Rating</p>
                </div>
                <%--Games list--%>
                <ul class="collection games-list">
                    <c:forEach var="game" items="${page.data}">
                        <li class="collection-item avatar col s12">
                            <div class="col s2 cover-pic-container valign-wrapper">
                                <img class="cover-picture valign" src="${game.coverPictureUrl}" alt="${game.name}">
                            </div>
                            <div class="col primary-content s7">
                                <p class="title">
                                    <a href="<c:url value="/game?id=${game.id}" />">${game.name}</a>
                                </p>
                                <p class="summary">${game.summary}</p>
                            </div>
                            <div class="col s1 center">
                                <p style="margin-top: 33px;">${empty game.releaseDate.year ? "Unknown" : game.releaseDate.year}</p>
                            </div>
                            <div class="col s2">
                                <div class="secondary-content">
                                    <c:choose>
                                        <c:when test="${game.avgScore <= 10 && game.avgScore > 0}">
                                            <p class="rating-number center"><b>${game.avgScore}</b></p>
                                            <p class="rating-stars hide-on-small-and-down">
                                                <c:forEach begin="0" end="4" var="i">
                                                    <c:choose>
                                                        <c:when test="${game.avgScore - (i * 2) - 1 < 0}">
                                                            <i class="material-icons">star_border</i>
                                                        </c:when>
                                                        <c:when test="${game.avgScore - (i * 2) - 1 == 0}">
                                                            <i class="material-icons">star_half</i>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <i class="material-icons">star</i>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </p>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="rating-number center"><b>Unrated</b></p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </li>
                    </c:forEach>
                </ul>

                <%--Another total games count--%>
                <ul class="row games-count">
                    <div class="col s6">
                        Games ${(page.pageNumber - 1) * page.pageSize + 1 } -
                            ${(page.pageNumber - 1) * page.pageSize + page.amountOfElements}
                        of ${page.overAllAmountOfElements}
                    </div>
                </ul>

                <%--Pagination--%>
                <div class="row">
                    <ul class="pagination center">
                            <%--Left chevron - only shown when there are > 1 pages --%>
                        <c:if test="${page.totalPages > 1}">
                            <c:choose>
                                <c:when test="${page.pageNumber > 1}">
                                    <li class="waves-effect">
                                        <a href="<c:url value="${changePageUrl}"> <c:param name="pageNumber" value="${page.pageNumber - 1}"/> </c:url>">
                                            <i class="material-icons">chevron_left</i>
                                        </a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="disabled">
                                        <a href="#!">
                                            <i class="material-icons">chevron_left</i>
                                        </a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                            <%--Page numbers - always shown--%>
                        <c:set var="just_one"
                               value="${(page.pageNumber - 4) <= 0 || page.totalPages <= 10}"/>
                        <c:set var="no_more_prev"
                               value="${(page.pageNumber + 5) > page.totalPages}"/>
                        <c:set var="the_first_ones"
                               value="${(page.pageNumber + 5) < 10}"/>
                        <c:set var="no_more_next"
                               value="${page.totalPages < 10 || (page.pageNumber + 5) >= page.totalPages}"/>
                        <c:forEach var="pageIt"
                                   begin="${just_one ? 1 : no_more_prev ? page.totalPages - 9 : page.pageNumber - 4}"
                                   end="${no_more_next ? page.totalPages : the_first_ones ? 10 : page.pageNumber + 5}">
                            <li class=${pageIt == page.pageNumber ? "active" : "waves-effect"}>
                                <c:choose>
                                    <c:when test="${pageIt == page.pageNumber}">
                                        <a>
                                                ${pageIt}
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="<c:url value="${changePageUrl}"> <c:param name="pageNumber" value="${pageIt}"/> </c:url>">
                                                ${pageIt}
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </li>
                        </c:forEach>
                            <%--Right chevron - only shown if there are > 1 pages--%>
                        <c:if test="${page.totalPages > 1}">
                            <c:choose>
                                <c:when test="${page.pageNumber == page.totalPages}">
                                    <li class="disabled">
                                        <a href="#!">
                                            <i class="material-icons">chevron_right</i>
                                        </a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="waves-effect">
                                        <a href="<c:url value="${changePageUrl}"> <c:param name="pageNumber" value="${page.pageNumber + 1}"/> </c:url>">
                                            <i class="material-icons">chevron_right</i>
                                        </a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </ul>
                </div>
            </c:if>
        </div>
        <%--END RESULTS--%>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
<style type="text/css">
    <c:set var="fontSizeStr" value="16px" /> /*TODO porque aca css???*/
    <c:set var="fontSizeInt" value="16" />
    <c:set var="lineHeight" value="1.5" />
    <c:set var="maxLines" value="3" />

    /* Multi-line clamping adapted from http://codepen.io/martinwolf/pen/qlFdp */
    .summary {
        margin: 5px auto 0 auto !important;
        display: block; /* Fallback for non-webkit */
        display: -webkit-box;
        max-width: 100%;
        font-size: ${fontSizeStr};
        line-height: ${lineHeight};
        -webkit-line-clamp: ${maxLines};
        height: ${fontSizeInt * lineHeight * maxLines}; /* Fallback for non-webkit */
        -webkit-box-orient: vertical;
        overflow: hidden;
        text-overflow: ellipsis;
    }
</style>

<script type="text/javascript" src="<c:url value="/js/jquery.materialize-autocomplete.js" />"></script>
<script type="text/javascript">

    /**
     * Keeps track of applied filters when the page originally loaded. Used to populate filter values on load.
     */
    var ORIGINAL_FILTERS = ${filters};

    /**
     * Tracks whether filters have changed. If so, searching will reset page number.
     */
    var filtersChanged = false;

    /*
     * Tracks the searched name.
     */
    var NAME = "${searchedName}";

    /**
     * Tracks whether the searched name has changed. If so, searching will reset page number.
     */
    var nameChanged = false;

    /**
     * Tracks what results should be ordered by. Not to be confused with ORDER global.
     */
    var ORDER_BY = "${orderCategory}" || null;

    /**
     * Tracks whether order should be performed ascending or descendingly.
     */
    var ORDER = "${orderBoolean}" || null;

    /**
     * Tracks whether the ordering has changed. If so, searching will reset page number.
     */
    var orderChanged = false;

    //Platforms
    var PLATFORMS = [];
    <c:forEach items="${platforms}" var="platform" varStatus="status">
        PLATFORMS[${platform[0].id}] = "${platform[0].name}";
    </c:forEach>

    //Genres
    var GENRES = [];
    <c:forEach items="${genres}" var="genre" varStatus="status">
        GENRES[${genre[0].id}] = "${genre[0].name}";
    </c:forEach>

    //Developers
    var DEVELOPERS = [];
    <c:forEach items="${developers}" var="developer" varStatus="status">
        DEVELOPERS[${developer[0].id}] = "${developer[0].name}";
    </c:forEach>

    //Publishers
    var PUBLISHERS = [];
    <c:forEach items="${publishers}" var="publisher" varStatus="status">
        PUBLISHERS[${publisher[0].id}] = "${publisher[0].name}";
    </c:forEach>

    $(function () {
        $("#orderSelectId").val("${orderCategory == null ? "name" : orderCategory}");
        $("#orderSelectId").material_select();
        $("#orderBooleanId").val("${orderBoolean == null ? "ascending" : orderBoolean}");
        $("#orderBooleanId").material_select();
        $("#pageSizeSelectId").val("${page.pageSize}");
        $("#pageSizeSelectId").material_select();

        //Auto-select the platforms tab in search filters
        $(".indicator").attr('style',"right: 75%; left: 0;");
        $("#collapsible-header").on('click', function () {
            $('tabs').tabs('select_tab', 'platformTab');
        });

        $("#clear-filters").on('click', function () {
            clearFilters();
        });

        $("#submit-search").on('click', function () {
            search();
        });

        $("#name-form").on("submit", function(event) {
            event.preventDefault();
            var inputName = $(this).find("input[name='name']").val();
            nameChanged = inputName != NAME;
            NAME = inputName;
            search();
        });

        $("#sorting").on("click", "button", function() {
            var $elem = $(this);
            var category = $elem.data("category");
            var sort = $elem.data("sort");
            if(!$elem.hasClass("inactive")) {
                //Clicking on an active sorting should have the effect of flipping it.
                sort = (sort == "ascending" ? "descending" : "ascending");
            }
            orderChanged = category != ORDER_BY || sort != ORDER;
            ORDER_BY = category;
            ORDER = sort;
            search();
        });
    });

    //TODO reimplement page size and reimplement this function
    function changePageSize() {
        var selectedPageSize = document.getElementById("pageSizeSelectId");
        var pageSize = selectedPageSize.options[selectedPageSize.selectedIndex].value;
        var url = "<c:url value="${changePageSizeUrl}"/>";
        var hasQuery = url.indexOf("?") != -1;
        url += (hasQuery ? "&" : "?") + "pageSize=" + pageSize;
        window.location = url;
    }

    /**
     * Clears all applied filters.
     */
    function clearFilters() {
        var platforms = platformsAutocomplete.value;
        for(var i = 0; i < platforms.length; i++) {
            platformsAutocomplete.remove(platforms[i]);
        }
        var genres = genresAutocomplete.value;
        for(var i = 0; i < genres.length; i++) {
            genresAutocomplete.remove(genres[i]);
        }
        var developers = developersAutocomplete.value;
        for(var i = 0; i < developers.length; i++) {
            developersAutocomplete.remove(developers[i]);
        }
        var publishers = publishersAutocomplete.value;
        for(var i = 0; i < publishers.length; i++) {
            publishersAutocomplete.remove(publishers[i]);
        }
        filtersChanged = true;
    }

    /**
     * Builds a URL to search games with the currently set filters and sorting criteria, resetting the page number if
     * necessary. Note that this function is NOT meant to handle changes in page number.
     */
    function search() {
        if(!nameChanged && !filtersChanged && !orderChanged) {
            window.location.reload();
        }

        //Search changed. Build search URL and clear page number.
        var url = window.location.pathname;

        //Add name parameter
        url += "?name=" + NAME;

        //Build filters, extracting the name of each value.
        var filters = {};

        //Platforms
        var platformNames = [];
        var selectedPlatforms = platformsAutocomplete.value;
        for(var index in selectedPlatforms) {
            if(selectedPlatforms.hasOwnProperty(index)) {
                platformNames.push(selectedPlatforms[index].text);
            }
        }
        if(platformNames.length > 0) {
            filters.platform = platformNames;
        }

        //Genres
        var genreNames = [];
        var selectedGenres = genresAutocomplete.value;
        for(var index in selectedGenres) {
            if(selectedGenres.hasOwnProperty(index)) {
                genreNames.push(selectedGenres[index].text);
            }
        }
        if(genreNames.length > 0) {
            filters.genre = genreNames;
        }

        //Developers
        var developerNames = [];
        var selectedDevelopers = developersAutocomplete.value;
        for(var index in selectedDevelopers) {
            if(selectedDevelopers.hasOwnProperty(index)) {
                developerNames.push(selectedDevelopers[index].text);
            }
        }
        if(developerNames.length > 0) {
            filters.developer = developerNames;
        }

        //Publishers
        var publisherNames = [];
        var selectedPublishers = publishersAutocomplete.value;
        for(var index in selectedPublishers) {
            if(selectedPublishers.hasOwnProperty(index)) {
                publisherNames.push(selectedPublishers[index].text);
            }
        }
        if(publisherNames.length > 0) {
            filters.publisher = publisherNames;
        }

        //Add filters parameter
        url += "&filters=" + JSON.stringify(filters);

        //Add order parameter
        if(ORDER !== null) {
            url += "&orderCategory=" + ORDER_BY;
        }
        if(ORDER !== null) {
            url += "&orderBoolean=" + ORDER;
        }

        //Discard page number parameter

        //Done, redirect
        window.location = encodeURI(url);
    }

    /* ******************************************
     *        AUTOCOMPLETE INITIALIZATION
     * *****************************************/

    /**
     * Searches for values in an array matching a query using regex. Adapted from http://stackoverflow.com/a/10152699/2333689
     *
     * @param array The array in which to search.
     * @param query The query.
     * @returns {Array} The matching values as objects in the form {match ID, match text}
     */
    function searchInArray(array, query) {
        var results = [];
        for(var index in array) {
            //Case-insensitive matching with anything before and/or anything after
            if(array[index].match(new RegExp(".*"+query+".*", "i"))) {
                results.push({"id": index, "text": array[index]});
            }
        }
        return results;
    }

    /**
     * Finds a value in an autocomplete values array.
     *
     * @param valuesArray Values array, where each element is an object of the form {id: number, text: String}
     * @param valueObject The value to find, an object of the same form as each element of the values array.
     * @returns {boolean} Whether the value is in the array.
     */
    function isInAutocompleteValue(valuesArray, valueObject) {
        for(var i = 0; i < valuesArray.length; i++) {
            if(valuesArray.hasOwnProperty(i) && valuesArray[i].text === valueObject.text) {
                return true;
            }
        }
        return false;
    }

    var platformsAutocomplete = null;
    var genresAutocomplete = null;
    var developersAutocomplete = null;
    var publishersAutocomplete = null;

    $(function() {
        //Platforms
        platformsAutocomplete = $('#platforms-ac-input').materialize_autocomplete({
            cacheable: false,
            multiple: {
                enable: true,
                maxSize: 100,
                onAppend: function(addedValue) {
                    filtersChanged = true;
                    $("#submit-search").removeAttr("disabled");
                }
            },
            appender: {
                el: '#platforms-ac-appender'
            },
            dropdown: {
                el: '#platforms-ac-dropdown'
            },
            getData: function(query, callback) {
                //Perform search
                var matches = searchInArray(PLATFORMS, query);
                //Exclude selected values
                var result = [];
                var selected = platformsAutocomplete.value;
                for(var index in matches) {
                    if(matches.hasOwnProperty(index) && !isInAutocompleteValue(selected, matches[index])) {
                        result.push(matches[index]);
                    }
                }
                //Show dropdown with unselected results
                callback(query, result);
            }
        });
        if(ORIGINAL_FILTERS.hasOwnProperty("platform")) {
            for(var i = 0; i < ORIGINAL_FILTERS.platform.length; i++) {
                var name = ORIGINAL_FILTERS.platform[i];
                platformsAutocomplete.append({"id": ORIGINAL_FILTERS.platform.indexOf(name), "text": name});
            }
        }

        //Genres
        genresAutocomplete = $('#genres-ac-input').materialize_autocomplete({
            cacheable: false,
            multiple: {
                enable: true,
                maxSize: 100,
                onAppend: function(addedValue) {
                    filtersChanged = true;
                    $("#submit-search").removeAttr("disabled");
                }
            },
            appender: {
                el: '#genres-ac-appender'
            },
            dropdown: {
                el: '#genres-ac-dropdown'
            },
            getData: function(query, callback) {
                //Perform search
                var matches = searchInArray(GENRES, query);
                //Exclude selected values
                var result = [];
                var selected = genresAutocomplete.value;
                for(var index in matches) {
                    if(matches.hasOwnProperty(index) && !isInAutocompleteValue(selected, matches[index])) {
                        result.push(matches[index]);
                    }
                }
                //Show dropdown with unselected results
                callback(query, result);
            }
        });
        if(ORIGINAL_FILTERS.hasOwnProperty("genre")) {
            for(var i = 0; i < ORIGINAL_FILTERS.genre.length; i++) {
                var name = ORIGINAL_FILTERS.genre[i];
                genresAutocomplete.append({"id": ORIGINAL_FILTERS.genre.indexOf(name), "text": name});
            }
        }

        //Developers
        developersAutocomplete = $('#developers-ac-input').materialize_autocomplete({
            cacheable: false,
            multiple: {
                enable: true,
                maxSize: 100,
                onAppend: function(addedValue) {
                    filtersChanged = true;
                    $("#submit-search").removeAttr("disabled");
                }
            },
            appender: {
                el: '#developers-ac-appender'
            },
            dropdown: {
                el: '#developers-ac-dropdown'
            },
            getData: function(query, callback) {
                //Perform search
                var matches = searchInArray(DEVELOPERS, query);
                //Exclude selected values
                var result = [];
                var selected = developersAutocomplete.value;
                for(var index in matches) {
                    if(matches.hasOwnProperty(index) && !isInAutocompleteValue(selected, matches[index])) {
                        result.push(matches[index]);
                    }
                }
                //Show dropdown with unselected results
                callback(query, result);
            }
        });
        if(ORIGINAL_FILTERS.hasOwnProperty("developer")) {
            for(var i = 0; i < ORIGINAL_FILTERS.developer.length; i++) {
                var name = ORIGINAL_FILTERS.developer[i];
                developersAutocomplete.append({"id": ORIGINAL_FILTERS.developer.indexOf(name), "text": name});
            }
        }

        //Publishers
        publishersAutocomplete = $('#publishers-ac-input').materialize_autocomplete({
            cacheable: false,
            multiple: {
                enable: true,
                maxSize: 100,
                onAppend: function(addedValue) {
                    filtersChanged = true;
                    $("#submit-search").removeAttr("disabled");
                }
            },
            appender: {
                el: '#publishers-ac-appender'
            },
            dropdown: {
                el: '#publishers-ac-dropdown'
            },
            getData: function(query, callback) {
                //Perform search
                var matches = searchInArray(PUBLISHERS, query);
                //Exclude selected values
                var result = [];
                var selected = publishersAutocomplete.value;
                for(var index in matches) {
                    if(matches.hasOwnProperty(index) && !isInAutocompleteValue(selected, matches[index])) {
                        result.push(matches[index]);
                    }
                }
                //Show dropdown with unselected results
                callback(query, result);
            }
        });
        if(ORIGINAL_FILTERS.hasOwnProperty("publisher")) {
            for(var i = 0; i < ORIGINAL_FILTERS.publisher.length; i++) {
                var name = ORIGINAL_FILTERS.publisher[i];
                publishersAutocomplete.append({"id": ORIGINAL_FILTERS.publisher.indexOf(name), "text": name});
            }
        }
    });
</script>
</body>
</html>