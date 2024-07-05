package com.iwomi.soapAif.historymodel;

import javax.xml.bind.annotation.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class GetAccountHistoryMovementListResponse {
    @XmlElement( name ="accountIdentifier",namespace = "http://soprabanking.com/amplitude")
    public AccountIdentifier accountIdentifier;
    @XmlElement( name ="movement",namespace = "http://soprabanking.com/amplitude")
    public List<Movement> movement;
}