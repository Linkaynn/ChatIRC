package chat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author adrian
 */
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import util.JSONBuilder;
 
@ServerEndpoint(value = "/chat/{room}/{username}")
public class ChatEndPoint {
	private final Logger log = Logger.getLogger(getClass().getName());
 
	@OnOpen
	public void open(final Session session, @PathParam("room") final String room, @PathParam("username") final String username) {
            try {
                session.getUserProperties().put("room", room);
                session.getUserProperties().put("username", username);
                joinedMessage(session);
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
       
        private void sendMessage(Session sessions, String message) throws IOException, EncodeException{
            String room = (String) sessions.getUserProperties().get("room");
            for (Session session : sessions.getOpenSessions()) {
		if (session.isOpen() && room.equals(session.getUserProperties().get("room"))) {
                    session.getBasicRemote().sendObject(message);
		}
            }
        }
        
        private void joinedMessage(Session sessions) throws IOException, EncodeException{
            String room = (String) sessions.getUserProperties().get("room");
            String username = (String) sessions.getUserProperties().get("username");
            String usernames = "";
            
            sessions.getBasicRemote().sendObject(
                        new JSONBuilder().put("message").as("Welcome to #" + room + ", " + username + "!")
                                         .put("sender").as("#" + room)
                                         .put("received").as("").build());

            for (Session session : sessions.getOpenSessions()) {
		if (session.isOpen() && room.equals(session.getUserProperties().get("room")) && !session.getUserProperties().get("username").equals(username)) {
                    session.getBasicRemote().sendObject(
                            new JSONBuilder().put("status").as("joined")
                            .put("message").as(username + " has " + "joined.")
                            .put("username").as(username)
                            .put("received").as("").build()
                    );
		}
                usernames += session.getUserProperties().get("username") + ",";
            }
            
            for (Session session : sessions.getOpenSessions()) {
		if (session.isOpen() && room.equals(session.getUserProperties().get("room"))) {
                    session.getBasicRemote().sendObject(
                            new JSONBuilder().put("usernames").as(usernames.substring(0, usernames.length() -1)).build()
                    );
		}
            }
        }
}
