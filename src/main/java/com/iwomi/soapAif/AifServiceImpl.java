package com.iwomi.soapAif;

import com.google.gson.Gson;
import com.iwomi.config.SoapUtil;
import com.iwomi.model.AccountDetailPayload;
import com.iwomi.model.Nomenclature;
import com.iwomi.repository.NomenclatureRepository;
import com.iwomi.soapAif.historymodel.AccountHistoryEnvelope;
import com.iwomi.soapAif.historymodel.GetAccountHistoryMovementListResponse;
import com.iwomi.soapAif.infomodel.AccountDetailEnvelope;
import com.iwomi.soapAif.infomodel.GetAccountDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author TAGNE
 */

@Service
@RequiredArgsConstructor
public class AifServiceImpl {
    @Autowired
    NomenclatureRepository nomenclatureRepository;

    public SOAPMessage callSoapWebService(String soapEndpointUrl, SOAPMessage request) {
        System.out.println("soapEndpointUrl: " + soapEndpointUrl);

        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance(); // Create SOAP Connection
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            SoapUtil.trustAllHosts(); // Allow Connection to all parties. Remove SSL security
            SOAPMessage soapResponse = soapConnection.call(request, soapEndpointUrl); // Obtain response from soap message
            soapConnection.close();

            return soapResponse;
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            System.out.println("Error: " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    public Map<String, Object> getAccountDetail(AccountDetailPayload payload) {
        try {
            String soapAction = "getAccountDetail";
            String requestId = SoapUtil.generateRandomId(5);

            String soapEndpointUrl = "https://10.100.30.57:8095/smgv101/getAccountDetail";
            String userCode = "YSAKI";

            Nomenclature urlNomen = nomenclatureRepository.findTabcdAndAcsd("0012", "5001", "0");
            Nomenclature UserCodeNomen = nomenclatureRepository.findTabcdAndAcsd("0012", "5005", "0");

            if (urlNomen != null) {
                soapEndpointUrl = urlNomen.getLib2();
            }

            if (UserCodeNomen != null) {
                userCode = UserCodeNomen.getLib2();
            }

            SOAPMessage message = createSoapEnvelopeAccountDetails(soapAction, requestId, payload.age, payload.cpt, payload.dev, userCode);
            SOAPMessage response = callSoapWebService(soapEndpointUrl, message);

            String xmlData = SoapUtil.convertSoapMessageToString(response);

            if (xmlData == null) return SoapUtil.responseWithError("Couldn't parse Soap response message");

            JAXBContext jaxbContext = JAXBContext.newInstance(AccountDetailEnvelope.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader(xmlData);
            AccountDetailEnvelope accDetail = (AccountDetailEnvelope) jaxbUnmarshaller.unmarshal(reader);

            System.out.println("Account Detail envelope: " + accDetail.toString());

            String status = accDetail.getBody().getGetAccountDetailResponseFlow().getResponseStatus().getStatusCode();
            GetAccountDetailResponse accountRes = accDetail.getBody().getGetAccountDetailResponseFlow().getGetAccountDetailResponse();

            Map<String, Object> result = new HashMap<>();
            if (status.equalsIgnoreCase("0") && accountRes != null) {
                result.put("status", "01");
                result.put("data", new Gson().toJson(accDetail));
                result.put("message", "Operation Successful");
            } else {
                result.put("status", "100");
                result.put("data", xmlData);
                result.put("message", "Operation Failed");
            }
            return result;
        } catch (Exception ex) {
            Logger.getLogger(AifServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return SoapUtil.responseWithError("Exception caught");
        }
    }

    public Map<String, Object> getAccountHistory(Map<String, String> payload) {
        Map<String, Object> result = new HashMap<>();

        try {
            String number = SoapUtil.generateRandomId(6);
            String soapAction = "getAccountHistoryMovementList";
            String dateDebut = payload.get("startDate");
            String dateFin = payload.get("endDate");
            String age = payload.get("age");
            String cpt = payload.get("cpt");
            String dev = payload.get("dev");

            String soapEndpointUrl = "https://10.100.30.57:8095/smgv101/getAccountHistoryMovementList";
            String userCode = "YSAKI";

            Nomenclature urlNomen = nomenclatureRepository.findTabcdAndAcsd("0012", "5003", "0");
            Nomenclature UserCodeNomen = nomenclatureRepository.findTabcdAndAcsd("0012", "5005", "0");

            if (urlNomen != null) {
                soapEndpointUrl = urlNomen.getLib2();
            }

            if (UserCodeNomen != null) {
                userCode = UserCodeNomen.getLib2();
            }

            SOAPMessage message = createSoapEnvelopeAccountHistory(soapAction, number, age, cpt, dev, dateDebut, dateFin, userCode);
            SOAPMessage response = callSoapWebService(soapEndpointUrl, message);

            String xmlData = SoapUtil.convertSoapMessageToString(response);

            if (xmlData == null) return SoapUtil.responseWithError("Couldn't parse Soap response message");

            JAXBContext jaxbContext = JAXBContext.newInstance(AccountHistoryEnvelope.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader(xmlData);
            AccountHistoryEnvelope hist = (AccountHistoryEnvelope) jaxbUnmarshaller.unmarshal(reader);

            System.out.println("Account hist envelope: " + hist.toString());

            String sta = hist.getBody().getGetAccountHistoryMovementListResponseFlow().getResponseStatus().getStatusCode();
            GetAccountHistoryMovementListResponse actHisMovListRes = hist.getBody().getGetAccountHistoryMovementListResponseFlow().getGetAccountHistoryMovementListResponse();

            if (sta.equalsIgnoreCase("0") && actHisMovListRes != null) {
                result.put("status", "01");
                result.put("data", hist);
                result.put("message", "Operation Successful");
            } else {
                result.put("status", "100");
                result.put("data", xmlData);
                result.put("message", "Operation Successful");
            }
            return result;
        } catch (Exception ex) {
            Logger.getLogger(AifServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return SoapUtil.responseWithError("Exception caught");
        }
    }

    public SOAPMessage createSoapEnvelopeAccountDetails(String soapAction, String number, String age, String cpt, String devise, String userCode) throws SOAPException, IOException {
        System.out.println("Request SOAP MessageTESTYVO01:");

        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", "\"" + soapAction + "\"");

        SOAPEnvelope envelope = soapPart.getEnvelope();

        envelope.addNamespaceDeclaration("amp", "http://soprabanking.com/amplitude");

        System.out.println("Request SOAP MessageTESTYVO03:");

        SOAPBody soapBody = envelope.getBody();

        SOAPElement soapBodyElem = soapBody.addChildElement("getAccountDetailRequestFlow", "amp");
        SOAPElement soapBodyElemHead = soapBodyElem.addChildElement("requestHeader", "amp");

        SOAPElement requestId = soapBodyElemHead.addChildElement("requestId", "amp");
        requestId.setValue(number);

        SOAPElement serviceName = soapBodyElemHead.addChildElement("serviceName", "amp");
        serviceName.setValue("getAccountDetail");

        SOAPElement timestamp = soapBodyElemHead.addChildElement("timestamp", "amp");

        String dt = SoapUtil.getFormedDate();
        timestamp.setValue(dt);

        SOAPElement userCodeElement = soapBodyElemHead.addChildElement("userCode", "amp");
        userCodeElement.setValue(userCode);

        SOAPElement soapBodyElemrRequest = soapBodyElem.addChildElement("getAccountDetailRequest", "amp");

        SOAPElement soapBodyElemrRequestIdentifier = soapBodyElemrRequest.addChildElement("accountIdentifier", "amp");

        SOAPElement branch = soapBodyElemrRequestIdentifier.addChildElement("branch", "amp");
        branch.setValue(age);

        SOAPElement currency = soapBodyElemrRequestIdentifier.addChildElement("currency", "amp");
        currency.setValue(devise);

        SOAPElement account = soapBodyElemrRequestIdentifier.addChildElement("account", "amp");
        account.setValue(cpt);

        soapMessage.saveChanges();

        /* Print the request message, just for debugging purposes */
        System.out.println("Request SOAP MessageTESTYVO: ");
        soapMessage.writeTo(System.out);
        System.out.println("\n");

        return soapMessage;
    }

    public SOAPMessage createSoapEnvelopeAccountHistory(String soapAction, String number, String age, String cpt, String devise, String dateDebut, String dateFin, String userCode) throws SOAPException, IOException {
        System.out.println("Request SOAP AccountHistory:");

        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", "\"" + soapAction + "\"");
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("amp", "http://soprabanking.com/amplitude");

        System.out.println("Request SOAP MessageTESTYVO03:");

        SOAPBody soapBody = envelope.getBody();

        SOAPElement soapBodyElem = soapBody.addChildElement("getAccountHistoryMovementListRequestFlow", "amp");
        SOAPElement soapBodyElemHead = soapBodyElem.addChildElement("requestHeader", "amp");

        /* Build Request Header  */
        SOAPElement requestId = soapBodyElemHead.addChildElement("requestId", "amp");
        requestId.setValue(number);

        SOAPElement serviceName = soapBodyElemHead.addChildElement("serviceName", "amp");
        serviceName.setValue("getAccountHistoryMovement");

        SOAPElement timestamp = soapBodyElemHead.addChildElement("timestamp", "amp");
        String dt = SoapUtil.getFormedDate();
        timestamp.setValue(dt);

        SOAPElement userCodeElement = soapBodyElemHead.addChildElement("userCode", "amp");
        userCodeElement.setValue(userCode);
        SOAPElement soapBodyElementRequest = soapBodyElem.addChildElement("getAccountHistoryMovementListRequest", "amp");

        /* Build Children for account identifier */
        SOAPElement soapBodyElementRequestIdentifier = soapBodyElementRequest.addChildElement("accountIdentifier", "amp");

        SOAPElement branch = soapBodyElementRequestIdentifier.addChildElement("branch", "amp");
        branch.setValue(age);

        SOAPElement currency = soapBodyElementRequestIdentifier.addChildElement("currency", "amp");
        currency.setValue(devise);

        SOAPElement account = soapBodyElementRequestIdentifier.addChildElement("account", "amp");
        account.setValue(cpt);

        /* Build Data for start and end date  */
        SOAPElement soapBodyElementStartDate = soapBodyElementRequest.addChildElement("startDate", "amp");
        soapBodyElementStartDate.setValue(dateDebut);

        if (dateFin != null) {
            SOAPElement soapBodyElementEndDate = soapBodyElementRequest.addChildElement("endDate", "amp");
            soapBodyElementEndDate.setValue(dateFin);
        }

        soapMessage.saveChanges();

        /* Print the request message, just for debugging purposes */
        System.out.println("Request SOAP MessageTESTYVO: " + soapMessage);
        soapMessage.writeTo(System.out);
        System.out.println("\n");
        return soapMessage;
    }
}
