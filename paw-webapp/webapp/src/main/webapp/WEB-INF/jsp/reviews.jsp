<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <link href="<c:url value="/slick/slick.css" />" type="text/css" rel="stylesheet"/>
    <link href="<c:url value="/slick/slick-theme.css" />" type="text/css" rel="stylesheet"/>
    <title>
        <c:if test="${not empty game}">
            ${game.name}
        </c:if>
        Reviews
        <c:if test="${not empty user}">
            <%--TODO link to profile--%>
             by ${user.username}
        </c:if>
        - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>
    <%--TODO link back to game--%>
    <div class="container">
        <div class="section">
            <h2 class="header center orange-text">
                <c:if test="${not empty game}">
                    ${game.name}
                </c:if>
                Reviews
                <c:if test="${not empty user}">
                    <%--TODO link to profile--%>
                    by ${user.username}
                </c:if>
            </h2>
        </div>
        <%--REVIEWS--%>
        <div class="section">
            <div class="row">
                <c:choose>
                    <c:when test="${fn:length(reviews) > 0}">
                        <%--REVIEWS LIST--%>
                        <ul class="collection reviews-list">
                            <c:forEach items="${reviews}" var="review">
                                <li class="collection-item avatar">
                                    <i class="material-icons circle blue">exit_to_app</i>
                                    <span class="title">
                                        <c:choose>
                                            <c:when test="${empty user && not empty game}">
                                                <%--TODO link--%>
                                                <a href="#!">${review.user.username}</a>
                                            </c:when>
                                            <c:when test="${empty game && not empty user}">
                                                <a href="<c:url value="/game?id=${review.game.id}" />">${review.game.name}</a>
                                            </c:when>
                                            <c:otherwise>
                                                <%--TODO link user--%>
                                                <a href="<c:url value="/game?id=${review.game.id}" />">${review.game.name}</a> - <a href="#!">${review.user.username}</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                    <p class="secondary-content" style="color: black;">${review.date}</p>
                                    <p>
                                        <c:choose>
                                            <c:when test="${empty user && not empty game}">
                                                <a href="<c:url value="/reviews?userId=${review.user.id}" />">Other reviews by ${review.user.username}</a>
                                            </c:when>
                                            <c:when test="${empty game && not empty user}">
                                                <a href="<c:url value="/reviews?gameId=${review.game.id}" />">Other ${review.game.name} reviews</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="<c:url value="/reviews?gameId=${review.game.id}" />">Other ${review.game.name} reviews</a> - <a href="<c:url value="/reviews?userId=${review.user.id}" />">Other reviews by ${review.user.username}</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <br/>
                                    <div class="row">
                                        <p class="col s10">${review.review}</p>
                                        <div class="col s2">
                                            <p style="color: #26a69a;">
                                                Story: <span class="right">${review.storyScore}</span>
                                                <br/>
                                                Graphics: <span class="right">${review.graphicsScore}</span>
                                                <br/>
                                                Audio: <span class="right">${review.audioScore}</span>
                                                <br/>
                                                Controls: <span class="right">${review.controlsScore}</span>
                                                <br/>
                                                Fun: <span class="right">${review.funScore}</span>
                                                <br/>
                                                <b>Overall: <span class="right"><fmt:formatNumber value="${review.overallScore}" maxFractionDigits="2" /></span></b>
                                            </p>
                                        </div>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                        <%--END REVIEWS LIST--%>
                    </c:when>
                    <c:otherwise>
                        <%--NO REVIEWS--%>
                        <h5 class="center">No reviews yet!
                            <c:if test="${!isLoggedIn}">
                                <a href="<c:url value="/register" />">Sign up</a> or <a href="<c:url value="/login" />">log in</a> to write the first one!
                            </c:if>
                        </h5>
                    </c:otherwise>
                </c:choose>

                <%--Review button--%>
                <c:if test="${canSubmitReview}">
                    <div class="row">
                        <div class="col s12 center">
                            <a href="<c:url value="/write-review?id=${game.id}" />" class="center btn waves-effect waves-light offset-s4" style="">Write a Review <i class="material-icons right">send</i></a>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
        <%--END REVIEWS--%>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>