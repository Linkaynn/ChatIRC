<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="css/chatGeneral.css"/>
    <script src="js/chat.js"></script>

</head>
<body class="container">
    <div id="menu chat-wrapper" class="container">
    <h1 id="title" >#General @<% out.print(request.getParameter("username"));  %> </h1>
    <ul>
        <li><a href="#home">Crear sala</a></li>
        <li><a href="#news">Listar salas</a></li>
        <li class="dropdown">
            <a href="javascript:void(0)" class="dropbtn" onclick="myFunction()">Ajustes</a>
            <div class="dropdown-content" id="myDropdown">
                <a href="#">Link 1</a>
                <a href="#">Link 2</a>
                <a href="#">Link 3</a>
            </div>
        </li>
    </ul>
</div>


<div class="container" id="chatArea">
    <div>
        <div class="col-md-9 chatContentParent">
            <div id="chatContent">
            </div>
            <input autocomplete="off" type="text" class="chatInputText col-xs-12" id="messageChat" placeholder="Write a message...">
        </div>
        <div class="col-md-3 chatContentParent">
            <div id="userContent"></div>
            <input autocomplete="off" type="text" class="chatInputText col-xs-12" id="messageSearch" placeholder="Search a user..." onkeyup="findUser()">
        </div>
    </div>
    <br>
</div>


<script>
    /* When the user clicks on the button,
     toggle between hiding and showing the dropdown content */
    function myFunction() {
        document.getElementById("myDropdown").classList.toggle("show");
    }

    // Close the dropdown if the user clicks outside of it
    window.onclick = function(e) {
                if (!e.target.matches('.dropbtn')) {

                    var dropdowns = document.getElementsByClassName("dropdown-content");
                    for (var d = 0; d < dropdowns.length; d++) {
                        var openDropdown = dropdowns[d];
                        if (openDropdown.classList.contains('show')) {
                            openDropdown.classList.remove('show');
                        }
                    }
                }
            }
    setUsername("@<% out.print(request.getParameter("username"));  %>");
    ready();
</script>

</body>
</html>