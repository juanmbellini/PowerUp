<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>${user.username}'s Game List - PowerUp</title>
    <%--TODO suffix ' instead of 's if username ends with s--%>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main class="section">
    <div class="container">
        <div class="row">
            <h1 class="center">${user.username}'s Shelves</h1>
            <c:forEach var="shelf" items="${shelves}" varStatus="loopStatus">
                <h3>${shelf.name}</h3>
                <c:choose>
                    <c:when test="${fn:length(shelf.games) == 0}">
                        <h5 class="center">No games here</h5>
                    </c:when>
                    <c:otherwise>
                        <ul class="ul_game_list collection games-list">
                            <c:forEach items="${game}" var="shelf.games">
                                <li class="collection-item avatar col s12">
                                    <div class="col s2 cover-pic-container valign-wrapper">
                                        <img class="cover-picture valign" src="${game.coverPictureUrl}" alt="${game.name}">
                                    </div>
                                    <div class="col primary-content s7">
                                        <p class="title"><a href="<c:url value="/game?id=${game.id}" />">${game.name}</a></p>
                                    </div>
                                    <div class="col s1 center">
                                        <p style="margin-top: 33px;">${game.releaseDate.year}</p>
                                    </div>
                                    <div class="col s2">
                                        <div class="secondary-content">
                                            <c:set value="${game.avgScore}" var="score" />
                                            <c:choose>
                                                <c:when test="${score <= 10 && score>=0}">
                                                    <p class="rating-number center"><b><fmt:formatNumber value="${score}" maxFractionDigits="2" /></b></p>
                                                    <p class="rating-stars hide-on-small-and-down">
                                                        <c:forEach begin="0" end="4" var="i">
                                                            <c:choose>
                                                                <c:when test="${score-(i*2)-1<0}">
                                                                    <i class="material-icons">star_border</i>
                                                                </c:when>
                                                                <c:when test="${score-(i*2)-1==0}">
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
                    </c:otherwise>
                </c:choose>
                <c:if test="${!loopStatus.last}">
                    <div class="col s12 divider"></div>
                    <br/>
                </c:if>
            </c:forEach>
            <c:if test="${fn:length(shelves) == 0}">
                <h4 class="center">No shelves. Why not create one?</h4>
            </c:if>
        </div>
        <div class="row">
            <form action="<c:url value="/create-shelf" />" method="POST">
                <input type="hidden" name="name" value="test" />
                <button type="submit" class="btn waves-effect waves-light">Test</button>
            </form>
        </div>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>