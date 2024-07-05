package com.iwomi.controller;
import com.iwomi.model.TransactionHistory;
import com.iwomi.repository.TransHisRepository;
import io.jsonwebtoken.impl.TextCodec;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.iwomi.serviceInterface.MomoCollectionAPI;
import java.sql.Timestamp;
import java.util.Date;

@RestController
@CrossOrigin()
public class MomoOperationsController {
    
    @Autowired
    MomoCollectionAPI apiOperationsMTN ;
    
    @Autowired
    TransHisRepository transHisRepository ;
    
    
    TransactionHistory transactiomHistory ;
    
    @RequestMapping(value = "/OperationStatus", method = RequestMethod.POST)
    public String OperationStatus(@RequestBody Map<String, String> payload) throws Exception{
        return apiOperationsMTN.OperationStatus(payload.get("referenceId"),payload.get("subKey"),payload.get("token"));
        
    }
    
     @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public Map<String, String> makeRedrawal(@RequestBody Map<String, String> payload) 
        throws Exception {
        return apiOperationsMTN.performRedrawal(payload.get("amount"),payload.get("number"),payload.get("payerMsg"),payload.get("payeeMsg"),payload.get("currency"),payload.get("token"),payload.get("subKey"),"");

    }
   
      
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public String getToken(@RequestBody Map<String, String> payload) 
        throws Exception {
        return apiOperationsMTN.getToken(payload.get("apiUser"),payload.get("apiKey"),payload.get("subKey"));

    }
    
    @RequestMapping(value = "/accountMomoStatus", method = RequestMethod.POST)
    public String MomoAccountExist(@RequestBody Map<String, String> payload) 
        throws Exception {
        return apiOperationsMTN.MomoAccountExist(payload.get("number"),payload.get("subKey"),payload.get("token"));
    }
    
    @RequestMapping(value = "/accountBalance", method = RequestMethod.POST)
    public String getAccountBalance(@RequestBody Map<String, String> payload) 
        throws Exception {
        return apiOperationsMTN.getAccountBalance(payload.get("subKey"),payload.get("token"));

    }
}