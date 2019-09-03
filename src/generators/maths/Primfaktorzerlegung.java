/*
 * Primfaktorzerlegung.java
 * Teh-Hai Julian Zheng, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.ValidatingGenerator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.graphics.Size;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.views.MultipleChoiceQuestionView;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.items.IntegerPropertyItem;
import algoanim.util.Coordinates;
import animal.animator.FillInBlanksQuestionAction;
import animal.animator.MultipleChoiceQuestionAction;
import de.ahrgr.animal.kohnert.asugen.Rectangle;

public class Primfaktorzerlegung implements ValidatingGenerator {
	private Language lang;
	private SourceCodeProperties srcProperty;
	private SourceCodeProperties textProperty;
	private TextProperties titleProperty;
	private TextProperties textPropertyBold;
	private int n;
	private int calculations;
	private SourceCode src;
	private SourceCode description;
	private SourceCode closingText;
	private List<Integer> elements;

	public void init() {
		lang = new AnimalScript("Primfaktorzerlegung", "Teh-Hai Julian Zheng", 800, 600);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		srcProperty = (SourceCodeProperties) props.getPropertiesByName("srcProperty");
		textProperty = (SourceCodeProperties) props.getPropertiesByName("descriptionProperty");
		titleProperty = (TextProperties) props.getPropertiesByName("titleProperty");
		textPropertyBold = (TextProperties) props.getPropertiesByName("textPropertyBold");
		
		titleProperty.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20));
		textPropertyBold.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 20));

		n = (Integer) primitives.get("n");
		factorize(n);

		return lang.toString();
	}

	public String getName() {
		return "Primfaktorzerlegung";
	}

	public String getAlgorithmName() {
		return "Primfaktorisierung";
	}

	public String getAnimationAuthor() {
		return "Teh-Hai Julian Zheng & Edgardo Palza";
	}

	public String getDescription() {
		return "Eine Primfaktorisierung ist die Zerlegung einer natürlichen Zahl," + "\n" + "als Produkt von Primzahl."
				+ "\n" + "Dabei heißen die einzelnen Faktoren, aus denen das Produkt besteht," + "\n" + "Primfaktoren."
				+ "\n" + "Diese Darstellung der Zerlegung ist bis auf die Reihenfolge der Primfaktoren eindeutig."
				+ "\n";
	}

	public String getCodeExample() {
		return "public List<Integer>  primefactorization(int n) {" + "\n"
				+ "	List<Integer> factors = new ArrayList<>();" + "\n" + "	int d = 2;" + "\n" + "	while (n > 1) {"
				+ "\n" + "		while (n % d == 0) {" + "\n" + "			factors.add(d);" + "\n" + "			n /= d;"
				+ "\n" + "		}" + "\n" + "		d++;" + "\n" + "		if (d * d > n) {" + "\n"
				+ "			if (n > 1) {" + "\n" + "				factors.add(n);" + "\n" + "				break;"
				+ "\n" + "			}" + "\n" + "		}" + "\n" + "	}" + "\n" + "	return factors;" + "\n" + "}";
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
		return ValidatingGenerator.JAVA_OUTPUT;
	}

	public void factorize(int prim) {

		// Header
		Text header = lang.newText(new Coordinates(20, 30), "Primfaktorzerlegung", "Header", null, titleProperty);

		// Description
		description = lang.newSourceCode(new Coordinates(20, 60), "Beschreibung", null, textProperty);
		lang.nextStep();
		description.addCodeLine(
				"Eine Primfaktorisierung ist die Zerlegung einer natürlichen Zahl, als Produkt von Primzahl.", null, 0,
				null);
		description.addCodeLine("Dabei heißen die einzelnen Faktoren, aus denen das Produkt besteht, Primfaktoren.",
				null, 0, null);
		description.addCodeLine(
				"Diese Darstellung der Zerlegung ist bis auf die Reihenfolge der Primfaktoren eindeutig.", null, 0,
				null);
		description.addCodeLine("", null, 0, null);
		lang.nextStep();
		description.addCodeLine("Als Beispiel: Die Zerlegung von 1080 ist 2*2*2*3*3*3*5", null, 0, null);
		lang.nextStep();

		// Display of Src Code
		src = lang.newSourceCode(new Coordinates(20, 160), "sourceCode", null, srcProperty);
		src.addCodeLine("public List<Integer> primefactorization(int n)", null, 0, null);
		src.addCodeLine("{", null, 0, null);
		src.addCodeLine("List<Integer> factors = new ArrayList<>();", null, 1, null);
		src.addCodeLine("int d = 2;", null, 1, null);
		src.addCodeLine("while (n > 1) {", null, 1, null);
		src.addCodeLine("while (n % d == 0) {", null, 2, null);
		src.addCodeLine("factors.add(d);", null, 3, null);
		src.addCodeLine("n /= d;", null, 3, null);
		src.addCodeLine("}", null, 2, null);
		src.addCodeLine("d++;", null, 2, null);
		src.addCodeLine("if (d * d > n) {", null, 2, null);
		src.addCodeLine("if (n > 1) {", null, 3, null);
		src.addCodeLine("factors.add(n);", null, 4, null);
		src.addCodeLine("break;", null, 4, null);
		src.addCodeLine("}", null, 3, null);
		src.addCodeLine("}", null, 2, null);
		src.addCodeLine("}", null, 1, null);
		src.addCodeLine("return factors;", null, 1, null);
		src.addCodeLine("}", null, 0, null);
		description.hide();
		lang.nextStep();
		
		
		description = lang.newSourceCode(new Coordinates(400, 160), "Beschreibung", null, textProperty);
		
		description.addCodeLine("Die Liste der Faktoren factors und der Divisor d werden initialisiert.", null, 0, null);
		description.addCodeLine("", null, 0, null);
		src.highlight(2, 0, false);
		src.highlight(3, 0, false);
		lang.nextStep();
		
		description.addCodeLine("Der Algorithmus läuft solange die Zahl n größer eins ist.", null, 0, null);
		description.addCodeLine("Solange d noch ein Teiler von n ist, wird d der Liste hinzugefügt und n wird durch d geteilt.", null, 0, null);
		description.addCodeLine("Zudem wird d um eins inkrementiert.", null, 0, null);
		description.addCodeLine("", null, 0, null);
		src.unhighlight(2, 0, false);
		src.unhighlight(3, 0, false);
		src.highlight(4, 0, false);
		src.highlight(5, 0, false);
		src.highlight(6, 0, false);
		src.highlight(7, 0, false);
		src.highlight(9, 0, false);
		lang.nextStep();
		
		description.addCodeLine("Wenn d quadriert größer n ist und n größer als eins ist,", null, 0, null);
		description.addCodeLine("wird n der Liste hinzugefügt und die Schleife hält vorzeitig an.", null, 0, null);
		description.addCodeLine("", null, 0, null);
		src.unhighlight(4, 0, false);
		src.unhighlight(5, 0, false);
		src.unhighlight(6, 0, false);
		src.unhighlight(7, 0, false);
		src.unhighlight(9, 0, false);
		src.highlight(10, 0, false);
		src.highlight(11, 0, false);
		src.highlight(12, 0, false);
		src.highlight(13, 0, false);
		lang.nextStep();
		
		description.addCodeLine("Die Liste der Faktoren wird zurückgegeben.", null, 0, null);
		src.unhighlight(10, 0, false);
		src.unhighlight(11, 0, false);
		src.unhighlight(12, 0, false);
		src.unhighlight(13, 0, false);
		src.highlight(17, 0, false);
		lang.nextStep();
		
		
		src.unhighlight(17, 0, false);
		description.hide();
		lang.nextStep();

		// actual Algorithm
		elements = precalculation(n);
		primfaktorZerlegung(src, n);
		
		String solution = elements.stream().map(ele -> ele.toString()).collect(Collectors.joining(", "));
		closingText = lang.newSourceCode(new Coordinates(20, 100), "Schlusstext", null, srcProperty);
		closingText.addCodeLine("Wie man sieht, sind die Primfaktoren von der Zahl " + n + ": " + solution, null, 0, null);
		closingText.addCodeLine("Die Primfaktorzerlegung teilt die Zahl in die Multiplikation mehrerer kleinerer Zahlan", null, 0, null);
		closingText.addCodeLine("und die Zahl ist nicht in weitere Faktoren Zerlegbar.", null, 0, null);
		closingText.addCodeLine("Insgesamt gab es " + calculations + " Mathematische Operationen.", null, 0, null);
		lang.finalizeGeneration();
		


	}

	public List<Integer> primfaktorZerlegung(SourceCode src, int n) throws LineNotExistsException {

		// Init all Primitives
		int calc = 0;
		int idx = 0;
		String resultString = n + " =";

		Text result = lang.newText(new Coordinates(20, 80), resultString, "Result", null, textPropertyBold);
		Text calctext = lang.newText(new Coordinates(400, 170), "Berechnungen: " + calc, "Calculations", null,
				textPropertyBold);
		Text ntext = lang.newText(new Coordinates(400, 200), "n: " + n, "n", null, textPropertyBold);

		src.highlight(0, 0, false);
		lang.nextStep();

		// new List
		List<Integer> factors = new ArrayList<>();
		src.toggleHighlight(0, 0, false, 2, 0);
		lang.nextStep();

		// init d
		int d = 2;
		src.toggleHighlight(2, 0, false, 3, 0);
		Text dtext = lang.newText(new Coordinates(400, 230), "d: " + d, "d", null, textPropertyBold);
		
		MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("q1");
		q1.setPrompt("Welche Zahl wird der erste Faktor?");
		
		int answer = elements.get(idx);

		q1.addAnswer("" + getRandom(answer), 0, "Falsch!");
		q1.addAnswer("" + answer, 1, "Korrekt!");
		q1.addAnswer("" + getRandom(answer), 0, "Falsch!");
		lang.addMCQuestion(q1);

		lang.nextStep();
		src.unhighlight(3, 0, false);
		src.unhighlight(9, 0, false);
		src.toggleHighlight(3, 0, false, 4, 0);

		lang.nextStep();
		// while 1
		while (n > 1) {
			src.unhighlight(10, 0, false);

			// while 2
			src.toggleHighlight(4, 0, false, 5, 0);
			calc++;
			calctext.setText("Berechnungen: " + calc, null, null);
			lang.nextStep();
			while (n % d == 0) {


				src.toggleHighlight(5, 0, false, 6, 0);
				factors.add(d);
				idx++;
				String pre = " * " + d;
				if (resultString.indexOf("=") == resultString.length() - 1)
					pre = " " + d;
				resultString += pre;
				result.setText(resultString, null, null);
				
				if(idx == elements.size()-1) {
					MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel("q2");
					q2.setPrompt("Welche Zahl wird der nächste Faktor?");
					
					int answer2 = elements.get(idx);

					q2.addAnswer("" + getRandom(answer2), 0, "Falsch!");
					q2.addAnswer("" + answer2, 1, "Korrekt!");
					q2.addAnswer("" + getRandom(answer2), 0, "Falsch!");
					lang.addMCQuestion(q2);
					System.out.println(elements.get(idx));
				}
				

				lang.nextStep();
				src.toggleHighlight(6, 0, false, 7, 0);
				n /= d;
				calc++;
				calctext.setText("Berechnungen: " + calc, null, null);
				ntext.setText("n: " + n, null, null);

				lang.nextStep();
				src.unhighlight(7, 0, false);
			}
			
			src.unhighlight(4, 0, false);
			src.unhighlight(5, 0, false);
			src.highlight(9, 0, false);
			d++;
			calc++;
			calctext.setText("Berechnungen: " + calc, null, null);
			dtext.setText("d: " + d, null, null);
			lang.nextStep();

			src.toggleHighlight(9, 0, false, 10, 0);
			calc++;
			calctext.setText("Berechnungen: " + calc, null, null);
			lang.nextStep();
			if (d * d > n) {

				src.toggleHighlight(10, 0, false, 11, 0);
				lang.nextStep();
				if (n > 1) {

					
					src.toggleHighlight(11, 0, false, 12, 0);
					factors.add(n);
					resultString += " * " + n;
					result.setText(resultString, null, null);
					lang.nextStep();
					
					src.toggleHighlight(12, 0, false, 13, 0);
					lang.nextStep();
					break;
				}
			}
		}
		
		src.unhighlight(10, 0, false);
		src.unhighlight(11, 0, false);
		src.unhighlight(13, 0, false);
		src.highlight(17, 0, false);
		lang.nextStep();
		
		result.hide();
		ntext.hide();
		calctext.hide();
		dtext.hide();
		src.hide();
		calculations = calc;
		return factors;

	}

	private int getRandom(int d) {
		// TODO Auto-generated method stub
		Random rnd = new Random();
		int x = 0;

		do {
			x = rnd.nextInt(d * 2);
		} while (x == d || !isPrime(x));

		return x;
	}
	

    private static boolean isPrime(int inputNum){
        if (inputNum <= 3 || inputNum % 2 == 0) 
            return inputNum == 2 || inputNum == 3; //this returns false if number is <=1 & true if number = 2 or 3
        int divisor = 3;
        while ((divisor <= Math.sqrt(inputNum)) && (inputNum % divisor != 0)) 
            divisor += 2; //iterates through all possible divisors
        return inputNum % divisor != 0; //returns true/false
    }


	private List<Integer> precalculation(int n) {
		List<Integer> factors = new ArrayList<>();
		int d = 2;
		while (n > 1) {
			while (n % d == 0) {
				factors.add(d);
				n /= d;
			}
			d++;
			if (d * d > n) {
				if (n > 1) {
					factors.add(n);
					break;
				}
			}
		}
		return factors;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if ((Integer) primitives.get("n") <= 1)
			return false;
		return true;
	}
}