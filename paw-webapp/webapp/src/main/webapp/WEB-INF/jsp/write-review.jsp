<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <link href="<c:url value="/slick/slick.css" />" type="text/css" rel="stylesheet"/>
    <link href="<c:url value="/slick/slick-theme.css" />" type="text/css" rel="stylesheet"/>
    <title>Write a Review for ${game.name} - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>
    <div class="container">
        <div class="section">
            <h2 class="header center orange-text">Write a review for ${game.name}</h2>
        </div>
        <%--REVIEW FORM--%>
        <div class="section">
            <div class="row">
                <div class="col s12">
                    <h5 class="center">Write a Review</h5>
                    <form>
                        <div class="row">
                            <div class="input-field col s10 offset-s1">
                                <i class="material-icons prefix">mode_edit</i>
                                <textarea id="icon_prefix2" class="materialize-textarea"></textarea>
                                <label for="icon_prefix2">First Name</label>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <%--REVIEW FORM--%>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>