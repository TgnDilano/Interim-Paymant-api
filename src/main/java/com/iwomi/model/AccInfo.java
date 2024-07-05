/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import com.iwomi.model.AbstractModel;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author HP
 */

@Entity
@Table(name = "gpac")
public class AccInfo extends AbstractModel<Long> {
    private String mnt;
    private String cpt;
    private String age;
    private String dev;
    private String bkc;
    private String desp;

    public String getBkc() {
        return bkc;
    }

    public void setBkc(String bkc) {
        this.bkc = bkc;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getMnt() { return mnt; }
    public void setMnt(String value) { this.mnt = value; }

    public String getCpt() { return cpt; }
    public void setCpt(String value) { this.cpt = value; }

    public String getAge() { return age; }
    public void setAge(String value) { this.age = value; }

    public String getDev() { return dev; }
    public void setDev(String value) { this.dev = value; }

    
    
    
}
