/**
 * The Editor for a PTGraph
 * @see animal.main.graphics.PTGraph
 *
 * @author Pierre Villette
 */

package animal.editor.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import translator.AnimalTranslator;
import translator.ExtendedActionButton;
import translator.TranslatableGUIElement;
import animal.editor.Editor;
import animal.editor.graphics.meta.AbstractTextEditor;
import animal.graphics.PTGraph;
import animal.graphics.PTGraphicObject;
import animal.gui.DrawCanvas;
import animal.main.Animal;
import animal.main.Animation;
import animal.misc.ColorChoice;
import animal.misc.ColorChooserAction;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;
import animal.misc.XProperties;

public class GraphEditor extends AbstractTextEditor implements ChangeListener,
    ActionListener, PropertyChangeListener, KeyListener {

  /**
   * 
   */
  private static final long serialVersionUID = 4686563153442163179L;

  private ColorChooserAction //colorChooser, 
  highlightColorChooser,
      elemHighlightColorChooser, outlineColorChooser, fontColorNodeChooser,
      fontColorEdgeChooser;

  private JTextField nodeSize, contentNode, contentEdge, xNodePosition,
      yNodePosition;

  private JSpinner nodeSpinner, edgeStartSpinner, edgeEndSpinner,
      nodePositionSpinner;

  private JComboBox<String> fontChooserEdge, fontSizeEdge;

  private JCheckBox weighted, directed, showIndices, edgeBold, edgeItalic;

  public static final String BG_COLOR = "bgColorLabel";

  public static final String HIGHLIGHT_COLOR = "AbstractIndexedStructureEditor.hlColorLabel";

  public static final String ELEM_HIGHLIGHT_COLOR = "AbstractIndexedStructureEditor.elemHighlightColorLabel";

  public static final String OUTLINE_COLOR = "outlineColorLabel";

  public static final String FONT_COLOR_NODE = "fontColorLabelNode";

  public static final String FONT_COLOR_EDGE = "fontColorLabelEdge";

  public static final String SIZE = "size";

  public static final String GRAPH_CELL_NODE = "graphCellNode";

  public static final String GRAPH_CELL_EDGE_START = "graphCellEdgeStart";

  public static final String GRAPH_CELL_EDGE_END = "graphCellEdgeEnd";

  public static final String GRAPH_CONTENTS = "graphContents";

  public static final String GRAPH_COLORS = "graphColors";

  public static final String GRAPH_OPTIONS = "options";

  public static final String CONTENT_NODE = "cellContentNode";

  public static final String CONTENT_EDGE = "cellContentEdge";

  public static final String FONT_LABEL_NODE = "fontLabelNode";

  public static final String FONT_LABEL_EDGE = "fontLabelEdge";

  public static final String FONT = "font";

  public static final String FONT_SIZE_NODE = "fontSizeLabelNode";

  public static final String FONT_SIZE_EDGE = "fontSizeLabelEdge";

  public static final String WEIGHT_LABEL = "weight";

  public static final String DIRECTION_LABEL = "direction";

  public static final String SHOW_INDICES_LABEL = "AbstractIndexedStructureEditor.showIndices";

  public static final String POSITION_NODE_LABEL = "positionNodeLabel";

  public static final String X_POSITION_NODE_LABEL = "xPositionNodeLabel";

  public static final String Y_POSITION_NODE_LABEL = "yPositionNodeLabel";

  public static final String POSITION_LABEL = "positionLabel";

  /**
   * This is used in secondaryEditor to prohibit entry and thus change of
   * arraySize as an array is commonly understood to be of static size, and
   * cannot be resized while being used
   */
  // private static boolean firstEdit = true;
  /**
   * Construct a new GraphEditor window
   */
  public GraphEditor() {
    super();
  }

 
  protected void buildGUI() {
    initializeLayoutComponents();
    generateSizeSetting();
    generateGraphOptions();
    generateColorOptions();
    //generateEdgeFont();
    createFontAndStyleChooser("fontNodeBL");
    generateEdgeFont();
    //createFontAndStyleChooser("fontEdgeBL");
    finishEditor(cp);
    //finish(generator, cp);
    
    /*
    
    TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();
    // add array size selection box
   addBox(createGraphSizeBox(generator));

    // create options box (directed, weighted, show indices)
    addBox(createOptionsBox(generator));

    // add a box for the colors
    addBox(createColorBox(generator));

    // add a box for the font choice (nodes)
    addBox(generateFontAndStyleBox(generator, "fontNodeBL"));
    
    // add a box for the font choice (edges)
    addBox(generateEdgeFontAndStyleBox(generator, "fontEdgeBL"));

    finishBoxes();
    */
  }

  /**
   * Constructor used by the secondary editor window. This one is (and must be)
   * different from the default constructor, because by definition the array
   * size is static, so the according tab is missing in this version of the
   * GraphEditor window.
   * 
   * @param i
   *          the size of the Graph (nodes, edges) used to determine the
   *          correct indices of the array cells
   */
  private GraphEditor(int i) {
    super();
    
    initializeLayoutComponents();
    generateSizeSetting();
    generateGraphOptions();
    generateNodePositions(i);
    generatePositions(i);
    generateColorOptions();
    createFontAndStyleChooser("fontNodeBL");
    //createFontAndStyleChooser("fontEdgeBL");
    //generateFontSettings("fontNodeBL");
    //generateFontSettings("fontEdgeBL");
    finishEditor(cp);

    /*
    TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();

    // add array size selection box
    addBox(createGraphSizeBox(generator));
    
    //generateNodePositions(i);
    
    addBox(createNodePositionBox(generator, i));
    addBox(createPositionsBox(generator, i));

    // create options box (directed, weighted, show indices)
    addBox(createOptionsBox(generator));

    // add a box for the colors
    addBox(createColorBox(generator));

    // add a box for the font choice (nodes)
    addBox(generateFontAndStyleBox(generator, "fontNodeBL"));
    
    // add a box for the font choice (edges)
    addBox(generateEdgeFontAndStyleBox(generator, "fontEdgeBL"));

    finishBoxes();*/
  }

  protected Box createNodePositionBox(TranslatableGUIElement generator,
      int graphSize) {

    Box contentBox = generator.generateBorderedBox(BoxLayout.LINE_AXIS,
        "nodePosBL");
    // Box lineBox = new Box(BoxLayout.LINE_AXIS);
    contentBox.add(generator.generateJLabel(POSITION_NODE_LABEL));
    SpinnerNumberModel spinnerModelPositionNode = new SpinnerNumberModel(0, 0,
        graphSize - 1, 1);
    nodePositionSpinner = new JSpinner(spinnerModelPositionNode);
    contentBox.add(nodePositionSpinner);
    nodePositionSpinner.addChangeListener(this);
    contentBox.add(new JLabel(" of " + String.valueOf(graphSize)));

    // contentBox.add(lineBox);
    // Box secondBox = new Box(BoxLayout.LINE_AXIS);
    contentBox.add(generator.generateJLabel(X_POSITION_NODE_LABEL));
    String textNodePositionX = (getCurrentObject(false) == null) ? "" : String
        .valueOf(((PTGraph) getCurrentObject(false))
            .getXNode(spinnerModelPositionNode.getNumber().intValue()));
    xNodePosition = new JTextField(15);
    xNodePosition.setText(textNodePositionX);
    xNodePosition.addKeyListener(this);
    contentBox.add(xNodePosition);

    contentBox.add(generator.generateJLabel(Y_POSITION_NODE_LABEL));
    String textNodePositionY = (getCurrentObject(false) == null) ? "" : String
        .valueOf(((PTGraph) getCurrentObject(false))
            .getYNode(spinnerModelPositionNode.getNumber().intValue()));
    yNodePosition = new JTextField(15);
    yNodePosition.setText(textNodePositionY);
    yNodePosition.addKeyListener(this);
    contentBox.add(yNodePosition);
    return contentBox;
  }
  
  private void generateNodePositions(int graphSize) {
    insertSeparator("nodePosBL", cp, generator);
    cp.add(generator.generateJLabel(POSITION_NODE_LABEL), Editor.LAYOUT_PARAGRAPH_GAP);
    
    SpinnerNumberModel spinnerModelPositionNode = new SpinnerNumberModel(0, 0,
        graphSize - 1, 1);
    
    nodePositionSpinner = new JSpinner(spinnerModelPositionNode);
    nodePositionSpinner.addChangeListener(this);
    cp.add(nodePositionSpinner);
    
    cp.add(new JLabel(" of " + String.valueOf(graphSize)), Editor.LAYOUT_WRAP);
    
    String textNodePositionX = (getCurrentObject(false) == null) ? "" : String
        .valueOf(((PTGraph) getCurrentObject(false))
            .getXNode(spinnerModelPositionNode.getNumber().intValue()));
    
    cp.add(generator.generateJLabel(X_POSITION_NODE_LABEL), Editor.LAYOUT_PARAGRAPH_GAP);
    
    xNodePosition = new JTextField(15);
    xNodePosition.setText(textNodePositionX);
    xNodePosition.addKeyListener(this);
    
    cp.add(xNodePosition, Editor.LAYOUT_WRAP);
    
    cp.add(generator.generateJLabel(Y_POSITION_NODE_LABEL), Editor.LAYOUT_PARAGRAPH_GAP);
    
    String textNodePositionY = (getCurrentObject(false) == null) ? "" : String
        .valueOf(((PTGraph) getCurrentObject(false))
            .getYNode(spinnerModelPositionNode.getNumber().intValue()));
    yNodePosition = new JTextField(15);
    yNodePosition.setText(textNodePositionY);
    yNodePosition.addKeyListener(this);
    cp.add(yNodePosition, Editor.LAYOUT_WRAP);
  }
  
  private void generatePositions(int graphSize) {
    insertSeparator("graphPosBL", cp, generator);
    cp.add(generator.generateJLabel(GRAPH_CELL_NODE), Editor.LAYOUT_PARAGRAPH_GAP);
    
    SpinnerNumberModel spinnerModelNode = new SpinnerNumberModel(0, 0,
        graphSize - 1, 1);
    nodeSpinner = new JSpinner(spinnerModelNode);
    nodeSpinner.addChangeListener(this);
    cp.add(nodeSpinner);
    
    cp.add(new JLabel(" of " + String.valueOf(graphSize)), Editor.LAYOUT_WRAP);
    
    cp.add(generator.generateJLabel(CONTENT_NODE), Editor.LAYOUT_PARAGRAPH_GAP);
    
    String textNode = (getCurrentObject(false) == null) ? ""
        : ((PTGraph) getCurrentObject(false)).getValueNode(spinnerModelNode
            .getNumber().intValue());
    contentNode = new JTextField(15);
    contentNode.setText(textNode);
    contentNode.addKeyListener(this);
    
    cp.add(contentNode, Editor.LAYOUT_WRAP);
    
    cp.add(generator.generateJLabel(GRAPH_CELL_EDGE_START), Editor.LAYOUT_PARAGRAPH_GAP);
    
    SpinnerNumberModel spinnerModelEdgeStart = new SpinnerNumberModel(0, 0,
        graphSize - 1, 1);
    
    edgeStartSpinner = new JSpinner(spinnerModelEdgeStart);
    edgeStartSpinner.addChangeListener(this);
    cp.add(edgeStartSpinner);
    cp.add(new JLabel(" of " + String.valueOf(graphSize)), Editor.LAYOUT_WRAP);
    
    cp.add(generator.generateJLabel(GRAPH_CELL_EDGE_END), Editor.LAYOUT_PARAGRAPH_GAP);
    
    SpinnerNumberModel spinnerModelEdgeEnd = new SpinnerNumberModel(0, 0,
        graphSize - 1, 1);
    edgeEndSpinner = new JSpinner(spinnerModelEdgeEnd);
    edgeEndSpinner.addChangeListener(this);
    cp.add(edgeEndSpinner);
    cp.add(new JLabel(" of " + String.valueOf(graphSize)), Editor.LAYOUT_WRAP);
    
    String textEdge = (getCurrentObject(false) == null) ? ""
        : ((PTGraph) getCurrentObject(false)).getValueEdge(
            spinnerModelEdgeStart.getNumber().intValue(), spinnerModelEdgeEnd
                .getNumber().intValue());
    contentEdge = new JTextField(15);
    contentEdge.setText(textEdge);
    contentEdge.addKeyListener(this);
    cp.add(contentEdge, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
  }

  protected Box createPositionsBox(TranslatableGUIElement generator,
      int graphSize) {

    Box contentBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
        "graphPosBL");

    Box lineBox = new Box(BoxLayout.LINE_AXIS);

    lineBox.add(generator.generateJLabel(GRAPH_CELL_NODE));
    SpinnerNumberModel spinnerModelNode = new SpinnerNumberModel(0, 0,
        graphSize - 1, 1);
    nodeSpinner = new JSpinner(spinnerModelNode);
    lineBox.add(nodeSpinner);
    nodeSpinner.addChangeListener(this);
    lineBox.add(new JLabel(" of " + String.valueOf(graphSize)));
    // contentBox.add(lineBox);

    // Box secondLineBox = new Box(BoxLayout.LINE_AXIS);
    lineBox.add(generator.generateJLabel(CONTENT_NODE));
    String textNode = (getCurrentObject(false) == null) ? ""
        : ((PTGraph) getCurrentObject(false)).getValueNode(spinnerModelNode
            .getNumber().intValue());
    contentNode = new JTextField(15);
    contentNode.setText(textNode);
    contentNode.addKeyListener(this);
    lineBox.add(contentNode);
    contentBox.add(lineBox);

    Box thirdLineBox = new Box(BoxLayout.LINE_AXIS);
    thirdLineBox.add(generator.generateJLabel(GRAPH_CELL_EDGE_START));
    SpinnerNumberModel spinnerModelEdgeStart = new SpinnerNumberModel(0, 0,
        graphSize - 1, 1);
    edgeStartSpinner = new JSpinner(spinnerModelEdgeStart);
    thirdLineBox.add(edgeStartSpinner);
    edgeStartSpinner.addChangeListener(this);
    thirdLineBox.add(new JLabel(" of " + String.valueOf(graphSize)));
    // contentBox.add(thirdLineBox);

    // Box fourthLineBox = new Box(BoxLayout.LINE_AXIS);

    thirdLineBox.add(generator.generateJLabel(GRAPH_CELL_EDGE_END));
    SpinnerNumberModel spinnerModelEdgeEnd = new SpinnerNumberModel(0, 0,
        graphSize - 1, 1);
    edgeEndSpinner = new JSpinner(spinnerModelEdgeEnd);
    thirdLineBox.add(edgeEndSpinner);
    edgeEndSpinner.addChangeListener(this);
    thirdLineBox.add(new JLabel(" of " + String.valueOf(graphSize)));
    // contentBox.add(thirdLineBox);

    // Box fifthLineBox = new Box(BoxLayout.LINE_AXIS);

    thirdLineBox.add(generator.generateJLabel(CONTENT_EDGE));
    String textEdge = (getCurrentObject(false) == null) ? ""
        : ((PTGraph) getCurrentObject(false)).getValueEdge(
            spinnerModelEdgeStart.getNumber().intValue(), spinnerModelEdgeEnd
                .getNumber().intValue());
    contentEdge = new JTextField(15);
    contentEdge.setText(textEdge);
    contentEdge.addKeyListener(this);
    thirdLineBox.add(contentEdge);
    contentBox.add(thirdLineBox);

    return contentBox;
  }
  

  public Box generateEdgeFontAndStyleBox(TranslatableGUIElement generator,
      String borderKey) {
    Box fontBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS, borderKey);

    // add a box for choosing the font
    Box fontNameBox = new Box(BoxLayout.LINE_AXIS);
    fontNameBox.add(generator.generateJLabel("GenericEditor.nameLabel"));
    String[] fonts = Animal.GLOBAL_FONTS; // Toolkit.getDefaultToolkit().getFontList();
    fontChooserEdge = new JComboBox<String>();
    for (int j = 0; j < fonts.length; j++)
      fontChooserEdge.addItem(fonts[j]);
    fontChooserEdge.addActionListener(this);
    fontNameBox.add(fontChooserEdge);
    fontBox.add(fontNameBox);

    // add a font size choice box
    Box fontSizeBox = new Box(BoxLayout.LINE_AXIS);
    fontSizeBox.add(generator
        .generateJLabel("AbstractTextEditor.fontSizeLabel"));
    fontSizeEdge = new JComboBox<String>();
    fontSizeEdge.setEditable(true);
    fontSizeEdge.addItem("8");
    fontSizeEdge.addItem("10");
    fontSizeEdge.addItem("12");
    fontSizeEdge.addItem("14");
    fontSizeEdge.addItem("16");
    fontSizeEdge.addItem("24");
    fontSizeEdge.setSelectedItem("12");
    fontSizeEdge.addActionListener(this);
    fontSizeBox.add(fontSizeEdge);
    fontBox.add(fontSizeBox);

    // add a font style choice box (bold? italic?)
    Box fontTypeBox = new Box(BoxLayout.LINE_AXIS);

    fontTypeBox.add(generator
        .generateJLabel("AbstractTextEditor.fontStyleLabel"));
    edgeItalic = generator.generateJCheckBox("italic", null, this);
    edgeItalic.setHorizontalAlignment(SwingConstants.CENTER);
    fontTypeBox.add(edgeItalic);

    edgeBold = generator.generateJCheckBox("bold", null, this);
    edgeBold.setHorizontalAlignment(SwingConstants.CENTER);
    fontTypeBox.add(edgeBold);
    fontBox.add(fontTypeBox);

    return fontBox;
  }
  
  private void generateSizeSetting() {
    insertSeparator("graphSize", cp, generator);
    
    JLabel sizeLabel = generator.generateJLabel("graphSize");
    
    cp.add(sizeLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    
    nodeSize = new JTextField(20);
    nodeSize.addActionListener(this);
    cp.add(nodeSize, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
    JLabel indicesLabel = generator.generateJLabel("indices");
    
    cp.add(indicesLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    showIndices = generator.generateJCheckBox(SHOW_INDICES_LABEL, null, this);
    cp.add(showIndices, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
  }
  
  private void generateGraphOptions() {
    insertSeparator("graphOptionsBL", cp, generator);
    
    // TOOD: Labels fuer die Checkboxen.
    JLabel weightedLabel = generator.generateJLabel("weight.label");
    JLabel directedLabel = generator.generateJLabel("direction.label");
    
    weighted = generator.generateJCheckBox(WEIGHT_LABEL, null, this);
    directed = generator.generateJCheckBox(DIRECTION_LABEL, null, this);
    
    cp.add(weightedLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    cp.add(weighted, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    cp.add(directedLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    cp.add(directed, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
  }
  
  
  private void generateHighlightColorOptions() {
    insertSeparator("AbstractArrayEditor.highlightColors", cp, generator);
    
    JLabel highlightColor = generator.generateJLabel(HIGHLIGHT_COLOR);
    JLabel elemHighlightColor = generator.generateJLabel(ELEM_HIGHLIGHT_COLOR);
    
    cp.add(highlightColor, Editor.LAYOUT_PARAGRAPH_GAP);
    
    Color highlightInitialColor = (getCurrentObject(false) == null) ? Color.yellow
        : ((PTGraph) getCurrentObject(false)).getHighlightColor();
    highlightColorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(highlightInitialColor), "highlightColor",
        translator.AnimalTranslator.translateMessage(
            "GenericEditor.chooseColor", new Object[] { "Graph" }),
        highlightInitialColor);
    cp.add(new ExtendedActionButton(highlightColorChooser,
        KeyEvent.VK_H), Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
    cp.add(elemHighlightColor, Editor.LAYOUT_PARAGRAPH_GAP);

    Color elemHighlightInitialColor = (getCurrentObject(false) == null) ? Color.red
        : ((PTGraph) getCurrentObject(false)).getElemHighlightColor();
    elemHighlightColorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(elemHighlightInitialColor), "elemHighlightColor",
        translator.AnimalTranslator.translateMessage(
            "GenericEditor.chooseColor", new Object[] { "Graph" }),
        elemHighlightInitialColor);
    cp.add(new ExtendedActionButton(elemHighlightColorChooser,
        KeyEvent.VK_E), Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
  }
  
  private void generateColorOptions() {
    insertSeparator("GenericEditor.colorBL", cp, generator);
    
    JLabel outlineColor = generator.generateJLabel(OUTLINE_COLOR);
    // MOve to NodeFontSettings
    JLabel nodeFontColor = generator.generateJLabel(FONT_COLOR_NODE);
    // MOve to EdgeFontSettings  
    JLabel edgeFontColor = generator.generateJLabel(FONT_COLOR_EDGE);
    JLabel backgroundColor = generator.generateJLabel(BG_COLOR);
   
    
    cp.add(outlineColor, Editor.LAYOUT_PARAGRAPH_GAP);
    
    Color outlineInitialColor = (getCurrentObject(false) == null) ? Color.black
        : ((PTGraph) getCurrentObject(false)).getOutlineColor();
    outlineColorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(outlineInitialColor), "outlineColor",
        AnimalTranslator.translateMessage("GenericEditor.chooseColor",
            new Object[] { "Graph" }), outlineInitialColor);
    cp.add(new ExtendedActionButton(outlineColorChooser,
        KeyEvent.VK_O), Editor.LAYOUT_PARAGRAPH_GAP);
    
    // MOve to NodeFontSettings
    cp.add(nodeFontColor);
    
    // MOve to NodeFontSettings
    Color fontInitialColorNode = (getCurrentObject(false) == null) ? Color.black
        : ((PTGraph) getCurrentObject(false)).getNodeFontColor();
    fontColorNodeChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(outlineInitialColor), "fontColorNodeChooser",
        AnimalTranslator.translateMessage("GenericEditor.chooseColor",
            new Object[] { "Graph" }), fontInitialColorNode);
    cp.add(new ExtendedActionButton(fontColorNodeChooser,
        KeyEvent.VK_F), Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
    cp.add(backgroundColor, Editor.LAYOUT_PARAGRAPH_GAP);
    
    Color initialColor = (getCurrentObject(false) == null) ? Color.white
        : ((PTGraph) getCurrentObject(false)).getBGColor();
    colorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(initialColor), "bgColor",
        AnimalTranslator.translateMessage("GenericEditor.chooseColor",
            new Object[] { "Graph" }), initialColor);
    cp.add(new ExtendedActionButton(colorChooser, KeyEvent.VK_C), Editor.LAYOUT_PARAGRAPH_GAP );
    // MOve to NodeFontSettings
   cp.add(edgeFontColor);
   // MOve to NodeFontSettings
    Color fontInitialColorEdge = (getCurrentObject(false) == null) ? Color.black
        : ((PTGraph) getCurrentObject(false)).getEdgeFontColor();
    fontColorEdgeChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(outlineInitialColor), "fontColorEdge",
        AnimalTranslator.translateMessage("GenericEditor.chooseColor",
            new Object[] { "Graph" }), fontInitialColorEdge);
    cp.add(new ExtendedActionButton(fontColorEdgeChooser,
        KeyEvent.VK_G), Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
    generateHighlightColorOptions();
    
  }
  
  private void generateEdgeFont() {
   // insertSeparator("fontEdgeBL", cp, generator);
    
    JLabel edgeFontNameLabel = generator.generateJLabel("GenericEditor.nameLabel");
    JLabel edgeFontSizeLabel = generator.generateJLabel("AbstractTextEditor.fontSizeLabel");
    JLabel edgeFontStyleLabel = generator.generateJLabel("AbstractTextEditor.fontStyleLabel");
    
   // cp.add(edgeFontNameLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    
    String[] fonts = Animal.GLOBAL_FONTS;
    fontChooserEdge = new JComboBox<String>();
    
    for (int j = 0; j < fonts.length; j++)
      fontChooserEdge.addItem(fonts[j]);
    fontChooserEdge.addActionListener(this);
    
   // cp.add(fontChooserEdge, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
   // cp.add(edgeFontSizeLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    
    fontSizeEdge = new JComboBox<String>();
    fontSizeEdge.setEditable(true);
    fontSizeEdge.addItem("8");
    fontSizeEdge.addItem("10");
    fontSizeEdge.addItem("12");
    fontSizeEdge.addItem("14");
    fontSizeEdge.addItem("16");
    fontSizeEdge.addItem("24");
    fontSizeEdge.setSelectedItem("12");
    fontSizeEdge.addActionListener(this);
    
 //   cp.add(fontSizeEdge, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
 //   cp.add(edgeFontStyleLabel, Editor.LAYOUT_PARAGRAPH_GAP);
    
    edgeItalic = generator.generateJCheckBox("italic", null, this);
    //edgeItalic.setHorizontalAlignment(SwingConstants.CENTER);
 //   cp.add(edgeItalic, Editor.LAYOUT_PARAGRAPH_GAP);

    edgeBold = generator.generateJCheckBox("bold", null, this);
   // edgeBold.setHorizontalAlignment(SwingConstants.CENTER);
  //  cp.add(edgeBold, Editor.LAYOUT_PARAGRAPH_GAP_WRAP);
    
    //Box fontBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS, borderKey);

    // add a box for choosing the font
    /*Box fontNameBox = new Box(BoxLayout.LINE_AXIS);
    fontNameBox.add(generator.generateJLabel("GenericEditor.nameLabel"));
    String[] fonts = Animal.GLOBAL_FONTS; // Toolkit.getDefaultToolkit().getFontList();
    fontChooserEdge = new JComboBox<String>();
    for (int j = 0; j < fonts.length; j++)
      fontChooserEdge.addItem(fonts[j]);
    fontChooserEdge.addActionListener(this);
    fontNameBox.add(fontChooserEdge);
    fontBox.add(fontNameBox);

    // add a font size choice box
    Box fontSizeBox = new Box(BoxLayout.LINE_AXIS);
    fontSizeBox.add(generator
        .generateJLabel("AbstractTextEditor.fontSizeLabel"));
    fontSizeEdge = new JComboBox<String>();
    fontSizeEdge.setEditable(true);
    fontSizeEdge.addItem("8");
    fontSizeEdge.addItem("10");
    fontSizeEdge.addItem("12");
    fontSizeEdge.addItem("14");
    fontSizeEdge.addItem("16");
    fontSizeEdge.addItem("24");
    fontSizeEdge.setSelectedItem("12");
    fontSizeEdge.addActionListener(this);
    fontSizeBox.add(fontSizeEdge);
    fontBox.add(fontSizeBox);

    // add a font style choice box (bold? italic?)
    Box fontTypeBox = new Box(BoxLayout.LINE_AXIS);

    fontTypeBox.add(generator
        .generateJLabel("AbstractTextEditor.fontStyleLabel"));
    edgeItalic = generator.generateJCheckBox("italic", null, this);
    edgeItalic.setHorizontalAlignment(SwingConstants.CENTER);
    fontTypeBox.add(edgeItalic);

    edgeBold = generator.generateJCheckBox("bold", null, this);
    edgeBold.setHorizontalAlignment(SwingConstants.CENTER);
    fontTypeBox.add(edgeBold);
    fontBox.add(fontTypeBox);

    return fontBox;*/
  }
  

  /**
   * Set the current properties in the editor window as defined in the
   * XProperties.
   * 
   * @param props
   *          the used properties file
   */
  public void setProperties(XProperties props) {
    colorChooser.setColor(props.getColorProperty(PTGraph.TYPE_LABEL
        + ".bgColor", Color.WHITE));
    highlightColorChooser.setColor(props.getColorProperty(PTGraph.TYPE_LABEL
        + ".highlightColor", Color.YELLOW));
    elemHighlightColorChooser.setColor(props.getColorProperty(
        PTGraph.TYPE_LABEL + ".elemHighlightColor", Color.RED));
    outlineColorChooser.setColor(props.getColorProperty(PTGraph.TYPE_LABEL
        + ".outlineColor", Color.BLACK));
    fontColorNodeChooser.setColor(props.getColorProperty(PTGraph.TYPE_LABEL
        + ".nodeFontColor", Color.BLACK));
    fontColorEdgeChooser.setColor(props.getColorProperty(PTGraph.TYPE_LABEL
        + ".edgeFontColor", Color.BLACK));
    nodeSize.setText(String.valueOf(props.getIntProperty(PTGraph.TYPE_LABEL
        + ".graphSize", 1)));
    fontName.setSelectedItem(props.getProperty(PTGraph.TYPE_LABEL
        + ".nodeFontName", "Monospaced"));
    fontSize.setSelectedItem(props.getProperty(PTGraph.TYPE_LABEL
        + ".nodeFontSize", "14"));
    int fontStyle = props.getIntProperty(PTGraph.TYPE_LABEL + ".nodeFontStyle",
        Font.PLAIN);
    italic.setSelected((fontStyle & Font.ITALIC) == Font.ITALIC);
    bold.setSelected((fontStyle & Font.BOLD) == Font.BOLD);

    fontChooserEdge.setSelectedItem(props.getProperty(PTGraph.TYPE_LABEL
        + ".edgeFontName", "Monospaced"));
    fontSizeEdge.setSelectedItem(props.getProperty(PTGraph.TYPE_LABEL
        + ".edgeFontSize", "14"));
    fontStyle = props.getIntProperty(PTGraph.TYPE_LABEL + ".edgeFontStyle",
        Font.PLAIN);
    edgeItalic.setSelected((fontStyle & Font.ITALIC) == Font.ITALIC);
    edgeBold.setSelected((fontStyle & Font.BOLD) == Font.BOLD);
    weighted.setSelected(props.getBoolProperty(
        PTGraph.TYPE_LABEL + ".weighted", true));
    directed.setSelected(props.getBoolProperty(
        PTGraph.TYPE_LABEL + ".directed", true));
    showIndices.setSelected(props.getBoolProperty(PTGraph.TYPE_LABEL
        + ".showIndices", false));
  }

  private Box createColorBox(TranslatableGUIElement generator) {
    // create color box
    Box colorBox = generator.generateBorderedBox(BoxLayout.LINE_AXIS,
        "GenericEditor.colorBL");
    // Box firstColumnBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
    // "basicColors");
    Box firstColumnBox = new Box(BoxLayout.PAGE_AXIS);

    // first row: outline color
    firstColumnBox.add(generator.generateJLabel(OUTLINE_COLOR));
    Color outlineInitialColor = (getCurrentObject(false) == null) ? Color.black
        : ((PTGraph) getCurrentObject(false)).getOutlineColor();
    outlineColorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(outlineInitialColor), "outlineColor",
        AnimalTranslator.translateMessage("GenericEditor.chooseColor",
            new Object[] { "Graph" }), outlineInitialColor);
    firstColumnBox.add(new ExtendedActionButton(outlineColorChooser,
        KeyEvent.VK_O));

    firstColumnBox.add(generator.generateJLabel(FONT_COLOR_NODE));
    Color fontInitialColorNode = (getCurrentObject(false) == null) ? Color.black
        : ((PTGraph) getCurrentObject(false)).getNodeFontColor();
    fontColorNodeChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(outlineInitialColor), "fontColorNodeChooser",
        AnimalTranslator.translateMessage("GenericEditor.chooseColor",
            new Object[] { "Graph" }), fontInitialColorNode);
    firstColumnBox.add(new ExtendedActionButton(fontColorNodeChooser,
        KeyEvent.VK_F));

    firstColumnBox.add(generator.generateJLabel(FONT_COLOR_EDGE));
    Color fontInitialColorEdge = (getCurrentObject(false) == null) ? Color.black
        : ((PTGraph) getCurrentObject(false)).getEdgeFontColor();
    fontColorEdgeChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(outlineInitialColor), "fontColorEdge",
        AnimalTranslator.translateMessage("GenericEditor.chooseColor",
            new Object[] { "Graph" }), fontInitialColorEdge);
    firstColumnBox.add(new ExtendedActionButton(fontColorEdgeChooser,
        KeyEvent.VK_G));

    colorBox.add(firstColumnBox);

    Box secondColumnBox = new Box(BoxLayout.PAGE_AXIS);

    // second row: background color
    secondColumnBox.add(generator.generateJLabel(BG_COLOR)); // color
    Color initialColor = (getCurrentObject(false) == null) ? Color.white
        : ((PTGraph) getCurrentObject(false)).getBGColor();
    colorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(initialColor), "bgColor",
        AnimalTranslator.translateMessage("GenericEditor.chooseColor",
            new Object[] { "Graph" }), initialColor);
    secondColumnBox.add(new ExtendedActionButton(colorChooser, KeyEvent.VK_C));

    // Box secondColumnBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
    // "highlightColors");
    secondColumnBox.add(generator.generateJLabel(HIGHLIGHT_COLOR));
    Color highlightInitialColor = (getCurrentObject(false) == null) ? Color.yellow
        : ((PTGraph) getCurrentObject(false)).getHighlightColor();
    highlightColorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(highlightInitialColor), "highlightColor",
        translator.AnimalTranslator.translateMessage(
            "GenericEditor.chooseColor", new Object[] { "Graph" }),
        highlightInitialColor);
    secondColumnBox.add(new ExtendedActionButton(highlightColorChooser,
        KeyEvent.VK_H));

    secondColumnBox.add(generator.generateJLabel(ELEM_HIGHLIGHT_COLOR));
    Color elemHighlightInitialColor = (getCurrentObject(false) == null) ? Color.red
        : ((PTGraph) getCurrentObject(false)).getElemHighlightColor();
    elemHighlightColorChooser = new ColorChooserAction(this, ColorChoice
        .getColorName(elemHighlightInitialColor), "elemHighlightColor",
        translator.AnimalTranslator.translateMessage(
            "GenericEditor.chooseColor", new Object[] { "Graph" }),
        elemHighlightInitialColor);
    secondColumnBox.add(new ExtendedActionButton(elemHighlightColorChooser,
        KeyEvent.VK_E));

    colorBox.add(secondColumnBox);

    // finally, return the color box!
    return colorBox;
  }

  private Box createGraphSizeBox(TranslatableGUIElement generator) {
    Box sizeBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
        "graphSizeBL");
    Box internalBox = new Box(BoxLayout.LINE_AXIS);
    internalBox.add(generator.generateJLabel("graphSize"));
    nodeSize = new JTextField(8);
    nodeSize.addActionListener(this);
    internalBox.add(nodeSize);
    sizeBox.add(internalBox);

    // sizeBox.add(createIndicesOptionsBox(generator));

    return sizeBox;
  }

  private Box createOptionsBox(TranslatableGUIElement generator) {
    Box optionsBox = generator.generateBorderedBox(BoxLayout.PAGE_AXIS,
        "graphOptionsBL");
    weighted = generator.generateJCheckBox(WEIGHT_LABEL, null, this);
    optionsBox.add(weighted);

    directed = generator.generateJCheckBox(DIRECTION_LABEL, null, this);
    optionsBox.add(directed);

    showIndices = generator.generateJCheckBox(SHOW_INDICES_LABEL, null, this);
    optionsBox.add(showIndices);

    return optionsBox;
  }

  /**
   * Store the current properties to the XProperties.
   * 
   * The properites will finally be stored when Animal is closed.
   * 
   * @param props
   *          the used properties file
   */
  public void getProperties(XProperties props) {
    props.put(PTGraph.TYPE_LABEL + ".bgColor", colorChooser.getColor());
    props.put(PTGraph.TYPE_LABEL + ".highlightColor", highlightColorChooser
        .getColor());
    props.put(PTGraph.TYPE_LABEL + ".elemHighlightColor",
        elemHighlightColorChooser.getColor());
    props.put(PTGraph.TYPE_LABEL + ".outlineColor", outlineColorChooser
        .getColor());
    props.put(PTGraph.TYPE_LABEL + ".nodeFontColor", fontColorNodeChooser
        .getColor());
    props.put(PTGraph.TYPE_LABEL + ".edgeFontColor", fontColorEdgeChooser
        .getColor());
    props.put(PTGraph.TYPE_LABEL + ".graphSize", getInt(nodeSize.getText(), 1));
    props.put(PTGraph.TYPE_LABEL + ".nodeFontName", fontName.getSelectedItem());
    props.put(PTGraph.TYPE_LABEL + ".edgeFontName", fontChooserEdge
        .getSelectedItem());
    props.put(PTGraph.TYPE_LABEL + ".nodeFontSize", fontSize.getSelectedItem());
    props.put(PTGraph.TYPE_LABEL + ".edgeFontSize", fontSizeEdge
        .getSelectedItem());
    props.put(PTGraph.TYPE_LABEL + ".nodeFont", new Font((String) fontName
        .getSelectedItem(), Font.PLAIN, getInt((String) fontSize
        .getSelectedItem(), 14)));
    props.put(PTGraph.TYPE_LABEL + ".edgeFont", new Font(
        (String) fontChooserEdge.getSelectedItem(), Font.PLAIN, getInt(
            (String) fontSizeEdge.getSelectedItem(), 14)));
    props.put(PTGraph.TYPE_LABEL + ".weighted", weighted.isSelected());
    props.put(PTGraph.TYPE_LABEL + ".directed", directed.isSelected());
    props.put(PTGraph.TYPE_LABEL + ".showIndices", showIndices.isSelected());
  }

  /**
   * How many points are needed to create a new instance of the Graph.
   */
  public int pointsNeeded() {
    return 1;
  }

  public boolean nextPoint(int num, Point p) {
    switch (num) {
    case 1:
    case 2:
      ((PTGraph) getCurrentObject()).setOrigin(p);
      break;
    }
    return true;
  }

  /**
   * Determines the minimum distance from a point to the Graph.
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
   */
  public EditPoint[] getEditPoints(PTGraphicObject go) {
    Rectangle bBox = ((PTGraph) go).getBoundingBox();
    return new EditPoint[] {
        new EditPoint(-1, new Point(bBox.x, bBox.y)),
        new EditPoint(-2, new Point(bBox.x + bBox.width, bBox.y)),
        new EditPoint(-3, new Point(bBox.x, bBox.y + bBox.height)),
        new EditPoint(-4, new Point(bBox.x + bBox.width, bBox.y + bBox.height)),
        new EditPoint(-5, new Point(bBox.x + bBox.width / 2, bBox.y
            + bBox.height / 2)) };
  }

  /**
   * Finally create a new Graph by this editor.
   */
  public EditableObject createObject() {
    PTGraph result = new PTGraph(getInt(nodeSize.getText(), 1));
    storeAttributesInto(result);
    return result;
  }

  /**
   * The alternative editor after a Graph has already been created.
   * 
   * @param e
   *          the Graph that should be edited.
   * 
   * See <code>GraphEditor (int i)</code> for details.
   */
  public Editor getSecondaryEditor(EditableObject e) {
    GraphEditor result = new GraphEditor(((PTGraph) e).getLength());
    // don't display tab asking for number of Graph elements
    // firstEdit = false;
    result.extractAttributesFrom(e);
    return result;
  }

  /**
   * Set the values of the current Graph to the ones chosen in the editor.
   * 
   * @param eo
   *          the current Graph
   */
  public void storeAttributesInto(EditableObject eo) {
    super.storeAttributesInto(eo);
    PTGraph Graph = (PTGraph) eo;
    Graph.setBGColor(colorChooser.getColor());
    Graph.setHighlightColor(highlightColorChooser.getColor());
    Graph.setElemHighlightColor(elemHighlightColorChooser.getColor());
    Graph.setOutlineColor(outlineColorChooser.getColor());
    Graph.setNodeFontColor(fontColorNodeChooser.getColor());
    Graph.setEdgeFontColor(fontColorEdgeChooser.getColor());
    if (nodeSize != null)
      Graph.setSize(getInt(nodeSize.getText(), 1));
    Graph.enterValueNode(calcIndexNode(), getContentNode());
    Graph.enterValueEdge(calcIndexEdgeStart(), calcIndexEdgeEnd(),
        getContentEdge());
    Graph.setNodeFont(storeFontNode());
    Graph.setEdgeFont(storeFontEdge());
    Graph.setWeight(weighted.isSelected());
    Graph.setDirection(directed.isSelected());
    Graph.setIndices(showIndices.isSelected());
  }

  /**
   * Return the text in the content field of the editor for the node; if null
   * returns an empty string.
   */
  private String getContentNode() {
    return (contentNode == null) ? "" : contentNode.getText();
  }

  /**
   * Return the text in the content field of the editor for the edge; if null
   * returns an empty string.
   */
  private String getContentEdge() {
    return (contentEdge == null) ? "" : contentEdge.getText();
  }

  private int getContentXNodePosition() {
    return (xNodePosition == null) ? 0 : Integer.parseInt(xNodePosition
        .getText());
  }

  private int getContentYNodePosition() {
    return (yNodePosition == null) ? 0 : Integer.parseInt(yNodePosition
        .getText());
  }

  /**
   * extracts the Font from the components' settings for the nodes
   */
  Font storeFontNode() {
    String name = (String) fontName.getSelectedItem();
    String size = (String) fontSize.getSelectedItem();
    int fontStyle = Font.PLAIN;
    if (italic.isSelected())
      fontStyle |= Font.ITALIC;
    if (bold.isSelected())
      fontStyle |= Font.BOLD;

    return new Font(name, fontStyle, getInt(size, 12));
  }

  /**
   * extracts the Font from the components' settings for the edges
   */
  Font storeFontEdge() {
    String name = (String) fontChooserEdge.getSelectedItem();
    String size = (String) fontSizeEdge.getSelectedItem();
    int fontStyle = Font.PLAIN;
    if (edgeItalic.isSelected())
      fontStyle |= Font.ITALIC;
    if (edgeBold.isSelected())
      fontStyle |= Font.BOLD;
    return new Font(name, fontStyle, getInt(size, 12));
  }

  /**
   * sets the components according to the font for the nodes
   */
  void extractFontNode(Font f) {
    fontName.setSelectedItem(f.getName());
    fontSize.setSelectedItem(String.valueOf(f.getSize()));
    italic.setSelected(f.isItalic());
    bold.setSelected(f.isBold());
  }

  /**
   * sets the components according to the font for the edges
   */
  void extractFontEdge(Font f) {
    fontChooserEdge.setSelectedItem(f.getName());
    fontSizeEdge.setSelectedItem(String.valueOf(f.getSize()));
    edgeItalic.setSelected(f.isItalic());
    edgeBold.setSelected(f.isBold());
  }

  /**
   * Determines the currently chosen array cell for the nodes returns -1 if
   * Graph is not yet created.
   */
  private int calcIndexNode() {
    return (nodeSpinner == null) ? -1 : ((SpinnerNumberModel) nodeSpinner
        .getModel()).getNumber().intValue();
  }

  /**
   * Determines the currently chosen array cell for the edges returns -1 if
   * Graph is not yet created.
   */
  private int calcIndexEdgeStart() {
    return (edgeStartSpinner == null) ? -1
        : ((SpinnerNumberModel) edgeStartSpinner.getModel()).getNumber()
            .intValue();
  }

  /**
   * Determines the currently chosen array cell for the edges returns -1 if
   * Graph is not yet created.
   */
  private int calcIndexEdgeEnd() {
    return (edgeEndSpinner == null) ? -1 : ((SpinnerNumberModel) edgeEndSpinner
        .getModel()).getNumber().intValue();
  }

  /**
   * Determines the currently chosen node for changing the position returns -1
   * if Graph is not yet created.
   */
  private int calcIndexNodePosition() {
    return (nodePositionSpinner == null) ? 0
        : ((SpinnerNumberModel) nodePositionSpinner.getModel()).getNumber()
            .intValue();
  }

  public void extractAttributesFrom(EditableObject eo) {
    super.extractAttributesFrom(eo);
    PTGraph graph = (PTGraph) eo;
    colorChooser.setColor(graph.getBGColor());
    highlightColorChooser.setColor(graph.getHighlightColor());
    elemHighlightColorChooser.setColor(graph.getElemHighlightColor());
    outlineColorChooser.setColor(graph.getOutlineColor());
    fontColorNodeChooser.setColor(graph.getNodeFontColor());
    fontColorEdgeChooser.setColor(graph.getEdgeFontColor());
    if (nodeSize != null)
      nodeSize.setText(String.valueOf(graph.getSize()));
    contentNode.setText(graph.getValueNode(calcIndexNode()));
    contentEdge.setText(graph.getValueEdge(calcIndexEdgeStart(),
        calcIndexEdgeEnd()));
    xNodePosition.setText(String.valueOf(graph
        .getXNode(calcIndexNodePosition())));
    yNodePosition.setText(String.valueOf(graph
        .getYNode(calcIndexNodePosition())));
    extractFontNode(graph.getNodeFont());
    extractFontEdge(graph.getEdgeFont());
    weighted.setSelected(graph.hasWeight());
    directed.setSelected(graph.hasDirection());
    showIndices.setSelected(graph.indicesShown());
  }

  /**
   * Load the text that is displayed in the status line.
   */
  public String getStatusLineMsg() {
    return translator.AnimalTranslator.translateMessage("GraphEditor.statusLine",
        new Object[] { DrawCanvas.translateDrawButton(),
            DrawCanvas.translateFinishButton(),
            DrawCanvas.translateCancelButton() });
  }

  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);
    PTGraph Graph = (PTGraph) getCurrentObject();

    // change entry in the array cell, that corresponds to the chosen
    // value of the Spinner, if the text in content-field is changed
    if (e.getSource() == contentNode) {
      Graph.enterValueNode(calcIndexNode(), contentNode.getText());
    }
    if (e.getSource() == contentEdge) {
      Graph.enterValueEdge(calcIndexEdgeStart(), calcIndexEdgeEnd(),
          contentEdge.getText());
    }
    if (e.getSource() == nodeSize) {
      Graph.setSize(getInt(nodeSize.getText(), 1));
    }
    if (e.getSource() == fontName || e.getSource() == fontName) {
      Graph.setNodeFont(storeFontNode());
    }
    if (e.getSource() == fontChooserEdge || e.getSource() == fontSizeEdge) {
      Graph.setEdgeFont(storeFontEdge());
    }
    if (e.getSource() == weighted) {
      Graph.setWeight(weighted.isSelected());
    }
    if (e.getSource() == directed) {
      Graph.setDirection(directed.isSelected());
    }
    if (e.getSource() == showIndices) {
      Graph.setIndices(showIndices.isSelected());
    }
    if (e.getSource() == xNodePosition || e.getSource() == yNodePosition) {
      Graph.setPositionNode(calcIndexNodePosition(), getContentXNodePosition(),
          getContentYNodePosition());
    }
    repaintNow();
    Animation.get().doChange();
  }

  public void keyPressed(
  KeyEvent e) {
    // do nothing; only used for serialization
  }

  public void keyReleased(KeyEvent e) {
    PTGraph Graph = (PTGraph) getCurrentObject();
    if (e.getSource() == contentNode) {
      Graph.enterValueNode(calcIndexNode(), contentNode.getText());
    }
    if (e.getSource() == contentEdge) {
      Graph.enterValueEdge(calcIndexEdgeStart(), calcIndexEdgeEnd(),
          contentEdge.getText());
    }
    if (e.getSource() == xNodePosition || e.getSource() == yNodePosition) {
      Graph.setPositionNode(calcIndexNodePosition(), getContentXNodePosition(),
          getContentYNodePosition());
    }
    repaintNow();
    Animation.get().doChange();
  }

  public void keyTyped(KeyEvent e) {
    PTGraph Graph = (PTGraph) getCurrentObject();
    if (e.getSource() == contentNode) {
      Graph.enterValueNode(calcIndexNode(), contentNode.getText());
    }
    if (e.getSource() == contentEdge) {
      Graph.enterValueEdge(calcIndexEdgeStart(), calcIndexEdgeEnd(),
          contentEdge.getText());
    }
    if (e.getSource() == xNodePosition || e.getSource() == yNodePosition) {
      Graph.setPositionNode(calcIndexNodePosition(), getContentXNodePosition(),
          getContentYNodePosition());
    }
    repaintNow();
  }

  /**
   * Listener that executes if another cell is chosen in the editor. This is
   * <em> not </em> an ActionListener but a ChangeEventListener.
   * 
   * @param e
   *          the event to react upon
   */
  public void stateChanged(ChangeEvent e) {
    // super.stateChanged (e);
    PTGraph Graph = (PTGraph) getCurrentObject();
    if (e.getSource() == nodeSpinner) {
      contentNode.setText(Graph.getValueNode(calcIndexNode()));
    }
    if (e.getSource() == edgeStartSpinner || e.getSource() == edgeEndSpinner) {
      contentEdge.setText(Graph.getValueEdge(calcIndexEdgeStart(),
          calcIndexEdgeEnd()));
    }
    if (e.getSource() == nodePositionSpinner) {
      xNodePosition.setText(String.valueOf(Graph
          .getXNode(calcIndexNodePosition())));
      yNodePosition.setText(String.valueOf(Graph
          .getYNode(calcIndexNodePosition())));
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
    PTGraph Graph = (PTGraph) getCurrentObject();
    String eventName = event.getPropertyName();
    if (eventName.equals("bgColor"))
      Graph.setBGColor((Color) event.getNewValue());
    if (eventName.equals("highlightColor")) {
      Graph.setHighlightColor((Color) event.getNewValue());
    }
    if (eventName.equals("elemHighlightColor")) {
      Graph.setElemHighlightColor((Color) event.getNewValue());
    }
    if (eventName.equals("outlineColor"))
      Graph.setOutlineColor((Color) event.getNewValue());
    if (eventName.equals("fontColorNode"))
      Graph.setNodeFontColor((Color) event.getNewValue());
    if (eventName.equals("fontColorEdge"))
      Graph.setEdgeFontColor((Color) event.getNewValue());
    if (!event.getOldValue().equals(event.getNewValue())) {
      repaintNow();
      Animation.get().doChange();
    }
  }

  public String getBasicType() {
    return PTGraph.TYPE_LABEL;
  }
}