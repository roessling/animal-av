package generators.misc;
/*
 * Normalization2.java
 * Christopher Benz, Dennis Kuhn, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class Normalization implements Generator, ValidatingGenerator {
	
	public static Rect titleFrame;
	public static DoubleArray douArray;
	public static DoubleArray copy;
	
	public static SourceCode introduction;
	public static SourceCode fazit;
	public static SourceCode sc;
	
	public static Text mintext;
	public static Text maxtext;
	public static Text formula;
	
	public static MultipleChoiceQuestionModel mc1;
	public static MultipleChoiceQuestionModel mc2;
	public static MultipleChoiceQuestionModel mc3;
	
	public static RectProperties titleFrameP;
	public static TextProperties titleP;
	public static ArrayMarkerProperties currentP;
	public static TextProperties mmtextP;
	public static SourceCodeProperties scP;
	
	public static ArrayMarker current;
	public static TwoValueView cv;
	public static TwoValueCounter counter;
	public static CounterProperties cp;
	
	private Language lang;
	private ArrayProperties arrayP;
	private int[] intArray;
	private int type;
	private double mcprobability;

	public final static Timing defaultTiming = new TicksTiming(30);

	public void init() {
		lang = new AnimalScript("Min-Max Normalization", "Christopher Benz, Dennis Kuhn", 1280, 800);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		arrayP = (ArrayProperties) props.getPropertiesByName("array");
		intArray = (int[]) primitives.get("intArray");
		type = (Integer) primitives.get("int");
		mcprobability = (double) primitives.get("mcprobability");

		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		lang.setStepMode(true);

		showIntro();

		normalize(intArray, type, mcprobability);

		lang.finalizeGeneration();

		return lang.toString();
	}

	public String getName() {
		return "Min-Max Normalization";
	}

	public String getAlgorithmName() {
		return "Min-Max Normalization";
	}

	public String getAnimationAuthor() {
		return "Christopher Benz, Dennis Kuhn";
	}

	public String getDescription() {
		return "Der Algorithmus normalisiert ein Array numerischer Werte in das Intervall [0,1], wobei das Minimum "
				+ "\n" + "des Arrays zu Null wird und das Maximum zu Eins. Alle dazwischenliegenden Werte werden" + "\n"
				+ "entsprechend interpoliert. Hierzu wird zun�chst das Array durchlaufen, um das Minimum und" + "\n"
				+ "Maximum zu finden. Anschlie�end wird jedes Element des Arrays, je nachdem welche Normalisierungs-"
				+ "\n" + "methode gew�hlt wird, umgerechnet. Hier werden die lineare, quadratische und logarithmische"
				+ "\n"
				+ "Normalisierung gezeigt. Um eine Variante auszuw�hlen bestimmt der Nutzer einen Eingabe-Integer,"
				+ "\n"
				+ "wobei \"1\" der linearen, \"2\" der quadratischen und \"3\" der logarithmischen Variante entspricht."
				+ "\nEs wird folgenderma�en normalisiert:" + "\n" + "\n" + "Linear:" + "\n"
				+ "x_lin = (x - min) / (max - min)" + "\n" + "\n" + "Quadratisch:" + "\n"
				+ "x_sq = ((x - min) / (max - min))^2" + "\n" + "\n" + "Logarithmisch:" + "\n"
				+ "x_ln = (ln(x) - ln(min)) / (ln(max) - ln(min))" + "\n" + "\n";
	}

	public String getCodeExample() {
		return "public void normalize(double[] array) {" + "\n" + "    double min = array[0]" + "\n"
				+ "    double max = array[0]" + "\n" + "    for (int i = 1; i < array.length; i++) {" + "\n"
				+ "        if (array[i]<min) {" + "\n" + "            min=array[i];" + "\n" + "        }" + "\n"
				+ "        if (array[i]>max) {" + "\n" + "            max=array[i];" + "\n" + "        }" + "\n"
				+ "    }" + "\n" + "    for (double j: array) {" + "\n"
				+ "        array[j]= (array[j]-min) / (max-min);" + "\n" + "    }" + "\n" + "}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	public void showIntro() {
		// �berschrift
		TextProperties titleP = new TextProperties();
		titleP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		titleP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 26));
		Text title = lang.newText(new Coordinates(170, 10), "Min-Max Normalization", "Title", null, titleP);
		
		RectProperties titleFrameP = new RectProperties();
		titleFrameP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		title.show();
		titleFrame = lang.newRect(new Offset(-2, -2, title, AnimalScript.DIRECTION_NW),
				new Offset(2, 16, title, AnimalScript.DIRECTION_SE), "titleFrame", null, titleFrameP);

		// description
		String intro = "Die hier vorgestellte Min-Max Normalisierung skaliert eine Reihe numerischer Daten in das Intervall [0,1]."
				+ "\nDer kleinste Wert wird zu dabei zu Null normalisiert und der Gr\u00f6\u00dfte wird zu Eins."
				+ "\nAlle dazwischen liegenden Werte werden entsprechend interpoliert."
				+ "\nDie Min-Max Normalisierung kann auf verschiedene Arten durchgef\u00fchrt werden."
				+ "\nHier werden die lineare, quadratische und logarithmische Mix-Max Normalisierung gezeigt."
				+ "\nDer Algorithmus sucht zun\u004echst den minimalen (min) und den maximalen (max) Wert in der Datenreihe."
				+ "\nBei der linearen Version wird anschlie\u00dfend folgende Berechnung auf jedes Element x der Datenreihe angewandt:"
				+ "\n " + "\nx_lin = (x - min) / (max - min)" + "\n "
				+ "\nBei der quadratischen Variante wird der Ausdruck quadriert:" + "\n "
				+ "\nx_sq = ((x - min) / (max - min))^2" + "\n "
				+ "\nBei der logarithmischen Normalisierung nutzt man den nat�rlichen Logarithmus der Werte:" + "\n "
				+ "\nx_ln = (ln(x) - ln(min)) / (ln(max) - ln(min))" + "\n "
				+ "\nDie Normalisierung wird beispielsweise im Machine Learning eingesetzt. So verwendet k-Nearest Neighbour"
				+ "\ndie euklidische Distanz zur Bestimmung der \u00c4hnlichkeit von Datenobjekten. Wenn sich hierbei ein quantitatives"
				+ "\nAttribut in einem viel h\u00f6heren Wertebereich bewegt als die anderen, so wird die Distanzbestimmung von diesem"
				+ "\nAttribut dominiert, w\u004ehrend die anderen Attribute kaum Einfluss haben werden. Damit alle Attribute"
				+ "\ngleicherma\u00dfen einflie\u00dfen, k\u00f6nnte man die Attributauspr\u004egungen in das Intervall [0,1] normalisieren.";

		SourceCode introduction = lang.newSourceCode(new Coordinates(50, 60), "myintro", null);
		introduction.addMultilineCode(intro, "0", null);
		lang.nextStep("Intro");
		introduction.hide();
	}

	@SuppressWarnings("unused")
  public void normalize(int[] intarray, int type, double mcprobability) {

		// konversion des eingabe-intarray in ein double array, da man bei der
		// generierung der .xml kein doublearray als primitiv w�hlen konnte
		double[] array = new double[intarray.length];
		for (int i = 0; i < intarray.length; i++) {
			array[i] = intarray[i];
		}

		// Array und ArrayProperties erstellen und setten
		DoubleArray douArray = lang.newDoubleArray(new Coordinates(40, 110), array, "array", null, arrayP);

		// ArrayMarkerProperties
		ArrayMarkerProperties currentP = new ArrayMarkerProperties();
		currentP.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
		currentP.set(AnimationPropertiesKeys.LABEL_PROPERTY, "");
		lang.nextStep();

		// dieser text zeigt das aktuelle minimum und maximum des arrays an
		TextProperties mmtextP = new TextProperties();
		mmtextP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		mmtextP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		Text mintext = lang.newText(new Coordinates(40, 165), null, "meme too", null, mmtextP);
		Text maxtext = lang.newText(new Coordinates(40, 180), null, "meme too", null, mmtextP);
		mintext.setText("min =", null, null);
		maxtext.setText("max =", null, null);
		lang.nextStep();

		// source code anzeigen
		SourceCodeProperties scP = new SourceCodeProperties();
		scP.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		scP.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode sc = lang.newSourceCode(new Coordinates(40, 200), "sourceCode", null, scP);
		sc.addCodeLine("public void normalize(double[] array)", "", 0, null); // 0
		sc.addCodeLine("double min = array[0]", "", 1, null); // 1
		sc.addCodeLine("double max = array[0]", null, 1, null); // 2
		sc.addCodeLine("for (int i = 1; i < array.length; i++) {", null, 1, null); // 3
		sc.addCodeLine("if (array[i]<min) {", null, 2, null); // 4
		sc.addCodeLine("min=array[i];", null, 3, null); // 5
		sc.addCodeLine("}", null, 2, null); // 6
		sc.addCodeLine("if (array[i]>max) {", null, 2, null); // 7
		sc.addCodeLine("max=array[i];", null, 3, null); // 8
		sc.addCodeLine("}", null, 2, null); // 9
		sc.addCodeLine("}", null, 1, null); // 10

		// der restliche code h�ngt von der normalisierungsmethode ab
		Text normalizationvariant = lang.newText(new Coordinates(170, 40), null, "meme too", null, mmtextP);
		switch (type) { // type gibt die normalisierungsmethode an
		case 1:
			normalizationvariant.setText("linear variant", null, null);
			sc.addCodeLine("for (double j: array) {", null, 1, null); // 11
			sc.addCodeLine("array[j] = (array[j]-min) / (max-min);", null, 2, null); // 12
			break;
		case 2:
			normalizationvariant.setText("quadratic variant", null, null);
			sc.addCodeLine("for (double j: array) {", null, 1, null); // 11
			sc.addCodeLine("array[j] = Math.pow((array[j])-min) / (max-min), 2);", null, 2, null); // 12
			break;
		case 3:
			normalizationvariant.setText("logarithmic variant", null, null);
			sc.addCodeLine("for (double j: array) {", null, 1, null); // 11
			sc.addCodeLine("array[j] = (Math.log(array[j])-Math.log(min)) / (Math.log(max)-Math.log(min));", null, 2,
					null); // 12
			break;
		default: // Bei fehlerhaftem Input gibt's halt die lineare Variante!
			sc.addCodeLine("for (double j: array) {", null, 1, null); // 11
			sc.addCodeLine("array[j]= (array[j]-min) / (max-min);", null, 2, null); // 12
			break;
		}
		sc.addCodeLine("}", null, 1, null); // 13
		sc.addCodeLine("}", null, 0, null); // 14
		//counter anlegen
		TwoValueCounter counter = lang.newCounter(douArray);
		CounterProperties cp = new CounterProperties();
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
		TwoValueView cv = lang.newCounterView(counter, new Offset(370, 60, douArray, AnimalScript.DIRECTION_SW), cp,
				true, true);
		lang.nextStep("Suche nach Minimum und Maximum");

		// setze min und max auf das erste element des arrays
		sc.highlight(0);
		lang.nextStep();
		double min = douArray.getData(0);
		mintext.setText("min = " + min, null, null);
		sc.unhighlight(0);
		sc.highlight(1);
		lang.nextStep();
		double max = douArray.getData(0);
		maxtext.setText("max = " + max, null, null);
		sc.unhighlight(1);
		sc.highlight(2);
		lang.nextStep();
		sc.unhighlight(2);
		// ArrayMarker anlegen
		ArrayMarker current = lang.newArrayMarker(douArray, 1, "", null, currentP);
		//z�hle mit, wie oft ein neues minimum oder maximum gefunden wurde
		int timesnewminfound = 0;
		int timesnewmaxfound = 0;
		// suche min und max
		for (int i = 1; i < douArray.getLength(); i++) {
			sc.highlight(3);
			douArray.highlightElem(current.getPosition(), null, null);
			sc.highlight(4);
			lang.nextStep();

			if (douArray.getData(i) < min) {
				lang.nextStep();
				sc.unhighlight(4);
				sc.highlight(5);
				min = douArray.getData(i);
				timesnewminfound++;
				mintext.setText("min = " + min, null, null);
				lang.nextStep();

			}
			sc.unhighlight(4);
			sc.unhighlight(5);
			sc.highlight(7);
			lang.nextStep();
			if (douArray.getData(i) > max) {
				lang.nextStep();
				sc.unhighlight(7);
				sc.highlight(8);

				max = douArray.getData(i);
				timesnewmaxfound++;
				maxtext.setText("max = " + max, null, null);
			}
			lang.nextStep();
			sc.unhighlight(7);
			sc.unhighlight(8);
			lang.nextStep();

			douArray.unhighlightElem(current.getPosition(), null, null);
			current.increment(null, null);

		}

		sc.unhighlight(3);
		current.move(0, null, defaultTiming);
		douArray.highlightElem(current.getPosition(), null, null);
		sc.highlight(11);
		lang.nextStep("Normalisierung der Werte");
		sc.highlight(12);

		if (type == 1) { // type=1 bedeutet, die lineare variante wurde gew�hlt
			// Formel mit eingesetzten Werten
			Text formula = lang.newText(new Coordinates(370, 405), null, "eingesetzte werte", null, mmtextP);
			formula.setText("", null, null);

			for (int i = 0; i <= douArray.getLength() - 1; i++) {
				douArray.highlightElem(current.getPosition(), null, null);
				lang.nextStep();

				double temp = douArray.getData(i);
				if (Math.random() < mcprobability) {
					multipleChoice(temp, min, max, type);
				}
				// Berechnung des normalisierten Wertes und Schreiben ins Array
				douArray.put(i, round((temp - min) / (max - min)), null, null);
				// Formel mit eingesetzten Werten
				formula.setText(round((temp - min) / (max - min)) + " = (" + temp + " - " + min + ") / (" + max + " - "
						+ min + ")", null, null);

				douArray.unhighlightElem(current.getPosition(), null, null);
				douArray.highlightCell(current.getPosition(), null, null);
				lang.nextStep();
				current.increment(null, defaultTiming);
			}
		}
		if (type == 2) { // type=2 bedeutet, die quadratische variante wurde gew�hlt
			// Formel mit eingesetzten Werten
			Text formula = lang.newText(new Coordinates(460, 405), null, "eingesetzte werte", null, mmtextP);
			formula.setText("", null, null);

			for (int i = 0; i <= douArray.getLength() - 1; i++) {
				douArray.highlightElem(current.getPosition(), null, null);
				lang.nextStep();

				// Berechnung des normalisierten Wertes und Schreiben ins Array
				double temp = douArray.getData(i);
				if (Math.random() < mcprobability) {
					multipleChoice(temp, min, max, type);
				}
				douArray.put(i, round(Math.pow(((temp - min) / (max - min)), 2)), null, null);
				// Formel mit eingesetzten Werten
				formula.setText(round(Math.pow((temp - min) / (max - min), 2)) + " = ((" + temp + " - " + min + ") / ("
						+ max + " - " + min + "))^2", null, null);

				douArray.unhighlightElem(current.getPosition(), null, null);
				douArray.highlightCell(current.getPosition(), null, null);
				lang.nextStep();
				current.increment(null, defaultTiming);
			}
		}
		if (type == 3) { // type=3 bedeutet, die logarithmische vatriante wurde gew�hlt
			// Formel mit eingesetzten Werten
			Text formula = lang.newText(new Coordinates(550, 425), null, "eingesetzte werte", null, mmtextP);
			formula.setText("", null, null);

			for (int i = 0; i <= douArray.getLength() - 1; i++) {
				douArray.highlightElem(current.getPosition(), null, null);
				lang.nextStep();

				// Berechnung des normalisierten Wertes und Schreiben ins Array
				double temp = douArray.getData(i);
				if (Math.random() < mcprobability) {
					multipleChoice(temp, min, max, type);
				}
				douArray.put(i, round((Math.log(temp) - Math.log(min)) / (Math.log(max) - Math.log(min))), null, null);
				// Formel mit eingesetzten Werten
				formula.setText(round((Math.log(temp) - Math.log(min)) / (Math.log(max) - Math.log(min))) + " = (ln("
						+ temp + ") - ln(" + min + ")) / (ln(" + max + ") - ln(" + min + "))", null, null);
				douArray.unhighlightElem(current.getPosition(), null, null);
				douArray.highlightCell(current.getPosition(), null, null);
				lang.nextStep();
				current.increment(null, defaultTiming);
			}
		}
		lang.nextStep("Outro");
		lang.hideAllPrimitivesExcept(douArray);
		normalizationvariant.show();
		DoubleArray copy = lang.newDoubleArray(new Coordinates(40, 170), array, "array", null, arrayP);
		String concl = "Unten nochmal das urspr\u00fcngliche Array zum Vergleich" + "\nEs gab " + counter.getAccess()
				+ " Zugriffe und " + counter.getAssigments() + " Zuweisungen."
				+ "\nF\u00fcr jedes Element entstehen zwei Zugriffe durch die min- und max-Vergleiche. In diesem Fall waren das "
				+ douArray.getLength() * 2
				+ "\nsolcher Vergleiche. Zus\u004etzlich gibt es einen Zugriff bei einem neu gefundenen Minimum und Maximum. Hier wurden "
				+ timesnewminfound + "\nMale eine neues Minimum und " + timesnewmaxfound
				+ " Male ein neues Maximum gefunden. Schlie\u00dflich entsteht noch ein Zugriff pro"
				+ "\nBerechnung des normalisierten Wertes sowie eine Zuweisung" + "\n   	"
				+ "\nDie logarithmische Min-Max Normalisierung wird au�erdem auch in der Datenvisualisierung verwendet. Hat man"
				+ "\nbeispielsweise sehr viele zweidimensionale, numerische Datens\u004etze in einem Koordinatensystem"
				+ "\nvisualisiert, so kann es zu Overplotting kommen. Durch die Normalisierung (und Reskalierung)"
				+ "\nwerden die Datenpunkte auseinander ger\u00fcckt und die Visualisierung klarer.";

		SourceCode fazit = lang.newSourceCode(new Coordinates(40, 220), "Fazit", null);
		fazit.addMultilineCode(concl, "0", null);
	}

	// Zum Fragen stellen
	public void multipleChoice(double temp, double min, double max, int type) {
		MultipleChoiceQuestionModel mc1 = new MultipleChoiceQuestionModel("mcq1");
		MultipleChoiceQuestionModel mc2 = new MultipleChoiceQuestionModel("mcq2");
		MultipleChoiceQuestionModel mc3 = new MultipleChoiceQuestionModel("mcq3");
		// Auf welchen Wert wird das Minimum des Arrays normalisiert? -> 0
		if (temp == min) {
			mc1.setPrompt("Zu welchem Wert wird dieser Eintrag (ungef�hr) normalisiert?");
			mc1.addAnswer("0", 1,
					"Da es sich bei diesem Wert um das Minimum handelt, wird er zu 0 normalisiert. Richtig.");
			mc1.addAnswer("1", 0,
					"Leider nicht richtig. Da es sich bei diesem Wert um das Minimum handelt, wird er zu 0 normalisiert.");
			mc1.addAnswer("Zwischen 0.2 und 0.5", 0,
					"Leider nicht richtig. Da es sich bei diesem Wert um das Minimum handelt, wird er zu 0 normalisiert.");
			mc1.addAnswer("Zwischen 0.5 und 0.7", 0,
					"Leider nicht richtig. Da es sich bei diesem Wert um das Minimum handelt, wird er zu 0 normalisiert.");
			mc1.setNumberOfTries(1);
			lang.addMCQuestion(mc1);
		}
		// Auf welchen Wert wird das Maximum des Arrays normalisiert? -> 1
		if (temp == max) {
			mc2.setPrompt("Zu welchem Wert wird dieser Eintrag (ungef�hr) normalisiert?");
			mc2.addAnswer("Zwischen 0.3 und 0.6", 0,
					"Leider nicht richtig. Da es sich bei diesem Wert um das Maximum handelt, wird er zu 1 normalisiert.");
			mc2.addAnswer("Zwischen 0.6 und 0.9", 0,
					"Leider nicht richtig. Da es sich bei diesem Wert um das Maximum handelt, wird er zu 1 normalisiert.");
			mc2.addAnswer("1", 1,
					"Da es sich bei diesem Wert um das Maximum handelt, wird er zu 1 normalisiert. Richtig.");
			mc2.addAnswer("0", 0,
					"Leider nicht richtig. Da es sich bei diesem Wert um das Maximum handelt, wird er zu 1 normalisiert.");
			mc2.setNumberOfTries(1);
			lang.addMCQuestion(mc2);
		}

		// Auf welchen Wert wird ein Eintrag der etwa in der Mitte zwischen Minimum und
		// Maximum liegt normalisiert? Diese Frage wird nur bei der linearen
		// Variante gestellt, weil bei den anderen schwer absch�tzbar
		if (round((temp - min) / (max - min)) > 0.4 && round((temp - min) / (max - min)) < 0.6 && type == 1) {
			mc3.setPrompt("Zu welchem Wert wird dieser Eintrag (ungef�hr) normalisiert?");
			mc3.addAnswer("Etwa 0.5", 1,
					"Da sich dieser Eintrag in etwa in der Mitte von Minimum und Maximum befindet wird er auf etwa 0.5 normalisiert. Richtig.");
			mc3.addAnswer("Etwa 0.8", 0, "Leider nicht richtig. Beachte das Minimum und Maximum und sch�tze ab.");
			mc3.addAnswer("1", 0, "Leider nicht richtig. Beachte das Minimum und Maximum und sch�tze ab.");
			mc3.addAnswer("0", 0, "Leider nicht richtig. Beachte das Minimum und Maximum und sch�tze ab.");
			mc3.setNumberOfTries(1);
			lang.addMCQuestion(mc3);
		}
		lang.nextStep();
	}
	//hilfsmethode zum runden
	public static double round(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(4, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		try {
			type = (Integer) primitives.get("int");
			if (type < 1 || type > 3) { // es gibt nur die methoden 1 (linear), 2 (quadratisch), 3 (logarithmisch)
				throw new IllegalArgumentException("Der Integer muss 1 (lineare Normalisierung), 2 (quadratisch) oder 3 (logarithmisch) sein.");
			}
			intArray = (int[]) primitives.get("intArray");
			for (int i = 0; i < intArray.length; i++) {
				// wenn die logarithmische variante gew�hlt wurde, darf kein array-element 0 sein, da log(0) nicht berechenbar ist
				if (type == 3 && intArray[i] == 0) {
 					throw new IllegalArgumentException("Wenn die logarithmische Normalisierung gew�hlt wurde, kann kein Eintrag des Arrays Null sein, da log(0) nicht berechenbar ist.");
				}
			}
			if (intArray.length < 3) { // es macht keinen sinn ein array mit weniger als 3 eintr�gen zu normalisieren
				throw new IllegalArgumentException("Das Array sollte l�nger als 2 sein, damit es �berhaupt Sinn macht, es zu normalisieren.");
			}
			mcprobability = (double) primitives.get("mcprobability");
			// die wahrscheinlichkeit muss sinngem�� zwischen 0 und 1 liegen. 
			// es macht wegen der art der implementierung eigentlich keinen unterschied aber was soll's
			if (mcprobability < 0 || mcprobability > 1) {
				throw new IllegalArgumentException("Da es sich hier um eine Wahrscheinlichkeit handelt, muss der Wert zwischen 0 und 1 liegen.");
			}
			// sortiere das array, wenn erster (min) und letzter (max) wert
			// gleich sind, steht im nenner max-min=0 -> division durch 0
			Arrays.sort(intArray);
			if (intArray[0] == intArray[intArray.length - 1]) {
				throw new IllegalArgumentException("Ein Array, das nur aus Eintr�gen mit dem selben Wert bestehen, kann nicht normalisiert werden.");
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}