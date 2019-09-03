/*
 * Created on 24.07.2005 by T. Ackermann
 */
package generators.framework.properties.tree;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import algoanim.primitives.Graph;
import algoanim.properties.AnimationProperties;
import generators.framework.PropertiesGUI;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.framework.properties.GetAsXMLVisitor;
import generators.framework.properties.LoadFromXMLVisitor;

/**
 * This stores the tree model
 * 
 * @author T. Ackermann
 */
public class PropertiesTreeModel extends DefaultTreeModel
    implements ErrorHandler {

  private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

  private static final String W3C_XML_SCHEMA       = "http://www.w3.org/2001/XMLSchema";

  private static final String JAXP_SCHEMA_SOURCE   = "http://java.sun.com/xml/jaxp/properties/schemaSource";

  private static final String XSD_FILE             = "generators/framework/properties/tree/PropertiesTreeModel.xsd";

  /** we store this because PropertiesTreeModel is serializable */
  private static final long   serialVersionUID     = 3257283630224126006L;

  /** show items? */
  boolean                     showItems            = true;

  /** show hidden items? */
  boolean                     showHiddenItems      = true;

  /**
   * Constructor creates a new PropertiesTreeModel-Object.
   */
  public PropertiesTreeModel() {
    super(new PropertiesTreeNode("Root"));
  }

  /**
   * getPropertiesContainer returns an AnimationPropertiesContainer with all the
   * AnimationProperties that are in the tree. The Container can then be passed
   * to a Generator Implementation.
   * 
   * @return An AnimationPropertiesContainer.
   */
  public AnimationPropertiesContainer getPropertiesContainer() {
    AnimationPropertiesContainer apc = new AnimationPropertiesContainer(
        Math.min(23, getElementsCount()));
    Enumeration<TreeNode> e = ((PropertiesTreeNode) getRoot())
        .depthFirstEnumeration();

    while (e.hasMoreElements()) {
      TreePath t = new TreePath(
          ((PropertiesTreeNode) e.nextElement()).getPath());
      PropertiesTreeNode n = (PropertiesTreeNode) t.getLastPathComponent();
      if (!n.isProperty())
        continue;
      apc.add(n.getAnimationProperties());
    }
    apc.updateIndex();
    return apc;
  }

  /**
   * getPrimitivesContainer returns a Hashtable with all the Primitives that are
   * in the tree. The Hashtable can then be passed to a Generator
   * Implementation.
   * 
   * @return A Hashtable with all Primitives.
   */
  public Hashtable<String, Object> getPrimitivesContainer() {
    Hashtable<String, Object> h = new Hashtable<String, Object>(
        Math.min(23, getElementsCount()));
    Graph graph = null;
    Enumeration<TreeNode> e = ((PropertiesTreeNode) getRoot())
        .depthFirstEnumeration();
    while (e.hasMoreElements()) {
      TreePath t = new TreePath(
          ((PropertiesTreeNode) e.nextElement()).getPath());
      PropertiesTreeNode n = (PropertiesTreeNode) t.getLastPathComponent();
      if (!n.isPrimitive())
        continue;
      if (n.getName().contains("graph")) {
        graph = PropertiesGUI.getGraphFromScriptFile();

        if (graph != null) {
          h.put(n.getName(), graph);
        } else
          h.put(n.getName(), "%graphscript\n\ngraph 0");

      } else
        h.put(n.getName(), n.getValue());
    }
    return h;
  }

  /**
   * getAsXML returns the TreeModel with all the AnimationProperties in it as an
   * XML String, that can be read by loadFromXMLFile.
   * 
   * @return Returns the TreeModel as an XML String.
   */
  public String getAsXML() {
    StringBuilder ret = new StringBuilder(8192);
    GetAsXMLVisitor v = new GetAsXMLVisitor();

    ret.append(
        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<PropertiesTreeModel xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"PropertiesTreeModel.xsd\">\n");

    ret.append(getRecurseXML((PropertiesTreeNode) getRoot(), v, 1));

    ret.append("</PropertiesTreeModel>");
    return ret.toString();
  }

  /**
   * getRecurseXML returns the XML-String for the given Node.
   * 
   * @param n
   *          The current Node.
   * @param v
   *          The GetAsXMLVisitor
   * @param l
   *          The current Level
   * @return The XML-String for the given Node.
   */
  private String getRecurseXML(PropertiesTreeNode n, GetAsXMLVisitor v, int l) {
    if (n == null || n.isItem() || v == null)
      return "";
    int level = l;
    if (level < 0)
      level = 0;

    if (n.isProperty()) {
      return v.getPropertiesAsXML(n.getAnimationProperties(), level);
    } else if (n.isPrimitive()) {
      if (n.getDescription() != null && !n.getDescription().equals("")) {
        return v.getPrimitiveAsXML(n.getName(), n.getValue(),
            n.getDescription(), level);
      } else {
        return v.getPrimitiveAsXML(n.getName(), n.getValue(), level);
      }
    }

    StringBuilder ret = new StringBuilder(8192);
    for (int i = 0; i < level; i++)
      ret.append("\t");
    ret.append("<Folder name=\"");
    ret.append(GetAsXMLVisitor.replaceEntitiesForXML(n.getLabel()));
    ret.append("\">\n");
    int count = n.getChildCount();
    int newLevel = level + 1;
    for (int i = 0; i < count; i++) {
      ret.append(
          getRecurseXML((PropertiesTreeNode) n.getChildAt(i), v, newLevel));
    }
    for (int i = 0; i < level; i++)
      ret.append("\t");
    ret.append("</Folder>\n");
    return ret.toString();
  }

  public static String cleanString(String filename, String extension) {
    StringBuffer cleanName = new StringBuffer(filename.length());
    int i = 0, start = 0, endPos = filename.length() - extension.length() - 1;
    while (start < filename.length()
        && (i = filename.indexOf('.', start)) > -1) {
      cleanName.append(filename.substring(start, i));
      if (i < endPos)
        cleanName.append('/');
      start = i + 1;
    }
    cleanName.append('.').append(extension);
    return cleanName.toString();
  }

  /**
   * loadFromXMLFile clears the container and puts all the AnimationProperties
   * in it that are described by the given XML-File.
   * 
   * @param uri
   *          The Name of the XML-File.
   * @param isRessourceFile
   *          If True, then we are looking for a Ressource-File that lies in our
   *          package.
   * @throws IllegalArgumentException
   *           if something goes wrong.
   */
  public void loadFromXMLFile(String uri, boolean isRessourceFile)
      throws IllegalArgumentException {

    this.clear();
    if (uri == null || uri.length() == 0)
      return;

    /*
     * test if uri is a valid filename when opening a ressource file, we need to
     * prepend:
     * "de/tudarmstadt/informatik/tk/animalscriptapi/properties/preview.xml"
     */
    InputStream isXML = null;
    String fname = cleanString(uri, "xml");// uri.replace('.', '/');
    if (!isRessourceFile) {
      try {
        isXML = new FileInputStream(fname);
      } catch (FileNotFoundException e) {
        throw new IllegalArgumentException(
            "The File " + fname + "was not found!");
      }
    } else {
      isXML = this.getClass().getResourceAsStream("/" + fname);
    }

    if (isXML == null)
      throw new IllegalArgumentException(
          "Error reading XML-Ressource-File " + fname + "!");

    // test if XSD_FILE is a valid filename
    InputStream isXSD = this.getClass().getResourceAsStream("/" + XSD_FILE);
    if (isXSD == null)
      throw new IllegalArgumentException(
          "Error reading XSD-File " + XSD_FILE + "!");

    // set up factory
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    factory.setValidating(true);
    factory.setIgnoringComments(true);

    try {
      factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
      factory.setAttribute(JAXP_SCHEMA_SOURCE, isXSD);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "Error trying to validate the " + "XML-File " + uri
              + ",\nyour Java XML Parser does not support" + " JAXP 1.2!");
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
      loadXMLFolder((PropertiesTreeNode) getRoot(), child, v);

    } catch (ParserConfigurationException pce) {
      clear();
      throw new IllegalArgumentException(
          "Parser-Setup: " + pce.getLocalizedMessage());
    } catch (IOException ioe) {
      clear();
      throw new IllegalArgumentException("I/O: " + ioe.getLocalizedMessage());
    } catch (SAXException saxe) {
      clear();
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
  private void loadXMLFolder(PropertiesTreeNode n, Node c,
      LoadFromXMLVisitor v) {
    if (n == null || c == null || v == null)
      return;

    Node child = null;
    child = c.getFirstChild();
    while (child != null) {

      // skip blank lines in XML
      if (child.getNodeType() == Node.TEXT_NODE
          && child.getLocalName() == null) {
        child = child.getNextSibling();
        continue;
      }

      if ("AnimationProperties".equals(child.getLocalName())) {

        AnimationProperties ap = v.loadPropsFromXML(child);
        if (ap != null)
          insertNodeInto(getNewNode(ap), n, n.getChildCount());

      } else if ("Primitive".equals(child.getLocalName())) {

        PropertiesTreeNode primNode = v.loadPrimitiveFromXML(child);
        if (primNode != null)
          insertNodeInto(primNode, n, n.getChildCount());

      } else if ("Folder".equals(child.getLocalName())) {

        // read name
        NamedNodeMap attributes = child.getAttributes();
        if (!(attributes.getLength() == 1
            || attributes.item(0).getLocalName().equals("name")))
          throw new IllegalArgumentException(
              "'Folder' " + "needs a 'name' attribute!");
        String name = attributes.item(0).getNodeValue();

        PropertiesTreeNode newN = new PropertiesTreeNode(name);
        insertNodeInto(newN, n, n.getChildCount());

        // recursion
        loadXMLFolder(newN, child, v);

      } else
        throw new IllegalArgumentException(
            "Child Element is not 'AnimationProperties' or 'Primitive' or 'Folder'!");

      // next child
      child = child.getNextSibling();
    }
  }

  /**
   * clear removes all elements from the tree and reinitializes it.
   */
  public void clear() {
    PropertiesTreeNode r = (PropertiesTreeNode) getRoot();
    for (int i = r.getChildCount() - 1; i >= 0; i--)
      removeNodeFromParent((PropertiesTreeNode) r.getChildAt(i));
  }

  /**
   * getNewNode returns a TreeNode with the right show(Hidden)Items values.
   * 
   * @param p
   *          The AnimationProperties object for the Node.
   * @return A TreeNode with the right show(Hidden)Items values.
   */
  public PropertiesTreeNode getNewNode(AnimationProperties p) {
    if (p == null)
      return null;
    PropertiesTreeNode n = new PropertiesTreeNode(p);
    n.setShowHiddenItems(this.showHiddenItems);
    n.setShowItems(this.showItems);
    return n;
  }

  /**
   * update should be called after show(Hidden)Items has been changed.
   */
  public void update() {
    Enumeration<TreeNode> e = ((PropertiesTreeNode) getRoot())
        .depthFirstEnumeration();
    PropertiesTreeNode n;
    PropertiesTreeNode parent;
    int index;

    while (e.hasMoreElements()) {
      n = (PropertiesTreeNode) e.nextElement();
      if (n.isItem())
        continue;
      if (n.isFolder()) {
        nodeChanged(n);
        continue;
      }
      parent = (PropertiesTreeNode) n.getParent();
      index = parent.getIndex(n);
      removeNodeFromParent(n);

      if (n.isPrimitive()) {
        if (n.getDescription() == null || n.getDescription().equals("")) {
          insertNodeInto(new PropertiesTreeNode(n.getName(), n.getValue()),
              parent, index);
        } else {
          insertNodeInto(new PropertiesTreeNode(n.getName(), n.getValue(),
              n.getDescription()), parent, index);
        }
      } else {
        insertNodeInto(getNewNode(n.getAnimationProperties()), parent, index);
      }
    }
  }

  /**
   * getElementsCount returns the count of AnimationProperties and Primitives.
   * 
   * @return The count of AnimationProperties and Primitives.
   */
  public int getElementsCount() {
    Enumeration<TreeNode> e = ((PropertiesTreeNode) getRoot())
        .depthFirstEnumeration();
    PropertiesTreeNode n;
    int count = 0;

    while (e.hasMoreElements()) {
      n = (PropertiesTreeNode) e.nextElement();
      if (n.isProperty() || n.isPrimitive())
        count++;
    }
    return count;
  }

  /**
   * getShowItems returns true, if currently items are displayed.
   * 
   * @return true, if currently items are displayed.
   */
  public boolean getShowItems() {
    return this.showItems;
  }

  /**
   * setShowItems if b is true, then items are displayed.
   * 
   * @param b
   *          if true, then items are displayed.
   */
  public void setShowItems(boolean b) {
    this.showItems = b;
    update();
  }

  /**
   * getShowHiddenItems returns true, if currently hidden items are displayed.
   * 
   * @return true, if currently hidden items are displayed.
   */
  public boolean getShowHiddenItems() {
    return this.showHiddenItems;
  }

  /**
   * setShowHiddenItems if b is true, then hidden items are displayed.
   * 
   * @param b
   *          if true, then hidden items are displayed.
   */
  public void setShowHiddenItems(boolean b) {
    this.showHiddenItems = b;
    update();
  }

  // ****************************************************************************
  // *** BELOW ARE THE SAX EXCEPTION HANDLERS
  // ****************************************************************************

  /**
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
   */
  public void warning(SAXParseException exception) throws SAXException {
    throw exception;
  }

  /**
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
   */
  public void error(SAXParseException exception) throws SAXException {
    throw exception;
  }

  /**
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
   */
  public void fatalError(SAXParseException exception) throws SAXException {
    throw exception;
  }
}
