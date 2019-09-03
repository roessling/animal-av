package generators.compression.huffman2.HuffmanCoding;
 
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Map.Entry;

import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;
import generators.compression.huffman2.Node.TreeNode;
import generators.compression.huffman2.custom.AscendingComparator;
import interactionsupport.models.AnswerModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;

/**
 * The Class HuffmanCodingAnimal.
 */
public class HuffmanCodingAnimal {

	/**
	 * The Class GraphModel.
	 */
	private class GraphModel {

		/** The adjacency matrix. */
		int[][] adjacencyMatrix;

		/** The nodes. */
		List<Node> nodes;

		/** The label. */
		List<String> label;

		/** The nodes to hide. */
		List<Integer> nodesToHide;
	}

	/** The lang. */
	private Language lang;

	/** The final tree. */
	private TreeNode finalTree;

	/** The graph. */
	private Graph graph;

	/** The map node to ID. */
	private HashMap<TreeNode, Integer> mapNodeToID;

	/** The sc. */
	private SourceCode sc;

	/** The graph highlightcolor. */
	private Color graphHighlightcolor;

	/** The source highlightcolor. */
	private Color sourceHighlightcolor;

	private int pqArray_remove = 0;
	private int pqArray_add = 0;

	/**
	 * Instantiates a new huffman coding animal.
	 *
	 * @param l
	 *            the l
	 * @param tree
	 *            the tree
	 */
	public HuffmanCodingAnimal(Language l, Color graphHighlightcolor, Color sourceHighlightcolor, TreeNode tree) {
		this.finalTree = tree;
		this.lang = l;
		lang.setStepMode(true);
		mapNodeToID = new HashMap<>();
		this.sourceHighlightcolor = sourceHighlightcolor;
		this.graphHighlightcolor = graphHighlightcolor;
	}

	/**
	 * Compress.
	 *
	 * @param text
	 *            the text
	 */
	public void compress(String text) {
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		TextProperties headtp = new TextProperties();
		headtp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		Font font = new Font("Serif", Font.BOLD, 18);
		headtp.set(AnimationPropertiesKeys.FONT_PROPERTY, font);

		Text t1_1 = lang.newText(new Coordinates(40, 50), "Huffman Codierung.", "heading", null, headtp);

		Text t1_2 = lang.newText(new Coordinates(40, 80), "Eingabetext:", "heading", null, tp);

		Text t2 = lang.newText(new Coordinates(40, 110), text, "InputText", null, tp);
		lang.nextStep("Einleitung");

		String s = "Beschreibung des Algorithmus: #Die Huffman-Kodierung ist eine Form der Entropiekodierung, die 1952 von David A. Huffman entwickelt und in der Abhandlung 'A Method for the Construction of Minimum-Redundancy Codes' publiziert wurde. #Sie ordnet einer festen Anzahl an Quellsymbolen jeweils Codewörter mit variabler Länge zu. In der Informationstechnik ist sie ein Präfix-Code, die üblicherweise für verlustfreie Kompression benutzt wird. #Ähnlich anderer Entropiekodierungen werden häufiger auftauchende Zeichen mit weniger Bits repräsentiert als seltener auftauchende. ##Die Grundidee ist, einen k-nären Wurzelbaum (ein Baum mit jeweils k Kindern je Knoten) für die Darstellung des Codes zu verwenden. In diesem sog. #Huffman-Baum stehen die Blätter für die zu kodierenden Zeichen, während der Pfad von der Wurzel zum Blatt das Codesymbol bestimmt. ##Der bei der Huffman-Kodierung gewonnene Baum liefert garantiert eine optimale und präfixfreie Kodierung. #D. h. es existiert kein symbolbezogenes Kodierverfahren, das einen kürzeren Code generieren könnte, wenn die Auftrittswahrscheinlichkeiten der Symbole bekannt sind.";
		List<Text> desc_texts = new ArrayList<>();
		String[] lines = s.split("#", -1);
		int y_offset = 160;
		int y_stride = 40;
		for (int i = 0; i < lines.length; i++) {
			Text t = lang.newText(new Coordinates(40, y_offset + i * y_stride), lines[i], "wikipedia" + i, null, tp);
			desc_texts.add(t);
		}

		lang.nextStep();
		for (Text t : desc_texts) {
			t.hide();
		}
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));

		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, sourceHighlightcolor);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		// now, create the source code entity
		sc = lang.newSourceCode(new Coordinates(40, 140), "sourceCode", null, scProps);

		// TODO beschreibung huffman coding sinn & funktionalität

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		sc.addCodeLine("Q := PriorityQueueAscending(input);     // characters with frequencies", null, 0, null); // 0
		sc.addCodeLine("while |Q| > 1                           // execute while multiple elements remain in queue",
				null, 0, null);
		sc.addCodeLine("z := Node();                          // create new node", null, 1, null);
		sc.addCodeLine("z.left := Extract-Min(Q);             // extract node with minimum frequency", null, 1, null); // 3
		sc.addCodeLine("z.right := Extract-Min(Q);            // extract node with minimum frequency", null, 1, null); // 4
		sc.addCodeLine("z.freq := z.left.freq + z.right.freq; // calculate frequency", null, 1, null); // 5
		sc.addCodeLine("Insert(Q, z);                         // add the new node to the priority queue", null, 1,
				null); // 6
		sc.addCodeLine("end while", null, 0, null); // 7
		sc.addCodeLine("return Extract-Min(Q);                  // return root", null, 0, null); // 8
		lang.nextStep();

		char[] input = text.toCharArray();

		// tabulate all frequencies
		HashMap<Character, Integer> freq = new HashMap<>();
		for (int i = 0; i < input.length; i++) {
			freq.put(input[i], 0);
		}
		for (int i = 0; i < input.length; i++) {
			freq.put(input[i], freq.get(input[i]) + 1);
		}
		sc.highlight(0);

		// create the tree
		TreeNode root = buildTree(freq);

		sc.hide();
		graph.hide();
		for (int i = 0; i < graph.getSize(); i++) {
			graph.hideNode(i, null, null);
		}
		String s_e = "Der Algorithmus hat einen Baum mit " + TreeNode.countNodes(root)
				+ " Knoten erzeugt. #Der zuvor erwähnte Baum hat den Namen Huffman-Baum. Aus diesem wird ein Code-Wörterbuch zur Kodierung des Textes erstellt. #Dies geschieht, indem man von unten beginnend jede Verzweigung nach links wird mit einer '0', jede Verzweigung nach rechts mit einer '1' liest. #Anschließend kann man aus dem Huffman-Baum den Code von jedem Zeichen erfassen und ein Code-Wörterbuch erstellen. #Häufig vorkommende Buchstaben, erhalten hierbei einen kurzen Code. #Die Visualisierung des Wörterbuches ist nicht Teil unserer Animation. # # Während der Ausführung des Algorithmus zur Baumerstellung, wurden die Anzahl der hinzugefügten sowie entfernten Element in der PriorityQueue gezählt. Es wurde hierbei "
				+ pqArray_add + " mal ein Element hinzugefügt. Es wurde " + pqArray_remove
				+ " mal ein Element entfernt.";

		String[] lines_e = s_e.split("#", -1);
		int y_offset_e = 160;
		int y_stride_e = 25;
		for (int i = 0; i < lines_e.length; i++) {
			lang.newText(new Coordinates(40, y_offset_e + i * y_stride_e), lines_e[i], "wikipedia" + i, null, tp);
		}
		lang.finalizeGeneration();
	}

	/**
	 * Builds the tree.
	 *
	 * @param freq
	 *            the freq
	 * @return the tree node
	 */
	private TreeNode buildTree(HashMap<Character, Integer> freq) {

		// initialize priority queue with singleton trees
		PriorityQueue<TreeNode> pq = new PriorityQueue<TreeNode>(new AscendingComparator());

		for (Entry<Character, Integer> e : freq.entrySet()) {
			pq.add(new TreeNode(e.getKey().toString(), e.getValue().intValue(), null, null, null));
			pqArray_add++;
		}
		sc.unhighlight(0);
		sc.highlight(1);

		// merge two smallest trees
		TreeNode[] nodes = pq.toArray(new TreeNode[0]);
		Arrays.sort(nodes, new AscendingComparator());
		generateGraph(finalTree);

		ArrayProperties ap = new ArrayProperties();
		ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, graphHighlightcolor);
		Coordinates pqCoordinates = new Coordinates(800, 170);

		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		Text pqText = lang.newText(new Coordinates(800, 140), "PriorityQueue:", "pqText", null, tp);

		final List<String> nodesPQ_before = new ArrayList<>();
		pq.stream().forEach(tn -> nodesPQ_before.add(tn.label));
		StringArray pqArray_before = lang.newStringArray(new Coordinates(0, 0), nodesPQ_before.toArray(new String[0]),
				"array", null, ap);

		pqArray_before.moveTo(null, null, pqCoordinates, null, null);
		graph.show();
		lang.nextStep("Initialisierung");
		pqArray_before.hide();
		int iteration = 0;
		int modelID = 0;
		QuestionGroupModel qgm = new QuestionGroupModel("model1", 2);
		while (pq.size() > 1) {

			// view pq
			final List<String> nodesPQ = new ArrayList<>();
			pq.stream().forEach(tn -> nodesPQ.add(tn.label));
			StringArray pqArray = lang.newStringArray(new Coordinates(0, 0), nodesPQ.toArray(new String[0]), "array",
					null, ap);
			pqArray.moveTo(null, null, pqCoordinates, null, null);

			TreeNode algoLeftChild = pq.poll();
			TreeNode algoRightChild = pq.poll();
			pqArray_remove = pqArray_remove + 2;

			int graphLeftID = getGraphNodeID(algoLeftChild);
			int graphRightID = getGraphNodeID(algoRightChild);
			graph.highlightNode(graphLeftID, null, null);
			graph.highlightNode(graphRightID, null, null);
			pqArray.highlightCell(0, null, null);
			pqArray.highlightCell(1, null, null);
			sc.highlight(2);
			sc.highlight(3);
			sc.highlight(4);

			double frequency = algoLeftChild.frequency + algoRightChild.frequency;

			// Frage welche frequenz
			MultipleChoiceQuestionModel mcModel = new MultipleChoiceQuestionModel(modelID + "");
			mcModel.setPrompt("Welche frequency erh�lt der neue Knoten?");
			mcModel.addAnswer("" + frequency, 1, "Richtig, die neue frequency ist " + ((int) frequency) + ".");
			mcModel.addAnswer("" + (frequency + 1), 0, "Falsch, die neue frequency ist " + ((int) frequency) + ".");
			mcModel.addAnswer("" + (frequency - 1), 0, "Falsch, die neue frequency ist " + ((int) frequency) + ".");
			mcModel.setGroupID(qgm.getID());
			lang.addMCQuestion(mcModel);
			modelID++;

			lang.nextStep(++iteration + " Iteration");
			int parentID = getGraphParentID(graphLeftID);

			translateSubTree(graph, graphLeftID);
			translateSubTree(graph, graphRightID);

			graph.showNode(parentID, null, null);
			graph.unhighlightNode(graphLeftID, null, null);
			graph.unhighlightNode(graphRightID, null, null);
			sc.unhighlight(2);
			sc.unhighlight(3);
			sc.unhighlight(4);
			sc.highlight(5);
			sc.highlight(6);

			TreeNode parent = new TreeNode(algoLeftChild.label + "-" + algoRightChild.label, frequency, null,
					algoLeftChild, algoRightChild);
			algoLeftChild.parent = parent;
			algoRightChild.parent = parent;

			pq.add(parent);
			pqArray_add++;
			nodesPQ.clear();
			pq.stream().forEach(tn -> nodesPQ.add(tn.label));
			pqArray.hide();

			nodesPQ.clear();
			pq.stream().forEach(tn -> nodesPQ.add(tn.label));
			StringArray pqArray2 = lang.newStringArray(new Coordinates(0, 0), nodesPQ.toArray(new String[0]), "array",
					null, ap);
			pqArray2.moveTo(null, null, pqCoordinates, null, null);

			lang.nextStep();

			sc.unhighlight(5);
			sc.unhighlight(6);
			pqArray2.hide();
		}

		final List<String> nodesPQ_after = new ArrayList<>();
		pq.stream().forEach(tn -> nodesPQ_after.add(tn.label));
		StringArray pqArray_after = lang.newStringArray(new Coordinates(0, 0), nodesPQ_after.toArray(new String[0]),
				"array", null, ap);
		pqArray_after.moveTo(null, null, pqCoordinates, null, null);

		TreeNode algoRoot = pq.poll();
		pqArray_remove++;

		int rootID = getGraphNodeID(algoRoot);

		sc.unhighlight(1);
		sc.highlight(7);

		lang.nextStep("Ergebnisrückgabe");
		sc.unhighlight(7);
		sc.highlight(8);
		graph.highlightNode(rootID, null, null);
		pqArray_after.highlightCell(0, null, null);
		lang.nextStep("Fazit");
		sc.unhighlight(8);
		graph.unhighlightNode(rootID, null, null);
		pqArray_after.hide();
		pqText.hide();

		int nodeCount = TreeNode.countNodes(algoRoot);
		MultipleChoiceQuestionModel mcModel = new MultipleChoiceQuestionModel(modelID + "");
		if (nodeCount % 2 == 0) {
			mcModel.setPrompt("In welchem Jahr wurde der Algorithmus erfunden?");
			mcModel.addAnswer("1952", 1, "Richtig, er wurde im Jahr 1952 erfunden.");
			mcModel.addAnswer("1949", 0, "Falsch, er wurde im Jahr 1952 erfunden.");
			mcModel.addAnswer("1955", 0, "Falsch, er wurde im Jahr 1952 erfunden.");
			mcModel.setGroupID(qgm.getID());
		} else {
			mcModel.setPrompt("Wof�r wird der Huffman-Code verwendet?");
			mcModel.addAnswer("Verschl�sselung", 0, "Falsch, er wird zur Kodierung und damit zur Kompression verwendet.");
			mcModel.addAnswer("Kodierung", 1, "Richtig,  er wird zur Kodierung und damit zur Kompression verwendet.");
			mcModel.addAnswer("Signierung", 0, "Falsch, er wird zur Kodierung und damit zur Kompression verwendet.");
			mcModel.setGroupID(qgm.getID());
		}

		lang.addMCQuestion(mcModel);
		modelID++;

		return algoRoot;
	}

	/**
	 * Gets the child from final tree by label.
	 *
	 * @param node
	 *            the node
	 * @param label
	 *            the label
	 * @return the child from final tree by label
	 */
	private TreeNode getChildFromFinalTreeByLabel(TreeNode node, String label) {
		if (label.equals(node.label)) {
			return node;
		} else {
			if (node.isLeaf()) {
				return null;
			}
			TreeNode dummy = getChildFromFinalTreeByLabel(node.rightChild, label);
			if (dummy == null) {
				dummy = getChildFromFinalTreeByLabel(node.leftChild, label);
			}
			return dummy;
		}
	}

	/**
	 * Gets the tree node from final tree.
	 *
	 * @param algorithmNode
	 *            the algorithm node
	 * @return the tree node from final tree
	 */
	private TreeNode getTreeNodeFromFinalTree(TreeNode algorithmNode) {
		TreeNode currentNode = algorithmNode;
		int h = 0;
		while (!currentNode.isLeaf()) {
			currentNode = currentNode.rightChild;
			h = h + 1;
		}

		TreeNode searchedNode = getChildFromFinalTreeByLabel(finalTree, currentNode.label);
		for (int i = 0; i < h; i++) {
			searchedNode = searchedNode.parent;
		}
		return searchedNode;

	}

	/**
	 * Gets the graph node ID.
	 *
	 * @param algorithmNode
	 *            the algorithm node
	 * @return the graph node ID
	 */
	private int getGraphNodeID(TreeNode algorithmNode) {
		return mapNodeToID.get(getTreeNodeFromFinalTree(algorithmNode));
	}

	/**
	 * Gets the nodefor ID from final tree.
	 *
	 * @param nodeID
	 *            the node ID
	 * @return the nodefor ID from final tree
	 */
	private TreeNode getNodeforIDFromFinalTree(int nodeID) {
		for (Entry<TreeNode, Integer> e : mapNodeToID.entrySet()) {
			if (e.getValue() == nodeID) {
				return e.getKey();
			}
		}
		throw new IllegalArgumentException("should not happen!");
	}

	/**
	 * Gets the graph parent ID.
	 *
	 * @param nodeID
	 *            the node ID
	 * @return the graph parent ID
	 */
	private int getGraphParentID(int nodeID) {
		return mapNodeToID.get(getNodeforIDFromFinalTree(nodeID).parent);
	}

	/** The y stride. */
	final int y_stride = 60;

	/**
	 * Translate sub tree.
	 *
	 * @param graph
	 *            the graph
	 * @param nodeID
	 *            the node ID
	 */
	private void translateSubTree(Graph graph, int nodeID) {
		List<TreeNode> nodes = TreeNode.getAllNodesFromTree(getNodeforIDFromFinalTree(nodeID));
		int[] IDs = new int[nodes.size()];
		int i = 0;
		for (TreeNode node : nodes) {
			int currentNodeID = mapNodeToID.get(node);
			IDs[i] = currentNodeID + 1;
			i++;

		}
		// Timing effect
		graph.translateNodes(IDs,
				new Offset(-((Coordinates) graph.getNode(nodeID)).getX(), y_stride, graph.getNode(nodeID), null), null,
				Timing.MEDIUM);

	}

	/**
	 * Generate graph.
	 *
	 * @param tree
	 *            the tree
	 */
	private void generateGraph(TreeNode tree) {
		int totalNodeCount = TreeNode.countNodes(tree);

		GraphModel model = new GraphModel();
		model.adjacencyMatrix = new int[totalNodeCount][totalNodeCount];
		model.nodes = new ArrayList<>();
		model.label = new ArrayList<>();
		model.nodesToHide = new ArrayList<>();

		internalGraphCreation(tree, model);

		GraphProperties graphProps = new GraphProperties();
		// font color
		graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);
		graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, graphHighlightcolor);

		graph = lang.newGraph("graph", model.adjacencyMatrix, model.nodes.toArray(new Node[0]),
				model.label.toArray(new String[0]), null, graphProps);

		for (int node : model.nodesToHide) {
			graph.hideNode(node, null, null);
		}

	}

	/** The x start. */
	final int x_start = 20;

	/** The x offset. */
	final int x_offset = 50;

	/** The y start. */
	final int y_start = 400;

	/**
	 * Internal graph creation.
	 *
	 * @param currentNode
	 *            the current node
	 * @param model
	 *            the model
	 */
	private void internalGraphCreation(TreeNode currentNode, GraphModel model) {

		if (currentNode.leftChild != null) {
			internalGraphCreation(currentNode.leftChild, model);
		}

		mapNodeToID.put(currentNode, model.nodes.size());
		model.nodes.add(new Coordinates(x_start + x_offset * model.nodes.size(), y_start));

		if (currentNode.isLeaf()) {
			model.label.add(currentNode.label + " [" + currentNode.frequency + "]");
		} else {
			model.label.add("[" + currentNode.frequency + "]");
			model.nodesToHide.add(model.nodes.size() - 1);
		}

		if (currentNode.rightChild != null) {
			internalGraphCreation(currentNode.rightChild, model);
		}

		if (currentNode.rightChild != null) {
			int myID = mapNodeToID.get(currentNode);
			int childID = mapNodeToID.get(currentNode.rightChild);
			model.adjacencyMatrix[childID][myID] = 1;
		}

		if (currentNode.leftChild != null) {
			int myID = mapNodeToID.get(currentNode);
			int childID = mapNodeToID.get(currentNode.leftChild);
			model.adjacencyMatrix[childID][myID] = 1;
		}
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		// Create a new language object for generating animation code
		// this requires type, name, author, screen width, screen height
		Language l = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "Huffman Coding",
				"Jonathan Roth, Till Voß", 640, 480);

		// @Sebastian: this input parameter can be changed
		String text = "MISSISSIPPI";

		HuffmanModel model = HuffmanCoding.compress(text);
		HuffmanCodingAnimal hca = new HuffmanCodingAnimal(l, Color.YELLOW, Color.RED, model.tree);

		hca.compress(text);
		System.out.println(l);
	}
}
