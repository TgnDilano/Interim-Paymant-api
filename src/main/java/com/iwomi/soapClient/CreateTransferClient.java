/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.soapClient;

/**
 *
 * @author fabri
 */   
    
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.iwomi.DataBase;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.text.Document;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/// stream reader to string
//https://www.codota.com/code/java/methods/javax.xml.soap.SOAPMessage/writeTo
//https://stackoverflow.com/questions/32405343/java-created-soap-client-is-not-getting-response-but-same-is-working-from-soapui
public class CreateTransferClient {
    
    // SAAJ - SOAP Client Testing
    
//    public static void main(String args[]) {
//        /*
//            The example below requests from the Web Service at:
//             http://www.webservicex.net/uszip.asmx?op=GetInfoByCity
//            To call other WS, change the parameters below, which are:
//             - the SOAP Endpoint URL (that is, where the service is responding from)
//             - the SOAP Action
//               Also change the contents of the method createSoapEnvelope() in this class. It constructs
//               the inner part of the SOAP envelope that is actually sent.
//         */     
//      
////        String soapEndpointUrl = "http://172.20.20.68:8081/createTransfer";
//       // String soapEndpointUrl = "http://172.20.20.68:8081/getAccountActivity?wsdl";
////        String soapAction = "createTransfer";
//       // String soapAction = "http://172.20.20.68:8081/getAccountActivityRequestFlow";
//    //    callSoapWebService(soapEndpointUrl, soapAction);
//        String StatusCode ="";
//        String responseId ="";
//        String s ="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body><fjs1:createTransferResponseFlow xmlns:fjs1=\"http://soprabanking.com/amplitude\"><fjs1:responseHeader><fjs1:requestId>CCA20200520073952251IWCdySCd</fjs1:requestId><fjs1:responseId>413</fjs1:responseId><fjs1:timestamp>2020-05-20T07:27:37.28489+01:00</fjs1:timestamp><fjs1:serviceVersion>V1.0</fjs1:serviceVersion><fjs1:language><fjs1:code>001</fjs1:code><fjs1:designation>FRANCAIS</fjs1:designation></fjs1:language></fjs1:responseHeader><fjs1:responseStatus><fjs1:statusCode>0</fjs1:statusCode></fjs1:responseStatus><fjs1:createTransferResponse><fjs1:pain002>&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt;&lt;Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.002.001.03DB\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"&gt;\n" +
//"  &lt;CstmrPmtStsRpt&gt;\n" +
//"    &lt;GrpHdr&gt;\n" +
//"      &lt;MsgId&gt;RLCCAMCM20052000000371&lt;/MsgId&gt;\n" +
//"      &lt;CreDtTm&gt;2020-05-20T07:27:37&lt;/CreDtTm&gt;\n" +
//"      &lt;InitgPty&gt;\n" +
//"        &lt;Nm&gt;CREDIT COMMUNAUTAIRE D?AFRIQUE&lt;/Nm&gt;\n" +
//"        &lt;Id&gt;\n" +
//"          &lt;OrgId&gt;\n" +
//"            &lt;BICOrBEI&gt;CCAMCMCY&lt;/BICOrBEI&gt;\n" +
//"          &lt;/OrgId&gt;\n" +
//"        &lt;/Id&gt;\n" +
//"      &lt;/InitgPty&gt;\n" +
//"      &lt;DbtrAgt&gt;\n" +
//"        &lt;FinInstnId&gt;\n" +
//"          &lt;BIC&gt;CCAMCMCY&lt;/BIC&gt;\n" +
//"        &lt;/FinInstnId&gt;\n" +
//"        &lt;BrnchId&gt;\n" +
//"          &lt;Id&gt;10000&lt;/Id&gt;\n" +
//"        &lt;/BrnchId&gt;\n" +
//"      &lt;/DbtrAgt&gt;\n" +
//"      &lt;DltPrvtData&gt;\n" +
//"        &lt;FlwInd&gt;PROD&lt;/FlwInd&gt;\n" +
//"        &lt;DltPrvtDataDtl&gt;\n" +
//"          &lt;PrvtDtInf&gt;VIREMENT_SMG&lt;/PrvtDtInf&gt;\n" +
//"          &lt;Tp&gt;\n" +
//"            &lt;CdOrPrtry&gt;\n" +
//"              &lt;Cd&gt;CHANNEL&lt;/Cd&gt;\n" +
//"            &lt;/CdOrPrtry&gt;\n" +
//"          &lt;/Tp&gt;\n" +
//"        &lt;/DltPrvtDataDtl&gt;\n" +
//"      &lt;/DltPrvtData&gt;\n" +
//"    &lt;/GrpHdr&gt;\n" +
//"    &lt;OrgnlGrpInfAndSts&gt;\n" +
//"      &lt;OrgnlMsgId&gt;CCA20200520073952251IWCdySCd&lt;/OrgnlMsgId&gt;\n" +
//"      &lt;OrgnlMsgNmId&gt;PAIN.001.001.03&lt;/OrgnlMsgNmId&gt;\n" +
//"      &lt;OrgnlCreDtTm&gt;2020-05-20T07:39:52&lt;/OrgnlCreDtTm&gt;\n" +
//"      &lt;OrgnlNbOfTxs&gt;1&lt;/OrgnlNbOfTxs&gt;\n" +
//"      &lt;OrgnlCtrlSum&gt;1250&lt;/OrgnlCtrlSum&gt;\n" +
//"      &lt;OrgnlDltPrvtData&gt;\n" +
//"        &lt;FlwInd&gt;PROD&lt;/FlwInd&gt;\n" +
//"        &lt;DltPrvtDataDtl&gt;\n" +
//"          &lt;PrvtDtInf&gt;VIREMENT_SMG&lt;/PrvtDtInf&gt;\n" +
//"          &lt;Tp&gt;\n" +
//"            &lt;CdOrPrtry&gt;\n" +
//"              &lt;Cd&gt;CHANNEL&lt;/Cd&gt;\n" +
//"            &lt;/CdOrPrtry&gt;\n" +
//"          &lt;/Tp&gt;\n" +
//"        &lt;/DltPrvtDataDtl&gt;\n" +
//"        &lt;DltPrvtDataDtl&gt;\n" +
//"          &lt;PrvtDtInf&gt;MCCAMCM20052000000376&lt;/PrvtDtInf&gt;\n" +
//"          &lt;Tp&gt;\n" +
//"            &lt;CdOrPrtry&gt;\n" +
//"              &lt;Cd&gt;MOP_REFMSG&lt;/Cd&gt;\n" +
//"            &lt;/CdOrPrtry&gt;\n" +
//"          &lt;/Tp&gt;\n" +
//"        &lt;/DltPrvtDataDtl&gt;\n" +
//"      &lt;/OrgnlDltPrvtData&gt;\n" +
//"      &lt;GrpSts&gt;ACTC&lt;/GrpSts&gt;\n" +
//"      &lt;NbOfTxsPerSts&gt;\n" +
//"        &lt;DtldNbOfTxs&gt;1&lt;/DtldNbOfTxs&gt;\n" +
//"        &lt;DtldSts&gt;ACSP&lt;/DtldSts&gt;\n" +
//"        &lt;DtldCtrlSum&gt;1250&lt;/DtldCtrlSum&gt;\n" +
//"      &lt;/NbOfTxsPerSts&gt;\n" +
//"    &lt;/OrgnlGrpInfAndSts&gt;\n" +
//"    &lt;OrgnlPmtInfAndSts&gt;\n" +
//"      &lt;OrgnlPmtInfId&gt;CCA20200520073952251IWCdySCd&lt;/OrgnlPmtInfId&gt;\n" +
//"      &lt;OrgnlNbOfTxs&gt;1&lt;/OrgnlNbOfTxs&gt;\n" +
//"      &lt;OrgnlCtrlSum&gt;1250&lt;/OrgnlCtrlSum&gt;\n" +
//"      &lt;OrgnlDltPrvtData&gt;\n" +
//"        &lt;DltPrvtDataDtl&gt;\n" +
//"          &lt;PrvtDtInf&gt;SCCAMCM20052000000376&lt;/PrvtDtInf&gt;\n" +
//"          &lt;Tp&gt;\n" +
//"            &lt;CdOrPrtry&gt;\n" +
//"              &lt;Cd&gt;MOP_REFORDER&lt;/Cd&gt;\n" +
//"            &lt;/CdOrPrtry&gt;\n" +
//"          &lt;/Tp&gt;\n" +
//"        &lt;/DltPrvtDataDtl&gt;\n" +
//"        &lt;OrdrPrties&gt;\n" +
//"          &lt;Tp&gt;IMM&lt;/Tp&gt;\n" +
//"          &lt;Md&gt;CREATE&lt;/Md&gt;\n" +
//"        &lt;/OrdrPrties&gt;\n" +
//"      &lt;/OrgnlDltPrvtData&gt;\n" +
//"      &lt;PmtInfSts&gt;ACSP&lt;/PmtInfSts&gt;\n" +
//"      &lt;StsRsnInf&gt;\n" +
//"        &lt;Orgtr&gt;\n" +
//"          &lt;Nm&gt;CREDIT COMMUNAUTAIRE D?AFRIQUE&lt;/Nm&gt;\n" +
//"          &lt;Id&gt;\n" +
//"            &lt;OrgId&gt;\n" +
//"              &lt;BICOrBEI&gt;CCAMCMCY&lt;/BICOrBEI&gt;\n" +
//"            &lt;/OrgId&gt;\n" +
//"          &lt;/Id&gt;\n" +
//"        &lt;/Orgtr&gt;\n" +
//"      &lt;/StsRsnInf&gt;\n" +
//"      &lt;NbOfTxsPerSts&gt;\n" +
//"        &lt;DtldNbOfTxs&gt;1&lt;/DtldNbOfTxs&gt;\n" +
//"        &lt;DtldSts&gt;ACSP&lt;/DtldSts&gt;\n" +
//"        &lt;DtldCtrlSum&gt;1250&lt;/DtldCtrlSum&gt;\n" +
//"      &lt;/NbOfTxsPerSts&gt;\n" +
//"      &lt;TxInfAndSts&gt;\n" +
//"        &lt;StsId&gt;2020.05.20-07.27.37.219000000004880&lt;/StsId&gt;\n" +
//"        &lt;OrgnlInstrId&gt;014106371167&lt;/OrgnlInstrId&gt;\n" +
//"        &lt;OrgnlEndToEndId&gt;GIMAC_014106371167&lt;/OrgnlEndToEndId&gt;\n" +
//"        &lt;OrgnlDltPrvtData&gt;\n" +
//"          &lt;DltPrvtDataDtl&gt;\n" +
//"            &lt;PrvtDtInf&gt;TCCAMCM20052000000843&lt;/PrvtDtInf&gt;\n" +
//"            &lt;Tp&gt;\n" +
//"              &lt;CdOrPrtry&gt;\n" +
//"                &lt;Cd&gt;MOP_REFTX&lt;/Cd&gt;\n" +
//"              &lt;/CdOrPrtry&gt;\n" +
//"            &lt;/Tp&gt;\n" +
//"          &lt;/DltPrvtDataDtl&gt;\n" +
//"        &lt;/OrgnlDltPrvtData&gt;\n" +
//"        &lt;TxSts&gt;ACSP&lt;/TxSts&gt;\n" +
//"        &lt;AcctSvcrRef&gt;20200520_10000367141028&lt;/AcctSvcrRef&gt;\n" +
//"        &lt;OrgnlTxRef&gt;\n" +
//"          &lt;IntrBkSttlmAmt Ccy=\"XAF\"&gt;1250&lt;/IntrBkSttlmAmt&gt;\n" +
//"          &lt;Amt&gt;\n" +
//"            &lt;InstdAmt Ccy=\"XAF\"&gt;1250&lt;/InstdAmt&gt;\n" +
//"          &lt;/Amt&gt;\n" +
//"          &lt;ReqdExctnDt&gt;2020-05-20&lt;/ReqdExctnDt&gt;\n" +
//"          &lt;PmtTpInf&gt;\n" +
//"            &lt;ClrChanl&gt;BOOK&lt;/ClrChanl&gt;\n" +
//"          &lt;/PmtTpInf&gt;\n" +
//"          &lt;PmtMtd&gt;TRF&lt;/PmtMtd&gt;\n" +
//"          &lt;RmtInf&gt;\n" +
//"            &lt;Ustrd&gt;ANNUL VIR CONF 014106371167&lt;/Ustrd&gt;\n" +
//"          &lt;/RmtInf&gt;\n" +
//"          &lt;DbtrAcct&gt;\n" +
//"            &lt;Id&gt;\n" +
//"              &lt;Othr&gt;\n" +
//"                &lt;Id&gt;72830000014&lt;/Id&gt;\n" +
//"                &lt;SchmeNm&gt;\n" +
//"                  &lt;Prtry&gt;BKCOM_ACCOUNT&lt;/Prtry&gt;\n" +
//"                &lt;/SchmeNm&gt;\n" +
//"              &lt;/Othr&gt;\n" +
//"            &lt;/Id&gt;\n" +
//"            &lt;Ccy&gt;XAF&lt;/Ccy&gt;\n" +
//"          &lt;/DbtrAcct&gt;\n" +
//"          &lt;DbtrAgt&gt;\n" +
//"            &lt;FinInstnId&gt;\n" +
//"              &lt;BIC&gt;CCAMCMCY&lt;/BIC&gt;\n" +
//"            &lt;/FinInstnId&gt;\n" +
//"            &lt;BrnchId&gt;\n" +
//"              &lt;Id&gt;10000&lt;/Id&gt;\n" +
//"            &lt;/BrnchId&gt;\n" +
//"          &lt;/DbtrAgt&gt;\n" +
//"          &lt;CdtrAgt&gt;\n" +
//"            &lt;FinInstnId&gt;\n" +
//"              &lt;Othr&gt;\n" +
//"                &lt;Id&gt;10039&lt;/Id&gt;\n" +
//"                &lt;SchmeNm&gt;\n" +
//"                  &lt;Prtry&gt;BANK&lt;/Prtry&gt;\n" +
//"                &lt;/SchmeNm&gt;\n" +
//"              &lt;/Othr&gt;\n" +
//"            &lt;/FinInstnId&gt;\n" +
//"            &lt;BrnchId&gt;\n" +
//"              &lt;Id&gt;10000&lt;/Id&gt;\n" +
//"              &lt;Nm&gt;CCA SIEGE&lt;/Nm&gt;\n" +
//"              &lt;PstlAdr&gt;\n" +
//"                &lt;TwnNm&gt;YAOUNDE&lt;/TwnNm&gt;\n" +
//"                &lt;Ctry&gt;CM&lt;/Ctry&gt;\n" +
//"                &lt;AdrLine&gt;CCA SIEGE - YAOUNDE&lt;/AdrLine&gt;\n" +
//"                &lt;AdrLine&gt;1573 BLD RUDOLPH MANGA BELL&lt;/AdrLine&gt;\n" +
//"              &lt;/PstlAdr&gt;\n" +
//"            &lt;/BrnchId&gt;\n" +
//"          &lt;/CdtrAgt&gt;\n" +
//"          &lt;CdtrAcct&gt;\n" +
//"            &lt;Id&gt;\n" +
//"              &lt;Othr&gt;\n" +
//"                &lt;Id&gt;00600028801&lt;/Id&gt;\n" +
//"                &lt;SchmeNm&gt;\n" +
//"                  &lt;Prtry&gt;BKCOM_ACCOUNT&lt;/Prtry&gt;\n" +
//"                &lt;/SchmeNm&gt;\n" +
//"              &lt;/Othr&gt;\n" +
//"            &lt;/Id&gt;\n" +
//"            &lt;Ccy&gt;XAF&lt;/Ccy&gt;\n" +
//"          &lt;/CdtrAcct&gt;\n" +
//"        &lt;/OrgnlTxRef&gt;\n" +
//"      &lt;/TxInfAndSts&gt;\n" +
//"    &lt;/OrgnlPmtInfAndSts&gt;\n" +
//"  &lt;/CstmrPmtStsRpt&gt;\n" +
//"&lt;/Document&gt;</fjs1:pain002></fjs1:createTransferResponse></fjs1:createTransferResponseFlow></SOAP-ENV:Body></SOAP-ENV:Envelope>";                
////                System.out.println("Response String of Create transafer query :"+ s);
////                String text = s.substring(s.lastIndexOf("<fjs1:pain002>"), s.indexOf("</fjs1:pain002>"));
//                
//                String[] arrOfStr = s.split("fjs1:"); 
//                String [] ret = new String[]{};
//                 int b=0;
//                 int c=0;
//                 for (String a : arrOfStr) {
//                     
//                        if(a.contains("responseId>") && b==0){
//                                b=1;
//                                String[] arr = a.split(">");
//                                    responseId =arr[1].substring(0, arr[1].length() - 2);
//                                    System.out.println("responseId "+arr[1].substring(0, arr[1].length() - 2));
//                        }
//                        if(a.contains("statusCode>") && c==0){
//                            String[] PmtInfSts = s.split("PmtInfSts");
//                            String[] DtldSts  = s.split("DtldSts");
//                            String[] GrpSts  = s.split("GrpSts");
//                            //if(a.endsWith("<")){}else{
//                                c=1;
//                                String[] arr = a.split(">");
//                                    StatusCode =arr[1].substring(0, arr[1].length() - 2);
//                                if(StatusCode.equalsIgnoreCase("0") && (PmtInfSts[1].contains("ACSP") || PmtInfSts[1].contains("ACTC") || PmtInfSts[1].contains("ACCP") || PmtInfSts[1].contains("ACSC"))
//                                        && (DtldSts[1].contains("ACSP") || DtldSts[1].contains("ACTC") || DtldSts[1].contains("ACCP") || DtldSts[1].contains("ACSC"))
//                                        && (GrpSts[1].contains("ACSP") || GrpSts[1].contains("ACTC") || GrpSts[1].contains("ACCP") || GrpSts[1].contains("ACSC"))){
//                                        System.out.println("PmtInfSts "+PmtInfSts[1]);
//                                        System.out.println("DtldSts "+DtldSts[1]);
//                                        System.out.println("GrpSts "+GrpSts[1]);
//                                }else{
//                                    StatusCode ="-1";
//                                }
//                                System.out.println("StatusCode "+StatusCode);
//                        }
//                        
//                      
//                        
//                 }
//                 
//                 System.out.println("  END TREATMENT");
//                
//                        //String d = soapResponse.toString();
//        /* if(StatusCode.equalsIgnoreCase("")){
//            
//            StatusCode ="108";
//            responseId ="ERROR Also check AIF SERVER if it is ONLINE";
//            System.out.println("StatusCode :"+ StatusCode);
//            System.out.println("Message :" + responseId);
//        }*/
//        System.out.println("END  callSoapWebServiceForCreateTransfer ///////////");
//    }
//    
//    public static String[] callSoapWebService(String soapEndpointUrl, String soapAction, Map<String, String> val,String [] PmtInfList,String total,String NbrTransaction,String reqId) {
    public static String[] callSoapWebServiceForCreateTransfer(String soapEndpointUrl, String soapAction, Map<String, String> val,String  PmtInfList,String total,String NbrTransaction,String reqId) {
        System.out.println("BEGIN  callSoapWebServiceForCreateTransfer ///////////");

        String StatusCode ="";
        String responseId ="";
        try {
            final boolean isHttps = soapEndpointUrl.toLowerCase().startsWith("https");
            HttpsURLConnection httpsConnection = null;
            // Open HTTPS connection
            if (isHttps) {
                    TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                         @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
                    };
                     
                    // Install the all-trusting trust manager
                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                    // Open HTTPS connection
                    URL url = new URL(soapEndpointUrl);
                    httpsConnection = (HttpsURLConnection) url.openConnection();
                    // Trust all hosts
                    httpsConnection.setHostnameVerifier(new TrustAllHosts());
                    // Connect
                    httpsConnection.connect();
            }
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            
            // Send SOAP Message to SOAP Server
       //     System.setProperty("java.net.useSystemProxies", "true");
//            System.setProperty("javax.net.ssl.trustStore", "Aifcerts");
//            System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction,val,PmtInfList,total,NbrTransaction,reqId), soapEndpointUrl);
//            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction,val,PmtInfList,total,NbrTransaction,reqId), url);
            //SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction), soapEndpointUrl);
            //SOAPMessageContext context = null;
        
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
               // context.getMessage().writeTo(os);
                soapResponse.writeTo(os);  
                os.close();
                String s =new String(os.toByteArray(), Charset.forName("UTF-8"));                
                System.out.println("Response String of Create transafer query :"+ s);
//                String text = s.substring(s.lastIndexOf("<fjs1:pain002>"), s.indexOf("</fjs1:pain002>"));
                
                String[] arrOfStr = s.split("fjs1:"); 
                String [] ret = new String[]{};
                 int b=0;
                 int c=0;
                 for (String a : arrOfStr) {
                     
                        if(a.contains("responseId>") && b==0){
                                b=1;
                                String[] arr = a.split(">");
                                    responseId =arr[1].substring(0, arr[1].length() - 2);
                                    System.out.println("responseId "+arr[1].substring(0, arr[1].length() - 2));
                        }
                        if(a.contains("statusCode>") && c==0){
                            String[] PmtInfSts = s.split("PmtInfSts");
                            String[] DtldSts  = s.split("DtldSts");
                            String[] GrpSts  = s.split("GrpSts");
                            //if(a.endsWith("<")){}else{
                                c=1;
                                String[] arr = a.split(">");
                                    StatusCode =arr[1].substring(0, arr[1].length() - 2);
                                    
                                if(StatusCode.equalsIgnoreCase("0") && (PmtInfSts[1].contains("ACSP") || PmtInfSts[1].contains("ACTC") || PmtInfSts[1].contains("ACCP") || PmtInfSts[1].contains("ACSC"))
                                        && (DtldSts[1].contains("ACSP") || DtldSts[1].contains("ACTC") || DtldSts[1].contains("ACCP") || DtldSts[1].contains("ACSC"))
                                        && (GrpSts[1].contains("ACSP") || GrpSts[1].contains("ACTC") || GrpSts[1].contains("ACCP") || GrpSts[1].contains("ACSC"))){
                                        System.out.println("PmtInfSts "+PmtInfSts[1]);
                                        System.out.println("DtldSts "+DtldSts[1]);
                                        System.out.println("GrpSts "+GrpSts[1]);
                                }else{
                                    StatusCode ="-1";
                                }
                                System.out.println("StatusCode "+StatusCode);
                        }
                        
                      
                        
                 }
                 
                 System.out.println("  END TREATMENT");
                 soapConnection.close();
                if (isHttps) {
			httpsConnection.disconnect();
		}
                        //String d = soapResponse.toString();
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
            StatusCode ="108";
            responseId ="FATAL ERROR OPERATION FAILED CANOT ACCESS THE SERVICE : check AIF SERVER if it is ONLINE IF IT IS THE CASE  Make sure you have the correct endpoint URL and SOAPAction";
            System.out.println("Customised StatusCode :"+ StatusCode);
            System.out.println("Message :" + responseId);
            System.out.println("END  callSoapWebServiceForCreateTransfer ///////////");

            return new String[]{StatusCode,responseId};
        }
       /* if(StatusCode.equalsIgnoreCase("")){
            
            StatusCode ="108";
            responseId ="ERROR Also check AIF SERVER if it is ONLINE";
            System.out.println("StatusCode :"+ StatusCode);
            System.out.println("Message :" + responseId);
        }*/
        System.out.println("END  callSoapWebServiceForCreateTransfer ///////////");

        return new String[]{StatusCode,responseId};

    }
    
    private static class TrustAllHosts implements HostnameVerifier {
	@Override
        public boolean verify(String string, SSLSession ssls) {
           return true;
        }
    }
    
    private static SOAPMessage createSOAPRequest(String soapAction,Map<String, String> val,String PmtInfList,String total,String NbrTransaction,String reqId) throws Exception {
        
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
     //   createSoapEnvelope2(soapMessage);
        createSoapEnvelopeManyTomany(soapMessage,val,PmtInfList,total,NbrTransaction,reqId);
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.setHeader("SOAPAction", soapAction);
//        headers.addHeader("requestId1", "1");
//        headers.addHeader("serviceName", "getAccountActivity");
//        headers.addHeader("timestamp", "2020-01-01T00:00:00");
//        headers.addHeader("originalName", formatter.format(date));
//        headers.addHeader("originalId", "sdcdscdscds");
 //       headers.addHeader("languageCode", "001");
//        headers.addHeader("userCode", "40054");
//        headers.addHeader("channelCode", "40054");

        soapMessage.saveChanges();
        /* Print the request message, just for debugging purposes */
        System.out.println("Request SOAP Message:");
        soapMessage.writeTo(System.out);      
        System.out.println("\n");
        return soapMessage;
        
    }
    
    private static void createSoapEnvelope2(SOAPMessage soapMessage) throws SOAPException, SQLException, ClassNotFoundException    {
        
//        DataBase data = new DataBase();
//        Map<String, String> val = data.getPain001conf();
//        
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String myNamespace = "amp";
        String myNamespaceURI = "http://soprabanking.com/amplitude";
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
            /*
            Constructed SOAP Request Message:
            <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:myNamespace="http://www.webserviceX.NET">
                <SOAP-ENV:Header/>
                <SOAP-ENV:Body>
                    <myNamespace:GetInfoByCity>
                        <myNamespace:USCity>New York</myNamespace:USCity>
                    </myNamespace:GetInfoByCity>
                </SOAP-ENV:Body>
            </SOAP-ENV:Envelope>
            */

        // SOAP Body     
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("createTransferRequestFlow", myNamespace);
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("requestHeader", myNamespace);
        SOAPElement soapBodyElem11 = soapBodyElem1.addChildElement("requestId", myNamespace);
        soapBodyElem11.addTextNode("201708744444774447878754145445");
        SOAPElement soapBodyElem12 = soapBodyElem1.addChildElement("serviceName", myNamespace);
        soapBodyElem12.addTextNode("createTransfer");
        SOAPElement soapBodyElem13 = soapBodyElem1.addChildElement("timestamp", myNamespace);
        soapBodyElem13.addTextNode("2020-03-12T10:57:29");
//         <!--Optional:-->
//            <amp:originalName>?</amp:originalName>
//            <!--Optional:-->
//            <amp:originalId>?</amp:originalId>
//            <!--Optional:-->
//            <amp:originalTimestamp>?</amp:originalTimestamp>
//            <!--Optional:-->
        SOAPElement soapBodyElem14 = soapBodyElem1.addChildElement("originalName", myNamespace);
        soapBodyElem14.addTextNode("Ori");
        SOAPElement soapBodyElem15 = soapBodyElem1.addChildElement("languageCode", myNamespace);
        soapBodyElem15.addTextNode("001");
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("createTransferRequest", myNamespace);
        SOAPElement soapBodyElem21 = soapBodyElem2.addChildElement("canal", myNamespace);
        soapBodyElem21.addTextNode("CANAL_VRT_WEB_MODIF");
        SOAPElement soapBodyElem22 =soapBodyElem2.addChildElement("pain001", myNamespace);
        String pain001 ="<![CDATA[<Document xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB\">\n" +
            "<CstmrCdtTrfInitn>\n" +
            "<GrpHdr>\n" +
            "<MsgId>201708744444774447878754145445</MsgId>\n" +
            "<CreDtTm>2020-03-12T10:57:29</CreDtTm>\n" +
            "<NbOfTxs>1</NbOfTxs>\n" +
            "<CtrlSum>110.0000</CtrlSum>\n" +
            "<InitgPty/>\n" +
            "<DltPrvtData>\n" +
            "<FlwInd>HOMOLOGATION</FlwInd>\n" +
            "<DltPrvtDataDtl>\n" +
            "<PrvtDtInf>CANAL_VRT_WEB_MODIF</PrvtDtInf>\n" +
            "<Tp>\n" +
            "<CdOrPrtry>\n" +
            "<Cd>CHANNEL</Cd>\n" +
            "</CdOrPrtry>\n" +
            "</Tp>\n" +
            "</DltPrvtDataDtl>\n" +
            "</DltPrvtData>\n" +
            "</GrpHdr>\n" +
            "<PmtInf>\n" +
            "<PmtInfId>201708744444774447878754145445</PmtInfId>\n" +
            "<PmtMtd>TRF</PmtMtd>\n" +
            "<BtchBookg>0</BtchBookg>\n" +
            "<NbOfTxs>1</NbOfTxs>\n" +
            "<CtrlSum>110.0000</CtrlSum>\n" +
            "<DltPrvtData>\n" +
            "<OrdrPrties>\n" +
            "<Tp>IMM</Tp>\n" +
            "<Md>CREATE</Md>\n" +
            "</OrdrPrties>\n" +
            "</DltPrvtData>\n" +
            "<PmtTpInf>\n" +
            "<InstrPrty>NORM</InstrPrty>\n" +
            "<SvcLvl>\n" +
            "<Prtry>INTERNAL</Prtry>\n" +
            "</SvcLvl>\n" +
            "</PmtTpInf>\n" +
            "<ReqdExctnDt>2020-03-12</ReqdExctnDt>\n" +
            "<Dbtr/>\n" +
            "<DbtrAcct>\n" +
            "<Id>\n" +
            "<Othr>\n" +
            "<Id>00700079301</Id>\n" +
            "<SchmeNm>\n" +
            "<Prtry>BKCOM_ACCOUNT</Prtry>\n" +
            "</SchmeNm>\n" +
            "</Othr>\n" +
            "</Id>\n" +
            "<Ccy>XAF</Ccy>\n" +
            "</DbtrAcct>\n" +
            "<DbtrAgt>\n" +
            "<FinInstnId>\n" +
            "<Nm>BANQUE CCA SIEGE</Nm>\n" +
            "<Othr>\n" +
            "<Id>10039</Id>\n" +
            "<SchmeNm>\n" +
            "<Prtry>ITF_DELTAMOP_IDETAB</Prtry>\n" +
            "</SchmeNm>\n" +
            "</Othr>\n" +
            "</FinInstnId>\n" +
            "<BrnchId>\n" +
            "<Id>10001</Id>\n" +          
            "<Nm>Agence</Nm>\n" +
            "</BrnchId>\n" +
            "</DbtrAgt>\n" +
            "<CdtTrfTxInf>\n" +
            "<PmtId>\n" +
            "<InstrId>669248695178978447548</InstrId>\n" +
            "<EndToEndId>66924867888574125452</EndToEndId>\n" +
            "</PmtId>\n" +
            "<Amt>\n" +
            "<InstdAmt Ccy=\"XAF\">110.0000</InstdAmt>\n" +
            "</Amt>\n" +
            "<CdtrAgt>\n" +
            "<FinInstnId>\n" +
            "<Nm>BANQUE CCA SIEGE</Nm>\n" +
            "<Othr>\n" +
            "<Id>10039</Id>\n" +
            "<SchmeNm>\n" +
            "<Prtry>ITF_DELTAMOP_IDETAB</Prtry>\n" +
            "</SchmeNm>\n" +
            "</Othr>\n" +
            "</FinInstnId>\n" +
            "<BrnchId>\n" +
            "<Id>10001</Id>\n" +
            "<Nm>Agence</Nm>\n" +
            "</BrnchId>\n" +
            "</CdtrAgt>\n" +
            "<Cdtr/>\n" +
            "<CdtrAcct>\n" +
            "<Id>\n" +
            "<Othr>\n" +
            "<Id>00700085301</Id>\n" +
            "<SchmeNm>\n" +
            "<Prtry>BKCOM_ACCOUNT</Prtry>\n" +
            "</SchmeNm>\n" +
            "</Othr>\n" +
            "</Id>\n" +
            "<Ccy>XAF</Ccy>\n" +
            "</CdtrAcct>\n" +
            "<RmtInf>\n" +
            "<Ustrd>TEST TEST MOTIF</Ustrd>\n" +
            "</RmtInf>\n" +
            "</CdtTrfTxInf>\n" +
            "</PmtInf>\n" +
            "</CstmrCdtTrfInitn>\n" +
            "</Document>\n" +
            "]]>";
             soapBodyElem22.addTextNode(pain001);
    }
    
    //https://stackoverflow.com/questions/47333185/convert-soap-xml-response-to-object
    //
//    private static void createSoapEnvelope(SOAPMessage soapMessage) throws SOAPException    {
//        SOAPPart soapPart = soapMessage.getSOAPPart();
//
//        String myNamespace = "amp";
//        String myNamespaceURI = "http://soprabanking.com/amplitude";
//
//        // SOAP Envelope
//        SOAPEnvelope envelope = soapPart.getEnvelope();
//        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
//
//            /*
//            Constructed SOAP Request Message:
//            <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:myNamespace="http://www.webserviceX.NET">
//                <SOAP-ENV:Header/>
//                <SOAP-ENV:Body>
//                    <myNamespace:GetInfoByCity>
//                        <myNamespace:USCity>New York</myNamespace:USCity>
//                    </myNamespace:GetInfoByCity>
//                </SOAP-ENV:Body>
//            </SOAP-ENV:Envelope>
//            */
//
//        // SOAP Body
//        SOAPBody soapBody = envelope.getBody();
//        SOAPElement soapBodyElem = soapBody.addChildElement("getAccountActivityRequestFlow", myNamespace);
//        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("requestHeader", myNamespace);
//        SOAPElement soapBodyElem11 = soapBodyElem1.addChildElement("requestId", myNamespace);
//        soapBodyElem11.addTextNode("1");
//        SOAPElement soapBodyElem12 = soapBodyElem1.addChildElement("serviceName", myNamespace);
//        soapBodyElem12.addTextNode("getAccountActivity");
//        SOAPElement soapBodyElem13 = soapBodyElem1.addChildElement("timestamp", myNamespace);
//        soapBodyElem13.addTextNode("2020-01-01T00:00:00");
//        SOAPElement soapBodyElem14 = soapBodyElem1.addChildElement("languageCode", myNamespace);
//        soapBodyElem14.addTextNode("001");
//        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("getAccountActivityRequest", myNamespace);
//        SOAPElement soapBodyElem21 = soapBodyElem2.addChildElement("accountIdentifier", myNamespace);
//        SOAPElement soapBodyElem211 = soapBodyElem21.addChildElement("branch", myNamespace);
//        soapBodyElem211.addTextNode("10000");
//        SOAPElement soapBodyElem212 = soapBodyElem21.addChildElement("currency", myNamespace);
//        soapBodyElem212.addTextNode("950");
//        SOAPElement soapBodyElem213 = soapBodyElem21.addChildElement("account", myNamespace);
//        soapBodyElem213.addTextNode("00600006601");
//        SOAPElement soapBodyElem22 = soapBodyElem2.addChildElement("movementPeriod", myNamespace);
//        SOAPElement soapBodyElem221 = soapBodyElem22.addChildElement("bothMovements", myNamespace);
//        SOAPElement soapBodyElem2211 = soapBodyElem221.addChildElement("startDate", myNamespace);
//        soapBodyElem2211.addTextNode("2019-12-01");       
//    }

    
    // TOTAL MOTIF DATEVAL:
//        
//    private static void createSoapEnvelopeOneTomany(SOAPMessage soapMessage, JSONArray arr,JSONObject obj,String total,String motif) throws SOAPException, SQLException, ClassNotFoundException    {
//        
//        DataBase data = new DataBase();
//        Map<String, String> val = data.getPain001conf();
//        String dateval = data.DateVal();
//        SOAPPart soapPart = soapMessage.getSOAPPart();
//        String myNamespace = val.get("myNamespace");
//        String myNamespaceURI = val.get("myNamespaceURI");
//        String NbrTransaction = String.valueOf(arr.length());
//        // SOAP Envelope
//        SOAPEnvelope envelope = soapPart.getEnvelope();
//        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
//            /*
//            Constructed SOAP Request Message:
//            <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:myNamespace="http://www.webserviceX.NET">
//                <SOAP-ENV:Header/>
//                <SOAP-ENV:Body>
//                    <myNamespace:GetInfoByCity>
//                        <myNamespace:USCity>New York</myNamespace:USCity>
//                    </myNamespace:GetInfoByCity>
//                </SOAP-ENV:Body>
//            </SOAP-ENV:Envelope>
//            */
//
//        // SOAP Body     
//        String reqId=getReqId();
//        String getCreDtTm =getCreDtTm();
//        String creditedBody = getComptesAcrediter(val,motif,arr);
//        SOAPBody soapBody = envelope.getBody();
//        SOAPElement soapBodyElem = soapBody.addChildElement("createTransferRequestFlow", myNamespace);
//        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("requestHeader", myNamespace);
//        SOAPElement soapBodyElem11 = soapBodyElem1.addChildElement("requestId", myNamespace);
//        soapBodyElem11.addTextNode(reqId);
//        SOAPElement soapBodyElem12 = soapBodyElem1.addChildElement("serviceName", myNamespace);
//        soapBodyElem12.addTextNode("createTransfer");
//        SOAPElement soapBodyElem13 = soapBodyElem1.addChildElement("timestamp", myNamespace);
//        soapBodyElem13.addTextNode(getCreDtTm);
////         <!--Optional:-->
////            <amp:originalName>?</amp:originalName>
////            <!--Optional:-->
////            <amp:originalId>?</amp:originalId>
////            <!--Optional:-->
////            <amp:originalTimestamp>?</amp:originalTimestamp>
////            <!--Optional:-->
//
//        SOAPElement soapBodyElem14 = soapBodyElem1.addChildElement("originalName", myNamespace);
//        soapBodyElem14.addTextNode("Ori");
//        SOAPElement soapBodyElem15 = soapBodyElem1.addChildElement("languageCode", myNamespace);
//        soapBodyElem15.addTextNode("001");
//        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("createTransferRequest", myNamespace);
//        SOAPElement soapBodyElem21 = soapBodyElem2.addChildElement("canal", myNamespace);
//        soapBodyElem21.addTextNode(val.get("DltPrvtDataDtlPrvtDtInf"));
//        SOAPElement soapBodyElem22 =soapBodyElem2.addChildElement("pain001", myNamespace);
//        String pain001 ="<![CDATA[<Document xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB\">\n" +
//            "<CstmrCdtTrfInitn>\n" +
//            "<GrpHdr>\n" +
//            "<MsgId>"+reqId+"</MsgId>\n" +
//            "<CreDtTm>"+getCreDtTm+"</CreDtTm>\n" +
//            "<NbOfTxs>"+NbrTransaction+"</NbOfTxs>\n" +
//            "<CtrlSum>"+total+"</CtrlSum>\n" +
//            "<InitgPty/>\n" +
//            "<DltPrvtData>\n" +
//            "<FlwInd>"+val.get("DltPrvtDataFlwInd")+"</FlwInd>\n" +
//            "<DltPrvtDataDtl>\n" +
//            "<PrvtDtInf>"+val.get("DltPrvtDataDtlPrvtDtInf")+"</PrvtDtInf>\n" +
//            "<Tp>\n" +
//            "<CdOrPrtry>\n" +
//            "<Cd>"+val.get("DltPrvtDataDtlCdOrPrtry")+"</Cd>\n" +  
//            "</CdOrPrtry>\n" +
//            "</Tp>\n" +
//            "</DltPrvtDataDtl>\n" +
//            "</DltPrvtData>\n" +
//            "</GrpHdr>\n" +
//            "<PmtInf>\n" +
//            "<PmtInfId>"+reqId+"</PmtInfId>\n" +
//            "<PmtMtd>"+val.get("PmtInfPmtMtd")+"</PmtMtd>\n" +     
//            "<BtchBookg>"+val.get("PmtInfBtchBookg")+"</BtchBookg>\n" +   
//            "<NbOfTxs>"+NbrTransaction+"</NbOfTxs>\n" +
//            "<CtrlSum>"+total+"</CtrlSum>\n" +
//            "<DltPrvtData>\n" +
//            "<OrdrPrties>\n" +
//            "<Tp>"+val.get("DltPrvtDataTp")+"</Tp>\n" +    
//            "<Md>"+val.get("DltPrvtDataMd")+"</Md>\n" +
//            "</OrdrPrties>\n" +
//            "</DltPrvtData>\n" +
//            "<PmtTpInf>\n" +
//            "<InstrPrty>"+val.get("PmtTpInfInstrPrty")+"</InstrPrty>\n" +    
//            "<SvcLvl>\n" +
//            "<Prtry>"+val.get("PmtTpInfInstrPrty")+"</Prtry>\n" +
//            "</SvcLvl>\n" +
//            "</PmtTpInf>\n" +
//            "<ReqdExctnDt>"+dateval+"</ReqdExctnDt>\n" +
//            "<Dbtr/>\n" +
//            "<DbtrAcct>\n" +
//            "<Id>\n" +
//            "<Othr>\n" +
//            "<Id>"+obj.getString("12")+"</Id>\n" +
//            "<SchmeNm>\n" +
//            "<Prtry>"+val.get("DbtrAcctSchmeNmPrtry")+"</Prtry>\n" +   	 
//            "</SchmeNm>\n" +
//            "</Othr>\n" +
//            "</Id>\n" +
//            "<Ccy>"+val.get("DbtrAcctcc")+"</Ccy>\n" +   
//            "</DbtrAcct>\n" +
//            "<DbtrAgt>\n" +
//            "<FinInstnId>\n" + 
//            "<Nm>"+val.get("FinInstnIdNm")+"</Nm>\n" +
//            "<Othr>\n" +
//            "<Id>"+val.get("FinInstnIdId")+"</Id>\n" +
//            "<SchmeNm>\n" +
//            "<Prtry>"+val.get("DbtrAgtSchmeNm")+"</Prtry>\n" +  	
//            "</SchmeNm>\n" +
//            "</Othr>\n" +
//            "</FinInstnId>\n" +
//            "<BrnchId>\n" +
//            "<Id>"+obj.getString("11")+"</Id>\n" +
//            "<Nm>Agence</Nm>\n" +
//            "</BrnchId>\n" +
//            "</DbtrAgt>\n" +
//            creditedBody +
//            "</PmtInf>\n" +
//            "</CstmrCdtTrfInitn>\n" +
//            "</Document>\n" +
//            "]]>";
//             soapBodyElem22.addTextNode(pain001);
//    }    
//         
    public String getMsgIdPmtInfId(){
        
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 9;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
          .limit(targetStringLength)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
//        System.out.println(generatedString);
        String v= "";
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date now = new Date();
        String strDate = "CCA"+sdfDate.format(now)+"IWCORE"+generatedString;
        
        return  strDate;
    } 
      
    public static String getReqId(){
        
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
          .limit(targetStringLength)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
//        System.out.println(generatedString);
        String v= "";
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date now = new Date();
       // String strDate = "CCArqid"+sdfDate.format(now)+"IWCORE"+generatedString;
        String strDate = "CCA"+sdfDate.format(now)+"IWC"+generatedString;
        
        return  strDate;
    } 
    
    public String ReqdExctnDt(){
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        return sdf.format(now);
    } 
    
    public static  String getCreDtTm() throws SQLException, ClassNotFoundException{
        final DataBase  dat= new DataBase();
        String dateValeur =dat.DateValService();
        SimpleDateFormat sdf = new SimpleDateFormat("'T'HH:mm:ss");
        Date now = new Date();
        return dateValeur+sdf.format(now);
        
    }
    
    
    
    public static String getComptesAcrediter(Map<String, String> val ,String motif,JSONArray arr,String ref){
        
        String  V ="";
        for(int i=0;i<arr.length();i++){
            JSONObject jo= arr.getJSONObject(i);                    
            
            V =V+"<CdtTrfTxInf>\n" +
                    "<PmtId>\n" +
                    "<InstrId>"+ref+"</InstrId>\n" +
                    "<EndToEndId>"+"GIMAC_"+ref+"</EndToEndId>\n" +
                    "</PmtId>\n" +
                    "<Amt>\n" +
                    "<InstdAmt Ccy="+'"'+val.get("DbtrAcctcc")+'"'+">"+jo.getString("12")+"</InstdAmt>\n" +
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
                    "<Id>"+jo.getString("10")+"</Id>\n" +
                    "<Nm>Agence</Nm>\n" +
                    "</BrnchId>\n" +
                    "</CdtrAgt>\n" +
                    "<Cdtr/>\n" +
                    "<CdtrAcct>\n" +
                    "<Id>\n" +
                    "<Othr>\n" +
                    "<Id>"+jo.getString("11")+"</Id>\n" +
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
    
    public static String getComptesAcrediterAnn(Map<String, String> val ,String motif,JSONArray arr,String ref,String Amount){
        
        String  V ="";
        for(int i=0;i<arr.length();i++){
            JSONObject jo= arr.getJSONObject(i);                    
            
            V =V+"<CdtTrfTxInf>\n" +
                    "<PmtId>\n" +
                    "<InstrId>"+ref+"</InstrId>\n" +
                    "<EndToEndId>"+"GIMAC_"+ref+"</EndToEndId>\n" +
                    "</PmtId>\n" +
                    "<Amt>\n" +
                    "<InstdAmt Ccy="+'"'+val.get("DbtrAcctcc")+'"'+">"+Amount+"</InstdAmt>\n" +
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
                    "<Id>"+jo.getString("11")+"</Id>\n" +
                    "<Nm>Agence</Nm>\n" +
                    "</BrnchId>\n" +
                    "</CdtrAgt>\n" +
                    "<Cdtr/>\n" +
                    "<CdtrAcct>\n" +
                    "<Id>\n" +
                    "<Othr>\n" +
                    "<Id>"+jo.getString("12")+"</Id>\n" +
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
    
    public String pain001debitedaccount(Map<String, String> val,JSONObject obj,String reqId,String NbrTransaction,String total,String dateval){
        
        String v = "";
        v="<PmtInfId>"+reqId+"</PmtInfId>\n" +
            "<PmtMtd>"+val.get("PmtInfPmtMtd")+"</PmtMtd>\n" +     
            "<BtchBookg>"+val.get("PmtInfBtchBookg")+"</BtchBookg>\n" +   
            "<NbOfTxs>"+NbrTransaction+"</NbOfTxs>\n" +
            "<CtrlSum>"+total+"</CtrlSum>\n" +
            "<DltPrvtData>\n" +
            "<OrdrPrties>\n" +
            "<Tp>"+val.get("DltPrvtDataTp")+"</Tp>\n" +    
            "<Md>"+val.get("DltPrvtDataMd")+"</Md>\n" +
            "</OrdrPrties>\n" +
            "</DltPrvtData>\n" +
            "<PmtTpInf>\n" +
            "<InstrPrty>"+val.get("PmtTpInfInstrPrty")+"</InstrPrty>\n" +    
            "<SvcLvl>\n" +
            "<Prtry>"+val.get("SvcLvlPrtry")+"</Prtry>\n" +
            "</SvcLvl>\n" +
            "</PmtTpInf>\n" +
            "<ReqdExctnDt>"+dateval+"</ReqdExctnDt>\n" +
            "<Dbtr/>\n" +
            "<DbtrAcct>\n" +
            "<Id>\n" +
            "<Othr>\n" +
            "<Id>"+obj.getString("12")+"</Id>\n" +
            "<SchmeNm>\n" +
            "<Prtry>"+val.get("DbtrAcctSchmeNmPrtry")+"</Prtry>\n" +   	 
            "</SchmeNm>\n" +
            "</Othr>\n" +
            "</Id>\n" +
            "<Ccy>"+val.get("DbtrAcctcc")+"</Ccy>\n" +   
            "</DbtrAcct>\n" +
            "<DbtrAgt>\n" +
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
            "<Id>"+obj.getString("11")+"</Id>\n" +
            "<Nm>Agence</Nm>\n" +
            "</BrnchId>\n" +
            "</DbtrAgt>\n";
        
        return v;      
    }
    
    public String pain001debitedaccountAnn(Map<String, String> val,JSONObject obj,String reqId,String NbrTransaction,String total,String dateval){
        
        String v = "";
        v="<PmtInfId>"+reqId+"</PmtInfId>\n" +
            "<PmtMtd>"+val.get("PmtInfPmtMtd")+"</PmtMtd>\n" +     
            "<BtchBookg>"+val.get("PmtInfBtchBookg")+"</BtchBookg>\n" +   
            "<NbOfTxs>"+NbrTransaction+"</NbOfTxs>\n" +
            "<CtrlSum>"+total+"</CtrlSum>\n" +
            "<DltPrvtData>\n" +
            "<OrdrPrties>\n" +
            "<Tp>"+val.get("DltPrvtDataTp")+"</Tp>\n" +    
            "<Md>"+val.get("DltPrvtDataMd")+"</Md>\n" +
            "</OrdrPrties>\n" +
            "</DltPrvtData>\n" +
            "<PmtTpInf>\n" +
            "<InstrPrty>"+val.get("PmtTpInfInstrPrty")+"</InstrPrty>\n" +    
            "<SvcLvl>\n" +
            "<Prtry>"+val.get("SvcLvlPrtry")+"</Prtry>\n" +
            "</SvcLvl>\n" +
            "</PmtTpInf>\n" +
            "<ReqdExctnDt>"+dateval+"</ReqdExctnDt>\n" +
            "<Dbtr/>\n" +
            "<DbtrAcct>\n" +
            "<Id>\n" +
            "<Othr>\n" +
            "<Id>"+obj.getString("12")+"</Id>\n" +
            "<SchmeNm>\n" +
            "<Prtry>"+val.get("DbtrAcctSchmeNmPrtry")+"</Prtry>\n" +   	 
            "</SchmeNm>\n" +
            "</Othr>\n" +
            "</Id>\n" +
            "<Ccy>"+val.get("DbtrAcctcc")+"</Ccy>\n" +   
            "</DbtrAcct>\n" +
            "<DbtrAgt>\n" +
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
            "<Id>"+obj.getString("11")+"</Id>\n" +
            "<Nm>Agence</Nm>\n" +
            "</BrnchId>\n" +
            "</DbtrAgt>\n";
        
        return v;      
    }
    
    public static String pain001paymentInf(String bodyDebited, String listCredited){
    
         return "<PmtInf>\n" + bodyDebited +listCredited + "</PmtInf>\n";
        
    }
    
    public static String pain001ListpaymentInf(String [] PmtInfList){
    
         String v ="";
         for(int i = 0; i<PmtInfList.length;i++){
             v = v+PmtInfList[i];         
         }
                 
         return v;
    }
   
    //POUR GERER UNE OU PLUSIEURS REMISES PLUSIEURS REMISE ( 1 REMISE EST L EQUIVALENT DE 1 DEBIT ET PLUSIEURS CREDITS
    private static void createSoapEnvelopeManyTomany(SOAPMessage soapMessage, Map<String, String> val,String PmtInfgroup,String total,String NbrTransaction,String reqId) throws SOAPException, SQLException, ClassNotFoundException    {
        
        // DataBase data = new DataBase();
        // Map<String, String> val = data.getPain001conf();
        //String dateval = data.DateVal();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String myNamespace = val.get("myNamespace");
        String myNamespaceURI = val.get("myNamespaceURI");
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
            /*
            Constructed SOAP Request Message:
            <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:myNamespace="http://www.webserviceX.NET">
                <SOAP-ENV:Header/>
                <SOAP-ENV:Body>
                    <myNamespace:GetInfoByCity>
                        <myNamespace:USCity>New York</myNamespace:USCity>
                    </myNamespace:GetInfoByCity>
                </SOAP-ENV:Body>
            </SOAP-ENV:Envelope>
            */

        // SOAP Body     
        //String reqId=getReqId();
        String getCreDtTm =getCreDtTm();
        SOAPBody soapBody = envelope.getBody();
     //   String PmtInfgroup = pain001ListpaymentInf(PmtInfList);
        SOAPElement soapBodyElem = soapBody.addChildElement("createTransferRequestFlow", myNamespace);
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("requestHeader", myNamespace);
        SOAPElement soapBodyElem11 = soapBodyElem1.addChildElement("requestId", myNamespace);
        soapBodyElem11.addTextNode(reqId);
        SOAPElement soapBodyElem12 = soapBodyElem1.addChildElement("serviceName", myNamespace);
        soapBodyElem12.addTextNode("createTransfer");
        SOAPElement soapBodyElem13 = soapBodyElem1.addChildElement("timestamp", myNamespace);
        soapBodyElem13.addTextNode(getCreDtTm);
//         <!--Optional:-->
//            <amp:originalName>?</amp:originalName>
//            <!--Optional:-->
//            <amp:originalId>?</amp:originalId>
//            <!--Optional:-->
//            <amp:originalTimestamp>?</amp:originalTimestamp>
//            <!--Optional:-->

        SOAPElement soapBodyElem14 = soapBodyElem1.addChildElement("originalName", myNamespace);
        soapBodyElem14.addTextNode("Orig");
        SOAPElement soapBodyElem15 = soapBodyElem1.addChildElement("languageCode", myNamespace);
        soapBodyElem15.addTextNode("001");
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("createTransferRequest", myNamespace);
        SOAPElement soapBodyElem21 = soapBodyElem2.addChildElement("canal", myNamespace);
        soapBodyElem21.addTextNode(val.get("DltPrvtDataDtlPrvtDtInf"));
        SOAPElement soapBodyElem22 =soapBodyElem2.addChildElement("pain001", myNamespace);
        String pain001 ="<![CDATA[<Document xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB\">\n" +
            "<CstmrCdtTrfInitn>\n" +
            "<GrpHdr>\n" +
            "<MsgId>"+reqId+"</MsgId>\n" +
            "<CreDtTm>"+getCreDtTm+"</CreDtTm>\n" +
            "<NbOfTxs>"+NbrTransaction+"</NbOfTxs>\n" +
            "<CtrlSum>"+total+"</CtrlSum>\n" +
            "<InitgPty/>\n" +
            "<DltPrvtData>\n" +
            "<FlwInd>"+val.get("DltPrvtDataFlwInd")+"</FlwInd>\n" +
            "<DltPrvtDataDtl>\n" +
            "<PrvtDtInf>"+val.get("DltPrvtDataDtlPrvtDtInf")+"</PrvtDtInf>\n" +
            "<Tp>\n" +
            "<CdOrPrtry>\n" +
            "<Cd>"+val.get("DltPrvtDataDtlCdOrPrtry")+"</Cd>\n" +  
            "</CdOrPrtry>\n" +
            "</Tp>\n" +
            "</DltPrvtDataDtl>\n" +
            "</DltPrvtData>\n" +
            "</GrpHdr>\n" +
            PmtInfgroup+
            "</CstmrCdtTrfInitn>\n" +
            "</Document>\n" +
            "]]>";
             soapBodyElem22.addTextNode(pain001);
    }
        
}