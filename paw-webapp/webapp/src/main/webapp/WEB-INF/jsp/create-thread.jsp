<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="header.jsp" %>
    <title>Create Thread - PowerUp</title>
</head>
<body>
<header>
    <%@include file="nav.jsp" %>
</header>

<main class="section">
    <div class="container row">
        <%--Title--%>
        <h1 class="center">Create Thread</h1>

        <c:url value="/create-thread" var="postUrl" />
        <form:form modelAttribute="createThreadForm" action="${postUrl}" method="POST">
            <div class='row'>
                <div class='input-field col s12'>
                    <form:input type="text" path="title" required="required" length="50" />
                    <form:label path="title">Title</form:label>
                    <form:errors path="title" cssClass="formError" element="p"/>
                </div>
                <div class="row"></div>
                <div class='input-field col s12'>
                    <form:textarea path="body" cssClass="materialize-textarea" />
                    <form:label path="body">Initial comment (optional)</form:label>
                    <form:errors path="body" cssClass="formError" element="p"/>
                </div>
            </div>

            <div class="row">
                <div class="col s4 offset-s5">
                    <button type='submit' name='btn_login' class='col s6 btn btn-large waves-effect light-blue'>
                        Submit <i class="material-icons right">send</i>
                    </button>
                </div>
            </div>
        </form:form>
    </div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>