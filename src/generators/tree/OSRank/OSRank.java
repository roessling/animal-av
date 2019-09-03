package generators.tree.OSRank;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.tree.rbtree_helper.*;
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

public class OSRank {

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
		 * @param newValue The new value of the variable
		 */
		public void update(String newValue) {
			variables.declare("string", name, newValue);
			variables.setRole(name, "stepper");
		}
	}

	private static Language lang;
	public static Variables variables;

	// Properties
	private RectProperties rectProperties;
	private CircleProperties parentNodeProps;
	private CircleProperties currentNodeProps;
	private CircleProperties siblingNodeProps;
	private SourceCodeProperties columnProps;
	private SourceCodeProperties nodeTextProps;
	private SourceCodeProperties sourceCodeProps;
	private SourceCodeProperties textProps;

	// Primitives
	private SourceCode iterationColumn;
	private SourceCode keyYColumn;
	private SourceCode rColumn;
	private SourceCode sc;
	private SourceCode scDescription;
	private Translator translator;
	private Rect sourceCodeRect;
	private Rect iterationTableRect;
	private Rect sourceCodeDescriptionRect;
	private Color color_currentNode;

	public OSRank(Language l, Translator trans, Hashtable<String, Object> primitives, AnimationPropertiesContainer props) {
		lang = l;
		lang.setStepMode(true);
		
		translator = trans;
		initVisualizationProperties(primitives, props);
		
		// Initialize description box for sourcecode
		scDescription = lang.newSourceCode(new Coordinates(0, 0), "null", null);
		scDescription.addCodeLine("", "null", 0, null);
	}
	
	/**
	 * Initialize the globally used visualization properties
	 * @param primitives the user-provided primitives
	 * @param props the user-provided properties 
	 */
	private void initVisualizationProperties(Hashtable<String, Object> primitives, AnimationPropertiesContainer props) {
		// Define visualization properties for source code (pseudocode)
		Color color_sourceCodeHighlight = (Color)primitives.get(translator.translateMessage("color_sourceCodeHighlight"));
        Color color_sourceCode = (Color)primitives.get(translator.translateMessage("color_sourceCode"));
		sourceCodeProps = new SourceCodeProperties();
		sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
				Color.BLUE);
		sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 18));
		sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				color_sourceCodeHighlight);
		sourceCodeProps
				.set(AnimationPropertiesKeys.COLOR_PROPERTY, color_sourceCode);
		// Define visualization properties for signs and symbols table
        siblingNodeProps = new CircleProperties();
		siblingNodeProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
		siblingNodeProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		
		parentNodeProps = new CircleProperties();
		parentNodeProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
		parentNodeProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		
        color_currentNode = (Color)primitives.get(translator.translateMessage("color_currentNode"));
		currentNodeProps = new CircleProperties();
		currentNodeProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				color_currentNode);
		currentNodeProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		
		nodeTextProps = new SourceCodeProperties();
		nodeTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 14));
		nodeTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        
		// Define visualization properties for text boxes
        textProps = (SourceCodeProperties) props.getPropertiesByName(translator.translateMessage("textProperties_startEndScreen"));
        if (textProps == null) {
    		textProps = new SourceCodeProperties();
    		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    				"SansSerif", Font.PLAIN, 20));
    		textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        }
        
		// Define visualization properties for rectangles
        Color color_rect = (Color)primitives.get(translator.translateMessage("color_rect"));
        rectProperties = new RectProperties();
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
				color_rect);
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
		// Create SourceCode coordinates, name, display options,
		// default properties

		// now, create the source code entity
		sc = lang.newSourceCode(new Coordinates(10, 10), "sourceCode", null,
				sourceCodeProps);

		// Add the lines to the SourceCode object.
		// (Line content, name, indentation, display delay)
		sc.addCodeLine("OS-RANK(T, x)", null, 0, null); // 0
		sc.addCodeLine("r = x.left.size + 1", null, 1, null); // 1
		sc.addCodeLine("y = x", null, 1, null); // 2
		sc.addCodeLine("while y != T.root", null, 1, null); // 3
		sc.addCodeLine("if (y == y.p.right)", null, 2, null); // 4
		sc.addCodeLine("r = r + y.p.left.size + 1", null, 3, null); // 5
		sc.addCodeLine("y = y.p", null, 2, null); // 6
		sc.addCodeLine("return r;", null, 1, null); // 7

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
	 * Starts the visualization of the osRank algorithm.
	 * 
	 * @param t
	 *            The RedBlack-Tree on which the algorithm is executed on
	 * @param x
	 *            The node whose rank should be determined
	 * @return The rank of the node x
	 */
	public int osRank(Tree t, Node x) {
		// Properties for signs and symbols table
		int CIRCLES_Y_OFFSET = -50;
		
		Offset leftNodeOffset = new Offset(-120, CIRCLES_Y_OFFSET,
				sourceCodeDescriptionRect, AnimalScript.DIRECTION_S);
		Offset rightNodeOffset = new Offset(120, CIRCLES_Y_OFFSET,
				sourceCodeDescriptionRect, AnimalScript.DIRECTION_S);
		Offset parentNodeOffset = new Offset(0, CIRCLES_Y_OFFSET, 
				sourceCodeDescriptionRect, AnimalScript.DIRECTION_S);

		// Define variables shown in variable window
		Variable a_x = new Variable("x", variables);
		Variable a_r = new Variable("r", variables);
		Variable a_y = new Variable("y", variables);
		int i = 1;

		a_x.update(x.toString());

		// [1.] code line
		sc.highlight(1, 0, false);
		showDescription(1, null);
		lang.nextStep("Start OS-Rank");
		int r = x.getLeftChild().getSize() + 1;
		a_r.update(String.valueOf(r));
		// t.highlightNode(x.getLeftChild().getId());

		// [2.] code line
		highlightLine(1, 2, null);
		lang.nextStep();
		Node y = x;
		a_y.update(y.toString());
		t.highlightNode(y.getId());
		
		// Initialize displayCircles in sourceCodeDescription table
		DisplayCircle currentNode, siblingNode, parentNode;
		if (y.getParent().getLeftChild() == y) {
			currentNode = new DisplayCircle(lang, currentNodeProps, leftNodeOffset, y.toString(), "y.p.left");
			siblingNode = new DisplayCircle(lang, siblingNodeProps, rightNodeOffset, y.getParent().getRightChild().toString(), "y.p.right");
			siblingNode.rbColorFixup(y.getParent().getRightChild());
		} else {
			currentNode = new DisplayCircle(lang, currentNodeProps, rightNodeOffset, y.toString(), "y.p.right");
			siblingNode = new DisplayCircle(lang, siblingNodeProps, leftNodeOffset, y.getParent().getLeftChild().toString(), "y.p.left");
			siblingNode.rbColorFixup(y.getParent().getLeftChild());
		}
		parentNode = new DisplayCircle(lang, parentNodeProps, parentNodeOffset, y.getParent().toString(), "y.p");
		parentNode.rbColorFixup(y.getParent());
		
		// if the while loop will not be executed
		if (y == Tree.nil) {
			// sc.toggleHighlight(2, 3);
			highlightLine(2, 3, null);
			lang.nextStep();
		}
		// [3.] code line
		while (y != t.getRoot()) {

			insertIterationTable(String.valueOf(i), String.valueOf(y.getKey()),
					String.valueOf(r));

			highlightLine(2, 3, null);
			showDescription(3, null);
			
			lang.nextStep(String.valueOf(i) + ". Iteration");

			// [4.] code line
			highlightLine(3, 4, null);
			showDescription(4, null);
			lang.nextStep();
			if (y == y.getParent().getRightChild()) {
				// [5.] code line
				highlightLine(4, 5, null);
				lang.nextStep();
				r = r + y.getParent().getLeftChild().getSize() + 1;
				a_r.update(String.valueOf(r));
				highlightLine(5, 6, null);
			} else {
				highlightLine(4, 6, null);
			}
			// [6.] code line
			lang.nextStep();
			t.unhighlightNode(y.getId());
			Node oldY = y;
			y = y.getParent();
			t.highlightNode(y.getId());
			
			if ((oldY.getParent().getLeftChild() == oldY && y.getParent().getRightChild() == y) ||
				(oldY.getParent().getRightChild() == oldY && y.getParent().getLeftChild() == y)) {
				currentNode.exchange(siblingNode);
			} 
			currentNode.setCircleText(y.toString());
			siblingNode.setCircleText(y.getSibling().toString());
			siblingNode.rbColorFixup(y.getSibling());
			parentNode.setCircleText(y.getParent().toString());
			parentNode.rbColorFixup(y.getParent());
			
			a_y.update(y.toString());

			
			highlightLine(6, 3, null);
			if (i % 4 == 0) {
				// Insert interation table
				iterationColumn.hide();
				keyYColumn.hide();
				rColumn.hide();
				initializeTable();
			}
			
			
			
			if (y == t.getRoot()) {
				highlightLine(6, 3, null);
				currentNode.exchange(parentNode);
				siblingNode.hide();
				parentNode.hide();
				currentNode.setCircleSubtext("y = T.root");
				
				i=i+1;
				insertIterationTable(String.valueOf(i), String.valueOf(y.getKey()),
						String.valueOf(r));
				
				lang.nextStep(String.valueOf(i) + ". Iteration");}
//			} else {
//				highlightLine(6, 3, null);
//				if (i % 4 == 0) {
//					// Insert interation table
//					iterationColumn.hide();
//					keyYColumn.hide();
//					rColumn.hide();
//					initializeTable();
//				}
//			}

			i++;
		}
		// [7.] code line
		HashMap<String, String> m = new HashMap<String, String>();
		m.put("r", "r = " + String.valueOf(r));
		highlightLine(3, 7, m);
		lang.nextStep("OS-Rank Ende return: " + String.valueOf(r));

		return r;
	}

	/**
	 * Initializes the iteration table
	 */
	private void initializeTable() {
		iterationColumn = lang.newSourceCode(new Offset(10, -10,
				iterationTableRect, AnimalScript.DIRECTION_NW),
				"iterationColumn", null, columnProps);

		keyYColumn = lang.newSourceCode(new Offset(-10, -10,
				iterationTableRect, AnimalScript.DIRECTION_N), "keyYColumn",
				null, columnProps);

		rColumn = lang.newSourceCode(new Offset(-70, -10, iterationTableRect,
				AnimalScript.DIRECTION_NE), "rColumn", null, columnProps);

		insertIterationTable("Iteration", "key[y]", "r");
	}

	/**
	 * Creates the startscreen with an introductional text about the algorithm 
	 */
	public void showStartscreen() {
		
		final String DESCRIPTION_KEY_IDENTIFIER = "startscreen.description.line"; 
		
		// DESCRIPTION
		SourceCode startScreen = lang.newSourceCode(new Coordinates(50, 100),
				"intro", null, textProps);

		int noOfLines = Integer.valueOf(translator.translateMessage(DESCRIPTION_KEY_IDENTIFIER + "1"));
		String keyName;
		String outputString = "";
		for (int i = 2; i <= noOfLines; i++) {
			keyName = DESCRIPTION_KEY_IDENTIFIER + i;
			outputString = outputString + " \n " + translator.translateMessage(keyName);
		}
		
		startScreen.addMultilineCode(outputString, "startScreenLine", null);

		Rect startScreenRect = lang.newRect(new Offset(-5, -5, startScreen,
				AnimalScript.DIRECTION_NW), new Offset(5, 5, startScreen,
				AnimalScript.DIRECTION_SE), "frame1", null, rectProperties);

		showHeadline(new Offset(-70, -40, startScreenRect,
				AnimalScript.DIRECTION_N));
		
	
		// SOURCECODE
		// create the source code entity
		sc = lang.newSourceCode(new Offset(45, 0, startScreenRect, AnimalScript.DIRECTION_E), "null", null,
				sourceCodeProps);
		
		// add the lines to the SourceCode object
		// (Line content, name, indentation, display delay)
		sc.addCodeLine("OS-RANK(T, x)", null, 0, null); // 0
		sc.addCodeLine("r = x.left.size + 1", null, 2, null); // 1
		sc.addCodeLine("y = x", null, 2, null); // 2
		sc.addCodeLine("while y != T.root", null, 2, null); // 3
		sc.addCodeLine("if (y == y.p.right)", null, 3, null); // 4
		sc.addCodeLine("r = r + y.p.left.size + 1", null, 4, null); // 5
		sc.addCodeLine("y = y.p", null, 3, null); // 6
		sc.addCodeLine("return r;", null, 2, null); // 7

		sourceCodeRect = lang.newRect(new Offset(20, 0, startScreenRect,
				AnimalScript.DIRECTION_NE), new Offset(415, 0, startScreenRect,
				AnimalScript.DIRECTION_SE), "null", null,
				rectProperties);
		
		// add line numbers to source code
		SourceCode scLineNo = lang.newSourceCode(new Offset(0, 5, sc, AnimalScript.DIRECTION_NW), "null", null,
				sourceCodeProps);
		scLineNo.addMultilineCode("1\n2\n3\n4\n5\n6\n7", "null", null);

		// Prepare for animation: create next step and hide all primitives
		lang.nextStep("Info OS-Rank");
		lang.hideAllPrimitives();
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

		int noOfLines = Integer.valueOf(translator.translateMessage(DESCRIPTION_KEY_IDENTIFIER + "1"));
		String keyName;
		String outputString = "";
		for (int i = 2; i <= noOfLines; i++) {
			keyName = DESCRIPTION_KEY_IDENTIFIER + i;
			outputString = outputString + " \n " + translator.translateMessage(keyName);
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
	 * @param offset The position where the headline should be shown
	 */
	private void showHeadline(Offset offset) {
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,
				Font.BOLD, 30));
		lang.newText(offset, "OS-RANK", "headline", null, tp);
	}

	/**
	 * Shows a textual description of the current processed sourcecode line indicated
	 * by codeLine
	 * @param codeLine The line whose description should be shown
	 */
	private void showDescription(int codeLine, Map<String, String> placeholder) {
		scDescription.hide();
		String key = "sourcecode.Description.line" + codeLine;
		String translatedText = translator.translateMessage(key);
		int linebreakThreshold = 35;
		int textLength = translatedText.length();

		scDescription = lang.newSourceCode(new Offset(10, -60,
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
	 * Changes the text highlighting from oldLine to newLine and shows the textual description 
	 * of the newLine
	 * @param oldLine The old line where the highlighting is currently enabled 
	 * @param newLine The new line where the highlighting should be moved to
	 */
	private void highlightLine(int oldLine, int newLine, Map<String, String> m) {
		sc.toggleHighlight(oldLine, newLine);
		showDescription(newLine, m);
	}

	/**
	 * Adds a new entry (i, key, r) to the iteration table. 
	 * See startScreen for details about animation variables (i, key, r)  
	 * @param i The current iteration
	 * @param key The key value to add
	 * @param r The rank of the node y
	 */
	private void insertIterationTable(String i, String key, String r) {
		iterationColumn.addCodeLine(i, null, 0, null);
		keyYColumn.addCodeLine(key, null, 0, null);
		rColumn.addCodeLine(r, null, 0, null);
	}
	
	public Color getCurrentNodeColor() {
		return color_currentNode;
	}

}
