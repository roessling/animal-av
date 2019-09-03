package gfgaa.gui.messages.warning;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.TwoButtonMessage;

import javax.swing.ImageIcon;

/** Warning Message<br>
  * Displayed if at least one of the animation subcomponents is outdatet.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class ComponentsOutdatetMessage extends TwoButtonMessage {

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
    public ComponentsOutdatetMessage(final GraphAlgController mainclass,
                                     final ImageIcon icon) {
        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_HINT;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            title = "Hint";
            this.message = new String[] {
                    "Some of the produced components are",
                    "possibly outdated. Would you want to",
                    "rebuild this components of the animation?"};

            this.buttonOneText = "Yes";
            this.buttonOneMnemonic = 'y';
            this.buttonTwoText = "No";
            this.buttonTwoMnemonic = 'n';

        } else {
            title = "Hinweis";
            this.message = new String[] {
                    "Einige der erzeugten Komponenten sind",
                    "möglicherweise veraltet. Wollen Sie die",
                    "Animations Komponenten aktualisieren?"};

            this.buttonOneText = "Ja";
            this.buttonOneMnemonic = 'j';
            this.buttonTwoText = "Nein";
            this.buttonTwoMnemonic = 'n';
        }

        this.setTitle(title);
        this.setContentPane(new TwoButtonPanel());
    }
}
