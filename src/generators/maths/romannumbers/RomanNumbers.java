package generators.maths.romannumbers;
import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;


import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class RomanNumbers {


	/**
	 * Haupt-Prozedur des Programms
	 */
	public static void main(String[] args) {

		Language lang = new AnimalScript("Römische Zahldarstellung", "Nicole Brunkhorst, Stefan Rado", 900, 900);
		RomanNumbers roman = new RomanNumbers(lang);
		roman.createAnimation(3999);

		String script = lang.toString();
		System.out.println(script);
		
		try {
			FileOutputStream fos = new FileOutputStream("romanGen.asu");
			OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
			osw.write(script);
			osw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	// Animal objects
	private Language lang;
	private SourceCodeProperties sourceCodeProperties;
	private SourceCodeProperties descriptionProperties;
	private Text h2;
	private Text algo2;
	private Text number1;
	private Text result;
	private SourceCode einleitung;
	private SourceCode ziffern;
	private SourceCode basis;
	private SourceCode darstellung;
	private SourceCode schreib;
	private SourceCode regel1;
	private SourceCode regel2;
	private SourceCode regel3;
	private SourceCode nulll;
	private SourceCode algo;
	private SourceCode algo3;
	private Rect ziffernRect;
	private Rect algoRect;
	private Rect numberRect;
	private Rect resultRect;


	public RomanNumbers(Language language) {
		this.lang = language;

		sourceCodeProperties = new SourceCodeProperties();
		sourceCodeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		sourceCodeProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		sourceCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 14));
		
		descriptionProperties = new SourceCodeProperties();
	}

	public void setSourceCodeProperties(SourceCodeProperties sourceCodeProperties) {
		this.sourceCodeProperties = sourceCodeProperties;
	}
	
	public void setDescriptionProperties(SourceCodeProperties descriptionProperties) {
		this.descriptionProperties = descriptionProperties;
	}
	
	public void createAnimation(int N) {
		lang.setStepMode(true);
		//Hier muss die animation zusammen gesetzt werden!
		createIntroductionStep1();
		createIntroductionStep2();
		createIntroductionStep3();
		createIntroductionStep4();
		createIntroductionStep5();
		createIntroductionStep6();
		createIntroductionStep7();
		createIntroductionStep8();
		createIntroductionStep9();
		createIntroductionStep10();
		createIntroductionStep11();
		createIntroductionStep12();
		createIntroductionStep13();
		createIntroductionStep14();
		createIntroductionStep15();
		createIntroductionStep16();
		createIntroductionStep17();
		createIntroductionStep18(N);
		createAlgorithmSteps(N);
		createIntroductionStep19();
	}

	private void createIntroductionStep1() {
		lang.addLine("label \"Einleitung\"");
		
		// Titel-Text
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		lang.newText(new Coordinates(20, 30), "Römische Zahldarstellung", "h1", null, tp);

		// Rahmen für Titel und Abschnitts-Name
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		lang.newRect(
				new Offset(-5, -5, "h1", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "h1", AnimalScript.DIRECTION_SE),
				"h1Rect", null, rp);


		// Text mit Titel des aktuellen Animations-Abschnittes
		tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 18));
		h2 = lang.newText(new Offset(5, 5, "h1Rect", AnimalScript.DIRECTION_SW), "Einleitung", "h2", null, tp);


		// Einleitungs-Text
		einleitung = lang.newSourceCode(new Offset(0, 0, "h2", AnimalScript.DIRECTION_SW), "einleitung", null, descriptionProperties);

		einleitung.addCodeLine("Als römische Zahlen bezeichnet man die gebräuchliche Zahlschrift", null, 0, null);
		einleitung.addCodeLine("die in der römischen Antike entstanden ist und noch heute für", null, 0, null);
		einleitung.addCodeLine("Nummern und besondere Zwecke genutzt wird.", null, 0, null);
	}

	private void createIntroductionStep2() {
		lang.nextStep();
		
		einleitung.addCodeLine("", null, 0, null);
		einleitung.addCodeLine("In der heutigen Normalform stehen lateinische Buchstaben", null, 0, null);
		einleitung.addCodeLine("als Zahlzeichen für die natürlichen Zahlen:", null, 0, null);
	}

	private void createIntroductionStep3() {
		lang.nextStep();
		
		ziffern = lang.newSourceCode(new Offset(0, 0, "einleitung", AnimalScript.DIRECTION_SW), "ziffern", null, descriptionProperties);

		ziffern.addCodeLine("I = 1", null, 0, null);
		ziffern.addCodeLine("V = 5", null, 0, null);
		ziffern.addCodeLine("X = 10", null, 0, null);
		ziffern.addCodeLine("L = 50", null, 0, null);
		ziffern.addCodeLine("C = 100", null, 0, null);
		ziffern.addCodeLine("D = 500", null, 0, null);
		ziffern.addCodeLine("M = 1000", null, 0, null);

		basis = lang.newSourceCode(new Offset(0, 0, "ziffern", AnimalScript.DIRECTION_SW), "basis", null, descriptionProperties);

		basis.addCodeLine("Die natürlichen Zahlen werden dabei zur Basis 10 mit", null, 0, null);
		basis.addCodeLine("Hilfsbasis 5 dargestellt.", null, 0, null);
	}
	
	private void createIntroductionStep4() {
		lang.nextStep("Darstellungsregeln");
		
		try {
			ziffern.moveTo("NW", "translate", new Offset(300, 0, "h1", AnimalScript.DIRECTION_NE) , null, new MsTiming(1000));
		} catch (IllegalDirectionException e) {
			// ignore this
			e.printStackTrace();
		}
		
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.PINK);
		ziffernRect = lang.newRect(
				new Offset(-5, -5, "ziffern", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "ziffern", AnimalScript.DIRECTION_SE),
				"ziffernRect", null, rp);
		
		try {
			ziffernRect.moveTo("NW", "translate", new Offset(295, -5, "h1", AnimalScript.DIRECTION_NE), null, new MsTiming(1000));
		} catch (IllegalDirectionException e) {
			// ignore this
			e.printStackTrace();
		}
		
		h2.setText("Darstellungsregeln", null, null);
		
		einleitung.hide();
		basis.hide();
	}
	
	private void createIntroductionStep5() {
		lang.nextStep();
		
		darstellung = lang.newSourceCode(new Offset(0, 0, "h2", AnimalScript.DIRECTION_SW), "darstellung", null, descriptionProperties);
		
		darstellung.addCodeLine("Bei der römischen Zahlendarstellung handelt es sich um eine", null, 0, null);
		darstellung.addCodeLine("additive Zahlschrift.", null, 0, null);
		darstellung.addCodeLine("", null, 0, null);
		darstellung.addCodeLine("Nach der früheren Schreibweise konnten viermal die gleichen", null, 0, null);
		darstellung.addCodeLine("Zeichen nebeneinander stehen.", null, 0, null);
	}
	
	private void createIntroductionStep6() {
		lang.nextStep();
		
		darstellung.addCodeLine("", null, 0, null);
		darstellung.addCodeLine("So steht z.B.:", null, 0, null);
		darstellung.addCodeLine("", null, 0, null);
		darstellung.addCodeLine("           IIII           (1 + 1 + 1 + 1)              für 4.", null, 0, null);
		darstellung.addCodeLine("", null, 0, null);
	}
	
	private void createIntroductionStep7() {
		lang.nextStep();
		
		darstellung.addCodeLine("Um diese Schreibweise zu verkürzen wurden die Regeln zur", null, 0, null);
		darstellung.addCodeLine("subtraktiven Schreibung eingeführt.", null, 0, null);
	}
	
	private void createIntroductionStep8() {
		lang.nextStep();
		
		darstellung.hide();
		h2.setText("Subtraktive Schreibweise", null, null);
	}
	
	private void createIntroductionStep9() {
		lang.nextStep();
		
		schreib = lang.newSourceCode(new Offset(0, 0, "h2", AnimalScript.DIRECTION_SW), "schreib", null, descriptionProperties);
		
		schreib.addCodeLine("Die subtraktive Schreibung besagt, das die Zahlzeichen zur", null, 0, null);
		schreib.addCodeLine("Basis 10 (also I, X, C und M) einem ihrer beiden jeweils", null, 0, null);
		schreib.addCodeLine("nächstgrößeren Zahlzeichen vorangestellt werden dürfen.", null, 0, null);
		schreib.addCodeLine("", null, 0, null);
		schreib.addCodeLine("Diese werden dann von den darauf folgenden Zeichen abgezogen.", null, 0, null);
		schreib.addCodeLine("", null, 0, null);
	}
	
	private void createIntroductionStep10() {
		lang.nextStep();
		
		schreib.addCodeLine("So steht z.B.:", null, 0, null);
		schreib.addCodeLine("", null, 0, null);
		schreib.addCodeLine("              IV         (5 - 1)          für 4", null, 0, null);
		schreib.addCodeLine("und       IX        (10 - 1)        für 9.", null, 0, null);
	}
	
	private void createIntroductionStep11() {
		lang.nextStep();
		
		schreib.hide();
		h2.setText("Regeln / häufig gemachte Fehler", null, null);
	}
	
	private void createIntroductionStep12() {
		lang.nextStep();
		
		regel1 = lang.newSourceCode(new Offset(0, 0, "h2", AnimalScript.DIRECTION_SW), "regel1", null, descriptionProperties);
		
		regel1.addCodeLine("Es ist nicht zulässig mehr als ein einziges Zeichen subtraktiv", null, 0, null);
		regel1.addCodeLine("zu verwenden.", null, 0, null);
		regel1.addCodeLine("", null, 0, null);
		regel1.addCodeLine("Zeichen wie", null, 0, null);
		regel1.addCodeLine("", null, 0, null);
		regel1.addCodeLine("           IIX      (10 - 1 - 1)         für 8 sind nicht gültig.", null, 0, null);
		regel1.addCodeLine("", null, 0, null);
		regel1.addCodeLine("Statt dessen ist", null, 0, null);
		regel1.addCodeLine("", null, 0, null);
		regel1.addCodeLine("           VIII     (5 + 1 + 1 + 1)        für 8 zu verwenden.", null, 0, null);
	}
	
	private void createIntroductionStep13() {
		lang.nextStep();
		
		regel1.hide();
		regel2 = lang.newSourceCode(new Offset(0, 0, "h2", AnimalScript.DIRECTION_SW), "regel2", null, descriptionProperties);
		
		regel2.addCodeLine("Weiterhin darf einem Zeichen auch seinem dritt- oder", null, 0, null);
		regel2.addCodeLine("viertgrösseren Zeichen nicht subtraktiv voran gestellt werden.", null, 0, null);
		regel2.addCodeLine("", null, 0, null);
		regel2.addCodeLine("Also wären falsch:", null, 0, null);
		regel2.addCodeLine("", null, 0, null);
		regel2.addCodeLine("             IL            (50 - 1)               für 49", null, 0, null);
		regel2.addCodeLine("             IC          (100 - 1)              für 99", null, 0, null);
		regel2.addCodeLine("             XM       (1000 - 10)           für 990", null, 0, null);
		regel2.addCodeLine("", null, 0, null);
		regel2.addCodeLine("Richtig ist es:", null, 0, null);
		regel2.addCodeLine("", null, 0, null);
		regel2.addCodeLine("           XLIX               ((50 - 10) + (10 - 1))         für 49", null, 0, null);
		regel2.addCodeLine("           XCIX             ((100 - 10) + (10 - 1))        für 99", null, 0, null);
		regel2.addCodeLine("           CMXC       ((1000 - 100) + (100 - 10))   für 990", null, 0, null);
	}
	
	private void createIntroductionStep14() {
		lang.nextStep();
		
		regel2.hide();
		regel3 = lang.newSourceCode(new Offset(0, 0, "h2", AnimalScript.DIRECTION_SW), "regel3", null, descriptionProperties);
		
		regel3.addCodeLine("Auch ist es nicht zulässig auf ein Subtraktionspaar ein", null, 0, null);
		regel3.addCodeLine("einzelnes Zeichen folgen zu lassen, das eine höhere", null, 0, null);
		regel3.addCodeLine("Wertigkeit als der linke Teil des Subtraktionspaars hat.", null, 0, null);
		regel3.addCodeLine("", null, 0, null);
		regel3.addCodeLine("Bspw. ist", null, 0, null);
		regel3.addCodeLine("", null, 0, null);
		regel3.addCodeLine("            XCLIII         ((100 - 10) + 50 + 1 + 1 + 1)        ", null, 0, null);
		regel3.addCodeLine("", null, 0, null);
		regel3.addCodeLine("als Darstellung von 143 nicht erlaubt.", null, 0, null);
		regel3.addCodeLine("L hat einen höheren Wert als X. Die korrekte Darstellung", null, 0, null);
		regel3.addCodeLine("von 143 ist:", null, 0, null);
		regel3.addCodeLine("", null, 0, null);
		regel3.addCodeLine("            CXLIII               (100 + (50 - 10) + 1 + 1 + 1).", null, 0, null);
	}
	
	private void createIntroductionStep15() {
		lang.nextStep();
		
		regel3.hide();
		nulll = lang.newSourceCode(new Offset(0, 0, "h2", AnimalScript.DIRECTION_SW), "nulll", null, descriptionProperties);
		
		nulll.addCodeLine("Eine Null oder ein Stellenwertsystem gibt es nicht.", null, 0, null);
		nulll.addCodeLine("", null, 0, null);
		nulll.addCodeLine("725 n. Chr. wurd das Zeichen N für die Bezeichnung der", null, 0, null);
		nulll.addCodeLine("Null verwendet.", null, 0, null);
		nulll.addCodeLine("", null, 0, null);
		nulll.addCodeLine("Da maximal drei mal die gleichen Zeichen neben-", null, 0, null);
		nulll.addCodeLine("einander stehen dürfen, ist die größte darstellbare", null, 0, null);
		nulll.addCodeLine("Zahl 3999 (MMMCMXCIX).", null, 0, null);
	}
	
	private void createIntroductionStep16() {
		lang.nextStep("Der Algorithmus");
		
		nulll.hide();
		h2.setText("Der Algorithmus", null, null);
		
		algo = lang.newSourceCode(new Offset(0, 0, "h2", AnimalScript.DIRECTION_SW), "algo", null, descriptionProperties);
		
		algo.addCodeLine("Damit der Algorithmus die Subtraktionsregeln einhalten", null, 0, null);
		algo.addCodeLine("kann, werden nicht nur die normalen Zahlen zur Basis 10 und 5", null, 0, null);
		algo.addCodeLine("benutzt, sondern auch die Kombinationen die durch die", null, 0, null);
		algo.addCodeLine("Subtraktionsregeln entstanden sind.", null, 0, null);
		algo.addCodeLine("Also:", null, 0, null);
		algo.addCodeLine("", null, 0, null);
		algo.addCodeLine("IV = 4", null, 0, null);
		algo.addCodeLine("IX = 9", null, 0, null);
		algo.addCodeLine("XL = 40", null, 0, null);
		algo.addCodeLine("XC = 90", null, 0, null);
		algo.addCodeLine("CD = 400", null, 0, null);
		algo.addCodeLine("CM = 900", null, 0, null);
		algo.addCodeLine("", null, 0, null);
		algo.addCodeLine("Der Algorithmus speichert in einem Array die Zahlenzeichen von 1000 (M)", null, 0, null);
		algo.addCodeLine("bis 1 (I) und in einem anderen Array die dazugehörigen Zahlen.", null, 0, null);
		algo.addCodeLine("Nun wird die darzustellende Zahl mit dem ersten Wert im Array verglichen.", null, 0, null);
		algo.addCodeLine("Ist die erste Zahl im Array kleiner oder gleich der darzustellenden,", null, 0, null);
		algo.addCodeLine("wird das entsprechende Zeichen an dem Ergebnisstring angehängt", null, 0, null);
		algo.addCodeLine("und der Wert von der darzustellenden Zahl abgezogen.", null, 0, null);
		algo.addCodeLine("Ist die darzustellende Zahl kleiner als der erste Wert im Array,", null, 0, null);
		algo.addCodeLine("so wird mit dem nächsten Wert im Array verglichen.", null, 0, null);
		algo.addCodeLine("Dies wird solange gemacht bis das Array komplett durchlaufen ist und die", null, 0, null);
		algo.addCodeLine("darzustellende Zahl auf Null gesunken ist. Der Ergebnisstring wird ausgegeben.", null, 0, null);
	}
	
	private void createIntroductionStep17() {
		lang.nextStep();
		
		algo.hide();
		ziffernRect.hide();
		ziffern.hide();
		
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
		algo2 = lang.newText(new Offset(0, 15, "h2", AnimalScript.DIRECTION_SW), "Der Algorithmus sieht folgendermaßen aus:", "algo2", null, tp);
				

		algo3 = lang.newSourceCode(new Offset(0, 15, "algo2", AnimalScript.DIRECTION_SW), "algo3", null, sourceCodeProperties);
		
		algo3.addCodeLine("public String toRomanNumeral(int number) {", null, 0, null);
		algo3.addCodeLine(  "if (number < 0 || number > 3999)", null, 1, null);
		algo3.addCodeLine(    "throw new IllegalArgumentException(\\\"Value must be in the range 0 - 3999.\\\");", null, 2, null);
		algo3.addCodeLine("", null, 0, null);
		algo3.addCodeLine(  "if (number == 0)", null, 1, null);
		algo3.addCodeLine(    "return \\\"N\\\";", null, 2, null);
		algo3.addCodeLine("", null, 0, null);
		algo3.addCodeLine(  "StringBuilder result = new StringBuilder();", null, 1, null);
		algo3.addCodeLine("", null, 0, null);
		algo3.addCodeLine(  "final int[] values = ", null, 1, null);
		algo3.addCodeLine(    "new int[] { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };", null, 2, null);
		algo3.addCodeLine(  "final String[] numerals = ", null, 1, null);
		algo3.addCodeLine(    "new String[] { \\\"M\\\", \\\"CM\\\", \\\"D\\\", \\\"CD\\\", \\\"C\\\", \\\"XC\\\", \\\"L\\\", \\\"XL\\\", \\\"X\\\", \\\"IX\\\", \\\"V\\\", \\\"IV\\\", \\\"I\\\" };", null, 2, null);
		algo3.addCodeLine("", null, 0, null);
		algo3.addCodeLine(  "for (int i = 0; i < values.length; i++) {", null, 1, null);
		algo3.addCodeLine(    "while (number >= values[i]) {", null, 2, null);
		algo3.addCodeLine(      "number -= values[i];", null, 3, null);
		algo3.addCodeLine(      "result.append(numerals[i]);", null, 3, null);
		algo3.addCodeLine(    "}", null, 2, null);
		algo3.addCodeLine(  "}", null, 1, null);
		algo3.addCodeLine(  "return result.toString();", null, 1, null);
		algo3.addCodeLine("}", null, 0, null);
		
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		algoRect = lang.newRect(
				new Offset(-5, -5, "algo3", AnimalScript.DIRECTION_NW),
				new Offset(800, 5, "algo3", AnimalScript.DIRECTION_SW),
				"algoRect", null, rp);
	}
	
	private void createIntroductionStep18(int n) {
		lang.nextStep("Umwandlung der Zahl " + n);
		
		algo2.setText("Es soll die Zahl " + n + " dargestellt werden.", null, null);
		
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		number1 = lang.newText(new Offset(100, 5, "h1", AnimalScript.DIRECTION_NE), "number = " + n, "number1", null, tp);
		
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.PINK);
		numberRect = lang.newRect(
				new Offset(-5, -5, "number1", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "number1", AnimalScript.DIRECTION_SE),
				"numberRect", null, rp);
	}
	
	private void createAlgorithmSteps(int number) {
		lang.nextStep();
		algo3.highlight(1);
		int number2 = number;
    algo2.setText("Kann " + number2 + " dargestellt werden?", null, null);
		
		if (number2 < 0 || number2 > 3999) {
			lang.nextStep();
			algo3.unhighlight(1);
			algo3.highlight(2);
			algo2.setText("Die Zahl " + number2 + " kann nicht dargestellt werden. Der Algorithmus terminiert mit einem Fehler.", null, null);
			return;
		}
		lang.nextStep();
		algo2.setText("Die Zahl " + number2 + " kann dargestellt werden!", null, null);
		algo3.unhighlight(1);
		
		lang.nextStep();
		algo2.setText("Ist " + number2 + " gleich 0?", null, null);
		algo3.highlight(4);
		
		if (number2 == 0) {
			lang.nextStep();
			algo3.unhighlight(4);
			algo3.highlight(5);
			algo2.setText("Die Zahl " + number2 + "ist gleich 0 und der Algorithmus terminiert mit der Ausgabe \\\"N\\\"", null, null);
			return;
		}
		
		lang.nextStep();
		algo2.setText("Die Zahl " + number2 + " ist ungleich 0!", null, null);
		algo3.unhighlight(4);
		
		lang.nextStep();
		algo2.setText("Es wird ein StringBuilder zum Zwischenspeichern des Ergebnisses erstellt.", null, null);
		algo3.highlight(7);
		
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		result = lang.newText(new Offset(0, 15, "number1", AnimalScript.DIRECTION_SW), "result =                                           " , "result", null, tp);
		
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.PINK);
		resultRect = lang.newRect(
				new Offset(-5, -5, "result", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "result", AnimalScript.DIRECTION_SE),
				"resultRect", null, rp);
		
		StringBuilder r = new StringBuilder();
		
		lang.nextStep();
		algo2.setText("Es wird ein Array mit den darstellbaren Zahlkombinationen erstellt und ...", null, null);
		algo3.unhighlight(7);
		algo3.highlight(9);
		algo3.highlight(10);
		
		final int[] values = new int[] { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
		
		lang.nextStep();
		algo2.setText("... parallel dazu ein Array mit den dazugehörigen Zifferkombinationen.", null, null);
		algo3.unhighlight(9);
		algo3.unhighlight(10);
		algo3.highlight(11);
		algo3.highlight(12);
	
		final String[] numerals = new String[] { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
		
		lang.nextStep();
		algo2.setText("Es wird eine Schleife gestartet die i als Zeiger für die Arrays benutzt.", null, null);
		algo3.unhighlight(11);
		algo3.unhighlight(12);
		algo3.highlight(14);
		
		for(int i = 0; i < values.length; i++ ){
			
			lang.nextStep();
			algo2.setText("Ist " + number2 + " größer gleich " + values[i] + "?", null, null);
			algo3.unhighlight(14);
			algo3.highlight(15);
			
			while(number2 >= values[i]) {
				algo3.unhighlight(14);
				algo3.unhighlight(17);
				algo3.highlight(15);
				
				lang.nextStep();
				algo2.setText("" + number2 + " ist größer gleich " + values[i] + ".", null, null);
				
				lang.nextStep();
				algo2.setText("Von " + number2 + " wird " + values[i] + " abgezogen.", null, null);
				algo3.unhighlight(15);
				algo3.highlight(16);
				
				lang.nextStep();
				number2 -= values[i];
				number1.setText("number = " + number2, null, null);
				
				lang.nextStep();
				algo2.setText("Und am StringBuffer wird das Zwischenergebnis \\\"" + numerals[i] + "\\\" angehängt.", null, null);
				algo3.unhighlight(16);
				algo3.highlight(17);
				
				lang.nextStep();
				r.append(numerals[i]);
				result.setText("result = " + r.toString(), null, null);
				
				lang.nextStep();
				algo2.setText("Ist " + number2 + " größer gleich " + values[i] + "?", null, null);
				algo3.highlight(15);
				algo3.unhighlight(17);
				
			}
			
			lang.nextStep();
			algo2.setText(" " + number2 + " ist kleiner als " + values[i] + " , der Pointer i wird um eins erhöht.", null, null);
			algo3.unhighlight(15);
			algo3.highlight(14);			
		}
		
		lang.nextStep("Ende der Umwandlung von " + number2);
		algo2.setText("Die Berechnung ist fertig. Die Schleife terminiert, da der Pointer i nun gleich values.length ist!", null, null);
		
		lang.nextStep();
		algo2.setText("Der StringBuffer wird in einen String umgewandelt und als Ergebnis zurückgegeben.", null, null);
		
		algo3.unhighlight(14);
		algo3.highlight(20);
		
		lang.nextStep();
		
		try {
			result.moveTo("SW", "translate", new Offset(0, 600, "h1", AnimalScript.DIRECTION_NE) , null, new MsTiming(1000));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}
		
		try {
			resultRect.moveTo("SW", "translate", new Offset(-5, 595, "h1", AnimalScript.DIRECTION_NE) , null, new MsTiming(1000));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}
	}
	
	private void createIntroductionStep19() {
		lang.nextStep("Quellenangaben");
		
		algo3.hide();
		number1.hide();
		numberRect.hide();
		result.hide();
		resultRect.hide();
		algo2.hide();
		algoRect.hide();
		
		h2.setText("Quellen", null, null);
		
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
		lang.newText(new Offset(5, 50, "h2", AnimalScript.DIRECTION_SW), "Wikipedia:", "Wikipedia", null, tp);
		
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.ITALIC, 14));
		lang.newText(new Offset(0, 0, "Wikipedia", AnimalScript.DIRECTION_SW), "http://de.wikipedia.org/wiki/R%C3%B6mische_Zahlen", "quelle1", null, tp);
		
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
		lang.newText(new Offset(0, 30, "quelle1", AnimalScript.DIRECTION_SW), "GDI 3 im WS 11/12, 1. Praktikum:", "gdi", null, tp);
		
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
		lang.newText(new Offset(0, 5, "gdi", AnimalScript.DIRECTION_SW), "Prof. Dr.-Ing. Michael Goesele, Sven Widmer, Dominik Wodniok, Michael Waechter:", "gdi1", null, tp);
		
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
		lang.newText(new Offset(0, 0, "gdi1", AnimalScript.DIRECTION_SW), "Grunkdlagen der Informatik III, 1. Praktikum \\\"Römische Zahlen\\\", 26.10.2011", "gdi2", null, tp);
		
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.ITALIC, 14));
		lang.newText(new Offset(0, 5, "gdi2", AnimalScript.DIRECTION_SW), "https://moodle-alt.tu-darmstadt.de/mod/resource/view.php?id=26384", "gdi3", null, tp);
	}
}