package generators.compression.shannon_fano.guielements;

import generators.compression.shannon_fano.guielements.tree.AbstractNode;
import generators.compression.shannon_fano.guielements.tree.NodeSet;
import generators.compression.shannon_fano.guielements.tree.Tree;
import generators.compression.shannon_fano.guielements.tree.TreeNode;
import generators.compression.shannon_fano.style.ShannonFanoStyle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.StringArray;
import algoanim.properties.ArrayProperties;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import animal.main.Animal;

public class EncodingTable {
	int test;

	private Tree tree;

	private StringArray encodingTableHeadCol;
	private ArrayList<StringArray> encodingTableCols = new ArrayList<StringArray>();

	private HashMap<String, String> encodingTable = new HashMap<String, String>();

	/**
	 * Constructor
	 * 
	 * @param initialOffset
	 * @param distrTable
	 * @param lang
	 * @param style
	 */
	public EncodingTable(Offset initialOffset, char[] symbols, Tree tree) {

		this.tree = tree;

		// initialize (internal) encoding table
		for (char c : symbols) {
			encodingTable.put(Character.toString(c), "");
		}

		// initialize GUI
		initGUI(initialOffset);
	}

	public StringArray getHeadElement() {
		return encodingTableHeadCol;
	}

	public StringArray getElement(int id) {
		return encodingTableCols.get(id);
	}

	public int size() {
		return encodingTableCols.size();
	}

	/**
	 * moves the encoding table from its position during the animation of the
	 * shannon-fano algorithm to its position in the animation of the result
	 */
	public void moveEncodingTable(Offset offset) {

		// moving element groups is not that trivial...
		// the position of the elements is updated at initialization and after a
		// step
		// so we need invisible align dummies, which simulate elements after
		// movement
		encodingTableHeadCol.moveTo(null, null, offset, null, new MsTiming(1000));

		String[] encodingTableHeadData = new String[3];
		encodingTableHeadData[0] = encodingTableHeadCol.getData(0);
		encodingTableHeadData[1] = encodingTableHeadCol.getData(1);

		ArrayProperties arrayProp = (ArrayProperties) tree.getStyle().getProperties(ShannonFanoStyle.ARRAY_FIRST_COL);
		StringArray offsetReference = tree.getLang().newStringArray(offset, encodingTableHeadData, "alignDummy", null,
				arrayProp);
		offsetReference.hide();

		for (StringArray encodingTableCol : encodingTableCols) {
			encodingTableCol.moveTo(null, null, new Offset(0, 0, offsetReference, AnimalScript.DIRECTION_NE), null,
					new MsTiming(1000));

			String[] encodingTableColData = new String[2];
			encodingTableColData[0] = encodingTableCol.getData(0);
			encodingTableColData[1] = encodingTableCol.getData(1);

			arrayProp = (ArrayProperties) tree.getStyle().getProperties(ShannonFanoStyle.ARRAY_REST);
			offsetReference = tree.getLang().newStringArray(
					new Offset(0, 0, offsetReference, AnimalScript.DIRECTION_NE), encodingTableColData, "alignDummy",
					null, arrayProp);
			offsetReference.hide();
		}
	}

	/**
	 * Initializes the encoding table GUI elements by creating initial primitives.
	 */
	private void initGUI(Offset initialOffset) {
		ArrayProperties arrayProp = (ArrayProperties) tree.getStyle().getProperties(ShannonFanoStyle.ARRAY_FIRST_COL);
		encodingTableHeadCol = tree.getLang().newStringArray(initialOffset, new String[] { "char", "encoding   " },
				"encodingTableCol0", null, arrayProp);
		
		arrayProp = (ArrayProperties) tree.getStyle().getProperties(ShannonFanoStyle.ARRAY_REST);
		StringArray offsetReference = encodingTableHeadCol;
		
		for (String symbol : encodingTable.keySet()) {
			
			// create and add new column
			StringArray newCol = tree.getLang().newStringArray(
					new Offset(0, 0, offsetReference, AnimalScript.DIRECTION_NE),
					new String[] { symbol, encodingTable.get(symbol) }, "EncTableCol", null, arrayProp);
			offsetReference = newCol;
			encodingTableCols.add(newCol);
		}
	}
	
	/**
	 * Updates the entire encoding table, including the GUI.
	 */
	public void update() {
		updateEncodingTable();
		updateGUI();
	}

	/**
	 * Updates the encoding table GUI by replacing the old primitives with new
	 * ones.
	 */
	private void updateGUI() {

		// In Animal, the positions of GUI elements are updated after
		// initialization and at the beginning of a new step. Thus, there is no
		// way to realign the encoding table columns without replacing them with
		// new columns.
		
		ArrayList<StringArray> newEncodingTableCols = new ArrayList<StringArray>();
		StringArray offsetReference = encodingTableHeadCol;
		ArrayProperties arrayProp = (ArrayProperties) tree.getStyle().getProperties(ShannonFanoStyle.ARRAY_REST);
		
		for (StringArray encodingTableCol : encodingTableCols) {
			
			// hide old column
			encodingTableCol.hide();
			
			// create and add new column
			String symbol = encodingTableCol.getData(0);
			StringArray newCol = tree.getLang().newStringArray(
					new Offset(0, 0, offsetReference, AnimalScript.DIRECTION_NE),
					new String[] { symbol, encodingTable.get(symbol) }, "EncTableCol", null, arrayProp);
			offsetReference = newCol;
			newEncodingTableCols.add(newCol);
			if (!newCol.getData(1).equals(encodingTableCol.getData(1))) {
				// cell changed, so highlight text
				// note: does not work (probably an Animal bug)
				newCol.changeColor(AnimalScript.COLORCHANGE_TEXTCOLOR, Color.RED, null, null);
			}
		}
		
		// replace old encoding table columns
		encodingTableCols = newEncodingTableCols;
	}

	/**
	 * Determines the current encoding paths to all leaves by traversing the
	 * tree.
	 * 
	 * @see #encodingTable
	 */
	private void updateEncodingTable() {
		encodingTable.clear();
		traverse(tree.getRoot(), "");
	}

	private void traverse(AbstractNode node, String path) {
		if (node instanceof TreeNode) {
			TreeNode treeNode = (TreeNode) node;
			if (treeNode.isLeaf()) {
				encodingTable.put(Character.toString(treeNode.getSymbol()), path);
			} else {
				if (treeNode.getLeftNode() != null) {
					traverse(treeNode.getLeftNode(), path + "0");
				}
				if (treeNode.getRightNode() != null) {
					traverse(treeNode.getRightNode(), path + "1");
				}
			}
		} else if (node instanceof NodeSet) {
			for (AbstractNode n : ((NodeSet) node).getNodes()) {
				TreeNode treeNode = (TreeNode) n;
				encodingTable.put(Character.toString(treeNode.getSymbol()), path);
			}
		}
	}

	public void hide() {
		encodingTableHeadCol.hide();
		for (StringArray encodingTableCol : encodingTableCols) {
			encodingTableCol.hide();
		}
	}
}
