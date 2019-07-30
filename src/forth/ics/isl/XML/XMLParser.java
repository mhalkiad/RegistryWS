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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
    
    
    
    public NodeList parseWholeXML(Node startingNode) throws ParserConfigurationException, SAXException, IOException {
      
        //String xmlStr = new String();
        
//        DocumentBuilderFactory dbFactoryDest = DocumentBuilderFactory.newInstance();
//	DocumentBuilder dBuilderDest = dbFactoryDest.newDocumentBuilder();
//        Document docDest = dBuilderDest.parse(new File("/home/mhalkiad/Desktop/oaiPmh.xml"));
        
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

               // parseWholeXML(childNode);
             }
            }    
            else
            {
            // trim() is used to ignore new lines and spaces elements.
                if(!childNode.getTextContent().trim().isEmpty())
                {
                    System.out.println(childNode.toString());
                    System.out.println(childNode.getTextContent());
                    
                  //  docDest.getElementsByTagName("metadata").item(0).appendChild(childNode);
                }
            }
            parseWholeXML(childNode);
        }
        return childNodes;   
    }
    
    
    public void copyElementsToXML(String strSource,NodeList nodeList) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
        
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document xmlDocument1 = documentBuilder.parse(strSource);
       
        //Node metadataNode = xmlDocument1.getElementsByTagName("metadata").item(0).appendChild(doc)
        //System.out.println("Metadata element: " + metadataElement.getNodeValue());
        
        for(int i=0; i<nodeList.getLength(); i++) {
                Node n  = nodeList.item(i).cloneNode(true);
                 if(n != null && n.getNodeType() == Node.ELEMENT_NODE) {
                    xmlDocument1.adoptNode(n);
                    System.out.println("----------->>>N:" + n.getNodeName());
                    xmlDocument1.getElementsByTagName("metadata").item(0).appendChild(n);
                    //xmlDocument1.getDocumentElement().appendChild(n);
            }
        }
        
        // create the xml file
        //transform the DOM Object to an XML File
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
        DOMSource domSource = new DOMSource(xmlDocument1);
        StreamResult streamResult = new StreamResult(new File(strSource));
        transformer.transform(domSource, streamResult);
    }

}
