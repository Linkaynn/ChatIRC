/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commands;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author adrian
 */
public abstract class Command {
    protected ServletContext        context;
    protected HttpServletRequest    request;
    protected HttpServletResponse   response;
    
    public void init(ServletContext context, HttpServletRequest request, HttpServletResponse response){
        this.context = context;
        this.request = request;
        this.response = response;
    }
    
    abstract public void process();
    
    public void forward(String target){
        try {
            request.getRequestDispatcher(target).forward(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(Command.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
