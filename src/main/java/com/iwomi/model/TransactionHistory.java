/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "thisty")

public class TransactionHistory extends AbstractModel<Long> {

    @Column
    private int amount;
    @Column
    private String currency;
    @Column
    private String name;
    @Column
    private String externalId;
    @Column
    private String payer;
    @Column
    private String payerMessage;
    @Column
    private String payeeNote;
    @Column
    private String status;
    @Column
    private String idReference;
    @Column
    private String opty;//operation_type
    @Column
    private Date doc;//date of operation
    @Column
    private String pty;//payertype
    @Column
    private String reason;
    @Column
    private String cmptbkst;
    @Column
    private String codser;
    @Column
    private int cntSend;
    public TransactionHistory(int amount, String name,String currency, String externalId, String payer, String payerMessage, String payeeNote, String status, String idReference, String operation_type, Date date_create, String payertype, String reason, String cmptbkst, String codser, int finTranId, String date_modify) {
        this.amount = amount;
        this.currency = currency;
        this.externalId = externalId;
        this.payer = payer;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.status = status;
        this.idReference = idReference;
        this.opty = operation_type;
        this.doc = date_create;
        this.pty = payertype;
        this.reason = reason;
        this.cmptbkst = cmptbkst;
        this.codser = codser;
        this.finTranId = finTranId;
        this.date_modify = date_modify;
        this.name = name;
    }

    public int getCntSend() {
        return cntSend;
    }

    public void setCntSend(int cntSend) {
        this.cntSend = cntSend;
    }

    public String getCodser() {
        return codser;
    }

    public void setCodser(String codser) {
        this.codser = codser;
    }

    public TransactionHistory(int amount, String name, String currency, String externalId, String payer, String payerMessage, String payeeNote, String status, String idReference, String operation_type, Date date_create, String payertype, String reason, String cmptbkst, int finTranId, String date_modify) {
        this.amount = amount;
        this.currency = currency;
        this.externalId = externalId;
        this.payer = payer;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.status = status;
        this.idReference = idReference;
        this.opty = operation_type;
        this.doc = date_create;
        this.pty = payertype;
        this.reason = reason;
        this.cmptbkst = cmptbkst;
        this.finTranId = finTranId;
        this.date_modify = date_modify;
        this.name = name;
        
    }

    public String getCmptbkst() {
        return cmptbkst;
    }

    public void setCmptbkst(String cmptbkst) {
        this.cmptbkst = cmptbkst;
    }
    private int finTranId;
    private String date_modify;

    public TransactionHistory() {
    }

    public TransactionHistory(int amount, String name,String currency, String externalId, String payer, String payerMessage, String payeeNote, String status, String idReference, String operation_type, Date date_create, String payertype, String reason) {
        this.amount = amount;
        this.currency = currency;
        this.externalId = externalId;
        this.payer = payer;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.status = status;
        this.idReference = idReference;
        this.opty = operation_type;
        this.doc = date_create;
        this.pty = payertype;
        this.reason = reason;
        this.finTranId = finTranId;
        this.name = name;
    }

    public TransactionHistory(int amount, String name, String currency, String externalId, String payer, String payerMessage, String payeeNote, String status, String idReference, String operation_type, String payertype, String reason, String date_modify) {
        this.amount = amount;
        this.currency = currency;
        this.date_modify = date_modify;
        this.externalId = externalId;
        this.payer = payer;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.status = status;
        this.idReference = idReference;
        this.opty = operation_type;
        this.doc = doc;
        this.pty = payertype;
        this.reason = reason;
        this.finTranId = finTranId;
        this.name = name;
    }

    public TransactionHistory(String amount, String name,String currency, String externalId, String payer, String payerMessage, String payeeNote, String status, String idReference, String operation_type, String payertype, String reason, String date_modify) {
        this.amount = Integer.parseInt(amount);
        this.currency = currency;
        this.date_modify = date_modify;
        this.externalId = externalId;
        this.payer = payer;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.status = status;
        this.idReference = idReference;
        this.opty = operation_type;
        this.doc = doc;
        this.pty = payertype;
        this.reason = reason;
        this.finTranId = finTranId;
        this.name = name;
    }

    public String getDate_modify() {
        return date_modify;
    }

    public void setDate_modify(String date_modify) {
        this.date_modify = date_modify;
    }

    public TransactionHistory(int i, String name, String test, String test0, String test1, String test2, String test3, String test4, String test5, String test6, Date date, String test7, String test8, int i0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getPayer() {
        return payer;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPayerMessage() {
        return payerMessage;
    }

    public void setPayerMessage(String payerMessage) {
        this.payerMessage = payerMessage;
    }

    public String getPayeeNote() {
        return payeeNote;
    }

    public void setPayeeNote(String payeeNote) {
        this.payeeNote = payeeNote;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdReference() {
        return idReference;
    }

    public void setIdReference(String idReference) {
        this.idReference = idReference;
    }

    public String getOpty() {
        return opty;
    }

    public void setOpty(String opty) {
        this.opty = opty;
    }

    public Date getDoc() {
        return doc;
    }

    public void setDoc(Date doc) {
        this.doc = doc;
    }

    public String getPty() {
        return pty;
    }

    public void setPty(String pty) {
        this.pty = pty;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getFinTranId() {
        return finTranId;
    }

    public void setFinTranId(int finTranId) {
        this.finTranId = finTranId;
    }

}

