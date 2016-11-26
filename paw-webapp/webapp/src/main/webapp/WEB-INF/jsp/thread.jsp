<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>${thread.title} - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main class="section">
    <div class="container row">
        <%--Title--%>
        <h1 class="center">${thread.title}</h1>

        <%--Original comment--%>
        <div class="row">
            <ul class="collection">
                <li class="collection-item avatar">
                    <img src="http://placehold.it/200x200" alt="${thread.title}" class="circle">
                    <span class="title">Submitted by ${thread.creator.username}</span>
                    <p>Submitted <fmt:formatDate value="${thread.createdAt.time}" type="both" /><br>
                        ${thread.initialComment}
                    </p>
                    <a href="#!" class="secondary-content"><i class="material-icons">grade</i> likes</a>
                </li>
            </ul>
        </div>

        <%--Top-level comments--%>
        <div class="row">
            <c:choose>
                <c:when test="${fn:length(thread.comments) == 0}">
                    <h5 class="center">No comments</h5>
                </c:when>
                <c:otherwise>
                    <ul class="collection">
                        <c:forEach var="comment" items="${thread.comments}" varStatus="status">
                            <li class="collection-item avatar">
                                <img src="http://placehold.it/200x200" alt="${comment.commenter}" class="circle">
                                <span class="title">${thread.commenter}</span>
                                <p>Submitted <fmt:formatDate value="${comment.createdAt.time}" type="both" /><br>
                                        ${thread.initialComment}
                                </p>
                                <a href="#!" class="secondary-content"><i class="material-icons">grade</i> likes</a>
                            </li>
                        </c:forEach>
                    </ul>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>