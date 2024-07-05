/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "mtnInfo")

public class SystemProp extends AbstractModel<Long> {

    private int amount;
    private String apiUser;
    private String apiKey;
    private String code;
    private String prod;
    private String srvid;
    private String callback;
    private String xtarget;
    private String requestUrl;
    private String checkUrl;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getCheckUrl() {
        return checkUrl;
    }

    public void setCheckUrl(String checkUrl) {
        this.checkUrl = checkUrl;
    }

    public SystemProp(int amount, String apiUser, String apiKey, String code, String prod, String srvid, String callback, String xtarget, String requestUrl, String checkUrl) {
        this.amount = amount;
        this.apiUser = apiUser;
        this.apiKey = apiKey;
        this.code = code;
        this.prod = prod;
        this.srvid = srvid;
        this.callback = callback;
        this.xtarget = xtarget;
        this.requestUrl = requestUrl;
        this.checkUrl = checkUrl;
    }

    public SystemProp(int amount, String apiUser, String apiKey, String code, String prod, String srvid, String callback, String xtarget) {
        this.amount = amount;
        this.apiUser = apiUser;
        this.apiKey = apiKey;
        this.code = code;
        this.prod = prod;
        this.srvid = srvid;
        this.callback = callback;
        this.xtarget = xtarget;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getXtarget() {
        return xtarget;
    }

    public void setXtarget(String xtarget) {
        this.xtarget = xtarget;
    }

    public SystemProp(int amount, String apiUser, String apiKey, String code, String prod, String srvid) {
        this.amount = amount;
        this.apiUser = apiUser;
        this.apiKey = apiKey;
        this.code = code;
        this.prod = prod;
        this.srvid = srvid;
    }

    public SystemProp() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public String getSrvid() {
        return srvid;
    }

    public void setSrvid(String srvid) {
        this.srvid = srvid;
    }

}
