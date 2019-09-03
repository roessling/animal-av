package generators.compression;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class ShannonFanoEncoding extends CompressionAlgorithm implements
		Generator {

	private static final int	inputLimit	= 10;

	SourceCode								sc;

	/**
	 * A Partition represents a Shannon Fanno tree (in fact a Node). If contains a
	 * left and a right branch as well as a value and the letters it has.
	 * 
	 * @author Florian Lindner
	 * 
	 */
	public static class Partition {

		private Partition				left;

		private Partition				right;

		private String					value;

		private Vector<Letter>	letters;

		/**
		 * Recursively creates the whole Shannon Fano tree.
		 * 
		 * @param lettersVec
		 *          A Vector that contains all the letters of the inout
		 */
		public Partition(Vector<Letter> lettersVec) {
			this.letters = lettersVec;
			int index;
			if (letters.size() == 1)
				this.value = letters.elementAt(0).getLetter();
			else {
				// get the sum of frequencys
				int sum = 0;
				for (int i = 0; i < letters.size(); i++) {
					sum += letters.elementAt(i).getFrequency();
				}
				// Partion the elements
				float half = (float) sum / 2;
				int count = 0;
				index = 0;
				float difference = half;
				for (int i = 0; i < letters.size(); i++) {
					if (Math.abs(half
							- ((float) count + (float) letters.elementAt(i).getFrequency())) < difference) {
						count += letters.elementAt(i).getFrequency();
						difference = Math.abs(half - count);
						index = i;
					} else
						break;
				}

				// create the 2 partitions
				Vector<Letter> l = new Vector<Letter>(0, 1);
				Vector<Letter> r = new Vector<Letter>(0, 1);
				for (int i = 0; i < letters.size(); i++) {
					if (i <= index)
						l.add(letters.elementAt(i));
					else
						r.add(letters.elementAt(i));
				}
				this.left = new Partition(l);
				this.right = new Partition(r);
			}
		}

		/**
		 * Get the height of the invoking tree.
		 * 
		 * @return the height of the tree
		 */
		public int getHeight() {
			if (this.value != null)
				return 0;
			return 1 + Math.max(this.left.getHeight(), this.right.getHeight());
		}

		/**
		 * Get the number of elements in the invoking tree.
		 * 
		 * @return the number of elements in the tree
		 */
		public int elements() {
			if (this.value != null)
				return 1;
			return 1 + this.left.elements() + this.right.elements();
		}

		public Vector<Partition> getInOrder(Vector<Partition> order) {
		  Vector<Partition> part = order;
		  part.add(this);
			if (this.value == null) {
			  part = this.getLeft().getInOrder(order);
			  part = this.getRight().getInOrder(order);
			}
			return part;
		}

		/**
		 * Get a vector that contains the nodes of the invoking tree ordered.
		 * 
		 * @return the in-order traversal ordering
		 */
		public Vector<Partition> getInOrder() {
			Vector<Partition> order = new Vector<Partition>(0, 1);
			return getInOrder(order);
		}

		/**
		 * Get the number of the invoking node in the Vector of the tree p.
		 * 
		 * @param p
		 * @return the order number
		 */
		public int getOrderNr(Partition p) {
			Vector<Partition> order = getInOrder();
			for (int i = 0; i < order.size(); i++) {
				if (p.equals(order.elementAt(i)))
					return i;
			}
			return -1;
		}

		/**
		 * Get the number of edges between the nodes upper and lower.
		 * 
		 * @param upper
		 * @param lower
		 * @return the distance
		 */
		public static int getDistance(Partition upper, Partition lower) {
			if (upper.equals(lower))
				return 0;
			else if (upper.value != null)
				return 1000;
			else
				return Math.min(1 + getDistance(upper.left, lower), 1 + getDistance(
						upper.right, lower));
		}

		/**
		 * Get a array of Nodes that is used in the constructor of a graph.
		 * 
		 * @param main
		 * @param actualCoords
		 * @param coords
		 * @return the nodes
		 */
		public Node[] getCoords(Partition main, Node actualCoords, Node[] coords) {
			// int actualToMain = getDistance(main, this);
		  Node[] myCoords = coords;
			int h = main.getHeight();
			float offsetFactor = (float) Math.pow(2, 1 - getDistance(main, this));
			float l = (float) 700 / ((float) (Math.pow(2, h) - (float) 1));
			int x;
			int y;
			int nrLeft;
			int nrRight;
			if (this.value == null) {
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
		 * Highlight the invoking node in the graph by changing its colour to
		 * yellow.
		 * 
		 * @param mainp
		 *          The root of the tree
		 */
		public void highlight(Partition mainp) {
			int nr = mainp.getOrderNr(this);
			gr.highlightNode(nr, null, null);
		}

		/**
		 * Unhighlight the invoking node in the graph by changing its colour to
		 * white.
		 * 
		 * @param mainp
		 *          The root of the tree
		 */
		public void unhighlight(Partition mainp) {
			int nr = mainp.getOrderNr(this);
			gr.unhighlightNode(nr, null, null);
		}

		/**
		 * Get the father of the invoking node.
		 * 
		 * @return the parent
		 */
		public Partition getParent() {
			Vector<Partition> order = mainPartition.getInOrder();
			for (int i = 0; i < order.size(); i++) {
				if (order.elementAt(i).getValue() == null) {
					if (order.elementAt(i).left.equals(this))
						return order.elementAt(i);
					else if (order.elementAt(i).right.equals(this))
						return order.elementAt(i);
				}
			}
			return null;
		}

		public Vector<Integer> getEdgeNodes(String letter) {
			// int h = this.getHeight();
			Vector<Partition> nodesOrdered = this.getInOrder();
			Vector<Integer> nodes = new Vector<Integer>();

			for (int i = 0; i < nodesOrdered.size(); i++) {
				if (nodesOrdered.elementAt(i).value != null) {
					if (nodesOrdered.elementAt(i).value.equals(letter)
							&& nodesOrdered.elementAt(i).getLeft() == null
							&& nodesOrdered.elementAt(i).getRight() == null) {
						nodes.add(this.getOrderNr(nodesOrdered.elementAt(i)));
						Partition tmp = nodesOrdered.elementAt(i);
						while (tmp.getParent() != null) {
							nodes.add(this.getOrderNr(tmp.getParent()));
							tmp = tmp.getParent();
						}
						break;
					}
				}

			}
			return nodes;
		}

		/**
		 * Get the sibling of the invoking node.
		 * 
		 * @return the sibling
		 */
		public Partition getSibling() {
			if (mainPartition.equals(this))
				return null;
			if (this.equals(this.getParent().left))
				return this.getParent().right;
			return this.getParent().left;
		}

		/**
		 * Get the sum of the absolute appearances of all nodes in the progeny of the
		 * invoking node.
		 * 
		 * @return the number of occurrences
		 */
		public int getSum() {
			int sum = 0;
			for (int i = 0; i < this.letters.size(); i++) {
				sum += this.letters.elementAt(i).getFrequency();
			}
			return sum;
		}

		public Partition getLeft() {
			return left;
		}

		public Vector<Letter> getLetters() {
			return letters;
		}

		public Partition getRight() {
			return right;
		}

		public String getValue() {
			return value;
		}

	}

	/**
	 * A letter decribes the chracter a node represents that contains the sum of
	 * all nodes in the progeny of the involing node.
	 * 
	 * @author Florian Lindner
	 * 
	 */
	public static class Letter {

		private String	letter;

		private int			frequency;

		public Letter(String let, int frequ) {
			this.letter = let;
			this.frequency = frequ;
		}

		public int getFrequency() {
			return frequency;
		}

		public String getLetter() {
			return letter;
		}
	}

	static Graph				gr;

	static Partition		mainPartition;

	private static StringArray	actualStrArray;

	private static StringArray	leftStrArry;

	private static StringArray	rightStrArray;

	private static Text					actualSumText;

	private static Text					leftSumText;

	private static Text					rightSumText;

	private static final String	DESCRIPTION	=
	  "Die Shannon Fano Kodierung ist ein Kodierungsverfahren, welches vergleichbar mit der"
	  + " Huffman-Kodierung ist. Die Buchstaben werden zuerst nach ihrer H&auml;ufigkeit sortiert."
	  + " Durch die Anwendung des Verfahrens werden f&uuml;r die h&auml;ufigsten Buchstaben die"
	  + " k&uuml;rzesten Bitfolgen f&uuml;r eine Kodierung gewählt.";

	private static final String	SOURCE_CODE	= 
	  "Der Algorithmus wird in einer Animation demonstriert.\nUm die grafische Animation in voller Gr&ouml;&szlig;e darstellen"
	  + " zu k&ouml;nnen, wird die Eingabe auf 10 Buchstaben begrenzt.";

	public ShannonFanoEncoding() {
		// nothing to be done here
	}

	/**
	 * This method does the whole animation . It traverses the tree and meanwhile
	 * highlights, unhighlights, shows and hides the nodes. It sets the labels of
	 * the nodes during the animation, as well as it shows the current sum of the
	 * actual node in traverse.
	 * 
	 * @param p
	 *          The tree sthat shall be animated.
	 */
	public void animate(Partition p) {

		actualStrArray.hide();
		actualSumText.hide();
		leftStrArry.hide();
		leftSumText.hide();
		rightStrArray.hide();
		rightSumText.hide();

		if (!p.equals(mainPartition)) {
			p.getParent().unhighlight(mainPartition);
			p.getSibling().unhighlight(mainPartition);
		}
		gr.showNode(0, null, null);
		p.highlight(mainPartition);

		String actualData[] = new String[p.getLetters().size()];
		for (int i = 0; i < p.getLetters().size(); i++) {
			actualData[i] = p.getLetters().elementAt(i).getLetter();
		}
		actualStrArray = lang.newStringArray(
				new Offset(0, 0, actualStrArray, "NW"), actualData, "strArray", null,
				ap);
		actualSumText = lang.newText(new Offset(0, -4, actualSumText, "SW"),
				"Summe: " + p.getSum(), "textSum", null, tpsteps);
		lang.nextStep();

		if (p.getValue() == null) {
			p.getLeft().highlight(mainPartition);
			gr.showNode(mainPartition.getOrderNr(p.getLeft()), null, null);

			String leftData[] = new String[p.getLeft().getLetters().size()];
			for (int i = 0; i < p.getLeft().getLetters().size(); i++) {
				leftData[i] = p.getLeft().getLetters().elementAt(i).getLetter();
			}
			leftStrArry = lang.newStringArray(new Offset(0, 0, leftStrArry, "NW"),
					leftData, "strArray", null, ap);
			leftSumText = lang.newText(new Offset(0, -4, leftSumText, "SW"),
					"Summe: " + p.getLeft().getSum(), "textSum", null, tpsteps);
			p.getRight().highlight(mainPartition);
			gr.showNode(mainPartition.getOrderNr(p.getRight()), null, null);

			String rightData[] = new String[p.getRight().getLetters().size()];
			for (int i = 0; i < p.getRight().getLetters().size(); i++) {
				rightData[i] = p.getRight().getLetters().elementAt(i).getLetter();
			}
			rightStrArray = lang.newStringArray(
					new Offset(0, 0, rightStrArray, "NW"), rightData, "strArray", null,
					ap);
			rightSumText = lang.newText(new Offset(0, -4, rightSumText, "SW"),
					"Summe: " + p.getRight().getSum(), "textSum", null, tpsteps);
			lang.nextStep();
			p.unhighlight(mainPartition);
			p.getRight().unhighlight(mainPartition);
			animate(p.getLeft());
			p.getLeft().unhighlight(mainPartition);
			animate(p.getRight());
			p.getRight().unhighlight(mainPartition);
		}
	}

	/**
	 * Enode the input.
	 * 
	 * @param text
	 * @throws LineNotExistsException
	 */
	public void compress(String[] text) throws LineNotExistsException {
		// trim input to maximum length
//		String ein = "";
		String[] t = new String[Math.min(text.length, inputLimit)];
		for (int i = 0; i < t.length; i++) {
			t[i] = text[i];
//			ein += text[i];
		}
//		text = t;

		// topic
		Text topic = lang.newText(new Coordinates(20, 50), "Shannon Fano Encoding",
				"Topic", null, tptopic);

		lang.newRect(new Offset(-5, -5, topic, "NW"),
				new Offset(5, 5, topic, "SE"), "topicRect", null, rctp);

		// Algo in words
		lang.nextStep();
		Text algoinWords = lang.newText(new Coordinates(20, 100),
				"Der Algorithmus in Worten", "inWords", null, tpwords);

		// Algo steps
		lang.nextStep();
		Text step1 = lang
				.newText(
						new Offset(0, 100, topic, "SW"),
						"1) Ermittle die Häufigkeiten der Buchstaben in der Eingabe und sortiere diese danach absteigend.",
						"line1", null, tpsteps);
		lang.nextStep();
		Text step2 = lang
				.newText(
						new Offset(0, 30, step1, "SW"),
						"2) Zu Beginn wird die Menge aller Buchstaben als eine Partition betrachtet.",
						"line2", null, tpsteps);
		lang.nextStep();
		Text step3 = lang
				.newText(
						new Offset(0, 30, step2, "SW"),
						"3) Teile jede Partition mit mehr als einem Buchstaben in 2 Teilpartitionen auf, so dass ",
						"line3", null, tpsteps);
		Text step31 = lang
				.newText(
						new Offset(0, 20, step3, "SW"),
						"      die Differenz der Summe der Häufigkeiten beider Seiten minimal ist.",
						"line31", null, tpsteps);
		lang.nextStep();
		Text step4 = lang
				.newText(
						new Offset(0, 30, step31, "SW"),
						"4) Nach 3. entsteht ein Baum, sobald nur noch Partitionen mit einem Buchstaben vorhanden sind.",
						"line4", null, tpsteps);
		Text step41 = lang
				.newText(
						new Offset(0, 20, step4, "SW"),
						"      Der Baum wird nun binär traversiert, so dass eine Kodierung für jeden Buchstaben entsteht.",
						"line32", null, tpsteps);
		lang.nextStep();

		algoinWords.hide();
		step1.hide();
		step2.hide();
		step3.hide();
		step31.hide();
		step4.hide();
		step41.hide();
		lang.nextStep();
		tpwords.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
				Font.PLAIN, 16));

		// extract the input
		String input = "";
		for (int i = 0; i < t.length; i++) {
			input += t[i];
		}
		algoinWords.setText("Eingabe:  " + input, null, null);
		algoinWords.show();
		step1.changeColor(null, Color.RED, null, null);
		step1.show();

		// algo

		// print the frequencys of the letters

		// get the frequencys of each letter
		int[] list = new int[256];
		for (int i = 0; i < t.length; i++) {
			list[new Integer(t[i].charAt(0))]++;
		}

		// get the number of diffrent letters
		int numberOfLetters = 0;
		for (int i = 0; i < 256; i++) {
			if (list[i] != 0)
				numberOfLetters++;
		}

		// step1: print the matrix of the frequencys
		String[][] freqPrint = new String[numberOfLetters][2];
		int[] listClone = list.clone();
		int cnt = 0;
		int big;
		int bigIndex;
		for (int i = 0; i < numberOfLetters; i++) {
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

		lang.newStringMatrix(new Offset(30, 0, step1, "NE"), freqPrint, "matrix",
				null, mp);
		lang.nextStep();
		step1.changeColor(null, Color.BLACK, null, null);
		step2.changeColor(null, Color.RED, null, null);
		step3.changeColor(null, Color.RED, null, null);
		step31.changeColor(null, Color.RED, null, null);
		step2.show();
		step3.show();
		step31.show();
		lang.nextStep();

		// sort all the letters into the initial partition (decreasin size!)
		Vector<Letter> letters = new Vector<Letter>(0, 1);
		int most = -1;
		int mostIndex = -1;

		// create the partition
		for (int i = 0; i < numberOfLetters; i++) {
			most = -1;
			mostIndex = -1;
			for (int j = 0; j < list.length; j++) {
				if (list[j] > 0 && list[j] > most) {
					most = list[j];
					mostIndex = j;
				}
			}
			letters.add(new Letter("" + (char) mostIndex, most));
			list[mostIndex] = 0;
		}

		mainPartition = new Partition(letters);

		// print the whole tree
		// int h = mainPartition.getHeight();
		// float x = (float)700 / (float)Math.pow(2, h) - (float)1;
		Vector<Partition> order = mainPartition.getInOrder();

		int[][] adj = new int[mainPartition.elements()][mainPartition.elements()];
		for (int i = 0; i < order.size(); i++) {
			if (order.elementAt(i).getValue() == null) {
				// left branch gets a "0", right branch a "1"
				// FIX ME how to create the branch length 0????
				adj[i][mainPartition.getOrderNr(order.elementAt(i).getLeft())] = 9;
				adj[i][mainPartition.getOrderNr(order.elementAt(i).getRight())] = 1;
			}
		}

		Node[] graphNodes = new Node[order.size()];
		graphNodes = mainPartition.getCoords(mainPartition, new Coordinates(350,
				300), graphNodes);
		graphNodes[0] = new Coordinates(350, 300);

		String[] label = new String[order.size()];
		for (int i = 0; i < order.size(); i++) {
			if (order.elementAt(i).getValue() != null)
				label[i] = order.elementAt(i).getValue();
			else
				label[i] = "";
		}

		gr = lang.newGraph("graph", adj, graphNodes, label, null, gp);
		// at first hide all nodes
		for (int i = 0; i < gr.getSize(); i++) {
			gr.hideNode(i, null, null);
		}

		// animation phase
		Text actual = lang.newText(new Offset(0, 50, gr, "SW"), "Gesamtmenge:",
				"actual", null, tpsteps);
		actualStrArray = lang.newStringArray(new Offset(20, 0, actual, "NE"),
				new String[] { "   ", "   ", "   ", "   " }, "actual", null, ap);
		actualSumText = lang.newText(new Offset(65, 5, actualStrArray, "E"),
				"Summe: " + mainPartition.getSum(), "sumtext", null, tpsteps);
		Text left = lang.newText(new Offset(0, 30, actual, "SW"),
				"Unterteilung 1:  ", "left", null, tpsteps);
		leftStrArry = lang.newStringArray(new Offset(20, 0, left, "NE"),
				new String[] { "   ", "   ", "   ", "   " }, "left", null, ap);
		leftSumText = lang.newText(new Offset(65, 9, leftStrArry, "E"), "Summe: "
				+ mainPartition.getLeft().getSum(), "sumtext", null, tpsteps);
		Text right = lang.newText(new Offset(0, 30, left, "SW"),
				"Unterteilung 2:  ", "right", null, tpsteps);
		rightStrArray = lang.newStringArray(new Offset(20, 0, right, "NE"),
				new String[] { "   ", "   ", "   ", "   " }, "right", null, ap);
		rightSumText = lang
				.newText(new Offset(65, 10, rightStrArray, "E"), "Summe: "
						+ mainPartition.getRight().getSum(), "sumtext", null, tpsteps);

		lang.nextStep();

		animate(mainPartition);

		gr.showEdgeWeight(0, gr.getSize() - 1, null, null);

		// encode

		// get the encoding for every letter by traversing the tree
		Hashtable<String, String> hash = new Hashtable<String, String>();
		hash = fillHash(hash, mainPartition, "");

		// Encode the input
		String result = "";
		for (int i = 0; i < t.length; i++) {
			result += hash.get(t[i]) + "  ";
		}

		actualStrArray.hide();
		leftStrArry.hide();
		rightStrArray.hide();

		Text fazit1 = lang.newText(new Offset(-210, 70, right, "SW"),
				"Jeder Buchstabe der Eingabe kann nun durch den Baum kodiert werden,",
				"fazit", null, tpsteps);
		Text fazit11 = lang.newText(new Offset(0, 20, fazit1, "SW"),
				"die Bitfolge beschreibt den Pfad von der Wurzel zum Blatt.", "fazit1",
				null, tpsteps);

		StringArray in = lang.newStringArray(new Offset(0, 35, fazit11, "SW"),
				t, "in", null, ap);
		in.highlightCell(0, null, null);

		StringTokenizer tResult = new StringTokenizer(result);
		String showResult = "";
		Text fazit2 = lang.newText(new Offset(0, 20, in, "SW"),
				"Die Ausgabe ist also: ", "fazit2", null, tpsteps);
		Text fazit21 = lang.newText(new Offset(20, -5, fazit2, "SE"), showResult,
				"fazit3", null, tpsteps);
		fazit21.changeColor(null, Color.BLUE, null, null);

		for (int j = 0; j < t.length; j++) {
			in.highlightCell(j, null, null);
			if (j > 0)
				in.unhighlightCell(j - 1, null, null);
			// highlight the path and change the output
			Vector<Integer> nodes = mainPartition.getEdgeNodes(t[j]);
			for (int k = 0; k < nodes.size() - 1; k++) {
				gr
						.highlightEdge(nodes.elementAt(k), nodes.elementAt(k + 1), null,
								null);
			}

			showResult += " " + tResult.nextToken();
			fazit21.setText(showResult, null, null);

			lang.nextStep();

			// hide the edges here
		}

	}

	/**
	 * fills a hastable recursivly. It contains letters as keys and the encoding
	 * bits as value.
	 * 
	 * @param hash
	 *          the Hashtablr to fill
	 * @param p
	 *          the partition that contains some letters
	 * @param currentBits
	 *          the bits the lead to the current partition node
	 * @return the filled Hastable
	 */
	public static Hashtable<String, String> fillHash(
			Hashtable<String, String> hash, Partition p, String currentBits) {
		if (p.getValue() != null) {
			hash.put(p.getValue(), currentBits);
		} else {
			fillHash(hash, p.getLeft(), currentBits + "0");
			fillHash(hash, p.getRight(), currentBits + "1");
		}
		return hash;
	}

	/**
	 * Get the adjacency matrix that is used to create the graph of a tree.
	 * 
	 * @param p
	 *          The root of the tree
	 * @param preorder
	 * @return the matrix
	 */
	public static int[][] fillAdj(Partition p, Vector<Integer> preorder) {
		int[][] adj = new int[p.elements()][p.elements()];
		if (p.getValue() != null)
			return adj;
		return adj;
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
		return "Shannon Fano Kodierung";
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		String[] strArray = (String[]) primitives.get("stringArray");
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
	
	@Override
	public void init() {
	  lang = new AnimalScript("Shannon Fano", "Florian Lindner", 800, 600);
	  lang.setStepMode(true);
	}

  @Override
  public String getAlgorithmName() {
    return "Shannon-Fano Komprimierung";
  }
}
