/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import chat.ChatEndPoint;
import java.util.Date;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author adrian
 */
@Stateless
@LocalBean
public class TimeOut {

    @Schedule(dayOfWeek = "*", month = "*", hour = "*", dayOfMonth = "*", year = "*", minute = "*/1", second = "0")
    
    public void timeOut(){
        for(Room room : ChatEndPoint.getChatRooms().values()){
            System.out.println("ROOOOOOOOMS " + ChatEndPoint.getChatRooms().values().size());
            room.timeOut();
        }
    }

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
