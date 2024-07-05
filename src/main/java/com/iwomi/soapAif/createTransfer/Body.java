package com.iwomi.soapAif.createTransfer;

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
public class Body {
    @XmlElement(name = "createTransferRequestFlow", namespace = "http://soprabanking.com/amplitude")
    public CreateTransferRequestFlow createTransferRequestFlow;
}
