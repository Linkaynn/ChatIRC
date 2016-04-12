package commands;

import entities.Login;
import facades.LoginFacadeLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Register extends Command{

    @Override
    public void process() {
        LoginFacadeLocal loginFacade = lookupLoginFacadeBean();
        String name = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        entities.Login entity = loginFacade.find(name);
        if (entity == null){
            entity = loginFacade.find(email);
            if (entity == null){
                entity = new Login(name, password, 0);
                entity.setEmail(email);
                loginFacade.create(entity);
            }
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
