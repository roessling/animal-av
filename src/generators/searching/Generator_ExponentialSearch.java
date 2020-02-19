/*
 * Generator_ExponentialSearch.java
 * Donghyuck Son und Timur Levent G�rg�?, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.searching;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

public class Generator_ExponentialSearch implements ValidatingGenerator {
	private Language lang;
	private int suchendesElement;
	private int[] sortiertesArray;

	public void init() {
		lang = new AnimalScript("Exponential Search", "Donghyuck Son und Timur Levent Görgü", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		suchendesElement = (Integer) primitives.get("suchendesElement");
		sortiertesArray = (int[]) primitives.get("sortiertesArray");
		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		ExponentialSearch s = new ExponentialSearch(lang);
		if (validateInput(props, primitives))
			s.search(sortiertesArray, suchendesElement);
		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		return lang.toString();
	}

	public String getName() {
		return "Exponential Search";
	}

	public String getAlgorithmName() {
		return "Exponential Search";
	}

	public String getAnimationAuthor() {
		return "Donghyuck Son und Timur Levent Görgü";
	}

	public String getDescription() {
		return "Der Exponential Search Algorithmus sucht ein Element in einem sortierten Array." + "\n"
				+ "Dabei werden durch die exponentiellen Schritte das Array unterteilt, " + "\n"
				+ "damit man in dem Subarray ein Binarysearch anwenden kann." + "\n" + "\n"
				+ "Der Algorithmus hat eine Lauftzeit von O(log i) - dabei steht das i für den Index des gesuchten Elements.";
	}

	public String getCodeExample() {
		return "    static int exponentialSearch(int arr[], " + "\n" + "                                 int n, int x) "
				+ "\n" + "    { " + "\n" + "        // If x is present at firt location itself " + "\n"
				+ "        if (arr[0] == x) " + "\n" + "            return 0; " + "\n" + "      " + "\n"
				+ "        // Find range for binary search by " + "\n" + "        // repeated doubling " + "\n"
				+ "        int i = 1; " + "\n" + "        while (i < n && arr[i] <= x) " + "\n"
				+ "            i = i*2; " + "\n" + "      " + "\n"
				+ "        // Call binary search for the found range. " + "\n"
				+ "        return Arrays.binarySearch(arr, i/2,  " + "\n"
				+ "                                   Math.min(i, n), x); " + "\n" + "    } ";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		int[] sortiertesArray = (int[]) arg1.get("sortiertesArray");
		for (int i = 1; i < sortiertesArray.length; i++) {
			if (sortiertesArray[i - 1] > sortiertesArray[i]) {
				System.out.println("Array ist nicht sortiert.");
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
//		DummyGenerator generators = new DummyGenerator();
//		generators.add(new Generator_ExponentialSearch());
		Generator generator = new Generator_ExponentialSearch(); // Generator erzeugen
		
		Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}
}
