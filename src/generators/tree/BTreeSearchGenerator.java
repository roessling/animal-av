/*
 * BTreeSearch.java
 * Fritz Beutel, Maxim Kuznetsov, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.tree;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.OffsetFromLastPosition;
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.OffsetCoords;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import translator.Translator;

public class BTreeSearchGenerator implements ValidatingGenerator {
	private Translator translator;
    private Language lang;
    
    // === Properties ===
    private TextProperties textProps; //Not Changeable
    private SourceCodeProperties sourceCodeProps; //User Set
    private TextProperties largeTreeNumberProps; //Not Changeable
    private PolylineProperties arrowProps; //Not Changeable
    private TextProperties falseComment; //User Set
    private TextProperties trueComment; //User Set
    private Color focusedColor; //User Set
    private Color unfocusedColor; //User Set
    private Color defaultColor; //User Set
    private Color trueColor; //User Set
    private Color falseColor; //User Set
    
    // === Primitives
    private Text header;
    private Rect hRect;
    private SourceCode src;
    private List<Primitive> visualTree = new ArrayList<>();
    private Text headText;
    private Text whileTrueText;
    private Text whileFalseText;
    private Text trueTrueText;
    private Text trueFalseText;
    private Text falseTrueText;
    private Text falseFalseText;
    
    // === Positions ===
    private Node headPos = new Coordinates(325, 76);
    private Node whilePos = new Coordinates(425, 116);    
    private Node truePos = new Coordinates(425, 156);
    private Node falsePos = new Coordinates(425, 196);
    private Node recursionPos = new Coordinates(375, 256);
    private Node resultPos = new Coordinates(25, 300);
    private int nodeOffset = 10;
    
    // === Variables ===
    private List<List<TreeNode>> depthTree = new ArrayList<>();
    private int nodeKeyCount;
    private static int nodeIdCounter = 0; //Used to create unique ids for the nodes
    private int recursionCount = 1;
    
    // === Data Structure of a Tree Node<
    public class TreeNode {
    	public final int id;
    	
    	public int numOfKeys;
    	public int keys[];
    	public TreeNode childs[];
    	public Node arrowPos[];
    	public Primitive pointer; //Position Marker
    	//public List<Integer> highlights = new ArrayList<>();
    	public Primitive[] arrows;
    	public List<Primitive> items = new ArrayList<>();
    	public TreeNode parent;
    	public Node pos;
    	
    	/**
    	 * Creates a new Tree-Node
    	 * @param size The number of keys this node can hold
    	 * @param keys The keys of this node
    	 * @param parent This nodes parent. Null if its the root node
    	 */
    	public TreeNode(int size, int[] keys, TreeNode parent) {
    		//Create unique id
    		id = nodeIdCounter++;
    		childs = new TreeNode[size + 1];
    		//Gets number of used keys
    		numOfKeys = 0;
    		for (int i : keys)
    			if (i >= 0)
    				numOfKeys++;
    		this.keys = keys;
    		arrowPos = new Node[size + 1];
    		arrows = new Primitive[size+1];
    		this.parent = parent;
    	}
    	
    	/**
    	 * Changes the Color of this node (The Rectangles and the Marker)
    	 * @param color The new color for this node
    	 */
    	public void changeColor(Color color) {
    		for (Primitive i : items) {
    			i.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, color, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    		}
    		pointer.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, color, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    	}
    	
    	/**
    	 * Changes the color of the arrow pointing to the i-th child
    	 * @param color The color the arrow should be changed to
    	 * @param child The child-Id of the arrow that should be changed
    	 */
    	public void changeChildColor(Color color, int child) {
    		arrows[child].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, color, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    	}
    	
    	public int numOfChilds() {
    		int result = 0;
    		for (TreeNode n : childs) {
    			if (n != null)
    				result++;
    		}
    		return result;
    	}
    }

    public BTreeSearchGenerator(Locale locale) {
        translator = new Translator("resources/BTreeSearch", locale);
    }
    
    public BTreeSearchGenerator() {
    	this(Locale.GERMANY);
    }
    
    /**
     * Draws the Tree to the canvas
     */
    private void generateTree() {
    	int drawnSize = 10+30*nodeKeyCount;
    	
    	//For each depth of the tree
    	//Bottom to top so the childs are drawn before their parents => Needed for the connection Arrows
    	for (int i = depthTree.size() - 1; i >= 0; i--) {	
    		//Calcs starting Position based on the Number of Nodes in this line
    		int count = depthTree.get(i).size();
	    	int startPos = 900-(int)((count/2.0)*(drawnSize + nodeOffset));
	    	Node newPos = new Coordinates(startPos, 150+100*i);
	    	//Draws all Nodes in this line
	    	for (TreeNode n : depthTree.get(i)) {
	    		newPos = drawNode(n, newPos);
	    		newPos = new OffsetCoords(newPos, 10, 0);
	    		
	    		//Draws Arrows for childs
	    		for (int j = 0; j < n.childs.length; j++) {
	    			if (n.childs[j] == null)
	    				continue;
	    			Node[] arrow = new Node[] {
	        				new OffsetCoords(n.pos, 5+30*j, 30),
	        				new OffsetFromLastPosition(0, 8),
	        				new OffsetCoords(n.childs[j].pos, (30*nodeKeyCount)/2+5, -3),
	        		};
	        		Primitive temp = lang.newPolyline(arrow, "node-" + n.id + "-childArrow" + j, null, arrowProps);
	        		visualTree.add(temp);
	        		n.arrows[j] = temp;
	    		}
	    	}
    	}
    }
    
    /**
     * Draws a single TreeNode to the canvas
     * @param node The Node that should be drawn
     * @param pos The Upper Left Position of the Node
     * @return The Upper Right Corner of the drawn Node
     */
    private Node drawNode(TreeNode node, Node pos) {
    	String objName = "node-" + node.id + "-";
    	int[] keys = node.keys;
    	node.pos = pos;
    	
    	visualTree.add(lang.newRect(pos, new OffsetFromLastPosition(10, 30), objName + "pointer0", null));
    	visualTree.add(lang.newText(new OffsetCoords(pos, /*6*/1, 5), "•", objName + "pointerDot0", null, textProps));
    	
    	for (int i = 0; i < keys.length; i++) {
    		//Box for the Key
    		visualTree.add(lang.newRect(new OffsetCoords(pos, 10+30*i, 0), new OffsetFromLastPosition(20, 30), objName + "valueField" + i, null));
    		//Save Location of Box for Marker pointing to it
    		node.arrowPos[i] = new OffsetCoords(pos, 20+30*i, -35);
    		//Box for the pointer/child
    		visualTree.add(lang.newRect(new OffsetFromLastPosition(0, -30), new OffsetFromLastPosition(10, 30), objName + "pointer" + (i+1), null));
    		//Adjust position and size of the keys based on their digit count
    		if (keys[i] < 0)
    			visualTree.add(lang.newText(new OffsetCoords(pos, 19+30*i, 3), "-", objName + "value" + i, null, textProps));
    		else if (keys[i] < 10)
    			visualTree.add(lang.newText(new OffsetCoords(pos, 16+30*i, 5), Integer.toString(keys[i]), objName + "value" + i, null, textProps));
    		else if (keys[i] < 100)
    			visualTree.add(lang.newText(new OffsetCoords(pos, 11+30*i, 5), Integer.toString(keys[i]), objName + "value" + i, null, textProps));
    		else
    			visualTree.add(lang.newText(new OffsetCoords(pos, 10+30*i, 7), Integer.toString(keys[i]), objName + "value" + i, null, largeTreeNumberProps));
    		visualTree.add(lang.newText(new OffsetCoords(pos, 1+30*(i+1), 5), "•", objName + "pointerDot" + (i+1), null, textProps));
    	}
    	//Add Marker Position for outOfBounds
    	node.arrowPos[keys.length] = new OffsetCoords(pos, 10+30*keys.length, -35);
    	//Get all added items for this node and save them in the node to modify them (change color,...)
    	for (int i = visualTree.size() - 1; i > visualTree.size() - 1 - (keys.length * 4) - 2; i--) { //3 Items per key + 1 for the initial pointer
    		node.items.add(visualTree.get(i));
    	}
    	//Create Marker for the current Node, sets it to the first key and hides it
    	node.pointer = lang.newPolyline(new Node[] {new OffsetCoords(pos, 20, -35), new OffsetCoords(pos, 20, 0)}, "a", null, arrowProps);
    	node.pointer.hide();
    	//returns upper right corner of this node
    	return new OffsetCoords(pos, 10+30*keys.length, 0);
    }
    
    /**
     * Starts the algo animation
     * @param tree The Tree the search should be performed on
     * @param searchKey The Key the tree should be searched for
     */
    public void start(TreeNode tree, int searchKey) {
    	TextProperties headerProps = new TextProperties();
    	headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
    	header = lang.newText(new Coordinates(20, 30), getAlgorithmName(), "header", null, headerProps);
    	RectProperties rectProps = new RectProperties();
    	rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    	rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
    	rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    	hRect = lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), 
    			new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);
    	
    	lang.nextStep("Einleitung");
    	int yPos = 100;
    	for (String s : translator.translateMessage("description").split("\n")) {
    		lang.newText(new Coordinates(10, yPos), s, "description", null, textProps);
    		yPos += 20;
    	}
    	
    	lang.nextStep();
    	lang.hideAllPrimitives();
    	header.show();
    	hRect.show();
    	//Shows the Tree
    	for (Primitive p : visualTree)
    		p.show();
    	
    	src = lang.newSourceCode(new Coordinates(15, 60), "sourceCode", null, sourceCodeProps);
    	src.addMultilineCode(getCodeExample(), null, null);
    	
    	Variables varSearchKey = lang.newVariables();
    	varSearchKey.declare("int", "searchKey", Integer.toString(searchKey), animal.variables.VariableRoles.FIXED_VALUE.toString()); // Wont Recognize Role
    	
    	TrueFalseQuestionModel question = new TrueFalseQuestionModel("currectSortedTree");
    	question.setPrompt(translator.translateMessage("sortedTreeQuestionPrompt"));
    	question.setCorrectAnswer(true);
    	question.setPointsPossible(1);
    	question.setNumberOfTries(2);
    	question.setFeedbackForAnswer(true, translator.translateMessage("sortedTreeQuestionAnswerRight")); // Feedback Not Working
    	question.setFeedbackForAnswer(false, translator.translateMessage("sortedTreeQuestionAnswerWrong"));
    	lang.addTFQuestion(question);
    	
    	lang.nextStep();
    	headText = lang.newText(headPos, "", "headerComment", null, trueComment);
    	whileTrueText = lang.newText(whilePos, "", "whileTrueComment", null, trueComment);
    	whileFalseText = lang.newText(whilePos, "", "whileFalseComment", null, falseComment);
    	trueTrueText = lang.newText(truePos, "", "ifElFoundTrueComment", null, trueComment);
    	trueFalseText = lang.newText(truePos, "", "ifElFoundFalseComment", null, falseComment);
    	falseTrueText = lang.newText(falsePos, "", "ifElNotFoundTrueComment", null, trueComment);
    	falseFalseText = lang.newText(falsePos, "", "ifElNotFoundFalseComment", null, falseComment);
    	//Start Algorithm
    	recursionCount = 0;
    	boolean result = searchNode(tree, searchKey);
    	
    	TextProperties resultProps = new TextProperties();
    	resultProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(((Font)trueComment.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily(), Font.BOLD, 20));
    	resultProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, focusedColor);
    	@SuppressWarnings("unused")
      Text resultText = lang.newText(resultPos, translator.translateMessage(result ? "resultTrue" : "resultFalse"), "resultText", null, resultProps);
    	
    	lang.nextStep();
    	
    	MultipleChoiceQuestionModel complexQuestion = new MultipleChoiceQuestionModel("complexityQuestion");
    	complexQuestion.setPrompt(translator.translateMessage("komplexQuestionPromt"));
    	complexQuestion.addAnswer("O(log(m) * log(n))", 1, translator.translateMessage("komplexQuestionPromtAnswerOlogmlogn"));
    	complexQuestion.addAnswer("O(m * log(n))", 3, translator.translateMessage("komplexQuestionPromtAnswerOmlogn"));
    	complexQuestion.addAnswer("O(log(n))", 0, translator.translateMessage("komplexQuestionPromtAnswerOlogn"));
    	complexQuestion.addAnswer("O(n)", 0, translator.translateMessage("komplexQuestionPromtAnswerOn"));
    	lang.addMCQuestion(complexQuestion);
    	
    	lang.nextStep("Fazit");
    	lang.hideAllPrimitives();
    	header.show();
    	hRect.show();
    	yPos = 100;
    	for (String s : translator.translateMessage("endScreen").split("\n")) {
    		lang.newText(new Coordinates(10, yPos), s, "description", null, textProps);
    		yPos += 20;
    	}
    }
    
    /**
     * Animate a single iteration/recursion of the algorithm
     * @param node The current Node that should be searched
     * @param searchKey The key that should be searched for
     * @return If the Key was found
     */
    private boolean searchNode(TreeNode node, int searchKey) {
    	node.changeColor(focusedColor);
    	
    	//Reset all highlighted Code
    	for (int i = 0; i < 10; i++)
    		src.unhighlight(i);
    	
    	src.highlight(0);	//Highlight Method Head
    	headText.setText("searchKey = " + searchKey + "; numOfKeys = " + node.numOfKeys, null, null);
    	
    	lang.nextStep("Iteration " + recursionCount++); // Change Text to RecursionCount?!
    	src.highlight(1);
    	int i = 0;
    	Variables var = lang.newVariables();
    	var.declare("int", "i", "0", animal.variables.VariableRoles.STEPPER.name());
    	var.set("i", "0");
    	
    	Variables varNumOfKeys = lang.newVariables();
    	varNumOfKeys.declare("int", "numOfKeys", Integer.toString(node.numOfKeys), animal.variables.VariableRoles.MOST_RECENT_HOLDER.name()); // Wont Recognize Role
    	
    	node.pointer.show();
    	
    	lang.nextStep();
    	src.unhighlight(1);
    	src.highlight(2);
    	while (i < node.numOfKeys && searchKey > node.keys[i]) {
    		//Display green evaluation of the while Condition
    		whileTrueText.setText(
    				"i = " + i + "; keys[" + i + "] = " + node.keys[i], 
    				null,
    				null);
    		whileTrueText.show();
    		
    		lang.nextStep();
    		src.highlight(3);
    		src.unhighlight(2);   		
    		i++;
    		var.set("i", i + "");
    		if (node.arrowPos[i] != null)
    			node.pointer.moveTo(AnimalScript.DIRECTION_SE, "translate", node.arrowPos[i], Timing.INSTANTEOUS, Timing.FAST);
    		
    		lang.nextStep();
    		src.unhighlight(3);
    		src.highlight(2);
    		//whileTrueText.hide(); // hide not working
    		whileTrueText.setText("", null, null);
    	}
    	//Display red evaluation of the while Condition
    	whileFalseText.setText(
    			"i = " + i + (i < node.numOfKeys
    							? "; keys[" + i + "] = " + node.keys[i]
    							: ""), 
    			null,
    			null);
    	whileFalseText.show();
		
    	lang.nextStep();
    	src.unhighlight(2);
    	//whileFalseText.hide(); // hide not working
    	whileFalseText.setText("", null, null);
    	src.highlight(4);
    	
    	lang.nextStep();
    	if (i < node.numOfKeys && searchKey == node.keys[i]) {
    		//Display green evaluation of the if Condition
    		trueTrueText.setText(
    				"i = " + i + "; keys[" + i + "] = " + node.keys[i],
    				null,
    				null);
    		trueTrueText.show();
    		
    		lang.nextStep();
    		src.highlight(5);
    		
    		lang.nextStep();
    		node.pointer.hide();
    		node.changeColor(defaultColor); //Change Back to the default Color
    		//trueTrueText.hide(); // hide not working
    		trueTrueText.setText("", null, null);
    		return true;
    	}
    	//Display red evaluation of the if Condition
    	trueFalseText.setText(
    			"i = " + i + (i < node.numOfKeys
    							? "; keys[" + i + "] = " + node.keys[i]
    							: ""),
    			null,
    			null);
    	trueFalseText.show();
    	
    	lang.nextStep();
    	src.unhighlight(4);
    	//trueFalseText.hide();
    	trueFalseText.setText("", null, null);
    	src.highlight(6);
    	
    	lang.nextStep();
    	if (node.childs[i] == null) {
    		//Display green evaluation of the if Condition
    		falseTrueText.setText("true", null, null);
    		falseTrueText.show();
    		
    		lang.nextStep();
    		src.highlight(7);
    		
    		lang.nextStep();
    		//falseTrueText.hide(); // hide not working
    		falseTrueText.setText("", null, null);
    		node.changeColor(defaultColor);
    		node.pointer.hide();
    		return false;
    	} else {
    		//Display red evaluation of the if Condition
    		falseFalseText.setText("false", null, null);
    		falseFalseText.show();
    		
    		lang.nextStep();
    		src.unhighlight(6);
    		src.highlight(8);
    		
    		lang.nextStep();
    		src.highlight(9);
    		
    		lang.nextStep();
    		// === Recursion ===
    		//falseFalseText.hide(); // hide not working
    		falseFalseText.setText("", null, null);
    		node.changeColor(unfocusedColor);
    		node.changeChildColor(unfocusedColor, i);
    		var.hide();
    		boolean result = searchNode(node.childs[i], searchKey);
    		var.set("i", Integer.toString(i));
    		varNumOfKeys.set("numOfKeys", Integer.toString(node.numOfKeys));
    		node.changeColor(focusedColor);
    		node.changeChildColor(defaultColor, i);
   			node.changeChildColor(defaultColor, i);
    		
    		//Resets Code highlight
    		for (int j = 0; j < 10; j++) {
    			src.unhighlight(j);
    		}
    		//Restores own Code highlight
    		src.highlight(0);
    		src.highlight(9);
    		
    		headText.setText("searchKey = " + searchKey + "; numOfKeys = " + node.numOfKeys, null, null);
    		
    		//Display result
    		lang.newText(recursionPos, result ? "true" : "false", "recursionComment", null, result ? trueComment : falseComment);
    		
    		lang.nextStep();
    		//Keep highlight if search is finished
    		if (node.parent == null)
    			node.changeColor(unfocusedColor);
    		else {
    			node.changeColor(defaultColor);
    			node.pointer.hide();
    		}
    		src.unhighlight(9);
    		src.unhighlight(0);
    		var.hide(); // hide not working
    		varNumOfKeys.hide();
    		if (node.parent == null)
    			src.highlight(10);
    		return result;
    	}
    }

    public void init(){
        lang = new AnimalScript("BTree Search", "Fritz Beutel, Maxim Kuznetsov", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        int searchKey = (Integer)primitives.get("searchKey");
        int[][] tree = (int[][])primitives.get("tree");
        //defaultColor = (Color)primitives.get("defaultColor");
        defaultColor = (Color)((TextProperties)props.getPropertiesByName("defaultColor")).get(AnimationPropertiesKeys.COLOR_PROPERTY);
        //trueColor = (Color)primitives.get("trueColor");
        trueColor = (Color)((TextProperties)props.getPropertiesByName("trueColor")).get(AnimationPropertiesKeys.COLOR_PROPERTY);
        //falseColor = (Color)primitives.get("falseColor");
        falseColor = (Color)((TextProperties)props.getPropertiesByName("falseColor")).get(AnimationPropertiesKeys.COLOR_PROPERTY);
        //focusedColor = (Color)primitives.get("focusedColor");
        focusedColor = (Color)((TextProperties)props.getPropertiesByName("focusedColor")).get(AnimationPropertiesKeys.COLOR_PROPERTY);
        //unfocusedColor = (Color)primitives.get("unfocusedColor");
        unfocusedColor = (Color)((TextProperties)props.getPropertiesByName("unfocusedColor")).get(AnimationPropertiesKeys.COLOR_PROPERTY);
        falseComment = (TextProperties)props.getPropertiesByName("evalComments");
        falseComment.set(AnimationPropertiesKeys.COLOR_PROPERTY, falseColor);
        trueComment = new TextProperties();
        //Copy evalComments properties
        for (String s : falseComment.getAllPropertyNames()) {
        	trueComment.set(s, falseComment.get(s));
        }
        trueComment.set(AnimationPropertiesKeys.COLOR_PROPERTY, trueColor);
        sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, defaultColor);
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, focusedColor);
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        		((Font)sourceCodeProps.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily(),
        		((Font)sourceCodeProps.get(AnimationPropertiesKeys.FONT_PROPERTY)).getStyle(), // Style Change wont work
        		16));
        
        // Wont Accept font size from xml. Therefore font has to be set again with proper size
        falseComment.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(((Font)falseComment.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily(), Font.PLAIN, 16));
        trueComment.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(((Font)trueComment.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily(), Font.PLAIN, 16));
        
        // === Preset/Unchangeable properties ===
        textProps = new TextProperties();
    	textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
    	//For keys with 3 digits
    	largeTreeNumberProps = new TextProperties();
    	largeTreeNumberProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 11));
    	
    	arrowProps = new PolylineProperties();
    	arrowProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    	arrowProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, defaultColor);
        
        TreeNode rootNode = createTree(tree);
        generateTree();
        for (Primitive p : visualTree)
        	p.hide();
        
        start(rootNode, searchKey);
        
        lang.finalizeGeneration();
        
        return lang.toString();
    }
    
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		int searchKey = (Integer)primitives.get("searchKey");
        int[][] tree = (int[][])primitives.get("tree");
        
        if (searchKey < 0)
        	throw new IllegalArgumentException(translator.translateMessage("negativeSearchKey"));
        
        TreeNode root = createTree(tree);
        
        //check if the tree is sorted
    	List<Integer> flat = traverseTree(root);
    	for (int i = 0; i < flat.size() - 1; i++) {
    		if (flat.get(i) > flat.get(i + 1)) {
    			throw new IllegalArgumentException(translator.translateMessage("unsortedTree"));
    		}
    	}
    	
    	//Check that all leafs are at the same depth
    	int depth = depthTree.size() - 1;
    	//create list of all non-leaf-nodes
    	List<TreeNode> nonLeafNodes = new ArrayList<>();
    	for (int i = depth - 1; i >= 0; i--) {
    		nonLeafNodes.addAll(depthTree.get(i));
    	}
    	if (depth > 0) {
    		for (TreeNode n : nonLeafNodes) {
    			if (n.numOfChilds() != n.numOfKeys + 1) {
    				throw new IllegalArgumentException(translator.translateMessage("unbalancedTree"));
    			}
    		}
    	}
    	return true;
	}
    
	/**
	 * Builds a Tree (TreeNode) from the input matrix
	 * @param matrix the input matrix for the tree
	 * @return The tree
	 */
    private TreeNode createTree(int[][] matrix) {
    	depthTree.clear();
    	if (matrix.length == 0) {
    		//Tree has to have at least one node. Does it?!
    		throw new IllegalArgumentException(translator.translateMessage("emptyTree"));
    	}
    		
    	//Get Node Size from first Element
    	nodeKeyCount = matrix[0].length;
    	int startIndex = 1; //For the non-root elements
    	//Saves the Position (in the matrix) from the first Node of each Depth of the tree
    	List<Integer> depthIndex = new ArrayList<>();
    	depthIndex.add(0); //root element Position
    	depthTree.add(new ArrayList<>());
    	while (startIndex < matrix.length) {
    		depthIndex.add(startIndex);
        	depthTree.add(new ArrayList<>());
    		startIndex += startIndex * nodeKeyCount + 1;
    	}
    	
    	TreeNode root = buildNode(depthIndex, matrix, 0, nodeKeyCount, 0, null);
    	
    	return root;
    }
    
    /**
     * Builds a single Node with all its children
     * @param index a list of startindexes in the matrix for each depth
     * @param matrix the input matrix
     * @param pos the position of the current node
     * @param size the number of childs per node 
     * @param depth the depth of the node (zero-Based)
     * @param parent the parent node of the current node. Null if its the root node
     * @return The complete TreeNode with all its children
     */
    public TreeNode buildNode(List<Integer> index, int[][] matrix, int pos, int size, int depth, TreeNode parent) {
    	//if pos is not valid position return
    	if (pos >= matrix.length)
    		return null;
    	//else create new node with data from pos
    	boolean t = false;
    	for (int i : matrix[pos]) {
    		if (i != 0) {
    			t = true;
    			break;
    		}
    	}
    	if (!t) {
    		return null;
    	}
    	
    	TreeNode currNode = new TreeNode(size, matrix[pos], parent);
    	depthTree.get(depth).add(currNode);
    	
    	//if (matrix[pos][0] == 43) 
    	//	System.err.println(pos);
    	
    	//If Node is on the deepest level/is a leaf return without creating childs
    	if (index.size() - 1 <= depth)
    		return currNode;

    	//Calc Positions of the Childs in the matrix
    	int ownOffset = pos - index.get(depth);
    	int startingPos = index.get(depth + 1);
    	if (ownOffset != 0) //ownOffset * size ==> end of prev child nodes; +1 == beginning of the child nodes 
    		startingPos += ownOffset * (size + 1);
    	//Build childs recursively
    	for (int i = 0; i < size + 1; i++) {
    		currNode.childs[i] = buildNode(index, matrix, startingPos + i, size, depth + 1, currNode);
    	}
    	return currNode;
    }

    /**
     * Converts the tree to a flat list (sorted as they were in the tree)
     * @param node the tree that should be converted
     * @return A flat list of all keys in the tree (sorted as they were in the tree)
     */
    public List<Integer> traverseTree(TreeNode node) {
    	if (node == null)
    		return new ArrayList<>();
    	
    	List<Integer> result = new ArrayList<>();
    	for (int i = 0; i < node.childs.length; i++) {
    		result.addAll(traverseTree(node.childs[i]));
    		if (i < node.keys.length && node.keys[i] >= 0)
    			result.add(node.keys[i]);
    	}
    	return result;
    }

    public String getName() {
        return translator.translateMessage("algoName");
    }

    public String getAlgorithmName() {
        return translator.translateMessage("algoName");
    }

    public String getAnimationAuthor() {
        return "Fritz Beutel, Maxim Kuznetsov";
    }

    public String getDescription(){
    	return translator.translateMessage("description") + "\n\n" + translator.translateMessage("tutorial");
    }

    public String getCodeExample(){
        return "public boolean find(int searchKey) {"
 +"\n"
 +"    int i = 0;"
 +"\n"
 +"    while (i < numOfKeys && searchKey > keys[i])"
 +"\n"
 +"        i++;"
 +"\n"
 +"    if (i < numOfKeys && searchKey == keys[i])"
 +"\n"
 +"        return true;"
 +"\n"
 +"    if (childs[i] == null)"
 +"\n"
 +"        return false;"
 +"\n"
 +"    else"
 +"\n"
 +"        return childs[i].find(searchKey);"
 +"\n"
 +"}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return translator.getCurrentLocale(); // Germany?
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}