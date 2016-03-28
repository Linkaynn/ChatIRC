<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>IRC Chat</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="css/index.css"/>
</head>
<body>
    <div class="container-fluid col-md-12">
        <h1>IRC Chat</h1>
        <div id="formEnter" class="col-md-4 col-xs-12">
            <form class="text-center" action="FrontController">
                <input type="text" placeholder="Nick" name="username" id="nickGuest">
                <br>
                <input type="hidden" name="command" value="Anonymous">
                <input class="btn btn-success" type="submit" value="Enter">
            </form>
        </div>
        <div id="formLogin" class="col-md-4 col-xs-12">
            <form class="text-center" action="FrontController">
                <input type="text" placeholder="Username" name="username">
                <br>
                <input type="password" placeholder="Password">
                <br>
                <a href="#">Forgot your password?</a>
                <br>
                <input type="hidden" name="command" value="Login">
                <input class="btn btn-success" type="submit" value="Login">
            </form>
        </div>
        <div id="formRegister" class="col-md-4 col-xs-12">
            <form class="text-center" action="FrontController">
                <input type="text" placeholder="Username" name="username">
                <br>
                <input type="password" placeholder="Password">
                <br>
                <input type="email" placeholder="Email">
                <br>
                <input type="hidden" name="command" value="Register">
                <input class="btn btn-success" type="submit" value="Register">
            </form>
        </div>
<script>
    function generateNick(){
        return "guest" + (Math.floor(Math.random() * 89000) + 10000);
    }
    $('#nickGuest').val(generateNick());
</script>
</body>
</html>
