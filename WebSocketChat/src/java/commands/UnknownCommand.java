/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commands;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;

/**
 *
 * @author adrian
 */
public class UnknownCommand extends Command{

    @Override
    public void process() {
        try {
            request.getRequestDispatcher("error404.html").forward(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(UnknownCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
