package com.liu;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import java.io.File;

public class Test {
    @org.junit.Test
    public void test01() throws SAXException, DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File("E:\\GitRepository\\2024study\\RPCInfra\\soyan-rpc-core\\src\\test\\java\\com\\liu\\test.xml"));
        Element rootElement = document.getRootElement();
        Element person = rootElement.element("person");
        String name = person.elementText("name");
        System.out.println(name);
    }
}
