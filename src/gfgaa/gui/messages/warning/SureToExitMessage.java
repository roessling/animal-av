package gfgaa.gui.messages.warning;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.TwoButtonMessage;

import javax.swing.ImageIcon;

/** Warning Message<br>
  * Displayed if the user has clicked on the exit MenuItem.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class SureToExitMessage extends TwoButtonMessage {

    /**
   * 
   */
  private static final long serialVersionUID = 1237932453464627428L;

    /** (constructor)<br>
      * Creates a new message.
      *
      * @param mainclass            Reference to the projects mainclass
      * @param icon                 Reference to the displayed icon
      */
    public SureToExitMessage(final GraphAlgController mainclass,
                             final ImageIcon icon) {
        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_HINT;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            this.title = "Hint";
            this.message = new String[] {
                    "Are you really sure to exit",
                    "the program?"};

            this.buttonOneText = "Yes";
            this.buttonOneMnemonic = 'y';
            this.buttonTwoText = "No";
            this.buttonTwoMnemonic = 'n';

        } else {
            this.title = "Sicherheitshinweis";
            this.message = new String[] {
                    "Sind Sie sicher das Sie das",
                    "Programm beenden wollen?"};

            this.buttonOneText = "Ja";
            this.buttonOneMnemonic = 'j';
            this.buttonTwoText = "Nein";
            this.buttonTwoMnemonic = 'n';
        }

        this.setTitle(title);
        this.setContentPane(new TwoButtonPanel());
    }
}
