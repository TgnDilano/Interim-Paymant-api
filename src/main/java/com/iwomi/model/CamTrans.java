/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.springframework.data.jpa.repository.Temporal;

/**
 *
 * @author Tebit Roger
 */
@Entity
@Table(name = "camtrans")
public class CamTrans extends AbstractModel<Long> {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
    @Column(name = "bkpn")
    private String bankPaymentNumber;
    @Column(name = "bkcd")
    private String bankCode;
    @Column(name = "bknm")
    private String bankName;
    @Column(name = "tpn")
    private String taxpayerNumber;
    @Column(name = "ttal")
    private BigDecimal totalAmount;
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    @Column(name = "pydt")
    private LocalDateTime paymentDate;
    @Column(name = "acnb")
    private String accountNumber;
    @Column(name = "pymd")
    private String paymentMethod;
    @Column(name = "acnm")
    private String accountName;
    @Column(name = "stts")
    private String status;// for the view
    @Column(name = "rst")
    private String result;//from camsis
    @Column(name = "msg")
    private String message;
    @Column(name = "prmsg")
    private String paymentResultMsg;
    @Column(name = "prcd")
    private String paymentResultCode;
    @Column(name = "epid")
    private Long epaymentId;
    @Column(name = "rmsg")
    private String resultMessage;
    @Column(name = "vstts")
    private String ventilationStatus;
    @Column(name = "vmsg")
    private String ventilationMessage;

    public String getVentilationStatus() {
        return ventilationStatus;
    }

    public void setVentilationStatus(String ventilationStatus) {
        this.ventilationStatus = ventilationStatus;
    }

    public String getVentilationMessage() {
        return ventilationMessage;
    }

    public void setVentilationMessage(String ventilationMessage) {
        this.ventilationMessage = ventilationMessage;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setNoticeList(List<NotList> noticeList) {
        this.noticeList = noticeList;
    }

    public CamTrans(String bankPaymentNumber, String bankCode, String bankName, String taxpayerNumber, BigDecimal totalAmount, LocalDateTime paymentDate, String accountNumber, String paymentMethod, String accountName, String status, String result, String message, String paymentResultMsg, String paymentResultCode, Long epaymentId, String resultMessage, String ventilationStatus, String ventilationMessage) {
        this.bankPaymentNumber = bankPaymentNumber;
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.taxpayerNumber = taxpayerNumber;
        this.totalAmount = totalAmount;
        this.paymentDate = paymentDate;
        this.accountNumber = accountNumber;
        this.paymentMethod = paymentMethod;
        this.accountName = accountName;
        this.status = status;
        this.result = result;
        this.message = message;
        this.paymentResultMsg = paymentResultMsg;
        this.paymentResultCode = paymentResultCode;
        this.epaymentId = epaymentId;
        this.resultMessage = resultMessage;
        this.ventilationStatus = ventilationStatus;
        this.ventilationMessage = ventilationMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPaymentResultMsg() {
        return paymentResultMsg;
    }

    public void setPaymentResultMsg(String paymentResultMsg) {
        this.paymentResultMsg = paymentResultMsg;
    }

    public String getPaymentResultCode() {
        return paymentResultCode;
    }

    public void setPaymentResultCode(String paymentResultCode) {
        this.paymentResultCode = paymentResultCode;
    }

    public Long getEpaymentId() {
        return epaymentId;
    }

    public void setEpaymentId(Long epaymentId) {
        this.epaymentId = epaymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "cam_not",
            joinColumns = {
                @JoinColumn(name = "cam_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "not_id", referencedColumnName = "id")})
    @Column(name = "ntlst")
    private List<NotList> noticeList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "cam_vt",
            joinColumns = {
                @JoinColumn(name = "cam_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "ven_id", referencedColumnName = "id")})
    @Column(name = "datevtl")    
    private List<DataVentile> dataventile= new ArrayList<>();
    
    public CamTrans() {
    }

    public List<DataVentile> getDataventile() {
        return dataventile;
    }

    public void setDataventile(List<DataVentile> dataventile) {
        this.dataventile = dataventile;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
    public String getBankPaymentNumber() {
        return bankPaymentNumber;
    }

    public void setBankPaymentNumber(String bankPaymentNumber) {
        this.bankPaymentNumber = bankPaymentNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getTaxpayerNumber() {
        return taxpayerNumber;
    }

    public void setTaxpayerNumber(String taxpayerNumber) {
        this.taxpayerNumber = taxpayerNumber;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public List<NotList> getNoticeList() {
        return noticeList;
    }

}
