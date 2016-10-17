<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <div class="section">
        <h2 class="orange-text center">Register</h2>
    </div>

    <div class="section">
        <div class="container row">
            <div class="col s8 offset-s2">
                <div class="card-panel grey lighten-3 col s12">
                    <div class="container">
                        <c:url value="/register" var="postUrl"/>
                        <form:form modelAttribute="registerForm" action="${postUrl}" method="post" class="center-align">
                            <form method="post">
                                <div class='row'>
                                    <div class='input-field col s6'>
                                        <p>Email:</p>
                                        <form:input type="email" path="email"/>
                                        <form:errors path="email" cssClass="formError" element="p"/>
                                    </div>
                                    <div class='input-field col s6'>
                                        <p>Username:</p>
                                        <form:input type="text" path="username"/>
                                        <form:errors path="username" cssClass="formError" element="p" Style="size: 1px"/>
                                        <%--TODO check if username exists onChange--%>
                                    </div>
                                </div>
                                <div class='row'>
                                    <div class='input-field col s6'>
                                        <p>Password:</p>
                                        <form:input type="password" path="password"/>
                                        <form:errors path="password" cssClass="formError" element="p"/>
                                    </div>
                                    <div class='input-field col s6'>
                                        <p>Repeat Password:</p>
                                        <form:input type="password" path="repeatPassword"/>
                                        <form:errors path="repeatPassword" cssClass="formError" element="p"/>
                                    </div>
                                </div>

                                <div class='row'>
                                    <div class="col s3"></div>
                                    <button type='submit' name='btn_login' class='col s6 btn btn-large waves-effect light-blue'>
                                        Submit <i class="material-icons right">send</i></button>
                                </div>
                            </form>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
        <h5 class="center"><a href="<c:url value="/login"/>">Log In</a></h5>
    </div>
</main>
<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>