package util;

import java.lang.String;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.websocket.Session;

public class User {
    private String username;
    private String lastRoom;
    private HashMap<String, Session> sessions = new HashMap<>();
    private boolean typed = false;
    
    @EJB
    private ChatPreferences preferences;

    
    public User(String username, String room, Session session) {
        this.username = username;
        sessions.put(room, session);
        lastRoom = room;
        try {
            preferences = (ChatPreferences)InitialContext.doLookup("java:global/ChatIRC/CapaNegocio/ChatPreferencesLocal");
            preferences.setColor(0);
        } catch (NamingException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    public User(String username, String room, HashMap<String, Session> sessions) {
        this.username = username;
        lastRoom = room;
        this.sessions = sessions;
        try {
            preferences = (ChatPreferences) InitialContext.doLookup("java:global/ChatIRC/CapaNegocio/ChatPreferencesLocal");
            preferences.setColor(0);
        } catch (NamingException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public HashMap<String, Session> sessions() {
        return sessions;
    }

    public boolean isTyped() {
        return typed;
    }

    public void setTyped(boolean typed) {
        this.typed = typed;
    }
    
    public ChatPreferences getPreferences() {
        return preferences;
    }

    void changeColor(String substring) {
        preferences.setColor(Integer.valueOf(substring));
    }
}