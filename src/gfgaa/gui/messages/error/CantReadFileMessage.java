package gfgaa.gui.messages.error;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.OneButtonMessage;

import javax.swing.ImageIcon;

/** Error Message<br>
  * Displayed if a choosen graphscript file can not be readed.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class CantReadFileMessage extends OneButtonMessage {

    /**
   * 
   */
  private static final long serialVersionUID = 6781268798902063512L;

    /** (constructor)<br>
      * Creates a new message.
      *
      * @param mainclass            Reference to the projects mainclass
      * @param icon                 Reference to the displayed icon
      */
    public CantReadFileMessage(final GraphAlgController mainclass,
                               final ImageIcon icon) {
        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_ERROR;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            this.title = "Error";
            this.message = new String[] {"Error during reading the file."};
        } else {
            this.title = "Fehler";
            this.message = new String[] {"Fehler beim Lesen der Datei."};
        }

        this.buttonText = "OK";
        this.buttonMnemonic = 'o';

        this.setTitle(title);
        this.setContentPane(new OneButtonPanel());
    }
}
