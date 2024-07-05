/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.service;

import com.iwomi.maviance.ApiException;
import com.iwomi.maviance.HMACSignature;
import com.iwomi.maviance.Pair;
import com.iwomi.maviance.StringUtil;

import com.iwomi.model.PaymentObjectAPI;
import com.iwomi.repository.BicecTransDetialsResp;
import com.iwomi.repository.NomenclatureRepository;
// import com.iwomi.model.MavianceAccessConfig;
// import com.iwomi.repository.partner.MavConfigRepository;

import com.iwomi.model.Nomenclature;
import com.iwomi.repository.MavConfigRepository;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author HP
 */


@Service
public class MavianceService {
    private String separator = "  --  ";
    private String basePath1 = ""; // API base Url
    private String accessToken1 = ""; // User access token
    private String accessSecret1 = ""; // User access secret.
    private String phonec1 = ""; // User access secret.
    @Autowired
    BicecTransDetialsResp bicecTransDetialsResp;
    @Autowired

    NomenclatureRepository nomenclatureRepository;
    //MavConfigRepository mavConfigRepository;

    MavConfigRepository mavConfigRepository;


     
    
    @PostConstruct
    public void MavianceService1 (){


       String basePath1 = "https://s3p.smobilpay.staging.maviance.info/v2"; //test environment
        String accessToken1 = "DBC9B4B6-51D3-7B06-4FDA-834A51EC22E3";
        String accessSecret1 = "A46A6042-6B34-4129-082F-8BBBDC79540C";
        String phonec1 = "237658868721";
       
        Nomenclature n = nomenclatureRepository.findTabcdAndAcsd("6005","0001","0");
        if(n!=null){
                basePath1 = n.getLib1(); //test environment
                accessToken1 = n.getLib2();
                accessSecret1 = n.getLib3();
                phonec1= n.getLib4();
            }
            this.basePath1 = basePath1;
            this.accessToken1 = accessToken1;
            this.accessSecret1 = accessSecret1;
            this.phonec1 = phonec1;
    }
     public JSONObject getmaviance(){//lib1 is paymeth, lib3 =age. lib4 cpt, taux=backcde
        JSONObject Credit = new JSONObject();
        String basePath = "https://s3pv2cm.smobilpay.com/v2"; //test environment
        String accessToken = "6E9E7220-FA06-622E-E72E-9896CD6AAE45";
        String accessSecret = "230AD300-091F-C309-BA87-9D9337D20723";
        String phonec = "237658868721";
       
        Nomenclature n = nomenclatureRepository.findTabcdAndAcsd("6005","0001","0");
        if(n!=null){
                basePath = n.getLib1(); //test environment
                accessToken= n.getLib2();
                accessSecret = n.getLib3();
                phonec= n.getLib4();
            }
        Credit.put("basePath", basePath) ;
         Credit.put("accessToken", accessToken) ;
         Credit.put("accessSecret", accessSecret) ;
         Credit.put("phonec", phonec) ;
        return Credit;
    }
 
    
     public Map<String, Object> ping1() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException{
     //  JSONObject js=getmaviance("6005","0001", "0");
        Map<String, Object> result = new HashMap();
       // String url = basePath+"/ping";
       JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
        String url = js.getString("basePath")+"/ping";
         System.out.println("yvoT see this "+url);
        
        String request = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String path = "/ping";
        String method = "GET";
        ArrayList<Pair> data = new ArrayList<Pair>();
        String head = buildAuthorizationHeader(path, method, data);
        System.out.println(head);
        //head = "s3pAuth s3pAuth_nonce=\"50970315401\", s3pAuth_signature=\"iHvwmW576Y5pZwvwaWxVEL5+NPc=\", s3pAuth_signature_method=\"HMAC-SHA1\", s3pAuth_timestamp=\"1611921331\", s3pAuth_token=\"B14888BD-0064-1AB9-A4F4-13179318B0DC\"";
        headers.add("Authorization", head);
        headers.add("xApiVersion", "3.0.0");
        HttpEntity<String> entity = new HttpEntity<>(request, headers);
        //RestTemplate restTemplate = new RestTemplate();
        RestTemplate restTemplate = getRestTemplate();
        try{
            //ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity,String.class);
            //ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class,entity);
            
            System.out.println("yann see this "+response.getStatusCodeValue());
            if (response.getStatusCodeValue() == 200) {
                System.out.println("yann see this "+response.getBody());
                JSONObject res = new JSONObject(response.getBody());
                if(res.has("key")){
                    return res.toMap();
                }
                else{
                    result.put("status", "500");
                    result.put("message", "Service currently not available");
                    return result;
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.toString());
            System.out.println(ex.getMessage());
        }
        return null;


    }
    
    
        
     public Map<String, Object> account() throws NoSuchAlgorithmException{
       
        Map<String, Object> result = new HashMap();
        try {
                  JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
        
            String url = basePath+"/account";
            
            Map<String, String> request = new HashMap();
            
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            String path = "/account";
            String method = "GET";
            ArrayList<Pair> data = new ArrayList<Pair>();
            
            headers.add("Authorization", buildAuthorizationHeader(path, method, data));
            headers.add("xApiVersion", "3.0.0");
            
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = getRestTemplate();
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity,String.class);
            
            
            System.out.println("yann see this "+response.getStatusCodeValue());
            
            
            if (response.getStatusCodeValue() == 200) {
                System.out.println("yann see this "+response.getBody());
                JSONObject res = new JSONObject(response.getBody());
                return res.toMap();
            }
            
        } catch (KeyStoreException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;


    }
     
     
    public List<Object> merchant() throws NoSuchAlgorithmException{

       Map<String, Object> result = new HashMap();
       try {
                 JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
           String url = basePath +"/merchant";

           Map<String, String> request = new HashMap();


           HttpHeaders headers = new HttpHeaders();
           headers.setContentType(MediaType.APPLICATION_JSON);

           String path = "/merchant";
           String method = "GET";
           ArrayList<Pair> data = new ArrayList<Pair>();

           headers.add("Authorization", buildAuthorizationHeader(path, method, data));
           headers.add("xApiVersion", "3.0.0");

           HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

           //RestTemplate restTemplate = new RestTemplate();
           RestTemplate restTemplate = getRestTemplate();

           ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity,String.class);


           System.out.println("yann see this "+response.getStatusCodeValue());


           if (response.getStatusCodeValue() == 200) {
               System.out.println("yann see this "+response.getBody());
               JSONArray res = new JSONArray(response.getBody());
               return res.toList();
           }

       } catch (KeyStoreException ex) {
           System.out.println(ex.getMessage());
           Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
       } catch (KeyManagementException ex) {
           System.out.println(ex.getMessage());
           Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
       }
       return null;


   }

    
    public List<Object> service() throws NoSuchAlgorithmException{

       Map<String, Object> result = new HashMap();
       try {
                 JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
           String url = basePath+ "/service";

           Map<String, String> request = new HashMap();


           HttpHeaders headers = new HttpHeaders();
           headers.setContentType(MediaType.APPLICATION_JSON);

           String path = "/service";
           String method = "GET";
           ArrayList<Pair> data = new ArrayList<Pair>();

           headers.add("Authorization", buildAuthorizationHeader(path, method, data));
           headers.add("xApiVersion", "3.0.0");

           HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

           //RestTemplate restTemplate = new RestTemplate();
           RestTemplate restTemplate = getRestTemplate();

           ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity,String.class);


           System.out.println("yann see this "+response.getStatusCodeValue());


           if (response.getStatusCodeValue() == 200) {
               System.out.println("yann see this "+response.getBody());
               JSONArray res = new JSONArray(response.getBody());
               return res.toList();
           }

       } catch (KeyStoreException ex) {
           System.out.println(ex.getMessage());
           Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
       } catch (KeyManagementException ex) {
           System.out.println(ex.getMessage());
           Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
       }
       return null;


   }

    public JSONArray product() throws NoSuchAlgorithmException{

       Map<String, Object> result = new HashMap();
       try {
                 JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
           String url = basePath+ "/product";

           Map<String, String> request = new HashMap();


           HttpHeaders headers = new HttpHeaders();
           headers.setContentType(MediaType.APPLICATION_JSON);

           String path = "/product";
           String method = "GET";
           ArrayList<Pair> data = new ArrayList<Pair>();

           headers.add("Authorization", buildAuthorizationHeader(path, method, data));
           headers.add("xApiVersion", "3.0.0");

           HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

           //RestTemplate restTemplate = new RestTemplate();
           RestTemplate restTemplate = getRestTemplate();

           ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity,String.class);


           System.out.println("yann see this "+response.getStatusCodeValue());


           if (response.getStatusCodeValue() == 200) {
               System.out.println("yann see this "+response.getBody());
               JSONArray res = new JSONArray(response.getBody());
               return res;
           }

       } catch (KeyStoreException ex) {
           System.out.println(ex.getMessage());
           Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
       } catch (KeyManagementException ex) {
           System.out.println(ex.getMessage());
           Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
       }
       return null;


   }

    

   public JSONArray subscription() throws NoSuchAlgorithmException{

       Map<String, Object> result = new HashMap();
       try {
                 JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
           String url = basePath+ "/subscription";

           Map<String, String> request = new HashMap();


           HttpHeaders headers = new HttpHeaders();
           headers.setContentType(MediaType.APPLICATION_JSON);

           String path = "/subscription";
           String method = "GET";
           ArrayList<Pair> data = new ArrayList<Pair>();
           

           headers.add("Authorization", buildAuthorizationHeader(path, method, data));
           headers.add("xApiVersion", "3.0.0");

           HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

           //RestTemplate restTemplate = new RestTemplate();
           RestTemplate restTemplate = getRestTemplate();

           ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity,String.class);


           System.out.println("yann see this "+response.getStatusCodeValue());


           if (response.getStatusCodeValue() == 200) {
               System.out.println("yann see this "+response.getBody());
               JSONArray res = new JSONArray(response.getBody());
               return res;
           }

       } catch (KeyStoreException ex) {
           System.out.println(ex.getMessage());
           Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
       } catch (KeyManagementException ex) {
           System.out.println(ex.getMessage());
           Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
       }
       return null;


   }

    
    public Map<String, Object> unique_service(String service_id) throws NoSuchAlgorithmException{
        
        Map<String, Object> result = new HashMap();
        try {
                  JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
            String url = basePath+"/service/"+service_id;
            
            Map<String, String> request = new HashMap();
            
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            String path = "/service/"+service_id;
            String method = "GET";
            ArrayList<Pair> data = new ArrayList<Pair>();
            
            headers.add("Authorization", buildAuthorizationHeader(path, method, data));
            headers.add("xApiVersion", "3.0.0");
            
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = getRestTemplate();
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity,String.class);
            
            
            System.out.println("yann see this "+response.getStatusCodeValue());
            
            
            if (response.getStatusCodeValue() == 200) {
                System.out.println("yann see this "+response.getBody());
                JSONObject res = new JSONObject(response.getBody());
                return res.toMap();
            }
            
        } catch (KeyStoreException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;


    }
     
    
    public List<Object> SEARCHABLE_BILL(String merchant, int service_id, String service_number, String endpoint) throws NoSuchAlgorithmException{

        Map<String, Object> result = new HashMap();
        try {
            
        JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
            String url = basePath+ "/"+endpoint;
            Map<String, String> request = new HashMap();

            HttpHeaders headers = new HttpHeaders();
            
            String path = "/"+endpoint;
            String method = "GET";
            ArrayList<Pair> data = new ArrayList<Pair>();
            data.add(new Pair("serviceid", String.valueOf(service_id)));
            headers.add("xApiVersion", "3.0.0");
            UriComponentsBuilder builder = null;
            if(endpoint.equalsIgnoreCase("bill")){
                data.add(new Pair("merchant", merchant));
                data.add(new Pair("serviceNumber", service_number));
                 builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("merchant", merchant)
                .queryParam("serviceid", service_id)
                .queryParam("serviceNumber", service_number);
            } else  if(endpoint.equalsIgnoreCase("subcription")){
                data.add(new Pair("merchant", merchant));
                data.add(new Pair("serviceNumber", service_number));
                data.add(new Pair("customerNumber", service_number));
                 builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("merchant", merchant)
                .queryParam("serviceid", service_id)
                .queryParam("customerNumber", service_number);
            } else {
              builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("serviceid", service_id);
            }
            headers.add("Authorization", buildAuthorizationHeader(path, method, data));
            HttpEntity<?> entity = new HttpEntity<>(headers);
          //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<String> response = null;
            try{
                response = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,entity,String.class);
            }
            catch (Exception ex){
                return null;  
            }
            System.out.println("yann see this "+response.getBody());
            
            
            if (response.getStatusCodeValue() == 200) {
                System.out.println("yann see this "+response.getBody());
                JSONArray res = new JSONArray(response.getBody());
                return res.toList();
            }
            
        } catch (KeyStoreException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;


    }
    
    
    public List<Object> NONE_SEARCHABLE_BILL(String merchant,int service_id, String service_number) throws NoSuchAlgorithmException{

        
        Map<String, Object> result = new HashMap();
        try {
                  JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
            String url = basePath+"/bill";
            
            Map<String, String> request = new HashMap();
            
            
            HttpHeaders headers = new HttpHeaders();
            
            String path = "/bill";
            String method = "GET";
            ArrayList<Pair> data = new ArrayList<Pair>();
            data.add(new Pair("merchant", merchant));
            data.add(new Pair("serviceid", String.valueOf(service_id)));
            data.add(new Pair("serviceNumber", service_number));
            
            headers.add("Authorization", buildAuthorizationHeader(path, method, data));
            headers.add("xApiVersion", "3.0.0");
            
            
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("merchant", merchant)
                .queryParam("serviceid", service_id)
                .queryParam("serviceNumber", service_number);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = getRestTemplate();
            
            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,entity,String.class);
            
            
            System.out.println("yann see this "+response.getBody());
            
            
            if (response.getStatusCodeValue() == 200) {
                System.out.println("yann see this "+response.getBody());
                JSONArray res = new JSONArray(response.getBody());
                return res.toList();
            }
            
        } catch (KeyStoreException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;


    }
    
    
    public JSONArray topup(String merchant,String service_id ) throws NoSuchAlgorithmException{

        
        Map<String, Object> result = new HashMap();
        try {
                  JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
            String url = basePath+"/topup";
            
            Map<String, String> request = new HashMap();
            
            
            HttpHeaders headers = new HttpHeaders();
            
            String path = "/topup";
            String method = "GET";
            ArrayList<Pair> data = new ArrayList<Pair>();
            data.add(new Pair("merchant", merchant));
            data.add(new Pair("serviceid", service_id));
            
            headers.add("Authorization", buildAuthorizationHeader(path, method, data));
            headers.add("xApiVersion", "3.0.0");
            
            
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("merchant", merchant)
                .queryParam("serviceid", service_id);
                //.queryParam("serviceNumber", service_number);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = getRestTemplate();
            
            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,entity,String.class);
            
            
            System.out.println("yann see this1 "+response.getBody());
            
            
            if (response.getStatusCodeValue() == 200) {
                System.out.println("yann see this2 "+response.getBody());
                JSONArray res = new JSONArray(response.getBody());
                return res;
            }
            
        } catch (KeyStoreException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;


    }
    
    
    public JSONArray subscriptionv3(String merchant, String customerNumber, int serviceid ) throws NoSuchAlgorithmException, ApiException{

        Map<String, Object> result = new HashMap();
        try {
                  JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
            String url = basePath+"/subscription";
            
            Map<String, Object> request = new HashMap();
            
            
            HttpHeaders headers = new HttpHeaders();
            
            String path = "/subscription";
            String method = "GET";
            ArrayList<Pair> data = new ArrayList<Pair>();
            data.add(new Pair("serviceid", String.valueOf(serviceid)));
            data.add(new Pair("merchant", merchant));
            data.add(new Pair("customerNumber", customerNumber));
            
           
            
            //data.addAll(bodyConverter(request));
            System.out.println(path);
            System.out.println(method);
            System.out.println(data);
            headers.add("Authorization", buildAuthorizationHeader(path, method, data));
            headers.add("xApiVersion", "3.0.0");
            headers.setContentType(MediaType.APPLICATION_JSON);

             UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("merchant", merchant)
                .queryParam("serviceid", serviceid)
                .queryParam("customerNumber", customerNumber);
            

            HttpEntity<Map<String, Object> > entity = new HttpEntity<>(request, headers);

            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = getRestTemplate();
            
          //  ResponseEntity<String> response = null;
            //ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(url, entity, String.class); // yan commented
            
            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,entity,String.class);
        
        
        System.out.println("yann see this "+response.getBody());
        
        if (response.getStatusCodeValue() == 200) {
            System.out.println("yann see this "+response.getBody());
            JSONArray res = new JSONArray(response.getBody());
            return res;
        }
      /*  if (response.getStatusCodeValue() == 200) {
            System.out.println("yann see this "+response.getBody());
            JSONObject res = new JSONObject(response.getBody());
            return res.toMap();
        }*/

            // try{
            //     response = restTemplate.exchange(url, HttpMethod.GET, entity,String.class);
            //     System.out.println("yann see this "+response.getStatusCodeValue());
            //     System.out.println("yann see this origin"+response.getHeaders().getOrigin());
            // }
            // catch (Exception ex){
            //     return null;
                
            // }
            
            // System.out.println("yann see this from quotestd "+response.getBody());
            
            
            // if (response.getStatusCodeValue() == 200) {
            //     System.out.println("yann see this "+response.getBody());
            //     JSONObject res = new JSONObject(response.getBody());
            //     return res.toMap();
            // }



            
        } catch (KeyStoreException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;


    }

  
   public JSONArray subscriptionv2(String merchant, String customerNumber, int serviceid) throws NoSuchAlgorithmException{
        
    Map<String, Object> result = new HashMap();
          JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
        String url = basePath+"/subscription";
         System.out.println("yvoT Subcrip see this "+url);
        String path = "/subscription";
        String method = "GET";
        ArrayList<Pair> data = new ArrayList<Pair>();
        data.add(new Pair("customerNumber", customerNumber));
        data.add(new Pair("merchant", merchant));
        data.add(new Pair("serviceid", String.valueOf(serviceid)));
             Unirest.config().verifySsl(false);
             HttpResponse<String>response = null; 
           
                response = Unirest.get(url)
                .header("Authorization", buildAuthorizationHeader(path, method, data))
                .header("xApiVersion", "3.0.0")
                .header("Content-Type", "application/json")
                .queryString("customerNumber", customerNumber)
                .queryString("merchant", merchant)
                .queryString("serviceid", serviceid)
                .asString();
                System.out.println("yann see this "+response.getStatus());
                System.out.println("yann see this origin"+response.getHeaders());
            System.out.println("yann see this subscriptionv2 Unirest:"+response.getBody());
         if (response.getStatus() == 200) {
            System.out.println("yann see this "+response.getBody());
            JSONArray res = new JSONArray(response.getBody());
            return res;
        }
    return null;


}

    
    
    
    
    
    public Map<String, Object> quotestd(int amount,String payItemId ) throws NoSuchAlgorithmException, ApiException{

        Map<String, Object> result = new HashMap();
        try {
                  JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
            String url = basePath+"/quotestd";
             System.out.println("yvoTquotes see this "+url);
            Map<String, Object> request = new HashMap();
            
            
            HttpHeaders headers = new HttpHeaders();
            
            String path = "/quotestd";
            String method = "POST";
            ArrayList<Pair> data = new ArrayList<Pair>();
            data.add(new Pair("amount", String.valueOf(amount)));
            data.add(new Pair("payItemId", payItemId));
            
            request.put("amount",amount);
            request.put("payItemId",payItemId);
            
            //data.addAll(bodyConverter(request));
            System.out.println(path);
            System.out.println(method);
            System.out.println(data);
            headers.add("Authorization", buildAuthorizationHeader(path, method, data));
            headers.add("xApiVersion", "3.0.0");
            headers.setContentType(MediaType.APPLICATION_JSON);
            

            HttpEntity<Map<String, Object> > entity = new HttpEntity<>(request, headers);

            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = getRestTemplate();
            
            ResponseEntity<String> response = null;
            //ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(url, entity, String.class);
            try{
                response = restTemplate.exchange(url, HttpMethod.POST, entity,String.class);
                System.out.println("yann see this "+response.getStatusCodeValue());
                System.out.println("yann see this origin"+response.getHeaders().getOrigin());
            }
            catch (Exception ex){
                return null;
                
            }
            
            System.out.println("yann see this from quotestd "+response.getBody());
            
            
            if (response.getStatusCodeValue() == 200) {
                System.out.println("yann see this "+response.getBody());
                JSONObject res = new JSONObject(response.getBody());
                return res.toMap();
            }
            
        } catch (KeyStoreException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;


    }
    public Map<String, Object> quotestdv2(int amount,String payItemId ) throws NoSuchAlgorithmException, ApiException{
         System.out.println("yvon see this: quotestdv2 "+payItemId);
               JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
        Map<String, Object> result = new HashMap();
       String url = basePath+"/quotestd";
            
            Map<String, Object> request = new HashMap();
            String path = "/quotestd";
            String method = "POST";
            ArrayList<Pair> data = new ArrayList<Pair>();
            data.add(new Pair("amount", String.valueOf(amount)));
            data.add(new Pair("payItemId", payItemId));
            
            request.put("amount",amount);
            request.put("payItemId",payItemId);
            System.out.println(path);
            System.out.println(method);
            System.out.println(data);
             Unirest.config().verifySsl(false);
             HttpResponse<String>response = null; 
             try{
                response = Unirest.post(url)
                .header("Authorization", buildAuthorizationHeader(path, method, data))
                .header("xApiVersion", "3.0.0")
                .header("Content-Type", "application/json")
                //.headers(r)
                .body(request)
                .asString();
                System.out.println("yann see this "+response.getStatus());
                System.out.println("yann see this origin"+response.getHeaders());
            }
            catch (Exception ex){
                return null;    
            }
//            
            System.out.println("yann see this from quotestd v2 "+response.getBody());
          if (response.getStatus() == 200) {
                System.out.println("yann see this "+response.getBody());
                JSONObject res = new JSONObject(response.getBody());
                return res.toMap();
            }
        return null;


    }
    
    public Map<String, Object> quotestdv2F(int amount,String payItemId ) throws NoSuchAlgorithmException, ApiException{
         System.out.println("yvon see this: quotestdv2 "+payItemId);
               JSONObject js=getmaviance();
                   try{
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
        Map<String, Object> result = new HashMap();
       String url = basePath+"/quotestd";
            
            Map<String, Object> request = new HashMap();
            HttpHeaders headers = new HttpHeaders();
            String path = "/quotestd";
            String method = "POST";
            ArrayList<Pair> data = new ArrayList<Pair>();
            data.add(new Pair("amount", String.valueOf(amount)));
            data.add(new Pair("payItemId", payItemId));
            
            request.put("amount",amount);
            request.put("payItemId",payItemId);
            System.out.println(path);
            System.out.println(method);
            System.out.println(data);
            headers.add("Authorization", buildAuthorizationHeader(path, method, data));
            headers.add("xApiVersion", "3.0.0");
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object> > entity = new HttpEntity<>(request, headers);
    
            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<String> response = null;
            // Unirest.config().verifySsl(false);
            // HttpResponse<String>response = null; 
         
                  response = restTemplate.exchange(url, HttpMethod.POST, entity,String.class);
//                response = Unirest.post(url)
//                .header("Authorization", buildAuthorizationHeader(path, method, data))
//                .header("xApiVersion", "3.0.0")
//                .header("Content-Type", "application/json")
//                //.headers(r)
//                .body(request)
//                .asString();
                System.out.println("yann see this "+response.getStatusCode());
                System.out.println("yann see this origin"+response.getHeaders());
                 System.out.println("yann see this from quotestd v2 "+response.getBody());
                if (response.getStatusCodeValue() == 200) {
                      System.out.println("yann see this "+response.getBody());
                      JSONObject res = new JSONObject(response.getBody());
                      return res.toMap();
                  }
            }
            catch (Exception ex){
                return null;    
            }
//            
           
        return null;


    }
    
    
    public Map<String, Object> collectstd(String quoteId,String customerPhonenumber,String customerEmailaddress,String customerName,/*String customerAddress,String customerNumber,*/String serviceNumber,String trid) throws NoSuchAlgorithmException, ApiException{
        
        Map<String, Object> result = new HashMap();
        try {
                  JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
            String url = basePath+"/collectstd";
             System.out.println("yvoT collectsee this "+url);
            Map<String, Object> request = new HashMap();
            
            
            HttpHeaders headers = new HttpHeaders();
            
            String path = "/collectstd";
            String method = "POST";
            ArrayList<Pair> data = new ArrayList<Pair>();
            data.add(new Pair("quoteId", quoteId));
            data.add(new Pair("customerPhonenumber", customerPhonenumber));
            data.add(new Pair("customerEmailaddress", customerEmailaddress));
            data.add(new Pair("customerName", customerName));
            data.add(new Pair("customerAddress", "Cameroon"));
            data.add(new Pair("customerNumber", customerPhonenumber));
            data.add(new Pair("serviceNumber", serviceNumber));
            data.add(new Pair("trid", trid));
            
            request.put("quoteId", quoteId);
            request.put("customerPhonenumber",customerPhonenumber);
            request.put("customerEmailaddress",customerEmailaddress);
            request.put("customerName", customerName);
            request.put("customerAddress", "Cameroon");
            request.put("customerNumber", customerPhonenumber);
            request.put("serviceNumber", serviceNumber);
            request.put("trid", trid);
            
            
            headers.add("Authorization", buildAuthorizationHeader(path, method, data));
            headers.add("xApiVersion", "3.0.0");
            headers.setContentType(MediaType.APPLICATION_JSON);
            

            HttpEntity<Map<String, Object> > entity = new HttpEntity<>(request, headers);

            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = getRestTemplate();
            
            //ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity,String.class);
            //ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(url, entity, String.class);
            
            ResponseEntity<String> response = null;
            //ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(url, entity, String.class);
            try{
                response = restTemplate.exchange(url, HttpMethod.POST, entity,String.class);
                
            }
            catch (Exception ex){
                System.out.println("yanick see this "+ex.getMessage());
                return null;
                
            }
            
            System.out.println("yann see this "+response.getBody());
            System.out.println("yann see this "+response.getStatusCodeValue());
            
            
            if (response.getStatusCodeValue() == 200) {
                System.out.println("yann see this "+response.getBody());
                JSONObject res = new JSONObject(response.getBody());
                return res.toMap();
            }  else if (response.getStatusCodeValue() == 400) {
                System.out.println("yann see this "+response.getBody());
                JSONObject res = new JSONObject(response.getBody());
                res.put("expire", response.getStatusCodeValue());
                return res.toMap();
            } 
            
        } catch (KeyStoreException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;


    }
   
    
    public JSONObject collectstdFF(String quoteId,String customerPhonenumber,String customerEmailaddress,
            String customerName,String customerAddress,String customerNumber,String trid) throws NoSuchAlgorithmException, ApiException{
        
        Map<String, Object> result = new HashMap();
        try {
                  JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
            String url = basePath+"/collectstd";
             System.out.println("yvoTvvvccc see this "+url);
            Map<String, Object> request = new HashMap();
            HttpHeaders headers = new HttpHeaders();
           System.out.println("yvokkk customerPhonenumber: "+phonec);
            String path = "/collectstd";
            String method = "POST";
            String customerPhonenumber1=phonec;
            String motif="ko";
            String sabc="5000";
            PaymentObjectAPI pa= bicecTransDetialsResp.findAllByPnbr(trid);
            Nomenclature nm =nomenclatureRepository.findUrl1("0012","0124","0");
            if(nm!=null){
                sabc=nm.getLib2();
            }
            if(pa!=null){
                motif=pa.getMotif();
            }

            ArrayList<Pair> data = new ArrayList<Pair>();
            data.add(new Pair("quoteId", quoteId));
            data.add(new Pair("customerPhonenumber", customerPhonenumber1));
            data.add(new Pair("customerEmailaddress", customerEmailaddress));
            data.add(new Pair("customerName", customerName));
            data.add(new Pair("customerAddress", customerAddress));
            data.add(new Pair("trid", trid));
            if(motif.equalsIgnoreCase(sabc)){//verification si c'est un paiement SABC
                data.add(new Pair("customerNumber", customerNumber));
                data.add(new Pair("serviceNumber", ""));
                request.put("customerNumber", customerNumber);
                request.put("serviceNumber", "");
            } else{
                data.add(new Pair("customerNumber", ""));
                data.add(new Pair("serviceNumber", customerNumber));
                request.put("serviceNumber", customerNumber);
                request.put("customerNumber", "");
            }
          
            request.put("quoteId", quoteId);
            request.put("customerPhonenumber",customerPhonenumber1);
            request.put("customerEmailaddress",customerEmailaddress);
            request.put("customerName", customerName);
            request.put("customerAddress", customerAddress);
            request.put("trid", trid);
            
            
            headers.add("Authorization", buildAuthorizationHeader(path, method, data));
            headers.add("xApiVersion", "3.0.0");
            headers.setContentType(MediaType.APPLICATION_JSON);
            

            HttpEntity<Map<String, Object> > entity = new HttpEntity<>(request, headers);


            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = getRestTemplate();

            //ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity,String.class);
            //ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(url, entity, String.class);

            ResponseEntity<String> response = null;
            //ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(url, entity, String.class);
            try{
                response = restTemplate.postForEntity(url,  entity,String.class);

            }
            catch (Exception ex){
                System.out.println("yvotyp0 see this "+ex.toString());
                return null;

            }

            System.out.println("yvotyv1 see this "+response.getBody());
            System.out.println("yvotyp2 see this "+response.getStatusCodeValue());
            
            
            if (response.getStatusCodeValue() == 200) {
                System.out.println("yvotyp3 see this "+response.getBody());
                JSONObject res = new JSONObject(response.getBody());
                return res;
            }  else if (response.getStatusCodeValue() == 400) {
                System.out.println("yvotyp4 see this "+response.getBody());
                JSONObject res = new JSONObject(response.getBody());
                res.put("expire", response.getStatusCodeValue());
                return res;
            } 
            
        } catch (KeyStoreException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println("yvotyp5 see this "+ex.toString());
                JSONObject res = new JSONObject(ex.toString());
            //  res.put("expire", response.getStatusCodeValue());
                return res;
        }
        return null;


    }
   
    
    public Map<String, Object> collectstdF(String quoteId,String customerPhonenumber,String customerEmailaddress,
            String customerName,String customerAddress,String customerNumber,String trid) throws NoSuchAlgorithmException, ApiException{
    Map<String, Object> result = new HashMap();
       // try {
             JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
            String url = basePath+"/collectstd";
            Map<String, Object> request = new HashMap();
            HttpHeaders headers = new HttpHeaders();
            String path = "/collectstd";
            String method = "POST";
            ArrayList<Pair> data = new ArrayList<Pair>();
            data.add(new Pair("quoteId", quoteId));
            data.add(new Pair("customerPhonenumber", customerPhonenumber));
            data.add(new Pair("customerEmailaddress", customerEmailaddress));
            data.add(new Pair("customerName", customerName));
            data.add(new Pair("customerAddress", customerAddress));
            data.add(new Pair("customerNumber", customerNumber));
             data.add(new Pair("serviceNumber", customerNumber));
            data.add(new Pair("trid", trid));
            
            request.put("quoteId", quoteId);
            request.put("customerPhonenumber",customerPhonenumber);
            request.put("customerEmailaddress",customerEmailaddress);
            request.put("customerName", customerName);
            request.put("customerAddress", customerAddress);
            request.put("customerNumber", customerNumber);
            request.put("serviceNumber", customerNumber);
            request.put("trid", trid);
            Unirest.config().verifySsl(false);
             HttpResponse<String>response = null; 
             try{
                response = Unirest.post(url)
                .header("Authorization", buildAuthorizationHeader(path, method, data))
                .header("xApiVersion", "3.0.0")
                .header("Content-Type", "application/json")
                //.headers(r)
                .body(request)
                .asString();
                System.out.println("yann see this "+response.getStatus());
                System.out.println("yann see this origin"+response.getHeaders());
            }
            catch (Exception ex){
                return null;    
            }
            
            System.out.println("yann see this "+response.getBody());
            System.out.println("yann see this "+response.getStatus());
            
            
            if (response.getStatus() == 200) {
                System.out.println("yann see this collect final Unirest "+response.getBody());
                JSONObject res = new JSONObject(response.getBody());
                return res.toMap();
            }
        return null;


    }
    public JSONObject collectstdFJ(String quoteId,String customerPhonenumber,String customerEmailaddress,
            String customerName,String customerAddress,String customerNumber,String trid) throws NoSuchAlgorithmException, ApiException{
    Map<String, Object> result = new HashMap();
       // try {
             JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
            String url = basePath+"/collectstd";
             System.out.println("yvoT ccc see this "+url);
            Map<String, Object> request = new HashMap();
            HttpHeaders headers = new HttpHeaders();
            String path = "/collectstd";
            String method = "POST";
            ArrayList<Pair> data = new ArrayList<Pair>();
            data.add(new Pair("quoteId", quoteId));
            data.add(new Pair("customerPhonenumber", customerPhonenumber));
            data.add(new Pair("customerEmailaddress", customerEmailaddress));
            data.add(new Pair("customerName", customerName));
            data.add(new Pair("customerAddress", customerAddress));
            data.add(new Pair("customerNumber", customerNumber));
             data.add(new Pair("serviceNumber", customerNumber));
            data.add(new Pair("trid", trid));
            
            request.put("quoteId", quoteId);
            request.put("customerPhonenumber",customerPhonenumber);
            request.put("customerEmailaddress",customerEmailaddress);
            request.put("customerName", customerName);
            request.put("customerAddress", customerAddress);
            request.put("customerNumber", customerNumber);
            request.put("serviceNumber", customerNumber);
            request.put("trid", trid);
            Unirest.config().verifySsl(false);
             HttpResponse<String>response = null; 
             try{
                response = Unirest.post(url)
                .header("Authorization", buildAuthorizationHeader(path, method, data))
                .header("xApiVersion", "3.0.0")
                .header("Content-Type", "application/json")
                //.headers(r)
                .body(request)
                .asString();
                System.out.println("yann see this "+response.getStatus());
                System.out.println("yann see this origin"+response.getHeaders());
                JSONObject res = new JSONObject(response.getBody());
                return res;
            }
            catch (Exception ex){
                return null;    
            }


    }
   
    public Map<String, Object> collectstd_airtime(String quoteId,String serviceNumber,String trid) throws NoSuchAlgorithmException, ApiException{
        
        Map<String, Object> result = new HashMap();
        try {
            
                  JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
            String url = basePath+"/collectstd";
            
            Map<String, Object> request = new HashMap();
            
            
            HttpHeaders headers = new HttpHeaders();
            
            String path = "/collectstd";
            String method = "POST";
            ArrayList<Pair> data = new ArrayList<Pair>();
            data.add(new Pair("quoteId", quoteId));;
            data.add(new Pair("serviceNumber", serviceNumber));
            data.add(new Pair("trid", trid));
            
            request.put("quoteId", quoteId);
            request.put("serviceNumber", serviceNumber);
            request.put("trid", trid);
            
            
            headers.add("Authorization", buildAuthorizationHeader(path, method, data));
            headers.add("xApiVersion", "3.0.0");
            headers.setContentType(MediaType.APPLICATION_JSON);
            

            HttpEntity<Map<String, Object> > entity = new HttpEntity<>(request, headers);

            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = getRestTemplate();
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity,String.class);
            //ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(url, entity, String.class);
            
            
            System.out.println("yann see this "+response.getBody());
            
            
            if (response.getStatusCodeValue() == 200) {
                System.out.println("yann see this "+response.getBody());
                JSONObject res = new JSONObject(response.getBody());
                return res.toMap();
            }
            
        } catch (KeyStoreException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;


    }
   
    
    public JSONArray historystd(String ptn) throws NoSuchAlgorithmException{
        
        Map<String, Object> result = new HashMap();
        try {
                  JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
            
            String url = basePath+"/historystd";
            
            Map<String, String> request = new HashMap();
            
            
            HttpHeaders headers = new HttpHeaders();
            
            String path = "/historystd";
            String method = "GET";
            ArrayList<Pair> data = new ArrayList<Pair>();
            data.add(new Pair("ptn", ptn));
            
            headers.add("Authorization", buildAuthorizationHeader(path, method, data));
            headers.add("xApiVersion", "3.0.0");
            
            
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("ptn", ptn);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = getRestTemplate();
            
            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,entity,String.class);
            
            
            System.out.println("yann see this "+response.getBody());
            
            
            if (response.getStatusCodeValue() == 200) {
                System.out.println("yann see this "+response.getBody());
                JSONArray res = new JSONArray(response.getBody());
                return res;
            }
            
        } catch (KeyStoreException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;


    }
      //verify transaction
      public JSONArray verifytx(String trid) throws NoSuchAlgorithmException{
          System.out.println("yvo see debut veriftx "+trid);
                JSONObject js=getmaviance();
                try {
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
            String url = basePath+"/verifytx";
            
            Map<String, Object> request = new HashMap();
             HttpHeaders headers = new HttpHeaders();
             
            String path = "/verifytx";
            String method = "GET";
             ArrayList<Pair> data = new ArrayList<Pair>();
             data.add(new Pair("trid", trid));
             
             headers.add("Authorization", buildAuthorizationHeader(path, method, data));
            headers.add("xApiVersion", "3.0.0");
            headers.setContentType(MediaType.APPLICATION_JSON);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("trid", trid);
            
            // Unirest.config().verifySsl(false);
           //  HttpResponse<String>response = null; 
              HttpEntity<Map<String, Object> > entity = new HttpEntity<>(request, headers);

            
            //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = getRestTemplate();
            
          //  ResponseEntity<String> response = null;
            //ResponseEntity<Map<String, Object>> response = restTemplate.postForEntity(url, entity, String.class); // yan commented
            
            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,entity,String.class);
           
//                response = Unirest.get(url)
//                .header("Authorization", buildAuthorizationHeader(path, method, data))
//                .header("xApiVersion", "3.0.0")
//                .header("Content-Type", "application/json")
//                .queryString("trid", trid)
//                .asString();
            System.out.println("yvo see this verif Unrest "+response.getBody());
            if (response.getStatusCodeValue() == 200) {
                System.out.println("yann see this "+response.getBody());
                JSONArray res = new JSONArray(response.getBody());
                return res;
            }
              } catch (KeyStoreException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MavianceService.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return null;


    }
    //verify transaction
      public JSONArray verifytx2(String trid) throws NoSuchAlgorithmException{
          System.out.println("yvo see debut veriftx "+trid);
                JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
            String url = basePath+"/verifytx";
            
            String path = "/verifytx";
            String method = "GET";
             ArrayList<Pair> data = new ArrayList<Pair>();
             data.add(new Pair("trid", trid));
             HttpHeaders headers = new HttpHeaders();
            headers.add("xApiVersion", "3.0.0");
             //headers.add("Content-Type", "application/json");
            UriComponentsBuilder builder = null;    
              builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("trid", trid);
            headers.add("Authorization", buildAuthorizationHeader(path, method, data));
            HttpEntity<?> entity = new HttpEntity<>(headers);
          //RestTemplate restTemplate = new RestTemplate();
            
            ResponseEntity<String> response = null;
            try{
                RestTemplate restTemplate = getRestTemplate();
                response = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,entity,String.class);
            }
            catch (Exception ex){
                return null;  
            }
            System.out.println("yvo see this verif Unrest "+response.getBody());
            if (response.getStatusCodeValue()== 200) {
                System.out.println("yann see this "+response.getBody());
                JSONArray res = new JSONArray(response.getBody());
                return res;
            }
        return null;


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
        //RestTemplate restTemplate = new RestTemplate(requestFactory);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
   
    /*
    public String buildAuthorizationHeader(String endpoint, String method, List<Pair> data) {
        String authTitleKey = "s3pAuth", authTokenKey = "s3pAuth_token", authNonceKey = "s3pAuth_nonce",
                authSignatureKey = "s3pAuth_signature", authSignatureMethodKey = "s3pAuth_signature_method",
                authTimeStampKey = "s3pAuth_timestamp", separator = ", ", signatureMethod = "HMAC-SHA1",
                nonce = StringUtil.stringGenerator(), timeStamp = String.valueOf(Instant.now()
                        .getEpochSecond());

        List<Pair> headerParams = new ArrayList<>();
        headerParams.add(new Pair(authNonceKey, nonce));
        headerParams.add(new Pair(authSignatureMethodKey, signatureMethod));
        headerParams.add(new Pair(authTimeStampKey, timeStamp));
        headerParams.add(new Pair(authTokenKey, accessToken));

        data.addAll(headerParams);

        String url = basePath + endpoint;
        String signature = new HMACSignature(method, url, data).generate(accessSecret);

        return authTitleKey + " " + authNonceKey + "=\"" + nonce + "\"" + separator + authSignatureKey + "=\""
                + signature + "\"" + separator + authSignatureMethodKey + "=\"" + signatureMethod + "\"" + separator
                + authTimeStampKey + "=\"" + timeStamp + "\"" + separator + authTokenKey + "=\"" + accessToken + "\"";
    }
    */
    
    private String buildAuthorizationHeader(String endpoint, String method, List<Pair> data) {
        
              JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String accessToken = js.getString("accessToken");
        String accessSecret =js.getString("accessSecret");
        String phonec =js.getString("phonec");
        
        String authTitleKey = "s3pAuth", authTokenKey = "s3pAuth_token", authNonceKey = "s3pAuth_nonce",
                authSignatureKey = "s3pAuth_signature", authSignatureMethodKey = "s3pAuth_signature_method",
                authTimeStampKey = "s3pAuth_timestamp", separator = ", ", signatureMethod = "HMAC-SHA1",
                nonce = StringUtil.stringGenerator(), timeStamp = String.valueOf(Instant.now()
                        .getEpochSecond());

        List<Pair> headerParams = new ArrayList<>();
        headerParams.add(new Pair(authNonceKey, nonce));
        headerParams.add(new Pair(authSignatureMethodKey, signatureMethod));
        headerParams.add(new Pair(authTimeStampKey, timeStamp));
        headerParams.add(new Pair(authTokenKey, accessToken));

        data.addAll(headerParams);

        String url = basePath + endpoint;
        String signature = new HMACSignature(method, url, data).generate(accessSecret);

        return authTitleKey + " " + authNonceKey + "=\"" + nonce + "\"" + separator + authSignatureKey + "=\""
                + signature + "\"" + separator + authSignatureMethodKey + "=\"" + signatureMethod + "\"" + separator
                + authTimeStampKey + "=\"" + timeStamp + "\"" + separator + authTokenKey + "=\"" + accessToken + "\"";
    }
    
    public Map<String, Object> ping() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException{
       
        Map<String, Object> result = new HashMap();
         JSONObject js=getmaviance();
        String basePath = js.getString("basePath"); //test environment
        String url = basePath+"/ping";
        String request = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String path = "/ping";
        String method = "GET";
        ArrayList<Pair> data = new ArrayList<Pair>();
        String head = buildAuthorizationHeader(path, method, data);
        System.out.println(head);
        //head = "s3pAuth s3pAuth_nonce=\"50970315401\", s3pAuth_signature=\"iHvwmW576Y5pZwvwaWxVEL5+NPc=\", s3pAuth_signature_method=\"HMAC-SHA1\", s3pAuth_timestamp=\"1611921331\", s3pAuth_token=\"B14888BD-0064-1AB9-A4F4-13179318B0DC\"";
        headers.add("Authorization", head);
        headers.add("xApiVersion", "3.0.0");
        HttpEntity<String> entity = new HttpEntity<>(request, headers);
        //RestTemplate restTemplate = new RestTemplate();
        RestTemplate restTemplate = getRestTemplate();
        try{
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity,String.class);
            //ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            //ResponseEntity<String> response = restTemplate.getForEntity(url, String.class,entity);
            
            System.out.println("iwomi see this "+response.getStatusCodeValue());
            if (response.getStatusCodeValue() == 200) {
                System.out.println("iwomi see this ping response "+response.getBody());
                JSONObject res = new JSONObject(response.getBody());
                if(res.has("key")){
                    return res.toMap();
                }
                else{
                    result.put("status", "500");
                    result.put("message", "Service currently not available");
                    return result;
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.toString());
            System.out.println(ex.getMessage());
        }
        return null;


    }
    
    
    
    
}
