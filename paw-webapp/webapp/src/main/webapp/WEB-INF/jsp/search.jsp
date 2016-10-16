<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--<%@ taglib prefix="reload"%> --%>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>Results - PowerUp</title>

    <!-- variables -->
    <c:set var="filtersJson" value="${pageContext.request.getParameter('filters')}"/>
    <c:set var="pageSizeUrl" value="${pageContext.request.getParameter('pageSize')}"/>

</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>
    <div class="container">
        <div class="section">
            <h1 class="header center orange-text">Results for "${searchedName}"<c:if test="${hasFilters}"> with
                filters</c:if></h1>
        </div>
        <div class="row">

            <div class="col s2">

            </div>
        </div>
        <div class="section ">
            <div class="row filters-row">
                <div class="col s6">
                </div>
                <div class="col s4">
                    <div class="row select-title">
                        Order by
                    </div>
                    <div class="row">
                        <select class="col s5 select-drop-down" id="orderSelectId" onchange="changeOrderDropDown()">
                            <option value="name">Name</option>
                            <option value="release date">Release date</option>
                            <option value="avg-rating">Rating</option>
                        </select>
                        <div class="col s1">

                        </div>
                        <select class="col s5 select-drop-down" id="orderBooleanId" onchange="changeOrderDropDown()">
                            <option value="ascending">Ascending</option>
                            <option value="descending">Descending</option>
                        </select>
                        <div class="col s1">

                        </div>
                    </div>
                </div>
                <div class="col s1">

                </div>
                <div class="col s1">
                    <div class="row select-title">
                        Page size
                    </div>
                    <select class="row select-drop-down" id="pageSizeSelectId" onchange="changePageSize()">
                        <option value="25">25</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                    </select>
                </div>

            </div>
            <div class="row">
                <c:choose>
                    <c:when test="${ fn:length(results) == 0 }">
                        <h3 class="center">No results</h3>
                    </c:when>
                    <c:otherwise>
                        <ul class="row games-count">
                            <div class="col s6">
                                Games ${(pageNumber - 1) * pageSize + 1 } -
                                    ${(pageNumber - 1) * pageSize + amountOfElements}
                            </div>
                        </ul>
                        <ul class="collection" id="results">
                            <c:forEach var="game" items="${results}">
                                <li class="collection-item avatar col s12">
                                    <div class="col s2" style="padding: 0;">
                                        <img src="${game.coverPictureUrl}" alt="${game.name}"
                                             style="max-width: 100%; height: auto;">
                                    </div>
                                    <div class="col primary-content s7">
                                        <p class="title"><a
                                                href="<c:url value="/game?id=${game.id}" />">${game.name}</a></p>
                                        <p>
                                            <c:forEach var="platform" items="${game.platforms}" varStatus="status">
                                                ${platform} <c:if test="${!status.last}"> | </c:if>
                                            </c:forEach>
                                        </p>
                                    </div>
                                    <div class="col s1 center">
                                        <p style="margin-top: 33px;">${game.releaseDate.year}</p>
                                    </div>
                                    <div class="col s2">
                                        <div class="secondary-content">
                                            <c:choose>
                                                <c:when test="${game.avgScore <= 10 && game.avgScore>=0}">
                                                    <p class="rating-number center"><b>${game.avgScore}</b></p>
                                                    <p class="rating-stars hide-on-small-and-down">
                                                        <c:forEach begin="0" end="4" var="i">
                                                            <c:choose>
                                                                <c:when test="${game.avgScore-(i*2)-1<0}">
                                                                    <i class="material-icons">star_border</i>
                                                                </c:when>
                                                                <c:when test="${game.avgScore-(i*2)-1==0}">
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
                                                    <p class="rating-number center"><b>unrated</b></p>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                        <ul class="row games-count">
                            <div class="col s6">
                                Games ${(pageNumber - 1) * pageSize + 1 } -
                                    ${(pageNumber - 1) * pageSize + amountOfElements}
                            </div>
                        </ul>
                        <div class="row">
                            <div class="col s8">
                                <ul class="pagination">
                                    <c:choose>
                                        <c:when test="${pageNumber == 1}">
                                            <li class="disabled">
                                                <a href="#!" class="disabled" onclick="return false">
                                                    <i class="material-icons disabled">chevron_left</i>
                                                </a>
                                            </li>
                                        </c:when>
                                        <c:otherwise>
                                            <li class="waves-effect">
                                                <a href="#!" onclick="changePage(${pageNumber - 1})">
                                                    <i class="material-icons">chevron_left</i>
                                                </a>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:set var="just_one"
                                           value="${(pageNumber - 4) <= 0 || totalPages <= 10}"/>
                                    <c:set var="no_more_prev"
                                           value="${(pageNumber + 5) > totalPages}"/>
                                    <c:set var="the_first_ones"
                                           value="${(pageNumber + 5) < 10}"/>
                                    <c:set var="no_more_next"
                                           value="${totalPages < 10 || (pageNumber + 5) >= totalPages}"/>
                                    <c:forEach var="page"
                                               begin="${just_one ? 1 : no_more_prev ? totalPages - 9 : pageNumber - 4}"
                                               end="${no_more_next ? totalPages : the_first_ones ? 10 : pageNumber + 5}">
                                        <li class=${page == pageNumber ? "active" : "waves-effect"}>
                                            <a href="#!" onclick="changePage(${page})">
                                                ${page}
                                            </a>
                                        </li>
                                    </c:forEach>
                                    <c:choose>
                                        <c:when test="${pageNumber == totalPages}">
                                            <li class="disabled">
                                                <a href="#!" class="disabled" onclick="return false">
                                                    <i class="material-icons disabled">chevron_right</i>
                                                </a>
                                            </li>
                                        </c:when>
                                        <c:otherwise>
                                            <li class="waves-effect">
                                                <a href="#!" onclick="changePage(${pageNumber + 1})">
                                                    <i class="material-icons">chevron_right</i>
                                                </a>
                                            </li>
                                        </c:otherwise>
                                    </c:choose>
                                </ul>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>

<script type="text/javascript" src="<c:url value='/js/advanced-search.js' />"></script>


</body>
</html>


<script>
    $(document).ready(function () {

        $("#orderBooleanId").val("${orderBoolean}");
        $("#orderBooleanId").material_select();
        $("#orderSelectId").val("${orderCategory}");
        $("#orderSelectId").material_select();
        $("#pageSizeSelectId").val("${pageSizeUrl}");
        $("#pageSizeSelectId").material_select();
    });

    function changeOrderDropDown() {

        var selectedOrderCategory = document.getElementById("orderSelectId");
        var selectedOrderBoolean = document.getElementById("orderBooleanId");

        var name;
        var filters;
        var pageSize;

        <c:if test="${searchedName != null && !searchedName.equals('')}">
            name = "${searchedName}";
        </c:if>
        <c:if test="${filtersJson != null && !filtersJson.equals('')}">
            filters = JSON.stringify(${filtersJson});
        </c:if>
        var strOrderCategory = selectedOrderCategory.options[selectedOrderCategory.selectedIndex].value;
        var strOrderBoolean = selectedOrderBoolean.options[selectedOrderBoolean.selectedIndex].value;
        <c:if test="${pageSizeUrl != null && !pageSizeUrl.equals('')}">
            pageSize = "${pageSizeUrl}";
        </c:if>
        reload(name, filters, strOrderCategory, strOrderBoolean, pageSize, null);
    }

    function changePageSize() {
        var selectedPageSize = document.getElementById("pageSizeSelectId");

        var name;
        var filters;
        var orderCategory;
        var orderBoolean;
        <c:if test="${searchedName != null && !searchedName.equals('')}">
            name = "${searchedName}";
        </c:if>
        <c:if test="${filtersJson != null && !filtersJson.equals('')}">
            filters = JSON.stringify(${filtersJson});
        </c:if>
        <c:if test="${orderCategory != null && !orderCategory.equals('')}">
            orderCategory = "${orderCategory}";
        </c:if>
        <c:if test="${orderBoolean != null && !orderBoolean.equals('')}">
            orderBoolean = "${orderBoolean}";
        </c:if>
        var pageSize = selectedPageSize.options[selectedPageSize.selectedIndex].value;
        reload(name, filters, orderCategory, orderBoolean, pageSize, null);
    }

    function changePage(pageNumber) {
        var name, filters, orderCategory, orderBoolean, pageSize;

        <c:if test="${searchedName != null && !searchedName.equals('')}">
            name = "${searchedName}";
        </c:if>
        <c:if test="${filtersJson != null && !filtersJson.equals('')}">
            filters = JSON.stringify(${filtersJson});
        </c:if>
        <c:if test="${orderCategory != null && !orderCategory.equals('')}">
            orderCategory = "${orderCategory}";
        </c:if>
        <c:if test="${orderBoolean != null && !orderBoolean.equals('')}">
            orderBoolean = "${orderBoolean}";
        </c:if>
        <c:if test="${pageSizeUrl != null && !pageSizeUrl.equals('')}">
            pageSize = "${pageSizeUrl}";
        </c:if>
        reload(name, filters, orderCategory, orderBoolean, pageSize, pageNumber);
    }

    function reload(name, filters, orderCategory, orderBoolean, pageSize, pageNumber) {
        window.location = create_new_url(name, filters, orderCategory, orderBoolean, pageSize, pageNumber);
    }

    function create_new_url(name, filters, orderCategory, orderBoolean, pageSize, pageNumber) {
        var url = "/search";
        var params = ["name", "filters", "orderCategory", "orderBoolean", "pageSize", "pageNumber"];
        var values = [name, filters, orderCategory, orderBoolean, pageSize, pageNumber];
        return url + create_new_url_recursive(0, params, values, true);
    }


    function create_new_url_recursive(index, params, values, question_mark) {
        if (index == params.length) {
            return "";
        }
        var symbol = question_mark ? "?" : "&";
        var aux = "";
        if (values[index] != null && values[index] != "") {
            question_mark = false;
            aux = symbol + params[index] + "=" + encodeURIComponent(values[index]);
        }
        return aux + create_new_url_recursive(index + 1, params, values, question_mark);
    }

</script>