<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>Recent Threads - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main class="section">
    <div class="container row">
        <%--Title--%>
        <h1 class="center">PowerUp Threads</h1>

        <%--Threads--%>
        <div class="row">
            <c:choose>
                <c:when test="${fn:length(threads) == 0}">
                    <h5 class="center">
                        No threads to show.
                        <c:if test="${isLoggedIn}">
                            Why not <a href="<c:url value="/create-thread" />">create one?</a>
                        </c:if>
                    </h5>
                </c:when>
                <c:otherwise>
                    <div class="row">
                        <div class="col s12">
                            <a class="btn waves-effect waves-light<c:if test="${order != 'hot'}"> inactive</c:if>" href="<c:url value="/threads?order=hot"/>">Hottest</a>
                            <a class="btn waves-effect waves-light<c:if test="${order != 'newest'}"> inactive</c:if>" href="<c:url value="/threads?order=newest"/>">Newest</a>
                            <a class="btn waves-effect waves-light<c:if test="${order != 'best'}"> inactive</c:if>" href="<c:url value="/threads?order=best"/>">Highest Scoring</a>
                        </div>
                    </div>
                    <ul class="collection">
                        <c:forEach var="thread" items="${threads}">
                            <li class="collection-item avatar">
                                <img src="<c:url value="/profile-picture?username=${thread.creator.username}" />" alt="<c:out value="${thread.creator.username}" />" class="circle">
                                <a class="title truncate" href="<c:url value="/thread?id=${thread.id}" />"><c:out value="${thread.title}" /></a>
                                <p>Submitted <fmt:formatDate value="${thread.createdAt.time}" type="both" /> by <c:out value="${thread.creator.username}" /></p>
                                <br>
                                <p class="truncate">
                                    <c:out value="${thread.initialComment}" />
                                </p>
                                <%--Un/like section--%>
                                <span href="#!" class="secondary-content"><b>${thread.likeCount}</b>&nbsp;&nbsp;
                                    <c:choose>
                                        <c:when test="${isLoggedIn}">
                                            <c:choose>
                                                <c:when test="${thread.isLikedBy(currentUser)}">
                                                    <a href="#!" class="unlike-thread" data-thread-id="${thread.id}"><i class="material-icons green-text">thumb_up</i></a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="#!" class="like-thread" data-thread-id="${thread.id}"><i class="material-icons black-text">thumb_up</i></a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="material-icons black-text">thumb_up</i>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                                <%--End un/like section--%>
                            </li>
                        </c:forEach>
                    </ul>
                </c:otherwise>
            </c:choose>
        </div>
        <%--FAB--%>
        <c:if test="${isLoggedIn}">
            <div class="fixed-action-btn" style="bottom:10%;">
                <a id="create-thread-fab" class="btn-floating btn-large waves-effect waves-light red" href="<c:url value="/create-thread" />">
                    <i class="large material-icons">mode_edit</i>
                </a>
            </div>
        </c:if>
        <%--END FAB--%>
    </div>

</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
<script type="text/javascript">
    $(function() {
        $(".like-thread").on("click", function(event) {
            var threadId = $(this).data("thread-id");
            //Create an inline form and submit it to redirect with POST
            $("<form action='<c:url value="/like-thread" />' method='POST'> \
                <input type='hidden' name='threadId' value='" + threadId + "' /> \
                <input type='hidden' name='returnUrl' value='/threads#" + threadId + "' /> \
               </form>").submit();
        });

        $(".unlike-thread").on("click", function(event) {
            var threadId = $(this).data("thread-id");
            //Create an inline form and submit it to redirect with POST
            $("<form action='<c:url value="/unlike-thread" />' method='POST'> \
                <input type='hidden' name='threadId' value='" + threadId + "' /> \
                <input type='hidden' name='returnUrl' value='/threads#" + threadId + "' /> \
               </form>").submit();
        });
    });
</script>
</html>