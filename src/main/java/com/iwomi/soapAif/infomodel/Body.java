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
public class Body {
    @XmlElement(name = "getAccountDetailResponseFlow", namespace = "http://soprabanking.com/amplitude")
    private GetAccountDetailResponseFlow getAccountDetailResponseFlow;
}
