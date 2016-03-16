<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="chatGeneral.css"/>
    <script>
        var wsocket;
        var serviceLocation = "ws://localhost:8080/WebSocketChat/chat/";
        var $nickName;
        var $message;
        var $chatWindow;
        var room = '';

        function onMessageReceived(evt) {            
            $chatWindow.val($chatWindow.val() + JSON.parse(evt.data).message);
        }
        function sendMessage(message) {
            wsocket.send(message);
            $message.val('').focus();
        }

        function connectToChatserver() {
            //room = $('#chatroom option:selected').val();
            alert(serviceLocation + room + "/" + $nickName);
            wsocket = new WebSocket(serviceLocation + room + "/" + $nickName);
            wsocket.onmessage = onMessageReceived;
        }

        function leaveRoom() {
            wsocket.close();
            $chatWindow.empty();
            $('.chat-wrapper').hide();
            $('.chat-signin').show();
            $nickName.focus();
        }
        
        function buildJSON(sender, message, received){
            return '{"message":"' + message + '", "sender":"'+ sender + '", "received":"' + received + '"}';
        }

        
        $(document).ready(function() {
            $nickName = "@<% out.print(request.getParameter("username"));  %>";
            $message = $('#message');
            $chatWindow = $('#textArea');
            room = $('#title').text().split(" ")[0].substring(1);
            
            connectToChatserver();
            $message.focus();
            
            //sendMessage(buildJSON("#General", "Welcome, " + $nickName, ""));
            
            $('#message').bind("enterKey", function(evt) {
                evt.preventDefault();                
                sendMessage(buildJSON($nickName, $message.val(), ""));
            });
            
            $('#message').keyup(function (e){
                if (e.keyCode === 13){
                    $(this).trigger("enterKey");
                }
            });

            $('#leave-room').click(function(){
                leaveRoom();
            });
        });
    </script>

</head>
<body>
    <div id="menu chat-wrapper">
        <h1 id="title" >#General @<% out.print(request.getParameter("username"));  %> </h1>
        <ul class="nav navbar-nav">
            <li><a href="#">Listar Sala</a></li>
            <li><a href="#">Crear sala</a></li>
            <li><a href="#">Ajustes</a></li>
         </ul>
        <form class="navbar-form navbar-left" role="search">
            <div class="input-group add-on">
                <input type="text" class="form-control" placeholder="Username" name="srch-term" id="srch-term">
                <div class="input-group-btn">
                    <button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i></button>
                </div>
            </div>
        </form>
    </div>
        <!--
        	<div class="container-fluid container chat-wrapper" id="chatArea">
		<form id="do-chat">
			<h2 class="alert alert-success"></h2>
			<table id="response" class="table table-bordered"></table>
			<fieldset>
				<legend>Enter your message..</legend>
				<div class="controls">
					<input type="text" class="input-block-level" placeholder="Your message..." id="message" style="height:60px"/>
					<input type="submit" class="btn btn-large btn-block btn-primary"
						value="Send message" />
					<button class="btn btn-large btn-block" type="button" id="leave-room">Leave
						room</button>
				</div>
			</fieldset>
		</form>
	</div>
        -->
    <div class="container-fluid" id="chatArea">
        <div>
            <textarea rows="20" id="textArea"></textarea>
            <textarea rows="20" cols="28"></textarea>
        </div>
        <br>
        <input type="text" class="chatInputText" id="message">
    </div>

</body>
</html>