package com.iwomi.soapAif.historymodel;

import javax.xml.bind.annotation.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseHeader {
    @XmlElement( name ="requestId",namespace = "http://soprabanking.com/amplitude")
    private String requestId;

    @XmlElement ( name ="responseId", namespace = "http://soprabanking.com/amplitude")
    private String responseId;

    @XmlElement( name ="timestamp",namespace = "http://soprabanking.com/amplitude")
    private String timestamp;

    @XmlElement( name ="serviceVersion",namespace = "http://soprabanking.com/amplitude")
    private String serviceVersion;

    @XmlElement( name ="language",namespace = "http://soprabanking.com/amplitude")
    private Language language;
}