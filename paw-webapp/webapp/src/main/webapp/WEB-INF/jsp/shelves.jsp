<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <link href="<c:url value="/slick/slick.css" />" type="text/css" rel="stylesheet"/>
    <link href="<c:url value="/slick/slick-theme.css" />" type="text/css" rel="stylesheet"/>
    <title><c:out value="${user.username}'s Game List - PowerUp"/></title>
    <%--TODO suffix ' instead of 's if username ends with s--%>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main class="section">
    <div class="row">
        <div class="col s2">
            <form id="shelfForm" action="<c:url value="/list"/>">
                <h5>Filter by Status</h5>
                <c:forEach var="playStatus" items="${playStatusEnumValues}" >
                    <p class="checkbox-list">
                        <input type="checkbox" name="playStatusesCheckbox"  <c:if test="${playStatusesFilter.contains(playStatus.name())}">checked</c:if> id="<c:out value="${playStatus}"/>" value="<c:out value="${playStatus.name()}"/>" />
                        <label class="wrap-text" for="<c:out value="${playStatus}"/>"><c:out value="${playStatus.pretty}"/></label>
                    </p>
                </c:forEach>
                <h5>Filter by Shelf</h5>
                <c:forEach items="${shelves}" var="shelf">
                     <p class="checkbox-list">
                        <input type="checkbox" name="shelvesCheckbox" id="<c:out value="${shelf.id}"/>" <c:if test="${shelvesFilter.contains(shelf.name)}">checked</c:if> value="<c:out value="${shelf.name}"/>"/>
                        <label class="wrap-text" for="<c:out value="${shelf.id}"/>">
                            <c:out value="${shelf.name}"/>
                            <c:if test="${isLoggedIn && currentUsername == user.username}">
                                <a href="#!" class="rename material-icons black-text" style="vertical-align: middle;" data-id="<c:out value='${shelf.id}'/>" data-name="<c:out value='${shelf.name}'/>">mode_edit</a>
                                <a href="#!" class="delete material-icons red-text text-lighten-1" style="vertical-align: middle;" data-id="<c:out value="${shelf.id}"/>" data-name="<c:out value="${shelf.name}"/>">delete</a>
                            </c:if>
                        </label>
                    </p>
                </c:forEach>
                <button type='submit' class='btn waves-effect'>Search <i class="material-icons right">search</i></button>
            </form>
            <br />
            <c:if test="${isLoggedIn && currentUsername == user.username}">
                <div class="col s12 divider"></div>
                <h5 style="margin-bottom:0;">Create a Shelf</h5>
                <form action="<c:url value="/create-shelf" />" method="POST">
                    <div class="input-field center col s12">
                        <input type="text" name="name" required />
                    </div>
                    <button type='submit' class='btn waves-effect light-blue'>Create <i class="material-icons right">playlist_add</i></button>
                </form>
            </c:if>
        </div>
        <div class="col s9">

                <div class="row">
                    <h1 class="center"><c:out value="${user.username}'s Game List"/></h1>
                    <c:if test="${isLoggedIn && user.username == currentUsername}"><h5 class="center"><a href="<c:url value="/search" />">Search games</a> to add them to your list!</h5></c:if>

                    <div class="col s12 divider"></div>
                    <br/>

                    <c:choose>
                        <c:when test="${fn:length(games) == 0}">
                            <h5 class="center">No results</h5>
                        </c:when>
                        <c:otherwise>
                            <%--Games list header--%>
                            <div class="col s12 center" id="games-list-header">
                                <p class="col s2">Cover Picture</p>
                                <p class="col s3 ">Title</p>
                                <p class="col s1 ">Play Status</p>
                                <p class="col s2 ">Shelves</p>
                                <p class="col s1 ">Avg. Rating</p>
                                <p class="col s2 ">Own score</p>
                                <p class="col s1"></p>
                            </div>
                            <ul class="collection games-list">
                                <%--TODO limit number of shown games, create link to show more--%>
                                <c:forEach items="${games}" var="game">
                                    <li class="collection-item avatar col s12">
                                        <div class="col s2 cover-pic-container valign-wrapper">
                                            <img class="cover-picture valign" src="${game.coverPictureUrl}" alt="${game.name}">
                                        </div>
                                        <div class="col primary-content s3">
                                            <p class="title wrap-text"><a href="<c:url value="/game?id=${game.id}" />"><c:out value="${game.name}"></c:out></a></p>
                                        </div>
                                        <div class="col s1 center">
                                            <p style="margin-top: 33px;"><b>${empty playStatuses.get(game) ? "No status" : playStatuses.get(game).pretty}</b></p>
                                        </div>
                                        <div class="col s2 center">
                                            <p style="margin-top: 33px;"><b>
                                                <c:choose>
                                                    <c:when test="${ empty shelvesForGamesMap.get(game)}">No shelves</c:when>
                                                    <c:otherwise>
                                                        <c:forEach items="${shelvesForGamesMap.get(game)}" var="shelf">
                                                            <c:out value="${shelf.name}"/>
                                                        </c:forEach>
                                                    </c:otherwise>
                                                </c:choose>
                                            </b></p>
                                        </div>
                                        <div class="col s1 center">
                                            <p style="margin-top: 33px;"><b>
                                                <c:choose>
                                                    <c:when test="empty game.avgScore || game.avgScore==0">
                                                        <c:out value="No avg rating"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <fmt:formatNumber value="${game.avgScore}" maxFractionDigits="2" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </b></p>
                                        </div>
                                        <div class="col s2 center">
                                            <%--<div class="secondary-content">--%>
                                                <c:set var="score" value="${scores.get(game)}"/>
                                                <c:choose>
                                                    <c:when test="${score <= 10 && score>=0}">
                                                        <p style="margin-top: 27px;" class="rating-number center"><b>${score}</b></p>
                                                        <p class="rating-stars hide-on-small-and-down">
                                                            <c:forEach begin="0" end="4" var="i">
                                                                <c:choose>
                                                                    <c:when test="${score-(i*2)-1<0}">
                                                                        <i class="material-icons">star_border</i>
                                                                    </c:when>
                                                                    <c:when test="${score-(i*2)-1==0}">
                                                                        <i class="material-icons">star_half</i>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <i class="material-icons">star</i>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:forEach>
                                                        </p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p style="margin-top: 33px;" class="rating-number center"><b>Unrated</b></p>
                                                    </c:otherwise>
                                                </c:choose>
                                            <%--</div>--%>
                                        </div>
                                        <%--<div class="col s1 center">--%>
                                            <%--<p style="margin-top: 33px;"><b>${empty scores.get(game) ? "No score" : scores.get(game)}</b></p>--%>
                                        <%--</div>--%>

                                        <div class="col s1">
                                            <div class="secondary-content">
                                                <%--<a href="#!" class="delete material-icons red-text text-lighten-1" style="vertical-align: middle;" data-id="<c:out value="${shelf.id}"/>" data-name="<c:out value="${shelf.name}"/>">delete</a>--%>
                                                <a href="#!" class="material-icons red-text text-lighten-1 delete-button" data-user-id="${user.id}" data-game-id="${game.id}"><i class="material-icons right">delete</i></a>
                                            </div>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:otherwise>
                    </c:choose>
                    <%--<c:if test="${fn:length(shelves) == 0}">--%>
                    <%--<h4 class="center">No shelves. <c:if test="${user.username == currentUsername}">Why not create one?</c:if></h4>--%>
                    <%--</c:if>--%>
                </div>
                <%--<c:if test="${user.username == currentUsername}">--%>
                <%--<div class="col s12 divider"></div>--%>
                <%--<div class="row">--%>
                <%--<div class="col s6">--%>

                <%--</div>--%>
                <%--</div>--%>
                <%--</c:if>--%>
            </div>
            <div class="col s1">

            </div>
        <div class="container">
            <div class="section">
                <c:if test="${ fn:length( recommendedGames) > 0 }">
                    <div class="row">
                        <h5 class="center">Recommended Games for you <c:if test="${! empty shelvesFilter}">for this shelves</c:if>!</h5>
                        <div class="slick-carousel">
                            <c:forEach var="game" items="${recommendedGames}">
                                <div class="slide-container">
                                    <div class="valign-wrapper slide-image">
                                        <a href="<c:url value="/game?id=${game.id}"/>">
                                            <img data-lazy="${game.coverPictureUrl}" class="valign"/>
                                        </a>
                                    </div>
                                    <h5 class="center slide-text">
                                        <a href="<c:url value="/game?id=${game.id}"/>">${game.name}</a>
                                    </h5>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>



</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
<script type="text/javascript" src="<c:url value="/js/sweetalert.min.js" />"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/sweetalert.css"/>" />
<script type="text/javascript">

    var shelves = [];
    var playStatuses = [];



    $(function() {
        $(".delete-button").on('click', function (event) {
            var gameId = $(this).data('game-id');
            var userId = $(this).data('user-id');
            //Create an inline form and submit it to redirect with POST
            $("<form action='<c:url value="/remove-from-list" />' method='POST'> \
                <input type='hidden' name='gameId' value='" + gameId + "' /> \
                <input type='hidden' name='userId' value='" + userId + "' /> \
                <input type='hidden' name='returnUrl' value='" + window.location.pathname + window.location.search + "'/> \
               </form>").submit();
        });

        /* ***********************
         *      SWEET ALERTS
         * **********************/

        //Delete links
        $(".delete").on('click', function (event) {
            var $target = $(this);
            var name = $target.data('name');
            var id = $target.data('id');
            swal({
                title: "Are you sure?",
                text: "You are about to permanently delete shelf \"" + name + "\"",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Delete",
                closeOnConfirm: false
            },
            function () {
                //Disable submit button to prevent multiple submissions
                $(".confirm").attr('disabled', 'disabled');
                <c:url value="/delete-shelf" var="deleteUrl" />
                //Create an inline form and submit it to redirect with POST
                $("<form action='${deleteUrl}' method='POST'><input type='hidden' name='shelfId' value='" + id + "' /></form>").submit();
            });
        });

        //Rename links
        $(".rename").on('click', function (event) {
            var $target = $(this);
            var name = $target.data('name');
            var id = $target.data('id');
            swal({
                title: "Rename \"" + name + "\" to...",
                type: "input",
                showCancelButton: true,
                closeOnConfirm: false,
                confirmButtonText: "Rename",
                inputPlaceholder: "New name"
            },
            function (inputValue) {
                if (inputValue === false) return false;

                if (inputValue === "") {
                    swal.showInputError("You need to write something!");
                    return false;
                }

                //Disable submit button to prevent multiple submissions
                $(".confirm").attr('disabled', 'disabled');
                <c:url value="/rename-shelf" var="renameUrl" />
                //Create an inline form and submit it to redirect with POST
                $("<form action='${renameUrl}' method='POST'><input type='hidden' name='shelfId' value='" + id + "' /><input type='hidden' name='name' value='" + escapeHtml(inputValue) + "' /></form>").submit();
            });
        })

        $('.slick-carousel').slick({
            infinite: false,
            slidesToShow: 4,
            slidesToScroll: 4,
            arrows: true,
            lazyload: 'ondemand'
        });
    });

    var entityMap = {
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': '&quot;',
        "'": '&#39;',
        "/": '&#x2F;'
    };

    function escapeHtml(string) {
        return String(string).replace(/[&<>"'\/]/g, function (s) {
            return entityMap[s];
        });
    }

</script>

<script type="text/javascript" src="<c:url value="/slick/slick.min.js" />"></script>
</body>
</html>