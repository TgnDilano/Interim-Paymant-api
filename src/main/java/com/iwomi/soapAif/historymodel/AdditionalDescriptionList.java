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
public class AdditionalDescriptionList {
    @XmlElement( name ="additionalDescription",namespace = "http://soprabanking.com/amplitude")
    public List<AdditionalDescription> additionalDescription;
}
