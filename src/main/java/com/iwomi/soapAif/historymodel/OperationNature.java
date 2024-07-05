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
public class OperationNature {
    @XmlElement( name ="operationNatureCode",namespace = "http://soprabanking.com/amplitude")
    public String operationNatureCode;
    @XmlElement( name ="designation",namespace = "http://soprabanking.com/amplitude")
    public String designation;
}