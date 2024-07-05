package com.iwomi.soapAif.createTransfer.pain001;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class CdtTrfTxInf {
    @XmlElement(name = "PmtId", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private PmtId pmtId;

    @XmlElement(name = "PmtTpInf", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private PmtTpInf pmtTpInf;

    @XmlElement(name = "Amt", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private Amt amt;

    @XmlElement(name = "ChrgAmt", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private ChrgAmt chrgAmt;

    @XmlElement(name = "CdtrAgt", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private CdtrAgt cdtrAgt;

    @XmlElement(name = "Cdtr", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private Cdtr cdtr;

    @XmlElement(name = "CdtrAcct", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private CdtrAcct cdtrAcct;

    @XmlElement(name = "RmtInf", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private RmtInf rmtInf;

}
