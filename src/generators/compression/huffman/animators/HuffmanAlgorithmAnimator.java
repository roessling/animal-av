package generators.compression.huffman.animators;

import generators.compression.huffman.guielements.EncodingTable;
import generators.compression.huffman.guielements.distributiontable.DistributionTable;
import generators.compression.huffman.guielements.distributiontable.DistributionTableElement;
import generators.compression.huffman.guielements.priorityqueue.PQInsertCounter;
import generators.compression.huffman.guielements.priorityqueue.PriorityQueue;
import generators.compression.huffman.guielements.priorityqueue.PriorityQueueElement;
import generators.compression.huffman.guielements.tree.Tree;
import generators.compression.huffman.pregeneration.PreGenerator;
import generators.compression.huffman.style.HuffmanStyle;

import java.util.HashMap;
import java.util.ResourceBundle;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Offset;

public class HuffmanAlgorithmAnimator extends ChapterAnimator {

	private static final String CHAPTER_LABEL = "Huffman Coding Algorithm";

	private Text headline;
	private SourceCode sc;
	private PriorityQueue pq;
	private PQInsertCounter insertCounter;
	private DistributionTable distrTable;
	private EncodingTable encTable;
	private Tree tree;

	private PreGenerator preGenerator;

	public EncodingTable getEncTable() {
		return encTable;
	}

	public Tree getTree() {
		return tree;
	}
	
	public PQInsertCounter getInsertCounter() {
		return insertCounter;
	}
	
	public HuffmanAlgorithmAnimator(Language lang, HuffmanStyle huffmanStyle,
			ResourceBundle messages, Text headline, DistributionTable distrTable) {

		super(lang, huffmanStyle, messages, CHAPTER_LABEL);

		this.headline = headline;
		this.distrTable = distrTable;
	}

	@Override
	public void animate() {

		super.animate();

		initSourceCode();

		StringArray offsetReference = lang.newStringArray(new Offset(250, 0,
				headline, AnimalScript.DIRECTION_NE), new String[] { "1", "2",
				"3" }, "alignDummy", null, (ArrayProperties) huffmanStyle
				.getProperties(HuffmanStyle.ARRAY_FIRST_COL));
		Offset offset = new Offset(0, 20, offsetReference,
				AnimalScript.DIRECTION_SW);
		offsetReference.hide();
		pq = new PriorityQueue(lang, offset, huffmanStyle);
		Offset insertCounterOffset = new Offset(0, 40, sc,
				AnimalScript.DIRECTION_SW);
		insertCounter = new PQInsertCounter(lang, insertCounterOffset, huffmanStyle);
		pq.setInsertCounter(insertCounter);
		
		preGenerator = new PreGenerator(distrTable.getSymbols(),
				distrTable.getFrequencies(), distrTable.getSumFrequencies());
		preGenerator.preGenerate();
		int treeHeight = preGenerator.getTreeHeight();
		HashMap<Integer, Integer> bottomIdToPosition = preGenerator
				.getBottomNodePositions();

		offset = new Offset(0, treeHeight * Tree.Y_STEP_SIZE * -1 + 120,
				pq.getHeadElement(), AnimalScript.DIRECTION_SW);
		encTable = new EncodingTable(lang, preGenerator, offset, distrTable,
				huffmanStyle);

		tree = new Tree(lang, huffmanStyle, pq.getHeadElement(),
				bottomIdToPosition, treeHeight);

		animateHuffmanAlgorithm();

		doTransition();
	}

	/**
	 * initializes the source code element
	 */
	private void initSourceCode() {

		sc = lang.newSourceCode(new Offset(0, 20, headline,
				AnimalScript.DIRECTION_SW), "sourceCode", null,
				(SourceCodeProperties) huffmanStyle
						.getProperties(HuffmanStyle.SOURCECODE));

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display delay
		sc.addCodeLine("1. For each symbol create a leaf node", null, 0, null); // 0
		sc.addCodeLine("    and add it to the priority queue.", null, 0, null); // 1
		sc.addCodeLine("", null, 0, null); // 2
		sc.addCodeLine("2. While there is more than one node in the queue:",
				null, 0, null); // 3
		sc.addCodeLine("", null, 0, null); // 4
		sc.addCodeLine("1. Remove the two nodes of highest priority", null, 1,
				null); // 5
		sc.addCodeLine("    (lowest probability) from the queue", null, 1, null); // 6
		sc.addCodeLine("2. Create a new node and add these two nodes", null, 1,
				null); // 7
		sc.addCodeLine("    as children. The probability is equal to the",
				null, 1, null); // 8
		sc.addCodeLine("    sum of the two nodes' probabilities.", null, 1,
				null); // 9
		sc.addCodeLine("3. Add the new node to the queue.", null, 1, null); // 10
		sc.addCodeLine("", null, 0, null); // 11
		sc.addCodeLine("3. The remaining node is the root node of the tree.",
				null, 0, null); // 12
	}

	/**
	 * This method is the root of all the animation steps, which build up the
	 * encoding tree based on the given characters and their frequencies.
	 */
	public void animateHuffmanAlgorithm() {

		doFirstAlgorithmStep();
		doSecondAlgorithmStep();
		doThirdAlgorithmStep();
	}

	/**
	 * animates the first algorithm step
	 */
	private void doFirstAlgorithmStep() {

		sc.highlight(0);
		sc.highlight(1);

		lang.nextStep();

		distrTable.highlightElement(0);

		lang.nextStep();

		for (int i = 0; i < distrTable.size(); i++) {
			insertInitialNode(i);
			lang.nextStep();
		}

		// Unhighlight last column in distributionTable
		distrTable.unhighlightElement(distrTable.size() - 1);

		// Unhighlight first step in the source code
		sc.unhighlight(0);
		sc.unhighlight(1);
	}

	/**
	 * animates the second algorithm step (the while loop)
	 */
	private void doSecondAlgorithmStep() {

		while (pq.size() > 1) {
			doLoopRun();
		}
	}

	/**
	 * animates the third algorithm step
	 */
	private void doThirdAlgorithmStep() {

		sc.unhighlight(3);
		sc.highlight(12);

		lang.nextStep();
	}

	/**
	 * inserts StringArray i of the distribution table into the priority queue
	 * and draws the initial node at the bottom of the new tree
	 */
	private void insertInitialNode(int i) {

		DistributionTableElement distrTableElement = distrTable.getElement(i);
		int id = i + 1;
		char charac = distrTableElement.getSymbol();
		int freq = distrTableElement.getFrequency();
		float prob = distrTableElement.getProbability();

		tree.makeBottomNode(i + 1, charac, freq);

		pq.insertNode(id, freq, prob);

		lang.nextStep();

		distrTable.unhighlightElement(i);

		if (!(distrTable.size() - 1 == i)) {
			distrTable.highlightElement(i + 1);
		}
	}

	/**
	 * does one loop run of the second algorithm step (the while loop)
	 */
	private void doLoopRun() {

		// Highlight second step in the source code
		sc.highlight(3);

		lang.nextStep();

		// Unhighlight second step in the source code
		sc.unhighlight(3);

		// Highlight first step in the while loop
		sc.highlight(5);
		sc.highlight(6);

		lang.nextStep();

		// Highlight the first two nodes of the priority queue
		PriorityQueueElement firstNode = pq.getElement(0);
		PriorityQueueElement secondNode = pq.getElement(1);
		pq.highlightElement(0);
		pq.highlightElement(1);

		lang.nextStep();

		// Remove the first two nodes from the priority queue
		pq.removeFirstTwo();

		lang.nextStep();

		// Unhighlight first step in the while loop
		sc.unhighlight(5);
		sc.unhighlight(6);

		// Highlight second step in the while loop
		sc.highlight(7);
		sc.highlight(8);
		sc.highlight(9);

		lang.nextStep();

		// make and draw the new node
		int freq = firstNode.getFrequency() + secondNode.getFrequency();
		float prob = freq / (float) distrTable.getSumFrequencies();

		tree.makeParentNode(firstNode.getID(), secondNode.getID(), freq);
		encTable.updateEncodingTable(tree.size());

		lang.nextStep();

		// Unhighlight second step in the while loop
		sc.unhighlight(7);
		sc.unhighlight(8);
		sc.unhighlight(9);

		// Highlight third step in the while loop
		sc.highlight(10);

		lang.nextStep();

		pq.insertNode(tree.size(), freq, prob);

		lang.nextStep();

		// Unhighlight third step in the while loop
		sc.unhighlight(10);

		// Highlight second step in the source code
		sc.highlight(3);

		lang.nextStep();
	}

	protected void doTransition() {
		
		// hide everything but the headline and the encoding table
		tree.hide();
		sc.hide();
		distrTable.hide();
		pq.hide();
		insertCounter.hide();
		
		// move encoding table to the top
		encTable.moveEncodingTable(new Offset(0, 50, headline,
				AnimalScript.DIRECTION_SW));
	}
}
