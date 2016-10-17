<%@ page import="ar.edu.itba.paw.webapp.utilities.Page" %>
<%@ page import="java.net.URLEncoder" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<%!

    private String createNewUrl(String name, String filters, String orderCategory,
                                String orderBoolean, String pageSize, String pageNumber) {
        String url = "/search";
        String[] params = {"name", "filters", "orderCategory", "orderBoolean", "pageSize", "pageNumber"};
        String[] values = {name, filters, orderCategory, orderBoolean, pageSize, pageNumber};
        return url + createNewUrlRecursive(0, params, values, true);
    }

    private String createNewUrlRecursive(int index, String[] params, String[] values, boolean question_mark) {
        if (index == params.length) {
            return "";
        }
        String symbol = question_mark ? "?" : "&";
        String aux = "";
        if (values[index] != null && values[index] != "") {
            question_mark = false;
            try {
                aux = symbol + params[index] + "=" + URLEncoder.encode(values[index], "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return aux + createNewUrlRecursive(index + 1, params, values, question_mark);
    }

%>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>Results - PowerUp</title>

    <!-- variables -->
    <c:set var="filtersJson" value="${pageContext.request.getParameter('filters')}" scope="request"/>
    <c:set var="pageSizeUrl" value="${pageContext.request.getParameter('pageSize')}" scope="request"/>

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
        <div class="section search-results">
            <div class="row filters-row">
                <div class="col s6">
                    <!-- Nothing here... may be used by future things -->
                </div>
                <div class="col s4">
                    <div class="row select-title">
                        Order by
                    </div>
                    <div class="row">
                        <select class="col s5 select-drop-down" id="orderSelectId" onchange="changeOrderDropDown()">
                            <option value="name">Name</option>
                            <option value="release">Release date</option>
                            <option value="rating">Rating</option>
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
                <div class="col s2">
                    <div class="row select-title">
                        Page size
                    </div>
                    <select class="row select-drop-down" id="pageSizeSelectId" onchange="changePageSize()">
                        <option value="25">25</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                        <c:if test="${pageSizeUrl != null && pageSizeUrl != 25 && pageSizeUrl != 50 && pageSizeUrl != 100}">
                            <option value="${pageSizeUrl}">Other: ${pageSizeUrl}</option>
                        </c:if>
                    </select>
                </div>

            </div>
            <div class="row">
                <c:choose>
                <c:when test="${ fn:length(page.data) == 0 }">
                    <h3 class="center">No results</h3>
                </c:when>
                <c:otherwise>
                <ul class="row games-count">
                    <div class="col s6">
                        Games ${(page.pageNumber - 1) * page.pageSize + 1 } -
                            ${(page.pageNumber - 1) * page.pageSize + page.amountOfElements}
                        of ${page.overAllAmountOfElements}
                    </div>
                </ul>
                <ul class="collection games-list">
                    <c:forEach var="game" items="${page.data}">
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
                        Games ${(page.pageNumber - 1) * page.pageSize + 1 } -
                            ${(page.pageNumber - 1) * page.pageSize + page.amountOfElements}
                        of ${page.overAllAmountOfElements}
                    </div>
                </ul>
                <div class="row">
                    <ul class="pagination center">
                        <c:choose>
                            <c:when test="${page.pageNumber == 1}">
                                <li class="disabled">
                                    <a class="disabled">
                                        <i class="material-icons disabled">chevron_left</i>
                                    </a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="waves-effect">
                                    <%
                                        request.setAttribute("prevPageLink",
                                                createNewUrl((String) request.getAttribute("searchedName"),
                                                        (String) request.getAttribute("filtersJson"),
                                                        (String) request.getAttribute("orderCategory"),
                                                        (String) request.getAttribute("orderBoolean"),
                                                        (String) request.getAttribute("pageSizeUrl"),
                                                        Integer.toString(((Page) request.getAttribute("page"))
                                                                .getPageNumber() - 1)));
                                    %>
                                    <a href="<c:url value="${prevPageLink}"/> ">
                                        <i class="material-icons">chevron_left</i>
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>
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

                                <!-- Makes it possible to be accessed within scriptlet -->
                                <c:set var="pageIt" value="${pageIt}" scope="request"/>

                                <%
                                    request.setAttribute("numberPageLink",
                                            createNewUrl((String) request.getAttribute("searchedName"),
                                                    (String) request.getAttribute("filtersJson"),
                                                    (String) request.getAttribute("orderCategory"),
                                                    (String) request.getAttribute("orderBoolean"),
                                                    (String) request.getAttribute("pageSizeUrl"),
                                                    Integer.toString((int) request.getAttribute("pageIt"))));
                                %>
                                <c:choose>
                                    <c:when test="${pageIt == page.pageNumber}">
                                        <a>
                                                ${pageIt}
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="<c:url value="${numberPageLink}"/>">
                                                ${pageIt}
                                        </a>
                                    </c:otherwise>
                                </c:choose>

                            </li>
                        </c:forEach>
                        <c:choose>
                            <c:when test="${page.pageNumber == page.totalPages}">
                                <li class="disabled">
                                    <a class="disabled">
                                        <i class="material-icons disabled">chevron_right</i>
                                    </a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="waves-effect">
                                    <%
                                        request.setAttribute("nextPageLink",
                                                createNewUrl((String) request.getAttribute("searchedName"),
                                                        (String) request.getAttribute("filtersJson"),
                                                        (String) request.getAttribute("orderCategory"),
                                                        (String) request.getAttribute("orderBoolean"),
                                                        (String) request.getAttribute("pageSizeUrl"),
                                                        Integer.toString(((Page) request.getAttribute("page"))
                                                                .getPageNumber() + 1)));
                                    %>
                                    <a href="<c:url value="${nextPageLink}"/> ">
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

        <c:choose>
        <c:when test="${orderCategory == null}">
        <c:set var="selectedOrderCategory" value="name"/>
        </c:when>
        <c:otherwise>
        <c:set var="selectedOrderCategory" value="${orderCategory}"/>
        </c:otherwise>
        </c:choose>

        <c:choose>
        <c:when test="${orderBoolean == null}">
        <c:set var="selectedOrderBoolean" value="ascending"/>
        </c:when>
        <c:otherwise>
        <c:set var="selectedOrderBoolean" value="${orderBoolean}"/>
        </c:otherwise>
        </c:choose>

        <c:choose>
        <c:when test="${pageSizeUrl == null}">
        <c:set var="selectedPageSizeUrl" value="25"/>
        </c:when>
        <c:otherwise>
        <c:set var="selectedPageSizeUrl" value="${pageSizeUrl}"/>
        </c:otherwise>
        </c:choose>

        $("#orderBooleanId").val("${selectedOrderBoolean}");
        $("#orderBooleanId").material_select();
        $("#orderSelectId").val("${selectedOrderCategory}");
        $("#orderSelectId").material_select();
        $("#pageSizeSelectId").val("${selectedPageSizeUrl}");
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


    function reload(name, filters, orderCategory, orderBoolean, pageSize, pageNumber) {
        var url = "<%= request.getContextPath() %>" + "/search";
        var params = ["name", "filters", "orderCategory", "orderBoolean", "pageSize", "pageNumber"];
        var values = [name, filters, orderCategory, orderBoolean, pageSize, pageNumber];

        window.location = url + create_new_url(0, params, values, true);
    }


    function create_new_url(index, params, values, question_mark) {
        if (index == params.length) {
            return "";
        }
        var symbol = question_mark ? "?" : "&";
        var aux = "";
        if (values[index] != null && values[index] != "") {
            question_mark = false;
            aux = symbol + params[index] + "=" + encodeURIComponent(values[index]);
        }
        return aux + create_new_url(index + 1, params, values, question_mark);
    }

</script>