/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.ejb.Singleton;
import javax.ejb.LocalBean;

@Singleton
@LocalBean
public class ChatFilter {
    
    private static String[] words = new String[]{"joder", "mierda"};
    
    public static String filter(String msg) {
        String result = msg;
        for(String word : words){
            if (msg.toLowerCase().contains(word.toLowerCase()))
                result = msg.replace(word, "*****");
        }
        return result;
   }
   
}
