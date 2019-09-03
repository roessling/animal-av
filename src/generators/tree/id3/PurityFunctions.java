package generators.tree.id3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class PurityFunctions {

	private static String yes = null;

	private static LinkedList<String> protocolList = new LinkedList<String>();



	public static void setYes(String yes) {
		PurityFunctions.yes = yes;
	}



	private static final Function<DataTable, Double> ENTROPY = (DataTable table) -> {

		HashMap<String, Integer> classValues = table.countClassOccurences();

		int numberOfInstances = table.getNumberOfInstances();

		double entropy = 0.0;
		for (String cValue : classValues.keySet()) {
			double pPlus = classValues.get(cValue) / ((double) numberOfInstances);

			if (!(pPlus == 0.0)) {
				entropy = entropy - (pPlus * (Math.log10(pPlus) / Math.log10(2.0)));
			}
		}

		return entropy;

	};

	public static final BiFunction<DataTable, String, Double> INFORMATION_GAIN = (DataTable table, String feature) -> {

		protocolList.add("_" + feature);

		LinkedList<String> values = table.getValues(feature);

		int numberOfInstances = table.getNumberOfInstances();

		double informationGain = ENTROPY.apply(table);
		protocolList.add(informationGain + "");

		for (String value : values) {
			DataTable reduced = table.reduce(feature, value);
			protocolList.add(value);
			double relativeNumber = reduced.getNumberOfInstances() / ((double) numberOfInstances);
			protocolList.add(reduced.getNumberOfInstances() + "");
			protocolList.add(numberOfInstances + "");
			double currentEntropy = ENTROPY.apply(reduced);
			protocolList.add(currentEntropy + "");
			informationGain = informationGain - (relativeNumber * currentEntropy);
		}
		protocolList.add(informationGain + "");
		return informationGain;
	};


	private static final Function<DataTable, Double> GINI_INDEX = (DataTable table) -> {

		HashMap<String, Integer> classValues = table.countClassOccurences();

		int numberOfInstances = table.getNumberOfInstances();

		double p = 0;

		if (classValues.containsKey(yes)) {
			p = classValues.get(yes) / ((double) numberOfInstances);
		}

		double gini = 1 - ((p * p) + (1 - p) * (1 - p));

		return gini;

	};


	public static final BiFunction<DataTable, String, Double> AVERAGE_GINI_INDEX = (DataTable table, String feature) -> {

		LinkedList<String> values = table.getValues(feature);

		int numberOfInstances = table.getNumberOfInstances();

		double giniIndex = 0;

		protocolList.add("_" + feature);

		for (String value : values) {
			protocolList.add(value);
			DataTable reduced = table.reduce(feature, value);
			protocolList.add(reduced.getNumberOfInstances() + "");
			protocolList.add(numberOfInstances + "");
			double relativeNumber = reduced.getNumberOfInstances() / ((double) numberOfInstances);

			double currentGini = GINI_INDEX.apply(reduced);
			protocolList.add(currentGini + "");
			giniIndex = giniIndex + (relativeNumber * currentGini);
		}

		protocolList.add(giniIndex + "");
		return giniIndex;
	};



	public static String getMostInformativeFeature(DataTable table, BiFunction<DataTable, String, Double> purityFunction) {
		protocolList.clear();
		String[] featureNames = table.getFeatureNames();

		int indexOfBestFeature = 0;
		double bestPurity = purityFunction.apply(table, featureNames[0]);

		for (int i = 1; i < featureNames.length; i++) {

			double purityMeasure = purityFunction.apply(table, featureNames[i]);

			// Average Gini Index
			if (purityFunction == AVERAGE_GINI_INDEX) {
				if (purityMeasure < bestPurity) {
					bestPurity = purityMeasure;
					indexOfBestFeature = i;
				}

				// Information Gain
			} else {
				if (purityMeasure > bestPurity) {
					bestPurity = purityMeasure;
					indexOfBestFeature = i;
				}
			}
		}

		return featureNames[indexOfBestFeature];
	}


	public static LinkedList<String> getList() {

		LinkedList<String> toReturn = new LinkedList<String>();
		for (String s : protocolList) {
			if (s.matches(".*[0123456789].*")) {
				double d = Double.parseDouble(s);
				d = ((int) (d * 1000)) / 1000.0;
				toReturn.add(d + "");
			} else {
				toReturn.add(s);
			}
		}

		protocolList = new LinkedList<String>();

		return toReturn;
	}

}