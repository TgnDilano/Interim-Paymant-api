package com.iwomi.soapAif.infomodel;

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
public class Currency {
    @XmlElement(name = "alphaCode", namespace = "http://soprabanking.com/amplitude")
    private String alphaCode;

    @XmlElement(name = "numericCode", namespace = "http://soprabanking.com/amplitude")
    private String numericCode;

    @XmlElement(name = "designation", namespace = "http://soprabanking.com/amplitude")
    private String designation;
}
