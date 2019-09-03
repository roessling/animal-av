package animal.editor.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import translator.AnimalTranslator;
import translator.ExtendedActionButton;
import animal.editor.Editor;
import animal.editor.graphics.meta.AbstractTextEditor;
import animal.graphics.PTBoxPointer;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTRectangle;
import animal.graphics.PTText;
import animal.gui.DrawCanvas;
import animal.main.Animation;
import animal.misc.ColorChooserAction;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;
import animal.misc.XProperties;

/**
 * The Editor for the BoxPointer. Demonstrates implementing a new Editor and
 * using Swing components to edit them.<br>
 * BoxPointerEditor uses a Choice, a TextField, a Checkbox and a Button, so most
 * of the possible options are available in this demo Editor.
 * 
 * @see PTBoxPointer
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class BoxPointerEditor extends AbstractTextEditor implements
    ActionListener, PropertyChangeListener {
  // for each attribute of the PTBoxPointer there is a component here.
  // There are no additional variables to reflect the attributes as they
  // would be redundant. All required information can be fetched from
  // these components.

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -5358323736501168554L;

//  private ColorChooserAction colorChooser;

  protected ColorChooserAction textBoxFillColorChooser;

  protected ColorChooserAction pointerAreaColorChooser;

  protected ColorChooserAction pointerAreaFillColorChooser;

  protected JComboBox<String> pointerCount;

  protected JComboBox<String> pointerPosition;

  protected boolean isItalic, isBold;

  /**
   * plus optional components. This one is used to reset the BoxPointer's
   * pointer to null.
   */
  protected AbstractButton nullPointerButton;

  /**
   * The Editor's constructor. Writes the components into a Panel and adds this
   * Panel to the Editor, then adds the Buttons.
   */
  public BoxPointerEditor() {
    super();
  }

  protected void buildGUI() {
    // ensure that cp, generator exist!
    initializeLayoutComponents();

    // add pointer position, count, and null pointers box
    installPointerComponent();

    // add color settings box
    installColorSettingsComponent();

    // add text entry and cut/copy/paste
    createTextOperationsChooser("AbstractTextEditor.textBL");

    // add font, size, bold/italics selection
    createFontAndStyleChooser("AbstractTextEditor.fontBL");

    finishEditor(cp);
//    finishBoxes();
  }

  protected void installPointerComponent() {
    // add a labelled separator for the pointer box
    insertSeparator("BoxPointerEditor.ptrPosBL", cp, generator);
    
    // add a label for the position of the pointers
    cp.add(generator.generateJLabel("BoxPointerEditor.pointerPositionLabel"),
        Editor.LAYOUT_PARAGRAPH_GAP);
    // 1. pointer position, number of pointers, and null pointer(s)
//    Box pointerPosBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
//        "BoxPointerEditor.ptrPosBL");
//    Box firstRowBox = new Box(BoxLayout.LINE_AXIS);
//    firstRowBox.add(generator
//        .generateJLabel("BoxPointerEditor.pointerPositionLabel"));

    pointerPosition = new JComboBox<String>();
    pointerPosition.addItem(AnimalTranslator
        .translateMessage("BoxPointerEditor.positionNone"));
    pointerPosition.addItem(AnimalTranslator
        .translateMessage("BoxPointerEditor.positionRight"));
    pointerPosition.addItem(AnimalTranslator
        .translateMessage("BoxPointerEditor.positionLeft"));
    pointerPosition.addItem(AnimalTranslator
        .translateMessage("BoxPointerEditor.positionTop"));
    pointerPosition.addItem(AnimalTranslator
        .translateMessage("BoxPointerEditor.positionBottom"));
    pointerPosition.addActionListener(this);
    cp.add(pointerPosition, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
//    firstRowBox.add(pointerPosition);
//    pointerPosBox.add(firstRowBox);

    cp.add(generator.generateJLabel("BoxPointerEditor.pointerCountLabel"),
        Editor.LAYOUT_PARAGRAPH_GAP);
//    Box secondRowBox = new Box(BoxLayout.LINE_AXIS);
//    secondRowBox.add(generator
//        .generateJLabel("BoxPointerEditor.pointerCountLabel"));
    pointerCount = new JComboBox<String>();
    for (int j = 0; j < 10; j++)
      pointerCount.addItem(String.valueOf(j));
    pointerCount.addActionListener(this);
    cp.add(pointerCount, Editor.LAYOUT_PARAGRAPH_GAP);
//    secondRowBox.add(pointerCount);
//    pointerPosBox.add(secondRowBox);

//    Box thirdRowBox = new Box(BoxLayout.LINE_AXIS);
    nullPointerButton = generator.generateJButton(
        "BoxPointerEditor.nullPtrButton", null, false, this);
    cp.add(nullPointerButton, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
//    thirdRowBox.add(nullPointerButton);
//    pointerPosBox.add(thirdRowBox);
//    return pointerPosBox;
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

  protected void installColorSettingsComponent() {
    // add a separator
    insertSeparator("GenericEditor.colorBL", cp, generator);

//    createGenericColorSetting("GenericEditor.colorLabel", KeyEvent.VK_C);
    
    PTBoxPointer bp = (PTBoxPointer)getCurrentObject(false);
    
    Color pointerColor = (bp == null) ? Color.black
        : bp.getColor();
    colorChooser = makeColorChooser("BoxPointerEditor.boxPtrColorLabel",
        pointerColor, "color", "GenericEditor.chooseColor",
        "BoxPointerEditor.boxPtrColor", KeyEvent.VK_C);

    Color initialColor = (bp == null) ? Color.BLACK 
        : bp.getTextComponent().getColor();
    textColorChooser = makeColorChooser("fontColor", initialColor, 
        "textColor",
        "GenericEditor.chooseColor", "BoxPointerEditor.textColor", KeyEvent.VK_T);

    // create entry for element (text) color
    Color initialColor2 = (bp == null) ? Color.BLACK
        : bp.getTextBox().getFillColor();
    textBoxFillColorChooser = makeColorChooser("GenericEditor.fillColorLabel",
        initialColor2, "textBoxFillColor", "GenericEditor.chooseColor",
        "textBoxFillColor", KeyEvent.VK_F);

    // color settings
//    Box colorsBox = generator.generateBorderedBox(BoxLayout.X_AXIS,
//        "GenericEditor.colorBL");

    // color
//    Box colorFillColorBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
//        "BoxPointerEditor.boxColBL");
    // colorFillColorBox.add(generator.generateJLabel("boxPtrColorLabel"));
//    colorFillColorBox.add(generator.generateJLabel("GenericEditor.colorLabel"));

//    Color initialColor = (getCurrentObject(false) == null) ? Color.black
//        : ((PTBoxPointer) getCurrentObject()).getColor();
//    colorChooser = new ColorChooserAction(this, ColorChoice
//        .getColorName(initialColor), "color", AnimalTranslator
//        .translateMessage("BoxPointerEditor.boxPtrColor",
//            new Object[] { "boxPtrColor" }), initialColor);
//    colorFillColorBox.add(new ExtendedActionButton(
//        colorChooser, KeyEvent.VK_C));

//    colorFillColorBox.add(generator
//        .generateJLabel("GenericEditor.fillColorLabel"));

//    initialColor = (getCurrentObject(false) == null) ? Color.black
//        : ((PTBoxPointer) getCurrentObject()).getTextBox().getFillColor();
//    textBoxFillColorChooser = new ColorChooserAction(this, ColorChoice
//        .getColorName(initialColor), "textBoxFillColor", AnimalTranslator
//        .translateMessage("GenericEditor.chooseColor",
//            new Object[] { "textBoxFillColor" }), initialColor);
//    colorFillColorBox.add(new ExtendedActionButton(textBoxFillColorChooser,
//        KeyEvent.VK_F));
//    // add color / fill color box to color box
//    colorsBox.add(colorFillColorBox);

    Color pointerAreaColor = (bp == null) ? Color.black :
      bp.getPointerArea().getColor();
    pointerAreaColorChooser = makeColorChooser(
        "BoxPointerEditor.pointerBoxColorLabel",
        pointerAreaColor, "pointerAreaColor", "GenericEditor.chooseColor",
        "pointerBoxColor", KeyEvent.VK_P);

//    Box ptrBoxFillColorBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
//        "BoxPointerEditor.ptrBoxBL");
//    ptrBoxFillColorBox.add(generator
//        .generateJLabel("BoxPointerEditor.pointerBoxColorLabel"));
//
//    initialColor = (getCurrentObject(false) == null) ? Color.black
//        : ((PTBoxPointer) getCurrentObject()).getPointerArea().getColor();
//    pointerAreaColorChooser = new ColorChooserAction(this, ColorChoice
//        .getColorName(initialColor), "pointerAreaColor", AnimalTranslator
//        .translateMessage("GenericEditor.chooseColor",
//            new Object[] { "pointerBoxColor" }), initialColor);
//    ptrBoxFillColorBox.add(new ExtendedActionButton(pointerAreaColorChooser,
//        KeyEvent.VK_P));
    
    Color pointerAreaFillColor = (bp == null) ? Color.white
        : bp.getPointerArea().getFillColor();
    pointerAreaFillColorChooser = makeColorChooser("pointerAreaFillColorLabel",
        pointerAreaFillColor, "pointerAreaFillColor", "GenericEditor.chooseColor",
        "pointerAreaFillColor", KeyEvent.VK_A);
//    ptrBoxFillColorBox.add(generator
//        .generateJLabel("GenericEditor.fillColorLabel"));
//    initialColor = (getCurrentObject(false) == null) ? Color.black
//        : ((PTBoxPointer) getCurrentObject()).getPointerArea().getFillColor();
//    pointerAreaFillColorChooser = new ColorChooserAction(this, ColorChoice
//        .getColorName(initialColor), "pointerAreaFillColor", AnimalTranslator
//        .translateMessage("GenericEditor.chooseColor",
//            new Object[] { "pointerAreaFillColor" }), initialColor);
//    ptrBoxFillColorBox.add(new ExtendedActionButton(
//        pointerAreaFillColorChooser, KeyEvent.VK_F));

    // add pointer color settings to color box
//    colorsBox.add(ptrBoxFillColorBox);

//    return colorsBox;
  }

  /**
   * how many points do we need to specify a BoxPointer?
   * 
   * @see #nextPoint
   */
  public int pointsNeeded() {
    return 2 + ((PTBoxPointer) getCurrentObject()).getPointerCount();
  }

  /**
   * sets one of the BoxPointer's points. <br>
   * 1st and 2nd: set Location(by this, the BoxPointer is not displayed before
   * the mouse is clicked once)<br>
   * 3rd: set pointer
   */
  public boolean nextPoint(int num, Point p) {
    PTBoxPointer bp = (PTBoxPointer) getCurrentObject();
    switch (num) {
    case 1:
    case 2:
      bp.setPosition(p);
      // bp.setLocation(p);
      break;
    default:
      bp.setTip(num - 3, p);
      break;
    }
    return true;
  } // nextPoint;

  /**
   * returns the minimal distance from point <i>p</i> to the BoxPointer.
   * IMPORTANT: use <code>PTGraphicObject</code> as first parameter and cast
   * it to a PTBoxPointer inside the method. Otherwise this method won't be
   * called.
   */
  public int getMinDist(PTGraphicObject go, Point p) {
    PTBoxPointer bp = (PTBoxPointer) go;
    PTRectangle box = bp.getTextBox();
    PTRectangle pointerArea = bp.getPointerArea();
    int i;
    // if inside, it's not far away...
    if (box.toPolygon().contains(p))
      return 0;
    if (pointerArea != null && pointerArea.toPolygon().contains(p))
      return 0;
    // calculate distance from upper edge
    int minDist = Integer.MAX_VALUE;
    boolean hasSafePointerArea = pointerArea != null
        && pointerArea.getNodeCount() > 3;
    if (hasSafePointerArea) {
      minDist = Math.min(MSMath.dist(p, box.getLocation(), box.getEndNode()),
          MSMath.dist(p, pointerArea.getLocation(), pointerArea.getEndNode()));
    } else {
      minDist = MSMath.dist(p, box.getLocation(), box.getEndNode());
    }

    // compare with distance to pointer
    for (i = 0; i < bp.getPointerCount(); i++)
      minDist = Math.min(minDist, MSMath.dist(p, bp.getPointerOrigin(i), bp
          .getTip(i)));
    return minDist;
  }

  /**
   * returns the EditPoints of the BoxPointer. Again, the parameter has to be of
   * type <b>PTGraphicObject</b>. A BoxPointer only has one MovePoint(its upper
   * left corner) and one ChangePoint(its tip).
   */
  public EditPoint[] getEditPoints(PTGraphicObject go) {
    PTBoxPointer bp = (PTBoxPointer) go;
    int count = bp.getPointerArea() != null ? bp.getPointerCount() : 0;
    EditPoint[] result = new EditPoint[1 + count];
    result[0] = new EditPoint(-1, bp.getOrigin());
    for (int i = 0; i < count; i++)
      result[i + 1] = new EditPoint(i + 3, bp.getTip(i));
    return result;
  } // getEditPoints

  /**
   * extract the properties from <i>props</i>. All components are set according
   * to these properties.
   */
  public void setProperties(XProperties props) {
    if (textField != null)
      textField.setText(props.getProperty(PTBoxPointer.TYPE_LABEL + ".text"));
    fontName.setSelectedItem(props.getProperty(PTBoxPointer.TYPE_LABEL
        + ".fontName", "SansSerif"));
    fontSize.setSelectedItem(props.getProperty(PTBoxPointer.TYPE_LABEL
        + ".fontSize", "12"));
    int fontStyle = props
        .getIntProperty(PTBoxPointer.TYPE_LABEL + ".fontStyle");
    italic.setSelected((fontStyle & Font.ITALIC) == Font.ITALIC);
    bold.setSelected((fontStyle & Font.BOLD) == Font.BOLD);
    pointerPosition.setSelectedItem(props.getProperty(PTBoxPointer.TYPE_LABEL
        + ".pointerPosition", "Right"));
    pointerCount.setSelectedItem(String.valueOf(props.getIntProperty(
        PTBoxPointer.TYPE_LABEL + ".nrPointers", 1)));
    colorChooser.setColor(props.getColorProperty(
        PTBoxPointer.TYPE_LABEL + ".color", Color.black));
    textBoxFillColorChooser.setColor(props.getColorProperty(
        PTBoxPointer.TYPE_LABEL + ".fillColor", Color.white));
    pointerAreaColorChooser.setColor(props.getColorProperty(
        PTBoxPointer.TYPE_LABEL + ".pointerAreaColor", Color.white));
    pointerAreaFillColorChooser.setColor(props.getColorProperty(
        PTBoxPointer.TYPE_LABEL + ".pointerAreaFillColor", Color.white));
    textColorChooser.setColor(props.getColorProperty(PTBoxPointer.TYPE_LABEL
        + ".textColor", Color.black));
  }

  /**
   * writes the Editor's current permanent attributes to the Properties object.
   */
  public void getProperties(XProperties props) {
    props.put(PTBoxPointer.TYPE_LABEL + ".color", colorChooser
        .getColor());
    props.put(PTBoxPointer.TYPE_LABEL + ".fillColor", textBoxFillColorChooser
        .getColor());
    props.put(PTBoxPointer.TYPE_LABEL + ".pointerAreaColor",
        pointerAreaColorChooser.getColor());
    props.put(PTBoxPointer.TYPE_LABEL + ".pointerAreaFillColor",
        pointerAreaFillColorChooser.getColor());
    props.put(PTBoxPointer.TYPE_LABEL + ".textColor", textColorChooser
        .getColor());
    props.put(PTBoxPointer.TYPE_LABEL + ".text", textField.getText());
    props
        .put(PTBoxPointer.TYPE_LABEL + ".fontName", fontName.getSelectedItem());
    props
        .put(PTBoxPointer.TYPE_LABEL + ".fontSize", fontSize.getSelectedItem());
    int fontStyle = (italic.isSelected()) ? Font.ITALIC : Font.PLAIN;
    if (bold.isSelected())
      fontStyle |= Font.BOLD;
    props.put(PTBoxPointer.TYPE_LABEL + ".fontStyle", fontStyle);
    props.put(PTBoxPointer.TYPE_LABEL + ".pointerPosition", pointerPosition
        .getSelectedItem());
    props.put(PTBoxPointer.TYPE_LABEL + ".nrPointers", pointerCount
        .getSelectedItem());
  }

  /**
   * creates a new BoxPointer and uses the attributes of this Editor as default
   * values.
   */
  public EditableObject createObject() {
    PTBoxPointer bp = new PTBoxPointer();
    bp.init();
    storeAttributesInto(bp);
    return bp;
  }

  /**
   * applies the Editor's settings to the BoxPointer by setting all of
   * BoxPointers attributes according to the components. Parameter must be of
   * type <b>EditableObject</b> and be casted inside the method.
   */
  protected void storeAttributesInto(EditableObject eo) {
    // don't forget to store the parent's attributes!
    super.storeAttributesInto(eo);
    PTBoxPointer bp = (PTBoxPointer) eo; // just a shortcut
    bp.setColor(colorChooser.getColor());
    PTRectangle currentBox = bp.getTextBox();
    currentBox.setFillColor(textBoxFillColorChooser.getColor());
    currentBox = bp.getPointerArea();
    currentBox.setColor(pointerAreaColorChooser.getColor());
    currentBox.setFillColor(pointerAreaFillColorChooser.getColor());
    PTText text = bp.getTextComponent();
    text.setColor(textColorChooser.getColor());
    if (textField == null)
      bp.setText("");
    else
      bp.setText(textField.getText());
    bp.getTextComponent().setFont(storeFont());
    bp.setPointerPosition(pointerPosition.getSelectedIndex());

    try {
      bp.setPointerCount(Integer.parseInt((String) pointerCount
          .getSelectedItem()));
    } catch (NumberFormatException ignore) {
      // do nothing; only used for serialization
    }
  }

//  /**
//   * extracts the Font from the components' settings.
//   */
//  Font storeFont() {
//    String name = (String) fontName.getSelectedItem();
//    String size = (String) fontSize.getSelectedItem();
//    return new Font(name, Font.PLAIN + (isBold ? Font.BOLD : 0)
//        + (isItalic ? Font.ITALIC : 0), getInt(size, 12));
//  }
//
//  /**
//   * sets the components according to the font.
//   */
//  void extractFont(Font f) {
//    fontName.setSelectedItem(f.getName());
//    fontSize.setSelectedItem(String.valueOf(f.getSize()));
//    italic.setSelected(f.isItalic());
//    bold.setSelected(f.isBold());
//  }

  /**
   * makes the Editor reflect the BoxPointer's attributes by setting all
   * components according to the attributes. Parameter must be of type
   * <b>EditableObject</b>, otherwise there may be problems with calls to super
   * that can be difficult to debug.
   */
  protected void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    PTBoxPointer bp = (PTBoxPointer) eo;

    colorChooser.setColor(bp.getColor());
    textBoxFillColorChooser.setColor(bp.getTextBox().getFillColor());
    pointerAreaColorChooser.setColor(bp.getPointerArea().getColor());
    pointerAreaFillColorChooser.setColor(bp.getPointerArea().getFillColor());
    textColorChooser.setColor(bp.getTextComponent().getColor());
    textField.setText(bp.getTextComponent().getText());
    extractFont(bp.getTextComponent().getFont());
    pointerPosition.setSelectedIndex(bp.getPointerPosition());
    pointerCount.setSelectedItem(String.valueOf(bp.getPointerCount()));
  }

  /**
   * creates a secondary Editor for the given <b>EditableObject</b> and copies
   * all of the object's attributes into the components. We can rely on this
   * object always being a <b>PTBoxPointer</b>.
   */
  public Editor getSecondaryEditor(EditableObject eo) {
    BoxPointerEditor result = new BoxPointerEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  /**
   * returns a short description how to create a BoxPointer.
   */
  public String getStatusLineMsg() {
    return AnimalTranslator.translateMessage("BoxPointerEditor.statusLine",
        new Object[] { DrawCanvas.translateDrawButton(),
            DrawCanvas.translateFinishButton(),
            DrawCanvas.translateCancelButton() });
  }

  /**
   * handles the button press by calling a BoxPointer method.
   */
  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);

    PTBoxPointer bp = (PTBoxPointer) getCurrentObject();

    isItalic = bp.getTextComponent().getFont().isItalic(); // important
    isBold = bp.getTextComponent().getFont().isBold();

    if (e.getSource() == textField)
      bp.setText(textField.getText());

    if ((e.getSource() == fontName) || (e.getSource() == fontSize))
      bp.getTextComponent().setFont(storeFont());

    if (e.getSource() == italic) {
      isItalic = !isItalic;
      String name = (String) fontName.getSelectedItem();
      String size = (String) fontSize.getSelectedItem();
      Font newFont = new Font(name, Font.PLAIN
          + (bold.isSelected() ? Font.BOLD : 0)
          + (italic.isSelected() ? Font.ITALIC : 0), getInt(size, 12));
      bp.getTextComponent().setFont(newFont);
    }

    if (e.getSource() == bold) {
      isBold = !isBold;
      String name = (String) fontName.getSelectedItem();
      String size = (String) fontSize.getSelectedItem();
      Font newFont = new Font(name, Font.PLAIN
          + (bold.isSelected() ? Font.BOLD : 0)
          + (italic.isSelected() ? Font.ITALIC : 0), getInt(size, 12));
      bp.getTextComponent().setFont(newFont);
    }

    if (e.getSource() == pointerPosition)
      bp.setPointerPosition(pointerPosition.getSelectedIndex());

    if (e.getSource() == pointerCount)
      try {
        bp.setPointerCount(Integer.parseInt((String) pointerCount
            .getSelectedItem()));
      } catch (NumberFormatException ignore) {
        // do nothing
      }

    if (e.getSource() == nullPointerButton)
      bp.makeNullPointer();

    bp.init();
    if (Animation.get() != null)
      Animation.get().doChange();
    repaintNow();
  }

  public void propertyChange(PropertyChangeEvent event) {
    PTBoxPointer bp = (PTBoxPointer) getCurrentObject();
    String eventName = event.getPropertyName();
    if ("color".equals(eventName))
      bp.setColor((Color) event.getNewValue());
    else if ("textBoxFillColor".equals(eventName))
      bp.getTextBox().setFillColor((Color) event.getNewValue());
    else if ("pointerAreaColor".equals(eventName)) {
      if (bp.getPointerArea() != null)
        bp.getPointerArea().setColor((Color) event.getNewValue());
    } else if ("pointerAreaFillColor".equals(eventName)) {
      if (bp.getPointerArea() != null)
        bp.getPointerArea().setFillColor((Color) event.getNewValue());
    } else if ("textColor".equals(eventName)) {
        bp.getTextComponent().setColor((Color) event.getNewValue());
    }
    if (!event.getOldValue().equals(event.getNewValue())) {
      repaintNow();
      if (Animation.get() != null)
        Animation.get().doChange();
    }
  }

  public String getBasicType() {
    return PTBoxPointer.TYPE_LABEL;
  }
} // BoxPointerEditor
