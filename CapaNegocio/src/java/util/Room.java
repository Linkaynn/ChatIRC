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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.Session;

public class Room {
    private String name;
    private User owner;
    private boolean isPublic; 
    private String password;
    private ArrayList<User> users = new ArrayList<>();

    public Room(String name, User owner, boolean isPublic) {
        this.name = name;
        this.owner = owner;
        this.isPublic = isPublic;
        users.add(owner);
    }
    
    public Room(String name, User owner, boolean isPublic, String password) {
        this(name, owner, isPublic);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
    public void addUser(User user){
        if(!users.contains(user))
            users.add(user);
    }
    
    public void removeUser(User user){
        users.remove(user);
    }
    
    public void removeUser(String userName) throws IOException, EncodeException{
        for (User user: users){
            if(user.username().equals(userName)){ 
                users.remove(user);
                updateUserList(user, "has left.");
                return;
            }
        }
    }
    
    public ArrayList<String> userList(){
        ArrayList<String> usernames = new ArrayList<>();
        for (User user: users) usernames.add(user.username());
        Collections.sort(usernames);
        return usernames;
    }
    
    public boolean userExists(User username){
        return users.contains(username);
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
    
    public void joinedMessage(User user) throws IOException, EncodeException {

        user.session(name).getBasicRemote().sendObject(
                new JSONBuilder().put("message").as("Welcome to #" + name + ", " + user.username() + "!")
                        .put("sender").as("#" + name)
                        .put("received").as("").build());
        
        user.session(name).getBasicRemote().sendObject(
                new JSONBuilder().put("usernames").as(users()).build()
        );

        updateUserList(user, "has joined.");
    }

    private void updateUserList(User user, String message) throws IOException, EncodeException {
        for (User u: users){
            if (u.session(name).isOpen() && u.isIn(name) && !u.username().equals(user.username())){
                u.session(name).getBasicRemote().sendObject(
                        new JSONBuilder()
                                .put("message").as(user.username() + " " + message)
                                .put("username").as(user.username())
                                .put("received").as("").build()
                );
                u.session(name).getBasicRemote().sendObject(
                        new JSONBuilder().put("usernames").as(users()).build()
                );
            }
        }
    }

    private void exitRoomInstruction(String sender)  {
        ChatEndPoint.removeUser(sender);
        try {
            removeUser(sender);
        } catch (IOException | EncodeException ex) {
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

}
