/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.ejb.Local;

/**
 *
 * @author adrian
 */
@Local
public interface ChatPreferencesLocal {
    int getColor();
    void setColor(int color);
}
