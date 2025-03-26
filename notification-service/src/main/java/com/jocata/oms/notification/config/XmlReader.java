package com.jocata.oms.notification.config;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class XmlReader {

    public static EmailData readEmailDataFromXml(String filePath) {

        try {
            JAXBContext context = JAXBContext.newInstance(EmailData.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (EmailData) unmarshaller.unmarshal(new File(filePath));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
