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
            <h5 class="center orange-text">{Score}</h5>
        </div>
        <div class="section">
            <div class="row">
                <img class="col s3" src="https://myanimelist.cdn-dena.com/images/anime/9/21055.jpg" alt="">
                <div class="col s6">
                    <p style="margin-top: 0;">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer bibendum urna sit amet est molestie pellentesque. Suspendisse sem diam, scelerisque lacinia blandit id, mollis id nulla. Ut eget scelerisque nulla. Nam et aliquet ante. Maecenas blandit consectetur mattis. Donec et dapibus orci, vehicula feugiat ipsum. Pellentesque ex ante, convallis id massa ut, vestibulum volutpat sem. Nam arcu arcu, posuere eu felis in, malesuada tincidunt ex. Curabitur pellentesque, erat non auctor tincidunt, lectus nisl mollis velit, ac aliquet nisl sem ut libero.
                    </p>
                </div>
                <div class="col s3">
                    <p style="margin-top:0;">Score</p>
                    <p><b>Genres</b></p>
                    <p>Genre 1, genre 2, genre 3, ...</p>
                    <p><b>Platforms</b></p>
                    <p>Console - Date 1, Date 2, ...</p>
                    <p>Console - Date 1, Date 2, ...</p>
                    <p><b>Developers</b></p>
                    <p>Developer 1, developer 2, ...</p>
                    <p><b>Publishers</b></p>
                    <p>Publisher 1, Publisher 2, ...</p>
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