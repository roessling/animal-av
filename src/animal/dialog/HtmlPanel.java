package animal.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;

import translator.AnimalTranslator;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.misc.MessageDisplay;

/**
 * This class can display HTML text.
 *
 * @version 1.7 02/02/98
 * @author Jeff Dinkins
 * @author Tim Prinzing
 * @author Peter Korn (accessibility support)
 */
public class HtmlPanel extends JPanel implements HyperlinkListener {
  // =================================================================
  //                               ATTRIBUTES
  // =================================================================

  /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -2455829185592463221L;
	
	/**
	 * The underlying JEditorPane
	 */
	public JEditorPane html;

  // =================================================================
  //                               CONSTRUCTORS
  // =================================================================

  /**
   * Generate the HTMLPanel on the given URL
   */
  public HtmlPanel() {
  	init();
  }
  
  
  protected void init() {
  	setBackground(Color.WHITE);
    setBorder(new EmptyBorder(10, 10, 10, 10));
    setLayout(new BorderLayout());
    getAccessibleContext().setAccessibleName(AnimalTranslator.translateMessage(
        "htmlPanelName", null));
    getAccessibleContext().setAccessibleDescription(AnimalTranslator.translateMessage(
        "htmlPanelDescr", null));
  }
  
  public void setURL(String urlString) {
    try {
      URL url = new URL(urlString);
      html = new JEditorPane(url);
    }
    catch (MalformedURLException e) {
    	MessageDisplay.errorMsg(AnimalTranslator.translateMessage("malformedURL",
          new Object[] { urlString }), MessageDisplay.RUN_ERROR);
    		manuallyGenerate(urlString);
    }
    catch (IOException e) {
    	MessageDisplay.errorMsg(AnimalTranslator.translateMessage("ioException",
          new Object[] { urlString }), MessageDisplay.RUN_ERROR);
    	if (html == null) {
    		manuallyGenerate(urlString);
    	}
    }
    finishDisplay();
  }
  
  public void setContentString(String contentString) {
  	html = new JEditorPane("text/html", contentString);
    finishDisplay();
  }
  
  protected void finishDisplay() {
    html.setEditable(false);
    html.addHyperlinkListener(this);

    JScrollPane scroller = new JScrollPane(html);
    scroller.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));

    add(scroller, BorderLayout.CENTER);
    html.setVisible(true);
    setVisible(true);
  }

  public void manuallyGenerate(String urlString) {
  	if (urlString == null)
  		return;
		String tutorialID = 
			AnimalConfiguration.getDefaultConfiguration().getProperty("tutorial.path", 
					AnimalTranslator.translateMessage("tutorialPath", null));
 		String localeString = Animal.getAnimalConfiguration().getProperty("animal.Locale",
 		  "en_US");
  	if (urlString.equalsIgnoreCase(tutorialID))  {
   		URL url = this.getClass().getResource("/AnimalTutorial_" +localeString +".html");
   		System.err.println("Retrieving local copy at " +url.toString());
  		try {
  			html = new JEditorPane(url);
  			finishDisplay();
  		} catch(IOException e) {
  			// do nothing
  		}
  	} else {
  		URL url = getClass().getResource("/animalScript_" + localeString +".html");
   		System.err.println("Retrieving local copy at " +url.toString());
  		try {
  			html = new JEditorPane(url);
  			finishDisplay();
  		} catch(IOException e) {
    		url = getClass().getResource("/animalScript.html");
     		try {
    			html = new JEditorPane(url);
     		} catch(IOException e2) {
     			MessageDisplay.errorMsg("URL open failed for '" +urlString +"'", 
     					MessageDisplay.RUN_ERROR);
     		}
  		}
  	}
  }
  
  
  /**
   * Notification of a change relative to a hyperlink.
   *
   * @param e the Hyperlink event
   */
  public void hyperlinkUpdate(HyperlinkEvent e) {
    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
      linkActivated(e.getURL());
    }
  }

  /**
    * Follows the reference in an
    * link.  The given url is the requested reference.
    * By default this calls <a href="#setPage">setPage</a>,
    * and if an exception is thrown the original previous
    * document is restored and a beep sounded.  If an
    * attempt was made to follow a link, but it represented
    * a malformed url, this method will be called with a
    * null argument.
    *
    * @param u the URL to follow
    */
  protected void linkActivated(URL u) {
    Cursor c = html.getCursor();
    Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    html.setCursor(waitCursor);
    SwingUtilities.invokeLater(new PageLoader(u, c));
  }

  /**
    * temporary class that loads synchronously(although
    * later than the request so that a cursor change
    * can be done).
    */
  class PageLoader implements Runnable {
    /**
     * The cursor position
     */
    Cursor cursor;

    /**
     * The URL used for the displayed page
     */
    URL url;

    /**
     * The constructor; stores URL an cursor position
     *
     * @param u the URL to use
     * @param c the cursor position
     */
    PageLoader(URL u, Cursor c) {
      url = u;
      cursor = c;
    }

    /**
     * Load the document and set / update the cursor
     */
    public void run() {
      if (url == null) {
        // restore the original cursor
        html.setCursor(cursor);

        // PENDING(prinz) remove this hack when
        // automatic validation is activated.
        Container parent = html.getParent();
        parent.repaint();
      }
      else {
        Document doc = html.getDocument();

        try {
          html.setPage(url);
        }
        catch (IOException ioe) {
          html.setDocument(doc);
          getToolkit().beep();
        }
        finally { // schedule the cursor to revert after

          // the paint has happended.
          url = null;
          SwingUtilities.invokeLater(this);
        }
      }
    }
  }
}
