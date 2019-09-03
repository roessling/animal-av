package generators.compression;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class Sequitur extends CompressionAlgorithm implements Generator {

	private static final int		inputLimit	= 10;

	private static final String	DESCRIPTION	=
	  "Sequitur ist ein W&ouml;rterbuch-basiertes Kompressionsverfahren. "
	  + " Durch ein iteratives Vorgehen wird aus dem Eingabetext eine Grammatik erzeugt, so"
	  + " dass h&auml;ufig auftretende Zeichen kodiert werden k&ouml;nnen. Zu einem kodierten Text"
	  + " wird dementsprechend eine Kodierungsliste mit ausgeliefert.";

	private static final String	SOURCE_CODE	= "public void sequitur(char[] array) {" // 0
																							+ "\n  String tmp, rest;" // 1
																							+ "\n  Hashtable<String, String> dict = new Hashtable<String, String>();" // 2
																							+ "\n  for (int i = 0; i < input.length(); i++) {" // 3
																							+ "\n    text += input[i];" // 4
																							+ "\n  	 for (int j = 0; j < text.length() - 1; j++) {" // 5
																							+ "\n  	    tmp = text[j] + text[j+1];" // 6
																							+ "\n  		rest = null;" // 7
																							+ "\n  		for (int k = 0; k<text.length(); k++) {" // 8
																							+ "\n  	       if (k != j && k != j + 1) rest += text[k];" // 9
																							+ "\n  	    }" // 10
																							+ "\n  		if (rest.contains(tmp)) {" // 11
																							+ "\n  		   dict.put((char)ascii, tmp);" // 12
																							+ "\n  	       text = text.replaceAll(tmp, (char)ascii);" // 13
																							+ "\n  	       input = input.replaceAll(tmp, (char)ascii);" // 14
																							+ "\n  	       ascii++;" // 15
																							+ "\n  	       j = j - 2;" // 16
																							+ "\n          i = i - 2;" // 17
																							+ "\n       }" // 18
																							+ "\n    }" // 18
																							+ "\n  }" // 19
																							+ "\n}";																													// 20

	public Sequitur() {
		// nothing to be done here
	}

	public void compress(String[] textIn) throws LineNotExistsException {
		// trim input to maximum length
//		String ein = "";
		String[] t = new String[Math.min(textIn.length, inputLimit)];
		for (int i = 0; i < t.length; i++) {
			t[i] = textIn[i];
//			ein += textIn[i];
		}
//		textIn = t;
		// topic
		Text topic = lang.newText(new Coordinates(20, 50), "Sequitur", "Topic",
				null, tptopic);

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
						"1) Lese den Eingabestring buchstabenweise ein, und erweitere einen neuen",
						"line1", null, tpsteps);
		Text step12 = lang.newText(new Offset(0, 20, step1, "SW"),
				"   String um die eingelesenen Buchstaben.", "line1", null, tpsteps);
		lang.nextStep();
		Text step2 = lang
				.newText(
						new Offset(0, 40, step12, "SW"),
						"2) Tritt eine gelesene Zeichenfolge redundant auf, ersetzte sie durch eine Chiffre.",
						"line2", null, tpsteps);
		lang.nextStep();
		Text step3 = lang
				.newText(
						new Offset(0, 40, step2, "SW"),
						"3) Führe die Ersetzungen solange fort, bis der String vollständig ist.",
						"line3", null, tpsteps);
		lang.nextStep();

		algoinWords.hide();
		step1.hide();
		step12.hide();
		step2.hide();
		step3.hide();
		// extract the input
		String input = "";
		for (int i = 0; i < t.length; i++) {
			input += t[i];
		}
		input = input.toLowerCase();
		algoinWords.hide();

		StringArray in = lang.newStringArray(new Offset(0, 75, topic, "SW"),
				t, "in", null, ap);
		ArrayMarker am = lang.newArrayMarker(in, 0, "am", null, amp);
		in.highlightCell(0, null, null);
		lang.nextStep();

		/*
		 * String[] empty = new String[]{""}; StringArray strArray =
		 * lang.newStringArray(new Offset(0,155,topic,"SW"), empty, "stringArray",
		 * null, ap); strArray.hide();
		 */

		SourceCode sc = lang.newSourceCode(new Offset(0, 75, in, "SW"), "codeName",
				null, scp);
		sc.addCodeLine("public void sequitur(char[] array) {", null, 0, null);
		sc.addCodeLine("String tmp, rest;", null, 1, null);
		sc.addCodeLine(
				"Hashtable<String, String> dict = new Hashtable<String, String>();",
				null, 1, null);
		sc.addCodeLine("for (int i = 0; i < input.length(); i++) {", null, 1, null);
		sc.addCodeLine("text += input[i];", null, 2, null);
		sc.addCodeLine("for (int j = 0; j < text.length() - 1; j++) {", null, 2,
				null);
		sc.addCodeLine("tmp = text[j] + text[j + 1];", null, 3, null);
		sc.addCodeLine("rest = null;", null, 3, null);
		sc.addCodeLine("for (int k = 0; k < text.length(); k++) {", null, 3, null);
		sc.addCodeLine("if (k != j && k != j + 1) rest += text[k];", null, 4, null);
		sc.addCodeLine("}", null, 3, null);
		sc.addCodeLine("if (rest.contains(tmp)) {", null, 3, null);
		sc.addCodeLine("dict.put((char)ascii, tmp);", null, 4, null);
		sc.addCodeLine("text = text.replaceAll(tmp, (char)ascii);", null, 4, null);
		sc
				.addCodeLine("input = input.replaceAll(tmp, (char)ascii);", null, 4,
						null);
		sc.addCodeLine("ascii++;", null, 4, null);
		sc.addCodeLine("j = j - 2;", null, 4, null);
		sc.addCodeLine("i = i - 2;", null, 4, null);
		sc.addCodeLine("}", null, 3, null);
		sc.addCodeLine("}", null, 2, null);
		sc.addCodeLine("}", null, 1, null);

		// algo

		Text iLabel = lang.newText(new Offset(0, 35, sc, "SW"), "i :  ", "i", null,
				tpsteps);
		Text iContent = lang.newText(new Offset(15, 7, iLabel, "E"), "0", "i",
				null, tpsteps);
		iContent.changeColor(null, Color.RED, null, null);

		Text jLabel = lang.newText(new Offset(0, 20, iLabel, "SW"), "j :  ", "j",
				null, tpsteps);
		Text jContent = lang.newText(new Offset(15, 7, jLabel, "E"), "0", "j",
				null, tpsteps);
		jContent.changeColor(null, Color.RED, null, null);

		Text kLabel = lang.newText(new Offset(0, 20, jLabel, "SW"), "k:  ", "k",
				null, tpsteps);
		Text kContent = lang.newText(new Offset(15, 7, kLabel, "E"), "0", "k",
				null, tpsteps);
		kContent.changeColor(null, Color.RED, null, null);

		Text tmpLabel = lang.newText(new Offset(0, 20, kLabel, "SW"), "tmp:",
				"tmp", null, tpsteps);
		Text tmpContent = lang.newText(new Offset(5, 7, tmpLabel, "E"), " ", "tmp",
				null, tpsteps);
		tmpContent.changeColor(null, Color.RED, null, null);

		Text restLabel = lang.newText(new Offset(0, 20, tmpLabel, "SW"), "rest:",
				" ", null, tpsteps);
		Text restContent = lang.newText(new Offset(5, 7, restLabel, "E"), " ",
				"rest", null, tpsteps);
		restContent.changeColor(null, Color.RED, null, null);

		Vector<StringArray> strArrays = new Vector<StringArray>();

		sc.highlight(0, 0, false);
		lang.nextStep();

		sc.toggleHighlight(0, 0, false, 1, 0);
		String text = "";
		String tmp; // 1
		String rest; // 1
		lang.nextStep();

		int ascii = 65;
		sc.toggleHighlight(1, 0, false, 2, 0);
		Hashtable<String, String> dict = new Hashtable<String, String>(); // 2
		lang.nextStep();

		sc.toggleHighlight(2, 0, false, 3, 0);

		StringMatrix strMatrix = null;
		String[] textOut = null;

		// save the cell to highlight because i changes its value
		int highlightCounter = 0;
		for (int i = 0; i < input.length(); i++, highlightCounter++, sc.highlight(
				3, 0, false), iContent.setText("" + i, null, null)) { // 3

			in.highlightCell(highlightCounter, null, null);
			am.move(highlightCounter, null, null);
			if (highlightCounter > 0) {
				in.unhighlightCell(highlightCounter - 1, null, null);
			}
			lang.nextStep();

			sc.toggleHighlight(3, 0, false, 4, 0);

			text += input.charAt(i); // 4
			// print the actual result
			textOut = new String[text.length()];
			for (int p = 0; p < text.length(); p++) {
				textOut[p] = "" + text.charAt(p);
			}

			// strArray.hide();
			for (int z = 0; z < strArrays.size(); z++) {
				strArrays.elementAt(z).hide();
			}
			strArrays.add(lang.newStringArray(new Offset(0, 135, topic, "SW"),
					textOut, "stringArray", null, ap));
			lang.nextStep();

			sc.toggleHighlight(4, 0, false, 5, 0);
			for (int j = 0; j < text.length() - 1; j++, sc.highlight(5, 0, false), jContent
					.setText("" + j, null, null)) { // 5
				lang.nextStep();
				sc.toggleHighlight(5, 0, false, 6, 0);
				tmp = "" + text.charAt(j) + text.charAt(j + 1); // 6
				tmpContent.setText(tmp, null, null);
				lang.nextStep();
				sc.toggleHighlight(6, 0, false, 7, 0);
				rest = ""; // 7
				lang.nextStep();

				sc.toggleHighlight(7, 0, false, 8, 0);
				for (int k = 0; k < text.length(); k++, sc.highlight(8, 0, false), kContent
						.setText("" + k, null, null)) { // 8
					lang.nextStep();
					sc.toggleHighlight(8, 0, false, 9, 0);
					if (k != j && k != j + 1)
						rest += text.charAt(k); // 9
					restContent.setText(rest, null, null);
					lang.nextStep();
					sc.unhighlight(9, 0, false);
				} // 10
				lang.nextStep();
				sc.unhighlight(5, 0, false);
				sc.unhighlight(8, 0, false);

				sc.highlight(11, 0, false);
				if (rest.contains(tmp)) { // 11
					lang.nextStep();
					sc.toggleHighlight(11, 0, false, 12, 0);
					dict.put("" + (char) ascii, tmp); // 12
					// print the current hashtable
					String[][] dictData = new String[dict.size()][2];
					Enumeration<String> keys = dict.keys();
					for (int q = 0; q < dict.size(); q++) {
						dictData[q][0] = keys.nextElement();
						dictData[q][1] = dict.get(dictData[q][0]);
					}
					if (strMatrix != null)
						strMatrix.hide();
					strMatrix = lang.newStringMatrix(new Offset(50, 0, sc, "NE"),
							dictData, "dict", null, mp);

					lang.nextStep();
					sc.toggleHighlight(12, 0, false, 13, 0);
					text = text.replaceAll(tmp, "" + (char) ascii); // 13
					// print the actual result
					textOut = new String[text.length()];
					for (int p = 0; p < text.length(); p++) {
						textOut[p] = "" + text.charAt(p);
					}

					lang.nextStep();
					for (int z = 0; z < strArrays.size(); z++) {
						strArrays.elementAt(z).hide();
					}
					strArrays.add(lang.newStringArray(new Offset(0, 135, topic, "SW"),
							textOut, "stringArray", null, ap));
					// strArray = lang.newStringArray(new Offset(0,90,topic,"SW"),
					// textOut, "stringArray", null, ap);
					lang.nextStep();
					sc.toggleHighlight(13, 0, false, 14, 0);
					input = input.replaceAll(tmp, "" + (char) ascii); // 14
					lang.nextStep();
					sc.toggleHighlight(14, 0, false, 15, 0);
					ascii++; // 15
					lang.nextStep();
					sc.toggleHighlight(15, 0, false, 16, 0);
					j = j - 2; // 16
					lang.nextStep();
					sc.toggleHighlight(16, 0, false, 17, 0);
					i = i - 2; // 17
					lang.nextStep();
					sc.toggleHighlight(17, 0, false, 18, 0);
					lang.nextStep();
					sc.unhighlight(11, 0, false);
					sc.unhighlight(18, 0, false);
					break; // 18
				}
				sc.unhighlight(8, 0, false);
				sc.unhighlight(11, 0, false);
			}
			lang.nextStep();
			sc.unhighlight(5, 0, false);
		}
		lang.nextStep();
		sc.unhighlight(5, 0, false);

		// show the result and explain it
		String result = "";
		for (int i = 0; i < textOut.length; i++) {
			result += textOut[i];
		}

		Text fazit1 = lang.newText(new Offset(0, 50, restLabel, "SW"),
//				"Daraus ergibt sich die Ausgabe:  "
		    "Resulting encoding: ",
		    "fazit", null, tpsteps);
		tpsteps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

		lang.newText(new Offset(15, 0, fazit1, "SE"), result, "fazit1", null,
				tpsteps);
		tpsteps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		Text fazit2 = lang
				.newText(
						new Offset(0, 15, fazit1, "SW"),
						"The dictionary shown on the right has to be added to the output,",
//						"Neben der Ausgabe wird das rechts aufgeführte Wörterbuch z.B. in Form ",
						"fazit2", null, tpsteps);

		lang.newText(new Offset(0, 15, fazit2, "SW"),
		    "e.g., coded as a Hashtable.",
//				"einer Hashtable zum Dekodieren mit ausgeliefert.",
				"fazit3", null,
				tpsteps);
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
		return "Sequitur";
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
	  lang = new AnimalScript("Sequitur", "Florian Lindner", 800, 600);
	  lang.setStepMode(true);
	}
	
  @Override
  public String getAlgorithmName() {
    return "Sequitur Komprimierung (Nevill-Manning, Witten 1997)";
  }
}
