/*
 * Fitch.java
 * Peter Reinhardt, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.tree;

import java.awt.Color;
import java.awt.Font;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

/**
 * @author Peter Reinhardt
 */
public class Fitch implements Generator {

	/**
	 * The concrete language object used for creating output
	 */
	private Language language;

	/**
	 * The title text including the headline
	 */
	private Text title;

	/**
	 * The status text including the status
	 */
	private Text status;

	/**
	 * The rectangle around the headline
	 */
	private Rect tRect;

	/**
	 * the source code shown in the animation
	 */
	private SourceCode sourceCode;

	/**
	 * the graph
	 */
	private Graph graph;

	/**
	 * the nodes for the graph
	 */
	private Node[] graphNodes;

	/**
	 * Globally defined properties
	 */
	private SourceCodeProperties sourceCodeProps = new SourceCodeProperties();
	private TextProperties headerProps = new TextProperties();
	private TextProperties subHeaderProps = new TextProperties();
	private TextProperties textProps = new TextProperties();
	private RectProperties titleRectProps = new RectProperties();
	private RectProperties codeRectProps = new RectProperties();
	private GraphProperties graphProps = new GraphProperties();

	/**
	 * Default constructor
	 */
	public Fitch() {
	}

	/**
	 * Initializes the animation. Shows a start page with a description header
	 */
	private void start(String[] inputLabel) {

		// show the title with a heading surrounded by a rectangle
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 30));
		title = language.newText(new Coordinates(20, 30), "Fitch-Algorithmus", "title", null, titleProps);
		tRect = language.newRect(new Offset(-5, -5, "title", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "title", "SE"), "tRect", null, titleRectProps);

		// show the description header under the title
		language.newText(new Offset(20, 40, "title", AnimalScript.DIRECTION_SW), "Beschreibung des Algorithmus",
				"descriptionHeader", null, headerProps);
		SourceCode descr;
		descr = language.newSourceCode(new Offset(0, 0, "descriptionHeader", AnimalScript.DIRECTION_SW), "descr", null,
				sourceCodeProps);
		descr.addCodeLine("Der Fitch-Algorithmus ist ein Algorithmus aus der Algorithmischen Bioinformatik.", null, 0,
				null);
		descr.addCodeLine(
				"Mit Hilfe des Verfahrens ist es möglich das kleine Parsimony-Problem, ein Teilproblem der Maximum-parsimony-Methodik zur Erstellung ",
				null, 0, null);
		descr.addCodeLine("phylogenetischer Stammbäume, zu lösen.", null, 0, null);
		descr.addCodeLine("", null, 0, null);
		descr.addCodeLine(
				"Als Eingabe für den Algorithmus dient einerseits eine zuvor festgelegte Baumtopologie, welche in der nachfolgenden Animation nicht",
				null, 0, null);
		descr.addCodeLine(
				"veränderbar ist, und andererseits eine Menge an Nucleotid- oder Aminosäurensequenzen, deren Verwandtschaftsverhältnis zueinander",
				null, 0, null);
		descr.addCodeLine(
				"unteersucht werden soll. Die Menge der Eingabesequenz ist eine Veränderliche, welche der Benutzer über das Wizard verändern kann.",
				null, 0, null);
		descr.addCodeLine("Die Sequenzen bilden dann die Blätter in der Baumtopologie. ", null, 0, null);
		descr.addCodeLine(
				"Mit Hilfe dieser Daten werden dann neue Sequenzen für die internen Knoten des Baumes berechnet, so dass",
				null, 0, null);
		descr.addCodeLine(
				"die Summe der Mutationen zwischen den Sequenzen minimal ist und die Sequenzunterschiede der Eingabesequenzen",
				null, 0, null);
		descr.addCodeLine("erklärt werden.", null, 0, null);
		descr.addCodeLine("Der Algorithmus unterteilt sich in zwei Phasen.", null, 0, null);
		language.nextStep("Einleitung");
		// ------------------------------------------------------------------------------------------------------------

		// show the description header under the title
		language.newText(new Offset(20, 40, "descr", AnimalScript.DIRECTION_SW), "Erste Phase", "stepOneHeader", null,
				subHeaderProps);
		SourceCode stepOne = language.newSourceCode(new Offset(0, 0, "stepOneHeader", AnimalScript.DIRECTION_SW),
				"stepOne", null, sourceCodeProps);
		stepOne.addCodeLine(
				"Im ersten Schritt werden die minimale Anzahl an Mutationen und alle potentiell möglichen Beschriftungen",
				null, 0, null);
		stepOne.addCodeLine(
				"der internen Knoten für diese Mutationsanzahl ermittelt. Beginnend bei den Blättern wird  jeder interne",
				null, 0, null);
		stepOne.addCodeLine(
				"Knoten mit einer neuen Merkmalsmenge versehen. Existiert eine Schnittmenge zwischen den beiden ", null,
				0, null);
		stepOne.addCodeLine(
				"Merkmalsmengen zweier Knoten, die mit einem gemeinsamen Vorgängerknoten über eine Kante verbunden",
				null, 0, null);
		stepOne.addCodeLine("sind, so wird auch der entsprechende Vorgängerknoten mit dieser Schnittmenge beschriftet.",
				null, 0, null);
		stepOne.addCodeLine("Existiert dagegen keine solche Schnittmenge, so wird der zu beschriftende Knoten", null, 0,
				null);
		stepOne.addCodeLine("mit der Vereinigung der beiden Mengen versehen.", null, 0, null);
		language.nextStep("");
		// ------------------------------------------------------------------------------------------------------------

		// show the description header under the title
		language.newText(new Offset(0, 40, "stepOne", AnimalScript.DIRECTION_SW), "Zweite Phase", "stepTwoHeader", null,
				subHeaderProps);
		SourceCode stepTwo = language.newSourceCode(new Offset(0, 0, "stepTwoHeader", AnimalScript.DIRECTION_SW),
				"stepTwoHeader", null, sourceCodeProps);
		stepTwo.addCodeLine("In der zweiten Phase wird von der Wurzel des Baumes ausgehend eine endgültige", null, 0,
				null);
		stepTwo.addCodeLine("Beschriftung für die internen Knoten ausgewählt, indem ein bestimmtes Merkmal", null, 0,
				null);
		stepTwo.addCodeLine("an jedem internen Knoten ausgewählt wird. Zunächst wird ein Merkmal aus der ", null, 0,
				null);
		stepTwo.addCodeLine("Merkmalsmenge der Wurzel zufällig ausgewählt. Ist dieses auch Teil der Merkmalsmengen",
				null, 0, null);
		stepTwo.addCodeLine(
				"der Kindsknoten der Wurzel, so werden auch die Kindsknoten mit diesem Merkmal beschriftet.", null, 0,
				null);
		stepTwo.addCodeLine("Andernfalls wird wieder ein Merkmal zufällig ausgewählt. Dies wird solange wiederholt,",
				null, 0, null);
		stepTwo.addCodeLine("bis für jeden Knoten genau ein Merkmal ausgewählt ist. Hierdurch erhählt man einen", null,
				0, null);
		stepTwo.addCodeLine("Baum mit minimaler Mutationsanzahl.", null, 0, null);
		language.nextStep("");
		// ------------------------------------------------------------------------------------------------------------
		buildGraph(inputLabel);
	}

	private void buildGraph(String[] inputLabel) {

		// build all variables for the graph
		String[] labels = buildLabels(inputLabel);
		int[][] graphAdjacencyMatrix = buildAdjacencyMatrix(labels.length);
		TreeNodeFitch tree = constructTree(graphAdjacencyMatrix, labels, 0);
		graphNodes = new Node[labels.length];
		buildNodes(tree, 600, 350, 250);

		// build graph
		language.hideAllPrimitives();
		title.show();
		tRect.show();
		Text phase = language.newText(new Offset(20, 40, "title", AnimalScript.DIRECTION_SW), "Eingabe", "phase", null,
				subHeaderProps);
		status = language.newText(new Offset(600, 20, "title", AnimalScript.DIRECTION_SW),
				"[STATUS] Gegeben ist eine feste Baumtopologie mit den Eingabesequenzen als Blätter.", "status", null,
				textProps);
		graph = language.newGraph("graph", graphAdjacencyMatrix, graphNodes, labels, null, graphProps);

		// source code
		sourceCode = language.newSourceCode(new Offset(100, -60, "graph", AnimalScript.DIRECTION_NE), "sourceCode",
				null, sourceCodeProps);
		sourceCode.addCodeLine("jeder Knoten v im Baum hat eine Menge X(v)", null, 0, null);
		sourceCode.addCodeLine("Phase 1:", null, 0, null);
		sourceCode.addCodeLine(
				"\t\t\t durchlaufe den Baum in Postorder (linker Teilbaum -> rechter Teilbaum -> Wurzel)", null, 0,
				null);
		sourceCode.addCodeLine("\t\t\t\t\t\t falls v ein Blatt ist, dann sei X(v) das bei v beobachtete Nucleotid",
				null, 0, null);
		sourceCode.addCodeLine(
				"\t\t\t\t\t\t falls v ein Knoten mit den Kindern u und w ist, dann sei Y = X(u) \u2229 X(w)", null, 0,
				null);
		sourceCode.addCodeLine("\t\t\t\t\t\t\t\t\t falls Y \u2260 \u2205, dann sei X(v) = Y", null, 0, null);
		sourceCode.addCodeLine("\t\t\t\t\t\t\t\t\t falls Y = \u2205, dann sei X(v) = X(u) \u222A X(w)", null, 0, null);
		sourceCode.addCodeLine("Phase 2:", null, 0, null);
		sourceCode.addCodeLine("\t\t\t durchlaufe den Baum in Preorder (Wurzel -> linker Teilbaum -> rechter Teilbaum)",
				null, 0, null);
		sourceCode.addCodeLine(
				"\t\t\t\t\t\t falls v keinen Elternknoten besitzt (Wurzel des Baumes), dann setze label(v) beliebig aus X(v)",
				null, 0, null);
		sourceCode.addCodeLine(
				"\t\t\t\t\t\t falls v ein Knoten mit einem Elternknoten ist, dann sei Y = label(parent(v))", null, 0,
				null);
		sourceCode.addCodeLine("\t\t\t\t\t\t\t\t\t falls Y \u2208 X(v), dann setze label(v) = Y", null, 0, null);
		sourceCode.addCodeLine("\t\t\t\t\t\t\t\t\t sonst setze label(v) beliebig aus X(v)", null, 0, null);
		language.newRect(new Offset(-5, -5, "sourceCode", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "sourceCode", "SE"), "codeRect", null, codeRectProps);
		sourceCode.highlight(0);
		
		TrueFalseQuestionModel bigParsimony = new TrueFalseQuestionModel("bigParsimony", false, 1);
		bigParsimony.setPrompt("Mit Hilfe des Fitch-Algorithmus ist es möglich das große Parsimony-Problem zu lösen.");
		language.addTFQuestion(bigParsimony);
		
		language.nextStep("Eingabe");
		sourceCode.unhighlight(0);
		// ------------------------------------------------------------------------------------------------------------
		sourceCode.highlight(1);
		sourceCode.highlight(2);
		phase.setText("1. Phase: Ermittlung der Merkmalsmengen", null, null);
		status.setText("[STATUS] Durchlaufe den Baum in Postorder", null, null);
		
		FillInBlanksQuestionModel firstNodePostorder = new FillInBlanksQuestionModel("firstNodePostorder");
		firstNodePostorder.setPrompt("Überlege dir welcher Knoten als erstes bei der Postorder betrachtet wird. Wenn du es weißt gebe ja ein, ansonst nein.");
		firstNodePostorder.addAnswer("ja", 1, "linker Teilbaum -> rechter Teilbaum -> Wurzel");
		language.addFIBQuestion(firstNodePostorder);
		
		language.nextStep("1. Phase");
		// ------------------------------------------------------------------------------------------------------------
		fitchStepOne(tree);
		sourceCode.unhighlight(1);
		sourceCode.unhighlight(2);
		status.setText("[STATUS] Phase 1 des Fitch-Algorithmus ist jetzt abgeschlossen.", null, null);
		language.nextStep();
		// ------------------------------------------------------------------------------------------------------------
		sourceCode.highlight(7);
		sourceCode.highlight(8);
		phase.setText("2. Phase: endgültige Beschriftung der inneren Knoten", null, null);
		status.setText("[STATUS] Durchlaufe den Baum in Preorder", null, null);
		
		FillInBlanksQuestionModel firstNodePreorder = new FillInBlanksQuestionModel("firstNodePreorder");
		firstNodePreorder.setPrompt("Überlege dir welcher Knoten als erstes bei der Preorder betrachtet wird. Wenn du es weißt gebe ja ein, ansonsten nein.");
		firstNodePreorder.addAnswer("ja", 1, "Wurzel -> linker Teilbaum -> rechter Teilbaum");
		language.addFIBQuestion(firstNodePreorder);
		
		language.nextStep("2. Phase");
		// ------------------------------------------------------------------------------------------------------------
		fitchStepTwo(tree, null);
		sourceCode.unhighlight(7);
		sourceCode.unhighlight(8);
		status.setText("[STATUS] Phase 2 des Fitch-Algorithmus ist jetzt abgeschlossen.", null, null);
		
		TrueFalseQuestionModel manyReult = new TrueFalseQuestionModel("manyReult", false, 1);
		manyReult.setPrompt("Findet der Algorithmus immer den optimalen Baum?");
		language.addTFQuestion(manyReult);
		
		language.nextStep();
		// ------------------------------------------------------------------------------------------------------------
		conclusion();
	}

	private void fitchStepOne(TreeNodeFitch treeNode) {

		if (treeNode == null)
			return;

		// first recur on left subtree
		fitchStepOne(treeNode.getLeft());

		// then recur on right subtree
		fitchStepOne(treeNode.getRight());

		// node is a leaf
		if (treeNode.getLeft() == null || treeNode.getRight() == null) {
			sourceCode.highlight(3);
			graph.highlightNode(treeNode.getNodeID(), null, null);
			status.setText("[STATUS] Der aktuelle Knoten ist ein Blatt. Es wird X(v) = "
					+ treeNode.getLabel().toString() + " gesetzt.", null, null);
			language.nextStep();
			sourceCode.unhighlight(3);
			graph.unhighlightNode(treeNode.getNodeID(), null, null);
			// ------------------------------------------------------------------------------------------------------------
			// node is a subtree
		} else {
			sourceCode.highlight(4);
			graph.highlightNode(treeNode.getNodeID(), null, null);
			Set<String> intersection = new HashSet<String>(treeNode.getLeft().getLabel());
			intersection.retainAll(treeNode.getRight().getLabel());
			status.setText(
					"[STATUS] Der aktuelle Knoten ist ein Teilbaum. Es wird die Schnittmenge der Labels der Kinder berechnet.",
					null, null);
			
			TrueFalseQuestionModel whatIntersection = new TrueFalseQuestionModel("whatIntersection", intersection.isEmpty(), 1);
			whatIntersection.setPrompt("Ist die Schnittmenge leer?");
			language.addTFQuestion(whatIntersection);
			
			language.nextStep();
			// ------------------------------------------------------------------------------------------------------------
			// X(v) = X(u) union X(w)
			if (intersection.isEmpty()) {
				sourceCode.highlight(6);
				Set<String> union = new HashSet<String>(treeNode.getLeft().getLabel());
				union.addAll(treeNode.getRight().getLabel());
				treeNode.getLabel().addAll(union);
				treeNode.getLabel().addAll(union);
				graph.setNodeLabel(treeNode.getNodeID(), treeNode.getLabel().toString(), null, null);
				status.setText(
						"[STATUS] Da die Schnittmenge leer ist, wird als Label des aktuellen Knoten die Vereinigung der Label der Kinder gesetzt.",
						null, null);
				language.nextStep();
				// ------------------------------------------------------------------------------------------------------------
				sourceCode.unhighlight(4);
				sourceCode.unhighlight(6);
				graph.unhighlightNode(treeNode.getNodeID(), null, null);
				// X(v) = X(u) intersection X(w)
			} else {
				sourceCode.highlight(5);
				treeNode.getLabel().addAll(intersection);
				graph.setNodeLabel(treeNode.getNodeID(), treeNode.getLabel().toString(), null, null);
				status.setText("[STATUS] Die Schnittmenge wird als Label des aktuellen Knotens gesetzt.", null, null);
				language.nextStep();
				// ------------------------------------------------------------------------------------------------------------
				sourceCode.unhighlight(4);
				sourceCode.unhighlight(5);
				graph.unhighlightNode(treeNode.getNodeID(), null, null);
			}
		}
	}

	private void fitchStepTwo(TreeNodeFitch treeNode, Set<String> parentLabel) {

		if (treeNode == null || treeNode.getLeft() == null || treeNode.getRight() == null)
			return;

		// root node
		if (parentLabel == null) {
			sourceCode.highlight(9);
			graph.highlightNode(treeNode.getNodeID(), null, null);
			treeNode.chooseOneLabelRandomly();
			graph.setNodeLabel(treeNode.getNodeID(), treeNode.getLabel().toString(), null, null);
			status.setText(
					"[STATUS] Da es sich bei dem Knoten um die Wurzel handelt, wird zufällig "
							+ treeNode.getLabel().toString() + " gewählt und als endgültiges Label gesetzt.",
					null, null);
			language.nextStep();
			sourceCode.unhighlight(9);
			// ------------------------------------------------------------------------------------------------------------
			graph.unhighlightNode(treeNode.getNodeID(), null, null);
		} else {
			sourceCode.highlight(10);
			graph.highlightNode(treeNode.getNodeID(), null, null);
			status.setText("[STATUS] Da der aktuelle Knoten einen Elternknoten besitzt, wird nun Y = "
					+ parentLabel.toString() + " gesetzt.", null, null);
			language.nextStep();
			// ------------------------------------------------------------------------------------------------------------
			for (String label : parentLabel) {
				if (treeNode.getLabel().contains(label)) {
					sourceCode.highlight(11);
					treeNode.setNewLabel(label);
					graph.setNodeLabel(treeNode.getNodeID(), treeNode.getLabel().toString(), null, null);
					status.setText(
							"[STATUS] Da " + parentLabel.toString()
									+ " ein Element aus der Labelmenge des aktuellen Knotens ist, wird auch "
									+ parentLabel.toString() + " als endgültiges Label für diesen Knoten gesetzt.",
							null, null);
					language.nextStep();
					// ------------------------------------------------------------------------------------------------------------
					graph.unhighlightNode(treeNode.getNodeID(), null, null);
					sourceCode.unhighlight(10);
					sourceCode.unhighlight(11);
					break;
				} else {
					sourceCode.highlight(12);
					treeNode.chooseOneLabelRandomly();
					graph.setNodeLabel(treeNode.getNodeID(), treeNode.getLabel().toString(), null, null);
					status.setText("[STATUS] Da " + parentLabel.toString()
							+ " nicht in der Labelmenge des aktuellen Knotens ist, wird zufällig " + treeNode.getLabel()
							+ " aus der Labelmenge des aktuellen Knotens gewählt und als endgültiges Label gesetzt.",
							null, null);
					language.nextStep();
					// ------------------------------------------------------------------------------------------------------------
					graph.unhighlightNode(treeNode.getNodeID(), null, null);
					sourceCode.unhighlight(10);
					sourceCode.unhighlight(12);
				}
			}
		}

		fitchStepTwo(treeNode.getLeft(), treeNode.getLabel());
		fitchStepTwo(treeNode.getRight(), treeNode.getLabel());
	}

	/**
	 * shows the conclusion site
	 */
	public void conclusion() {

		language.hideAllPrimitives();
		title.show();
		tRect.show();
		language.newText(new Offset(20, 40, "title", AnimalScript.DIRECTION_SW), "Varianten", "variantsHeader", null,
				headerProps);
		SourceCode variants = language.newSourceCode(new Offset(0, 0, "variantsHeader", AnimalScript.DIRECTION_SW),
				"variants", null, sourceCodeProps);
		variants.addCodeLine(
				"Ein weiteres Verfahren zum Lösen des kleinen Parsimony-Problems ist der von D. Sankoff 1975 entwickelte",
				null, 0, null);
		variants.addCodeLine(
				"Algorithmus. Er basiert auf dem gleichen Lösungsansatz wie der hier vorgestellte Fitch-Algorithmus,",
				null, 0, null);
		variants.addCodeLine(
				"bietet jedoch die Möglichkeit, unterschiedliche Mutationswahrscheinlichkeiten für die Basen", null, 0,
				null);
		variants.addCodeLine("einzusetzen.", null, 0, null);
		language.nextStep("Varianten");
		// ------------------------------------------------------------------------------------------------------------
		language.newText(new Offset(0, 40, "variants", AnimalScript.DIRECTION_SW), "Fazit", "conclusionHeader", null,
				headerProps);
		SourceCode conclusion = language.newSourceCode(new Offset(0, 0, "conclusionHeader", AnimalScript.DIRECTION_SW),
				"conclusion", null, sourceCodeProps);
		conclusion.addCodeLine("Durch die zufällige Wahl eines Merkmals aus der Merkmalsmenge in der zweiten Phase",
				null, 0, null);
		conclusion.addCodeLine("des Algorithmus gibt es mehrere Möglichkeiten die internen Knoten korrekt zu", null, 0,
				null);
		conclusion.addCodeLine("beschriften. Dies führt dazu, dass der Algorithmus von Fitch nicht immer", null, 0,
				null);
		conclusion.addCodeLine("den optimalen Baum findet. ", null, 0, null);
		conclusion.addCodeLine("Zudem gehört der Algorithmus zu der speziellen Klasser der Greedy-Algorithmen,", null,
				0, null);
		conclusion.addCodeLine("welche in jedem Schritt den Folgezustand so auswählen, dass der Gewinn maximiert", null,
				0, null);
		conclusion.addCodeLine("wird. Daher erkennt der Algorithmus nicht, dass ein Wechsel des Merkmals", null, 0,
				null);
		conclusion.addCodeLine("sich später auszahlen kann.", null, 0, null);
		conclusion.addCodeLine("Eine Verbesserung bietet hierbei der oben vorgestellt Algorithmus von Sankoff.", null,
				0, null);
		conclusion.addCodeLine("Auch eine Erweiterung des Algorithmus um verschiedene Gewichte für die Änderungen",
				null, 0, null);
		conclusion.addCodeLine("eines Merkmals würde diesen Nachteil ausgleichen.", null, 0, null);
		conclusion.addCodeLine("", null, 0, null);
		language.nextStep("Fazit");
	}

	/**
	 * build the labels for each node to draw the graph
	 * 
	 * @param inputLabel the input label from the animal program
	 */
	private String[] buildLabels(String[] inputLabel) {
		String[] labels = new String[(inputLabel.length * 2) - 1];
		for (int i = 0; i < labels.length; i++) {
			labels[i] = "";
		}
		System.arraycopy(inputLabel, 0, labels, inputLabel.length - 1, inputLabel.length);
		return labels;
	}

	/**
	 * build the symmetric adjacency matrix to draw the graph
	 * 
	 * @param size the size of the symmetric adjacency matrix
	 */
	private int[][] buildAdjacencyMatrix(int size) {
		int[][] matrix = new int[size][size];
		for (int row = 0; row < matrix.length; row++) {
			for (int column = 0; column < matrix.length; column++) {
				if ((((row + 1) * 2) - 1) == column || ((row + 1) * 2) == column) {
					matrix[row][column] = 1;
				} else {
					matrix[row][column] = 0;
				}
			}
		}
		return matrix;
	}

	/**
	 * builds the tree data structure
	 * 
	 * @param matrix  a symmetric adjacency matrix
	 * @param labels  the labels for each node
	 * @param labelID the nodeID
	 */
	private TreeNodeFitch constructTree(int[][] matrix, String[] labels, int labelID) {

		if (matrix == null)
			return null;

		for (int column = 0; column < matrix.length; column++) {

			// found edge
			if (matrix[labelID][column] != 0) {
				return new TreeNodeFitch(labelID, labels[labelID], constructTree(matrix, labels, column),
						constructTree(matrix, labels, column + 1));
			}
		}
		// it is a leaf
		return new TreeNodeFitch(labelID, labels[labelID]);
	}

	/**
	 * builds the coordinates of the nodes in the graph
	 * 
	 * @param treeNode    a tree data structure
	 * @param treeWidth   the maximum width of the tree
	 * @param coordinateX the x coordinate of the root
	 * @param coordinateY the y coordinate of the root
	 */
	public void buildNodes(TreeNodeFitch treeNode, int treeWidth, int coordinateX, int coordinateY) {

		if (treeNode == null)
			return;

		graphNodes[treeNode.getNodeID()] = new Coordinates(coordinateX, coordinateY);
		buildNodes(treeNode.getLeft(), treeWidth / 2, coordinateX - (treeWidth / 4), coordinateY + 50);
		buildNodes(treeNode.getRight(), treeWidth / 2, coordinateX + (treeWidth / 4), coordinateY + 50);
	}

	public static void main(String[] args) {
		// generate generator
		Generator generator = new Fitch();

		// start Animal with generator
		Animal.startGeneratorWindow(generator);
	}

	public void init() {
		// Create a new language object for generating animation code
		// this requires type, name, author, screen width, screen height
		language = new AnimalScript("Fitch-Algorithmus", "Peter Reinhardt", 1024, 768);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		language.setStepMode(true);
		language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		// properties
		textProps = (TextProperties) props.getPropertiesByName("textProps");
		codeRectProps = (RectProperties) props.getPropertiesByName("codeRectProps");
		headerProps = (TextProperties) props.getPropertiesByName("headertextProps");
		sourceCodeProps = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProps");
		titleRectProps = (RectProperties) props.getPropertiesByName("titleRectProps");
		subHeaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 17));
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
		graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(151, 255, 255));
		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);

		String[] inputLabel = (String[]) primitives.get("sequence");

		start(inputLabel);
		language.finalizeGeneration();

		return language.toString();
	}

	public String getName() {
		return "Fitch-Algorithmus";
	}

	public String getAlgorithmName() {
		return "Fitch-Algorithmus";
	}

	public String getAnimationAuthor() {
		return "Peter Reinhardt";
	}

	public String getDescription() {
		return "Der Fitch-Algorithmus ist ein Algorithmus aus der Algorithmischen Bioinformatik." + "\n"
				+ "Mit Hilfe des Verfahrens ist es möglich das kleine Parsimony-Problem zu lösen." + "\n"
				+ "Als Eingabe für den Algorithmus dient eine Menge an Nucleotid- oder Aminosäuresequenzen, deren Verwandtschaftssverhätnis zueinander untersucht werden soll. Hierfür wird eine festgelegte Baumtopologie verwendet und die Sequenzen an die Blätter des Baumes geschrieben."
				+ "\n"
				+ "Anschließend werden dann neue Sequenzen für die internen Knoten des Baumes berechnet, so daß die Summe der Mutationen zwischen den Sequenzen minimal ist und die beobachteten Sequenzunterschiede der Eingabesequenzen erklärt werden.";
	}

	public String getCodeExample() {
		return "jeder Knoten v im Baum hat eine Menge X(v)" + "\n" + "Phase 1:" + "\n"
				+ "durchlaufe den Baum in Postorder (linker Teilbaum -> rechter Teilbaum -> Wurzel)" + "\n"
				+ "	falls v ein Blatt ist, dann sei X(v) das bei v beobachtete Nucleotid" + "\n"
				+ "	falls v ein Knoten mit den Kindern u und w ist, dann sei Y = X(u) \u2229 X(w)" + "\n"
				+ "		falls Y \u2260 \u2205, dann sei X(v) = Y" + "\n"
				+ "		falls Y = \u2205, dann sei X(v) = X(u) \u222A X(w)" + "\n" + "Phase 2:" + "\n"
				+ "durchlaufe den Baum in Preorder (Wurzel -> linker Teilbaum -> rechter Teilbaum)" + "\n"
				+ "	falls v keinen Elternknoten besitzt (Wurzel des Baumes), dann setze label(v) beliebig aus X(v)"
				+ "\n" + "	falls v ein Knoten mit einem Elternknoten ist, dann sei Y = label(parent(v))" + "\n"
				+ "		falls Y \u2208 X(v), dann setze label(v) = Y" + "\n"
				+ "		sonst setze label(v) beliebig aus X(v)";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}
}
