package gfgaa.gui.messages.error;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.OneButtonMessage;

import javax.swing.ImageIcon;

/** Error Message<br>
  * Displayed if a graphscript file or the content of the input panel
  * can not be parsed.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class ScriptCannotBeParsedMessage extends OneButtonMessage {

    /**
   * 
   */
  private static final long serialVersionUID = 6174147826893619622L;

    /** (constructor)<br>
      * Creates a new message.
      *
      * @param mainclass            Reference to the projects mainclass
      * @param icon                 Reference to the displayed icon
      */
    public ScriptCannotBeParsedMessage(final GraphAlgController mainclass,
                                   final ImageIcon icon) {
        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_ERROR;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            this.title = "Error";
            this.message = new String[] {"Script could not be parsed."};
        } else {
            this.title = "Fehler";
            this.message = new String[] {"Script konnte nicht geparst werden."};
        }

        this.buttonText = "OK";
        this.buttonMnemonic = 'o';

        this.setTitle(title);
        this.setContentPane(new OneButtonPanel());
    }
}
