/*
 * TunstallCodingGenerator.java
 * Jonathan Roth, Till Vo�, 2017 for the Animal project at TU Darmstadt.
 */
package generators.compression.tunstall;

import generators.compression.tunstall.TunstallCoding.TunstallCoding;
import generators.compression.tunstall.TunstallCoding.TunstallCodingAnimal;
import generators.compression.tunstall.TunstallCoding.TunstallModel;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import algoanim.primitives.generators.Language;

import java.util.HashMap;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import java.awt.Color;

public class TunstallCodingGenerator implements ValidatingGenerator {
	private Language lang;
	private Color graphHighlightcolor;
	private String message;
	private Color sourceHighlightcolor;
	private int capacity;

	public void init() {
		lang = new AnimalScript("Tunstall Coding [Tree building]", "Jonathan Roth, Till Vo�", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		graphHighlightcolor = (Color) primitives.get("graphHighlightcolor");
		message = (String) primitives.get("message");
		sourceHighlightcolor = (Color) primitives.get("sourceHighlightcolor");
		capacity = (Integer) primitives.get("capacity");

		TunstallModel tmodel = TunstallCoding.compress(message, capacity);
		TunstallCodingAnimal hca = new TunstallCodingAnimal(lang, graphHighlightcolor, sourceHighlightcolor,
				tmodel.tree, capacity);
		hca.compress(message);

		return lang.toString();
	}

	public String getName() {
		return "Tunstall Coding [Tree building]";
	}

	public String getAlgorithmName() {
		return "Tunstall Coding";
	}

	public String getAnimationAuthor() {
		return "Jonathan Roth, Till Vo�";
	}

	public String getDescription() {
		return "Die Tunstall-Kodierung ist eine Form der verlustfreien Datenkompression und Entropiekodierung, " + "\n"
				+ "die 1967 von Brian Parker Tunstall in seiner Doktorarbeit am Georgia Institute of Technology entwickelt wurde."
				+ "\n"
				+ "Im Gegensatz zu ähnlichen Verfahren wie der Huffman-Kodierung ordnet die Tunstall-Kodierung einem "
				+ "\n"
				+ "Quellensymbol mit variabler Länge ein Codesymbol mit einer fixen Anzahl von Bits (Stellen) zu "
				+ "\n" + "(Quelle: https://de.wikipedia.org/wiki/Tunstall-Kodierung)." + "\n\n"
				+ "Die Parameter unterliegen folgenden Beschränkungen:\n"
				+ "1) Die Capacity muss mindestens der Anzahl der Buchstaben entsprechen.\n"
				+ "2) Die Anzahl der verschienen Buchstaben sowie die Capacity dürfen nicht grö�er als 20 sein (wg. der Baumgrö�e).";
	}


	public String getCodeExample() {
		return "T := tree with |U| leaves			// one leaf for each letter in alphabet U" + "\n"
				+ "while |T| < C				// C is the maximal capacity of the dictionary" + "\n"
				+ "	z := GetMostProbableLeaf(T);" + "\n"
				+ "	for(Letter l : U) 			// convert most probable leaf to tree with |U| leaves" + "\n"
				+ "		n := Node(l);" + "\n" + "		AddChild(z, n);" + "\n" + "	end for" + "\n" + "end while" + "\n"
				+ "return T;";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		try {
			Color c1 = (Color) arg1.get("graphHighlightcolor");
			Objects.requireNonNull(c1);
			Color c2 = (Color) arg1.get("sourceHighlightcolor");
			Objects.requireNonNull(c2);
			String s = (String) arg1.get("message");
			Objects.requireNonNull(s);
			if (s.isEmpty()) {
				return false;
			}
			Map<Character, Integer> frequencies = new HashMap<>();
			for (char c : s.toCharArray()) {
				frequencies.put(c, 0);
			}

			int capacity = (int) arg1.get("capacity");
			if (frequencies.size() > 20) {
				throw new IllegalArgumentException("The alphabet of the input text is too big.");
			}
			if (capacity < frequencies.size() || capacity > 20) {
				throw new IllegalArgumentException("Invalid value for capacity.");
			}

			TunstallModel model = TunstallCoding.compress(s, capacity);
			String textR = TunstallCoding.expand(model);
			if (!s.equals(textR)) {
				throw new IllegalArgumentException("Invalid combination of parameters.");
			}
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}