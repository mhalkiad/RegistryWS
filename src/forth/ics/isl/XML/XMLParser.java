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
import org.w3c.dom.NamedNodeMap;
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
    
    
       
    public NodeList parseWholeXML(Node startingNode) throws ParserConfigurationException, SAXException, IOException {
      
        NodeList childNodes = startingNode.getChildNodes();
        for(int i=0; i<childNodes.getLength(); i++)
        {
            Node childNode = childNodes.item(i);
            if(childNode.getNodeType() == Node.ELEMENT_NODE) {
                
                if (childNode.hasAttributes()) {
               // get attributes names and values
               NamedNodeMap nodeMap = childNode.getAttributes();
               for (int j = 0; j < nodeMap.getLength(); j++)
               {
                   Node tempNode = nodeMap.item(j);
                   System.out.println("Attr name : " + tempNode.getNodeName()+ "; Value = " + tempNode.getNodeValue());
               }

             }
            }    
            else
            {
            // trim() is used to ignore new lines and spaces elements.
                if(!childNode.getTextContent().trim().isEmpty())
                {
                    System.out.println(childNode.toString());
                    System.out.println(childNode.getTextContent());
                }
            }
            parseWholeXML(childNode);
        }
        return childNodes;   
    }

}
