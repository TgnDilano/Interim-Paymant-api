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
public class AdditionalDescription {
    @XmlElement( name ="OrderNumber",namespace = "http://soprabanking.com/amplitude")
    public int OrderNumber;
    @XmlElement( name ="Description",namespace = "http://soprabanking.com/amplitude")
    public String Description;
}