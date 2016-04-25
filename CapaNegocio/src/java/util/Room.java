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
    private ArrayList<User> admins = new ArrayList<>();
    private ArrayList<User> bannedusers = new ArrayList<>();
    
    public Room(String name, User owner, boolean isPublic) {
        this.name = name;
        this.owner = owner;
        this.isPublic = isPublic;
        users.add(owner);
        admins.add(owner);
        SingletonBean.addUser();
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
        if(!users.contains(user)){
            users.add(user);
            SingletonBean.addUser();
        }
    }
    
    public void removeUser(User user){
        users.remove(user);
    }
    
    public void removeUser(String userName) throws IOException, EncodeException{
        for (User user: users){
            if(user.username().equals(userName)){ 
                users.remove(user);
                updateUserList(user, "has left.");
                SingletonBean.removeUser();
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
    
    public ArrayList<String> adminList(){
        ArrayList<String> adminNames = new ArrayList<>();
        for (User admin: users) adminNames.add(admin.username());
        Collections.sort(adminNames);
        return adminNames;
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
        if (isInstruction(msg)){
            String message = processInstruction(msg, JSON);
            if (message.length() != 0)
                session.getBasicRemote().sendObject(message);
        }else
            broadCast(JSON);
           
    }

    private boolean isInstruction(String message) {
        return message.charAt(0) == '/';
    }

    private String processInstruction(String msg, String JSON) {
        String sender = JSON.substring(JSON.indexOf("\"sender\":") + "\"sender\":".length() + 1, JSON.indexOf("\", \"received"));
        if (msg.toLowerCase().equals("/salas")){
            JSON = JSON.replaceFirst(sender, "#" + name);
            return JSON.replaceFirst(msg, listRoomInstruction());
        }else if (msg.toLowerCase().equals("/exit")){
            exitRoomInstruction(sender);
            return "";
        }
        else if (msg.toLowerCase().substring(0, 3).equals("/me")){
            JSON = JSON.replaceFirst(sender, "#" + name);
            JSON = JSON.replaceFirst("/me", sender + " says ");
            broadCast(JSON);
            return "";
        }else if (msg.toLowerCase().substring(0,4).equals("/ban")){
            JSON = JSON.replaceFirst(msg, msg.substring(5)  + " kicked from the chat room");
            broadCast(JSON);
            banUser(JSON.substring(5).trim());
            return "";
        }else
            return JSON.replaceFirst(msg, "Command not found.");
    }
    
    private void broadCast(String JSON){
        for (User user : users)
            try {
                user.session(name).getBasicRemote().sendObject(JSON);
            } catch (IOException | EncodeException ex) {
                Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    private String listRoomInstruction() {
        String rooms = "";
        for (Room room: ChatEndPoint.getChatRooms().values())
            rooms += room.name + "<br>";
        return rooms;
    }
    
    public void joinedMessage(User user) throws IOException, EncodeException {
        for (User u : users) {
            if (u.username().equals(u.username())) return;
        }
        
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

    private void banUser(String sender) {
        for (User user : users) {
            if (("@"+user.username()).equals(sender)){
                bannedusers.add(user);
                user.exit(name);
            }
        }
    }
   
}
