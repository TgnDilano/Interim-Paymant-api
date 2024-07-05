package com.iwomi.soapAif.infomodel;

import javax.xml.bind.annotation.XmlElement;

public class User {
    @XmlElement(name = "code", namespace = "http://soprabanking.com/amplitude")
    public String code;

    @XmlElement(name = "name", namespace = "http://soprabanking.com/amplitude")
    public String name;

    @XmlElement(name = "forcingLevel", namespace = "http://soprabanking.com/amplitude")
    public String forcingLevel;

    @XmlElement(name = "language", namespace = "http://soprabanking.com/amplitude")
    public Language language;
}
