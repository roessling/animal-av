/**
 * Editor for SetFont
 * @see animal.main.animator.SetFont
 *
 * Created on 16. December 2006, 0:05
 *
 * @author MichaelSchmitt
 * @version 0.2.1a
 * @date 2006-02-15
 */

package animal.editor.animators;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import translator.AnimalTranslator;
import translator.TranslatableGUIElement;
import animal.animator.SetFont;
import animal.editor.Editor;
import animal.main.Animal;
import animal.misc.EditableObject;

public class SetFontEditor extends TimedAnimatorEditor {

  /**
   * 
   */
  private static final long serialVersionUID = -2832505946091287650L;

  /**
   * Make the highlight panel scrollable if it contains too many elements.
   */
  private JScrollPane scp;

  private JComboBox<String> fontName;

  private JComboBox<String> fontSize;

  private JCheckBox isItalicBox;

  private JCheckBox isBoldBox;

  private boolean isItalic, isBold;

  /**
   * Create a new SwapEditor
   */
  public SetFontEditor() {
    super();
  }

  protected void buildGUI() {
    super.buildGUI();
    TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
    JTabbedPane tp = new JTabbedPane();
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    JPanel fontPanel = new JPanel(gbl);

    fontPanel.add(generator.generateJLabel("GenericEditor.nameLabel"),
        gbc);
    String[] fonts = Animal.GLOBAL_FONTS; // Toolkit.getDefaultToolkit().getFontList();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    fontName = new JComboBox<String>();
    fontPanel.add(fontName, gbc);
    for (int j = 0; j < fonts.length; j++)
      fontName.addItem(fonts[j]);
    fontName.addActionListener(this);
    gbc.gridwidth = 1;

    // JLabel fontSizeLabel = new JLabel("Size: ");
    // fontPanel.add(fontSizeLabel, gbc);
    fontPanel.add(generator.generateJLabel("AbstractTextEditor.fontSizeLabel"),
        gbc);
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    fontSize = new JComboBox<String>();
    fontPanel.add(fontSize, gbc);
    fontSize.setEditable(true);
    // add the standard font sizes
    fontSize.addItem("8");
    fontSize.addItem("10");
    fontSize.addItem("12");
    fontSize.addItem("14");
    fontSize.addItem("16");
    fontSize.addItem("24");
    fontSize.setSelectedItem("12");
    fontSize.addActionListener(this);
    gbc.gridwidth = 1;

    // JLabel fontStyleLabel = new JLabel("Style: ");
    // fontPanel.add(fontStyleLabel, gbc);
    fontPanel.add(generator.generateJLabel("AbstractTextEditor.fontStyleLabel"), gbc);
    isItalicBox = generator.generateJCheckBox("italic", null,
        this);
    isItalicBox.setHorizontalAlignment(SwingConstants.CENTER);
    fontPanel.add(isItalicBox, gbc);
    // italic.addActionListener(this);

    // bold = new JCheckBox("bold");
    isBoldBox = generator.generateJCheckBox("bold", null,
        this);
    isBoldBox.setHorizontalAlignment(SwingConstants.CENTER);
    fontPanel.add(isBoldBox, gbc);
    // bold.addActionListener(this);

    generator.insertTranslatableTab("SetFontEditor.font", null,
        fontPanel, tp);
    scp = new JScrollPane(fontPanel);
    addLayer(scp);
    finish();
  }

  /**
   * sets the components according to the font.
   */
  void extractFont(Font f) {
    fontName.setSelectedItem(f.getName());
    fontSize.setSelectedItem(String.valueOf(f.getSize()));
    isItalicBox.setSelected(f.isItalic());
    isBoldBox.setSelected(f.isBold());
  }

  /**
   * Extract the attributes from the chosen object and adjust the corresponding
   * editor values
   * 
   * @param eo
   *          the object to be modified
   */
  public void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    Font f = ((SetFont) eo).getValue();
    isItalic = f.isItalic();
    isBold = f.isBold();
    isItalicBox.setSelected(isItalic);
    isBoldBox.setSelected(isBold);
    fontName.setSelectedItem(f.getName());
    fontSize.setSelectedItem(String.valueOf(f.getSize()));
  }

  /**
   * Set the attributes of the chosen object to the values of the editor
   * 
   * @param eo
   *          the object to be modified
   */
  public void storeAttributesInto(EditableObject eo) {
    super.storeAttributesInto(eo);
    SetFont setFont = (SetFont) eo;
    setFont.setValue(storeFont());
  }

  /**
   * extracts the Font from the components' settings.
   */
  Font storeFont() {
    String name = (String) fontName.getSelectedItem();
    String size = (String) fontSize.getSelectedItem();
    isItalic = isItalicBox.isSelected();
    isBold = isBoldBox.isSelected();
    return new Font(name, Font.PLAIN + (isBold ? Font.BOLD : 0)
        + (isItalic ? Font.ITALIC : 0), getInt(size, 12));
  }

  /**
   * Open a secondary SwapEditor window
   * 
   * @param eo
   *          the object from which to take the current settings
   */
  public Editor getSecondaryEditor(EditableObject eo) {
    SetFontEditor result = new SetFontEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  /**
   * Create a new swap animation with the current settings
   */
  public EditableObject createObject() {
    SetFont h = new SetFont();
    storeAttributesInto(h);
    return h;
  }
}