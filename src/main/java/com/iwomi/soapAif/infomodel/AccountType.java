package com.iwomi.soapAif.infomodel;

import javax.xml.bind.annotation.XmlElement;

public class AccountType {
    @XmlElement(name = "code", namespace = "http://soprabanking.com/amplitude")
    public String code;

    @XmlElement(name = "designation", namespace = "http://soprabanking.com/amplitude")
    public String designation;
}
