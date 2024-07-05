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

public class DebitAccount {
    
    String acc; // le compte sur 11 positions 
    String accPrtry;  // le type de compte exple de valeur  BKCOM_ACCOUNT
    String ccy;  //  la devise exple XAF
    String bkname; // le nom de la banque 
    String bkid;  //   l id de la banque exple pour cca 10039
    String bkPrtry; // la propriete de la  banque exple ITF_DELTAMOP_IDETAB
    String ageId;  // l agence 
    String ageName;  // le nom de l agence 

    public DebitAccount(String acc, String accPrtry, String ccy, String bkname, String bkid, String bkPrtry, String ageId, String ageName) {
        
        this.acc = acc;
        this.accPrtry = accPrtry;
        this.ccy = ccy;
        this.bkname = bkname;
        this.bkid = bkid;
        this.bkPrtry = bkPrtry;
        this.ageId = ageId;
        this.ageName = ageName;
        
    } 

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getAccPrtry() {
        return accPrtry;
    }

    public void setAccPrtry(String accPrtry) {
        this.accPrtry = accPrtry;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getBkname() {
        return bkname;
    }

    public void setBkname(String bkname) {
        this.bkname = bkname;
    }

    public String getBkid() {
        return bkid;
    }

    public void setBkid(String bkid) {
        this.bkid = bkid;
    }

    public String getBkPrtry() {
        return bkPrtry;
    }

    public void setBkPrtry(String bkPrtry) {
        this.bkPrtry = bkPrtry;
    }

    public String getAgeId() {
        return ageId;
    }

    public void setAgeId(String ageId) {
        this.ageId = ageId;
    }

    public String getAgeName() {
        return ageName;
    }

    public void setAgeName(String ageName) {
        this.ageName = ageName;
    }
    
}
