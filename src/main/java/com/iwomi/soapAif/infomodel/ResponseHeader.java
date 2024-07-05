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
public class ResponseHeader {
    @XmlElement(name = "requestId", namespace = "http://soprabanking.com/amplitude")
    private String requestId;

    @XmlElement(name = "responseId", namespace = "http://soprabanking.com/amplitude")
    private String responseId;

    @XmlElement(name = "timestamp", namespace = "http://soprabanking.com/amplitude")
    private String timestamp;

    @XmlElement(name = "serviceVersion", namespace = "http://soprabanking.com/amplitude")
    private String serviceVersion;

    @XmlElement(name = "language", namespace = "http://soprabanking.com/amplitude")
    private Language language;
}
