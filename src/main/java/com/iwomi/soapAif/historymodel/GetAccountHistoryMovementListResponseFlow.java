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
public class GetAccountHistoryMovementListResponseFlow {
    @XmlElement( name ="responseHeader",namespace = "http://soprabanking.com/amplitude")
    public ResponseHeader responseHeader;
    @XmlElement( name ="responseStatus",namespace = "http://soprabanking.com/amplitude")
    public ResponseStatus responseStatus;
    @XmlElement( name ="getAccountHistoryMovementListResponse",namespace = "http://soprabanking.com/amplitude")
    public GetAccountHistoryMovementListResponse getAccountHistoryMovementListResponse;

}