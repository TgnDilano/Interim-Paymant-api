/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Tebit Roger
 */
@Entity
@Table(name = "notlist")
public class NotList extends AbstractModel<Long>{
    @Column(name = "ntid")
    private Long noticeId;
    @Column(name = "ntnm")
    private String noticeNumber;
    @Column(name = "rnmsg")
    private String resultNoticeMsg;
    @Column(name = "rncd")
    private String resultNoticeCode;

    public String getResultNoticeMsg() {
        return resultNoticeMsg;
    }

    public void setResultNoticeMsg(String resultNoticeMsg) {
        this.resultNoticeMsg = resultNoticeMsg;
    }

    public String getResultNoticeCode() {
        return resultNoticeCode;
    }

    public void setResultNoticeCode(String resultNoticeCode) {
        this.resultNoticeCode = resultNoticeCode;
    }
    @Column(name = "ntamt")
    private BigDecimal noticeAmount;

    
    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeNumber() {
        return noticeNumber;
    }

    public void setNoticeNumber(String noticeNumber) {
        this.noticeNumber = noticeNumber;
    }

    public BigDecimal getNoticeAmount() {
        return noticeAmount;
    }

    public void setNoticeAmount(BigDecimal noticeAmount) {
        this.noticeAmount = noticeAmount;
    }

    public NotList() {
    }

}
