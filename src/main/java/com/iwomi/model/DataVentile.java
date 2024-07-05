/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * @author Tebit Roger
 */
@Entity
@Table(name = "dvtle")
public class DataVentile extends AbstractModel<Long>  {
    @Column(name = "ntinb")
    private String noticeNumber;
      @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "dv_dd",
            joinColumns = {
                @JoinColumn(name = "dv_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "dd_id", referencedColumnName = "id")})
    @Column(name = "ddvt")   
    private List<VentileDetail> ventileDetail = new  ArrayList<>();
    public String getNoticeNumber() {
        return noticeNumber;
    }

    public void setNoticeNumber(String noticeNumber) {
        this.noticeNumber = noticeNumber;
    }

    public List<VentileDetail> getVentileDetail() {
        return ventileDetail;
    }

    public void setVentileDetail(List<VentileDetail> ventileDetail) {
        this.ventileDetail = ventileDetail;
    }

    public DataVentile() {
    }
}
