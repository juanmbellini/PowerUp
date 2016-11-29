<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
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
            <form id="shelfForm" action="<c:url value="/shelves"/>">
                <c:forEach items="${playstatus}" var="playStatus">
                    <p>
                        <input type="checkbox" name="playStatusesCheckbox"  <c:if test="${playStatusesFilter.contains(playStatus.name())}">checked</c:if> id="${playStatus}" value="${playStatus.name()}" />
                        <label for="${playStatus}">${playStatus.name()}</label>
                    </p>
                </c:forEach>
                <c:forEach items="${shelves}" var="shelf">
                     <p>
                        <input type="checkbox" name="shelvesCheckbox" id="${shelf.id}" <c:if test="${shelvesFilter.contains(shelf.name)}">checked</c:if> value="${shelf.name}"/>
                        <label for="${shelf.id}">${shelf.name}</label>
                    </p>
                    <c:if test="${isLoggedIn && currentUsername == user.username}">
                        <span style="font-size: 0.45em;"><a href="#!" class="rename" data-id="<c:out value="${shelf.id}"></c:out>"data-name="<c:out value="${shelf.name}"></c:out>">rename</a> | <a href="#!" class="delete" data-id="<c:out value="${shelf.id}"></c:out>" data-name="<c:out value="${shelf.name}"></c:out>" >delete</a></span>
                    </c:if>
                </c:forEach>
                <br/>
                <input type="submit">
            </form>
            <span>Create a Shelf</span>
            <form action="<c:url value="/create-shelf" />" method="POST">
                <div class="input-field center col s12">
                    <input type="text" name="name" required />
                </div>
                <button type='submit' class='col s4 btn waves-effect light-blue'>Submit <i class="material-icons right">send</i></button>
            </form>

        </div>
        <div class="col s9">

                <div class="row">
                    <h1 class="center"><c:out value="${user.username}'s Game List"/></h1>
                    <c:if test="${user.username == currentUsername}"><h5 class="center"><a href="<c:url value="/search" />">Search games</a> to add them to your list!</h5></c:if>

                    <div class="col s12 divider"></div>
                    <br/>

                    <%--<h4>--%>
                    <%--<c:out value="${shelf.name}"></c:out>--%>

                    <%--</h4>--%>
                    <c:choose>
                        <c:when test="${fn:length(games) == 0}">
                            <h5 class="center"><c:out value="Game list is empty"></c:out></h5>
                        </c:when>
                        <c:otherwise>
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
                                            <p style="margin-top: 33px;"><b>${empty scores.get(game) ? "No score" : scores.get(game)}</b></p>
                                        </div>
                                        <div class="col s1 center">
                                            <p style="margin-top: 33px;"><b>${empty game.avgScore || game.avgScore==0? "No avg rating" : game.avgScore}</b></p>
                                        </div>
                                        <div class="col s1">
                                            <div class="secondary-content">
                                                <button class="btn delete-button waves-effect waves-light" data-shelf-id="${shelf.id}" data-game-id="${game.id}"><i class="material-icons right">delete</i></button>
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

        <%--$("#shelfForm").on("submit", function() {--%>
            <%--debugger;--%>
            <%--var url = "http://localhost:8080/list";--%>
            <%--url+="?";--%>

            <%--<c:forEach items="${playstatus}" var="playStatus">--%>
                <%--if($("#${playStatus}").is(":checked")) {--%>
                    <%--playStatuses.push("${playStatus}");--%>
                <%--}--%>
            <%--</c:forEach>--%>
            <%--<c:forEach items="${shelves}" var="shelf">--%>
                <%--if($("#${shelf.id}").is(":checked")) {--%>
                    <%--shelves.push("${shelf.name}");--%>
                <%--}--%>
            <%--</c:forEach>--%>
            <%--debugger;--%>
            <%--url+="playStatuses="+JSON.stringify(playStatuses);--%>
            <%--url+="&shelves="+JSON.stringify(shelves)--%>
            <%--url=encodeURI(url);--%>
            <%--window.location = url;--%>
        <%--});--%>

        $(".delete-button").on('click', function (event) {
            var gameId = $(this).data('game-id');
            var shelfId = $(this).data('shelf-id');
            //Create an inline form and submit it to redirect with POST
            $("<form action='<c:url value="/update-shelves-by-game" />' method='POST'> \
                <input type='hidden' name='gameId' value='" + gameId + "' /> \
                <input type='hidden' name='" + shelfId + "' value='false' /> \
                <input type='hidden' name='returnUrl' value='" + window.location.pathname + window.location.search + "' /> \
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
            debugger;
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
                $("<form action='${renameUrl}' method='POST'><input type='hidden' name='shelfId' value='" + id + "' /><input type='hidden' name='name' value='" + inputValue + "' /></form>").submit();
            });
        });
    });
</script>
</body>
</html>