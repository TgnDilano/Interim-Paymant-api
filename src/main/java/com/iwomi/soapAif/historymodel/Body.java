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
public class Body {
    @XmlElement(name = "getAccountHistoryMovementListResponseFlow", namespace = "http://soprabanking.com/amplitude")
    public GetAccountHistoryMovementListResponseFlow getAccountHistoryMovementListResponseFlow;
}