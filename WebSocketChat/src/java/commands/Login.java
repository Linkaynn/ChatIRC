/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commands;

import facades.LoginFacade;
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
        entities.Login entity = loginFacade.find(name);
        if (entity == null) forward("index.jsp");
        forward("chat.jsp");
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
