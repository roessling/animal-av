package algoanim.primitives;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.generators.SourceCodeGenerator;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * Represents a source code element defined by its upper left corner and source
 * code lines, which can be added.
 * 
 * @author Stephan Mehlhase
 */
public class SourceCode extends Primitive {
  /* primitive variables */
  protected SourceCodeProperties     properties;
  protected SourceCodeGenerator      generator;
  protected Node                     upperLeft;

  /* source code variables */
  protected LinkedList<String>       lines;
  protected HashMap<String, Integer> labelLines;
  protected HashMap<String, Integer> labelRows;
  protected Integer                  actRow;
  protected Vector<String>           highlightedLabels;

  /**
   * Instantiates the <code>SourceCode</code> and calls the create() method of
   * the associated <code>SourceCodeGenerator</code>.
   * 
   * @param generator
   *          the appropriate code <code>Generator</code>.
   * @param upperLeft
   *          the upper left corner of this <code>SourceCode</code> element.
   * @param name
   *          the name of this <code>SourceCode</code> element.
   * @param display
   *          [optional] the <code>DisplayOptions</code> of this
   *          <code>SourceCode</code> element.
   * @param properties
   *          [optional] the properties of this <code>SourceCode</code> element.
   */
  public SourceCode(SourceCodeGenerator generator, Node upperLeft, String name,
      DisplayOptions display, SourceCodeProperties properties) {
    /* setup primitive */
    super(generator, display);
    this.upperLeft = upperLeft;
    this.properties = properties;
    this.setName(name);
    this.generator = generator;

    /* setup source code stuff */
    lines = new LinkedList<String>();
    labelLines = new HashMap<String, Integer>();
    labelRows = new HashMap<String, Integer>();
    actRow = 0;
    highlightedLabels = new Vector<String>();

    /* call generator */
    generator.create(this);
  }

  /**
   * @see algoanim.primitives.Primitive#setName(java.lang.String)
   */
  public void setName(String newName) {
    properties.setName(newName);
    super.setName(newName);
  }

  /**
   * short form without row
   */
  public void registerLabel(String label, int lineNo) {
    registerLabel(label, lineNo, 0);
  }

  public void registerLabel(String label, int lineNo, int rowNo) {
    // System.out.println("register: "+label+"-->"+(new Integer(lineNo)));
    if (label != null && lineNo >= 0 && rowNo >= 0) {
      // System.out.println("register: \""+label+"\" --> "+lineNo +":"+ rowNo);
      labelLines.put(label, Integer.valueOf(lineNo));
      labelRows.put(label, Integer.valueOf(rowNo));
    }
  }

  /**
   * Returns the properties of this <code>SourceCode</code> element.
   * 
   * @return the properties of this <code>SourceCode</code> element.
   */
  public SourceCodeProperties getProperties() {
    return properties;
  }

  /**
   * Returns the upper left corner of this <code>SourceCode</code> element.
   * 
   * @return the upper left corner of this<code>SourceCode</code> element.
   */
  public Node getUpperLeft() {
    return upperLeft;
  }

  /**
   * Adds a new code line to this <code>SourceCode</code> element.
   * 
   * @param code
   *          the actual code.
   * @param label
   *          a distinct name for the line.
   * @param indentation
   *          the indentation to apply to this line.
   * @param delay
   *          [optional] the delay after which this operation shall be
   *          performed.
   * @return the line number of the added code line to be able to reference it
   *         later on.
   */
  public int addCodeLine(String code, String label, int indentation,
      Timing delay) throws NullPointerException {
    if (code != null) {
      // add the code line at the end of the list of lines
      lines.add(code);
      this.generator.addCodeLine(this, code, label, indentation, delay);

      int thisLineNo = lines.size() - 1;
      actRow = 0;

      if (label != null)
        registerLabel(label, thisLineNo);

      return thisLineNo;
    }
    throw new NullPointerException("Please provide a code line.");
  }

  /**
   * Adds a new code element to this <code>SourceCode</code> element.
   * 
   * @param code
   *          the actual code.
   * @param label
   *          a distinct name for the line.
   * @param indentation
   *          the indentation to apply to this line.
   * @param delay
   *          [optional] the delay after which this operation shall be
   *          performed.
   * @return the line number of that line, to which this element was added, to
   *         be able to reference it later on.
   */
  public int addCodeElement(String code, String label,
      boolean noSpaceSeparator, int indentation, Timing delay)
      throws NullPointerException {
    if (code == null)
      throw new NullPointerException("Please provide a code line.");

    actRow++;
    int lineNo = lines.size() - 1;

    // associate the code element with a row placement
    if (label != null)
      registerLabel(label, lineNo, actRow);

    this.generator.addCodeElement(this, code, label, indentation,
        noSpaceSeparator, actRow, delay);

    return lineNo;
  }

  /**
   * Adds a new code element to this <code>SourceCode</code> element.
   * 
   * @param code
   *          the actual code.
   * @param label
   *          a distinct name for the line.
   * @param indentation
   *          the indentation to apply to this line.
   * @param delay
   *          [optional] the delay after which this operation shall be
   *          performed.
   * @return the line number of that line, to which this element was added, to
   *         be able to reference it later on.
   */
  public int addCodeElement(String code, String label, int indentation,
      Timing delay) throws NullPointerException {
    return addCodeElement(code, label, false, indentation, delay);
  }

  /**
   * Adds multiple lines of code and uses \t indentation to determine the indentation depth
   * 
   * @param code the code to be added; lines have to be separated by a carriage return ("\n"),
   * and indentation is performed by a tab character ("\t")
   * @param label a distinct base name for the lines; "_0", "_1" etc. will be added to make
   * the names unique.
   * @param delay the delay imposed before the code lines are displayed
   */
  public void addMultilineCode(String code, String label, Timing delay) {    
    String[] lines = code.split("\n");
    int lineNo = 0;
    for (String line : lines){
      int indentation=0;
      while (line.charAt(indentation)=='\t') 
        indentation++;
      line = line.replaceAll("\t+", "");
      lineNo++;
      this.addCodeLine(line, label +"_" +lineNo, indentation, delay);
    }
  }

  /**
   * Highlights a line in this <code>SourceCode</code> element.
   * 
   * @param label
   *          the name of the line to highlight
   */
  public void highlight(String label) throws LineNotExistsException {
    highlight(label, false);
  }

  /**
   * Highlights a line in this <code>SourceCode</code> element.
   * 
   * @param label
   *          the name of the line to highlight
   * @param context
   *          use the code context color instead of the code highlight color.
   */
  public void highlight(String label, boolean context)
      throws LineNotExistsException {
    highlight(label, context, null, null);
  }

  /**
   * Highlights a line in this <code>SourceCode</code> element.
   * 
   * @param label
   *          the name of the line to highlight
   * @param context
   *          use the code context color instead of the code highlight color.
   * @param delay
   *          [optional] the delay to apply to this operation.
   * @param duration
   *          [optional] the duration of the action.
   */
  public void highlight(String label, boolean context, Timing delay,
      Timing duration) throws LineNotExistsException {
    Integer lineNo = labelLines.get(label);
    Integer rowNo = labelRows.get(label);

    if (lineNo == null || rowNo == null)
      System.err.println("label not found: " + label);
    else {
      this.highlight(lineNo, rowNo, context, delay, duration);
      if (!highlightedLabels.contains(label))
        highlightedLabels.add(label);
    }
  }

  /**
   * Highlights a line in this <code>SourceCode</code> element.
   * 
   * @param lineNo
   *          the line to highlight
   */
  public void highlight(int lineNo) throws LineNotExistsException {
    highlight(lineNo, 0, false);
  }

  /**
   * Highlights a line in this <code>SourceCode</code> element.
   * 
   * @param lineNo
   *          the line to highlight
   * @param colNo
   *          the code element to highlight
   * @param context
   *          use the code context color instead of the code highlight color.
   */
  public void highlight(int lineNo, int colNo, boolean context)
      throws LineNotExistsException {
    highlight(lineNo, colNo, context, null, null);
  }

  /**
   * Highlights a line in this <code>SourceCode</code> element.
   * 
   * @param lineNo
   *          the line to highlight
   * @param colNo
   *          the code element to highlight
   * @param context
   *          use the code context color instead of the code highlight color.
   * @param delay
   *          [optional] the delay to apply to this operation.
   * @param duration
   *          [optional] the duration of the action.
   */
  public void highlight(int lineNo, int colNo, boolean context, Timing delay,
      Timing duration) throws LineNotExistsException {
    if (lineNo >= lines.size())
      throw new LineNotExistsException("lineNo \"" + lineNo + "\" too high");

    this.generator.highlight(this, lineNo, colNo, context, delay, duration);
  }

  /* ****************************************************************************
   * UNHIGHLIGHT
   * ***************************************************************************
   */

  /**
   * Unhighlights a line in this <code>SourceCode</code> element.
   * 
   * @param label
   *          the name of the line to unhighlight
   */
  public void unhighlight(String label) throws LineNotExistsException {
    unhighlight(label, false);
  }

  /**
   * Unhighlights a line in this <code>SourceCode</code> element.
   * 
   * @param label
   *          the name of the line to unhighlight
   * @param context
   *          use the code context color instead of the code highlight color.
   */
  public void unhighlight(String label, boolean context)
      throws LineNotExistsException {
    unhighlight(label, context, null, null);
  }

  /**
   * Unhighlights a line in this <code>SourceCode</code> element.
   * 
   * @param label
   *          the name of the line to unhighlight
   * @param context
   *          use the code context color instead of the code highlight color.
   * @param delay
   *          [optional] the delay to apply to this operation.
   * @param duration
   *          [optional] the duration of the action.
   */
  public void unhighlight(String label, boolean context, Timing delay,
      Timing duration) throws LineNotExistsException {
    Integer lineNo = labelLines.get(label);
    Integer rowNo = labelRows.get(label);

    if (lineNo == null || rowNo == null || lineNo.intValue() >= lines.size())
      throw new LineNotExistsException("lineNo \"" + lineNo + "\" too high");

    generator.unhighlight(this, lineNo, rowNo, context, delay, duration);
    highlightedLabels.remove(label);
  }

  /**
   * Unhighlights a line in this <code>SourceCode</code> element.
   * 
   * @param lineNo
   *          the line to unhighlight
   */
  public void unhighlight(int lineNo) throws LineNotExistsException {
    unhighlight(lineNo, 0, false);
  }

  /**
   * Unhighlights a line in this <code>SourceCode</code> element.
   * 
   * @param lineNo
   *          the line to unhighlight
   * @param colNo
   *          the code element to unhighlight
   * @param context
   *          use the code context color instead of the code highlight color.
   */
  public void unhighlight(int lineNo, int colNo, boolean context)
      throws LineNotExistsException {
    unhighlight(lineNo, colNo, context, null, null);
  }

  /**
   * Unhighlights a line in this <code>SourceCode</code> element.
   * 
   * @param lineNo
   *          the line to unhighlight
   * @param colNo
   *          the code element to unhighlight
   * @param context
   *          use the code context color instead of the code highlight color.
   * @param delay
   *          [optional] the delay to apply to this operation.
   * @param duration
   *          [optional] the duration of the action.
   */
  public void unhighlight(int lineNo, int colNo, boolean context, Timing delay,
      Timing duration) throws LineNotExistsException {
    if (lineNo >= lines.size())
      throw new LineNotExistsException("lineNo \"" + lineNo + "\" too high");

    generator.unhighlight(this, lineNo, colNo, context, delay, duration);
  }

  /* ****************************************************************************
   * TOGGLE
   * ***************************************************************************
   */

  public void toggleHighlight(String label) {
    // alle highlights entfernen
    if (highlightedLabels.size() > 0) {
      Object[] labels = highlightedLabels.toArray();
      for (int i = 0; i < labels.length; i++)
        this.unhighlight((String) labels[i]);
    }

    // das neue label highlighten
    this.highlight(label);
  }

  /**
   * Toggles the highlight from one component to the next.
   * 
   * @param oldLabel
   *          the name of the line which should no longer be highlighted
   * @param newLabel
   *          the name of the new line to highlight
   */
  public void toggleHighlight(String oldLabel, String newLabel)
      throws LineNotExistsException {
    toggleHighlight(oldLabel, false, newLabel);
  }

  /**
   * Toggles the highlight from one component to the next.
   * 
   * @param oldLabel
   *          the name of the line which should no longer be highlighted
   * @param switchToContextMode
   *          determines if highlighting should be turned off for the previous
   *          element, of it it should be put into context mode
   * @param newLabel
   *          the name of the new line to highlight
   */
  public void toggleHighlight(String oldLabel, boolean switchToContextMode,
      String newLabel) throws LineNotExistsException {
    toggleHighlight(oldLabel, switchToContextMode, newLabel, null, null);
  }

  /**
   * Toggles the highlight from one component to the next.
   * 
   * @param oldLabel
   *          the name of the line which should no longer be highlighted
   * @param switchToContextMode
   *          determines if highlighting should be turned off for the previous
   *          element, of it it should be put into context mode
   * @param newLabel
   *          the name of the new line to highlight
   * @param delay
   *          [optional] the delay to apply to this operation.
   * @param duration
   *          [optional] the duration of the action.
   */
  public void toggleHighlight(String oldLabel, boolean switchToContextMode,
      String newLabel, Timing delay, Timing duration)
      throws LineNotExistsException {
    // first, unhighlight the previous element
    unhighlight(oldLabel, switchToContextMode, delay, duration);

    // now, turn on highlight for the new element
    highlight(newLabel, switchToContextMode, delay, duration);
  }

  /**
   * Toggles the highlight from one component to the next.
   * 
   * @param oldLine
   *          the line which should no longer be highlighted
   * @param newLine
   *          the new line to highlight
   */
  public void toggleHighlight(int oldLine, int newLine)
      throws LineNotExistsException {
    toggleHighlight(oldLine, 0, false, newLine, 0, null, null);
  }

  /**
   * Toggles the highlight from one component to the next.
   * 
   * @param oldLine
   *          the line which should no longer be highlighted
   * @param oldColumn
   *          the code element to unhighlight
   * @param switchToContextMode
   *          determines if highlighting should be turned off for the previous
   *          element, of it it should be put into context mode
   * @param newLine
   *          the new line to highlight
   * @param newColumn
   *          the code element to highlight
   */
  public void toggleHighlight(int oldLine, int oldColumn,
      boolean switchToContextMode, int newLine, int newColumn)
      throws LineNotExistsException {
    toggleHighlight(oldLine, oldColumn, switchToContextMode, newLine,
        newColumn, null, null);
  }

  /**
   * Toggles the highlight from one component to the next.
   * 
   * @param oldLine
   *          the line which should no longer be highlighted
   * @param oldColumn
   *          the code element to unhighlight
   * @param switchToContextMode
   *          determines if highlighting should be turned off for the previous
   *          element, of it it should be put into context mode
   * @param newLine
   *          the new line to highlight
   * @param newColumn
   *          the code element to highlight
   * @param delay
   *          [optional] the delay to apply to this operation.
   * @param duration
   *          [optional] the duration of the action.
   */
  public void toggleHighlight(int oldLine, int oldColumn,
      boolean switchToContextMode, int newLine, int newColumn, Timing delay,
      Timing duration) throws LineNotExistsException {
    // first, unhighlight the previous element
    unhighlight(oldLine, oldColumn, switchToContextMode, delay, duration);

    // now, turn on highlight for the new element
    highlight(newLine, newColumn, switchToContextMode, delay, duration);
  }
  
  /**
   * returns the number of lines in this SourceCode instance
   * 
   * @return the number of lines (including empty lines)
   */
  public int length() {
    if (lines == null)
      return 0;
    return lines.size();
  }
}