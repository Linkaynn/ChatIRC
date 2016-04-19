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
    <script>var password = <% out.print(request.getParameter("password")); %>   </script>
    <script src="js/chat.js"></script>

</head>
<body class="container">
    <div id="menu chat-wrapper" class="container">
        <h1 id="title" >#<% out.print(request.getParameter("room")); %></h1>
    <div>
        
    </div>
    <ul class="menuOptions">
        <li><a href="#"><i class="fa fa-user"></i> @<% out.print(request.getParameter("username"));  %></a> </li>

        <div class="pull-right">
        <li ><a onclick="changeModal('Create room', 'Create')" href="#" data-toggle="modal" data-target="#myModal"><i title="Crear sala" class="fa fa-plus"></i></a></li>
<li><a onclick="changeModal('Join room', 'Join')" href="#" data-toggle="modal" data-target="#myModal"><i title="Unirse a sala" class="fa fa-users" aria-hidden="true"></i></a></li>
        <li id="listRooms"><a href="#"><i title="Listar salas" class="fa fa-list-alt"></i></a></li>
        <li class="dropdown">
           <a href="#" data-toggle="modal" data-target="#myModalAjustes"><i title="Ajustes" class="fa fa-cog"></i></a>
        </li>
        <li id="leave-room">
            <form id="exitForm" action="FrontController">
                <input type="hidden" name="username" value=<% out.print(request.getParameter("username"));  %>>
                <input type="hidden" name="command" value="Logout">
                <a style="cursor: pointer;" onclick="leaveRoom()" ><i title='Abandonar sala' class='fa fa-times'></i></a>
            </form>
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
              <div class="col-md-12"><p style="margin-bottom: 5px"><input type="text" placeholder="Name of the room..." name="room"><br></p></div>
              <p>
                
              <div class="col-md-3">
                  <label>&nbsp;&nbsp;Private</label>
                  <input id="private-checkbox" type="checkbox" name="my-checkbox" onclick="status()">
                </div
              </p>
              <p>
              <div class="col-md-12"><input hidden="hidden" id="private-room-password" name="password" type="password" placeholder="Password" /></div>
              </p>              
            </div>
            <div class="modal-footer">
                <input type="hidden" name="command" value="Anonymous">
                <input type="submit" id="modal-submit" class="btn btn-default" value="Create">
            </div>
        </div>
    </div>
    </form>
</div>
              
<!-- Modal Ajustes-->
<div class="modal fade" id="myModalAjustes" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h1 class="modal-title" style="margin: 15px 0 10px 0;">Preferences</h1>
 
                <div class="themes">
                    <h2 style="margin: 15px 0 10px 0;">Change Theme</h2>
                    <button onclick="changeTheme('blue')">
                        <span class="glyphicon glyphicon-tint" style="color:#226765"></span>
                    </button>
                    <button onclick="changeTheme('purple')">
                        <span class="glyphicon glyphicon-tint" style="color:#6E256E"></span></button>
                    </button>
                    <button onclick="changeTheme('red')">
                        <span class="glyphicon glyphicon-tint" style="color:#A8383B"></span>
                    </button>
                    <button onclick="changeTheme('brown')">
                        <span class="glyphicon glyphicon-tint" style="color:#AA7369"></span>
                    </button>
                    <button onclick="changeTheme('green')">
                        <span class="glyphicon glyphicon-tint" style="color:#2D882D"></span>
                    </button>
                </div>
                <div class="legends">
                    <h2 style="margin: 15px 0 10px 0;">Legend</h2>
                    <ul class="legend-ul">
                        <li><b>/sala</b> - List chat rooms available</li>
                        <li><b>/me text</b> - The server displays "username text"</li>
                        <li><b>/join roomName</b> - Enters a room</li>
                        <li><b>@username</b> - Receive notifications</li>
                        <li><b>/ignore @username</b> - Ignore a user</li>
                        <li><b>/unignore @username</b> - Unignore a user</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
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
    
    var checkBoxStatus = false;
    
        var colors = {
    blue  :  {
        menu: '#00BCD4',
        body: '#80DEEA',
        title: '2px 2px 4px #006064'
    },
    purple : {
        menu: '#673AB7',
        body: '#B39DDB',
        title: '2px 2px 4px #311B92'
    },
    red : {
        menu: '#F44336',
        body: '#EF9A9A',
        title: '2px 2px 4px #B71C1C'
    },
    brown : {
        menu: '#795548',
        body: '#BCAAA4',
        title: '2px 2px 4px #3E2723'
    },
    green : {
        menu: '#8BC34A',
        body: '#C5E1A5',
        title: '2px 2px 4px #33691E'
    }
};
    
    function status(){
        checkBoxStatus = !checkBoxStatus;
        if(!checkBoxStatus)
            $('#private-room-password').attr("hidden", true);
        else
            $('#private-room-password').removeAttr("hidden");            
    }

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
    
    function changeModal(title, submit){
        $('.modal-title').text(title);
        $('#modal-submit').val(submit);
    }
       
 
function changeTheme(nameColor){
    if(colors[nameColor]){
        $('.menuOptions').css({"background-color":colors[nameColor].menu});
        $('body').css({"background-color":colors[nameColor].body});
        $('h1').css({"text-shadow":colors[nameColor].title});
        $('#myModalAjustes').modal('hide');
    }
}
</script>
<script src="js/util.js"></script>

</body>
</html>