<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title><c:out value="${thread.title}" /> - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main class="section">
    <div class="container row">
        <%--Title--%>
        <h1 class="center wrap-text"><c:out value="${thread.title}" /></h1>

        <%--Original comment--%>
        <div class="row">
            <ul class="collection">
                <li class="collection-item avatar">
                    <img src="http://placehold.it/200x200" alt="<c:out value="${thread.title}" />" class="circle">
                    <span class="title wrap-text">Submitted by <c:out value="${thread.creator.username}" /></span>
                    <p><fmt:formatDate value="${thread.createdAt.time}" type="both" /></p>
                    <br />
                    <p class="preserve-newlines wrap-text"><c:out value="${thread.initialComment}" /></p>
                    <%--Thread action links--%>
                    <c:if test="${isLoggedIn && thread.creator.id == currentUser.id}">
                        <div class="action-links">
                            <%--Rename link--%>
                            <a href="#!" class="edit-thread-title-link" data-thread-id="${thread.id}" data-thread-title="${thread.title}">Change Title</a>
                            <%--Edit link--%>
                            | <a href="#!" class="edit-thread-initial-comment-link" data-thread-id="${thread.id}" data-thread-initial-comment="<c:out value="${thread.initialComment}"/>" data-form-shown="false">Edit Comment</a>
                            <%--Delete link--%>
                            | <a href="#!" class="delete-thread-link" data-thread-id="${thread.id}">Delete</a>
                        </div>
                        <div class="action-form"></div>
                    </c:if>
                    <%--Un/like thread section--%>
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
                    <%--End un/like thread section--%>
                </li>
            </ul>
        </div>

        <div class="row col s12 divider"></div>

        <%--COMMENTS--%>
        <div class="row">
            <c:choose>
                <c:when test="${fn:length(thread.allComments) == 0}">
                    <h5 class="center">No comments</h5>
                </c:when>
                <c:otherwise>
                    <h5 class="center">All ${fn:length(thread.allComments)} comments</h5>
                    <%--TOP-LEVEL COMMENTS and replies - recursive JSP--%>
                    <ul class="collection">
                        <c:forEach var="comment" items="${thread.topLevelComments}">
                            <li class="collection-item avatar">
                                <a name="${comment.id}"></a>
                                <img src="http://placehold.it/200x200"
                                     alt="<c:out value="${comment.commenter.username}" />" class="circle">
                                <span class="title wrap-text">
                                    <a href="<c:url value="/profile?username=${comment.commenter.username}" />">
                                        <c:out value="${comment.commenter.username}"/>
                                    </a>
                                </span>
                                <p>Submitted <fmt:formatDate value="${comment.createdAt.time}" type="both"/></p>
                                <br/>
                                <p class="preserve-newlines wrap-text"><c:out value="${comment.comment}"/></p>
                                <br/>
                                <%--Comment action links--%>
                                <c:if test="${isLoggedIn}">
                                    <div class="action-links">
                                        <%--Reply link--%>
                                        <a href="#!" class="reply-link" data-comment-id="${comment.id}" data-form-shown="false">Reply</a>
                                        <%--Edit link--%>
                                        <c:if test="${comment.commenter.id == currentUser.id}">
                                            | <a href="#!" class="edit-comment-link" data-comment-id="${comment.id}" data-comment="<c:out value="${comment.comment}"/>" data-form-shown="false">Edit</a>
                                        </c:if>
                                        <%--Delete link--%>
                                        <c:if test="${comment.commenter.id == currentUser.id}">
                                            | <a href="#!" class="delete-comment-link" data-comment-id="${comment.id}" data-form-shown="false">Delete</a>
                                        </c:if>
                                    </div>
                                    <div class="action-form"></div>
                                </c:if>
                                <%--Un/like comment section--%>
                                <span href="#!" class="secondary-content"><b>${comment.likeCount}</b>&nbsp;&nbsp;
                                    <c:choose>
                                        <c:when test="${isLoggedIn}">
                                            <c:choose>
                                                <c:when test="${comment.isLikedBy(currentUser)}">
                                                    <a href="#!" class="unlike-comment" data-comment-id="${comment.id}"><i class="material-icons green-text">thumb_up</i></a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="#!" class="like-comment" data-comment-id="${comment.id}"><i class="material-icons black-text">thumb_up</i></a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="material-icons black-text">thumb_up</i>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                                <%--End un/like comment section--%>
                                
                                <%--Replies--%>
                                <c:if test="${fn:length(comment.replies) > 0}">
                                    <ul class="collection">
                                        <c:forEach var="reply" items="${comment.replies}">
                                            <li class="collection-item avatar">
                                                <a name="${reply.id}"></a>
                                                <img src="http://placehold.it/200x200"
                                                     alt="<c:out value="${reply.commenter.username}" />"
                                                     class="circle">
                                                <span class="title wrap-text">
                                                    <a href="<c:url value="/profile?username=${reply.commenter.username}" />">
                                                        <c:out value="${reply.commenter.username}"/>
                                                    </a>
                                                </span>
                                                <p>Submitted <fmt:formatDate value="${reply.createdAt.time}" type="both"/></p>
                                                <br/>
                                                <p class="preserve-newlines wrap-text"><c:out value="${reply.comment}"/></p>
                                                <c:if test="${isLoggedIn}">
                                                    <div class="action-links">
                                                        <%--Edit link--%>
                                                        <c:if test="${reply.commenter.id == currentUser.id}">
                                                            | <a href="#!" class="edit-comment-link" data-comment-id="${reply.id}" data-comment="<c:out value="${reply.comment}"/>" data-form-shown="false">Edit</a>
                                                        </c:if>
                                                        <%--Delete link--%>
                                                        <c:if test="${reply.commenter.id == currentUser.id}">
                                                            | <a href="#!" class="delete-comment-link" data-comment-id="${reply.id}" data-form-shown="false">Delete</a>
                                                        </c:if>
                                                    </div>
                                                    <div class="action-form"></div>
                                                </c:if>
                                                <%--Un/like reply section--%>
                                                <span href="#!" class="secondary-content"><b>${reply.likeCount}</b>&nbsp;&nbsp;
                                                    <c:choose>
                                                        <c:when test="${isLoggedIn}">
                                                            <c:choose>
                                                                <c:when test="${reply.isLikedBy(currentUser)}">
                                                                    <a href="#!" class="unlike-comment" data-comment-id="${reply.id}"><i class="material-icons green-text">thumb_up</i></a>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <a href="#!" class="like-comment" data-comment-id="${reply.id}"><i class="material-icons black-text">thumb_up</i></a>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <i class="material-icons black-text">thumb_up</i>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </span>
                                                <%--End un/like reply section--%>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:if>
                            </li>
                        </c:forEach>
                    </ul>

                    <%--TODO allow further nesting level--%>
                    <%--<c:set var="root" value="${thread.comments}" scope="request" />--%>
                    <%--<c:set var="depth" value="0" scope="request" />--%>
                    <%--<c:import url="comment-partial.jsp" />--%>

                    <%--<jsp:include page="comment-partial.jsp">--%>
                    <%--<jsp:param name="root" value="${thread.comments}"/>--%>
                    <%--<jsp:param name="depth" value="1"/>--%>
                    <%--</jsp:include>--%>
                </c:otherwise>
            </c:choose>
        </div>
        <%--END COMMENTS--%>

        <c:if test="${isLoggedIn}">
            <%--Add a comment form--%>
            <div class="row">
                <c:url value="/comment" var="commentUrl"/>
                <form:form modelAttribute="commentForm" action="${commentUrl}" method="POST">
                    <form:hidden path="threadId"/>
                    <div class='row'>
                        <div class='input-field col s12'>
                            <form:textarea path="comment" cssClass="materialize-textarea" required="required" />
                            <form:label path="comment">New comment</form:label>
                            <form:errors path="comment" cssClass="formError" element="p"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col s4 offset-s5">
                            <button type='submit' class='col s6 btn btn-large waves-effect light-blue'>
                                Submit <i class="material-icons right">send</i>
                            </button>
                        </div>
                    </div>
                </form:form>
            </div>
            <%--End comment form--%>
            <%--FAB--%>
            <div class="fixed-action-btn" style="bottom:10%;">
                <a class="btn-floating btn-large waves-effect waves-light red" href="<c:url value="/create-thread" />">
                    <i class="large material-icons">mode_edit</i>
                </a>
            </div>
            <%--END FAB--%>
        </c:if>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
<%--Sweet Alert--%>
<script type="text/javascript" src="<c:url value="/js/sweetalert.min.js" />"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/sweetalert.css"/>" />
<%--End Sweet Alert--%>
<script type="text/javascript">
    $(function () {

        /* ************************
         *      Thread actions
         * ***********************/
        $(".edit-thread-initial-comment-link").on("click", function (event) {
            var $me = $(this);
            if (!$me.data("form-shown")) {
                var initialComment = $me.data("thread-initial-comment");
                var $form = $("<form></form>");
                $form.attr("action", "<c:url value="/edit-thread-initial-comment" />");
                $form.attr("method", "POST");
                $form.css("margin-top", "10px");

                $form.append("<input type='hidden' name='threadId' value='${thread.id}' />");
                $form.append("<input type='hidden' name='returnUrl' value='<c:out value="/thread?id=${thread.id}" />' />");

                //Text area
                $form.append("<div class='row'> \
                        <div class='input-field col s12'> \
                            <textarea name='newComment' class='materialize-textarea' required='required'>"+ initialComment +"</textarea> \
                            <label for='newComment'>New initial comment</label> \
                        </div> \
                    </div>");

                //Submit button
                $form.append("<div class='row'>	\
                        <div class='col s4 offset-s5'>	\
                            <button type='submit' class='col s6 btn btn-large waves-effect light-blue'>	\
                                Submit <i class='material-icons right'>send</i>	\
                            </button>	\
                        </div>	\
                    </div>");

                $me.parent().parent().find(".action-form").html($form);
                $me.data("form-shown", true);
                $form.find("textarea").trigger('autoresize');
                $form.find("textarea").focus();
            } else {
                $me.parent().parent().find(".action-form form textarea").focus();
            }
        });

        $(".edit-thread-title-link").on("click", function (event) {
            var $me = $(this);
            var threadId = $me.data("thread-id");
            var threadTitle = $me.data("thread-title");
            swal({
                title: "Change thread title",
                text: "Change from \""+ threadTitle +"\" to",
                type: "input",
                showCancelButton: true,
                closeOnConfirm: false,
                inputPlaceholder: "New title"
            },
            function(inputValue){
                if(inputValue === false) {
                    return false
                }

                if (inputValue === "") {
                    swal.showInputError("You need to write something!");
                    return false
                }

                //Disable submit button to prevent multiple submissions
                $(".confirm").attr('disabled', 'disabled');

                //Create an inline form and submit it to redirect with POST
                $("<form action='<c:url value="/edit-thread-title" />' method='POST'> \
                        <input type='hidden' name='threadId' value='" + threadId + "' /> \
                        <input type='hidden' name='newTitle' value='"+ inputValue +"' /> \
                        <input type='hidden' name='returnUrl' value='<c:out value="/thread?id=${thread.id}" />' /> \
                        </form>").submit();
            });
        });

        $(".delete-thread-link").on("click", function (event) {
            var $me = $(this);
            var threadId = $me.data("thread-id");
            swal({
                    title: "Are you sure?",
                    text: "This thread and all its comments will be permanently lost",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Yes",
                    cancelButtonText: "No",
                    closeOnConfirm: false
                },
                function(){
                    //Disable submit button to prevent multiple submissions
                    $(".confirm").attr('disabled', 'disabled');

                    //Create an inline form and submit it to redirect with POST
                    $("<form action='<c:url value="/delete-thread" />' method='POST'> \
                        <input type='hidden' name='threadId' value='" + threadId + "' /> \
                        <input type='hidden' name='returnUrl' value='<c:out value="/threads" />' /> \
                        </form>").submit();
                        });
        });

        $(".like-thread").on("click", function(event) {
            var threadId = $(this).data("thread-id");
            //Create an inline form and submit it to redirect with POST
            $("<form action='<c:url value="/like-thread" />' method='POST'> \
                <input type='hidden' name='threadId' value='" + threadId + "' /> \
                <input type='hidden' name='returnUrl' value='" + window.location.pathname + "?id=" + threadId + "' /> \
               </form>").submit();
        });

        $(".unlike-thread").on("click", function(event) {
            var threadId = $(this).data("thread-id");
            //Create an inline form and submit it to redirect with POST
            $("<form action='<c:url value="/unlike-thread" />' method='POST'> \
                <input type='hidden' name='threadId' value='" + threadId + "' /> \
                <input type='hidden' name='returnUrl' value='" + window.location.pathname + "?id=" + threadId + "' /> \
               </form>").submit();
        });

        /* ************************
         *      Comment actions
         * ***********************/

        //Comment replies
        $(".reply-link").on("click", function (event) {
            var $me = $(this);
            if (!$me.data("form-shown")) {
                var parentCommentId = $me.data("comment-id");
                var $form = $("<form></form>");
                $form.attr("action", "<c:url value="/reply" />");
                $form.attr("method", "POST");
                $form.css("margin-top", "10px");

                //Thread and parent comment IDs
                $form.append("<input type='hidden' name='threadId' value='${thread.id}' />");
                $form.append("<input type='hidden' name='parentCommentId' value='" + parentCommentId + "' />");

                //Text area
                $form.append("<div class='row'> \
                        <div class='input-field col s12'> \
                            <textarea name='reply' class='materialize-textarea' required='required' /> \
                            <label for='reply'>Your reply</label> \
                        </div> \
                    </div>");

                //Submit button
                $form.append("<div class='row'>	\
                        <div class='col s4 offset-s5'>	\
                            <button type='submit' class='col s6 btn btn-large waves-effect light-blue'>	\
                                Submit <i class='material-icons right'>send</i>	\
                            </button>	\
                        </div>	\
                    </div>");


                $me.parent().parent().find(".action-form").html($form);
                $me.data("form-shown", true);
                $me.siblings(".edit-comment-link").data("form-shown", false);
                $form.find("textarea").trigger('autoresize');
                $form.find("textarea").focus();
            } else {
                $me.parent().parent().find(".action-form form textarea").focus();
            }
        });

        //Comment edits
        $(".edit-comment-link").on("click", function (event) {
            var $me = $(this);
            if (!$me.data("form-shown")) {
                var commentId = $me.data("comment-id");
                var comment = $me.data("comment").trim();
                var $form = $("<form></form>");
                $form.attr("action", "<c:url value="/edit-comment" />");
                $form.attr("method", "POST");
                $form.css("margin-top", "10px");

                $form.append("<input type='hidden' name='commentId' value='"+ commentId +"' />");
                $form.append("<input type='hidden' name='returnUrl' value='"+ window.location.pathname + "?id=${thread.id}#" + commentId +"' />");

                //Text area
                $form.append("<div class='row'> \
                        <div class='input-field col s12'> \
                            <textarea name='newComment' class='materialize-textarea' required='required'>"+comment+"</textarea> \
                            <label for='newComment'>Comment</label> \
                        </div> \
                    </div>");

                //Submit button
                $form.append("<div class='row'>	\
                        <div class='col s4 offset-s5'>	\
                            <button type='submit' class='col s6 btn btn-large waves-effect light-blue'>	\
                                Submit <i class='material-icons right'>send</i>	\
                            </button>	\
                        </div>	\
                    </div>");

                $me.parent().parent().find(".action-form").html($form);
                $me.data("form-shown", true);
                $me.siblings(".reply-link").data("form-shown", false);
                $form.find("textarea").trigger('autoresize');
                $form.find("textarea").focus();
            } else {
                $me.parent().parent().find(".action-form form textarea").focus();
            }
        });

        //Comment deletions
        $(".delete-comment-link").on("click", function (event) {
            var $me = $(this);
            var commentId = $me.data("comment-id");
            swal({
                title: "Are you sure?",
                text: "This comment and all replies will be permanently lost",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes",
                cancelButtonText: "No",
                closeOnConfirm: false
            },
            function(){
                //Disable submit button to prevent multiple submissions
                $(".confirm").attr('disabled', 'disabled');

                //Create an inline form and submit it to redirect with POST
                $("<form action='<c:url value="/delete-comment" />' method='POST'> \
                <input type='hidden' name='commentId' value='" + commentId + "' /> \
                <input type='hidden' name='returnUrl' value='" + window.location.pathname + "?id=${thread.id}' /> \
               </form>").submit();
            });
        });

        $(".like-comment").on("click", function(event) {
            var commentId = $(this).data("comment-id");
            //Create an inline form and submit it to redirect with POST
            $("<form action='<c:url value="/like-comment" />' method='POST'> \
                <input type='hidden' name='commentId' value='" + commentId + "' /> \
                <input type='hidden' name='returnUrl' value='" + window.location.pathname + "?id=${thread.id}#" + commentId + "' /> \
               </form>").submit();
        });

        $(".unlike-comment").on("click", function(event) {
            var commentId = $(this).data("comment-id");
            //Create an inline form and submit it to redirect with POST
            $("<form action='<c:url value="/unlike-comment" />' method='POST'> \
                <input type='hidden' name='commentId' value='" + commentId + "' /> \
                <input type='hidden' name='returnUrl' value='" + window.location.pathname + "?id=${thread.id}#" + commentId + "' /> \
               </form>").submit();
        });
    });
</script>
</body>
</html>