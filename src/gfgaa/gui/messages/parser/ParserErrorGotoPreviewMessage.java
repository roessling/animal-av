package gfgaa.gui.messages.parser;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.TwoButtonMessage;

import javax.swing.ImageIcon;

/** Parser Hint Message<br>
  * Displayed if an exception occured while parsing the content of the
  * graphscript inputpane.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class ParserErrorGotoPreviewMessage extends TwoButtonMessage {

    /**
   * 
   */
  private static final long serialVersionUID = -916602466337725529L;

    /** (constructor)<br>
      * Creates a new message.
      *
      * @param mainclass            Reference to the projects mainclass
      * @param icon                 Reference to the displayed icon
      */
    public ParserErrorGotoPreviewMessage(final GraphAlgController mainclass,
                                         final ImageIcon icon) {
        super(mainclass);

        this.type = MessageDialog.MESSAGE_TYPE_PARSER_ERROR;
        this.icon = icon;

        if (mainclass.getLanguageSettings() == LANGUAGE_ENGLISH) {
            title = "Parser Error";
            this.message = new String[] {
                    "Error during parsing the file.",
                    "Would you like to load the file",
                    "into the GraphScript Panel?"};

            this.buttonOneText = "Yes";
            this.buttonOneMnemonic = 'y';
            this.buttonTwoText = "No";
            this.buttonTwoMnemonic = 'n';

        } else {
            title = "Fehler beim Parsen der Datei";
            this.message = new String[] {
                    "Beim Parsen der Datei traten Fehler",
                    "auf. Wollen Sie die Datei in das",
                    "Eingabe Panel laden?"};

            this.buttonOneText = "Ja";
            this.buttonOneMnemonic = 'j';
            this.buttonTwoText = "Nein";
            this.buttonTwoMnemonic = 'n';
        }

        this.setTitle(title);
        this.setContentPane(new TwoButtonPanel());
    }
}
