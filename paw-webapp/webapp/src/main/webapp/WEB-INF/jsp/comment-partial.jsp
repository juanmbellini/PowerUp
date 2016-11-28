<%--
  Partial JSP that includes itself recursively to show part of a tree of comments and replies.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--<c:set var="root" value="${requestScope.attribute.root}" />--%>
<%--<c:set var="depth" value="${requestScope.attribute.depth}" />--%>

Included with depth ${depth}

<c:if test="${depth <= 5}">
    <ul class="collection">
        <c:forEach var="comment" items="${root}">
            <li class="collection-item avatar">
                <a name="${comment.id}"></a>
                <img src="http://placehold.it/200x200" alt="<c:out value="${comment.commenter.username}" />" class="circle">
                <span class="comment"><a href="<c:url value="/profile?username=${comment.commenter.username}" />"><c:out value="${comment.commenter.username}" /></a></span>
                <p>Submitted <fmt:formatDate value="${comment.createdAt.time}" type="both" /></p>
                <br />
                <p><c:out value="${comment.comment}" /></p>
                <a href="#!" class="secondary-content"><i class="material-icons">grade</i> likes</a>
                <br />
                <a href="#!" class="reply-link" data-comment-id="${comment.id}" data-form-shown="false">Reply</a>

                <%--Replies--%>
                <c:set var="depth" value="${depth+1}" scope="request" />


                <jsp:include page="comment-partial.jsp">
                    <jsp:param name="root" value="${comment.replies}" />
                    <jsp:param name="depth" value="${depth+1}" />
                </jsp:include>
            </li>
        </c:forEach>
    </ul>
</c:if>
