package animal.misc;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;

/**
 * temporary class that loads synchronously(although later than the request so
 * that a cursor change can be done).
 */
public class PageLoader extends Component implements Runnable {
  /**
   * 
   */
  private static final long serialVersionUID = 5461026355920719407L;

  /**
   * The cursor position
   */
  private Cursor cursor;

  private JEditorPane editorPane;

  /**
   * The URL used for the displayed page
   */
  private URL url;

  /**
   * The constructor; stores URL an cursor position
   * 
   * @param u
   *          the URL to use
   * @param c
   *          the cursor position
   */
  public PageLoader(URL u, Cursor c, JEditorPane htmlPane) {
    url = u;
    cursor = c;
    editorPane = htmlPane;
  }

  /**
   * Load the document and set / update the cursor
   */
  public void run() {
    if (url == null) {
      // restore the original cursor
      editorPane.setCursor(cursor);

      // PENDING(prinz) remove this hack when
      // automatic validation is activated.
      Container parent = editorPane.getParent();
      parent.repaint();
    } else {
      Document doc = editorPane.getDocument();

      try {
        editorPane.setPage(url);
      } catch (IOException ioe) {
        editorPane.setDocument(doc);
        getToolkit().beep();
      } finally { // schedule the cursor to revert after

        // the paint has happended.
        url = null;
        SwingUtilities.invokeLater(this);
      }
    }
  }
}
