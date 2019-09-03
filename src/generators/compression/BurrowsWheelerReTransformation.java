package generators.compression;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class BurrowsWheelerReTransformation extends CompressionAlgorithm
		implements Generator {

	public static int						index;

	private static final int		inputLimit	= 20;

	private static final String	DESCRIPTION	= "Die Burrows Wheeler Retransformation ist die Umkehrung der gleichnamigen Transformation. Die Transformation"
																							+ " dient als eine Vorbereitung f&uuml;r andere Kompressionsverfahren. Es handelt sich um eine Transformation,"
																							+ " da die Daten nicht verringert, sondern nur anders angeordnet werden.";

	private static final String	SOURCE_CODE	= "Der Algorithmus wird in einer Animation demonstriert.\nUm die grafische Animation in voller Gr&ouml;&szlig;e darstellen"
																							+ " zu k&ouml;nnen,\nwird die Eingabe auf 20 Buchstaben begrenzt.\nEs handelt sich hier um einen Dekodierungsalgorithmus.\nIhre Eingabe wird zun&auml;chst durch die"
																							+ " entsprechende Kodierung kodiert.\nErst diese Daten werden dekodiert.";

	public BurrowsWheelerReTransformation() {
		// nothing to be done here
	}

	/**
	 * Re-Transforms the input
	 * 
	 * @param text
	 */
	public void transform(String[] text) {

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
				"Burrows Wheeler Retransformation", "Topic", null, tptopic);

		lang.newRect(new Offset(-5, -5, topic, "NW"),
				new Offset(5, 5, topic, "SE"), "topicRect", null, rctp);

		// Algo in words
		lang.nextStep();
		Text algoinWords = lang.newText(new Coordinates(20, 100),
				"Der Algorithmus in Worten", "inWords", null, tpwords);

		// Algo steps
		lang.nextStep();
		Text step1 = lang.newText(new Offset(0, 100, topic, "SW"),
				"1) Bilde einen Vektor L, der genau die Eingabebuchstaben beinhaltet.",
				"line1", null, tpsteps);
		lang.nextStep();
		Text step2 = lang.newText(new Offset(0, 30, step1, "SW"),
				"2) Bilde einen Vektor F, der die Eingabebuchstaben in alphabetisch",
				"line2", null, tpsteps);
		Text step21 = lang.newText(new Offset(0, 30, step2, "SW"),
				"      geordneter Reihenfolge beinhaltet.", "line21", null, tpsteps);
		lang.nextStep();
		Text step3 = lang.newText(new Offset(0, 30, step21, "SW"),
				"3) Erstelle den Transformationsvektor T, welcher L auf F abbildet.",
				"line3", null, tpsteps);
		lang.nextStep();
		Text step4 = lang.newText(new Offset(0, 30, step3, "SW"),
				"4) Berechne die Vorgängerbuchstaben jedes Buchstabens in L. Beginne",
				"line4", null, tpsteps);
		Text step41 = lang.newText(new Offset(0, 20, step4, "SW"),
				"      hierfür an der Indexstelle, welche bei der Transformation",
				"line32", null, tpsteps);
		Text step42 = lang.newText(new Offset(0, 20, step41, "SW"),
				"      übergeben wurde. Für den Vorgängerbuchstaben gilt:", "line32",
				null, tpsteps);
		Text step43 = lang.newText(new Offset(0, 20, step42, "SW"),
				"      Präfix von L[i] = L[T[i]]", "line32", null, tpsteps);
		lang.nextStep();

		algoinWords.hide();
		step1.hide();
		step2.hide();
		step21.hide();
		step3.hide();
		step4.hide();
		step41.hide();
		step42.hide();
		step43.hide();
		tpwords.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
				Font.PLAIN, 16));

		// extract the input
		String input = "";
		for (int i = 0; i < t.length; i++) {
			input += t[i];
		}
		algoinWords.setText("Eingabe:  " + input, null, null);
		algoinWords.show();
		lang.nextStep();

		// get the enocoded string
		input = encode(t);

		lang.newText(new Offset(0, 20, algoinWords, "SW"),
				"Durch die Burrows Wheeler Transformation erhalten wir: "
						+ encode(t) + " sowie die Indexstelle " + index, "text", null,
				tpwords);
		lang.nextStep();

		// get a legend for the index
		TextProperties tplabel = new TextProperties();
		tplabel.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
				Font.BOLD, 22));
		tplabel.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		int[][] indexContent = new int[input.length()][1];
		for (int i = 0; i < input.length(); i++) {
			indexContent[i][0] = i;
		}
		Text labelIndex = lang.newText(new Offset(0, 74, step1, "SW"), "Index",
				"lable", null, tplabel);
		IntMatrix matrixInd = lang.newIntMatrix(
				new Offset(-5, 15, labelIndex, "S"), indexContent, "matrixInd", null,
				mp);

		lang.nextStep();
		step1.changeColor(null, Color.RED, null, null);
		step1.show();

		// step 1: print L the input
		String[] L = new String[input.length()];
		for (int i = 0; i < input.length(); i++) {
			L[i] = "" + input.charAt(i);
		}

		String[][] contentL = new String[L.length][1];
		for (int i = 0; i < L.length; i++) {
			contentL[i][0] = L[i];
		}

		lang.nextStep();
		Text labelL = lang.newText(new Offset(70, 74, step1, "SW"), "L", "lable",
				null, tplabel);
		StringMatrix matrixL = lang.newStringMatrix(
				new Offset(-5, 15, labelL, "S"), contentL, "matrixL", null, mp);

		lang.nextStep();
		step1.setText(step2.getText(), null, null);
		step2.changeColor(null, Color.RED, null, null);
		step2.setText(step21.getText(), null, null);
		step2.show();

		lang.nextStep();
		// F alphabetically ordered
		String[] F = new String[input.length()];
		String tmpS = input;
		char tmp = 255;

		for (int i = 0; i < input.length(); i++) {
			tmp = 255;
			for (int j = 0; j < tmpS.length(); j++) {
				if (tmpS.charAt(j) < tmp)
					tmp = tmpS.charAt(j);
			}
			F[i] = "" + tmp;
			tmpS = tmpS.replaceFirst("" + tmp, "");
		}

		String[][] contentF = new String[F.length][1];
		for (int i = 0; i < F.length; i++) {
			contentF[i][0] = F[i];
		}

		Text labelF = lang.newText(new Offset(160, 74, step1, "SW"), "F", "lable",
				null, tplabel);

		lang.newStringMatrix(new Offset(-5, 15, labelF, "S"), contentF, "matrixF",
				null, mp);

		lang.nextStep();
		step2.hide();
		step1.setText(step3.getText(), null, null);

		lang.nextStep();

		// T the transformation Vector
		int[] T = new int[input.length()];
		boolean[] done = new boolean[input.length()];
		for (int i = 0; i < L.length; i++) {

			for (int j = 0; j < F.length; j++) {
				if (L[i].equals(F[j]) && done[j] == false) {
					T[i] = j;
					done[j] = true;
					break;
				}
			}

		}
		int[][] contentT = new int[T.length][1];
		for (int i = 0; i < T.length; i++) {
			contentT[i][0] = T[i];
		}

		Text labelT = lang.newText(new Offset(250, 74, step1, "SW"), "T", "lable",
				null, tplabel);
		IntMatrix matrixT = lang.newIntMatrix(new Offset(-5, 15, labelT, "S"),
				contentT, "matrixT", null, mp);

		lang.nextStep();
		step1.changeColor(null, Color.BLACK, null, null);
		Text iterate = lang
				.newText(
						new Offset(0, 50, matrixInd, "SW"),
						"Wir berechnen die Vorbuchstaben jedes Buchstaben der Ausgabe durch: Präfix von L[i] = L[T[i]]",
						"iter", null, tpsteps);
		Text iterate2 = lang.newText(new Offset(0, 15, iterate, "SW"),
				"und beginnen in L an der errechneten Indexstelle ", "iter", null,
				tpsteps);
		Text iterate3 = lang.newText(new Offset(15, -4, iterate2, "SE"),
				"" + index, "iter", null, tpsteps);
		iterate.changeColor(null, Color.RED, null, null);
		iterate2.changeColor(null, Color.RED, null, null);
		iterate3.changeColor(null, Color.BLUE, null, null);

		lang.nextStep();
		// start at the given index at L to get prefixes of each ketter
		String result = L[index];
		// int cnt = index;

		Text fin;
		Text fin2;
		while (result.length() < input.length()) {
			fin = lang.newText(new Offset(0, 50, iterate2, "SW"), "Das Präfix von "
					+ L[index] + " ist " + L[T[index]], "fin", null, tpsteps);
			result = L[T[index]] + result;

			// highlight cells
			matrixL.highlightCell(index, 0, null, null);
			matrixL.highlightCell(T[index], 0, null, null);
			matrixT.highlightCell(index, 0, null, null);

			fin2 = lang.newText(new Offset(0, 15, fin, "SW"),
					"Kombiniert ergibt das für die Ausgabe:   " + result, "fin", null,
					tpsteps);

			lang.nextStep();

			// unhighlight cells
			matrixL.unhighlightCell(index, 0, null, null);
			matrixL.unhighlightCell(T[index], 0, null, null);
			matrixT.unhighlightCell(index, 0, null, null);

			index = T[index];
			fin.hide();
			fin2.hide();
		}

		lang.nextStep();
		// show the result and explain it
		Text fazit1 = lang.newText(new Offset(0, 50, iterate2, "SW"),
				"Daraus ergibt sich die Ausgabe:  ", "fazit", null, tpsteps);
		tpsteps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

		lang.newText(new Offset(15, 8, fazit1, "E"), result, "fazit1", null,
				tpsteps);
		tpsteps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	}

	/**
	 * transform the input
	 * 
	 * @param text
	 * @return
	 */
	private static String encode(String text[]) {
		// fill a vector with all rotations
		Vector<String[]> rotations = new Vector<String[]>(0, 1);
		String[] tmp = text;
		for (int i = 0; i < text.length; i++) {
			rotations.add(rotateLeft(tmp));
			tmp = rotateLeft(tmp);
		}

		// sort the vector
		Vector<String[]> sorted = new Vector<String[]>(0, 1);
		String[] early = rotations.elementAt(0);
		while (!rotations.isEmpty()) {
			for (int i = 0; i < rotations.size(); i++) {
				if (isEarlier(rotations.elementAt(i), early)) {
					early = rotations.elementAt(i);
					index = 1;
				}
			}
			sorted.add(early);
			rotations.removeElement(early);
			index = 0;
			if (!rotations.isEmpty())
				early = rotations.elementAt(0);
		}

		// get the index for the output row
		for (int i = 0; i < sorted.size(); i++) {
			boolean equal = true;
			for (int j = 0; j < text.length; j++) {
				if (text[j] != sorted.elementAt(i)[j]) {
					equal = false;
					break;
				}
			}
			if (equal) {
				index = i;
				break;
			}
		}

		// take the last letters in a row
		String result = "";

		for (int i = 0; i < sorted.size(); i++) {
			result += sorted.elementAt(i)[sorted.elementAt(i).length - 1];
		}
		return result;
	}

	/**
	 * Rotates a string array one position to the left side
	 * 
	 * @param text
	 *          The text string array
	 */
	public static String[] rotateLeft(String[] text) {
		String[] tmp = new String[text.length];
		for (int i = 0; i < text.length - 1; i++) {
			tmp[i] = text[i + 1];
		}
		tmp[text.length - 1] = text[0];
		return tmp;
	}

	/**
	 * Tests whether text1 is alphabetically smaller then text2
	 * 
	 * @param text1
	 * @param text2
	 */
	public static boolean isEarlier(String[] text1, String[] text2) {
		int first;
		int second;
		for (int i = 0; i < text1.length; i++) {
			first = (new Integer(text1[i].charAt(0))).intValue();
			second = (new Integer(text2[i].charAt(0))).intValue();
			if (text1[i].equals("."))
				first = Integer.MAX_VALUE;
			if (text2[i].equals("."))
				second = Integer.MAX_VALUE;

			if (first < second)
				return true;
			if (first > second)
				return false;
		}
		// will never happen on different texts with the same size!
		return false;
	}

	public static String getDESCRIPTION() {
		return DESCRIPTION;
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
		return "Burrows-Wheeler Re-Transformation";
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		String[] strArray = (String[]) primitives.get("stringArray");
		transform(strArray);

		lang.finalizeGeneration();
		return lang.getAnimationCode();
	}

	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
	}

	public String getAlgorithmName() {
		return "Burrows-Wheeler Transformation";
	}

	public void init() {
		lang = new AnimalScript("Burrows Wheeler Retransformation",
        "Florian Lindner", 800, 600);
		lang.setStepMode(true);
	}

}
