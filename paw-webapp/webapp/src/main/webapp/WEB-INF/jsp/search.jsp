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
            <h1 class="header center orange-text">Results for ${searchedName}<c:if test="${hasFilters}"> with filters</c:if></h1>
        </div>
        <div class="section">
            <div class="row">
                <c:choose>
                    <c:when test="${ fn:length(results) == 0 }">
                        <h3 class="center">No results</h3>
                    </c:when>
                    <c:otherwise>
                        <ul class="collection" id="results">
                            <c:forEach var="game" items="${results}">
                                <li class="collection-item avatar col s12">
                                    <img class="col s2" src="https://myanimelist.cdn-dena.com/images/anime/9/21055.jpg"
                                         alt="">
                                    <div class="primary-content col s8">
                                        <p class="title"><a href="#!">${game.name}</a></p>
                                        <p>
                                            <c:forEach var="platform" items="${game.platforms}" varStatus="status">
                                                ${platform}<c:if test="!${status.last}"> | </c:if>
                                            </c:forEach>
                                        </p>
                                        <p>${game.release}</p>
                                    </div>
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

</body>
</html>