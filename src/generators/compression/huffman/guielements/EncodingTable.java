package generators.compression.huffman.guielements;

import generators.compression.huffman.guielements.distributiontable.DistributionTable;
import generators.compression.huffman.guielements.distributiontable.DistributionTableElement;
import generators.compression.huffman.pregeneration.PreGenerator;
import generators.compression.huffman.style.HuffmanStyle;

import java.util.ArrayList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class EncodingTable {

	private Language lang;

	private StringArray encodingTableHeadCol;
	private ArrayList<StringArray> encodingTableCols;

	private PreGenerator preGenerator;

	private HuffmanStyle huffmanStyle;

	public EncodingTable(Language lang, PreGenerator preGenerator, Offset initialOffset,
			DistributionTable distrTable, HuffmanStyle huffmanStyle) {

		this.lang = lang;
		this.preGenerator = preGenerator;
		this.huffmanStyle = huffmanStyle;

		encodingTableCols = new ArrayList<StringArray>();
		encodingTableHeadCol = lang.newStringArray(initialOffset, new String[] {
				"char", "encoding   " }, "encodingTableCol0", null,
				(ArrayProperties) huffmanStyle
						.getProperties(HuffmanStyle.ARRAY_FIRST_COL));

		StringArray offsetReference = encodingTableHeadCol;
		for (int i = 0; i < distrTable.size(); i++) {
			DistributionTableElement distrTableElement = distrTable.getElement(i);
			String symbol = Character.toString(distrTableElement.getSymbol());
			StringArray encodingTableCol = lang.newStringArray(new Offset(0, 0,
					offsetReference, AnimalScript.DIRECTION_NE), new String[] {
					symbol, "" }, "encodingTableCol" + (i + 1), null,
					(ArrayProperties) huffmanStyle
							.getProperties(HuffmanStyle.ARRAY_REST));

			encodingTableCols.add(encodingTableCol);
			offsetReference = encodingTableCol;
		}
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
	 * huffman algorithm to its position in the animation of the result
	 */
	public void moveEncodingTable(Offset offset) {

		// moving element groups is not that trivial...
		// the position of the elements is updated at initialization and after a
		// step
		// so we need invisible align dummies, which simulate elements after
		// movement
		encodingTableHeadCol.moveTo(null, null, offset, null,
				new MsTiming(1000));

		String[] encodingTableHeadData = new String[3];
		encodingTableHeadData[0] = encodingTableHeadCol.getData(0);
		encodingTableHeadData[1] = encodingTableHeadCol.getData(1);

		StringArray offsetReference = lang.newStringArray(offset,
				encodingTableHeadData, "alignDummy", null,
				(ArrayProperties) huffmanStyle
						.getProperties(HuffmanStyle.ARRAY_FIRST_COL));
		offsetReference.hide();

		for (StringArray encodingTableCol : encodingTableCols) {
			encodingTableCol.moveTo(null, null, new Offset(0, 0,
					offsetReference, AnimalScript.DIRECTION_NE), null,
					new MsTiming(1000));

			String[] encodingTableColData = new String[2];
			encodingTableColData[0] = encodingTableCol.getData(0);
			encodingTableColData[1] = encodingTableCol.getData(1);

			offsetReference = lang.newStringArray(new Offset(0, 0,
					offsetReference, AnimalScript.DIRECTION_NE),
					encodingTableColData, "alignDummy", null,
					(ArrayProperties) huffmanStyle
							.getProperties(HuffmanStyle.ARRAY_REST));
			offsetReference.hide();
		}
	}

	/**
	 * adds 0s to the encoding of the leaves below the left child and 1s to the
	 * encoding of the leaves below the right child of the new parent node
	 * 
	 * @param parentID
	 *            the ID of the new node
	 */
	public void updateEncodingTable(int parentID) {

		ArrayList<Integer> leftLeaves = preGenerator
				.getLeftChildLeaves(parentID);
		ArrayList<Integer> rightLeaves = preGenerator
				.getRightChildLeaves(parentID);

		for (int i = 0; i < encodingTableCols.size(); i++) {
			StringArray encodingTableCol = encodingTableCols.get(i);

			if (leftLeaves.contains(i + 1)) {
				String encoding = "0" + encodingTableCol.getData(1);
				encodingTableCol.put(1, encoding, null, null);
			}

			if (rightLeaves.contains(i + 1)) {
				String encoding = "1" + encodingTableCol.getData(1);
				encodingTableCol.put(1, encoding, null, null);
			}
		}

		// In Animal, the positions of GUI elements are updated after
		// initialization and at the beginning of a new step. Thus, there is no
		// way to realign the encoding table columns without replacing them with
		// new columns.
		ArrayList<StringArray> newEncodingTableCols = new ArrayList<StringArray>();

		StringArray offsetReference = encodingTableHeadCol;
		for (StringArray encodingTableCol : encodingTableCols) {
			StringArray newEncodingTableCol = lang.newStringArray(new Offset(0,
					0, offsetReference, AnimalScript.DIRECTION_NE),
					new String[] { encodingTableCol.getData(0),
							encodingTableCol.getData(1) }, "xy", null,
					(ArrayProperties) huffmanStyle
							.getProperties(HuffmanStyle.ARRAY_REST));
			offsetReference = newEncodingTableCol;
			newEncodingTableCols.add(newEncodingTableCol);
			encodingTableCol.hide();
		}

		encodingTableCols = newEncodingTableCols;
	}

	public void hide() {
		
		encodingTableHeadCol.hide();
		for(StringArray encodingTableCol : encodingTableCols) {
			encodingTableCol.hide();
		}
		
	}	
}
