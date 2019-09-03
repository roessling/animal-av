package gfgaa.gui.messages.warning;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.TwoButtonMessage;

import javax.swing.ImageIcon;

/** Warning Message<br>
  * Displayed if the animation process has been failed by unknown reason.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class SelectedAnimationNotCreatedMessage extends TwoButtonMessage {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6328804691317960217L;

	/** (constructor)<br>
      * Creates a new message.
      *
      * @param mainclass            Reference to the projects mainclass
      * @param icon                 Reference to the displayed icon
      */
    public SelectedAnimationNotCreatedMessage(
                final GraphAlgController mainclass, final ImageIcon icon) {

        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_HINT;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            title = "Hint";
            this.message = new String[] {
                    "Some of the selected components were",
                    "not produced yet. Do you want to",
                    "save the animation nevertheless?"};

            this.buttonOneText = "Yes";
            this.buttonOneMnemonic = 'y';
            this.buttonTwoText = "No";
            this.buttonTwoMnemonic = 'n';

        } else {
            title = "Sicherheitshinweis";
            this.message = new String[] {
                    "Einige der ausgewählten Komponenten",
                    "wurden noch nicht erzeugt. Wollen Sie",
                    "die Animation trotzdem speichern?"};

            this.buttonOneText = "Fortfahren";
            this.buttonOneMnemonic = 'f';
            this.buttonTwoText = "Abbrechen";
            this.buttonTwoMnemonic = 'a';
        }

        this.setTitle(title);
        this.setContentPane(new TwoButtonPanel());
    }
}
