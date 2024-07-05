package com.iwomi.soapAif.transfer;

import javax.xml.bind.annotation.*;
import lombok.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class CreateTransferResponse {

    @XmlElement(name = "pain002", namespace = "http://soprabanking.com/amplitude")
    private String pain002;

    // Getters and setters
    public String getPain002() {
        return pain002;
    }

    public void setPain002(String pain002) {
        this.pain002 = pain002;
    }
}

