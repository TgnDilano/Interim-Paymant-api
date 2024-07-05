package com.iwomi.controller;

import java.security.NoSuchAlgorithmException;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iwomi.maviance.ApiException;
import com.iwomi.repository.MavTransHistoryRepository;
import com.iwomi.repository.MavianceMerchantRepository;
//import com.iwomi.model.MavianceMerchants;
import com.iwomi.model.MavTempTrans;
import com.iwomi.repository.MavTempTransRepo;

import com.iwomi.service.MavianceService;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import org.json.JSONObject;


@RestController
@CrossOrigin("*")
@RequestMapping("/mav")

public class MavianceController {

     @Autowired
    MavianceService mavianceService;

    @Autowired
    MavTransHistoryRepository mavTransHistoryRepository;

    @Autowired
    MavianceMerchantRepository mavMerchantRepo;

    // @Autowired
    // MavianceMerchants mavianceMerchants;

    // @Autowired
    // MavTempTrans mavTempTrans;

    @Autowired
    MavTempTransRepo mavTempTransRepo;



   //not ok
    @RequestMapping(value = "/getSubscription", method = RequestMethod.POST)
    public Map<String,Object> getSubscription (@RequestBody Map<String, String> payload) throws NoSuchAlgorithmException, ApiException, JsonProcessingException {
       
       Map<String,Object> result = new HashMap();
        // JSONArray resultat = new JSONArray();
         System.out.println("This is payload "+payload);
         String merchant = payload.get("merchant");
        String customerNumber = payload.get("customerNumber");
        int service_id = Integer.parseInt(payload.get("serviceid"));

          JSONArray data_subs = mavianceService.subscriptionv3(merchant, customerNumber, service_id);
         // Map <String, Object> data_subs = mavianceService.subscriptionv3(merchant, customerNumber, service_id);
         // Map<String, Object> data_quote = mavianceService.quotestd(amount, payItemId);

        System.out.println("This is the response from subscription "+data_subs);
        if(data_subs != null ){
            
            String payItemId = data_subs.getJSONObject(0).get("payItemId").toString();
             String merchant_name = data_subs.getJSONObject(0).get("merchant").toString();
              String customerName = data_subs.getJSONObject(0).get("customerName").toString();

             result.put("status", "01");
             result.put("customerName", customerName);
             result.put("payItemId", payItemId);
             result.put("merchant", merchant_name);
             result.put("message", "successfully retrieved!");
             
             //resultat.put(data_subs);
            
            
           // result.put("data", data_subs);
            // return data_subs;
             return result;
        
        
    }
       
             result.put("status", "404");
            result.put("message", "Invalid service_id or merchant");
            return result;
           // return null;

    
}

     @RequestMapping(value = "/getSubscription2", method = RequestMethod.POST)
    public Map<String,Object> getSubscription2 (@RequestBody Map<String, String> payload) throws NoSuchAlgorithmException, ApiException, JsonProcessingException {
       
       Map<String,Object> result = new HashMap();
        // JSONArray resultat = new JSONArray();
         System.out.println("This is payload "+payload);
         String merchant = payload.get("merchant");
        String customerNumber = payload.get("customerNumber");
        int service_id = Integer.parseInt(payload.get("serviceid"));

          JSONArray data_subs = mavianceService.subscriptionv2(merchant, customerNumber, service_id);
         // Map <String, Object> data_subs = mavianceService.subscriptionv3(merchant, customerNumber, service_id);
         // Map<String, Object> data_quote = mavianceService.quotestd(amount, payItemId);

        System.out.println("This is the response from subscription "+data_subs);
        if(data_subs != null ){
            
            String payItemId = data_subs.getJSONObject(0).get("payItemId").toString();
             String merchant_name = data_subs.getJSONObject(0).get("merchant").toString();
              String customerName = data_subs.getJSONObject(0).get("customerName").toString();

             result.put("status", "01");
             result.put("customerName", customerName);
             result.put("payItemId", payItemId);
             result.put("merchant", merchant_name);
             result.put("message", "successfully retrieved!");
             
             //resultat.put(data_subs);
            
            
           // result.put("data", data_subs);
            // return data_subs;
             return result;
        
        
    }
       
             result.put("status", "404");
            result.put("message", "Invalid service_id or merchant");
            return result;
           // return null;

    
}

    @RequestMapping(value = "/quotestd", method = RequestMethod.POST)
    public Map<String, Object> quotestd (@RequestBody Map<String, String> payload) throws NoSuchAlgorithmException, ApiException, JsonProcessingException {
       
        Map<String,Object> result = new HashMap();
         //JSONArray result = new JSONArray();
         System.out.println("This is payload Quotestd "+payload);
         String amount = payload.get("amount");
        String payItemId = payload.get("payItemId");
        Map<String, Object> data_quote = mavianceService.quotestdv2F(Integer.valueOf(amount), payItemId);

        System.out.println("This is the response from subscription "+data_quote);
        if(data_quote != null ){
             data_quote.put("status", "01");
             return data_quote;
         }
       
            result.put("status", "404");
            result.put("message", "Invalid service_id or merchant");
            return result;
}

      @RequestMapping(value = "/quotestd2", method = RequestMethod.POST)
    public Map<String, Object> quotestd2 (@RequestBody Map<String, String> payload) throws NoSuchAlgorithmException, ApiException, JsonProcessingException {
       
        Map<String,Object> result = new HashMap();
         //JSONArray result = new JSONArray();
         System.out.println("This is payload Quotestd "+payload);
         String amount = payload.get("amount");
        String payItemId = payload.get("payItemId");
        Map<String, Object> data_quote = mavianceService.quotestdv2(Integer.valueOf(amount), payItemId);

        System.out.println("This is the response from subscription "+data_quote);
        if(data_quote != null ){
             data_quote.put("status", "01");
             return data_quote;
         }
       
            result.put("status", "404");
            result.put("message", "Invalid service_id or merchant");
            return result;
}


//ok
@RequestMapping(value = "/getMavMerchants", method = RequestMethod.GET)
    public Map<String, Object> getMavMerchants () throws NoSuchAlgorithmException, ApiException, JsonProcessingException {
       
        Map<String,Object> result = new HashMap();

          List <Object> allMerchants = mavianceService.merchant();

        if(allMerchants != null ){
          //  mavMerchantRepo.saveAll(null);
            
            System.out.println("This is the response from subscription "+allMerchants);
             result.put("status", "01");
             result.put("message", "successfully retrieved!");
             result.put("data", allMerchants);
             return result;
        
        
    }
       
             result.put("status", "404");
            result.put("message", "Invalid service_id or merchant");
            return result;

    
}


//ok
@RequestMapping(value = "/verifytrans", method = RequestMethod.POST)
    public Map<String, Object> verifytrans (@RequestBody Map<String, String> payload) throws NoSuchAlgorithmException, ApiException, JsonProcessingException {
       
        Map<String,Object> result = new HashMap();

         String trid = payload.get("trid");
        
          JSONArray trans = mavianceService.verifytx(trid);

        if(trans != null ){
            MavTempTrans mav = mavTempTransRepo.findByTrid(trid);
            if(mav!=null){
               mav.setStatus(trans.getJSONObject(0).optString("status"));
               System.out.println("This is the response from verifytx "+trans);
               mavTempTransRepo.save(mav); 
            }
            
           //  result.put("status", "01");
             result.put("message", "successfully retrieved!");
              result.put("status", trans.getJSONObject(0).optString("status"));
             result.put("trid", trans.getJSONObject(0).optString("trid"));
              result.put("data", trans.toString());
             return result;
        
        
    }
       
             result.put("status", "404");
            result.put("message", "Invalid service_id or merchant");
            return result;

    
}
   

     // ok for now
    @RequestMapping(value = "/paybill", method = RequestMethod.POST)
    public Map<String,Object> paybill( @RequestBody Map<String, String> payload) throws NoSuchAlgorithmException, ApiException, JsonProcessingException{
        Map<String,Object> result = new HashMap();

        
        //payload requires : amount, tel, email, name, payItemId, customerNumber
        
        String customerNumber = payload.get("customerNumber");
      
        String payItemId = payload.get("payItemId");  
        
        int amount = Integer.parseInt(payload.get("amount"));
        
        String tel = payload.get("tel");
        String email = payload.get("email");
        String name = payload.get("name");
        String trid = payload.get("trid");
        String quoteId = payload.get("quoteId");
//        Map<String, Object> data_quote = mavianceService.quotestd(amount, payItemId);
//        
//        if(data_quote == null ){
//            result.put("status", "404");
//            result.put("message", "Invalid payItemId or amount");
//            return result;
//        }
        String time = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
        String unique = UUID.randomUUID().toString();
        String unique_8 = unique.substring(28);
       // String itrans_id = "BIC"+time.toString()+unique_8;
        //( quoteId, customerPhonenumber, customerEmailaddress, customerName, 
        //customerAddress, customerNumber, serviceNumber, trid)
        
       // String quoteId = data_quote.get("quoteId").toString();
       
        
       // String trid = itrans_id;
        
        Map<String, Object> data_collect = mavianceService.collectstd(quoteId, tel, email,name,customerNumber,trid);
        
        if(data_collect == null ){
            result.put("status", "404");
            result.put("message", "Invalid service number or tel");
            return result;
        }
        //  if(data_collect != null){
        //     result.put("status",data_collect.get("status"));
        //     result.put("internal_id", data_collect.get("trid"));
        //     result.put("veriCode", data_collect.get("veriCode"));
        //     result.put("message", "Payment has been initiated");
        //     return result;
        // }
        //this will terminate the operation at this level for now

        String ptn = data_collect.get("ptn").toString();
        
        JSONArray data_history = mavianceService.historystd(ptn);
        
        // if(data_history == null){
        //     result.put("status", "500");
        //     result.put("message", "An error occured please try again later");
        //     return result;
        // }
         if(data_history != null){
            System.out.println("Rex see this history "+data_history);
            result.put("status", data_collect.get("status"));
            result.put("internal_id", data_collect.get("trid"));
             result.put("veriCode", data_history.getJSONObject(0).get("veriCode").toString());
             result.put("payItemId", data_history.getJSONObject(0).get("payItemId").toString());
            result.put("message", "Operation has been initiated ...");
            return result;
        }
           result.put("status", "500");
            result.put("message", "An error occured please try again later");
            return result;
         // Now do the magic.
        //Historystd data = new Gson().fromJson(data_history.getJSONObject(0).toString(), Historystd.class); //Yan commented this     
           
       

    }

     // ok for now
    @RequestMapping(value = "/collectstd", method = RequestMethod.POST)
    public  Map<String,Object> collectstd( @RequestBody Map<String, String> payload) throws NoSuchAlgorithmException, ApiException, JsonProcessingException{
        Map<String,Object> result = new HashMap();
        System.out.println("This is payload collectstd "+payload);
       String quoteId = payload.get("quoteId");
        String customerNumber = payload.get("customerNumber");
         String tel = payload.get("customerPhonenumber");  
          String email = payload.get("customerEmailaddress");
        String name = payload.get("customerName");
        String customerAddress = payload.get("customerAddress");
        String trid = payload.get("trid");
       System.out.println("This is payload collectstd debut ");
       JSONObject data_collect = mavianceService.collectstdFF(quoteId, tel, email,name,customerAddress,customerNumber,trid);
        if(data_collect!=null){ 
       System.out.println("This is payload collectstd fin: "+data_collect.toString());
        } else{
         System.out.println("This is payload collectstd fin: collect null ");   
        }
      MavTempTrans mav = new MavTempTrans();

    
      mav.setDele("0");
      mav.setPay_item_descr(trid);
      


        if(data_collect == null ){
            
            result.put("status", "404");
            result.put("message", "Invalid service number or tel");
            return result;
        } 
  try{
        mav.setPay_item_id(data_collect.opt("payItemId").toString());
        mav.setLocal_cur(data_collect.opt("localCur").toString());
        mav.setPrice_local_cur(data_collect.opt("priceLocalCur").toString());
        mav.setStatus(data_collect.opt("status").toString());
        mav.setVeri_code(data_collect.opt("veriCode").toString());
        mav.setAgent_balance(data_collect.opt("agentBalance").toString());
        mav.setReceipt_number(data_collect.opt("receiptNumber").toString());
        mav.setTimestamp1(data_collect.opt("timestamp").toString());
        mav.setPtn(data_collect.opt("ptn").toString());
        mav.setTrid(data_collect.opt("trid").toString());
        mav.setPrice_system_cur(data_collect.opt("priceSystemCur").toString());
        //mav.setPin(data_collect.get("pin").toString());

        //save the transaction to the db
        System.out.println("See this transaction before saving "+mav);
        if(data_collect.opt("payItemId").toString().isEmpty()){
            
        } else{
          mavTempTransRepo.save(mav);  
        }
        
  } catch (Exception ex){
              System.out.println("See this transaction before saving "+new JSONObject(mav).toString());   
            }

      //  data_collect.put("reuest", "01");
       return data_collect.toMap();
    }


      // ok for now
    @RequestMapping(value = "/getTempTransById", method = RequestMethod.POST)
    public  Map<String,Object> getTempTransById( @RequestBody Map<String, String> payload) throws NoSuchAlgorithmException, ApiException, JsonProcessingException{
        Map<String,Object> result = new HashMap();
       //payload requires : trid
       
        String trid = payload.get("trid");
      
         MavTempTrans trans = mavTempTransRepo.findByTrid(trid);

         System.out.println("See this trans  "+trans);

        if(trans == null ){
            
            result.put("status", "404");
            result.put("message", "transaction not found");
            return result;
        }
       
            result.put("status", "01");
            result.put("message", "transaction retrieved");
            result.put("data", trans);
            return result;
        }
        
        //ok
        @RequestMapping(value = "/getAllTempTrans", method = RequestMethod.GET)
        public Map<String,Object> getAllTempTrans(){
            Map<String,Object> result = new HashMap();
            String dele = "0";
            List<MavTempTrans> hist = mavTempTransRepo.findAllTempTrans(dele);
            if(hist!=null){
                result.put("status", "01");
                result.put("data", hist);
                result.put("message", "Successfully retrieved");
                return result;
            }
            
            result.put("status", "100");
            result.put("message", "An error occurred, try later or contact support");
            return result;
    
        }

 @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public Map<String, Object> ping() throws NoSuchAlgorithmException,KeyStoreException, KeyManagementException {
       
        Map<String,Object> result = new HashMap();
         //JSONArray result = new JSONArray();
       
        Map<String, Object> getPing = mavianceService.ping();

        System.out.println("This is the response from ping "+getPing);
        if(getPing != null ){
             getPing.put("status", "01");
             return getPing;
         }
       
            result.put("status", "404");
            result.put("message", "Service unavailable");
            return result;
}
      
     @RequestMapping(value = "/getBill", method = RequestMethod.POST)
    public Map<String,Object> getProduct (@RequestBody Map<String, String> payload) throws NoSuchAlgorithmException, ApiException, JsonProcessingException {
       
       Map<String,Object> result = new HashMap();
        // JSONArray resultat = new JSONArray();
         System.out.println("This is payload "+payload);
         String merchant = payload.get("merchant");
         String endpoint = payload.get("endpoint");
        String customerNumber = payload.get("customerNumber");
        int service_id = Integer.parseInt(payload.get("serviceid"));
//String merchant, int service_id, String service_number, String endpoind
          List<Object>  data_subs = mavianceService.SEARCHABLE_BILL(merchant, service_id, customerNumber,endpoint);
          System.out.println("This is the response from subscription "+data_subs);
        if(data_subs != null ){
            

             result.put("status", "01");
              // result.put("data", "01");
             result.put("message", "successfully retrieved!");
             
             //resultat.put(data_subs);
            
            
            result.put("data", data_subs);
            // return data_subs;
             return result;
        
        
    }
       
             result.put("status", "404");
            result.put("message", "Invalid service_id or merchant");
            return result;
           // return null;

    
}


    }
    
    

