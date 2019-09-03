package translator;

import generators.framework.components.ArrayInputTable;
import generators.framework.components.ColorChooserComboBox;
import generators.framework.components.FontChooserComboBox;
import generators.framework.components.IntegerTextField;
import generators.framework.components.IntegerTextFieldEx;
import generators.framework.components.MatrixInputTable;

import java.awt.Color;
import java.awt.Component;
import java.awt.MediaTracker;
import java.net.URL;

import javax.swing.ImageIcon;

import animal.misc.MessageDisplay;

/**
 * Provides a common interface for translatable GUI element generation Requires
 * an appropriate resource file containing the message translations.
 * 
 * @version 1.1 2000-01-11
 * @author Guido R&ouml;&szlig;ling (<a href="mailto:roessling@acm.org">
 *         roessling@acm.org</a>)
 */
public class AnimalSpecificTranslatableGUIElement extends
    TranslatableGUIElement {

  /**
   * Generate a new GUI generator using the concrete Translator passed in
   * 
   * @param t
   *          the current Translator for this object
   */
  public AnimalSpecificTranslatableGUIElement(Translator t) {
    super(t);
  }

  /**
   * Method for generating a new ArrayInputTable, with an empty Constructor.
   * 
   * @param key
   *          The Key for the new ArrayInputTable.
   * @return A new ArrayInputTable-Object.
   */
  public ArrayInputTable generateArrayInputTable(String key) {
    ArrayInputTable internalArrayInputTable = new ArrayInputTable();
    internalArrayInputTable.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    internalArrayInputTable.setLocalHeaderString(getTranslator()
        .translateMessage("element"));
    registerComponent(key, internalArrayInputTable);
    return internalArrayInputTable;
  }

  /**
   * Method for generating a new ArrayInputTable.
   * 
   * @param key
   *          The Key for the new ArrayInputTable.
   * @param numElements
   *          The number of elements in the table.
   * @return A new ArrayInputTable-Object.
   */
  public ArrayInputTable generateArrayInputTable(String key, int numElements) {
    ArrayInputTable internalArrayInputTable = new ArrayInputTable(numElements);
    internalArrayInputTable.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    internalArrayInputTable.setLocalHeaderString(getTranslator()
        .translateMessage("element"));
    registerComponent(key, internalArrayInputTable);
    return internalArrayInputTable;
  }

  /**
   * Method for generating a new ArrayInputTable, with passed int values.
   * 
   * @param key
   *          The Key for the new ArrayInputTable.
   * @param newValues
   *          The int-values to use in the table.
   * @return A new ArrayInputTable-Object.
   */
  public ArrayInputTable generateArrayInputTable(String key, int[] newValues) {
    ArrayInputTable internalArrayInputTable = new ArrayInputTable(newValues);
    internalArrayInputTable.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    internalArrayInputTable.setLocalHeaderString(getTranslator()
        .translateMessage("element"));
    registerComponent(key, internalArrayInputTable);
    return internalArrayInputTable;
  }

  /**
   * Method for generating a new ArrayInputTable, with passed String values.
   * 
   * @param key
   *          The Key for the new ArrayInputTable.
   * @param newValues
   *          The String-values to use in the table.
   * @return A new ArrayInputTable-Object.
   */
  public ArrayInputTable generateArrayInputTable(String key, String[] newValues) {
    ArrayInputTable internalArrayInputTable = new ArrayInputTable(newValues);
    internalArrayInputTable.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    internalArrayInputTable.setLocalHeaderString(getTranslator()
        .translateMessage("element"));
    registerComponent(key, internalArrayInputTable);
    return internalArrayInputTable;
  }

  /**
   * Method for generating a new ColorChooserComboBox, with an empty
   * Constructor.
   * 
   * @param key
   *          The Key for the new ColorChooserComboBox.
   * @return A new ColorChooserComboBox-Object.
   */
  public ColorChooserComboBox generateColorChooserComboBox(String key) {
    ColorChooserComboBox internalComboBox = new ColorChooserComboBox();

    // translate all the Strings for this component
    String[] strKeys = internalComboBox.getTranslatorKeys();
    String[] strLocal = new String[strKeys.length];
    for (int i = 0; i < strKeys.length; i++) {
      strLocal[i] = getTranslator().translateMessage(strKeys[i]);
    }
    internalComboBox.setLocalStrings(strLocal);

    internalComboBox.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    registerComponent(key, internalComboBox);
    return internalComboBox;
  }

  /**
   * Method for generating a new ColorChooserComboBox and setting the selected
   * Color.
   * 
   * @param key
   *          The Key for the new ColorChooserComboBox.
   * @param colorSelected
   *          The Color that should be selected (as a Color).
   * @return A new ColorChooserComboBox-Object.
   */
  public ColorChooserComboBox generateColorChooserComboBox(String key,
      Color colorSelected) {
    ColorChooserComboBox internalComboBox = new ColorChooserComboBox(
        colorSelected);

    // translate all the Strings for this component
    String[] strKeys = internalComboBox.getTranslatorKeys();
    String[] strLocal = new String[strKeys.length];
    for (int i = 0; i < strKeys.length; i++) {
      strLocal[i] = getTranslator().translateMessage(strKeys[i]);
    }
    internalComboBox.setLocalStrings(strLocal);

    internalComboBox.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    registerComponent(key, internalComboBox);
    return internalComboBox;
  }

  /**
   * Method for generating a new ColorChooserComboBox and setting the selected
   * Color.
   * 
   * @param key
   *          The Key for the new ColorChooserComboBox.
   * @param strSelected
   *          The Color that should be selected (as an Animal-Color-String).
   * @return A new ColorChooserComboBox-Object.
   */
  public ColorChooserComboBox generateColorChooserComboBox(String key,
      String strSelected) {
    ColorChooserComboBox internalComboBox = new ColorChooserComboBox(
        strSelected);

    // translate all the Strings for this component
    String[] strKeys = internalComboBox.getTranslatorKeys();
    String[] strLocal = new String[strKeys.length];
    for (int i = 0; i < strKeys.length; i++) {
      strLocal[i] = getTranslator().translateMessage(strKeys[i]);
    }
    internalComboBox.setLocalStrings(strLocal);

    internalComboBox.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    registerComponent(key, internalComboBox);
    return internalComboBox;
  }

  /**
   * Method for generating a new FontChooserComboBox, with an empty Constructor.
   * 
   * @param key
   *          The Key for the new FontChooserComboBox.
   * @return A new FontChooserComboBox-Object.
   */
  public FontChooserComboBox generateFontChooserComboBox(String key) {
    FontChooserComboBox internalComboBox = new FontChooserComboBox();
    internalComboBox.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    registerComponent(key, internalComboBox);
    return internalComboBox;
  }

  /**
   * Method for generating a new FontChooserComboBox, and setting the default
   * Font.
   * 
   * @param key
   *          The Key for the new FontChooserComboBox.
   * @param selected
   *          The default Font. Can be "Serif", "SansSerif" and "Monospaced".
   * @return A new FontChooserComboBox-Object.
   */
  public FontChooserComboBox generateFontChooserComboBox(String key,
      String selected) {
    FontChooserComboBox internalComboBox = new FontChooserComboBox(selected);
    internalComboBox.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    registerComponent(key, internalComboBox);
    return internalComboBox;
  }

  /**
   * Method for generating a new IntegerTextField, with an empty Constructor.
   * 
   * @param key
   *          The Key for the new IntegerTextField.
   * @return A new IntegerTextField-Object.
   */
  public IntegerTextField generateIntegerTextField(String key) {
    IntegerTextField internalIntegerTextField = new IntegerTextField();
    internalIntegerTextField.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    registerComponent(key, internalIntegerTextField);
    return internalIntegerTextField;
  }

  /**
   * Method for generating a new IntegerTextField, and setting the default Text.
   * 
   * @param key
   *          The Key for the new IntegerTextField.
   * @param text
   *          The Text that should be displayed.
   * @return A new IntegerTextField-Object.
   */
  public IntegerTextField generateIntegerTextField(String key, String text) {
    IntegerTextField internalIntegerTextField = new IntegerTextField(text);
    internalIntegerTextField.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    registerComponent(key, internalIntegerTextField);
    return internalIntegerTextField;
  }

  /**
   * Method for generating a new IntegerTextFieldEx, with an empty Constructor.
   * 
   * @param key
   *          The Key for the new IntegerTextFieldEx.
   * @return A new IntegerTextFieldEx-Object.
   */
  public IntegerTextFieldEx generateIntegerTextFieldEx(String key) {
    IntegerTextFieldEx internalIntegerTextField = new IntegerTextFieldEx();
    internalIntegerTextField.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    registerComponent(key, internalIntegerTextField);
    return internalIntegerTextField;
  }

  /**
   * Method for generating a new IntegerTextFieldEx, and setting the default
   * Text.
   * 
   * @param key
   *          The Key for the new IntegerTextFieldEx.
   * @param text
   *          The Text that should be displayed.
   * @return A new IntegerTextFieldEx-Object.
   */
  public IntegerTextFieldEx generateIntegerTextFieldEx(String key, String text) {
    IntegerTextFieldEx internalIntegerTextField = new IntegerTextFieldEx(text);
    internalIntegerTextField.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    registerComponent(key, internalIntegerTextField);
    return internalIntegerTextField;
  }

  /**
   * Method for generating a new MatrixInputTable with an empty Constructor.
   * 
   * @param key
   *          The Key for the new ArrayInputTable.
   * @return A new MatrixInputTable-Object.
   */
  public MatrixInputTable generateMatrixInputTable(String key) {
    MatrixInputTable internalMatrixInputTable = new MatrixInputTable();
    internalMatrixInputTable.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    internalMatrixInputTable.setLocalHeaderString(getTranslator()
        .translateMessage("column"));
    registerComponent(key, internalMatrixInputTable);
    return internalMatrixInputTable;
  }

  /**
   * Method for generating a new MatrixInputTable.
   * 
   * @param key
   *          The Key for the new ArrayInputTable.
   * @param numRows
   *          The number of displayed Rows.
   * @param numColumns
   *          The number of displayed Columns.
   * @return A new MatrixInputTable-Object.
   */
  public MatrixInputTable generateMatrixInputTable(String key, int numRows,
      int numColumns) {
    MatrixInputTable internalMatrixInputTable = new MatrixInputTable(numRows,
        numColumns);
    internalMatrixInputTable.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    internalMatrixInputTable.setLocalHeaderString(getTranslator()
        .translateMessage("column"));
    registerComponent(key, internalMatrixInputTable);
    return internalMatrixInputTable;
  }

  /**
   * Method for generating a new MatrixInputTable, with passed int values.
   * 
   * @param key
   *          The Key for the new ArrayInputTable.
   * @param newValues
   *          The int-values to use in the table.
   * @return A new MatrixInputTable-Object.
   */
  public MatrixInputTable generateMatrixInputTable(String key, int[][] newValues) {
    MatrixInputTable internalMatrixInputTable = new MatrixInputTable(newValues);
    internalMatrixInputTable.setToolTipText(getTranslator().translateMessage(
        key + ".toolTipText"));
    internalMatrixInputTable.setLocalHeaderString(getTranslator()
        .translateMessage("column"));
    registerComponent(key, internalMatrixInputTable);
    return internalMatrixInputTable;
  }

  /**
   * returns the imageIcon with the given name.
   * 
   * @return <b>null</b> if the Icon could not be found or read, <br>
   *         the Icon otherwise.
   */
  public ImageIcon getImageIcon(String name) {
    return ResourceLocator.getResourceLocator().getImageIcon(name);
  }

  protected void updateComponent(String key, Component component) {
    super.updateComponent(key, component);
    if (component != null) {
      if (component instanceof ArrayInputTable) {
        ArrayInputTable ait = (ArrayInputTable) component;
        // translate the header of each column
        ait.setLocalHeaderString(getTranslator().translateMessage("element"));
        ait.setToolTipText(getTranslator().translateMessage(
            key + ".toolTipText"));
      } else if (component instanceof ColorChooserComboBox) {
        ColorChooserComboBox cc = (ColorChooserComboBox) component;
        // translate all the Strings for this component
        String[] strKeys = cc.getTranslatorKeys();
        String[] strLocal = new String[strKeys.length];
        for (int i = 0; i < strKeys.length; i++) {
          strLocal[i] = getTranslator().translateMessage(strKeys[i]);
        }
        cc.setLocalStrings(strLocal);
        cc.setToolTipText(getTranslator()
            .translateMessage(key + ".toolTipText"));
      } else if (component instanceof MatrixInputTable) {
        MatrixInputTable mit = (MatrixInputTable) component;
        // translate the header of each column
        mit.setLocalHeaderString(getTranslator().translateMessage("column"));
        mit.setToolTipText(getTranslator().translateMessage(
            key + ".toolTipText"));
      }
    }
  }
}
