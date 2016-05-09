package util;

import chat.ChatEndPoint;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

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
    
   
 
   
    
}
