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
public class RequestHeader {
    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String requestId;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String serviceName;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String timestamp;

    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String userCode;
}
