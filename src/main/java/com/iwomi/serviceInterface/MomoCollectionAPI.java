/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.serviceInterface;

import java.util.Map;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author HP
 */
public interface MomoCollectionAPI {
    public String getToken( String apiUser, String apiKey, String subKey);
    
    public Map<String, String> performRedrawal(String amount,String number, String payerMsg, String payeeMsg, String currency,String token, String subKey,String s);

    public String OperationStatus(String referenced_id, String subKey, String token);
    
    public String getAccountBalance(String subKey,String token);
    
    public String MomoAccountExist(String accountHolderId,String subKey, String token);
    
}
