/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.sun.xml.ws.util.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collector;

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
    
}
