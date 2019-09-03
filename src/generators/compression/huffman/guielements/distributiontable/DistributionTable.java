package generators.compression.huffman.guielements.distributiontable;

import generators.compression.huffman.utils.ProbabilityFormatter;

import java.util.ArrayList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class DistributionTable {

	private ArrayProperties arrayPropsFirstCol;
	private ArrayProperties arrayPropsRest;

	private Language lang;

	private StringArray distributionTableHeadCol;

	private ArrayList<DistributionTableElement> elements;

	/**
	 * the total number of chars in the input string
	 */
	private int sumFrequencies;

	public int getSumFrequencies() {
		return sumFrequencies;
	}

	public int[] getFrequencies() {

		int[] frequencies = new int[elements.size()];
		for (int i = 0; i < elements.size(); i++) {
			frequencies[i] = elements.get(i).getFrequency();
		}
		return frequencies;
	}

	public char[] getSymbols() {

		char[] symbols = new char[elements.size()];
		for (int i = 0; i < elements.size(); i++) {
			symbols[i] = elements.get(i).getSymbol();
		}
		return symbols;
	}

	public DistributionTable(Language lang, Offset initialOffset,
			ArrayProperties arrayPropsFirstCol, ArrayProperties arrayPropsRest) {

		this.lang = lang;
		this.arrayPropsFirstCol = arrayPropsFirstCol;
		this.arrayPropsRest = arrayPropsRest;

		distributionTableHeadCol = lang.newStringArray(initialOffset,
				new String[] { "char", "frequency", "probability" },
				"distributionTableCol0", null, arrayPropsFirstCol);
		elements = new ArrayList<DistributionTableElement>();
	}

	public int size() {
		return elements.size();
	}

	public DistributionTableElement getElement(int id) {
		return elements.get(id);
	}

	public StringArray getHeadCol() {
		return distributionTableHeadCol;
	}

	public void hide() {

		distributionTableHeadCol.hide();
		for (DistributionTableElement element : elements) {
			element.hide();
		}
	}

	public void insertElement(Character symbol) {

		sumFrequencies++;

		updateProbabilities();

		StringArray reference = null;
		if (elements.size() == 0) {

			reference = distributionTableHeadCol;
		} else {

			reference = elements.get(elements.size() - 1).getStringArray();
		}
		Offset offset = new Offset(0, 0, reference, AnimalScript.DIRECTION_NE);

		String[] data = new String[3];
		data[0] = Character.toString(symbol);
		data[1] = Integer.toString(1);
		float probability = 1 / (float) sumFrequencies;
		data[2] = ProbabilityFormatter.format(probability);

		StringArray distributionTableCol = lang.newStringArray(offset, data,
				"distributionTableCol" + (elements.size() + 1), null,
				arrayPropsRest);

		DistributionTableElement element = new DistributionTableElement(symbol,
				1, probability, distributionTableCol);
		elements.add(element);
	}

	private void updateProbabilities() {

		for (DistributionTableElement element : elements) {

			int freq = element.getFrequency();
			float updatedProb = freq / (float) sumFrequencies;
			element.update(freq, updatedProb);
		}
	}

	/**
	 * increases the frequency of a symbol in the distribution table by one, and
	 * then updates the whole distribution table (hence the probabilities of
	 * each of the symbols has changed)
	 * 
	 * @param symbol
	 *            the symbol, whose frequency should be increased by one
	 */
	public void increaseFreqByOne(char symbol) {

		sumFrequencies++;

		for (int i = 0; i < elements.size(); i++) {

			DistributionTableElement element = elements.get(i);

			if (element.getSymbol() == symbol) {

				int currentFreq = element.getFrequency();
				int updatedFreq = currentFreq + 1;
				float updatedProb = updatedFreq / (float) sumFrequencies;
				element.update(updatedFreq, updatedProb);
			} else {

				int freq = element.getFrequency();
				float updatedProb = freq / (float) sumFrequencies;
				element.update(freq, updatedProb);
			}
		}
	}

	public void highlightElement(int id) {

		elements.get(id).highlight();
	}

	public void unhighlightElement(int id) {

		elements.get(id).unhighlight();
	}

	public void moveTo(Offset offset, Timing duration) {

		// moving element groups is not that trivial...
		// the position of the elements is updated at initialization and after a
		// step
		// so we need invisible align dummies, which simulate elements after
		// movement
		distributionTableHeadCol.moveTo(null, null, offset, null, duration);

		String[] distrTableHeadData = new String[3];
		distrTableHeadData[0] = distributionTableHeadCol.getData(0);
		distrTableHeadData[1] = distributionTableHeadCol.getData(1);
		distrTableHeadData[2] = distributionTableHeadCol.getData(2);

		String[] distrTableData = new String[3];
		distrTableData[0] = "";
		distrTableData[1] = "";
		distrTableData[2] = ProbabilityFormatter.format(1);

		StringArray offsetReference = lang.newStringArray(offset,
				distrTableHeadData, "alignDummy", null, arrayPropsFirstCol);
		offsetReference.hide();
		for (DistributionTableElement element : elements) {
			StringArray distrTableCol = element.getStringArray();
			distrTableCol.moveTo(null, null, new Offset(0, 0, offsetReference,
					AnimalScript.DIRECTION_NE), null, duration);
			offsetReference = lang.newStringArray(new Offset(0, 0,
					offsetReference, AnimalScript.DIRECTION_NE),
					distrTableData, "alignDummy", null, arrayPropsRest);
			offsetReference.hide();
		}
	}
}
