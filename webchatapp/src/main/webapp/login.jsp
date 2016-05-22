<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en" >
<head>
    <meta charset="utf-8">

    <title>Sign In</title>

    <style>

        <%@include file='css/bootstrap.css' %>
        <%@include file='css/signin.css' %>

    </style>

    <script src="js/scrypt.js" ></script>

</head>

<body>
<div class="site-wrapper">

    <div class="site-wrapper-inner">

        <div class="cover-container">



            <div class="inner cover">
                <form class="form-signin" action="/login" method="post">
                    <h2 class="form-signin-heading">Hello. Sign in.<a href="/signup">sign up</a></h2>
                    <input type="text" class="form-control" id="input_name" name="username" placeholder="login" required autofocus>
                    <input type="password" class="form-control" name="password" placeholder="Password" required>
                    <button class="btn btn-lg btn-primary btn-block" onclick="loginInput()" type="submit">login</button>

                </form>
            </div>

            <div class="mastfoot">
                <div class="inner">
                    <p>by Eugene sayko, 2016.</p>
                </div>
            </div>

        </div>

    </div>

</div>

</body>
</html>