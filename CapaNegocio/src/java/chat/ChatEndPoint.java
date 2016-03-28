package chat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author adrian
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Predicate;
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
            User user = users.containsKey(username) ? users.get(username) : new User(username, room, session);
            users.put(username, user);
            if (rooms.containsKey(room)) rooms.get(room).addUser(user);
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

    private void sendMessage(Session sessions, String message) throws IOException, EncodeException {
        String room = (String) sessions.getUserProperties().get("room");
        for (Session session : sessions.getOpenSessions()) {
            if (session.isOpen() && room.equals(session.getUserProperties().get("room"))) {
                session.getBasicRemote().sendObject(message);
            }
        }
    }

    private void joinedMessage(String username) throws IOException, EncodeException {
        final String room = users.get(username).lastRoom();

        users.get(username).session().getBasicRemote().sendObject(
                new JSONBuilder().put("message").as("Welcome to #" + room + ", " + username + "!")
                        .put("sender").as("#" + room)
                        .put("received").as("").build());
        
        users.get(username).session().getBasicRemote().sendObject(
                new JSONBuilder().put("usernames").as(rooms.get(room).users()).build()
        );

        for (User user: users.values()){
            if (user.session().isOpen() && user.isIn(room) && !user.username().equals(username)){
                user.session().getBasicRemote().sendObject(
                        new JSONBuilder().put("status").as("joined")
                                .put("message").as(username + " has " + "joined.")
                                .put("username").as(username)
                                .put("received").as("").build()
                );
                user.session().getBasicRemote().sendObject(
                        new JSONBuilder().put("usernames").as(rooms.get(room).users()).build()
                );
            }
        }
    }
}
