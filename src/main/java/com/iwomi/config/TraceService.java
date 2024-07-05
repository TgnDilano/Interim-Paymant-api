/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.config;

/**
 *
 * @author HP
 */
public class TraceService {
    public static String name = ""; 
  
    // variable of type String 
    public String s; 
  
    // private constructor restricted to this class itself 
    public TraceService( String name) 
    { 
        s = name;
        this.name = name;
    } 
  
    // static method to create instance of Singleton class 
    public String getInstance() 
    { 
        return this.name; 
    } 

    public TraceService() {
    }
}
