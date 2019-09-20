/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forth.ics.isl.XML;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 *
 * @author mhalkiad
 */

public class XMLBuilder {
    
    private String xmlFilePath;
    private DocumentBuilderFactory documentFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private Element oaiPmh;
    private Element listRecordsElement;
    
    
    public XMLBuilder(String xmlFilePath) throws ParserConfigurationException {
        
        this.xmlFilePath = xmlFilePath;
        
        documentFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentFactory.newDocumentBuilder();
        document = documentBuilder.newDocument();
        
        oaiPmh = document.createElement("OAI-PMH");
        document.appendChild(oaiPmh);   
    }
    
    
    public void createOaipmhElement(String xmlns, String xsi, String schemaLocation) {
        

        Attr xmlnsAttr = document.createAttribute("xmlns");
        xmlnsAttr.setValue(xmlns);
        
        Attr xsiAttr = document.createAttribute("xmlns:xsi");
        xsiAttr.setValue(xsi);
        
        Attr schemaLocationAttr = document.createAttribute("xsi:schemaLocation");
        schemaLocationAttr.setValue(schemaLocation);
        
        oaiPmh.setAttributeNode(xmlnsAttr);
        oaiPmh.setAttributeNode(xsiAttr);
        oaiPmh.setAttributeNode(schemaLocationAttr);       
    }
    
    
    public void createResponseDateElement() {
        
        Element responseDate = document.createElement("responseDate");
        responseDate.appendChild(document.createTextNode(Instant.now().toString()));
        oaiPmh.appendChild(responseDate);   
    }
    
    
    public void createRequestElement(String serviceURL) {
        
        Element requestElement = document.createElement("request");
        oaiPmh.appendChild(requestElement);
        
        Attr verbAttr = document.createAttribute("verb");
        verbAttr.setValue("ListRecords");
        requestElement.setAttributeNode(verbAttr);
        
        Attr metadataPrefixAttr = document.createAttribute("metadataPrefix");
        metadataPrefixAttr.setValue("FORTH");
        requestElement.setAttributeNode(metadataPrefixAttr);
        
        Attr setAttr = document.createAttribute("set");
        setAttr.setValue("UJA");
        requestElement.setAttributeNode(setAttr);
        
        requestElement.appendChild(document.createTextNode(serviceURL));   
    }
    
    
    public void createListRecordsElement() {
        
       listRecordsElement = document.createElement("ListRecords");
       oaiPmh.appendChild(listRecordsElement);    
    }
    
    
    public void createRecordElement(String setSpec, Node rootElement, NodeList metadataList, String lastModified) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        
        Element recordElement = document.createElement("record");
        listRecordsElement.appendChild(recordElement);
        
        Element headerElement = document.createElement("header");
        recordElement.appendChild(headerElement);
        
        Element idElement = document.createElement("identifier");
        idElement.appendChild(document.createTextNode(UUID.randomUUID().toString()));
        headerElement.appendChild(idElement);
        
        Element dateStampElement = document.createElement("datestamp");
        dateStampElement.appendChild(document.createTextNode(lastModified));
        headerElement.appendChild(dateStampElement);
        
        Element setSpecElement = document.createElement("setSpec");
        setSpecElement.appendChild(document.createTextNode(setSpec));
        headerElement.appendChild(setSpecElement);
        
        Element metadataElement = copyElementsToXML(rootElement);
        recordElement.appendChild(metadataElement); 
    }
    
    
    public void createResumptionToken(String resumptionStr) {
        
        Element resumptionElement = document.createElement("resumptionToken");
        resumptionElement.appendChild(document.createTextNode(resumptionStr));
        listRecordsElement.appendChild(resumptionElement);
        
        
        Attr cursorAttr = document.createAttribute("cursor");
        cursorAttr.setValue("0");
        resumptionElement.setAttributeNode(cursorAttr);   
    }
    
    
    public String exportXMLFile() throws TransformerConfigurationException, TransformerException {
        
        // create the xml file
        //transform the DOM Object to an XML File
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(xmlFilePath));
        
        transformer.transform(domSource, streamResult);
       
        return  xmlFilePath;
    }
    
    
  
    private Element copyElementsToXML(Node rootNode) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
        
        Element metadataElement = document.createElement("metadata");
        
        rootNode.cloneNode(true);
        document.adoptNode(rootNode);
        metadataElement.appendChild(rootNode);

//        for(int i=0; i<nodeList.getLength(); i++) {
//                Node n  = nodeList.item(i).cloneNode(true);
//                 if(n != null && n.getNodeType() == Node.ELEMENT_NODE) {
//                    document.adoptNode(n);
//                    System.out.println("----------->>>N:" + n.getNodeName());
//            
//                    metadataElement.appendChild(n);
////                    document.getElementsByTagName("metadata").item(0).appendChild(n);
//            }
//        }
        return metadataElement;
    }
      
}
