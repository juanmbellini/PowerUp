<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>${user.username} - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main class="section container">
    <h1 class="center orange-text">${user.username}'s Profile</h1>
    <div class="row">
        <div class="col s4">
            <%--TODO replace with actual profile picture and allow changes "http://placehold.it/250x250" --%>
            <img src="<c:url value="/profile-picture?username=${user.username}"></c:url>" style="max-width: 100%; height:auto;"/>
            <br /> <br />
            <h5>Profile Stats</h5>
            <p>Played <b>${playedGames.size()}</b> game${playedGames.size() == 1 ? "" : "s"}</p>
            <p>Playing <b>${playingGames.size()}</b> game${playingGames.size() == 1 ? "" : "s"}</p>
            <p>Plans to play <b>${planToPlayGames.size()}</b> game${planToPlayGames.size() == 1 ? "" : "s"}</p>
        </div>
        <div class="col s8">
            <h4 class="center">Top 10 games</h4>
            <c:choose>
                <c:when test="${fn:length(topGames) == 0}">
                    <p class="center">No scored games. Why don't you <a href="<c:url value="/search"/>">search</a> and score some?</p>
                </c:when>
                <c:otherwise>
                    <ul class="collection games-list">
                        <c:forEach var="entry" items="${topGames}">
                            <c:set var="game" value="${entry.key}" />
                            <c:set var="score" value="${entry.value}" />
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
                                        <p class="rating-number center"><b><fmt:formatNumber value="${score}" maxFractionDigits="0" /></b></p>
                                        <p class="rating-stars hide-on-small-and-down">
                                            <c:forEach begin="0" end="4" var="i">
                                                <c:choose>
                                                    <c:when test="${score - (i * 2) - 1 < 0}">
                                                        <i class="material-icons">star_border</i>
                                                    </c:when>
                                                    <c:when test="${score - (i * 2) - 1 == 0}">
                                                        <i class="material-icons">star_half</i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="material-icons">star</i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </p>
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <c:url value="/upload-file" var="uploadFileUrl" />
    <%--<form method="POST" action="${uploadFileUrl}" enctype="multipart/form-data">--%>

        <%--&lt;%&ndash;<form:errors path="*" cssClass="errorblock" element="div" />&ndash;%&gt;--%>

        <%--<input type='hidden' name='username' value='${user.username}'/>--%>
        <%--Please select a file to upload : <input type="file" name="file" accept=".jpg,.png,.gif" />--%>
        <%--<input type="submit" value="upload" />--%>
    <%--&lt;%&ndash;<span><form:errors path="file" cssClass="error" />&ndash;%&gt;--%>
		<%--&lt;%&ndash;</span>&ndash;%&gt;--%>

    <%--</form>--%>

    <form method="POST" action="${uploadFileUrl}" enctype="multipart/form-data">
        File to upload: <input type="file" name="file" accept=".jpg,.png,.gif">

        <input type='hidden' name='username' value='${user.username}'/>

        <input type="submit" value="Upload" > Press here to upload the file!
    </form>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>