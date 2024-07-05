/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.controller;

//import com.iwomi.model.CamTrans;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwomi.model.CamTrans;
import com.iwomi.model.OnPaidObj;
import com.iwomi.model.SystemProp;
import com.iwomi.model.TransactionHistory;
import com.iwomi.model.User;
import com.iwomi.repository.CamPayResp;
import com.iwomi.repository.SystePropRepository;
import com.iwomi.repository.TransHisRepository;
import com.iwomi.repository.UserAdminRepository;
import com.iwomi.service.ServiceApiImpl;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin()
@RequestMapping("/api")
public class UserAdminController {

    @Autowired
    UserAdminRepository userAdminRepository;

    @Autowired
    SystePropRepository systePropRepositroy;

    @Autowired
    TransHisRepository transHisRepository;

    @Autowired
    ServiceApiImpl serviceApiImpl;

    @Autowired
    CamPayResp camPayResp;

    User user;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody Map<String, String> payload) throws Exception {
        Map<String, Object> response = new HashMap();
        String username = payload.get("nom");
        String password = payload.get("pwd");

        user = userAdminRepository.findUserByNameAndPassword(username, password);

        if (user != null) {
            response.put("success", "01");
            response.put("data", user);
        } else {
            response.put("success", "100");
            response.put("message", "Wrong login details, correct your username or password and try again later");
            response.put("data", user);
        }

        return response;

    }

    @RequestMapping(value = "/transactionDetail", method = RequestMethod.POST)
    public Map<String, Object> transactionDetail(@RequestBody Map<String, String> payload) throws Exception {
        Map<String, Object> response = new HashMap();
        List<TransactionHistory> transactionHistory = transHisRepository.findall();

        if (transactionHistory != null) {
            response.put("success", "01");
            response.put("data", transactionHistory);
        } else {
            response.put("success", "100");
            response.put("message", "no transaction");
            response.put("data", null);
        }

        return response;

    }

    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    public Map<String, Object> transaction() throws Exception {
        Map<String, Object> response = new HashMap();
        List<TransactionHistory> transactionHistory = transHisRepository.findall();

        if (transactionHistory != null) {
            response.put("success", "01");
            response.put("data", transactionHistory);
        } else {
            response.put("success", "100");
            response.put("message", "no transaction");
            response.put("data", null);
        }

        return response;

    }

    @RequestMapping(value = "/systemop", method = RequestMethod.POST)
    public Map<String, Object> systemop() throws Exception {
        Map<String, Object> response = new HashMap();
        List<SystemProp> systeProp = systePropRepositroy.findall();

        if (user != null) {
            response.put("success", "01");
            response.put("data", systeProp);
        } else {
            response.put("success", "100");
            response.put("message", "Wrong login details, correct your username or password and try again later");
            response.put("data", null);
        }

        return response;

    }

    @RequestMapping(value = "/systemopUpdate", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody SystemProp systemProp) throws Exception {
        Map<String, Object> response = new HashMap();
        SystemProp systeProp = systePropRepositroy.save(systemProp);

        if (systeProp != null) {
            response.put("success", "01");
            response.put("data", systeProp);
        } else {
            response.put("success", "100");
            response.put("message", "echec d\'enregistrement");
            response.put("data", null);
        }
        return response;
    }

    @RequestMapping(value = "/authClient", method = RequestMethod.POST)
    public String authClient(@RequestBody Map<String, String> payload) {
        return serviceApiImpl.AuthCamcis(payload.get("nui"), payload.get("email"), payload.get("uscat"));
    }

    @RequestMapping(value = "/authClient1", method = RequestMethod.GET)
    public String authClient1() {
        return serviceApiImpl.AuthCamcis("M121300048114H", "bosco.kuate@campass.org", "R");
    }

    @RequestMapping(value = "/unpaidlist", method = RequestMethod.POST)
    public Map<String, Object> unpaidlist(@RequestBody OnPaidObj onPaidObj) throws JsonProcessingException, URISyntaxException {
        System.out.println("its here 1");
        String s = serviceApiImpl.ListUnPaid(onPaidObj);
        System.out.println("its here 2");
        Map<String, Object> s1 = new HashMap();
        Map<String, Object> s2 = new HashMap();
        System.out.println("its here 3");
        System.out.println(s);
        List<HashMap> list = new ArrayList<HashMap>();
        if (s != null) {
//        String s = serviceApiImpl.ListUnPaid("", "", "M121300048114H", "", "");
            JSONObject data = new JSONObject(s);

            JSONArray rr = data.getJSONArray("resultData");
            for (int i = 0; i < rr.length(); i++) {
                System.out.println("we are at position " + i);
                JSONObject result1 = rr.getJSONObject(i);
                s1.put("nui", result1.getString("taxPayerNumber"));
                s1.put("nfac", result1.getString("noticeNumber"));
                s1.put("tdev", result1.getBigInteger("noticeId"));
                s1.put("mon", result1.getBigInteger("noticeAmount"));
                s1.put("nom", result1.getString("taxPayerName"));
                s1.put("demi", result1.getString("notificationDate"));
                s1.put("dexp", result1.getString("dueDate"));
                s1.put("uti", result1.getString("issuerOffice"));
                list.add(i, (HashMap) ((HashMap) s1).clone());
                s1.clear();
            }
            s2.put("success", "01");
            s2.put("data", list);
            return s2;
        } else {
            s2.put("success", "01");
            s2.put("data", list);
            return s2;
        }

    }

    @RequestMapping(value = "/paidlist", method = RequestMethod.POST)
    public Map<String, Object> paidlist(@RequestBody OnPaidObj onPaidObj) throws JsonProcessingException, URISyntaxException {
        String s = serviceApiImpl.ListPaid(onPaidObj);
        JSONObject data = new JSONObject(s);
        Map<String, Object> s1 = new HashMap();
        Map<String, Object> outp = new HashMap();
        List<HashMap> list = new ArrayList<HashMap>();
        JSONArray rr = data.getJSONArray("resultData");
        for (int i = 0; i < rr.length(); i++) {
            System.out.println("we are at position " + i);
            JSONObject result1 = rr.getJSONObject(i);
            System.out.println(result1);
            s1.put("nui", result1.getString("taxPayerNumber"));
            s1.put("nfac", result1.getString("noticeNumber"));
            s1.put("mon", result1.getBigInteger("amountReceived"));
            s1.put("nom", result1.getString("taxPayerName"));
            s1.put("paydate", result1.getString("paymentDate"));
            s1.put("notdate", result1.getString("notificationDate"));
            list.add(i, (HashMap) ((HashMap) s1).clone());
            s1.clear();
        }
        outp.put("succes", "01");
        outp.put("data", list);
        return outp;
    }

    @RequestMapping(value = "/beneficiery", method = RequestMethod.POST)
    public Map<String, Object> beneficiery(@RequestBody OnPaidObj onPaidObj) throws JsonProcessingException, URISyntaxException {
        String s = serviceApiImpl.ListUnPaid(onPaidObj);
        JSONObject data = new JSONObject(s);
        Map<String, Object> s1 = new HashMap();
        Map<String, Object> outp = new HashMap();
        List<HashMap> list = new ArrayList<HashMap>();
        if (!data.getJSONArray("resultData").isEmpty()) {
            JSONArray rr = data.getJSONArray("resultData").getJSONObject(0).getJSONArray("beneficiaryList");
            BigInteger bg = new BigInteger("0");
            for (int i = 0; i < rr.length(); i++) {
                JSONObject result1 = rr.getJSONObject(i);
                System.out.println(result1);
                bg = bg.add(result1.getBigInteger("amount"));
                s1.put("mnt", result1.getBigInteger("amount").toString());
                s1.put("bkc", result1.getString("bankCode"));
                s1.put("acnt", result1.getString("accountNumber"));
                s1.put("desp", result1.getString("beneficiaryName"));
                s1.put("bencode", result1.getString("beneficiaryCode"));
                list.add(i, (HashMap) ((HashMap) s1).clone());
                s1.clear();
            }
            outp.put("success", "01");
            outp.put("data", list);
            outp.put("total", bg);
        } else {
            outp.put("success", "01");
            outp.put("data", list);
            outp.put("total", new BigInteger("0"));
        }
        return outp;
    }

    @RequestMapping(value = "/ventilationNotification", method = RequestMethod.POST)
    public Map<String, Object> ventilationNotification(@RequestBody CamTrans camTrans) {
//                DONE,FAIL, PENDING, IN_PROGRESS
        List<CamTrans> camTrans2 = camPayResp.findBybankPaymentNumber(camTrans.getBankPaymentNumber());
        if (camTrans2.size() == 1) {
            CamTrans camTrans1 = camTrans2.get(0);
            camTrans1.setBankCode(camTrans.getBankCode());
            camTrans1.setVentilationStatus(camTrans.getVentilationStatus());
            camTrans1.setDataventile(camTrans.getDataventile());
            return serviceApiImpl.NotifVent1(camTrans1);
        } else {
            Map<String, Object> outp = new HashMap<>();
            outp.put("success", "100");
            outp.put("message", "Transaction introuvable");
            return outp;
        }

    }
    private String getPaymenttype(String s){
        Map<String, String> map = new HashMap<>();
            map.put("BANK", "001");
            map.put("MOMO", "002");
            map.put("OM", "003");
            map.put("GIMAC", "");
            map.put( "VISA/MASTER CARD","005");
            map.put( "EXPRESS UNION","006");
            map.put( "GIMACV" ,"007");
            return map.get(s);
    }
    @RequestMapping(value = "/reglementCam", method = RequestMethod.POST)
    public Map<String, Object> reglementCam(@RequestBody CamTrans camTrans) {
        System.out.println("it is the entring point");
        System.out.println(camTrans);
        System.out.println(camTrans.toString());
        System.out.println(camTrans.getAccountName());
        camTrans.setPaymentDate(LocalDateTime.now());
        return serviceApiImpl.Payment1(camTrans);
    }

    @RequestMapping(value = "/reglementCam1", method = RequestMethod.POST)
    public String reglementCam1(@RequestBody CamTrans camTrans) {
        System.out.println("it is the entring point");
        return serviceApiImpl.vantilation_message(camTrans);
    }

    @RequestMapping(value = "/patriceTest", method = RequestMethod.GET)
    public Map<String, Object> patriceTest() {
        Map<String, Object> data = new HashMap();
        Map<String, Object> data1 = new HashMap();
        Map<String, Object> data2 = new HashMap();
        data1.put("success", "01");
        List<Map<String, Object>> e = new ArrayList<>();
        data.put("nui", "M121300048114H");
        data.put("nfac", "OP20200526123456");
        data.put("tdev", "145");
        data.put("mon", 175000);
        data.put("nom", "IWOMI TECH");
        data.put("demi", "20200620");
        data.put("dexp", "20200620");
        data.put("uti", "192");
        e.add(data);
        data2.put("nui", "M121300048114H");
        data2.put("nfac", "NT00012020052612");
        data2.put("tdev", "156");
        data2.put("mon", 200000);
        data2.put("nom", "IWOMI TECH22");
        data2.put("demi", "20200620");
        data2.put("dexp", "20200620");
        data2.put("uti", "192");
        e.add(data2);
        data1.put("data", e);
        return data1;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public List<CamTrans> unpaist() {
        return camPayResp.findAll();

    }
    
    
    @RequestMapping(value = "/apUnPaid", method = RequestMethod.POST)
    public Map<String, Object> ApUnPaid(@RequestBody Map<String, String> payload) {
        String s = serviceApiImpl.ApUnPaid(payload.get("taxpayerNumber"));
        Map<String, Object> s1 = new HashMap();
        Map<String, Object> s2 = new HashMap();
        System.out.println(s);
        List<HashMap> list = new ArrayList<HashMap>();
        if (s != null) {
            JSONObject data = new JSONObject(s);
            JSONArray rr = data.getJSONArray("resultData");
            for (int i = 0; i < rr.length(); i++) {
                System.out.println("we are at position " + i);
                JSONObject result1 = rr.getJSONObject(i);
                s1.put("nui", result1.getString("taxPayerNumber"));
                s1.put("nfac", result1.getString("noticeNumber"));
                s1.put("tdev", result1.getBigInteger("noticeId"));
                s1.put("mon", result1.getBigInteger("noticeAmount"));
                s1.put("nom", result1.getString("taxPayerName"));
                s1.put("demi", result1.getString("notificationDate"));
                s1.put("dexp", result1.getString("dueDate"));
                s1.put("uti", result1.getString("issuerOffice"));
                list.add(i, (HashMap) ((HashMap) s1).clone());
                s1.clear();
            }
            s2.put("success", "01");
            s2.put("data", list);
            return s2;
        } else {
            s2.put("success", "01");
            s2.put("data", list);
            return s2;
        }
    }
}
