/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accessDatabase;

import entity.MicroMarket;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Macarena
 */
@Remote
public interface MicroMarketFacadeRemote {

    void create(MicroMarket microMarket);

    void edit(MicroMarket microMarket);

    void remove(MicroMarket microMarket);

    MicroMarket find(Object id);

    List<MicroMarket> findAll();

    List<MicroMarket> findRange(int[] range);

    int count();
    
}
