<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>Advanced Search - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>
    <div class="container">
        <div class="section">
            <h1 class="center header orange-text">Advanced Search</h1>
            <div class="row">
                <form id="advanced-search-form" class="col s12">
                    <div class="row">
                        <div class="input-field col s12">
                            <input id="title" name="title" type="text" class="validate">
                            <label for="title">Title</label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s6">
                            <select name="platform" multiple>
                                <option value="" selected disabled>Any</option>
                                <c:forEach var="platform" items="${PLATFORMS}">
                                    <option value="${platform}">${platform}</option>
                                </c:forEach>
                            </select>
                            <label>Platform</label>
                        </div>
                        <div class="input-field col s6">
                            <select name="genre" multiple>
                                <option value="" selected disabled>Any</option>
                                <c:forEach var="genre" items="${GENRES}">
                                    <option value="${genre}">${genre}</option>
                                </c:forEach>
                            </select>
                            <label>Genre</label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s6">
                            <select name="developer" multiple>
                                <option value="" selected disabled>Any</option>
                                <c:forEach var="developer" items="${DEVELOPERS}">
                                    <option value="${developer}">${developer}</option>
                                </c:forEach>
                            </select>
                            <label>Developer</label>
                        </div>
                        <div class="input-field col s6">
                            <select name="publisher" multiple>
                                <option value="" selected disabled>Any</option>
                                <c:forEach var="publisher" items="${PUBLISHERS}">
                                    <option value="${publisher}">${publisher}</option>
                                </c:forEach>
                            </select>
                            <label>Publisher</label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="center">
                            <button class="btn-large waves-effect waves-light" type="submit">
                                Search
                                <i class="material-icons right">send</i>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
<script type="text/javascript" src="<c:url value='/js/advanced-search.js' />"></script>
</body>
</html>
