package generators.compression.huffman.pregeneration;

public class PreGenPQElement {

	private int id;
	private int frequency;
	private float probability;

	public PreGenPQElement(int id, int frequency, float probability) {

		this.id = id;
		this.frequency = frequency;
		this.probability = probability;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public float getProbability() {
		return probability;
	}

	public void setProbability(float probability) {
		this.probability = probability;
	}
}
