<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>Starter Template - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main>
    &nbsp;
    <div class="section"></div>
    <h2 class="orange-text" style="text-align: center" >Please, log in into your account</h2>
    <div class="section"></div>

    <c:url value="/create" var="postPath"/>
    <form:form modelAttribute="loginForm" action="${postPath}" method="post" class="center-align">
        <div class="container" style="text-align: center">
            <div class="z-depth-1 grey lighten-4 row" style="display: inline-block; padding: 0px 48px 0px 48px; border: 1px solid #EEE;">

                <form class="col s12" method="post">
                    <div class='row'>
                        <div class='col s12'>
                        </div>
                    </div>

                    <div class='row'>
                        <div class='input-field col s12'>
                            <p>Username:</p>
                            <form:input type="text" path="username"/>
                            <form:errors path="username" cssClass="formError" element="p" Style="size: 1px"/>
                        </div>
                    </div>

                    <div class='row'>
                        <div class='input-field col s12'>
                            <p>Password:</p>
                            <form:input type="password" path="password"/>
                            <form:errors path="password" cssClass="formError" element="p"/>
                        </div>
                        <label style='float: right;'>
                            <a class='light-blue-text' href='#!'><b>Forgot Password?</b></a>
                        </label>
                    </div>

                    <br />
                        <div class='row'>
                            <button type='submit' name='btn_login' class='col s12 btn btn-large waves-effect light-blue'>Login</button>
                        </div>
                </form>
            </div>
        </div>
        <div style="text-align: center"><a href="/register">Create account</a></div>
    </form:form>
    <div class="section"></div>
    <div class="section"></div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>