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
    <%--TODO link back to game (in new tab)--%>
    <div class="container">
        <div class="section">
            <h2 class="header center orange-text">Write a review for ${game.name}</h2>
        </div>
        <%--REVIEW FORM--%>
        <div class="section">
            <div class="row">
                <div class="col s12">
                    <c:url value="/write-review" var="postURL">
                        <c:param name="id" value="${game.id}" />
                    </c:url>
                    <form:form modelAttribute="reviewForm" action="${postURL}" method="post">
                        <div class="row">
                            <div class="col s9">
                                <div class="input-field">
                                    <i class="material-icons prefix">mode_edit</i>
                                    <form:textarea path="review" cssClass="materialize-textarea" length="10000" />
                                    <form:errors path="review" />
                                    <form:label path="review">Review</form:label>
                                </div>
                            </div>
                            <div class="col s3">
                                <c:set var="criteria" value="${['story', 'graphics', 'audio', 'controls', 'fun']}" />
                                <c:set var="scores" value="${[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]}" />
                                <c:forEach items="${criteria}" var="criterium">
                                    <p style="text-transform: capitalize;">${criterium}</p>
                                    <form:select path="${criterium}Score">
                                        <form:option disabled="true" selected="true" value="-1" label="--Please Select" />
                                        <form:options items="${scores}"/>
                                    </form:select>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="row center">
                            <button class="btn-large waves-effect waves-light" type="submit">
                                Submit <i class="material-icons right">send</i>
                            </button>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
        <%--REVIEW FORM--%>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
<script type="text/javascript">
    $(document).ready(function() {
        $('textarea').characterCounter();
    });
</script>
</body>
</html>