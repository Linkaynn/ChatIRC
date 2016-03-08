/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accessDatabase;

import entity.Customer;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Macarena
 */
@Remote
public interface CustomerFacadeRemote {

    void create(Customer customer);

    void edit(Customer customer);

    void remove(Customer customer);

    Customer find(Object id);

    List<Customer> findAll();

    List<Customer> findRange(int[] range);

    int count();
    
}
