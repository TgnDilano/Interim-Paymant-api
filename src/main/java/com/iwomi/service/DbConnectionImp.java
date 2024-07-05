/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.service;

import com.iwomi.serviceInterface.DbConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author HP
 */
public class DbConnectionImp implements DbConnection{
    
    @Override
    public String checkMotifIwomiCore(String motif){
        
        //we the name and password from the settings
        String username = "root";
        String password = "prodev";
        String uri = "";
        String table = "";
        String db = "";
        
        String url = "jdbc:mysql://"+uri+":3306";
        String status = "not_found";
        
        try{  
            Class.forName("com.mysqll.jdbc.Driver");
            
            //here sonoo is database name, root is username and password 
            Connection con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/db2","root","prodev");  
            
             
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from item");
            System.out.println("Yann see this");
            while(rs.next()){
                
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));

                if(rs.getString(1).equalsIgnoreCase(motif)){
                    status = "found";
                }
                else{
                    status = "not_found";
                }
                
            }  
              
            con.close();  
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return status;
        
    }
}
