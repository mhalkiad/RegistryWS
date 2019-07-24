package forth.ics.isl.service;


import forth.ics.isl.XML.XMLBuilder;
import forth.ics.isl.XML.XMLParser;
import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


/**
 *
 * @author mhalkiad
 */

@Path("/registry")
public class RegistryWS {
	
    @GET
    public Response helloWorld() throws ParserConfigurationException, TransformerConfigurationException, TransformerException, SAXException, IOException {
        
//        XMLBuilder xmlBuilder = new XMLBuilder("/home/mhalkiad/Desktop/oaiPmh.xml");
//        xmlBuilder.createOaipmhElement("xmlns", "xsi", "schemaLocation");
//        xmlBuilder.createResponseDateElement();
//        xmlBuilder.createRequestElement("serviceURL");
//        xmlBuilder.createListRecordsElement();
//        xmlBuilder.createRecordElement("setSpec", "metadata");
//        xmlBuilder.createResumptionToken("resumptionStr");
//
//        String output = xmlBuilder.exportXMLFile();
        
        XMLParser xmlParser = new XMLParser("/home/mhalkiad/service.xml");
        //String output = xmlParser.getTypeElement();
        Node rootElement = xmlParser.getRootElement();
       //System.out.println(rootElement.getTextContent());
        String output = xmlParser.parseWholeXML(rootElement);
       
        System.out.println(output);
       
        return Response.status(200).entity("ok").build();
    }
	
}
