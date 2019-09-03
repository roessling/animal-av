package generators.maths.eratosthenes;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.MatrixProperties;

public class EratosthenesGenerator implements Generator {
	private Language lang;

	public void init() {
		lang = new AnimalScript("Sieb des Eratosthenes [DE]", "Nicole Brunkhorst, Stefan Rado", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		Eratosthenes eratosthenes = new Eratosthenes(lang);
		eratosthenes.setTableProperties((MatrixProperties) props.getPropertiesByName("NumberTable"));
		eratosthenes.setPrimeNumberColor((Color) primitives.get("PrimeNumberColor"));
		eratosthenes.setNonPrimeNumberColor((Color) primitives.get("NonPrimeNumberColor"));

		int N = (Integer) primitives.get("BiggestNumberToCheck");
		eratosthenes.createAnimation(N);

		return lang.toString();
	}

	public String getName() {
		return "Sieb des Eratosthenes [DE]";
	}

	public String getAlgorithmName() {
		return "Sieb des Eratosthenes";
	}

	public String getAnimationAuthor() {
		return "Nicole Brunkhorst, Stefan Rado";
	}

	public String getDescription() {
		return "Das Sieb des Eratosthenes ist ein Algorithmus zur Bestimmung einer Liste oder Tabelle aller Primzahlen kleiner oder gleich einer vorgegebenen Zahl.";
	}

	public String getCodeExample() {
		// @formatter:off
		return "function FindePrimzahlen(Integer N)\n" +
				"  // Primzahlenfeld initialisieren\n" +
				"  // Alle Zahlen sind zu Beginn potentielle Primzahlen\n" +
				"  var Prim: array [2..N] of Boolean = True\n" +
				"\n" +
				"  for i := 2 to N do\n" +
				"    if Prim[i] then\n" +
				"      // i ist Primzahl, gib i aus\n" +
				"      print i\n" +
				"\n" +
				"      // und markiere die Vielfachen als nicht prim, beginnend mit i*i\n" +
				"      // (denn k*i mit k&lt;i wurde schon als Vielfaches von k gestrichen)\n" +
				"      for j = i*i to N step i do\n" +
				"        Prim[j] = False\n" +
				"      end\n" +
				"    endif\n" +
				"  end\n" +
				"end";
		// @formatter:on
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

}
