<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>${game.name} - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>
    <div class="container">
        <div class="section">
            <h1 class="header center orange-text">${game.name}</h1>
            <h5 class="center orange-text">${game.releaseDate.year}</h5>
        </div>
        <div class="section">
            <div class="row">
                <c:choose>
                    <c:when test="${game == null}">
                        <h3 class="center">No game found =(</h3>
                        <h5 class="center">Go back <a href="<c:url value='/' />">home</a></h5>
                    </c:when>
                    <c:otherwise>
                        <img class="col s3" src="https://myanimelist.cdn-dena.com/images/anime/9/21055.jpg" alt="">
                        <div class="col s6">
                            <p style="margin-top: 0;">
                                <c:choose>
                                    <c:when test="${empty game.summary}">No summary =(</c:when>
                                    <c:otherwise>${game.summary}</c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                        <div class="col s3">
                            <p style="margin-top:0;">10/10 m8</p>
                            <p><b>Genres</b></p>
                            <p>

                                <c:forEach var="genre" items="${game.genres}" varStatus="status">
                                    <a href="<c:url value="/search">
                                        <c:param name="name" value=""/>
                                        <c:param name="filters" value='{"genre":["${genre}"]}'/>
                                       </c:url>
                                    ">
                                        ${genre}
                                    </a>
                                    <c:if test="${!status.last}"><br /></c:if>
                                </c:forEach>
                            </p>
                            <p><b>Platforms</b></p>


                                <c:forEach var="platform" items="${game.platforms}" varStatus="status">
                                    <a href="<c:url value="/search">
                                        <c:param name="name" value=""/>
                                        <c:param name="filters" value='{"platform":["${platform}"]}'/>
                                       </c:url>
                                    ">
                                            ${platform} - {Release year}
                                    </a>
                                    <c:if test="${!status.last}"><br /></c:if>
                                </c:forEach>


                            <p><b>Developers</b></p>
                            <c:forEach var="developer" items="${game.developers}" varStatus="status">

                                <a href="<c:url value="/search">
                                        <c:param name="name" value=""/>
                                        <c:param name="filters" value='{"developer":["${developer}"]}'/>
                                       </c:url>
                                    ">
                                        ${developer}
                                </a>

                                <c:if test="${!status.last}"><br /></c:if>
                            </c:forEach>
                            <p><b>Publishers</b></p>
                            <c:forEach var="publisher" items="${game.publishers}" varStatus="status">
                                <a href="<c:url value="/search">
                                        <c:param name="name" value=""/>
                                        <c:param name="filters" value='{"publisher":["${publisher}"]}'/>
                                       </c:url>
                                    ">
                                        ${publisher}
                                </a>

                                <c:if test="${!status.last}"><br /></c:if>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>