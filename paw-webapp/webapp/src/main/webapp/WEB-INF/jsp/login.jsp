<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

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

        <h5 class="orange-text" style="text-align: center" >Please, login into your account</h5>
        <div class="section"></div>

        <div class="container" style="text-align: center">
            <div class="z-depth-1 grey lighten-4 row" style="display: inline-block; padding: 32px 48px 0px 48px; border: 1px solid #EEE;">

                <form class="col s12" method="post">
                    <div class='row'>
                        <div class='col s12'>
                        </div>
                    </div>

                    <div class='row'>
                        <div class='input-field col s12'>
                            <input class='validate' type='text' name='username' id='username' />
                            <label for='username'>Enter your username</label>
                        </div>
                    </div>

                    <div class='row'>
                        <div class='input-field col s12'>
                            <input class='validate' type='password' name='password' id='password' />
                            <label for='password'>Enter your password</label>
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

    <div class="section"></div>
    <div class="section"></div>
</main>

<footer class="page-footer orange">
    <%@include file="footer.jsp" %>
</footer>
</body>
</html>