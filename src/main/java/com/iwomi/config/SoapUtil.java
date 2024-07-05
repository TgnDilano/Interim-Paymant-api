package com.iwomi.config;

import com.iwomi.soapAif.AifServiceImpl;

import javax.net.ssl.*;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoapUtil {
    public static String generateRandomId(int length) {
        String chars = "1234567890";
        StringBuilder pass = new StringBuilder();
        for (int x = 0; x < length; x++) {
            int i = (int) Math.floor(Math.random() * (chars.length() - 1));
            pass.append(chars.charAt(i));
        }
        return pass.toString();
    }

    public static String convertSoapMessageToString(SOAPMessage response) {
        System.out.println("SOAP Message to be converted to string: " + response);

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            response.writeTo(System.out);
            response.writeTo(os);
            os.close();
        } catch (Exception ex) {
            Logger.getLogger(AifServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        String xmlData = new String(os.toByteArray(), StandardCharsets.UTF_8);
        System.out.println("yvo xmlDATA: " + xmlData);

        return xmlData;
    }

    public static Map<String, Object> responseWithError(String msg) {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "100");
        result.put("data", new HashMap<>());
        result.put("message", msg);
        return result;
    }

    public static String getFormedDate() {
        String ISO_8601BASIC_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(ISO_8601BASIC_DATE_PATTERN);
        String currentDateTime = dateFormat.format(new Date());
        System.out.println("Request SOAP DATE: " + currentDateTime);
        return currentDateTime;
    }

    public static void trustAllHosts() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509ExtendedTrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException {

                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException {

                }

            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            // log.error("Error occurred",e);
        }
    }
}
