package animal.editor.graphics.meta;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultEditorKit.InsertTabAction;

import translator.AnimalTranslator;
import translator.ExtendedActionButton;
import translator.TranslatableGUIElement;
import animal.editor.Editor;
import animal.editor.IndexedContentChooser;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTIntMatrix;
import animal.graphics.meta.PTMatrix;
import animal.graphics.meta.PTMatrix.Alignment;
import animal.gui.DrawCanvas;
import animal.main.Animation;
import animal.misc.ColorChoice;
import animal.misc.ColorChooserAction;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;
import animal.misc.XProperties;

/**
 * The Editor for a matrix type
 */
// TODO Unterstuetzung von ALL bei den Indizes
public abstract class AbstractMatrixEditor extends AbstractTextEditor implements
    ActionListener, ChangeListener, ItemListener, PropertyChangeListener {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 2295451210977833754L;

  // elements of colorBoxes

  protected ColorChooserAction fillColorChooser;

  protected ColorChooserAction textColorChooser;

  protected ColorChooserAction cellHighlightColorChooser;

  protected ColorChooserAction elemHighlightColorChooser;

  protected JCheckBox filledCB;

  // elements of sizeBox
  protected JTextField rowCnt;

  // elements of marginAlignmentBox
  protected JSpinner topMarginSpinner, bottomMarginSpinner, leftMarginSpinner,
      rightMarginSpinner;

  protected JRadioButton leftRB, centerRB, rightRB, leftRowRB, centerRowRB,
      rightRowRB;

  protected IndexedContentChooser chooseIndex;

  public static final String MATRIX_SIZE_BL = "Matrix.rowCountBL";

  public static final String MATRIX_ROW_COUNT_LABEL = "Matrix.rowCountLabel";

  public static final String MATRIX_TEXT_OPERATIONS_LABEL = "Matrix.enterValueLabel";

  public static final String MATRIX_FONT_AND_STYLE_LABEL = "AbstractTextEditor.fontBL";

  public static final String MATRIX_MARGIN_AND_ALIGNMENT_BOX_LABEL = "Matrix.marginAlignmentBL";

  public static final String MATRIX_TOP_MARGIN_LABEL = "Matrix.topMarginLabel";

  public static final String MATRIX_BOTTOM_MARGIN_LABEL = "Matrix.bottomMarginLabel";

  public static final String MATRIX_LEFT_MARGIN_LABEL = "Matrix.leftMarginLabel";

  public static final String MATRIX_RIGHT_MARGIN_LABEL = "Matrix.rightMarginLabel";

  public static final String MATRIX_TEXT_ALIGNMENT_LABEL = "Matrix.textAlignmentLabel";

  public static final String MATRIX_LEFT_TEXT_ALIGNMENT = "leftAlignment";

  public static final String MATRIX_CENTERED_TEXT_ALIGNMENT = "centeredAlignment";

  public static final String MATRIX_RIGHT_TEXT_ALIGNMENT = "rightAlignment";

  public static final String MATRIX_ALIGNMENT_LABEL = "alignmentLabel";

  public static final String MATRIX_LEFT_ALIGNMENT = "leftAlignment";

  public static final String MATRIX_CENTERED_ALIGNMENT = "centeredAlignment";

  public static final String MATRIX_RIGHT_ALIGNMENT = "rightAlignment";

  public static final String MATRIX_METHOD_SET_TEXT = "Method.setText";

  public static final String MATRIX_METHOD_SET_HIGHLIGHTED = "Method.setHighlighted";

  public static final String MATRIX_METHOD_SET_UNHIGHLIGHTED = "Method.setUnhighlighted";

  public static final String MATRIX_METHOD_SET_ELEMENT_HIGHLIGHTED = "Method.setElemHighlighted";

  public static final String MATRIX_METHOD_SET_ELEMENT_UNHIGHLIGHTED = "Method.setElemUnhighlighted";

  public static final String MATRIX_METHOD_SET_VISIBLE = "Method.setVisible";

  public static final String MATRIX_METHOD_SET_INVISIBLE = "Method.setInvisible";

  public static final String MATRIX_METHOD_SET_OUTLINED = "Method.setOutlined";

  public static final String MATRIX_METHOD_SET_NOTOUTLINED = "Method.setNotOutlined";

  public static final String MATRIX_METHOD_SET_COLUMN_COUNT = "Matrix.methodSetColumnCount";

  public static final String MATRIX_METHOD_SET_DIAGONAL_DOWN = "Matrix.methodSetDiagonalDown";

  public static final String MATRIX_METHOD_SET_DIAGONAL_UP = "Matrix.methodSetDiagonalUp";

  public static final String MATRIX_INDEX_CHOOSER_LABEL = "IndexedContenChooser.selectObjects";

  public AbstractMatrixEditor() {
    super();
    // buildGUI();
  }

  protected void buildGUI() {
    initializeLayoutComponents();
    
    generateSizeOption();
    insertSeparator(MATRIX_INDEX_CHOOSER_LABEL, cp, generator);
    chooseIndex = new IndexedContentChooser(cp, MATRIX_INDEX_CHOOSER_LABEL);
    createTextOperationsChooser(MATRIX_TEXT_OPERATIONS_LABEL);
    generateColorOptions();
    generateHighlightColorOptions();
    generateTextMarginAndAlignmentOptions();
    createFontAndStyleChooser(MATRIX_FONT_AND_STYLE_LABEL);
    italic.addItemListener(this);
    bold.addItemListener(this);
    
    finishEditor(cp);
    
   // TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
    /*createSizeBox(generator);
//    chooseIndex = new IndexedContentChooser(MATRIX_INDEX_CHOOSER_LABEL);
    addBox(chooseIndex.getContentBox());
    addBox(generateTextOperationsBox(generator, MATRIX_TEXT_OPERATIONS_LABEL));
    createColorBoxes(generator);
    createTextMarginAndAlignmentBox(generator);
    addBox(generateFontAndStyleBox(generator, MATRIX_FONT_AND_STYLE_LABEL));
    italic.addItemListener(this);
    bold.addItemListener(this);*/

   // finishBoxes();
  }

 

  protected void setChooseIndexContent(PTGraphicObject object) {
    PTGraphicObject[] objects = new PTGraphicObject[1];
    objects[0] = object;
    Vector<String> methods = new Vector<String>();
    methods.add(PTMatrix.KIND_OF_OBJECT_CELL + " " + MATRIX_METHOD_SET_TEXT);
    methods.add(PTMatrix.KIND_OF_OBJECT_CELL + " "
        + MATRIX_METHOD_SET_HIGHLIGHTED);
    methods.add(PTMatrix.KIND_OF_OBJECT_CELL + " "
        + MATRIX_METHOD_SET_UNHIGHLIGHTED);
    methods.add(PTMatrix.KIND_OF_OBJECT_CELL + " "
        + MATRIX_METHOD_SET_ELEMENT_HIGHLIGHTED);
    methods.add(PTMatrix.KIND_OF_OBJECT_CELL + " "
        + MATRIX_METHOD_SET_ELEMENT_UNHIGHLIGHTED);
    methods.add(PTMatrix.KIND_OF_OBJECT_CELL + " " + MATRIX_METHOD_SET_VISIBLE);
    methods.add(PTMatrix.KIND_OF_OBJECT_CELL + " "
        + MATRIX_METHOD_SET_INVISIBLE);
    methods
        .add(PTMatrix.KIND_OF_OBJECT_CELL + " " + MATRIX_METHOD_SET_OUTLINED);
    methods.add(PTMatrix.KIND_OF_OBJECT_CELL + " "
        + MATRIX_METHOD_SET_NOTOUTLINED);
    methods.add(PTMatrix.KIND_OF_OBJECT_CELL + " "
        + MATRIX_METHOD_SET_DIAGONAL_DOWN);
    methods.add(PTMatrix.KIND_OF_OBJECT_CELL + " "
        + MATRIX_METHOD_SET_DIAGONAL_UP);
    methods.add(PTMatrix.KIND_OF_OBJECT_ROW + " "
        + MATRIX_METHOD_SET_COLUMN_COUNT);
    chooseIndex.setData(objects, methods);
  }

  protected void createTextMarginAndAlignmentBox(
      TranslatableGUIElement generator) {
    // create box
    Box marginAlignmentBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
        MATRIX_MARGIN_AND_ALIGNMENT_BOX_LABEL);
    // Box marginAlignmentBox = new Box(BoxLayout.PAGE_AXIS);
    // marginAlignmentBox.setBorder(new TitledBorder(null, "Margin and
    // Alignment",
    // TitledBorder.LEADING, TitledBorder.TOP));
    Box topBottomBox = new Box(BoxLayout.LINE_AXIS);
    // top margin spinner
    topBottomBox.add(generator.generateJLabel(MATRIX_TOP_MARGIN_LABEL));
    SpinnerNumberModel spinnerModelTopMargin = new SpinnerNumberModel(3, 0, 99,
        1);
    topMarginSpinner = new JSpinner(spinnerModelTopMargin);
    topMarginSpinner.addChangeListener(this);
    topBottomBox.add(topMarginSpinner);
    // bottom margin spinner
    topBottomBox.add(generator.generateJLabel(MATRIX_BOTTOM_MARGIN_LABEL));
    SpinnerNumberModel spinnerModelBottomMargin = new SpinnerNumberModel(3, 0,
        99, 1);
    bottomMarginSpinner = new JSpinner(spinnerModelBottomMargin);
    bottomMarginSpinner.addChangeListener(this);
    topBottomBox.add(bottomMarginSpinner);

    marginAlignmentBox.add(topBottomBox);
    // left margin spinner
    Box leftRightBox = new Box(BoxLayout.LINE_AXIS);
    leftRightBox.add(generator.generateJLabel(MATRIX_LEFT_MARGIN_LABEL));
    SpinnerNumberModel spinnerModelLeftMargin = new SpinnerNumberModel(3, 0,
        99, 1);
    leftMarginSpinner = new JSpinner(spinnerModelLeftMargin);
    leftMarginSpinner.addChangeListener(this);
    leftRightBox.add(leftMarginSpinner);
    // right margin spinner
    leftRightBox.add(generator.generateJLabel(MATRIX_RIGHT_MARGIN_LABEL));
    SpinnerNumberModel spinnerModelRightMargin = new SpinnerNumberModel(3, 0,
        99, 1);
    rightMarginSpinner = new JSpinner(spinnerModelRightMargin);
    rightMarginSpinner.addChangeListener(this);
    leftRightBox.add(rightMarginSpinner);

    marginAlignmentBox.add(leftRightBox);
    // Alignment group
    Box alignmentBox = new Box(BoxLayout.LINE_AXIS);
    ButtonGroup alignmentGroup = new ButtonGroup();
    leftRB = (JRadioButton) generator.generateJToggleButton(
        MATRIX_LEFT_TEXT_ALIGNMENT, null, this, true);
    leftRB.setSelected(true);
    leftRB.addItemListener(this);
    alignmentGroup.add(leftRB);
    centerRB = (JRadioButton) generator.generateJToggleButton(
        MATRIX_CENTERED_TEXT_ALIGNMENT, null, this, true);
    centerRB.addItemListener(this);
    centerRB.setSelected(false);
    alignmentGroup.add(centerRB);
    rightRB = (JRadioButton) generator.generateJToggleButton(
        MATRIX_RIGHT_TEXT_ALIGNMENT, null, this, true);
    rightRB.addItemListener(this);
    rightRB.setSelected(false);
    alignmentGroup.add(rightRB);

    alignmentBox.add(generator.generateJLabel(MATRIX_TEXT_ALIGNMENT_LABEL));

    alignmentBox.add(leftRB);
    alignmentBox.add(centerRB);
    alignmentBox.add(rightRB);
    marginAlignmentBox.add(alignmentBox);

    // RowAlignment group
    Box rowAlignmentBox = new Box(BoxLayout.LINE_AXIS);
    ButtonGroup rowAlignmentGroup = new ButtonGroup();
    leftRowRB = (JRadioButton) generator.generateJToggleButton(
        MATRIX_LEFT_ALIGNMENT, null, this, true);
    leftRowRB.setSelected(true);
    leftRowRB.addItemListener(this);
    rowAlignmentGroup.add(leftRowRB);
    centerRowRB = (JRadioButton) generator.generateJToggleButton(
        MATRIX_CENTERED_ALIGNMENT, null, this, true);
    centerRowRB.addItemListener(this);
    centerRowRB.setSelected(false);
    rowAlignmentGroup.add(centerRowRB);
    rightRowRB = (JRadioButton) generator.generateJToggleButton(
        MATRIX_RIGHT_ALIGNMENT, null, this, true);
    rightRowRB.addItemListener(this);
    rightRowRB.setSelected(false);
    rowAlignmentGroup.add(rightRowRB);

    rowAlignmentBox.add(generator.generateJLabel(MATRIX_ALIGNMENT_LABEL));
    rowAlignmentBox.add(leftRowRB);
    rowAlignmentBox.add(centerRowRB);
    rowAlignmentBox.add(rightRowRB);
    marginAlignmentBox.add(rowAlignmentBox);
    addBox(marginAlignmentBox);
  }
  
  private void generateTextMarginAndAlignmentOptions() {
    insertSeparator(MATRIX_MARGIN_AND_ALIGNMENT_BOX_LABEL, cp, generator);
    cp.add(generator.generateJLabel(MATRIX_TOP_MARGIN_LABEL), Editor.LAYOUT_PARAGRAPH_GAP);
    
    SpinnerNumberModel spinnerModelTopMargin = new SpinnerNumberModel(3, 0, 99,
        1);
    topMarginSpinner = new JSpinner(spinnerModelTopMargin);
    topMarginSpinner.addChangeListener(this);
    cp.add(topMarginSpinner);
    
    
    cp.add(generator.generateJLabel(MATRIX_BOTTOM_MARGIN_LABEL));
    SpinnerNumberModel spinnerModelBottomMargin = new SpinnerNumberModel(3, 0,
        99, 1);
    bottomMarginSpinner = new JSpinner(spinnerModelBottomMargin);
    bottomMarginSpinner.addChangeListener(this);
    cp.add(bottomMarginSpinner, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
    cp.add(generator.generateJLabel(MATRIX_LEFT_MARGIN_LABEL), Editor.LAYOUT_PARAGRAPH_GAP);
    
    SpinnerNumberModel spinnerModelLeftMargin = new SpinnerNumberModel(3, 0,
        99, 1);
    leftMarginSpinner = new JSpinner(spinnerModelLeftMargin);
    leftMarginSpinner.addChangeListener(this);
    cp.add(leftMarginSpinner);
    
    cp.add(generator.generateJLabel(MATRIX_RIGHT_MARGIN_LABEL));
    SpinnerNumberModel spinnerModelRightMargin = new SpinnerNumberModel(3, 0,
        99, 1);
    rightMarginSpinner = new JSpinner(spinnerModelRightMargin);
    rightMarginSpinner.addChangeListener(this);
    cp.add(rightMarginSpinner, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
    generateMarginAndAlignmentCheckBoxes();
  }
  
  private void generateMarginAndAlignmentCheckBoxes() {
    cp.add(generator.generateJLabel(MATRIX_TEXT_ALIGNMENT_LABEL), Editor.LAYOUT_PARAGRAPH_GAP);
    
    leftRB = (JRadioButton) generator.generateJToggleButton(
        MATRIX_LEFT_TEXT_ALIGNMENT, null, this, true);
    leftRB.setSelected(true);
    leftRB.addItemListener(this);
    cp.add(leftRB);
    
    centerRB = (JRadioButton) generator.generateJToggleButton(
        MATRIX_CENTERED_TEXT_ALIGNMENT, null, this, true);
    centerRB.addItemListener(this);
    centerRB.setSelected(false);
    cp.add(centerRB);
    
    rightRB = (JRadioButton) generator.generateJToggleButton(
        MATRIX_RIGHT_TEXT_ALIGNMENT, null, this, true);
    
    rightRB.addItemListener(this);
    rightRB.setSelected(false);
    cp.add(rightRB, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
    cp.add(generator.generateJLabel(MATRIX_ALIGNMENT_LABEL), Editor.LAYOUT_PARAGRAPH_GAP);
    
    leftRowRB = (JRadioButton) generator.generateJToggleButton(
        MATRIX_LEFT_ALIGNMENT, null, this, true);
    leftRowRB.setSelected(true);
    leftRowRB.addItemListener(this);
    cp.add(leftRowRB);
    centerRowRB = (JRadioButton) generator.generateJToggleButton(
        MATRIX_CENTERED_ALIGNMENT, null, this, true);
    centerRowRB.addItemListener(this);
    centerRowRB.setSelected(false);
    cp.add(centerRowRB);
    rightRowRB = (JRadioButton) generator.generateJToggleButton(
        MATRIX_RIGHT_ALIGNMENT, null, this, true);
    rightRowRB.addItemListener(this);
    rightRowRB.setSelected(false);
    cp.add(rightRowRB, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
  }
  
  private void generateSizeOption() {
    insertSeparator(MATRIX_ROW_COUNT_LABEL, cp, generator);
    cp.add(generator.generateJLabel(MATRIX_ROW_COUNT_LABEL), Editor.LAYOUT_PARAGRAPH_GAP);
    rowCnt = new JTextField();
    rowCnt.addActionListener(this);
    rowCnt.setText("1");
    cp.add(rowCnt, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
  }
  
  private void generateColorOptions() {
    insertSeparator("GenericEditor.colorBL", cp, generator);
    cp.add(generator.generateJLabel("GenericEditor.colorLabel"), Editor.LAYOUT_PARAGRAPH_GAP);
    
    Color initialColor = (getCurrentObject(false) == null) ? Color.black
        : ((PTIntMatrix) getCurrentObject(false)).getColor();
    colorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(initialColor), "color", AnimalTranslator
        .translateMessage("GenericEditor.chooseColor",
            new Object[] { AnimalTranslator
                .translateMessage("GenericEditor.chooseColor") }), initialColor);
    cp.add(new ExtendedActionButton(colorChooser,KeyEvent.VK_C), Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
      
    cp.add(generator.generateJLabel("AbstractArrayEditor.fontColorLabel"), Editor.LAYOUT_PARAGRAPH_GAP);
    
    initialColor = (getCurrentObject(false) == null) ? Color.black
        : ((PTIntMatrix) getCurrentObject(false)).getTextColor();
    textColorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(initialColor), "textColor", AnimalTranslator
        .translateMessage("GenericEditor.chooseColor",
            new Object[] { AnimalTranslator.translateMessage("fontColor") }),
        initialColor);
    cp.add(new ExtendedActionButton(textColorChooser, KeyEvent.VK_T), Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
    cp.add(generator.generateJLabel("GenericEditor.fillColorLabel"), Editor.LAYOUT_PARAGRAPH_GAP);
    
    initialColor = (getCurrentObject(false) == null) ? Color.white
        : ((PTIntMatrix) getCurrentObject(false)).getFillColor();
    fillColorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(initialColor), "fillColor", AnimalTranslator
        .translateMessage("GenericEditor.chooseColor",
            new Object[] { AnimalTranslator
                .translateMessage("GenericEditor.fillColor") }), initialColor);
    cp.add(new ExtendedActionButton(fillColorChooser, KeyEvent.VK_F), Editor.LAYOUT_PARAGRAPH_GAP);
    filledCB = generator.generateJCheckBox("GenericEditor.filled", null, this);
    filledCB.addItemListener(this);
    
    cp.add(filledCB, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
  }
  
  private void generateHighlightColorOptions() {
    insertSeparator("AbstractArrayEditor.highlightColors", cp, generator);
    
    cp.add(generator.generateJLabel("highlightColorLabel"), Editor.LAYOUT_PARAGRAPH_GAP);

    Color initialColor = (getCurrentObject(false) == null) ? Color.yellow
        : ((PTIntMatrix) getCurrentObject(false)).getHighlightColor();
    cellHighlightColorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(initialColor), "highlightColor",
        AnimalTranslator
            .translateMessage("GenericEditor.chooseColor",
                new Object[] { AnimalTranslator
                    .translateMessage("highlightColor") }), initialColor);
    cp.add(new ExtendedActionButton(cellHighlightColorChooser, KeyEvent.VK_T), Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
    cp.add(generator.generateJLabel("AbstractIndexedStructureEditor.elemHighlightColorLabel"), Editor.LAYOUT_PARAGRAPH_GAP);

    initialColor = (getCurrentObject(false) == null) ? Color.red
        : ((PTIntMatrix) getCurrentObject(false)).getElemHighlightColor();
    elemHighlightColorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(initialColor), "elemHighlightColor", AnimalTranslator
        .translateMessage("GenericEditor.chooseColor",
            new Object[] { AnimalTranslator
                .translateMessage("AbstractIndexedStructureEditor.elemHighlightColor") }), initialColor);
    cp.add(new ExtendedActionButton(elemHighlightColorChooser, KeyEvent.VK_T), Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
  }

  private void createSizeBox(TranslatableGUIElement generator) {
    Box sizeBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
        MATRIX_SIZE_BL);
    Box firstRow = new Box(BoxLayout.LINE_AXIS);
    firstRow.add(generator.generateJLabel(MATRIX_ROW_COUNT_LABEL));
    rowCnt = new JTextField(9);
    rowCnt.addActionListener(this);
    rowCnt.setText("1");
    firstRow.add(rowCnt);
    sizeBox.add(firstRow);
    addBox(sizeBox);
  }
  
  

  private void createColorBoxes(TranslatableGUIElement generator) {
    Box colorBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
        "GenericEditor.colorBL");

    JPanel panel = new JPanel();
    GridBagLayout gbl = new GridBagLayout();
    panel.setLayout(gbl);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2);

    // Line Color
    Box lineColorBox = new Box(BoxLayout.LINE_AXIS);
    JLabel colorLabel = generator.generateJLabel("GenericEditor.colorLabel");
    lineColorBox.add(colorLabel);

    Color initialColor = (getCurrentObject(false) == null) ? Color.black
        : ((PTIntMatrix) getCurrentObject(false)).getColor();
    colorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(initialColor), "color", AnimalTranslator
        .translateMessage("GenericEditor.chooseColor",
            new Object[] { AnimalTranslator
                .translateMessage("GenericEditor.chooseColor") }), initialColor);
    ExtendedActionButton button = new ExtendedActionButton(colorChooser,
        KeyEvent.VK_C);
    lineColorBox.add(button);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbl.setConstraints(lineColorBox, gbc);
    panel.add(lineColorBox);
    // Fill Color
    Box fillColorBox = new Box(BoxLayout.LINE_AXIS);
    JLabel fillColorLabel = generator
        .generateJLabel("GenericEditor.fillColorLabel");
    fillColorBox.add(fillColorLabel);

    initialColor = (getCurrentObject(false) == null) ? Color.white
        : ((PTIntMatrix) getCurrentObject(false)).getFillColor();
    fillColorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(initialColor), "fillColor", AnimalTranslator
        .translateMessage("GenericEditor.chooseColor",
            new Object[] { AnimalTranslator
                .translateMessage("GenericEditor.fillColor") }), initialColor);
    fillColorBox.add(new ExtendedActionButton(fillColorChooser, KeyEvent.VK_F));
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbl.setConstraints(fillColorBox, gbc);
    panel.add(fillColorBox);
    // Text Color
    Box textColorBox = new Box(BoxLayout.LINE_AXIS);
    textColorBox.add(generator.generateJLabel("AbstractArrayEditor.fontColorLabel"));

    initialColor = (getCurrentObject(false) == null) ? Color.black
        : ((PTIntMatrix) getCurrentObject(false)).getTextColor();
    textColorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(initialColor), "textColor", AnimalTranslator
        .translateMessage("GenericEditor.chooseColor",
            new Object[] { AnimalTranslator.translateMessage("fontColor") }),
        initialColor);
    textColorBox.add(new ExtendedActionButton(textColorChooser, KeyEvent.VK_T));
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbl.setConstraints(textColorBox, gbc);
    panel.add(textColorBox);
    // CellHighlightColor
    Box cellHighlightColorBox = new Box(BoxLayout.LINE_AXIS);
    cellHighlightColorBox.add(generator.generateJLabel("highlightColorLabel"));

    initialColor = (getCurrentObject(false) == null) ? Color.yellow
        : ((PTIntMatrix) getCurrentObject(false)).getHighlightColor();
    cellHighlightColorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(initialColor), "highlightColor",
        AnimalTranslator
            .translateMessage("GenericEditor.chooseColor",
                new Object[] { AnimalTranslator
                    .translateMessage("highlightColor") }), initialColor);
    cellHighlightColorBox.add(new ExtendedActionButton(
        cellHighlightColorChooser, KeyEvent.VK_T));
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbl.setConstraints(cellHighlightColorBox, gbc);
    panel.add(cellHighlightColorBox);
    // ElemHighlightColor
    Box elemHighlightColorBox = new Box(BoxLayout.LINE_AXIS);
    elemHighlightColorBox.add(generator
        .generateJLabel("AbstractIndexedStructureEditor.elemHighlightColorLabel"));

    initialColor = (getCurrentObject(false) == null) ? Color.red
        : ((PTIntMatrix) getCurrentObject(false)).getElemHighlightColor();
    elemHighlightColorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(initialColor), "elemHighlightColor", AnimalTranslator
        .translateMessage("GenericEditor.chooseColor",
            new Object[] { AnimalTranslator
                .translateMessage("AbstractIndexedStructureEditor.elemHighlightColor") }), initialColor);
    elemHighlightColorBox.add(new ExtendedActionButton(
        elemHighlightColorChooser, KeyEvent.VK_T));
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbl.setConstraints(elemHighlightColorBox, gbc);
    panel.add(elemHighlightColorBox);

    filledCB = generator.generateJCheckBox("GenericEditor.filled", null, this);
    filledCB.addItemListener(this);

    gbc.gridx = 1;
    gbc.gridy = 2;
    gbl.setConstraints(filledCB, gbc);
    panel.add(filledCB);
    colorBox.add(panel);
    addBox(colorBox);
  }

  public void setProperties(XProperties props) {
    // colorChooser.setColor(props.getColorProperty(PTIntMatrix.POINT_COLOR,
    // Color.black));
  }

  public void getProperties(XProperties props) {
    // props.put(PTPoint.POINT_COLOR, colorChooser.getColor());
  }

  public int pointsNeeded() {
    return 1;
  }

  public boolean nextPoint(int num, Point p) {
    if (num == 1)
      ((PTMatrix) getCurrentObject()).setLocation(p);
    return true;
  }

  public int getMinDist(PTGraphicObject go, Point p) {
    return MSMath.dist(p, go.getBoundingBox());
  }

  public EditPoint[] getEditPoints(PTGraphicObject go) {
    Rectangle box = go.getBoundingBox();
    return new EditPoint[] {
        new EditPoint(-1, ((PTMatrix) go).getLocation()),
        new EditPoint(-2, new Point(box.x + box.width, box.y)),
        new EditPoint(-3, new Point(box.x + box.width, box.y + box.height)),
        new EditPoint(-4, new Point(box.x + (box.width / 2), box.y
            + (box.height / 2))),
        new EditPoint(-5, new Point(box.x, box.y + box.height)) };
  }

  protected void storeAttributesInto(EditableObject eo) {
    super.storeAttributesInto(eo);
    PTMatrix matrix = (PTMatrix) eo;
    matrix.setColor(colorChooser.getColor());
    matrix.setHighlightColor(cellHighlightColorChooser.getColor());
    matrix.setElemHighlightColor(elemHighlightColorChooser.getColor());
    matrix.setTextColor(textColorChooser.getColor());
    matrix.setFillColor(fillColorChooser.getColor());
    matrix.setFilled(filledCB.isSelected());
    matrix.setFont(storeFont());
    matrix.setMargin(0, ((SpinnerNumberModel) topMarginSpinner.getModel())
        .getNumber().intValue());
    matrix.setMargin(1, ((SpinnerNumberModel) rightMarginSpinner.getModel())
        .getNumber().intValue());
    matrix.setMargin(2, ((SpinnerNumberModel) bottomMarginSpinner.getModel())
        .getNumber().intValue());
    matrix.setMargin(3, ((SpinnerNumberModel) leftMarginSpinner.getModel())
        .getNumber().intValue());
    handleIndexedContentPropertyChange(matrix);
    storeAlignment(matrix);
  }

  protected void storeAlignment(PTMatrix matrix) {
    if (leftRB.isSelected())
      matrix.setTextAlignment(Alignment.LEFT);
    else if (centerRB.isSelected())
      matrix.setTextAlignment(Alignment.CENTER);
    else if (rightRB.isSelected())
      matrix.setTextAlignment(Alignment.RIGHT);
    if (leftRowRB.isSelected())
      matrix.setRowAlignment(Alignment.LEFT);
    else if (centerRowRB.isSelected())
      matrix.setRowAlignment(Alignment.CENTER);
    else if (rightRowRB.isSelected())
      matrix.setRowAlignment(Alignment.RIGHT);

  }

  protected Font storeFont() {
    String name = (String) fontName.getSelectedItem();
    String size = (String) fontSize.getSelectedItem();
    int fontStyle = Font.PLAIN;
    if (italic.isSelected())
      fontStyle |= Font.ITALIC;
    if (bold.isSelected())
      fontStyle |= Font.BOLD;

    return new Font(name, fontStyle, getInt(size, 12));
  }

  protected void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    PTMatrix matrix = (PTMatrix) eo;
    colorChooser.setColor(matrix.getColor());
    cellHighlightColorChooser.setColor(matrix.getHighlightColor());
    elemHighlightColorChooser.setColor(matrix.getElemHighlightColor());
    textColorChooser.setColor(matrix.getTextColor());
    fillColorChooser.setColor(matrix.getFillColor());
    rowCnt.setText(String.valueOf(matrix.getRowCount()));
    ((SpinnerNumberModel) topMarginSpinner.getModel()).setValue(matrix
        .getMargin(0));
    ((SpinnerNumberModel) rightMarginSpinner.getModel()).setValue(matrix
        .getMargin(1));
    ((SpinnerNumberModel) bottomMarginSpinner.getModel()).setValue(matrix
        .getMargin(2));
    ((SpinnerNumberModel) leftMarginSpinner.getModel()).setValue(matrix
        .getMargin(3));
    extractAlignment(matrix);
    filledCB.setSelected(matrix.isFilled());
    extractFont(matrix.getFont());
    setChooseIndexContent(matrix);
  }

  private void extractAlignment(PTMatrix matrix) {
    Alignment textAlignment = matrix.getRowAlignment(); //TODO was matrix.getTextAlignment();
    if (textAlignment == Alignment.LEFT) {
      leftRB.setSelected(true);
    } else if (textAlignment == Alignment.CENTER) {
      centerRB.setSelected(true);
    } else if (textAlignment == Alignment.RIGHT) {
      rightRB.setSelected(true);
    }
    Alignment rowAlignment = matrix.getRowAlignment();
    if (rowAlignment == Alignment.LEFT) {
      leftRowRB.setSelected(true);
    } else if (rowAlignment == Alignment.CENTER) {
      centerRowRB.setSelected(true);
    } else if (rowAlignment == Alignment.RIGHT) {
      rightRowRB.setSelected(true);
    }
  }

//  void extractFont(Font f) {
//    fontName.setSelectedItem(f.getName());
//    fontSize.setSelectedItem(String.valueOf(f.getSize()));
//    italic.setSelected(f.isItalic());
//    bold.setSelected(f.isBold());
//  }

  public String getStatusLineMsg() {
    return AnimalTranslator.translateMessage(getBasicType()
        + "Editor.statusLine",
        new Object[] { DrawCanvas.translateDrawButton(),
            DrawCanvas.translateFinishButton(),
            DrawCanvas.translateCancelButton() });
  }

  public void propertyChange(PropertyChangeEvent event) {
    PTMatrix matrix = (PTMatrix) getCurrentObject();
    String eventName = event.getPropertyName();
    if (matrix != null) {
      if ("color".equals(eventName))
        matrix.setColor((Color) event.getNewValue());
      if ("fillColor".equals(eventName))
        matrix.setFillColor((Color) event.getNewValue());
      if ("textColor".equals(eventName))
        matrix.setTextColor((Color) event.getNewValue());
      if ("highlightColor".equals(eventName))
        matrix.setHighlightColor((Color) event.getNewValue());
      if ("elemHighlightColor".equals(eventName))
        matrix.setElemHighlightColor((Color) event.getNewValue());
      if (!event.getOldValue().equals(event.getNewValue())) {
        repaintNow();
        if (Animation.get() != null)
          Animation.get().doChange();
      }
    }
  }

  public void itemStateChanged(ItemEvent e) {
    PTMatrix matrix = (PTMatrix) getCurrentObject();
    if (matrix != null) {
      if (e.getSource() == filledCB) {
        matrix.setFilled(filledCB.isSelected());
      } else if (e.getSource() == leftRB) {
        matrix.setTextAlignment(Alignment.LEFT);
      } else if (e.getSource() == centerRB) {
        matrix.setTextAlignment(Alignment.CENTER);
      } else if (e.getSource() == rightRB) {
        matrix.setTextAlignment(Alignment.RIGHT);
      } else if (e.getSource() == leftRowRB) {
        matrix.setRowAlignment(Alignment.LEFT);
      } else if (e.getSource() == centerRowRB) {
        matrix.setRowAlignment(Alignment.CENTER);
      } else if (e.getSource() == rightRowRB) {
        matrix.setRowAlignment(Alignment.RIGHT);
      } else if (e.getSource() == bold || e.getSource() == italic) {
        matrix.setFont(storeFont());
      }
    }
    Animation.get().doChange();
    repaintNow();
  }

  public void stateChanged(ChangeEvent e) {
    if (null != getCurrentObject()) {
      PTMatrix matrix = (PTMatrix) getCurrentObject();
      if (e.getSource() == topMarginSpinner) {
        matrix.setMargin(0, ((SpinnerNumberModel) topMarginSpinner.getModel())
            .getNumber().intValue());
      } else if (e.getSource() == bottomMarginSpinner) {
        matrix.setMargin(2, ((SpinnerNumberModel) bottomMarginSpinner
            .getModel()).getNumber().intValue());
      } else if (e.getSource() == leftMarginSpinner) {
        matrix.setMargin(3, ((SpinnerNumberModel) leftMarginSpinner.getModel())
            .getNumber().intValue());
      } else if (e.getSource() == rightMarginSpinner) {
        matrix.setMargin(1,
            ((SpinnerNumberModel) rightMarginSpinner.getModel()).getNumber()
                .intValue());
      }
      Animation.get().doChange();
      repaintNow();

    }
  }

  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);
    if (null != getCurrentObject()) {
      PTMatrix matrix = (PTMatrix) getCurrentObject();
      if (e.getSource() == rowCnt) {
        matrix.setRowCount(getInt(rowCnt.getText(), matrix.getRowCount()));
        chooseIndex.updateIndexSets();
      } else if (e.getSource() == textField) {
        handleIndexedContentPropertyChange(matrix);
      } else if (e.getSource() == fontName || e.getSource() == fontSize) {
        matrix.setFont(storeFont());
      }
      Animation.get().doChange();
      repaintNow();
    }
  }

  protected void handleIndexedContentPropertyChange(PTMatrix matrix) {
    Vector<Integer> indices = this.chooseIndex.getSelectedIndices();
    if (PTMatrix.KIND_OF_OBJECT_ROW.equals(chooseIndex
        .getSelectedKindOfObject())) {
      if (indices.size() == 1
          && chooseIndex.getSelectedMethod().equals(
              PTMatrix.KIND_OF_OBJECT_ROW + " "
                  + MATRIX_METHOD_SET_COLUMN_COUNT)) {
        int r = indices.get(0);
        if (r == -1) {
          for (int row = 0; row < matrix.getRowCount(); ++row)
            matrix.setColumnCount(row, getInt(textField.getText(), matrix
                .getColumnCount(row)));
        } else
          matrix.setColumnCount(r, getInt(textField.getText(), matrix
              .getColumnCount(r)));
        chooseIndex.updateIndexSets();
      }
    } else if (PTMatrix.KIND_OF_OBJECT_CELL.equals(chooseIndex
        .getSelectedKindOfObject())) {
      if (indices.size() == 2) {
        int r = indices.get(0);
        int c = indices.get(1);
        if (r == -1) {
          for (int row = 0; row < matrix.getRowCount(); row++) {
            if (c == -1) {
              for (int column = 0; column < matrix.getColumnCount(row); column++) {
                handleChosenMethod(matrix, row, column);
              }
            } else {
              handleChosenMethod(matrix, row, c);
            }
          }
        } else if (c == -1) {
          for (int column = 0; column < matrix.getColumnCount(r); column++) {
            handleChosenMethod(matrix, r, column);
          }
        } else {
          handleChosenMethod(matrix, r, c);
        }
      }
    }

  }

  protected void handleChosenMethod(PTMatrix matrix, int row, int column) {
    String selectedMethod = this.chooseIndex.getSelectedMethod();
    String kindOfObject = chooseIndex.getSelectedKindOfObject();
    if (selectedMethod.equals(kindOfObject + " " + MATRIX_METHOD_SET_TEXT)) {
      setDataAt(row, column, textField.getText(), matrix);
    } else if (selectedMethod.equals(kindOfObject + " "
        + MATRIX_METHOD_SET_HIGHLIGHTED)) {
      matrix.setHighlighted(row, column, true);
    } else if (selectedMethod.equals(kindOfObject + " "
        + MATRIX_METHOD_SET_UNHIGHLIGHTED)) {
      matrix.setHighlighted(row, column, false);
    } else if (selectedMethod.equals(kindOfObject + " "
        + MATRIX_METHOD_SET_ELEMENT_HIGHLIGHTED)) {
      matrix.setElementHighlighted(row, column, true);
    } else if (selectedMethod.equals(kindOfObject + " "
        + MATRIX_METHOD_SET_ELEMENT_UNHIGHLIGHTED)) {
      matrix.setElementHighlighted(row, column, false);
    } else if (selectedMethod.equals(kindOfObject + " "
        + MATRIX_METHOD_SET_VISIBLE)) {
      matrix.setVisible(row, column, true);
    } else if (selectedMethod.equals(kindOfObject + " "
        + MATRIX_METHOD_SET_INVISIBLE)) {
      matrix.setVisible(row, column, false);
    } else if (selectedMethod.equals(kindOfObject + " "
        + MATRIX_METHOD_SET_OUTLINED)) {
      matrix.setOutlined(row, column, true);
    } else if (selectedMethod.equals(kindOfObject + " "
        + MATRIX_METHOD_SET_NOTOUTLINED)) {
      matrix.setOutlined(row, column, false);
    } else if (selectedMethod.equals(kindOfObject + " "
        + MATRIX_METHOD_SET_DIAGONAL_DOWN)) {
      matrix.setDiagonalElements(row, column, textField.getText(), true);
    } else if (selectedMethod.equals(kindOfObject + " "
        + MATRIX_METHOD_SET_DIAGONAL_UP)) {
      matrix.setDiagonalElements(row, column, textField.getText(), false);
    }
  }

  protected abstract void setDataAt(int row, int column, String text,
      PTMatrix matrix);
}
