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
public class ResponseStatus {
    @XmlElement( name ="statusCode",namespace = "http://soprabanking.com/amplitude")
    public String statusCode;
}

