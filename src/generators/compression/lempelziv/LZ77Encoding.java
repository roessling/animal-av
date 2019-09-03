package generators.compression.lempelziv;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class LZ77Encoding extends CompressionAlgorithm implements Generator {

	private static final int		inputLimit	= 11;

	private static final String	DESCRIPTION	= "LZ77 is the name of a lossless data compression algorithm."
																							+ " Words or parts of them that appear several times in a text are replaced by a key.";

	private static final String	SOURCE_CODE	= "The algorithm is explained by an animation. To ensure the animation "
																							+ " is not larger than the frame size, the text is limited to 11 characters.";

	public LZ77Encoding() {
		// nothing to be done
	}

	public void compress(String[] text) throws LineNotExistsException {
		// trim input to maximum length
		String[] t = new String[text.length];
		for (int i = 0; i < Math.min(inputLimit, text.length); i++) {
			t[i] = text[i];
		}
//		text = t;

		// topic
		Text topic = lang.newText(new Coordinates(20, 50), "LZ77", "Topic", null,
				tptopic);

		lang.newRect(new Offset(-5, -5, topic, "NW"),
				new Offset(5, 5, topic, "SE"), "topicRect", null, rctp);

		// Algo in words
		lang.nextStep();
		Text algoinWords = lang.newText(new Coordinates(20, 100),
				"Description of the algorithm:", "inWords", null, tpwords);

		// Algo steps
		lang.nextStep();
		Text step1 = lang
				.newText(
						new Offset(0, 100, topic, "SW"),
						"1) Set up a table with 2 columns: a text window and a buffer. The text window saves",
						"line1", null, tpsteps);
		Text step12 = lang.newText(new Offset(0, 20, step1, "SW"),
				"     the last read text with increasing indexes from right to left.",
				"line1", null, tpsteps);
		Text step13 = lang.newText(new Offset(0, 20, step12, "SW"),
				"     The buffer contains read characters that are not yet encoded.",
				"line1", null, tpsteps);
		lang.nextStep();
		Text step2 = lang
				.newText(
						new Offset(0, 40, step13, "SW"),
						"2) Find a substring in the text window that is equal to left sided beginning of the buffer.",
						"line2", null, tpsteps);
		lang.nextStep();
		Text step3 = lang.newText(new Offset(0, 40, step2, "SW"),
				"3) Encode the substring in a tuple (a,b,c).", "line3", null, tpsteps);
		Text step31 = lang
				.newText(
						new Offset(0, 20, step3, "SW"),
						"      a:    index of the position of the substring in the text window",
						"line3", null, tpsteps);
		Text step32 = lang.newText(new Offset(0, 20, step31, "SW"),
				"      b:    length of the substring", "line3", null, tpsteps);
		Text step33 = lang.newText(new Offset(0, 20, step32, "SW"),
				"      c:    following  character of the substring in the buffer",
				"line3", null, tpsteps);
		lang.nextStep();
		Text step4 = lang
				.newText(
						new Offset(0, 40, step33, "SW"),
						"4)  Shift the substring and the following character iteratively to the left window. Go to step 2).",
						"line3", null, tpsteps);
		lang.nextStep();

		algoinWords.hide();
		step1.hide();
		step12.hide();
		step13.hide();
		step2.hide();
		step3.hide();
		step31.hide();
		step32.hide();
		step33.hide();
		step4.hide();

		lang.nextStep();

		Text description = lang.newText(new Offset(0, 40, topic, "SW"),
				"We are searching for first letters of the buffer in the text window.",
				"desc", null, tpsteps);
		// extract the input
		String input = "";
		int[][] ind = new int[1][t.length];
		for (int i = 0; i < t.length; i++) {
			input += t[i];
			ind[0][i] = t.length - i - 1;
		}

		// Indizes
		IntMatrix index = lang.newIntMatrix(new Offset(5, 20, description, "SW"),
				ind, "index", null, mp);
		Text bufText = lang.newText(new Offset(60, 0, index, "SE"), "buffer",
				"buftext", null, tpsteps);
		bufText.changeColor(null, Color.BLUE, null, null);

		// text window + buffer
		String winContent[] = new String[input.length()];
		String bufContent[] = new String[input.length()];
		for (int i = 0; i < winContent.length; i++) {
			winContent[i] = "      ";
			bufContent[i] = "  " + t[i] + "  ";
		}
		StringArray windowArray = lang.newStringArray(new Offset(-5, 15, index,
				"SW"), winContent, "window", null, ap);
		StringArray bufferArray = lang.newStringArray(new Offset(35, 0,
				windowArray, "NE"), bufContent, "buffer", null, ap);
		StringArray lastWindow; // save to last used text window array to get an
														// offset
		StringArray lastBuffer; // save to last used text window array to get an
														// offset
		lastWindow = windowArray;
		lastBuffer = bufferArray;
		lang.nextStep();

		// algorithm
		String window = "";
		String buffer = "";
		String result = "";

		// initialize window and buffer
		for (int i = 0; i < t.length; i++) {
			buffer += t[i];
		}

		while (buffer.length() != 0) {
			String tmp = "" + buffer.charAt(0);
			int cnt = 0;

			while (cnt < buffer.length() - 1 && window.contains(tmp)) {
				cnt++;
				if (!window.contains(tmp + buffer.charAt(cnt)))
					break;
				tmp += "" + buffer.charAt(cnt);
			}

			if (!window.contains(tmp)) {
				result += "(0,0," + tmp + ") ";
				Text tupel = lang.newText(new Offset(10, 20, lastBuffer, "NE"), "(0,0,"
						+ tmp + ") ", "tupel", null, tpsteps);
				tupel.changeColor(null, Color.RED, null, null);
				bufferArray.highlightCell(0, tmp.length() - 1, null, null);
				lang.nextStep();

				// shift
				window += buffer.substring(0, 1);
				buffer = buffer.substring(1, buffer.length());

				// clear the array for the graphical output
				for (int i = 0; i < winContent.length; i++) {
					winContent[i] = "       ";
					bufContent[i] = "       ";
				}

				// textwinow + buffer
				for (int i = 0; i < winContent.length; i++) {
					if (i < window.length())
						winContent[winContent.length - i - 1] = "  "
								+ window.charAt(window.length() - i - 1) + "  ";
					else
						winContent[winContent.length - i - 1] = "      ";
				}

				for (int i = 0; i < bufContent.length; i++) {
					if (i > buffer.length() - 1)
						bufContent[i] = "      ";
					else
						bufContent[i] = "  " + buffer.charAt(i) + "  ";
				}
				windowArray = lang.newStringArray(new Offset(0, 20, lastWindow, "SW"),
						winContent, "window", null, ap);
				bufferArray = lang.newStringArray(new Offset(0, 20, lastBuffer, "SW"),
						bufContent, "buffer", null, ap);
				lastWindow = windowArray;
				lastBuffer = bufferArray;

				lang.nextStep();
			} else {
				if (cnt + 1 < buffer.length()) {
					result += "(" + (window.length() - window.indexOf(tmp) - 1) + ","
							+ tmp.length() + "," + buffer.charAt(cnt) + ") ";
					Text tupel = lang.newText(new Offset(10, 20, lastBuffer, "NE"), "("
							+ (window.length() - window.indexOf(tmp) - 1) + ","
							+ tmp.length() + "," + buffer.charAt(cnt) + ") ", "tupel", null,
							tpsteps);
					tupel.changeColor(null, Color.RED, null, null);
					bufferArray.highlightCell(0, tmp.length() - 1, null, null);
					lang.nextStep();

					// shift
					window += buffer.substring(0, tmp.length() + 1);
					buffer = buffer.substring(tmp.length() + 1, buffer.length());

					// clear the array for the graphical output
					for (int i = 0; i < winContent.length; i++) {
						winContent[i] = "       ";
						bufContent[i] = "       ";
					}

					// textwinow + buffer
					for (int i = 0; i < winContent.length; i++) {
						if (i < window.length())
							winContent[winContent.length - i - 1] = "  "
									+ window.charAt(window.length() - i - 1) + "  ";
						else
							winContent[winContent.length - i - 1] = "      ";
					}

					for (int i = 0; i < bufContent.length; i++) {
						if (i > buffer.length() - 1)
							bufContent[i] = "      ";
						else
							bufContent[i] = "  " + buffer.charAt(i) + "  ";
					}
					windowArray = lang.newStringArray(
							new Offset(0, 20, lastWindow, "SW"), winContent, "window", null,
							ap);
					bufferArray = lang.newStringArray(
							new Offset(0, 20, lastBuffer, "SW"), bufContent, "buffer", null,
							ap);
					lastWindow = windowArray;
					lastBuffer = bufferArray;

					lang.nextStep();
				} else {
					result += "(" + (window.length() - window.indexOf(tmp) - 1) + ","
							+ tmp.length() + "," + "EOF)";
					Text tupel = lang.newText(new Offset(10, 20, lastBuffer, "NE"), "("
							+ (window.length() - window.indexOf(tmp) - 1) + ","
							+ tmp.length() + "," + "EOF)", "tupel", null, tpsteps);
					tupel.changeColor(null, Color.RED, null, null);
					bufferArray.highlightCell(0, tmp.length() - 1, null, null);
					lang.nextStep();
					break;
				}
			}

			lang.nextStep();

		}

		Text fazit = lang.newText(new Offset(0, 90, lastWindow, "SW"), "output:  ",
				"Ausgabe", null, tpsteps);
		Text fazit2 = lang.newText(new Offset(15, -5, fazit, "SE"), result,
				"Ausgabe", null, tpsteps);
		fazit2.changeColor(null, Color.BLUE, null, null);
		Text fazit3 = lang.newText(new Offset(0, 20, fazit2, "SW"),
				"The result can now be compressed by Huffman encoding.", "fazit", null,
				tpsteps);

		lang.newText(new Offset(0, 20, fazit3, "SW"),
				"You can find a combination of this procedures in Deflate Encoding.",
				"fazit", null, tpsteps);
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

  public String getAlgorithmName() {
    return "LZ77 (Lempel, Ziv 1977)";
  }
	public String getName() {
		return "LZ77 Compression";
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

	/**
	 * getContentLocale returns the target Locale of the generated output Use e.g.
	 * Locale.US for English content, Locale.GERMANY for German, etc.
	 * 
	 * @return a Locale instance that describes the content type of the output
	 */
	public Locale getContentLocale() {
		return Locale.US;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
	}
	
	public void init() {
	  lang = new AnimalScript("LZ77 EN", "Florian Lindner", 800, 600);
	  lang.setStepMode(true);
	}
}
