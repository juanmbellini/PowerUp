<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="paw" uri="http://paw.edu.itba.edu.ar/taglibs" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <link href="<c:url value="/slick/slick.css" />" type="text/css" rel="stylesheet"/>
    <link href="<c:url value="/slick/slick-theme.css" />" type="text/css" rel="stylesheet"/>
    <link href="<c:url value="/css/lightbox.css" />" type="text/css" rel="stylesheet"/>
    <title><c:out value="${game.name} - PowerUp" /></title>
</head>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>
    <div class="container">
        <div class="section no-pad-bot">
            <h1 class="header center orange-text"><c:out value="${game.name}"/></h1>
            <h5 class="center orange-text"><c:out value="${game.releaseDate.year}"/></h5>
        </div>
        <%--ACTION FORMS--%>
        <div class="section">
            <%--RATE AND STATUS FORM--%>
            <div class="row">
                <c:url value="/rateAndUpdateStatus?id=${game.id}" var="postPath"/>
                <form:form modelAttribute="rateAndStatusForm" action="${isLoggedIn ? postPath : ''}" method="post" class="center-align" id="rateAndStatusForm">
                    <div class="col s3"></div>
                    <div class="col s6 center-align">
                        <div class="row" style="margin-bottom:0;">
                            <div class="col s4 center-align">
                                <form:select path="score" id="score">
                                    <form:option value="" label="Select score"/>
                                    <form:options items="${scoreValues}"/>
                                </form:select>
                                <form:label path="score">Score: </form:label>
                                    <%--<form:input type="text" path="score"/>--%>
                                <form:errors path="score" cssClass="formError" element="p" Style="size: 1px"/>
                            </div>

                            <div class="col s4 center-align">
                                <form:select path="playStatus" id="status">
                                    <form:option selected="true" value="" label="Select play status" disabled="true"/>
                                    <form:options items="${statuses}"/>
                                </form:select>
                                <form:label path="playStatus">PlayStatus: </form:label>
                                    <%--<form:input type="playStatus" path="playStatus" />--%>
                                <form:errors path="playStatus" cssClass="formError" element="p"/>
                            </div>

                            <c:choose>
                                <c:when test="${isLoggedIn}">
                                    <div class="col s4 center">
                                        <button id="submit" type="submit" class="btn waves-effect waves-light">Update List</button>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col s4 center">
                                        <%--TODO redirect user back here after login--%>
                                        <a class="btn waves-effect waves-light" href="<c:url value="/login" />">Login</a>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </form:form>
                <c:if test="${isLoggedIn && canUpdateShelves}">
                    <button id="delete-button" class="btn waves-effect waves-light red lighten-1">Remove <i class="material-icons right">delete</i></button>
                </c:if>
            </div>
            <%--END RATE AND STATUS FORM--%>
            <%--SHELVES FORM--%>
            <c:if test="${canUpdateShelves}">
                <div class="row" style="margin-bottom:0; margin-top: 0;">
                    <c:url value="/update-shelves-by-game" var="shelvesUrl"/>
                    <form action="${isLoggedIn ? shelvesUrl : ''}" method="POST" class="center-align" id="shelvesForm">
                        <input type="hidden" name="gameId" value="${game.id}"/>
                        <div class="col s3"></div>
                        <div class="col s6 center-align">
                            <p class="col s4 center">Update in shelves</p>
                            <div class="col s4">
                                <c:choose>
                                    <c:when test="${fn:length(shelves) == 0}">
                                        <p>You have no shelves. Why not <a href="<c:url value="/list" />">create
                                            one</a>?</p>
                                    </c:when>
                                    <c:otherwise>
                                        <select id="shelves" multiple>
                                            <option value="" disabled selected>Select shelves</option>
                                                <%--<option id="newShelf" value="newShelf" >New shelf...</option>--%>
                                            <c:forEach var="entry" items="${shelves}">
                                                <c:set var="shelf" value="${entry.key}"/>
                                                <c:set var="isInShelf" value="${entry.value}"/>
                                                <option value="${shelf.id}"
                                                        <c:if test="${isInShelf}">selected</c:if> >
                                                    <c:out value="${shelf.name}"/></option>
                                            </c:forEach>
                                                <%--Forced to loop twice, the <input> element can't go inside the <select>--%>
                                            <c:forEach var="entry" items="${shelves}">
                                                <c:set var="shelf" value="${entry.key}"/>
                                                <c:set var="isInShelf" value="${entry.value}"/>
                                                <input type="hidden" class="shelfHidden" name="${shelf.id}"
                                                       value="${isInShelf}"/></c:forEach>
                                        </select>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <c:choose>
                                <c:when test="${isLoggedIn}">
                                    <button type="submit" class="btn waves-effect waves-light">Update Shelves</button>
                                </c:when>
                                <c:otherwise>
                                    <%--TODO redirect user back  here after login--%>
                                    <a class="btn waves-effect waves-light" href="<c:url value="/login" />">Log in</a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </form>
                </div>
            </c:if>
            <%--END SHELVES FORM--%>
        </div>
        <%--END ACTION FORMS--%>

        <div class="section">
            <c:choose>
                <c:when test="${game == null}">
                    <div class="row">
                        <h3 class="center">No game found =(</h3>
                        <h5 class="center">Go back <a href="<c:url value='/' />">home</a></h5>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row">
                        <img class="col s3" src="${game.coverPictureUrl}" alt="${game.name}">
                        <div class="col s5">
                            <p style="margin-top: 0;">
                                <c:out value=" ${genre.name}" />
                                <c:choose>
                                    <c:when test="${empty game.summary}">No summary =(</c:when>
                                    <c:otherwise> <c:out value="${game.summary}" /></c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                        <div class="col s4">
                            <p><b>Rating</b></p>
                            <c:choose>
                                <c:when test="${game.avgScore > 0}">
                                    <p style="margin-top:0;"><fmt:formatNumber value="${game.avgScore}" maxFractionDigits="2" /></p>
                                </c:when>
                                <c:otherwise>
                                    <p style="margin-top:0;">unrated</p>
                                </c:otherwise>
                            </c:choose>

                            <p><b>Genres</b></p>
                            <p>
                                <c:forEach var="genre" items="${genres}" varStatus="status">
                                    <a href="<c:url value="/search">
                                        <c:param name="name" value=""/>
                                        <c:param name="filters" value='{"genre":["${genre.name}"]}'/>
                                       </c:url>
                                    ">
                                        <c:out value=" ${genre.name}" />

                                    </a>
                                    <c:if test="${!status.last}"><br/></c:if>
                                </c:forEach>
                            </p>
                            <p><b>Platforms</b></p>
                            <c:forEach var="platformEntry" items="${platforms}" varStatus="status">
                                <a href="<c:url value="/search">
                                        <c:param name="name" value="" />
                                        <c:param name="filters" value='{"platform":["${platformEntry.key.name}"]}'/>
                                       </c:url>
                                    ">
                                        <c:out value=" ${platformEntry.key.name}" />
                                       </a><span style="font-size: small; float: right;"><c:out value="${platformEntry.value.releaseDate}" /></span>
                                    <c:if test="${!status.last}"><div class="col s12 divider"></div><br /></c:if>
                                </c:forEach>
                            <p><b>Developers</b></p>
                            <c:forEach var="developer" items="${developers}" varStatus="status">
                                <a href="<c:url value="/search">
                                        <c:param name="name" value=""/>
                                        <c:param name="filters" value='{"developer":["${developer.name}"]}'/>
                                       </c:url>
                                    ">

                                            <c:out value="${developer.name}" />
                                </a>
                                <c:if test="${!status.last}"><br/></c:if>
                            </c:forEach>
                            <p><b>Publishers</b></p>
                            <c:forEach var="publisher" items="${publishers}" varStatus="status">
                                <a href="<c:url value="/search">
                                        <c:param name="name" value=""/>
                                        <c:param name="filters" value='{"publisher":["${publisher.name}"]}'/>
                                       </c:url>
                                    ">
                                        <c:out value="${publisher.name}" />
                                </a>

                                <c:if test="${!status.last}"><br/></c:if>
                            </c:forEach>
                        </div>
                    </div>

                    <%--SCREENSHOTS AND VIDEOS--%>
                    <c:set var="videosLength" value="${fn:length(videos)}" />
                    <c:set var="picturesLength" value="${fn:length(pictures)}" />
                    <c:if test="${videosLength > 0 || picturesLength > 0}">
                        <div class="row">
                            <div class="col s12 divider"></div>
                        </div>
                        <c:if test="${videosLength > 0}">
                            <div class="row">
                                <h4 class="center">Videos</h4>
                                <div class="slick-carousel center" id="videos-carousel" data-slick='{"slidesToShow": ${paw:min(videosLength, 4)}, "slidesToScroll": ${paw:min(videosLength, 4)}}'>
                                    <c:forEach var="entry" items="${videos}">
                                        <c:set var="videoId" value="${entry.key}" />
                                        <c:set var="videoName" value="${entry.value}" />
                                        <div class="slide-container">
                                            <iframe type="text/html" width="${videosLength > 1 ? '90%' : '640'}" height="360"
                                                    src="https://www.youtube.com/embed/${videoId}"
                                                    <%--?autoplay=1&origin=http://example.com--%>
                                                    frameborder="0"></iframe>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${picturesLength > 0}">
                            <c:if test="${videosLength > 0}">
                                <div class="row">
                                    <div class="col s12 divider"></div>
                                </div>
                            </c:if>
                            <div class="row">
                                <h4 class="center">Screenshots</h4>
                                <div class="slick-carousel center" id="screenshots-carousel" data-slick='{"slidesToShow": ${paw:min(picturesLength, 4)}, "slidesToScroll": ${paw:min(picturesLength, 4)}}'>
                                    <c:forEach var="url" items="${pictures}">
                                        <div class="slide-container">
                                            <div class="valign-wrapper slide-image">
                                                <a href="${url}" data-lightbox="screenshot-${status.index}">
                                                    <img data-lazy="${url}" class="valign"/>
                                                </a>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>
                    </c:if>
                    <%--END SCREENSHOTS AND VIDEOS--%>

                    <%--REVIEWS--%>
                    <div class="row">
                        <div class="col s12 divider"></div>
                    </div>
                    <div class="row">
                        <c:choose>
                            <%--No Reviews--%>
                            <c:when test="${fn:length(reviews) == 0}">
                                <h5 class="center">Recent Reviews</h5>
                                <p class="center">No reviews</p>
                            </c:when>
                            <%--End No Reviews--%>
                            <%--Review list--%>
                            <c:otherwise>
                                <h5 class="center">Recent Reviews - <a
                                        href="<c:url value="/reviews?gameId=${game.id}" />">See All</a></h5>
                                <ul class="collection">
                                    <c:forEach items="${reviews}" var="review">
                                        <li class="collection-item avatar">
                                            <img src="<c:url value="/profile-picture?username=${review.user.username}" />" alt="<c:out value="${review.user.username}" />" class="circle">
                                            <span class="title"><c:out value="${review.user.username}" /></span>
                                            <p class="secondary-content" style="color: black;">${review.date}</p>
                                            <c:if test="${review.user.username == currentUsername}">
                                                <a href="#!" class="material-icons red-text text-lighten-1 delete-button" data-review-id="${review.id}"><i class="material-icons right">delete</i></a>
                                            </c:if>
                                            <p><a href="<c:url value="/reviews?userId=${review.user.id}" />">Other reviews by <c:out value="${review.user.username}" /></a></p>
                                            <br/>
                                            <div class="row">
                                                <p class="col s10 wrap-text preserve-newlines"><c:out value="${review.review}" /></p>
                                                <div class="col s2">
                                                    <p style="color: #26a69a;">
                                                        Story: <span class="right">${review.storyScore}</span>
                                                        <br/>
                                                        Graphics: <span class="right">${review.graphicsScore}</span>
                                                        <br/>
                                                        Audio: <span class="right">${review.audioScore}</span>
                                                        <br/>
                                                        Controls: <span class="right">${review.controlsScore}</span>
                                                        <br/>
                                                        Fun: <span class="right">${review.funScore}</span>
                                                        <br/>
                                                        <b>Overall: <span class="right"><fmt:formatNumber
                                                                value="${review.overallScore}"
                                                                maxFractionDigits="2"/></span></b>
                                                    </p>
                                                </div>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                            <%--End Review List--%>
                        </c:choose>

                        <c:if test="${canSubmitReview}">
                            <div class="row">
                                <div class="col s12 center">
                                    <a href="<c:url value="/write-review?id=${game.id}" />"
                                       class="center btn waves-effect waves-light offset-s4" style="">Write a Review <i
                                            class="material-icons right">send</i></a>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    <%--END REVIEWS--%>

                    <%--RELATED GAMES--%>
                    <c:if test="${ fn:length( relatedGames) > 0 }">
                        <div class="row">
                            <div class="col s12 divider"></div>
                        </div>
                        <div class="row">
                            <h5 class="center">Related Games</h5>
                            <div class="slick-carousel" id="related-games-carousel">
                                <c:forEach var="game" items="${relatedGames}">
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
                    <%--END RELATED GAMES--%>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
<script type="text/javascript" src="<c:url value="/slick/slick.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/game.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/sweetalert.min.js" />"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/sweetalert.css"/>" />
<script type="text/javascript">
    $(function () {
        $("#delete-button").on('click', function (event) {
            swal({
                title: "Are you sure?",
                text: "This game will be removed from all your shelves.",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Remove",
                closeOnConfirm: false
            },
            function () {
                //Disable submit button to prevent multiple submissions
                $(".confirm").attr('disabled', 'disabled');
                <c:url value="/delete-shelf" var="deleteUrl" />
                //Create an inline form and submit it to redirect with POST
                $("<form action='<c:url value="/remove-from-list" />' method='POST'> \
                    <input type='hidden' name='gameId' value='${game.id}' /> \
                    <input type='hidden' name='userId' value='${currentUser.id}' /> \
                    <input type='hidden' name='returnUrl' value='" + window.location.pathname + window.location.search + "'/> \
                   </form>").submit();
            });
        });

        $(".delete-button").on('click', function (event) {
            var reviewId = $(this).data('reviewId');
            //Create an inline form and submit it to redirect with POST
            $("<form action='<c:url value="/delete-review" />' method='POST'> \
                <input type='hidden' name='reviewId' value='" + reviewId + "' /> \
                <input type='hidden' name='returnUrl' value='" + window.location.pathname + window.location.search + "'/> \
               </form>").submit();
        });

        $("#shelves").on("change", function (event) {
            //$(this).material_select();
            var selectedShelves = $(this).val();
            if (selectedShelves.length == 0) {
                //No selected shelves, set everything to false
                $(".shelfHidden").val("false");
            } else {
                $(".shelfHidden").each(function (i, element) {
                    element = $(element);
                    element.val(selectedShelves.indexOf(element.attr("name")) != -1);
                });
            }
            $("#shelvesForm").find("button[type=submit]").removeAttr('disabled');
        });
    });
</script>
<script type="text/javascript" src="<c:url value="/js/lightbox.js" />"></script>
</body>
</html>