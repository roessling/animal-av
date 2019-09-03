package gfgaa.gui.messages.error;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.OneButtonMessage;

import javax.swing.ImageIcon;

/** Error Message<br>
  * Displayed if the choosen file to save something (e.g. graphscript)
  * can not be written.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class ErrorWritingFileMessage extends OneButtonMessage {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** (constructor)<br>
      * Creates a new Message.
      *
      * @param mainclass            Reference to the projects mainclass
      * @param icon                 Reference to the displayed icon
      */
    public ErrorWritingFileMessage(final GraphAlgController mainclass,
                                   final ImageIcon icon) {
        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_ERROR;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            this.title = "Error";
            this.message = new String[] {"Error during writing the file."};
        } else {
            this.title = "Fehler";
            this.message = new String[] {"Fehler beim Schreiben der Datei."};
        }

        this.buttonText = "OK";
        this.buttonMnemonic = 'o';

        this.setTitle(title);
        this.setContentPane(new OneButtonPanel());
    }
}
