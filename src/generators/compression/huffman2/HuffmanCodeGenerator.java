/*
  * HuffmanCodeGenerator.java
 * Jonathan Roth, Till Vo�, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.compression.huffman2;

import generators.compression.huffman2.HuffmanCoding.HuffmanCoding;
import generators.compression.huffman2.HuffmanCoding.HuffmanCodingAnimal;
import generators.compression.huffman2.HuffmanCoding.HuffmanModel;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Objects;

import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import java.awt.Color;
import algoanim.properties.TextProperties;

public class HuffmanCodeGenerator implements ValidatingGenerator {
	private Language lang;
	private Color graphHighlightcolor;
	private String message;
	private Color sourceHighlightcolor;

	@Override
	public void init() {
		lang = new AnimalScript("Huffman Code - Tree Building", "Jonathan Roth, Till Vo�", 800, 600);
	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		graphHighlightcolor = (Color) primitives.get("graphHighlightcolor");
		sourceHighlightcolor = (Color) primitives.get("sourceHighlightcolor");
		message = (String) primitives.get("message");

		// @Sebastian: this input parameter can be changed
		// String text = "MISSISSIPPI";

		HuffmanModel model = HuffmanCoding.compress(message);
		HuffmanCodingAnimal hca = new HuffmanCodingAnimal(lang, graphHighlightcolor, sourceHighlightcolor, model.tree);
		hca.compress(message);
		return lang.toString();
	}

	@Override
	public String getName() {
		return "Huffman Code - Tree Building";
	}

	@Override
	public String getAlgorithmName() {
		return "Huffman Code";
	}

	@Override
	public String getAnimationAuthor() {
		return "Jonathan Roth, Till Vo�";
	}

	public String getDescription() {
		return "Die Huffman-Kodierung ist eine Form der Entropiekodierung, die 1952 von David A. Huffman entwickelt und in der Abhandlung 'A Method for the Construction of Minimum-Redundancy Codes' publiziert wurde. "
				+ "\n"
				+ "Sie ordnet einer festen Anzahl an Quellsymbolen jeweils Codewörter mit variabler Länge zu. In der Informationstechnik ist sie ein Präfix-Code, die üblicherweise für verlustfreie Kompression benutzt wird. "
				+ "\n"
				+ "ähnlich anderer Entropiekodierungen werden häufiger auftauchende Zeichen mit weniger Bits repräsentiert als seltener auftauchende. "
				+ "\n" + "\n"
				+ "Die Grundidee ist, einen k-nären Wurzelbaum (ein Baum mit jeweils k Kindern je Knoten) für die Darstellung des Codes zu verwenden. In diesem sog. "
				+ "\n"
				+ "Huffman-Baum stehen die Blätter für die zu kodierenden Zeichen, während der Pfad von der Wurzel zum Blatt das Codesymbol bestimmt. "
				+ "\n" + "\n"
				+ "Der bei der Huffman-Kodierung gewonnene Baum liefert garantiert eine optimale und präfixfreie Kodierung. "
				+ "\n"
				+ "D. h. es existiert kein symbolbezogenes Kodierverfahren, das einen kürzeren Code generieren könnte, wenn die Auftrittswahrscheinlichkeiten der Symbole bekannt sind."
				+ "\n" + "		";
	}

	@Override
	public String getCodeExample() {
		return "Q := PriorityQueueAscending(input); // characters with frequencies" + "\n" + "while |Q| > 1" + "\n"
				+ "	z := Node();" + "\n" + "	z.left := Extract-Min(Q);" + "\n" + "	z.right := Extract-Min(Q);"
				+ "\n" + "	z.freq := z.left.freq + z.right.freq;" + "\n" + "	Insert(Q, z);" + "\n" + "end while"
				+ "\n" + "return Extract-Min(Q); // return root" + "\n";
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
	}

	@Override
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
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}