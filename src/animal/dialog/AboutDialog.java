package animal.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import translator.AnimalTranslator;
import translator.ResourceLocator;
import animal.main.Animal;
import animal.main.AnimalConfiguration;

/**
 * A simple dialog that displays information about Animal.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling</a>
 * @version 1.1 2000-06-30
 */
public class AboutDialog extends JDialog implements ActionListener {
  // =================================================================
  // STATICS
  // =================================================================

  /**
   * Comment for <code>serialVersionUID</code>.
   */
  private static final long serialVersionUID = -5083531199461628721L;

  /**
   * Store the reference to the non-modal dialog.
   */
  private static AboutDialog dialog = null;

  // =================================================================
  // ATTRIBUTES
  // =================================================================

  /**
   * The <code>animal.main.Animal</code> reference is required for reading the
   * image.
   * 
   * @see animal.main.AnimalFrame#getImageIcon(String)
   */
  private Animal animal;

  /**
   * The <code>okButton</code> closes the dialog.
   */
  private AbstractButton okButton;
  private JEditorPane        textArea;
  private Font               defaultFont      = new Font("Dialog", 0, 14);

  // ======================================================================
  // CONSTRUCTORS
  // ======================================================================


  /**
   * Open a new AboutDialog.
   * 
   * @param animalInstance
   *          The Animal object currently in use
   */
  public AboutDialog(final Animal animalInstance) {
    super(animalInstance, AnimalTranslator
        .translateMessage("about.label", null), false);
    animal = animalInstance;

    String htmlString = createHTMLRendition();
    textArea = new JEditorPane("text/html", htmlString);
    textArea.setEditable(false);
    textArea.setPreferredSize(new Dimension(640, 480));
    textArea.setCaretPosition(0);
    
    JScrollPane scroller = new JScrollPane(textArea);
    scroller.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(scroller, BorderLayout.CENTER);

    okButton = new JButton(AnimalTranslator.translateMessage("ok", null));
    getContentPane().add(okButton, BorderLayout.SOUTH); // ,gbc);

    pack();

    okButton.addActionListener(this);
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(final WindowEvent e) {
        AboutDialog.this.windowClosing();
      }
    });
    dialog = this;
  }

  private String createHTMLRendition() {
    StringBuilder sb = new StringBuilder(16392);
    String homeURL = "http://www.algoanim.net";
    
    sb.append("<html>");
    sb.append("\n\n<body>");
    
//    sb.append("\n\n<div align='center'><img src='graphics/Animal.png' "); // /graphics/Animal.gif' ");
//    sb.append("alt='Animal Logo' /></div>");
    
    sb.append("\n\n<h1 style='text-align: center; font-variant: small-caps'>Animal</h1>");
    sb.append("\n\n<h2 style='text-align: center;'>");
    sb.append("<span style='text-color: red'>A</span>dvanced ");
    sb.append("<span style='text-color: red'>N</span>avigation");
    sb.append(" and <span style='text-color: red'>I</span>nteractive ");
    sb.append("<span style='text-color: red'>M</span>odeling for ");
    sb.append("<span style='text-color: red'>A</span>nimations ");
    sb.append(" in <span style='text-color: red'>L</span>ectures</h2>");
    
    String versionInfo = 
      AnimalConfiguration.getDefaultConfiguration().getVersionLine("versionInfoLine");
    sb.append("\n\n<p><em>Version: ").append(versionInfo).append("</em></p>");
    sb.append("\n\n<p>&copy; 1998 Markus Sch&uuml;ler, Guido R&ouml;&szlig;ling [1.1]");
    sb.append("\n\n<br />&copy; 1999+ Guido R&ouml;&szlig;ling [1.2+]");
    sb.append("\n<br />Contact email: <code>roessling@acm.org</code>");
    sb.append("\n<br />Home page: <a href='").append(homeURL);
    sb.append("'>").append(homeURL).append("</a></p>");
   
    sb.append("\n\n<h2>Contributors</h2>\n\n");
    try {
      InputStream is = ResourceLocator.getResourceLocator().getResourceStream("contributors.txt");
//      System.err.println("IS: " +is);
      BufferedReader bis = 
        new BufferedReader(
            new InputStreamReader(is));//new FileInputStream("contributors.txt"),
                                  //"MacRoman")); //"ISO-8859-1"));    
        String line = null;
        while ((line = bis.readLine()) != null)
          sb.append(line).append("\n");
        bis.close();
      } catch (IOException e) {
        System.err.println(e.getMessage());
      }
      sb.append("\n\n");
      if (Animal.generatorDemo == null) {
        animal.openGenerator();
        Animal.generatorDemo.setVisible(false);
      }
        
      // gather content generation authors
      sb.append(Animal.generatorDemo.getContentAuthors());

      sb.append("</body>\n</html>\n");
      return sb.toString();
  }

  // ======================================================================
  // Attribute get/set
  // ======================================================================

  /**
   * returns the currently active AboutDialog, null if none such exists.
   * 
   * @param animalInstance
   *          the current instance of Animal.
   * @return the currently active AboutDialog if existant, else null.
   */
  public static AboutDialog getAboutDialog(final Animal animalInstance) {
    if (dialog == null) {
      dialog = new AboutDialog(animalInstance);
    }

    return dialog;
  }

  /**
   * Center the dialog relative to its parent before showing it.
   * 
   * @param showIt
   *          the value that is used for 'setVisible'.
   */
  public void setVisible(final boolean showIt) {
    if (!isVisible()) {
      setLocationRelativeTo(getParent());
    }

    super.setVisible(showIt);
  }

  // ======================================================================
  // Event handling
  // ======================================================================

  /**
   * React to the OK button.
   * 
   * @param event
   *          the event generated
   */
  public void actionPerformed(final ActionEvent event) {
    if (event.getSource() == okButton) {
      windowClosing();
    }
  }

  /**
   * Close the AboutDialog. This has to be done as only one AboutDialog may
   * exist at a time.
   */
  public void windowClosing() {
//    AboutDialog.dialog = null;
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

    if (textArea != null) {
      Font f = new Font(textArea.getFont().getFontName(),
          textArea.getFont().getStyle(), defaultFont.getSize());

      textArea.setFont(f);
      textArea.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES,
          Boolean.TRUE);

    }

    this.setSize(dim);

  }


} // AboutDialog