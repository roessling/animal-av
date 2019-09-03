package generators.tree.id3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DataTable {

	private LinkedList<LinkedList<String>> instances = new LinkedList<LinkedList<String>>();

	private String[] featureNames;

	private String className;



	public String getClassName() {
		return className;
	}


	public DataTable(String[] featureNames, String className) {
		this.featureNames = featureNames;
		this.className = className;
	}


	public void addInstance(String... data) {

		LinkedList<String> instance = new LinkedList<String>();

		for (String s : data) {
			instance.add(s);
		}

		instances.add(instance);
	}


	public boolean singleClassLeft() {

		LinkedList<String> classValues = new LinkedList<String>();

		for (LinkedList<String> instance : instances) {
			String classAttribute = instance.getLast();
			if (!classValues.contains(classAttribute)) {
				classValues.add(classAttribute);
			}
		}

		int numberOfValues = classValues.size();

		if (numberOfValues > 1) {
			return false;
		}
		return true;

	}


	public String getFirstFoundClass() {
		String className = instances.getFirst().getLast();
		return className;
	}


	public LinkedList<String> getValues(String feature) {

		int fNumber = getFeatureNumber(feature);

		LinkedList<String> values = new LinkedList<String>();

		for (LinkedList<String> instance : instances) {

			String value = instance.get(fNumber + 1);
			if (!values.contains(value)) {
				values.add(value);
			}
		}

		return values;
	}


	public int getFeatureNumber(String feature) {

		int fNumber;
		for (fNumber = 0; fNumber < featureNames.length; fNumber++) {
			if (featureNames[fNumber].equals(feature)) {
				break;
			}
		}

		return fNumber;
	}


	public HashMap<String, LinkedList<String>> createValueMap() {

		HashMap<String, LinkedList<String>> map = new HashMap<String, LinkedList<String>>();

		for (String f : featureNames) {
			map.put(f, getValues(f));
		}

		return map;
	}


	public DataTable reduce(String featureName, String value) {

		DataTable reduced = new DataTable(featureNames, className);

		int fNumber = getFeatureNumber(featureName);

		for (List<String> instance : instances) {
			if (instance.get(fNumber + 1).equals(value)) {
				String[] instanceArray = instance.toArray(new String[instance.size()]);
				reduced.addInstance(instanceArray);
			}
		}

		return reduced;
	}


	public String[] getFeatureNames() {
		return featureNames;
	}


	public HashMap<String, Integer> countClassOccurences() {

		HashMap<String, Integer> values = new HashMap<String, Integer>();

		for (LinkedList<String> instance : instances) {

			String value = (String) instance.getLast();
			if (values.containsKey(value)) {
				int number = values.get(value);
				values.put(value, number + 1);
			} else {
				values.put(value, 1);
			}
		}

		return values;
	}


	public int getNumberOfInstances() {
		return instances.size();
	}


	public int getNumberOfFeatures() {
		return featureNames.length;
	}


	public String[][] dataAsStringArray() {

		int height = getNumberOfInstances() + 1;
		int width = featureNames.length + 2;


		String[][] data = new String[height][width];

		data[0][0] = "Id";
		data[0][width - 1] = className;

		for (int x = 0; x < width - 2; x++) {
			data[0][x + 1] = featureNames[x];
		}

		// for (int y = 0; y < height - 1; y++) {
		// data[y + 1][0] = instances.get(y).get(0);
		// }

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height - 1; y++) {
				data[y + 1][x] = instances.get(y).get(x);
			}
		}

		return data;

	}


	public LinkedList<String> getOrderedClassOccurences() {
		LinkedList<String> occurences = new LinkedList<String>();

		for (LinkedList<String> instance : instances) {
			occurences.add(instance.getLast());
		}

		return occurences;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (LinkedList<String> instance : instances) {
			for (String entry : instance) {
				builder.append(entry + ", ");
			}
			builder.delete(builder.length() - 2, builder.length());
			builder.append(System.lineSeparator());
		}

		return builder.toString();
	}

}
