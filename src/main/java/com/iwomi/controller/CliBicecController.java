/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.controller;

/**
 *
 * @author HP
 */

import com.iwomi.model.Nomenclature;
import com.iwomi.model.Pwd;
import com.iwomi.repository.NomenclatureRepository;
import com.iwomi.repository.PwdRepository;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import static org.apache.commons.lang3.StringUtils.trim;

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
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin()

public class CliBicecController {
    
    @Autowired
    NomenclatureRepository nomenclatureRepository;
    @Autowired
    PwdRepository pwdRepository;
    
    @RequestMapping(value ="/getBicecWallet", method = RequestMethod.GET)
    public Map<String, String> getBicecWallet(){
        Map <String, String> response = new HashMap();
        String wallet = "";
        String type = "gimac";
        //from the nomencreature table respository, find the repository.
        List<Nomenclature> nomenList = nomenclatureRepository.findTabcdAndDel("6000","0");
        for(int i = 0; i<nomenList.size();i++){
            Nomenclature nomen = nomenList.get(i);
            if(nomen != null){
                if(type.equalsIgnoreCase(nomen.getLib1())){
                    wallet = nomen.getLib5();
                    response.put("success", "01");
                    response.put("wallet", wallet);
                    return response;
                }
            }
        }
        response.put("success", "100");
        response.put("wallet", wallet);
        
        return response;
    }
    
    @RequestMapping(value ="/sendSMS", method = RequestMethod.POST)
    public Map<String, Object> sendSMS(@RequestBody Map<String, String> payload){
        Map<String, Object> result = new HashMap();
        String tel = payload.get("tel");
        String msg = payload.get("msg");
        Pwd pwd = pwdRepository.findByAcscd("0217","0");
          String uri="";
        if(pwd!=null){
             uri=pwd.getLib1()+"&phone="+tel+"&body="+msg;
        } else{
           uri = "http://10.100.23.21/bicec/admin/json.php?module=sms&action=send_wallet&phone="+tel+"&body="+msg;
          
        }
       
        Map<String, String> request = new HashMap();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(request, headers);
        RestTemplate restTemplate = new RestTemplate();
        try{
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity,String.class);
            System.out.println("This is the status " + response.getBody());

            if (response.getStatusCodeValue() == 200) {
                //success
                result.put("success", "01");
                result.put("message", "SMS successfully send");
                result.put("data", payload);
                return result;
            }
            else {
                result.put("success", "100");
                result.put("message", "SMS not send");
                result.put("data", payload);
                return null;
            }
        }
        catch(Exception e){
            result.put("success", "100");
            result.put("message", e.getMessage());
            result.put("data", payload);
            return null;
            
        }
        
        
    }
    
    @RequestMapping(value ="/sendEmail", method = RequestMethod.POST)
    public Map<String, Object> sendEmail(@RequestBody Map<String, String> payload) {
         Map<String, Object> response = new HashMap();
         
//         Nomenclature nomen1= nomenclatureRepository.findTabcdAndAcsd("0012", "0214", "0");
//         final String user = nomen1.getLib2();
//         Nomenclature nomen2= nomenclatureRepository.findTabcdAndAcsd("0012", "0215", "0");
//         byte[] decoder = Base64.getDecoder().decode(trim(nomen2.getLib2().toString()));
//         final String pass = new String(decoder);
//         Nomenclature nomen3 = nomenclatureRepository.findTabcdAndAcsd("0012", "0216", "0");
//         final String port = nomen3.getLib2();
//         Nomenclature nomen4= nomenclatureRepository.findTabcdAndAcsd("0012", "0217", "0");
//         final String mail_Server = nomen4.getLib2();
         
        Pwd pwd = pwdRepository.findByAcscd("0214","0");
        
        byte[] decoder = Base64.getDecoder().decode(trim(pwd.getPass().toString()));
        String v = new String(decoder);
        final String mail_Server = trim(pwd.getLib1());
        final String user= trim(pwd.getLogin());
        final String pass = trim(v);
        final String port = pwd.getLib2();
        /*System.out.println("mail server "+mail_Server);
        System.out.println("mail user "+user);
        System.out.println("mail pass "+pass);
        System.out.println("mail port "+port);
        System.out.println("mail message "+payload.get("msg"));
        System.out.println("mail object "+payload.get("obj"));
        System.out.println("mail email of receiver "+payload.get("ema"));
        System.out.println("mail.smtp.host "+ mail_Server);
        System.out.println("mail.smtp.ssl.trust "+ mail_Server);
        System.out.println("mail.smtp.port "+port);
        System.out.println("mail.smtp.auth " +"false");
        System.out.println("mail.smtp.starttls.enable"+ "true"); //TLS*/
        
         
         String msg = payload.get("msg");
         String title = payload.get("obj");
         String email = payload.get("ema");           
   
        Properties prop = new Properties();
	prop.put("mail.smtp.host", mail_Server);
        prop.put("mail.smtp.ssl.trust", mail_Server);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", "false");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, pass);
                    }
                });

        try {
           //System.out.println("PasswordAuthentication "+ "true"); //TLS
          // System.out.println("user "+ user); //TLS
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            message.setSubject(title);
           // message.setText(msg);
            message.setContent("<P>"+msg+"</p><br/><br/><br/>","text/html");
            Transport.send(message);
            response.put("'success", "01");
            response.put("message", "message successfully send");
            response.put("data", payload);

            System.out.println("Done");

        } catch (MessagingException e) {
            response.put("'success", "100");
            response.put("message", "message not send, please try again later");
            response.put("data", payload);
            e.printStackTrace();
        }
        return response;
    }
     @RequestMapping(value ="/testService", method = RequestMethod.POST)
    public Map<String, String> testService(@RequestBody Map<String, String> payload){
       return payload; 
    }
    
   
    
}
