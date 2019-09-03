package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.tree.TreeBuilder.TreeNode;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class BSTreeTraverse implements ValidatingGenerator {

	private static Language lang;

	// Tree building
	private TreeBuilder treeBuilder = new TreeBuilder();
	private String textualTree;
	private Graph tree;
	private GraphProperties treeProps = new GraphProperties();
	// labels
	private HashMap<Integer, Text> labelPointers;
	// Properties
	private TextProperties titleProps = new TextProperties();
	private SourceCodeProperties resultProps = new SourceCodeProperties();
	private SourceCodeProperties pseudoCodeProps = new SourceCodeProperties();
	private TextProperties seenChildrenProps = new TextProperties();
	private SourceCodeProperties introProps = new SourceCodeProperties();
	private SourceCodeProperties ComplexityExpProps = new SourceCodeProperties();
	private SourceCodeProperties conclusionProps = new SourceCodeProperties();
	private ArrayProperties stackProps = new ArrayProperties();

	/**
	 * 
	 */
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		//
		String textualTree = (String) primitives.get("tree");
		TreeNode root = treeBuilder.parseString(textualTree);
		if (root == null)
			return false;
		//
		if (!checkTreeLeafIntegerValue(root))
			return false;

		tree = treeBuilder.buildTree(lang, textualTree, treeProps);
		return validateTree(tree);


	
	}

	/**
	 * 
	 * @param tree
	 * @return
	 */

	private boolean validateTree(Graph tree) {

		boolean result = true;
		ArrayList<Integer> children = new ArrayList<>();
		ArrayList<String> children1 = new ArrayList<>();
		for (int i = 0; i < tree.getSize(); i++) {
			 children = getChildren(tree, i);
			 for(int c=0;c<children.size();c++){
			 children1.add(tree.getNodeLabel(c));
			 }
			 System.out.println("Children of "+ tree.getNodeLabel(i)+ "are : "
			 +children1.toString());
			
			 // check tree Structure
			 if (children.size() > 2)
			 result = false;
			
			 if (children.size() == 2) {
			 if (Integer.parseInt(tree.getNodeLabel(children.get(0))) >
			 Integer
			 .parseInt(tree.getNodeLabel(i))
			 && Integer.parseInt(tree.getNodeLabel(children.get(1))) > Integer
			 .parseInt(tree.getNodeLabel(i)) ||
			 Integer.parseInt(tree.getNodeLabel(children.get(0))) < Integer
			 .parseInt(tree.getNodeLabel(i))
			 && Integer.parseInt(tree.getNodeLabel(children.get(1))) < Integer
			 .parseInt(tree.getNodeLabel(i)) )
			 result = false;
			 }

		}
		
		
		return result;
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	private boolean checkTreeLeafIntegerValue(TreeNode node) {
		if (node.children == null) {
			try {
				Integer.parseInt(node.label);
			} catch (NumberFormatException e) {
				return false;
			}
		} else
			for (TreeNode child : node.children)
				if (!checkTreeLeafIntegerValue(child))
					return false;
		return true;

	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		labelPointers = new HashMap<>();
		textualTree = (String) primitives.get("tree");
		treeProps = (GraphProperties) props.getPropertiesByName("treeProps");
		seenChildrenProps = (TextProperties) props
				.getPropertiesByName("seenChildrenProps");
		titleProps = (TextProperties) props.getPropertiesByName("titleProps");
		pseudoCodeProps = (SourceCodeProperties) props
				.getPropertiesByName("pseudoCodeProps");
		stackProps = (ArrayProperties) props.getPropertiesByName("stackProps");
		introProps = (SourceCodeProperties) props
				.getPropertiesByName("introProps");
		ComplexityExpProps = (SourceCodeProperties) props
				.getPropertiesByName("ComplexityExpProps ");
		resultProps = (SourceCodeProperties) props
				.getPropertiesByName("resultProps");
		conclusionProps = (SourceCodeProperties) props
				.getPropertiesByName("conclusionProps");

		start();
		traverse();

		return lang.toString();

	}

	public void init() {
		lang = new AnimalScript("Binary Search Tree Traverse",
				"Farouk Houami,El bouabidi Fethi,Pascal Weisenburger", 800, 600);
		lang.setStepMode(true);
	}

	public String getName() {
		return "Binary Search Tree Traverse";
	}

	public String getAlgorithmName() {
		return "BSTreeTraverse";
	}

	public String getAnimationAuthor() {
		return "Farouk Houami,El bouabidi Fethi,Pascal Weisenburger";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	/**
	 * The concrete language object used for creating output
	 */

	/*
	 * Executes the traverse algorithm on the given Tree
	 */

	public BSTreeTraverse(Language l) {
		lang = new AnimalScript("Reverse Delete ",
				"Haouami Farouk,El Fethi Bouabidi", 800, 600);
	}

	public BSTreeTraverse() {

	}

	public void start() {

		Text title = lang.newText(new Coordinates(30, 20),
				"Binary Search Tree Traverse", "title", null, titleProps);
		title.show();

		SourceCode intro1 = lang.newSourceCode(new Coordinates(50, 70),
				"Presnetation1", null, introProps);

		intro1.addCodeLine("", null, 0, null);

		intro1.addCodeLine(
				"Binary Search Tree Traverse is an iterative algorithm that sorts  the keys ​​stored in a binary search tree in ascending order and  collect them  in a sequence",
				null, 0, null);
		intro1.addCodeLine(
				" and  collect them  in a sequence.",
				null, 0, null);
		intro1.addCodeLine("", null, 0, null);
		intro1.addCodeLine(
				"Therefore we need a  stack  whose elements are pairs consisting of :  ", null,
				0, null);

		intro1.addCodeLine("1)a pointer to a a binary search tree node ", null, 0,
				null);
		intro1.addCodeLine(
				"2) a natural number SeenChildren in the range {0,1,2},that stores the number seen children of a node.",
				null, 0, null);

		intro1.addCodeLine(
				"SeenChildren ist >= 1  all keys contained ​​in the left subtree of the current node are already in the sequence  (including the current node )",
				null, 0, null);

		intro1.addCodeLine(
				"SeenChildren ist 2 when additionally all keys contained in the right subtree of the current node are in the sequence",
				null, 0, null);
		lang.nextStep();
		intro1.hide();

	}

	/**
 * 
 */
	public void traverse() {
		// creates Tree
		tree = treeBuilder.buildTree(lang, textualTree, treeProps);

		for (int c = 0; c < tree.getSize(); c++) {
			System.out.println("node @ index " + c + " is "
					+ tree.getNodeLabel(c));
		}

		tree.show();

		// result

		SourceCode result = lang.newSourceCode(new Coordinates(100, 600),
				"result", null, resultProps);
		result.addCodeLine("Result :  ", "result", 0, null);
		result.show();

		// pseudo Code
		pseudoCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.red);
		SourceCode pc = lang
				.newSourceCode(new Offset(200, 0, tree,
						AnimalScript.DIRECTION_NE), "sourceCode", null,
						pseudoCodeProps);

		pc.addCodeLine("Pointer p = tree.getRoot() && p.seenChildren == 0",
				"pointer", 0, null);
		pc.addCodeLine("stack.push(p)", "init", 0, null);
		pc.addCodeLine("", "", 0, null);
		pc.addCodeLine("    while(stack.length()!= 0)  ", "while", 0, null);
		pc.addCodeLine("", "", 0, null);
		pc.addCodeLine("         if (p.ssenChildren == 0) ", "sc0", 0, null);
		pc.addCodeLine("              if(p.left != void) ", "leftIf", 0, null);
		pc.addCodeLine("                    p = p.left  ", "x", 0, null);
		pc.addCodeLine("                    Set p.seenChildrn == 0 ",
				"pushLeft", 0, null);
		pc.addCodeLine("                    Stack.push(p)", "pl", 0, null);
		pc.addCodeLine("              else  Set p.seenChildren == 1", "el", 0,
				null);
		pc.addCodeLine("                    Result.append p ", "append", 0,
				null);
		pc.addCodeLine("", "", 0, null);
		pc.addCodeLine("         else if(p.seenChildren == 1)  ", "sc1", 0,
				null);
		pc.addCodeLine("              if (p.right != void)", "rightIf", 0, null);
		pc.addCodeLine("                    p = p.right ", "y", 0, null);
		pc.addCodeLine("                    Set p.seenChildrn  == 0",
				"pushRight", 0, null);
		pc.addCodeLine("                    Stack.push(p)", "p2", 0, null);
		pc.addCodeLine("              else  Set p.seenChildren == 2",
				"elseRight", 0, null);
		pc.addCodeLine("", "", 0, null);
		pc.addCodeLine("         else if(p.seenChildren == 2) ", "sc3", 0, null);
		pc.addCodeLine("                    Stack.pop()   ", "pop", 0, null);
		pc.addCodeLine("                    p = Stack.top(); ", "top", 0, null);
		pc.addCodeLine("                    p.seenChildren += 1 ", "set", 0,
				null);
		pc.addCodeLine("              if(p.seenChildren == 1)", "z", 0, null);
		pc.addCodeLine("              && p.left != void ", "iff", 0, null);
		pc.addCodeLine("                    Result.append(p)", "append2", 0,
				null);

		lang.nextStep("begin");

		// first step
		String[] scr = new String[1];
		scr[0] = tree.getNodeLabel(0) + " : " + 0;
		StringArray stack = lang.newStringArray(new Coordinates(290, 70), scr,
				"intArray", null, stackProps);

		tree.highlightNode(0, null, null);
		setVisitedChildren(tree, 0, 0, labelPointers, seenChildrenProps);
		pc.highlight("pointer");
		pc.highlight("init");

		lang.nextStep();
		pc.unhighlight("pointer");
		pc.unhighlight("init");
		pc.highlight("while");

		// Begin to traverse
		int stackPointer = 0;
		int treePointer = 0;
		while (true) {

			pc.unhighlight("iff");
			pc.unhighlight("append2");
			// elem.seenChildren == 0
			if (stack.getData(stackPointer).endsWith("0")) {
				pc.highlight("sc0");
				lang.nextStep();
				// node.left!= void
				if (getLeftChild(tree, treePointer) != -1) {
					tree.highlightEdge(treePointer,
							getLeftChild(tree, treePointer), null, null);
					// p = p.left
					pc.highlight("leftIf");
					lang.nextStep();
					tree.unhighlightEdge(treePointer,
							getLeftChild(tree, treePointer), null, null);
					pc.unhighlight("leftIf");
					pc.highlight("x");
					tree.unhighlightNode(treePointer, null, null);
					treePointer = getLeftChild(tree, treePointer);
					tree.highlightNode(treePointer, null, null);
					lang.nextStep();
					// push(p)
					pc.unhighlight("leftIf");
					pc.unhighlight("x");
					stack.hide();
					stack = push(stack, tree.getNodeLabel(treePointer));

					setVisitedChildren(tree, treePointer, 0, labelPointers,
							seenChildrenProps);
					stackPointer++;
					stack.show();

					pc.highlight("pushLeft");
					pc.highlight("pl");
					lang.nextStep();
					pc.unhighlight("sc0");
					pc.unhighlight("leftIf");
					pc.unhighlight("pushLeft");
					pc.unhighlight("pl");

				} else {
					// Set p.seenChildren == 1
					pc.highlight("el");
					stack.hide();
					stack = SetSeenChildren1(stack, 1);

					setVisitedChildren(tree, treePointer, 1, labelPointers,
							seenChildrenProps);
					stack.show();
					stack.highlightCell(stackPointer, null, null);
					lang.nextStep();
					// append p
					stack.unhighlightCell(stackPointer, null, null);
					pc.unhighlight("el");
					pc.highlight("append");
					// append to result
					result.addCodeElement(tree.getNodeLabel(treePointer), null,
							0, null);
					lang.nextStep();
					pc.unhighlight("append");
					pc.unhighlight("sc0");
				}
			}

			// // elem.seenChildren == 1
			else if (stack.getData(stackPointer).endsWith("1")) {

				pc.highlight("sc1");
				lang.nextStep();
				// node.right == void
				if (getRightChild(tree, treePointer) != -1) {
					tree.highlightEdge(treePointer,
							getRightChild(tree, treePointer), null, null);
					// if p.left != void -> p = p.right
					pc.highlight("rightIf");
					lang.nextStep();
					tree.unhighlightEdge(treePointer,
							getRightChild(tree, treePointer), null, null);
					pc.unhighlight("rightIf");
					pc.highlight("y");
					tree.unhighlightNode(treePointer, null, null);
					treePointer = getRightChild(tree, treePointer);
					tree.highlightNode(treePointer, null, null);
					lang.nextStep();
					// push (p)
					pc.unhighlight("rightIf");
					pc.unhighlight("y");
					pc.highlight("pushRight");
					pc.highlight("p2");
					stack.hide();
					stack = push(stack, tree.getNodeLabel(treePointer));

					setVisitedChildren(tree, treePointer, 0, labelPointers,
							seenChildrenProps);
					stackPointer++;
					stack.show();

					lang.nextStep();
					pc.unhighlight("pushRight");
					pc.unhighlight("p2");
					pc.unhighlight("sc1");

				} else {
					pc.highlight("elseRight");
					stack.hide();
					stack = SetSeenChildren1(stack, 2);

					setVisitedChildren(tree, treePointer, 2, labelPointers,
							seenChildrenProps);
					for (int i = 0; i < labelPointers.size(); i++)
						if (labelPointers.get(treePointer).getText()
								.equals("2"))
							System.out.println((labelPointers.get(treePointer)
									.getText()));
					stack.highlightCell(stackPointer, null, null);
					stack.show();

					lang.nextStep();
					stack.unhighlightCell(stackPointer, null, null);
					pc.unhighlight("elseRight");
					pc.unhighlight("sc1");

				}

			}

			// elem.SennChildren == 2

			else if (stack.getData(stackPointer).endsWith("2")) {
				pc.highlight("sc3");

				lang.nextStep();
				tree.unhighlightNode(treePointer, null, null);
				pc.unhighlight("sc3");
				pc.highlight("pop");
				stack.hide();
				stack = pop(stack);
				stackPointer--;
				stack.show();

				treePointer = getParent(tree, treePointer);
				lang.nextStep();
				pc.unhighlight("pop");
				//
				// stack is empty : terminate
				if (stack.getLength() == 0) {
					result.moveBy(null, 330, -400, null, null);
					pc.hide();
					end();
					lang.nextStep();

					return;
				}

				pc.highlight("top");
				tree.highlightNode(treePointer, null, null);

				int seenChildren = Integer.parseInt(stack.getData(stackPointer)
						.substring(stack.getData(stackPointer).length() - 1));
				lang.nextStep();
				pc.unhighlight("top");
				pc.highlight("set");
				stack.hide();
				stack = SetSeenChildren1(stack, seenChildren + 1);

				setVisitedChildren(tree, treePointer, seenChildren + 1,
						labelPointers, seenChildrenProps);
				stack.highlightCell(stackPointer, null, null);
				stack.show();

				lang.nextStep();
				stack.unhighlightCell(stackPointer, null, null);
				pc.highlight("z");
				pc.unhighlight("set");
				if (stack.getData(stackPointer).endsWith("1")
						&& getLeftChild(tree, treePointer) != -1)
					result.addCodeElement(tree.getNodeLabel(treePointer), null,
							0, null);
				pc.highlight("iff");
				pc.highlight("append2");

				lang.nextStep();

				pc.unhighlight("z");
				pc.unhighlight("iff");
				pc.unhighlight("append2");
				pc.unhighlight("sc3");

			}
			lang.nextStep("end");
		}

	}

	private void end() {

		for (int i = 0; i < labelPointers.size(); i++) {
			labelPointers.get(i).moveBy(null, 1000, 1000, null, null);
		}
		// result
		tree.moveBy(null, 330, 150, null, null);
		resultProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		SourceCode conclusion = lang.newSourceCode(new Coordinates(50, 100),
				"source", null, conclusionProps);
		conclusion
				.addCodeLine(
						"The sequence contains all the keys ​​stored in the tree sorted in ascending order",
						"concl", 0, null);
		// Complexity:

		SourceCode complexity = lang.newSourceCode(new Coordinates(900, 100),
				"complexity", null, ComplexityExpProps);

		complexity
				.addCodeLine(
						" the complexity of the algorithm is linear in the length of the sequence in the worst case ,",
						"1", 0, null);
		complexity.addCodeLine("", "1", 0, null);
		complexity.addCodeLine("This is achieved as follows :,", "1", 0, null);
		complexity.addCodeLine("* Each iteration of the loop takes constant time", "1", 0, null);
		complexity
				.addCodeLine(
						"* Each node is visited in at most three iterations, Viz. once with  seenChildren 0,1 and 2, "
						+ "respectively",
						"1", 0, null);

	}

	/**
	 * 
	 * @param trees
	 * @param treePointer
	 * @param visitedChildren
	 * @param labelsPointers
	 * @param tp
	 * @return
	 */
	private void setVisitedChildren(Graph tree, int treePointer,
			int visitedChildren, HashMap<Integer, Text> labelsPointers,
			TextProperties tp) {

		Text result;

		if (labelsPointers.get(treePointer) != null) {

			labelsPointers.get(treePointer).setText(
					Integer.toString(visitedChildren), null, null);
			result = labelsPointers.get(treePointer);
			result.show();

		} else {
			Text label = lang.newText(tree.getNode(treePointer),
					Integer.toString(visitedChildren),
					tree.getNodeLabel(treePointer), null, tp);

			label.moveBy(null, -20, 44, null, null);
			label.show();
			result = label;
		}
		labelsPointers.put(treePointer, result);

	}

	/**
	 * 
	 * @param tree
	 * @param index
	 * @return
	 */
	private int getRightChild(Graph tree, int index) {
		int child = -1;
		int[][] adjMatrix = tree.getAdjacencyMatrix();

		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix.length; j++) {

				if (i == index || j == index) {
					if (adjMatrix[i][j] != 0) {
						if (i == index && child != j) {
							
							if (Integer.parseInt(tree.getNodeLabel(j)) >= Integer
									.parseInt(tree.getNodeLabel(index))
									&& index < j)
								child = j;

						} else if (j == index && child != i) {
							if (Integer.parseInt(tree.getNodeLabel(i)) >= Integer
									.parseInt(tree.getNodeLabel(index))
									&& index < i)
								child = i;
						}

					}
				}

			
			
			}
		}
		return child;

	}

	/**
	 * 
	 * @param tree
	 * @param index
	 * @return
	 */
	private int getLeftChild(Graph tree, int index) {
		int child = -1;
		int[][] adjMatrix = tree.getAdjacencyMatrix();

		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix.length; j++) {

				if (i == index || j == index) {
					if (adjMatrix[i][j] != 0) {
						if (i == index && child != j) {
							if (Integer.parseInt(tree.getNodeLabel(j)) <= Integer
									.parseInt(tree.getNodeLabel(index))
									&& index < j)
								child = j;

						} else if (j == index && child != i) {
							if (Integer.parseInt(tree.getNodeLabel(i)) <= Integer
									.parseInt(tree.getNodeLabel(index))
									&& index < i)
								child = i;
						}

					}
				}

			}
		}
		return child;

	}

	/**
	 * 
	 * @param tree
	 * @param index
	 * @return
	 */
	private int getParent(Graph tree, int index) {
		int parent = -1;
		int[][] adjMatrix = tree.getAdjacencyMatrix();

		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix.length; j++) {
				if (adjMatrix[i][j] != 0) {
					if (i == index && j < index)
						parent = j;
					else if (j == index && i < index)
						parent = i;
				}
			}
		}
		return parent;
	}

	/**
	 * 
	 * @param tree
	 * @param index
	 * @return the children indices of the given indexx
	 */
	private ArrayList<Integer> getChildren(Graph tree, int index) {
		ArrayList<Integer> children = new ArrayList<>();

		int[][] adjMatrix = tree.getAdjacencyMatrix();

		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix.length; j++) {

				if (i == index || j == index) {
					if (adjMatrix[i][j] != 0) {
						if (i == index && j > i) {
							if (!children.contains(j))
								children.add(j);

						} else if (j == index && j < i) {
							if (!children.contains(i))
								children.add(i);
						}

					}
				}

			}
		}

		return children;

	}

	/**
	 * 
	 * @param stack
	 * @param s
	 *            element to push
	 * @return
	 */

	private StringArray push(StringArray stack, String s) {

		String[] temp1 = new String[stack.getLength() + 1];
		for (int i = 0; i < stack.getLength(); i++) {
			temp1[i] = stack.getData(i);
		}
		temp1[temp1.length - 1] = s + " : " + 0;

		StringArray temp = lang.newStringArray(new Coordinates(290, 50), temp1,

		"intArray", null, stackProps);

		temp.hide();

		return temp;

	}

	/**
	 * pop the last element pushed on the stack
	 * 
	 * @param stack
	 * @return
	 */
	private StringArray pop(StringArray stack) {

		String[] temp1 = new String[stack.getLength() - 1];

		for (int i = 0; i < stack.getLength() - 1; i++) {
			temp1[i] = stack.getData(i);
		}

		StringArray temp = lang.newStringArray(new Coordinates(290, 50), temp1,
				"intArray", null, stackProps);

		temp.hide();
		return temp;

	}

	/**
	 * 
	 * @param stack
	 * @param seenChildren
	 * @return
	 */
	private StringArray SetSeenChildren1(StringArray stack, int seenChildren) {
		String[] temp1 = new String[stack.getLength()];

		if (stack.getLength() == 1) {
			temp1[temp1.length - 1] = stack.getData(stack.getLength() - 1)
					.substring(0,
							stack.getData(stack.getLength() - 1).length() - 1)
					+ seenChildren;
		} else {
			for (int i = 0; i < stack.getLength() - 1; i++) {
				temp1[i] = stack.getData(i);
			}

			temp1[temp1.length - 1] = stack.getData(stack.getLength() - 1)
					.substring(0,
							stack.getData(stack.getLength() - 1).length() - 1)
					+ seenChildren;

			temp1[temp1.length - 1] = stack.getData(stack.getLength() - 1)
					.substring(0,
							stack.getData(stack.getLength() - 1).length() - 1)
					+ seenChildren;
		}
		StringArray temp = lang.newStringArray(new Coordinates(290, 50), temp1,

		"intArray", null, stackProps);

		temp.hide();

		return temp;
	}

	/**
	 * 
	 * @return
	 */
	public static GraphProperties getDefaultGraphProperties() {

		GraphProperties graphProps = new GraphProperties();
		graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.black);
		graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.PINK);
		graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
		graphProps
				.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.red);

		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.green);

		return graphProps;

	}

	public String getDescription() {
		return "Binary Search Tree Traverse is an iterative algorithm that sorts  the keys ​​stored in a binary search tree in ascending order and  collect them  in a sequence ."
				+ "\n"
				+ "\n"
				+ "Therefore we need a  stack  whose elements are pairs consisting of :"
				+ "\n"
				+ " 1) a binary search tree node                                                                                           "
				+ "\n"
				+ " 2) a natural number SeenChildren in the range {0,1,2},that stores the number seen children of a node."
				+ "\n"
				+ " "
				+ "\n"
				+ "\n"
				+ "SeenChildren ist >= 1  all keys contained ​​in the left subtree of the current node are already in the sequence  (including the current node )"
				+ "\n"
				+ "\n"
				+ "SeenChildren ist 2 when additionally all keys contained in the right subtree of the current node are in the sequence  "
				+ "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n";
	}

	public String getCodeExample() {
		return "		Pointer p = tree.getRoot( && p.seenChildren ==0) "
				+ "\n"
				+ "	                              stack.push(p)"
				+ "\n"
				+ "		  "
				+ "\n"
				+ "		  while(stack.length(!=0))"
				+ "\n"
				+ "		      if (p.ssenChildren ==  0){  "
				+ "\n"
				+ "		              if(p.left != null){  "
				+ "\n"
				+ "		                    p = p.left   ;"
				+ "\n"
				+ "		                    Set p.seenChildrn == 0;  "
				+ "\n"
				+ "		                    Stack.push(p)"
				+ "\n"
				+ "		              }else  Set p.seenChildren == 1"
				+ "\n"
				+ "  		                  Result.append p  "
				+ "\n"
				+ "			        }"
				+ "\n"
				+ "		   "
				+ "\n"
				+ "		      else if(p.seenChildren == 1){   "
				+ "\n"
				+ "		                 if (p.right != null){ "
				+ "\n"
				+ "		                    p = p.right; "
				+ "\n"
				+ "		                    Set p.seenChildrn  == 0; "
				+ "\n"
				+ "		                    Stack.push(p)"
				+ "\n"
				+ "		                 }"
				+ "\n"
				+ "		                   else  Set p.seenChildren == 2"
				+ "\n"
				+ "                                                                 }"
				+ "\n"
				+ "		   "
				+ "\n"
				+ "		        else if(p.seenChildren == 2)"
				+ "\n"
				+ "		                    Stack.pop();"
				+ "\n"
				+ "		                    p = Stack.top();"
				+ "\n"
				+ "		                    p.seenChildren += 1  "
				+ "\n"
				+ "		           "
				+ "\n"
				+ "                                                                              if((p.seenChildren == 1)&& (p.left != null))"
				+ "\n" + "		                    Result.append(p);" + "\n"
				+ "				" + "\n";
	}

	public static void main(String[] args) {

		BSTreeTraverse b = new BSTreeTraverse();

		Hashtable<String, Object> primitives = new Hashtable<String, Object>();
		AnimationPropertiesContainer props = new AnimationPropertiesContainer();

		b.generate(props, primitives);

	}

}
