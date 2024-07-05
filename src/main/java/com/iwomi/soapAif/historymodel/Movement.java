package com.iwomi.soapAif.historymodel;

import javax.xml.bind.annotation.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class Movement {
    @XmlElement( name ="accountHistoryMovementIdentifier",namespace = "http://soprabanking.com/amplitude")
    public AccountHistoryMovementIdentifier accountHistoryMovementIdentifier;
    @XmlElement( name ="movementNumber",namespace = "http://soprabanking.com/amplitude")
    public String movementNumber;
    @XmlElement( name ="LedgerDate",namespace = "http://soprabanking.com/amplitude")
    public Date LedgerDate;
    @XmlElement( name ="valueDate",namespace = "http://soprabanking.com/amplitude")
    public Date valueDate;
    @XmlElement( name ="movementAmount",namespace = "http://soprabanking.com/amplitude")
    public MovementAmount movementAmount;
    @XmlElement( name ="movementSide",namespace = "http://soprabanking.com/amplitude")
    public String movementSide;
    @XmlElement( name ="designation",namespace = "http://soprabanking.com/amplitude")
    public String designation;
    @XmlElement( name ="module",namespace = "http://soprabanking.com/amplitude")
    public Module module;
    @XmlElement( name ="fileReference",namespace = "http://soprabanking.com/amplitude")
    public String fileReference;
    @XmlElement( name ="eventOperation",namespace = "http://soprabanking.com/amplitude")
    public EventOperation eventOperation;
    @XmlElement( name ="operationNature",namespace = "http://soprabanking.com/amplitude")
    public OperationNature operationNature;
    @XmlElement( name ="additionalDescriptionList",namespace = "http://soprabanking.com/amplitude")
    public AdditionalDescriptionList additionalDescriptionList;
}