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
public class Branch {
    @XmlElement( name ="code",namespace = "http://soprabanking.com/amplitude")
    public String code;
    @XmlElement( name ="designation",namespace = "http://soprabanking.com/amplitude")
    public String designation;
}