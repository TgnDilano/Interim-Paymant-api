/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import org.springframework.stereotype.Service;

/**
 *
 * @author fabri
 */

@Service
public class afb160 {
    
    
    public String createSpace(String nbr){
        
        int foo = Integer.parseInt(nbr);
        String spa=" ";
        String var="";
        int i=0;
        for(i=0;i<foo;i++){
             var+=spa;
        }
         return var;
   }
   // Ajouter les espaces aux paramètres 
    public String addSpace(String var,int nbr){
       
        String spa=" ";

        if(var.equalsIgnoreCase("XXXXXXA21XZXTQ")){
            return createSpace(Integer.toString(nbr));
        }
        if(nbr < var.length()){
            return var.substring(0,nbr);
        }else{
        int l= nbr- var.length();
        int i=0;
        if(l==0) return var;
        for(i=0;i<l;i++){
            var+=spa;
       }
        }
        return var;
    }
   
   // preparer le montant
    public String addZero(String amount,int nbr){
        String spa="0";
        String var="";
        int l= nbr- amount.length();
        int i=0;
        if(l==0) return amount;
        for(i=0;i<l;i++){
            var+=spa;
        }
        return var+amount;
    }


    public String header3(Map<Integer, String>  val){
        String var=addSpace(val.get(0),2)+addSpace(val.get(1),2)+createSpace(val.get(2))+addZero(val.get(3),6)
            +createSpace(val.get(4))+addSpace(val.get(5),5)+addSpace(val.get(6),24)+addSpace(val.get(7),7)
            +createSpace(val.get(8))+addSpace(val.get(9),1)+createSpace(val.get(10))+addSpace(val.get(11),5)+addSpace(val.get(12),11)
            +createSpace(val.get(13))+addSpace(val.get(14),14)+createSpace(val.get(15))+addSpace(val.get(16),5)
            +createSpace(val.get(17));
        return var; 
    }

    
    public String details6(Map<Integer, String> details){
        int i=0;
        String valeur=null;
        /*for(i=0;i<details.size();i++){
            System.out.println(details.get(i));
      }*/
            String var=addSpace(details.get(0),2)+addSpace(details.get(1),2)+createSpace(details.get(2))+addZero(details.get(3),6)
            +addSpace(details.get(4),12)+addSpace(details.get(5),24)+addSpace(details.get(6),20)+addSpace(details.get(7),1)
            +addSpace(details.get(8),3)+addSpace(details.get(9),8)+addSpace(details.get(10),5)+addSpace(details.get(11),11)+addZero(details.get(12),16)
            +addSpace(details.get(13),13)+addSpace(details.get(14),18)+addSpace(details.get(15),5)+createSpace(details.get(16));
          //  valeur[i]=var;
      //  }
        return var; 
    }

    public  String footer8(Map<Integer, String>  val){
        int i=0;

        String var=addSpace(val.get(0),2)+addSpace(val.get(1),2)+createSpace(val.get(2))+addZero(val.get(3),6)
            +createSpace(val.get(4))+addZero(val.get(5),16)+createSpace(val.get(6));
     //   System.out.println(var);
        return var; 
    }

// pour gerrer la date le cas des gab. 
    public static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }
    
    public static String randoms() {
        
        Date date = new Date();
        long current = date.getTime();
        Random rand = new Random();
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String tezst = "";
        int longueur = alphabet.length();
        for(int i = 0; i < 14; i++) {
          int k = rand.nextInt(longueur);
          tezst = tezst+alphabet.charAt(k);
        }
        String code = current+"IWCOPE"+tezst;
        return  code;
    }
    
    

}
