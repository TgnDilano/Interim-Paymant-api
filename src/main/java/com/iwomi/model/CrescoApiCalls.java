/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author HP
 */

@Entity
@Table(name = "crescoapi")
public class CrescoApiCalls implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    @Column(name="token", nullable=false, unique = true)
    public String token;
    public String time;
    @JsonFormat(pattern = "d/M/y")
    @Temporal(TemporalType.DATE)
    public Date cdate; 
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String response;
    public String lib1;
    public String lib2;

    public CrescoApiCalls() {
    }

    public CrescoApiCalls(String token, String time, Date cdate,String data) {
        this.token = token;
        this.time = time;
        this.cdate = cdate;
        this.response = data;
    }

    public CrescoApiCalls(String token, String time, Date cdate) {
        this.token = token;
        this.time = time;
        this.cdate = cdate;
    }

    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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

    
    
    
    
    
}
