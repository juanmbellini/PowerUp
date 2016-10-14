
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>Register - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>
<h2 class="center-align">Register</h2>
<c:url value="/create" var="postPath"/>
<form:form modelAttribute="registerForm" action="${postPath}" method="post" class="center-align">
    <div class="row">
        <div class="col s4"></div>
        <div class="col s4 center-align">
            <form:label path="username">Username: </form:label>
            <form:input type="text" path="username"/>
            <form:errors path="username" cssClass="formError" element="p" Style="size: 1px"/>
        </div>
    </div>

    <div class="row">
        <div class="col s4"></div>
        <div class="col s4 center-align">
            <form:label path="password">Password: </form:label>
            <form:input type="password" path="password" />
            <form:errors path="password" cssClass="formError" element="p"/>
        </div>
    </div>
    <div class="row">
        <div class="col s4"></div>
        <div class="col s4 center">
            <form:label path="repeatPassword">Repeatpassword: </form:label>
            <form:input type="password" path="repeatPassword"/>
            <form:errors path="repeatPassword" cssClass="formError" element="p"/>
        </div>
    </div>
    <div class="row">
        <div class="col s4"></div>
        <div class="col s4 center">
            <form:label path="email">email: </form:label>
            <form:input type="email" path="email"/>
            <form:errors path="email" cssClass="formError" element="p"/>
        </div>
    </div>
    <div class="row">
        <div class="col s4"></div>
        <div class="col s4 center">
            <input type="submit" value="Register!"/>
        </div>
    </div>
</form:form>
</body>
</html>