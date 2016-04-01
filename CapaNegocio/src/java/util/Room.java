/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import chat.ChatEndPoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.websocket.EncodeException;
import javax.websocket.Session;

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
    }
    
    public void addUser(User user){
        users.add(user);
    }
    
    public void removeUser(User user){
        users.remove(user);
    }
    
    public void removeUser(String userName){
        for (User user: users)
            if(user.username().equals(userName)) users.remove(user);
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
        
        if (!isInstruction(msg)){
            if (msg.toLowerCase().substring(0, 3).equals("/me"))
                JSON = meInstruction(msg, JSON);
            for (User user : users)
                user.session(name).getBasicRemote().sendObject(JSON);
        }
        else
            session.getBasicRemote().sendObject(processInstruction(msg, JSON));
           
    }
    
    private String meInstruction(String msg, String JSON){
        String sender = JSON.substring(JSON.indexOf("\"sender\":") + "\"sender\":".length() + 1, JSON.indexOf("\", \"received"));
        JSON = JSON.replaceFirst(sender, "#" + name);
        return JSON.replaceFirst(msg.substring(0, 3), sender); 
    }

    private boolean isInstruction(String message) {
        return !(message.substring(0, 3).equals("/me")) && message.charAt(0) == '/';
    }

    private String processInstruction(String msg, String JSON) {
        String sender = JSON.substring(JSON.indexOf("\"sender\":") + "\"sender\":".length() + 1, JSON.indexOf("\", \"received"));
        JSON = JSON.replaceFirst(sender, "#" + name);
        if (msg.toLowerCase().equals("/salas"))
            return JSON.replaceFirst(msg, listRoomInstruction());
        else if (msg.toLowerCase().equals("/exit")){
            exitRoomInstruction(sender);
            return "";
        }
        else
            return JSON.replaceFirst(msg, "Command not found.");
    }

    private String listRoomInstruction() {
        String rooms = "";
        for (Room room: ChatEndPoint.getChatRooms().values())
            rooms += room.name + "<br>";
        return rooms;
    }

    private void exitRoomInstruction(String sender)  {
        ChatEndPoint.removeUser(sender);
        removeUser(sender);
    }
    
}
