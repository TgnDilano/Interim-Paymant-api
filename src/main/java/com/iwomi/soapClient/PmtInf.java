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

// payment informations 
public class PmtInf {
   
    
    String PmtInfId; // jusqu a 35 en longueur Unique code for the  group of payment orders (“Payment Information” part of the message).  The first three characters shall be the code of the service – in this case, “AWB” (Alpha Web Banking). The next 32(maximum) characters will specify one unique code that is created by the customer The specific part  is allowed only once in the file 
    String PmtMtd; // Allowed value: “TRF”. 
    //file:///F:/projets/CCA/UTSIT-Vade-Mecum-2013-ISO20022-pour-les-entreprises.pdf   page 56
    String BtchBookg; // optionnal in the exemple i ve 0. but the documentation says. 
   // La balise « Batchbooking » - <BtchBookg> est optionnelle et au niveau du PaymentInformation. Elle permet au payeur d’indiquer à sa banque le mode de comptabilisation souhaité.
    //<BtchBookg>true</BtchBookg> <!-- comptabilisation globale --> <BtchBookg>false</BtchBookg> <!-- comptabilisation unitaire -->
    String NbOfTxs2;
    Double CtrlSum;
    String OrdrTp; // is the value in the test IMM 
    String OrdrMd; // the value on the example CREATE
    //endtoendidΟrdering institution’s reference code.  If not supplied by the ordering institution, then the value “NOTPROVIDED” is entered.    
   // PmtTpInf Information specifying the customer’s order. 
   String  InstrPrty;  // on the example norm somewhere HIGHT
   String  SvcLvlPrty;  //INTERNAL
   String  ReqdExctnDt;  // date d execution ( pour les virements simples pas d incidence.)

   public PmtInf(String PmtInfId, String PmtMtd, String BtchBookg, String NbOfTxs2, Double CtrlSum, String OrdrTp, String OrdrMd, String InstrPrty, String SvcLvlPrty, String ReqdExctnDt) {
        this.PmtInfId = PmtInfId;
        this.PmtMtd = PmtMtd;
        this.BtchBookg = BtchBookg;
        this.NbOfTxs2 = NbOfTxs2;
        this.CtrlSum = CtrlSum;
        this.OrdrTp = OrdrTp;
        this.OrdrMd = OrdrMd;
        this.InstrPrty = InstrPrty;
        this.SvcLvlPrty = SvcLvlPrty;
        this.ReqdExctnDt = ReqdExctnDt;
    }

    public String getPmtInfId() {
        return PmtInfId;
    }

    public void setPmtInfId(String PmtInfId) {
        this.PmtInfId = PmtInfId;
    }

    public String getPmtMtd() {
        return PmtMtd;
    }

    public void setPmtMtd(String PmtMtd) {
        this.PmtMtd = PmtMtd;
    }

    public String getBtchBookg() {
        return BtchBookg;
    }

    public void setBtchBookg(String BtchBookg) {
        this.BtchBookg = BtchBookg;
    }

    public String getNbOfTxs2() {
        return NbOfTxs2;
    }

    public void setNbOfTxs2(String NbOfTxs2) {
        this.NbOfTxs2 = NbOfTxs2;
    }

    public Double getCtrlSum() {
        return CtrlSum;
    }

    public void setCtrlSum(Double CtrlSum) {
        this.CtrlSum = CtrlSum;
    }

    public String getOrdrTp() {
        return OrdrTp;
    }

    public void setOrdrTp(String OrdrTp) {
        this.OrdrTp = OrdrTp;
    }

    public String getOrdrMd() {
        return OrdrMd;
    }

    public void setOrdrMd(String OrdrMd) {
        this.OrdrMd = OrdrMd;
    }

    public String getInstrPrty() {
        return InstrPrty;
    }

    public void setInstrPrty(String InstrPrty) {
        this.InstrPrty = InstrPrty;
    }

    public String getSvcLvlPrty() {
        return SvcLvlPrty;
    }

    public void setSvcLvlPrty(String SvcLvlPrty) {
        this.SvcLvlPrty = SvcLvlPrty;
    }

    public String getReqdExctnDt() {
        return ReqdExctnDt;
    }

    public void setReqdExctnDt(String ReqdExctnDt) {
        this.ReqdExctnDt = ReqdExctnDt;
    }
   
   
   
   
   final String CreateHeadInitiation ="<PmtInf>"
                                        + "<PmtInfId>A2017087467778957838899975</PmtInfId>"
                                        + "<PmtMtd>TRF</PmtMtd>"
                                        + "<BtchBookg>0</BtchBookg>"
                                        + "<NbOfTxs>1</NbOfTxs>"
                                        + "<CtrlSum>10000.0000</CtrlSum>"
                                        + "<DltPrvtData>"
                                        + "<OrdrPrties>"
                                            + "<Tp>IMM</Tp>"
                                            + "<Md>CREATE</Md>"
                                            + "</OrdrPrties>"
                                            + "</DltPrvtData>"
                                            + "<PmtTpInf>"
                                            + "<InstrPrty>NORM</InstrPrty>"
                                            + "<SvcLvl>"
                                            + "<Prtry>INTERNAL</Prtry>"
                                            + "</SvcLvl>"
                                            + "</PmtTpInf>"
                                            + "<ReqdExctnDt>2020-01-21</ReqdExctnDt>";
    
    
}
