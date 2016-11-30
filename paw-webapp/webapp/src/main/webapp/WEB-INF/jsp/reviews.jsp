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
             by <c:out value="${user.username}" />
        </c:if>
        - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>
    <div class="container">
        <div class="section no-pad-bot">
        <%--TITLE--%>
            <h2 class="header center orange-text wrap-text">
                <c:if test="${not empty game}">
                    <a href="<c:url value="/game?id=${game.id}" />">${game.name}</a>
                </c:if>
                Reviews
                <c:if test="${not empty user}">
                    by <a href="<c:url value="/profile?username=${user.username}" />"><c:out value="${user.username}" /> <img class="circle" style="width:50px; height:50px; vertical-align: middle;" src="<c:url value="/profile-picture?username=${user.username}" />" /></a>
                </c:if>
            </h2>
            <%--PAGE SIZE--%>
            <c:if test="${reviews.overAllAmountOfElements > 0}">
                <div class="row">
                    <div class="input-field col s2 offset-s5">
                        <select id="pageSize">
                            <c:forEach var="pageSize" items="${pageSizes}">
                                <option ${reviews.pageSize == pageSize ? "selected" : ""}>${pageSize}</option>
                            </c:forEach>
                        </select>
                        <label>Reviews per page</label>
                    </div>
                </div>
            </c:if>
        </div>
        <%--REVIEWS--%>
        <div class="section no-pad-top">
            <div class="row">
                <c:choose>
                    <c:when test="${reviews.overAllAmountOfElements > 0}">
                        <%--REVIEWS LIST--%>
                        <ul class="collection reviews-list">
                            <c:forEach items="${reviews.data}" var="review">
                                <li class="collection-item avatar">
                                    <c:choose>
                                        <c:when test="${empty user && not empty game}">
                                            <%--Viewing reviews by game, variable is user--%>
                                            <img src="<c:url value="/profile-picture?username=${review.user.username}" />" alt="<c:out value="${review.user.username}" />" class="circle">
                                            <span class="title wrap-text">
                                                <a href="<c:url value="/profile?username=${user.username}" />"><c:out value="${review.user.username}" /></a>
                                            </span>
                                        </c:when>
                                        <c:when test="${empty game && not empty user}">
                                            <%--Viewing reviews by user, variable is game--%>
                                            <img src="<c:url value="${review.game.coverPictureUrl}" />" alt="<c:out value="${review.game.name}" />" class="circle">
                                            <span class="title wrap-text">
                                                <a href="<c:url value="/game?id=${review.game.id}" />">${review.game.name}</a>
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <%--Viewing reviews by game and user, show user in picture but provide links to both game and user--%>
                                            <img src="<c:url value="/profile-picture?username=${review.user.username}" />" alt="<c:out value="${review.user.username}" />" class="circle">
                                            <span class="title wrap-text">
                                                <a href="<c:url value="/game?id=${review.game.id}" />">${review.game.name}</a> | <a href="<c:url value="/profile?username=${review.user.username}" />"><c:out value="${review.user.username}" /></a>
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                    <p class="secondary-content" style="color: black;">${review.date}</p>
                                    <p class="wrap-text">
                                        <c:choose>
                                            <c:when test="${empty user && not empty game}">
                                                <a href="<c:url value="/reviews?userId=${review.user.id}" />">Other reviews by <c:out value="${review.user.username}" /></a>
                                            </c:when>
                                            <c:when test="${empty game && not empty user}">
                                                <a href="<c:url value="/reviews?gameId=${review.game.id}" />">Other ${review.game.name} reviews</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="<c:url value="/reviews?gameId=${review.game.id}" />">Other ${review.game.name} reviews</a> | <a href="<c:url value="/reviews?userId=${review.user.id}" />">Other reviews by <c:out value="${review.user.username}" /></a>
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <br/>
                                    <div class="row">
                                        <p class="col s10 preserve-newlines wrap-texxt"><c:out value="${review.review}" /></p>
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
                        <%--PAGINATION--%>
                        <c:if test="${reviews.totalPages > 1}">
                            <div class="row">
                                <ul class="pagination center">
                                        <%--Left chevron - only shown when there are > 1 pages --%>
                                    <c:if test="${reviews.totalPages > 1}">
                                        <c:choose>
                                            <c:when test="${reviews.pageNumber > 1}">
                                                <li class="waves-effect">
                                                    <a href="${changePageUrl}pageNumber=${reviews.pageNumber - 1}&pageSize=${reviews.pageSize}">
                                                        <i class="material-icons">chevron_left</i>
                                                    </a>
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <li class="disabled">
                                                    <a href="#!">
                                                        <i class="material-icons">chevron_left</i>
                                                    </a>
                                                </li>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                        <%--Page numbers - always shown--%>
                                    <c:set var="just_one"
                                           value="${(reviews.pageNumber - 4) <= 0 || reviews.totalPages <= 10}"/>
                                    <c:set var="no_more_prev"
                                           value="${(reviews.pageNumber + 5) > reviews.totalPages}"/>
                                    <c:set var="the_first_ones"
                                           value="${(reviews.pageNumber + 5) < 10}"/>
                                    <c:set var="no_more_next"
                                           value="${reviews.totalPages < 10 || (reviews.pageNumber + 5) >= reviews.totalPages}"/>
                                    <c:forEach var="pageIt"
                                               begin="${just_one ? 1 : no_more_prev ? reviews.totalPages - 9 : reviews.pageNumber - 4}"
                                               end="${no_more_next ? reviews.totalPages : the_first_ones ? 10 : reviews.pageNumber + 5}">
                                        <li class=${pageIt == reviews.pageNumber ? "active" : "waves-effect"}>
                                            <c:choose>
                                                <c:when test="${pageIt == reviews.pageNumber}">
                                                    <a>
                                                            ${pageIt}
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="${changePageUrl}pageNumber=${pageIt}&pageSize=${reviews.pageSize}">
                                                            ${pageIt}
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                        </li>
                                    </c:forEach>
                                        <%--Right chevron - only shown if there are > 1 pages--%>
                                    <c:if test="${reviews.totalPages > 1}">
                                        <c:choose>
                                            <c:when test="${reviews.pageNumber == reviews.totalPages}">
                                                <li class="disabled">
                                                    <a href="#!">
                                                        <i class="material-icons">chevron_right</i>
                                                    </a>
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <li class="waves-effect">
                                                    <a href="${changePageUrl}pageNumber=${reviews.pageNumber + 1}&pageSize=${reviews.pageSize}">
                                                        <i class="material-icons">chevron_right</i>
                                                    </a>
                                                </li>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </ul>
                            </div>
                        </c:if>
                        <%--END PAGINATION--%>
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
<script type="text/javascript">
    $(function() {
        $("#pageSize").on("change", function() {
            window.location = "${changePageUrl}pageSize=" + $(this).val();
        });
    });
</script>
</body>
</html>