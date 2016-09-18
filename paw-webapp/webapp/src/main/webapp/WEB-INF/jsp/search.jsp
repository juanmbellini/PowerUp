<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>Results - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>
    <div class="container">
        <div class="section">
            <h1 class="header center orange-text">Results for {search}</h1>
        </div>
        <div class="section">
            <div class="row">
                <ul class="collection" id="results">
                    <li class="collection-item avatar col s12">
                        <img class="col s2" src="https://myanimelist.cdn-dena.com/images/anime/9/21055.jpg" alt="">
                        <div class="primary-content col s8">
                            <p class="title"><a href="#!">Dragon Ball Z</a></p>
                            <p>Console 1 | Console 2 | Console 3</p>
                            <p>Release year</p>
                        </div>
                        <div class="secondary-content">
                            <p class="rating-stars hide-on-small-and-down">
                                <i class="material-icons">star</i>
                                <i class="material-icons">star</i>
                                <i class="material-icons">star</i>
                                <i class="material-icons">star</i>
                                <i class="material-icons">star</i>
                            </p>
                            <p class="rating-number center"><b>10</b></p>
                        </div>
                    </li>
                    <li class="collection-item avatar col s12">
                        <img class="col s2" src="https://upload.wikimedia.org/wikipedia/en/a/ad/Black_cover_art.jpg" alt="">
                        <div class="primary-content col s8">
                            <p class="title"><a href="#!">Black</a></p>
                            <p>PS2</p>
                            <p>2005</p>
                        </div>
                        <div class="secondary-content">
                            <p class="rating-stars hide-on-small-and-down">
                                <i class="material-icons">star</i>
                                <i class="material-icons">star</i>
                                <i class="material-icons">star</i>
                                <i class="material-icons">star</i>
                                <i class="material-icons">star_border</i>
                            </p>
                            <p class="rating-number center"><b>8</b></p>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>

</body>
</html>