package generators.compression.lempelziv;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class LZ77Decoding extends CompressionAlgorithm implements Generator {

	private static final int		inputLimit	= 11;

	private static final String	DESCRIPTION	= "Die LZ77 Dekodierung ist die Umkehrung der gleichnamigen Kodierung.";

	private static final String	SOURCE_CODE	= "Der Algorithmus wird in einer Animation demonstriert. Um die grafische Animation in voller Größe darstellen"
																							+ " zu können, wird die Eingabe auf 30 Buchstaben begrenzt. Es handelt sich hier um einen Dekodierungsalgorithmus. Ihre Eingabe wird zunächst durch die"
																							+ " entsprechende Kodierung kodiert. Erst diese Daten werden dekodiert.";

	public LZ77Decoding() {
		// nothing to be done
	}

	/**
	 * Decode the LZ77 encoded input.
	 * 
	 * @param text
	 * @throws LineNotExistsException
	 */
	public void decode(String[] text) throws LineNotExistsException {
		// trim input to maximum length
//		String ein = "";
		String[] t = new String[Math.min(text.length, inputLimit)];
		for (int i = 0; i < t.length; i++) {
			t[i] = text[i];
//			ein += text[i];
		}
//		text = t;

		// topic
		Text topic = lang.newText(new Coordinates(20, 50), "LZ77 Decoding",
				"Topic", null, tptopic);

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
				"Durch die LZ77-Kodierung erhalten wir:  ", "line1", null, tpsteps);
		String in = LZ77compress(t);
		Text step22 = lang.newText(new Offset(10, -5, step2, "SE"), in, "line1",
				null, tpsteps);
		step22.changeColor(null, Color.RED, null, null);
		Text step3 = lang
				.newText(new Offset(0, 30, step2, "SW"),
						"Wir wollen die Ausgabe nun wieder dekodieren.", "line1", null,
						tpsteps);
		lang.nextStep();

		Text step4 = lang.newText(new Offset(10, 40, step3, "SW"),
				"Lese die 3-Tupel (x, y, z) der Eingabe einzeln ein. ", "line2", null,
				tpsteps);
		Text step41 = lang.newText(new Offset(0, 20, step4, "SW"),
				"- Ist x = 0 und y = 0, so erweitere die Ausgabe um z.", "line2", null,
				tpsteps);
		Text step42 = lang.newText(new Offset(0, 20, step41, "SW"),
				"- Ansonsten erweitere die Ausgabe um den Substring, der sich in",
				"line2", null, tpsteps);
		Text step43 = lang
				.newText(
						new Offset(0, 20, step42, "SW"),
						"  der Ausgabe an Indexstelle (absteigend numeriert!) x befindet und die",
						"line2", null, tpsteps);
		Text step44 = lang.newText(new Offset(0, 20, step43, "SW"),
				"  Länge y hat. Konkateniere zusätzlich noch z an die Ausgabe.",
				"line2", null, tpsteps);
		lang.nextStep();

		Text tupelLabel = lang.newText(new Offset(0, 60, step44, "SW"), "Tupel: ",
				"tupelLabel", null, tpsteps);
		Text tup = lang.newText(new Offset(15, -5, tupelLabel, "SE"),
				"(Index,length,next)", "tup", null, tpsteps);
		tupelLabel.changeColor(null, Color.BLUE, null, null);
		StringArray tupel = lang.newStringArray(new Offset(25, -4, tup, "NE"),
				new String[] { "     ", "     ", "     " }, "tupel", null, ap);
		tupel.changeColor(null, Color.BLUE, null, null);

		String result = "";
		Text ausgabeLabel = lang.newText(new Offset(0, 100, tupelLabel, "SW"),
				"Ausgabe:", "ausgabe", null, tpsteps);
		ausgabeLabel.changeColor(null, Color.RED, null, null);

		int[][] ind = new int[1][inputLimit];
		for (int i = 0; i < ind[0].length; i++) {
			ind[0][i] = i;
		}
		IntMatrix indizes = lang.newIntMatrix(new Offset(20, -20, ausgabeLabel,
				"Ne"), ind, "ind", null, mp);
		String[] l = new String[11];
		for (int i = 0; i < l.length; i++) {
			l[i] = "     ";
		}
		StringArray let = lang.newStringArray(new Offset(-7, 7, indizes, "SW"), l,
				"letters", null, ap);

		// algo

		int index;
		int length;
		char next;

		int s = 1;

		while (s < in.length()) {
			// parse #############
			index = Integer.parseInt("" + in.charAt(s));
			s += 2; // read ','
			length = Integer.parseInt("" + in.charAt(s));
			s += 2;// read ','
			next = in.charAt(s);
			s += 4; // read ') ('
			// #####################
			tupel.hide();
			tupel = lang.newStringArray(new Offset(25, -4, tup, "NE"), new String[] {
					"  " + index + " ", "  " + length + " ", "  " + next + " " },
					"tupel", null, ap);
			tupel.changeColor(null, Color.BLUE, null, null);

			if (index == 0 && length == 0) {
				result += next;

			} else {
				result += result.substring(result.length() - index - 1, result.length()
						- index - 1 + length);
				if (in.charAt(s - 3) != 'O')
					result += next; // prevent EOF
			}
			indizes.hide();
			let.hide();
			ind = new int[1][result.length()];
			for (int i = 0; i < ind[0].length; i++) {
				ind[0][i] = ind[0].length - i - 1;
			}
			indizes = lang.newIntMatrix(new Offset(20, -20, ausgabeLabel, "Ne"), ind,
					"ind", null, mp);
			l = new String[result.length()];
			for (int i = 0; i < result.length(); i++) {
				l[i] = "  " + result.charAt(i) + "  ";
			}
			let = lang.newStringArray(new Offset(-7, 7, indizes, "SW"), l, "letters",
					null, ap);
			lang.nextStep();
		}

		lang.newText(new Offset(0, 60, ausgabeLabel, "SW"),
				"Die Ausgabe entspricht genau der anfänglichen Eingabe.", "Ausgabe",
				null, tpsteps);
	}

	/**
	 * Encode the input by LZ77.
	 * 
	 * @param text
	 */
	public static String LZ77compress(String[] text) {
		String window = "";
		String buffer = "";
		String result = "";
		// initialize window and buffer
		for (int i = 0; i < text.length; i++) {
			buffer += text[i];
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

				// shift
				window += buffer.substring(0, 1);
				buffer = buffer.substring(1, buffer.length());
			} else {
				if (cnt + 1 < buffer.length()) {
					result += "(" + (window.length() - window.indexOf(tmp) - 1) + ","
							+ tmp.length() + "," + buffer.charAt(cnt) + ") ";

					// shift
					window += buffer.substring(0, tmp.length() + 1);
					buffer = buffer.substring(tmp.length() + 1, buffer.length());
				} else {
					result += "(" + (window.length() - window.indexOf(tmp) - 1) + ","
							+ tmp.length() + "," + "EOF)";
					break;
				}
			}
		}
		return result;
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
		return "LZ77 Dekomprimierung";
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		String[] strArray = (String[]) primitives.get("stringArray");
		try {
			decode(strArray);
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
    return "LZ77 (Lempel, Ziv 1977)";
  }

  @Override
  public void init() {
    lang = new AnimalScript("LZ77 Decoding", "Florian Lindner", 800, 600);
    lang.setStepMode(true);
  }
}
