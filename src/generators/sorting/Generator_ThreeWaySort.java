/*
 * Generator_ThreeWaySort.java
 * Donghyuck Son, Timur Levent G�rg�, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.sorting;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

public class Generator_ThreeWaySort implements ValidatingGenerator {
	private Language lang;
	private int[] intArray;

	public void init() {
		lang = new AnimalScript("3-Way QuickSort (Dutch National Flag)", "Donghyuck Son, Timur Levent G�rg�", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		intArray = (int[]) primitives.get("intArray");
		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		ThreeWaySort s = new ThreeWaySort(lang);
		if (validateInput(props, primitives))
			s.sort(intArray);
		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

		return lang.toString();
	}

	public String getName() {
		return "3-Way QuickSort (Dutch National Flag)";
	}

	public String getAlgorithmName() {
		return "3-Way QuickSort (Dutch National Flag)";
	}

	public String getAnimationAuthor() {
		return "Donghyuck Son, Timur Levent G�rg�";
	}

	public String getDescription() {
		return "In simple QuickSort algorithm, we select an element as pivot, partition the array around pivot and recur for subarrays on left and right of pivot."
				+ "\n"
				+ "The idea of 3 way QuickSort is to process all occurrences of pivot and is based on Dutch National Flag algorithm.";
	}

	public String getCodeExample() {
		return "// Partition routine using Dutch National Flag Algorithm" + "\n"
				+ "public static Pair<Integer, Integer> partition(int[] arr, int start, int end)" + "\n" + "{" + "\n"
				+ "	int mid = start;" + "\n" + "	int pivot = arr[end];" + "\n" + "\n" + "	while (mid <= end) {"
				+ "\n" + "		if (arr[mid] < pivot) {" + "\n" + "			swap(arr, start, mid);" + "\n"
				+ "			++start;" + "\n" + "			++mid;" + "\n" + "		}" + "\n"
				+ "		else if (arr[mid] > pivot) {" + "\n" + "			swap(arr, mid, end);" + "\n"
				+ "			--end;" + "\n" + "		}" + "\n" + "		else {" + "\n" + "			++mid;" + "\n"
				+ "		}" + "\n" + "	}" + "\n" + "\n"
				+ "	// arr[start .. mid - 1] contains all occurrences of pivot" + "\n"
				+ "	return new Pair<Integer, Integer>(start - 1, mid);" + "\n" + "}" + "\n" + "\n"
				+ "// Three-way Quicksort routine" + "\n"
				+ "public static void quicksort(int[] arr, int start, int end)" + "\n" + "{" + "\n"
				+ "	// base condition for 0 or 1 elements" + "\n" + "	if (start >= end) {" + "\n" + "		return;"
				+ "\n" + "	}" + "\n" + "\n" + "	// handle 2 elements separately as Dutch national flag" + "\n"
				+ "	// algorithm will work for 3 or more elements" + "\n" + "	if (start - end == 1)" + "\n" + "	{"
				+ "\n" + "		if (arr[start] < arr[end]) {" + "\n" + "			swap(arr, start, end);" + "\n"
				+ "		}" + "\n" + "		return;" + "\n" + "	}" + "\n" + "\n"
				+ "	// rearrange the elements across pivot using Dutch" + "\n" + "	// national flag problem algorithm"
				+ "\n" + "	Pair pivot = partition(arr, start, end);" + "\n" + "\n"
				+ "	// recur on sub-array containing elements that are less than pivot" + "\n"
				+ "	quicksort(arr, start, (int) pivot.getKey());" + "\n" + "\n"
				+ "	// recur on sub-array containing elements that are more than pivot" + "\n"
				+ "	quicksort(arr, (int) pivot.getValue(), end);" + "\n" + "}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		int[] sortiertesArray = (int[]) arg1.get("intArray");
		if(sortiertesArray == null || sortiertesArray.length == 0){
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		Generator generator = new Generator_ThreeWaySort(); // Generator erzeugen
		Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}

}