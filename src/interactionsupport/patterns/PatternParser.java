/*
 * Created on 19.09.2008 by Bjoern Dasbach <dasbach@rbg.informatik.tu-darmstadt.de>
 */
package interactionsupport.patterns;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PatternParser {
	private Validator validator;
	private Schema schemaXSD;
	private DocumentBuilder parser;
	private SchemaFactory schemaFactory;
	private String filename;
	private Document document;
	private NodeList interactions = null;
	private Element root;
	
	public PatternParser(String schema, String file) {
		try {
			filename = file;
			
			// build an XSD-aware SchemaFactory
			schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			// get the custom xsd schema describing the required format for my XML files.
			schemaXSD = schemaFactory.newSchema(new File (schema));
	
			// Create a Validator capable of validating XML files according to my custom schema.
			validator = schemaXSD.newValidator();
			
			// Get a parser capable of parsing vanilla XML into a DOM tree
			parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			
			parse();
		} catch (SAXException saxe) {
			System.err.println(saxe.toString());
		} catch (ParserConfigurationException pce) {
			System.err.println(pce.toString());
		}		
	}
	
	public void parse() {
		try {
			// parse the XML purely as XML and get a DOM tree representation.
			document = parser.parse(new File(filename));

			// parse the XML DOM tree against the stricter XSD schema
			validator.validate(new DOMSource(document));
			
			root = document.getDocumentElement();
			interactions = root.getChildNodes();
		} catch (SAXException saxe) {
			System.err.println(saxe.toString());
			System.exit(-1);
		} catch (IOException ioe) {
			System.err.println(ioe.toString());
			System.exit(-2);
		}
	}
	
	public Element getRoot() {
		return root;
	}
	
	public Element getInteraction(String pattern) {
		Node temp;
		
		for(int i = 0; i < interactions.getLength(); i++) {
			temp = interactions.item(i);
			
			if(temp.hasAttributes() && temp.getAttributes().getNamedItem("uid").getNodeValue().equalsIgnoreCase(pattern)) {
				return (Element)temp;
			}
		}
		
		System.err.println("No Interaction Pattern \"" + pattern + "\" was found.");
		return null;
	}
	
	public Vector<String> getUids() {
		Node temp;
		Vector<String> tmp = new Vector<String>();
		
		for(int i = 0; i < interactions.getLength(); i++) {
			temp = interactions.item(i);
			
			if(temp.hasAttributes()) {
				tmp.add(temp.getAttributes().getNamedItem("uid").getNodeValue());	
			}
		}
		
		return tmp;
	}
	
	public boolean isUidExisting(String uid) {
		Node temp;

		for(int i = 0; i < interactions.getLength(); i++) {
			temp = interactions.item(i);
			
			if(temp.hasAttributes() && temp.getAttributes().getNamedItem("uid").getNodeValue().equalsIgnoreCase(uid)) {
				return true;
			}
		}		
		return false;
	}
	
	public String getType(String uid) {
		Node temp;
		
		for(int i = 0; i < interactions.getLength(); i++) {
			temp = interactions.item(i);
			
			if(temp.hasAttributes() && temp.getAttributes().getNamedItem("uid").getNodeValue().equalsIgnoreCase(uid)) {
				return temp.getNodeName();
			}
		}
		
		return null;
	}
}
