package generators.compression.huffman.guielements.distributiontable;

import generators.compression.huffman.utils.ProbabilityFormatter;
import algoanim.primitives.StringArray;

public class DistributionTableElement {

	private char symbol;
	private int frequency;
	private float probability;

	private StringArray stringArray;

	public DistributionTableElement(Character symbol, int frequency,
			float probability, StringArray stringArray) {

		this.symbol = symbol;
		this.frequency = frequency;
		this.stringArray = stringArray;
	}

	public StringArray getStringArray() {
		return stringArray;
	}

	public int getFrequency() {
		return frequency;
	}

	public char getSymbol() {
		return symbol;
	}

	public float getProbability() {
		return probability;
	}

	public void update(int frequency, float probability) {

		this.frequency = frequency;
		this.probability = probability;

		stringArray.put(1, Integer.toString(frequency), null, null);
		stringArray
				.put(2, ProbabilityFormatter.format(probability), null, null);
	}

	public void hide() {
		stringArray.hide();
	}

	public void highlight() {

		stringArray.highlightCell(0, null, null);
		stringArray.highlightCell(1, null, null);
		stringArray.highlightCell(2, null, null);
	}

	public void unhighlight() {

		stringArray.unhighlightCell(0, null, null);
		stringArray.unhighlightCell(1, null, null);
		stringArray.unhighlightCell(2, null, null);
	}
}
