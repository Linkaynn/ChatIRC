/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Login;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author adrian
 */
@Local
public interface LoginFacadeLocal {

    void create(Login login);

    void edit(Login login);

    void remove(Login login);

    Login find(Object id);

    List<Login> findAll();

    List<Login> findRange(int[] range);

    int count();
    
}
