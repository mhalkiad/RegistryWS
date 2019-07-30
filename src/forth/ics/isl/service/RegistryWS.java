package forth.ics.isl.service;


import forth.ics.isl.XML.XMLBuilder;
import forth.ics.isl.XML.XMLParser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 *
 * @author mhalkiad
 */

@Path("/registry")
public class RegistryWS {
	
    @GET
    public Response registry(@QueryParam("xml-folder") String xmlFolder) throws ParserConfigurationException, TransformerConfigurationException, TransformerException, SAXException, IOException {
        
        // create initial OAI_PMH xml file
        XMLBuilder xmlBuilder = new XMLBuilder("/home/mhalkiad/Desktop/oaiPmh.xml");
        xmlBuilder.createOaipmhElement("http://www.openarchives.org/OAI/2.0/",
                                       "http://www.w3.org/2001/XMLSchema-instance",
                                       "http://www.openarchives.org/OAI/2.0/\n" +
                                       "http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd");
        
        xmlBuilder.createResponseDateElement();
        xmlBuilder.createRequestElement("http://more.locloud.eu:8080/carare/");
        xmlBuilder.createListRecordsElement();
        xmlBuilder.createRecordElement("UJA", "metadata");
        xmlBuilder.createResumptionToken("X599391871/1");

        String initialOAI = xmlBuilder.exportXMLFile();
        
        // copy each xml file in xmlFolder into OAI-PMH file
        ArrayList<String> results = (ArrayList<String>) getFileNames(xmlFolder);
        
        for(int i=0; i<results.size(); i++) {
            
            String fileName = results.get(i);
            System.out.println("FileName: " + fileName);
            
            XMLParser xmlParser = new XMLParser(xmlFolder + fileName);
            NodeList nodeList = xmlParser.parseWholeXML(xmlParser.getRootElement());
            xmlParser.copyElementsToXML(initialOAI, nodeList);
        }
        
        
        //XMLParser xmlParser = new XMLParser("/home/mhalkiad/Desktop/xmlFiles/service.xml");
        //String output = xmlParser.getTypeElement();
       // Node rootElement = xmlParser.getRootElement();
       //System.out.println(rootElement.getTextContent());
       // String output = xmlParser.parseWholeXML(rootElement);
       
        //System.out.println(output);
        
        //xmlParser.copyXMLFile("/home/mhalkiad/Desktop/xmlFiles/service.xml", "/home/mhalkiad/Desktop/oaiPmh.xml");
        //NodeList nodeList = xmlParser.parseWholeXML(xmlParser.getRootElement());
        
//         for(int i=0; i<nodeList.getLength(); i++)
//         {
//             System.out.println("Node: "+ i +" value: " + nodeList.item(i).getTextContent());
//         }
         
         //xmlParser.copyElementsToXML("/home/mhalkiad/Desktop/oaiPmh.xml", nodeList);
        
//        ArrayList<String> results = (ArrayList<String>) getFileNames(xmlFolder);
//        
//        for(int i=0; i<results.size(); i++)
//            System.out.println(results.get(i));
       
        return Response.status(200).entity("ok").build();
    }
    
    
    
    private List<String> getFileNames(String xmlFolder) {
    
        List<String> results = new ArrayList<String>();
        
        File[] files = new File(xmlFolder).listFiles();
        
        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }
        return results;
    }
}
