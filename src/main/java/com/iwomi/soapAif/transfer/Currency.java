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
public class Currency {
    @XmlElement( name ="alphaCode",namespace = "http://soprabanking.com/amplitude")
    public String alphaCode;
    @XmlElement( name ="numericCode",namespace = "http://soprabanking.com/amplitude")
    public String numericCode;
    @XmlElement( name ="designation",namespace = "http://soprabanking.com/amplitude")
    public String designation;
    @XmlElement( name ="currency",namespace = "http://soprabanking.com/amplitude")
    public Currency currency;
    @XmlElement( name ="numberOfDecimals",namespace = "http://soprabanking.com/amplitude")
    public String numberOfDecimals;
}