package com.iwomi.soapAif.transfer;

import javax.xml.bind.annotation.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateTransferResponseFlow {
    @XmlElement(name = "responseHeader", namespace = "http://soprabanking.com/amplitude")
    public ResponseHeader responseHeader;

    @XmlElement(name = "responseStatus", namespace = "http://soprabanking.com/amplitude")
    public ResponseStatus responseStatus;

    @XmlElement(name = "createTransferResponse", namespace = "http://soprabanking.com/amplitude")
    public CreateTransferResponse createTransferResponse;
}
