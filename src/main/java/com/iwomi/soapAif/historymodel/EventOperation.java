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
public class EventOperation {
    @XmlElement( name ="code",namespace = "http://soprabanking.com/amplitude")
    public String code;
    @XmlElement( name ="designation",namespace = "http://soprabanking.com/amplitude")
    public String designation;
}

