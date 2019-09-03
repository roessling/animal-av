package generators.compression;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.HtmlDocumentationModel;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.TreeSet;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.Graph;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Huffman extends CompressionAlgorithm implements Generator {

	private static final int	inputLimit	= 11;

	/**
	 * Represents a node in a Huffman tree, that saves the right and theft branch,
	 * the value of the node and its letter.S
	 * 
	 * @author Florian Lindner
	 * 
	 */
	public static class NodeH implements Comparable<NodeH> {
		int						sum;
		char					letter;
		public NodeH	left;
		public NodeH	right;

		/**
		 * Used to crate a leave.
		 * 
		 * @param letterIn
		 * @param sumIn
		 */
		public NodeH(char letterIn, int sumIn) {
			this.letter = letterIn;
			this.sum = sumIn;
		}

		/**
		 * Creates a new tree by connecting two nodes.
		 * 
		 * @param leftIn
		 * @param rightIn
		 */
		public NodeH(NodeH leftIn, NodeH rightIn) {
			// the left side must be the smallest in ascii
			if (leftIn.letter < rightIn.letter)
				this.letter = leftIn.letter;
			else
				this.letter = rightIn.letter;

			this.sum = leftIn.sum + rightIn.sum;
			this.left = leftIn;
			this.right = rightIn;
		}

		/**
		 * When the tree is completed this method is used to return the encoding
		 * bits of the invoking node
		 */
		public String getBits(char letterIn) {
			if (this.letter == letterIn && this.left == null && this.right == null)
				return "";
			if (this.left == null && this.right == null)
				return null;
			if (this.left.getBits(letterIn) != null)
				return "0" + this.left.getBits(letterIn);
			if (this.right.getBits(letterIn) != null)
				return "1" + this.right.getBits(letterIn);
			return null;
		}

		/**
		 * This method is used to ensure the relative sizes of the nodes in the
		 * TreeSet.
		 */
		public int compareTo(NodeH o) {
			// return negative if this object is smaller, positive if greater and zero
			// if equal
			if (this.sum < ((NodeH) o).sum)
				return -1;
			if (this.sum > ((NodeH) o).sum)
				return 1;
			// if equal compare the letters!
			// else
			if (this.getLetter() < ((NodeH) o).getLetter())
				return -1;
			if (this.getLetter() > ((NodeH) o).getLetter())
				return 1;
			return 0;
		}

		/**
		 * Get the height of the tree.
		 * 
		 * @return the height of the tree
		 */
		public int getHeight() {
			if (this.getRight() == null && this.getLeft() == null)
				return 0;
			return 1 + Math.max(this.left.getHeight(), this.right.getHeight());
		}

		/**
		 * Get number of nodes in the tree.
		 * 
		 * @return the number of nodes in the tree
		 */
		public int elements() {
			if (this.getRight() == null && this.getLeft() == null)
				return 1;
			return 1 + this.left.elements() + this.right.elements();
		}

		/**
		 * Get a Vector that contains all the nodes in the tree in In-order.
		 * 
		 * @return the nodes visited in in-order
		 */
		public Vector<NodeH> getInOrder() {
			Vector<NodeH> order = new Vector<NodeH>(0, 1);
			return getInOrder(order);
		}

		public Vector<NodeH> getInOrder(Vector<NodeH> order) {
			Vector<NodeH> result = order;
			result.add(this);
			if (this.getLeft() != null) {
				result = this.getLeft().getInOrder(result);

			}
			if (this.getRight() != null) {
			  result = this.getRight().getInOrder(result);
			}
			return result;
		}

		/**
		 * Get the number of the invoking node in the tree p where the nodes are
		 * sorted in In-order.
		 * 
		 * @param p
		 * @return the number of the noe
		 */
		public int getOrderNr(NodeH p) {
			Vector<NodeH> order = getInOrder();
			for (int i = 0; i < order.size(); i++) {
				if (p.equals(order.elementAt(i)))
					return i;
			}
			return -1;
		}

		public Vector<Integer> getEdgeNodes(char letter) {
			// int h = this.getHeight();
			Vector<NodeH> nodesOrdered = this.getInOrder();
			Vector<Integer> nodes = new Vector<Integer>();

			for (int i = 0; i < nodesOrdered.size(); i++) {
				if (nodesOrdered.elementAt(i).getLetter() == letter
						&& nodesOrdered.elementAt(i).getLeft() == null
						&& nodesOrdered.elementAt(i).getRight() == null) {
					nodes.add(this.getOrderNr(nodesOrdered.elementAt(i)));
					NodeH tmp = nodesOrdered.elementAt(i);
					while (tmp.getFather(this) != null) {
						nodes.add(this.getOrderNr(tmp.getFather(this)));
						tmp = tmp.getFather(this);
					}
					break;
				}
			}
			return nodes;
		}

		/**
		 * Get the number of edges between two nodes upper and lower.
		 * 
		 * @param upper
		 * @param lower
		 * @return the distance between the nodes
		 */
		public static int getDistance(NodeH upper, NodeH lower) {
			if (upper.equals(lower))
				return 0;
			else if (upper.getLeft() == null && upper.getRight() == null)
				return 1000;
			else
				return Math.min(1 + getDistance(upper.left, lower), 1 + getDistance(
						upper.right, lower));
		}

		/**
		 * This method is used internal to get an Array of Nodes. This array is
		 * created to fit with the constructor of the graph.
		 * 
		 * @param main
		 *          The rootNode of the tree
		 * @param actualCoords
		 *          the first node of the tree
		 * @param coords
		 * @return the nodes
		 */
		public Node[] getCoords(NodeH main, Node actualCoords, Node[] coords) {
		  Node[] myCoords = coords;
			// int actualToMain = getDistance(main, this);
			int h = main.getHeight();
			float offsetFactor = (float) Math.pow(2, 1 - getDistance(main, this));
			float l = (float) 700 / ((float) (Math.pow(2, h) - (float) 1));
			int x;
			int y;
			int nrLeft;
			int nrRight;
			if (this.getLeft() != null || this.getRight() != null) {
				x = ((Coordinates) actualCoords).getX()
						- new Float(l * offsetFactor).intValue();
				y = ((Coordinates) actualCoords).getY() + 50;
				nrLeft = main.getOrderNr(this.left);
				coords[nrLeft] = new Coordinates(x, y);

				x = ((Coordinates) actualCoords).getX()
						+ new Float(l * offsetFactor).intValue();
				nrRight = main.getOrderNr(this.right);
				coords[nrRight] = new Coordinates(x, y);

				myCoords = this.left.getCoords(main, coords[nrLeft], coords);
				myCoords = this.right.getCoords(main, coords[nrRight], coords);
			}
			return myCoords;
		}

		/**
		 * Get the father of the invoking node
		 * 
		 * @param p
		 *          the root of the tree
		 * @return the NodeH determined
		 */
		public NodeH getFather(NodeH p) {
			Vector<NodeH> nodes = p.getInOrder();
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.elementAt(i).left != null) {
					if (nodes.elementAt(i).left.equals(this))
						return nodes.elementAt(i);
				}
				if (nodes.elementAt(i).right != null) {
					if (nodes.elementAt(i).right.equals(this))
						return nodes.elementAt(i);
				}
			}
			return null;
		}

		public NodeH getLeft() {
			return left;
		}

		public char getLetter() {
			return letter;
		}

		public NodeH getRight() {
			return right;
		}

		public int getSum() {
			return sum;
		}
	}

	private static final String	DESCRIPTION	= "Die Huffman-Kodierung ist ähnlich wie die Shannon-Fano-Kodierung ein entropisches"
																							+ " Kodierungsverfahren. So werden bei der Huffman-Kodierung ebenfalls die Häufigkeiten"
																							+ " des Eingabetextes verwendet, um einen binären Baum zu erzeugen.";

	private static final String	SOURCE_CODE	= "Der Algorithmus wird in einer Animation demonstriert. Um die grafische Animation in voller Größe darstellen"
																							+ " zu können, wird die Eingabe auf 11 Buchstaben begrenzt.";

	public Huffman() {
    // nothing to be done here
	}

	@SuppressWarnings("unchecked")
	public void compress(String[] text2) throws LineNotExistsException {
	  String[] text = text2;
		// trim input to maximum length
		String[] t = new String[Math.min(inputLimit, text.length)];
		for (int i = 0; i < Math.min(inputLimit, text.length); i++) {
			t[i] = text[i];
		}
		text = t;

		// topic
		Text topic = lang.newText(new Coordinates(20, 50), "Huffman Encoding",
				"Topic", null, tptopic);
		lang.newRect(new Offset(-5, -5, topic, "NW"),
				new Offset(5, 5, topic, "SE"), "topicRect", null, rctp);

		// Algo in words
		lang.nextStep();
		Text algoinWords = lang.newText(new Coordinates(20, 100),
				"Der Algorithmus in Worten", "inWords", null, tpwords);

		// Algo steps
		lang.nextStep();
		Text step1 = lang.newText(new Offset(0, 100, topic, "SW"),
				"1) Ermittle die Häufigkeiten der Buchstaben in der Eingabe.", "line1",
				null, tpsteps);
		lang.nextStep();
		Text step2 = lang
				.newText(
						new Offset(0, 30, step1, "SW"),
						"2) Erstelle aus jedem auftretenden Buchstaben einen Knoten mit dessen Häufigkeit als Blatt eines Baumes.",
						"line2", null, tpsteps);
		lang.nextStep();
		Text step3 = lang
				.newText(
						new Offset(0, 30, step2, "SW"),
						"3) Ermittle die zwei Teilbäume mit der geringsten Häufigkeit. Verbinde sie durch",
						"line3", null, tpsteps);
		Text step31 = lang
				.newText(
						new Offset(0, 20, step3, "SW"),
						"      einen Baum, bei dem die Häufigkeit der Summe der Häufigkeiten der beiden Söhne",
						"line31", null, tpsteps);
		Text step32 = lang
				.newText(
						new Offset(0, 20, step31, "SW"),
						"      entspricht. Den Söhnen sei von nun an keine Häufigkeit mehr zugeordnet.",
						"line31", null, tpsteps);
		lang.nextStep();
		Text step4 = lang
				.newText(
						new Offset(0, 30, step32, "SW"),
						"4) Wende Schritt 3 solange an, bis es nur noch einen Wurzelknoten gibt. Die Kanten",
						"line4", null, tpsteps);
		Text step41 = lang
				.newText(
						new Offset(0, 20, step4, "SW"),
						"       nach links zeigend kodieren eine 0, die Rechts-Kanten eine 1. Um einen",
						"line32", null, tpsteps);
		Text step42 = lang
				.newText(
						new Offset(0, 20, step41, "SW"),
						"       Buchstaben zu kodieren ,wird der Weg von der Wurzel zum Blatt verfolgt.",
						"line32", null, tpsteps);
		lang.nextStep();

		algoinWords.hide();
		step1.hide();
		step2.hide();
		step3.hide();
		step31.hide();
		step32.hide();
		step4.hide();
		step41.hide();
		step42.hide();
		tpwords.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
				Font.PLAIN, 16));
		
		FillInBlanksQuestionModel algoYear = new FillInBlanksQuestionModel("algoYear");
		algoYear.setPrompt("In welchem Jahr wurde der Algorithmus gefunden?");
		algoYear.addAnswer("1952", 1, "David A. Huffman fand den Algorithmus im Jahre 1952");
		lang.addFIBQuestion(algoYear);
		lang.nextStep();
		
		// extract the input text
		String input = "";
		for (int i = 0; i < text.length; i++) {
			input += text[i];
		}
		input = input.toUpperCase();

		algoinWords.setText("Eingabe:  " + input, null, null);
		algoinWords.show();
		lang.nextStep();
		step1.changeColor(null, Color.RED, null, null);
		step1.show();

		// algo
		// get the frequencys of each letter
		int[] list = new int[256];
		for (int i = 0; i < text.length; i++) {
			list[new Integer(text[i].charAt(0))]++;
		}

		// get the number of diffrent letters
		int numberOfLetters = 0;
		for (int i = 0; i < 256; i++) {
			if (list[i] != 0)
				numberOfLetters++;
		}

		// step1: print the matrix of the frequencys
		String[][] freqPrint = new String[numberOfLetters + 1][3];
		freqPrint[0][0] = "char  ";
		freqPrint[0][1] = "freq.  ";
		freqPrint[0][2] = "code  ";
		int[] listClone = list.clone();
		int cnt = 1;
		int big;
		int bigIndex;
		for (int i = 0; i < numberOfLetters; i++) {
			freqPrint[cnt][2] = "    ";
			big = -1;
			bigIndex = -1;
			for (int j = 0; j < list.length; j++) {
				if (list[j] != 0 && list[j] > big) {
					big = list[j];
					bigIndex = j;
				}
			}
			freqPrint[cnt][0] = "" + (char) bigIndex + ":  ";
			freqPrint[cnt][1] = "" + big;
			list[bigIndex] = -1;
			cnt++;
		}

		// restore the old list of frequencys
		list = listClone;

		StringMatrix strMatrix = lang.newStringMatrix(new Offset(115, -20, step2,
				"NE"), freqPrint, "matrix", null, mp);
		lang.nextStep();
		step1.changeColor(null, Color.BLACK, null, null);
		step2.changeColor(null, Color.RED, null, null);
		step3.changeColor(null, Color.RED, null, null);
		step31.changeColor(null, Color.RED, null, null);
		step32.changeColor(null, Color.RED, null, null);
		step4.changeColor(null, Color.RED, null, null);
		step41.changeColor(null, Color.RED, null, null);
		step42.changeColor(null, Color.RED, null, null);
		step2.show();

		lang.nextStep();

		// create leaves
		TreeSet<NodeH> nodes = new TreeSet<NodeH>();
		for (int i = 0; i < list.length; i++) {
			if (list[i] > 0) {
				nodes.add(new NodeH((char) i, list[i]));
			}
		}

		// copy leaves for visualisation issues
		TreeSet<NodeH> leaves = (TreeSet<NodeH>) nodes.clone();

		// build the final tree
		while (nodes.size() > 1) {
			NodeH small = nodes.first();
			nodes.remove(small);
			NodeH small2 = nodes.first();
			nodes.remove(small2);
			nodes.add(new NodeH(small, small2));
		}

		// build the whole graph
		NodeH root = nodes.first();
		// int h = root.getHeight();
		// int n = root.elements();
		// x: distance between 2 nodes on the leave level
		// float x = (float)1200 / (float)Math.pow(2, h) - (float)1;
		Vector<NodeH> order = root.getInOrder();

		int[][] adj = new int[root.elements()][root.elements()];
		for (int i = 0; i < order.size(); i++) {
			if (order.elementAt(i).left != null && order.elementAt(i).right != null) {
				// FIXME 9 is a bad idea
				adj[i][root.getOrderNr(order.elementAt(i).left)] = 9;
				adj[i][root.getOrderNr(order.elementAt(i).right)] = 1;
			}
		}

		Node[] graphNodes = new Node[order.size()];
		graphNodes = root.getCoords(root, new Coordinates(350, 400), graphNodes);
		graphNodes[0] = new Coordinates(350, 250);

		String[] label = new String[order.size()];
		for (int i = 0; i < order.size(); i++) {
			if (order.elementAt(i).left == null && order.elementAt(i).right == null)
				label[i] = "" + order.elementAt(i).letter;
			else
				label[i] = "";
		}
		Graph gr = lang.newGraph("graph", adj, graphNodes, label, null, gp);

		// show the leaves only
		tpsteps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
				Font.PLAIN, 10));
		for (int i = 0; i < order.size(); i++) {
			if (order.elementAt(i).left != null || order.elementAt(i).right != null) {
				gr.hideNode(i, null, null);
			}
		}

		lang.nextStep();

		step2.changeColor(null, Color.BLACK, null, null);
		step3.changeColor(null, Color.RED, null, null);
		step31.changeColor(null, Color.RED, null, null);
		step32.changeColor(null, Color.RED, null, null);
		step4.changeColor(null, Color.RED, null, null);
		step41.changeColor(null, Color.RED, null, null);
		step42.changeColor(null, Color.RED, null, null);
		step3.show();
		step31.show();
		step32.show();
		step4.show();
		step41.show();
		step42.show();

		lang.nextStep();

		step1.hide();
		step2.hide();
		step3.hide();
		step31.hide();
		step32.hide();
		step4.hide();
		step41.hide();
		step42.hide();

		lang.nextStep();

		// build up the tree

		while (leaves.size() > 1) {
			NodeH smallest = leaves.first();
			NodeH father = smallest.getFather(root);
			gr.showNode(root.getOrderNr(father), null, null);
			Text labelRelation = lang.newText(gr.getNode(root.getOrderNr(father)), ""
					+ father.getSum(), "label", null, tpsteps);
			labelRelation.changeColor(null, Color.RED, null, null);
			gr.hideEdgeWeight(root.getOrderNr(father), root.getOrderNr(father.left),
					null, null);
			gr.hideEdgeWeight(root.getOrderNr(father), root.getOrderNr(father.right),
					null, null);
			leaves.remove(smallest);
			NodeH smallest2 = leaves.first();
			leaves.remove(smallest2);
			leaves.add(father);
			lang.nextStep();
		}

		// FIXME how to create labels by an offset to a NODE

		tpsteps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
				Font.PLAIN, 16));
		Text expl = lang.newText(new Offset(-150, 45, gr, "SW"),
				"Um die Kodierung für jeden Buchstaben zu bestimmen, traversiere",
				"expl", null, tpsteps);
		Text expl2 = lang.newText(new Offset(0, 20, expl, "SW"),
				"den Baum nach obigem Bitmuster.", "expl", null, tpsteps);

		lang.nextStep();
		// show edge weights
		for (int i = 0; i < gr.getSize(); i++) {
			gr.showEdgeWeight(i, i, null, null);
		}

		lang.nextStep();

		// show the encoding entries in the matrix and hightlight the correspondig
		// edges
		big = 0;
		cnt = 1;
		bigIndex = -1;

		for (int i = 0; i < numberOfLetters; i++) {
			/*
			 * find the letter with the lowest number of appereance to fit the order
			 * chosen in the graphical output above
			 */
			big = -1;
			for (int j = 0; j < list.length; j++) {
				if (list[j] > big) {
					big = list[j];
					bigIndex = j;
				}
			}
			
			FillInBlanksQuestionModel fib = new FillInBlanksQuestionModel("bitcode" + i);
			fib.setPrompt("Wie lautet der ermittelte Bitcode f&uuml;r den Buchstaben " + (char) bigIndex);
			fib.addAnswer(nodes.first().getBits((char) bigIndex), 2, "Das ist korrekt");
			fib.setGroupID("bitcode");
			lang.addFIBQuestion(fib);
			lang.nextStep();
			
			// bigindex is the cahr value of the letter
			strMatrix.put(cnt, 2, nodes.first().getBits((char) bigIndex), null, null);
			strMatrix.highlightCell(cnt, 2, null, null);
			// highlight the edges that encode the bits of the letter#
			Vector<Integer> nedgeNodes = root.getEdgeNodes((char) bigIndex);

			for (int k = 0; k < nedgeNodes.size() - 1; k++) {
				gr.highlightEdge(nedgeNodes.elementAt(k), nedgeNodes.elementAt(k + 1),
						null, null);
			}

			cnt++;
			// the list can be overwritten now
			list[bigIndex] = -1;
			lang.nextStep();

			strMatrix.unhighlightCell(cnt - 1, 2, null, null);

			// unhighlight the edges
			for (int l = 0; l < gr.getSize() - 1; l++) {
				gr.unhighlightEdge(l, l + 1, null, null);
			}
		}

		// decode the input
		String result = "";
		for (int i = 0; i < input.length(); i++) {
			result += nodes.first().getBits(input.charAt(i));
			result += " ";
		}

		Text fazit = lang.newText(new Offset(0, 40, expl2, "SW"),
				"Durch die bitweise Traversierung des Baumes können wir nun jeden",
				"Ausgabe", null, tpsteps);
		Text fazit2 = lang.newText(new Offset(0, 20, fazit, "SW"),
				"Buchstaben der Eingabe einzeln kodieren. Dadurch erhalten wir:",
				"ausgabe", null, tpsteps);
		Text fazit3 = lang.newText(new Offset(0, 20, fazit2, "SW"), result,
				"fazit", null, tpsteps);
		fazit3.changeColor(null, Color.BLUE, null, null);
		
		HtmlDocumentationModel link = new HtmlDocumentationModel("link");
		link.setLinkAddress("http://de.wikipedia.org/wiki/Huffman-Codierung");
		lang.addDocumentationLink(link);
		lang.nextStep();
	}

	public static String getSOURCE_CODE() {
		return SOURCE_CODE;
	}

	public String getCodeExample() {
		return SOURCE_CODE;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getName() {
		return "Huffman-Komprimierung";
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		String[] strArray = (String[]) primitives.get("stringArray");
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		lang.addQuestionGroup(new QuestionGroupModel("bitcode", 3));
		
		try {
			compress(strArray);
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}
		lang.finalizeGeneration();
		return lang.getAnimationCode();
	}

	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
	}

	public String getAlgorithmName() {
		return "Huffman";
	}

	public void init() {
	  lang = new AnimalScript("Huffman Kodierung", "Florian Lindner", 800, 600);
	  lang.setStepMode(true);
	}

}
