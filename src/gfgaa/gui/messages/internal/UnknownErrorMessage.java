package gfgaa.gui.messages.internal;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.OneButtonMessage;

import javax.swing.ImageIcon;

/** Internal Error Message<br>
  * Displayed if none of the other message has been specified.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class UnknownErrorMessage extends OneButtonMessage {

    /**
   * 
   */
  private static final long serialVersionUID = 4729645200912006416L;

    /** (constructor)<br>
      * Creates a new message.
      *
      * @param mainclass            Reference to the projects mainclass
      * @param icon                 Reference to the displayed icon
      */
    public UnknownErrorMessage(final GraphAlgController mainclass,
                               final ImageIcon icon) {
        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_INTERNAL_ERROR;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            this.title = "Internal Error";
            this.message = new String[] {
                    "Internal Error - UnknownErrorId"};
        } else {
            this.title = "Interner Fehler";
            this.message = new String[] {
                    "Interner Fehler - Unbekannte Id"};
        }

        this.buttonText = "OK";
        this.buttonMnemonic = 'o';

        this.setTitle(title);
        this.setContentPane(new OneButtonPanel());
    }
}
