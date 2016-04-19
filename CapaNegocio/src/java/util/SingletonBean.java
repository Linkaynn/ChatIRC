/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 *
 * @author adrian
 */
@Singleton
public class SingletonBean {
    
    static int userCount = 0;

    @PostConstruct
    public void init(){
        
    }
    
    @PreDestroy
    public void destruct(){
        
    }
    
    public static void addUser(){
        userCount++;
    }
    
    public static void removeUser(){
        userCount--;
    }
    
    public int getUsers(){
        return userCount;
    }
    
    
    @Schedule(second = "*", minute = "*", hour="*")
    public void scheduleTimer() {
        sendMessage("HOLA");
    }

    public void sendMessage(String hola) {
        
    }
   
    
}
