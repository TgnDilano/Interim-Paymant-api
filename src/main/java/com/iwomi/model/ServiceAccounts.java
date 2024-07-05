package com.iwomi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import javax.persistence.Column;

public class ServiceAccounts {
    private String name;
    private String pwd;
    private String descp;
    private String code;
    private Date crdt;
    private Date mdt;
    private String chl1;
    private String chl2;
    private String chl3;
    private String chl4;
    private String chl5;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCrdt() {
        return crdt;
    }

    public void setCrdt(Date crdt) {
        this.crdt = crdt;
    }

    public Date getMdt() {
        return mdt;
    }

    public void setMdt(Date mdt) {
        this.mdt = mdt;
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



    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
