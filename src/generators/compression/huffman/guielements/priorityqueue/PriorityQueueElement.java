package generators.compression.huffman.guielements.priorityqueue;

import algoanim.primitives.StringArray;

public class PriorityQueueElement {

	int id;
	int frequency;
	float probability;

	private StringArray stringArray;

	public int getFrequency() {
		return frequency;
	}

	public StringArray getStringArray() {
		return stringArray;
	}

	public int getID() {
		return id;
	}

	public PriorityQueueElement(int id, int frequency, float probability,
			StringArray stringArray) {

		this.id = id;
		this.frequency = frequency;
		this.probability = probability;

		this.stringArray = stringArray;
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
