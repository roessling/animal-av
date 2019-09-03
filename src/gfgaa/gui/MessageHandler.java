package gfgaa.gui;

//Imports from Awt und Swing
import gfgaa.gui.messages.MessageDialog;
import gfgaa.gui.messages.error.CantCreateAnimationMessage;
import gfgaa.gui.messages.error.CantReadFileMessage;
import gfgaa.gui.messages.error.ErrorWritingFileMessage;
import gfgaa.gui.messages.error.ScriptCannotBeParsedMessage;
import gfgaa.gui.messages.internal.CantAddAlgorithmMessage;
import gfgaa.gui.messages.internal.ErrorReadingTalktableMessage;
import gfgaa.gui.messages.internal.TalktableNotFoundMessage;
import gfgaa.gui.messages.internal.UnknownErrorMessage;
import gfgaa.gui.messages.parser.ParserErrorGotoInputPanelMessage;
import gfgaa.gui.messages.parser.ParserErrorGotoPreviewMessage;
import gfgaa.gui.messages.warning.ComponentsOutdatetMessage;
import gfgaa.gui.messages.warning.DataMayBeLostMessage;
import gfgaa.gui.messages.warning.GenerationCanceldMessage;
import gfgaa.gui.messages.warning.SelectedAnimationNotCreatedMessage;
import gfgaa.gui.messages.warning.SureToExitMessage;
import gfgaa.gui.messages.warning.SureToRemoveEdgeMessage;
import gfgaa.gui.messages.warning.SureToRemoveNodeMessage;
import gfgaa.gui.others.JarFileLoader;
import gfgaa.gui.others.LanguageInterface;

import javax.swing.ImageIcon;

/** Error communication controll<br>
  * Class that handels the popup messages.
  *
  * @author S. Kulessa
  * @version 0.92
  */
public final class MessageHandler implements LanguageInterface {

    /** Reference to the mainclass of the project. */
    private GraphAlgController mainclass;

    /** Reference to the MessageDialog object. */
    private MessageDialog mesdia;

    /** ImageIcon used for error messages. */
    private ImageIcon errorIcon;

    /** ImageIcon used for question messages. */
    private ImageIcon questionIcon;

    /** Constant used for indexing the DataMayBeLost Message. */
    public static final int MESSAGE_DATA_MAY_BE_LOST = 0;

    /** Constant used for indexing the ParserErrorsGotoPreview Message. */
    public static final int MESSAGE_PARSER_ERRORS_GOTO_PREVIEW = 1;

    /** Constant used for indexing the CantReadFile Message. */
    public static final int MESSAGE_CANT_READ_FILE = 2;

    /** Constant used for indexing the SureToRemoveNode Message. */
    public static final int MESSAGE_SURE_TO_REMOVE_NODE = 3;

    /** Constant used for indexing the SureToRemoveEdge Message. */
    public static final int MESSAGE_SURE_TO_REMOVE_EDGE = 4;

    /** Constant used for indexing the ParserErrorsGotoInput Message. */
    public static final int MESSAGE_PARSER_ERRORS_GOTO_INPUTPANEL = 5;

    /** Constant used for indexing the ScriptCantParsed Message. */
    public static final int MESSAGE_SCRIPT_CANT_PARSED = 6;

    /** Constant used for indexing the ErrorWritingFile MEssage. */
    public static final int MESSAGE_ERROR_WRITING_FILE = 7;

    /** Constant used for indexing the SureToExit Message. */
    public static final int MESSAGE_SURE_TO_EXIT = 8;

    /** Constant used for indexing the SelectedAnimationNotCreated Message. */
    public static final int MESSAGE_SELECTED_ANIMATION_NOT_CREATED = 9;

    /** Constant used for indexing the CreateComponentsOutdatet Message. */
    public static final int MESSAGE_CREATE_COMPONENTS_OUTDATET = 10;

    /** Constant used for indexing the CantCreateAnimation Message. */
    public static final int MESSAGE_CANT_CREATE_ANIMATION = 11;

    /** Constant used for indexing the CantAddAlgorithm Message. */
    public static final int MESSAGE_CANT_ADD_ALGORITHM = 12;

    /** Constant used for indexing the ErrorReadingTalktable Message. */
    public static final int MESSAGE_ERROR_READING_TALKTABLE = 13;

    /** Constant used for indexing the TalktableNotFound Message. */
    public static final int MESSAGE_TALKTABLE_NOT_FOUND = 14;

    /** Constant used for indexing the GenerationCanceld Message. */
    public static final int MESSAGE_GENERATION_CANCELD = 15;

    /** (constructor)<br>
      * Creates a MessageHandler object.
      *
      * @param mainclass        Reference to the projects mainclass
      */
    public MessageHandler(final GraphAlgController mainclass) {
        this.mainclass = mainclass;

        String path = mainclass.getClass()
                               .getResource("MessageHandler.class")
                               .toString();
        path = path.substring(0, path.length() - 20);

        this.errorIcon = JarFileLoader.loadImage(path + "Icons/Error.png");
        this.questionIcon = JarFileLoader.loadImage(path
                                                    + "Icons/Question.png");
    }

    /** (internal data method)<br>
      * Sets the variables of the message that should be displayed.
      *
      * @param eMessageId        Error message id
      */
    protected void setMessage(final int eMessageId) {
        switch (eMessageId) {
            case MESSAGE_DATA_MAY_BE_LOST:
                    mesdia = new DataMayBeLostMessage(mainclass,
                                                      questionIcon);
                break;
            case MESSAGE_PARSER_ERRORS_GOTO_PREVIEW:
                    mesdia = new ParserErrorGotoPreviewMessage(mainclass,
                                                               questionIcon);
                break;
            case MESSAGE_CANT_READ_FILE:
                    mesdia = new CantReadFileMessage(mainclass, errorIcon);
                break;
            case MESSAGE_SURE_TO_REMOVE_NODE:
                    mesdia = new SureToRemoveNodeMessage(mainclass,
                                                         questionIcon);
                break;
            case MESSAGE_SURE_TO_REMOVE_EDGE:
                    mesdia = new SureToRemoveEdgeMessage(mainclass,
                                                         questionIcon);
                break;
            case MESSAGE_PARSER_ERRORS_GOTO_INPUTPANEL:
                    mesdia =
                        new ParserErrorGotoInputPanelMessage(mainclass,
                                                             questionIcon);
                break;
            case MESSAGE_SCRIPT_CANT_PARSED:
                    mesdia = new ScriptCannotBeParsedMessage(mainclass,
                                                         errorIcon);
                break;
            case MESSAGE_ERROR_WRITING_FILE:
                    mesdia = new ErrorWritingFileMessage(mainclass,
                                                         errorIcon);
                break;
            case MESSAGE_SURE_TO_EXIT:
                    mesdia = new SureToExitMessage(mainclass, questionIcon);
                break;
            case MESSAGE_SELECTED_ANIMATION_NOT_CREATED:
                    mesdia =
                        new SelectedAnimationNotCreatedMessage(mainclass,
                                                               errorIcon);
                break;
            case MESSAGE_CREATE_COMPONENTS_OUTDATET:
                    mesdia = new ComponentsOutdatetMessage(mainclass,
                                                           questionIcon);
                break;
            case MESSAGE_CANT_CREATE_ANIMATION:
                    mesdia = new CantCreateAnimationMessage(mainclass,
                                                            errorIcon);
                break;
            case MESSAGE_CANT_ADD_ALGORITHM:
                    mesdia = new CantAddAlgorithmMessage(mainclass,
                                                         errorIcon);
                break;
            case MESSAGE_ERROR_READING_TALKTABLE:
                    mesdia = new ErrorReadingTalktableMessage(mainclass,
                                                              errorIcon);
                break;
            case MESSAGE_TALKTABLE_NOT_FOUND:
                    mesdia = new TalktableNotFoundMessage(mainclass,
                                                          errorIcon);
            case MESSAGE_GENERATION_CANCELD:
                    mesdia = new GenerationCanceldMessage(mainclass,
                                                          errorIcon);
                break;
            default:
                mesdia = new UnknownErrorMessage(mainclass, errorIcon);
        }
    }

    /** (internal info method)<br>
      * Returns the type of the message.
      *
      * @return                 Type of the message
      */
    protected int getMessageType() {
        return mesdia.getMessageType();
    }

    /** (internal info method)<br>
      * Returns wheter the user has answered yes or no.
      *
      * @return         Answer of the user
      */
    protected boolean getAnswer() {
        return mesdia.getAnswer();
    }

    /** (internal display method)<br>
      * Sets the errormessage visible state to TRUE.
      */
    protected void setMessageVisible() {
        mesdia.setVisible(true);
    }
}
