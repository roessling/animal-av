package generators.compression.huffman.guielements.priorityqueue;

import generators.compression.huffman.style.HuffmanStyle;
import generators.compression.huffman.utils.ProbabilityFormatter;

import java.util.ArrayList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class PriorityQueue {

	private Language lang;

	private HuffmanStyle huffmanStyle;

	private StringArray priorityQueueHeadCol;
	private ArrayList<PriorityQueueElement> elements;

	private PQInsertCounter insertCounter;

	public void setInsertCounter(PQInsertCounter insertCounter) {
		this.insertCounter = insertCounter;
	}

	public PriorityQueue(Language lang, Offset initialOffset, HuffmanStyle huffmanStyle) {

		this.lang = lang;
		this.huffmanStyle = huffmanStyle;

		elements = new ArrayList<PriorityQueueElement>();
		priorityQueueHeadCol = lang.newStringArray(initialOffset, new String[] {
				"node no.", "frequency", "probability" }, "priorityQueueCol0",
				null, (ArrayProperties) huffmanStyle
						.getProperties(HuffmanStyle.ARRAY_FIRST_COL));
	}

	public int size() {

		return elements.size();
	}

	public PriorityQueueElement getElement(int id) {

		return elements.get(id);
	}

	public StringArray getHeadElement() {

		return priorityQueueHeadCol;
	}

	public void hide() {

		priorityQueueHeadCol.hide();
		for (PriorityQueueElement element : elements) {
			element.hide();
		}
	}

	public void highlightElement(int id) {

		elements.get(id).highlight();
	}

	public void unhighlightElement(int id) {

		elements.get(id).unhighlight();
	}

	public void removeFirstTwo() {

		PriorityQueueElement firstNode = elements.get(0);
		PriorityQueueElement secondNode = elements.get(1);

		// Remove the first two nodes from the priority queue
		firstNode.hide();
		secondNode.hide();
		for (int i = 2; i < elements.size(); i++) {
			Offset offset = new Offset(0, 0, elements.get(i - 2)
					.getStringArray(), null);
			elements.get(i).getStringArray()
					.moveTo(null, null, offset, null, null);
		}
		elements.remove(0);
		elements.remove(0);
	}

	/**
	 * inserts new node into the priority queue
	 * 
	 * @param id
	 *            the id of the node
	 * @param freq
	 *            the node's frequency
	 * @param prob
	 *            the node's probability
	 */
	public void insertNode(int id, int freq, float prob) {

		if (insertCounter != null) {
			insertCounter.incrementNrInserts(1);
		}

		Coordinates dummyCoordinate = new Coordinates(0, 0);
		String[] data = new String[3];
		data[0] = Integer.toString(id);
		data[1] = Integer.toString(freq);
		data[2] = ProbabilityFormatter.format(prob);
		StringArray priorityQueueNode = lang.newStringArray(dummyCoordinate,
				data, "priorityQueueNode" + id, null,
				(ArrayProperties) huffmanStyle
						.getProperties(HuffmanStyle.ARRAY_REST));

		int insertionPoint = 0;
		if (!(elements.size() == 0)) {
			int insertFreq = freq;
			insertionPoint = elements.size();
			for (int j = 0; j < elements.size(); j++) {

				PriorityQueueElement col1 = elements.get(j);

				float currentFreq = col1.getFrequency();

				if (insertFreq < currentFreq) {
					insertionPoint = j;
					break;
				}
			}
		}

		StringArray insertionPointLeftElement = insertionPoint == 0 ? priorityQueueHeadCol
				: elements.get(insertionPoint - 1).getStringArray();

		priorityQueueNode.moveTo(null, null, new Offset(0, 0,
				insertionPointLeftElement, AnimalScript.DIRECTION_NE), null,
				null);

		for (int k = insertionPoint; k < elements.size(); k++) {

			Offset offset = new Offset(0, 0, elements.get(k).getStringArray(),
					AnimalScript.DIRECTION_NE);
			elements.get(k).getStringArray()
					.moveTo(null, null, offset, null, null);
		}

		elements.add(insertionPoint, new PriorityQueueElement(id, freq, prob,
				priorityQueueNode));
	}
}
