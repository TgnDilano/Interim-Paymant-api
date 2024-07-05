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
public class ResponseHeader {
    @XmlElement( name ="requestId",namespace = "http://soprabanking.com/amplitude")
    public String requestId;

    @XmlElement ( name ="responseId", namespace = "http://soprabanking.com/amplitude")
    public String responseId;

    @XmlElement( name ="timestamp",namespace = "http://soprabanking.com/amplitude")
    public String timestamp;

    @XmlElement( name ="serviceVersion",namespace = "http://soprabanking.com/amplitude")
    public String serviceVersion;

    @XmlElement( name ="language",namespace = "http://soprabanking.com/amplitude")
    public Language language;
}