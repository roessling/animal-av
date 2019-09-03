package generators.hashing;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.*;
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

public class Luhn implements Generator {
	private Language lang;
	private Color markerColor;
	private ArrayProperties arrayProps;
	private SourceCodeProperties sourceCodeProps;
	private SourceCodeProperties descriptionProps;
	private int[] digits;

	private Rect hRect;

	private Text sumTitle;
	private Text sum;
	private Text parity;

	private IntArray ia;
	private SourceCode code;
	private ArrayMarker iMarker;

	private TextProperties titleProps;

	private static final Timing defaultTiming = new TicksTiming(50);

	private static final String NUMBER_CORRECT = "Die angegebene Nummer ist gültig.";
	private static final String NUMBER_INVALID = "Die angegebene Nummer ist ungültig.";

	// Uebung 5
	private Text numberOfMultiplications;
	private int multiplications = 0;
	private Text numberOfAdditions;
	private int additions = 0;
	
	public void init() {
		lang = new AnimalScript("Luhn-Algorithmus", "Ralf Thaenert", 800, 600);
	}

	/**
	 * Displays the Source code of the algorithm
	 * 
	 * @param sourceCodeProps
	 */
	private void showSourceCode(SourceCodeProperties sourceCodeProps) {
		code = lang.newSourceCode(new Offset(0, 50, sumTitle,
				AnimalScript.DIRECTION_SW), "code", null, sourceCodeProps);
		code.addCodeLine("public boolean checkLuhn(int[] digits) {", null, 0,
				null);
		code.addCodeLine("int sum=0;", null, 1, null);
		code.addCodeLine("int parity = digits.length % 2;", null, 1, null);
		code.addCodeLine("for (int i = 0; i < digits.length; i++) {", null, 1,
				null);
		code.addCodeLine("if (i % 2 == parity) {", null, 2, null);
		code.addCodeLine("digits[i] *= 2;", null, 3, null);
		code.addCodeLine("}", null, 2, null);
		code.addCodeLine("if (digits[i] > 9) {", null, 2, null);
		code.addCodeLine("digits[i] -= 9;", null, 3, null);
		code.addCodeLine("}", null, 2, null);
		code.addCodeLine("sum += digits[i];", null, 2, null);
		code.addCodeLine("}", null, 1, null);
		code.addCodeLine("return (sum % 10) == 0;", null, 1, null);
		code.addCodeLine("}", null, 0, null);
	}

	/**
	 * Displays the Conclusion and further information about the algorithm
	 */
	private void showConclusion(SourceCodeProperties descriptionProps) {
		Text resTitle = lang.newText(new Offset(0, 30, hRect,
				AnimalScript.DIRECTION_SW), "Fazit", "descHeader", null,
				titleProps);
		SourceCode resText = lang.newSourceCode(new Offset(0, 00, resTitle,
				AnimalScript.DIRECTION_SW), "resText", null, descriptionProps);
		resText.addCodeLine(
				"Das Verfahren aus der Identifikationstechnik ist leicht zu implementieren und reicht für den",
				null, 0, null);
		resText.addCodeLine(
				"gegebenen Anwendungsfall (Erkennen von Tipp- bzw Schreibfehlern) meist aus.",
				null, 0, null);
		resText.addCodeLine("", null, 0, null);
		resText.addCodeLine(
				"Durch das Verdoppeln jeder zweiten Ziffer werden nebeneinanderliegende Ziffern unterschiedlich",
				null, 0, null);
		resText.addCodeLine(
				"gewertet. Das führt dazu, dass ein Vertauschen zweier Ziffern die Summe des Algorithmus verändert. ",
				null, 0, null);
		resText.addCodeLine(
				"Dadurch wird in den meisten Fällen der Luhn-Test fehlschlagen, wenn zwei Ziffern vertauscht werden.",
				null, 0, null);
		resText.addCodeLine("", null, 0, null);
		resText.addCodeLine(
				"Es werden allerdings nicht alle Fehler beim Vertauschen zweier Ziffern erkannt.",
				null, 0, null);
		resText.addCodeLine(
				"Zum Beispiel wird ein Vertauschen der Ziffern 0 und 9 nicht erkannt.",
				null, 0, null);
		
		
		resText.addCodeLine("", null, 0, null);
		resText.addCodeLine(
				"Es wird nur eine zusätzliche Ziffer angehängt, um eine Nummer so zu verändern, ",
				null, 0, null);
		resText.addCodeLine(
				"dass sie den Luhn-Test besteht. Die zusätzliche Ziffer berechnet sich wie folgt:",
				null, 0, null);
		resText.addCodeLine("1. Berechne die Summe aller Ziffern", null, 1,
				null);
		resText.addCodeLine("2. Multipliziere die Summe mit 9", null, 1, null);
		resText.addCodeLine(
				"3. Hänge die letzte Ziffer dieser Summe an die ursprüngliche Nummer an",
				null, 1, null);
		resText.addCodeLine("", null, 0, null);
		resText.addCodeLine("Komplexität des Algorithmus", null, 0, null);
		resText.addCodeLine("Da der Algorithmus u.a. auch mechanisch implementiert werden sollte ist", null, 0, null);
		resText.addCodeLine("er simpel gestaltet. Der Luhn-Algorithmus liegt in O(n), die Anzahl der ", null, 0, null);
		resText.addCodeLine("Operationen wächst also linear, wie die Animation zeigt.", null, 0, null);
		resText.addCodeLine("", null, 0, null);
		resText.addCodeLine(
				"Weitere Informationen: http://de.wikipedia.org/wiki/Luhn-Algorithmus",
				null, 0, null);
	}

	/**
	 * Performs the Luhn-Algorithm
	 * 
	 * @param digits
	 *            the number to check
	 * @param code
	 *            instance of the code to highlight
	 * @return true if the number is correct
	 */
	public boolean checkLuhn(IntArray digits, SourceCode code) {
		int sum = 0;
		this.sum.setText(String.valueOf(sum), null, null);
		code.toggleHighlight(0, 1);
		lang.nextStep("Initialisierung");

		int parity = digits.getLength() % 2;
		this.parity.setText(String.valueOf(parity), null, null);
		code.toggleHighlight(1, 2);
		lang.nextStep();

		code.unhighlight(2);
		for (int i = 0; i < digits.getLength(); i++) {
			code.highlight(3);
			iMarker.move(i, null, defaultTiming);
			if(i==0) {
				lang.nextStep("Iteration");
			} else {
				lang.nextStep();
			}
			code.toggleHighlight(3, 4);
			lang.nextStep();

			code.unhighlight(4);
			if (i % 2 == parity) {
				digits.put(i, digits.getData(i) * 2, null, defaultTiming);
				incrementMult();
				code.highlight(5);
				lang.nextStep();
				code.unhighlight(5);
			}
			code.highlight(7);
			lang.nextStep();
			code.unhighlight(7);
			if (digits.getData(i) > 9) {
				digits.put(i, digits.getData(i) - 9, null, defaultTiming);
				code.highlight(8);
				lang.nextStep();
				code.unhighlight(8);
			}
			sum += digits.getData(i);
			incrementAdd();
			code.highlight(10);
			this.sum.setText(String.valueOf(sum), null, null);
			lang.nextStep();
			code.unhighlight(10);
		}
		code.toggleHighlight(11, 12);
		lang.nextStep();
		code.unhighlight(12);
		return (sum % 10) == 0;
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		markerColor = (Color) primitives.get("markerColor");
		arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
		sourceCodeProps = (SourceCodeProperties) props
				.getPropertiesByName("sourceCodeProps");
		descriptionProps = (SourceCodeProperties) props
				.getPropertiesByName("descriptionProps");
		digits = (int[]) primitives.get("digits");
		lang.setStepMode(true);

		// Title
		TextProperties headerTitleProps = new TextProperties();
		headerTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", 0, 24));
		Text title = lang.newText(new Coordinates(20, 30), "Luhn-Algorithmus",
				"headerTitle", null, headerTitleProps);
		RectProperties rectProperties = new RectProperties();
		rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		hRect = lang.newRect(new Offset(-5, -5, title,
				AnimalScript.DIRECTION_NW), new Offset(5, 5, title,
				AnimalScript.DIRECTION_SE), "titleRect", null, rectProperties);

		// Description Title
		titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 16));
		Text descTitle = lang.newText(new Offset(0, 20, hRect,
				AnimalScript.DIRECTION_SW), "Beschreibung des Algorithmus",
				"descHeader", null, titleProps);

		// Description
		SourceCode desc = lang.newSourceCode(new Offset(0, 10, descTitle,
				AnimalScript.DIRECTION_SW), "description", null,
				descriptionProps);
		desc.addCodeLine(
				"Der Luhn-Algorithmus (auch Modulo 10 Algorithmus) ist ein einfaches Verfahren um Identifikationsnummern",
				null, 0, null);
		desc.addCodeLine(
				"wie zum Beispiel Kredtikarten- oder Sozialversicherungsnummern auf ihre Gültigkeit zu prüfen.",
				null, 0, null);
		desc.addCodeLine("", null, 0, null);
		desc.addCodeLine(
				"Der Algorithmus wird verwendet, um häufige Tippfehler bei Identifikationsnummern (z.B. eine Ziffer wurde falsch ",
				null, 0, null);
		desc.addCodeLine(
				"geschrieben oder zwei Ziffern wurden vertauscht) schnell zu erkennen.",
				null, 0, null);
		desc.addCodeLine("", null, 0, null);

		// Values and Array
		TextProperties valueProps = new TextProperties();
		valueProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", 0, 14));
		Text digitsTitle = lang.newText(new Offset(0, 50, desc,
				AnimalScript.DIRECTION_SW), "digits", "id", null, valueProps);
		
		// Number of Multiplications / Additions
		numberOfAdditions = lang.newText(new Offset(300, 0, digitsTitle,
				AnimalScript.DIRECTION_NE), "# Additions: 0", "id", null, valueProps);
		numberOfMultiplications = lang.newText(new Offset(0, 10, numberOfAdditions,
				AnimalScript.DIRECTION_SW), "# Multiplications: 0", "id", null, valueProps);
		
		sumTitle = lang.newText(new Offset(0, 10, digitsTitle,
				AnimalScript.DIRECTION_SW), "sum", "sum", null, valueProps);
		lang.newText(new Offset(0, 10, sumTitle, AnimalScript.DIRECTION_SW),
				"parity", "parity", null, valueProps);
		ia = lang.newIntArray(new Offset(10, 0, digitsTitle,
				AnimalScript.DIRECTION_NE), digits, "", null, arrayProps);
		ArrayMarkerProperties ami = new ArrayMarkerProperties();
		ami.set(AnimationPropertiesKeys.COLOR_PROPERTY, markerColor);
		ami.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		iMarker = lang.newArrayMarker(ia, 0, "i", null, ami);
		sum = lang.newText(new Offset(0, 5, ia, AnimalScript.DIRECTION_SW), "",
				"sum", null, valueProps);
		parity = lang.newText(
				new Offset(0, 10, sum, AnimalScript.DIRECTION_SW), "",
				"parity", null, valueProps);
		showSourceCode(sourceCodeProps);
		lang.nextStep("Einleitung");
		code.highlight(0);
		lang.nextStep();
		boolean res = false;
		try {
			res = checkLuhn(ia, code);
			lang.nextStep();
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}
		lang.newText(new Offset(0, 20, code, AnimalScript.DIRECTION_SW),
				(res ? NUMBER_CORRECT : NUMBER_INVALID), "resultText", null,
				titleProps);
		lang.nextStep("Ergebnis");
		lang.hideAllPrimitives();
		title.show();
		hRect.show();
		showConclusion(descriptionProps);
		lang.nextStep("Fazit");
		return lang.toString();
	}
	
	private void incrementAdd() {
		additions++;
		numberOfAdditions.setText("# Additions: " + 
				additions, defaultTiming, null);
	}
	
	private void incrementMult() {
		multiplications++;
		numberOfMultiplications.setText("# Multiplications: " + 
				multiplications, defaultTiming, null);
	}

	public String getName() {
		return "Luhn-Algorithmus";
	}

	public String getAlgorithmName() {
		return "Luhn-Algorithmus";
	}

	public String getAnimationAuthor() {
		return "Ralf Thaenert";
	}

	/**
	 * Returns the Description for the Content Generator Window
	 */
	public String getDescription() {
		return "Der Luhn-Algorithmus ist eine einfache und weit verbreitete Methode, um Mitgliedsnummern "
				+ "\n"
				+ "(wie zum Beispiel Kreditkartennummern) auf Korrektheit zu prüfen und einfache Schreib/Tippfehler "
				+ "\n"
				+ "zu erkennen."
				+ "\n"
				+ "\n"
				+ "Luhn Nummern werden erzeugt, indem eine zusätzliche Prüfziffer an eine Zahl gehängt wird. "
				+ "\n"
				+ "\n"
				+ "Der Algorithmus gewichtet die einzelnen Ziffern unterschiedlich, dadurch führt ein Vertauschen "
				+ "\n"
				+ "zweier Ziffern in den meisten Fällen zu einem anderen Ergebnis.";
	}

	/**
	 * Returns the Code example for the Content Generator Window
	 */
	public String getCodeExample() {
		return "public boolean checkLuhn(int[] digits) {" + "\n"
				+ "	int sum = 0;" + "\n" + "	int parity = digits.length % 2;"
				+ "\n" + "	for(int i=0; i < digits.length; i++) {" + "\n"
				+ "		if(i % 2 == parity) {" + "\n" + "			digits[i] *= 2;"
				+ "\n" + "		}" + "\n" + "		if(digits[i] > 9) {" + "\n"
				+ "			digits[i] -= 9;" + "\n" + "		}" + "\n"
				+ "		sum += digits[i];" + "\n" + "	}" + "\n"
				+ "	return (sum % 10) == 0;" + "\n" + "}";
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
}