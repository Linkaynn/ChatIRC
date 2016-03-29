/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import chat.ChatEndPoint;
import com.sun.xml.ws.util.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collector;
import javax.websocket.EncodeException;
import javax.websocket.Session;

/**
 *
 * @author adrian
 */
public class Room {
    private String name;
    private User owner;
    private boolean isPublic; 
    private ArrayList<User> users = new ArrayList<>();

    public Room(String name, User owner, boolean isPublic) {
        this.name = name;
        this.owner = owner;
        this.isPublic = isPublic;
        users.add(owner);
        owner.enter(name);
    }
    
    public void addUser(User user){
        users.add(user);
        user.enter(name);
    }
    
    public void removeUser(User user){
        users.remove(user);
    }
    
    public ArrayList<String> userList(){
        ArrayList<String> usernames = new ArrayList<>();
        for (User user: users) usernames.add(user.username());
        Collections.sort(usernames);
        return usernames;
    }
    
    public String users(){
        String result = "";
        for (String user : userList()) {
            result += user + ",";
        }
        return result.substring(0, result.length() - 1);
    }

    public void processMesage(Session session, String JSON) throws IOException, EncodeException{
        String msg = JSON.substring(JSON.indexOf(":") +2, JSON.indexOf(",") -1);
        if (isInstruction(msg))
            session.getBasicRemote().sendObject(processInstruction(msg, JSON));
        else
            for (User user : users)
                user.session().getBasicRemote().sendObject(JSON);
    }

    private boolean isInstruction(String message) {
        return message.charAt(0) == '/';
    }

    private String processInstruction(String msg, String JSON) {
        String sender = JSON.substring(JSON.indexOf("\"sender\":") + "\"sender\":".length() + 1, JSON.indexOf("\", \"received"));
        JSON = JSON.replaceFirst(sender, "#" + name);
        if (msg.toLowerCase().equals("/salas"))
            return JSON.replaceFirst(msg, listRoomInstruction());
        if (msg.toLowerCase().substring(0, 3).equals("/me"))
            return JSON.replaceFirst(msg.substring(0, 3), sender);
        else
            return JSON.replaceFirst(msg, "Command not found.");
    }

    private String listRoomInstruction() {
        String rooms = "";
        for (Room room: ChatEndPoint.getChatRooms().values())
            rooms += room.name + "<br>";
        return rooms;
    }
    
}
