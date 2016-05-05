
package util;

import java.util.Random;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;

@Singleton
@LocalBean
public class PrivateRoomWrapper {
    
    private String generateHash(){
        String numbers = "1234567890";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String upper = lower.toUpperCase();
        
        String hash = "";
        for (int i = 0; i < 10; i++){
            int selection = new Random().nextInt(3);
            switch (selection){
                case 0:
                   hash += numbers.charAt(new Random().nextInt(numbers.length()));
                   break;
                case 1:
                   hash += lower.charAt(new Random().nextInt(lower.length()));
                   break;
                case 2:
                   hash += upper.charAt(new Random().nextInt(upper.length()));
                   break;
            }
        }
        return hash;
    }
    
    public String wrap(String user, String hostUsername){
        return hostUsername.equals(user) 
                ? 
                "<a href='#'>" + user + "</a>" 
                : 
                String.format("<a target='_blank' onclick='sendNotification(this)' href='http://localhost:8080/WebSocketChat/FrontController?username=%s&room=%s&command=Anonymous&isPrivate=1'>%s</a>", hostUsername.substring(1, hostUsername.length()), generateHash(), user);
    }

   
}
