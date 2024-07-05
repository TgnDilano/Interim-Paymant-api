/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwomi.model.CamTrans;
import com.iwomi.model.DataVentile;
import com.iwomi.repository.NomenclatureRepository;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.iwomi.model.Nomenclature;
import com.iwomi.model.OnPaidObj;
import com.iwomi.model.VentileDetail;
import com.iwomi.repository.CamPayResp;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

/**
 *
 * @author yvo
 */
@Service
public class ServiceApiImpl {

    String apikey = "tRECAusiqD2y9Go0ZdCeZLSFCUZApa2o";
    int id = 1;
    String nreg = "CAMSIS";
    String dele = "0";
    RestTemplate restTemplate;

    @Autowired
    private NomenclatureRepository nomenclatureRepository;

//     @Autowired
//    private GfregRepository gfregRepository; 
//     
//   
// @Autowired
//    private GflopRepository gflopRepository;
//     
//    //1
    public String ListUnPaid(OnPaidObj onpaidObj) {
        String tabcd = "0012";
        String acscd = "0206"; //nm.getLib2()
        Nomenclature nm = nomenclatureRepository.findUrl1(tabcd, acscd, dele);
        final String url = nm.getLib2() + "list-unpaid-notice";
        System.out.println(url);
//        try {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("apikey", apikey);
        headers.set("Method", "POST");
        Map<String, Object> map = new HashMap<>();
        map.put("noticeNumber", onpaidObj.getNoticeNumber());
        map.put("notificationDate", onpaidObj.getNotificationDate());
        map.put("taxpayerNumber", onpaidObj.getTaxpayerNumber());
        map.put("taxpayerRepresentativeNumber", onpaidObj.getTaxpayerRepresentativeNumber());
        map.put("dueDate", onpaidObj.getDueDate());
        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        System.out.println(response);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Post Created");
            System.out.println(response.getBody());
        } else {
            System.out.println("Request Failed");
        }
        return response.getBody();
//        } catch (Exception exception) {
//            System.out.println("call To RestService Error :" + exception.getMessage());
//        }
//        return null;
    }

    static Map<String, String> statusSig() {
        final Map<String, String> statusPayment = new HashMap<String, String>();
        statusPayment.put("01", "Confirme");
        statusPayment.put("02", "Demande du double paiement");
        statusPayment.put("03", "Erreur sur le N° de l'A.P.");
        statusPayment.put("04", "L'A.P. déjà réglé");
        statusPayment.put("05", "L'A.P. annulé");
        statusPayment.put("06", " Erreur sur les informations du responsable financier");
        statusPayment.put("07", "Erreur sur le montant de l'A.P");
        return statusPayment;
    }

    @Autowired
    CamPayResp camPayResp;

    private JSONObject getNoticeDetail(JSONArray s, String key) {
        for (int i = 0; i < s.length(); i++) {
            if (s.getJSONObject(i).getString("noticeNumber").equals(key)) {
                return s.getJSONObject(i);
            }
        }
        System.out.println("abnormal case the array is :" + s + " and the key for noticenumber is " + key);
        return null;
    }

    //2
    public Map<String, Object> Payment1(CamTrans camTrans) {
        //final String url = "http://localhost:8080/api/epayment/payment-of-notice";
        String tabcd = "0012";
        String acscd = "0206"; //nm.getLib2()
        Map<String, Object> outp = new HashMap<>();
        Nomenclature nm = nomenclatureRepository.findUrl1(tabcd, acscd, dele);
        final String url = nm.getLib2() + "payment-of-notice";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        camTrans.setBankPaymentNumber(camTrans.getBankCode() + CurentDate01() + NumeroSerie());
        System.out.println("the code tebit generated is : " + camTrans.getBankPaymentNumber());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("apikey", apikey);
        headers.set("Method", "POST");
        //save transaction on our table
        camTrans.setStatus("1000");
        // build the request
        HttpEntity<CamTrans> entity = new HttpEntity<>(camTrans, headers);
        // send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        System.out.println(response);
        if (response.getStatusCode() == HttpStatus.OK) {
            camTrans = camPayResp.save(camTrans);
            JSONObject ss = new JSONObject(response.getBody());
            System.out.println("This is the value of return status");
            System.out.println("This is the value of return status"+ss.getString("result"));
            System.out.println("This is the value of return status"+ss.getString("result")=="S");
            if (ss.getString("result").equalsIgnoreCase("S")) {
                JSONArray notList = ss.getJSONArray("noticesList");
                camTrans.setPaymentResultCode(ss.getString("paymentResultCode"));
                camTrans.setPaymentResultMsg(ss.getString("paymentResultMsg"));
//            setting each 
                camTrans.getNoticeList().forEach(e -> {
                    JSONObject s = getNoticeDetail(notList, e.getNoticeNumber());
                    System.out.println("the value of s is :" + s);
                    e.setResultNoticeCode(s.getString("resultNoticeMsg"));
                    e.setResultNoticeCode(s.getString("resultNoticeCode"));
                });
                camTrans = camPayResp.save(camTrans);
                outp.put("success", "01");
                outp.put("message", ss.getString("paymentResultMsg"));
                outp.put("process_id", ss.getString("bankPaymentNumber"));
                outp.put("data", camTrans.getNoticeList());
            } else {

                camTrans.setPaymentResultCode(ss.getString("paymentResultCode"));
                camTrans.setPaymentResultMsg(ss.getString("paymentResultMsg"));
                camTrans = camPayResp.save(camTrans);
                outp.put("success", "100");
                outp.put("message", ss.getString("paymentResultMsg"));
                outp.put("process_id", ss.getString("bankPaymentNumber"));
                outp.put("data", camTrans.getNoticeList());
            }
        } else {
            System.out.println("Request Failed");
            System.out.println(response.getStatusCode());
            outp.put("success", "100");
            outp.put("message", "echec");
            outp.put("error", response.getStatusCode());
            outp.put("data", response.getBody());
        }

        return outp;
    }

    public DateFormat CurentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat;
    }

    public DateFormat CurentDate2() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat;
    }

    public DateFormat CurentDateTime1() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat;
    }

    public String CurentDateTime() {
        Timestamp dat = new Timestamp(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(dat);
    }

    public String CurentDate01() {
        Timestamp dat = new Timestamp(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(dat);
    }

    public int NumeroSerie() {
        int aNumber = 0;
        aNumber = (int) ((Math.random() * 900000) + 100000);
        return aNumber;
    }
//    
//     public String SavePaiement (String bankCode, Map<String, Object> data ){
//          String cod0="CAMCIS";
//            String cod3=cod0+CurentDate01()+NumeroSerie();
//       Double totalAmount= new Double(data.get("totalAmount").toString());
//       Double tv =1.00; Double tv1 =0.00;
//       String eta="1";String stu="1";
//        String dev="222"; String url="IWOMI";
//        Gfreg gfreg = new Gfreg();
//        gfreg.setEtab(url);// code etablissement
//        gfreg.setNui(data.get("taxPayerNumber").toString());//Numéro du responsable financier(NUI),
//        gfreg.setCfac(bankCode);//code facturier
//        gfreg.setCli(url); //Code client
//        gfreg.setNreg(nreg+CurentDate01()+NumeroSerie()); //Numero du reglement
//        gfreg.setMreg(totalAmount); //Montant du reglement
//        gfreg.setNfac(data.get("bankPaymentNumber").toString());//Numéro de la facture
//        gfreg.setNmor(url);//Numéro du moratoire / échéance
//        gfreg.setMfac(totalAmount);//Montant de la facture
//        gfreg.setMreg(totalAmount);
//        gfreg.setDev(dev);//Devise de la facture (code iso numérique)
//        gfreg.setTdev(tv);//Tau de devise
//        gfreg.setMres(tv1);//Montant restant
//        gfreg.setDreg(new Date(System.currentTimeMillis()));//Date règlement de la facture
//        gfreg.setCumreg(tv1); //Cumul des règlements
//        gfreg.setMdp(url);//Moyen de paiement
//        gfreg.setIdmdp(url);//Moyen de paiement
//        gfreg.setEta(cod3);
//        gfreg.setSta(stu);
//        gfreg.setUti(url);
//        gfreg.setUtimo(url);
//        gfreg.setDou(new Timestamp(System.currentTimeMillis()));
//	gfreg.setDmo(new Timestamp(System.currentTimeMillis()));
//        gfregRepository.save(gfreg);
//        Gflop gflop =new Gflop();
//                  gflop.setDele(gfreg.getDele());
//                  gflop.setSta(gfreg.getDele());
//                  gflop.setNlot(gfreg.getEta());
//                   gflop.setMon(gfreg.getMfac());
//                    gflop.setEtab(gfreg.getEtab());
//                     gflop.setNfac(gfreg.getCfac());
//                   gflop.setMpai(gfreg.getMreg());
//                   gflop.setMres(gfreg.getMres());
//                 gflop.setDou(new Timestamp(System.currentTimeMillis()));
//		 gflop.setDmo(new Timestamp(System.currentTimeMillis()));
//                 Gflop gflop1=gflopRepository.save(gflop);
//        return "{Status:01; Message: Sauvegarde reglement avec Success}";
//        //return url;
//    }

    //3 API Liste des avis de paiement réglés 
    public String ListPaid(OnPaidObj onPaidObj) {
        // final String url = "http://169.239.43.130//api/epayment/list-paid-notice";
        String tabcd = "0012";
        String acscd = "0206"; //nm.getLib2()
        Nomenclature nm = nomenclatureRepository.findUrl1(tabcd, acscd, dele);
        final String url = nm.getLib2() + "list-paid-notice";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("apikey", apikey);
        headers.set("Method", "POST");
        Map<String, Object> map = new HashMap<>();
        map.put("noticeNumber", onPaidObj.getNoticeNumber());
        map.put("notificationDate", onPaidObj.getNotificationDate());
        map.put("taxpayerNumber", onPaidObj.getTaxpayerNumber());
        map.put("taxpayerRepresentativeNumber", onPaidObj.getTaxpayerRepresentativeNumber());
        map.put("dueDate", onPaidObj.getDueDate());
        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        System.out.println(response);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Post Created");
            System.out.println(response.getBody());
        } else {
            System.out.println("Request Failed");
            System.out.println(response.getStatusCode());
        }

        return response.getBody();
    }

    //4 API Authentification d’un utilisateur sur CAMCIS
    public String AuthCamcis(String taxpayerNumber, String userEmail, String userCategory) {
        //final String url = "http://169.239.43.130//api/epayment/confirm-user-infos";
        String tabcd = "0012";
        String acscd = "0206"; //nm.getLib2()
        Nomenclature nm = nomenclatureRepository.findUrl1(tabcd, acscd, dele);
        String url = nm.getLib2() + "confirm-user-infos";
        System.out.println("the url of system");
        System.out.println(url);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("apikey", apikey);
        headers.set("Method", "POST");
        headers.set("Content-Type", "application/json");
        Map<String, Object> map = new HashMap<>();
        map.put("taxpayerNumber", taxpayerNumber);
        map.put("userEmail", userEmail);
        map.put("userCategory", userCategory);

        JSONObject req = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonResult = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(map);
            req = new JSONObject(jsonResult);
        } catch (JsonProcessingException ex) {
            System.out.println("error converting to json ");
        }

        System.out.println("object ");
        System.out.println(req);
        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        System.out.println(response);
        if (response.getStatusCode() == HttpStatus.CREATED) {
            System.out.println("Post Created");
            System.out.println(response.getBody());
        } else {
            System.out.println("Request Failed");
            System.out.println(response.getStatusCode());
        }

        return response.getBody();
    }

    //5 API Notification de la Ventilation 
    public String NotifVent(String bankPaymentNumber, String bankCode, String ventilationStatus, String ventilationMessage) {
        //final String url = "http://169.239.43.130/api/epayment/notify-ventilation";
        String tabcd = "0012";
        String acscd = "0206"; //nm.getLib2()
        Nomenclature nm = nomenclatureRepository.findUrl1(tabcd, acscd, dele);
        final String url = nm.getLib2() + "notify-ventilation";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("apikey", apikey);
        headers.set("Method", "POST");
        Map<String, Object> map = new HashMap<>();
        map.put("bankPaymentNumber", bankPaymentNumber);
        map.put("bankCode", bankCode);
        map.put("ventilationStatus", ventilationStatus);
        map.put("ventilationMessage", ventilationMessage);

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        System.out.println(response);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Post Created");
            System.out.println(response.getBody());
        } else {
            System.out.println("Request Failed");
            System.out.println(response.getStatusCode());
        }

        return response.getBody();
    }
    //5 API Notification de la Ventilation 

    public Map<String, Object> NotifVent1(CamTrans camTrans) {
        //final String url = "http://169.239.43.130/api/epayment/notify-ventilation";
        String tabcd = "0012";
        String acscd = "0206"; //nm.getLib2()
        Nomenclature nm = nomenclatureRepository.findUrl1(tabcd, acscd, dele);
        final String url = nm.getLib2() + "notify-ventilation";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("apikey", apikey);
        headers.set("Method", "POST");
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> outp = new HashMap<>();
        map.put("bankPaymentNumber", camTrans.getBankPaymentNumber());
        map.put("bankCode", camTrans.getBankCode());
        map.put("ventilationStatus", camTrans.getVentilationStatus());
        String sp = vantilation_message(camTrans);
        map.put("ventilationMessage", sp);
        System.out.println("request informationnn");
        System.out.println(map);
        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
        // send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject obj = new JSONObject(response.getBody());
            System.out.println("the out put is :" + response.getBody());
            camTrans.setResultMessage(obj.getString("resultMessage"));
//            camTrans.setResult(obj.getString("result"));
            camTrans.setStatus("01");
            camTrans.setVentilationMessage(sp);
            camTrans = camPayResp.save(camTrans);
            outp.put("data", camTrans);
            outp.put("success", "01");
            outp.put("message", obj.getString("resultMessage"));
        } else {
            outp.put("data", response.getBody());
            outp.put("success", "100");
        }

        return outp;
    }

    public String vantilation_message(CamTrans camTrans) {
        String sp = "";
        String mp = "";
        System.out.println("its the output problem");
        List<DataVentile> s = camTrans.getDataventile();
        for (int i = 0; i < s.size(); i++) {
            System.out.println("its the output problem dataventil :" + i);
            sp += "#" + s.get(i).getNoticeNumber() + "|";
            List<VentileDetail> r = s.get(i).getVentileDetail();
            for (int z = 0; z < r.size(); z++) {
                System.out.println("its the output problem details :" + z);
                String tt = (r.get(z).getCodeBeneficiair() == r.get(r.size() - 1).getCodeBeneficiair())
                        && (r.get(z).getResultTraitm() == r.get(r.size() - 1).getResultTraitm()) ? "" : "##";
                mp += r.get(z).getCodeBeneficiair() + "(" + r.get(z).getResultTraitm() + ")" + tt;
            }
            sp += mp;
            mp = "";
        }
        sp += "#";
        return sp;
    }

    //6 API de récupération des facture(APs) non réglés sélectionnés sur le portail e-Payment
    public String ApUnPaid(String taxpayerNumber) {
        // final String url = "http://169.239.43.130/api/epayment/retrieve-selected-unpaid-notice";
        String tabcd = "0012";
        String acscd = "0206"; //nm.getLib2()
        Nomenclature nm = nomenclatureRepository.findUrl1(tabcd, acscd, dele);
        final String url = nm.getLib2() + "retrieve-selected-unpaid-notice";
        System.out.printf("debut execution");
        System.out.printf(url);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("apikey", apikey);
        headers.set("Method", "POST");
        Map<String, Object> map = new HashMap<>();
        map.put("taxpayerNumber", taxpayerNumber);

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        System.out.println(response);
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Post Created");
            System.out.println(response.getBody());
        } else {
            System.out.println("Request Failed");
            System.out.println(response.getStatusCode());
        }

        return response.getBody();
    }

}
