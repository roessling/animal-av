package interactionsupport.models;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Model representing a link to an external documentation.
 * 
 * @author Guido Roessling <roessling@acm.org> / Simon Sprankel
 *         <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public class HtmlDocumentationModel extends InteractionModel {

  /** URL of the documentation the user should see */
  private URI targetURI = null;

  public HtmlDocumentationModel(String elementID) {
    super(elementID);
  }

  public HtmlDocumentationModel(String elementID, String url) {
    super(elementID);
    setLinkAddress(url);
  }

  public HtmlDocumentationModel(String elementID, URI uri) {
    super(elementID);
    setLinkAddress(uri);
  }

  /**
   * Retrieves the documentation link URI for this element.
   * 
   * @return the target URI
   */
  public URI getTargetURI() {
    return targetURI;
  }

  /**
   * Assigns the target URI for the documentation link as a String.
   * 
   * @param targetString
   *          the targetURI as a String
   */
  public void setLinkAddress(String targetString) {
    try {
      targetURI = new URI(targetString);
    } catch (URISyntaxException uriSyntaxExc) {
      // nothing to be done here
    }
  }

  /**
   * Assigns the target URI for the documentation link.
   * 
   * @param newTargetURI
   *          the target URI
   */
  public void setLinkAddress(URI newTargetURI) {
    targetURI = newTargetURI;
  }

}