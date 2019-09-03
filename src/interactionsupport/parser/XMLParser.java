package interactionsupport.parser;

import generators.framework.properties.LoadFromXMLVisitor;
import generators.framework.properties.tree.PropertiesTreeNode;
import interactionsupport.models.InteractionModel;
import interactionsupport.models.QuestionGroupModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import algoanim.properties.AnimationProperties;

public class XMLParser implements LanguageParserInterface, ErrorHandler {

  private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

  private static final String W3C_XML_SCHEMA       = "http://www.w3.org/2001/XMLSchema";

  private static final String JAXP_SCHEMA_SOURCE   = "http://java.sun.com/xml/jaxp/properties/schemaSource";

  private static final String XSD_FILE             = "avinteraction/parser/PropertiesTreeModel.xsd";

  public XMLParser() {
    // loadFromXMLFile("D:\\temp\\BubbleSort.ptm", false);
  }

  public void loadFromXMLFile(String uri, boolean isRessourceFile)
      throws IllegalArgumentException {
    if (uri == null || uri.length() == 0)
      return;

    /*
     * test if uri is a valid filename when opening a ressource file, we need to
     * prepend:
     * "de/tudarmstadt/informatik/tk/animalscriptapi/properties/preview.xml"
     */

    InputStream isXML = null;
    if (!isRessourceFile) {
      try {
        isXML = new FileInputStream(uri);
      } catch (FileNotFoundException e) {
        throw new IllegalArgumentException("The File " + uri + "was not found!");
      }
    } else {
      isXML = this.getClass().getResourceAsStream("/" + uri);
    }

    if (isXML == null)
      throw new IllegalArgumentException("Error reading XML-Ressource-File "
          + uri + "!!!");

    // test if XSD_FILE is a valid filename
    InputStream isXSD = this.getClass().getResourceAsStream("/" + XSD_FILE);
    if (isXSD == null)
      throw new IllegalArgumentException("Error reading XSD-File " + XSD_FILE
          + "!");

    // set up factory
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    factory.setValidating(true);
    factory.setIgnoringComments(true);

    try {
      factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
      factory.setAttribute(JAXP_SCHEMA_SOURCE, isXSD);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Error trying to validate the "
          + "XML-File " + uri + ",\nyour Java XML Parser does not support"
          + " JAXP 1.2!");
    }

    LoadFromXMLVisitor v = new LoadFromXMLVisitor();

    try {
      // the final setup
      DocumentBuilder parser = factory.newDocumentBuilder();
      parser.setErrorHandler(this);
      Document doc = parser.parse(isXML);

      // start parsing!
      Node r = null; // root
      r = doc.getDocumentElement(); // or: doc.getFirstChild();

      if (r == null)
        throw new IllegalArgumentException("No Root Element was found!");
      if (!r.getLocalName().equals("PropertiesTreeModel"))
        throw new IllegalArgumentException(
            "Root Element is not 'PropertiesTreeModel'!");

      Node child = null;
      child = r.getFirstChild();

      // skip blank lines in XML
      while (child != null && child.getNodeType() == Node.TEXT_NODE
          && child.getLocalName() == null) {
        child = child.getNextSibling();
        continue;
      }

      if (child == null)
        throw new IllegalArgumentException("Root Folder wasn't found!");
      if (!child.getLocalName().equals("Folder"))
        throw new IllegalArgumentException("Root Folder wasn't found!");

      // parse root folder
      loadXMLFolder(new PropertiesTreeNode("a"), child, v);
      isXML.close();
    } catch (ParserConfigurationException pce) {
      throw new IllegalArgumentException("Parser-Setup: "
          + pce.getLocalizedMessage());
    } catch (IOException ioe) {
      throw new IllegalArgumentException("I/O: " + ioe.getLocalizedMessage());
    } catch (SAXException saxe) {
      throw new IllegalArgumentException(saxe.getLocalizedMessage());
    }
  }

  /**
   * loadXMLFolder loads the given folder into the TreeModel.
   * 
   * @param n
   *          The current TreeNode.
   * @param c
   *          The current XML Node.
   * @param v
   *          The current LoadFromXMLVisitor.
   */
  private void loadXMLFolder(PropertiesTreeNode n, Node c, LoadFromXMLVisitor v) {
    if (n == null || c == null || v == null)
      return;

    Node child = null;
    child = c.getFirstChild();
    while (child != null) {

      // skip blank lines in XML
      if (child.getNodeType() == Node.TEXT_NODE && child.getLocalName() == null) {
        child = child.getNextSibling();
        continue;
      }

      if (child.getLocalName().equals("AnimationProperties")) {
        AnimationProperties ap = v.loadPropsFromXML(child);
        if (ap != null)
          System.err.println(ap.toString());

      } else if (child.getLocalName().equals("Primitive")) {

        PropertiesTreeNode primNode = v.loadPrimitiveFromXML(child);
        if (primNode != null)
          System.err.println(primNode.toString());

      } else if (child.getLocalName().equals("Folder")) {

        // read name
        NamedNodeMap attributes = child.getAttributes();
        if (!(attributes.getLength() == 1 || attributes.item(0).getLocalName()
            .equals("name")))
          throw new IllegalArgumentException("'Folder' "
              + "needs a 'name' attribute!");
        String name = attributes.item(0).getNodeValue();

        PropertiesTreeNode newN = new PropertiesTreeNode(name);
        System.err.println(newN.toString());
        // recursion
        loadXMLFolder(newN, child, v);

      } else
        throw new IllegalArgumentException(
            "Child Element is not 'AnimationProperties' or 'Primitive' or 'Folder'!");

      // next child
      child = child.getNextSibling();
    }
  }

  public Hashtable<String, QuestionGroupModel> getQuestionGroups() {
    // TODO Auto-generated method stub
    return null;
  }

  public Hashtable<String, InteractionModel> parse(String filename) {
    // TODO Auto-generated method stub
    return null;
  }

  public void warning(SAXParseException arg0) {
    // TODO Auto-generated method stub

  }

  public void error(SAXParseException arg0) {
    // TODO Auto-generated method stub

  }

  public void fatalError(SAXParseException arg0) {
    // TODO Auto-generated method stub

  }
}
