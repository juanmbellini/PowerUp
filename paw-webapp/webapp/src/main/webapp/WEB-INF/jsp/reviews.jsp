<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <link href="<c:url value="/slick/slick.css" />" type="text/css" rel="stylesheet"/>
    <link href="<c:url value="/slick/slick-theme.css" />" type="text/css" rel="stylesheet"/>
    <title>${game.name} Reviews - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>
    <div class="container">
        <div class="section">
            <h2 class="header center orange-text">${game.name} Reviews</h2>
        </div>
        <%--REVIEWS--%>
        <div class="section">
            <div class="row">
                <c:choose>
                    <c:when test="${fn:length(reviews) > 0}">
                        <%--REVIEWS LIST--%>
                        <ul class="collection">
                            <c:forEach items="${reviews}" var="statusMap" varStatus="loopStatus">
                                <li class="collection-item avatar">
                                    <%--TODO: Fill with data--%>
                                    <h5><b>TODO: Fill with data</b></h5>
                                    <i class="material-icons circle blue">exit_to_app</i>
                                    <span class="title">Username</span>
                                    <p class="secondary-content" style="color: black;">Date</p>
                                    <p><a href="#!">Other reviews by &lt;username&gt;</a></p>
                                    <br/>
                                    <div class="row">
                                        <p class="col s10">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi facilisis
                                            condimentum elit, et posuere dui faucibus at. Nunc facilisis ligula viverra risus
                                            vestibulum, tincidunt lacinia justo finibus. Donec id pharetra purus, a lacinia sapien.
                                            Proin vel arcu ut ligula rhoncus gravida. Praesent augue ex, suscipit at iaculis vitae,
                                            posuere eu erat. Duis egestas enim non purus tempus vestibulum. Cras in sem ut metus
                                            luctus maximus posuere sit amet tellus. In varius non diam sed porttitor. Suspendisse ut
                                            ullamcorper est. Sed imperdiet ante diam, eget aliquet nisl mattis nec. Interdum et
                                            malesuada fames ac ante ipsum primis in faucibus. Vestibulum tempus consectetur tortor a
                                            blandit.</p>
                                        <div class="col s2">
                                            <p style="color: #26a69a;">
                                                Story: <span class="right">5</span>
                                                <br/>
                                                Graphics: <span class="right">7</span>
                                                <br/>
                                                Audio: <span class="right">6</span>
                                                <br/>
                                                Controls: <span class="right">2</span>
                                                <br/>
                                                Fun: <span class="right">5</span>
                                                <br/>
                                                <b>Overall: <span class="right">5</span></b>
                                            </p>
                                        </div>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <%--NO REVIEWS--%>
                        <h5 class="center">No reviews yet!
                            <c:if test="${!isLoggedIn}">
                                <a href="<c:url value="/register" />">Sign up</a> or <a href="<c:url value="/login" />">log in</a> to write the first one!
                            </c:if>
                        </h5>
                    </c:otherwise>
                </c:choose>

                <%--Review button TODO show this conditionally and link properly--%>
                <c:if test="${isLoggedIn}">
                    <div class="row">
                        <div class="col s12 center">
                            <a href="#!" class="center btn waves-effect waves-light offset-s4" style="">Write a Review <i class="material-icons right">send</i></a>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
        <%--END REVIEWS--%>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>