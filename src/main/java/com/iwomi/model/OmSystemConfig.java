/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author HP
 */

@Entity
@Table(name = "orange_info")
public class OmSystemConfig extends AbstractModel<Long>{
    
    private String apiUser;
    private String apiSecret;
    private String apiPass;
    private String apiKey;
    private String chmsd;
    private String pin;
    private String code;//100 for cashIn, 200 for cashOut
    private String balance;
    private String chl1;
    private String chl2;
    private String chl3;
    private String chl4;
    private String chl5;

    public OmSystemConfig(String apiUser, String apiKey, String chmsd, String pin, String chl1, String chl2, String chl3, String chl4, String chl5) {
        this.apiUser = apiUser;
        this.apiKey = apiKey;
        this.chmsd = chmsd;
        this.pin = pin;
        this.chl1 = chl1;
        this.chl2 = chl2;
        this.chl3 = chl3;
        this.chl4 = chl4;
        this.chl5 = chl5;
    }

    public OmSystemConfig() {
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    
    
    
    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getApiPass() {
        return apiPass;
    }

    public void setApiPass(String apiPass) {
        this.apiPass = apiPass;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    

    public String getApiUser() {
        return apiUser;
    }

    public void setApiUser(String apiUser) {
        this.apiUser = apiUser;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getChmsd() {
        return chmsd;
    }

    public void setChmsd(String chmsd) {
        this.chmsd = chmsd;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getChl1() {
        return chl1;
    }

    public void setChl1(String chl1) {
        this.chl1 = chl1;
    }

    public String getChl2() {
        return chl2;
    }

    public void setChl2(String chl2) {
        this.chl2 = chl2;
    }

    public String getChl3() {
        return chl3;
    }

    public void setChl3(String chl3) {
        this.chl3 = chl3;
    }

    public String getChl4() {
        return chl4;
    }

    public void setChl4(String chl4) {
        this.chl4 = chl4;
    }

    public String getChl5() {
        return chl5;
    }

    public void setChl5(String chl5) {
        this.chl5 = chl5;
    }
    
    
    
}
