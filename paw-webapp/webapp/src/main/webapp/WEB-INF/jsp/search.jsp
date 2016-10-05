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
                <option value="avg-rating">avg-rating</option>
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
                <h6>avg-rating</h6>
            </div>
        </div>
        <div class="section ">
            <div class="row">
                <c:choose>
                    <c:when test="${ fn:length(results) == 0 }">
                        <h3 class="center">No results</h3>
                    </c:when>
                    <c:otherwise>
                        <ul class="collection" id="results">
                            <c:forEach var="game" items="${results}">
                                <li class="collection-item avatar col s12">
                                    <img class="col s2" src="${game.coverPictureUrl}" alt="${game.name}">
                                    <div class="primary-content col s8">
                                        <p class="title"><a
                                                href="<c:url value="/game?id=${game.id}" />">${game.name}</a></p>
                                        <p>
                                            <c:forEach var="platform" items="${game.platforms}" varStatus="status">
                                                ${platform} <c:if test="${!status.last}"> | </c:if>
                                            </c:forEach>
                                        </p>
                                    </div>
                                    <div class="primary-content col s2">
                                        <p>${game.releaseDate}</p>
                                        <div class="secondary-content">
                                            <p class="rating-stars hide-on-small-and-down">
                                                <i class="material-icons">star</i>
                                                <i class="material-icons">star</i>
                                                <i class="material-icons">star</i>
                                                <i class="material-icons">star</i>
                                                <i class="material-icons">star</i>
                                            </p>
                                            <p class="rating-number center"><b>10</b></p>
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