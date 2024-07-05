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
public class DbtrAgt {
    @XmlElement(name = "FinInstnId", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private FinInstnId finInstnId;

    @XmlElement(name = "BrnchId", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03DB")
    private BrnchId brnchId;
}
