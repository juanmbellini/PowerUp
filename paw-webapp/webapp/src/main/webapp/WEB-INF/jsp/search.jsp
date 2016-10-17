<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>Results - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>
    <div class="container">
        <div class="section">
            <h1 class="header center orange-text">Results for ${searchedName}<c:if test="${hasFilters}"> with
                filters</c:if></h1>
        </div>
        <div class="row">
            <select class="col s2" id="orderSelectId" onchange="reload()">
                <option value="name">name</option>
                <option value="release date">release date</option>
                <option value="avg-score">avg-score</option>
            </select>
            <select class="col s2" id="orderBooleanId" onchange="reload()">
                <option value="ascending">ascending</option>
                <option value="descending">descending</option>
            </select>
        </div>
        <div class="row">
            <div class="col s4 center">
                <h6>name</h6>
            </div>
            <div class="col s6 center">
                <h6>release date</h6>
            </div>
            <div class="col 2 center">
                <h6>avg-score</h6>
            </div>
        </div>
        <div class="section ">
            <div class="row">
                <c:choose>
                    <c:when test="${ fn:length(results) == 0 }">
                        <h3 class="center">No results</h3>
                    </c:when>
                    <c:otherwise>
                        <ul class="collection games-list">
                            <c:forEach var="game" items="${results}">
                                <li class="collection-item avatar col s12">
                                    <div class="col s2" style="padding: 0;">
                                        <img src="${game.coverPictureUrl}" alt="${game.name}" style="max-width: 100%; height: auto;">
                                    </div>
                                    <div class="col primary-content s7">
                                        <p class="title"><a href="<c:url value="/game?id=${game.id}" />">${game.name}</a></p>
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
    $( document ).ready(function() {
        $("#orderBooleanId").val("${orderBoolean}");
        $("#orderBooleanId").material_select();
        $("#orderSelectId").val("${orderCategory}");
        $("#orderSelectId").material_select();
    });

    function reload() {


        var selectOrderCategory = document.getElementById("orderSelectId");
        var strOrderCategory = selectOrderCategory.options[selectOrderCategory.selectedIndex].value;

        var selectOrderBoolean = document.getElementById("orderBooleanId");
        var strOrderBoolean = selectOrderBoolean.options[selectOrderBoolean.selectedIndex].value;

        var URL = "/search?name=" + encodeURIComponent("${searchedName}") + "&";
        URL += "filters=" + encodeURIComponent(JSON.stringify(${filters}));
        URL += "&orderCategory=" + encodeURIComponent(strOrderCategory);
        URL += "&orderBoolean=" + encodeURIComponent(strOrderBoolean);
        window.location = URL;
    }

</script>