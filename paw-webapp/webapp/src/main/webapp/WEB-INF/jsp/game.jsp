<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>{Game title} - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>
    <div class="container">
        <div class="section">
            <h1 class="header center orange-text">{Game title}</h1>
            <h5 class="center orange-text">{year}</h5>
        </div>
        <div class="section">
            <div class="row">
                <img class="col s4" src="https://myanimelist.cdn-dena.com/images/anime/9/21055.jpg" alt="">
                <div class="col s8">
                    <p>{Description}</p>
                    <p>Some more data</p>
                </div>
            </div>
        </div>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>

</body>
</html>