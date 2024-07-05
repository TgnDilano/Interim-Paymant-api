/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import java.io.Serializable;
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
@Table(name = "pwd")
public class Pwd implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public int id;
    public String login;//user
    public String pass;//pass
    public String descrip; 
    public String etab;
    public String acscd;
    public String lib1;//ip
    public String lib2;//port
    public String crtd;
    public String mdfi;
    public String dele;

    public Pwd(String login, String pass, String descrip, String etab, String acscd, String lib1, String lib2, String crtd, String mdfi, String dele) {
        this.login = login;
        this.pass = pass;
        this.descrip = descrip;
        this.etab = etab;
        this.acscd = acscd;
        this.lib1 = lib1;
        this.lib2 = lib2;
        this.crtd = crtd;
        this.mdfi = mdfi;
        this.dele = dele;
    }

    public Pwd() {
    }
    
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getEtab() {
        return etab;
    }

    public void setEtab(String etab) {
        this.etab = etab;
    }

    public String getAcscd() {
        return acscd;
    }

    public void setAcscd(String acscd) {
        this.acscd = acscd;
    }

    public String getLib1() {
        return lib1;
    }

    public void setLib1(String lib1) {
        this.lib1 = lib1;
    }

    public String getLib2() {
        return lib2;
    }

    public void setLib2(String lib2) {
        this.lib2 = lib2;
    }

    public String getCrtd() {
        return crtd;
    }

    public void setCrtd(String crtd) {
        this.crtd = crtd;
    }

    public String getMdfi() {
        return mdfi;
    }

    public void setMdfi(String mdfi) {
        this.mdfi = mdfi;
    }

    public String getDele() {
        return dele;
    }

    public void setDele(String dele) {
        this.dele = dele;
    }
    
    
    
}
