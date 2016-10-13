
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
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
    <div class="section"></div>

    <h2 class="orange-text" style="text-align: center" >Register</h2>
    <c:url value="/create" var="postPath"/>
    <form:form modelAttribute="registerForm" action="${postPath}" method="post" class="center-align">
    <div class="section"></div>
    <div class="container" style="text-align: center">
        <div class="z-depth-1 grey lighten-4 row col" style="padding: 0px 48px 0px 48px; border: 1px solid #EEE;" >
            <form class="col s12" method="post">
                <div class='row'>
                    <div class='col s12'>
                    </div>
                </div>

                <div class='row'>
                    <div class='input-field col s6'>
                        <p>Username:</p>
                        <form:input type="text" path="username"/>
                        <form:errors path="username" cssClass="formError" element="p" Style="size: 1px"/>
                    </div>
                    <div class='input-field col s6'>
                        <p>Password:</p>
                        <form:input type="password" path="password"/>
                        <form:errors path="password" cssClass="formError" element="p"/>
                    </div>
                </div>
                <div class='row'>
                    <div class='input-field col s6'>
                        <p>Email:</p>
                        <form:input type="email" path="email"/>
                        <form:errors path="email" cssClass="formError" element="p"/>


                    </div>
                    <div class='input-field col s6'>
                        <p>Repeat Password:</p>
                        <form:input type="password" path="repeatPassword"/>
                        <form:errors path="repeatPassword" cssClass="formError" element="p"/>
                    </div>
                </div>
                <div class='row'>


                </div>

                <br />
                <div class='row'>
                    <div class="col s3"></div>
                    <button type='submit' name='btn_login' class='col s6 btn btn-large waves-effect light-blue'>Submit <i class="material-icons right">send</i></button>
                </div>
            </form>
        </div>
</div>
    </form:form>
    <div class="section"></div>
    <div class="section"></div>

</main>
<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>