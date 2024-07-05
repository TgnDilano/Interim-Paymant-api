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
public class Module {
    @XmlElement( name ="Code",namespace = "http://soprabanking.com/amplitude")
    public String Code;
    @XmlElement( name ="Name",namespace = "http://soprabanking.com/amplitude")
    public String Name;
}
