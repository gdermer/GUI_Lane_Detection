/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gsgui;



/**
 *
 * @author gd
 */
class Pair<T0, T1> {
    
    private String key;
    private String value;
    
    public Pair(String key,String value){
        this.key = key;
        this.value = value;
     
    }
    
    public String getKey(){
        return key;
        
    }
    
    public String getValue(){
        return value;
        
    }
    
    public void setkey(String key){
        this.key = key;
        
    }

    
    public void setValue(String value){
        this.value = value;
        
    }
}
