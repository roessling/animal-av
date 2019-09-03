/*
 * Petrinetze.java
 * Nadja Geisler,Jan Fischer, 2014 for the Animal project at TU Darmstadt. 
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.Circle;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * @author Nadja Geisler (nadja.geisler@stud.tu-darmstadt.de)
 * @author Jan Fischer (fischer@stud.tu-darmstadt.de)
 * @version 1.1
 * @since 2014
 */
public class Petrinetze implements ValidatingGenerator {

	private Language lang;

	private int[][] incidenceMatrix;
	private int[] placeCapacities;
	private int[][] currentTokens;
	private int numberMaxSteps;

	private int numberOfPlaces;
	private int numberOfTransitions;

	private ArrayProperties arrayProp;
	private CircleProperties placeProp;
	private SourceCodeProperties sourceCode;
	private RectProperties transitionProp;
	private PolylineProperties lineProp;
	private CircleProperties tokenProp;
	private CircleProperties usedTokenProp;
	private RectProperties transitionUsedProp;
	private TextProperties textProp;
	private RectProperties rectProp;

	private Text header;
	private Rect hRect;
	private SourceCode pseudoCode;
	private Circle[] tokens;
	private Rect[] transitions;
	private Text[] tokenText;
	private Text[] statesText;
	private ArrayList<IntArray> states;
	private TwoValueCounter counter;

	public void init() {
		lang = new AnimalScript("Petrinetze", "Nadja Geisler,Jan Fischer",
				1600, 900);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		incidenceMatrix = (int[][]) primitives.get("incidenceMatrix");
		placeCapacities = (int[]) primitives.get("placeCapacities");
		int[] tokens = (int[]) primitives.get("startTokens");
		currentTokens = new int[2][incidenceMatrix.length];
		for (int i = 0; i < incidenceMatrix.length; i++)
			currentTokens[0][i] = tokens[i];

		numberMaxSteps = (int) primitives.get("numberMaxSteps");

		numberOfTransitions = incidenceMatrix[0].length;
		numberOfPlaces = incidenceMatrix.length;

		arrayProp = (ArrayProperties) props.getPropertiesByName("arrays");
		placeProp = (CircleProperties) props.getPropertiesByName("placeProp");
		sourceCode = (SourceCodeProperties) props
				.getPropertiesByName("sourceCode");
		transitionProp = (RectProperties) props
				.getPropertiesByName("transitionProp");
		lineProp = (PolylineProperties) props.getPropertiesByName("linesProp");
		tokenProp = (CircleProperties) props.getPropertiesByName("tokenProp");
		transitionUsedProp = (RectProperties) props
				.getPropertiesByName("transitionUsedProp");
		usedTokenProp = (CircleProperties) props
				.getPropertiesByName("usedTokenProp");
		textProp = (TextProperties) props.getPropertiesByName("text");
		rectProp = (RectProperties) props.getPropertiesByName("rects");

		petrinet();

		return lang.toString();
	}

	public String getName() {
		return "Petrinetze";
	}

	public String getAlgorithmName() {
		return "Petrinetze";
	}

	public String getAnimationAuthor() {
		return "Nadja Geisler,Jan Fischer";
	}

	public String getDescription() {
		return "Dieser Algorithmus veranschaulicht die Funktionsweise von Petrinetzen."
				+ "\n"
				+ "Ein Petrinetz ist ein Graph mit zwei verschiedenen Arten von Knoten: Pl&auml;tze und Transitionen. Diese Knoten werden durch gerichtete Kanten verbunden. Kanten verbinden immer einen Platz mit einer Transition oder umgekehrt."
				+ "\n"
				+ "Die Pl&auml;tze k&ouml;nnen jeweils mit einer beliebigen Anzahl an Markierungen belegt sein."
				+ "\n"
				+ "Die Transitionen k&ouml;nnen schalten und das Petrinetz dadurch in einen neuen Zustand bringen."
				+ "\n"
				+ "Schalten bedeutet, dass auf jedem Platz, von dem eine Kante zu einer bestimmten Transition f&uuml;hrt, mindestens eine Markierung liegen muss."
				+ "\n"
				+ "Von diesen Pl&auml;tzen wird eine Markierung entfernt und es wird jeweils eine Markierung auf alle Pl&auml;tze gelegt, zu denen eine Kante von der Transition aus f&uuml;hrt."
				+ "\n"
				+ "Entsprechend kann eine Transition nur schalten, wenn in jedem eingehenden Platz mindestens eine Markierung ist und in keinem ausgehenden Platz die Maximalanzahl der Markierungen erreicht ist.";
	}

	public ArrayList<String> getDescriptionUTF8() {

		ArrayList<String> result = new ArrayList<String>();

		result.add("");
		result.add("Dieser Algorithmus veranschaulicht die Funktionsweise von Petrinetzen.");
		result.add("Ein Petrinetz ist ein Graph mit zwei verschiedenen Arten von Knoten:");
		result.add("Plätze und Transitionen. Diese Knoten werden durch gerichtete Kanten");
		result.add("verbunden. Kanten verbinden immer einen Platz mit einer Transition");
		result.add("oder umgekehrt.");
		result.add("Die Plätze können jeweils mit einer beliebigen Anzahl an Markierungen");
		result.add("belegt sein.");
		result.add("Die Transitionen können schalten und das Petrinetz dadurch in einen");
		result.add("neuen Zustand bringen.");
		result.add("Schalten bedeutet, dass auf jedem Platz, von dem eine Kante zu einer");
		result.add("bestimmten Transition führt, mindestens eine Markierung liegen muss.");
		result.add("Von diesen Plätzen wird eine Markierung entfernt und es wird jeweils");
		result.add("eine Markierung auf alle Plätze gelegt, zu denen eine Kante von der");
		result.add("Transition aus führt.");
		result.add("Entsprechend kann eine Transition nur schalten, wenn in jedem eingehenden");
		result.add("Platz mindestens eine Markierung ist und in keinem ausgehenden Platz die");
		result.add("Maximalanzahl der Markierungen erreicht ist.");

		return result;
	}

	public String getCodeExample() {
		return "solange es eine Transition gibt, die feuern kann"
				+ "\n \n"
				+ "    für jede Transition t"
				+ "\n \n"
				+ "        falls t feuern kann"
				+ "\n \n"
				+ "            verringere Markierungen in eingehenden Pl&auml;tzen von t um 1"
				+ "\n \n"
				+ "            erh&ouml;he Markierungen in ausgehenden Pl&auml;tzen von t um 1"
				+ "\n \n";
	}

	public String getCodeExampleUTF8() {
		return "solange es eine Transition gibt, die feuern kann"
				+ "\n \n"
				+ "        für jede Transition t"
				+ "\n \n"
				+ "                falls t feuern kann"
				+ "\n \n"
				+ "                        verringere Markierungen in eingehenden Plätzen von t um 1"
				+ "\n \n"
				+ "                        erhöhe Markierungen in ausgehenden Plätzen von t um 1"
				+ "\n \n";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_NETWORK);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {

		int[][] incidenceMatrix = (int[][]) arg1.get("incidenceMatrix");
		int[] currentTokens = (int[]) arg1.get("startTokens");
		int[] placeCapacities = (int[]) arg1.get("placeCapacities");
		int numberMaxSteps = (int) arg1.get("numberMaxSteps");

		boolean valid = true;

		if (!(numberMaxSteps > 0)) {
			JOptionPane.showMessageDialog(null,
					"Die maximale Zahl an Iterationen muss mindestens 1 sein!",
					"ERROR", JOptionPane.ERROR_MESSAGE);
			valid = false;
		}
		if (incidenceMatrix.length != currentTokens.length) {
			JOptionPane
					.showMessageDialog(
							null,
							"Es gibt nicht genau so viele Anzahlen für Startmarkierungen wie Plätze!",
							"ERROR", JOptionPane.ERROR_MESSAGE);
			valid = false;
		}
		if (incidenceMatrix.length != placeCapacities.length) {
			JOptionPane.showMessageDialog(null,
					"Es gibt nicht genau so viele Kapazitäten wie Plätze!",
					"ERROR", JOptionPane.ERROR_MESSAGE);
			valid = false;
		}

		for (int i = 0; i < currentTokens.length; i++) {
			if (currentTokens[i] < 0) {
				JOptionPane
						.showMessageDialog(
								null,
								"Die Anzahl von Startmarkierungen darf nicht negativ sein!",
								"ERROR", JOptionPane.ERROR_MESSAGE);
				valid = false;
				break;
			}
		}

		return valid;
	}

	// -------------------------------------------------------------------------
	// Ablauf der Animation
	// -------------------------------------------------------------------------
	private void petrinet() {

		introduction();

		drawPetrinet();

		introduceCounter();

		fireTransitions();

		conclusion();
	}

	// -------------------------------------------------------------------------
	// Anzeige von Überschrift, Beschreibung und Pseudocode
	// -------------------------------------------------------------------------
	private void introduction() {
		// Überschrift mit Rechteck
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) textProp
				.get(AnimationPropertiesKeys.FONT_PROPERTY))
				.deriveFont((float) 24));
		headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				textProp.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		header = lang.newText(new Coordinates(20, 30), "Petrinetze", "header",
				null, headerProps);

		hRect = lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"),
				"hRect", null, rectProp);

		lang.nextStep("Beschreibung des Algorithmus");

		// Beschreibung des Algorithms
		ArrayList<Text> description = new ArrayList<Text>();
		ArrayList<String> strings = getDescriptionUTF8();

		for (int i = 0; i < strings.size(); i++)
			description.add(lang.newText(new Offset(0, 10 + i * 15, hRect,
					AnimalScript.DIRECTION_SW), strings.get(i), "description"
					+ i, null, textProp));

		lang.nextStep("Beispielcode des Algorithmus");
		for (Text t : description)
			t.hide();

		// Pseudocode des Algorithmus
		pseudoCode = lang.newSourceCode(new Coordinates(20, 80), "srcCode",
				null, sourceCode);
		pseudoCode.addMultilineCode(getCodeExampleUTF8(), "srcCode", null);

		lang.nextStep();

		pseudoCode.moveBy(null, 100
				+ (75 + numberOfTransitions / (numberOfPlaces - 1) * 30)
				* (numberOfPlaces - 1), 0, Timing.INSTANTEOUS, new TicksTiming(
				500));

		lang.nextStep("Das Petrinetz");

	}

	// -------------------------------------------------------------------------
	// das eingebene Petrinetz visualisieren
	// -------------------------------------------------------------------------
	private void drawPetrinet() {

		Circle[] places = new Circle[numberOfPlaces];
		tokens = new Circle[numberOfPlaces];
		transitions = new Rect[numberOfTransitions];
		tokenText = new Text[numberOfPlaces];

		// Plätze mit Markierungen
		for (int i = 0; i < numberOfPlaces; i++) {

			Offset o = new Offset(50 + i
					* (75 + numberOfTransitions / (numberOfPlaces - 1) * 30),
					100, hRect, AnimalScript.DIRECTION_SW);

			places[i] = lang.newCircle(o, 22, "place" + i + 1, null, placeProp);

			tokens[i] = lang.newCircle(o, 17, "tok" + i + 1, null, tokenProp);

			Offset oText = new Offset(-10, -8, places[i],
					AnimalScript.DIRECTION_C);

			if (currentTokens[0][i] > placeCapacities[i])
				currentTokens[0][i] = placeCapacities[i];
			tokenText[i] = lang.newText(oText, "  " + currentTokens[0][i],
					"tokens" + i + 1, null, textProp);

			o = new Offset(0, -20, places[i], AnimalScript.DIRECTION_NW);
			lang.newText(o, "P" + (i + 1), "text" + i + 1, null, textProp);

		}

		lang.nextStep();

		// Transitionen
		for (int i = 0; i < numberOfTransitions; i++) {

			int width = (75 + numberOfTransitions / (numberOfPlaces - 1) * 30)
					* (numberOfPlaces - 1) / (numberOfTransitions - 1);

			Offset nw = new Offset(50 + width * i, 200, hRect,
					AnimalScript.DIRECTION_SW);
			Offset se = new Offset(60 + width * i, 240, hRect,
					AnimalScript.DIRECTION_SW);

			transitions[i] = lang.newRect(nw, se, "trans" + i + 1, null,
					transitionProp);

			Offset o = new Offset(0, 5, transitions[i],
					AnimalScript.DIRECTION_SW);
			lang.newText(o, "t" + (i + 1), "text" + i + 1, null, textProp);
		}

		lang.nextStep();

		// Übergänge zwischen Plätzen und Transitionen
		for (int i = 0; i < numberOfPlaces; i++) {
			for (int j = 0; j < numberOfTransitions; j++) {

				if (incidenceMatrix[i][j] > 0) {
					Node[] n = {
							new Offset(0, 0, transitions[j],
									AnimalScript.DIRECTION_E),
							new Offset(0, 0, places[i],
									AnimalScript.DIRECTION_W) };
					lang.newPolyline(n, "arc" + (j + i), null, lineProp);
				} else if (incidenceMatrix[i][j] < 0) {
					Node[] n = {
							new Offset(0, 0, places[i],
									AnimalScript.DIRECTION_E),
							new Offset(0, 0, transitions[j],
									AnimalScript.DIRECTION_W) };
					lang.newPolyline(n, "arc" + (j + i), null, lineProp);
				}
			}
		}
	}

	// -------------------------------------------------------------------------
	// Counter für Markierungen erstellen
	// -------------------------------------------------------------------------
	private void introduceCounter() {
		// Counter
		IntArray counterArray = lang.newIntArray(new Coordinates(0, 0),
				currentTokens[0], "counterArray", null);
		counterArray.hide();

		Text counterText1 = lang.newText(new Offset(0, 25, pseudoCode,
				AnimalScript.DIRECTION_SW), "verwendete Markierungen:",
				"counterText1", null, textProp);
		lang.newText(new Offset(0, 45, pseudoCode, AnimalScript.DIRECTION_SW),
				"entstandene Markierungen:", "counterText2", null, textProp);

		counter = lang.newCounter(counterArray);

		CounterProperties cp = new CounterProperties();

		TwoValueView view = lang.newCounterView(counter, new Offset(-50, 0,
				counterText1, AnimalScript.DIRECTION_NE), cp, true, true);
		view.hideText();

		lang.nextStep("Feuern der Transitionen");
	}

	// -------------------------------------------------------------------------
	// mögliche Transitionen ausführen (max bis Iterationslimit erreict)
	// -------------------------------------------------------------------------
	private void fireTransitions() {

		boolean lastRunNoFire = false;

		states = new ArrayList<IntArray>();
		statesText = new Text[numberMaxSteps];

		// Iterationen
		for (int i = 0; (i < numberMaxSteps) && !lastRunNoFire; i++) {

			pseudoCode.highlight(0);

			lang.nextStep((i + 1) + ". Iteration");

			pseudoCode.unhighlight(0);

			if (i > 0)
				states.get(i - 1).unhighlightCell(0,
						states.get(i - 1).getLength() - 1, Timing.INSTANTEOUS,
						new TicksTiming(50));

			lastRunNoFire = true;

			// einzelne Transitionen
			for (int tr = 0; tr < numberOfTransitions; tr++) {

				boolean canfire = true;

				for (int pl = 0; pl < numberOfPlaces; pl++) {

					if (incidenceMatrix[pl][tr] < 0) {
						int sub = 0;
						if (currentTokens[1][pl] < 0)
							sub += currentTokens[1][pl];
						if (!(currentTokens[0][pl] + sub + (-1) >= 0))
							canfire = false;
					} else if (incidenceMatrix[pl][tr] > 0) {
						int add = 0;
						if (currentTokens[1][pl] > 0)
							add += currentTokens[1][pl];
						if (!(currentTokens[0][pl] + add + 1 <= placeCapacities[pl]))
							canfire = false;
					}
				}

				pseudoCode.highlight(2);
				transitions[tr] = setTransitionProps(transitions[tr],
						transitionUsedProp);
				lang.nextStep();

				pseudoCode.unhighlight(2);
				pseudoCode.highlight(4);

				// Transition pseudofeuern
				if (canfire) {

					lang.nextStep();

					pseudoCode.highlight(6);
					pseudoCode.highlight(8);

					for (int pl = 0; pl < numberOfPlaces; pl++) {

						if (incidenceMatrix[pl][tr] < 0) {
							counter.assignmentsInc(1);
							tokens[pl] = setCircleProps(tokens[pl],
									usedTokenProp);
							currentTokens[1][pl] += -1;
						}
						if (incidenceMatrix[pl][tr] > 0) {
							counter.accessInc(1);
							tokens[pl] = setCircleProps(tokens[pl],
									usedTokenProp);
							currentTokens[1][pl] += 1;
						}
					}

					lastRunNoFire = false;
					updateNet();

					lang.nextStep();

					pseudoCode.unhighlight(8);
					pseudoCode.unhighlight(6);

					for (int pl = 0; pl < numberOfPlaces; pl++) {
						if (incidenceMatrix[pl][tr] != 0)
							tokens[pl] = setCircleProps(tokens[pl], tokenProp);

					}

				}

				transitions[tr] = setTransitionProps(transitions[tr],
						transitionProp);

				pseudoCode.unhighlight(4);
			}

			lang.nextStep();

			for (int j = 0; j < currentTokens[0].length; j++) {
				currentTokens[0][j] += currentTokens[1][j];
				currentTokens[1][j] = 0;
			}
			updateNet();

			lang.nextStep();

			// Übersicht des Ergebnisses der Transition anzeigen
			statesText[i] = lang.newText(new Offset(-50 + (i / 5)
					* (85 + numberOfPlaces * 15), 50 + 50 * (i % 5),
					transitions[0], AnimalScript.DIRECTION_SW), i
					+ ". Iteration: ", "itText" + i, null, textProp);
			states.add(lang.newIntArray(new Offset(10, 0, statesText[i],
					AnimalScript.DIRECTION_NE), currentTokens[0], "state" + i,
					null, arrayProp));
			states.get(i).highlightCell(0, states.get(i).getLength() - 1,
					Timing.INSTANTEOUS, new TicksTiming(50));

		}

		lang.nextStep("Zusammenfassung");
		pseudoCode.unhighlight(0);

	}

	// -------------------------------------------------------------------------
	// Kreis durch einen anderen mit anderen Properties übermalen
	// -------------------------------------------------------------------------
	private Circle setCircleProps(Circle c, CircleProperties props) {

		return lang.newCircle(c.getCenter(), c.getRadius(), c.getName() + "n",
				null, props);
	}

	// -------------------------------------------------------------------------
	// Rechteck durch ein anderes mit anderen Properties übermalen
	// -------------------------------------------------------------------------
	private Rect setTransitionProps(Rect r, RectProperties props) {

		return lang.newRect(new Offset(0, 0, r, AnimalScript.DIRECTION_NW),
				new Offset(0, 0, r, AnimalScript.DIRECTION_SE), r.getName()
						+ "n", null, props);
	}

	// -------------------------------------------------------------------------
	// Markierungsanzahlen in der Visualisierung aktualisieren
	// -------------------------------------------------------------------------
	private void updateNet() {

		for (int i = 0; i < numberOfPlaces; i++) {
			if (currentTokens[1][i] == 0)
				tokenText[i].setText("  " + currentTokens[0][i],
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			else if (currentTokens[1][i] < 0)
				tokenText[i].setText(
						"" + currentTokens[0][i] + "-"
								+ Math.abs(currentTokens[1][i]),
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			else
				tokenText[i].setText("" + currentTokens[0][i] + "+"
						+ currentTokens[1][i], Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);
		}
	}

	// -------------------------------------------------------------------------
	// Zusammenfassung anzeigen
	// -------------------------------------------------------------------------
	private void conclusion() {

		lang.hideAllPrimitives();
		header.show();
		hRect.show();

		ArrayList<String> strings = new ArrayList<String>();
		ArrayList<Text> conclusion = new ArrayList<Text>();

		strings.add("");
		strings.add("Es wurden " + states.size() + " Iterationen ausgeführt.");
		strings.add("");
		strings.add("Dabei wurden von den Transitionen "
				+ counter.getAssigments() + " Markierungen verwendet.");
		strings.add("und " + counter.getAccess()
				+ " Markierungen sind entstanden.");
		strings.add("");
		if (states.size() == numberMaxSteps)
			strings.add("Der Algorithmus wurde beendet, da die Maximalzahl an Iterationen erreicht wurde");
		else
			strings.add("Der Algorithmus wurde beendet, weil keine Transition mehr feuern kann.");
		strings.add("");
		strings.add("");
		strings.add("");

		for (int i = 0; i < strings.size(); i++)
			conclusion.add(lang.newText(new Offset(0, 10 + i * 15, hRect,
					AnimalScript.DIRECTION_SW), strings.get(i), "conclusion"
					+ i + i, null, textProp));

		lang.nextStep("Iterationsübersicht");
		// Iterationsergebnisse wieder anzeigen
		for (IntArray a : states)
			a.show(new TicksTiming(100));
		for (Text t : statesText)
			if (t != null)
				t.show(new TicksTiming(100));

		lang.nextStep();

		for (Text t : conclusion)
			t.moveBy(null, 100
					+ (75 + numberOfTransitions / (numberOfPlaces - 1) * 30)
					* (numberOfPlaces - 1), 0, Timing.INSTANTEOUS,
					new TicksTiming(500));

		lang.nextStep();

		drawPetrinet();
	}
}