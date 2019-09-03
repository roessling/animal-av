package gfgaa.gui.messages.internal;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.OneButtonMessage;

import javax.swing.ImageIcon;

/** Internal Error Message<br>
  * Displayed if the talktable file of the selected graphalgorithm generator
  * can not be found.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class TalktableNotFoundMessage extends OneButtonMessage {

    /**
   * 
   */
  private static final long serialVersionUID = -3130657769939755139L;

    /** (constructor)<br>
      * Creates a new message.
      *
      * @param mainclass            Reference to the projects mainclass
      * @param icon                 Reference to the displayed icon
      */
    public TalktableNotFoundMessage(final GraphAlgController mainclass,
                                    final ImageIcon icon) {
        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_INTERNAL_ERROR;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            this.title = "Internal Error";
            this.message = new String[] {
                    "The talktable file of the selected",
                    "algorithm generator could not be opened."};
        } else {
            this.title = "Interner Fehler";
            this.message = new String[] {
                    "Die Talktable Datei des Algorithmus",
                    "Generators konnte nicht geöffnet werden."};
        }

        this.buttonText = "OK";
        this.buttonMnemonic = 'o';

        this.setTitle(title);
        this.setContentPane(new OneButtonPanel());
    }
}
