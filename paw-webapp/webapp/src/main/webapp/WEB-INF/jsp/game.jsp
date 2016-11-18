<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <link href="<c:url value="/slick/slick.css" />" type="text/css" rel="stylesheet"/>
    <link href="<c:url value="/slick/slick-theme.css" />" type="text/css" rel="stylesheet"/>
    <title>${game.name} - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>
    <div class="container">
        <div class="section">
            <h1 class="header center orange-text">${game.name}</h1>
            <h5 class="center orange-text">${game.releaseDate.year}</h5>
        </div>
        <%--Rate and status form if logged in--%>
        <div class ="section">
            <c:url value="/rateAndUpdateStatus?id=${game.id}" var="postPath"/>
            <form:form modelAttribute="rateAndStatusForm" action="${isLoggedIn ? postPath : ''}" method="post" class="center-align" id="rateAndStatusForm">
                <div class="row" >
                    <div class="col s3">
                    </div>
                    <div class="col s6 center-align">
                        <div class="row">

                            <div class="col s4 center-align">
                                <form:select path="score" id="score">
                                    <form:option value="" label="Select score"/>
                                    <form:options items="${scoreValues}"/>
                                </form:select>
                                <form:label path="score">Score: </form:label>
                                    <%--<form:input type="text" path="score"/>--%>
                                <form:errors path="score" cssClass="formError" element="p" Style="size: 1px"/>
                            </div>

                            <div class="col s4 center-align">
                                <form:select path="playStatus" id="status">
                                    <form:option value="" label="Select status"/>
                                    <form:options items="${statuses}"/>
                                </form:select>
                                <form:label path="playStatus">PlayStatus: </form:label>
                                    <%--<form:input type="playStatus" path="playStatus" />--%>
                                <form:errors path="playStatus" cssClass="formError" element="p"/>
                            </div>

                            <c:choose>
                                <c:when test="${isLoggedIn}">
                                    <div class="col s4 center">
                                        <button id="submit" type="submit" class="btn waves-effect waves-light">Update List!</button>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col s4 center">
                                        <%--TODO redirect user back  here after login--%>
                                        <a class="btn waves-effect waves-light" href="<c:url value="/login" />">Log in</a>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </form:form>
        </div>
        <div class="section">
            <c:choose>
                <c:when test="${game == null}">
                    <div class="row">
                        <h3 class="center">No game found =(</h3>
                        <h5 class="center">Go back <a href="<c:url value='/' />">home</a></h5>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row">
                        <img class="col s3" src="${game.coverPictureUrl}" alt="${game.name}">
                        <div class="col s5">
                            <p style="margin-top: 0;">
                                <c:choose>
                                    <c:when test="${empty game.summary}">No summary =(</c:when>
                                    <c:otherwise>${game.summary}</c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                        <div class="col s4">
                            <p><b>Rating</b></p>
                            <c:choose>
                                <c:when test="${game.avgScore>0}">
                                    <p style="margin-top:0;"><fmt:formatNumber value="${game.avgScore}" maxFractionDigits="2" /></p>
                                </c:when>
                                <c:otherwise>
                                    <p style="margin-top:0;">unrated</p>
                                </c:otherwise>
                            </c:choose>

                            <p><b>Genres</b></p>
                            <p>
                                <c:forEach var="genre" items="${genres}" varStatus="status">
                                    <a href="<c:url value="/search">
                                        <c:param name="name" value=""/>
                                        <c:param name="filters" value='{"genre":["${genre.name}"]}'/>
                                       </c:url>
                                    ">
                                    ${genre.name}
                                    </a>
                                    <c:if test="${!status.last}"><br /></c:if>
                                </c:forEach>
                            </p>
                            <p><b>Platforms</b></p>
                                <c:forEach var="platformEntry" items="${platforms}" varStatus="status">
                                    <a href="<c:url value="/search">
                                        <c:param name="name" value="" />
                                        <c:param name="filters" value='{"platform":["${platformEntry.key.name}"]}'/>
                                       </c:url>
                                    ">${platformEntry.key.name}</a><span style="font-size: small; float: right;">${platformEntry.value.releaseDate}</span>
                                    <c:if test="${!status.last}"><div class="col s12 divider"></div><br /></c:if>
                                </c:forEach>
                            <p><b>Developers</b></p>
                            <c:forEach var="developer" items="${developers}" varStatus="status">
                                <a href="<c:url value="/search">
                                        <c:param name="name" value=""/>
                                        <c:param name="filters" value='{"developer":["${developer.name}"]}'/>
                                       </c:url>
                                    ">
                                        ${developer.name}
                                </a>
                                <c:if test="${!status.last}"><br /></c:if>
                            </c:forEach>
                            <p><b>Publishers</b></p>
                            <c:forEach var="publisher" items="${publishers}" varStatus="status">
                                <a href="<c:url value="/search">
                                        <c:param name="name" value=""/>
                                        <c:param name="filters" value='{"publisher":["${publisher.name}"]}'/>
                                       </c:url>
                                    ">
                                        ${publisher.name}
                                </a>

                                <c:if test="${!status.last}"><br /></c:if>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col s12 divider"></div>
                    </div>
                    <c:if test="${ fn:length( relatedGames) > 0 }">
                        <div class="row">
                            <h5 class="center">Related Games</h5>
                            <div class="slick-carousel">
                                <c:forEach var="game" items="${relatedGames}">
                                    <div class="slide-container">
                                        <div class="valign-wrapper slide-image">
                                            <a href="<c:url value="/game?id=${game.id}"/>">
                                                <img data-lazy="${game.coverPictureUrl}" class="valign"/>
                                            </a>
                                        </div>
                                        <h5 class="center slide-text">
                                            <a href="<c:url value="/game?id=${game.id}"/>">${game.name}</a>
                                        </h5>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
<script type="text/javascript" src="<c:url value="/slick/slick.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/game.js" />"></script>
</body>
</html>