package gfgaa.gui.messages.parser;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.TwoButtonMessage;

import javax.swing.ImageIcon;

/** Parser Hint Message<br>
  * Displayed if an exception occured while parsing a graphscript file.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class ParserErrorGotoInputPanelMessage extends TwoButtonMessage {

    /**
   * 
   */
  private static final long serialVersionUID = 2880894176176197708L;

    /** (constructor)<br>
      * Creates a new message.
      *
      * @param mainclass            Reference to the projects mainclass
      * @param icon                 Reference to the displayed icon
      */
    public ParserErrorGotoInputPanelMessage(final GraphAlgController mainclass,
                                            final ImageIcon icon) {
        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_PARSER_ERROR;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            title = "Parser Error";
            this.message = new String[] {
                    "Errors during parsing the file.",
                    "Would you like to change to the",
                    "Preview Panel nevertheless?"};

            this.buttonOneText = "Yes";
            this.buttonOneMnemonic = 'y';
            this.buttonTwoText = "No";
            this.buttonTwoMnemonic = 'n';

        } else {
            title = "Fehler beim Parsen der Datei";
            this.message = new String[] {
                    "Beim Parsen des Scripts traten Fehler",
                    "auf. Wollen Sie trotzdem zum Preview",
                    "Panel wechseln?"};

            this.buttonOneText = "Ja";
            this.buttonOneMnemonic = 'j';
            this.buttonTwoText = "Nein";
            this.buttonTwoMnemonic = 'n';
        }

        this.setTitle(title);
        this.setContentPane(new TwoButtonPanel());
    }
}
