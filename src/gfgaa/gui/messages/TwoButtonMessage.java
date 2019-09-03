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
  * Contains the relevant attributes of two button messages.
  *
  * @author S. Kulessa
  * @version 0.95
  */
public abstract class TwoButtonMessage extends MessageDialog {

    /**
   * 
   */
  private static final long serialVersionUID = -2487270763917245657L;

    /** Panel class<br>
      * Represents the panel for all two button messages.
      *
      * @author S. Kulessa
      * @version 0.95
      */
    protected final class TwoButtonPanel extends JPanel {

        /**
       * 
       */
      private static final long serialVersionUID = -481569697818418928L;

        /** Panel component - Label that contains the message icon. */
        private JLabel iconLabel;

        /** Panel component - Labels that contains the message text. */
        private JLabel[] messageLabel;

        /** Panel component - Button to set the answer value of yes. */
        private JButton yes;

        /** Panel component - Button to set the answer value to no. */
        private JButton no;

        /** Contains the maximum length of the message text lines. */
        private int maxlength = -1;

        /** Contains the y-axis bonus for displaying the message text. */
        private int bonus;

        /** (constructor)<br>
          * Creates a new two button panel.
          */
        public TwoButtonPanel() {
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

            add(createYesButton());
            add(createNoButton());
        }

        /** (panel construction method)<br>
          * Creates the yes button.
          *
          * @return     yes button
          */
        private JButton createYesButton() {
            yes = new JButton(buttonOneText);

            yes.setBounds(60, 70, 100, 25);
            yes.setMnemonic(buttonOneMnemonic);

            yes.addActionListener(new ActionListener() {

                /**
                  *
                  * @param e
                  */
                public void actionPerformed(final ActionEvent e) {
                    answer = true;
                    close();
                }
            });

            return yes;
        }

        /** (panel construction method)<br>
          * Creates the no button.
          *
          * @return     no button
          */
        private JButton createNoButton() {
            no = new JButton(buttonTwoText);

            no.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    answer = false;
                    close();
                }
            });

            no.setBounds(160, 70, 100, 25);
            no.setMnemonic(buttonTwoMnemonic);
            return no;
        }

        /** (panel display method)<br>
          * Paints the panel and his components.
          *
          * @param g        Graphical component of this panel
          */
        public void paint(final Graphics g) {
            Dimension size = this.getSize();

            int[] pos = new int[5];
            pos[0] = (size.height - 95) / 2;
            pos[1] = pos[0] + bonus;
            pos[2] = (size.width - (maxlength * 6)) / 2 + 10;
            pos[3] = pos[0] + 65;
            pos[4] = (size.width - 210) / 2;

            iconLabel.setLocation((size.width - 290) / 2, pos[0] + 17);

            for (int i = 0; i < messageLabel.length; i++) {
                messageLabel[i].setLocation(pos[2], pos[1] + 20 * i);
            }

            yes.setLocation(pos[4], pos[3]);
            no.setLocation(pos[4] + 110, pos[3]);

            super.paint(g);
        }
    }

    /** Contains the mnemonic of the first button. */
    protected int buttonOneMnemonic;

    /** Contains the text of the first button. */
    protected String buttonOneText;

    /** Contains the mnemonic of the second button. */
    protected int buttonTwoMnemonic;

    /** Contains the text of the second button. */
    protected String buttonTwoText;

    /** (constructor)<br>
      * Creates a new TwoButtonMessageDialog.
      *
      * @param mainclass    Reference to the projects mainclass
      */
    public TwoButtonMessage(final GraphAlgController mainclass) {
        super(mainclass);

        this.setSize(320, 120);
        this.setResizable(false);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getSize().width) / 2,
                    (d.height - getSize().height) / 2);
    }
}
