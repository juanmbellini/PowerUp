<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>Starter Template - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>

    <ul>
        <c:forEach var="game" items="${recommendedGames}">
            <li>
            ${game}
            </li>
        </c:forEach>
    </ul>

</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>