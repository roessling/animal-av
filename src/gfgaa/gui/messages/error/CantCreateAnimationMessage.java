package gfgaa.gui.messages.error;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.OneButtonMessage;

import javax.swing.ImageIcon;

/** Error Message<br>
  * Displayed if the animation can not be created.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class CantCreateAnimationMessage extends OneButtonMessage {

    /**
   * 
   */
  private static final long serialVersionUID = 4509411381045139732L;

    /** (constructor)<br>
      * Creates a new message.
      *
      * @param mainclass            Reference to the projects mainclass
      * @param icon                 Reference to the displayed icon
      */
    public CantCreateAnimationMessage(final GraphAlgController mainclass,
                                      final ImageIcon icon) {
        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_ERROR;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            this.title = "Error";
            this.message = new String[] {
                    "Creation of some animation components",
                    "failed."};
        } else {
            this.title = "Fehler";
            this.message = new String[] {
                    "Die Animations Komponenten konnten nicht",
                    "erzeugt werden."};
        }

        this.buttonText = "OK";
        this.buttonMnemonic = 'o';

        this.setTitle(title);
        this.setContentPane(new OneButtonPanel());
    }
}
