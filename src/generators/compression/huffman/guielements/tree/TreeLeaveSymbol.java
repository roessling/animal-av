package generators.compression.huffman.guielements.tree;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class TreeLeaveSymbol {

	private Rect nodeRect;
	private Text symbolText;
	private int id;
	private char symbol;

	public char getSymbol() {
		return symbol;
	}

	public int getID() {
		return id;
	}

	public TreeLeaveSymbol(Rect nodeRect, Text symbolText, int id, char symbol) {

		this.nodeRect = nodeRect;
		this.symbolText = symbolText;
		this.id = id;
		this.symbol = symbol;
	}

	public void hide() {
		nodeRect.hide();
		symbolText.hide();
	}

	public void show() {
		nodeRect.show();
		symbolText.show();
	}

	public void moveTo(Circle alignDummy, Timing duration, Language lang) {

		Offset rectOffset = new Offset(0, 2, alignDummy,
				AnimalScript.DIRECTION_SW);

		nodeRect.moveTo(null, null, rectOffset, null, duration);

		Rect rectAlignDummy = lang.newRect(new Offset(0, 2, alignDummy,
				AnimalScript.DIRECTION_SW), new Offset(0, 22, alignDummy,
				AnimalScript.DIRECTION_SE), "alignDummy", null);
		rectAlignDummy.hide();
		
		Offset nodeCharOffset = new Offset(-3, -10, rectAlignDummy,
				AnimalScript.DIRECTION_C);
		
		symbolText.moveTo(null, null, nodeCharOffset, null, duration);
		
	}
}
