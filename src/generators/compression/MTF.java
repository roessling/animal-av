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
import algoanim.primitives.IntMatrix;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class MTF extends CompressionAlgorithm implements Generator {

	SourceCode									sc;

	private static final int		inputLimit	= 30;

	private static final String	DESCRIPTION	= 
	  "Das Move to Front Verfahren eignet sich f&uuml;r eine weitere Bearbeitung"
	  + " von Burrows-Wheeler-transformierten Daten. Der Eingabestring wird in eine Zahlenfolge verarbeitet, die sich"
	  + " wiederum verbessert Huffman-kodieren l&auml;sst.";

	private static final String	SOURCE_CODE	= 
	  "Der Algorithmus wird in einer Animation demonstriert.\nUm die grafische Animation in voller Gr&ouml;&szlig;e darstellen"
	  + " zu k&ouml;nnen, wird die Eingabe auf 30 Buchstaben begrenzt.";

	public MTF() {
		// nothing to be done
	}

	public void compress(String[] text) throws LineNotExistsException {
		// trim input to maximum length
		String eingabe = "";
		String[] t = new String[Math.min(text.length, inputLimit)];
		for (int i = 0; i < t.length; i++) {
			t[i] = text[i];
			eingabe += text[i];
		}
		
		// topic
		Text topic = lang.newText(new Coordinates(20, 50),
				"Move to Front Encoding", "Topic", null, tptopic);

		lang.newRect(new Offset(-5, -5, topic, "NW"),
				new Offset(5, 5, topic, "SE"), "topicRect", null, rctp);

		// input
		lang.nextStep();
		Text eingabeText = lang.newText(new Offset(0, 40, topic, "SW"), "Eingabe:",
				"eingabe", null, tpsteps);
		Text eingabeText2 = lang.newText(new Offset(20, -5, eingabeText, "SE"),
				eingabe, "eingabe", null, tpsteps);
		eingabeText2.changeColor(null, Color.RED, null, null);

		// Algo in words
		lang.nextStep();

		lang.newText(new Offset(0, 35, eingabeText, "SW"),
				"Der Algorithmus in Worten", "inWords", null, tpwords);

		// Algo steps
		lang.nextStep();
		Text step1 = lang
				.newText(
						new Offset(0, 120, topic, "SW"),
						"1) Initialisiere eine Tabelle der Eingabe, welche Burrows Wheeler transformiert ist.",
						"line1", null, tpsteps);
		Text step12 = lang
				.newText(
						new Offset(0, 20, step1, "SW"),
						"     Die Tabelle enthält jedes Zeichen genau einmal. Sie beinhaltet die Buchstaben",
						"line1", null, tpsteps);
		Text step13 = lang.newText(new Offset(0, 20, step12, "SW"),
				"     mit aufsteigenden Indizes und ist alphabetisch geordnet.",
				"line1", null, tpsteps);
		lang.nextStep();
		Text step2 = lang.newText(new Offset(0, 40, step13, "SW"),
				"2) Für jedes Zeichen der transformierten Eingabe:", "line2", null,
				tpsteps);
		Text step21 = lang
				.newText(
						new Offset(0, 20, step2, "SW"),
						"    - Erweitere die Ausgabe um die Indexstelle des aktuellen Zeichens.",
						"line2", null, tpsteps);
		Text step22 = lang.newText(new Offset(0, 20, step21, "SW"),
				"    - Setze das Zeichen in der Tabelle an Position 0.", "line2", null,
				tpsteps);

		// algo
		String letters = "";
		char small = 0;

		for (int i = 0; i < t.length; i++) {
			char tmp = 256;
			for (int j = 0; j < t.length; j++) {
				if (t[j].charAt(0) > small && t[j].charAt(0) < tmp) {
					tmp = t[j].charAt(0);
				}
			}
			small = tmp;
			if (small != 256)
				letters += small;
		}
		// letters now is the alphabetically sorted table

		// save the letters in a seperate String for the output
		String lettersOut = letters;

		// show a StringArray the represents the encoded input
//		int inputCnt = 0;
		StringArray in = lang.newStringArray(new Offset(100, 65, step22, "SW"),
				t, "in", null, ap);
		ArrayMarker am = lang.newArrayMarker(in, 0, "am", null, amp);
		in.highlightCell(0, null, null);

		int[][] ind = new int[1][letters.length()];
		for (int i = 0; i < ind[0].length; i++) {
			ind[0][i] = i;
		}
		IntMatrix indizes = lang.newIntMatrix(new Offset(25, 25, in, "SW"), ind,
				"ind", null, mp);
		String[] l = new String[letters.length()];
		for (int i = 0; i < l.length; i++) {
			l[i] = " " + letters.charAt(i) + "  ";
		}
		StringArray let = lang.newStringArray(new Offset(-7, 7, indizes, "SW"), l,
				"letters", null, ap);

		lang.nextStep();

		String result = "";
		Text ausgabeLabel = lang.newText(new Offset(0, 30, let, "SW"), "Ausgabe:",
				"ausgabe", null, tpsteps);
		Text ausgabe = lang.newText(new Offset(10, -5, ausgabeLabel, "SE"), result,
				"ausgabe", null, tpsteps);
		ausgabe.changeColor(null, Color.BLUE, null, null);

		// iterate over the input, apend the index of the current letter, und move
		// it to the front
		for (int i = 0; i < t.length; i++) {
			if (i > 0) {
				am.move(i, null, null);
				in.highlightCell(i, null, null);
				in.unhighlightCell(i - 1, null, null);
//				inputCnt++;
			}

			// FIXME
			/*
			 * by removing the nextstep command and involving the setText command that
			 * includes the timing the arraymarker is junping to strange positions
			 */
			lang.nextStep();

			result += letters.indexOf(t[i]);
			// ausgabe.setText(result, null, new MsTiming(200));
			ausgabe.setText(result, null, null);
			letters = t[i] + letters.replace(t[i], "");
			lang.nextStep();
			for (int j = 0; j < letters.length(); j++) {
				let.put(j, " " + letters.charAt(j) + "  ", null, null);
			}
			lang.nextStep();
		}

		Text fazit = lang
				.newText(
						new Offset(-100, 60, ausgabeLabel, "SW"),
						"Die Ausgabe wird als Weiterverarbeitung Burrows Wheeler transformierter Daten verwendet.",
						"Ausgabe", null, tpsteps);
		Text fazit2 = lang
				.newText(
						new Offset(0, 20, fazit, "SW"),
						"Die Ausgabe kann nun ideal mit einer Huffman Kodierung komprimiert werden.",
						"ausgabe", null, tpsteps);
		Text fazit3 = lang
				.newText(
						new Offset(0, 20, fazit2, "SW"),
						"Das Move to Front - Verfahren ist folglich das Mittelstück eines 3 teiligen Kompressions-",
						"fazit", null, tpsteps);
		Text fazit4 = lang.newText(new Offset(0, 20, fazit3, "SW"), "verfahrens.",
				"fazit", null, tpsteps);
		Text fazit5 = lang
				.newText(
						new Offset(0, 20, fazit4, "SW"),
						"Für die Dekodierung werden die einzelnen Buchstaben mit ausgeliefert: ",
						"fazit", null, tpsteps);
		Text fazit6 = lang.newText(new Offset(20, -5, fazit5, "SE"), lettersOut,
				"fazit", null, tpsteps);
		fazit6.changeColor(null, Color.BLUE, null, null);
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
    return "Move to Front Komprimierung";
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
	  lang = new AnimalScript("Move to Front", "Florian Lindner", 800, 600);
	  lang.setStepMode(true);
	}
}
