package gfgaa.gui.messages.internal;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.OneButtonMessage;

import javax.swing.ImageIcon;

/** Internal Error Message<br>
  * Displayed if an exception occured while reading the talktable file
  * of the selected graphalgorithm generator.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class ErrorReadingTalktableMessage extends OneButtonMessage {

    /**
   * 
   */
  private static final long serialVersionUID = -2101589606873235382L;

    /** (constructor)<br>
      * Creates a new message.
      *
      * @param mainclass            Reference to the projects mainclass
      * @param icon                 Reference to the displayed icon
      */
    public ErrorReadingTalktableMessage(final GraphAlgController mainclass,
                                        final ImageIcon icon) {
        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_INTERNAL_ERROR;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            this.title = "Internal Error";
            this.message = new String[] {
                    "An error occured while reading the",
                    "talktable file of the selected",
                    "algorithm generator."};
        } else {
            this.title = "Interner Fehler";
            this.message = new String[] {
                    "Während des Lesen der Talktable Datei",
                    "des ausgewählten Algorithmus Generators.",
                    "tratt ein Fehler auf."};
        }

        this.buttonText = "OK";
        this.buttonMnemonic = 'o';

        this.setTitle(title);
        this.setContentPane(new OneButtonPanel());
    }
}
