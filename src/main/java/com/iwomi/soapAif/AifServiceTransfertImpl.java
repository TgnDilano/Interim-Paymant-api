package com.iwomi.soapAif;

import com.google.gson.Gson;
import com.iwomi.config.SoapUtil;
import com.iwomi.model.Nomenclature;
import com.iwomi.repository.NomenclatureRepository;
import com.iwomi.soapAif.createTransfer.AifTransfert;
import com.iwomi.soapAif.createTransfer.CreditAcc;
import com.iwomi.soapAif.createTransfer.DebitAcc;
import com.iwomi.soapAif.transfer.CreateTransferResponse;
import com.iwomi.soapAif.transfer.TransferEnvelope;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AifServiceTransfertImpl {
    @Autowired
    NomenclatureRepository nomenclatureRepository;

    public Map<String, Object> getTransferDetail(AifTransfert payload) throws JAXBException {
        Map<String, Object> result = new HashMap<>();
        String reqId = SoapUtil.generateRandomId(7);
        String soapAction = "createTransfer";
        DebitAcc debitAcc1 = payload.getDebitAcc();

        JSONObject debitAcc = new JSONObject();
        debitAcc.put("cpt", debitAcc1.getCpt());
        debitAcc.put("age", debitAcc1.getAge());
        debitAcc.put("mnt", debitAcc1.getMnt());
        debitAcc.put("desp", debitAcc1.getDesc());

        System.out.println("yvoyvo test debit: " + payload.getDebitAcc().toString());

        List<CreditAcc> cr = payload.getCreditAcc();
        JSONArray arrCred = new JSONArray();
        int i = 0;
        for (CreditAcc cd : cr) {
            JSONObject cc = new JSONObject();
            cc.put("age", cd.getAge());
            cc.put("cpt", cd.getCpt());
            cc.put("mnt", cd.getMnt());
            cc.put("desp", cd.getDesc());
            arrCred.put(i, cc);
            i++;
        }

        String dateval = payload.getDateval();
        String ref = payload.getPnbr();
        String op_type = payload.getOptype();

        String soapEndpointUrl = "https://10.100.30.57:8095/smgv101/createTransfer";

        Nomenclature no = nomenclatureRepository.findTabcdAndAcsd("0012", "5002", "0");
        if (no != null) {
            soapEndpointUrl = no.getLib2();
        }

        SOAPMessage response = callSoapWebServiceTransfertDetails(soapEndpointUrl, soapAction, reqId, ref, op_type, debitAcc, arrCred, dateval);

        String xmlData = SoapUtil.convertSoapMessageToString(response);
        if (xmlData == null) return SoapUtil.responseWithError("Couldn't parse Soap response message");

        JAXBContext jaxbContext = JAXBContext.newInstance(TransferEnvelope.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        StringReader reader = new StringReader(xmlData);
        TransferEnvelope transfer = (TransferEnvelope) jaxbUnmarshaller.unmarshal(reader);

        System.out.println("Create Transfer envelope: " + transfer.toString());

        String sta = transfer.body.createTransferResponseFlow.responseStatus.statusCode;
        CreateTransferResponse createTransferResponse = transfer.body.createTransferResponseFlow.createTransferResponse;

        if (sta.equalsIgnoreCase("0") && createTransferResponse != null) {
            result.put("status", "01");
            result.put("data", new Gson().toJson(transfer));
            result.put("message", "Operation Successful");
        } else {
            result.put("status", "100");
            result.put("data", xmlData);
            result.put("message", "Operation not Successful");
        }
        return result;
    }

    public SOAPMessage callSoapWebServiceTransfertDetails(String soapEndpointUrl, String soapAction, String reqId, String ref, String op_type, JSONObject debitAcc, JSONArray arrCred, String dateval) {
        System.out.println("Request SOAP URL TRANSFER:  " + soapEndpointUrl);

        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            // Send SOAP Message to SOAP Server
            Map<String, String> val = getPain001conf();//
            //JSONArray arrCred=new JSONArray();//age,cpt
            String motif = op_type + " " + ref;

            int amtTotal = 0;
            System.out.println("Request SOAP URL TRANSFER: SEND00 ");
            String NbrTransaction = String.valueOf(arrCred.length());
            for (int i = 0; i < arrCred.length(); i++) {
                JSONObject jo = arrCred.getJSONObject(i);
                JSONObject o2 = new JSONObject();
                System.out.println(jo.getString("mnt"));
                o2.put("agence", jo.getString("age"));  // ici le nom du client sera l equivalent de son numero de carte.
                o2.put("compte", jo.getString("cpt")); // agence
                o2.put("cle", "xxx"); // compte
                o2.put("nom_client", jo.getString("desp"));  // montant
                o2.put("montant", String.valueOf(Integer.parseInt(jo.getString("mnt"))));  // montant
                o2.put("lib", "");  // libelle
                // credit_save.put(i, o2);
                amtTotal += Integer.parseInt(jo.getString("mnt"));  //  field 6
            }

            String listCredited = getComptesAcrediter(val, motif, arrCred, ref, String.valueOf(amtTotal));
            System.out.println("Request SOAP URLTRANSFER: SEND001 ");
            String bodyDebited = pain001DebitedAccount(val, debitAcc, reqId, NbrTransaction, String.valueOf(amtTotal), dateval);//compe a crediter
            String PmtInfList = pain001paymentInf(bodyDebited, listCredited);
            SOAPMessage request = createSoapEnvelopeTransfert(soapAction, reqId, reqId, NbrTransaction, String.valueOf(amtTotal), PmtInfList, val);
            System.out.println("Request SOAP MessageTESTYVO00: SEND");

            SoapUtil.trustAllHosts(); // Allow Connection to all parties. Remove SSL security
            SOAPMessage soapResponse = soapConnection.call(request, soapEndpointUrl);

            System.out.println("Response SOAP Message01:");

            soapConnection.close();

            return soapResponse;
        } catch (Exception ex) {
            Logger.getLogger(AifServiceTransfertImpl.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            return null;
        }
    }

    public SOAPMessage createSoapEnvelopeTransfert(String soapAction, String number, String reqId, String NbrTransaction, String total, String PmtInfList, Map<String, String> val) throws SOAPException, IOException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", "\"" + soapAction + "\"");
        System.out.println("Request SOAP createTransfer:");
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        // envelope.ad
        envelope.addNamespaceDeclaration("amp", "http://soprabanking.com/amplitude");
        System.out.println("Request SOAP createTransferVO03:");

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        // soapBody.setPrefix("soapenv");
        //  soapBody.addChildElement("testYVO", "amp");
        SOAPElement soapBodyElem = soapBody.addChildElement("createTransferRequestFlow", "amp");
        // soapBodyElem.setPrefix("amp");
        SOAPElement soapBodyElemHead = soapBodyElem.addChildElement("requestHeader", "amp");

        System.out.println("Request SOAP MessageTESTYVO0---:");
        SOAPElement requestId = soapBodyElemHead.addChildElement("requestId", "amp");
        requestId.setValue(number);
        // requestId.
        SOAPElement serviceName = soapBodyElemHead.addChildElement("serviceName", "amp");
        serviceName.setValue("createTransfer");

        SOAPElement timestamp = soapBodyElemHead.addChildElement("timestamp", "amp");
        String dt = SoapUtil.getFormedDate();
        timestamp.setValue(dt);

        SOAPElement userCode = soapBodyElemHead.addChildElement("userCode", "amp");
        userCode.setValue("YSAKI");
        System.out.println("Request SOAP MessageTESTYVO03--0:");
        SOAPElement soapBodycreateTransferRequestFlow = soapBodyElem.addChildElement("createTransferRequest", "amp");
        // soapBodyElemrRequest.setPrefix("amp");
        SOAPElement canal = soapBodycreateTransferRequestFlow.addChildElement("canal", "amp");
        canal.setValue("CANALSMGDEV");
        SOAPElement pain001 = soapBodycreateTransferRequestFlow.addChildElement("pain001", "amp");
        String getCreDtTm = dt;
        String pain001f = "<![CDATA[<?xml version='1.0' encoding='UTF-8'?>" +
                "<Document xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB\">\n" +
                "<CstmrCdtTrfInitn>\n" +
                "<GrpHdr>\n" +
                "<MsgId>" + reqId + "</MsgId>\n" +
                "<CreDtTm>" + getCreDtTm + "</CreDtTm>\n" +
                "<NbOfTxs>" + NbrTransaction + "</NbOfTxs>\n" +
                "<CtrlSum>" + total + "</CtrlSum>\n" +
                "<InitgPty/>\n" +
                "<DltPrvtData>\n" +
                "<FlwInd>" + val.get("DltPrvtDataFlwInd") + "</FlwInd>\n" +
                "<DltPrvtDataDtl>\n" +
                "<PrvtDtInf>" + val.get("DltPrvtDataDtlPrvtDtInf") + "</PrvtDtInf>\n" +
                "<Tp>\n" +
                "<CdOrPrtry>\n" +
                "<Cd>" + val.get("DltPrvtDataDtlCdOrPrtry") + "</Cd>\n" +
                "</CdOrPrtry>\n" +
                "</Tp>\n" +
                "</DltPrvtDataDtl>\n" +
                "</DltPrvtData>\n" +
                "</GrpHdr>\n" +
                PmtInfList +
                "</CstmrCdtTrfInitn>\n" +
                "</Document>\n" +
                "]]>";
        pain001.setValue(pain001f);//
        //envelope.removeNamespaceDeclaration("SOAP-ENV");
        soapMessage.saveChanges();

        /* Print the request message, just for debugging purposes */
        System.out.println("Request SOAP transfert MessageTESTYVO: ");
        soapMessage.writeTo(System.out);
        System.out.println("\n");
        return soapMessage;
    }

    public static String pain001paymentInf(String bodyDebited, String listCredited) {
        return "<PmtInf>\n" + bodyDebited + listCredited + "</PmtInf>\n";
    }

    public String pain001DebitedAccount(Map<String, String> val, JSONObject obj, String reqId, String NbrTransaction, String total, String dateval) {

        return "<PmtInfId>" + reqId + "</PmtInfId>\n" +
                "      <PmtMtd>" + val.get("PmtInfPmtMtd") + "</PmtMtd>\n" +
                "      <BtchBookg>" + val.get("PmtInfBtchBookg") + "</BtchBookg>\n" +
                "      <NbOfTxs>" + NbrTransaction + "</NbOfTxs>\n" +
                "      <CtrlSum>" + total + "</CtrlSum>\n" +
                "      <DltPrvtData>\n" +
                "        <OrdrPrties>\n" +
                "          <Tp>" + val.get("DltPrvtDataTp") + "</Tp>\n" +
                "          <Md>" + val.get("DltPrvtDataMd") + "</Md>\n" +
                "        </OrdrPrties>\n" +
                "      </DltPrvtData>\n" +
                "      <ReqdExctnDt>" + dateval + "</ReqdExctnDt>\n" +
                "      <Dbtr>\n" +
                "        <Nm>" + obj.getString("desp") + "</Nm>\n" +
                "      </Dbtr>\n" +
                "      <DbtrAcct>\n" +
                "        <Id>\n" +
                "          <Othr>\n" +
                "            <Id>" + obj.getString("cpt") + "</Id>\n" +
                "            <SchmeNm>\n" +
                "              <Prtry>" + val.get("DbtrAcctSchmeNmPrtry") + "</Prtry>\n" +
                "            </SchmeNm>\n" +
                "          </Othr>\n" +
                "        </Id>\n" +
                "        <Ccy>" + val.get("DbtrAcctCcy") + "</Ccy>\n" +
                "      </DbtrAcct>\n" +
                "      <DbtrAgt>\n" +
                "        <FinInstnId>\n" +
                "          <Nm>" + val.get("DbtrAgtFinInstnIdNm") + "</Nm>\n" +
                "          <Othr>\n" +
                "            <Id>" + val.get("FinInstnIdOthrId") + "</Id>\n" +
                "            <SchmeNm>\n" +
                "              <Prtry>" + val.get("FinInstnIdOthrSchmeNmPrtry") + "</Prtry>\n" +
                "            </SchmeNm>\n" +
                "          </Othr>\n" +
                "        </FinInstnId>\n" +
                "        <BrnchId>\n" +
                "          <Id>" + obj.getString("age") + "</Id>\n" +
                "          <Nm>Agence</Nm>\n" +
                "        </BrnchId>\n" +
                "      </DbtrAgt>\n";
    }


    public static String getComptesAcrediter(Map<String, String> val, String motif, JSONArray arr, String ref, String total) {
        String V = "";
        for (int i = 0; i < arr.length(); i++) {
            JSONObject jo = arr.getJSONObject(i);

            V = V + "<CdtTrfTxInf>\n" +
                    "        <PmtId>\n" +
                    "          <InstrId>" + ref + "</InstrId>\n" +
                    "          <EndToEndId>" + "IW" + ref + "</EndToEndId>\n" +
                    "        </PmtId>\n" +
                    "        <PmtTpInf>\n" +
                    "          <InstrPrty>NORM</InstrPrty>\n" +
                    "          <SvcLvl>\n" +
                    "            <Prtry>INTERNAL</Prtry>\n" +
                    "          </SvcLvl>\n" +
                    "        </PmtTpInf>\n" +
                    "        <Amt>\n" +
                    "          <InstdAmt Ccy=" + '"' + val.get("DbtrAcctCcy") + '"' + ">" + total + "</InstdAmt>\n" +
                    "        </Amt>\n" +
                    "        <ChrgAmt>\n" +
                    "          <Amt Ccy=" + '"' + val.get("DbtrAcctCcy") + '"' + ">1.0000</Amt>\n" +
                    "          <ChrgTp>FRAVRT</ChrgTp>\n" +
                    "        </ChrgAmt>\n" +
                    "        <CdtrAgt>\n" +
                    "          <FinInstnId>\n" +
                    "            <Nm>BANQUE BICEC</Nm>\n" +
                    "            <Othr>\n" +
                    "              <Id>85000</Id>\n" +
                    "              <SchmeNm>\n" +
                    "                <Prtry>ITF_DELTAMOP_IDETAB</Prtry>\n" +
                    "              </SchmeNm>\n" +
                    "            </Othr>\n" +
                    "          </FinInstnId>\n" +
                    "          <BrnchId>\n" +
                    "            <Id>" + jo.getString("age") + "</Id>\n" +
                    "            <Nm>Agence</Nm>\n" +
                    "          </BrnchId>\n" +
                    "        </CdtrAgt>\n" +
                    "        <Cdtr>\n" +
                    "          <Nm>HAMO</Nm>\n" +
                    "        </Cdtr>\n" +
                    "        <CdtrAcct>\n" +
                    "          <Id>\n" +
                    "            <Othr>\n" +
                    "              <Id>" + jo.getString("cpt") + "</Id>\n" +
                    "              <SchmeNm>\n" +
                    "                <Prtry>BKCOM_ACCOUNT</Prtry>\n" +
                    "              </SchmeNm>\n" +
                    "            </Othr>\n" +
                    "          </Id>\n" +
                    "          <Ccy>XAF</Ccy>\n" +
                    "        </CdtrAcct>" +
                    "<RmtInf>\n" +
                    " <Ustrd>TEST</Ustrd>\n" +
                    " </RmtInf>\n" +
                    "</CdtTrfTxInf>\n";
        }
        return V;
    }

    /*
        public static String getComptesAcrediterOld(Map<String, String> val , String motif, JSONArray arr, String ref){

            String  V ="";
            CdtTrfTxInf
            for(int i=0;i<arr.length();i++){
                JSONObject jo= arr.getJSONObject(i);

                V =V+"<CdtTrfTxInf>\n" +
                        "<PmtId>\n" +
                        "<InstrId>"+ref+"</InstrId>\n" +
                        "<EndToEndId>"+"B2W_"+ref+"</EndToEndId>\n" +
                        "</PmtId>\n" +
                        "<Amt>\n" +
                        "<InstdAmt Ccy="+'"'+val.get("DbtrAcctcc")+'"'+">"+jo.getString("mnt")+"</InstdAmt>\n" +
                        "</Amt>\n" +
                        "<CdtrAgt>\n" +
                        "<FinInstnId>\n" +
                        "<Nm>"+val.get("FinInstnIdNm")+"</Nm>\n" +
                        "<Othr>\n" +
                        "<Id>"+val.get("FinInstnIdId")+"</Id>\n" +
                        "<SchmeNm>\n" +
                        "<Prtry>"+val.get("DbtrAgtSchmeNm")+"</Prtry>\n" +
                        "</SchmeNm>\n" +
                        "</Othr>\n" +
                        "</FinInstnId>\n" +
                        "<BrnchId>\n" +
                        "<Id>"+jo.getString("age")+"</Id>\n" +
                        "<Nm>Agence</Nm>\n" +
                        "</BrnchId>\n" +
                        "</CdtrAgt>\n" +
                        "<Cdtr/>\n" +
                        "<CdtrAcct>\n" +
                        "<Id>\n" +
                        "<Othr>\n" +
                        "<Id>"+jo.getString("cpt")+"</Id>\n" +
                        "<SchmeNm>\n" +
                        "<Prtry>"+val.get("DbtrAcctSchmeNm")+"</Prtry>\n" +
                        "</SchmeNm>\n" +
                        "</Othr>\n" +
                        "</Id>\n" +
                        "<Ccy>"+val.get("DbtrAcctcc")+"</Ccy>\n" +
                        "</CdtrAcct>\n" +
                        "<RmtInf>\n" +
                        "<Ustrd>"+motif+"</Ustrd>\n" +
                        "</RmtInf>\n" +
                        "</CdtTrfTxInf>\n";
            }

            return V;

        }
    */

    public Map<String, String> getPain001conf() throws JSONException {
        Map<String, String> painContent = new HashMap<String, String>();

        painContent.put("DltPrvtDataFlwInd", "");
        painContent.put("DltPrvtDataDtlPrvtDtInf", "");
        painContent.put("DltPrvtDataDtlCdOrPrtry", "");
        painContent.put("PmtInfPmtMtd", "");
        painContent.put("PmtInfBtchBookg", "");
        painContent.put("DltPrvtDataTp", "");
        painContent.put("DltPrvtDataMd", "");
        painContent.put("PmtTpInfInstrPrty", "");
        painContent.put("SvcLvlPrtry", "");
        painContent.put("DbtrAcctSchmeNmPrtry", "");
        painContent.put("DbtrAcctcc", "");
        painContent.put("DbtrAcctSchmeNm", "");
        painContent.put("DbtrAgtSchmeNm", "");
        painContent.put("myNamespace", "");
        painContent.put("myNamespaceURI", "");
        painContent.put("FinInstnIdNm", "");
        painContent.put("FinInstnIdId", "");
        painContent.put("soapEndpointUrl", "");

        String PAIN_TABLE = "5011";
        List<Nomenclature> nom = nomenclatureRepository.findTabcdAndDel(PAIN_TABLE, "0");

        for (Nomenclature nomenclature : nom) {
            if (nomenclature.getAcscd().equalsIgnoreCase("0001")) {//FlwInd
                painContent.put("DltPrvtDataFlwInd", nomenclature.getLib3());
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0002")) {//PrvtDtInf
                painContent.put("DltPrvtDataDtlPrvtDtInf", nomenclature.getLib3());
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0003")) {//CdOrPrtry-CD
                painContent.put("DltPrvtDataDtlCdOrPrtry", nomenclature.getLib3());
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0004")) {
                painContent.put("PmtInfPmtMtd", nomenclature.getLib3()); // Le compte tva
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0005")) {
                painContent.put("PmtInfBtchBookg", nomenclature.getLib3()); // Le compte tva
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0006")) {
                painContent.put("DltPrvtDataTp", nomenclature.getLib3()); // Le compte tva
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0007")) {
                painContent.put("DltPrvtDataMd", nomenclature.getLib3()); // Le compte tva
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0008")) {
                painContent.put("PmtTpInfInstrPrty", nomenclature.getLib3()); // Le compte tva
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0009")) {
                painContent.put("SvcLvlPrtry", nomenclature.getLib3()); // Le compte tva
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0010")) {
                painContent.put("DbtrAcctSchmeNmPrtry", nomenclature.getLib3()); // Le compte tva
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0011")) {
                painContent.put("DbtrAcctCcy", nomenclature.getLib3()); // Le compte tva
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0012")) {
                painContent.put("DbtrAgtFinInstnIdNm", nomenclature.getLib3()); // Le compte tva
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0013")) {
                painContent.put("FinInstnIdOthrId", nomenclature.getLib3()); // Le compte tva
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0014")) {
                painContent.put("FinInstnIdOthrSchmeNmPrtry", nomenclature.getLib3()); // Le compte tva
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0015")) {
                painContent.put("myNamespaceURI", nomenclature.getLib3()); // Le compte tva
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0016")) {
                painContent.put("FinInstnIdNm", nomenclature.getLib3()); // Le compte tva
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0017")) {
                painContent.put("FinInstnIdId", nomenclature.getLib3()); // Le compte tva
            }
            if (nomenclature.getAcscd().equalsIgnoreCase("0018")) {
                painContent.put("soapEndpointUrl", nomenclature.getLib3()); // Le compte tva
            }
        }
        return painContent;
    }
}
