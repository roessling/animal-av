package generators.compression.tunstall.TunstallCoding;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;
import generators.compression.tunstall.Node.TreeNode;
import generators.compression.tunstall.custom.DescendingComparator;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;

public class TunstallCodingAnimal {

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

	private int dictionarySize;

	public TunstallCodingAnimal(Language l, Color graphHighlightcolor, Color sourceHighlightcolor, TreeNode tree,
			int dictionarySize) {
		this.finalTree = tree;
		this.lang = l;
		lang.setStepMode(true);
		mapNodeToID = new HashMap<>();
		this.sourceHighlightcolor = sourceHighlightcolor;
		this.graphHighlightcolor = graphHighlightcolor;
		this.dictionarySize = dictionarySize;
	}

	public TreeNode buildTree(String text) {
		generateGraph(finalTree);
		int textLength = text.length();
		char[] input = text.toCharArray();
		Map<Character, Integer> frequencies = new HashMap<>();
		for (char c : input) {
			frequencies.put(c, 0);
		}
		for (char c : input) {
			frequencies.put(c, frequencies.get(c) + 1);
		}
		if (dictionarySize < frequencies.size()) {
			throw new IllegalArgumentException("Dictionary size insufficient.");
		}

		TreeNode root = new TreeNode("", 1, null, null);
		PriorityQueue<TreeNode> leafs = new PriorityQueue<>(new DescendingComparator());

		for (Entry<Character, Integer> e : frequencies.entrySet()) {
			TreeNode leaf = new TreeNode(e.getKey().toString(), ((double) e.getValue()) / ((double) textLength), root,
					null);
			root.children.add(leaf);
			leafs.add(leaf);
		}
		sc.highlight(0);
		int rootID = getGraphNodeID(root);
		graph.showNode(rootID, null, null);
		for (TreeNode child : root.children) {
			int childID = getGraphNodeID(child);
			translateSubTree(graph, childID);
			graph.showNode(childID, null, null);
		}
		lang.nextStep("Initialisierung");
		sc.unhighlight(0);
		sc.highlight(1);
		lang.nextStep();
		int iteration = 0;
		int modelID = 0;
		QuestionGroupModel qgm = new QuestionGroupModel("model1", 2);
		qgm.setNumberOfRepeats(2);
		while (leafs.size() < dictionarySize) {
			TreeNode node = leafs.poll();
			sc.highlight(2);
			int nodeID = getGraphNodeID(node);
			graph.highlightNode(nodeID, null, null);

			lang.nextStep(++iteration + " Iteration");
			sc.unhighlight(2);
			sc.highlight(3);
			lang.nextStep();
			int counter = 0;
			for (Entry<Character, Integer> e : frequencies.entrySet()) {
				double freq = (((double) e.getValue()) / ((double) textLength)) * node.frequency;
				TreeNode leaf = new TreeNode(node.label + e.getKey().toString(), freq, node, null);
				node.children.add(leaf);
				leafs.add(leaf);

				// Frage welche frequenz
				MultipleChoiceQuestionModel mcModel = new MultipleChoiceQuestionModel(modelID + "");
				mcModel.setPrompt(
						"Welche frequency hat der Knoten '" + leaf.label + "', welcher als n�chstes hinzugef�gt wird?");
				mcModel.addAnswer(String.format(Locale.US, "%.2f", leaf.frequency), 1,
						"Richtig, die neue frequency ist " + String.format(Locale.US, "%.2f", leaf.frequency));

				mcModel.addAnswer("" + String.format(Locale.US, "%.2f", leaf.frequency + 0.1f), 0,
						"Falsch, die neue frequency ist " + String.format(Locale.US, "%.2f", leaf.frequency));

				mcModel.addAnswer("" + String.format(Locale.US, "%.2f", leaf.frequency - 0.1f), 0,
						"Falsch, die neue frequency ist " + String.format(Locale.US, "%.2f", leaf.frequency));
				mcModel.setGroupID(qgm.getID());
				boolean showQ = true;
				if (counter > 2) {
					double rnd = Math.random();
					System.out.println(rnd);
					if ((rnd * 100.0f) > 20) {
						
						showQ = false;
					}
				}
				if (showQ) {
					lang.addMCQuestion(mcModel);
					modelID++;
					counter++;
				}
				lang.nextStep();
				sc.highlight(4);
				sc.highlight(5);
				sc.highlight(6);
				int leafID = getGraphNodeID(leaf);
				translateSubTree(graph, leafID);
				graph.showNode(leafID, null, null);
				graph.highlightNode(leafID, null, null);
				lang.nextStep();
				sc.unhighlight(4);
				sc.unhighlight(5);
				sc.unhighlight(6);
				graph.unhighlightNode(leafID, null, null);
				lang.nextStep();
			}
			sc.highlight(7);
			lang.nextStep();
			graph.unhighlightNode(nodeID, null, null);
			sc.unhighlight(3);
			sc.unhighlight(7);

		}
		sc.highlight(8);
		lang.nextStep();
		sc.unhighlight(1);
		sc.unhighlight(8);
		sc.highlight(9);
		lang.nextStep("Ergebnisrückgabe");
		sc.unhighlight(9);
		return root;
	}

	public int buildDictionaryInternal(Map<String, Integer> dictionary, TreeNode node, int number) {
		if (!node.isLeaf()) {
			int myNumber = number;
			for (int i = 0; i < node.children.size(); i++) {
				myNumber = buildDictionaryInternal(dictionary, node.children.get(i), myNumber);
			}
			return myNumber;
		} else {
			dictionary.put(node.label, number);
			return number + 1;
		}
	}

	public Map<String, Integer> buildDictionary(TreeNode root) {
		Map<String, Integer> dictionary = new HashMap<>();
		buildDictionaryInternal(dictionary, root, 0);
		return dictionary;
	}

	public TunstallModel compress(String text) {
		// TODO validating generator
		// if (text == null || text.isEmpty()) {
		// throw new IllegalArgumentException("text size is insufficient");
		// }
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		TextProperties headtp = new TextProperties();
		headtp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		Font font = new Font("Serif", Font.BOLD, 18);
		headtp.set(AnimationPropertiesKeys.FONT_PROPERTY, font);

		Text t1_1 = lang.newText(new Coordinates(40, 50), "Tunstall Codierung.", "heading", null, headtp);
		Text t1_2 = lang.newText(new Coordinates(40, 80), "Eingabetext:", "heading", null, tp);

		Text t2 = lang.newText(new Coordinates(200, 80), text, "InputText", null, tp);

		Text t3_1 = lang.newText(new Coordinates(40, 100), "Kapazität:", "heading", null, tp);

		Text t3_2 = lang.newText(new Coordinates(200, 100), dictionarySize + "", "capacity", null, tp);

		lang.nextStep("Einleitung");

		String s = "Die Tunstall-Kodierung ist eine Form der verlustfreien Datenkompression und Entropiekodierung, # die 1967 von Brian Parker Tunstall in seiner Doktorarbeit am Georgia Institute of Technology entwickelt wurde. # Im Gegensatz zu ähnlichen Verfahren wie der Huffman-Kodierung ordnet die Tunstall-Kodierung einem # Quellensymbol mit variabler Länge ein Codesymbol mit einer fixen Anzahl von Bits (Stellen) zu # (Quelle: https://de.wikipedia.org/wiki/Tunstall-Kodierung). # # Der Ablauf des Algorithm wird nachfolgend beschrieben. # Zunächst wird ein Baum mit allen im Ursprungstext vorkommenden Zeichen gebildet. # Die neu erzeugten Konten enthalten jeweils auch die Häufigkeit der Buchstaben. # Anschlie�end wird in jeder Iteration des Algorithmus das Blatt mit der höchsten frequency ausgewählt und es wird für jeden Buchstaben ein Knoten als Blatt unter dem alten Knoten erzeugt. # Dies wird wiederholt bis die als Parameter definierte maximale Grö�e des Codewörterbuchs erreicht ist.";
		List<Text> desc_texts = new ArrayList<>();
		String[] lines = s.split("#", -1);
		int y_offset = 160;
		int y_stride = 25;
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

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		sc.addCodeLine(
				"T := tree with |U| leaves                 // one leaf for each letter in the alphabet U, b.frequency=|l|/|U|",
				null, 0, null); // 0
		sc.addCodeLine("while |T| < C                             // C is the maximal capacity of the dictionary", null, 0,
				null); // 1
		sc.addCodeLine("z := GetMostProbableLeaf(T);", null, 1, null); // 2
		sc.addCodeLine("for(Letter l : U)                       // convert most probable leaf to tree with |U| leaves", null,
				1, null); // 3
		sc.addCodeLine("n := Node(l);", null, 2, null); // 4
		sc.addCodeLine("n.freuquency= z.frequency * |l|/|U|     // calculate the frequency", null, 1, null); // 5
		sc.addCodeLine("AddChild(z, n)                        // add the new node as child;", null, 2, null); // 6
		sc.addCodeLine("end for", null, 1, null); // 7
		sc.addCodeLine("end while", null, 0, null); // 8
		sc.addCodeLine("return T                                 // return the tree", null, 0, null); // 9
		lang.nextStep();

		// create the tree
		TreeNode tree = buildTree(text);

		Map<String, Integer> dictionary = buildDictionary(tree);

		int binaryLength = Integer.toBinaryString(dictionary.size() - 1).length();

		Map<String, String> binaryDictionary = new HashMap<>();
		for (Entry<String, Integer> e : dictionary.entrySet()) {
			String binaryS = Integer.toBinaryString(e.getValue());
			for (int i = binaryS.length(); i < binaryLength; i++) {
				binaryS = "0" + binaryS;
			}
			binaryDictionary.put(e.getKey(), binaryS);
		}

		StringBuilder sb = new StringBuilder();
		String c = "";
		for (int i = 0; i < text.length(); i++) {
			c = c + text.charAt(i);
			if (binaryDictionary.containsKey(c)) {
				sb.append(binaryDictionary.get(c));
				c = "";
			}
		}

		TunstallModel model = new TunstallModel();
		model.text = text;
		model.tree = tree;
		model.lookupTable = binaryDictionary;
		model.compressed = sb.toString();

		sc.hide();
		graph.hide();
		for (int i = 0; i < graph.getSize(); i++) {
			graph.hideNode(i, null, null);
		}
		String s_e = "Der Algorithmus hat einen Baum mit " + TreeNode.countNodes(tree)
				+ " Knoten erzeugt. Von diesen Knoten sind " + TreeNode.countLeafs(tree)
				+ " Blätter. #Der zuvor erwähnte Baum hat den Namen Suchbaum. Aus diesem wird ein Code-Wörterbuch zur Kodierung des Textes erstellt. # Um die Anzahl der Bits, welche für die Kodierung benötigt werden, zu berechnen, wird die Funktion ceil(log2(|leafs|)) verwendet. # Die jeweilige Zuweisung der Code-Wörter an ein Blatt kann beliebig erfolgen. # Mithilfe der zugewiesenen Code-Wörter kann der Eingabetext anschlie�end kodiert werden. Für die Dekodierung muss das Code-Wörterbuch mit übertragen werden.";

		String[] lines_e = s_e.split("#", -1);
		int y_offset_e = 160;
		int y_stride_e = 25;
		for (int i = 0; i < lines_e.length; i++) {
			lang.newText(new Coordinates(40, y_offset_e + i * y_stride_e), lines_e[i], "wikipedia" + i, null, tp);
		}
		lang.nextStep("Fazit");

		int nodeCount = TreeNode.countLeafs(tree);
		MultipleChoiceQuestionModel mcModel = new MultipleChoiceQuestionModel("final_question");
		if (nodeCount % 2 == 0) {
			mcModel.setPrompt("In welchem Jahr wurde der Algorithmus erfunden?");
			mcModel.addAnswer("1967", 1, "Richtig, er wurde im Jahr 1952 erfunden.");
			mcModel.addAnswer("1965", 1, "Falsch, er wurde im Jahr 1952 erfunden.");
			mcModel.addAnswer("1959", 1, "Falsch, er wurde im Jahr 1952 erfunden.");
		} else {
			mcModel.setPrompt("Wof�r wird der Tunstall-Algorithmus verwendet?");
			mcModel.addAnswer("Verschl�sselung", 1,
					"Falsch, er wird zur Kodierung und damit zur Kompression verwendet.");
			mcModel.addAnswer("Kodierung", 1, "Richtig,  er wird zur Kodierung und damit zur Kompression verwendet.");
			mcModel.addAnswer("Signierung", 1, "Falsch, er wird zur Kodierung und damit zur Kompression verwendet.");
		}

		lang.addMCQuestion(mcModel);

		lang.finalizeGeneration();
		return model;
	}

	public String expand(TunstallModel model) {
		Map<String, String> dictionary = model.lookupTable.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

		int length = dictionary.keySet().toArray(new String[0])[0].length();
		String compressed = model.compressed;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < compressed.length(); i = i + length) {
			String a = dictionary.get(compressed.substring(i, i + length));
			sb.append(a);
		}
		return sb.toString();
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

		internalGraphCreation(tree, model, 0);

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
	final int x_start = 10;

	/** The x offset. */
	final int x_offset = 60;

	/** The y start. */
	final int y_start = 375;

	/**
	 * Internal graph creation.
	 *
	 * @param currentNode
	 *            the current node
	 * @param model
	 *            the model
	 */
	private int internalGraphCreation(TreeNode currentNode, GraphModel model, int offsetFactor) {
		int of = offsetFactor;
		int childrenCount = currentNode.children.size();
		int leftSide = childrenCount / 2;
		for (int i = 0; i < leftSide; i++) {
			of = internalGraphCreation(currentNode.children.get(i), model, of);
		}

		mapNodeToID.put(currentNode, model.nodes.size());
		model.nodes.add(new Coordinates(x_start + x_offset * of, y_start));

		model.label.add(currentNode.label + " [" + String.format("%.2f", currentNode.frequency) + "]");
		model.nodesToHide.add(model.nodes.size() - 1);

		boolean isEven = currentNode.children.size() % 2 == 0;
		if (isEven) {
			of = of + 1;
		}

		for (int i = leftSide; i < currentNode.children.size(); i++) {
			of = internalGraphCreation(currentNode.children.get(i), model, of);
		}

		int myID = mapNodeToID.get(currentNode);
		for (TreeNode child : currentNode.children) {
			int childID = mapNodeToID.get(child);
			model.adjacencyMatrix[childID][myID] = 1;
		}
		return of + 1;
	}

	private int getGraphNodeID(TreeNode algorithmNode) {
		TreeNode node = getNodeFromFinalTreeByLabel(finalTree, algorithmNode.label);
		System.out.println();
		return mapNodeToID.get(node);
	}

	private TreeNode getNodeFromFinalTreeByLabel(TreeNode node, String label) {
		if (label.equals(node.label)) {
			return node;
		} else {
			if (node.isLeaf()) {
				return null;
			}
			for (TreeNode child : node.children) {
				TreeNode dummy = getNodeFromFinalTreeByLabel(child, label);
				if (dummy != null) {
					return dummy;
				}
			}
		}
		return null;
	}

	/** The y stride. */
	final int y_stride = 100;

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
				null);
	}

	private TreeNode getNodeforIDFromFinalTree(int nodeID) {
		for (Entry<TreeNode, Integer> e : mapNodeToID.entrySet()) {
			if (e.getValue() == nodeID) {
				return e.getKey();
			}
		}
		throw new IllegalArgumentException("should not happen!");
	}

	public static void main(String[] args) {
		Language l = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "Tunstall Coding",
				"Jonathan Roth, Till Voß", 640, 480);

		String text = "MISS MISSISSIPPI ISST";
		for (int i = 0; i < 20; i++) {
			try {
				TunstallModel model = TunstallCoding.compress(text, i);

				String textR = TunstallCoding.expand(model);
				if (!text.equals(textR)) {
					System.err.println(i + ", error, text not equal: " + textR);
				}
			} catch (Exception e) {
				System.err.println(i + ", exception");
			}
		}
		// TunstallCodingAnimal tca = new TunstallCodingAnimal(l, Color.YELLOW,
		// Color.RED, model.tree, 10);

		// tca.compress(text);
		// System.out.println(l);
	}
}
