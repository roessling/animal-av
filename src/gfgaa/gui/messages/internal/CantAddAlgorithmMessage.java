package gfgaa.gui.messages.internal;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.OneButtonMessage;

import javax.swing.ImageIcon;

/** Internal Error Message<br>
  * Displayed if a GraphAlgorithm component can not be added to the GUI.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class CantAddAlgorithmMessage extends OneButtonMessage {

    /**
   * 
   */
  private static final long serialVersionUID = 4616469320380162832L;

    /** (constructor)<br>
      * Creates a new message.
      *
      * @param mainclass            Reference to the projects mainclass
      * @param icon                 Reference to the displayed icon
      */
    public CantAddAlgorithmMessage(final GraphAlgController mainclass,
                                   final ImageIcon icon) {
        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_INTERNAL_ERROR;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            this.title = "Internal Error";
            this.message = new String[] {
                    "An error occurred while adding the",
                    "GraphAlgorithms. Please look at your",
                    "console for further informations."};
        } else {
            this.title = "Interner Fehler";
            this.message = new String[] {
                    "Beim Einfügen der GraphenAlgorithmen",
                    "traten Fehler auf. Auf Ihrer Konsole",
                    "finden sie genaure Informationen."};
        }

        this.buttonText = "OK";
        this.buttonMnemonic = 'o';

        this.setTitle(title);
        this.setContentPane(new OneButtonPanel());
    }
}
