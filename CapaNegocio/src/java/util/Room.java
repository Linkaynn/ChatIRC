package util;

import chat.ChatEndPoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
    
    private PrivateRoomWrapper wrapper;
    
    public Room(String name, User owner, boolean isPublic) {
        this.name = name;
        this.owner = owner;
        this.isPublic = isPublic;
        users.add(owner);
        admins.add(owner);
        SingletonBean.addUser();
        try {
            wrapper = (PrivateRoomWrapper) InitialContext.doLookup("java:global/ChatIRC/CapaNegocio/PrivateRoomWrapper");
        } catch (NamingException ex) {
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        if(!users.contains(user) && !bannedusers.contains(user)){
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
    
    public String users(String username){
        String result = "";
        for (String user : userList()) {
            result += wrapper.wrap(user, username) + ",";
        }
        return result.substring(0, result.length() - 1);
    }

    public void processMesage(Session session, String JSON) throws IOException, EncodeException{
        String msg = JSON.substring(JSON.indexOf(":") +2, JSON.indexOf("sender") -4);
        String sender = JSON.substring(JSON.indexOf("\"sender\":") + "\"sender\":".length() + 1, JSON.indexOf("\", \"received"));
        
        
        if (isInstruction(msg)){
            String message = processInstruction(msg, JSON);
            if (message.length() != 0)
                session.getBasicRemote().sendObject(message);
        }else
            broadCast(JSON.replace(msg, ChatFilter.filter(msg)));
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
            if (!name.equals("General")){ 
                System.out.println("2");
                banUser(msg.substring(5).trim());
                JSON = JSON.replaceFirst(msg, msg.substring(5)  + " kicked from the chat room");
                broadCast(JSON);
                return "";
            }else{
                return "You have no power here";
            }
        }else if (msg.toLowerCase().substring(0,22).equals("/notificateprivatechat")){
            String target = msg.split(" ")[1];
            String url = JSON.replace(msg, msg.substring(msg.indexOf("Hi,")).replace(sender.substring(1), target.substring(1)));
            for (Room room : ChatEndPoint.getChatRooms().values()) {
                for (User user : room.users) {
                    if (user.username().equals(target))
                        try {
                            user.session(room.name).getBasicRemote().sendObject(url);
                    } catch (IOException | EncodeException ex) {
                        Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return "";
        }else if (msg.toLowerCase().substring(0,7).equals("/remove")){
            for (User admin : admins) {
                if (admin.username().equals(sender.substring(1))){
                    try {
                        removeUser(msg.split(" ")[1].substring(1));
                        broadCast(JSON.replace(msg, msg.split(" ")[1].substring(1) + " has been removed."));
                    } catch (IOException | EncodeException ex) {
                        Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return "";
        }else if (msg.toLowerCase().substring(0,7).equals("/delete")){
            if (sender.substring(1).equals(owner.username()))
                for (User user : users){
                  removeUser(user);
                }
          
            return "";
        }else if (msg.toLowerCase().substring(0,4).equals("/add")){
            if (sender.substring(1).equals(owner.username()))
                for (User user : users) 
                    if (user.username().equals(sender.substring(1))){
                        admins.add(user);
                        broadCast(JSON.replace(msg, user.username() + " has been promoted."));
                    }
            return "";
        }else if (msg.toLowerCase().substring(0,4).equals("/del")){
            if (sender.substring(1).equals(owner.username()))
                for (User admin : admins) 
                    if (admin.username().equals(sender.substring(1))){
                        admins.remove(admin);
                        broadCast(JSON.replace(msg, admin.username() + " has been degraded."));
                    }
            return "";
        }else if (msg.toLowerCase().substring(0,9).equals("/password")){
            if (sender.substring(1).equals(owner.username()))
                if (msg.split(" ")[1].equals(msg.split(" ")[2])){
                    password = msg.split(" ")[1];
                    return JSON.replace(msg, "The password has been changed");
                }
            return "";
        }else if (msg.toLowerCase().substring(0,5).equals("/name")){
            for (User admin : admins) 
                if (admin.username().equals(sender.substring(1))){
                    name = msg.split(" ")[1];
                    broadCast(JSON.replace(msg, owner + " has change room's name, please reload."));
                }
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
        for (User u : bannedusers) {
            if (u.username().equals(user.username())) return;
        }
        
        user.session(name).getBasicRemote().sendObject(
                new JSONBuilder().put("message").as("Welcome to #" + name + ", " + user.username() + "!")
                        .put("sender").as("#" + name)
                        .put("received").as("").build());
        
        user.session(name).getBasicRemote().sendObject(
                new JSONBuilder().put("usernames").as(users(user.username())).build()
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
                        new JSONBuilder().put("usernames").as(users(u.username())).build()
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
            if ((user.username()).equals(sender)){
                bannedusers.add(user);
                users.remove(user);
                user.exit(name);
                exitRoomInstruction(sender);
            }
        }
    }
    
    public String name(){
        return name;
    }
    
    
    public void timeOut(){
        for (User user : users) {
            if(user.isTyped()) user.setTyped(false);
            else{
                try {
                    exitRoomInstruction(user.username());
                    users.remove(user);
                    user.session(name).getBasicRemote().sendObject(
                            new JSONBuilder()
                                    .put("sender").as(user.username())
                                    .put("message").as("Has sido desconectado de la sala " + name + " por inactividad.")
                                    .put("received").as("").build());
                    user.exit(name);
                } catch (IOException | EncodeException ex) {
                    Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }
            }
        }
    }
   
}
