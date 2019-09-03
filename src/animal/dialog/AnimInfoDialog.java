package animal.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import translator.AnimalTranslator;
import animal.main.Animal;
import animal.main.Animation;

/**
 * Dialog that displays information about the current animation.
 *
  * @author <a href="http://www.ahrgr.de/guido/">Guido
 * R&ouml;&szlig;ling</a>
 * @version 1.1 2000-06-30
*/
public class AnimInfoDialog extends JDialog implements ActionListener {
  // =================================================================
  //                                 STATICS
  // =================================================================

  /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 391593064932584620L;

/**
   * Store the reference to the non-modal dialog
   */
  private static AnimInfoDialog dialog = null;

  // =================================================================
  //                               ATTRIBUTES
  // =================================================================

  /**
   * The <code>animal.main.Animal</code> reference is required for reading the image
   *
   * @see animal.main.AnimalFrame#getImageIcon(String)
   */
//  private Animal animal;

  /**
   * The <code>okButton</code> closes the dialog
   */
  private JButton okButton;
//  private Rectangle animSize = null;
  
  private Font                  defaultFont      = new Font("Dialog", 0, 14);

  private JLabel                dummy1;
  private JLabel                dummy2;
  private JLabel                l;
  private JLabel                l2;

  // ======================================================================
  //                               Class Constructors
  // ======================================================================

  /**
   * Open a new AnimInfoDialog.
   *
   * @param animal The Animal object currently in use
   */
  public AnimInfoDialog(Animal animal) {
    super(animal, AnimalTranslator.translateMessage("AnimInfo", null), false);

    JPanel internalPanel = new JPanel();
    Container contentPane = getContentPane();
    
    GridLayout grid = new GridLayout(0, 1);
    internalPanel.setLayout(grid);
    contentPane.setLayout(new BorderLayout());
    
    Font biggerFont = new Font("SansSerif", Font.BOLD, 18);

    l = new JLabel(AnimalTranslator.translateMessage("AnimTitle"), null,
    		SwingConstants.CENTER);
    l.setFont(biggerFont);
    internalPanel.add(l);

    Animation anim = animal.getAnimation();
    String dummy = ((anim == null) || (anim.getTitle() == null)) ? "---"
                                                                 : anim.getTitle();
    dummy1 = new JLabel(dummy);
    internalPanel.add(dummy1); // , JLabel.CENTER);
    l2 = new JLabel(AnimalTranslator.translateMessage("AnimAuthor", null), 
        SwingConstants.CENTER);
    l2.setFont(biggerFont);
    internalPanel.add(l2); // , JLabel.CENTER);
    dummy = ((anim == null) || (anim.getAuthor() == null)) ? "---"
                                                           : anim.getAuthor();
    dummy2 = new JLabel(dummy);
    internalPanel.add(dummy2); // , JLabel.CENTER);

    okButton = new JButton(AnimalTranslator.translateMessage("ok", null));
    okButton.setMnemonic(KeyEvent.VK_O);
    okButton.addActionListener(this);
    contentPane.add(okButton, BorderLayout.SOUTH);
    contentPane.add(internalPanel, BorderLayout.CENTER);
    pack();

    addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          AnimInfoDialog.this.windowClosing();
        }
      });
    dialog = this;
  }

  // ======================================================================
  //                            Attribute get/set
  // ======================================================================

  /**
   * returns the currently active AnimInfoDialog, null if none such exists.
   *
   * @param animal the current instance of Animal.
   * @return the currently active AnimInfoDialog if existant, else null.
   */
  public static AnimInfoDialog getAnimInfoDialog(Animal animal) {
    if (dialog == null) {
      dialog = new AnimInfoDialog(animal);
    }

    return dialog;
  }

  /**
   * Center the dialog relative to its parent before showing it.
   *
   * @param showIt the value that is used for 'setVisible'.
   */
  public void setVisible(boolean showIt) {
    if (!isVisible()) {
      setLocationRelativeTo(getParent());
    }

    super.setVisible(showIt);
  }

  // ======================================================================
  //                             Event handling
  // ======================================================================

  /**
   * React to the OK button.
   *
   * @param event the event generated
   */
  public void actionPerformed(ActionEvent event) {
    if (event.getSource() == okButton) {
      windowClosing();
    }
  }

  /**
   * Close the AnimInfoDialog. This has to be done as only one
   * AnimInfoDialog may exist at a time.
   */
  public void windowClosing() {
    dialog = null;
    setVisible(false);
    dispose();
  }

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {
    Dimension dim = this.getSize();

    if (zoomIn) {
      if (defaultFont.getSize() < 24) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() + 2);
      }
      if (dim.getWidth() < 1000) {
        dim.setSize(dim.getWidth() + 20, dim.getHeight() + 20);
      }

    } else {
      if (defaultFont.getSize() > 10) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() - 2);
      }
      if (dim.getWidth() > 200) {
        dim.setSize(dim.getWidth() - 20, dim.getHeight() - 20);
      }

    }

    if (okButton != null) {
      okButton.setFont(defaultFont);
    }

    if (l != null) {
      Font f = new Font(l.getFont().getFontName(), l.getFont().getStyle(),
          defaultFont.getSize() + 4);
      l.setFont(f);
    }

    if (l2 != null) {
      Font f = new Font(l.getFont().getFontName(), l.getFont().getStyle(),
          defaultFont.getSize() + 4);
      l2.setFont(f);
    }

    if (dummy1 != null) {
      Font f = new Font(dummy1.getFont().getFontName(),
          dummy1.getFont().getStyle(), defaultFont.getSize() + 4);
      dummy1.setFont(f);
    }

    if (dummy2 != null) {
      Font f = new Font(dummy2.getFont().getFontName(),
          dummy2.getFont().getStyle(), defaultFont.getSize() + 4);
      dummy2.setFont(f);
    }



    this.setSize(dim);

  }
} // AnimInfoDialog
