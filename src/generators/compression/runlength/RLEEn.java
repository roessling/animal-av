package generators.compression.runlength;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class RLEEn extends CompressionAlgorithm implements Generator {

	private static final int		inputLimit	= 15;

	SourceCode									sc;

	private static final String	DESCRIPTION	= 
	  "Run Length Encoding is a lossless compression algorithm. It"
	  + " is used for texts where characters are often repeated subsequently."
	  + " For example, the string 'AAAA' is summarized to '4A'. Characters that appear singly"
	  + " are not encoded. This version of the RLE algorithm cannot encode digits.";

	private static final String	SOURCE_CODE	= "The algorithm is explained by an animation. To ensure the animation "
																							+ " is not bigger than the size of the frame the text is limited to 15 characters.\n\n"
																							+ "public void rle(char[] array) {" // 0
																							+ "\n  int count = 1;" // 1
																							+ "\n  char tmp;" // 2
																							+ "\n  String result = \"\"" // 3
																							+ "\n  for (int i = 0; i < array.length; i++) {" // 4
																							+ "\n  	 tmp = array[i];" // 5
																							+ "\n  	 if (i == array.length - 1) {" // 6
																							+ "\n  		result += tmp;" // 7
																							+ "\n  		break;" // 8
																							+ "\n  	 }" // 9
																							+ "\n  	 while (array[i + 1] == tmp) {" // 10
																							+ "\n  		i++;" // 11
																							+ "\n  		count++;" // 12
																							+ "\n  	 }" // 13
																							+ "\n  	 if (count == 1) result += tmp;" // 14
																							+ "\n  	 else result = result + (String)count + tmp;" // 15
																							+ "\n  	 count = 1;" // 16
																							+ "\n  }" // 17
																							+ "\n}";																																				// 18

	public RLEEn() {
		// nothing to be done here
	}

	public void compress(String[] text) throws LineNotExistsException {

		// trim input to maximum length
		String[] t = new String[text.length];
		for (int i = 0; i < Math.min(inputLimit, text.length); i++) {
			t[i] = text[i];
		}
//		text = t;

		// topic
		Text topic = lang.newText(new Coordinates(20, 50),
				"Run Length Encoding EN", "Topic", null, tptopic);

		lang.newRect(new Offset(-5, -5, topic, "NW"),
				new Offset(5, 5, topic, "SE"), "topicRect", null, rctp);

		lang.nextStep();

		Text algoinWords = lang.newText(new Coordinates(20, 100),
				"Description of the algorithm:", "inWords", null, tpwords);

		lang.nextStep();

		Text step1 = lang.newText(new Offset(0, 100, topic, "SW"),
				"1) Iteratively read the characters of the string array.", "line1",
				null, tpsteps);
		lang.nextStep();
		Text step2 = lang.newText(new Offset(0, 40, step1, "SW"),
				"2) Count the number of equal subsequent following characters.",
				"line2", null, tpsteps);
		lang.nextStep();
		Text step3 = lang.newText(new Offset(0, 40, step2, "SW"),
				"3) Encode nX if there is a string with n times the character X and .",
				"line3", null, tpsteps);
		Text step31 = lang.newText(new Offset(0, 20, step3, "SW"),
				"   no letter inbetween.", "line3", null, tpsteps);
		lang.nextStep();
		Text step4 = lang.newText(new Offset(0, 40, step31, "SW"),
				"4) For the unique appereance of the letter Y encode Y.", "line4",
				null, tpsteps);
		lang.nextStep();

		algoinWords.hide();
		step1.hide();
		step2.hide();
		step3.hide();
		step31.hide();
		step4.hide();

		StringArray strArray = lang.newStringArray(new Offset(0, 100, topic, "SW"),
				t, "stringArray", null, ap);

		sc = lang.newSourceCode(new Offset(0, 50, strArray, "SW"), "codeName",
				null, scp);
		sc.addCodeLine("public void rle(char[] array) {", null, 0, null);
		sc.addCodeLine("int count = 1;", null, 1, null);
		sc.addCodeLine("char tmp; ", null, 1, null);
		sc.addCodeLine("String result;", null, 2, null);
		sc.addCodeLine("for (int i = 0; i < array.length; i++) {", null, 1, null);
		sc.addCodeLine("tmp = array[i];", null, 2, null);
		sc.addCodeLine("if (i == array.length - 1) {", null, 3, null);
		sc.addCodeLine("result += tmp;", null, 4, null);
		sc.addCodeLine("break;", null, 4, null);
		sc.addCodeLine("}", null, 3, null);
		sc.addCodeLine("while (array[i + 1] == tmp) {", null, 2, null);
		sc.addCodeLine("i++;", null, 3, null);
		sc.addCodeLine("count++;", null, 3, null);
		sc.addCodeLine("}", null, 2, null);
		sc.addCodeLine("if (count == 1) result += tmp;", null, 2, null);
		sc
				.addCodeLine("else result = result + (String)count + tmp;", null, 2,
						null);
		sc.addCodeLine("count = 1;", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("}", null, 0, null);

		// initialize the matrix that shows tmp and count
		String[][] varData = new String[2][2];
		varData[0][0] = "count";
		varData[1][0] = "tmp";
		varData[0][1] = "   ";
		varData[1][1] = "   ";
		StringMatrix varMatrix = lang.newStringMatrix(new Offset(75, 0, sc, "NE"),
				varData, "varMatrix", null, mp);

		Text resultLabel = lang.newText(new Offset(0, 50, sc, "SW"), "output:   ",
				"resultLabel", null, tpsteps);
		Text resultText = lang.newText(new Offset(10, -5, resultLabel, "SE"),
				"   ", "result", null, tpsteps);
		resultText.changeColor(null, Color.BLUE, null, null);
		lang.nextStep();

		sc.highlight(0, 0, false);
		lang.nextStep();

		sc.toggleHighlight(0, 0, false, 1, 0);
		varMatrix.put(0, 1, " 1 ", null, null);
		lang.nextStep();

		sc.toggleHighlight(1, 0, false, 2, 0);
		lang.nextStep();

		sc.toggleHighlight(2, 0, false, 3, 0);
		lang.nextStep();

		sc.toggleHighlight(3, 0, false, 4, 0);
		lang.nextStep();
		sc.unhighlight(4, 0, false);
		ArrayMarker am = lang.newArrayMarker(strArray, 0, "arrayMarker", null, amp);

		int marked = 0;

		int count = 1;
		String tmp;
		String result = "";

		for (int i = 0; i < t.length; i++) {
			am.move(i, null, null);
			for (int m = 0; m <= marked; m++) {
				strArray.unhighlightCell(i - m, null, null);
			}
			marked = 0;
			strArray.highlightCell(i, null, null);
			marked++;
			if (i > 0)
				strArray.unhighlightCell(i - 1, null, null);
			sc.unhighlight(4, 0, false);
			sc.highlight(5, 0, false);
			tmp = t[i];

			varMatrix.put(1, 1, " " + tmp + " ", null, null);
			lang.nextStep();

			sc.toggleHighlight(5, 0, false, 6, 0);
			if (i == t.length - 1) {
				lang.nextStep();
				sc.toggleHighlight(6, 0, false, 7, 0);
				result += tmp;

				resultText.setText(result, null, null);

				lang.nextStep();
				sc.toggleHighlight(7, 0, false, 8, 0);
				lang.nextStep();
				break;
			}
			// else
			lang.nextStep();
			sc.unhighlight(6, 0, false);

			sc.unhighlight(8, 0, false);
			sc.highlight(10, 0, false);
			while (t[i + 1].equals(tmp)) {
				lang.nextStep();
				sc.unhighlight(10, 0, false);
				sc.unhighlight(12, 0, false);
				sc.highlight(11, 0, false);
				i++;
				am.move(i, null, null);
				strArray.highlightCell(i, null, null);
				marked++;
				lang.nextStep();
				sc.toggleHighlight(11, 0, false, 12, 0);
				count++;

				varMatrix.put(0, 1, " " + count + " ", null, null);

			}
			lang.nextStep();
			sc.unhighlight(12, 0, false);
			sc.unhighlight(10, 0, false);
			sc.highlight(14, 0, false);
			if (count == 1) {
				lang.nextStep();
				sc.toggleHighlight(14, 0, false, 16, 0);
				result += tmp;
				resultText.setText(result, null, null);
			} else {
				lang.nextStep();
				result = result + (new Integer(count)).toString() + tmp;
				sc.toggleHighlight(14, 0, false, 15, 0);
				lang.nextStep();
				sc.toggleHighlight(15, 0, false, 16, 0);
				resultText.setText(result, null, null);
			}
			lang.nextStep();
			count = 1;
			varMatrix.put(0, 1, " " + count + " ", null, null);
			sc.toggleHighlight(16, 0, false, 4, 0);
			lang.nextStep();
		}

		// mark the end
		lang.nextStep();
		sc.unhighlight(8, 0, false);
		sc.highlight(18, 0, false);

		lang.nextStep();

		Text info = lang.newText(new Offset(0, 35, resultLabel, "SW"),
				"The Run Length Encoding is appropriate for texts that", "info", null,
				tpsteps);

		lang.newText(new Offset(0, 20, info, "SW"),
				"contain many subsequently repeating letters.", "info", null, tpsteps);
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
		return "Run Length Encoding";
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

	/**
	 * getContentLocale returns the target Locale of the generated output Use e.g.
	 * Locale.US for English content, Locale.GERMANY for German, etc.
	 * 
	 * @return a Locale instance that describes the content type of the output
	 */
	public Locale getContentLocale() {
		return Locale.US;
	}

  @Override
  public String getAlgorithmName() {
    return "Run-Length Encoding (RLE)";
  }
  
  @Override
  public void init() {
    lang = new AnimalScript("Run Length Encoding EN", "Florian Lindner", 800, 600);
    lang.setStepMode(true);
  }
}
