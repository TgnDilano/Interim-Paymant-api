package com.iwomi.soapAif.infomodel;

import javax.xml.bind.annotation.XmlElement;

public class Customer {
    @XmlElement(name = "customerNumber", namespace = "http://soprabanking.com/amplitude")
    public String customerNumber;

    @XmlElement(name = "displayedName", namespace = "http://soprabanking.com/amplitude")
    public String displayedName;
}
