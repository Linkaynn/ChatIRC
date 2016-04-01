package util;

import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import javax.websocket.Session;

public class User {
    private String username;
    private String lastRoom;
    private HashMap<String, Session> sessions = new HashMap<>();

    public User(String username, String room, Session session) {
        this.username = username;
        sessions.put(room, session);
        lastRoom = room;
    }

    public void exit(String room){
        sessions.remove(room);
    }

    public String username() {
        return username;
    }

    public void addSession(String room, Session session){
        lastRoom = room;
        sessions.put(room, session);
    }
    
    public Session session(String room) {
        return sessions.get(room);
    }

    public boolean isIn(String room){
        return sessions.containsKey(room);
    }
    
    public String lastRoom(){
        return lastRoom;
    }
}