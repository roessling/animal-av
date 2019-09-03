package gfgaa.gui.messages.warning;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.OneButtonMessage;

import javax.swing.ImageIcon;

/** Warning Message<br>
  * Displayed if the user has canceld the generation process of the animation.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class GenerationCanceldMessage extends OneButtonMessage {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4821075166908878305L;

	/** (constructor)<br>
      * Creates a new message.
      *
      * @param mainclass            Reference to the projects mainclass
      * @param icon                 Reference to the displayed icon
      */
    public GenerationCanceldMessage(final GraphAlgController mainclass,
            final ImageIcon icon) {
        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_HINT;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            this.title = "Hint";
            this.message = new String[] {
                    "The generation process has been canceld."};
        } else {
            this.title = "Hinweis";
            this.message = new String[] {
                    "Der Erzeugungsprozess der Animation",
                    "wurde abgebrochen."};
        }

        this.buttonText = "OK";
        this.buttonMnemonic = 'o';

        this.setTitle(title);
        this.setContentPane(new OneButtonPanel());
    }
}
