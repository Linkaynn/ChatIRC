package chat;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import util.*;

@ServerEndpoint(value = "/chat/{room}/{username}")
public class ChatEndPoint {

    private final Logger log = Logger.getLogger(getClass().getName());
    private static HashMap<String, User> users = new HashMap<>();
    private static HashMap<String, Room> rooms = new HashMap<>();

    @OnOpen
    public void open(final Session session, @PathParam("room") final String room, @PathParam("username") final String username) {
        try {
            User user;
            if (users.containsKey(username)){
                user = users.get(username);
                user.addSession(room, session);
            }else
                user = new User(username, room, session);
            users.put(username, user);
            if (rooms.containsKey(room) && !rooms.get(room).userExists(user)) rooms.get(room).addUser(user);
            else rooms.put(room, new Room(room, user, true));
            joinedMessage(username);
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(ChatEndPoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            sendMessage(session, message);
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(ChatEndPoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendMessage(Session session, String message) throws IOException, EncodeException {
        String room = message.substring(message.lastIndexOf(":") + 2, message.lastIndexOf('"'));
        rooms.get(room).processMesage(session, message);
    }

    private void joinedMessage(String username) throws IOException, EncodeException {
        final String room = users.get(username).lastRoom();

        users.get(username).session(room).getBasicRemote().sendObject(
                new JSONBuilder().put("message").as("Welcome to #" + room + ", " + username + "!")
                        .put("sender").as("#" + room)
                        .put("received").as("").build());
        
        users.get(username).session(room).getBasicRemote().sendObject(
                new JSONBuilder().put("usernames").as(rooms.get(room).users()).build()
        );

        for (User user: users.values()){
            if (user.session(room).isOpen() && user.isIn(room) && !user.username().equals(username)){
                user.session(room).getBasicRemote().sendObject(
                        new JSONBuilder().put("status").as("joined")
                                .put("message").as(username + " has " + "joined.")
                                .put("username").as(username)
                                .put("received").as("").build()
                );
                user.session(room).getBasicRemote().sendObject(
                        new JSONBuilder().put("usernames").as(rooms.get(room).users()).build()
                );
            }
        }
    }
    
    public static HashMap<String, Room> getChatRooms(){
        return rooms;
    }
    
    public static void removeUser(String username) {
        users.remove(username);
    }
}
