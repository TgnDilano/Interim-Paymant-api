/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwomi.model.OmSystemConfig;
import com.iwomi.model.Payer;
import com.iwomi.model.PaymentObject;
import com.iwomi.model.Pwd;
import com.iwomi.model.TransactiomHistory;
import com.iwomi.repository.OmSystemConfigResp;
import com.iwomi.repository.PwdRepository;
import com.iwomi.repository.TransHisRepository;
import com.iwomi.serviceInterface.OperationApi;
import io.jsonwebtoken.impl.TextCodec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import javax.net.ssl.SSLContext;
import static org.apache.commons.lang3.StringUtils.trim;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author HP
 */


@Service
public class OrangeMoneyCashOut {
    
    @Autowired
    OmSystemConfigResp omSystemConfigResp;
    
    @Autowired
    TransHisRepository transactionHistroy;
    
    @Autowired 
    OperationApi operationApi;
    
    @Autowired
    OperationApiImp operationApiImp;
    
    
    @Autowired
    TransHisRepository transHisRepository ;
    
    @Autowired
    OrangeMoneyCashOut orangeMoneyApiImp ;
    
    @Autowired
    PwdRepository pwdRepository;
    
    
    
    String api_key, api_secret;
    
    final String uri = "https://apiw.orange.cm/";
    //final String uri = "https://api-s1.orange.cm/";

    
    public String getToken() {

        String uri = this.uri +"token";
        String request = "";
        String consumer_key = ""; 
        String consumer_secret = "";
        OmSystemConfig om_info = omSystemConfigResp.findByCode("100");
        consumer_key = om_info.getApiKey().trim();
        consumer_secret = om_info.getApiSecret().trim();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type","client_credentials");
        headers.add("Authorization", "Basic " + TextCodec.BASE64.encode(consumer_key + ":" + consumer_secret));
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        
        
       // RestTemplate restTemplate = new RestTemplate();
        RestTemplate restTemplate = null;
        try {
            restTemplate = getRestTemplate();
        } catch (KeyStoreException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    }

    public String initOperation() {
        String token = "";
        String data = getToken();
        if(!data.equalsIgnoreCase("FAILED")){
            JSONObject ss = new JSONObject(data);
            if(ss.has("access_token")){
                token = ss.getString("access_token");
            }
        }
        else{
            return null;
        }
        System.out.println("yann see this token is successfull "+token);

        String uri = this.uri +"omcoreapis/1.0.2/mp/init";
        //String uri = "https://apiw.orange.cm/omcoreapis/1.0.2/cashin/init";
        
        String request = "";
        String apiUser = ""; 
        String apiPass = "";
        OmSystemConfig om_info = omSystemConfigResp.findByCode("100");
        apiUser = om_info.getApiUser().trim();
        apiPass = om_info.getApiPass().trim();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer "+token);
        headers.add("X-AUTH-TOKEN", TextCodec.BASE64.encode(apiUser + ":" + apiPass));
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        
        
       // RestTemplate restTemplate = new RestTemplate();
        RestTemplate restTemplate = null;
        try {
            restTemplate = getRestTemplate();
        } catch (KeyStoreException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);

        System.out.println("This is the status " + response.getStatusCodeValue() +"and the response body is "+response.getBody());

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

    }

    public Map<String, String> PayOm(String amount, String tel, String pnbr, String description) {
        Map<String, String> result = new HashMap();
        String channelUserMsisdn = "";
        String pin = "";
        String token = "";
        String paytoken = "";
        String data = getToken();
        String data2 = initOperation();
        //System.out.println("Yann please see this "+data);
        //System.out.println("Yann please see this "+data2);
        if(!data.equalsIgnoreCase("FAILED")){
            JSONObject ss = new JSONObject(data);
            if(ss.has("access_token")){
                token = ss.getString("access_token");
            }
        }
        else{
            return null;
        }
        if(!data2.equalsIgnoreCase("FAILED")){
            JSONObject ss = new JSONObject(data2);
            if(ss.has("message") && ss.has("data")){
                if(ss.getString("message").equalsIgnoreCase("Payment request successfully initiated") && !ss.getJSONObject("data").isEmpty()){
                    paytoken = ss.getJSONObject("data").getString("payToken");
                }
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }

        String uri = this.uri +"omcoreapis/1.0.2/mp/pay";
        //String uri = "https://apiw.orange.cm/omcoreapis/1.0.2/cashin/pay";
        String apiUser = ""; 
        String apiPass = "";
        OmSystemConfig om_info = omSystemConfigResp.findByCode("100");
        apiUser = om_info.getApiUser().trim();
        apiPass = om_info.getApiPass().trim();
        pin = om_info.getPin().trim();
        channelUserMsisdn = om_info.getChmsd().trim();
        
        /*JSONObject res = new JSONObject();
        res.put("notifUrl", "");
        res.put("channelUserMsisdn",channelUserMsisdn);
        res.put("amount",amount);
        res.put("subscriberMsisdn",tel);
        res.put("pin",pin);
        res.put("orderId",pnbr);
        res.put("description",description);
        res.put("payToken",paytoken);*/
        
        Map<String, String> request = new HashMap();
        request.put("notifUrl", "");
        request.put("channelUserMsisdn",channelUserMsisdn);
        request.put("amount",amount);
        request.put("subscriberMsisdn",tel);
        request.put("pin",pin);
        request.put("orderId",pnbr);
        request.put("description",description);
        request.put("payToken",paytoken);
        
        
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Authorization", "Bearer "+token);
        headers.add("X-AUTH-TOKEN", TextCodec.BASE64.encode(apiUser + ":" + apiPass));
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
        
        
        System.out.println(""+ new JSONObject(headers).toString());
        System.out.println("Body "+ new JSONObject(request).toString());
        
        if(true){
            //return null;
        }
        
       // RestTemplate restTemplate = new RestTemplate();
        RestTemplate restTemplate = null;
        try {
            restTemplate = getRestTemplate();
        } catch (KeyStoreException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResponseEntity<String> response = null;
        try{
            response = restTemplate.postForEntity(uri, entity, String.class);
        }
        catch(HttpStatusCodeException  ex){
            System.out.println("Yankoo see this bro "+ ResponseEntity.status(ex.getRawStatusCode()).headers(ex.getResponseHeaders()).body(ex.getResponseBodyAsString()));
            
            result.put("success", "409");
            result.put("message","Phone Number has insufficient balance to perform operation");
            return result;
            
        }

        System.out.println("This is the status " + response.toString());

        if (response.getStatusCodeValue() == 200) {
            result.put("trans_id", pnbr); 
            JSONObject resp = new JSONObject(response.getBody());
            JSONObject store = resp.getJSONObject("data");
            //Date date = new Date();
            //TransactiomHistory s = new TransactiomHistory(store.get("amount").toString(), "xaf",store.get("txnmode").toString(),store.get("subscriberMsisdn").toString(),store.get("description").toString(),store.get("inittxnmessage").toString(),store.get("status").toString(),store.get("payToken").toString(),"Orange CashOut", "","",date.toString());
            //transactionHistroy.save(s);
            result.put("data", store.toString());
            if(store.get("status").toString().equalsIgnoreCase("SUCCESSFULL")){
                result.put("success", "01");
                result.put("message","SUCCESSFULL");
            }
            else if(store.get("status").toString().equalsIgnoreCase("PENDING")){
                result.put("success", "1000");
                result.put("message","PENDING");
            }
            else{
                result.put("success", "100");
                result.put("message",store.get("inittxnmessage").toString());
            }
            
            return result;
        } else if (response.getStatusCodeValue() == 500) {
            result.put("success", "500");
            result.put("message","An internal processing error occured");
            return result;//"internal server error"; 
        } else if (response.getStatusCodeValue() == 417) {
            result.put("success", "409");
            result.put("message","OM Number has insufficient balance to perform operation");
            return result;//"internal server error";
        }
        else {
            result.put("success", "500");
            result.put("message","Service temporary unavailable, try again later");
            return result;//result;
        }

    }

    public String pushOm(String paytoken) {
        String token = "";
        String data = getToken();
        if(!data.equalsIgnoreCase("FAILED")){
            JSONObject ss = new JSONObject(data);
            if(ss.has("access_token")){
                token = ss.getString("access_token");
            }
        }
        else{
            return null;
        }
        
        String uri = this.uri +"omcoreapis/1.0.2/mp/push/"+paytoken;
        
        String apiUser = ""; 
        String apiPass = "";
        OmSystemConfig om_info = omSystemConfigResp.findByCode("100");
        apiUser = om_info.getApiUser().trim();
        apiPass = om_info.getApiPass().trim();
        
        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer "+token);
        headers.add("X-AUTH-TOKEN", TextCodec.BASE64.encode(apiUser + ":" + apiPass));
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        
        
       // RestTemplate restTemplate = new RestTemplate();
        RestTemplate restTemplate = null;
        try {
            restTemplate = getRestTemplate();
        } catch (KeyStoreException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity,String.class);

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

    }
    
    public Map<String, String> getStatusOm(String paytoken) {
        Map<String, String> result = new HashMap();
        String token = "";
        String data = getToken();
        if(!data.equalsIgnoreCase("FAILED")){
            JSONObject ss = new JSONObject(data);
            if(ss.has("access_token")){
                token = ss.getString("access_token");
            }
        }
        else{
            return null;
        }
        
        String uri = this.uri +"omcoreapis/1.0.2/mp/paymentstatus/"+paytoken;
        
        String apiUser = ""; 
        String apiPass = "";
        OmSystemConfig om_info = omSystemConfigResp.findByCode("100");
        apiUser = om_info.getApiUser().trim();
        apiPass = om_info.getApiPass().trim();
        
        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer "+token);
        headers.add("X-AUTH-TOKEN", TextCodec.BASE64.encode(apiUser + ":" + apiPass));
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        
        
       // RestTemplate restTemplate = new RestTemplate();
        RestTemplate restTemplate = null;
        try {
            restTemplate = getRestTemplate();
        } catch (KeyStoreException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ResponseEntity<String> response = null;
        try{
            response = restTemplate.exchange(uri, HttpMethod.GET, entity,String.class);
        }
        catch(HttpStatusCodeException ex){
            System.out.println("Yankoo see this bro "+ ResponseEntity.status(ex.getRawStatusCode()).headers(ex.getResponseHeaders()).body(ex.getResponseBodyAsString()));
            
            result.put("success", "409");
            result.put("message","Phone Number has insufficient balance to perform operation");
            return result;
            
        }

        System.out.println("This is the status " + response.getStatusCodeValue());
        
        System.out.println(response.getBody());

        if (response.getStatusCodeValue() == 200) {
            JSONObject resp = new JSONObject(response.getBody());
            JSONObject store = resp.getJSONObject("data");
            
            
            if(store.get("status").toString().equalsIgnoreCase("SUCCESSFULL")){
                result.put("success", "01");
                result.put("trans_id",store.get("txnmode").toString());
                result.put("message",store.get("confirmtxnmessage").toString());
            }
            else if(store.get("status").toString().equalsIgnoreCase("PENDING")){
                result.put("success", "1000");
                result.put("trans_id",store.get("txnmode").toString());
                result.put("message",store.get("confirmtxnmessage").toString());
            }
            else{
                result.put("success", "100");
                result.put("trans_id", store.get("txnmode").toString());
                result.put("message",store.get("confirmtxnmessage").toString());
            }
            
            return result;
        } else if (response.getStatusCodeValue() == 401) {
            result.put("success", "100");
            result.put("message","token error:unauthorized");
            return result;
        } else if (response.getStatusCodeValue() == 500) {
            result.put("success", "100");
            result.put("message",response.getBody());
            return result;//"internal server error";
        } else {
            result.put("success", "100");
            result.put("message",response.getBody());
            return result;//result;
        }

    }
    
    public Map<String, String> getStatusOmDontUse(String paytoken) {
        Map<String, String> result = new HashMap();
        String token = "";
        String data = getToken();
        if(!data.equalsIgnoreCase("FAILED")){
            JSONObject ss = new JSONObject(data);
            if(ss.has("access_token")){
                token = ss.getString("access_token");
            }
        }
        else{
            return null;
        }
        
        String uri = this.uri +"omcoreapis/1.0.2/mp/paymentstatus/"+paytoken;
        
        String apiUser = ""; 
        String apiPass = "";
        OmSystemConfig om_info = omSystemConfigResp.findByCode("100");
        apiUser = om_info.getApiUser().trim();
        apiPass = om_info.getApiPass().trim();
        
        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer "+token);
        headers.add("X-AUTH-TOKEN", TextCodec.BASE64.encode(apiUser + ":" + apiPass));
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        
        
       // RestTemplate restTemplate = new RestTemplate();
        RestTemplate restTemplate = null;
        try {
            restTemplate = getRestTemplate();
        } catch (KeyStoreException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        ResponseEntity<String> response = null;
        try{
            response = restTemplate.exchange(uri, HttpMethod.GET, entity,String.class);
        }
        catch(HttpStatusCodeException ex){
            System.out.println("Yankoo see this bro "+ ResponseEntity.status(ex.getRawStatusCode()).headers(ex.getResponseHeaders()).body(ex.getResponseBodyAsString()));
            
            
            result.put("success", "409");
            result.put("message","Phone Number has insufficient balance to perform operation");
            return result;
            
        }

        System.out.println("This is the status " + response.getBody());

        if (response.getStatusCodeValue() == 200) {
            JSONObject resp = new JSONObject(response.getBody());
            JSONObject store = resp.getJSONObject("data");
            
            
            if(store.get("status").toString().equalsIgnoreCase("SUCCESSFULL")){
                result.put("success", "01");
                result.put("trans_id",store.get("txnmode").toString());
                result.put("message",store.get("confirmtxnmessage").toString());
            }
            else if(store.get("status").toString().equalsIgnoreCase("PENDING")){
                result.put("success", "1000");
                result.put("trans_id",store.get("txnmode").toString());
                result.put("message",store.get("confirmtxnmessage").toString());
            }
            else{
                result.put("success", "100");
                result.put("trans_id", store.get("txnmode").toString());
                result.put("message",store.get("inittxnmessage").toString());
            }
            
            return result;
        } else if (response.getStatusCodeValue() == 401) {
            result.put("success", "100");
            result.put("message","token error:unauthorized");
            return result;
        } else if (response.getStatusCodeValue() == 500) {
            result.put("success", "100");
            result.put("message",response.getBody());
            return result;//"internal server error";
        } else {
            result.put("success", "100");
            result.put("message",response.getBody());
            return result;//result;
        }

    }

    
   public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {


        
//        String username = "iwomi";
//        String password = "";
//        final String proxyUrl = "10.100.18.1";
//        final int port = 3128;
        
        Pwd pwd = pwdRepository.findByAcscd("0010","0");
        String username = pwd.getLogin();
        String proxyUrl =  pwd.getLib1();
        int port = Integer.parseInt(pwd.getLib2());
        String password = "";
        
        try{
            byte[] decoder = Base64.getDecoder().decode(trim(pwd.getPass()));
            String v = new String(decoder);

            password = trim(v);
        }
        catch(Exception e){
            
        }
        

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials( 
            new AuthScope(proxyUrl, port), 
            new UsernamePasswordCredentials(username, password)
        );

        HttpHost myProxy = new HttpHost(proxyUrl, port);
        
                
        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        };
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).setProxy(myProxy).build();
        
        
        
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        //RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
        
        
    }

    /*
    
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
        //RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
        
        
    }
    
*/


}
