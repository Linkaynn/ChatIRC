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
            //var msg = eval('(' + evt.data + ')');
            var msg = JSON.parse(evt.data); // native API
            var $messageLine = $('<tr><td class="received">' + msg.received
                    + '</td><td class="user label label-info">' + msg.sender
                    + '</td><td class="message badge">' + msg.message
                    + '</td></tr>');
            $chatWindow.append($messageLine);
        }
        function sendMessage() {
            var msg = '{"message":"' + $message.val() + '", "sender":"'
                    + $nickName.val() + '", "received":""}';
            wsocket.send(msg);
            $message.val('').focus();
        }

        function connectToChatserver() {
            //room = $('#chatroom option:selected').val();
            room = "General";
            wsocket = new WebSocket(serviceLocation + room);
            wsocket.onmessage = onMessageReceived;
        }

        function leaveRoom() {
            wsocket.close();
            $chatWindow.empty();
            $('.chat-wrapper').hide();
            $('.chat-signin').show();
            $nickName.focus();
        }

        $(document).ready(function() {
            $nickName = $('#nickname');
            $message = $('#message');
            $chatWindow = $('#response');
            $('.chat-wrapper').hide();
            $nickName.focus();

            $('#enterRoom').click(function(evt) {
                evt.preventDefault();
                connectToChatserver();
                $('.chat-wrapper h2').text('Chat # '+$nickName.val() + "@" + room);
                $('.chat-signin').hide();
                $('.chat-wrapper').show();
                $message.focus();
            });
            $('#do-chat').submit(function(evt) {
                evt.preventDefault();
                sendMessage();
            });

            $('#leave-room').click(function(){
                leaveRoom();
            });
        });
    </script>

</head>
<body>
    <div id="menu">
        <h1>#Chat General</h1>
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