/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "sathz")
public class Sathz implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public int id;
    public Date crtd;
    public Date mdfi;
    public int cuser;
    public int muser;
    public String prfle;
    public String actn;
    public String rgts;
    public String cetab;
    public String nfact;

    public Sathz(Date crtd, Date mdfi, int cuser, int muser, String prfle, String actn, String rgts, String cetab, String nfact) {
        this.crtd = crtd;
        this.mdfi = mdfi;
        this.cuser = cuser;
        this.muser = muser;
        this.prfle = prfle;
        this.actn = actn;
        this.rgts = rgts;
        this.cetab = cetab;
        this.nfact = nfact;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCrtd() {
        return crtd;
    }

    public void setCrtd(Date crtd) {
        this.crtd = crtd;
    }

    public Date getMdfi() {
        return mdfi;
    }

    public void setMdfi(Date mdfi) {
        this.mdfi = mdfi;
    }

    public int getCuser() {
        return cuser;
    }

    public void setCuser(int cuser) {
        this.cuser = cuser;
    }

    public int getMuser() {
        return muser;
    }

    public void setMuser(int muser) {
        this.muser = muser;
    }

    public String getPrfle() {
        return prfle;
    }

    public void setPrfle(String prfle) {
        this.prfle = prfle;
    }

    public String getActn() {
        return actn;
    }

    public void setActn(String actn) {
        this.actn = actn;
    }

    public String getRgts() {
        return rgts;
    }

    public void setRgts(String rgts) {
        this.rgts = rgts;
    }

    public String getCetab() {
        return cetab;
    }

    public void setCetab(String cetab) {
        this.cetab = cetab;
    }

    public String getNfact() {
        return nfact;
    }

    public void setNfact(String nfact) {
        this.nfact = nfact;
    }
    
    
    
}
