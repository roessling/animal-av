package gfgaa.gui;

import gfgaa.gui.components.SPanel;
import gfgaa.gui.others.PanelManager;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/** Panel class<br>
  * Panel to display the parser messages.
  *
  * @author S. Kulessa
  * @version 0.97
  */
public final class ParserPanel extends SPanel {

    /**
   * 
   */
  private static final long serialVersionUID = 5001891154257512791L;

    /** Reference to projects mainclass. */
    private GraphAlgController mainclass;

    /** EditorPane to display the parser messages. */
    private JEditorPane outpars;

    /** ScrollPane to make the EditorPane scrollable. */
    JScrollPane scroll;

    /** (constructor)<br>
      * Creates the parser panel object.
      *
      * @param mainclass    Reference to projects mainclass
      */
    public ParserPanel(final GraphAlgController mainclass) {
        setLayout(new BorderLayout());

        this.mainclass = mainclass;
        this.mainclass.addPanel(PanelManager.PANEL_PARSER, this);

        outpars = new JEditorPane();
        outpars.setEditable(false);

        scroll = new JScrollPane(outpars);
        scroll.setVerticalScrollBarPolicy(
                                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroll);
    }

    /** (internal display method)<br>
      * Displays the given parser error messages.
      *
      * @param eData        ArrayList containing the error messages
      */
    protected void displayMessage(final ArrayList<String> eData) {
        String sData = "";
        int anz = eData.size();
        for (int i = 0; i < anz; i++) {
            sData += eData.get(i);
        }
        outpars.setText(sData);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                scroll.getVerticalScrollBar().setValue(0);
            }
        });
    }

    /** (internal display method)<br>
      * Display the given message.
      *
      * @param sData        Message
      */
    protected void displayMessage(final String sData) {
        outpars.setText(sData);

        // Scrollt das Editor Panel nach oben
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                scroll.getVerticalScrollBar().setValue(0);
            }
        });
    }

    /** (internal display method)<br>
      * Displays the given error message.
      *
      * @param errorId          Error Id
      * @param filename         Name of the file
      */
    protected void displayErrorMessage(final int errorId,
                                       final String filename) {
        String eMes = "** Following Error has occured while loading the File "
                    + filename + "\n";

        switch (errorId) {
            case 1: eMes += "Maximum File Size reached -> Loading stopped";
                 break;
            case 2: eMes += "Unable to find the File";
                 break;
            case 3: eMes += "Unable to read the File";
                 break;
            case 4: eMes += "Unable to close the File";
                 break;
            default: System.out.println("Internal Error @ParserPanel.display"
                                        + "ErrorMessage\nUnknown Error Id"
                                        + " -> " + errorId);
                     return;
        }

        outpars.setText(eMes);

        // Scrollt das Editor Panel nach oben
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                scroll.getVerticalScrollBar().setValue(0);
            }
        });
    }

    /** empty method - not in use.
      *
      * @see SPanel#refreshPanelComponents
      * @deprecated
      */
    public void refreshPanelComponents() {
    }
}
