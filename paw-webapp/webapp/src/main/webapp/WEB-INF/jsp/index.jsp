<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <link href="<c:url value="/slick/slick.css" />" type="text/css" rel="stylesheet"/>
    <link href="<c:url value="/slick/slick-theme.css" />" type="text/css" rel="stylesheet"/>
    <title>PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>
    <div class="section no-pad-bot" id="index-banner">
        <div class="container">
            <br><br>
            <h1 class="header center orange-text">PowerUp</h1>
            <div class="row center">
                <h5 class="header col s12 light">Your videogame database and discovery platform</h5>
            </div>
            <div class="row">
                <div class="col s12 m8 offset-m2 l6 offset-l3">
                    <form id="search-form" action="<c:url value="/search" />" method="GET">
                        <div class="input-field">
                            <input name="name" type="search" required="required" id="indexSearchBar">
                            <%--<label for="search2"><i class="material-icons">search2</i></label>--%>
                            <i class="material-icons" style="color:black;" id="search-icon">search</i>
                        </div>
                    </form>
                </div>
            </div>
            <br />
            <br />
        </div>
    </div>

    <div class="container">
        <div class="section">
            <c:if test="${ fn:length( recommendedGames) > 0 }">
                <div class="row">
                    <h5 class="center">Recommended Games</h5>
                    <div class="slick-carousel">
                        <c:forEach var="game" items="${recommendedGames}">
                            <div>
                                <a href="<c:url value="/game?id=${game.id}"/>">
                                    <img data-lazy="${game.coverPictureUrl}" />
                                </a>
                                <h5 class="center" style="overflow-wrap: break-word;">
                                    <a style="color:black;" href="<c:url value="/game?id=${game.id}"/>">${game.name}</a>
                                </h5>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </div>
    </div>

    <div class="container">
        <div class="section">
            <!--   Icon Section   -->
            <div class="row">
                <div class="col s12 m4">
                    <div class="icon-block">
                        <h2 class="center light-blue-text"><i class="material-icons">search</i></h2>
                        <h5 class="center">Search</h5>
                        <p class="light">
                            Our extensive game database, powered by <a href="http://www.igdb.com" target="_blank">IGDB.com</a>, contains games
                            from every year, every console, every publisher, every genre, every developer that you could think of. Dive into
                            the little details of every game that you played in your childhood or plan to play in the future.
                        </p>
                    </div>
                </div>

                <div class="col s12 m4">
                    <div class="icon-block">
                        <h2 class="center light-blue-text"><i class="material-icons">visibility</i></h2>
                        <h5 class="center">Discover</h5>

                        <p class="light">
                            Discover new games based on your searches and game history. With our game suggestion engine
                            you will find so many cool games that you will wonder why you didn't come across them earlier.
                        </p>
                    </div>
                </div>

                <div class="col s12 m4">
                    <div class="icon-block">
                        <h2 class="center light-blue-text"><i class="material-icons">group</i></h2>
                        <h5 class="center">Compare (coming soon)</h5>

                        <p class="light">
                            Join the PowerUp community and compare game collections and interests. Maybe you'll find
                            someone who has played through every single released Mario game in history? How will your
                            collection compare to others'?
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
<script type="text/javascript" src="<c:url value="/js/index.js" />"></script>
<script type="text/javascript" src="<c:url value="/slick/slick.min.js" />"></script>
</body>
</html>
