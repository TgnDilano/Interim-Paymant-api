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
public class MovementAmount {
    @XmlElement( name ="Amount",namespace = "http://soprabanking.com/amplitude")
    public double Amount;
    @XmlElement( name ="Currency",namespace = "http://soprabanking.com/amplitude")
    public Currency Currency;
}