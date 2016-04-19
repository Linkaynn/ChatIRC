        var wsocket;
        var serviceLocation = "ws://localhost:8080/WebSocketChat/chat/";
        var $nickName;
        var $message;
        var $chatWindow;
        var $userWindow;
        var room = '';
        var $usersIgnore = [];
        
        var needScroll = false;
        var $lastOffset = 0;
        var $messagesCount = 0;
        
        var isIgnored = false;

        function onMessageReceived(evt) {            
            
            var body = JSON.parse(evt.data).message;
            var sender = JSON.parse(evt.data).sender;
            var _class = sender === $nickName ? "me" : "other";
            var usernames = JSON.parse(evt.data).usernames;
            
            if (body !== undefined){
                $messagesCount++;
                if (body.indexOf($nickName) > -1)
                    new Audio("sounds/duck.wav").play();
            }
            
            manageScroll();
            
            $.each($usersIgnore, function(index, user){
                if ("@" + user === sender) isIgnored = true;
            });
            
            if (sender === undefined && body !== undefined)
                $chatWindow.html($chatWindow.html() + "<p><span class=\"" + _class + "Message\">" + body + "</span></p>");  
            else if (sender !== undefined && !isIgnored)
                $chatWindow.html($chatWindow.html() + "<p><span class=\"" + _class + "\">" + sender  + ":&nbsp</span><span class=\"" + _class + "Message\">" + body + "</span></p>");

            if (needScroll)
                $chatWindow.scrollTop($lastOffset);
            
            if (usernames !== undefined){
                var users = usernames.split(",");
                $userWindow.html("");
                for (var i=0; i<users.length; i++){
                    $userWindow.html($userWindow.html() + "<p><span class=\"" + _class + "\">" + users[i] + "</span></p>");
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
            wsocket = new WebSocket(serviceLocation + room + "/" + $nickName + "/" + password);
            wsocket.onmessage = onMessageReceived;
        }

        function leaveRoom() {
            wsocket.send(buildJSON($nickName, "/exit", ""));
            wsocket.close();
            $chatWindow.empty();
            $('#exitForm').submit();
        }
        
        function buildJSON(sender, message, received){
            return '{"message":"' + message + '", "sender":"'+ sender + '", "received":"' + received + '", "room":"' + room + '"}';
        }
        
        function setUsername(username){
            $nickName = username;
        }
        
        function ready() {
            $message = $('#messageChat');
            $chatWindow = $('#chatContent');
            $userWindow = $('#userContent');
            room = $('#title').text().split(" ")[0].substring(1);
                                  
            connectToChatserver();
            $message.focus();
            
            $('#listRooms').bind("listRooms", function(evt){
                evt.preventDefault();                
                sendMessage(buildJSON($nickName, "/salas", ""));
            });
            
            $('#listRooms').click(function (){
               $(this).trigger("listRooms");
            });
            
            $('#messageChat').bind("enterKey", function(evt) {
                evt.preventDefault(); 
                if ($message.val().indexOf("/join") >= 0){
                    var win = window.open("http://localhost:8080/WebSocketChat/FrontController?username=" + $nickName.substring(1) + "&room=" + $message.val().split(" ")[1] + "&command=Anonymous", "_blank");
                    if(win) win.focus();
                    else alert('Please allow popups for this site');
                } else if($message.val().indexOf("/ignore @") >= 0 && $.trim($message.val()).length > 9){
                    var user = $message.val().substring(9 , $message.val().length);
                    var contains = $nickName.substring(1) === user;
                    $.each($usersIgnore, function(index, u){
                        if (user === u) contains = true;
                    });
                    if (!contains){
                        $usersIgnore[$usersIgnore.length] = user;
                        $chatWindow.html($chatWindow.html() + "<p><span class=\"meMessage\">@" + user + " has been ignored. If you want to recieve message again from him you must /unignore @" + user + ".</span></p>");
                    }else{
                        $chatWindow.html($chatWindow.html() + "<p><span class=\"meMessage\">" + ($nickName.substring(1) === user ? "You can't ignore to yourself, kid." : "@" + user + " is already ignored, dude .l.") + "</span></p>");
                    }
                    $message.val('').focus();
                } else if($message.val().indexOf("/unignore @") >= 0 && $.trim($message.val()).length > 11){
                    var user = $message.val().substring(11 , $message.val().length);
                    $usersIgnore.splice($.inArray(user, $usersIgnore), 1);
                    //alert($usersIgnore.length);
                    
                    $chatWindow.html($chatWindow.html() + "<p><span class=\"meMessage\">@" + user + " has been unignored. If you want to ignore again you must type /ignore @" + user + ".</span></p>");
                    
                    $message.val('').focus();
                } else
                    sendMessage(buildJSON($nickName, $message.val(), ""));
            });
            
            $('#messageChat').keyup(function (e){
                if (e.keyCode === 13){
                    $(this).trigger("enterKey");
                }
            });

            $('#leave-room').click(function(){    
                leaveRoom();
            });
        }
        
        function isUsername(username) {
            return ($(username).text().substring(1).toLowerCase().indexOf($('#messageSearch').val()) >= 0);
        }

        function searchUsername(username) {
            if (!isUsername(username)) {
                $(username).hide();
            } else {
                $(username).show();
            }
        }

        function findUser(){
            $('#userContent p').each(function (index, username){
                searchUsername(username);
                if ($('#messageSearch').val() === "") $('#userContent p').show();
            });
        }
