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
public class CreateTransferRequestFlow {
    @XmlElement(name = "requestHeader", namespace = "http://soprabanking.com/amplitude")
    private RequestHeader requestHeader;

    @XmlElement(name = "createTransferRequest", namespace = "http://soprabanking.com/amplitude")
    private CreateTransferRequest createTransferRequest;
}
