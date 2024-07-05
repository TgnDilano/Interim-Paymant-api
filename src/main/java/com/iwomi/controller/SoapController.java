package com.iwomi.controller;

import com.iwomi.model.AccountDetailPayload;
import com.iwomi.soapAif.AifServiceImpl;
import com.iwomi.soapAif.AifServiceTransfertImpl;
import com.iwomi.soapAif.createTransfer.AifTransfert;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


@RestController
@RequestMapping("*")

/**
 *
 * @author TAGNE
 */
public class SoapController {
    AifServiceImpl aifServiceImpl;
    AifServiceTransfertImpl aifServiceTransfertImpl;

    public SoapController(AifServiceImpl aifServiceImpl, AifServiceTransfertImpl aifServiceTransfertImpl) {
        this.aifServiceImpl = aifServiceImpl;
        this.aifServiceTransfertImpl = aifServiceTransfertImpl;
    }

    @RequestMapping(value = "/getAccountDetail", method = RequestMethod.POST)
    public Map<String, Object> getAccountDetailC(@RequestBody AccountDetailPayload payload) {
        System.out.println("/getAccountDetail payload: " + payload);
        return this.aifServiceImpl.getAccountDetail(payload);
    }

    @RequestMapping(value = "/getAccountHistoryAif", method = RequestMethod.POST)
    public Map<String, Object> getAccountHistory(@RequestBody Map<String, String> payload) throws NoSuchAlgorithmException, SOAPException, JAXBException {
        System.out.println("/getAccountHistoryAif payload: " + payload);
        return this.aifServiceImpl.getAccountHistory(payload);
    }

    @RequestMapping(value = "/makeTransfertAif", method = RequestMethod.POST)
    public Map<String, Object> makeTransfertAif(@RequestBody AifTransfert payload) throws NoSuchAlgorithmException, SOAPException, JAXBException {
        System.out.println("/makeTransfertAif payload: " + new JSONObject(payload).toString());
        return this.aifServiceTransfertImpl.getTransferDetail(payload);
    }
}
