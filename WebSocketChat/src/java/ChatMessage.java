import java.util.Date;
 
public class ChatMessage {
	private String message;
	private String sender;
	private Date received;
 
        
        public String sender(){
            return sender;
        }
        
        public Date received(){
            return received;
        }
        
        public String getMessage(){
            return message;
        }
        
        public void setMessage(String message){
            this.message = message;
        }
        
        public void setSender(String sender){
            this.sender = sender;
        }  
        
}