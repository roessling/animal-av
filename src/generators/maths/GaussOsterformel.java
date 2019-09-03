/*
 * Gauss.java
 * Tristan Gahler, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
//import animal.main.Animal;

/**
 * 
 * @author Tristan Gahler
 * @version 1.0 16-05-2018
 */

public class GaussOsterformel implements ValidatingGenerator {
	private Language lang;
	private String calender_type;
	private Color color;
	private int year;

	public GaussOsterformel(Language l) {
		// Store the language object
		lang = l;
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divided by a call of lang.nextStep();
		lang.setStepMode(true);
	}

	public GaussOsterformel() {
		//lang = new AnimalScript("Gausssche Osterformel", "Tristan Gahler", 1000, 600);
	}

	private static final String DESCRIPTION = "Die Gausssche Osterformel bestimmt den genauen Tag"
			+ "an dem Ostern im angegebenen Jahr statt findet."
			+ "Die Berechnung fuer den julianischen, beziehungsweise gregorianischen Kalender,"
			+ "unterscheiden sich dabei geringfuegig. Die hier gezeigte Fassung von, Carl Friedrich Gauss"
			+ "aus dem Jahr 1816, beruecksichtigt die Mondgleichung.";

	private static final String SOURCE_CODE = "a = Jahr mod 19" + "\n b = Jahr mod 4" + "\n c = Jahr mod 7"
			+ "\n k = Jahr / 100" + "\n M = 15" + "\n d = (19a + M) mod 30" + "\n N = 6"
			+ "\n e = (2b + 4c + 6d + N) mod 7";

	public final static Timing defaultDuration = new TicksTiming(100);

	// generated

	public void init() {
		lang = new AnimalScript("Gausssche Osterformel", "Tristan Gahler", 1000, 600);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		calender_type = (String) primitives.get("calender_type");
		color = (Color) primitives.get("codeHighlightColor");
		year = (Integer) primitives.get("year");
		lang.setStepMode(true);
		display(year);
		// gibt Animal Code auf Konsole aus
		// System.out.println(lang);
		return lang.toString();
	}

	public String getName() {
		return "Gausssche Osterformel";
	}

	public String getAlgorithmName() {
		return "Gausssche Osterformel (Gauss, 1816)";
	}

	public String getAnimationAuthor() {
		return "Tristan Gahler";
	}

	public String getDescription() {
		return "Die Gausssche Osterformel von Carl Fridrich Gauss erlaubt die genaue Berechnung des Osterdatums (Ostersonntag) fuer ein gegebenes Jahr."
				+ "\n"
				+ "Der selbe Gleichungssatz gilt sowohl fuer den Julianischen Kalender, als auch fuer den Gregorianischen. Lediglich zwei variable Zwischengroe�en muessen fuer den Julianischen Kalender in Konstanten umgewandelt werden."
				+ "\n"
				+ "In seltenen Faellen ist der 26. April das Ergebnis. Die Kirche legte jedoch den 25. April als letzten Tag fuer Ostersonntag fest. Dies arbeitete Gau� nicht mit ein. In diesen Faellen ist Ostern am 19. April.";
	}

	public String getCodeExample() {
		return "Berechnung fuer den Gregorianischen Kalender:" + "\n" + "\n" + "a = Jahr mod 19" + "\n"
				+ "b = Jahr mod 4" + "\n" + "c = Jahr mod 7" + "\n" + "k = Jahr div 100" + "\n" + "p = (8k + 13) div 25"
				+ "\n" + "q = k div 4" + "\n" + "M = (15 + k - p - q) mod 30" + "\n" + "d = (19a + M) mod 30" + "\n"
				+ "N = (4 + k - q) mod 7" + "\n" + "e = (2b + 4c +6d +N) mod 7" + "\n" + "\n"
				+ "Ostern = (22 + d + e)ter Maerz" + "\n" + "(32. Maerz ist der 1. April usw.)" + "\n" + "\n" + "\n"
				+ "Berechnung f�r den Julianischen Kalender:" + "\n" + "\n" + "a = Jahr mod 19" + "\n"
				+ "b = Jahr mod 4" + "\n" + "c = Jahr mod 7" + "\n" + "k = Jahr div 100" + "\n" + "M = 15"
				+ "d = (19a + M) mod 30" + "\n" + "N = 6" + "e = (2b + 4c +6d +N) mod 7" + "\n" + "\n"
				+ "Ostern = (22 + d + e)ter Maerz" + "\n" + "(32. Maerz ist der 1. April usw.)";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {

		if (arg1.contains("gregorianisch") || arg1.contains("julianisch")) {
			return true;
		} else {
			throw new IllegalArgumentException("calender_type must be gregorianisch or julianisch");
		}
	}
	// end generated

	public void display(int a) {

		// header itself
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		lang.newText(new Coordinates(70, 30), "Gausssche Osterformel", "header", null, headerProps);

		// rectangle around the header
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
				null, rectProps);

		// introduction text, disappears later on
		TextProperties introductionProps = new TextProperties();
		introductionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		lang.nextStep("Intro");
		Text introduction1 = lang.newText(new Coordinates(70, 90),
				"Die Gausssche Osterformel bestimmt den genauen Tag an dem Ostern im angegebenen Jahr statt findet.",
				"introduction", null, introductionProps);
		Text introduction2 = lang.newText(new Coordinates(70, 110),
				"Die Berechnung fuer den julianischen, beziehungsweise gregorianischen Kalender, unterscheidet sich dabei geringfuegig.",
				"introduction2", null, introductionProps);
		Text introduction3 = lang.newText(new Coordinates(70, 130),
				"Die hier gezeigte Fassung, von Carl Friedrich Gauss aus dem Jahr 1816, beruecksichtigt die Mondgleichung.",
				"introduction3", null, introductionProps);
		Text introduction4 = lang.newText(new Coordinates(70, 170),
				"Nachfolgend zeigen wir die Berechnung des Ostersonntags fuer das Jahr " + year + " (" + calender_type
						+ ")",
				"introduction4", null, introductionProps);

		lang.nextStep();
		introduction1.hide();
		introduction2.hide();
		introduction3.hide();
		introduction4.hide();

		// secondHeader properties
		TextProperties secondHeader = new TextProperties();
		TextProperties thirdHeader = new TextProperties();
		secondHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
		thirdHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		if (calender_type.equals("gregorianisch")) {
			lang.newText(new Coordinates(70, 130), "Gregorianischer Kalender", "seoncdHeader", null, secondHeader);
			lang.newText(new Coordinates(70, 155), "(fuer das Jahr " + a + ")", "thirdHeader", null, thirdHeader);
		} else {
			lang.newText(new Coordinates(70, 130), "Julianischer Kalender", "seoncdHeader", null, secondHeader);
			lang.newText(new Coordinates(70, 155), "(fuer das Jahr " + a + ")", "thirdHeader", null, thirdHeader);
		}

		// following are the properties for the source code
		SourceCodeProperties gregProps = new SourceCodeProperties();
		gregProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, color);
		gregProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 16));
		gregProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		gregProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode greg = lang.newSourceCode(new Coordinates(70, 140), "sourceCode", null, gregProps);

		// source code on screen for greg
		if (calender_type.equals("gregorianisch")) {
			greg.addCodeLine("", null, 0, null);
			greg.addCodeLine("", null, 0, null);
			greg.addCodeLine("a = Jahr mod 19", null, 0, null);
			greg.addCodeLine("b = Jahr mod 4", null, 0, null);
			greg.addCodeLine("c = Jahr mod 7", null, 0, null);
			greg.addCodeLine("k = Jahr / 100", null, 0, null);
			greg.addCodeLine("p = (8k / 13) / 25", null, 0, null);
			greg.addCodeLine("q = k / 4", null, 0, null);
			greg.addCodeLine("M = (15 + k - p - q) mod 30", null, 0, null);
			greg.addCodeLine("d = (19a + M) mod 30", null, 0, null);
			greg.addCodeLine("N = (4 + k - q) mod 7", null, 0, null);
			greg.addCodeLine("e = (2b + 4c + 6d + N) mod 7)", null, 0, null);
			greg.addCodeLine("Ostern = (22 + d + e)ter Maerz", null, 0, null);
			lang.nextStep("Rechnung");

			// displays the result
			int ostertag = (calculate(a, null, greg));
			lang.nextStep("Fazit");
			String gMonat = ostertag > 31 ? "April" : "Maerz";
			int gTag = ostertag > 31 ? ostertag - 31 : ostertag;

			if (gTag == 26 && gMonat.equals("April")) {
				gTag = 19;
				greg.addCodeLine(
						"Vorsicht Sonderfall! F�llt Ostern auf den 26. April findet es statt dessen am 19. April statt",
						null, 0, null);
				greg.highlight(15);
			}

			// displays the conclusion
			TextProperties conclusionProps = new TextProperties();
			conclusionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
			lang.newText(new Coordinates(70, 510),
					"Nach dem gregorianischem Kalender ist Ostern im Jahr " + a + " am " + gTag + "ten " + gMonat,
					"conclusion", null, conclusionProps);

		}

		// following are the properties for the source code (jul)
		SourceCodeProperties julProps = new SourceCodeProperties();
		julProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, color);
		julProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 16));
		julProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		julProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode jul = lang.newSourceCode(new Coordinates(70, 140), "sourceCode", null, julProps);

		// source code on screen for jul
		if (calender_type.equals("julianisch")) {
			jul.addCodeLine("", null, 0, null);
			jul.addCodeLine("", null, 0, null);
			jul.addCodeLine("a = Jahr mod 19", null, 0, null);
			jul.addCodeLine("b = Jahr mod 4", null, 0, null);
			jul.addCodeLine("c = Jahr mod 7", null, 0, null);
			jul.addCodeLine("M = 15", null, 0, null);
			jul.addCodeLine("d = (19a + M) mod 30", null, 0, null);
			jul.addCodeLine("N = 6", null, 0, null);
			jul.addCodeLine("e = (2b + 4c + 6d + N) mod 7)", null, 0, null);
			jul.addCodeLine("Ostern = (22 + d + e)ter Maerz", null, 0, null);
			lang.nextStep("Rechnung");

			// displays the result
			int ostertag = (calculate(a, jul, null));
			lang.nextStep("Fazit");
			String monat = ostertag > 31 ? "April" : "Maerz";
			int tag = ostertag > 31 ? ostertag - 31 : ostertag;

			if (tag == 26 && monat.equals("April")) {
				tag = 19;
				greg.addCodeLine(
						"Vorsicht Sonderfall! F�llt Ostern auf den 26. April findet es statt dessen am 19. April statt",
						null, 0, null);
				greg.highlight(12);
			}

			// displays the conclusion
			TextProperties conclusionProps = new TextProperties();
			conclusionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
			lang.newText(new Coordinates(70, 450),
					"Nach dem julianischen Kalender ist Ostern im Jahr " + a + " am " + tag + "ten " + monat,
					"conclusion", null, conclusionProps);

		}

	}

	private int calculate(int jahr, SourceCode julSupport, SourceCode gregSupport) {
		Variables vars = lang.newVariables();

		SourceCodeProperties jProps = new SourceCodeProperties();
		jProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, color);
		jProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 16));
		jProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		jProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode jDisplay = lang.newSourceCode(new Coordinates(405, 120), "sourceCode", null, jProps);

		SourceCodeProperties gProps = new SourceCodeProperties();
		gProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, color);
		gProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 16));
		gProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		gProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode gDisplay = lang.newSourceCode(new Coordinates(405, 120), "sourceCode", null, gProps);

		SourceCodeProperties calcProps = new SourceCodeProperties();
		calcProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, color);
		calcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 16));
		calcProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		calcProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode calc = lang.newSourceCode(new Coordinates(550, 120), "sourceCode", null, calcProps);

		jDisplay.addCodeLine("", null, 0, null);
		jDisplay.addCodeLine("", null, 0, null);
		jDisplay.addCodeLine("", null, 0, null);

		gDisplay.addCodeLine("", null, 0, null);
		gDisplay.addCodeLine("", null, 0, null);
		gDisplay.addCodeLine("", null, 0, null);

		calc.addCodeLine("", null, 0, null);
		calc.addCodeLine("", null, 0, null);
		calc.addCodeLine("", null, 0, null);

		// starts at 3 because of the header and 2 blank lines
		// i,j for jul, x and y for greg Calendar
		int i = 2, x = 2;
		int j = 2, y = 2;

		if (calender_type.equals("julianisch")) {
			julSupport.highlight(i++);
			lang.nextStep();
			calc.addCodeLine("(" + jahr + " % " + 19 + ")", null, 0, null);
			jDisplay.addCodeLine("a = " + (jahr % 19), null, 0, null);
			int a = jahr % 19;
			String aS = Integer.toString(a);
			vars.declare("String", "a", aS);
			lang.nextStep();

			julSupport.unhighlight(j++);
			julSupport.highlight(i++);
			int b = jahr % 4;
			lang.nextStep();
			calc.addCodeLine("(" + jahr + " % " + 4 + ")", null, 0, null);
			jDisplay.addCodeLine("b = " + (jahr % 4), null, 0, null);
			String bS = Integer.toString(b);
			vars.declare("String", "b", bS);
			lang.nextStep();

			julSupport.unhighlight(j++);
			julSupport.highlight(i++);
			int c = jahr % 7;
			lang.nextStep();
			calc.addCodeLine("(" + jahr + " % " + 7 + ")", null, 0, null);
			jDisplay.addCodeLine("c = " + (jahr % 7), null, 0, null);
			String cS = Integer.toString(c);
			vars.declare("String", "c", cS);
			lang.nextStep();

			julSupport.unhighlight(j++);
			int k = jahr / 100;
			String kS = Integer.toString(k);
			vars.declare("String", "k", kS);

			julSupport.highlight(i++);
			int m = 15;
			lang.nextStep();
			calc.addCodeLine("(Konstanter Wert)", null, 0, null);
			jDisplay.addCodeLine("M = 15", null, 0, null);
			String mS = Integer.toString(m);
			vars.declare("String", "M", mS);
			lang.nextStep();

			julSupport.unhighlight(j++);
			julSupport.highlight(i++);
			int d = (19 * a + m) % 30;
			jDisplay.highlight(3, 0, true);
			jDisplay.highlight(6, 0, true);
			;
			lang.nextStep();
			calc.addCodeLine("(" + 19 + " * " + a + " + " + 15 + ")" + " % " + 30 + ")", null, 0, null);
			jDisplay.addCodeLine("d = " + (19 * a + 15) % 30, null, 0, null);
			String dS = Integer.toString(d);
			vars.declare("String", "d", dS);
			lang.nextStep();

			jDisplay.unhighlight(3);
			jDisplay.unhighlight(6);
			julSupport.unhighlight(j++);
			julSupport.highlight(i++);
			int N = 6;
			lang.nextStep();
			calc.addCodeLine("(Konstanter Wert)", null, 0, null);
			jDisplay.addCodeLine("N = 6", null, 0, null);
			String nS = Integer.toString(N);
			vars.declare("String", "N", nS);
			lang.nextStep();

			julSupport.unhighlight(j++);
			julSupport.highlight(i++);
			jDisplay.highlight(4, 0, true);
			jDisplay.highlight(5, 0, true);
			jDisplay.highlight(7, 0, true);
			jDisplay.highlight(8, 0, true);
			int e = (2 * b + 4 * c + 6 * d + N) % 7;
			lang.nextStep();
			calc.addCodeLine(
					"(" + "(" + 2 + " * " + b + " + " + 4 + " * " + c + " + " + "6" + " * " + d + " + " + N + ")" + ")",
					null, 0, null);
			jDisplay.addCodeLine("e = " + (2 * b + 4 * c + 6 * d + N) % 7, null, 0, null);
			String eS = Integer.toString(e);
			vars.declare("String", "e", eS);
			lang.nextStep();

			julSupport.unhighlight(j++);
			julSupport.highlight(i++);
			;
			jDisplay.unhighlight(4);
			jDisplay.unhighlight(5);
			jDisplay.unhighlight(7);
			jDisplay.unhighlight(8);
			jDisplay.highlight(7, 0, true);
			jDisplay.highlight(9, 0, true);
			int ostern = 22 + d + e;
			lang.nextStep();
			calc.addCodeLine("(22 + " + d + " + " + e + ")", null, 0, null);
			jDisplay.addCodeLine("O = " + (22 + d + e), null, 0, null);
			String oS = Integer.toString(ostern);
			vars.declare("String", "Ostern", oS);
			lang.nextStep();

			int result = ostern;
			julSupport.unhighlight(j++);
			jDisplay.unhighlight(7, 0, true);
			jDisplay.unhighlight(9, 0, true);
			lang.nextStep();

			if (ostern >= 32) {
				julSupport.addCodeLine("(32. Maerz = 1. April usw.)", null, 0, null);
				calc.addCodeLine("(" + ostern + " - " + 31 + ")", null, 0, null);
				jDisplay.addCodeLine(ostern - 31 + ". " + "April", null, 0, null);
				julSupport.highlight(j++);
				jDisplay.highlight(11, 0, true);
				lang.nextStep();
			} else {
				jDisplay.addCodeLine(ostern + ". " + "Maerz", null, 0, null);
			}
			return result;
		}

		else {
			gregSupport.highlight(x++);
			lang.nextStep();
			calc.addCodeLine("(" + jahr + " % " + 19 + ")", null, 0, null);
			gDisplay.addCodeLine("a = " + (jahr % 19), null, 0, null);
			int a = jahr % 19;
			String aS = Integer.toString(a);
			vars.declare("String", "a", aS);
			lang.nextStep();

			gregSupport.highlight(x++);
			gregSupport.unhighlight(y++);
			int b = jahr % 4;
			lang.nextStep();
			calc.addCodeLine("(" + jahr + " % " + 4 + ")", null, 0, null);
			gDisplay.addCodeLine("b = " + (jahr % 4), null, 0, null);
			String bS = Integer.toString(b);
			vars.declare("String", "b", bS);
			lang.nextStep();

			gregSupport.highlight(x++);
			gregSupport.unhighlight(y++);
			int c = jahr % 7;
			lang.nextStep();
			calc.addCodeLine("(" + jahr + " % " + 7 + ")", null, 0, null);
			gDisplay.addCodeLine("c = " + (jahr % 7), null, 0, null);
			String cS = Integer.toString(c);
			vars.declare("String", "c", cS);
			lang.nextStep();

			gregSupport.highlight(x++);
			gregSupport.unhighlight(y++);
			int k = jahr / 100;
			lang.nextStep();
			calc.addCodeLine("(" + jahr + " / " + 100 + ")", null, 0, null);
			gDisplay.addCodeLine("k = " + (jahr / 100), null, 0, null);
			String kS = Integer.toString(k);
			vars.declare("String", "k", kS);
			lang.nextStep();

			gregSupport.highlight(x++);
			gregSupport.unhighlight(y++);
			int p = (8 * k + 13) / 25;
			gDisplay.highlight(6, 0, true);
			lang.nextStep();
			calc.addCodeLine("(" + "(" + 8 + " * " + k + " + " + 13 + ") " + "/" + " 25" + ")", null, 0, null);
			gDisplay.addCodeLine("p = " + (8 * k + 13) / 25, null, 0, null);
			String pS = Integer.toString(p);
			vars.declare("String", "p", pS);
			lang.nextStep();

			gregSupport.highlight(x++);
			gregSupport.unhighlight(y++);
			int q = k / 4;
			lang.nextStep();
			calc.addCodeLine("(" + k + " / " + 4 + ")", null, 0, null);
			gDisplay.addCodeLine("q = " + k / 4, null, 0, null);
			String qS = Integer.toString(q);
			vars.declare("String", "q", qS);
			lang.nextStep();

			gregSupport.highlight(x++);
			gregSupport.unhighlight(y++);
			gDisplay.highlight(7, 0, true);
			gDisplay.highlight(8, 0, true);
			int mg = (15 + k - p - q) % 30;
			lang.nextStep();
			calc.addCodeLine("(" + "(" + 15 + " + " + k + " - " + p + " - " + q + ")" + "%" + 30 + ")", null, 0, null);
			gDisplay.addCodeLine("M = " + (15 + k - p - q) % 30, null, 0, null);
			String mS = Integer.toString(mg);
			vars.declare("String", "M", mS);
			lang.nextStep();

			gregSupport.highlight(x++);
			gregSupport.unhighlight(y++);
			gDisplay.unhighlight(6, 0, true);
			gDisplay.unhighlight(7, 0, true);
			gDisplay.unhighlight(8, 0, true);
			gDisplay.unhighlight(6);
			gDisplay.highlight(9, 0, true);
			gDisplay.highlight(3, 0, true);
			int dG = (19 * a + mg) % 30;
			lang.nextStep();
			calc.addCodeLine("(" + 19 + " * " + a + " + " + mg + ")", null, 0, null);
			gDisplay.addCodeLine("d = " + (19 * a + 15) % 30, null, 0, null);
			String dS = Integer.toString(dG);
			vars.declare("String", "d", dS);
			lang.nextStep();

			gregSupport.highlight(x++);
			gregSupport.unhighlight(y++);
			gDisplay.unhighlight(9, 0, true);
			gDisplay.unhighlight(3, 0, true);
			int NG = (4 + k - q) % 7;
			gDisplay.highlight(6, 0, true);
			gDisplay.highlight(8, 0, true);
			lang.nextStep();
			calc.addCodeLine("(" + "(" + 4 + " + " + k + " - " + q + ") " + "%" + " 7" + ")", null, 0, null);
			gDisplay.addCodeLine("N = " + (4 + k - q) % 7, null, 0, null);
			String nS = Integer.toString(NG);
			vars.declare("String", "N", nS);
			lang.nextStep();

			gregSupport.highlight(x++);
			gregSupport.unhighlight(y++);
			gDisplay.unhighlight(6, 0, true);
			gDisplay.unhighlight(8, 0, true);
			int eG = (2 * b + 4 * c + 6 * dG + NG) % 7;
			gDisplay.highlight(4, 0, true);
			gDisplay.highlight(5, 0, true);
			gDisplay.highlight(10, 0, true);
			gDisplay.highlight(11, 0, true);
			lang.nextStep();
			calc.addCodeLine("(" + "(" + 2 + " * " + b + " + " + 4 + " * " + c + " + " + "6" + " * " + dG + " + " + NG
					+ ")" + ")", null, 0, null);
			gDisplay.addCodeLine("e = " + (2 * b + 4 * c + 6 * dG + NG) % 7, null, 0, null);
			String eS = Integer.toString(eG);
			vars.declare("String", "e", eS);
			lang.nextStep();

			int gOstern = 22 + dG + eG;
			gregSupport.highlight(x++);
			gregSupport.unhighlight(y++);
			gDisplay.unhighlight(4, 0, true);
			gDisplay.unhighlight(5, 0, true);
			gDisplay.unhighlight(10, 0, true);
			gDisplay.unhighlight(11, 0, true);
			gDisplay.highlight(10, 0, true);
			gDisplay.highlight(12, 0, true);
			lang.nextStep();
			int os = (22 + dG + eG);
			calc.addCodeLine("(22 + " + dG + " + " + eG + ")", null, 0, null);
			gDisplay.addCodeLine("Ostern = " + (22 + dG + eG), null, 0, null);
			String oSt = Integer.toString(os);
			vars.declare("String", "Ostern", oSt);
			lang.nextStep();

			gDisplay.unhighlight(10, 0, true);
			gDisplay.unhighlight(12, 0, true);
			gregSupport.unhighlight(y++);
			if (gOstern >= 32) {
				gregSupport.addCodeLine("(32. Maerz = 1. April usw.)", null, 0, null);
				calc.addCodeLine("(" + os + " - " + 31 + ")", null, 0, null);
				gDisplay.addCodeLine(os - 31 + ". " + "April", null, 0, null);
				gregSupport.highlight(x++);
				gDisplay.highlight(14, 0, true);
				lang.nextStep();
			} else {
				gDisplay.addCodeLine(os + ". " + "Maerz", null, 0, null);
			}

			int result = gOstern;
			return result;
		}

	}

	protected String getAlgorithmDescription() {
		return DESCRIPTION;
	}

	protected String getAlgorithmCode() {
		return SOURCE_CODE;
	}

	//public static void main(String[] args) {
	//	Generator generator = new GaussOsterformel();
	//	Animal.startGeneratorWindow(generator);
	//}

}
