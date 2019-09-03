package generators.compression.shannon_fano.guielements.nodearray;

import generators.compression.shannon_fano.style.ShannonFanoStyle;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class NodeInsertCounter {

	private Language lang;

	private ShannonFanoStyle shannonFanoStyle;

	private Text label;
	private Rect progressBar;
	private Text insertCount;

	private int nrInserts;

	public NodeInsertCounter(Language lang, Offset initialOffset,
			ShannonFanoStyle shannonFanoStyle) {

		this.lang = lang;
		this.shannonFanoStyle = shannonFanoStyle;

		label = lang.newText(initialOffset, "Node inserts:",
				"#ILabel", null, (TextProperties) shannonFanoStyle
						.getProperties(ShannonFanoStyle.PLAINTEXT));
		progressBar = lang.newRect(new Offset(10, 0, label,
				AnimalScript.DIRECTION_NE), new Offset(11, 0, label,
				AnimalScript.DIRECTION_SE), "#IBox", null,
				(RectProperties) shannonFanoStyle
						.getProperties(ShannonFanoStyle.INSERT_COUNTER));
		insertCount = lang.newText(new Offset(0, 0, progressBar,
				AnimalScript.DIRECTION_SW), "0", "#I", null,
				(TextProperties) shannonFanoStyle
						.getProperties(ShannonFanoStyle.PLAINTEXT));
	}
	
	public Text getLabel() {
		return label;
	}

	/**
	 * increment the number of inserts by <em>howMany</em>
	 * 
	 * @param howMany
	 *            determines how many inserts have taken place
	 */
	public void incrementNrInserts(int howMany) {
		nrInserts += howMany;
		if (progressBar != null) {
			progressBar.moveBy("translate #2", howMany * 2, 0, null, null);
		}

		insertCount.setText(Integer.toString(nrInserts), null, null);
	}

	public void hide() {
		label.hide();
		progressBar.hide();
		insertCount.hide();
	}

	public void show() {
		label.show();
		progressBar.show();
		insertCount.show();
	}

	public void moveTo(Offset offset, Timing duration) {

		label.moveTo(null, null, offset, null, duration);

		Text alignDummyInsertLabel = lang.newText(offset,
				"Node inserts:", "alignDummy", null,
				(TextProperties) shannonFanoStyle
						.getProperties(ShannonFanoStyle.PLAINTEXT));
		alignDummyInsertLabel.hide();

		progressBar.moveTo(null, null, new Offset(10, 0,
				alignDummyInsertLabel, AnimalScript.DIRECTION_NE), null,
				duration);

		Rect alignDummyInsertBox = lang.newRect(new Offset(10, 0,
				alignDummyInsertLabel, AnimalScript.DIRECTION_NE), new Offset(
				11, 0, alignDummyInsertLabel, AnimalScript.DIRECTION_SE), "alignDummy",
				null, (RectProperties) shannonFanoStyle
						.getProperties(ShannonFanoStyle.INSERT_COUNTER));
		alignDummyInsertBox.hide();

		insertCount.moveTo(null, null, new Offset(0, 0, alignDummyInsertBox,
				AnimalScript.DIRECTION_SW), null, duration);
	}
}
