/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwomi.config.TraceService;
import com.iwomi.model.Payer;
import com.iwomi.model.PaymentObject;
import com.iwomi.model.SystemProp;
import com.iwomi.model.TransactionHistory;
import com.iwomi.repository.SystePropRepository;
import com.iwomi.repository.TransHisRepository;
import com.iwomi.serviceInterface.OperationApi;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author HP
 */
@Service
public class OperationApiImp implements OperationApi {

    String uri = "https://sandbox.momodeveloper.mtn.com/collection/";
//	final String uri = "https://sandbox.momodeveloper.mtn.com/disbursement/";
    public static final String CONN_STRING = "jdbc:mysql://localhost:3306/mtnapimanagement?autoReconnect=true&useSSL=false";
    @Autowired
    SystePropRepository systemProp;
    @Autowired
    TransHisRepository transactionHistroy;
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    Connection connection, con = null;
    String idTrait, codeFichier;
    String code = "";

    @Override
    public Map<String, String> ApiOperation(String amount, String number, String payerMsg, String payeeMsg,
            String currency, String token, String subKey, String externalId, String opr, String oo) {
        String name = new TraceService().getInstance();

        PaymentObject paymentObject;
        Payer payer;

        RestTemplate restTemplate = new RestTemplate();
        payer = new Payer("MSISDN", number);
        //int unique_id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        paymentObject = new PaymentObject(amount, currency, externalId, payer, payerMsg, payeeMsg);

        Map<String, String> response = new HashMap<String,String>();

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(paymentObject);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(OperationApiImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(json);

        UUID uuid = UUID.randomUUID();
        String urip = "";
        if (oo.equals("002")) {
            urip = this.uri + "v1_0/transfer";
        } else if (oo.equals("001")) {
            urip = this.uri + "v1_0/requesttopay";
        }
        System.out.println("THE code :" + oo);
        System.out.println("THE URL :" + this.uri);
        System.out.println("THE URL :" + urip);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Authorization", "Bearer " + token);
        // headers.add("X-Callback-Url", "");
        headers.add("X-Reference-Id", uuid.toString());
        headers.add("X-Target-Environment", "sandbox");
        headers.add("Ocp-Apim-Subscription-Key", subKey);

        HttpEntity<PaymentObject> entity = new HttpEntity<PaymentObject>(paymentObject, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(urip, entity, String.class);

        System.out.println("Yanick see the status code " + result.getStatusCodeValue());

        // Code = 200.
        if (result.getStatusCodeValue() == 202) {
            response.put("referenceId", uuid.toString());
            response.put("success", "01");
            response.put("data", result.getBody());
            // save operation in the system

            Date date = new Date();
//            Map<String, String> s = new HashMap<String, String>();
//( int amount, String currency, String externalId, String payer, String payerMessage, String payeeNote, String status,
//        String idReference, String operation_type, String payertype, String reason,String date_modify)
            TransactionHistory s = new TransactionHistory(amount,name, currency, externalId, number, payerMsg, payeeMsg,
                    "1000", uuid.toString(), opr, "", "", date.toString());
            transactionHistroy.save(s);

        } else if (result.getStatusCodeValue() == 409) {
            Date date = new Date();
//            Map<String, String> s = new HashMap<String, String>();
//( int amount, String currency, String externalId, String payer, String payerMessage, String payeeNote, String status,
//        String idReference, String operation_type, String payertype, String reason,String date_modify)
            TransactionHistory s = new TransactionHistory(amount, name, currency, externalId, number, payerMsg, result.getBody(),
                    "100", uuid.toString(), opr, "", "", date.toString());
            transactionHistroy.save(s);
            response.put("referenceId", uuid.toString());
            response.put("success", "100");
            response.put("data", result.getBody());
        } else {
            response.put("referenceId", uuid.toString());
            response.put("success", "100");
            response.put("data", result.getBody());
        }

        System.out.println("Yanick please see this " + response);
        return response;
    }

    public Map<String, String> ApiOperation_v1(String amount, String number, String payerMsg, String payeeMsg,
            String currency, String token, String subKey, String externalId, String opr, String oo) {
        
        String name = new TraceService().getInstance();

        PaymentObject paymentObject;
        Payer payer;

        RestTemplate restTemplate = new RestTemplate();
        payer = new Payer("MSISDN", number);
        //int unique_id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        paymentObject = new PaymentObject(amount, currency, externalId, payer, payerMsg, payeeMsg);

        Map<String, String> response = new HashMap<String, String>();

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(paymentObject);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(OperationApiImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(json);

        UUID uuid = UUID.randomUUID();
        String urip = "";
        if (oo.equals("002")) {
            urip = this.uri + "v1_0/transfer";
        } else if (oo.equals("001")) {
            urip = this.uri + "v1_0/requesttopay";
        }
        System.out.println("THE code :" + oo);
        System.out.println("THE URL :" + this.uri);
        System.out.println("THE URL :" + urip);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Authorization", "Bearer " + token);
        // headers.add("X-Callback-Url", "");
        headers.add("X-Reference-Id", uuid.toString());
        headers.add("X-Target-Environment", "sandbox");
        headers.add("Ocp-Apim-Subscription-Key", subKey);

        HttpEntity<PaymentObject> entity = new HttpEntity<PaymentObject>(paymentObject, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(urip, entity, String.class);

        System.out.println("Yanick see the status code " + result.getStatusCodeValue());

        // Code = 200.
        if (result.getStatusCodeValue() == 202) {
            response.put("referenceId", uuid.toString());
            response.put("success", "01");
            response.put("data", result.getBody());
            // save operation in the system

            Date date = new Date();
//            Map<String, String> s = new HashMap<String, String>();
//( int amount, String currency, String externalId, String payer, String payerMessage, String payeeNote, String status,
//        String idReference, String operation_type, String payertype, String reason,String date_modify)
            TransactionHistory s = new TransactionHistory(amount,name, currency, externalId, number, payerMsg, payeeMsg,
                    "1000", uuid.toString(), opr, "", "", date.toString());
            transactionHistroy.save(s);

        } else if (result.getStatusCodeValue() == 409) {
            Date date = new Date();
//            Map<String, String> s = new HashMap<String, String>();
//( int amount, String currency, String externalId, String payer, String payerMessage, String payeeNote, String status,
//        String idReference, String operation_type, String payertype, String reason,String date_modify)
            TransactionHistory s = new TransactionHistory(amount, name, currency, externalId, number, payerMsg, result.getBody(),
                    "100", uuid.toString(), opr, "", "", date.toString());
            transactionHistroy.save(s);
            response.put("referenceId", uuid.toString());
            response.put("success", "100");
            response.put("data", result.getBody());
        } else {
            response.put("referenceId", uuid.toString());
            response.put("success", "100");
            response.put("data", result.getBody());
        }

        System.out.println("Yanick please see this " + response);
        return response;
    }

    public void insertTrans(Map<String, String> s) throws SQLException {
        String select = "";
//		Statement stmt = (Statement) connection.createStatement();
        select = "insert into trans (payertype,amount,currency,externalId,payer,payerMessage,payeeNote,status,idReference,operation_type,date_modify)values('MSISDN','"
                + s.get("amount") + "','" + s.get("currency") + "','" + s.get("externalId") + "','" + s.get("payer")
                + "','" + s.get("payerMsg") + "','" + s.get("payeeMsg") + "','" + s.get("status") + "','"
                + s.get("idReference") + "','" + s.get("operation_type") + "','" + s.get("date_modify") + "')";
        System.out.println("BEFORE QUERRY");

//		stmt.execute(select);
        System.out.println("AFTER QUERRY");
    }

    public void updateStatus(String trans_key, String status) throws SQLException {
        System.out.println("updating status of transaction :SFF" + trans_key + " to status" + status);
//       st
//        select = "UPDATE trans set status ='" + status + "' where idReference = '" + trans_key + "'";
        List<TransactionHistory> r = transactionHistroy.findByIdReference(trans_key);
        r.get(0).setStatus(status);
        transactionHistroy.save(r.get(0));
    }

    public void updateStatus2(String trans_key, String status, String reason) throws SQLException {
        System.out.println("the status and the reason are" + status + " and " + reason);
//       st
//        select = "UPDATE trans set status ='" + status + "' where idReference = '" + trans_key + "'";
        List<TransactionHistory> r = transactionHistroy.findByIdReference(trans_key);
        r.get(0).setStatus(status);
        r.get(0).setReason(reason);
        transactionHistroy.save(r.get(0));
    }

    public void updateStatus1(String trans_key, String status) throws SQLException {
        List<TransactionHistory> r = transactionHistroy.findByIdReference(trans_key);
        r.get(0).setStatus(status);
        transactionHistroy.updateSTatus(status, trans_key);
    }

    @Override
    public Map<String, String> OperationStatus1(String referenceId, String subKey, String token, String oo) {
        Map<String, String> status = new HashMap<String, String>();
        if (oo == "001") {
            String uri = this.uri + "v1_0/requesttopay/" + referenceId;
        } else if (oo == "002") {
            String uri = this.uri + "v1_0/transfer/" + referenceId;
        }

        String request = "";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // add basic authentication
        headers.add("Ocp-Apim-Subscription-Key", subKey);
        headers.add("X-Target-Environment", "sandbox");
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<String>(request, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        System.out.println("This is the status " + response.getStatusCodeValue());

        if (response.getStatusCodeValue() == 200) {
            status.put("success", "01");
            status.put("statuscode", "200");
            status.put("data", response.getBody());

        } else if (response.getStatusCodeValue() == 401) {
            status.put("success", "100");
            status.put("statuscode", "401");
            status.put("msg", "unauthorized");
        } else if (response.getStatusCodeValue() == 500) {
            status.put("success", "100");
            status.put("statuscode", "401");
            status.put("msg", "internal server error");
        } else {
            status.put("success", "100");
            status.put("msg", response.getBody());
        }
        return status;
    }

    public void connection() throws SQLException, ClassNotFoundException, JSONException {
        Class.forName("com.mysql.jdbc.Driver");
        try {
            System.out.println("BEFORE CONNEXION");
            connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            System.out.println("AFTER CONNEXION");
        } catch (SQLException ex) {
        }
    }

    public Map<String, String> getFerenceIdDetail(String referenceId) throws SQLException {
        String select = "";
        System.out.println(select);
//        ResultSet result = stmt.executeQuery(select);
        List<TransactionHistory> r = transactionHistroy.findByIdReference(referenceId);
        Map<String, String> detail = new HashMap<String, String>();

        if (r.size() == 1) {
            detail.put("transaction_id", r.get(0).getExternalId());
            detail.put("idReference", r.get(0).getIdReference());
            detail.put("success", r.get(0).getStatus());
            detail.put("number", r.get(0).getPayer());

        }
        System.out.println("tebit on update");
        System.out.println(detail);
        return detail;
    }

    @Override
    public Map<String, String> callbackclient(String referenceId, String code) throws SQLException {
        // TODO Auto-generated method stub//we define the url of the client
        Map<String, String> detail = new HashMap();
        Map<String, String> status = new HashMap<String, String>();
        String s = "";
        try {
            detail = getFerenceIdDetail(referenceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //client
        String url = "http://10.100.20.53:8080/efacturepro/clients/callBack";
//        if (code == "001") {
// //           url = "https://www.softeller.com/Operation/set_status_op/" + detail.get("status") + "/"//http://10.100.20.53:8080/efacturepro/
//   //                 + detail.get("idReference");// backback address
//            url = "https://10.100.20.53:8080/efacturepro/clients/callBack/"+detail.get("externalId") ;// backback address   externalId
//        } else if (code == "002") {
////            url = "";
//            url = "https://localhost/efacturepro/admin/api_connexion/get_all";// backback address
////            url = "https://www.softeller.com/Operation/set_status_op/" + detail.get("status") + "/"
////                    + detail.get("idReference");// backback address
//        }

        ResponseEntity<String> result = urlCallCustomise(detail, url);
        // return of the callback function
        if (result.getStatusCodeValue() == 200) {
            status.put("success", s);

        } else {
            s = "100";
            status.put("success", "100");
        }

        return status;
    }

    private ResponseEntity<String> urlCallCustomise(Map<String, String> detail, String url) {

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.add("Authorization", "");
        HttpEntity<Map> entity = new HttpEntity<Map>(detail, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(url, entity, String.class);
        System.out.println("Yann see this with me :" + result.getBody());
        return result;
    }

    @Override
    public String getTokenSystem() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, String> getAccessToApI2(String serviceCode)
            throws SQLException, ClassNotFoundException, JSONException {
        if (serviceCode.equals("001")) {
            uri = "https://sandbox.momodeveloper.mtn.com/collection/";
        } else if (serviceCode.equals("002")) {
            uri = "https://sandbox.momodeveloper.mtn.com/disbursement/";
        }
        List<SystemProp> ob = systemProp.findByCode(serviceCode);
        System.out.println("collecting code from our database");
        System.out.println(ob);
        System.out.println("the size of the code:" + ob.size());
        Map<String, String> detail = new HashMap<String, String>();
        Boolean found = false;
        if (ob.size() > 0) {
            detail.put("apiUser", ob.get(0).getApiUser());
            detail.put("apiKey", ob.get(0).getApiKey());
            detail.put("subKey", ob.get(0).getSrvid());
            detail.put("cmptbkst", ob.get(0).getCallback());
            detail.put("codser", ob.get(0).getCode());
            found = true;
        }
       return detail;
       
    }

    public Map<String, String> getAccessToApI3(String serviceCode)
            throws SQLException, ClassNotFoundException, JSONException {

        List<SystemProp> ob = systemProp.findByCode(serviceCode);
        System.out.println("collecting code from our database");
        System.out.println(ob);
        System.out.println("the size of the code:" + ob.size());
        Map<String, String> detail = new HashMap<String, String>();
        Boolean found = false;
        if (ob.size() > 0) {
            detail.put("apiUser", ob.get(0).getApiUser());
            detail.put("apiKey", ob.get(0).getApiKey());
            detail.put("subKey", ob.get(0).getSrvid());
            found = true;
        }
        return detail;
    }



    @Override
    public String getToken1(String apiUser, String apiKey, String subKey) {

        String uri = this.uri + "token/";
        String token = "";
        String request = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println("Query to mtn ");
        // add basic authentication
        String pp = apiUser + ":" + apiKey;
        headers.add("Ocp-Apim-Subscription-Key", subKey);
        String BasicBase64format = Base64.getEncoder().encodeToString(pp.getBytes());
        System.out.println("code: " + BasicBase64format);
        System.out.println("code: " + subKey);
        System.out.println("code: " + uri);
        headers.add("Authorization", "Basic " + BasicBase64format);

        HttpEntity<String> entity = new HttpEntity<String>(request, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);

        System.out.println("This is the status " + response.getStatusCodeValue());

        if (response.getStatusCodeValue() == 200) {
            return response.getBody();
        } else if (response.getStatusCodeValue() == 401) {
            return "unauthorized";
        } else if (response.getStatusCodeValue() == 500) {
            return "internal server error";
        } else {
            String result = response.getBody();
            return result;
        }
    }

    @Override
    public Map<String, String> getAccessToApI(String s) throws SQLException, ClassNotFoundException, JSONException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
