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
public class AccountIdentifier {
    @XmlElement( name ="branch",namespace = "http://soprabanking.com/amplitude")
    public Branch branch;
    @XmlElement( name ="currency",namespace = "http://soprabanking.com/amplitude")
    public Currency currency;
    @XmlElement( name ="account",namespace = "http://soprabanking.com/amplitude")
    public String account;
    @XmlElement( name ="suffix",namespace = "http://soprabanking.com/amplitude")
    public String suffix;
}