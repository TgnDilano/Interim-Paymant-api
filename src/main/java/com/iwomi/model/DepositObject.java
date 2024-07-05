    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.model;

/**
 *
 * @author HP
 */
public class DepositObject {
    public String amount;
    public String currency;
    public String externalId;
    public Payee payee;
    public String payerMessage;
    public String payeeNote;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
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

    public Payee getPayee() {
        return payee;
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
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

    public DepositObject(String amount, String currency, String externalId, Payee payee, String payerMessage, String payeeNote) {
        this.amount = amount;
        this.currency = currency;
        this.externalId = externalId;
        this.payee = payee;
        this.payerMessage = payerMessage;
        this.payeeNote = payeeNote;
    }

    public void setPayeeNote(String payeeNote) {
        this.payeeNote = payeeNote;
    }

    public DepositObject() {
    }
   
    
}


