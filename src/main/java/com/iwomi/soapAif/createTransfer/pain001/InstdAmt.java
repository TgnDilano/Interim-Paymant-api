package com.iwomi.soapAif.createTransfer.pain001;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class InstdAmt {
    @XmlAttribute(name = "Ccy", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private String ccy;

    @XmlValue
    private String value;
}
