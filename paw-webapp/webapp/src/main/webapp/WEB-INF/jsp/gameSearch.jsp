<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%--TODO verify taglib prefix--%>

<html><%-- fn:length( gameList)--%>
<body>
<h2>Game Search!</h2>
<br />
<c:choose>
    <c:when test="${ fn:length( gameList)<= 0 }">
       No results
        <br />
    </c:when>
    <c:otherwise>
        <c:forEach var="game" items="${gameList}">
            <c:out value="${game.name}" />
            <br />
            <c:out value="${game.summary}" />
            <br />
        </c:forEach>

    </c:otherwise>
</c:choose>


</body>
</html>