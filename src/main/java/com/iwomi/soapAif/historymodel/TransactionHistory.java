//package com.iwomi.corebanking_interface.models.History;
//
//import com.iwomi.corebanking_interface.models.Structure.Language;
//import jakarta.xml.bind.annotation.XmlAccessType;
//import jakarta.xml.bind.annotation.XmlAccessorType;
//import jakarta.xml.bind.annotation.XmlElement;
//import jakarta.xml.bind.annotation.XmlRootElement;
//
//import java.util.Date;
//import java.util.List;
//
//public class TransactionHistory {
//
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public class Branch {
//        @XmlElement( name ="Code",namespace = "http://soprabanking.com/amplitude")
//        public int Code;
//        @XmlElement( name ="Designation",namespace = "http://soprabanking.com/amplitude")
//        public String Designation;
//    }
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public class Currency {
//        @XmlElement( name ="AlphaCode",namespace = "http://soprabanking.com/amplitude")
//        public String AlphaCode;
//        @XmlElement( name ="NumericCode",namespace = "http://soprabanking.com/amplitude")
//        public int NumericCode;
//        @XmlElement( name ="Designation",namespace = "http://soprabanking.com/amplitude")
//        public String Designation;
//        @XmlElement( name ="Currency",namespace = "http://soprabanking.com/amplitude")
//        public Currency Currency;
//        @XmlElement( name ="NumberOfDecimals",namespace = "http://soprabanking.com/amplitude")
//        public int NumberOfDecimals;
//    }
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public class AccountIdentifier {
//        @XmlElement( name ="Branch",namespace = "http://soprabanking.com/amplitude")
//        public Branch Branch;
//        @XmlElement( name ="Currency",namespace = "http://soprabanking.com/amplitude")
//        public Currency Currency;
//        @XmlElement( name ="Account",namespace = "http://soprabanking.com/amplitude")
//        public double Account;
//        @XmlElement( name ="Suffix",namespace = "http://soprabanking.com/amplitude")
//        public Object Suffix;
//    }
//
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public class MovementAmount {
//        @XmlElement( name ="Amount",namespace = "http://soprabanking.com/amplitude")
//        public double Amount;
//        @XmlElement( name ="Currency",namespace = "http://soprabanking.com/amplitude")
//        public Currency Currency;
//    }
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public class Module {
//        @XmlElement( name ="Code",namespace = "http://soprabanking.com/amplitude")
//        public String Code;
//        @XmlElement( name ="Name",namespace = "http://soprabanking.com/amplitude")
//        public String Name;
//    }
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public class EventOperation {
//        @XmlElement( name ="Code",namespace = "http://soprabanking.com/amplitude")
//        public int Code;
//        @XmlElement( name ="Designation",namespace = "http://soprabanking.com/amplitude")
//        public String Designation;
//    }
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public class OperationNature {
//        @XmlElement( name ="OperationNatureCode",namespace = "http://soprabanking.com/amplitude")
//        public String OperationNatureCode;
//        @XmlElement( name ="Designation",namespace = "http://soprabanking.com/amplitude")
//        public String Designation;
//    }
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public class Movement {
//        @XmlElement( name ="AccountHistoryMovementIdentifier",namespace = "http://soprabanking.com/amplitude")
//        public AccountHistoryMovementIdentifier AccountHistoryMovementIdentifier;
//        @XmlElement( name ="MovementNumber",namespace = "http://soprabanking.com/amplitude")
//        public int MovementNumber;
//        @XmlElement( name ="LedgerDate",namespace = "http://soprabanking.com/amplitude")
//        public Date LedgerDate;
//        @XmlElement( name ="ValueDate",namespace = "http://soprabanking.com/amplitude")
//        public Date ValueDate;
//        @XmlElement( name ="MovementAmount",namespace = "http://soprabanking.com/amplitude")
//        public MovementAmount MovementAmount;
//        @XmlElement( name ="MovementSide",namespace = "http://soprabanking.com/amplitude")
//        public String MovementSide;
//        @XmlElement( name ="Designation",namespace = "http://soprabanking.com/amplitude")
//        public String Designation;
//        @XmlElement( name ="Module",namespace = "http://soprabanking.com/amplitude")
//        public Module Module;
//        @XmlElement( name ="FileReference",namespace = "http://soprabanking.com/amplitude")
//        public String FileReference;
//        @XmlElement( name ="EventOperation",namespace = "http://soprabanking.com/amplitude")
//        public EventOperation EventOperation;
//        @XmlElement( name ="OperationNature",namespace = "http://soprabanking.com/amplitude")
//        public OperationNature OperationNature;
//        @XmlElement( name ="AdditionalDescriptionList",namespace = "http://soprabanking.com/amplitude")
//        public AdditionalDescriptionList AdditionalDescriptionList;
//    }
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public class AdditionalDescription {
//        @XmlElement( name ="OrderNumber",namespace = "http://soprabanking.com/amplitude")
//        public int OrderNumber;
//        @XmlElement( name ="Description",namespace = "http://soprabanking.com/amplitude")
//        public String Description;
//    }
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public class AdditionalDescriptionList {
//        @XmlElement( name ="AdditionalDescription",namespace = "http://soprabanking.com/amplitude")
//        public List<AdditionalDescription> AdditionalDescription;
//    }
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public class GetAccountHistoryMovementListResponse {
//        @XmlElement( name ="AccountIdentifier",namespace = "http://soprabanking.com/amplitude")
//        public AccountIdentifier AccountIdentifier;
//        @XmlElement( name ="Movement",namespace = "http://soprabanking.com/amplitude")
//        public List<Movement> Movement;
//    }
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public class GetAccountHistoryMovementListResponseFlow {
//        @XmlElement( name ="ResponseHeader",namespace = "http://soprabanking.com/amplitude")
//        public ResponseHeader ResponseHeader;
//        @XmlElement( name ="ResponseStatus",namespace = "http://soprabanking.com/amplitude")
//        public ResponseStatus ResponseStatus;
//        @XmlElement( name ="GetAccountHistoryMovementListResponse",namespace = "http://soprabanking.com/amplitude")
//        public GetAccountHistoryMovementListResponse GetAccountHistoryMovementListResponse;
////        public String Fjs1;
////        public String Text;
//    }
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public class Body {
//        @XmlElement(name = "GetAccountHistoryMovementListResponseFlow", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
//        public GetAccountHistoryMovementListResponseFlow GetAccountHistoryMovementListResponseFlow;
//    }
//
//
//
//}
