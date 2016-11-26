<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>Recent Threads - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main class="section">
    <div class="container row">
        <%--Title--%>
        <h1 class="center">PowerUp Threads</h1>

        <%--Threads--%>
        <div class="row">
            <c:choose>
                <c:when test="${fn:length(threads) == 0}">
                    <h5 class="center">No threads to show. Why not #create one?#</h5>
                </c:when>
                <c:otherwise>
                    <ul class="collection">
                        <c:forEach var="comment" items="${threads}">
                            <li class="collection-item avatar">
                                <img src="http://placehold.it/200x200" alt="<c:out value="${comment.creator.username}" />" class="circle">
                                <a class="title" href="<c:url value="/thread?id=${comment.id}" />"><c:out value="${comment.title}" /></a>
                                <p>Submitted <fmt:formatDate value="${comment.createdAt.time}" type="both" /> by <c:out value="${comment.creator.username}" /><br>
                                        <c:out value="${comment.initialComment}" />
                                </p>
                                <a href="#!" class="secondary-content"><i class="material-icons">grade</i> likes</a>
                            </li>
                        </c:forEach>
                    </ul>
                </c:otherwise>
            </c:choose>
        </div>
        <%--FAB--%>
        <div class="fixed-action-btn" style="bottom:10%;">
            <a id="create-thread-fab" class="btn-floating btn-large waves-effect waves-light red" href="<c:url value="/create-thread" />">
                <i class="large material-icons">mode_edit</i>
            </a>
        </div>
        <%--END FAB--%>
    </div>

</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>