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
public class Language {
    @XmlElement(name ="Code", namespace = "http://soprabanking.com/amplitude")
    public String Code;
    @XmlElement(name ="Designation", namespace = "http://soprabanking.com/amplitude")
    public String Designation;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        this.Code = code;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        this.Designation = designation;
    }
}
