<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<nav class="light-blue lighten-1" role="navigation">
    <div class="nav-wrapper container">

        <%--<a id="logo-container" href="<c:url value="/" />" class="brand-logo"><img src="<c:url value="/img/logo-transparent.png" />" /></a>--%>
        <a id="logo-container" href="<c:url value="/" />" class="brand-logo">P<span class="material-icons">gamepad</span>werUp</a>

        <ul class="right hide-on-med-and-down">
            <li><a href="<c:url value="/search" /> ">Search <i class="material-icons right">search</i></a></li>
            <c:choose>
                <c:when test="${isLoggedIn}">
                    <li><a href="<c:url value="/profile?username=${currentUsername}" />">${currentUsername} <i class="material-icons right">person</i></a></li>
                    <li><a href="<c:url value="/list?username=${currentUsername}" /> ">Game List <i class="material-icons right">menu</i></a></li>
                    <li><a href="<c:url value="/shelves?username=${currentUsername}" /> ">Shelves <i class="material-icons right">menu</i></a></li>
                    <li><a href="<c:url value="/logout" /> ">Log Out <i class="material-icons right">exit_to_app</i></a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="<c:url value="/login" /> ">Log In <i class="material-icons right">person_outline</i></a></li>
                    <li><a href="<c:url value="/register" /> ">Register <i class="material-icons right">assignment_ind</i></a></li>
                </c:otherwise>
            </c:choose>
        </ul>

        <ul id="nav-mobile" class="side-nav black-text">
            <%--No icons on mobile because they don't work nicely, and there's not as much room--%>
            <li><a href="<c:url value="/search" /> ">Search</a></li>
            <c:choose>
                <c:when test="${isLoggedIn}">
                    <li><a href="<c:url value="/profile?username=${currentUsername}" />">${currentUsername}</a></li>
                    <li><a href="<c:url value="/list?username=${currentUsername}" /> ">Game List</a></li>
                    <li><a href="<c:url value="/shelves?username=${currentUsername}" /> ">Shelves</a></li>
                    <li><a href="<c:url value="/logout" /> ">Log Out</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="<c:url value="/login" /> ">Log In</a></li>
                    <li><a href="<c:url value="/register" /> ">Register</a></li>
                </c:otherwise>
            </c:choose>
        </ul>

        <a href="#" data-activates="nav-mobile" class="button-collapse"><i class="material-icons">menu</i></a>

    </div>
</nav>