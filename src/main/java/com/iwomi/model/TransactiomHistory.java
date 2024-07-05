/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "TransactionHistory")

public class TransactiomHistory extends AbstractModel<Long> {

    private int amount;
    private String currency;
    private String externalId;
    private String payer;
    private String payerMessage;
    private String payeeNote;
    private String status;
    private String idReference;
    private String operation_type;
    private Date date_create;
    private String payertype;
    private String reason;
    private String cmptbkst;
    private String codser;
    private int cntSend;
    Date date =  new Date();
    public TransactiomHistory(int amount, String currency, String externalId, String payer, String payerMessage, String payeeNote, String status, String idReference, String operation_type, Date date_create, String payertype, String reason, String cmptbkst, String codser, int finTranId, String date_modify) {
        this.amount = amount;
        this.currency = currency;
        this.externalId = externalId;
        this.payer = payer;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.status = status;
        this.idReference = idReference;
        this.operation_type = operation_type;
        this.date_create = date_create;
        this.payertype = payertype;
        this.reason = reason;
        this.cmptbkst = cmptbkst;
        this.codser = codser;
        this.finTranId = finTranId;
        this.date_modify = date_modify;
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

    public TransactiomHistory(int amount, String currency, String externalId, String payer, String payerMessage, String payeeNote, String status, String idReference, String operation_type, Date date_create, String payertype, String reason, String cmptbkst, int finTranId, String date_modify) {
        this.amount = amount;
        this.currency = currency;
        this.externalId = externalId;
        this.payer = payer;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.status = status;
        this.idReference = idReference;
        this.operation_type = operation_type;
        this.date_create = date_create;
        this.payertype = payertype;
        this.reason = reason;
        this.cmptbkst = cmptbkst;
        this.finTranId = finTranId;
        this.date_modify = date_modify;
        
    }

    public String getCmptbkst() {
        return cmptbkst;
    }

    public void setCmptbkst(String cmptbkst) {
        this.cmptbkst = cmptbkst;
    }
    private int finTranId;
    private String date_modify;

    public TransactiomHistory() {
    }

    public TransactiomHistory(int amount, String currency, String externalId, String payer, String payerMessage, String payeeNote, String status, String idReference, String operation_type, Date date_create, String payertype, String reason) {
        this.amount = amount;
        this.currency = currency;
        this.externalId = externalId;
        this.payer = payer;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.status = status;
        this.idReference = idReference;
        this.operation_type = operation_type;
        this.date_create = date_create;
        this.payertype = payertype;
        this.reason = reason;
        this.finTranId = finTranId;
    }

    public TransactiomHistory(int amount, String currency, String externalId, String payer, String payerMessage, String payeeNote, String status, String idReference, String operation_type, String payertype, String reason, String date_modify) {
        this.amount = amount;
        this.currency = currency;
        this.date_modify = date_modify;
        this.externalId = externalId;
        this.payer = payer;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.status = status;
        this.idReference = idReference;
        this.operation_type = operation_type;
        this.date_create = date_create;
        this.payertype = payertype;
        this.reason = reason;
        this.finTranId = finTranId;
    }

    public TransactiomHistory(String amount, String currency, String externalId, String payer, String payerMessage, String payeeNote, String status, String idReference, String operation_type, String payertype, String reason, String date_modify) {
        this.amount = Integer.parseInt(amount);
        this.currency = currency;
        this.date_modify = date_modify;
        this.externalId = externalId;
        this.payer = payer;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
        this.status = status;
        this.idReference = idReference;
        this.operation_type = operation_type;
        this.date_create = date;
        this.payertype = payertype;
        this.reason = reason;
        this.finTranId = finTranId;
    }

    public String getDate_modify() {
        return date_modify;
    }

    public void setDate_modify(String date_modify) {
        this.date_modify = date_modify;
    }

    public TransactiomHistory(int i, String test, String test0, String test1, String test2, String test3, String test4, String test5, String test6, Date date, String test7, String test8, int i0) {
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

    public String getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(String operation_type) {
        this.operation_type = operation_type;
    }

    public Date getDate_create() {
        return date_create;
    }

    public void setDate_create(Date date_create) {
        this.date_create = date_create;
    }

    public String getPayertype() {
        return payertype;
    }

    public void setPayertype(String payertype) {
        this.payertype = payertype;
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
