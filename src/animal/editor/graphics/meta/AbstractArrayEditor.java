/*
 * The Editor for a AbstractArray.
 * @see animal.graphics.PTArray
 *
 * @author <a href="mailto:roessling@acm.org">Dr. Guido R&ouml;&szlig;ling</a>
 * @version 1.4
 * @date 2008-03-08
 */
package animal.editor.graphics.meta;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import translator.ExtendedActionButton;
import animal.editor.Editor;
import animal.editor.graphics.DoubleArrayEditor;
import animal.editor.graphics.IntArrayEditor;
import animal.editor.graphics.StringArrayEditor;
import animal.graphics.PTDoubleArray;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTIntArray;
import animal.graphics.PTStringArray;
import animal.graphics.meta.PTArray;
import animal.gui.DrawCanvas;
import animal.main.Animation;
import animal.misc.ColorChooserAction;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;
import animal.misc.XProperties;

/**
 * abstract editor for arrays
 * 
 * @author Dr. Guido Roessling <roessling@acm.org>
 * @version 2.5 2008-06-23
 */
public abstract class AbstractArrayEditor extends AbstractTextEditor implements
    ChangeListener, KeyListener {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 2295451210979833754L;
  protected String          actualType;
  protected ColorChooserAction highlightColorChooser,
      elemHighlightColorChooser, fillColorChooser, fontColorChooser;

  protected JTextField         arraySize, content;

  protected int                nrArrayElems = 10;

  protected JSpinner           arraySpinner;
  protected JLabel             spinnerLabel;
  protected SpinnerNumberModel spinnerModel;

  protected JCheckBox          showIndicesCB;

  /**
   * This is used in secondaryEditor to prohibit entry and thus change of
   * arraySize as an array is commonly understood to be of static size, and
   * cannot be resized while being used
   */
  // GR
  protected boolean            firstEdit    = true;

  /**
   * Construct a new AbstractrrayEditor window
   */
  public AbstractArrayEditor(String arrayType) {
    super();
    actualType = arrayType;
  }

  protected void buildGUI(String arrayType) {
    // ensure that cp, generator exist!
    initializeLayoutComponents();

    actualType = arrayType;
    insertSeparator("arraySizeBL", cp, generator);
    createArraySizeComponent();

   if (nrArrayElems == 0)
      nrArrayElems = 10;
    createArrayValueComponent(nrArrayElems);

    createColorComponent(arrayType);

    // ugly but important - make sure "colorChooser" is set!
    // NOTE: this is only for "straight" color objects, not for those
    // using embedded texts
    colorChooser = textColorChooser;

    // add a font choice
    createFontAndStyleChooser("AbstractTextEditor.fontBL");

    // finish with the standard editors
    // for primitives: depth and object name, plus OK/apply/cancel
    finishEditor(cp);
  }

  /**
   * Constructor used by the secondary editor window. This one is (and must be)
   * different from the default constructor, because by definition the array
   * size is static, so the according tab is missing in this version of the
   * IntArrayEditor window.
   * 
   * @param i
   *          the size of the IntArray used to determine the correct indices of
   *          the array cells
   */
  protected AbstractArrayEditor(String type, int i) {
    this(type);
    arraySize.setText(String.valueOf(i));
  }


  protected void createArrayValueComponent(int targetSize) {
    insertSeparator("AbstractArrayEditor.arrayValuesBL", cp, generator);

    JLabel cellLabel = generator
        .generateJLabel("AbstractArrayEditor.arrayCell");
    cp.add(cellLabel, Editor.LAYOUT_PARAGRAPH_GAP);

    spinnerModel = new SpinnerNumberModel(0, 0, targetSize - 1, 1);
    arraySpinner = new JSpinner(spinnerModel);
    arraySpinner.addChangeListener(this);
    cp.add(arraySpinner, Editor.LAYOUT_PARAGRAPH_GAP);

    spinnerLabel = new JLabel(" / " + String.valueOf(targetSize));
    cp.add(spinnerLabel, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);

    cp.add(generator.generateJLabel("AbstractArrayEditor.cellContent"),
        Editor.LAYOUT_PARAGRAPH_GAP);
    int spinnerValue = spinnerModel.getNumber().intValue();
    String text = (getCurrentObject(false) == null) ? ""
        : ((PTArray) getCurrentObject(false)).getStringValueAt(spinnerValue);
    content = new JTextField(15);
    content.setText(text);
    content.addKeyListener(this);
    cp.add(content, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
  }

  protected ColorChooserAction makeColorChooser(String labelKey,
      Color initialColor, String colorName, String prompt,
      String colorNameTranslation, int shortcut) {
    JLabel colorLabel = generator.generateJLabel(labelKey);
    cp.add(colorLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    ColorChooserAction action = createColorChooser(colorName, prompt,
        colorNameTranslation, initialColor);
    ExtendedActionButton colorButton = new ExtendedActionButton(action,
        shortcut);
    cp.add(colorButton, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    return action;
  }

  protected void createColorComponent(String type) {
    // create the color label
    insertSeparator("AbstractArrayEditor.basicColors", cp, generator);

    PTArray co = (PTArray) getCurrentObject(false);
    Color initialColor = (co == null) ? Color.WHITE : co.getOutlineColor();

    // create entry for cell (outline) color
    textColorChooser = makeColorChooser("GenericEditor.colorLabel",
        initialColor, "color", "GenericEditor.chooseColor",
        "AbstractIndexedStructureEditor.outlineColorTip", KeyEvent.VK_C);

    // create entry for element (text) color
    Color initialColor2 = (co == null) ? Color.BLACK : co.getFontColor();
    fontColorChooser = makeColorChooser("AbstractArrayEditor.fontColorLabel",
        initialColor2, "fontColor", "GenericEditor.chooseColor",
        "AbstractIndexedStructureEditor.textColorTip", KeyEvent.VK_F);

    // create entry for cell fill color
    Color initialColor3 = (co == null) ? Color.WHITE : co.getOutlineColor();
    fillColorChooser = makeColorChooser("GenericEditor.fillColorLabel",
        initialColor3, "fillColor", "GenericEditor.chooseColor",
        "AbstractIndexedStructureEditor.fillColorTip", KeyEvent.VK_O);

    // now the highlight colors
    insertSeparator("AbstractArrayEditor.highlightColors", cp, generator);

    // create entry for cell highlight color
    Color initialColor4 = (co == null) ? Color.yellow : co.getHighlightColor();
    highlightColorChooser = makeColorChooser(
        "AbstractIndexedStructureEditor.hlColorLabel", initialColor4,
        "highlightColor", "GenericEditor.chooseColor",
        "AbstractIndexedStructureEditor.cellHighlightTip", KeyEvent.VK_H);

    // create entry for element highlight color
    Color initialColor5 = (co == null) ? Color.red : co.getElemHighlightColor();
    elemHighlightColorChooser = makeColorChooser(
        "AbstractIndexedStructureEditor.elemHighlightColorLabel",
        initialColor5, "elemHighlightColor", "GenericEditor.chooseColor",
        "AbstractIndexedStructureEditor.elemHighlightTip", KeyEvent.VK_E);
  }
  protected void ensureTypeSet() {
    if (actualType == null) {
      Object o = getCurrentObject(false);
      if (o instanceof PTIntArray)
        actualType = PTIntArray.INT_ARRAY_TYPE;
      else if (o instanceof PTStringArray)
        actualType = PTStringArray.STRING_ARRAY_TYPE;
      else if (o instanceof PTDoubleArray)
        actualType = PTDoubleArray.DOUBLE_ARRAY_TYPE;
    }
  }

  /**
   * Set the current properties in the editor window as defined in the
   * XProperties.
   * 
   * @param props
   *          the used properties file
   */
  public void setProperties(XProperties props) {
    ensureTypeSet();
    colorChooser.setColor(props.getColorProperty(actualType + ".outlineColor",
        Color.WHITE));
    highlightColorChooser.setColor(props.getColorProperty(actualType
        + ".highlightColor", Color.YELLOW));
    elemHighlightColorChooser.setColor(props.getColorProperty(actualType
        + ".elemHighlightColor", Color.RED));
    fillColorChooser.setColor(props.getColorProperty(actualType + ".bgColor",
        Color.BLACK));
    fontColorChooser.setColor(props.getColorProperty(actualType + ".fontColor",
        Color.BLACK));
    arraySize.setText(String.valueOf(props.getIntProperty(actualType
        + ".arraySize", 1)));
    fontName.setSelectedItem(props.getProperty(actualType + ".fontName",
        "Monospaced"));
    fontSize.setSelectedItem(props.getProperty(actualType + ".fontSize", "14"));
    showIndicesCB.setSelected(props.getBoolProperty(
        actualType + ".showIndices", true));
  }

  protected void createArraySizeComponent() {
    JLabel arraySizeLabel = generator.generateJLabel("size");
    cp.add(arraySizeLabel, Editor.LAYOUT_PARAGRAPH_GAP);

    arraySize = new JTextField(8);
    arraySize.addActionListener(this);
    cp.add(arraySize, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    createIndicesOptionsComponent();
  }

  protected void createIndicesOptionsComponent() {
    JLabel label = generator.generateJLabel("indices");
    cp.add(label, Editor.LAYOUT_PARAGRAPH_GAP);
    showIndicesCB = generator.generateJCheckBox(
        "AbstractIndexedStructureEditor.showIndices", null, this);
    cp.add(showIndicesCB, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
  }

  /**
   * Store the current properties to the XProperties.
   * 
   * @param props
   *          the used properties file
   */
  public void getProperties(XProperties props) {
    ensureTypeSet();
    props.put(actualType + ".outlineColor", colorChooser.getColor());
    props.put(actualType + ".highlightColor", highlightColorChooser.getColor());
    props.put(actualType + ".elemHighlightColor",
        elemHighlightColorChooser.getColor());
    props.put(actualType + ".bgColor", fillColorChooser.getColor());
    props.put(actualType + ".fontColor", fontColorChooser.getColor());
    props.put(actualType + ".arraySize", getInt(arraySize.getText(), 1));
    props.put(actualType + ".fontName", fontName.getSelectedItem());
    props.put(actualType + ".fontSize", fontSize.getSelectedItem());
    props.put(
        actualType + ".font",
        new Font((String) fontName.getSelectedItem(), Font.PLAIN, getInt(
            (String) fontSize.getSelectedItem(), 14)));
    props.put(actualType + ".showIndices", showIndicesCB.isSelected());
  }

  /**
   * How many points are needed to create a new instance of the IntArray.
   * 
   * @return the number of the creation points (= 2).
   */
  public int pointsNeeded() {
    return 2;
  }

  /**
   * Executes the necessary actions when a new <code>PTStringArray</code> is
   * created. This depends on the number of mouse clicks that have to be
   * executed to create a new array.
   * 
   * @param num
   *          the number of the current point
   * @param p
   *          the current Point of the cursor
   * @return always true
   */
  public boolean nextPoint(int num, Point p) {
    switch (num) {
      case 1:
      case 2:
        ((PTArray) getCurrentObject()).setOrigin(p);
        break;
    }
    return true;
  }

  /**
   * Determines the minimum distance from a point to the IntArray.
   * 
   * @param go
   *          an object to which the distance is to be calculated
   * @param p
   *          the point of which the distance shall be determined
   */
  public int getMinDist(PTGraphicObject go, Point p) {
    return MSMath.dist(p, go.getBoundingBox());
  }

  /**
   * Returns the EditPoints of the Array. An array has only MovePoints because
   * the size is calculated automatically. DragPoints have negative indices,
   * while EditPoints are positive.
   * 
   * @param go
   *          the graphic object of which to return the edit points
   * @return an array of the edit point for this PTIntArray.
   */
  public EditPoint[] getEditPoints(PTGraphicObject go) {
    Rectangle bBox = ((PTArray) go).getBoundingBox();
    return new EditPoint[] {
        new EditPoint(-1, new Point(bBox.x, bBox.y)),
        new EditPoint(-2, new Point(bBox.x + bBox.width, bBox.y)),
        new EditPoint(-3, new Point(bBox.x, bBox.y + bBox.height)),
        new EditPoint(-4, new Point(bBox.x + bBox.width, bBox.y + bBox.height)),
        new EditPoint(-5, new Point(bBox.x + bBox.width / 2, bBox.y
            + bBox.height / 2)) };
  }

  /**
   * Set the values of the current IntArray to the ones chosen in the editor.
   * 
   * @param eo
   *          the current IntArray
   */
  protected void storeAttributesInto(EditableObject eo) {
    super.storeAttributesInto(eo);
    PTArray array = (PTArray) eo;

    array.setOutlineColor(colorChooser.getColor());
    array.setHighlightColor(highlightColorChooser.getColor());
    array.setElemHighlightColor(elemHighlightColorChooser.getColor());
    array.setBGColor(fillColorChooser.getColor());
    array.setFontColor(fontColorChooser.getColor());
    int arraySizeEntered = array.getSize();
    if (arraySize != null)
      arraySizeEntered = getInt(arraySize.getText(), 1);
    if (arraySizeEntered != array.getSize()) {
      array.setSize(getInt(arraySize.getText(), 1));
    }
    array.enterStringValueAt(calcIndex(), content.getText());
    // Font setzen
    array.setFont(storeFont());
    array.showIndices(showIndicesCB.isSelected());
  }

  /**
   * The alternative editor after a StringArray has already been created.
   * 
   * @param e
   *          the StringArray that should be edited.
   * 
   *          See <code>StringArrayEditor(int i)</code> for details.
   */
  public Editor getSecondaryEditor(EditableObject e) {
    AbstractArrayEditor result = null;
    if (getCurrentObject(false) instanceof PTStringArray)
      result = new StringArrayEditor(((PTArray) e).getSize());
    else if (getCurrentObject(false) instanceof PTIntArray)
      result = new IntArrayEditor(((PTArray) e).getSize());
    else if (getCurrentObject(false) instanceof PTDoubleArray)
      result = new DoubleArrayEditor(((PTArray) e).getSize());
    // don't display tab asking for number of stringArray elements
    result.firstEdit = false;
    nrArrayElems = ((PTArray) e).getSize();
    result.extractAttributesFrom(e);
    return result;
  }

  /**
   * Determines the currently chosen array cell; returns -1 if IntArray is not
   * yet created.
   */
  protected int calcIndex() {
    return (arraySpinner == null) ? -1 : ((SpinnerNumberModel) arraySpinner
        .getModel()).getNumber().intValue();
  }

  protected void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    PTArray array = (PTArray) eo;
    colorChooser.setColor(array.getOutlineColor());
    highlightColorChooser.setColor(array.getHighlightColor());
    elemHighlightColorChooser.setColor(array.getElemHighlightColor());
    fillColorChooser.setColor(array.getBGColor());
    fontColorChooser.setColor(array.getFontColor());
    if (arraySize != null)
      arraySize.setText(String.valueOf(array.getSize()));
    content.setText(array.getStringValueAt(calcIndex()));
    extractFont(array.getFont());
    showIndicesCB.setSelected(array.indicesShown());
  }

  /**
   * Load the text that is displayed in the status line of the ANIMAL drawing
   * window
   * 
   * @return the text of the status line
   */
  public String getStatusLineMsg() {
    return translator.AnimalTranslator.translateMessage(
        actualType + "Editor.statusLine",
        new Object[] { DrawCanvas.translateDrawButton(),
            DrawCanvas.translateFinishButton(),
            DrawCanvas.translateCancelButton() });
  }

  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);

    PTArray array = (PTArray) getCurrentObject();

    // change entry in the array cell, that corresponds to the chosen
    // value of the arraySpinner, if the text in content-field is changed
    if (e.getSource() == content) {
      array.enterStringValueAt(calcIndex(), content.getText());
    }
    if (e.getSource() == arraySize) {
      int newSize = getInt(arraySize.getText(), 1);
      if (newSize != array.getSize()) {
        // System.err.println("changed size: " +array.getSize() +" => "
        // +newSize);
        spinnerLabel.setText(" / " + String.valueOf(newSize));
        array.setSize(newSize);
        spinnerModel = new SpinnerNumberModel(0, 0, newSize - 1, 1);
        arraySpinner.setModel(spinnerModel);
        content.setText(array.getStringValueAt(0));
        apply();
      }
    }
    if (e.getSource() == fontName || e.getSource() == fontSize) {
      array.setFont(storeFont());
    }
    if (e.getSource() == showIndicesCB) {
      array.showIndices(showIndicesCB.isSelected());
    }
    repaintNow();
    Animation.get().doChange();
  }

  public void keyPressed(KeyEvent e) {
    // do nothing; only used for serialization
  }

  public void keyReleased(KeyEvent e) {
    PTArray array = (PTArray) getCurrentObject();
    if (e.getSource() == content) {
      array.enterStringValueAt(calcIndex(), content.getText());
    }
    repaintNow();
    Animation.get().doChange();
  }

  public abstract void keyTyped(KeyEvent e);

  /**
   * Listener that executes if another cell is chosen in the editor. This is
   * <em> not </em> an ActionListener but a ChangeEventListener.
   * 
   * @param e
   *          the event to react upon
   */
  public void stateChanged(ChangeEvent e) {
    // super.stateChanged (e);
    PTArray array = (PTArray) getCurrentObject();
    if (e.getSource() == arraySpinner) {
      content.setText(array.getStringValueAt(calcIndex()));
    }
    repaintNow();
    Animation.get().doChange();
  }

  /**
   * Set the objects properties according to those in the editor if these have
   * changed.
   * 
   * @param event
   *          the event to react upon
   */
  public void propertyChange(PropertyChangeEvent event) {
    PTArray array = (PTArray) getCurrentObject();
    String eventName = event.getPropertyName();
    if ("color".equals(eventName))
      array.setOutlineColor((Color) event.getNewValue());
    if ("highlightColor".equals(eventName)) {
      array.setHighlightColor((Color) event.getNewValue());
      array.getCell(0).setFillColor((Color) event.getNewValue());
    }
    if ("elemHighlightColor".equals(eventName)) {
      array.setElemHighlightColor((Color) event.getNewValue());
      array.getEntry(0).setColor((Color) event.getNewValue());
    }
    if ("fillColor".equals(eventName))
      array.setBGColor((Color) event.getNewValue());
    if ("fontColor".equals(eventName))
      array.setFontColor((Color) event.getNewValue());
    if (!event.getOldValue().equals(event.getNewValue())) {
      repaintNow();
      Animation.get().doChange();
    }
  }
}