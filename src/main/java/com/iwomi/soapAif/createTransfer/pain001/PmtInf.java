package com.iwomi.soapAif.createTransfer.pain001;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class PmtInf {
    @XmlElement(name = "PmtInfId", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private String pmtInfId;

    @XmlElement(name = "PmtMtd", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private String pmtMtd;

    @XmlElement(name = "BtchBookg", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private int btchBookg;

    @XmlElement(name = "NbOfTxs", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private int nbOfTxs;

    @XmlElement(name = "CtrlSum", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private String ctrlSum;

    @XmlElement(name = "DltPrvtData", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private DltPrvtData dltPrvtData;

    @XmlElement(name = "ReqdExctnDt", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private String reqdExctnDt;

    @XmlElement(name = "Dbtr", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private Dbtr dbtr;

    @XmlElement(name = "DbtrAcct", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private DbtrAcct dbtrAcct;

    @XmlElement(name = "DbtrAgt", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private DbtrAgt dbtrAgt;

    @XmlElement(name = "CdtTrfTxInf", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private List<CdtTrfTxInf> cdtTrfTxInf;
}
