package gfgaa.gui.messages;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.others.LanguageInterface;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

/** Message class<br>
  * Contains the relevant attributes of all messages.
  *
  * @author S. Kulessa
  * @version 0.95
  */
public abstract class MessageDialog extends JDialog
                                    implements LanguageInterface {

    /**
   * 
   */
  private static final long serialVersionUID = 2855193555573609213L;

    /** Constant - Indicates the type value for hint and warning messages. */
    public static final int MESSAGE_TYPE_HINT = 0;

    /** Constant - Indicates the type value for parser error messages. */
    public static final int MESSAGE_TYPE_PARSER_ERROR = 1;

    /** Constant - Indicates the type value for error messages. */
    public static final int MESSAGE_TYPE_ERROR = 2;

    /** Constant - Indicates the type value for internal error messages. */
    public static final int MESSAGE_TYPE_INTERNAL_ERROR = 3;

    /** Contains the message type. */
    protected int type;

    /** Contains the icon of the message. */
    protected ImageIcon icon;

    /** Contains the tile of the message. */
    protected String title;

    /** Contains the message text. */
    protected String[] message;

    /** Temporary variable - Contains the answer of the user. */
    protected boolean answer;

    /** (constructor)<br>
      * Creates a new Message Dialog.
      *
      * @param mainclass        Reference to the projects mainclass
      */
    public MessageDialog(final GraphAlgController mainclass) {
        super(mainclass.getGUI(), true);
    }

    /** (internal info method)<br>
      * Returns the answer of the user.
      *
      * @return     Answer of the user
      */
    public boolean getAnswer() {
        return answer;
    }

    /** (internal info method)<br>
      * Returns the typ of the message.
      *
      * @return     Message type
      */
    public int getMessageType() {
        return this.type;
    }

    /** (internal display method)<br>
      * Sets the dialog visible state to false.
      */
    public void close() {
        this.setVisible(false);
    }
}
