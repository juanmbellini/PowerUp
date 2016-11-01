<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

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
    <div class="container row">
        <h1 class="center">${user.username}'s Game List</h1>
        <c:forEach items="${gameList}" var="gameList" varStatus="loopStatus">
            <h4>${gameList.key.pretty}</h4>
            <c:choose>
                <c:when test="${gameList.value.size() == 0}">
                    <div class="div_none">
                        <h5>None</h5>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="div_game_list">
                        <ul class="ul_game_list collection games-list">
                            <c:forEach items="${gameList.value}" var="gameScorePair">
                                <c:set value="${gameScorePair.key}" var="game" />
                                <c:set value="${gameScorePair.value}" var="score" />
                                <li class="collection-item avatar col s12">
                                    <div class="col s2 cover-pic-container">
                                        <img class="cover-picture"
                                             src="${game.coverPictureUrl}" alt="${game.name}">
                                    </div>
                                    <div class="col primary-content s7">
                                        <p class="title"><a
                                                href="<c:url value="/game?id=${game.id}" />">${game.name}</a></p>
                                    </div>
                                    <div class="col s1 center">
                                        <p style="margin-top: 33px;">${game.releaseDate.year}</p>
                                    </div>
                                    <div class="col s2">
                                        <div class="secondary-content">
                                            <c:choose>
                                                <c:when test="${score <= 10 && score>=0}">
                                                    <p class="rating-number center"><b>${score}</b></p>
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
                    </div>
                </c:otherwise>
            </c:choose>
            <c:if test="${!loopStatus.last && gameList.value.size() == 0}">
                <div class="col s12 divider"></div>
                <br/>
            </c:if>
        </c:forEach>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>