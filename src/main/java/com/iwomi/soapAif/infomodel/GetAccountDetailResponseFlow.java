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
public class GetAccountDetailResponseFlow {
    @XmlElement(name = "responseHeader", namespace = "http://soprabanking.com/amplitude")
    private ResponseHeader responseHeader;

    @XmlElement(name = "responseStatus", namespace = "http://soprabanking.com/amplitude")
    private ResponseStatus responseStatus;

    @XmlElement(name = "getAccountDetailResponse", namespace = "http://soprabanking.com/amplitude")
    private GetAccountDetailResponse getAccountDetailResponse;
}
