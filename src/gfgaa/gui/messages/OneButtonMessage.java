package gfgaa.gui.messages;

import gfgaa.gui.GraphAlgController;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Message class<br>
  * Contains the relevant attributes of one button messages.
  *
  * @author S. Kulessa
  * @version 0.95
  */
public abstract class OneButtonMessage extends MessageDialog {

    /**
   * 
   */
  private static final long serialVersionUID = -2940780938699910604L;

    /** Panel class<br>
      * Represents the panel for all one button messages.
      *
      * @author S. Kulessa
      * @version 0.95
      */
    protected final class OneButtonPanel extends JPanel {

        /**
       * 
       */
      private static final long serialVersionUID = 8645899578440429981L;

        /** Panel component - Label that contains the message icon. */
        private JLabel iconLabel;

        /** Panel component - Labels that contains the message text. */
        private JLabel[] messageLabel;

        /** Panel component - Button to close the dialog. */
        private JButton ok;

        /** Contains the maximum length of the message text lines. */
        private int maxlength = -1;

        /** Contains the y-axis bonus for displaying the message text. */
        private int bonus;

        /** (constructor)<br>
          * Creates a new one button panel.
          */
        public OneButtonPanel() {
            this.setLayout(null);

            iconLabel = new JLabel(icon);
            iconLabel.setBounds(17, 17, 27, 27);
            this.add(iconLabel);

            int anz = message.length;
            messageLabel = new JLabel[anz];
            for (int i = 0; i < anz; i++) {
                messageLabel[i] = new JLabel(message[i]);
                messageLabel[i].setBounds(50, 20 * i, 250, 25);
                add(messageLabel[i]);
                if (message[i].length() > maxlength) {
                    maxlength = message[i].length();
                }
            }

            bonus = 0;
            if (messageLabel.length < 3) {
                if (messageLabel.length == 2) {
                    bonus = 10;
                } else {
                    bonus = 20;
                }
            }

            add(createOkButton());
        }

        /** (panel construction method)<br>
          * Creates the ok button.
          *
          * @return     ok button
          */
        private JButton createOkButton() {
            ok = new JButton(buttonText);

            ok.setBounds(110, 70, 100, 25);
            ok.setMnemonic(buttonMnemonic);

            ok.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    answer = true;
                    close();
                }
            });

            return ok;
        }

        /** (panel display method)<br>
          * Paints the panel and his components.
          *
          * @param g        Graphical component of this panel
          */
        public void paint(final Graphics g) {
            Dimension size = this.getSize();

            int[] pos = new int[3];
            pos[0] = (size.height - 95) / 2;
            pos[1] = pos[0] + bonus;
            pos[2] = (size.width - (maxlength * 6)) / 2 + 10;

            iconLabel.setLocation((size.width - 290) / 2, pos[0] + 17);

            for (int i = 0; i < messageLabel.length; i++) {
                messageLabel[i].setLocation(pos[2], pos[1] + 20 * i);
            }

            ok.setLocation((size.width - 100) / 2, pos[0] + 65);

            super.paint(g);
        }
    }

    /** Contains the buttons mnemonic. */
    protected int buttonMnemonic;

    /** Contains the text of the button. */
    protected String buttonText;

    /** (constructor)<br>
      * Creates a new OneButtonMessageDialog.
      *
      * @param mainclass    Reference to the projects mainclass
      */
    public OneButtonMessage(final GraphAlgController mainclass) {
        super(mainclass);

        this.setSize(320, 120);
        this.setResizable(false);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getSize().width) / 2,
                    (d.height - getSize().height) / 2);
    }
}
