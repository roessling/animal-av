package algoanim.animalscript;

import interactionsupport.models.HtmlDocumentationModel;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.InteractionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import interactionsupport.models.generators.InteractiveElementGenerator;

import java.io.FileWriter;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Arc;
import algoanim.primitives.ArrayBasedQueue;
import algoanim.primitives.ArrayBasedStack;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.Circle;
import algoanim.primitives.CircleSeg;
import algoanim.primitives.ConceptualQueue;
import algoanim.primitives.ConceptualStack;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Ellipse;
import algoanim.primitives.EllipseSeg;
import algoanim.primitives.Graph;
import algoanim.primitives.Group;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.ListBasedQueue;
import algoanim.primitives.ListBasedStack;
import algoanim.primitives.ListElement;
import algoanim.primitives.Point;
import algoanim.primitives.Polygon;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.AnimalVariablesGenerator;
import algoanim.primitives.generators.ArcGenerator;
import algoanim.primitives.generators.ArrayBasedQueueGenerator;
import algoanim.primitives.generators.ArrayBasedStackGenerator;
import algoanim.primitives.generators.ArrayMarkerGenerator;
import algoanim.primitives.generators.CircleGenerator;
import algoanim.primitives.generators.CircleSegGenerator;
import algoanim.primitives.generators.ConceptualQueueGenerator;
import algoanim.primitives.generators.ConceptualStackGenerator;
import algoanim.primitives.generators.DoubleArrayGenerator;
import algoanim.primitives.generators.DoubleMatrixGenerator;
import algoanim.primitives.generators.EllipseGenerator;
import algoanim.primitives.generators.EllipseSegGenerator;
import algoanim.primitives.generators.GraphGenerator;
import algoanim.primitives.generators.GroupGenerator;
import algoanim.primitives.generators.IntArrayGenerator;
import algoanim.primitives.generators.IntMatrixGenerator;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.ListBasedQueueGenerator;
import algoanim.primitives.generators.ListBasedStackGenerator;
import algoanim.primitives.generators.ListElementGenerator;
import algoanim.primitives.generators.PointGenerator;
import algoanim.primitives.generators.PolygonGenerator;
import algoanim.primitives.generators.PolylineGenerator;
import algoanim.primitives.generators.RectGenerator;
import algoanim.primitives.generators.SourceCodeGenerator;
import algoanim.primitives.generators.SquareGenerator;
import algoanim.primitives.generators.StringArrayGenerator;
import algoanim.primitives.generators.StringMatrixGenerator;
import algoanim.primitives.generators.TextGenerator;
import algoanim.primitives.generators.TriangleGenerator;
import algoanim.primitives.generators.VHDLLanguage;
import algoanim.primitives.generators.VariablesGenerator;
import algoanim.primitives.generators.vhdl.AndGateGenerator;
import algoanim.primitives.generators.vhdl.DFlipflopGenerator;
import algoanim.primitives.generators.vhdl.DemultiplexerGenerator;
import algoanim.primitives.generators.vhdl.JKFlipflopGenerator;
import algoanim.primitives.generators.vhdl.MultiplexerGenerator;
import algoanim.primitives.generators.vhdl.NAndGateGenerator;
import algoanim.primitives.generators.vhdl.NorGateGenerator;
import algoanim.primitives.generators.vhdl.NotGateGenerator;
import algoanim.primitives.generators.vhdl.OrGateGenerator;
import algoanim.primitives.generators.vhdl.RSFlipflopGenerator;
import algoanim.primitives.generators.vhdl.TFlipflopGenerator;
import algoanim.primitives.generators.vhdl.VHDLWireGenerator;
import algoanim.primitives.generators.vhdl.XNorGateGenerator;
import algoanim.primitives.generators.vhdl.XOrGateGenerator;
import algoanim.primitives.vhdl.AndGate;
import algoanim.primitives.vhdl.DFlipflop;
import algoanim.primitives.vhdl.Demultiplexer;
import algoanim.primitives.vhdl.JKFlipflop;
import algoanim.primitives.vhdl.Multiplexer;
import algoanim.primitives.vhdl.NAndGate;
import algoanim.primitives.vhdl.NorGate;
import algoanim.primitives.vhdl.NotGate;
import algoanim.primitives.vhdl.OrGate;
import algoanim.primitives.vhdl.RSFlipflop;
import algoanim.primitives.vhdl.TFlipflop;
import algoanim.primitives.vhdl.VHDLPin;
import algoanim.primitives.vhdl.VHDLWire;
import algoanim.primitives.vhdl.XNorGate;
import algoanim.primitives.vhdl.XOrGate;
import algoanim.properties.ArcProperties;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.CircleSegProperties;
import algoanim.properties.EllipseProperties;
import algoanim.properties.EllipseSegProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.ListElementProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.QueueProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.StackProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.properties.VHDLElementProperties;
import algoanim.properties.VHDLWireProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * @see algoanim.primitives.generators.Language
 * @author Jens Pfau, Stephan Mehlhase, Dima Vronskyi
 */
public class AnimalScript extends VHDLLanguage {
  /**
   * The initial size in kilobytes of the buffer used by the step mode.
   */
  public static int INITIAL_STEPBUFFER_SIZE = 4 * 1024;

  /**
   * The initial size in kilobytes of the output buffer.
   */
  public static int INITIAL_OUTPUTBUFFER_SIZE = 32 * 1024;

  /**
   * The initial size in kilobytes of the buffers used by generators.
   */
  public static int INITIAL_GENBUFFER_SIZE = 1 * 1024;

  /**
   * The initial size in kilobytes of the error buffer.
   */
  public static int INITIAL_ERRORBUFFER_SIZE = 4 * 1024;

  /**
   * The north west direction constant.
   */
  public static final String DIRECTION_NW = "NW";

  /**
   * The north direction constant.
   */
  public static final String DIRECTION_N = "N";

  /**
   * The north east direction constant.
   */
  public static final String DIRECTION_NE = "NE";

  /**
   * The west direction constant.
   */
  public static final String DIRECTION_W = "W";

  /**
   * The central direction constant.
   */
  public static final String DIRECTION_C = "C";

  /**
   * The east direction constant.
   */
  public static final String DIRECTION_E = "E";

  /**
   * The south west direction constant.
   */
  public static final String DIRECTION_SW = "SW";

  /**
   * The south direction constant.
   */
  public static final String DIRECTION_S = "S";

  /**
   * The south east direction constant.
   */
  public static final String DIRECTION_SE = "SE";

  /**
   * The direction constant for alignment from the (text) baseline's start.
   */
  public static final String DIRECTION_BASELINE_START = "baseline start";

  /**
   * The direction constant for alignment from the (text) baseline's end.
   */
  public static final String DIRECTION_BASELINE_END = "baseline end";
  
  /**
   * The color colortype constant.
   */
  public static final String COLORCHANGE_COLOR = "color";

  /**
   * The fillcolor colortype constant.
   */
  public static final String COLORCHANGE_FILLCOLOR = "fillColor";

  /**
   * The textcolor colortype constant.
   */
  public static final String COLORCHANGE_TEXTCOLOR = "textColor";

  /**
   * The colorsetting colortype constant.
   */
  public static final String COLORCHANGE_COLORSETTING = "colorSetting";

  /**
   * Controls if the class is in Stepmode or not
   */
  private boolean stepMode = false;
  
  /**
   * Counter for the animation step
   */
  private int step;
  /**
   * Contains the complete output which is written to a file in the end during
   * the program. 32k ought by enough
   */
  private StringBuilder output = new StringBuilder(
      AnimalScript.INITIAL_OUTPUTBUFFER_SIZE);

  /**
   * Contains the complete output which is written to the output when the
   * StepMode is disabled. 4k per step ought be enough
   */
  private StringBuilder stepBuffer = new StringBuilder(
      AnimalScript.INITIAL_STEPBUFFER_SIZE);

  private StringBuilder errorBuffer = new StringBuilder(
      AnimalScript.INITIAL_ERRORBUFFER_SIZE);

  /**
   * Contains a list of names, used by primitives, used to check that every name
   * is unique.
   */
  private Vector<String> names = new Vector<String>();

  private ArcGenerator arcGen = null;

  /**
   * Generator for <code>Circle</code>s.
   */
  private CircleGenerator circleGen = null;

  /**
   * Generator for <code>CircleSeg</code>s.
   */
  private CircleSegGenerator circleSegGen = null;

  /**
   * Generator for <code>DoubleArray</code>s.
   */
  private DoubleArrayGenerator doubleArrayGen = null;

  /**
   * Generator for <code>DoubleMatrix</code>s.
   */
  private DoubleMatrixGenerator doubleMatrixGen = null;

  /**
   * Generator for <code>Ellipse</code>s.
   */
  private EllipseGenerator ellipseGen = null;

  /**
   * Generator for <code>Ellipse</code>s.
   */
  private EllipseSegGenerator ellipseSegGen = null;
  
  /**
   * Generator for <code>Graph</code>s.
   */
  private GraphGenerator graphGen = null;
  
  /**
   * Generator for <code>IntArray</code>s.
   */
  private IntArrayGenerator intArrayGen = null;

  /**
   * Generator for <code>IntMatrix</code> elements.
   */
  private IntMatrixGenerator intMatrixGen = null;

  /**
   * Generator for <code>ListElement</code>s.
   */
  private ListElementGenerator listElemGen = null;

  /**
   * Generator for <code>Rect</code>s.
   */
  private RectGenerator rectGen = null;

  /**
   * Generator for <code>SourceCode</code>s.
   */
  private SourceCodeGenerator sourceGen = null;

  /**
   * Generator for <code>Square</code>s.
   */
  private SquareGenerator squareGen = null;

  /**
   * Generator for <code>StringArray</code>s.
   */
  private StringArrayGenerator stringArrayGen = null;
  
  /**
   * Generator for <code>StringMatrix</code> elements.
   */
  private StringMatrixGenerator stringMatrixGen = null;


  /**
   * Generator for <code>Text</code>s.
   */
  private TextGenerator textGen = null;

  /**
   * Generator for <code>Triangle</code>s.
   */
  private TriangleGenerator triangleGen = null;

  /**
   * Generator for <code>Polygon</code>s.
   */
  private PolygonGenerator polygonGen = null;

  /**
   * Generator for <code>Point</code>s.
   */
  private PointGenerator pointGen = null;

  /**
   * Generator for <code>ArrayMarker</code>s.
   */
  private ArrayMarkerGenerator amGen = null;

  /**
   * Generator for <code>Group</code>s.
   */
  private GroupGenerator groupGen = null;

  /**
   * Generator for <code>Polyline</code>s.
   */
  private PolylineGenerator polyGen = null;
  
  /**
   * Generator for <em>True/False Questions</em>.
   */
  private InteractiveElementGenerator internalQuestionSupportGen = null;

  /**
   * Generator for <em>Variables</em>.
   */
  private VariablesGenerator varGen = null;

  // VHDL stuff
  /**
   * Generator for <code>AND gates</code>s.
   */
  private AndGateGenerator andGateGen = null;

  /**
   * Generator for <code>NAND gates</code>s.
   */
  private NAndGateGenerator nandGateGen = null;

  /**
   * Generator for <code>NOR gates</code>s.
   */
  private NorGateGenerator norGateGen = null;


  /**
   * Generator for <code>NOT gates</code>s.
   */
  private NotGateGenerator notGateGen = null;


  /**
   * Generator for <code>OR gates</code>s.
   */
  private OrGateGenerator orGateGen = null;


  /**
   * Generator for <code>XNOR gates</code>s.
   */
  private XNorGateGenerator xNorGateGen = null;


  /**
   * Generator for <code>XOR gates</code>s.
   */
  private XOrGateGenerator xorGateGen = null;

  /** 
  * Generator for <em>D flipflops</em>
  */
  private DFlipflopGenerator dFlipflopGen = null;
  
  /** 
   * Generator for <em>JK flipflops</em>
   */
  private JKFlipflopGenerator jkFlipflopGen = null;

   
   /** 
    * Generator for <em>RS flipflops</em>
    */
  private RSFlipflopGenerator rsFlipflopGen = null;

    
  /** 
   * Generator for <em>T flipflops</em>
   */
  private TFlipflopGenerator tFlipflopGen = null;

  
  /** 
   * Generator for <em>Demultiplexers</em>
   */
   private DemultiplexerGenerator demuxGen = null;

   /** 
    * Generator for <em>multiplexers</em>
    */
   private MultiplexerGenerator muxGen = null;


   /** 
    * Generator for <em>VHDL-based wires</em>
    */
   private VHDLWireGenerator wireGen = null;
  
  /**
   * All valid directions for AnimalScript move methods.
   */
  private Vector<String> directions;

  /**
   * Creates the headline of the AnimalScript File and the AnimalScript Object.
   * 
   * @param title
   *          the title of this AnimalScript. may be null.
   * @param author
   *          the autor of this AnimalScript. may be null.
   * @param x
   *          the width of the animation window.
   * @param y
   *          the height of the animation window.
   */
  public AnimalScript(String title, String author, int x, int y) {
    super(title, author, x, y);
    
    step = 0;
    
    directions = new Vector<String>();
    directions.add(AnimalScript.DIRECTION_NW);
    directions.add(AnimalScript.DIRECTION_N);
    directions.add(AnimalScript.DIRECTION_NE);
    directions.add(AnimalScript.DIRECTION_W);
    directions.add(AnimalScript.DIRECTION_C);
    directions.add(AnimalScript.DIRECTION_E);
    directions.add(AnimalScript.DIRECTION_SW);
    directions.add(AnimalScript.DIRECTION_S);
    directions.add(AnimalScript.DIRECTION_SE);

    StringBuilder buf = new StringBuilder();
    buf.append("%Animal 2");
    if (x == Language.UNDEFINED_SIZE || y == Language.UNDEFINED_SIZE) {
      buf.append(' ').append(Math.max(640, x)).append('*');
      buf.append(Math.max(480, y));
    } else {
      buf.append(" ").append(x).append("*").append(y);
    }

    if (title != null) {
      buf.append("\ntitle \"").append(title).append("\"");
    }

    if (author != null) {
      buf.append("\nauthor \"").append(author).append("\"");
    }

    addLine(buf);
    generateGenerators();
  }

  /**
   * Generates all generators needed.
   */
  private void generateGenerators() {
    arcGen = new AnimalArcGenerator(this);
    circleGen = new AnimalCircleGenerator(this);
    circleSegGen = new AnimalCircleSegGenerator(this);
    doubleArrayGen = new AnimalDoubleArrayGenerator(this);
    doubleMatrixGen = new AnimalDoubleMatrixGenerator(this);
    ellipseGen = new AnimalEllipseGenerator(this);
    ellipseSegGen = new AnimalEllipseSegGenerator(this);
    graphGen = new AnimalGraphGenerator(this);
    intArrayGen = new AnimalIntArrayGenerator(this);
    intMatrixGen = new AnimalIntMatrixGenerator(this);
    listElemGen = new AnimalListElementGenerator(this);
    rectGen = new AnimalRectGenerator(this);
    sourceGen = new AnimalSourceCodeGenerator(this);
    squareGen = new AnimalSquareGenerator(this);
    stringArrayGen = new AnimalStringArrayGenerator(this);
    stringMatrixGen = new AnimalStringMatrixGenerator(this);
    textGen = new AnimalTextGenerator(this);
    triangleGen = new AnimalTriangleGenerator(this);
    polygonGen = new AnimalPolygonGenerator(this);
    pointGen = new AnimalPointGenerator(this);
    amGen = new AnimalArrayMarkerGenerator(this);
    groupGen = new AnimalGroupGenerator(this);
    polyGen = new AnimalPolylineGenerator(this);
    varGen = new AnimalVariablesGenerator(this);
    internalQuestionSupportGen = 
      new AnimalJHAVETextInteractionGenerator(this);
    
    // VHDL stuff
    andGateGen = new AnimalAndGenerator(this);
    nandGateGen = new AnimalNAndGenerator(this);
    norGateGen = new AnimalNorGenerator(this);
    notGateGen = new AnimalNotGenerator(this);
    orGateGen = new AnimalOrGenerator(this);
    xNorGateGen = new AnimalXNorGenerator(this);
    xorGateGen = new AnimalXorGenerator(this);

    dFlipflopGen = new AnimalDFlipflopGenerator(this);
    jkFlipflopGen = new AnimalJKFlipflopGenerator(this);
    rsFlipflopGen = new AnimalRSFlipflopGenerator(this);
    tFlipflopGen = new AnimalTFlipflopGenerator(this);
    
    muxGen = new AnimalMultiplexerGenerator(this);
    demuxGen = new AnimalDemultiplexerGenerator(this);
    
    wireGen = new AnimalWireGenerator(this);
  }

  /**
   * Adds a line to the concurrent buffer and determines if in stepmode or not.
   * The given string must not end with a newline character, which is
   * automatically appended.
   * 
   * @param line
   *          the line of AnimalScript code added, must NOT end with a newline.
   */
  public void addLine(StringBuilder line) {
    line.append("\r\n");
    if (stepMode) {
      stepBuffer.append("  ").append(line);
    } else {
      output.append(line);
    }
  }

  /**
   * Adds a label to the current AnimalScript which can be used for navigation.
   * If the given label is null, no label is inserted.
   * 
   * @param label
   *          The label
   */
  public void addLabel(String label) {
    if (label != null) {
      addLine("label \"" + label + "\"");
    }
  }

  /**
   * @see algoanim.primitives.generators.Language
   *      #addError(java.lang.StringBuilder)
   */
  public void addError(StringBuilder error) {
    error.append("\r\n");
    errorBuffer.append(error);
  }

  /**
   * Returns the current error buffer.
   * 
   * @return the current error buffer.
   */
  public String getErrorOutput() {
    return errorBuffer.toString();
  }

  /**
   * Adds the given <code>Primitive</code> to the internal database, which is
   * used to control that dupes are produced.
   * 
   * @param p
   *          the <code>Primitive</code> to add.
   */
  public void addItem(Primitive p) {
    names.add(p.getName());
  }

  /**
   * Writes all AnimalScript commands to the file given to that function. If in
   * stepmode, Stepmode is diasabled and added to output.
   * 
   * @param fileName
   *          the name of the file to write to.
   */
  public void writeFile(String fileName) {
    finalizeGeneration();
    try {
      FileWriter fw = new FileWriter(fileName);
      fw.write(toString());
      fw.close();
    } catch (Exception ex) {
      System.err.println("Error while writing file:");
      System.err.println("Filename: " + fileName);
      System.err.println(ex.getMessage());
    }

  }

  /**
   * Returns a String with the generated AnimalScript.
   */
  public String toString() {
    setStepMode(false);
    return output.toString();
  }

  /**
   * Enables or disables Step mode. All lines added after that statement is
   * cached in a buffer and flushed when the step mode is disabled. All lines in
   * one step are executed concurrently.
   * 
   * @param mode
   *          whether to enable step mode or not.
   */
  public void setStepMode(boolean mode) {
    if (!stepBuffer.toString().equalsIgnoreCase("")) {
      nextStep();
    }
    stepMode = mode;
  }
  
  
  @Override
	public int getStep() {
		return step;
	}

  /**
   * Flushes the stepbuffer to the output variable.
   */
  public void nextStep(int delay, String label) {
    step++;
    output.append("{\n").append(stepBuffer.toString());
    if (showInThisStep.size() > 0) {
      output.append("  show ");
      for (String s : showInThisStep)
        output.append('"').append(s).append("\" ");
      showInThisStep.clear();
      output.append("\n");
    }
    if (hideInThisStep.size() > 0) {
      output.append("  hide ");
      for (String s : hideInThisStep)
        output.append('"').append(s).append("\" ");
      hideInThisStep.clear();
      output.append("\n");
    }
    output.append("}\n");
    if (label != null)
      output.append("label \"").append(label).append("\"\n");
    if (delay >= 0) // we don't do negative delays!
      output.append("delay ").append(delay).append(" ms\n");
    stepBuffer = new StringBuilder(AnimalScript.INITIAL_STEPBUFFER_SIZE);
  }

  /**
   * Clears all animation data.
   */
  public void resetAnimation() {
//	nothing to be done here
  }

  /**
   * @see algoanim.primitives.generators.Language
   *      #isNameUsed(java.lang.String)
   */
  public boolean isNameUsed(String name) {
    return names.contains(name);
  }

  /**
   * @see algoanim.primitives.generators.Language#validDirections()
   */
  public Vector<String> validDirections() {
    return directions;
  }

  /**
   * @see algoanim.primitives.generators.Language
   *      #isValidDirection(java.lang.String)
   */
  public boolean isValidDirection(String direction) {
    return direction == null || directions.contains(direction)
        || direction == null;
  }

  /**
   * @see algoanim.primitives.generators.Language #newArc(
   *      algoanim.util.Node, algoanim.util.Node,
   *      java.lang.String, algoanim.util.DisplayOptions,
   *      algoanim.properties.ArcProperties)
   */
  public Arc newArc(Node center, Node radius, String name,
      DisplayOptions display, ArcProperties ep) {
    name = name==null ? "" : name;
    return new Arc(arcGen, center, radius, name, display, ep);
  }

  /**
   * @see algoanim.primitives.generators.Language #newCircle(
   *      algoanim.util.Node, int, java.lang.String,
   *      algoanim.util.DisplayOptions,
   *      algoanim.properties.CircleProperties)
   */
  public Circle newCircle(Node center, int radius, String name,
      DisplayOptions display, CircleProperties cp) {
    name = name==null ? "" : name;
    return new Circle(circleGen, center, radius, name, display, cp);
  }

  /**
   * @see algoanim.primitives.generators.Language #newCircleSeg(
   *      algoanim.util.Node, int, java.lang.String,
   *      algoanim.util.DisplayOptions,
   *      algoanim.properties.CircleSegProperties)
   */
  public CircleSeg newCircleSeg(Node center, int radius, String name,
      DisplayOptions display, CircleSegProperties cp) {
    name = name==null ? "" : name;
    return new CircleSeg(circleSegGen, center, radius, name, display, cp);
  }

  /**
   * @see algoanim.primitives.generators.Language
   *      #newEllipse(algoanim.util.Node, algoanim.util.Node,
   *      java.lang.String, algoanim.util.DisplayOptions,
   *      algoanim.properties.EllipseProperties)
   */
  public Ellipse newEllipse(Node center, Node radius, String name,
      DisplayOptions display, EllipseProperties ep) {
    name = name==null ? "" : name;
    return new Ellipse(ellipseGen, center, radius, name, display, ep);
  }


  @Override
  public EllipseSeg newEllipseSeg(Node center, Node radius, String name,
      DisplayOptions display, EllipseSegProperties cp) {
    name = name==null ? "" : name;
    return new EllipseSeg(ellipseSegGen, center, radius, name, display, cp);
  }

  /**
   * @see algoanim.primitives.generators.Language#newIntArray(
   *      algoanim.util.Node, int[], java.lang.String,
   *      algoanim.util.ArrayDisplayOptions)
   */
  public IntArray newIntArray(Node upperLeft, int[] data, String name,
      ArrayDisplayOptions display, ArrayProperties iap) {
    name = name==null ? "" : name;
    return new IntArray(intArrayGen, upperLeft, data, name, display, iap);
  }
  
  /**
   * @see algoanim.primitives.generators.Language#newIntMatrix(
   *      algoanim.util.Node, int[][], java.lang.String,
   *      algoanim.util.DisplayOptions,
   *      algoanim.properties.MatrixProperties)
   */
  public IntMatrix newIntMatrix(Node upperLeft, int[][] data, String name,
      DisplayOptions display, MatrixProperties iap) {
    name = name==null ? "" : name;
    return new IntMatrix(intMatrixGen, upperLeft, data, name, display, iap);
  }


  /**
   * @see algoanim.primitives.generators.Language#newDoubleMatrix(
   *      algoanim.util.Node, double[][], java.lang.String,
   *      algoanim.util.DisplayOptions,
   *      algoanim.properties.MatrixProperties)
   */
  public DoubleMatrix newDoubleMatrix(Node upperLeft, double[][] data, String name,
      DisplayOptions display, MatrixProperties iap) {
    name = name==null ? "" : name;
    return new DoubleMatrix(doubleMatrixGen, upperLeft, data, name, display, iap);
  }

  /**
   * @see algoanim.primitives.generators.Language #newListElement(
   *      algoanim.util.Node, int, java.util.LinkedList,
   *      algoanim.primitives.ListElement, java.lang.String,
   *      algoanim.util.DisplayOptions,
   *      algoanim.properties.ListElementProperties)
   */
  public ListElement newListElement(Node upperLeft, int pointers,
      LinkedList<Object> pointerLocations, ListElement prev, ListElement next, String name,
      DisplayOptions display, ListElementProperties lp) {
    name = name==null ? "" : name;
    return new ListElement(listElemGen, upperLeft, pointers,
    		pointerLocations, prev, next, name, display, lp);
  }

  /**
   * @see algoanim.primitives.generators.Language #newPoint(
   *      algoanim.util.Node, java.lang.String,
   *      algoanim.util.DisplayOptions,
   *      algoanim.properties.PointProperties)
   */
  public Point newPoint(Node coords, String name, DisplayOptions display,
      PointProperties pp) {
    name = name==null ? "" : name;
    return new Point(pointGen, coords, name, display, pp);
  }

  /**
   * @see algoanim.primitives.generators.Language #newPolygon(
   *      algoanim.util.Node[], java.lang.String,
   *      algoanim.util.DisplayOptions,
   *      algoanim.properties.PolygonProperties)
   */
  public Polygon newPolygon(Node[] vertices, String name,
      DisplayOptions display, PolygonProperties pp)
      throws NotEnoughNodesException {
    name = name==null ? "" : name;
    return new Polygon(polygonGen, vertices, name, display, pp);
  }

  /**
   * @see algoanim.primitives.generators.Language #newRect(
   *      algoanim.util.Node, algoanim.util.Node,
   *      java.lang.String, algoanim.util.DisplayOptions,
   *      algoanim.properties.RectProperties)
   */
  public Rect newRect(Node upperLeft, Node lowerRight, String name,
      DisplayOptions display, RectProperties rp) {
    name = name==null ? "" : name;
    return new Rect(rectGen, upperLeft, lowerRight, name, display, rp);
  }

  /**
   * @see algoanim.primitives.generators.Language #newSourceCode(
   *      algoanim.util.Node, java.lang.String,
   *      algoanim.util.DisplayOptions,
   *      algoanim.properties.SourceCodeProperties)
   */
  public SourceCode newSourceCode(Node upperLeft, String name,
      DisplayOptions display, SourceCodeProperties sp) {
    name = name==null ? "" : name;
    return new SourceCode(sourceGen, upperLeft, name, display, sp);
  }

  /**
   * @see algoanim.primitives.generators.Language #newSquare(
   *      algoanim.util.Node, int, java.lang.String,
   *      algoanim.util.DisplayOptions,
   *      algoanim.properties.SquareProperties)
   */
  public Square newSquare(Node upperLeft, int width, String name,
      DisplayOptions display, SquareProperties sp) {
    name = name==null ? "" : name;
    return new Square(squareGen, upperLeft, width, name, display, sp);
  }

  /**
   * @see algoanim.primitives.generators.Language#newStringArray(
   *      algoanim.util.Node, java.lang.String[], java.lang.String,
   *      algoanim.util.ArrayDisplayOptions)
   */
  public StringArray newStringArray(Node upperLeft, String[] data, String name,
      ArrayDisplayOptions display, ArrayProperties sap) {
    name = name==null ? "" : name;
    return new StringArray(stringArrayGen, upperLeft, data, name, display,
        sap);
  }

  
  /**
   * @see algoanim.primitives.generators.Language#newStringMatrix(
   *      algoanim.util.Node, String[][], java.lang.String,
   *      algoanim.util.DisplayOptions)
   */
  public StringMatrix newStringMatrix(Node upperLeft, String[][] data, String name,
      DisplayOptions display, MatrixProperties iap) {
    name = name==null ? "" : name;
    return new StringMatrix(stringMatrixGen, upperLeft, data, name, 
    		display, iap);
  }

  /**
   * @see algoanim.primitives.generators.Language #newText(
   *      algoanim.util.Node, java.lang.String, java.lang.String,
   *      algoanim.util.DisplayOptions,
   *      algoanim.properties.TextProperties)
   */
  public Text newText(Node upperLeft, String text, String name,
      DisplayOptions display, TextProperties tp) {
    name = name==null ? "" : name;
    return new Text(textGen, upperLeft, text, name, display, tp);
  }

  /**
   * @see algoanim.primitives.generators.Language #newTriangle(
   *      algoanim.util.Node, algoanim.util.Node,
   *      algoanim.util.Node, java.lang.String,
   *      algoanim.util.DisplayOptions,
   *      algoanim.properties.TriangleProperties)
   */
  public Triangle newTriangle(Node x, Node y, Node z, String name,
      DisplayOptions display, TriangleProperties tp) {
    name = name==null ? "" : name;
    return new Triangle(triangleGen, x, y, z, name, display, tp);
  }
  
  /**
   * @see algoanim.primitives.generators.Language #newVariables()
   */
  public Variables newVariables() {
	  return new Variables(varGen, null);
  }

  /**
   * @see algoanim.primitives.generators.Language #newArrayMarker(
   *      algoanim.primitives.ArrayPrimitive, int, java.lang.String,
   *      algoanim.util.DisplayOptions,
   *      algoanim.properties.ArrayMarkerProperties)
   */
  public ArrayMarker newArrayMarker(ArrayPrimitive a, int index, String name,
      DisplayOptions display, ArrayMarkerProperties ap) {
    name = name==null ? "" : name;
    return new ArrayMarker(amGen, a, index, name, display, ap);
  }

  /**
   * @see algoanim.primitives.generators.Language
   *      #newGroup(java.util.LinkedList, java.lang.String)
   */
  public Group newGroup(LinkedList<Primitive> primitives, String name) {
    name = name==null ? "" : name;
    return new Group(groupGen, primitives, name);
  }

  /**
   * @see algoanim.primitives.generators.Language #newPolyline(
   *      algoanim.util.Node[], java.lang.String,
   *      algoanim.util.DisplayOptions,
   *      algoanim.properties.PolylineProperties)
   */
  public Polyline newPolyline(Node[] vertices, String name,
      DisplayOptions display, PolylineProperties pp) {
    name = name==null ? "" : name;
    return new Polyline(polyGen, vertices, name, display, pp);
  }
 
  /**
   * @see algoanim.primitives.generators.Language #newGraph(
   *      java.lang.String, int[][], algoanim.util.Node[],
   *      java.lang.String[],
   *      algoanim.util.DisplayOptions,
   *      algoanim.properties.GraphProperties)
   */
	public Graph newGraph(String name, int[][] graphAdjacencyMatrix, 
			Node[] graphNodes, String[] labels, DisplayOptions display, 
			GraphProperties graphProps) {
    name = name==null ? "" : name;
    return new Graph(graphGen, name, graphAdjacencyMatrix,
    		graphNodes, labels, display, graphProps);
	}

  @Override
  public Graph addGraph(Graph aGraph, DisplayOptions display,
      GraphProperties props) {
    return new Graph(graphGen, aGraph, display, props);
  }
  

  /**
   * @see algoanim.primitives.generators.Language #newGraph( java.lang.String,
   *      int[][], java.lang.String[], algoanim.util.DisplayOptions,
   *      algoanim.properties.GraphProperties, int, int, int, int, int, int, int,
   *      int, boolean, int, int)
   */
  public Graph newGraph(String name, int[][] graphAdjacencyMatrix, String[] labels, DisplayOptions display,
      GraphProperties graphProps, int xUpperLeft, int yUpperLeft, int xLowerRight, int yLowerRight,
      int minEdgeLength, int nodeRadius, int loopIterations, int postIterations, boolean raster, int xEpsilon,
      int yEpsilon) {

    return new Graph(graphGen, name, graphAdjacencyMatrix, labels, display, graphProps, xUpperLeft, yUpperLeft,
        xLowerRight, yLowerRight, minEdgeLength, nodeRadius, loopIterations, postIterations, raster, xEpsilon,
        yEpsilon);
  }
  
  /**
   * @see algoanim.primitives.generators.Language #newGraph( java.lang.String,
   *      int[][], java.lang.String[], algoanim.util.DisplayOptions,
   *      algoanim.properties.GraphProperties, int, int, int, int, int, int, int,
   *      int)
   */
  public Graph newGraph(String name, int[][] graphAdjacencyMatrix, String[] labels, DisplayOptions display,
      GraphProperties graphProps, int xUpperLeft, int yUpperLeft, int xLowerRight, int yLowerRight,
      int minEdgeLength, int nodeRadius, int loopIterations, int postIterations) {

    return new Graph(graphGen, name, graphAdjacencyMatrix, labels, display, graphProps, xUpperLeft, yUpperLeft,
        xLowerRight, yLowerRight, minEdgeLength, nodeRadius, loopIterations, postIterations, false, 0, 0);
  }

  /**
   * @see algoanim.primitives.generators.Language #newGraph( java.lang.String,
   *      int[][], java.lang.String[], algoanim.util.DisplayOptions,
   *      algoanim.properties.GraphProperties, int, int, int, int, boolean, int,
   *      int)
   */
  public Graph newGraph(String name, int[][] graphAdjacencyMatrix, String[] labels, DisplayOptions display,
      GraphProperties graphProps, int xUpperLeft, int yUpperLeft, int xLowerRight, int yLowerRight,
      boolean raster, int xEpsilon, int yEpsilon) {

    return new Graph(graphGen, name, graphAdjacencyMatrix, labels, display, graphProps, xUpperLeft, yUpperLeft,
        xLowerRight, yLowerRight, raster, xEpsilon, yEpsilon);
  }

  /**
   * @see algoanim.primitives.generators.Language #newGraph( java.lang.String,
   *      int[][], java.lang.String[], algoanim.util.DisplayOptions,
   *      algoanim.properties.GraphProperties, int, int, int, int)
   */
  public Graph newGraph(String name, int[][] graphAdjacencyMatrix, String[] labels, DisplayOptions display,
      GraphProperties graphProps, int xUpperLeft, int yUpperLeft, int xLowerRight, int yLowerRight) {

    return new Graph(graphGen, name, graphAdjacencyMatrix, labels, display, graphProps, xUpperLeft, yUpperLeft,
        xLowerRight, yLowerRight);
  }

  /**
   * @see algoanim.primitives.generators.Language #newGraph( java.lang.String,
   *      int[][], java.lang.String[], int, int, int, int)
   */
  public Graph newGraph(String name, int[][] graphAdjacencyMatrix, String[] labels, int xUpperLeft, int yUpperLeft,
      int xLowerRight, int yLowerRight) {

    return new Graph(graphGen, name, graphAdjacencyMatrix, labels, xUpperLeft, yUpperLeft, xLowerRight,
        yLowerRight);
  }

   /**
   * @see algoanim.primitives.generators.Language #newGraph(
   *      java.lang.String, int[][], algoanim.util.Node[],
   *      java.lang.String[],
   *      algoanim.util.DisplayOptions,
   *      algoanim.properties.GraphProperties,
   *      int, int, int,
      int, int, int, int, int, boolean, int, int)
   */
  public Graph newGraph(String name, int[][] graphAdjacencyMatrix, 
      Node[] graphNodes, String[] labels, DisplayOptions display, 
      GraphProperties graphProps, int xUpperLeft, int yUpperLeft, int xLowerRight,
        int yLowerRight, int minEdgeLength, int nodeRadius, int loopIterations, int postIterations, boolean raster, int xEpsilon, int yEpsilon) {
    
    return new Graph(graphGen, name, graphAdjacencyMatrix,
        graphNodes, labels, display, graphProps, xUpperLeft, yUpperLeft, xLowerRight,
        yLowerRight, minEdgeLength, nodeRadius, loopIterations, postIterations, raster, xEpsilon, yEpsilon);
  }
  
  
  /**
     * @see algoanim.primitives.generators.Language #newGraph(
     *      java.lang.String, int[][], algoanim.util.Node[],
     *      java.lang.String[],
     *      algoanim.util.DisplayOptions,
     *      algoanim.properties.GraphProperties,
     *      int, int, int,
        int, int, int, int, int)
     */
    public Graph newGraph(String name, int[][] graphAdjacencyMatrix, 
        Node[] graphNodes, String[] labels, DisplayOptions display, 
        GraphProperties graphProps, int xUpperLeft, int yUpperLeft, int xLowerRight,
          int yLowerRight, int minEdgeLength, int nodeRadius, int loopIterations, int postIterations) {
      
      return new Graph(graphGen, name, graphAdjacencyMatrix,
          graphNodes, labels, display, graphProps, xUpperLeft, yUpperLeft, xLowerRight,
        yLowerRight, minEdgeLength, nodeRadius, loopIterations, postIterations, false, 0, 0);
    }

    /**
       * @see algoanim.primitives.generators.Language #newGraph(
       *      java.lang.String, int[][], algoanim.util.Node[],
       *      java.lang.String[],
       *      algoanim.util.DisplayOptions,
       *      algoanim.properties.GraphProperties,
       *      int, int, int,
          int,  boolean, int, int)
       */
      public Graph newGraph(String name, int[][] graphAdjacencyMatrix, 
          Node[] graphNodes, String[] labels, DisplayOptions display, 
          GraphProperties graphProps, int xUpperLeft, int yUpperLeft, int xLowerRight,
            int yLowerRight, boolean raster, int xEpsilon, int yEpsilon) {
        
        return new Graph(graphGen, name, graphAdjacencyMatrix,
            graphNodes, labels, display, graphProps, xUpperLeft, yUpperLeft, xLowerRight,
            yLowerRight,  raster, xEpsilon, yEpsilon);
      }
      
      /**
         * @see algoanim.primitives.generators.Language #newGraph(
         *      java.lang.String, int[][], algoanim.util.Node[],
         *      java.lang.String[],
         *      algoanim.util.DisplayOptions,
         *      algoanim.properties.GraphProperties,
         *      int, int, int,
            int)
         */
        public Graph newGraph(String name, int[][] graphAdjacencyMatrix, 
            Node[] graphNodes, String[] labels, DisplayOptions display, 
            GraphProperties graphProps, int xUpperLeft, int yUpperLeft, int xLowerRight,
              int yLowerRight) {
          
          return new Graph(graphGen, name, graphAdjacencyMatrix,
              graphNodes, labels, display, graphProps, xUpperLeft, yUpperLeft, xLowerRight,yLowerRight);
        }

  /**
   * @see algoanim.primitives.generators.Language #newGraph( java.lang.String,
   *      int[][], algoanim.util.Node[], java.lang.String[], int, int, int, int)
   */
  public Graph newGraph(String name, int[][] graphAdjacencyMatrix, Node[] graphNodes, String[] labels, int xUpperLeft,
      int yUpperLeft, int xLowerRight, int yLowerRight) {

    return new Graph(graphGen, name, graphAdjacencyMatrix, graphNodes, labels, xUpperLeft, yUpperLeft, xLowerRight,
        yLowerRight);
  }
  
	
	/** 
	 * @see algoanim.primitives.generators.Language#newConceptualStack(
	 * 		algoanim.util.Node, java.util.List, java.lang.String, 
	 * 		algoanim.util.DisplayOptions, algoanim.properties.StackProperties)
	 */
	public <T> ConceptualStack<T> newConceptualStack(Node upperLeft, List<T> content,
			String name, DisplayOptions display, StackProperties sp) {
    name = name==null ? "" : name;
		ConceptualStackGenerator<T> csGen = new AnimalConceptualStackGenerator<T>(this);
		return new ConceptualStack<T>(csGen, upperLeft, content, name, display, sp);
	}
	
	/** 
	 * @see algoanim.primitives.generators.Language#newArrayBasedStack(
	 * 		algoanim.util.Node, java.util.List, java.lang.String, 
	 * 		algoanim.util.DisplayOptions, algoanim.properties.StackProperties,
	 * 		int)
	 */
	public <T> ArrayBasedStack<T> newArrayBasedStack(Node upperLeft, List<T> content,
			String name, DisplayOptions display, StackProperties sp, int capacity) {
    name = name==null ? "" : name;
		ArrayBasedStackGenerator<T> absGen = new AnimalArrayBasedStackGenerator<T>(this);
		return new ArrayBasedStack<T>(absGen, upperLeft, content, name, display, sp, capacity);
	}
	
	/** 
	 * @see algoanim.primitives.generators.Language#newListBasedStack(
	 * 		algoanim.util.Node, java.util.List, java.lang.String, 
	 * 		algoanim.util.DisplayOptions, algoanim.properties.StackProperties)
	 */
	public <T> ListBasedStack<T> newListBasedStack(Node upperLeft, List<T> content,
			String name, DisplayOptions display, StackProperties sp) {
    name = name==null ? "" : name;
		ListBasedStackGenerator<T> lbsGen = new AnimalListBasedStackGenerator<T>(this);
		return new ListBasedStack<T>(lbsGen, upperLeft, content, name, display, sp);
	}

	/**
	 * @see algoanim.primitives.generators.Language#newConceptualQueue(
	 * 		algoanim.util.Node, java.util.List, java.lang.String, 
	 * 		algoanim.util.DisplayOptions, algoanim.properties.QueueProperties)
	 */
	public <T> ConceptualQueue<T> newConceptualQueue(Node upperLeft, List<T> content, 
			String name, DisplayOptions display, QueueProperties qp) {
    name = name==null ? "" : name;
		ConceptualQueueGenerator<T> cqGen = new AnimalConceptualQueueGenerator<T>(this);
		return new ConceptualQueue<T>(cqGen, upperLeft, content, name, display, qp);
	}
	
	/** 
	 * @see algoanim.primitives.generators.Language#newArrayBasedQueue(
	 * 		algoanim.util.Node, java.util.List, java.lang.String,
	 * 		algoanim.util.DisplayOptions, algoanim.properties.QueueProperties, 
	 * 		int)
	 */
	public <T> ArrayBasedQueue<T> newArrayBasedQueue(Node upperLeft, List<T> content,
			String name, DisplayOptions display, QueueProperties qp, int capacity) {
    name = name==null ? "" : name;
		ArrayBasedQueueGenerator<T> abqGen = new AnimalArrayBasedQueueGenerator<T>(this);
		return new ArrayBasedQueue<T>(abqGen, upperLeft, content, name, display, qp, capacity);
	}

	/**
	 * @see algoanim.primitives.generators.Language#newListBasedQueue(
	 * 		algoanim.util.Node, java.util.List, java.lang.String, 
	 * 		algoanim.util.DisplayOptions, algoanim.properties.QueueProperties)
	 */
	public <T> ListBasedQueue<T> newListBasedQueue(Node upperLeft, List<T> content, 
			String name, DisplayOptions display, QueueProperties qp) {
    name = name==null ? "" : name;
		ListBasedQueueGenerator<T> lbqGen = new AnimalListBasedQueueGenerator<T>(this);
		return new ListBasedQueue<T>(lbqGen, upperLeft, content, name, display, qp);
	}

  public void addDocumentationLink(HtmlDocumentationModel docuLink) {
    internalQuestionSupportGen.createInteraction(docuLink);
    interactiveElements.put(docuLink.getID(), docuLink);
  }
  
  public void addTFQuestion(TrueFalseQuestionModel tfQuestion) {
    internalQuestionSupportGen.createInteraction(tfQuestion);
    interactiveElements.put(tfQuestion.getID(), tfQuestion);
  }
  
  public void addFIBQuestion(FillInBlanksQuestionModel fibQuestion) {
    internalQuestionSupportGen.createInteraction(fibQuestion);
    interactiveElements.put(fibQuestion.getID(), fibQuestion);
  }
  
  public void addMCQuestion(MultipleChoiceQuestionModel mcQuestion) {
    internalQuestionSupportGen.createInteraction(mcQuestion);
    interactiveElements.put(mcQuestion.getID(), mcQuestion);
  }

  public void addMSQuestion(MultipleSelectionQuestionModel msQuestion) {
    internalQuestionSupportGen.createInteraction(msQuestion);
    interactiveElements.put(msQuestion.getID(), msQuestion);
  }

  public void addQuestionGroup(QuestionGroupModel group) {
    interactiveElements.put(group.getID(), group);
  }

  
  public void finalizeGeneration() {
    setStepMode(false);
    if (!interactiveElements.isEmpty()) {
      Set<String> interactionKeys = interactiveElements.keySet();
      Iterator<String> interactionIterator = interactionKeys.iterator();
      while (interactionIterator.hasNext()) {
        String key = interactionIterator.next();
        InteractionModel element = interactiveElements.get(key);
        internalQuestionSupportGen.createInteractiveElementCode(element);
      }
      // dump data
      internalQuestionSupportGen.finalizeInteractiveElements();
    }
  }

  public String getAnimationCode() {
    return output.toString();
  }
  private String generateInteractionKey() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    StringBuffer sb = new StringBuffer(80);
    sb.append("intDef");
    sb = sdf.format(new Date(), sb, new FieldPosition(DateFormat.YEAR_FIELD));
    sb.append(".txt");
    return sb.toString();
  }

  public void setInteractionType(int interactionTypeCode) {
    setInteractionType(interactionTypeCode, generateInteractionKey()); //, "intDefFile.txt");
  }
  
  public void setInteractionType(int interactionTypeCode, String key) {
    if (!interactiveElements.isEmpty())
      throw new IllegalStateException("You cannot change the interaction type if elements have been generated before!");
    if (interactionTypeCode == Language.INTERACTION_TYPE_AVINTERACTION)
      internalQuestionSupportGen = new InteractionDefinitionGenerator(this,
          key);
    else
      internalQuestionSupportGen = 
        new AnimalJHAVETextInteractionGenerator(this); 
  }

  @Override
  public DoubleArray newDoubleArray(Node upperLeft, double[] data, String name,
      ArrayDisplayOptions display, ArrayProperties iap) {
    name = name==null ? "" : name;
    return new DoubleArray(doubleArrayGen, upperLeft, data, name, display, iap);
  }

  @Override
  public AndGate newAndGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display,
      VHDLElementProperties properties) {
    name = name==null ? "" : name;
    return new AndGate(andGateGen, upperLeft, width, height, name,
        pins, display, properties);
  }

  @Override
  public NAndGate newNAndGate(Node upperLeft, int width, int height,
      String name, List<VHDLPin> pins, DisplayOptions display,
      VHDLElementProperties properties) {
    name = name==null ? "" : name;
    return new NAndGate(nandGateGen, upperLeft, width, height, name,
        pins, display, properties);  }

  @Override
  public NorGate newNorGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display,
      VHDLElementProperties properties) {
    name = name==null ? "" : name;
    return new NorGate(norGateGen, upperLeft, width, height, name,
        pins, display, properties);  }

  @Override
  public NotGate newNotGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display,
      VHDLElementProperties properties) {
    name = name==null ? "" : name;
    return new NotGate(notGateGen, upperLeft, width, height, name,
        pins, display, properties);  }

  @Override
  public OrGate newOrGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display,
      VHDLElementProperties properties) {
    name = name==null ? "" : name;
    return new OrGate(orGateGen, upperLeft, width, height, name,
        pins, display, properties);  }

  @Override
  public XNorGate newXNorGate(Node upperLeft, int width, int height,
      String name, List<VHDLPin> pins, DisplayOptions display,
      VHDLElementProperties properties) {
    name = name==null ? "" : name;
    return new XNorGate(xNorGateGen, upperLeft, width, height, name,
        pins, display, properties); }

  @Override
  public XOrGate newXOrGate(Node upperLeft, int width, int height, String name,
      List<VHDLPin> pins, DisplayOptions display,
      VHDLElementProperties properties) {
    name = name==null ? "" : name;
    return new XOrGate(xorGateGen, upperLeft, width, height, name,
        pins, display, properties);  }

  @Override
  public DFlipflop newDFlipflop(Node upperLeft, int width, int height,
      String name, List<VHDLPin> pins, DisplayOptions display,
      VHDLElementProperties properties) {
    name = name==null ? "" : name;
    return new DFlipflop(dFlipflopGen, upperLeft, width, height, name,
        pins, display, properties);
  }

  @Override
  public JKFlipflop newJKFlipflop(Node upperLeft, int width, int height,
      String name, List<VHDLPin> pins, DisplayOptions display,
      VHDLElementProperties properties) {
    name = name==null ? "" : name;
    return new JKFlipflop(jkFlipflopGen, upperLeft, width, height, name,
        pins, display, properties);
  }

  @Override
  public RSFlipflop newRSFlipflop(Node upperLeft, int width, int height,
      String name, List<VHDLPin> pins, DisplayOptions display,
      VHDLElementProperties properties) {
    name = name==null ? "" : name;
    return new RSFlipflop(rsFlipflopGen, upperLeft, width, height, name,
        pins, display, properties);
  }

  @Override
  public TFlipflop newTFlipflop(Node upperLeft, int width, int height,
      String name, List<VHDLPin> pins, DisplayOptions display,
      VHDLElementProperties properties) {
    name = name==null ? "" : name;
    return new TFlipflop(tFlipflopGen, upperLeft, width, height, name,
        pins, display, properties);
  }

  @Override
  public Demultiplexer newDemultiplexer(Node upperLeft, int width, int height,
      String name, List<VHDLPin> pins, DisplayOptions display,
      VHDLElementProperties properties) {
    name = name==null ? "" : name;
    return new Demultiplexer(demuxGen, upperLeft, width, height, name,
        pins, display, properties);
  }

  @Override
  public Multiplexer newMultiplexer(Node upperLeft, int width, int height,
      String name, List<VHDLPin> pins, DisplayOptions display,
      VHDLElementProperties properties) {
    name = name==null ? "" : name;
    return new Multiplexer(muxGen, upperLeft, width, height, name,
        pins, display, properties);
  }

  @Override
  public VHDLWire newWire(List<Node> nodes, int speed, String name, 
      DisplayOptions display, VHDLWireProperties properties) {
    name = name==null ? "" : name;
    return new VHDLWire(wireGen, nodes, speed, name,
        display, properties);
  }

  @Override
  public void hideAllPrimitives() {
    stepBuffer.append("  hideAll\n");  
  }

  @Override
  public void hideAllPrimitivesExcept(Primitive keepThisOne) {
    if (keepThisOne == null)
      hideAllPrimitives();
    else {
      stepBuffer.append("  hideAllBut \"").append(keepThisOne.getName());
      stepBuffer.append("\"\n");
    }
  }
  @Override
  public void hideAllPrimitivesExcept(List<Primitive> keepThese) {
    if (keepThese == null || keepThese.size() == 0)
      hideAllPrimitives();
    else {
      stepBuffer.append("  hideAllBut ");
      for (Primitive prim : keepThese)
        stepBuffer.append('\"').append(prim.getName()).append("\" ");
      stepBuffer.append("\n");
    }
  }

}