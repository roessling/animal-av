package generators.compression.shannon_fano.animators;

import generators.compression.shannon_fano.guielements.EncodingTable;
import generators.compression.shannon_fano.guielements.distributiontable.DistributionTable;
import generators.compression.shannon_fano.guielements.nodearray.NodeArray;
import generators.compression.shannon_fano.guielements.nodearray.NodeInsertCounter;
import generators.compression.shannon_fano.guielements.tree.AbstractNode;
import generators.compression.shannon_fano.guielements.tree.NodeSet;
import generators.compression.shannon_fano.guielements.tree.Tree;
import generators.compression.shannon_fano.guielements.tree.TreeNode;
import generators.compression.shannon_fano.style.ShannonFanoStyle;

import java.util.ArrayList;
import java.util.ResourceBundle;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class ShannonFanoAlgorithmAnimator extends ChapterAnimator {

	private static final String CHAPTER_LABEL = "Shannon-Fano Coding Algorithm";

	private Primitive offsetReference;

	private Text headline;
	private SourceCode sc;
	private NodeInsertCounter insertCounter;
	private DistributionTable distrTable;
	private EncodingTable encTable;
	private NodeArray nodeArray;
	private Tree tree;

	public EncodingTable getEncTable() {
		return encTable;
	}

	public Tree getTree() {
		return tree;
	}

	public NodeInsertCounter getInsertCounter() {
		return insertCounter;
	}

	public ShannonFanoAlgorithmAnimator(Language lang, ShannonFanoStyle shannonFanoStyle, ResourceBundle messages,
			Text headline, DistributionTable distrTable) {

		super(lang, shannonFanoStyle, messages, CHAPTER_LABEL);

		this.headline = headline;
		this.distrTable = distrTable;
	}

	@Override
	public void animate() {

		super.animate();

		initSourceCode();

		// create offset reference
		offsetReference = lang.newStringArray(new Offset(250, 0, headline, AnimalScript.DIRECTION_NE), new String[] {
				"1", "2", "3" }, "alignDummy", null,
				(ArrayProperties) style.getProperties(ShannonFanoStyle.ARRAY_FIRST_COL));
		offsetReference.hide();

		Offset nodeArrayOffset = new Offset(0, 20, offsetReference, AnimalScript.DIRECTION_SW);
		nodeArray = new NodeArray(lang, nodeArrayOffset, style);

		Offset insertCounterOffset = new Offset(0, 40, sc, AnimalScript.DIRECTION_SW);
		insertCounter = new NodeInsertCounter(lang, insertCounterOffset, style);

		tree = new Tree(lang, style, nodeArray.getHeadElement());

		Offset encTableOffset = new Offset(0, 40, insertCounter.getLabel(), AnimalScript.DIRECTION_SW);
		encTable = new EncodingTable(encTableOffset, distrTable.getSymbols(), tree);

		animateAlgorithm();
		
		nodeArray.hide();

		doTransition();
	}

	/**
	 * Initializes the source code element
	 */
	private void initSourceCode() {

		sc = lang.newSourceCode(new Offset(0, 20, headline, AnimalScript.DIRECTION_SW), "sourceCode", null,
				(SourceCodeProperties) style.getProperties(ShannonFanoStyle.SOURCECODE));

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display delay
		sc.addCodeLine("1. For each symbol create a leaf node", null, 0, null); // 0
		sc.addCodeLine("    and sort these nodes according to the frequency,", null, 0, null); // 1
		sc.addCodeLine("    with the most frequently occurring symbols at ", null, 0, null); // 2
		sc.addCodeLine("    the left and the least common at the right.", null, 0, null); // 3
		sc.addCodeLine("", null, 0, null); // 4
		sc.addCodeLine("2. Divide the set of nodes into two parts, so that the ", null, 0, null); // 5
		sc.addCodeLine("    total frequency of the left part is as close to ", null, 0, null); // 6
		sc.addCodeLine("    the total of the right as possible.", null, 0, null); // 7
		sc.addCodeLine("", null, 0, null); // 8
		sc.addCodeLine("3. Insert a common parent node for the two sets and", null, 0, null); // 9
		sc.addCodeLine("    assign the binary digit '0' to the left child and '1' to the", null, 0, null); // 10
		sc.addCodeLine("    right child. ", null, 0, null); // 11
		sc.addCodeLine("", null, 0, null); // 12
		sc.addCodeLine("4. If there is more than one node in any of the two parts, ", null, 0, null); // 13
		sc.addCodeLine("    apply steps 2 to 4 recursively to these.", null, 0, null); // 14
	}

	/**
	 * This method is the root of all the animation steps, which build up the
	 * encoding tree based on the given characters and their frequencies.
	 */
	public void animateAlgorithm() {
		NodeSet s = intialAlgorithmStep(); // first step in the algorithm
		recursiveAlgorithmSteps(s); // steps 2 - 4 in the algorithm
	}

	/**
	 * Animates the first algorithm step
	 */
	private NodeSet intialAlgorithmStep() {

		highlightStep(1);

		lang.nextStep();

		// sort nodes and get distribution
		distrTable.sort();
		char[] symbols = distrTable.getSymbols();
		int[] frequencies = distrTable.getFrequencies();
		float[] probabilities = distrTable.getProbabilities();

		// create initial nodes
		ArrayList<AbstractNode> initialNodes = new ArrayList<AbstractNode>(distrTable.size());
		for (int i = 0; i < distrTable.size(); i++) {
			distrTable.highlightElement(i);

			TreeNode node = new TreeNode(frequencies[i], probabilities[i], symbols[i], i * Tree.STEP_SIZE_X,
					Tree.OFFSET_TOP, tree);
			insertCounter.incrementNrInserts(1);
			initialNodes.add(node);
			nodeArray.insertElement(node);

			lang.nextStep();

			distrTable.unhighlightElement(i);
		}

		// add initial set to tree and return it for the next step
		NodeSet s = new NodeSet(initialNodes, tree);
		tree.add(s);

		lang.nextStep();
		unhighlightStep(1);

		return s;
	}

	/**
	 * Animates the steps 2 - 4 in the algorithm
	 * 
	 * @param node
	 */
	private void recursiveAlgorithmSteps(AbstractNode node) {

		// a set of nodes must be there, otherwise split fails
		if (node instanceof TreeNode) {
			return;
		}

		highlightStep(2);

		// split current set S into two sets S1 and S2
		AbstractNode[] sets = ((NodeSet) node).split();
		sets[0].hide();
		sets[1].hide();

		// move sets one layer down
		Timing duration = new MsTiming(Tree.MOVE_DOWN_DURATION);
		sets[0].moveBy(0, Tree.STEP_SIZE_Y, duration);
		sets[1].moveBy(0, Tree.STEP_SIZE_Y, duration);

		lang.nextStep();
		unhighlightStep(2);
		highlightStep(3);

		// add new parent node for S1 and S2
		int freq = sets[0].getFrequency() + sets[1].getFrequency();
		float prob = sets[0].getProbability() + sets[1].getProbability();
		TreeNode newParent = new TreeNode(freq, prob, node.getCenterX(), node.getCenterY() - 5, tree);
		newParent.setLeftNode(sets[0], "0", Tree.STEP_SIZE_Y);
		newParent.setRightNode(sets[1], "1", Tree.STEP_SIZE_Y);
		if (node.getParent() != null) {
			if (node.getParent().getLeftNode() == node) {
				node.getParent().setLeftNode(newParent, "0");
			} else if (node.getParent().getRightNode() == node) {
				node.getParent().setRightNode(newParent, "1");
			}
		}
		tree.add(newParent);
		if (tree.getRoot() == null) {
			tree.setRoot(newParent);
		}
		nodeArray.insertElement(newParent);
		insertCounter.incrementNrInserts(1);

		// a new node has been inserted, so update the encoding table
		encTable.update();

		lang.nextStep();
		unhighlightStep(3);
		highlightStep(4);

		lang.nextStep();
		unhighlightStep(4);

		recursiveAlgorithmSteps(sets[0]);
		recursiveAlgorithmSteps(sets[1]);
	}

	private void highlightStep(int stepNumber) {
		switch (stepNumber) {
		case 1:
			sc.highlight(0);
			sc.highlight(1);
			sc.highlight(2);
			sc.highlight(3);
			break;
		case 2:
			sc.highlight(5);
			sc.highlight(6);
			sc.highlight(7);
			break;
		case 3:
			sc.highlight(9);
			sc.highlight(10);
			sc.highlight(11);
			break;
		case 4:
			sc.highlight(13);
			sc.highlight(14);
		}
	}

	private void unhighlightStep(int stepNumber) {
		switch (stepNumber) {
		case 1:
			sc.unhighlight(0);
			sc.unhighlight(1);
			sc.unhighlight(2);
			sc.unhighlight(3);
			break;
		case 2:
			sc.unhighlight(5);
			sc.unhighlight(6);
			sc.unhighlight(7);
			break;
		case 3:
			sc.unhighlight(9);
			sc.unhighlight(10);
			sc.unhighlight(11);
			break;
		case 4:
			sc.unhighlight(13);
			sc.unhighlight(14);
		}
	}

	// /**
	// * Inserts StringArray i of the distribution table into the node array and
	// * draws the initial node at the top of the new tree (to be moved down
	// later)
	// */
	// private void insertInitialNode(int i) {
	//
	// DistributionTableElement distrTableElement = distrTable.getElement(i);
	// int id = i + 1;
	// char charac = distrTableElement.getSymbol();
	// int freq = distrTableElement.getFrequency();
	// float prob = distrTableElement.getProbability();
	//
	// // place a new tree node
	// tree.placeInitialNode(i + 1, charac, freq);
	//
	// // add an entry to the node array
	// nodeArray.insertNode(id, freq, prob);
	//
	// lang.nextStep();
	//
	// distrTable.unhighlightElement(i);
	//
	// if (!(distrTable.size() - 1 == i)) {
	// distrTable.highlightElement(i + 1);
	// }
	// }

	// /**
	// * does one loop run of the second algorithm step (the while loop)
	// */
	// private void doLoopRun() {
	//
	// // Highlight second step in the source code
	// sc.highlight(3);
	//
	// lang.nextStep();
	//
	// // Unhighlight second step in the source code
	// sc.unhighlight(3);
	//
	// // Highlight first step in the while loop
	// sc.highlight(5);
	// sc.highlight(6);
	//
	// lang.nextStep();
	//
	// // Highlight the first two nodes of the priority queue
	// NodeArrayElement firstNode = nodeArray.getElement(0);
	// NodeArrayElement secondNode = nodeArray.getElement(1);
	// nodeArray.highlightElement(0);
	// nodeArray.highlightElement(1);
	//
	// lang.nextStep();
	//
	// // Remove the first two nodes from the priority queue
	// nodeArray.removeFirstTwo();
	//
	// lang.nextStep();
	//
	// // Unhighlight first step in the while loop
	// sc.unhighlight(5);
	// sc.unhighlight(6);
	//
	// // Highlight second step in the while loop
	// sc.highlight(7);
	// sc.highlight(8);
	// sc.highlight(9);
	//
	// lang.nextStep();
	//
	// // make and draw the new node
	// int freq = firstNode.getFrequency() + secondNode.getFrequency();
	// float prob = freq / (float) distrTable.getSumFrequencies();
	//
	// tree.makeParentNode(firstNode.getID(), secondNode.getID(), freq);
	// encTable.updateEncodingTable(tree.size());
	//
	// lang.nextStep();
	//
	// // Unhighlight second step in the while loop
	// sc.unhighlight(7);
	// sc.unhighlight(8);
	// sc.unhighlight(9);
	//
	// // Highlight third step in the while loop
	// sc.highlight(10);
	//
	// lang.nextStep();
	//
	// nodeArray.insertNode(tree.size(), freq, prob);
	//
	// lang.nextStep();
	//
	// // Unhighlight third step in the while loop
	// sc.unhighlight(10);
	//
	// // Highlight second step in the source code
	// sc.highlight(3);
	//
	// lang.nextStep();
	// }

	protected void doTransition() {

		// hide everything but the headline and the encoding table
		tree.hide();
		sc.hide();
		distrTable.hide();
		nodeArray.hide();
		insertCounter.hide();

		// move encoding table to the top
		encTable.moveEncodingTable(new Offset(0, 40, headline, AnimalScript.DIRECTION_SW));
	}

	// public class OuterNodes {
	// public TreeNode leftmostNode;
	// public TreeNode rightmostNode;
	// }
}
