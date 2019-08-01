package forth.ics.isl.service;


import forth.ics.isl.XML.XMLBuilder;
import forth.ics.isl.XML.XMLParser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
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
        XMLBuilder xmlBuilder = new XMLBuilder(System.getProperty("user.dir") + File.separator +"oaiPmh.xml");
        System.out.println("OAI-PMH filepath:" + System.getProperty("user.dir") + File.separator +"oaiPmh.xml");
        
        xmlBuilder.createOaipmhElement("http://www.openarchives.org/OAI/2.0/",
                                       "http://www.w3.org/2001/XMLSchema-instance",
                                       "http://www.openarchives.org/OAI/2.0/\n" +
                                       "http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd");
        
        xmlBuilder.createResponseDateElement();
        xmlBuilder.createRequestElement("http://more.locloud.eu:8080/carare/");
        xmlBuilder.createListRecordsElement();

        // copy each xml file in xmlFolder into OAI-PMH file
        ArrayList<String> results = (ArrayList<String>) getFileNames(xmlFolder);
        
        for(int i=0; i<results.size(); i++) {
            
            String fileName = results.get(i);
            System.out.println("FileName: " + fileName);
            
            XMLParser xmlParser = new XMLParser(xmlFolder + File.separator + fileName);
            NodeList metadataList = xmlParser.parseWholeXML(xmlParser.getRootElement());
            
            xmlBuilder.createRecordElement(xmlParser.getTypeElement(), xmlParser.getRootElement(), metadataList);        
        }
        
        xmlBuilder.createResumptionToken("X599391871/1");
        String initialOAI = xmlBuilder.exportXMLFile();
        
        // the service returns the created oai-pmh file 
        String filepath = System.getProperty("user.dir") + File.separator +"oaiPmh.xml";
        File file = new File(filepath);
        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition","attachment; filename=" + filepath);
        return response.build();
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
