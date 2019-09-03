package generators.hashing;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class LinearHashing7 implements Generator {

	static class SimpleFileWriter {
		public static void writeFile(File selSort, String data) {
			try {
				FileWriter fw = new FileWriter(selSort);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(data);
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Language lang;
	private ArrayProperties arrayProperties;
	private int n;
	private IntArray eingabeArray;
	private IntArray ausgabeArray;
	private ArrayMarkerProperties arrayMarkerProperties;
	private ArrayMarker arrayMarker;
	private Text hashFunktionText, hashErgebnisText, kollisionsFunktionText,
			kollisionsErgebnisText, kollisionsText;
	private Timing defaultTiming;
	private int[] arrayContents;
	private Rect background;
	private SourceCode sc;

	private int a;
	private int b;
	private int showCounter;

	public LinearHashing7() {
	}
    private void setupDefaults() {
		arrayProperties = new ArrayProperties();
		TextProperties textProperties = new TextProperties();
		textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		Text header = lang.newText(new Coordinates(20, 30), "Lineares Hashing",
				"header", null, textProperties);

		RectProperties rectProperties = new RectProperties();
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		background = lang.newRect(new Offset(-5, -5, header,
				AnimalScript.DIRECTION_NW), new Offset(5, 5, header,
				AnimalScript.DIRECTION_SE), "HeaderBackground", null,
				rectProperties);

		defaultTiming = new TicksTiming(100);
	}

	public void init() {
		lang = new AnimalScript("LinearesHashing",
				"Johannes Born und Tuba G&ouml;zel", 640, 480);
		lang.setStepMode(true);
	}

	public void showSourceCode() {

		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 12));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		sc = lang.newSourceCode(new Offset(300, 0, background,
				AnimalScript.DIRECTION_NE), "sourceCode", null, scProps);

		// SourceCode der runHashing-Funktion
		sc.addCodeLine("private int [] runHashing(int[] eingabe){", null, 0,
				null); // line 0
		sc.addCodeLine("int n = eingabe.length;", null, 1, null); // line 1
		sc.addCodeLine("int[] ausgabe = new int[n];", null, 1, null); // line 2
		sc.addCodeLine("int hashWert;", null, 1, null); // line 3
		sc.addCodeLine("for(int i = 0; i < n; i++){", null, 1, null); // line 4
		sc.addCodeLine("int input = eingabe[i];", null, 2, null); // line 5
		sc.addCodeLine("if (input != 0) {", null, 2, null); // line 6
		sc.addCodeLine("hashWert = hashFunktion(input);", null, 3, null); // line
		// 7
		sc.addCodeLine("if (hashWert != -1) ", null, 3, null); // line 8
		sc.addCodeLine("ausgabe[hashWert] = input;", null, 4, null); // line
		// 9
		sc.addCodeLine("}", null, 2, null); // line 10
		sc.addCodeLine("}", null, 1, null); // line 11
		sc.addCodeLine("return ausgabe;", null, 1, null); // line 12
		sc.addCodeLine("}", null, 0, null); // line 13
		sc.addCodeLine("", null, 0, null); // line 14

		// SourceCode der hashFunktion
		sc.addCodeLine("private int hashFunktion(int input, int[] ausgabe) {",
				null, 0, null); // line 15
		sc.addCodeLine("int hashWert = (a * input + b) % n;", null, 1, null); // line
		// 16
		sc.addCodeLine("if (ausgabe[hashWert] == 0)", null, 1, null); // line
		// 17
		sc.addCodeLine("return (a * input + b) % n;", null, 2, null); // line 18
		sc.addCodeLine("else ", null, 1, null); // line 19
		sc.addCodeLine("return kollisionsHashFunktion(input, hashWert);", null,
				2, null); // line 20
		sc.addCodeLine("}", null, 0, null); // line 21
		sc.addCodeLine("", null, 0, null); // line 22

		// SourceCode der kollisionsHashFunktion
		sc
				.addCodeLine(
						"private int kollisionsHashFunktion(int input, int hashWert, int[] ausgabe) {",
						null, 0, null); // line 23
		sc.addCodeLine("int kollisionsHashWert;", null, 1, null); // line 24
		sc.addCodeLine("for (int i = 0; i < n; i++){", null, 1, null); // line
		// 25
		sc.addCodeLine("kollisionsHashWert = (hashWert + i) % n;", null, 2,
				null); // line 26
		sc.addCodeLine("if (ausgabe[kollisionsHashWert] == 0)", null, 2, null); // line
		// 27
		sc.addCodeLine("return kollisionsHashWert;", null, 3, null); // line 28
		sc.addCodeLine("}", null, 1, null); // line 29
		sc.addCodeLine("return -1;", null, 0, null); // line 30
		sc.addCodeLine("}", null, 0, null); // line 31
	}

	private void runHashing(int[] eingabe) {
		sc.highlight(0);
		lang.nextStep();

		sc.toggleHighlight(0, 1);
		int n = eingabe.length;
		lang.nextStep();

		sc.toggleHighlight(1, 2);
		lang.nextStep();

		sc.toggleHighlight(2, 3);
		hashFunktionText.setText("Hashfunktion: h(x) = " + a + " * x + " + b
				+ " mod " + n + "", null, defaultTiming);
		kollisionsFunktionText.setText(
				"Kollisionsfunktion: h'(x) = (h(x) + i) mod " + n, null,
				defaultTiming);
		int hashWert;
		lang.nextStep();

		sc.toggleHighlight(3, 4);

		for (int i = 0; i < n; i++) {
			lang.nextStep();
			sc.toggleHighlight(4, 5);
			eingabeArray.highlightCell(i, null, defaultTiming);
			int input = eingabe[i];
			lang.nextStep();

			sc.toggleHighlight(5, 6);
			lang.nextStep();

			if (input != 0) {
				sc.toggleHighlight(6, 7);
				lang.nextStep();

				sc.highlight(15);
				hashWert = hashFunktion(input);
				lang.nextStep();

				sc.toggleHighlight(7, 8);
				lang.nextStep();
				if (hashWert != -1) {

					sc.toggleHighlight(8, 9);
					ausgabeArray.put(hashWert, input, null, null);
					ausgabeArray.highlightCell(hashWert, null, defaultTiming);
					lang.nextStep();

					for (int j = 0; j <= showCounter; j++) {
						kollisionsErgebnisText.hide();
					}
					showCounter = 0;
					eingabeArray.unhighlightCell(i, null, defaultTiming);
					kollisionsText.hide(defaultTiming);
					sc.unhighlight(9);
				} else {
					sc.unhighlight(8);
				}
			} else {
				sc.unhighlight(6);
			}
			sc.highlight(4);
		}
		sc.unhighlight(4);
		sc.highlight(12);
		lang.nextStep();
	}

	private int hashFunktion(int input) {
		lang.nextStep();
		sc.toggleHighlight(15, 16);
		int hashWert = (a * input + b) % n;
		hashErgebnisText.setText("Berechnung der Hashfunktion mit x = " + input
				+ " ergibt h(x) = " + hashWert, null, defaultTiming);
		lang.nextStep();

		sc.toggleHighlight(16, 17);
		lang.nextStep();
		if (ausgabeArray.getData(hashWert) == 0) {
			sc.toggleHighlight(17, 18);
			lang.nextStep();
			sc.unhighlight(18);
			return hashWert;
		} else {
			sc.toggleHighlight(17, 19);
			kollisionsText.setText(
					"Position besetzt. Kollisionsfunktion wird aufgerufen.",
					null, null);
			kollisionsText.show();
			arrayMarker.move(hashWert, null, null);
			lang.nextStep();

			sc.toggleHighlight(19, 20);
			lang.nextStep();
			sc.highlight(23);
			lang.nextStep();
			return kollisionsHashFunktion(input, hashWert);
		}
	}

	private int kollisionsHashFunktion(int input, int hashWert) {
		sc.toggleHighlight(23, 24);
		int kollisionsHashWert;
		lang.nextStep();

		sc.toggleHighlight(24, 25);
		for (int i = 0; i < n; i++) {
			lang.nextStep();
			sc.toggleHighlight(25, 26);
			kollisionsHashWert = (hashWert + i) % n;
			ausgabeArray.highlightElem(kollisionsHashWert, null, null);
			arrayMarker.move(kollisionsHashWert, null, defaultTiming);
			kollisionsErgebnisText.setText(
					"Berechnung der Kollisionsfunktion mit i = " + i
							+ " ergibt h'(x) = " + kollisionsHashWert, null,
					defaultTiming);
			kollisionsErgebnisText.show();
			showCounter++;
			lang.nextStep();

			sc.toggleHighlight(26, 27);
			lang.nextStep();
			if (ausgabeArray.getData(kollisionsHashWert) == 0) {
				sc.toggleHighlight(27, 28);
				lang.nextStep();
				sc.unhighlight(28);
				lang.nextStep();
				sc.unhighlight(20);
				for (int j = 0; j < ausgabeArray.getLength(); j++) {
					ausgabeArray.unhighlightElem(j, null, null);
				}
				return kollisionsHashWert;
			} else {
				sc.unhighlight(27);
				sc.highlight(25);
			}
		}
		return -1;
	}
//
//	private void initForMain() {
//		arrayContents = new int[] { 457, 203, 105, 154, 216, 112, 981 };
//		n = arrayContents.length;
//		a = 3;
//		b = 5;
//		arrayProperties.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
//				Color.RED);
//		arrayProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
//		arrayProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
//		arrayProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
//				Color.YELLOW);
//
//		initForGenerate();
//		runHashing(arrayContents);
//	}

	private void initForGenerate() {

		eingabeArray = lang.newIntArray(new Coordinates(50, 100),
				arrayContents, "array", null, arrayProperties);
		ausgabeArray = lang.newIntArray(new Coordinates(50, 200), new int[n],
				"array", null, arrayProperties);
		hashFunktionText = lang.newText(new Offset(0, 100, ausgabeArray,
				AnimalScript.DIRECTION_SW), "", "", null);
		hashErgebnisText = lang.newText(new Offset(10, 5, hashFunktionText,
				AnimalScript.DIRECTION_SW), "", "text", null);
		kollisionsText = lang.newText(new Offset(0, 5, hashErgebnisText,
				AnimalScript.DIRECTION_SW), "", "", null);
		kollisionsFunktionText = lang.newText(new Offset(-10, 20,
				kollisionsText, AnimalScript.DIRECTION_SW), "", "", null);
		kollisionsErgebnisText = lang.newText(new Offset(10, 5,
				kollisionsFunktionText, AnimalScript.DIRECTION_SW), "", "",
				null);

		arrayMarkerProperties = new ArrayMarkerProperties();
		arrayMarkerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.BLACK);
		arrayMarkerProperties.set(AnimationPropertiesKeys.LABEL_PROPERTY, "");

		arrayMarker = lang.newArrayMarker(ausgabeArray, 0, "", null,
				arrayMarkerProperties);
		arrayMarker.hide(defaultTiming);
		showSourceCode();

	}

	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {
	    setupDefaults();
		arrayContents = (int[]) arg1.get("eingabe");
		n = arrayContents.length;
		a = (Integer) arg1.get("a - das Skalar");
		b = (Integer) arg1.get("b - die Konstante");

		//TO DO: arrayProperties.setIsEditable(, false);
		arrayProperties = (ArrayProperties)arg0.getPropertiesByName("eingabe");
//		arrayProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, arg0.get(
//				"eingabe", AnimationPropertiesKeys.COLOR_PROPERTY));
//		arrayProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, arg0.get(
//				"eingabe", AnimationPropertiesKeys.FILL_PROPERTY));
//		arrayProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, arg0.get(
//				"eingabe", AnimationPropertiesKeys.FILLED_PROPERTY));
//		arrayProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
//				arg0.get("eingabe",
//						AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));

		initForGenerate();
		runHashing(arrayContents);
		return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
    return "Hashing mit linearer Sondierung";
	}

	@Override
	public String getAnimationAuthor() {
		return "Johannes Born, Tuba Gözel";
	}

	@Override
	public String getCodeExample() {

		return "public void runHashing(int[]eingabe) {<br />"
				+ "  int n = eingabe.length;<br />"
				+ "  int[] ausgabe = new int[n];<br />"
				+ "  int hashWert;<br />"
				+ "  for (int i = 0; i < n; i++) {<br />"
				+ "    int input = eingabe[i];<br />"
				+ "    if (input != 0) {<br />"
				+ "      hashWert = hashFunktion(input);<br />"
				+ "      if (hashWert != -1) <br />"
				+ "        ausgabe[hashWert] = input;<br />"
				+ "    }<br />"
				+ "  }<br />"
				+ "  return ausgabe;<br />"
				+ "}<br />"
				+ "<br />"
				+ "private int hashFunktion(int input, int[] ausgabe) {<br />"
				+ "  int hashWert = (a * input + b) % n;<br />"
				+ "  if (ausgabe[hashWert] == 0)<br />"
				+ "    return (a * input + b) % n;<br />"
				+ "  else<br />"
				+ "    return kollisionsHashFunktion(input, hashWert, ausgabe);<br />"
				+ "}<br />"
				+ "<br />"
				+ "private int kollisionsHashFunktion(int input, int hashWert, int[] ausgabe) {<br />"
				+ "  int kollisionsHashWert;<br />"
				+ "  for (int i = 0; i &lt n; i++) {<br />"
				+ "    kollisionsHashWert = (hashWert + i) % n;<br />"
				+ "    if (ausgabe[kollisionsHashWert] == 0)<br />"
				+ "      return kollisionsHashWert;<br />" + "  }<br />"
				+ "  return -1;<br />" + "}";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Das Hash-Verfahren wird verwendet, um Datenstrukturen abzulegen, "
				+ "damit sie schneller gespeichert, gefunden und gel&ouml;scht werden "
				+ "k&ouml;nnen. Eines von den mehreren Hash-Verfahren ist das Lineares "
				+ "Hashing. Hier werden die Daten nach einer linearen Funktion gehasht."
				+ "<br /><br />"
				+ "Hashfunktion:    h(x) = (ax+b) mod n <br />"
				+ "Kollisionsfunktion:  h'(x) = (h(x) + i) mod n<br /><br />"
				+ "Eingabeparameter:<br />"
				+ "  - eingabe: x Werte, die in nacheinander in der Hashfunktion aufgerufen werden<br />"
				+ "  - a: das Skalar bei Hashfunktion<br />"
				+ "  - b: die Konstante bei Hashfunktion";
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
	}

	@Override
	public String getName() {
		return "LinearHashingDemo";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
}
