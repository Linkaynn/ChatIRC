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

@ServerEndpoint(value = "/chat/{room}/{username}/{password}/{isPrivate}")
public class ChatEndPoint {

    private final Logger log = Logger.getLogger(getClass().getName());
    private static HashMap<String, User> users = new HashMap<>();
    private static HashMap<String, Room> rooms = new HashMap<>();

    @OnOpen
    public void open(final Session session, @PathParam("room") final String room, @PathParam("username") final String username, @PathParam("password") final String password, @PathParam("isPrivate") final String isPrivate) {
        try {
            User user;
            if (rooms.containsKey(room)){
                if (rooms.get(room).getPassword().equals(password)){
                  if (users.containsKey(username)){
                    user = users.get(username);
                    user.addSession(room, session);
                  }else
                    user = new User(username, room, session);
                  users.put(username, user);
                  rooms.get(room).addUser(user);
                  rooms.get(room).joinedMessage(user);
                }
            }else{
                if (users.containsKey(username)){
                    user = users.get(username);
                    user.addSession(room, session);
                  }else
                    user = new User(username, room, session);
                users.put(username, user);
                rooms.put(room, new Room(room, user, true, password));
                rooms.get(room).joinedMessage(user);
            }
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

    public static HashMap<String, Room> getChatRooms(){
        return rooms;
    }
    
    public static void removeUser(String username) {
        users.remove(username);
    }
}
