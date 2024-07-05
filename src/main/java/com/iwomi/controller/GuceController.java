package com.iwomi.controller;

import com.iwomi.maviance.Pair;
import com.iwomi.model.Nomenclature;
import com.iwomi.repository.NomenclatureRepository;
import com.iwomi.service.MavianceService;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin("*")
//@RequestMapping("/guce")
public class GuceController {

    @Autowired
    NomenclatureRepository nomenclatureRepository;

    @RequestMapping(value = "/guceSend", method = RequestMethod.POST)
    ResponseEntity<String> billPayToGuce(@RequestBody Map<String, Object> request){
         System.out.println("This is the guceSend : " + request.toString());
        JSONObject response = new JSONObject();
        JSONObject payload = new JSONObject(request);
        //JSONObject pi = new JSONObject();
        try {
           // pi = new JSONObject(payload.get("payerinfo").toString());
            JSONArray pi2 = new JSONArray(payload.get("payerinfo").toString());
            //String tel1=pi.getString("iden");
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
                BigInteger createtimeb = datam.optBigInteger("createTime", BigInteger.ZERO);
                BigInteger dueTime = datam.optBigInteger("dueTime", BigInteger.ZERO);
                BigInteger dueAmount = datam.getBigInteger("dueAmount");
                String v=datam.optString("status");
                String v1=datam.optString("currency");
                String v2=datam.optString("billRef");
            }
        }catch (Exception ex) {
            payload.put("error", ex.getLocalizedMessage());
            payload.put("error_description", ex.getMessage());
            payload.put("state", "REJECTED");
            return ResponseEntity.badRequest().body(payload.toString());
        }

        try {
            String uri = "http://localhost:9091/guce/guceSend";
            Nomenclature n = nomenclatureRepository.findTabcdAndAcsd("0012","0231","0");
            if(n!=null){
                uri = n.getLib2()+"/guce/g";
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

    @RequestMapping(value = "/accessToken", method = RequestMethod.GET)
    public Map<String, String> accessToken() {
        String baseUrel="https://172.18.23.61:8463/paymentendpoint/v2/external";
        String username="bicec";
        String password="test123";
        String test="ko";
        System.out.println("body token:" );
        Nomenclature n = nomenclatureRepository.findTabcdAndAcsd("6006","0001","0");
        if(n!=null){
            baseUrel = n.getLib1();
              System.out.println("body token:"+ baseUrel );
            username=n.getLib2();
            password=n.getLib3();
            test=n.getLib4();
        }
        System.out.println("body status"+ test);
        Map<String, String> resp = new HashMap<>();
        int i=0;
    if(test.equalsIgnoreCase("ok")){
        System.out.println("body debut getToken:"+ baseUrel );
        Unirest.config().verifySsl(false);
        HttpResponse<String> response = Unirest.post(baseUrel + "/oauth/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body("grant_type=password&client_id=restapp&client_secret=restapp&scope=read&username=" + username + "&password=" + password + "")
                .asString();

        System.out.println("body :" + response.getBody());
            switch (response.getStatus()) {
                case 200:
                    JSONObject jsonObject = new JSONObject(response.getBody());
                    String  token = jsonObject.getString("access_token");
                    System.out.println("Token :" + token);
                    resp.put("status", "01");
                    resp.put("data", token);
                    resp.put("data1", response.getBody());
                    break;
                case 500:
                    i++;
                    if(i<3){
                        accessToken() ;
                    } else{
                        resp.put("status", "100");
                        resp.put("data", response.getBody());
                    }
                    break;
                default:
                    resp.put("status", "100");
                    resp.put("data", response.getBody());
                    break;
            }
    } else{
        System.out.println("Token KO :" );
        resp.put("status", "01");
        resp.put("data", "");
    }
        return resp;
    }
    
   @RequestMapping(value = "/accessToken2", method = RequestMethod.GET) 
    public Map<String, String> accessToken2() throws NoSuchAlgorithmException{
  String baseUrel="https://172.18.23.61:8463/paymentendpoint/v2/external";
        String username="bicec";
        String password="test123";
        String test="ko";
        int i=0;
        System.out.println("body token:" );
        Nomenclature n = nomenclatureRepository.findTabcdAndAcsd("6006","0001","0");
        if(n!=null){
            baseUrel = n.getLib1();
              System.out.println("body token:"+ baseUrel );
            username=n.getLib2();
            password=n.getLib3();
            test=n.getLib4();
        }
        System.out.println("body status"+ test);
        Map<String, String> resp = new HashMap<>();
        try {
                   
           HttpHeaders headers = new HttpHeaders();
           headers.add("Content-Type", "application/x-www-form-urlencoded");
           // headers.add("xApiVersion", "3.0.0");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrel)
                .queryParam("grant_type", "password")
                .queryParam("client_id", "restapp")
                .queryParam("client_secret", "restapp")
                .queryParam("scope", "read")
                .queryParam("username", username)
                .queryParam("password", password);

            HttpEntity<?> entity = new HttpEntity<>(headers);
           //RestTemplate restTemplate = new RestTemplate();
            RestTemplate restTemplate = getRestTemplate();
            //
            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(),HttpMethod.GET,entity,String.class);
             System.out.println("yvo see this TOKEN009: "+response.getBody());
               switch (response.getStatusCodeValue()) {
                case 200:
                    JSONObject jsonObject = new JSONObject(response.getBody());
                    String  token = jsonObject.getString("access_token");
                    System.out.println("Token :" + token);
                    resp.put("status", "01");
                    resp.put("data", token);
                    resp.put("data1", response.getBody());
                    break;
                case 500:
                    i++;
                    if(i<3){
                        accessToken() ;
                    } else{
                        resp.put("status", "100");
                        resp.put("data", response.getBody());
                    }
                    break;
                default:
                    resp.put("status", "100");
                    resp.put("data", response.getBody());
                    break;
            }
            
        } catch (KeyStoreException ex) {
            System.out.println(ex.getMessage());
              resp.put("status", "100");
                    resp.put("data", ex.getMessage());
            Logger.getLogger(GuceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            System.out.println(ex.getMessage());
            resp.put("status", "100");
                    resp.put("data", ex.getMessage());
            Logger.getLogger(GuceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resp;


    }
    
    

    @RequestMapping(value = "/guceSendB", method = RequestMethod.POST)//acquisition
    public   ResponseEntity<String>  paymentInquiryRequests(@RequestBody Map<String, Object> request) {
        Map<String, String> respond = new HashMap();     
        Map<String, String> getToken = accessToken();
        JSONObject response1 = new JSONObject();
        JSONObject payload = new JSONObject(request);
         System.out.println("datatoBill " + request.toString());
        String test="ko";
         try {
            String baseUrl = "https://172.18.23.61:8463/paymentendpoint/v2/external/payment/send";
            Nomenclature n = nomenclatureRepository.findTabcdAndAcsd("6006","0001","0");
                if(n!=null){
                    baseUrl = n.getLib1()+"/payment/send";
                    test=n.getLib4();
                }
            HttpHeaders headers = new HttpHeaders();
            String token = "";
            if(test.equalsIgnoreCase("ko")){
                 Nomenclature nu = nomenclatureRepository.findTabcdAndAcsd("0012","0231","0");
                if(nu!=null){
                    baseUrl = nu.getLib2()+"/guce/guceSendBok";
                } 
                token = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
            } else{
                if (getToken.get("status").equalsIgnoreCase("01")) {
                    System.out.println("data " + getToken.get("data"));
                    token = getToken.get("data");
                } else {
                    respond.put("status", "501");
                    respond.put("data", " token failed");
                     return ResponseEntity.badRequest().body(respond.toString());
                }
            }
                System.out.println("yvo senBillGuce Url " + baseUrl);
                System.out.println("Yvo Debut SENDBILLGU " );
                
                  headers.setContentType(MediaType.APPLICATION_JSON);
                  headers.add("Authorization", "Bearer " + token);
               
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
                RestTemplate restTemplate = getRestTemplate();
                System.out.println("This is the statusBBill send " );
                ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, entity, String.class);
                System.out.println("This is the statusBBilll " + response.getStatusCodeValue());
                System.out.println("This is the statusBBilll " + response);
            if (response.getStatusCodeValue() == 200) {
               return response;
            } else {
                 return response;
            }
          } catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException ex) {
            request.put("error", ex.getMessage());
            request.put("error_description", ex.getMessage());
            return ResponseEntity.badRequest().body(request.toString());
        }
    }

 @RequestMapping(value = "/accessToken1", method = RequestMethod.GET)
    public Map<String, String> accessToken1() {
        String baseUrel="https://172.18.23.61:8463/paymentendpoint/v2/external";
        String username="bicec";
        String password="test123";
        Map<String, String> resp = new HashMap<>();
        System.out.println("body debut getToken:"+ baseUrel );
        Unirest.config().verifySsl(false);
        HttpResponse<String> response = Unirest.post(baseUrel + "/oauth/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body("grant_type=password&client_id=restapp&client_secret=restapp&scope=read&username=" + username + "&password=" + password + "")
                .asString();
        System.out.println("body :" + response.getBody());
            switch (response.getStatus()) {
                case 200:
                    JSONObject jsonObject = new JSONObject(response.getBody());
                    String  token = jsonObject.getString("access_token");
                    System.out.println("Token :" + token);
                    resp.put("status", "01");
                    resp.put("data", token);
                    resp.put("data1", response.getBody());
                    break;
              
                default:
                    resp.put("status", "100");
                    resp.put("data", response.getBody());
                    break;
            }
    
        return resp;
    }


}
