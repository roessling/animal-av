package gfgaa.gui.messages.warning;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.TwoButtonMessage;

import javax.swing.ImageIcon;

/** Warning Messagee<br>
  * Displayed if data could be lost (for example when loading a file).
  *
  * @author S. Kulessa
  * @version 0.92
  */
public class DataMayBeLostMessage extends TwoButtonMessage {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** (constructor)<br>
      * Creates a new message.
      *
      * @param mainclass            Reference to the projects mainclass
      * @param icon                 Reference to the displayed icon
      */
    public DataMayBeLostMessage(final GraphAlgController mainclass,
                                final ImageIcon icon) {
        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_HINT;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            title = "Hint";
            this.message = new String[] {
                    "If you continue you will possibly",
                    "lose data."};

            this.buttonOneText = "Continue";
            this.buttonOneMnemonic = 'c';
            this.buttonTwoText = "Stop";
            this.buttonTwoMnemonic = 's';

        } else {
            title = "Sicherheitshinweis";
            this.message = new String[] {
                    "Wenn Sie fortfahren verlieren Sie",
                    "möglicherweise Daten."};

            this.buttonOneText = "Weiter";
            this.buttonOneMnemonic = 'w';
            this.buttonTwoText = "Abbrechen";
            this.buttonTwoMnemonic = 'a';
        }

        this.setTitle(title);
        this.setContentPane(new TwoButtonPanel());
    }
}
