package generators.compression.huffman.guielements.priorityqueue;

import generators.compression.huffman.style.HuffmanStyle;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class PQInsertCounter {

	private Language lang;

	private HuffmanStyle huffmanStyle;

	private Text nrInsertLabel;
	private Rect nrInsertsBox;
	private Text nrInsertsText;

	private int nrInserts;

	public PQInsertCounter(Language lang, Offset initialOffset,
			HuffmanStyle huffmanStyle) {

		this.lang = lang;
		this.huffmanStyle = huffmanStyle;

		nrInsertLabel = lang.newText(initialOffset, "Priority queue inserts:",
				"#ILabel", null, (TextProperties) huffmanStyle
						.getProperties(HuffmanStyle.PLAINTEXT));
		nrInsertsBox = lang.newRect(new Offset(10, 0, nrInsertLabel,
				AnimalScript.DIRECTION_NE), new Offset(11, 0, nrInsertLabel,
				AnimalScript.DIRECTION_SE), "#IBox", null,
				(RectProperties) huffmanStyle
						.getProperties(HuffmanStyle.INSERT_COUNTER));
		nrInsertsText = lang.newText(new Offset(0, 0, nrInsertsBox,
				AnimalScript.DIRECTION_SW), "0", "#I", null,
				(TextProperties) huffmanStyle
						.getProperties(HuffmanStyle.PLAINTEXT));
	}

	/**
	 * increment the number of inserts by <em>howMany</em>
	 * 
	 * @param howMany
	 *            determines how many inserts have taken place
	 */
	void incrementNrInserts(int howMany) {
		nrInserts += howMany;
		if (nrInsertsBox != null) {
			nrInsertsBox.moveBy("translate #2", howMany * 2, 0, null, null);
		}

		nrInsertsText.setText(Integer.toString(nrInserts), null, null);
	}

	public void hide() {

		nrInsertLabel.hide();
		nrInsertsBox.hide();
		nrInsertsText.hide();
	}

	public void show() {

		nrInsertLabel.show();
		nrInsertsBox.show();
		nrInsertsText.show();
	}

	public void moveTo(Offset offset, Timing duration) {

		nrInsertLabel.moveTo(null, null, offset, null, duration);

		Text alignDummyInsertLabel = lang.newText(offset,
				"Priority queue Inserts:", "alignDummy", null,
				(TextProperties) huffmanStyle
						.getProperties(HuffmanStyle.PLAINTEXT));
		alignDummyInsertLabel.hide();

		nrInsertsBox.moveTo(null, null, new Offset(10, 0,
				alignDummyInsertLabel, AnimalScript.DIRECTION_NE), null,
				duration);

		Rect alignDummyInsertBox = lang.newRect(new Offset(10, 0,
				alignDummyInsertLabel, AnimalScript.DIRECTION_NE), new Offset(
				11, 0, alignDummyInsertLabel, AnimalScript.DIRECTION_SE), "alignDummy",
				null, (RectProperties) huffmanStyle
						.getProperties(HuffmanStyle.INSERT_COUNTER));
		alignDummyInsertBox.hide();

		nrInsertsText.moveTo(null, null, new Offset(0, 0, alignDummyInsertBox,
				AnimalScript.DIRECTION_SW), null, duration);
	}
}
