package commands;

import facades.LoginFacadeLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Login extends Command {

    
    @Override
    public void process() {
        LoginFacadeLocal loginFacade = lookupLoginFacadeBean();
        String name = request.getParameter("username");
        String password = request.getParameter("password");
        entities.Login entity = loginFacade.find(name);
        if (entity == null) forward("index.jsp");
        else{
            if (entity.getPassword().equals(password)){
                if(entity.getStatus() == 0){
                    entity.setStatus(1);
                    loginFacade.edit(entity);
                    forward("chat.jsp");
                }
                forward("index.jsp");
            }
            forward("index.jsp");
        }
        forward("index.jsp");
    }

    private LoginFacadeLocal lookupLoginFacadeBean() {
        try {
            Context c = new InitialContext();
            return (LoginFacadeLocal) c.lookup("java:global/ChatIRC/CapaNegocio/LoginFacade!facades.LoginFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
