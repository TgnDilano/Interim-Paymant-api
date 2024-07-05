package com.iwomi.soapAif.infomodel;

import lombok.*;

import javax.xml.bind.annotation.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountDetailEnvelope {
    @XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
    private Body body;
}
