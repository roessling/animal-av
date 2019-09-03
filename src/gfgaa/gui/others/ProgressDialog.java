package gfgaa.gui.others;

import gfgaa.gui.components.SPanel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/** Progress Dialog<br>
  * Indicates the progress of the animation code generation.
  *
  * @author S. Kulessa
  * @version 0.95
  */
public final class ProgressDialog extends JDialog {

    /**
   * 
   */
  private static final long serialVersionUID = -8646539574006805794L;

    /** ProgressBar component. */
    protected JProgressBar progress;

    /** Reference to the second Thread. */
    protected Thread t;

    /** Internal Flag - used to start the second Thread. */
    protected boolean ready = false;

    /** Progress Panel<br>
      * Panel of the progress dialog.
      *
      * @author S. Kulessa
      * @version 0.92
      */
    private final class ProgressPanel extends SPanel {

        /**
       * 
       */
      private static final long serialVersionUID = 6273422310267699571L;

        /** Button to terminate both Threads. */
        private JButton cancel;

        /** Label with "in progress" message. */
        private JLabel proLab1;

        /** Label with "please wait" message. */
        private JLabel proLab2;

        /** (constructor)<br>
          * Creates a new ProgressPanel.
          */
        public ProgressPanel() {
            this.setLayout(null);
            this.create();
        }

        /** (panel creation method)<br>
          * Creates the components of this panel.
          */
        private void create() {
            cancel = new JButton("Cancel");
            cancel.setBounds(50, 90, 100, 25);

            cancel.addActionListener(new ActionListener() {
               public void actionPerformed(final ActionEvent e) {
                   close();
               }
            });

            this.add(cancel);

            proLab1 = new JLabel("* Operation in progress * ");
            proLab1.setFont(new Font("Serif", Font.BOLD, 15));
            proLab1.setBounds(20, 5, 175, 25);
            this.add(proLab1);

            proLab2 = new JLabel("... Please wait ...");
            proLab2.setFont(new Font("Serif", Font.BOLD, 15));
            proLab2.setBounds(20, 30, 105, 25);
            this.add(proLab2);

            progress = new JProgressBar();
            progress.setBounds(25, 60, 150, 15);
            progress.setIndeterminate(true);
            this.add(progress);
        }

        /** (panel display method)<br>
          * Controlls the location of the panel components.
          */
        public void refreshPanelComponents() {
            Dimension size = this.getSize();
            cancel.setLocation((size.width - 100) / 2,
                               ((size.height - 100) / 2) + 75);
            proLab1.setLocation((size.width - 175) / 2, 5);
            proLab2.setLocation((size.width - 105) / 2, 30);
            progress.setLocation((size.width - 150) / 2, 60);
        }

        /** (panel display method)<br>
          * Draws the components of the panel.
          *
          * @param g    Graphical component of this panel
          */
        public void paint(final Graphics g) {
            refreshPanelComponents();
            super.paint(g);

            if (!ready) {
                t.start();
                ready = true;
            }
        }
    }

    /** (Constructor)<br>
      * Creates a new ProgressDialog.
      *
      * @param t            Thread t
      * @param gui          Reference to the GUI
      */
    public ProgressDialog(final Thread t, final JFrame gui) {
        super(gui, true);

        this.t = t;

        this.setTitle("Progress");
        this.setSize(200, 150);
        this.setResizable(false);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((d.width - getSize().width) / 2,
                         (d.height - getSize().height) / 2);

        this.setContentPane(new ProgressPanel());
    }

    /** (display method)<br>
      * Closes the ProgressDialog and stops the second Thread.
      */
    public void close() {
        this.setVisible(false);

        if (t.isAlive()) {
            t.interrupt();
        }
    }

    /** (runnable method)<br>
      * Displays the Dialog and starts the assigned Thread.
      */
    public void run() {
        this.setVisible(true);
    }

    /** (display method)<br>
      * Sets the progress information.
      *
      * @param percent      progress in percent
      */
    public void setProgress(final int percent) {
        progress.setIndeterminate(false);
        progress.setValue(percent);
    }
}
