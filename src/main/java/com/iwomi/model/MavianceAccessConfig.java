/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "mav_config")

public class MavianceAccessConfig extends AbstractModel<Long> {
    private String type;
    private String base_path;
    private String access_token;
    private String access_Secret;
    private String chl1;
    private String chl2;
    private String chl3;

    public MavianceAccessConfig(String type, String base_path, String access_token, String access_Secret, String chl1, String chl2, String chl3) {
        this.type = type;
        this.base_path = base_path;
        this.access_token = access_token;
        this.access_Secret = access_Secret;
        this.chl1 = chl1;
        this.chl2 = chl2;
        this.chl3 = chl3;
    }

    public MavianceAccessConfig() {
    }
    
    

    public String getBase_path() {
        return base_path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    

    public void setBase_path(String base_path) {
        this.base_path = base_path;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_Secret() {
        return access_Secret;
    }

    public void setAccess_Secret(String access_Secret) {
        this.access_Secret = access_Secret;
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
    
    

    

}
