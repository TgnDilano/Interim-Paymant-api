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

//file:///F:/projets/CCA/UTSIT-Vade-Mecum-2013-ISO20022-pour-les-entreprises.pdf  page 56 bonne description
//https://www.alpha.gr/-/media/alphagr/files/files-archive/businessbanking/sepaxmlpaymentfile_customerspecs_v1.pdf

public class HeadGen {
    
    
    String MsgId;  // should be unique 
    String CreDtTm;         // date ad time the file was created  YYYY-MMDDThh:mm:ss.sss (eg 2015-06-25T11:17:06.345) 
    String NbOfTxs; //   
    Double CtrlSum;  // somme de tous les montants credité. 
    String FlwInd;   // example HOMOLOGATION  
    String PrvtDtInf;  // la valeur du canal 
    String CdOrPrtry;  // exemple de donnees CHANNEL

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String MsgId) {
        this.MsgId = MsgId;
    }

    public String getCreDtTm() {
        return CreDtTm;
    }

    public void setCreDtTm(String CreDtTm) {
        this.CreDtTm = CreDtTm;
    }

    public String getNbOfTxs() {
        return NbOfTxs;
    }

    public void setNbOfTxs(String NbOfTxs) {
        this.NbOfTxs = NbOfTxs;
    }

    public Double getCtrlSum() {
        return CtrlSum;
    }

    public void setCtrlSum(Double CtrlSum) {
        this.CtrlSum = CtrlSum;
    }

    public String getFlwInd() {
        return FlwInd;
    }

    public void setFlwInd(String FlwInd) {
        this.FlwInd = FlwInd;
    }

    public String getPrvtDtInf() {
        return PrvtDtInf;
    }

    public void setPrvtDtInf(String PrvtDtInf) {
        this.PrvtDtInf = PrvtDtInf;
    }

    public String getCdOrPrtry() {
        return CdOrPrtry;
    }

    public void setCdOrPrtry(String CdOrPrtry) {
        this.CdOrPrtry = CdOrPrtry;
    }

//    
//        final String CreateHead = "<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB\">"
//                            + "<CstmrCdtTrfInitn><GrpHdr><MsgId>A2017087467738477889970803875</MsgId>"
//                            + "<CreDtTm>2020-01-14T17:57:29</CreDtTm>"
//                            + "<NbOfTxs>1</NbOfTxs><CtrlSum>10000.0000</CtrlSum>"
//                            + "<InitgPty/><DltPrvtData><FlwInd>HOMOLOGATION</FlwInd><DltPrvtDataDtl>"
//                            + "<PrvtDtInf>CANAL_VRT_WEB_MODIF</PrvtDtInf><Tp><CdOrPrtry><Cd>CHANNEL</Cd>"
//                            + "</CdOrPrtry></Tp></DltPrvtDataDtl></DltPrvtData></GrpHdr>";
//    //https://www.alpha.gr/-/media/alphagr/files/files-archive/businessbanking/sepaxmlpaymentfile_customerspecs_v1.pdf
//  
//    
//    

    public HeadGen(String MsgId, String CreDtTm, String NbOfTxs, Double CtrlSum, String FlwInd, String PrvtDtInf, String CdOrPrtry) {
        this.MsgId = MsgId;
        this.CreDtTm = CreDtTm;
        this.NbOfTxs = NbOfTxs;
        this.CtrlSum = CtrlSum;
        this.FlwInd = FlwInd;
        this.PrvtDtInf = PrvtDtInf;
        this.CdOrPrtry = CdOrPrtry;
    }
    
    
    
    
}
