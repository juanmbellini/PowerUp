<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>${user.username} - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main class="section container">
    <h1 class="center orange-text">${user.username}'s Profile</h1>
    <div class="row">
        <div class="col s4">
            <img src="<c:url value="/profile-picture?username=${user.username}" />" style="max-width: 100%; height:auto;"/>
            <c:if test="${isLoggedIn && user.username == currentUsername}">
                <c:url value="/profile-picture" var="uploadFileUrl" />
                <form method="POST" action="${uploadFileUrl}" enctype="multipart/form-data">
                    <input type='hidden' name='username' value='${user.username}'/>
                    <div class="file-field input-field">
                        <div class="btn waves-effect waves-light">
                            <span>Upload picture</span>
                            <input type="file" name="picture" accept=".jpeg, .jpg, .png, .gif" required>
                        </div>
                        <div class="file-path-wrapper">
                            <input class="file-path" type="text" />
                        </div>
                    </div>
                    <button class="btn waves-effect waves-light" type="submit" name="action">Submit
                        <i class="material-icons right">send</i>
                    </button>
                    <c:if test="${user.hasProfilePicture()}">
                        or <a id="remove-profile-picture" href="#!" class="red-text">delete profile picture</a>
                        <script type="text/javascript">
                            $(function() {
                                var removingProfilePicture = false;
                                $("#remove-profile-picture").on("click", function() {
                                    if(!removingProfilePicture) {
                                        removingProfilePicture = true;
                                        //Create an inline form and submit it to redirect with POST
                                        $("<form action='<c:url value="/remove-profile-picture" />' method='POST'> \
                                            <input type='hidden' name='returnUrl' value='/profile?username=${currentUsername}' /> \
                                           </form>").submit();
                                    }
                                });
                            });
                        </script>
                    </c:if>
                </form>
            </c:if>
            <br />
            <!-- Modal Trigger -->
            <a id="change-password-trigger" class="waves-effect waves-light btn" href="#change-password-modal">Change Password</a>
            <br />
            <h5>Profile Stats</h5>
            <p>Played <b>${fn:length(playedGames)}</b> game${fn:length(playedGames) == 1 ? "" : "s"}</p>
            <p>Playing <b>${fn:length(playingGames)}</b> game${fn:length(playingGames) == 1 ? "" : "s"}</p>
            <p>Plans to play <b>${fn:length(planToPlayGames)}</b> game${fn:length(planToPlayGames) == 1 ? "" : "s"}</p>
        </div>
        <div class="col s8">
            <h4 class="center">Top 10 games</h4>
            <c:choose>
                <c:when test="${fn:length(topGames) == 0}">
                    <p class="center">No scored games. <c:if test="${user.username == currentUsername}">Why don't you <a href="<c:url value="/search"/>">search</a> and score some?</c:if> </p>
                </c:when>
                <c:otherwise>
                    <ul class="collection games-list">
                        <c:forEach var="entry" items="${topGames}">
                            <c:set var="game" value="${entry.key}" />
                            <c:set var="score" value="${entry.value}" />
                            <li class="collection-item avatar col s12">
                                <div class="col s2 cover-pic-container valign-wrapper">
                                    <img class="cover-picture valign" src="${game.coverPictureUrl}" alt="${game.name}">
                                </div>
                                <div class="col primary-content s7">
                                    <p class="title"><a href="<c:url value="/game?id=${game.id}" />">${game.name}</a></p>
                                </div>
                                <div class="col s1 center">
                                    <%--<p style="margin-top: 33px;">${game.releaseDate.year}</p>--%>
                                </div>
                                <div class="col s2">
                                    <div class="secondary-content">
                                        <p class="rating-number center"><b><fmt:formatNumber value="${score}" maxFractionDigits="0" /></b></p>
                                        <p class="rating-stars hide-on-small-and-down">
                                            <c:forEach begin="0" end="4" var="i">
                                                <c:choose>
                                                    <c:when test="${score - (i * 2) - 1 < 0}">
                                                        <i class="material-icons">star_border</i>
                                                    </c:when>
                                                    <c:when test="${score - (i * 2) - 1 == 0}">
                                                        <i class="material-icons">star_half</i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="material-icons">star</i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </p>
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>

<!-- PASSWORD RESET MODAL -->
<div id="change-password-modal" class="modal">
    <c:url value="/change-password" var="postUrl" />
    <form:form id="change-password-form" method="POST" modelAttribute="changePasswordForm" action="${postUrl}">
        <input type="hidden" value="${user.username}" name="username" />
        <div class="modal-content">
            <h4 class="center">Change Your Password</h4>
            <div class='input-field'>
                <form:input type="password" path="newPassword" required="required" />
                <form:errors path="" cssClass="formError" element="p" Style="size: 1px"/>
                <form:label path="newPassword">New Password</form:label>
            </div>
            <div class='input-field'>
                <form:input type="password" path="repeatNewPassword" required="required"/>
                <form:errors path="" cssClass="formError" element="p" Style="size: 1px"/>
                <form:label path="repeatNewPassword">Repeat New Password</form:label>
            </div>
            <div class="row">
                <button type="button" class="btn waves-effect waves-light col s3 offset-s1 red lighten-1">Cancel<i class="material-icons right">close</i></button>
                <button type="submit" class="btn waves-effect waves-light col s3 offset-s4">Submit<i class="material-icons right">send</i></button>
            </div>
        </div>
    </form:form>
</div>
<!-- END PASSWORD RESET MODAL -->
<script type="text/javascript">
    $(function(){
        $('#change-password-trigger').on("click", function() {
            $("#change-password-modal").openModal({
                dismissible: false
            });
        });

        $('#change-password-form').on("submit", function() {
            $(this).find("button").attr("disabled", "disabled");
        });

        $('#change-password-modal button[type=button]').on("click", function() {
            $("#change-password-modal").closeModal();
        });

        <c:if test="${formHasErrors}">
            $('#change-password-trigger').trigger("click");
        </c:if>

//        $("#change-password-form").on("submit", function(event) {
//            var errorState = false;
//            //TODO validate matching new password
//        });
    });
</script>
</body>
</html>