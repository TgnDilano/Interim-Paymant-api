/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.serviceInterface;

import java.sql.SQLException;
import java.util.Map;

import org.json.JSONException;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author HP
 */
public interface OperationApi {
//    public String getToken( String apiUser, String apiKey, String subKey);

    public Map<String, String> ApiOperation(String amount, String number, String payerMsg, String payeeMsg, String currency, String token, String subKey, String externalId, String opr, String oo);

//    public String OperationStatus(String referenced_id, String subKey, String token);
    public Map<String, String> OperationStatus1(String referenced_id, String subKey, String token, String ss);

    public Map<String, String> callbackclient(String string,String s) throws SQLException;

    public void updateStatus(String string, String string2) throws SQLException;
    
    void updateStatus2(String trans_key, String status,String reason) throws SQLException;
            
    public String getTokenSystem();

    String getToken1(String apiUser, String apiKey, String subKey);

    Map<String, String> getAccessToApI(String s) throws SQLException, ClassNotFoundException, JSONException;

    Map<String, String> getAccessToApI2(String serviceCode)throws SQLException, ClassNotFoundException, JSONException;
    Map<String, String> getAccessToApI3(String serviceCode)throws SQLException, ClassNotFoundException, JSONException;
//    public String getAccountBalance(String subKey);
//    
//    public String MomoAccountExist(String accountHolderId,String subKey, String token);

}
