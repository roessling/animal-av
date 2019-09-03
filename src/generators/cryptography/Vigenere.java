package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class Vigenere implements Generator {
	public int WINDOW_WIDTH = 800;
	public int WINDOW_HEIGHT = 600;
	public String ALGORITHM_NAME;
	public String DESCRIPTION;
	public String ANIMATION_AUTHOR = "Artur Seitz, Darjush Siahdohoni";

	public String SOURCE_CODE = "/*\n* M = plaintext\n* K = key\n* C = ciphertext\n*/\n"
			+ "VIGENERE-CIPHER(M, K) BEGIN\n"
			+ "\tC = \"\"\n"
			+ "\tj = 0\n"
			+ "\tFOR i=0 TO M.length-1 BEGIN\n"
			+ "\t\tIF (M[i] < 'A' || M[i] > 'Z') THEN\n"
			+ "\t\t\tcontinue\n"
			+ "\t\tEND IF\n"
			+ "\t\tC = C + (M[i] + K[j]) % 26\n"
			+ "\t\tj = (j + 1) % K.length\n"
			+ "\tEND\n"
			+ "\treturn C\n"
			+ "END";

	private Language language;
	private Translator translator;
	private Locale lang;

	public Vigenere(Locale l) {
		lang = l;
		translator = new Translator(
				"generators/cryptography/vigenereLang/vigenere", lang);
		ALGORITHM_NAME = translator.translateMessage("algorithm_name");
		DESCRIPTION = translator.translateMessage("description");
	}

	public void init() {
		language = new AnimalScript(ALGORITHM_NAME, ANIMATION_AUTHOR,
				WINDOW_WIDTH, WINDOW_HEIGHT);
		language.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		// Title
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 24));
		language.newText(new Coordinates(300, 30), ALGORITHM_NAME, "header",
				null, headerProps);

		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		language.newRect(
				new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "header", AnimalScript.DIRECTION_SE),
				"headerRect", null, rectProps);

		// Properties
		MatrixProperties matrixProperties = (MatrixProperties) props
				.getPropertiesByName("matrixProperties");
		TextProperties textProp = (TextProperties) props
				.getPropertiesByName("textProp");
		SourceCodeProperties scProp = (SourceCodeProperties) props
				.getPropertiesByName("scProp");
		// Primitives
		String message = (((String) primitives.get("message")).toUpperCase())
				.replaceAll("Ä", "AE").replaceAll("Ö", "OE")
				.replaceAll("Ü", "UE").replaceAll("ß", "SS");
		String key = ((String) primitives.get("key")).toUpperCase();
		// Timing
		Timing defaultTiming = new TicksTiming(10);

		language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		// Variables
		Variables vars = language.newVariables();

		// Start Animation
		language.nextStep();

		LinkedList<Text> textList = new LinkedList<Text>();
		textList.add(language.newText(new Offset(-250, 20, "headerRect",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line1"), "line1", null, textProp));
		textList.add(language.newText(new Offset(0, 10, "line1",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line2"), "line2", null, textProp));
		textList.add(language.newText(new Offset(0, 10, "line2",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line3"), "line3", null, textProp));
		textList.add(language.newText(new Offset(0, 10, "line3",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line4"), "line4", null, textProp));
		textList.add(language.newText(new Offset(0, 10, "line4",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line5"), "line5", null, textProp));
		textList.add(language.newText(new Offset(0, 10, "line5",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line6"), "line6", null, textProp));
		textList.add(language.newText(new Offset(0, 10, "line6",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line7"), "line7", null, textProp));
		textList.add(language.newText(new Offset(0, 10, "line7",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line8"), "line8", null, textProp));
		textList.add(language.newText(new Offset(0, 10, "line8",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line9"), "line9", null, textProp));
		textList.add(language.newText(new Offset(0, 10, "line9",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line10"), "line10", null, textProp));
		textList.add(language.newText(new Offset(0, 10, "line10",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line11"), "line11", null, textProp));
		textList.add(language.newText(new Offset(0, 10, "line11",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line12"), "line12", null, textProp));

		language.nextStep(this.translator.translateMessage("introduction"));

		for (Text text : textList)
			text.hide();

		textList = new LinkedList<Text>();

		String[][] m = new String[26][26];
		int character = 65;
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
				int tmp = character + j;
				if (tmp > 90)
					tmp -= m.length;
				m[i][j] = Character.toString((char) tmp);
			}
			character++;
		}

		textList.add(language.newText(new Offset(-250, 20, "headerRect",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("matrix"), "table", null, textProp));
		StringMatrix matrix = language.newStringMatrix(new Offset(-10, 20,
				"table", AnimalScript.DIRECTION_SW), m, "vigenere_table", null,
				matrixProperties);

		TwoValueView counter = language.newCounterView(language
				.newCounter(matrix), new Offset(30, 0, "headerRect",
				AnimalScript.DIRECTION_NE),
				new CounterProperties("counterProp"), true, true);

		language.nextStep(this.translator.translateMessage("translation"));

		textList.add(language.newText(new Offset(30, 0, "vigenere_table",
				AnimalScript.DIRECTION_NE),
				this.translator.translateMessage("keyword") + ": " + key,
				"keyword", null, textProp));
		textList.add(language.newText(new Offset(0, 20, "keyword",
				AnimalScript.DIRECTION_SW),
				this.translator.translateMessage("cleartext") + ": " + message,
				"cleartext", null, textProp));
		Text text = language.newText(new Offset(0, 20, "cleartext",
				AnimalScript.DIRECTION_SW),
				this.translator.translateMessage("ciphertext") + ": ",
				"ciphertext", null, textProp);
		textList.add(text);

		textList.add(language.newText(new Offset(0, 30, "ciphertext",
				AnimalScript.DIRECTION_SW), "Pseudocode:", "pseudocode", null,
				textProp));

		vars.declare("int", "i", "0");
		vars.declare("int", "j", "0");
		vars.declare("String", "M", message);
		vars.declare("String", "K", key);
		vars.declare("String", "C", "");

		SourceCode sc = language.newSourceCode(new Offset(0, 5, "pseudocode",
				AnimalScript.DIRECTION_NW), "", null, scProp);

		sc.addCodeLine("VIGENERE-CIPHER(M, K) BEGIN", "", 1, null);
		sc.addCodeLine("C = \\\"\\\"", "", 2, null);
		sc.addCodeLine("j = 0", "", 2, null);
		sc.addCodeLine("FOR i=0 TO M.length-1 BEGIN", "", 2, null);
		sc.addCodeLine("IF (M[i] < 'A' || M[i] > 'Z') THEN", "", 3, null);
		sc.addCodeLine("continue", "", 4, null);
		sc.addCodeLine("END IF", "", 3, null);
		sc.addCodeLine("C = C + (M[i] + K[j]) % 26", "", 3, null);
		sc.addCodeLine("j = (j + 1) % K.length", "", 3, null);
		sc.addCodeLine("END", "", 2, null);
		sc.addCodeLine("return C", "", 2, null);
		sc.addCodeLine("END", "", 1, null);

		sc.highlight(0);
		sc.highlight(1);
		sc.highlight(2);

		language.nextStep();

		sc.unhighlight(0);
		sc.unhighlight(1);
		sc.unhighlight(2);

		int j = 0;
		for (int i = 0; i < message.length(); i++) {
			vars.set("i", String.valueOf(i));
			sc.highlight(3);
			sc.highlight(9);

			if (message.charAt(i) < 'A' || message.charAt(i) > 'Z') {
				text.setText(text.getText() + message.charAt(i), null, null);
				vars.set("C", vars.get("C") + " ");

				sc.highlight(4);
				sc.highlight(5);
				sc.highlight(6);

				language.nextStep();

				sc.unhighlight(4);
				sc.unhighlight(5);
				sc.unhighlight(6);
			} else {
				sc.highlight(7);
				sc.highlight(8);

				int x = getIndex(key.charAt(j));
				int y = getIndex(message.charAt(i));

				matrix.highlightCellRowRange(0, matrix.getNrRows() - 1, y,
						null, defaultTiming);
				matrix.highlightCellColumnRange(x, 0, matrix.getNrCols() - 1,
						null, defaultTiming);

				String elem = matrix.getElement(x, y);
				vars.set("C", vars.get("C") + elem);
				text.setText(text.getText() + elem, null, null);

				language.nextStep();

				sc.unhighlight(7);
				sc.unhighlight(8);

				matrix.unhighlightCellRowRange(0, matrix.getNrRows() - 1, y,
						null, null);
				matrix.unhighlightCellColumnRange(x, 0, matrix.getNrCols() - 1,
						null, null);
			}

			sc.unhighlight(3);
			sc.unhighlight(9);

			j = (j + 1) % key.length();
			vars.set("j", String.valueOf(j));
		}

		FillInBlanksQuestionModel q = new FillInBlanksQuestionModel("Question");
		q.setPrompt(this.translator.translateMessage("question"));
		q.addAnswer("REJVS", 1, this.translator.translateMessage("right"));
		language.addFIBQuestion(q);

		sc.highlight(10);
		sc.highlight(11);

		language.nextStep(this.translator.translateMessage("result"));

		sc.hide();
		matrix.hide();
		for (Text t : textList)
			t.hide();

		counter.hide();

		language.newText(new Offset(-250, 20, "headerRect",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line13"), "line13", null, textProp);
		language.newText(
				new Offset(0, 10, "line13", AnimalScript.DIRECTION_SW), 
				this.translator.translateMessage("line14"), "line14", null, 
				textProp);
		language.newText(
				new Offset(0, 10, "line14", AnimalScript.DIRECTION_SW),
				this.translator.translateMessage("line15"), "line15", null,
				textProp);
		language.newText(
				new Offset(0, 10, "line15", AnimalScript.DIRECTION_SW),
				this.translator.translateMessage("line16"), "line16", null,
				textProp);
		language.newText(
				new Offset(0, 10, "line16", AnimalScript.DIRECTION_SW),
				this.translator.translateMessage("line17"), "line17", null,
				textProp);
		language.newText(
				new Offset(0, 10, "line17", AnimalScript.DIRECTION_SW),
				this.translator.translateMessage("line18"), "line18", null,
				textProp);
		language.newText(
				new Offset(0, 10, "line18", AnimalScript.DIRECTION_SW),
				this.translator.translateMessage("line19"), "line19", null,
				textProp);
		language.newText(
				new Offset(0, 10, "line19", AnimalScript.DIRECTION_SW),
				this.translator.translateMessage("line20"), "line20", null,
				textProp);
		language.newText(
				new Offset(0, 10, "line20", AnimalScript.DIRECTION_SW),
				this.translator.translateMessage("line21"), "line21", null,
				textProp);

		language.nextStep(this.translator.translateMessage("conclusion"));

		language.finalizeGeneration();

		return language.toString();
	}

	private int getIndex(char c) {
		return Math.abs(c - 65) % 26;
	}

	public String getAlgorithmName() {
		return ALGORITHM_NAME;
	}

	public String getAnimationAuthor() {
		return ANIMATION_AUTHOR;
	}

	public String getCodeExample() {
		return SOURCE_CODE;
	}

	public Locale getContentLocale() {
		return lang;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getFileExtension() {
		return "asu";
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getName() {
		return ALGORITHM_NAME;
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}
}