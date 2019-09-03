package generators.tree.OSSelect;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.tree.rbtree_helper.DisplayCircle;
import generators.tree.rbtree_helper.Node;
import generators.tree.rbtree_helper.Tree;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * @author Patrick
 *
 */
public class OSSelect {

	/**
	 * Class to handle variables in the variable window
	 *
	 */
	private static class Variable {
		String name;
		Variables variables;

		public Variable(String name, Variables variables) {
			this.name = name;
			this.variables = variables;
		}

		/**
		 * Updates the value of the variable to newValue
		 * 
		 * @param newValue
		 *            The new value of the variable
		 */
		public void update(String newValue) {
			variables.declare("string", name, newValue);
			variables.setRole(name, "stepper");
		}
		
		public void update(int val) {
			String newValue = String.valueOf(val);
			variables.declare("string", name, newValue);
			variables.setRole(name, "stepper");
		}
	}

	private static Language lang;
	public static Variables variables;

	// Properties
	private RectProperties rectProperties;
	private CircleProperties currentNodeProps;
	private CircleProperties childrenNodeProps;
	private SourceCodeProperties columnProps;
	private SourceCodeProperties nodeTextProps;
	private SourceCodeProperties sourceCodeProps;
	private SourceCodeProperties textProps;

	// Primitives
	private SourceCode recursionColumn;
	private SourceCode keyXColumn;
	private SourceCode rColumn;
	private SourceCode iColumn;
	private SourceCode sc;
	private SourceCode scDescription;
	private Translator translator;
	private Rect sourceCodeRect;
	private Rect iterationTableRect;
	private Rect sourceCodeDescriptionRect;
	private Color color_currentNode;
	
	// Variables for the variable window
	private Variable a_x;
	private Variable a_r;
	private Variable a_i;
	private Variable a_rec;
	
	// Variables for signs and symbols table
	private DisplayCircle currentNode;
	private DisplayCircle leftChildNode;
	private DisplayCircle rightChildNode;
	
	// Variables for animation execution
	// Remember which lines are highlighted
	// 1: Line is highlighted; 0: Line is not highlighted
	private int[] lines;
	private int recCounter;
	private int iInitialValue;
	private int lastHighlightedNodeID = -1;


	public OSSelect(Language l, Translator trans,
			Hashtable<String, Object> primitives,
			AnimationPropertiesContainer props) {
		lang = l;
		lang.setStepMode(true);

		translator = trans;
		initVisualizationProperties(primitives, props);
		
		lines = new int[]{0, 0, 0, 0, 0, 0, 0}; 

		// Initialize description box for sourcecode
		scDescription = lang.newSourceCode(new Coordinates(0, 0), "null", null);
		scDescription.addCodeLine("", "null", 0, null);
	}
	
	/**
	 * Initialize the globally used visualization properties
	 * @param primitives the user-provided primitives
	 * @param props the user-provided properties 
	 */
	private void initVisualizationProperties(
			Hashtable<String, Object> primitives,
			AnimationPropertiesContainer props) {
		
		Color color_childrenNodes;
		Color color_sourceCode;
		Color color_sourceCodeHighlighting;
		Color color_backgroundRectangles;
				
		if (primitives != null && props != null) {
			// Display properties
			color_currentNode = (Color) primitives.get(translator.translateMessage("color_currentNode"));
			color_sourceCode = (Color) primitives.get(translator.translateMessage("color_sourceCode"));
			color_backgroundRectangles = (Color) primitives
					.get(translator.translateMessage("color_backgroundRectangles"));
			color_sourceCodeHighlighting = (Color) primitives.get(translator.translateMessage("color_sourceCodeHighlighting"));
			textProps = (SourceCodeProperties) props
					.getPropertiesByName(translator.translateMessage("textProperties_startEndScreen"));
		} else {
			color_sourceCodeHighlighting = Color.RED;
			color_sourceCode = Color.BLACK;
			color_currentNode = Color.YELLOW;
			color_backgroundRectangles = Color.LIGHT_GRAY;
			textProps = new SourceCodeProperties();
			textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					"SansSerif", Font.PLAIN, 20));
			textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		}
		color_childrenNodes = Color.GRAY;
		
		// Define visualization properties for source code (pseudocode)
		sourceCodeProps = new SourceCodeProperties();
		sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
				Color.BLUE);
		sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 20));
		sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				color_sourceCodeHighlighting);
		sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				color_sourceCode);

		// Define visualization properties for signs and symbols table
		childrenNodeProps = new CircleProperties();
		childrenNodeProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				color_childrenNodes);
		childrenNodeProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		currentNodeProps = new CircleProperties();
		currentNodeProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				color_currentNode);
		currentNodeProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		nodeTextProps = new SourceCodeProperties();
		nodeTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 14));
		nodeTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		// Define visualization properties for text boxes

		// Define visualization properties for rectangles
		rectProperties = new RectProperties();
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, color_backgroundRectangles);
		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		// Define visualization properties for iteration table
		columnProps = new SourceCodeProperties();
		columnProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
				Color.BLUE);
		columnProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 22));
		columnProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.RED);
		columnProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	}

	/**
	 * Initializes the iteration table, the sourceCode description text field,
	 * adds the source code to the sourceCode primitive, creates the headline
	 */
	public void initializeAnimation() {
		
		sc = lang.newSourceCode(new Coordinates(10, 10), "sourceCode", null, sourceCodeProps);
		setSourceCode(sc);

		sourceCodeRect = lang.newRect(new Offset(-5, -5, sc,
				AnimalScript.DIRECTION_NW), new Offset(5, 5, sc,
				AnimalScript.DIRECTION_SE), "sourceCodeRect", null,
				rectProperties);

		// Rect for sourceCodeDescription
		sourceCodeDescriptionRect = lang.newRect(new Offset(20, 0,
				sourceCodeRect, AnimalScript.DIRECTION_NE), new Offset(440, 0,
				sourceCodeRect, AnimalScript.DIRECTION_SE),
				"sourceCodeDescription", null, rectProperties);

		// Rect for iteration table
		iterationTableRect = lang.newRect(new Offset(20, 0,
				sourceCodeDescriptionRect, AnimalScript.DIRECTION_NE),
				new Offset(380, 0, sourceCodeDescriptionRect,
						AnimalScript.DIRECTION_SE), "iterationTable", null,
				rectProperties);

		// HeadLine
		Offset offset = new Offset(-70, 20, sourceCodeDescriptionRect,
				AnimalScript.DIRECTION_N);

		showHeadline(offset);

		initializeTable();

		lang.nextStep();
	}

	/**
	 * Starts the visualization of the osSelect algorithm.
	 * 
	 * @param t
	 *            The RedBlack-Tree on which the algorithm is executed on
	 * @param x
	 *            The node whose rank should be determined
	 * @return The rank of the node x
	 */
	public void osSelectStart(Tree t, Node x, int i) {
		
		// Define signs and symbols table
		int CIRCLES_Y_OFFSET = -50;
		Offset leftNodeOffset = new Offset(-120, CIRCLES_Y_OFFSET,
				sourceCodeDescriptionRect, AnimalScript.DIRECTION_S);
		Offset rightNodeOffset = new Offset(120, CIRCLES_Y_OFFSET,
				sourceCodeDescriptionRect, AnimalScript.DIRECTION_S);
		Offset currentNodeOffset = new Offset(0, CIRCLES_Y_OFFSET,
				sourceCodeDescriptionRect, AnimalScript.DIRECTION_S);
		leftChildNode = new DisplayCircle(lang, childrenNodeProps,	leftNodeOffset, x.getLeftChild().toString(), "x.left");
		leftChildNode.rbColorFixup(x.getLeftChild());
		currentNode = new DisplayCircle(lang, currentNodeProps,	currentNodeOffset, x.toString(), "x");
		rightChildNode = new DisplayCircle(lang, childrenNodeProps,	rightNodeOffset, x.getRightChild().toString(), "x.right");
		rightChildNode.rbColorFixup(x.getRightChild());
		
		// Define variables shown in variable window
		a_x = new Variable("x", variables);
		a_r = new Variable("r", variables);
		a_i = new Variable("i", variables);
		a_rec = new Variable("calls", variables);
		
		// Reset all temporary variables for next call of osSelect
		recCounter = 0;
		iInitialValue = i;
		lastHighlightedNodeID = -1;
		
		// Call OS-SELECT
		Node n = osSelect(t, x, i);
		
		// show final values in variable window
		a_rec.update(recCounter);
		a_x.update(n.toString());
		
		//lang.nextStep();
	}

	/**
	 * OS-SELECT returns the node containing the i-th smallest key in the subtree rooted at node x.
	 * @param t the tree in which the nodes are in
	 * @param x the root of the subtree
	 * @param i the position of the i-th smallest key in linear order 
	 * @return the Node with the i-th smallest key in linear order
	 */
	private Node osSelect(Tree t, Node x, int i) {
		
		currentNode.setCircleText(x.toString());
		leftChildNode.setCircleText(x.getLeftChild().toString());
		leftChildNode.rbColorFixup(x.getLeftChild());
		rightChildNode.setCircleText(x.getRightChild().toString());
		rightChildNode.rbColorFixup(x.getRightChild());
		
		recCounter++;
		a_rec.update(recCounter);
		
		highlightLine(1, null);
		// unhighlight previously highlighted node
		if (lastHighlightedNodeID != -1) {
			t.unhighlightNode(lastHighlightedNodeID);
		}
		t.highlightNode(x.getId());
		lastHighlightedNodeID = x.getId();
		currentNode.setCircleText(x.toString());
		lang.nextStep(recCounter + ".) recursive call");
		// [1.] code line
		int r = x.getLeftChild().getSize() + 1;
		a_rec.update(recCounter);
		a_i.update(i);
		a_x.update(x.toString());
		a_r.update(r);
		
		if (recCounter % 5 == 0) {
			recursionColumn.hide();
			keyXColumn.hide();
			rColumn.hide();
			iColumn.hide();
			initializeTable();
		}
		insertIterationTable(recCounter, x, i, r);

		highlightLine(2, null);
		lang.nextStep();
		// [2.] code line
		if (i == r){
			// [3.] code line
			HashMap<String, String> m = new HashMap<String, String>();
			m.put("i", String.valueOf(iInitialValue) + ".");
			m.put("x", x.toString());
			highlightLine(3, m);
			lang.nextStep("  -> return: node " + x.toString());
			return x;
		}
		// [4.] code line
		else {
			highlightLine(4, null);
			lang.nextStep();
			if (i < r){	
				highlightLine(5, null);
				lang.nextStep("  -> OS-SELECT(" + x.getLeftChild().toString() + ", " + i + ")");
				t.unhighlightNode(x.getId());
				// [5.] code line
				return osSelect(t, x.getLeftChild(), i);
			}
			// [6.] code line
			else{
				highlightLine(6, null);
				lang.nextStep("  -> OS-SELECT(" + x.getRightChild().toString() + ", " + (i-r) + ")");
				t.unhighlightNode(x.getId());
				return osSelect(t, x.getRightChild(), i - r);
			}
		}
	}

	/**
	 * Initializes the iteration table
	 */
	private void initializeTable() {
		recursionColumn = lang.newSourceCode(new Offset(10, -10,
				iterationTableRect, AnimalScript.DIRECTION_NW),
				"iterationColumn", null, columnProps);

		keyXColumn = lang.newSourceCode(new Offset(-10, -10,
				iterationTableRect, AnimalScript.DIRECTION_N), "keyXColumn",
				null, columnProps);
		
		iColumn = lang.newSourceCode(new Offset(-40, -10, iterationTableRect, AnimalScript.DIRECTION_NE), "iColumn", null, columnProps);

		rColumn = lang.newSourceCode(new Offset(-80, -10, iterationTableRect,
				AnimalScript.DIRECTION_NE), "rColumn", null, columnProps);

		labelIterationTableColumns();
	}
	
	/**
	 * Adds labels to the iteration table columns
	 */
	private void labelIterationTableColumns(){
		recursionColumn.addCodeLine("Recursion", null, 0, null);
		keyXColumn.addCodeLine("x.key", null, 0, null);
		iColumn.addCodeLine("i", null, 0, null);
		rColumn.addCodeLine("r", null, 0, null);
	}

	/**
	 * Creates the startscreen with an introductional text about the algorithm
	 */
	public void showStartscreen() {

		final String DESCRIPTION_KEY_IDENTIFIER = "startscreen.description.line";

		// DESCRIPTION
		SourceCode startScreen = lang.newSourceCode(new Coordinates(50, 100),
				"intro", null, textProps);

		int noOfLines = Integer.valueOf(translator
				.translateMessage(DESCRIPTION_KEY_IDENTIFIER + "1"));
		String keyName;
		String outputString = "";
		for (int i = 2; i <= noOfLines; i++) {
			keyName = DESCRIPTION_KEY_IDENTIFIER + i;
			outputString = outputString + " \n "
					+ translator.translateMessage(keyName);
		}

		startScreen.addMultilineCode(outputString, "startScreenLine", null);

		Rect startScreenRect = lang.newRect(new Offset(-5, -5, startScreen,
				AnimalScript.DIRECTION_NW), new Offset(5, 5, startScreen,
				AnimalScript.DIRECTION_SE), "frame1", null, rectProperties);

		showHeadline(new Offset(-70, -40, startScreenRect,
				AnimalScript.DIRECTION_N));

		// SOURCECODE
		sc = lang.newSourceCode(new Offset(45, 0, startScreenRect, AnimalScript.DIRECTION_E), "null", null, sourceCodeProps);
		setSourceCode(sc);

		sourceCodeRect = lang.newRect(new Offset(20, 0, startScreenRect,
				AnimalScript.DIRECTION_NE), new Offset(495, 0, startScreenRect,
				AnimalScript.DIRECTION_SE), "null", null, rectProperties);

		// add line numbers to source code
		SourceCode scLineNo = lang.newSourceCode(new Offset(0, 5, sc,
				AnimalScript.DIRECTION_NW), "null", null, sourceCodeProps);
		scLineNo.addMultilineCode("1\n2\n3\n4\n5\n6", "null", null);

		// Prepare for animation: create next step and hide all primitives
		lang.nextStep("Info OS-Select");
		lang.hideAllPrimitives();
	}

	/**
	 * Adds the OS-SELECT code to the source code primitive
	 * @param sc the source code primitive where the code should be added to
	 * @return the modified source code object
	 */
	private SourceCode setSourceCode(SourceCode sc) {

		// add the lines to the SourceCode object
		// (Line content, name, indentation, display delay)
		sc.addCodeLine("OS-SELECT(x, i)", null, 0, null); // 0
		sc.addCodeLine("r = x.left.size + 1", null, 1, null); // 1
		sc.addCodeLine("if (i == r)", null, 1, null); // 2
		sc.addCodeLine("return x", null, 2, null); // 3
		sc.addCodeLine("else if (i < r)", null, 1, null); // 4
		sc.addCodeLine("return OS-SELECT(x.left, i)", null, 2, null); // 5
		sc.addCodeLine("else return OS-SELECT(x.right, i-r)", null, 1, null); // 6
		
		return sc;
	}

	/**
	 * Creates the endscreen with the according text
	 */
	public void showEndscreen() {

		lang.hideAllPrimitives();

		final String DESCRIPTION_KEY_IDENTIFIER = "endscreen.description.line";

		// DESCRIPTION
		SourceCode endScreen = lang.newSourceCode(new Coordinates(50, 100),
				"outro", null, textProps);

		int noOfLines = Integer.valueOf(translator
				.translateMessage(DESCRIPTION_KEY_IDENTIFIER + "1"));
		String keyName;
		String outputString = "";
		for (int i = 2; i <= noOfLines; i++) {
			keyName = DESCRIPTION_KEY_IDENTIFIER + i;
			outputString = outputString + " \n "
					+ translator.translateMessage(keyName);
		}

		endScreen.addMultilineCode(outputString, "endScreenLine", null);

		Rect endScreenRect = lang.newRect(new Offset(-5, -5, endScreen,
				AnimalScript.DIRECTION_NW), new Offset(5, 5, endScreen,
				AnimalScript.DIRECTION_SE), "frame1", null, rectProperties);

		showHeadline(new Offset(-70, -40, endScreenRect,
				AnimalScript.DIRECTION_N));

		// Prepare for animation: create next step and hide all primitives
		lang.nextStep("Outro OS-Rank");
		lang.hideAllPrimitives();

	}

	/**
	 * Shows the headline at the position given as Offset
	 * 
	 * @param offset
	 *            The position where the headline should be shown
	 */
	private void showHeadline(Offset offset) {
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
				Font.BOLD, 30));
		lang.newText(offset, "OS-SELECT", "headline", null, tp);
	}

	/**
	 * Shows a textual description of the current processed sourcecode line
	 * indicated by codeLine
	 * 
	 * @param codeLine
	 *            The line whose description should be shown
	 */
	private void showDescription(int codeLine, Map<String, String> placeholder) {
		scDescription.hide();
		String key = "sourcecode.Description.line" + codeLine;
		String translatedText = translator.translateMessage(key);
		int linebreakThreshold = 32;
		int textLength = translatedText.length();

		scDescription = lang.newSourceCode(new Offset(10, -65,
				sourceCodeDescriptionRect, AnimalScript.DIRECTION_W), "null",
				null, sourceCodeProps);
		String currentLine;
		String modifiedOutput;
		String newString = "";
		int startIndex = 0;
		int endIndex = Math.min((startIndex + linebreakThreshold), textLength);
		int spaceIndex = -1;
		do {
			currentLine = translatedText.substring(startIndex, endIndex);
			if (textLength > linebreakThreshold) {
				spaceIndex = currentLine.indexOf(" ");
				while (spaceIndex != -1
						&& currentLine.length() <= linebreakThreshold) {
					newString = newString
							+ currentLine.substring(0, spaceIndex + 1);
					currentLine = currentLine.substring(spaceIndex + 1,
							currentLine.length());
					spaceIndex = currentLine.indexOf(" ");
				}
				modifiedOutput = findAndReplace(placeholder, newString);
				scDescription.addCodeLine(modifiedOutput, null, 0, null);
			}
			startIndex = startIndex + newString.length();
			endIndex = Math.min((startIndex + linebreakThreshold), textLength);
			currentLine = translatedText.substring(startIndex, endIndex);
		} while (currentLine.length() > linebreakThreshold);
		modifiedOutput = findAndReplace(placeholder, currentLine);
		scDescription.addCodeLine(modifiedOutput, null, 0, null);
		
	}

	/**
	 * Finds the keys in the String newString and replaces them with the value of
	 * the corresponding value in the placeholder
	 * @param placeholder the map with the key-value mapping
	 * @param newString the string where the keys should be searched
	 * @return the modified newString
	 */
	private String findAndReplace(Map<String, String> placeholder, String newString) {
		String result = newString;
		// only make replacements if placeholder mapping is given
		if (placeholder != null) {
			StringBuilder sb = new StringBuilder();
			String[] words = newString.split("\\s");
			String key;
			
			// go through each word of the string
			for (int i = 0; i < words.length; i++) {
				// check if any key is found in the word
				for (Iterator<String> it = placeholder.keySet().iterator(); it.hasNext();) {
					key = (String) it.next(); 
					// if the key is found, replace the word by the placeholder value
					if (words[i].matches(key + "[,.]*")) {
						words[i] = words[i].replaceAll(key, (String) placeholder.get(key));
					}
				}
			}
			// rebuild single String from String array
			for (int i = 0; i < words.length; i++) {
				sb.append(words[i]);
				sb.append(" ");
			}
			result = sb.toString();
		}
		// return result, if no placeholder map was given, return original String
		return result;			
		}


	/**
	 * Changes the text highlighting from oldLine to newLine and shows the
	 * textual description of the newLine
	 * 
	 * @param oldLine
	 *            The old line where the highlighting is currently enabled
	 * @param newLine
	 *            The new line where the highlighting should be moved to
	 */
	private void highlightLine(int newLine, Map<String, String> m) {
		// unhighlight all currently highlighted lines
		// (usually there should only be ONE line highlighted)
		for (int i = 0; i < lines.length; i++) {
			if (lines[i] == 1) {
				sc.unhighlight(i);
				lines[i] = 0;
			}
		}
		
		//sc.toggleHighlight(oldLine, newLine);
		sc.highlight(newLine);
		lines[newLine] = 1;
		showDescription(newLine, m);
	}

	/**
	 * Adds a new entry (i, key, r) to the iteration table. See startScreen for
	 * details about animation variables (i, key, r)
	 * 
	 * @param i
	 *            The current iteration
	 * @param x
	 *            The key value to add
	 * @param r
	 *            The rank of the node y
	 * @param i 
	 */
	private void insertIterationTable(int recCounter, Node x, int i, int r) {
		recursionColumn.addCodeLine(String.valueOf(recCounter), null, 0, null);
		keyXColumn.addCodeLine(x.toString(), null, 0, null);
		iColumn.addCodeLine(String.valueOf(i), null, 0, null);
		rColumn.addCodeLine(String.valueOf(r), null, 0, null);
	}
	
	public Color getCurrentNodeColor() {
		return color_currentNode;
	}
	
}
