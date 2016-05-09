package util;

import chat.ChatEndPoint;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
@Stateless
@LocalBean
public class TimeOut {

    @Schedule(dayOfWeek = "*", month = "*", hour = "*", dayOfMonth = "*", year = "*", minute = "*/5", second = "0")
    
    public void timeOut(){
        for(Room room : ChatEndPoint.getChatRooms().values()){
            room.timeOut();
        }
    }
}
