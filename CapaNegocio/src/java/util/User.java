package util;

import java.lang.String;
import java.util.ArrayList;
import javax.websocket.Session;

public class User {
    private String username;
    private ArrayList<String> rooms = new ArrayList<>();
    private Session session;

    public User(String username, String room, Session session) {
        this.username = username;
        enter(room);
        this.session = session;
    }

    public void enter(String room) {
        rooms.add(room);
    }

    public void exit(String room){
        rooms.remove(room);
    }

    public String username() {
        return username;
    }

    public Session session() {
        return session;
    }

    public boolean isIn(String room){
        return rooms.contains(room);
    }
    
    public String lastRoom(){
        return rooms.get(rooms.size() - 1);
    }
}