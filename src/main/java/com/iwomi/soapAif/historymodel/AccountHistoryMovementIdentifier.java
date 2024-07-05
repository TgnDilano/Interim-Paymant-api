package com.iwomi.soapAif.historymodel;

import javax.xml.bind.annotation.*;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountHistoryMovementIdentifier {
    @XmlElement( name ="IntegrationHistoryDate",namespace = "http://soprabanking.com/amplitude")
    public Date IntegrationHistoryDate;
    @XmlElement( name ="EntryIdentifier",namespace = "http://soprabanking.com/amplitude")
    public String EntryIdentifier;
    @XmlElement( name ="OrderNumberIdentifier",namespace = "http://soprabanking.com/amplitude")
    public int OrderNumberIdentifier;
}