package com.iwomi.soapAif.transfer;

import javax.xml.bind.annotation.*;

import cucumber.api.java.bs.A;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class Body {
    @XmlElement(name = "createTransferResponseFlow", namespace = "http://soprabanking.com/amplitude")
    public CreateTransferResponseFlow createTransferResponseFlow;
}
