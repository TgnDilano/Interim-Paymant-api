/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import javax.persistence.Column;

/**
 *
 * @author Tebit Roger
 */
public class OnPaidObj {
    private String noticeNumber;

    public OnPaidObj() {
    }

    public String getNoticeNumber() {
        return noticeNumber;
    }

    public void setNoticeNumber(String noticeNumber) {
        this.noticeNumber = noticeNumber;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getTaxpayerNumber() {
        return taxpayerNumber;
    }

    public void setTaxpayerNumber(String taxpayerNumber) {
        this.taxpayerNumber = taxpayerNumber;
    }

    public String getTaxpayerRepresentativeNumber() {
        return taxpayerRepresentativeNumber;
    }

    public void setTaxpayerRepresentativeNumber(String taxpayerRepresentativeNumber) {
        this.taxpayerRepresentativeNumber = taxpayerRepresentativeNumber;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    @Column(name = "ntdate")
    private String notificationDate;
    @Column(name = "txpyn")
    private String taxpayerNumber;
    @Column(name = "txprn")
    private String taxpayerRepresentativeNumber;
    @Column(name = "dudt")
    private String dueDate;
    
}
