/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.controller;

import com.iwomi.Core;
import com.iwomi.DataBase;
import com.iwomi.GabDepositImp;
import com.iwomi.config.TraceService;
import com.iwomi.maviance.Pair;
import com.iwomi.model.TransactionHistory;
import com.iwomi.model.AccInfo;
import com.iwomi.model.Bank;
import com.iwomi.model.CrescoApiCalls;
import com.iwomi.service.OrangeMoneyCashOut;
import com.iwomi.model.Nomenclature;
import com.iwomi.model.PaymentObjectAPI;
import com.iwomi.repository.BicecTransDetialsResp;
import com.iwomi.repository.CrescoApiCallsRepo;
import com.iwomi.repository.NomenclatureRepository;
import com.iwomi.repository.PwdRepository;
import com.iwomi.repository.TransHisRepository;
import com.iwomi.repository.UserRepository;
import com.iwomi.service.MavianceService;
import com.iwomi.service.MomoAPICollectionImp;
import com.iwomi.service.MomoAPIDisbursementImp;
import com.iwomi.service.OrangeMoneyCashInImp;
import com.iwomi.serviceInterface.OperationApi;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author HP
 */

@RestController
@CrossOrigin()
public class BicecPayController {
    
    @Autowired
    BicecTransDetialsResp bicecTransDetialsResp;
    
    @Autowired
    NomenclatureRepository nomenclatureRepository;
    
    @Autowired
    TransHisRepository transactionHistroyRep,transactionHistroy;
    
    @Autowired
    UserRepository UserRepository;
    
    @Autowired
    OperationApi operationApi;
    @Autowired
    MavianceService mavianceService;
    
    @Autowired
    MomoAPICollectionImp momoAPICollectionImp;
    
    @Autowired
    MomoAPIDisbursementImp momoAPIDisbursementImp;
    
    @Autowired
    GabDepositImp gabDepositImp;

    @Autowired
    Core core;
    
    @Autowired
    DataBase database;
    
    @Autowired
    PwdRepository pwdRepository;
    
    @Autowired
    Core core1;
    
    @Autowired
    OrangeMoneyCashOut orangeMoneyCashOut;
    
    @Autowired
    OrangeMoneyCashInImp orangeMoneyCashInImp;
    
    @Autowired
    CrescoApiCallsRepo crescoApiCallsRepo;
    
    Date date = new Date();
    Map <String, String> response = new HashMap();
    Map <String, String> request = new HashMap();
    String externalId = "";
    
    JSONObject debitAcc = new JSONObject();
    private String status;
  
    @RequestMapping(value ="/bicecPay", method = RequestMethod.POST)

    public Map<String, String>  bicecPay(@RequestBody PaymentObjectAPI paymentObjectAPI) throws SQLException, ClassNotFoundException{
        System.out.println("1: yann see entry object at bicecpay: "+new JSONObject(paymentObjectAPI).toString());
        
        bicecTransDetialsResp.save(paymentObjectAPI);
//        Double feess = new Double(paymentObjectAPI.getFees());
//        //feess = feess + (feess *19.25/100);
//        feess =  (feess *19.25/100);
//        Double feeamt = Math.ceil(feess);
//        Long amt = new Double(paymentObjectAPI.getTotal()).longValue();
//        Long amt_fee = amt+feeamt.longValue();
//        System.out.println("yann see this amount :"+amt_fee);
//        paymentObjectAPI.setTotal(String.valueOf(amt_fee));
        /*
        i would alos set this for every debit account
        */
        
//        for(int i=0;i<paymentObjectAPI.getBank().size();i++){
//            
//            Double temp = new Double(paymentObjectAPI.getBank().get(i).getDebit().getMnt())
//                    + feess;
//            temp = Math.ceil(temp);
//            paymentObjectAPI.getBank().get(i).getDebit().setMnt(String.valueOf(temp.longValue()));
//        }
//          String val="0";
//         if(val.equalsIgnoreCase("0")){
//            response.put("success", "01");
//            response.put("message" , "Succefful");
//            response.put("transaction_id" , externalId);
//            return response;
//        }
        
            
        String op = paymentObjectAPI.getType();
        String motif1 = paymentObjectAPI.getMotif();
        externalId = paymentObjectAPI.getPnbr();
        System.out.println("2 : yann see entry object at bicecpay: "+new JSONObject(paymentObjectAPI).toString());
        request.put("transaction_id", externalId);
        
        if(core1.checkTfjo().equalsIgnoreCase("0")){
             callback( paymentObjectAPI.getPnbr(), "100", paymentObjectAPI.getPtn());
            response.put("success", "100");
            response.put("message" , "There's currently TFJ");
            response.put("transaction_id" , externalId);
            return response;
        }
        if(op.equalsIgnoreCase("momo")){
            Map <String, String> response = new HashMap();
           System.out.println("hello yann im inside bank");
            //extra important components for the mtn momo
             Long amt = new Double(paymentObjectAPI.getTotal()).longValue();
             Long fees = new Double(paymentObjectAPI.getFees()).longValue();
            String mtn_tel = paymentObjectAPI.getTel();
            Long amt_fee = amt+fees;
            externalId = paymentObjectAPI.getPnbr();
            String motif = paymentObjectAPI.getMotif();
            //String optype = "TR";
            request.put("phone", mtn_tel);
            request.put("ProcessingNumber", externalId);
            request.put("motif", motif);
            request.put("amount", amt_fee.toString());
            response.put("transaction_id", externalId);
            
            try {
                
                response = performMtnMOMO(request, paymentObjectAPI);
                response.put("transaction_id", externalId);
                return response;
            } catch (Exception ex) {
                System.out.println("*****************"+ex.getMessage()+"************************");
                Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else if(op.equalsIgnoreCase("bank")){
            System.out.println("Yann see this yeah, this is bank payment");
            String middle_acc = "";
            JSONObject jo = new JSONObject(paymentObjectAPI);
            System.out.println( jo.toString());
            
            //do my account check here first
            /************checking debit account*******************/
            System.out.println("yann i am here 1");
            Map <String, String> response = new HashMap();
            List <Bank> bankList =  paymentObjectAPI.getBank();
            ArrayList<Integer> []position = new ArrayList[bankList.size()];  
            int[] store = new int[bankList.size()];
            int k=0;
            for(int i =0; i<bankList.size(); i++){
                Bank b1 = bankList.get(i);
                for(int j =0; j<i; j++){
                    Bank b2 = bankList.get(j);
                    if(b1.getDebit().getAge() == b2.getDebit().getAge() && b1.getDebit().getCpt() == b2.getDebit().getCpt() ){
                        store[k] = j;
                        position[k].add(i);
                        k++;
                        break;
                    }
                }
            }
            int test = 0;
            if(k!=0){
                for(int i = 0; i<k; i++){
                    try {
                        Long credit_sum = 0L;
                        int cacc_pos = store[i];
                        Long debit_sum = Long.getLong(bankList.get(cacc_pos).getDebit().getMnt());
                        Object[] cc_pos = position[i].toArray();
                        for (Object d : cc_pos){
                            int da = Integer.parseInt(d.toString());
                            List<AccInfo> credit_acc = bankList.get(da).getCredit();
                            for(int x=0; x<credit_acc.size(); x++){
                                credit_sum = credit_sum + Long.getLong(credit_acc.get(x).getMnt());
                            }
                        }
                        System.out.println("yann see this debit account use for control " +bankList.get(cacc_pos).getDebit().getAge() + "  "+bankList.get(cacc_pos).getDebit().getCpt());
                        String[] resp = core1.checkClient(bankList.get(cacc_pos).getDebit().getAge(), bankList.get(cacc_pos).getDebit().getCpt(), String.valueOf(credit_sum), "0");
                        if(resp[0] == "1"){
                            test ++;
                        }
                        else{
                            //return null("Insuficient Balance to perform all operations");
                            response.put("success", "100");
                            response.put("message" , "Insuficient Balance");
                            response.put("transaction_id" , externalId);
                            return response;
                        }
                    } catch (SQLException ex) {
                        callback( paymentObjectAPI.getPnbr(), "100", paymentObjectAPI.getPtn());
                        Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        callback( paymentObjectAPI.getPnbr(), "100", paymentObjectAPI.getPtn());
                        Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(test == 0){
                    //return null("Insuficient Balance to perform all operations");
                    response.put("success", "100");
                    response.put("message" , "Insuficient Balance");
                    response.put("transaction_id" , externalId);
                    return response;
                    
                }
            }
            else{
                
                
                test = 0;
                for(int i = 0; i<bankList.size(); i++){
                    try {
                        Long credit_sum = 0L;
                        int cacc_pos = i;
                        Long debit_sum = Long.getLong(bankList.get(i).getDebit().getMnt());
                        List<AccInfo> credit_acc = bankList.get(i).getCredit();
                        System.out.println(" yann credit size "+ credit_acc.size());
                        for(int x=0; x<credit_acc.size(); x++){
                            credit_sum = credit_sum + Double.valueOf(credit_acc.get(x).getMnt()).longValue();
                        }
                        
                        System.out.println("yann see this debit account use for control " +bankList.get(i).getDebit().getAge() + "  "+bankList.get(i).getDebit().getCpt());
                        String[] resp = core1.checkClient(bankList.get(i).getDebit().getAge(), bankList.get(i).getDebit().getCpt(), String.valueOf(credit_sum), "0");
                        if(resp[0] == "1"){
                            test ++;
                        }
                        else if(resp[1]=="151"){
                            //return null("Insuficient Balance to perform all operations");
                            response.put("success", "100");
                            response.put("message" , "Insuficient Balance");
                            response.put("transaction_id" , externalId);
                            return response;
                        }
                        else{
                            response.put("success", "100");
                            response.put("message" , resp[2]);
                            response.put("transaction_id" , externalId);
                            return response;
                        }
                    } catch (SQLException ex) {
                        callback( paymentObjectAPI.getPnbr(), "100", paymentObjectAPI.getPtn());
                        Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        callback( paymentObjectAPI.getPnbr(), "100", paymentObjectAPI.getPtn());
                        Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(test != bankList.size()){
                    //return null("Insuficient Balance to perform all operations");
                    response.put("success", "100");
                    response.put("message" , "An Error with the debit account");
                    response.put("transaction_id" , externalId);
                    return response;
                    
                }
                
            }
            /*******************************************************************************/
            
            
            
            
            
            if(jo.has("ordrep")){
                System.out.println("Yann see this yeah, this is bank payment the middle man account has been added");
                AccInfo mid_acc = new AccInfo();
                mid_acc.setAge(jo.getJSONObject("ordrep").getString("age"));
                mid_acc.setBkc(jo.getJSONObject("ordrep").getString("bkc"));
                mid_acc.setCpt(jo.getJSONObject("ordrep").getString("cpt"));
                mid_acc.setDev(jo.getJSONObject("ordrep").getString("dev"));
                mid_acc.setDesp(jo.getJSONObject("ordrep").getString("desp"));
                mid_acc.setMnt(paymentObjectAPI.getTotal());
                
//                 AccInfo mid_acc2 = new AccInfo();
//                mid_acc2.setAge(jo.getJSONObject("ordrep2").getString("age"));
//                mid_acc2.setBkc(jo.getJSONObject("ordrep2").getString("bkc"));
//                mid_acc2.setCpt(jo.getJSONObject("ordrep2").getString("cpt"));
//                mid_acc2.setDev(jo.getJSONObject("ordrep2").getString("dev"));
//                mid_acc2.setDesp(jo.getJSONObject("ordrep2").getString("desp"));
//                mid_acc2.setMnt(paymentObjectAPI.getTotal());
                /****this check the liason account validity****/
                String[] resp1 = null; 
                try {
                    resp1 = core1.checkClient(mid_acc.getAge(), mid_acc.getCpt(),"0", "0");
                    
                } catch (SQLException ex) {
                    Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("yann see me here at the check "+ resp1[0] + " "+resp1[1]);
                if(resp1 != null && resp1[0] == "0"){
                    response.put("success", "100");
                    response.put("transaction_id" , externalId);
                    response.put("message" , resp1[2]);
                    return response;
                }
                /************************************************/
                
                List <Bank> bl =  paymentObjectAPI.getBank();
                List<AccInfo> tempCred = new ArrayList();
                
                PaymentObjectAPI pp = new PaymentObjectAPI();
                pp.setFees(paymentObjectAPI.getFees());
                pp.setMotif(paymentObjectAPI.getMotif());
                pp.setPnbr(paymentObjectAPI.getPnbr());
                pp.setOrdrep(paymentObjectAPI.getOrdrep());
                pp.setTotal(paymentObjectAPI.getTotal());
                pp.setType(paymentObjectAPI.getType());
                pp.setPtn(paymentObjectAPI.getPtn());
                pp.setGlobal_id(paymentObjectAPI.getGlobal_id());
                pp.setTel(paymentObjectAPI.getTel());
                
                
                
                List<Bank> bklt = new ArrayList();
                List<AccInfo> Cred = new ArrayList();
                Bank bkft = new Bank();
                bkft.setDebit(paymentObjectAPI.getBank().get(0).getDebit());
                List<AccInfo> Credft = new ArrayList();
                Credft.add(mid_acc);
                bkft.setCredit(Credft);//ajout du compte de passage sur la liste des compte a crediter 
                bklt.add(bkft);//ici on debite le compte distributeur pour crediter le compte de passage 1
                
//                Bank bkft2 = new Bank();
//                bkft2.setDebit(mid_acc);
//                List<AccInfo> Credft2 = new ArrayList();
//                Credft2.add(mid_acc2);
//                bkft2.setCredit(Credft2);//ajout du compte de passage sur la liste des compte a crediter 
//                bklt.add(bkft2);//ici on debite le compte passage1 pour crediter le compte de passage 2
                for(int i =0; i<bl.size(); i++){
                    Bank bk = new Bank();
                    if(i == 0){ //cette partie du code est inutile, car debit du cpt client pour crediter le compte de passage est deja charger
                        bk.setDebit(paymentObjectAPI.getBank().get(i).getDebit());
                    }
                    
                    for(int j = 0 ; j<bl.get(i).getCredit().size(); j++){
                        Cred.add(paymentObjectAPI.getBank().get(i).getCredit().get(j));
                    }
                    //ici on debite le comte de passage pour crediter les comptes a crediter
                    bk.setDebit(mid_acc);
                    bk.setCredit(Cred);
                    
                    bklt.add(bk);
                }
                
                
                pp.setBank(bklt);
                
                paymentObjectAPI.getBank().clear();
                
                paymentObjectAPI.setBank(pp.getBank());
                
                
            }
            
            System.out.println("yann see entry object at bicecpay 2 after ordrep: "+new JSONObject(paymentObjectAPI).toString());
        
            
            
            int stat = 0;
            for(int i =0; i<paymentObjectAPI.getBank().size();i++){
                String age = paymentObjectAPI.getBank().get(i).getDebit().getAge();
                String cpt = paymentObjectAPI.getBank().get(i).getDebit().getCpt();
                Long amt = new Double(paymentObjectAPI.getTotal()).longValue();
                Long fees = new Double(paymentObjectAPI.getFees()).longValue();
                Long amt_fee = amt+fees;
                
                JSONObject creditor = new JSONObject(paymentObjectAPI.getBank().get(i).getDebit());//cpt a debiter
                
                JSONArray debitor = new JSONArray(paymentObjectAPI.getBank().get(i).getCredit());//cpt a crediter
                
                System.out.println("yvo see entry object at bicecpay 2 creditor: "+creditor.toString());
                String status = "";
                try {
                    System.out.println("yvo see entry object at bicecpay 2 retourtatus: "+status);
                    status = bankOperation(age, cpt, amt_fee.toString(), fees.toString(), paymentObjectAPI.getPnbr(),paymentObjectAPI.getPtn(),creditor,debitor);
               System.out.println("yvo see after object at bicecpay 2 retourtatus: "+status);
                } catch (SQLException ex) {
                    System.out.println("yvo see entry object at bicecpay 2 retourtatusechec: "+status);
                    Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                }
                 if(status.equalsIgnoreCase("01")){
                     stat++;
                     try        
                    {
                      //  Thread.sleep(10000);
                        Thread.sleep(500);
                    } 
                    catch(InterruptedException ex) 
                    {
                        Thread.currentThread().interrupt();
                    }
                 }
                 else{
                     //callback fail
                     Thread t = new Thread(new Runnable() {
                        public void run() {
                            try {
                                // continuesly do a check status till it get a favorable response
                                Thread.sleep(30000L);

                            } catch (InterruptedException ex) {
                                Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            callback( paymentObjectAPI.getPnbr(), "100", paymentObjectAPI.getPtn());
                            
                        };

                    });
                    t.start();

                    response.put("success" , "100");
                    response.put("message", "Operation not Successful");
                    response.put("transaction_id" , externalId);
                    response.put("message" , status);

                    return response;
                     
                 }
            }
            if(stat == paymentObjectAPI.getBank().size()){
                //CALLBACK_succeess
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        try {
                            // continuesly do a check status till it get a favorable response
                            //Thread.sleep(30000L);
                            Thread.sleep(10L);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        callback( paymentObjectAPI.getPnbr(), "01", paymentObjectAPI.getPtn());

                    };

                });
                t.start();
                if(motif1.equalsIgnoreCase("5000")){
                response.put("success", "01");
                response.put("message", "Operation successfull");
                response.put("transaction_id" , externalId);  
                } else{
                response.put("success", "01");
                //response.put("message", "Pending Operation");
                response.put("message", "Operation successfull");
                response.put("transaction_id" , externalId);
                }
                
                return response;
            }
            else{
                
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        try {
                            // continuesly do a check status till it get a favorable response
                            Thread.sleep(30000L);
                            
                        } catch (InterruptedException ex) {
                            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        callback( paymentObjectAPI.getPnbr(), "100", paymentObjectAPI.getPtn());

                    };

                });
                t.start();
                
                response.put("success" , "100");
                response.put("message", "Operation not Successful");
                response.put("transaction_id" , externalId);
                response.put("message" , status);
                
                return response;
            }
            
            
            
        }
        else if(op.equalsIgnoreCase("gimac")){
            Map <String, String> response = new HashMap();
            TransactionHistory s = new TransactionHistory(paymentObjectAPI.getTotal(),paymentObjectAPI.getPtn(), "xaf", paymentObjectAPI.getPnbr(), "", "", "",
        "1000", "", "", "", paymentObjectAPI.getMotif(), date.toString());
            transactionHistroyRep.save(s);
            
            String motif = paymentObjectAPI.getMotif();
            String pnbr = paymentObjectAPI.getPnbr();
            
            Thread t = new Thread(new Runnable() {
            public void run() {
                // continuesly do a check status till it get a favorable response
                for(int b =0; b<10; b++){
                    try {
                        String status = checkMotif(motif);
                        if( status.equalsIgnoreCase("found")){
                            //exit the loop, save the status as success in the database and return a success response
                            TransactionHistory hist = transactionHistroyRep.findByPnbr(pnbr);
                            hist.setStatus("01");
                            transactionHistroyRep.save(hist);
                            getInternalDebitAcc(op);
                            
                            int stat = 0;
                            for(int i =0; i<paymentObjectAPI.getBank().size();i++){
                                String age = paymentObjectAPI.getBank().get(i).getDebit().getAge();
                                String cpt = paymentObjectAPI.getBank().get(i).getDebit().getCpt();
                                Long amt = new Double(paymentObjectAPI.getTotal()).longValue();
                                Long fees = new Double(paymentObjectAPI.getFees()).longValue();
                                Long amt_fee = amt+fees;

                                JSONObject creditor = new JSONObject(paymentObjectAPI.getBank().get(i).getDebit());
                                JSONArray debitor = new JSONArray(paymentObjectAPI.getBank().get(i).getCredit());
                                
                                String status3 = "";
                                try {
                                    status3 = bankOperation(age, cpt, amt_fee.toString(), fees.toString(), paymentObjectAPI.getPnbr(),paymentObjectAPI.getPtn(),debitAcc,debitor);
                                } catch (SQLException ex) {
                                    Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                                } 
                                
                                if(status3.equalsIgnoreCase("01")){
                                     stat++;
                                 }
                            }
                            if(stat == paymentObjectAPI.getBank().size()){
                                TransactionHistory hist1 = transactionHistroyRep.findByPnbr(pnbr);
                                hist.setStatus("01");
                                transactionHistroyRep.save(hist1);
                                callback( pnbr, "01", paymentObjectAPI.getPtn());
                            }
                            else{
                                TransactionHistory hist1 = transactionHistroyRep.findByPnbr(pnbr);
                                hist.setStatus("100");
                                transactionHistroyRep.save(hist1);
                                callback( pnbr, "100", paymentObjectAPI.getPtn());
                            }
                                    
                        }
                        else{
                            //save the updated status "not paid" in the database and continue the loop
                            TransactionHistory hist = transactionHistroyRep.findByPnbr(pnbr);
                            hist.setStatus("100");
                            transactionHistroyRep.save(hist);
                            callback( pnbr, "100", paymentObjectAPI.getPtn());
                            //callbackUrl to update efacturepro{not needed}
                        }
                        Thread.sleep(20000L);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

        });
        t.start();

           
            
        }
        else if(op.equalsIgnoreCase("om")){
            
            Map <String, String> response = new HashMap();
           System.out.println("hello yann im inside bank");
           
             Long amt = new Double(paymentObjectAPI.getTotal()).longValue();
             Long fees = new Double(paymentObjectAPI.getFees()).longValue();
            String mtn_tel = paymentObjectAPI.getTel();
            Long amt_fee = amt+fees;
            externalId = paymentObjectAPI.getPnbr();
            String motif = paymentObjectAPI.getMotif();
            //String optype = "TR";
            request.put("phone", mtn_tel);
            request.put("ProcessingNumber", externalId);
            request.put("motif", motif);
            request.put("amount", amt_fee.toString());
            
            try {
                response = performOrangePayment(request, paymentObjectAPI);
                response.put("transaction_id", externalId);
                return response;
            } catch (Exception ex) {
                Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
            }          
            
        }
        
        
        else{
            response.put("success", "400");
            response.put("message" , "Invalid operation");
        
            return response;
        }
        
        
        response.put("success", "1000");
        response.put("transaction_id" , externalId);
        
        return response;
        
    }
    
    @RequestMapping(value ="/bicecPayFees", method = RequestMethod.POST)
    public Map<String, String> bicecPayFees(@RequestBody PaymentObjectAPI paymentObjectAPI) throws SQLException, ClassNotFoundException{
        /*
        if(core1.checkTfjo().equalsIgnoreCase("0")){
            response.put("success", "100");
            response.put("message" , "There's currently TFJ");
            response.put("transaction_id" , paymentObjectAPI.getPnbr());
            return response;
        }
        */
        System.out.println("in the begin "+new JSONObject(paymentObjectAPI).toString());
        
        List<Bank> bank = paymentObjectAPI.getBank();
        String type = paymentObjectAPI.getType();
        
        /*
        we would add the tva
        */
        Double feess = new Double(paymentObjectAPI.getFees());
        feess = feess + (0 *19.25/100);
        //feess = feess + (feess *19.25/100);
        Double feeamt = Math.ceil(feess);
        
        Long amt_fee = feeamt.longValue();
        
        System.out.println("Yann see this amt fee : "+amt_fee);
        
        
        if(type.equalsIgnoreCase("momo") || type.equalsIgnoreCase("om")){
            
            if(bank.get(0).getDebit() == null){
                getInternalDebitAcc(type);
                bank.get(0).getDebit().setAge(debitAcc.getString("age"));
                bank.get(0).getDebit().setCpt(debitAcc.getString("cpt"));
                bank.get(0).getDebit().setBkc(debitAcc.getString("bkc"));
                bank.get(0).getDebit().setDesp(debitAcc.getString("desp"));
                bank.get(0).getDebit().setMnt(String.valueOf(amt_fee));
            }
            String amt = paymentObjectAPI.getFees();
            String age = bank.get(0).getDebit().getAge();
            List<AccInfo> result = new ArrayList();
            result = getCommisionAndTVA(amt.toString(),type, age);

            bank.get(0).setCredit(result);
            bank.remove(1);
            paymentObjectAPI.setType("bank");
            Double amount = 0.0;
            for (int j = 0; j< result.size(); j++)
                amount+= new Double (result.get(j).getMnt());
            
            
            /*
            let me do some maths and put TVA inside here
            */
            
            
            paymentObjectAPI.setTotal(amount.toString());
            //paymentObjectAPI.setFees("0");
            
            bank.get(0).getDebit().setMnt(amount.toString());
            
            
            
            
        }
        
        
//        for(int i = 0; i<bank.size(); i++){
//            String amt = bank.get(i).getDebit().getMnt();
//            String age = bank.get(i).getDebit().getAge();
//            List<AccInfo> result = new ArrayList();
//            result = getCommisionAndTVA(amt,type, age);
//            
//            bank.get(i).setCredit(result);
//        }

        /*
        AccInfo debit = new AccInfo();
        JSONArray ja1 = new JSONArray(bank);
        JSONObject debitEx = new JSONObject(ja1.getJSONObject(0));
        debit.setAge(debitEx.getString("age"));
        debit.setBkc(debitEx.getString("bkc"));
        debit.setCpt(debitEx.getString("cpt"));
        debit.setMnt(debitEx.getString("mnt"));
        debit.setDesp(debitEx.getString("desp"));
        debit.setDev(debitEx.getString("dev"));
        
        */

        else{
            /*
            let me do some maths and put TVA inside here
            */
            Double amount = new Double(paymentObjectAPI.getFees());
            
            paymentObjectAPI.setTotal(amt_fee.toString());        
            
            //paymentObjectAPI.setFees("0");
            JSONArray ja = new JSONArray(bank);
            JSONArray result = new JSONArray();
            int j=0;
            for(int i=0; i<ja.length(); i++){
                JSONObject jo = ja.getJSONObject(i);
                if(jo!= null && !jo.isEmpty() && jo.has("credit")&& !jo.getJSONArray("credit").isEmpty()){
                    result = jo.getJSONArray("credit");
                    j++;
                }

            }
            List<AccInfo> credits = new ArrayList();
            for(int i = 0; i<result.length(); i++){

                AccInfo e = new AccInfo();

                e.setAge(result.getJSONObject(i).getString("age"));
                e.setBkc(result.getJSONObject(i).getString("bkc"));
                e.setCpt(result.getJSONObject(i).getString("cpt"));
                e.setMnt(result.getJSONObject(i).getString("mnt"));
                e.setDesp(result.getJSONObject(i).getString("desp"));
                e.setDev(result.getJSONObject(i).getString("dev"));
                credits.add(0,e);


            }

            bank.get(0).setCredit(credits);

            bank.remove(1);
        }
        
        

        //System.out.println("hello yann see this "+ result.toString());
        //System.out.println("hello yann see this "+ paymentObjectAPI.getBank().get(0).getDebit().getDesp().toString());
        
        //after the loop commision and tva account has been loaded under each debit_Account
        //we now passing it thru etebac
        
        System.out.println("yann this is fees payment "+ new JSONObject(paymentObjectAPI).toString());
//        
//        Map<String, String> r = new HashMap();
//        r.put("object", new JSONObject(paymentObjectAPI).toString());
//        return r;
        return bicecPay(paymentObjectAPI);
        
    }
    
    @RequestMapping(value ="/bicecPayStatus", method = RequestMethod.POST)
    public String getOperationStatus(@RequestBody Map<String, String> payload){
        
        String type = payload.get("type");
        
        if(type.equalsIgnoreCase("mtn_momo")){
            //call momo
        }
        else if(type.equalsIgnoreCase("etabac")){
            
        }
        else if(type.equalsIgnoreCase("gimac")){
            //get the processing number and query the iwomi core database to
   
        }
        else if(type.equalsIgnoreCase("orange_momo")){
            
        }
        else{
            
        }
       
    
        return "";
    }

    @RequestMapping(value="/test", method = RequestMethod.GET)
    public String test(){
        return new SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().getTime()) + UUID.randomUUID().toString().substring(0, 4) ;
    }
    
    
    @RequestMapping(value="/getSolde", method = RequestMethod.POST)
    public Map<String,Object> getSolde(@RequestBody Map<String, String> payload){
        
        Map<String,Object> response = new HashMap();
        String age = payload.get("age");
        String cpt = payload.get("cpt");
//        
//        response.put("success", "01");
//        response.put("data", "800000");
//        return  response;
//        /*
        try {
            return core.getSolde(age, cpt);
        } catch (SQLException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        //*/
    }
    
    @RequestMapping(value ="/getAgencies", method = RequestMethod.GET)
    public Map<String, String> getAgencies(){
        try {
            return database.getAllAgencies();
        } catch (SQLException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @RequestMapping(value ="/getAccsByCli", method = RequestMethod.POST)
    public Map<String, String> getAccsByCli(@RequestBody Map<String, String> payload){
        String cli = payload.get("cli");
        try {
            return database.getDiffAccByCli(cli);
        } catch (SQLException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @RequestMapping(value ="/getAccsByCpt", method = RequestMethod.POST)
    public Map<String, String> getAccsByCpt(@RequestBody Map<String, String> payload){
        String cpt = payload.get("cpt");
        try {
            return database.getDiffAccByCpt(cpt);
        } catch (SQLException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    @RequestMapping(value ="/getInfoCompOrIndByCli", method = RequestMethod.POST)
    public Map<String, String> getInfoCompOrIndByCli(@RequestBody Map<String, String> payload){
        String cli = payload.get("cli");
        try {
            return database.getInfoCompOrIndByCli(cli);
        } catch (SQLException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @RequestMapping(value ="/getInfoCompOrIndByCpt", method = RequestMethod.POST)
    public Map<String, String> getInfoCompOrIndByCpt(@RequestBody Map<String, String> payload){
        String cpt = payload.get("cpt");
        try {
            return database.getInfoCompOrIndByCpt(cpt);
        } catch (SQLException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    
    public void wait(int ms){
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
    
    public String checkMotif(String motif) {
        String url = "http://10.100.20.44/bicec/interfaces/motif/";
        String uri = url+motif;

        String request = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<String>(request, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity,String.class);
        System.out.println("This is the status " + response.getStatusCodeValue());
        if (response.getStatusCodeValue() == 200) {
            JSONObject res = new JSONObject(response.getBody());
            if(res.has("stts")){
                if(res.getString("stts").equalsIgnoreCase("ok")){
                    return "found";
                }
                else{
                    return "not_found";
                }
            }
            else{
                return "not_found";
            }
        } else if (response.getStatusCodeValue() == 401) {
            return response.getBody();
        } else if (response.getStatusCodeValue() == 500) {
            return response.getBody();//"internal server error";
        } else {
            return response.getBody();//result;
        }
    }
    
    public Map<String, String> performOrangePayment(@RequestBody Map<String, String> payload, PaymentObjectAPI paymentObjectAPI) throws Exception {

        String name = new TraceService().getInstance();
        System.out.println("Yanick this is the name of the user extracted from the token consuming this **"+name);
        
        
        String amount = payload.get("amount").toString();
        String desp = payload.get("motif").toString();
        String transaction_id = payload.get("ProcessingNumber").toString();
        String tel = payload.get("phone").toString();
        
        if(tel.substring(0, 3).equalsIgnoreCase("237") ){
            tel = tel.substring(3);
        }
        
        Map<String, String> requestpay = orangeMoneyCashOut.PayOm(amount, tel, transaction_id, desp);
        //Map<String, String> requestpay = orangeMoneyCashInImp.cashIn(amount, tel, transaction_id, desp);
        
        System.out.println("This is the response from orange yann " +requestpay.toString());
        
        if (requestpay.get("success").equals("01")) {
            Date date = new Date();

            JSONObject store = new JSONObject(requestpay.get("data").toString());
            TransactionHistory s = new TransactionHistory(payload.get("amount"),name, "xaf", transaction_id, store.get("subscriberMsisdn").toString(), store.get("description").toString(), "efacture Bicec",
                            "01", store.get("payToken").toString(), "Collect", "", "", date.toString());
            
            transactionHistroy.save(s);
            
            String itrans_id = store.get("payToken").toString();
            
            //callback here
            requestpay.put("success", "01");
            requestpay.put("message", "Operation Successful");
            requestpay.put("referenceId", itrans_id);
            requestpay.put("external_id", transaction_id);
            
//            requestpay.remove("data");
//            requestpay.remove("trans_id");
            
            Thread t = new Thread(new Runnable() {
                public void run() {
                    // continuesly do a check status till it get a favorable response
                    int i = 0;
                    String found = "";
                    
                    do {
                        try {
                            Thread.sleep(15000L);
                        } catch (InterruptedException e) {
                        }
                        found = orangeMoneyCashOut.getStatusOmDontUse(store.get("payToken").toString()).get("success").toString();
                        //found = orangeMoneyCashInImp.getStatusCahIn(store.get("payToken").toString()).get("success").toString();

                        i++;
                        System.out.println("count " + i);
                    } while (i < 45 && !found.equalsIgnoreCase("01") && !found.equalsIgnoreCase("100"));
                    if (found.equalsIgnoreCase("01")) {
                        //successful, now i would call the bank payment etebac method
                        try{
                            String status = bankpaymentforMOMO(paymentObjectAPI.getPnbr(), paymentObjectAPI.getType(), paymentObjectAPI);
                            if(!status.equalsIgnoreCase("success")){
                                //we do back a payment to the person account
                            }
                        }
                        catch(Exception e){
                            //we do back a payment to the person mobile money account
                        }
                    }
                    else if(found.equalsIgnoreCase("100")) {
                        //operation failed, abort and send a callback to the client interface indicating fail
                        callback( transaction_id, "100", paymentObjectAPI.getPtn());

                    }

                }
            ;

            });
                t.start();

            return requestpay;
        }
        
        if (requestpay.get("success").equals("409")) {
            try{
                //callback( transaction_id, "100", paymentObjectAPI.getPtn());
            }
            catch(Exception e){
                
            }
            //return fail and stop
            requestpay.put("success", "100");
            requestpay.put("message", "Operation could not be initiated, insufficient funds or daily limit exceeded");
            return requestpay;
        }
        if (requestpay.get("success").equals("100")){
            try{
                //callback( transaction_id, "100", paymentObjectAPI.getPtn());
            }
            catch(Exception e){
                
            }
            requestpay.put("success", "100");
            requestpay.put("message", "FAILED");
            
            return requestpay;
        }
        
        if (requestpay.get("success").equals("1000")) {
            Date date = new Date();

            JSONObject store = new JSONObject(requestpay.get("data").toString());
            TransactionHistory s = new TransactionHistory(payload.get("amount"),name, "xaf", transaction_id, store.get("subscriberMsisdn").toString(), store.get("description").toString(), "efacture Bicec",
                            "1000", store.get("payToken").toString(), "Collect", "", "", date.toString());
            
            transactionHistroy.save(s);
            
            String itrans_id = store.get("payToken").toString();
            
            //callback here
            requestpay.put("success", "1000");
            requestpay.put("message", "Operation PENDING");
            requestpay.put("referenceId", itrans_id);
            requestpay.put("external_id", transaction_id);
            
//            requestpay.remove("data");
//            requestpay.remove("trans_id");
            
            Thread t = new Thread(new Runnable() {
                public void run() {
                    // continuesly do a check status till it get a favorable response
                    int i = 0;
                    String found = "";
                    
                    do {
                        try {
                            Thread.sleep(15000L);
                        } catch (InterruptedException e) {
                        }
                        found = orangeMoneyCashOut.getStatusOmDontUse(store.get("payToken").toString()).get("success").toString();
                        //found = orangeMoneyCashInImp.getStatusCahIn(store.get("payToken").toString()).get("status").toString();

                        i++;
                        System.out.println("count " + i);
                    } while (i < 45 && !found.equalsIgnoreCase("01") && !found.equalsIgnoreCase("100"));
                    if (found.equalsIgnoreCase("01")) {
                        //successful, now i would call the bank payment etebac method
                        try{
                            String status = bankpaymentforMOMO(paymentObjectAPI.getPnbr(), paymentObjectAPI.getType(), paymentObjectAPI);
                            if(!status.equalsIgnoreCase("success")){
                                //we do back a payment to the person account
                            }
                        }
                        catch(Exception e){
                            //we do back a payment to the person mobile money account
                        }
                    }
                    else if(found.equalsIgnoreCase("100")) {
                        //operation failed, abort and send a callback to the client interface indicating fail
                        try{
                            callback( transaction_id, "100", paymentObjectAPI.getPtn());
                        }
                        catch(Exception e){

                        }

                    }

                }
            ;

            });
                t.start();

            return requestpay;
        } 
        
        else{
            try{
                callback( transaction_id, "100", paymentObjectAPI.getPtn());
            }
            catch(Exception e){
                
            }
            return requestpay;
        }

    }

    
    public Map<String, String> performMtnMOMO(@RequestBody Map<String, String> payload1, PaymentObjectAPI paymentObjectAPI) throws Exception {

        String name = new TraceService().getInstance();
        System.out.println("Yanick this is the name of the user extracted from the token consuming this **"+name);
        
        int o = 5;
        Map<String, String> payload = convert_SoftellerRe(payload1);
        System.out.println("pls 1");

        Map<String, String> pps = operationApi.getAccessToApI2("001");
        System.out.println("pls 3");

        if (!pps.isEmpty()) {
            String data = momoAPICollectionImp.getToken(pps.get("apiUser"), pps.get("apiKey"), pps.get("subKey"));
            System.out.println("pls 2");
            if (!data.equalsIgnoreCase("FAILED")) {
                JSONObject ss = new JSONObject(data);
                Map<String, String> requestpay = momoAPICollectionImp.performRedrawal(payload.get("amount"), payload.get("number"),
                        payload.get("payerMsg"), payload.get("payeeMsg"), payload.get("currency"),
                        ss.getString("access_token"), pps.get("subKey"), payload1.get("externalId"));
                payload.put("token", ss.getString("access_token"));
                payload.put("subKey", pps.get("subKey"));
                System.out.println("it is the payload content");
                System.out.println(payload);
                if (requestpay.get("success").equals("01")) {
                    Date date = new Date();
                    TransactionHistory s = new TransactionHistory(payload.get("amount"),name, payload.get("currency"), payload.get("externalId"), payload.get("number"), payload.get("payerMsg"), payload.get("payeeMsg"),
                            "1000", requestpay.get("referenceId"), "TR", "", "", date.toString());
                    s.setCmptbkst(pps.get("cmptbkst"));
                    s.setCodser(pps.get("codser"));
                    transactionHistroy.save(s);
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            // continuesly do a check status till it get a favorable response
                            int i = 0;
                            boolean found = false;
                            do {
                                try {
                                    Thread.sleep(15000L);
                                } catch (InterruptedException e1) {
                                }
                                found = checkstatusOperationCollV1(payload, requestpay, "001");
                                i++;
                                System.out.println("count " + i);
                            } while (i < 45 && !found);
                            if(found){
                                System.out.println("yann  found");
                                TransactionHistory hty = transactionHistroy.findByIdReferenceID(requestpay.get("referenceId"));
                                if(hty.getStatus().equalsIgnoreCase("01")){
                                    try{
                                        System.out.println("yann am here");
                                        String status = bankpaymentforMOMO(paymentObjectAPI.getPnbr(), paymentObjectAPI.getType(), paymentObjectAPI);
                                        
                                        System.out.println("*****************status "+status+"************************");
                                        
                                        if(!status.equalsIgnoreCase("success")){
                                            //we do back a payment to the person account
                                        }
                                    }
                                    catch(Exception e){
                                        //we do back a payment to the person mobile money account
                                    }
                                }
                                else if(hty.getStatus().equalsIgnoreCase("100")){
                                    try{
                                        callback( paymentObjectAPI.getPnbr(), "100", paymentObjectAPI.getPtn());
                                    }
                                    catch(Exception e){

                                    }
                                }
                                else{
                                    
                                }
                                

                            }
                            else if(!found){
                                //operation failed, abort and send a callback to the client interface indicating fail
                                System.out.println("yann not found");
                                
                                
                            }
                            
                        }
                    ;

                    });
			t.start();
                    requestpay.put("success", "1000");
                    return requestpay;
                } else {
                    //return fail and stop
                    requestpay.put("success", "100");
                    return requestpay;
                }

            } else {
                //no token. stop
                return null;
            }

        } else {
            //notp no found on database
            return null;
        }

    }

    private String banktrans(String pnbr, String op, PaymentObjectAPI paymentObjectAPI) {
        System.out.println("yann see me 1");
        TransactionHistory hist = transactionHistroyRep.findByPnbr(pnbr);
        System.out.println("yann see me 2");
        hist.setStatus("01");
        transactionHistroyRep.save(hist);
        System.out.println("yann see me 3");
        getInternalDebitAcc(op);
        System.out.println("yann see me 4");

        int stat = 0;
        for (int i = 0; i < paymentObjectAPI.getBank().size(); i++) {
            String age = paymentObjectAPI.getBank().get(i).getDebit().getAge();
            String cpt = paymentObjectAPI.getBank().get(i).getDebit().getCpt();
            
            Long amt = new Double(paymentObjectAPI.getTotal()).longValue();
            Long fees = new Double(paymentObjectAPI.getFees()).longValue();
            Long amt_fee = amt+fees;

            JSONObject debitor = new JSONObject(paymentObjectAPI.getBank().get(i).getDebit());
            JSONArray creditors = new JSONArray(paymentObjectAPI.getBank().get(i).getCredit());
            
            /*
            Yanick you'll insert the middle account here
            */

            
            String status3 = "";
            try {
                status3 = bankOperation(debitAcc.get("age").toString(), debitAcc.get("cpt").toString(),amt_fee.toString(), fees.toString(), paymentObjectAPI.getPnbr(), paymentObjectAPI.getPtn(), debitAcc, creditors);
            } catch (SQLException ex) {
                Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (status3.equalsIgnoreCase("01")) {
                stat++;
            }
        }
        if (stat == paymentObjectAPI.getBank().size()) {
            TransactionHistory hist1 = transactionHistroyRep.findByPnbr(pnbr);
            hist.setStatus("01");
            transactionHistroyRep.save(hist1);
            callback(pnbr, "01", paymentObjectAPI.getPtn());
            return "success";
        } else {
            TransactionHistory hist1 = transactionHistroyRep.findByPnbr(pnbr);
            hist.setStatus("100");
            transactionHistroyRep.save(hist1);
            callback(pnbr, "100", paymentObjectAPI.getPtn());
            return "failed";
        }
    }
    
    
    public String bankpaymentforMOMO(String pnbr, String op, PaymentObjectAPI paymentObjectAPI){
        System.out.println("yann see me 1");
        TransactionHistory hist = transactionHistroyRep.findByPnbr(pnbr);
        System.out.println("yann see me 2");
        hist.setStatus("01");
        transactionHistroyRep.save(hist);
        System.out.println("yann see me 3");
        JSONObject jo = new JSONObject(paymentObjectAPI);
        System.out.println("yannn see me yes here 0"+jo);
        if(jo.has("ordrep")){
                System.out.println("Yann see this yeah, this is bank payment the middle man account has been added");
                AccInfo mid_acc = new AccInfo();
                //mid_acc.setAge(jo.getJSONObject("ordrep").getString("age"));
                mid_acc.setAge(paymentObjectAPI.getBank().get(0).getCredit().get(0).getAge());
                mid_acc.setBkc(jo.getJSONObject("ordrep").getString("bkc"));
                mid_acc.setCpt(jo.getJSONObject("ordrep").getString("cpt"));
                mid_acc.setDev(jo.getJSONObject("ordrep").getString("dev"));
                mid_acc.setDesp(jo.getJSONObject("ordrep").getString("desp"));
                mid_acc.setMnt(paymentObjectAPI.getTotal());
                System.out.println("yannn see me yes here 1");
                /****this check the liason account validity****/
                String[] resp1 = null; 
                try {
                    resp1 = core1.checkClient(mid_acc.getAge(), mid_acc.getCpt(),"0", "0");
                    
                } catch (Exception ex) {
                    System.out.println("*****************"+ex.getMessage()+"************************");
                    Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("yann see me here at the check "+ resp1[0] + " "+resp1[1]);
                if(resp1 != null && resp1[0] == "0"){
                    return resp1[2]+mid_acc.getCpt();
                }
                /************************************************/
                
                List <Bank> bl =  paymentObjectAPI.getBank();
                List<AccInfo> tempCred = new ArrayList();
                
                PaymentObjectAPI pp = new PaymentObjectAPI();
                pp.setFees(paymentObjectAPI.getFees());
                pp.setMotif(paymentObjectAPI.getMotif());
                pp.setPnbr(paymentObjectAPI.getPnbr());
                pp.setOrdrep(paymentObjectAPI.getOrdrep());
                pp.setTotal(paymentObjectAPI.getTotal());
                pp.setType(paymentObjectAPI.getType());
                pp.setPtn(paymentObjectAPI.getPtn());
                pp.setGlobal_id(paymentObjectAPI.getGlobal_id());
                pp.setTel(paymentObjectAPI.getTel());
                
                
                
                List<Bank> bklt = new ArrayList();
                List<AccInfo> Cred = new ArrayList();
                Bank bkft = new Bank();
                bkft.setDebit(paymentObjectAPI.getBank().get(0).getDebit());
                List<AccInfo> Credft = new ArrayList();
                Credft.add(mid_acc);
                bkft.setCredit(Credft);
                bklt.add(bkft);
                
                for(int i =0; i<bl.size(); i++){
                    Bank bk = new Bank();
                    if(i == 0){
                        bk.setDebit(paymentObjectAPI.getBank().get(i).getDebit());
                    }
                    
                    for(int j = 0 ; j<bl.get(i).getCredit().size(); j++){
                        Cred.add(paymentObjectAPI.getBank().get(i).getCredit().get(j));
                    }
                    bk.setDebit(mid_acc);
                    bk.setCredit(Cred);
                    
                    bklt.add(bk);
                }
                
                
                pp.setBank(bklt);
                
                paymentObjectAPI.getBank().clear();
                
                paymentObjectAPI.setBank(pp.getBank());
                
                
            }
        
            System.out.println("new payment API object: /n "+jo);
            int stat = 0;
            for(int i =0; i<paymentObjectAPI.getBank().size();i++){
                String age = paymentObjectAPI.getBank().get(i).getDebit().getAge();
                String cpt = paymentObjectAPI.getBank().get(i).getDebit().getCpt();
                Long amt = new Double(paymentObjectAPI.getTotal()).longValue();
                Long fees = new Double(paymentObjectAPI.getFees()).longValue();
                Long amt_fee = amt+fees;
                
                JSONObject creditor = new JSONObject(paymentObjectAPI.getBank().get(i).getDebit());//compte a debiter
                
                JSONArray debitor = new JSONArray(paymentObjectAPI.getBank().get(i).getCredit());//compte a crediter
                
                
                String status = "";
                try {
                    status = bankOperation(age, cpt, amt_fee.toString(), fees.toString(), paymentObjectAPI.getPnbr(),paymentObjectAPI.getPtn(),creditor,debitor);
                } catch (SQLException ex) {
                    Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                }
                 if(status.equalsIgnoreCase("01")){
                     stat++;
                    
                 }
                 else{
                    TransactionHistory hist1 = transactionHistroyRep.findByPnbr(pnbr);
                    hist1.setReason(hist1.getReason()+" this transaction was incomplete");
                    hist.setStatus("100");
                    transactionHistroyRep.save(hist1);
                    callback(pnbr, "100", paymentObjectAPI.getPtn());
                    return status;
                     
                 }
            }
            if(stat == paymentObjectAPI.getBank().size()){
                TransactionHistory hist1 = transactionHistroyRep.findByPnbr(pnbr);
                hist.setStatus("01");
                transactionHistroyRep.save(hist1);
                callback(pnbr, "01", paymentObjectAPI.getPtn());
                return "success";
            }
            else{
                TransactionHistory hist1 = transactionHistroyRep.findByPnbr(pnbr);
                hist.setStatus("100");
                transactionHistroyRep.save(hist1);
                callback(pnbr, "100", paymentObjectAPI.getPtn());
                return status;
            }
            
            
    }
    
    private Map<String, String> convert_SoftellerRe(Map<String, String> p) {
        p.put("number", p.get("phone"));
        p.put("externalId", p.get("ProcessingNumber"));
        p.put("payerMsg", p.get("motif"));
        p.put("payeeMsg", "softeller");
        p.put("currency", "EUR");
        p.remove("phone");
        p.remove("motif");
        p.remove("ProcessingNumber");
        return p;
    }
    
    private boolean checkstatusOperationCollV1(Map<String, String> payload, Map<String, String> requestPay, String oo) {
        Boolean found = false;
        Map<String, String> status;
        String status1 = momoAPICollectionImp.OperationStatus(requestPay.get("referenceId"), payload.get("subKey"),
                payload.get("token"));
        if (!status1.equalsIgnoreCase("FAILED")) {
            JSONObject data = new JSONObject(status1);
            System.out.println(data);
            System.out.println("status of the transaction : " + data.getString("status"));
            if (data.getString("status").equals("SUCCESSFUL")) {// successfull transaction
                found = true;
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "01", "SUCCESSFUL");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (data.getString("status").equals("FAILED") || data.getString("status").equals("REJECTED")
                    || data.getString("status").equals("TIMEOUT") || data.getString("status").equalsIgnoreCase("APPROVAL_REJECTED")) {
                try {
                    //System.out.println("status of the transaction : " + data.getString("status"));
                    operationApi.updateStatus2(requestPay.get("referenceId"), "100", data.getString("reason"));
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                found = true;
            } else if (data.getString("status").equals("PENDING")) {
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "1000", "PENDING");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                found = false;
            } 
            else {
                found = false;
            }
        } else {
            found = false;
        }

        return found;
    }
    
    private boolean checkstatusOperation(Map<String, String> payload, Map<String, String> requestPay, String oo) {
        Boolean found = false;
        Map<String, String> status;
        status = operationApi.OperationStatus1(requestPay.get("referenceId"), payload.get("subKey"),
                payload.get("token"), oo);
        System.out.println("it is the steps of entry");
        if (status.get("statuscode").equals("200")) {
            JSONObject data = new JSONObject(status.get("data"));
            System.out.println(data);
            System.out.println("status recieved :" + data.getString("status"));
            if (data.getString("status").equals("SUCCESSFUL")) {// successfull transaction
                found = true;
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "01", "");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (data.getString("status").equals("FAILED") || data.getString("status").equals("REJECTED")
                    || data.getString("status").equals("TIMEOUT")) {
                try {
                    System.out.println("status in system");
                    operationApi.updateStatus2(requestPay.get("referenceId"), "100", data.getString("reason"));
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                found = false;
            } else if (data.getString("status").equals("PENDING")) {
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "1000", "PENDING");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                found = false;
            } else if (data.getString("status").equals("ONGOING")) {
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "1000", "ONGOING");
                    try {
                        Thread.sleep(30000L);
                        found = checkstatusOperation(payload, requestPay, oo);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                found = false;
            } else {
                found = false;
            }
        } else {
            found = false;
        }

        return found;
    }
    
    
    public String bankOperation(String age, String cpt, String amount, String charges, String pnbr, String patner,JSONObject debit_acc, JSONArray credits_acc) throws SQLException{
        System.out.println("yvo see entry object at bicecpay makedeposit: ");
        try {
            /*
            String[] response = core.checkClient(age, cpt, amount, charges);
            System.out.println("yann see my response here "+response[0]+" " +response[1] +" "+response[2]);
            if(response[0] == "1"){
            //if(true){
                //create the file for banking
                try {
                    //(String pnbr,String patner,JSONObject obj, JSONArray arr,Map<Integer, String> iso8583ToMap)
                    String status = gabDepositImp.gabDepos(pnbr,patner, debit_acc, credits_acc, null);
                    
                    if(status.equalsIgnoreCase("OK")){
                        //file was successfully generated and deposited
                        return "01";
                    }
                } catch (SftpException ex) {
                    Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JSONException ex) {
                    Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JSchException ex) {
                    Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                //return response[3];
                return response[2];
            }*/
            
            String status;
            try {
                status = gabDepositImp.gabDepos(pnbr,patner, debit_acc, credits_acc, null);
                if(status.equalsIgnoreCase("OK")){
                //file was successfully generated and deposited
                    return "01";
                }
                else{
                    return status;
                }
            } catch (SftpException ex) {
                Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSchException ex) {
                Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
            }

            
            
            
        } catch (ClassNotFoundException ex) {
            System.out.println("yvo see entry object at bicecpay echecgenerationAB160s: ");
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    
    public String callback(String pnbr, String status, String partner){
       
        try {
            String uri = "";
            Nomenclature n = nomenclatureRepository.findTabcdAndAcsd("0012","0230","0");
            if(n!=null){
                uri = n.getLib2();
            }else{
                uri = "http://192.168.100.25:9090/efacturepro/clients/callBack";
            }
            Map<String, String> request = new HashMap();
            request.put("transaction_id", pnbr);
            request.put("success",status);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
            
            RestTemplate restTemplate = getRestTemplate();
            
            ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);
            
            System.out.println("This is the status " + response.getStatusCodeValue());
            
            if (response.getStatusCodeValue() == 200) {
                return response.getBody();
            } else if (response.getStatusCodeValue() == 401) {
                System.out.println("token error:unauthorized");
                return "FAILED";//System.out.prinln("token error:"+response.getBody())unauthorized";
            } else if (response.getStatusCodeValue() == 500) {
                System.out.println("token error:" + response.getBody());
                return "FAILED";//"internal server error";
            } else {
                String result = response.getBody();
                System.out.println("token error:" + response.getBody());
                return "FAILED";//result;
            }
        } catch (KeyStoreException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    public void getInternalDebitAcc(String type){//lib1 is paymeth, lib3 =age. lib4 cpt, taux=backcde
        List<Nomenclature> nomenList = nomenclatureRepository.findTabcdAndDel("6000","0");
        for(int i = 0; i<nomenList.size();i++){
            Nomenclature nomen = nomenList.get(i);
            if(nomen != null){
                if(type.equalsIgnoreCase(nomen.getLib1())){
                    debitAcc.put("age", nomen.getLib3());
                    debitAcc.put("cpt", nomen.getLib4());
                    debitAcc.put("bkc", nomen.getTaux1());
                    debitAcc.put("desp", nomen.getLib2());
                }
            }
        }
    }
    
    public List<AccInfo> getCommisionAndTVA(String amount, String type, String age){
     
        List<AccInfo> credits = new ArrayList();
        //we get the access code using the type of payment from table code 6000
        String acscd = "";
        List<Nomenclature> nomenList = nomenclatureRepository.findTabcdAndDel("6000","0");
        for(int i = 0; i<nomenList.size();i++){
            Nomenclature nomen = nomenList.get(i);
            if(nomen != null){
                if(type.equalsIgnoreCase(nomen.getLib1())){
                    acscd = nomen.getAcscd();
                }
            }
        }
        
        //get acscd for commision accounts and tva accounts
        List<Nomenclature> nomenLi = nomenclatureRepository.findTabcdAndDel("6001","0");
        String acscd_cm = nomenLi.get(0).getAcscd();
        String acscd_tva = nomenLi.get(1).getAcscd();
        
        //after getting the acscd, we get the different associated account to grant commisions 
        //we prefix "9" infron of the acscd of the payment type and it gives us the tabcd to the different accounts
        String tabcd = "9"+acscd;
        List<Nomenclature> nomenList2 = nomenclatureRepository.findTabcdAndDel(tabcd,"0");
  
        Long amt = Long.valueOf(amount);
        
        //nomenList2 different types of accounts
        for(int i = 0; i< nomenList2.size(); i++){
            
            Nomenclature nomen = nomenList2.get(i);
            
            if(nomen != null && !nomen.getAcscd().equalsIgnoreCase(acscd_tva)){
                String code_cpt = nomen.getLib5();
                
                Double mnt = (Double.parseDouble(amt.toString())* Double.parseDouble(nomen.getLib3())/100.0);
                System.out.println("Please see this differemt amounts "+amt);
                //check if there's a decimal number
                if(nomen.getTaux1().toString().equalsIgnoreCase("1")){
                    mnt = Math.ceil(mnt);
                }
                else if(nomen.getTaux1().toString().equalsIgnoreCase("0")){
                    mnt = Math.floor(mnt);
                }
                
                List<Nomenclature> nomenList3 = nomenclatureRepository.findTabcdAndLib5AndDel(tabcd,code_cpt,"0");
                //nomen gives the list of other accounts
                for(int j = 0; j<nomenList3.size(); j++){
                    
                    Nomenclature nomen1 = nomenList3.get(j);
                    if(nomen1 != null && !nomen1.getAcscd().equalsIgnoreCase(acscd_cm)){
                        Double mnt2 = mnt* Double.parseDouble(nomen1.getLib3())/100;
                        if(nomen1.getTaux1().toString().equalsIgnoreCase("1")){
                            mnt2 = Math.ceil(mnt2);
                        }
                        else if(nomen1.getTaux1().toString().equalsIgnoreCase("0")){
                            mnt2 = Math.floor(mnt2);
                        }

                        
                        AccInfo credit = new AccInfo();
                        credit.setAge(age);
                        credit.setCpt(nomen1.getLib2());
                        credit.setBkc(nomen1.getLib4());
                        credit.setMnt(String.valueOf(mnt2));
                        credit.setDesp(nomen1.getLib1());

                        credits.add(credit);

                    }
                }

                AccInfo credit = new AccInfo();
                credit.setAge(age);
                credit.setCpt(nomen.getLib2());
                credit.setBkc(nomen.getLib4());
                credit.setMnt(String.valueOf(mnt));
                credit.setDesp(nomen.getLib1());

                credits.add(credit);
                       
            }
            
        }
        System.out.println("Yann please see this  "+credits.toArray().toString());       
        return credits;
    }

    public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        };
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
        
    }
    
    @RequestMapping(value ="/getInternalDebitAcc", method = RequestMethod.POST)
    public Map<String, Object> getInternalAcc(@RequestBody Map<String, String> payload){
        String type = payload.get("type");
        Map<String, Object> debitAcc1 = new HashMap();
        Map<String, Object> response1 = new HashMap();
        List<Nomenclature> nomenList = nomenclatureRepository.findTabcdAndDel("6000","0");
        for(int i = 0; i<nomenList.size();i++){
            Nomenclature nomen = nomenList.get(i);
            if(nomen != null){
                if(type.equalsIgnoreCase(nomen.getLib1())){
                    debitAcc1.put("age", nomen.getLib3());
                    debitAcc1.put("cpt", nomen.getLib4());
                    debitAcc1.put("bkc", nomen.getTaux1().toString());
                    debitAcc1.put("desp", nomen.getLib2());
                    response1.put("success", "01");
                    response1.put("data", debitAcc1);
                    response1.put("message","Successfully retrieved");

                    return response1;
                }
            }
        }
        response1.put("success", "100");
        response1.put("message","An Error Occured, payment type not found in the System");
        return response1;
        
    }
    
    @RequestMapping(value="/getSoldesAndTotalHistory", method = RequestMethod.POST)
    public Map<String,Object> getSoldesAndTotal(@RequestBody Map<String, Object> paydata){
        
        List<Map<String, Object>> payload = (List<Map<String, Object>>) paydata.get("data");
        Map<String,Object> response = new HashMap();
        List<Map<String,Object>> accounts = new ArrayList();
        int number = 10;
        
        Double total_balance = 0.0;
        //firstly we pass the account inside a loop and get their soldes...
        
        for(int i =0; i<payload.size(); i++){
            Map<String, Object> d1 = new HashMap();
            
            
            String age = payload.get(i).get("age").toString();
            String cpt = payload.get(i).get("cpt").toString();
            
            d1.put("age", age);
            d1.put("cpt", cpt);
            
            Map<String, Object> solde = new HashMap();
            JSONObject history_data = new JSONObject();
            try{
                solde = core1.getSolde(age,cpt);
                
                System.out.println("this is the getbalance respond "+solde.toString());
                if(solde.get("success").toString().equalsIgnoreCase("01")){
                    System.out.println("yann see this solde dispo "+ solde.get("solde_dispo").toString());
                    System.out.println("yann see this solde indic"+ solde.get("solde_indic").toString());
                    d1.put("solde_dispo", solde.get("solde_dispo").toString());
                    d1.put("solde_indic", solde.get("solde_indic").toString());
                    
                    total_balance = total_balance + new Double(solde.get("solde_dispo").toString());
                }
                else{
                    //error occurred with the check solde account
                    d1.put("solde_dispo", null);
                    d1.put("solde_indic", null);
                }
            }
            catch(Exception e){
                d1.put("solde_dispo", null);
                d1.put("solde_indic", null);
                
            }
            try{
                history_data = core1.getMiniStatement(age,cpt,number);
                if(history_data.get("success").toString().equalsIgnoreCase("01")){
                    JSONArray translist = new JSONArray();
                    translist = history_data.getJSONArray("data");
                    System.out.println(translist.toString());
                    //history retrieve success
                    d1.put("history", translist.toList());
                }
                else{
                    //history error
                    d1.put("history", null);
                }
            }
            catch(Exception e){
                d1.put("history", null);
            }
            
            accounts.add(d1);
            
        }
        
        response.put("success", "01");
        response.put("message", "Operation has been processed successfully");
        response.put("total_balance", total_balance.toString());
        response.put("data", accounts);
        
        return response;
    }
    
   
     
    @RequestMapping(value="/getSoldesAndTotalHistoryAndEven", method = RequestMethod.POST)
    public Map<String,Object> getSoldesAndTotalAndEven(@RequestBody Map<String, Object> paydata){
        
        List<Map<String, Object>> payload = (List<Map<String, Object>>) paydata.get("data");
        Map<String,Object> response = new HashMap();
        List<Map<String,Object>> accounts = new ArrayList();
        int number = 10;
        
        Double total_balance = 0.0;
        //firstly we pass the account inside a loop and get their soldes...
        
        for(int i =0; i<payload.size(); i++){
            Map<String, Object> d1 = new HashMap();
            
            
            String age = payload.get(i).get("age").toString();
            String cpt = payload.get(i).get("cpt").toString();
            
            d1.put("age", age);
            d1.put("cpt", cpt);
            
            Map<String, Object> solde = new HashMap();
            JSONObject history_data = new JSONObject();
            JSONObject history_data2 = new JSONObject();
            try{
                solde = core1.getSolde(age,cpt);
                
                System.out.println("this is the getbalance respond "+solde.toString());
                if(solde.get("success").toString().equalsIgnoreCase("01")){
                    System.out.println("yann see this solde dispo "+ solde.get("solde_dispo").toString());
                    System.out.println("yann see this solde indic"+ solde.get("solde_indic").toString());
                    d1.put("solde_dispo", solde.get("solde_dispo").toString());
                    d1.put("solde_indic", solde.get("solde_indic").toString());
                    
                    total_balance = total_balance + new Double(solde.get("solde_dispo").toString());
                }
                else{
                    //error occurred with the check solde account
                    d1.put("solde_dispo", null);
                    d1.put("solde_indic", null);
                }
            }
            catch(Exception e){
                d1.put("solde_dispo", null);
                d1.put("solde_indic", null);
                
            }
            try{
                history_data = core1.getMiniStatement(age,cpt,number);
                if(history_data.get("success").toString().equalsIgnoreCase("01")){
                    JSONArray translist = new JSONArray();
                    translist = history_data.getJSONArray("data");
                    System.out.println(translist.toString());
                    //history retrieve success
                    d1.put("history", translist.toList());
                }
                else{
                    //history error
                    d1.put("history", null);
                }
            }
            catch(Exception e){
                d1.put("history", null);
            }
            
           try{
                history_data2 = core1.getMiniStatementInEven(age,cpt,number);
                if(history_data2.get("success").toString().equalsIgnoreCase("01")){
                    JSONArray translist = new JSONArray();
                    translist = history_data2.getJSONArray("data");
                    System.out.println(translist.toString());
                    //history retrieve success
                    d1.put("even", translist.toList());
                }
                else{
                    //history error
                    d1.put("even", null);
                }
            }
            catch(Exception e){
                d1.put("even", null);
            }
            
            accounts.add(d1);
            
            
            
            
            
            
        }
        
        response.put("success", "01");
        response.put("message", "Operation has been processed successfully");
        response.put("total_balance", total_balance.toString());
        response.put("data", accounts);
        
        return response;
    }
   
    
    @RequestMapping(value="/getAccountHistory", method = RequestMethod.POST)
    public Map<String,Object> accountHistory(@RequestBody Map<String, String> payload) throws SQLException, ClassNotFoundException{
        String age = payload.get("age");
        String cpt = payload.get("cpt");
        Map<String,Object> response = new HashMap();
        int number = 10;
        
        return core1.getMiniStatement(age,cpt,number).toMap();
        
    }
    
     @RequestMapping(value="/getMiniStatementInEven", method = RequestMethod.POST)
    public Map<String,Object> getMiniStatementInEven(@RequestBody Map<String, String> payload) throws SQLException, ClassNotFoundException{
        String age = payload.get("age");
        String cpt = payload.get("cpt");
        Map<String,Object> response = new HashMap();
        int number = 10;
        
        return core1.getMiniStatementInEven(age,cpt,number).toMap();
        
    }
   
    
     @RequestMapping(value="/getMiniStatementInEven2", method = RequestMethod.POST)
    public Map<String,Object> getMiniStatementInEven2(@RequestBody Map<String, String> payload) throws SQLException, ClassNotFoundException{
        String age = payload.get("age");
        String cpt = payload.get("cpt");
        Map<String,Object> response = new HashMap();
        int number = 10;
        
        return core1.getMiniStatementInEven2(age,cpt,number).toMap();
        
    }
    
    @RequestMapping(value="/getClientInfo", method = RequestMethod.POST)
    public Map<String,Object> getClientInfo(@RequestBody Map<String, String> payload) throws SQLException, ClassNotFoundException{
        Map<String,Object> response = new HashMap();
        String age = "";
        String cpt = "";
        String token = "";
        try{
        
            cpt = payload.get("cpt");
            token = payload.get("token");
        }
        catch(Exception e){
            response.put("status", "03");
            response.put("message", "Bad request, please format data correctly. "+e.getMessage());
            return response;
            
        }
        
        
        //we query the token and make sure it's unique in our DB and proceed else we return a duplicate status
        CrescoApiCalls api_call = crescoApiCallsRepo.findByToken(token);
        if(api_call != null){
            response.put("status", "02");
            response.put("message", "Token not unique, this token has been used already, please kindly generate another token for your request");
            return response;
        }
        
        
        response =  core1.getClientInfo(cpt, token);
        
        Date date = new Timestamp(System.currentTimeMillis());
        String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()) ;
        
        CrescoApiCalls api_call2 = new CrescoApiCalls(token, time,date);
        api_call2.setResponse(response.toString());
        
        crescoApiCallsRepo.save(api_call2);
        response.put("status", "01");
        return response;
        
    }
    
    @RequestMapping(value="/clientFileTreatmentStatus", method = RequestMethod.POST)
    public Map<String,Object> clientFileTreatmentStatus(@RequestBody Map<String, String> payload) throws SQLException, ClassNotFoundException{
        Map<String,Object> response = new HashMap();
        String age = "";
        String cpt = "";
        String token = "";
        try{
        
            cpt = payload.get("cpt");

            token = payload.get("token");
        }
        catch(Exception e){
            response.put("success", "400");
            response.put("message", "Bad request, please format data correctly. "+e.getMessage());
            return response;
            
        }
        //we query the token and make sure it's unique in our DB and proceed else we return a duplicate status
        CrescoApiCalls api_call = crescoApiCallsRepo.findByToken(token);
        if(api_call != null){
            response.put("status", "02");
            response.put("message", "Token not unique, this token has been used already, please kindly generate another token for your request");
            return response;
        }
        
        
        response =  core1.clientFileTreatmentStatus(cpt, token);
        
        Date date = new Timestamp(System.currentTimeMillis());
        String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()) ;
        
        CrescoApiCalls api_call2 = new CrescoApiCalls(token, time,date);
        api_call2.setResponse(response.toString());
        
        crescoApiCallsRepo.save(api_call2);
        response.put("status", "01");
        return response;
        
    }

    @RequestMapping(value="/getQuotiteByCli", method = RequestMethod.POST)
    public Map<String,Object> getQuotiteByCli(@RequestBody Map<String, String> payload) throws SQLException, ClassNotFoundException{
        Map<String,Object> response = new HashMap();
        String age = "";
        String cpt = "";
        String token = "";
        Long mnt1 = 0L;
        Long mnt2 = 0L;
        Long mnt3 = 0L;
        try{
        
            cpt = payload.get("cpt");

            token = payload.get("token");
            mnt1 = new Double(payload.get("mnt1").toString()).longValue();
            mnt2 = new Double(payload.get("mnt2").toString()).longValue();
            mnt3 = new Double(payload.get("mnt3").toString()).longValue();
            
        }
        catch(Exception e){
            response.put("success", "400");
            response.put("message", "Bad request, please format data correctly. mnt are Integers. "+e.getMessage());
            return response;
            
        }
        //we query the token and make sure it's unique in our DB and proceed else we return a duplicate status
        CrescoApiCalls api_call = crescoApiCallsRepo.findByToken(token);
        if(api_call != null){
            response.put("status", "02");
            response.put("message", "Token not unique, this token has been used already, please kindly generate another token for your request");
            return response;
        }
        
        //getQuotiteByCli(String compte ,Long mnt1,Long mnt2,Long mnt3,  String token)
        response =  core1.getQuotiteByCli(cpt,mnt1,mnt2,mnt3, token);
        
        Date date = new Timestamp(System.currentTimeMillis());
        String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()) ;
        
        CrescoApiCalls api_call2 = new CrescoApiCalls(token, time,date);
        api_call2.setResponse(response.toString());
        
        crescoApiCallsRepo.save(api_call2);
        response.put("status", "01");
        return response;
        
    }
    
     @RequestMapping(value ="/rollBackPaybill", method = RequestMethod.POST)
    public Map<String,Object> rolPaybill(@RequestBody Map<String, String> payload) throws SQLException, NoSuchAlgorithmException{
        Map<String,Object> resp = new HashMap();
         System.out.println("this is element for rolback "+payload.toString());
         JSONArray trans = mavianceService.verifytx(payload.get("pnbr"));
         PaymentObjectAPI paymentObjectAPI= bicecTransDetialsResp.findAllByPnbr(payload.get("pnbr"));
         if(trans != null ){
            if(trans.getJSONObject(0).get("status").toString().equalsIgnoreCase("SUCCESS") ){
                callback(payload.get("pnbr"), "01", paymentObjectAPI.getPtn());
                resp.put("message", "successfully retrieved!");
                resp.put("success","02" );
                return resp;
            }
        }
        
        AccInfo midle_acc=new AccInfo();
        midle_acc.setAge(payload.get("age"));
        midle_acc.setCpt(payload.get("cpt"));
        midle_acc.setDesp(payload.get("desp"));
        midle_acc.setDev(payload.get("dev"));
        midle_acc.setBkc(payload.get("bkc"));
        List<Bank> bklt = new ArrayList();
        List<Bank> bklt2 = new ArrayList();
                List<AccInfo> bl = paymentObjectAPI.getBank().get(0).getCredit();    
                  for(int j = 0 ; j<bl.size(); j++){
                           Bank bk = new Bank();
                           AccInfo md=new AccInfo();
                            bk.setDebit(bl.get(j));
                            List<AccInfo> midj = new ArrayList();
                            md.setAge(payload.get("age"));
                            md.setCpt(payload.get("cpt"));
                            md.setDesp(payload.get("desp"));
                            md.setDev(payload.get("dev"));
                            md.setBkc(payload.get("bkc"));
                            md.setMnt(bl.get(j).getMnt());
                            midj.add(md);
                            bk.setCredit(midj);
                            bklt2.add(bk);
                        }
       
                List<AccInfo> Debft = new ArrayList();
                Bank bkft = new Bank();
                Debft.add(paymentObjectAPI.getBank().get(0).getDebit());
                bkft.setCredit(Debft);// ajout du compte client sur les compte a crediter
                midle_acc.setMnt(paymentObjectAPI.getTotal());
                bkft.setDebit(midle_acc);//ajout du compte de passage sur la liste des compte a debiter 
                bklt2.add(bkft);
                paymentObjectAPI.getBank().clear();
                paymentObjectAPI.setBank(bklt2);
                int stat = 0;
            for(int i =0; i<paymentObjectAPI.getBank().size();i++){
                String age = paymentObjectAPI.getBank().get(i).getDebit().getAge();
                String cpt = paymentObjectAPI.getBank().get(i).getDebit().getCpt();
                Long amt = new Double(paymentObjectAPI.getTotal()).longValue();
                Long fees = new Double(paymentObjectAPI.getFees()).longValue();
                Long amt_fee = amt+fees;
                
                JSONObject debitor= new JSONObject(paymentObjectAPI.getBank().get(i).getDebit());//cpt a debiter
                
                JSONArray  creditor = new JSONArray(paymentObjectAPI.getBank().get(i).getCredit());//cpt a crediter
                
                
                String status = "";
                try {
                    status = bankOperation(age, cpt, amt_fee.toString(), fees.toString(), paymentObjectAPI.getPnbr(),paymentObjectAPI.getPtn(),debitor,creditor);
                } catch (SQLException ex) {
                    Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                }
                 if(status.equalsIgnoreCase("01")){
                     stat++;
                     try        
                    {
                        Thread.sleep(10000);
                    } 
                    catch(InterruptedException ex) 
                    {
                        Thread.currentThread().interrupt();
                    }
                 }
                 else{
                     Thread t = new Thread(new Runnable() {
                        public void run() {
                            try {
                                // continuesly do a check status till it get a favorable response
                                Thread.sleep(30000L);

                            } catch (InterruptedException ex) {
                                Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            callback( paymentObjectAPI.getPnbr(), "100", paymentObjectAPI.getPtn());
                            
                        };

                    });
                    t.start();

                    resp.put("success" , "100");
                    resp.put("message", "Operation roolBack not Successful");
                    resp.put("transaction_id" , externalId);
                    resp.put("message" , status);

                    return resp;
                     
                 }
            }
             if(stat == paymentObjectAPI.getBank().size()){
                
                resp.put("success", "01");
                resp.put("message", "Operation successfull");
                resp.put("transaction_id" , externalId);  
                
                return resp;
            }
            else{
                
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        try {
                            // continuesly do a check status till it get a favorable response
                            Thread.sleep(30000L);
                            
                        } catch (InterruptedException ex) {
                            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        callback( paymentObjectAPI.getPnbr(), "1000", paymentObjectAPI.getPtn());

                    };

                });
                t.start();
                
                resp.put("success" , "100");
                resp.put("message", "Operation rollBack not Successful");
                resp.put("transaction_id" , externalId);
                resp.put("message" , status);
                
                return resp;
            }
          
    }

      @RequestMapping(value ="/getPaybill", method = RequestMethod.POST)
    public Map<String,Object> getPaybill(@RequestBody Map<String, String> payload) throws SQLException{
        Map<String,Object> resp = new HashMap();
         System.out.println("this is element for rolback "+payload.toString());
        PaymentObjectAPI paymentObjectAPI= bicecTransDetialsResp.findAllByPnbr(payload.get("pnbr"));
       
       resp.put("success", "01");
     //  resp.put("data", bicecTransDetialsResp.findAllByPnbr(payload.get("pnbr")));
       resp.put("data", paymentObjectAPI);
        return resp;
    }
    
     //fonction pour chect statu du compte en entree
    @RequestMapping(value = "/checkTfjo", method = RequestMethod.POST)
    public Map<String, String> checkTfjo(@RequestBody Map<String, String> payload) throws SQLException, ClassNotFoundException {
        Map<String, String> response = new HashMap();
        System.out.println("execution requête Oracle :yvo tout est ok =" );
        System.out.println("YVO FIND ssi le compte existe ");  
        try {
            String respp = core.checkTfjo();
             if ("0".equalsIgnoreCase(respp)) {
                             System.out.println("on blocke la connection: ");
                            response.put("success", "02");
                            response.put("message2", "ko");
                            //response.put("message", respp[2]);
                            return response;
                        }
        } catch (SQLException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.put("success", "01");
        response.put("message2", "ok");
        response.put("message", "Mode jour ou made nuit");
        return response;
    }
 
    //fonction pour chect statu du compte en entree
    @RequestMapping(value = "/checkClient", method = RequestMethod.POST)
    public Map<String, Object> checkClient(@RequestBody Map<String, String> payload) throws SQLException, ClassNotFoundException {
        Map<String, Object> response = new HashMap();
        System.out.println("execution requête Oracle :yvo tout est ok =" );
        System.out.println("YVO FIND ssi le compte existe ");  
        String agence =payload.get("age");
        String compte=payload.get("cpt");
        String montant= payload.get("mnt");
        String frais =payload.get("frais");
        try {
            String respp []= core.checkClient(agence,  compte, montant, frais );
          //   if ("0".equalsIgnoreCase(respp)) {
                             System.out.println("data: ");
                            response.put("success", "01");
                            response.put("message2", respp[0] +respp[1]+respp[2]);
                            response.put("data",  respp );
                            return response;
                     //   }
        } catch (SQLException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.put("success", "01");
        response.put("message2", "ok");
        response.put("message", "Mode jour ou made nuit");
        return response;
    }
 
      
    //fonction pour chect statu du compte en entree
    @RequestMapping(value = "/checkEtebac", method = RequestMethod.POST)
    public Map<String, Object> checketebac(@RequestBody Map<String, String> payload) throws SQLException, ClassNotFoundException {
        Map<String, Object> response = new HashMap();
        System.out.println("execution requête Oracle :yvo tout est ok =" );
        System.out.println("YVO FIND ssi le compte ETEBAC");  
        String agence =payload.get("age");
        String compte=payload.get("cpt");
        try {
            JSONObject respp = core.getEtebac(agence,  compte );
          //   if ("0".equalsIgnoreCase(respp)) {
                             System.out.println("data: ");
                            response.put("success", "01");
                            response.put("data",  respp.toString());
                            return response;
                     //   }
        } catch (SQLException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.put("success", "01");
        response.put("message2", "ok");
        return response;
    }
    
    
    @RequestMapping(value = "/guceSend1", method = RequestMethod.POST)
    ResponseEntity<String> billPayToGuce(@RequestBody Map<String, Object> request){
        JSONObject response = new JSONObject();
        JSONObject payload = new JSONObject(request);
        JSONObject pi = new JSONObject();
        try {
            pi = new JSONObject(payload.get("payerinfo").toString());
            String tel1=pi.getString("iden");
            response.put("intent", payload.get("intent"));
            response.put("createtime", payload.get("createtime"));
            response.put("acquirertrxref", payload.get("acquirertrxref"));
            response.put("amount", payload.get("amount"));
            response.put("currency", payload.get("currency"));
            response.put("description", payload.optString("description"));
            response.put("payerinfo", payload.get("payerinfo"));
            response.put("billList", payload.get("billList"));
            JSONArray data = new JSONArray(payload.get("billList").toString());
            JSONObject datam = new JSONObject();
            for (int i=0;i<data.length();i++){
                datam = new JSONObject(data.get(i).toString());
                System.out.println("Start yvo save facture depuis GUCE03 : "+ datam.toString());
                BigInteger createtimeb = datam.getBigInteger("createTime");
                BigInteger dueTime = datam.getBigInteger("dueTime");
                BigInteger dueAmount = datam.getBigInteger("dueAmount");
                String v=datam.getString("status");
                String v1=datam.getString("currency");
                String v2=datam.getString("billRef");
            }
        }catch (Exception ex) {
            payload.put("error", ex.getLocalizedMessage());
            payload.put("error_description", ex.getMessage());
            payload.put("state", "REJECTED");
            return ResponseEntity.badRequest().body(payload.toString());
        }

        try {
            String uri = "http://localhost:9091/guce/guceSend";
           Nomenclature n = nomenclatureRepository.findTabcdAndAcsd("0012","0230","0");
            if(n!=null){
                uri = n.getLib2();
            }else{
                uri = "http://10.100.30.25:8080/efacturepro/guce/guceSend";
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            RestTemplate restTemplate = getRestTemplate();
            System.out.println("This is the statusBB send " );
            ResponseEntity<String> response1 = restTemplate.postForEntity(uri, entity, String.class);
            System.out.println("This is the statusBB " + response1.getStatusCodeValue());
            System.out.println("This is the statusBB " + response);
            if(response1.getStatusCodeValue()==200){
                System.out.println("This is the statusBB ok" );
                return response1;
            } else if(response1.getStatusCodeValue()==400){
                System.out.println("This is the statusBB 400" );
                return response1;
            } else{
                System.out.println("This is the statusBB KO" );
                return response1;
           }
        } catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException ex) {
            request.put("error", ex.getMessage());
            request.put("error_description", ex.getMessage());
            //respond.put("data", ex.toString());
            return ResponseEntity.badRequest().body(request.toString());
        }

    }

    @RequestMapping(value = "/guceSendB1", method = RequestMethod.POST)
    ResponseEntity<String> billPayToGuceB(@RequestBody Map<String, Object> request){
        JSONObject response = new JSONObject();
        JSONObject payload = new JSONObject(request);
        try {
            String uri = "http://localhost:9091/guce/guceSendBok";
            Nomenclature n = nomenclatureRepository.findTabcdAndAcsd("0012","0231","0");
            if(n!=null){
                uri = n.getLib2();
            }else{
                uri = "http://localhost:9091/guce/guceSendBok";
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            RestTemplate restTemplate = getRestTemplate();
            System.out.println("This is the statusBB send " );
            ResponseEntity<String> response1 = restTemplate.postForEntity(uri, entity, String.class);
            System.out.println("This is the statusBB " + response1.getStatusCodeValue());
            System.out.println("This is the statusBB " + response);
            if(response1.getStatusCodeValue()==200){
                System.out.println("This is the statusBB ok" );
                return response1;
            } else if(response1.getStatusCodeValue()==400){
                System.out.println("This is the statusBB 400" );
                return response1;
            } else{
                System.out.println("This is the statusBB KO" );
                return response1;
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException ex) {
            payload.put("error", ex.getMessage());
            payload.put("error_description", ex.getMessage());
            return ResponseEntity.badRequest().body(payload.toString());
        }
    }



//    @RequestMapping(value = "/checkClientAccountAll", method = RequestMethod.POST)
//    public Map<String, String> checkClientAccountAll(@RequestBody Map<String, String> payload) {
//        Map<String, String> response = new HashMap();
//        String ag = payload.get("age");
//        String cpt = payload.get("cpt");
//        String cli = payload.get("cli");
//        System.out.println("YVO FIND les compte back office " + cpt);
//        try {
//            String[] resp = core1.checkBackOfficeAcc(ag, cpt);
//            if ("1".equals(resp[0])) {
//                System.out.println("YVO FIND les compte back office: Compte ko  " + cpt);
//                //return response;
//            } else {
//                System.out.println("YVO FIND les compte back office compte ok" + cpt);
//                response.put("success", "100");
//                response.put("message2", "KO");
//                response.put("message", resp[2]);
//                return response;
//            }
//        } catch (SQLException | ClassNotFoundException ex) {
//            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        System.out.println("YVO FIND si opposition compte " + cpt);
//        try {
//            String[] resp = core1.checkOppAcc(ag, cpt);
//            if ("1".equals(resp[0])) {
//                System.out.println("YVO FIND les compte Opp: Compte Ok  " + cpt);
//                //return response;
//            } else {
//                System.out.println("YVO FIND les compte OP compte KO" + cpt);
//                response.put("success", "100");
//                response.put("message2", "KO");
//                response.put("message", resp[2]);
//                return response;
//            }
//        } catch (SQLException | ClassNotFoundException ex) {
//            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        System.out.println("YVO FIND si opposition client " + cli);
//        try {
//            String[] respc = core1.checkOppCli(cli);
//            if ("1".equals(respc[0])) {
//                System.out.println("YVO FIND  Opp: client ko " + cpt);
//                //return response;
//            } else {
//                System.out.println("YVO FIND les compte OP Client ok: " + cpt);
//                response.put("success", "100");
//                response.put("message2", "KO");
//                response.put("message", respc[2]);
//                return response;
//            }
//        } catch (SQLException | ClassNotFoundException ex) {
//            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        System.out.println("YVO FIND si xcomptze provision xcli: " + cli);
//        try {
//            String[] respp = core1.checkCliProv(cli);
//            if ("1".equalsIgnoreCase(respp[0])) {
//                System.out.println("YVO FIND les compte provision cpt KO " + cpt);
//                //return response;
//            } else {
//                System.out.println("YVO FIND les compte avec provision ok: " + cpt);
//                response.put("success", "100");
//                response.put("message2", "KO");
//                response.put("message", respp[2]);
//                return response;
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//     
//        System.out.println("YVO FIND si compte dormant: " + cli);
//        try {
//            String[] respp = core1.checkCptSleep(ag, cpt);
//            if ("1".equalsIgnoreCase(respp[0])) {
//                System.out.println("YVO FIND les compte dormant cpt KO " + cpt);
//                response.put("success", "01");
//                response.put("message2", "compte ok pour transaction");
//                response.put("message", respp[2]);
//                return response;
//            } else {
//                System.out.println("YVO FIND les compte avec dormant ok: " + cpt);
//                response.put("success", "100");
//                response.put("message2", "KO: compte dormant");
//                response.put("message", respp[2]);
//                return response;
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(BicecPayController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        response.put("success", "01");
//        response.put("message2", "0k");
//        response.put("message", "compte inexistant");
//        return response;
//    }


}
