/*
 * SpencersOsterformel.java
 * Frank Nelles, Patrick Wienhold, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;

import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import animal.main.Animal;

public class SpencersOsterformel implements Generator {
	private Language lang;
	private Locale LANG = Locale.GERMANY;
	Translator t = new Translator("resources/SpencersOsterformel", LANG);
	private int Jahr;
	private TextProperties Titel;
	private TextProperties Beschreibungen;
	private SourceCodeProperties Quellcode;

	
	/* For testing purposes only */
	public static void main(String[] args) {
		Generator generator = new SpencersOsterformel(Locale.US); // Generator erzeugen
		Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}
	
	public SpencersOsterformel(Locale l) {
		t.setTranslatorLocale(l);
	}

	public void init() {
		lang = new AnimalScript("Spencers Osterformel", "Frank Nelles, Patrick Wienhold", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		Jahr = (Integer) primitives.get("Jahr");
		Titel = (TextProperties) props.getPropertiesByName("Titel");
		Beschreibungen = (TextProperties) props.getPropertiesByName("Beschreibungen");
		Quellcode = (SourceCodeProperties) props.getPropertiesByName("Quellcode");

		calculateEasterDate();

		return lang.toString();
	}

	public String getName() {
		return t.translateMessage("title");
	}

	public String getAlgorithmName() {
		return t.translateMessage("title");
	}

	public String getAnimationAuthor() {
		return "Frank Nelles, Patrick Wienhold";
	}

	public String getDescription() {
		return t.translateMessage("desc");
	}

	public String getCodeExample() {
		return "a = " + t.translateMessage("year") + " mod 19" + "\n" + "b = " + t.translateMessage("year") + " div 100"
				+ "\n" + "c = " + t.translateMessage("year") + " mod 100" + "\n" + "d = b div 4" + "\n"
				+ "e = b mod 4" + "\n" + "f = (b + 8) div 25" + "\n" + "g = (b - f + 1) div 3" + "\n"
				+ "h = (19a + b - d - g + 15) mod 30" + "\n" + "i = c div 4" + "\n" + "k = c mod 4" + "\n"
				+ "l = (32 + 2e + 2i - h - k) mod 7" + "\n" + "m = (a + 11h + 22l) div 451" + "\n"
				+ "n = (h + l - 7m + 114) div 31" + "\n" + "p = (h + l - 7m + 114) mod 31";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return LANG;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	/**
	 * Calculate the easter date
	 */
	private void calculateEasterDate() {
		/* Set font size properties */
		Titel.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font)Titel.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont((float) 24.0));
		Beschreibungen.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font)Beschreibungen.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont((float) 18.0));
		Quellcode.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font)Quellcode.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont((float) 16.0));
		
		// Values for calculation
		int a, b, c, d, e, f, g, h, i, k, l, m, n, p;

		/* Start of generation */
		// Show title
		Text head = lang.newText(new Coordinates(50, 30), this.getName(), "head", null, Titel);

		lang.nextStep();

		// Show an explanation at the beginning
		Text explanation_1 = lang.newText(new Coordinates(50, 80), t.translateMessage("expl_1"), "explanation_1", null, Beschreibungen);
		Text explanation_2 = lang.newText(new Coordinates(50, 100), t.translateMessage("expl_2"), "explanation_2", null, Beschreibungen);
		Text explanation_3 = lang.newText(new Coordinates(50, 120), t.translateMessage("expl_3"), "explanation_3", null, Beschreibungen);
		Text explanation_4 = lang.newText(new Coordinates(50, 140), t.translateMessage("expl_4"), "explanation_4", null, Beschreibungen);
		Text explanation_5 = lang.newText(new Coordinates(50, 160), t.translateMessage("expl_5"), "explanation_5", null, Beschreibungen);

		lang.nextStep(t.translateMessage("expl"));
		// Hide explanations
		explanation_1.hide();
		explanation_2.hide();
		explanation_3.hide();
		explanation_4.hide();
		explanation_5.hide();

		// Add three source code panels (first shows the actual algorithm, second is the
		// final value of the var and the third are the auxiliary calculations)
		SourceCode srcCode_y = lang.newSourceCode(new Coordinates(50, 60), "sourceCode_y", null, Quellcode);
		SourceCode srcCode_1 = lang.newSourceCode(new Coordinates(50, 80), "sourceCode_1", null, Quellcode);
		SourceCode srcCode_2 = lang.newSourceCode(new Coordinates(420, 80), "sourceCode_2", null, Quellcode);
		SourceCode srcCode_3 = lang.newSourceCode(new Coordinates(550, 80), "sourceCode_3", null, Quellcode);

		// Show the year as source code
		srcCode_y.addCodeLine(t.translateMessage("year") + " = " + Jahr, null, 0, null);

		// Add full sourcecode in left panel
		srcCode_1.addCodeLine("a = " + t.translateMessage("year") + " mod 19", null, 0, null); // 0
		srcCode_1.addCodeLine("b = " + t.translateMessage("year") + " div 100", null, 0, null); // 1
		srcCode_1.addCodeLine("c = " + t.translateMessage("year") + " mod 100", null, 0, null); // 2
		srcCode_1.addCodeLine("d = b div 4", null, 0, null); // 3
		srcCode_1.addCodeLine("e = b mod 4", null, 0, null); // 4
		srcCode_1.addCodeLine("f = (b + 8) div 25", null, 0, null); // 5
		srcCode_1.addCodeLine("g = (b - f + 1) div 3", null, 0, null); // 6
		srcCode_1.addCodeLine("h = (19a + b - d - g + 15) mod 30", null, 0, null); // 7
		srcCode_1.addCodeLine("i = c div 4", null, 0, null); // 8
		srcCode_1.addCodeLine("k = c mod 4", null, 0, null); // 9
		srcCode_1.addCodeLine("l = (32 + 2e + 2i - h - k) mod 7", null, 0, null); // 10
		srcCode_1.addCodeLine("m = (a + 11h + 22l) div 451", null, 0, null); // 11
		srcCode_1.addCodeLine("n = (h + l - 7m + 114) div 31", null, 0, null); // 12
		srcCode_1.addCodeLine("p = (h + l - 7m + 114) mod 31", null, 0, null); // 13

		lang.nextStep(t.translateMessage("exec"));

		/*
		 * Start calculation of the variables each one at a time. In every step the
		 * calculation and the vars which are used in the calculation will be
		 * highlighted.
		 */
		srcCode_1.highlight(0);
		srcCode_y.highlight(0);
		a = Jahr % 19;
		srcCode_2.addCodeLine("a = " + a, null, 0, null);
		srcCode_3.addCodeLine("(" + Jahr + " % 19)", null, 0, null);

		lang.nextStep();

		srcCode_1.unhighlight(0);
		srcCode_1.highlight(1);
		b = Jahr / 100;
		srcCode_2.addCodeLine("b = " + b, null, 0, null);
		srcCode_3.addCodeLine("(" + Jahr + " / 100)", null, 0, null);

		lang.nextStep();

		srcCode_1.unhighlight(1);
		srcCode_1.highlight(2);
		c = Jahr % 100;
		srcCode_2.addCodeLine("c = " + c, null, 0, null);
		srcCode_3.addCodeLine("(" + Jahr + " % 100)", null, 0, null);

		lang.nextStep();

		srcCode_y.unhighlight(0);
		srcCode_1.unhighlight(2);
		srcCode_1.highlight(3);
		srcCode_2.highlight(1);
		d = b / 4;
		srcCode_2.addCodeLine("d = " + d, null, 0, null);
		srcCode_3.addCodeLine("(" + b + " / 4)", null, 0, null);

		lang.nextStep();

		srcCode_1.unhighlight(3);
		srcCode_1.highlight(4);
		e = b % 4;
		srcCode_2.addCodeLine("e = " + e, null, 0, null);
		srcCode_3.addCodeLine("(" + b + " % 4)", null, 0, null);

		lang.nextStep();

		srcCode_1.unhighlight(4);
		srcCode_1.highlight(5);
		f = (b + 8) / 25;
		srcCode_2.addCodeLine("f = " + f, null, 0, null);
		srcCode_3.addCodeLine("((" + b + " + 8) / 25)", null, 0, null);

		lang.nextStep();

		srcCode_1.unhighlight(5);
		srcCode_1.highlight(6);
		srcCode_2.highlight(5);
		g = (b - f + 1) / 3;
		srcCode_2.addCodeLine("g = " + g, null, 0, null);
		srcCode_3.addCodeLine("((" + b + " - " + f + " + 1) / 3)", null, 0, null);

		lang.nextStep();

		srcCode_1.unhighlight(6);
		srcCode_1.highlight(7);
		srcCode_2.unhighlight(5);
		srcCode_2.highlight(0);
		srcCode_2.highlight(3);
		srcCode_2.highlight(6);
		h = (19 * a + b - d - g + 15) % 30;
		srcCode_2.addCodeLine("h = " + h, null, 0, null);
		srcCode_3.addCodeLine("((19*" + a + " + " + b + " - " + d + " - " + g + " + 15) % 30)", null, 0, null);

		lang.nextStep();

		srcCode_1.unhighlight(7);
		srcCode_1.highlight(8);
		srcCode_2.unhighlight(0);
		srcCode_2.unhighlight(1);
		srcCode_2.unhighlight(3);
		srcCode_2.unhighlight(6);
		srcCode_2.highlight(2);
		i = c / 4;
		srcCode_2.addCodeLine("i = " + i, null, 0, null);
		srcCode_3.addCodeLine("(" + c + " / 4)", null, 0, null);

		lang.nextStep();

		srcCode_1.unhighlight(8);
		srcCode_1.highlight(9);
		k = c % 4;
		srcCode_2.addCodeLine("k = " + k, null, 0, null);
		srcCode_3.addCodeLine("(" + c + " % 4)", null, 0, null);

		lang.nextStep();

		srcCode_1.unhighlight(9);
		srcCode_1.highlight(10);
		srcCode_2.unhighlight(2);
		srcCode_2.highlight(4);
		srcCode_2.highlight(7);
		srcCode_2.highlight(8);
		srcCode_2.highlight(9);
		l = (32 + 2 * e + 2 * i - h - k) % 7;
		srcCode_2.addCodeLine("l = " + l, null, 0, null);
		srcCode_3.addCodeLine("((32 + 2*" + e + " + 2*" + i + " - " + h + " - " + k + ") % 7)", null, 0, null);

		lang.nextStep();

		srcCode_1.unhighlight(10);
		srcCode_1.highlight(11);
		srcCode_2.unhighlight(4);
		srcCode_2.unhighlight(8);
		srcCode_2.unhighlight(9);
		srcCode_2.highlight(0);
		srcCode_2.highlight(7);
		srcCode_2.highlight(10);
		m = (a + 11 * h + 22 * l) / 451;
		srcCode_2.addCodeLine("m = " + m, null, 0, null);
		srcCode_3.addCodeLine("((" + a + " + 11*" + h + " + 22*" + l + ") / 451)", null, 0, null);

		lang.nextStep();

		srcCode_1.unhighlight(11);
		srcCode_1.highlight(12);
		srcCode_2.unhighlight(0);
		srcCode_2.unhighlight(4);
		srcCode_2.highlight(11);
		n = (h + l - 7 * m + 114) / 31;
		srcCode_2.addCodeLine("n = " + n, null, 0, null);
		srcCode_3.addCodeLine("((" + h + " + " + l + " - 7*" + m + " + 114) / 31)", null, 0, null);

		lang.nextStep();

		srcCode_1.unhighlight(12);
		srcCode_1.highlight(13);
		p = (h + l - 7 * m + 114) % 31;
		srcCode_2.addCodeLine("p = " + p, null, 0, null);
		srcCode_3.addCodeLine("((" + h + " + " + l + " - 7*" + m + " + 114) % 31)", null, 0, null);

		lang.nextStep();

		/*
		 * The calculations ended here. In the last step will be a short summary and the
		 * results.
		 */

		srcCode_1.unhighlight(13);
		srcCode_2.unhighlight(2);
		srcCode_2.unhighlight(7);
		srcCode_2.unhighlight(10);
		srcCode_2.unhighlight(11);

		srcCode_2.highlight(12);
		srcCode_2.highlight(13);

		String formatDay = String.format("%02d", (p + 1));
		String formatMonth = String.format("%02d", n);

		Text summary_1 = lang.newText(new Coordinates(50, 430),
				t.translateMessage("summ_1", intToStr(p+1), intToStr(n)), "summary_1", null, Beschreibungen);
		Text summary_2 = lang.newText(new Coordinates(50, 455),
				t.translateMessage("summ_2", formatDay, formatMonth, intToStr(Jahr)), "summary_2", null, Beschreibungen);
	}
	
	private String intToStr(int i) {
		return Integer.toString(i);
	}
}