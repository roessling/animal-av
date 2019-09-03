package animal.editor.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultEditorKit;

import translator.AnimalTranslator;
import translator.ExtendedActionButton;
import animal.editor.Editor;
import animal.editor.graphics.meta.OrientedFillablePrimitiveEditor;
import animal.graphics.PTArc;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTText;
import animal.graphics.meta.FillablePrimitive;
import animal.main.Animal;
import animal.main.Animation;
import animal.misc.ColorChooserAction;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;
import animal.misc.TextUtilities;
import animal.misc.XProperties;

/**
 * The Editor for the Arc.
 * 
 * @see animal.graphics.PTArc
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.5 2008-06-23
 */
public class ArcEditor extends OrientedFillablePrimitiveEditor {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3323874737657161500L;

  private ExtendedActionButton fillColorChooserButton;
  protected ColorChooserAction textColorChooser;
  protected JCheckBox isCircular;
  protected JComboBox<String> fontName;
  protected JComboBox<String> fontSize;
  protected JCheckBox isClosed;

  protected JCheckBox isItalic, isBold;
  protected JTextField textField;
  
  // arrows can only be set if the arc is neither closed nor filled
  protected JCheckBox bwArrow;
  protected JCheckBox fwArrow;

  /**
   * The Editor's constructor. Writes the components into a Panel and adds this
   * Panel to the Editor, then adds the Buttons.
   */
  public ArcEditor() {
    super();
  }

  protected void buildGUI() {
    // ensure that cp, generator exist!
    initializeLayoutComponents();
    
    // add basic color choice
    createGenericColorSetting("GenericEditor.colorLabel", KeyEvent.VK_C);
    
    // create the elements for entering color and fill color
    createFillColorChoice("GenericEditor.fillColor", KeyEvent.VK_F,
        Editor.LAYOUT_PARAGRAPH_GAP);
    
    // is the arc filled?
    filledCB = generator.generateJCheckBox("GenericEditor.filled", null, this);
    filledCB.addItemListener(this);
    cp.add(filledCB, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
    insertSeparator("LineEditor.propertiesBL", cp, generator);
    
    JLabel arrowLabel = generator.generateJLabel("ArcEditor.arrows");
    cp.add(arrowLabel, Editor.LAYOUT_PARAGRAPH_GAP);

    fwArrow = generator.generateJCheckBox("ArrowableShapeEditor.fwArrow");
    /*fwArrow = new JCheckBox(AnimalTranslator
        .translateMessage("ArrowableShapeEditor.fwArrow"));*/
    fwArrow.addItemListener(this);
    cp.add(fwArrow, Editor.LAYOUT_PARAGRAPH_GAP);

    bwArrow = generator.generateJCheckBox("ArrowableShapeEditor.bwArrow");
    
   /* bwArrow = new JCheckBox(AnimalTranslator
        .translateMessage("ArrowableShapeEditor.bwArrow"));*/
    bwArrow.addItemListener(this);
    cp.add(bwArrow, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
    JLabel clockwiseLabel = generator.generateJLabel("ArcBasedShapeEditor.orientation");
    cp.add(clockwiseLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    // add orientation
    clockwise = generator.generateJCheckBox("ArcBasedShapeEditor.clockwise",
        null, this);
    clockwise.addItemListener(this);
    cp.add(clockwise, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
    JLabel propsLabel = generator.generateJLabel("ArcEditor.arcPropsBL");
    cp.add(propsLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    
    // is this arc circular or ellipsoid?
    isCircular = generator.generateJCheckBox("ArcEditor.circle", null, this);
    isCircular.addActionListener(this);
    cp.add(isCircular, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
    JLabel cFLabel = generator.generateJLabel("ArcEditor.closedFilled");
    cp.add(cFLabel, Editor.LAYOUT_PARAGRAPH_GAP);

    // is the arc open or closed?
    isClosed = generator.generateJCheckBox("ArcEditor.closed", null, this);
    isClosed.addItemListener(this);
    cp.add(isClosed, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);

    
    

    
    
    
    
    
    
//    TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
//
//    Box colorBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
//        "GenericEditor.colorBL");
//    Box firstRowBox = new Box(BoxLayout.LINE_AXIS);
//    firstRowBox.add(generator.generateJLabel("GenericEditor.colorLabel"));
//    Color initialColor = (getCurrentObject(false) == null) ? Color.black
//        : ((PTText) getCurrentObject(false)).getColor();
//    colorChooser = new ColorChooserAction(this, ColorChoice
//        .getColorName(initialColor), "color", AnimalTranslator
//        .translateMessage("GenericEditor.chooseColor",
//            new Object[] { AnimalTranslator
//                .translateMessage("ArcEditor.lineColor") }), initialColor);
//    firstRowBox.add(new ExtendedActionButton(colorChooser, KeyEvent.VK_C));
//    colorBox.add(firstRowBox);
//
//    Box secondRowBox = new Box(BoxLayout.LINE_AXIS);
//    secondRowBox.add(generator.generateJLabel("GenericEditor.fillColorLabel"));
//
//    initialColor = (getCurrentObject(false) == null) ? Color.black
//        : ((PTArc) getCurrentObject()).getFillColor();
//    fillColorChooser = new ColorChooserAction(this, ColorChoice
//        .getColorName(initialColor), "fillColor", AnimalTranslator
//        .translateMessage("GenericEditor.chooseColor",
//            new Object[] { AnimalTranslator
//                .translateMessage("GenericEditor.fillColor") }), initialColor);
//    fillColorChooserButton = new ExtendedActionButton(fillColorChooser,
//        KeyEvent.VK_F);
//    secondRowBox.add(fillColorChooserButton);
//    colorBox.add(secondRowBox);
//    addBox(colorBox);

//    filledCB = new JCheckBox(AnimalTranslator
//        .translateMessage("GenericEditor.filled"));
//    filledCB.addItemListener(this);

//    clockwise = new JCheckBox(AnimalTranslator
//        .translateMessage("ArcBasedShapeEditor.clockwise"));
//    clockwise.addActionListener(this);


//    Box arcPropertiesBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
//        "ArcEditor.arcPropsBL");
//
//    Box closedFilledBox = new Box(BoxLayout.LINE_AXIS);
//    closedFilledBox.add(isClosed);
//    closedFilledBox.add(filledCB);
//
//    Box circleClockwiseBox = new Box(BoxLayout.LINE_AXIS);
//    circleClockwiseBox.add(isCircular);
//    circleClockwiseBox.add(clockwise);
//
//    Box arrowModeBox = new Box(BoxLayout.LINE_AXIS);
//    arrowModeBox.add(fwArrow);
//    arrowModeBox.add(bwArrow);
//
//    arcPropertiesBox.add(closedFilledBox);
//    arcPropertiesBox.add(circleClockwiseBox);
//    arcPropertiesBox.add(arrowModeBox);
//    addBox(arcPropertiesBox);
    addTextStuff("ArcEditor.text");

    // finish with the standard editors
    // for primitives: depth and object name, plus OK/apply/cancel
    finishEditor(cp);
//  addBox(generateTextComponentBox(generator));
    // finish the boxes
//    finishBoxes();
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

  protected void addTextStuff(String borderKey) {

    // insert the properly labeled separator
    insertSeparator(borderKey, cp, generator);

    JLabel label = generator.generateJLabel("ArcEditor.text");
    cp.add(label, Editor.LAYOUT_PARAGRAPH_GAP);
    // add a text input field
    textField = new JTextField(16);
    textField.addActionListener(this);
    cp.add(textField, Editor.LAYOUT_PARAGRAPH_GAP_GROWX_SPAN_WRAP);

    JLabel gapLabel = new JLabel(" ");
    cp.add(gapLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    
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
    
    // add a box for choosing the font
    JLabel colorLabel = generator.generateJLabel("GenericEditor.nameLabel");
    cp.add(colorLabel, Editor.LAYOUT_PARAGRAPH_GAP);

    fontName = getSupportedFontsChooser();
    fontName.addActionListener(this);
    cp.add(fontName, Editor.LAYOUT_PARAGRAPH_GAP_GROWX_SPAN_WRAP);

    // add a font size choice box
    cp.add(generator.generateJLabel("AbstractTextEditor.fontSizeLabel"),
        Editor.LAYOUT_PARAGRAPH_GAP);

    fontSize = getSupportedFontSizeChooser();
    fontSize.addActionListener(this);
    cp.add(fontSize, Editor.LAYOUT_PARAGRAPH_GAP_GROWX_SPAN_WRAP);

    // create the text color label + chooser
    JLabel fillColorLabel = generator.generateJLabel("AbstractTextEditor.textColor");
    cp.add(fillColorLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    
    
    //Changes the fillColor of the arc, not the text color
    Color initialColor = (getCurrentObject(false) == null) ? Color.black
        : ((FillablePrimitive)getCurrentObject(false)).getFillColor();
    textColorChooser = createColorChooser("textColor", "GenericEditor.chooseColor",
        "AbstractTextEditor.textColor", initialColor);
    fillColorChooserButton = new ExtendedActionButton(fillColorChooser,
        KeyEvent.VK_T);
    cp.add(fillColorChooserButton, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);

//    insertSeparator("AbstractTextEditor.textBL", cp, generator);


    // add a font style choice box (bold? italic?)
    cp.add(generator.generateJLabel("AbstractTextEditor.fontStyleLabel"),
        Editor.LAYOUT_PARAGRAPH_GAP);

    isItalic = generator.generateJCheckBox("italic", null, this);
    isItalic.setHorizontalAlignment(SwingConstants.CENTER);
    cp.add(isItalic, Editor.LAYOUT_PARAGRAPH_GAP);

    isBold = generator.generateJCheckBox("bold", null, this);
    isBold.setHorizontalAlignment(SwingConstants.CENTER);
    cp.add(isBold, Editor.LAYOUT_WRAP);

  }
  /**
   * how many points do we need to specify a Arc?
   * 
   * @see #nextPoint
   */
  public int pointsNeeded() {
    return 4;
  }

  /**
   * sets one of the Arc's points. <br>
   * 1: center 2: size(xRadius and yRadius) 3: startAngle 4: arcAngle
   */
  public boolean nextPoint(int num, Point p) {
    PTArc arc = (PTArc) getCurrentObject();
    switch (num) {
    case 1:
      // must only be called from nextPoint when creating
      // not when moving!
      arc.setCenter(p);
      arc.setStartAngle(0);
      // use arcAngle as a tag to determine whether the arc
      // is just being created. This is important, as during
      // creation only a line is drawn for the start angle while
      // afterwards all the arc is shifted.
      // In addition, by using 720 degrees, always a full ellipse
      // is painted.

      arc.setTotalAngle(720);
      break;
    case 2:
      if (arc.isCircular())
        arc.setRadius(MSMath.dist(arc.getCenter(), p));
      else {
        int xRadius = Math.abs(p.x - arc.getCenter().x);
        int yRadius = Math.abs(p.y - arc.getCenter().y);
        arc.setRadius(new Point(xRadius, yRadius));
      }
      break;
    case 3:
      arc.setStartAngle(arc.getAngle(p));
      if (Math.abs(arc.getTotalAngle()) == 720) {
        // arc was just created? see case 1:
        // then enable drawing of just a line, otherwise use
        // the current attributes.
        arc.setClosed(true);
        arc.setFilled(false);
      }
      break;
    case 4:
      int angle = arc.getAngle(p) - arc.getStartAngle();
      if (arc.isClockwise())
        angle = -angle;
      if (angle <= 0)
        angle += 360;
      arc.setTotalAngle(angle);
      // now use the real attributes again
      arc.setClosed(isClosed.isSelected());
      arc.setFilled(filledCB.isSelected());
      break;
    }
    return true;
  } // nextPoint;

  /**
   * returns the minimal distance from point <i>p</i> to the Arc. IMPORTANT:
   * use <code>PTGraphicObject</code> as first parameter and cast it to a
   * PTArc inside the method. Otherwise this method won't be called.
   */
  public int getMinDist(PTGraphicObject go, Point p) {
    int dist;
    PTArc arc = (PTArc) go;
    int angle = arc.getAngle(p);
    Point c = arc.getCenter();
    Point firstPoint = arc.getPointAtAngle(arc.getStartAngle());
    Point secondPoint = arc.getPointAtAngle(arc.getStartAngle()
        + arc.getTotalAngle());

    if (arc.isAngleInside(angle)) {
      // d is the "distance" according to the normal form of the
      // ellipse: x^2/a^2+y^2/b^2 = d = 1 for points on the ellipse
      double d = MSMath.sqr((double) (p.x - c.x) / arc.getXRadius())
          + MSMath.sqr((double) (p.y - c.y) / arc.getYRadius());
      if (d >= 1)// outside the ellipse

        dist = (int) Math.sqrt((d - 1) * 500);
      // scaling to get reasonable values
      else {
        if (arc.isFilled())
          dist = 0;
        else if (arc.isClosed())
          // minimum of distance to the border and the distance
          // to the lines from the border to the center
          dist = Math.min((int) Math.sqrt((1 - d) * 500), Math.min(MSMath.dist(
              p, firstPoint, c), MSMath.dist(p, secondPoint, c)));
        else
          // inside without lines from border to center
          // only consider the distance to the border

          dist = (int) Math.sqrt((1 - d) * 500);
      }
    } else { // !isAngleInside
      if (arc.isFilled() || arc.isClosed())// distance to the lines from border
        // to center

        dist = Math.min(MSMath.dist(p, firstPoint, c), MSMath.dist(p,
            secondPoint, c)); // as above.
      else
        // distance to the start and end point of the ellipse

        dist = Math
            .min(MSMath.dist(p, firstPoint), MSMath.dist(p, secondPoint));
    }
    return dist;
  }

  /**
   * returns the EditPoints of the Arc. Again, the parameter has to be of type
   * <b>PTGraphicObject</b>. A Arc has only one MovePoint(its center) and six
   * ChangePoints: if a circle, its radiuses; if an arc, the corners of its
   * bounding box, and the start and end angle.
   */
  public EditPoint[] getEditPoints(PTGraphicObject go) {
    PTArc arc = (PTArc) go;
    Point c = arc.getCenter();
    if (arc.isCircular()) {
      int r = arc.getRadius();
      return new EditPoint[] {
          new EditPoint(-1, c),
          // arcAngle must be before startAngle because if they coincide
          // still the arcAngle must be changeable
          new EditPoint(4, arc.getPointAtAngle(arc.getStartAngle()
              + arc.getTotalAngle())),
          new EditPoint(3, arc.getPointAtAngle(arc.getStartAngle())),
          // both arcAngle and startAngle must be before the radiuses
          // as those points may coincide
          new EditPoint(2, new Point(c.x - r, c.y)),
          new EditPoint(2, new Point(c.x + r, c.y)),
          new EditPoint(2, new Point(c.x, c.y - r)),
          new EditPoint(2, new Point(c.x, c.y + r)), };
    }

    int xr = arc.getXRadius();
    int yr = arc.getYRadius();
    return new EditPoint[] {
        new EditPoint(-1, c),
        new EditPoint(2, new Point(c.x - xr, c.y - yr)),
        new EditPoint(2, new Point(c.x - xr, c.y + yr)),
        new EditPoint(2, new Point(c.x + xr, c.y - yr)),
        new EditPoint(2, new Point(c.x + xr, c.y + yr)),
        // the above edit points are outside the arc, so
        // the order doesn't matter here.
        new EditPoint(4, arc.getPointAtAngle(arc.getStartAngle()
            + arc.getTotalAngle())),
        new EditPoint(3, arc.getPointAtAngle(arc.getStartAngle())), };

  } // getEditPoints

  /**
   * extract the properties from <i>props</i>. All components are set according
   * to these properties.
   */
  public void setProperties(XProperties props) {
    super.setProperties(props);
    // already covered:
    // color, depth, filled, fillColor, clockwise
    isBold.setSelected(props.getBoolProperty(PTArc.TYPE_LABEL + ".bold"));
    bwArrow.setSelected(props.getBoolProperty(PTArc.TYPE_LABEL + ".bwArrow"));
    isCircular.setSelected(props.getBoolProperty(PTArc.TYPE_LABEL + ".circle"));
    isClosed.setSelected(props.getBoolProperty(PTArc.TYPE_LABEL + ".closed"));
    fontName.setSelectedItem(props.getProperty(PTArc.TYPE_LABEL + ".fontName",
        "SansSerif"));
    fontSize.setSelectedItem(props.getProperty(PTArc.TYPE_LABEL + ".fontSize",
        "12"));
    fwArrow.setSelected(props.getBoolProperty(PTArc.TYPE_LABEL + ".fwArrow"));
    isItalic.setSelected(props.getBoolProperty(PTArc.TYPE_LABEL + ".italic"));
    if (textField != null)
      textField.setText(props.getProperty(PTArc.TYPE_LABEL + ".text"));
    textColorChooser.setColor(props.getColorProperty(PTArc.TYPE_LABEL
        + ".textColor", Color.black));
  }

  /**
   * writes the Editor's current permanent attributes to the Properties object.
   */
  public void getProperties(XProperties props) {
    super.getProperties(props);
    // already covered:
    // color, depth, filled, fillColor, clockwise
    props.put(PTArc.TYPE_LABEL + ".bold", isBold.isSelected());
    props.put(PTArc.TYPE_LABEL + ".bwArrow", bwArrow.isSelected());
    props.put(PTArc.TYPE_LABEL + ".circle", isCircular.isSelected());
    props.put(PTArc.TYPE_LABEL + ".closed", isClosed.isSelected());
    props.put(PTArc.TYPE_LABEL + ".fontName", fontName.getSelectedItem());
    props.put(PTArc.TYPE_LABEL + ".fontSize", fontSize.getSelectedItem());
    props.put(PTArc.TYPE_LABEL + ".fwArrow", fwArrow.isSelected());
    props.put(PTArc.TYPE_LABEL + ".italic", isItalic.isSelected());
    props.put(PTArc.TYPE_LABEL + ".text", textField.getText());
    props.put(PTArc.TYPE_LABEL + ".textColor", textColorChooser.getColor());
  }

  /**
   * creates a new Arc and uses the attributes of this Editor as default values.
   */
  public EditableObject createObject() {
    PTArc arc = new PTArc();
    storeAttributesInto(arc);
    return arc;
  }
  /**
   * extracts the Font from the components' settings.
   */
  Font storeFont() {
    String name = (String) fontName.getSelectedItem();
    String size = (String) fontSize.getSelectedItem();
    return new Font(name, Font.PLAIN 
        + (isBold.isSelected() ? Font.BOLD : 0)
        + (isItalic.isSelected() ? Font.ITALIC : 0), 
        getInt(size, 12));
  }

  /**
   * sets the components according to the font.
   */
  void extractFont(Font f) {
    fontName.setSelectedItem(f.getName());
    fontSize.setSelectedItem(String.valueOf(f.getSize()));
    isItalic.setSelected(f.isItalic());
    isBold.setSelected(f.isBold());
  }

  /**
   * applies the Editor's settings to the Arc by setting all of Arcs attributes
   * according to the components. Parameter must be of type <b>EditableObject</b>
   * and be casted inside the method.
   */
  protected void storeAttributesInto(EditableObject eo) {
    // don't forget to store the parent's attributes!
    super.storeAttributesInto(eo);
    PTArc arc = (PTArc) eo; // just a shortcut
    if (textField != null)
      arc.setText(textField.getText());
    else
      arc.setText("");
    arc.getTextComponent().setFont(storeFont());
    arc.setCircle(isCircular.isSelected());
    arc.setClosed(isClosed.isSelected());
    arc.setFWArrow(fwArrow.isSelected());
    arc.setBWArrow(bwArrow.isSelected());
    arc.setTextColor(textColorChooser.getColor());
    PTText textCompo = arc.getTextComponent();
    Font targetFont = storeFont();
    textCompo.setFont(targetFont);
    int width = 10;
    if (textField != null)
      width = Animal.getStringWidth(textField.getText(), targetFont);
    textCompo.setPosition(arc.getCenter().x - width / 2, arc.getCenter().y
        + targetFont.getSize() / 2);
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
   * makes the Editor reflect the Arc's attributes by setting all components
   * according to the attributes. Parameter must be of type <b>EditableObject</b>,
   * otherwise there may be problems with calls to super that can be difficult
   * to debug.
   */
  protected void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    PTArc arc = (PTArc) eo;
    textField.setText(arc.getTextComponent().getText());
    extractFont(arc.getTextComponent().getFont());
    isCircular.setSelected(arc.isCircular());
    isClosed.setSelected(arc.isClosed());
    fwArrow.setSelected(arc.hasFWArrow());
    bwArrow.setSelected(arc.hasBWArrow());
    textColorChooser.setColor(arc.getTextComponent().getColor());
  }

  /**
   * creates a secondary Editor for the given <b>EditableObject</b> and copies
   * all of the object's attributes into the components. We can rely on this
   * object always being a <b>PTArc</b>.
   */
  public Editor getSecondaryEditor(EditableObject eo) {
    ArcEditor result = new ArcEditor();
    result.extractAttributesFrom(eo);
    return result;
  }

  /**
   * arrows can only be set if the arc is neither closed nor filled
   */
  public void itemStateChanged(ItemEvent e) {
    super.itemStateChanged(e);
    PTArc arc = (PTArc) getCurrentObject();

    if (arc != null) {
      if (e.getSource() == isClosed) {
        // only closed arcs may be filled
        filledCB.setEnabled(isClosed.isSelected());

        // only open arcs may have arrows
        fwArrow.setEnabled(!isClosed.isSelected());
        bwArrow.setEnabled(!isClosed.isSelected());
        arc.setClosed(isClosed.isSelected());
      } // else if (e.getSource() == filled)

      if (!isClosed.isSelected())
        arc.setFilled(false);
      else
        arc.setFilled(filledCB.isSelected());

      if (e.getSource() == fwArrow)
        arc.setFWArrow(fwArrow.isSelected());

      if (e.getSource() == bwArrow)
        arc.setBWArrow(bwArrow.isSelected());

      // in either case, set fillColor
      // fillColor.setEnabled(closed.isSelected() && filled.isSelected());
      fillColorChooserButton.setEnabled(isClosed.isSelected()
          && filledCB.isSelected());

      if (Animation.get() != null)
        Animation.get().doChange();
      repaintNow();
    }
  }

  /**
   * handles the button press by calling a BoxPointer method.
   */
  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);

    PTArc arc = (PTArc) getCurrentObject();

    if (e.getSource() == textField)
      arc.setText(textField.getText());

    if ((e.getSource() == fontName) || (e.getSource() == fontSize)
        || (e.getSource() == isItalic) || (e.getSource() == isBold))
      arc.getTextComponent().setFont(storeFont());

//    if (e.getSource() == isItalic) {
//      fontIsItalic = !fontIsItalic;
//      String name = (String) fontName.getSelectedItem();
//      String size = (String) fontSize.getSelectedItem();
//      Font newFont = new Font(name, Font.PLAIN
//          + (isBold.isSelected() ? Font.BOLD : 0)
//          + (isItalic.isSelected() ? Font.ITALIC : 0), getInt(size, 12));
//      arc.getTextComponent().setFont(newFont);
//    }

//    if (e.getSource() == isBold) {
//      fontIsBold = !fontIsBold;
//      String name = (String) fontName.getSelectedItem();
//      String size = (String) fontSize.getSelectedItem();
//      Font newFont = new Font(name, Font.PLAIN
//          + (isBold.isSelected() ? Font.BOLD : 0)
//          + (isItalic.isSelected() ? Font.ITALIC : 0), getInt(size, 12));
//      arc.getTextComponent().setFont(newFont);
//    }

    if (e.getSource() == isCircular)
      arc.setCircle(isCircular.isSelected());

    if (e.getSource() == clockwise)
      arc.setClockwise(clockwise.isSelected());

    if (Animation.get() != null)
      Animation.get().doChange();
    repaintNow();
  }

  public void propertyChange(PropertyChangeEvent event) {
    super.propertyChange(event);
    PTArc arc = (PTArc) getCurrentObject();
    String eventName = event.getPropertyName();
    if ("textColor".equals(eventName))
      arc.setTextColor((Color) event.getNewValue());
    if (!event.getOldValue().equals(event.getNewValue())) {
      repaintNow();
      if (Animation.get() != null)
        Animation.get().doChange();
    }
  }

  public String getBasicType() {
    return PTArc.TYPE_LABEL;
  }
} // ArcEditor
