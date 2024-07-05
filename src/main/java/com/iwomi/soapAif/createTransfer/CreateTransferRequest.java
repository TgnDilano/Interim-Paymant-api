package com.iwomi.soapAif.createTransfer;

import com.iwomi.soapAif.createTransfer.pain001.Document;
import lombok.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.StringReader;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateTransferRequest {
    @XmlElement(namespace = "http://soprabanking.com/amplitude")
    private String canal;
    @XmlElement(name = "pain001", namespace = "http://soprabanking.com/amplitude")
    private String pain001;

    public void updatePain001(Document pain001) {
        this.pain001 = "<![CDATA[<?xml version='1.0' encoding='UTF-8'?>" + pain001.toString() + "]]>";
    }

    public String obtainPain001Doc() {
        int startIdx = this.pain001.indexOf("<Document");
        int endIdx = this.pain001.lastIndexOf("</Document>") + "</Document>".length();
        return this.pain001.substring(startIdx, endIdx);
    }

    public Document getPain001Document(){
        try {
            var xmlData = obtainPain001Doc();
            JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader(xmlData);
            Document object = (Document) jaxbUnmarshaller.unmarshal(reader);

            System.out.println("Pain001 Doc: "+ object.toString());

            return object;
        } catch (JAXBException e) {
            return null;
        }
    }
}
