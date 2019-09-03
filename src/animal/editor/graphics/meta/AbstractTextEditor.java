/*
 * Created on 25.04.2007 by Guido Roessling (roessling@acm.org>
 */
package animal.editor.graphics.meta;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultEditorKit;

import translator.AnimalTranslator;
import translator.ExtendedActionButton;
import translator.TranslatableGUIElement;
import animal.editor.Editor;
import animal.graphics.PTArc;
import animal.graphics.PTText;
import animal.graphics.meta.ImmediateTextContainer;
import animal.graphics.meta.PTArray;
import animal.graphics.meta.TextContainer;
import animal.main.Animal;
import animal.main.Animation;
import animal.misc.ColorChooserAction;
import animal.misc.TextUtilities;
import animal.misc.XProperties;

/**
 * support for text editors
 * 
 * @author roessling
 * @version 1.1 2008-06-23
 */
public abstract class AbstractTextEditor extends GraphicEditor implements
    ActionListener {

  /**
	 * 
	 */
  private static final long    serialVersionUID = -3830670048284912742L;

  protected JTextField         textField;

  protected JComboBox<String>  fontName;

  protected JComboBox<String>  fontSize;

  protected JCheckBox          bold;

  protected JCheckBox          italic;

  protected ColorChooserAction textColorChooser;

  public Box generateTextOperationsBox(TranslatableGUIElement generator,
      String borderKey) {
    Box textEntryBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
        borderKey);

    // add a text input field
    textField = new JTextField(16);
    textField.addActionListener(this);
    textEntryBox.add(textField);

    // add a box with cut, copy, paste buttons
    Box textEditBox = new Box(BoxLayout.LINE_AXIS);
    Action theAction = TextUtilities
        .findTextFieldAction(DefaultEditorKit.cutAction);
    textEditBox.add(generator.generateActionButton("AbstractTextEditor.cut",
        null, theAction));

    theAction = TextUtilities.findTextFieldAction(DefaultEditorKit.copyAction);
    textEditBox.add(generator.generateActionButton("AbstractTextEditor.copy",
        null, theAction));

    theAction = TextUtilities.findTextFieldAction(DefaultEditorKit.pasteAction);
    textEditBox.add(generator.generateActionButton("AbstractTextEditor.paste",
        null, theAction));
    textEntryBox.add(textEditBox);

    return textEntryBox;
  }

  public void createTextOperationsChooser(String borderKey) {

    // insert the properly labeled separator
    insertSeparator(borderKey, cp, generator);

    // add a text input field
    textField = new JTextField(16);
    textField.addActionListener(this);
    cp.add(textField, Editor.LAYOUT_PARAGRAPH_GAP_GROWX_SPAN_WRAP);

    // add the cut, copy, paste buttons
    Action theAction = TextUtilities
        .findTextFieldAction(DefaultEditorKit.cutAction);
    cp.add(generator.generateActionButton("AbstractTextEditor.cut", null,
        theAction), Editor.LAYOUT_PARAGRAPH_GAP);
    
    theAction = TextUtilities.findTextFieldAction(DefaultEditorKit.copyAction);
    cp.add(generator.generateActionButton("AbstractTextEditor.copy", null,
        theAction), Editor.LAYOUT_PARAGRAPH_GAP);

    theAction = TextUtilities.findTextFieldAction(DefaultEditorKit.pasteAction);
    cp.add(generator.generateActionButton("AbstractTextEditor.paste", null,
        theAction), Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
  }

  public void createTextComponentChooser(Color initialColor, String colorName) {

    // make sure cp, generator are set properly!
    initializeLayoutComponents();

    // add a border
    //insertSeparator("AbstractTextEditor.textCompBL", cp, generator);

    // first, add text entry and cut/copy/paste
    createTextOperationsChooser("AbstractTextEditor.textBL");

    // add font, size, bold/italics selection
    createFontAndStyleChooser("AbstractTextEditor.fontBL");

    // add color choice
    insertSeparator("textColorBL", cp, generator);

    // create the color label
    JLabel colorLabel = generator.generateJLabel("GenericEditor.colorLabel");
    cp.add(colorLabel, Editor.LAYOUT_PARAGRAPH_GAP);

    textColorChooser = createColorChooser(colorName,
        "GenericEditor.chooseColor", "AbstractTextEditor.textColor",
        initialColor);
    ExtendedActionButton colorButton = new ExtendedActionButton(
        textColorChooser, KeyEvent.VK_T);
    cp.add(colorButton, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);

    // // second row: polygon fill color
    // // create the color label
    // JLabel fillColorLabel =
    // generator.generateJLabel("GenericEditor.fillColorLabel");
    // cp.add(fillColorLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    //
    // initialColor = (getCurrentObject(false) == null) ? Color.black
    // : ((FillablePrimitive)getCurrentObject(false)).getFillColor();
    // fillColorChooser = createColorChooser("fillColor",
    // "GenericEditor.chooseColor",
    // "GenericEditor.fillColor", initialColor);
    // fillColorChooserButton = new ExtendedActionButton(fillColorChooser,
    // KeyEvent.VK_F);
    // cp.add(fillColorChooserButton, Editor.LAYOUT_PARAGRAPH_GAP);

    // Box textBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
    // "AbstractTextEditor.textCompBL");
    // OK like this?

    // textBox.add(generateTextOperationsBox(generator,
    // "AbstractTextEditor.textBL"));
    // now, add font, size, bold/italics selection
    // textBox.add(generateFontAndStyleBox(generator,
    // "AbstractTextEditor.fontBL"));

    // finally, add text color choice
    // Box textColorBox = generator.generateBorderedBox(BoxLayout.LINE_AXIS,
    // "textColorBL");
    // textColorBox.add(generator.generateJLabel("GenericEditor.colorLabel"));
    // Color initialColor = (getCurrentObject(false) == null) ? Color.black
    // : ((PTArc) getCurrentObject()).getTextComponent().getColor();
    // textColorChooser = createColorChooser("textColor",
    // "AbstractTextEditor.textColor", initialColor);
    // textColorBox.add(new ExtendedActionButton(textColorChooser,
    // KeyEvent.VK_T));
    // textBox.add(textColorBox);
    // return textBox;
  }

  public Box generateTextComponentBox(TranslatableGUIElement generator) {
    Box textBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
        "AbstractTextEditor.textCompBL");
    // OK like this?

    // first, add text entry and cut/copy/paste
    textBox.add(generateTextOperationsBox(generator,
        "AbstractTextEditor.textBL"));
    // now, add font, size, bold/italics selection
    textBox
        .add(generateFontAndStyleBox(generator, "AbstractTextEditor.fontBL"));

    // finally, add text color choice
    Box textColorBox = generator.generateBorderedBox(BoxLayout.LINE_AXIS,
        "textColorBL");
    textColorBox.add(generator.generateJLabel("GenericEditor.colorLabel"));
    Color initialColor = (getCurrentObject(false) == null) ? Color.black
        : ((PTArc) getCurrentObject()).getTextComponent().getColor();
    textColorChooser = createColorChooser("textColor",
        "AbstractTextEditor.textColor", initialColor);
    textColorBox.add(new ExtendedActionButton(textColorChooser, KeyEvent.VK_T));
    textBox.add(textColorBox);
    return textBox;
  }

  protected JComboBox<String> getSupportedFontsChooser() {
    String[] fonts = Animal.GLOBAL_FONTS; // Toolkit.getDefaultToolkit().getFontList();
    fontName = new JComboBox<String>();
    for (int j = 0; j < fonts.length; j++)
      fontName.addItem(fonts[j]);
    return fontName;
  }

  protected JComboBox<String> getSupportedFontSizeChooser() {
    fontSize = new JComboBox<String>();
    fontSize.setEditable(true);
    fontSize.addItem("8");
    fontSize.addItem("10");
    fontSize.addItem("12");
    fontSize.addItem("14");
    fontSize.addItem("16");
    fontSize.addItem("24");
    fontSize.setSelectedItem("12");
    return fontSize;
  }

  public void createFontAndStyleChooser(String borderKey) {
    insertSeparator(borderKey, cp, generator);
    TextContainer text = (TextContainer)getCurrentObject(false);
    Font f = null;
    if (text == null)
      text = new PTText();
    f = text.getFont();
//    System.err.println("f: " +f);

    // add a box for choosing the font
    JLabel colorLabel = generator.generateJLabel("GenericEditor.nameLabel");
    cp.add(colorLabel, Editor.LAYOUT_PARAGRAPH_GAP);

    fontName = getSupportedFontsChooser();
    fontName.setSelectedItem(f.getName());
    fontName.addActionListener(this);
    cp.add(fontName, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    //cp.add(fontName, Editor.LAYOUT_PARAGRAPH_GAP_GROWX_SPAN_WRAP);

    // add a font size choice box
    cp.add(generator.generateJLabel("AbstractTextEditor.fontSizeLabel"),
        Editor.LAYOUT_PARAGRAPH_GAP);

    fontSize = getSupportedFontSizeChooser();
    fontSize.setSelectedItem(String.valueOf(f.getSize()));
    fontSize.addActionListener(this);
    cp.add(fontSize, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    //cp.add(fontSize, Editor.LAYOUT_PARAGRAPH_GAP_GROWX_SPAN_WRAP);

    // add a font style choice box (bold? italic?)
    cp.add(generator.generateJLabel("AbstractTextEditor.fontStyleLabel"),
        Editor.LAYOUT_PARAGRAPH_GAP);

    italic = generator.generateJCheckBox("italic", null, null);
    italic.setSelected(f.isItalic());
    italic.addActionListener(this);
//    italic.setHorizontalAlignment(SwingConstants.CENTER);
    cp.add(italic, Editor.LAYOUT_PARAGRAPH_GAP);

    bold = generator.generateJCheckBox("bold", null, null);
    bold.setSelected(f.isBold());
    bold.addActionListener(this);
//    bold.setHorizontalAlignment(SwingConstants.CENTER);
    cp.add(bold, Editor.LAYOUT_WRAP);
//    System.err.println(" - italic? " +italic.isSelected() + ", bold? " + bold.isSelected());
  }
  
  public void createFontAndStyleChooserActionListener(String borderKey) {
    insertSeparator(borderKey, cp, generator);
    TextContainer text = (TextContainer)getCurrentObject(false);
    Font f = null;
    if (text == null)
      text = new PTText();
    f = text.getFont();
//    System.err.println("f: " +f);

    // add a box for choosing the font
    JLabel colorLabel = generator.generateJLabel("GenericEditor.nameLabel");
    cp.add(colorLabel, Editor.LAYOUT_PARAGRAPH_GAP);

    fontName = getSupportedFontsChooser();
    fontName.setSelectedItem(f.getName());
    fontName.addActionListener(this);
    cp.add(fontName, Editor.LAYOUT_PARAGRAPH_GAP_GROWX_SPAN_WRAP);

    // add a font size choice box
    cp.add(generator.generateJLabel("AbstractTextEditor.fontSizeLabel"),
        Editor.LAYOUT_PARAGRAPH_GAP);

    fontSize = getSupportedFontSizeChooser();
    fontSize.setSelectedItem(String.valueOf(f.getSize()));
    fontSize.addActionListener(this);
    cp.add(fontSize, Editor.LAYOUT_PARAGRAPH_GAP_GROWX_SPAN_WRAP);

    // add a font style choice box (bold? italic?)
    cp.add(generator.generateJLabel("AbstractTextEditor.fontStyleLabel"),
        Editor.LAYOUT_PARAGRAPH_GAP);

    italic = generator.generateJCheckBox("italic", null, this);
    italic.setSelected(f.isItalic());
   // italic.addActionListener(this);
//    italic.setHorizontalAlignment(SwingConstants.CENTER);
    cp.add(italic, Editor.LAYOUT_PARAGRAPH_GAP);

    bold = generator.generateJCheckBox("bold", null, this);
    bold.setSelected(f.isBold());
   /// bold.addActionListener(this);
//    bold.setHorizontalAlignment(SwingConstants.CENTER);
    cp.add(bold, Editor.LAYOUT_WRAP);
//    System.err.println(" - italic? " +italic.isSelected() + ", bold? " + bold.isSelected());
  }
 
  
  
  public Box generateFontAndStyleBox(TranslatableGUIElement generator,
      String borderKey) {
    Box fontBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS, borderKey);

    // add a box for choosing the font
    Box fontNameBox = new Box(BoxLayout.LINE_AXIS);
    fontNameBox.add(AnimalTranslator.getGUIBuilder().generateJLabel(
        "GenericEditor.nameLabel"));
    String[] fonts = Animal.GLOBAL_FONTS; // Toolkit.getDefaultToolkit().getFontList();
    fontName = new JComboBox<String>();
    for (int j = 0; j < fonts.length; j++)
      fontName.addItem(fonts[j]);
    fontName.addActionListener(this);
    fontNameBox.add(fontName);
    fontBox.add(fontNameBox);

    // add a font size choice box
    Box fontSizeBox = new Box(BoxLayout.LINE_AXIS);
    fontSizeBox.add(generator
        .generateJLabel("AbstractTextEditor.fontSizeLabel"));
    fontSize = new JComboBox<String>();
    fontSize.setEditable(true);
    fontSize.addItem("8");
    fontSize.addItem("10");
    fontSize.addItem("12");
    fontSize.addItem("14");
    fontSize.addItem("16");
    fontSize.addItem("24");
    fontSize.setSelectedItem("12");
    fontSize.addActionListener(this);
    fontSizeBox.add(fontSize);
    fontBox.add(fontSizeBox);

    // add a font style choice box (bold? italic?)
    Box fontTypeBox = new Box(BoxLayout.LINE_AXIS);

    fontTypeBox.add(generator
        .generateJLabel("AbstractTextEditor.fontStyleLabel"));
    italic = generator.generateJCheckBox("italic", null, this);
    italic.setHorizontalAlignment(SwingConstants.CENTER);
    fontTypeBox.add(italic);

    bold = generator.generateJCheckBox("bold", null, this);
    bold.setHorizontalAlignment(SwingConstants.CENTER);
    fontTypeBox.add(bold);
    fontBox.add(fontTypeBox);

    return fontBox;
  }

  /**
   * extracts the Font from the components' settings.
   */
  protected Font storeFont() {
    String name = (String) fontName.getSelectedItem();
    String size = (String) fontSize.getSelectedItem();
    return new Font(name, Font.PLAIN + (bold.isSelected() ? Font.BOLD : 0)
        + (italic.isSelected() ? Font.ITALIC : 0), getInt(size, 12));
  }

  /**
   * sets the components according to the font.
   */
  protected void extractFont(Font f) {
    fontName.setSelectedItem(f.getName());
    fontSize.setSelectedItem(String.valueOf(f.getSize()));
    italic.setSelected(f.isItalic());
    bold.setSelected(f.isBold());
  }

  /**
   * writes the Editor's current permanent attributes to the Properties object.
   */
  public void getProperties(XProperties props) {
    super.getProperties(props);
    String baseKey = getBasicType();
    props.put(baseKey + ".bold", bold.isSelected());
    props.put(baseKey + ".italic", italic.isSelected());
    props.put(baseKey + ".fontName", fontName.getSelectedItem());
    props.put(baseKey + ".fontSize", fontSize.getSelectedItem());
    props.put(baseKey + ".text", textField.getText());
  }

  /**
   * extract the properties from <em>props</em>. All components are set
   * according to these properties.
   */
  public void setProperties(XProperties props) {
    super.setProperties(props);
    String baseKey = getBasicType();
    bold.setSelected(props.getBoolProperty(baseKey + ".bold", false));
    italic.setSelected(props.getBoolProperty(baseKey + ".italic", false));
    fontName.setSelectedItem(props.getProperty(baseKey + ".fontName",
        "SansSerif"));
    fontSize.setSelectedItem(props.getProperty(baseKey + ".fontSize", "12"));
    if (textField != null)
      textField.setText(props.getProperty(baseKey + ".text", ""));
  }
  

  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);
    // System.err.println(e.getSource());

    TextContainer t = (TextContainer) getCurrentObject();

    boolean setToItalic = italic.isSelected();
    boolean setToBold = bold.isSelected();
//    boolean isItalic = t.getFont().isItalic();
//    boolean isBold = t.getFont().isBold();

    if (e.getSource() == textField && t instanceof ImmediateTextContainer) {
      // if (e.getSource() == textField && t instanceof ImmediateTextContainer)
      ((ImmediateTextContainer) t).setText(textField.getText());
      System.err.println("updated text to "
          + ((ImmediateTextContainer) t).getText());
    }
    if (e.getSource() == fontName || e.getSource() == fontSize)
      t.setFont(storeFont());

    if (e.getSource() == italic || e.getSource() == bold) {
      // isItalic = !isItalic;
      String name = (String) fontName.getSelectedItem();
      String size = (String) fontSize.getSelectedItem();
      Font newFont = new Font(name, Font.PLAIN + (setToBold ? Font.BOLD : 0)
          + (setToItalic ? Font.ITALIC : 0), getInt(size, 12));
      t.setFont(newFont);
    }

    // if (e.getSource() == bold) {
    // isBold = !isBold;
    // String name = (String) fontName.getSelectedItem();
    // String size = (String) fontSize.getSelectedItem();
    // Font newFont = new Font(name, Font.PLAIN
    // + (bold.isSelected() ? Font.BOLD : 0)
    // + (italic.isSelected() ? Font.ITALIC : 0), getInt(size, 12));
    // t.setFont(newFont);
    // }

    repaintNow();
    if (Animation.get() != null)
      Animation.get().doChange();
  }

}
