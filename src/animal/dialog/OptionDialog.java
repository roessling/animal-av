package animal.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import translator.AnimalTranslator;
import translator.ExtendedActionButton;
import animal.gui.AnimalMainWindow;
import animal.gui.DrawCanvas;
import animal.gui.DrawWindow;
import animal.main.Animal;
import animal.misc.ColorChoice;
import animal.misc.ColorChooserAction;

/**
 * Dialog to set Animal's options.
 *
  * @author <a href="http://www.ahrgr.de/guido/">Guido
 * R&ouml;&szlig;ling</a>
 * @version 1.1 2000-06-30
*/
public class OptionDialog extends JDialog implements ActionListener,
  PropertyChangeListener {
  // =================================================================
  //                                 STATICS
  // =================================================================

  /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 4816005119499540962L;

/**
   * Only one OptionDialog may be displayed at a time
   * @see animal.dialog.AboutDialog
   */
  private static OptionDialog dialog = null;

  private Font                defaultFont      = new Font("Dialog.bold", 1, 14);
  private Font                bigFont          = new Font("SansSerif",
      Font.BOLD, 16);
  private Font                middleFont       = new Font("SansSerif",
      Font.PLAIN, 14);
  // =================================================================
  //                             ATTRIBUTES
  // =================================================================

  /**
   * The base Animal object used for retrieving the properties etc.
   */
  private Animal animal;

  /**
   * The ColorChooser for choosing the animation window background color
   *
   * @see animal.misc.ColorChooserAction
   */
  private ColorChooserAction animationBackgroundColorChooser;

  private ExtendedActionButton animationBackgroundButton;

  /**
   * The Button for the "apply" operation
   */
  private AbstractButton applyButton;

  /**
   * The ColorChooser for choosing the(drawing window) background color
   *
   * @see animal.misc.ColorChooserAction
   */
  private ColorChooserAction backgroundColorChooser;
  private ExtendedActionButton backgroundColorButton;

  /**
   * The Button for the "cancel" operation
   */
  private AbstractButton cancelButton;

  /**
   * The checkbox for "autoload last file"
   */
  private JCheckBox cbAutoload;
  
  /**
   * The checkbox for "autohide pdf menu"
   */
  private JCheckBox cbAutohide;

  /**
   * The name of the base font
   */
  private JComboBox<String> fontName;

  /**
   * The ColorChooser for choosing the grid color
   *
   * @see animal.misc.ColorChooserAction
   */
  private ColorChooserAction gridColorChooser;
  private ExtendedActionButton gridColorButton;

  /**
   * The Button for the "OK" operation
   */
  private AbstractButton okButton;

  /**
   * The button for the grid type "lineGrid"
   */
  private JRadioButton rbLineGrid;

  /**
   * The button for the grid type "pointGrid"
   */
  private JRadioButton rbPointGrid;

  /**
   * The button for converting all fonts to "sane" sizes
   */
  private JButton saneFontSizeButton;

  /**
   * The delay between steps in slide show mode
   */
  private JComboBox<String> slideShowDelayBox;

  private JLabel              l;
  private JLabel              l2;
  private JLabel              l3;
  private JLabel              l4;
  private JLabel              l5;
  private JLabel              l6;
  private JLabel              l7;

  // =================================================================
  //                            CONSTRUCTORS
  // =================================================================

  /**
   * Construct a new OptionDialog on the base object passed
   *
   * @param animalInstance the base Animal object
   */
  public OptionDialog(Animal animalInstance) {
    super(animalInstance, AnimalTranslator.translateMessage("options", null), false);

    animal = animalInstance;

    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    this.setSize(400, 300);


    getContentPane().setLayout(gbl);

    l = new JLabel(AnimalTranslator.translateMessage("selectOptions", null));
    l.setPreferredSize(new Dimension(260, 30));

    bigFont = new Font("SansSerif", Font.BOLD, 16);
    middleFont = new Font("SansSerif", Font.PLAIN, 14);
    l.setFont(bigFont);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    getContentPane().add(l, gbc);
    l2 = new JLabel(AnimalTranslator.translateMessage("gridStyleOption", null));
    l2.setPreferredSize(new Dimension(260, 30));
    gbc.gridwidth = 1;
    l2.setFont(middleFont);
    getContentPane().add(l2, gbc);

    rbLineGrid = new JRadioButton(AnimalTranslator.translateMessage(
          "lineGridOption", null));
    gbc.gridwidth = 1;
    getContentPane().add(rbLineGrid, gbc);

    rbPointGrid = new JRadioButton(AnimalTranslator.translateMessage(
          "pointGridOption", null));
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    getContentPane().add(rbPointGrid, gbc);

    ButtonGroup bg = new ButtonGroup();
    bg.add(rbLineGrid);
    bg.add(rbPointGrid);

    l3 = new JLabel(AnimalTranslator.translateMessage("gridColorOption", null));
    l3.setPreferredSize(new Dimension(260, 30));
    l3.setFont(middleFont);
    gbc.gridwidth = 2;
    getContentPane().add(l3, gbc);

    DrawWindow drawWindow = AnimalMainWindow.getWindowCoordinator().getDrawWindow(true); 
    DrawCanvas drawCanvas = drawWindow.getDrawCanvas();
    Color initialColor = drawCanvas.getGridColor();
    gridColorChooser = new ColorChooserAction(this,
        ColorChoice.getColorName(initialColor), "gridColor",
        AnimalTranslator.translateMessage("chooseColor",
          new Object[] { AnimalTranslator.translateMessage("gridColor") }),
        initialColor);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gridColorButton = new ExtendedActionButton(gridColorChooser,
        KeyEvent.VK_G);
    getContentPane().add(gridColorButton, gbc);

    l4 = new JLabel(AnimalTranslator.translateMessage("bgColorOption", null));
    l4.setPreferredSize(new Dimension(260, 30));
    l4.setFont(middleFont);
    gbc.gridwidth = 2;
    getContentPane().add(l4, gbc);

    initialColor = drawCanvas.getBackgroundColor();
    backgroundColorChooser = new ColorChooserAction(this,
        ColorChoice.getColorName(initialColor), "backgroundColor",
        AnimalTranslator.translateMessage("chooseColor",
          new Object[] { AnimalTranslator.translateMessage("bgColor") }),
        initialColor);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    backgroundColorButton = new ExtendedActionButton(backgroundColorChooser,
        KeyEvent.VK_B);
    getContentPane().add(backgroundColorButton, gbc);

    l5 = new JLabel(
        AnimalTranslator.translateMessage("animBGColorOption", null));
    l5.setPreferredSize(new Dimension(260, 30));
    l5.setFont(middleFont);
    gbc.gridwidth = 2;
    getContentPane().add(l5, gbc);
    initialColor = AnimalMainWindow.getWindowCoordinator().getAnimationWindow(true).getAnimationCanvas()
                         .getBackgroundColor();
    animationBackgroundColorChooser = new ColorChooserAction(this,
        ColorChoice.getColorName(initialColor), "animationBackground",
        AnimalTranslator.translateMessage("chooseColor",
          new Object[] { AnimalTranslator.translateMessage("animBGColor") }),
        initialColor);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    animationBackgroundButton = new ExtendedActionButton(
        animationBackgroundColorChooser, KeyEvent.VK_A);
    getContentPane().add(animationBackgroundButton, gbc);

    l6 = new JLabel(AnimalTranslator.translateMessage("fontOption", null));
    l6.setPreferredSize(new Dimension(260, 30));
    l6.setFont(middleFont);
    gbc.gridwidth = 2;
    getContentPane().add(l6, gbc);

    String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                        .getAvailableFontFamilyNames();

    // Toolkit.getDefaultToolkit().getFontList();
    fontName = new JComboBox<String>();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    getContentPane().add(fontName, gbc);

    for (int a = 0; a < fonts.length; a++)
      fontName.addItem(fonts[a]);

    l7 = new JLabel(AnimalTranslator.translateMessage("slideShowDelay", null));
    l7.setPreferredSize(new Dimension(260, 30));
    l7.setFont(middleFont);
    gbc.gridwidth = 2;
    getContentPane().add(l7, gbc);

    slideShowDelayBox = new JComboBox<String>();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    getContentPane().add(slideShowDelayBox, gbc);

    for (int a = 0; a < 1000; a += 100)
      slideShowDelayBox.addItem(String.valueOf(a));

    int delay = Animal.getSlideShowDelay();

    if ((delay > 1000) || (delay < 0) || ((delay % 100) != 0)) {
      slideShowDelayBox.addItem(String.valueOf(delay));
    }

    slideShowDelayBox.setSelectedItem(String.valueOf(delay));
    slideShowDelayBox.setEditable(true);

    gbc.gridwidth = 2;
    cbAutoload = new JCheckBox(AnimalTranslator.translateMessage("autoLoadOption",
          null));
    cbAutoload.setFont(middleFont);
    getContentPane().add(cbAutoload, gbc);

    gbc.gridwidth = GridBagConstraints.REMAINDER;
    saneFontSizeButton = new JButton(AnimalTranslator.translateMessage(
          "fixFontSizeOption", null));
    saneFontSizeButton.setMnemonic(KeyEvent.VK_F);
    saneFontSizeButton.addActionListener(this);
    getContentPane().add(saneFontSizeButton, gbc);
    
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    cbAutohide = new JCheckBox(AnimalTranslator.translateMessage("hideMenu", null));
    cbAutohide.setFont(middleFont);
    getContentPane().add(cbAutohide, gbc);
    
    
    JPanel p = new JPanel();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.CENTER;
    p.add(okButton = AnimalTranslator.getGUIBuilder().generateJButton("ok", null, false,
          this));
    p.add(cancelButton = AnimalTranslator.getGUIBuilder().generateJButton("cancel",
          null, false, this));
    p.add(applyButton = AnimalTranslator.getGUIBuilder().generateJButton("apply", null,
          false, this));
    getContentPane().add(p, gbc);

    // set all components according to Animal's settings
    if (drawWindow.getDrawCanvas().isPointGrid()) {
      rbPointGrid.setSelected(true);
    }
    else {
      rbLineGrid.setSelected(true);
    }

    fontName.setSelectedItem(animal.getAnimationFont());

    cbAutoload.setSelected(animal.isAutoloadLastFile());
    
    cbAutohide.setSelected(animal.isAutoHide());
    pack();

    addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          OptionDialog.this.windowClosing();
        }
      });
    dialog = this;
  }

  // =================================================================
  //                          STATIC METHODS
  // =================================================================

  /**
   * returns the currently active OptionDialog or null, if none such exists.
   *
   * @param parent the base Animal object
   * @return an instance of this OptionDialog. If this was null before, create
   * and store the new reference.
   */
  public static OptionDialog getOptionDialog(Animal parent) {
    if (dialog == null) {
      dialog = new OptionDialog(parent);
    }

    return dialog;
  }

  // =================================================================
  //                          EVENT HANDLING
  // =================================================================

  /**
   * reacts to buttons.
   *
   * @param e the ActionEvent
   */
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == applyButton) {
      applyChanges();
    }
    else if (e.getSource() == okButton) {
      applyChanges();
      windowClosing();
    }
    else if (e.getSource() == cancelButton) {
      windowClosing();
    }
    else if (e.getSource() == saneFontSizeButton) {
      animal.saneFontSizes();
    }
  }

  /**
   * Applies the component settings to Animal.
   */
  void applyChanges() {
    DrawCanvas drawCanvas = AnimalMainWindow.getWindowCoordinator().getDrawWindow(true).getDrawCanvas();
    drawCanvas.setPointGrid(rbPointGrid.isSelected());
    drawCanvas.repaintAll();
    animal.setAnimationFont((String) fontName.getSelectedItem());
    animal.setAutoloadLastFile(cbAutoload.isSelected());
    animal.setAutoHide(cbAutohide.isSelected());
    
    // set slide show delay...
    int delay = Animal.getSlideShowDelay();

    try {
      delay = Integer.parseInt((String) slideShowDelayBox.getSelectedItem());
    }
    catch (NumberFormatException e) {
      delay = 500;
    }

    Animal.setSlideShowDelay(delay);
    Animal.savePropertiesExtern();
  }

  /**
   * React to property changes - concerns color settings.
   *
   * @param event the PropertyChangeEvent storing the changed property
   */
  public void propertyChange(PropertyChangeEvent event) {
    String eventName = event.getPropertyName();

    DrawCanvas drawCanvas = AnimalMainWindow.getWindowCoordinator().getDrawWindow(true).getDrawCanvas();
    if (eventName.equals("gridColor")) {
        drawCanvas.setGridColor((Color) event.getNewValue());
    }
    else if (eventName.equals("backgroundColor")) {
        drawCanvas.setBackgroundColor((Color) event.getNewValue());
    }
    else if (eventName.equals("animationBackground")) {
      AnimalMainWindow.getWindowCoordinator().getAnimationWindow(true).getAnimationCanvas().setBackgroundColor((Color) event.getNewValue());
    }

    drawCanvas.repaintAll();
    AnimalMainWindow.getWindowCoordinator().getAnimationWindow(true).getAnimationCanvas().repaint();
    pack();
  }

  // =================================================================
  //                        WINDOW OPERATIONS
  // =================================================================

  /**
    * Toggle the visibility mode of the window
    *
    * @param showIt if <code>true</code>, show the window, else hide it
    */
  public void setVisible(boolean showIt) {
    if (!isVisible()) {
      setLocationRelativeTo(getParent());
    }

    super.setVisible(showIt);
  }

  /**
   * Close the window
   */
  void windowClosing() {
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
    Dimension labelDim = new Dimension(0, 0);

    if (l != null) {
      labelDim = l.getSize();
    }

    if (zoomIn) {
      if (defaultFont.getSize() < 24) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() + 2);
      }
      if (middleFont.getSize() < 24) {
        middleFont = new Font(middleFont.getFontName(), middleFont.getStyle(),
            middleFont.getSize() + 2);
      }

      if (bigFont.getSize() < 26) {
        bigFont = new Font(bigFont.getFontName(), bigFont.getStyle(),
            bigFont.getSize() + 2);
      }
      if (dim.getWidth() < 1000) {
        dim.setSize(dim.getWidth() + 60, dim.getHeight() + 20);
      }
    } else {
      if (defaultFont.getSize() > 10) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() - 2);
      }
      if (middleFont.getSize() > 10) {
        middleFont = new Font(middleFont.getFontName(), middleFont.getStyle(),
            middleFont.getSize() - 2);
      }

      if (bigFont.getSize() > 12) {
        bigFont = new Font(bigFont.getFontName(), bigFont.getStyle(),
            bigFont.getSize() - 2);
      }
      if (dim.getWidth() > 340) {
        dim.setSize(dim.getWidth() - 60, dim.getHeight() - 20);
      }


    }

    if (applyButton != null) {
      applyButton.setFont(defaultFont);
    }

    if (cancelButton != null) {
      cancelButton.setFont(defaultFont);
    }

    if (cbAutoload != null) {
      cbAutoload.setFont(defaultFont);
    }

    if (fontName != null) {
      fontName.setFont(defaultFont);
    }

    if (okButton != null) {
      okButton.setFont(defaultFont);
    }

    if (rbLineGrid != null) {
      rbLineGrid.setFont(defaultFont);
    }

    if (rbPointGrid != null) {
      rbPointGrid.setFont(defaultFont);
    }

    if (saneFontSizeButton != null) {
      saneFontSizeButton.setFont(defaultFont);
    }

    if (slideShowDelayBox != null) {
      slideShowDelayBox.setFont(defaultFont);
    }

    if (l != null) {
      l.setFont(bigFont);
    }
    if (l2 != null) {
      l2.setFont(middleFont);
    }

    if (l3 != null) {
      l3.setFont(middleFont);
    }

    if (l4 != null) {
      l4.setFont(middleFont);
    }

    if (l5 != null) {
      l5.setFont(middleFont);
    }

    if (l6 != null) {
      l6.setFont(middleFont);
    }

    if (l7 != null) {
      l7.setFont(middleFont);
      // l7.setSize(labelDim);
      // l7.setPreferredSize(labelDim);
    }
    
    if(animationBackgroundColorChooser != null) {
      animationBackgroundColorChooser.zoom(zoomIn);
    }
    if (animationBackgroundButton != null) {
      animationBackgroundButton.setFont(defaultFont);
    }

    if(backgroundColorChooser != null) {
      backgroundColorChooser.zoom(zoomIn);
    }
    if (backgroundColorButton != null) {
      backgroundColorButton.setFont(defaultFont);
    }

    if(gridColorChooser != null) {
      gridColorChooser.zoom(zoomIn);
    }
    if (gridColorButton != null) {
      gridColorButton.setFont(defaultFont);
    }

    this.setSize(dim);

  }
} // OptionDialog.java


