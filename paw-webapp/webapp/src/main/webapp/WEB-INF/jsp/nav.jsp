<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<nav class="light-blue lighten-1" role="navigation">
    <div class="nav-wrapper container">

        <%--<a id="logo-container" href="<c:url value="/" />" class="brand-logo"><img src="<c:url value="/img/logo-transparent.png" />" /></a>--%>
        <a id="logo-container" href="<c:url value="/" />" class="brand-logo">P<span class="material-icons">gamepad</span>werUp</a>

        <ul class="right">
            <li><a href="<c:url value="/advanced-search" /> "><i class="material-icons">search</i></a></li>
            <li><a href="<c:url value="/register" /> ">Register</a></li>
            <li><a href="<c:url value="/list" /> ">List</a></li>
            <li><a href="<c:url value="/recommend" /> ">Recommend me!</a></li>
            <%--<li><a href="#!"><i class="material-icons">person_outline</i></a></li>--%>
            <%--<li><a href="#!"><i class="material-icons">person</i></a></li>--%>
            <%--TODO change to full person icon when logged in (or use an icon)--%>
        </ul>

        <ul id="nav-mobile" class="side-nav black-text">
            <li><a href="<c:url value="/advanced-search" /> "><i class="material-icons">search</i>Search</a></li>
            <li> <a href="<c:url value="/register" /> ">Register</a></li>
            <li><a href="<c:url value="/list" /> ">List</a></li>
            <li><a href="<c:url value="/recommend" /> ">Recommend me!</a></li>
            <%--<li><a href="#!"><i class="material-icons">person_outline</i>Log in</a></li>--%>
            <%--<li><a href="#!"><i class="material-icons">person</i>Profile</a></li>--%>
            <%--TODO change to full person icon when logged in (or use an icon)--%>
        </ul>

        <a href="#" data-activates="nav-mobile" class="button-collapse"><i class="material-icons">menu</i></a>

    </div>
</nav>