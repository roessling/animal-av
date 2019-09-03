package generators.compression;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class MTFDecoding extends CompressionAlgorithm implements Generator {

	private static String				lettersOrdered;

	private static final int		inputLimit	= 30;

	private static final String	DESCRIPTION	= 
	  "Das Move to Front - Verfahren eignet sich f&uuml;r eine weitere Bearbeitung"
	  + " Burrows Wheeler transformierter Daten. Der Eingabestring wird in eine Zahlenfolge verarbeitet, die sich"
	  + " wiederum verbessert Huffman-kodieren l&auml;sst.";

	private static final String	SOURCE_CODE	= "Der Algorithmus wird durch eine Animation demonstriert.\nEs handelt sich hier um einen Dekodierungsalgorithmus.\nIhre Eingabe wird zun&auml;chst durch die"
																							+ " entsprechende Kodierung kodiert.\nErst diese Daten werden dekodiert.";

	public MTFDecoding() {
		// nothing to be done
	}

	public void compress(String[] text) throws LineNotExistsException {
		// trim input to maximum length
//		String ein = "";
		String[] t = new String[Math.min(text.length, inputLimit)];
		for (int i = 0; i < t.length; i++) {
			t[i] = text[i];
//			ein += text[i];
		}
//		text = t;

		// topic
		Text topic = lang.newText(new Coordinates(20, 50),
				"Move to Front Decoding", "Topic", null, tptopic);

		lang.newRect(new Offset(-5, -5, topic, "NW"),
				new Offset(5, 5, topic, "SE"), "topicRect", null, rctp);

		// extract the input
		String input = "";
		for (int i = 0; i < t.length; i++) {
			input += t[i];
		}

		// Algo steps
		lang.nextStep();
		Text step1 = lang.newText(new Offset(0, 40, topic, "SW"),
				"Die Eingabe ist:  ", "line1", null, tpsteps);
		Text step12 = lang.newText(new Offset(10, -5, step1, "SE"), input, "line1",
				null, tpsteps);
		step12.changeColor(null, Color.RED, null, null);
		lang.nextStep();
		Text step2 = lang.newText(new Offset(0, 30, step1, "SW"),
				"Durch die Move to Front Kodierung erhalten wir:  ", "line1", null,
				tpsteps);
		String in = transformMTF(t);
		Text step22 = lang.newText(new Offset(10, -5, step2, "SE"), in, "line1",
				null, tpsteps);
		Text step23 = lang.newText(new Offset(10, -5, step22, "SE"), "und",
				"line1", null, tpsteps);
		Text step24 = lang.newText(new Offset(10, -5, step23, "SE"),
				lettersOrdered, "line1", null, tpsteps);
		step12.changeColor(null, Color.BLACK, null, null);
		step22.changeColor(null, Color.RED, null, null);
		step24.changeColor(null, Color.RED, null, null);
		Text step3 = lang
				.newText(new Offset(0, 30, step2, "SW"),
						"Wir wollen die Ausgabe nun wieder dekodieren.", "line1", null,
						tpsteps);
		lang.nextStep();
		Text step4 = lang
				.newText(
						new Offset(10, 40, step3, "SW"),
						"- Erweitere die Ausgabe durch den Buchstaben an entsprechender Indexstelle. ",
						"line2", null, tpsteps);
		Text step41 = lang
				.newText(
						new Offset(0, 20, step4, "SW"),
						"- Der eben genannte Buchstbabe wird an die erste Stelle verschoben, wobei",
						"line2", null, tpsteps);
		Text step42 = lang.newText(new Offset(0, 20, step41, "SW"),
				"  die anderen Buchstaben nach rechts rotieren.", "line2", null,
				tpsteps);
		Text step43 = lang.newText(new Offset(0, 20, step42, "SW"),
				"- Führe dies fort, bis die Eingabeziffern vollständig gelesen sind.",
				"line2", null, tpsteps);

		int[][] ind = new int[1][lettersOrdered.length()];
		for (int i = 0; i < ind[0].length; i++) {
			ind[0][i] = i;
		}
		IntMatrix indizes = lang.newIntMatrix(new Offset(155, 35, step43, "SW"),
				ind, "ind", null, mp);
		String[] l = new String[lettersOrdered.length()];
		for (int i = 0; i < l.length; i++) {
			l[i] = " " + lettersOrdered.charAt(i) + "  ";
		}
		StringArray let = lang.newStringArray(new Offset(-7, 7, indizes, "SW"), l,
				"letters", null, ap);

		Text eingabe = lang.newText(new Offset(0, 200, step42, "SW"), "Eingabe:  ",
				"in", null, tpsteps);
		int[] inData = new int[in.length()];
		for (int i = 0; i < in.length(); i++) {
			inData[i] = Integer.parseInt("" + in.charAt(i));
		}
		IntArray inArray = lang.newIntArray(new Offset(30, -5, eingabe, "NE"),
				inData, "inArray", null, ap);
		ArrayMarker am = lang.newArrayMarker(inArray, 0, "am", null, amp);
		inArray.highlightCell(0, null, null);

		String result = "";
		Text ausgabeLabel = lang.newText(new Offset(0, 55, eingabe, "SW"),
				"Ausgabe:", "ausgabe", null, tpsteps);
		Text ausgabe = lang.newText(new Offset(10, -5, ausgabeLabel, "SE"), result,
				"ausgabe", null, tpsteps);
		ausgabe.changeColor(null, Color.BLUE, null, null);

		for (int i = 0; i < in.length(); i++) {
			if (i != 0) {
				inArray.unhighlightCell(i - 1, null, null);
				am.move(i, null, null);
				inArray.highlightCell(i, null, null);
			}
			lang.nextStep();
			result += lettersOrdered.charAt(Integer.parseInt("" + in.charAt(i)));
			ausgabe.setText(result, null, null);
			lettersOrdered = lettersOrdered.charAt(Integer
					.parseInt("" + in.charAt(i)))
					+ lettersOrdered.replace(""
							+ lettersOrdered.charAt(Integer.parseInt("" + in.charAt(i))), "");
			for (int j = 0; j < lettersOrdered.length(); j++) {
				let.put(j, " " + lettersOrdered.charAt(j) + "  ", null, null);
			}
			lang.nextStep();
		}

		lang.newText(new Offset(0, 60, ausgabeLabel, "SW"),
				"Die Ausgabe entspricht genau der Eingabe", "Ausgabe", null, tpsteps);
	}

	/**
	 * Transform the data by Move To Front.
	 * 
	 * @param text
	 * @return the transformed String
	 */
	public static String transformMTF(String[] text) {
		// create a alphabetically sorted string of the letters
		String letters = "";
		char small = 0;

		for (int i = 0; i < text.length; i++) {
			char tmp = 256;
			for (int j = 0; j < text.length; j++) {
				if (text[j].charAt(0) > small && text[j].charAt(0) < tmp) {
					tmp = text[j].charAt(0);
				}
			}
			small = tmp;
			if (small != 256)
				letters += small;
		}

		lettersOrdered = letters;

		// iterate over the input, apend the index of the current letter, und move
		// it to the front
		String result = "";
		for (int i = 0; i < text.length; i++) {
			result += letters.indexOf(text[i]);
			letters = text[i] + letters.replace(text[i], "");
		}
		return result;
	}

	public static String getSOURCE_CODE() {
		return SOURCE_CODE;
	}

	public String getCodeExample() {
		return SOURCE_CODE;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

  @Override
  public String getAlgorithmName() {
    return "Move to Front";
  }

	public String getName() {
		return "Move to Front Dekomprimierung";
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		String[] strArray = (String[]) primitives.get("stringArray");
		try {
			compress(strArray);
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}
		lang.finalizeGeneration();
		return lang.getAnimationCode();
	}

	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
	}
	
	@Override
	public void init() {
	  lang = new AnimalScript("Move to Front Decoding", "Florian Lindner", 800, 600);
	  lang.setStepMode(true);
	}

}
