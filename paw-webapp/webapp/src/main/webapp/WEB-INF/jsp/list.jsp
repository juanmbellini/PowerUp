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
    <div class="container">
        <h1>${user.username}'s Game List</h1>
        <c:forEach items="${playStatuses}" var="statusMap">
            ${statusMap.key}
            <br>
            <c:if test="${statusMap.value.size()==0}">
            <div class="div_none">
                -None-
            </div>
            </c:if>
            <div class="div_game_list">
                <ul class="ul_game_list">
                    <c:forEach items="${statusMap.value}" var="game">
                        <li>Game: ${game.name} , Score: ${user.scoredGames.get(game.id)}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:forEach>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>