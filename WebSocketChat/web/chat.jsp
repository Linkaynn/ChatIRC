<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>#<% out.print(request.getParameter("room")); %></title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="css/chatGeneral.css"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
    <link href="libs/botstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet">
    <script src="libs/botstrap-switch/js/bootstrap-switch.min.js"></script>
    <script src="js/chat.js"></script>

</head>
<body class="container">
    <div id="menu chat-wrapper" class="container">
        <h1 id="title" >#<% out.print(request.getParameter("room")); %></h1>
    <div>
        
    </div>
    <ul>
        <li><a href="#"><i class="fa fa-user"></i> @<% out.print(request.getParameter("username"));  %></a> </li>

        <div class="pull-right">
        <li ><a href="#" data-toggle="modal" data-target="#myModal"><i title="Crear sala" class="fa fa-plus"></i></a></li>
        <li id="listRooms"><a href="#"><i title="Listar salas" class="fa fa-list-alt"></i></a></li>
        <li class="dropdown">
            <a href="javascript:void(0)" class="dropbtn" onclick="myFunction()"><i title="Ajustes" class="fa fa-cog"></i></a>
            <div class="dropdown-content" id="myDropdown">
                <a href="#">Link 1</a>
                <a href="#">Link 2</a>
                <a href="#">Link 3</a>
            </div>
        </li>
        <li id="leave-room">
            <a href="http://localhost:8080/WebSocketChat"><i title="Abandonar sala" class="fa fa-times"></i></a>
        </li>
        </div>
    </ul>
     
</div>
    
<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">
    <form action="FrontController" target="_blank">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">&times;</button>
              <h2 class="modal-title" style="margin: 15px 0 10px 0;">Create room</h2>
              <input type="hidden" name="username" value="<% out.print(request.getParameter("username")); %>">
              <p style="margin-bottom: 5px"><input type="text" placeholder="Name of the room..." name="room"><br></p>
              <p>
              <input id="private-room-checkbox" type="checkbox" name="my-checkbox">
                  <label>&nbsp;&nbsp;Private</label> 
              <p><input hidden="hidden" id="private-room-password" type="password" placeholder="Password" /></p>
              </p>
            </div>
            <div class="modal-footer">
                <input type="hidden" name="command" value="Anonymous">
                <input type="submit" class="btn btn-default" value="Create">
            </div>
        </div>
    </div>
    </form>
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
    $("[name='my-checkbox']").bootstrapSwitch();

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
    };
    setUsername("@<% out.print(request.getParameter("username"));  %>");
    ready();
</script>
<script src="js/util.js"></script>

</body>
</html>