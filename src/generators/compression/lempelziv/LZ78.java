package generators.compression.lempelziv;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class LZ78 extends CompressionAlgorithm implements Generator {

	private static final int		inputLimit	= 26;

	private static final String	DESCRIPTION	= "LZ78 ist ein verlustfreies Kompressionsverfahren, welches"
																							+ " eine Weiterentwicklung des LZ77 Verfahrens darstellt. In dem Verfahren werden durch das Scannen"
																							+ " des Eingabestrings Dateien in einem Wörterbuch abgelegt.";

	private static final String	SOURCE_CODE	= "Der Algorithmus wird in einer Animation demonstriert. Um die grafische Animation in voller Größe darstellen"
																							+ " zu können, wird die Eingabe auf 26 Buchstaben begrenzt.";

	public LZ78() {
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
		Text topic = lang.newText(new Coordinates(20, 50), "LZ78", "Topic", null,
				tptopic);

		lang.newRect(new Offset(-5, -5, topic, "NW"),
				new Offset(5, 5, topic, "SE"), "topicRect", null, rctp);

		// Algo in words
		lang.nextStep();
		Text algoinWords = lang.newText(new Coordinates(20, 100),
				"Der Algorithmus in Worten", "inWords", null, tpwords);

		// Algo steps
		lang.nextStep();
		Text step1 = lang
				.newText(
						new Offset(0, 100, topic, "SW"),
						"1) Lege ein Wörterbuch an, das initial das Ende einer Datei (ferner EOF) mit 0 und",
						"line1", null, tpsteps);
		Text step12 = lang.newText(new Offset(0, 20, step1, "SW"),
				"     die Buchstaben von A bis Z mit den Zahlen 1 bis 26 kodiert.",
				"line1", null, tpsteps);
		lang.nextStep();
		Text step2 = lang
				.newText(
						new Offset(0, 40, step12, "SW"),
						"2) Nun wird die Eingabe zeichenweise bis zum Ende des Textes eingelesen.",
						"line2", null, tpsteps);
		lang.nextStep();
		Text step3 = lang
				.newText(
						new Offset(0, 40, step2, "SW"),
						"3) Das eingelesene Zeichen wird mit sovielen folgenden Buchstaben ergänzt,",
						"line3", null, tpsteps);
		Text step31 = lang
				.newText(
						new Offset(0, 20, step3, "SW"),
						"      bie die entstehende Kombination nicht mehr im Wörterbuch enthalten ist. ",
						"line3", null, tpsteps);
		lang.nextStep();
		Text step4 = lang
				.newText(
						new Offset(0, 40, step31, "SW"),
						"4)  Diese Kombination wäre also ohne ihren letzten Buchstaben noch im Wörterbuch vorhanden.",
						"line3", null, tpsteps);
		Text step41 = lang
				.newText(
						new Offset(0, 20, step4, "SW"),
						"    Das Ergebnis wird um den passenden Schlüssel dieser nicht vollständigen Kombination erweitert. Für die",
						"line3", null, tpsteps);
		Text step42 = lang
				.newText(
						new Offset(0, 20, step41, "SW"),
						"    vollständige Kombination wird ein Eintrag an der nächsten freien Stelle im Wörterbuch angelegt.",
						"line3", null, tpsteps);
		lang.nextStep();

		algoinWords.hide();
		step1.hide();
		step12.hide();
		step2.hide();
		step3.hide();
		step31.hide();
		step4.hide();
		step41.hide();
		step42.hide();

		// algo

		// extract the input
		String input = "";
		for (int i = 0; i < t.length; i++) {
			input += t[i];
		}
		input = input.toUpperCase();

		StringArray strArray = lang.newStringArray(new Offset(0, 90, topic, "SW"),
				t, "text", null, ap);
		ArrayMarker marker = lang.newArrayMarker(strArray, 0, "marker", null, amp);

		// fill the initial dictionary
		Vector<String> dict = new Vector<String>(0, 1);
		dict.add("EOF"); // 0
		for (int i = 65; i < 91; i++) {
			dict.add("" + (char) i);
		}

		String[][] dictData = new String[inputLimit][2];

		for (int i = 0; i < dictData.length; i++) {
			dictData[i][0] = "  ";
			dictData[i][1] = "  ";
		}
		dictData[0][0] = "0";
		dictData[0][1] = "EOF";
		dictData[1][0] = "1";
		dictData[1][1] = "A";
		dictData[2][0] = "...";
		dictData[2][1] = "...";
		dictData[3][0] = "26";
		dictData[3][1] = "Z";
		int matrixCounter = 4;

		StringMatrix dictMatrix = lang.newStringMatrix(new Offset(110, 0, strArray,
				"NE"), dictData, "dict", null, mp);

		lang.nextStep();

		String result = "";

		Text resultText = lang.newText(new Offset(0, 35, strArray, "SW"),
				"Ausgabe:  ", "result", null, tpsteps);
		Text resultText2 = lang.newText(new Offset(5, -5, resultText, "SE"),
				result, "result", null, tpsteps);
		resultText2.changeColor(null, Color.RED, null, null);

		lang.nextStep();

		for (int i = 0; i < input.length(); i++) {
			marker.move(i, null, null);
			String tmp = "" + input.charAt(i);
			strArray.unhighlightCell(0, i, null, null);
			strArray.highlightCell(i, null, null);

			while (dict.contains(tmp) && i + 1 < input.length()) {
				if (dict.contains(tmp + input.charAt(i + 1))) {
					tmp += input.charAt(i + 1);
					i++;
					strArray.highlightCell(i, null, null);
				} else {
					dict.add(tmp + input.charAt(i + 1));

					dictMatrix.put(matrixCounter, 0, "" + (matrixCounter + 23), null,
							null);
					dictMatrix.put(matrixCounter, 1, "" + tmp + input.charAt(i + 1),
							null, null);
					dictMatrix.highlightCell(matrixCounter, 0, null, null);
					dictMatrix.highlightCell(matrixCounter, 1, null, null);
					if (matrixCounter > 0) {
						dictMatrix.unhighlightCell(matrixCounter - 1, 0, null, null);
						dictMatrix.unhighlightCell(matrixCounter - 1, 1, null, null);
					}
					matrixCounter++;
					if (tmp.length() > 1) {
						dictMatrix.highlightCell(dict.indexOf(tmp) - 23, 1, null, null);
					}
					lang.nextStep();
					if (tmp.length() > 1) {
						dictMatrix.unhighlightCell(dict.indexOf(tmp) - 23, 1, null, null);
					}
					break;
				}
			}
			result += dict.indexOf(tmp) + " ";
			resultText2.setText(result, null, null);
			lang.nextStep();
		}

		Text fazit = lang.newText(new Offset(0, 90, resultText, "SW"),
				"Damit ergibt sich die Ausgabe:  ", "Ausgabe", null, tpsteps);
		Text fazit2 = lang.newText(new Offset(0, 20, fazit, "SW"), result,
				"Ausgabe", null, tpsteps);
		fazit2.changeColor(null, Color.BLUE, null, null);

		lang.newText(new Offset(0, 20, fazit2, "SW"),
				"Die zusätzliche Ausgabe des Wörterbuchs ist nicht notwendig.",
				"fazit", null, tpsteps);
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

	public String getName() {
		return "LZ78 Komprimierung";
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
  public String getAlgorithmName() {
    return "LZ78 (Lempel, Ziv 1978)";
  }
  
  @Override
  public void init() {
    lang = new AnimalScript("LZ78", "Florian Lindner", 800, 600);
    lang.setStepMode(true);
  }
}
