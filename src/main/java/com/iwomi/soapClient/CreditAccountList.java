/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi.soapClient;

/**
 *
 * @author fabri
 */
//https://www.alpha.gr/-/media/alphagr/files/files-archive/businessbanking/sepaxmlpaymentfile_customerspecs_v1.pdf

public class CreditAccountList {
     String InstrId;  // the reference of Gimac
     String EndToEndId; //  can be the reference of gimac or 
     Double InstdAmt;  // the amount to be credited 
     String Ccy;      // the currency 
     String FinInstnIdNm;  // The name of the bank   BANQUE CCA SIEGE
     String BankId;       // le code bank  1039 cca
     String BankSchmeNmPrtry; //  ITF_DELTAMOP_IDETAB  ( pourrait etre fixe)
     String BrnchId;    // branchId 
     String BrnchIdNm;  // branch name  
     String CdtrAcctId;   // credit account 11 positions 
     String CdtrAcctSchmeNmPrtry;  //  type de compte c est mieux d utiliser la requete.   BKCOM_ACCOUNT
     String Ustrd;   // ΟΝus Entries: Use of the first 20 characters for the payment reason  description and of the remaining 120 for “free text”.
   
     public CreditAccountList(String InstrId, String EndToEndId, Double InstdAmt, String Ccy, String FinInstnIdNm, String BankId, String BankSchmeNmPrtry, String BrnchId, String BrnchIdNm, String CdtrAcctId, String CdtrAcctSchmeNmPrtry, String Ustrd) {
        this.InstrId = InstrId;
        this.EndToEndId = EndToEndId;
        this.InstdAmt = InstdAmt;
        this.Ccy = Ccy;
        this.FinInstnIdNm = FinInstnIdNm;
        this.BankId = BankId;
        this.BankSchmeNmPrtry = BankSchmeNmPrtry;
        this.BrnchId = BrnchId;
        this.BrnchIdNm = BrnchIdNm;
        this.CdtrAcctId = CdtrAcctId;
        this.CdtrAcctSchmeNmPrtry = CdtrAcctSchmeNmPrtry;
        this.Ustrd = Ustrd;
    }

    public String getInstrId() {
        return InstrId;
    }

    public void setInstrId(String InstrId) {
        this.InstrId = InstrId;
    }

    public String getEndToEndId() {
        return EndToEndId;
    }

    public void setEndToEndId(String EndToEndId) {
        this.EndToEndId = EndToEndId;
    }

    public Double getInstdAmt() {
        return InstdAmt;
    }

    public void setInstdAmt(Double InstdAmt) {
        this.InstdAmt = InstdAmt;
    }

    public String getCcy() {
        return Ccy;
    }

    public void setCcy(String Ccy) {
        this.Ccy = Ccy;
    }

    public String getFinInstnIdNm() {
        return FinInstnIdNm;
    }

    public void setFinInstnIdNm(String FinInstnIdNm) {
        this.FinInstnIdNm = FinInstnIdNm;
    }

    public String getBankId() {
        return BankId;
    }

    public void setBankId(String BankId) {
        this.BankId = BankId;
    }

    public String getBankSchmeNmPrtry() {
        return BankSchmeNmPrtry;
    }

    public void setBankSchmeNmPrtry(String BankSchmeNmPrtry) {
        this.BankSchmeNmPrtry = BankSchmeNmPrtry;
    }

    public String getBrnchId() {
        return BrnchId;
    }

    public void setBrnchId(String BrnchId) {
        this.BrnchId = BrnchId;
    }

    public String getBrnchIdNm() {
        return BrnchIdNm;
    }

    public void setBrnchIdNm(String BrnchIdNm) {
        this.BrnchIdNm = BrnchIdNm;
    }

    public String getCdtrAcctId() {
        return CdtrAcctId;
    }

    public void setCdtrAcctId(String CdtrAcctId) {
        this.CdtrAcctId = CdtrAcctId;
    }

    public String getCdtrAcctSchmeNmPrtry() {
        return CdtrAcctSchmeNmPrtry;
    }

    public void setCdtrAcctSchmeNmPrtry(String CdtrAcctSchmeNmPrtry) {
        this.CdtrAcctSchmeNmPrtry = CdtrAcctSchmeNmPrtry;
    }

    public String getUstrd() {
        return Ustrd;
    }

    public void setUstrd(String Ustrd) {
        this.Ustrd = Ustrd;
    }
     
       
}

   
