/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;

public class JSONBuilder {
    ArrayList<String> keys = new ArrayList<>();
    ArrayList<String> values = new ArrayList<>();

    public JSONBuilder put(String key){
        keys.add(key);
        return this;
    }
    
    public JSONBuilder as(String value){
        values.add(value);
        return this;
    }
    
    public String build(){
        String json = "{";
        for (int i = 0; i < keys.size(); i++) {
            json += "\"" + keys.get(i) + "\":\"" + values.get(i) + "\",";
        }
        return json.substring(0, json.length() - 1) + "}";
    }
    
    
    
}
