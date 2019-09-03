package gfgaa.gui.messages.warning;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.TwoButtonMessage;

import javax.swing.ImageIcon;

/** Warning Message<br>
  * Displayed if the user wants to remove an edge from the graph.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class SureToRemoveEdgeMessage extends TwoButtonMessage {

    /**
   * 
   */
  private static final long serialVersionUID = 1185443350032617964L;

    /** (constructor)<br>
      * Creates a new message.
      *
      * @param mainclass            Reference to the projects mainclass
      * @param icon                 Reference to the displayed icon
      */
    public SureToRemoveEdgeMessage(final GraphAlgController mainclass,
                                   final ImageIcon icon) {
        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_HINT;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            title = "Hint";
            this.message = new String[] {
                    "Are you really sure that you want",
                    "delete these edge?"};

            this.buttonOneText = "Yes";
            this.buttonOneMnemonic = 'y';
            this.buttonTwoText = "No";
            this.buttonTwoMnemonic = 'n';

        } else {
            title = "Sicherheitshinweis";
            this.message = new String[] {
                    "Sind Sie sicher das sie die Kante",
                    "wirklich löschen wollen?"};

            this.buttonOneText = "Ja";
            this.buttonOneMnemonic = 'j';
            this.buttonTwoText = "Nein";
            this.buttonTwoMnemonic = 'n';
        }

        this.setTitle(title);
        this.setContentPane(new TwoButtonPanel());
    }
}
