        var wsocket;
        var serviceLocation = "ws://localhost:8080/WebSocketChat/chat/";
        var $nickName;
        var $message;
        var $chatWindow;
        var $userWindow;
        var room = '';
        
        var needScroll = false;
        $lastOffset = 0;
        $messagesCount = 0;

        function onMessageReceived(evt) {            
            
            var body = JSON.parse(evt.data).message;
            var sender = JSON.parse(evt.data).sender;
            var _class = sender === $nickName ? "me" : "other";
            var usernames = JSON.parse(evt.data).usernames;
            
            if (body !== undefined)
                $messagesCount++;
            
            manageScroll();
            if (sender === undefined && body !== undefined)
                $chatWindow.html($chatWindow.html() + "<p><span class=\"" + _class + "Message\">" + body + "</span></p>");  
            else if (sender !== undefined)
                $chatWindow.html($chatWindow.html() + "<p><span class=\"" + _class + "\">" + sender  + ":&nbsp</span><span class=\"" + _class + "Message\">" + body + "</span></p>");

            if (needScroll)
                $chatWindow.scrollTop($lastOffset);
            
            if (usernames !== undefined){
                var x = usernames.split(",");
                $userWindow.html("");
                for (var i=0; i<x.length; i++){
                    $userWindow.html($userWindow.html() + "<p><span class=\"" + _class + "\">" + x[i] + "</span></p>");
                }
            }
            
        }
        
        function manageScroll(){
            if ($messagesCount > 23 && $chatWindow.scrollTop() === $lastOffset){
                needScroll = true;
            }else{
                needScroll = false;
            }
            if ($messagesCount > 23){
                $lastOffset = $lastOffset + 30;
            }
        }
        
        function sendMessage(message) {
            wsocket.send(message);            
            $message.val('').focus();
        }

        function connectToChatserver() {
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
        
        function setUsername(username){
            $nickName = username;
        }
        
        function ready() {
            $message = $('#message');
            $chatWindow = $('#chatContent');
            $userWindow = $('#userContent');
            room = $('#title').text().split(" ")[0].substring(1);
                                  
            connectToChatserver();
            $message.focus();
                        
            
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
        }
