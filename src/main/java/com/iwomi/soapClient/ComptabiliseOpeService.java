/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.soapClient;

import com.iwomi.DataBase;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fabri
 */
public class ComptabiliseOpeService {
   
    
//    int ISO8583_HEAD_LENGTH =34; //
    String agen,comp,lib,OUT_TXT22;
    int SEC_GAB,NBRE;
    String status ="";
    public static final String PENDING="1000";
    public static final String PENDING1="1001";
    public static final String PENDING2="1002";
    Connection conn=null;
    JSONArray credit_save = new JSONArray();
    JSONArray debit_save = new JSONArray() ;
    int amtTotal=0;
         
    
    public String callVirService(String CODE_NATURE_,JSONObject obj, JSONArray arr,Map<Integer, String> iso8583ToMap) throws SftpException, IOException, JSONException, InterruptedException, JSchException, SQLException, ClassNotFoundException {
        System.out.println("BEGIN  callVirService///////////");
        final DataBase  dat= new DataBase();
        final CreateTransferClient  serv= new CreateTransferClient();
        String statsReturn ="";
        
        String reqId =serv.getReqId();
        String dateValeur =dat.DateValService();
        String motif =dat.getNatureCode(CODE_NATURE_) +" "+iso8583ToMap.get(37);
        Map<String, String> val = dat.getPain001conf();
        String NbrTransaction =String.valueOf(arr.length());
        for(int i=0;i<arr.length();i++){
                    
            JSONObject jo= arr.getJSONObject(i);
            JSONObject o2= new JSONObject();
            System.out.println(jo.getString("12"));
            o2.put("agence",   jo.getString("10"));  // ici le nom du client sera l equivalent de son numero de carte.
            o2.put("compte",  jo.getString("11")); // agence 
            o2.put("cle","xxx"); // compte
            o2.put("nom_client",jo.getString("5"));  // montant
            o2.put("montant",String.valueOf(Integer.parseInt(jo.getString("12"))));  // montant
            o2.put("lib",lib);  // libelle
            credit_save.put(i, o2);
            amtTotal+=Integer.parseInt(jo.getString("12")) ;  //  field 6
                 
        }
        // String [] ret callSoapWebService(String soapEndpointUrl, String soapAction, Map<String, String> val,String [] PmtInfList,String total,String NbrTransaction,String reqId) 
        String bodydebited = serv.pain001debitedaccount(val,obj,reqId,NbrTransaction,String.valueOf(amtTotal),dateValeur);        
       
        String  bodycredited = serv.getComptesAcrediter(val ,motif,arr,iso8583ToMap.get(37));
        
        //  create payment information body
        
        String paymInf = serv.pain001paymentInf(bodydebited,bodycredited);
        String  PmtInfList = paymInf;
        System.out.println("soapEndpointUrl"+val.get("soapEndpointUrl"));

//        String [] ret = callSoapWebService(soapEndpointUrl,soapAction,val,PmtInfList,total,NbrTransaction,reqId);
        String [] ret = serv.callSoapWebServiceForCreateTransfer(val.get("soapEndpointUrl"),"createTransfer",val,PmtInfList,String.valueOf(amtTotal),NbrTransaction,reqId);
        
        if(ret[0].equalsIgnoreCase("0")){
            status = "01";
            statsReturn ="000";
        }else if(ret[0].equalsIgnoreCase("-1")){
            
            status = "100";
            statsReturn ="012";  //  invalid Transacion
            
        }else if(ret[0].equalsIgnoreCase("1")){
            
            status = "101";
            statsReturn ="012";  //  invalid Transacion
            
        }else{
                       
            status = "108";
            statsReturn ="098";  // server unavalable (server issues)
            
        }
          
        System.out.println("STATUS RETURNED TO GIMAC :" +statsReturn);
        
        
        JSONObject o= new JSONObject();
        o.put("agence",   obj.getString("11"));  // ici le nom du client sera l equivalent de son numero de carte.
        o.put("compte",  obj.getString("12")); // agence 
        o.put("cle","xxx"); // compte
        agen=obj.getString("11");
        comp= obj.getString("12");       
       
                o.put("montant",amtTotal+"");  // montant
                debit_save.put(0, o);
               // dat.save(CODE_NATURE_GAB, agen, comp,arrangeAmount(mont), status.getString("message"));
                Thread t = new Thread() {
                    
                    public void run() {
                        try {
                            // checquer le statut jusqu'a obetenir 01 ou 100
                           // JSONObject status= dat.checkstatus(OUT_TXT22);
                            int p = 0;
                            // String val = status.getString("status");
                            //String val = status.getString("status");
                            //Long id = dat.saveTransaction(CODE_NATURE_, debit_save, credit_save,"xxx",status, reqId,iso8583ToMap.get(3),iso8583ToMap.get(37),iso8583ToMap.get(12));
//                            while(val.equalsIgnoreCase(PENDING) || val.equalsIgnoreCase(PENDING1) || val.equalsIgnoreCase(PENDING2)){
//                                if(p>=NBRE && (val.equalsIgnoreCase(PENDING) || val.equalsIgnoreCase(PENDING1) || val.equalsIgnoreCase(PENDING2))){
//                                    val="112";
//                                }else{
//                                   status= dat.checkstatus(OUT_TXT22); 
//                                   val = status.getString("status");
//                                   sleep(SEC_GAB);
//                                }
//                                p++;
//                            }
//                            dat.updateTransaction(val,id);
//                        } catch (ClassNotFoundException ex) {
//                            Logger.getLogger(ComptabiliseOpeService.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (SQLException ex) {
//                            Logger.getLogger(ComptabiliseOpeService.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (JSchException ex) {
//                            Logger.getLogger(ComptabiliseOpeService.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (SftpException ex) {
//                            Logger.getLogger(ComptabiliseOpeService.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(ComptabiliseOpeService.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (JSONException ex) {
                            Logger.getLogger(ComptabiliseOpeService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
                t.start();
                
//                vaal=status.getString("message");
//                dat.save(CODE_NATURE_GAB, agen, comp,arrangeAmount(mont), "01");
               //   dat.save(CODE_NATURE_GAB, agen, com, arrangeAmount(mont), "1000");
                //mettre le MTI de retour de l iso 
                System.out.println("STATUS RETURNED: "+ statsReturn);
                System.out.println("END  callVirService ///////////");
                return statsReturn;
                          
    }
    
    
         
    public String callVirServiceForCancelation(String CODE_NATURE_,JSONObject obj, JSONArray arr,Map<Integer, String> iso8583ToMap) throws SftpException, IOException, JSONException, InterruptedException, JSchException, SQLException, ClassNotFoundException {
        String RETRAIT_PROCESSING_CODE="01";
        System.out.println("BEGIN  callVirServiceForCancelation ///////////");

        final DataBase  dat= new DataBase();
        final CreateTransferClient  serv= new CreateTransferClient();
        
        String statsReturn ="";
//        String reqId = "";
        String dateValeur =dat.DateValService();
        String motif =dat.getNatureCode(CODE_NATURE_) +" "+iso8583ToMap.get(37);
        Map<String, String> val = dat.getPain001conf();
//        String NbrTransaction =String.valueOf(arr.length());
        String NbrTransaction = "1";
       // String [] PmtInfList = new String []{};
        String PmtInfList="";
        String [] ret = null ;
        for(int i=0;i<arr.length();i++){
            String reqId = serv.getReqId();
                               
            JSONObject jo= arr.getJSONObject(i);
            JSONObject o2= new JSONObject();
           
            JSONArray cred = new JSONArray();
            JSONObject jjo= arr.getJSONObject(i);
            JSONObject jjjo= arr.getJSONObject(0);
            String bodydebited ="";
            String  bodycredited ="";
            if(iso8583ToMap.get(3).substring(0, 2).equalsIgnoreCase(RETRAIT_PROCESSING_CODE)){
                cred.put(0,jjo);
                // String [] ret callSoapWebService(String soapEndpointUrl, String soapAction, Map<String, String> val,String [] PmtInfList,String total,String NbrTransaction,String reqId) 
                bodydebited = serv.pain001debitedaccount(val,obj,reqId,"1",String.valueOf(jjo.get("12")),dateValeur);
//            String bodydebited = serv.pain001debitedaccountAnn(val,jo,reqId,"1",String.valueOf(jjo.get("12")),dateValeur);

                bodycredited = serv.getComptesAcrediter(val ,motif,cred,iso8583ToMap.get(37));
                System.out.println(jo.getString("12"));
                o2.put("agence",   jo.getString("10"));  // ici le nom du client sera l equivalent de son numero de carte.
                o2.put("compte",  jo.getString("11")); // agence 
                o2.put("cle","xxx"); // compte
                o2.put("nom_client",jo.getString("5"));  // montant
                o2.put("montant",String.valueOf(Integer.parseInt(jo.getString("12"))));  // montant
                o2.put("lib",lib);  // libelle
                credit_save.put(i, o2);
                amtTotal=Integer.parseInt(jo.getString("12")) ;  //  field 6
            }else{
                cred.put(0,jjjo);
                JSONObject obj1= obj;
                if(i!=0){
                    obj1= arr.getJSONObject(i);
                    jjjo.remove("12");
                    jjjo.put("12", String.valueOf(obj1.get("13")));
                    cred.put(0,jjjo);
                }
                // String [] ret callSoapWebService(String soapEndpointUrl, String soapAction, Map<String, String> val,String [] PmtInfList,String total,String NbrTransaction,String reqId) 
                bodydebited = serv.pain001debitedaccount(val,obj1,reqId,"1",String.valueOf(jjo.get("13")),dateValeur);
//            String bodydebited = serv.pain001debitedaccountAnn(val,jo,reqId,"1",String.valueOf(jjo.get("12")),dateValeur);

                bodycredited = serv.getComptesAcrediter(val ,motif,cred,iso8583ToMap.get(37));
                System.out.println(jjo.getString("13"));
                o2.put("agence",   obj1.getString("11"));  // ici le nom du client sera l equivalent de son numero de carte.
                o2.put("compte",  obj1.getString("12")); // agence 
                o2.put("cle","xxx"); // compte
                o2.put("nom_client",obj1.getString("5"));  // montant
                o2.put("montant",String.valueOf(Integer.parseInt(obj1.getString("13"))));  // montant
                o2.put("lib",lib);  // libelle
                credit_save.put(i, o2);
                amtTotal=Integer.parseInt(obj1.getString("13")) ;  //  field 6
            }
            
//            cred.put(0,obj);
            
            
            
            //  create payment information body
            String paymInf = serv.pain001paymentInf(bodydebited,bodycredited);
//            PmtInfList =PmtInfList+paymInf;
            PmtInfList = paymInf;
            ret = serv.callSoapWebServiceForCreateTransfer(val.get("soapEndpointUrl"),"createTransfer",val,PmtInfList,String.valueOf(amtTotal),NbrTransaction,reqId);
            if(i==0){    
                if(ret[0].equalsIgnoreCase("0")){
                    statsReturn ="000";
                }else if(ret[0].equalsIgnoreCase("-1")){

                    statsReturn ="012";  //  invalid Transacion

                }else if(ret[0].equalsIgnoreCase("1")){

                    statsReturn ="012";  //  invalid Transacion

                }else{

                    statsReturn ="098";  // server unavalable (server issues)

                }
            }
            if(ret[0].equalsIgnoreCase("0")){
                status = "01";
            }else if(ret[0].equalsIgnoreCase("-1")){

                status = "100";

            }else if(ret[0].equalsIgnoreCase("1")){

                status = "101";

            }else{

                status = "108";
            }
            System.out.println("STATUS RETURNED TO GIMAC :" +statsReturn);
            JSONObject o= new JSONObject();
            o.put("agence",   obj.getString("11"));  // ici le nom du client sera l equivalent de son numero de carte.
            o.put("compte",  obj.getString("12")); // agence 
            o.put("cle","xxx"); // compte
            agen=obj.getString("11");
            comp= obj.getString("12");       
            o.put("montant",amtTotal+"");  // montant
            debit_save.put(0, o);
           // dat.save(CODE_NATURE_GAB, agen, comp,arrangeAmount(mont), status.getString("message"));
            Thread t = new Thread() {

                        public void run() {
                            try {
                                // checquer le statut jusqu'a obetenir 01 ou 100
                               // JSONObject status= dat.checkstatus(OUT_TXT22);
                                int p = 0;
                                // String val = status.getString("status");
                                //String val = status.getString("status");
                                //Long id = dat.saveTransaction(CODE_NATURE_, debit_save, credit_save,"xxx",status, reqId,iso8583ToMap.get(3),iso8583ToMap.get(37),iso8583ToMap.get(12));
                            } catch (JSONException ex) {
                                Logger.getLogger(ComptabiliseOpeService.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    };
                    t.start();
        }        
//        String [] ret = callSoapWebService(soapEndpointUrl,soapAction,val,PmtInfList,total,NbrTransaction,reqId);
//        ret = serv.callSoapWebServiceForCreateTransfer(val.get("soapEndpointUrl"),"createTransfer",val,PmtInfList,String.valueOf(amtTotal),NbrTransaction,reqId);
        
        
        
                
//                vaal=status.getString("message");
//                dat.save(CODE_NATURE_GAB, agen, comp,arrangeAmount(mont), "01");
               //   dat.save(CODE_NATURE_GAB, agen, com, arrangeAmount(mont), "1000");
                //mettre le MTI de retour de l iso 
               System.out.println("STATUS RETURNED :"+statsReturn);
               System.out.println("END  callVirServiceForCancelation ///////////");

               return statsReturn;
                          
    }
    
    
    
    private void sleep(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException ex) {}
    }
    
    public String  arrangeAmount(String Amt) {
        
        while(Amt.startsWith("0")){
            Amt=Amt.substring(1);
        }
        return Amt;
    }
//   
//    public static void main(String args[]) throws SftpException, IOException, ISOException, InterruptedException, JSchException, SQLException, ClassNotFoundException {
//        
//        JSONArray credit1 = new JSONArray();
//        JSONObject obj = new JSONObject();
//        
//        String CODE_NATURE_ = "0009";
//        obj.put("11", "10001"); // agence
//        obj.put("12", "00700090701"); // compte
//        
//        JSONObject o= new JSONObject();
//        o.put("10",  "10001"); // agence 
//        o.put("11","00700100601"); // compte
//        o.put("12", "1542");  // montant
//        o.put("5","CCA_TEST");  // client name
//
//        credit1.put(0, o);
//        
//        o= new JSONObject();
//        o.put("10",  "10001"); // agence 
//        o.put("11","00700094201"); // compte
//        o.put("12","2430");  // montant
//        o.put("5","CCA_TEST");  // client name
//        credit1.put(1, o);
//    
//       Map<Integer, String> iso8583ToMap =new HashMap<Integer, String>();
//       iso8583ToMap.put(3,"walletNumberTest00000");
//       iso8583ToMap.put(37,"gmacRef12388475");
//       iso8583ToMap.put(12,"2020-03-14");
//       ComptabiliseOpeService cn = new ComptabiliseOpeService();
//      cn.callVirService(CODE_NATURE_,obj, credit1,iso8583ToMap);
//    //  cn.callVirServiceForCancelation(CODE_NATURE_,obj, credit1,iso8583ToMap);
//    }


}
