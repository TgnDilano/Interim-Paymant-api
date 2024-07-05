package com.iwomi.controller;
//import io.jsonwebtoken.impl.TextCodec;

import com.iwomi.config.TraceService;
import com.iwomi.model.TransactionHistory;
import com.iwomi.repository.TransHisRepository;
import com.iwomi.service.MomoAPICollectionImp;
import com.iwomi.service.MomoAPIDisbursementImp;
import com.iwomi.serviceInterface.OperationApi;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.json.JSONException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
public class SoftellerApi {
   @Autowired
    OperationApi operationApi;
    @Autowired
    MomoAPICollectionImp momoAPICollectionImp;
    @Autowired
    MomoAPIDisbursementImp momoAPIDisbursementImp;
    @Autowired
    TransHisRepository transactionHistroy;
    
    
    @RequestMapping(value = "/checkstatustreate", method = RequestMethod.POST)
    public void checkstatustreate() {//update status
//    public void checkstatustreate(@RequestBody Map<String, String> payload1) {//update status
        Map<String, String> payload1 = null;
        if (!payload1.isEmpty() && payload1.containsKey("status") && payload1.containsKey("idReference")) {
            String status = payload1.get("status");
            String id = payload1.get("idReference");
            TransactionHistory s = transactionHistroy.findByIdReference(id).get(0);
            if (s != null) {
                s.setCntSend(s.getCntSend() + 1);
                s.setStatus(status);
                transactionHistroy.save(s);
                //send checkstatus to custom
                for (int i = 0; i < 2; i++) {
//            Callback to customer
                    System.out.println("Callback to customer");
                    try {
                        operationApi.callbackclient(id, "");
                    } catch (SQLException ex) {
                        Logger.getLogger(SoftellerApi.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else {
            System.out.println("Parameters not well coded");
        }
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

    @RequestMapping(value = "/requestPayment_v1", method = RequestMethod.POST)
    public Map<String, String> RequestPayment_v1(@RequestBody Map<String, String> payload1) throws Exception {
        
        
        String name = new TraceService().getInstance();
        System.out.println("Yanick this is the name of the user extracted from the token consuming this **"+name);
        
        /***checking it its a mobile money number***/
        /*if(!checkMomo(payload1.get("phone"))){
            Map<String, String> response = new HashMap();
            response.put("success", "100");
            response.put("message", "User do not have a mobile money account");
            return response;
        }
        */
        
        int o = 4;
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
                                    Thread.sleep(500L);
                                } catch (InterruptedException e1) {
                                }
                                found = checkstatusOperationCollV1(payload, requestpay, "001");
                                i++;
                                System.out.println("count " + i);
                            } while (i < o && !found);
                            if (found || i == o) {
                                // function to call send to him message
                                
                                try {
                                    operationApi.callbackclient(requestpay.get("referenceId"), pps.get("cmptbkst"));
                                } catch (SQLException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                        }
                    ;

                    });
			t.start();
                    return requestpay;
                } else {
                    //return fail and stop
                    return null;
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
    @RequestMapping(value = "/makeDeposit_v1", method = RequestMethod.POST)
    public Map<String, String> makeDeposit_v1(@RequestBody Map<String, String> payload1) throws Exception {
        
        String name = new TraceService().getInstance();
        int o = 4;
        Map<String, String> payload = convert_SoftellerRe(payload1);
        System.out.println("pls 1");

        Map<String, String> pps = operationApi.getAccessToApI3("002");
        System.out.println("pls 3");
        System.out.println(pps.get("apiUser"));
        System.out.println(pps.get("apiKey"));
        System.out.println(pps.get("subKey"));

        if (!pps.isEmpty()) {
            String data = momoAPIDisbursementImp.getToken(pps.get("apiUser"), pps.get("apiKey"), pps.get("subKey"));
            System.out.println("pls 2");
            if (!data.equalsIgnoreCase("FAILED")) {

                JSONObject ss = new JSONObject(data);
                /***we check the balance of our account before proceeding****/
                /*
                String balanceMap = momoAPIDisbursementImp.getAccountBalance(pps.get("subKey"),ss.getString("access_token"));

                JSONObject balance = new JSONObject(balanceMap);
                if (balance.has("availableBalance")) {
                    
                    if( Double.parseDouble(payload.get("amount"))  >Double.parseDouble(balance.getString("availableBalance"))){
                        Map <String, String> response = new HashMap();
                        //iwomi has small balance to do the payment
                        response.put("success", "100");
                        response.put("message", "IWOMI has run out of funds");
                        return response;
                    }
                }
                */
                Map<String, String> requestpay
                        = momoAPIDisbursementImp.performPayment(payload.get("amount"), payload.get("number"),
                                payload.get("payerMsg"), payload.get("payeeMsg"), payload.get("currency"),
                                ss.getString("access_token"), pps.get("subKey"), payload1.get("externalId"));
                payload.put("token", ss.getString("access_token"));
                payload.put("subKey", pps.get("subKey"));
                System.out.println("it is the payload content");
                System.out.println(payload);
                if (requestpay.get("success").equals("01")) {
                    Date date = new Date();
                    TransactionHistory s = new TransactionHistory(payload.get("amount"), name, payload.get("currency"),
                            payload.get("externalId"), payload.get("number"), payload.get("payerMsg"), payload.get("payeeMsg"),
                            "1000", requestpay.get("referenceId"), "DE", "", "", date.toString());
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
                                    Thread.sleep(500L);
                                } catch (InterruptedException e1) {
                                }
                                found = checkstatusOperationDepV1(payload, requestpay, "002");
                                i++;
                                System.out.println("count " + i);
                            } while (i < o && !found);

                            if (found) {
                                // function to call send to him message
                                try {
                                    operationApi.callbackclient(requestpay.get("referenceId"), "002");
                                } catch (SQLException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    ;

                    });
			t.start();
                    return requestpay;
                } else {
                    //return fail and stop
                    return null;
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
    
    @RequestMapping(value ="/history", method = RequestMethod.GET)
    public List<TransactionHistory> getHistory(){
        return transactionHistroy.findAll();
    }

    private boolean checkstatusOperationDepV1(Map<String, String> payload, Map<String, String> requestPay, String oo) {
        Boolean found = false;
        Map<String, String> status;
        String status1 = momoAPIDisbursementImp.OperationStatus(requestPay.get("referenceId"), payload.get("subKey"),
                payload.get("token"));
        if (!status1.equalsIgnoreCase("FAILED")) {
            JSONObject data = new JSONObject(status1);
            System.out.println(data);
            if (data.getString("status").equals("SUCCESSFUL")) {// successfull transaction
                found = true;
                try {
                    operationApi.updateStatus2(requestPay.get("referenceId"), "01", "SUCCESSFUL");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (data.getString("status").equals("FAILED") || data.getString("status").equals("REJECTED")
                    || data.getString("status").equals("TIMEOUT")) {
                try {
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
                    || data.getString("status").equals("TIMEOUT")) {
                try {
                    System.out.println("status of the transaction : " + data.getString("status"));
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
    
    public boolean checkMomo(String number) throws SQLException{
        
        Map<String, String> pps = null;
        try {
            pps = operationApi.getAccessToApI2("001");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SoftellerApi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SoftellerApi.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!pps.isEmpty()) {
            String data = momoAPICollectionImp.getToken(pps.get("apiUser"), pps.get("apiKey"), pps.get("subKey"));
            if (!data.equalsIgnoreCase("FAILED")) {
                JSONObject ss = new JSONObject(data);
                String result = momoAPICollectionImp.MomoAccountExist(number,pps.get("subKey"),ss.getString("access_token"));
                if(result == "true"){
                    return true;
                }
                else if (result== "false"){
                    return false;
                }
                else{
                    //"error occured"
                }
            }
        }
        return true;
    }
    
    
    @RequestMapping(value ="/getPaymentStatus/{processingNumber}", method = RequestMethod.GET)
    public Map<String, String> checkCollectStatus(@PathVariable String processingNumber) throws SQLException{
        
        Map<String, String> res = new HashMap();

        Map<String, String> pps = null;
        try {
            pps = operationApi.getAccessToApI2("001");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SoftellerApi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SoftellerApi.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!pps.isEmpty()) {
            String data = momoAPICollectionImp.getToken(pps.get("apiUser"), pps.get("apiKey"), pps.get("subKey"));
            if (!data.equalsIgnoreCase("FAILED")) {
                JSONObject ss = new JSONObject(data);
                String result = momoAPICollectionImp.OperationStatus(processingNumber,pps.get("subKey"),ss.getString("access_token"));
                
                JSONObject response = new JSONObject(result);
                
                res.put("status", response.getString("status"));
                
                Map<String, String> ans = new HashMap();
                
                if(response.has("status")){
                    if(response.getString("status").equalsIgnoreCase("SUCCESSFUL")){
                        ans.put("success", "01");
                        return ans;
                    }
                    else if(response.getString("status").equalsIgnoreCase("FAILED")){
                        JSONObject reason = new JSONObject(response.get("reason").toString());
                        ans.put("success", "100");
                        ans.put("message", reason.getString("message"));
                        return ans;
                    }
                    
                }
            }
        }
        return res;
    }
    
        @RequestMapping(value ="/getTransferStatus/{processingNumber}", method = RequestMethod.GET)
    public Map<String, String> checkDisburseStatus(@PathVariable String processingNumber) throws SQLException{
        
        Map<String, String> res = new HashMap();

        Map<String, String> pps = null;
        try {
            
            pps = operationApi.getAccessToApI3("002");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SoftellerApi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SoftellerApi.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!pps.isEmpty()) {
            String data = momoAPIDisbursementImp.getToken(pps.get("apiUser"), pps.get("apiKey"), pps.get("subKey"));
            if (!data.equalsIgnoreCase("FAILED")) {
                JSONObject ss = new JSONObject(data);
                String result = momoAPIDisbursementImp.OperationStatus(processingNumber,pps.get("subKey"),ss.getString("access_token"));
                
                JSONObject response = new JSONObject(result);
                
                res.put("status", response.getString("status"));
                
                Map<String, String> ans = new HashMap();
                
                if(response.has("status")){
                    if(response.getString("status").equalsIgnoreCase("SUCCESSFUL")){
                        ans.put("success", "01");
                        return ans;
                    }
                    else if(response.getString("status").equalsIgnoreCase("FAILED")){
                        JSONObject reason = new JSONObject(response.get("reason").toString());
                        ans.put("success", "100");
                        ans.put("message", reason.getString("message"));
                        return ans;
                    }
                    
                }
            }
        }
        return res;
    }

    
    
    public Map<String, String> performMtnMOMO(@RequestBody Map<String, String> payload1) throws Exception {
        
        
        String name = new TraceService().getInstance();
        System.out.println("Yanick this is the name of the user extracted from the token consuming this **"+name);
        
        int o = 4;
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
                                    Thread.sleep(30000L);
                                } catch (InterruptedException e1) {
                                }
                                found = checkstatusOperationCollV1(payload, requestpay, "001");
                                i++;
                                System.out.println("count " + i);
                            } while (i < o && !found);
                            if (found || i == o) {
                                // function to call send to him message
                                
                                try {
                                    operationApi.callbackclient(requestpay.get("referenceId"), pps.get("cmptbkst"));
                                } catch (SQLException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                        }
                    ;

                    });
			t.start();
                    return requestpay;
                } else {
                    //return fail and stop
                    return null;
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
    
}
