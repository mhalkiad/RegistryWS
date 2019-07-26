/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forth.ics.isl.XML;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author mhalkiad
 */
public class XMLParser {
    
    private File xmlFile;
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private Document doc;
        
        
    public XMLParser(String xmlPath) throws ParserConfigurationException, SAXException, IOException {
        
        this.xmlFile = new File(xmlPath);
        
        dbFactory = DocumentBuilderFactory.newInstance();
	dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(xmlFile);
        
        //normalization
        doc.getDocumentElement().normalize();
    }
    
    
    public String getTypeElement() {
        
        return doc.getElementsByTagName("type").item(0).getTextContent();
    }
    
    
    public Node getRootElement() {
        
        return doc.getDocumentElement();
    }
    
    
    public void copyXMLFile(String sourceFile, String destFile) throws ParserConfigurationException, SAXException, IOException {
        
        DocumentBuilderFactory dbFactoryDest = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilderDest = dbFactoryDest.newDocumentBuilder();
        Document docDest = dBuilderDest.parse(new File(destFile));

        //normalization
        docDest.getDocumentElement().normalize();
        
        Node startingNode = getRootElement();
        System.out.println("StartingNode:" + startingNode.toString());
        
        NodeList childNodes = startingNode.getChildNodes();
        for(int i=0; i<childNodes.getLength(); i++)
        {
            System.out.println("Node: " + childNodes.item(i));
            Node newNode = docDest.importNode(childNodes.item(i), true);
            //docDest.adoptNode(newNode);
            docDest.getElementsByTagName(destFile).item(0).appendChild(newNode);
        }
        
    }
    
    
    
    public String parseWholeXML(Node startingNode) throws ParserConfigurationException, SAXException, IOException {
      
        //String xmlStr = new String();
        
        DocumentBuilderFactory dbFactoryDest = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilderDest = dbFactoryDest.newDocumentBuilder();
        Document docDest = dBuilderDest.parse(new File("/home/mhalkiad/Desktop/oaiPmh.xml"));
        
        NodeList childNodes = startingNode.getChildNodes();
        for(int i=0; i<childNodes.getLength(); i++)
        {
            Node childNode = childNodes.item(i);
            if(childNode.getNodeType() == Node.ELEMENT_NODE) {
                parseWholeXML(childNode);
            }
            else
            {
            // trim() is used to ignore new lines and spaces elements.
                if(!childNode.getTextContent().trim().isEmpty())
                {
                    System.out.println(childNode.toString());
                    System.out.println(childNode.getTextContent());
                    
                    docDest.getElementsByTagName("metadata").item(0).appendChild(childNode);
                }
            }
        }
        return "OK";   
    }
    
    
}
