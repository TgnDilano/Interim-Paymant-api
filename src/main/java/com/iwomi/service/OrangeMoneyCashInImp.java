/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.service;

import com.iwomi.model.OmSystemConfig;
import com.iwomi.model.Pwd;
import com.iwomi.repository.OmSystemConfigResp;
import com.iwomi.repository.PwdRepository;
import com.iwomi.repository.TransHisRepository;
import io.jsonwebtoken.impl.TextCodec;
import java.util.HashMap;
import java.util.Map;
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

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
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
public class OrangeMoneyCashInImp {
    
    @Autowired
    OmSystemConfigResp omSystemConfigResp;
    
    @Autowired
    TransHisRepository transactionHistroy;
    
    @Autowired
    PwdRepository pwdRepository;
    
    String api_key, api_secret;
    
    //http://api-s1.orange.cm/omcoreapis/1.0.2
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

    public String initOperationCashIn() {
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
        //System.out.println("yann see this token is successfull "+token);

        String uri = this.uri +"omcoreapis/1.0.2/cashin/init";
        //String uri = "https://apiw.orange.cm/omcoreapis/1.0.2/cashin/init";
        
        String request = "";
        String apiUser = ""; 
        String apiPass = "";
        OmSystemConfig om_info = omSystemConfigResp.findByCode("100");
        apiUser = om_info.getApiUser();
        apiPass = om_info.getApiPass();
        
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

        //System.out.println("This is the status " + response.getStatusCodeValue() +"and the response body is "+response.getBody());

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

    public Map<String, String> cashIn(String amount, String tel, String pnbr, String description) {
        
        Map<String, String> result = new HashMap();
        
        
        String channelUserMsisdn = "";
        String pin = "";
        String token = "";
        String paytoken = "";
        String data = getToken();
        String data2 = initOperationCashIn();
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

        String uri = this.uri +"omcoreapis/1.0.2/cashin/pay";
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
        
        //System.out.println(new JSONObject(headers).toString());
        //System.out.println(new JSONObject(request).toString());
        
        
        //RestTemplate restTemplate = new RestTemplate();
        RestTemplate restTemplate = null;
        try {
            restTemplate = getRestTemplate();
        } catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException ex) {
            
            Logger.getLogger(OrangeMoneyCashOut.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        ResponseEntity<String> response = null;
        try{
            response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);//.postForEntity(uri, entity, String.class);
        }
        catch(HttpStatusCodeException  ex){
            System.out.println("Yankoo see this bro "+ ResponseEntity.status(ex.getRawStatusCode()).headers(ex.getResponseHeaders()).body(ex.getResponseBodyAsString()));
        
            result.put("status", "417");
            result.put("message",ex.toString());
            return result;
            
        }
        
        
        System.out.println("This is the status " + response.getStatusCodeValue());

        if (response.getStatusCodeValue() == 200) {
            //result.put("trans_id", pnbr);
            JSONObject resp = new JSONObject(response.getBody());
            JSONObject store = resp.getJSONObject("data");
            //Date date = new Date();
            //TransactiomHistory s = new TransactiomHistory(store.get("amount").toString(), "xaf",store.get("txnmode").toString(),store.get("subscriberMsisdn").toString(),store.get("description").toString(),store.get("txnmessage").toString(),store.get("status").toString(),store.get("payToken").toString(),"Orange CashIn", "","",date.toString());
            //System.out.println(response.getBody());
            //transactionHistroy.save(s);
            
            if(store.get("status").toString().equalsIgnoreCase("SUCCESSFULL")){
                result.put("data", store.toString());
                result.put("status", "01");
                result.put("message",store.get("txnmessage").toString());
            }
            else{
                result.put("data", store.toString());
                result.put("status", "100");
                result.put("message",store.get("txnmessage").toString());
            }
            
            return result;
        } else if (response.getStatusCodeValue() == 500) {
            result.put("status", "500");
            result.put("message","An internal processing error occured");
            return result;//"internal server error";
        }
        else if (response.getStatusCodeValue() == 417) {
            result.put("status", "417");
            result.put("message","Please report this status to IWOMIPAY Admin");
            return result;
        }else {
            result.put("status", "500");
            result.put("message","Service temporary unavailable, try again later");
            return result;//result;
        }

    }

    
    public Map<String, String> getStatusCahIn(String paytoken) {
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
        
        String uri = this.uri +"omcoreapis/1.0.2/cashin/paymentstatus/"+paytoken;
        
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
        
        //ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity,String.class);
        
        
        ResponseEntity<String> response = null;
        try{
            response = restTemplate.exchange(uri, HttpMethod.GET, entity,String.class);
        }
        catch(HttpStatusCodeException ex){
            System.out.println("Yankoo see this bro "+ ResponseEntity.status(ex.getRawStatusCode()).headers(ex.getResponseHeaders()).body(ex.getResponseBodyAsString()));
            
            
            result.put("status", "409");
            result.put("message","Phone Number has insufficient balance to perform operation");
            return result;
            
        }

        System.out.println("This is the status " + response.getStatusCodeValue());

        if (response.getStatusCodeValue() == 200) {
            JSONObject resp = new JSONObject(response.getBody());
            JSONObject store = resp.getJSONObject("data");
            
            
            if(store.get("status").toString().equalsIgnoreCase("SUCCESSFULL")){
                result.put("status", "01");
                result.put("trans_id",store.get("txnmode").toString());
                result.put("message",store.get("txnmessage").toString());
            }
            else{
                result.put("status", "100");
                result.put("trans_id", store.get("txnmode").toString());
                result.put("message",store.get("txnmessage").toString());
            }
            
            return result;
        } else if (response.getStatusCodeValue() == 401) {
            result.put("status", "100");
            result.put("message","token error:unauthorized");
            return result;
        } else if (response.getStatusCodeValue() == 500) {
            result.put("status", "100");
            result.put("message",response.getBody());
            return result;//"internal server error";
        } else {
            result.put("status", "100");
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
