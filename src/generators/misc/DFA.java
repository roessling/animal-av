/*
 * DFA.java
 * Nadja Geisler,Jan Fischer, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
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
 * @version 1.4
 * @since 2014
 */

public class DFA implements ValidatingGenerator {

	private Language lang;

	private SourceCodeProperties code;
	private ArrayProperties arrays;
	private GraphProperties graphs;
	private GraphProperties highlight1;
	private GraphProperties highlight2;
	private TextProperties texts;
	private RectProperties rects;

	private int[][] transitions1;
	private int startState1;
	private int[] acceptingStates1;
	private int[][] transitions2;
	private int startState2;
	private int[] acceptingStates2;

	private Graph automaton1;
	private Graph automaton2;
	private ArrayList<Integer> acceptingIndices1;
	private ArrayList<Integer> acceptingIndices2;
	private Graph startA1;
	private Graph startA2;
	private Graph accA1;
	private Graph accA2;

	private int[] alphabet;
	private Graph newAutomaton;
	private int newStartIndex;
	private ArrayList<Integer> newAcceptingIndices;
	private int newAutomatonSize;
	private Graph startHighlight;
	private Graph accHighlight;

	private Text header;
	private Rect headerRect;
	private SourceCode srcCode;
	private Text legend;
	private IntArray alphabetArray;

	public void init() {
		lang = new AnimalScript("Schnittmengenautomat zweier DFA",
				"Nadja Geisler,Jan Fischer", 1600, 900);

		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		code = (SourceCodeProperties) props.getPropertiesByName("code");
		arrays = (ArrayProperties) props.getPropertiesByName("arrays");
		graphs = (GraphProperties) props.getPropertiesByName("graphs");
		highlight1 = (GraphProperties) props.getPropertiesByName("highlight1");
		highlight2 = (GraphProperties) props.getPropertiesByName("highlight2");
		texts = (TextProperties) props.getPropertiesByName("text");
		rects = (RectProperties) props.getPropertiesByName("rects");

		transitions1 = (int[][]) primitives.get("transitions1");
		startState1 = (int) primitives.get("startState1");
		acceptingStates1 = (int[]) primitives.get("acceptingStates1");
		transitions2 = (int[][]) primitives.get("transitions2");
		startState2 = (int) primitives.get("startState2");
		acceptingStates2 = (int[]) primitives.get("acceptingStates2");

		intersect();

		lang.finalizeGeneration();

		return lang.toString();
	}

	public String getName() {
		return "Schnittmengenautomat zweier DFA";
	}

	public String getAlgorithmName() {
		return "Schnittmengenautomat zweier DFA";
	}

	public String getAnimationAuthor() {
		return "Nadja Geisler,Jan Fischer";
	}

	public String getDescription() {

		return "Dieser Algorithmus dient dazu, auf Basis zweier deterministischer, endlicher Zustandsautomaten(DFA)"
				+ "\n"
				+ "einen neuen DFA zu erstellen, der die Schnittmenge der beiden akzeptierten Sprachen der Ausgangsautomaten akzeptiert."
				+ "\n \n"
				+ "Dazu muss das zugrundeliegende Alphabet der beiden Ausgangsautomaten das gleiche sein, auf dem dann"
				+ "\n"
				+ "auch der neue Automat beruht."
				+ "\n \n"
				+ "Die Zustandsmenge ergibt sich aus dem Kreuzprodukt der beiden Zustandsmengen, der Startzustand entsteht aus der Kombination der beiden Startzust&auml;nde, die Menge der akzeptierenden Zust&auml;nde ebenfalls"
				+ "\n"
				+ "aus dem Kreuzprodukt der beiden Mengen akzeptierender Zust&auml;nde."
				+ "\n \n"
				+ "Die Zustands&uuml;bergangsfunktion entsteht durch Kombination der beiden Zustands&uuml;bergangsfunktionen anhand der Zust&auml;nde, die durch das Kreuzprodukt entstanden sind.";

	}

	public ArrayList<String> getDescriptionUTF8() {

		ArrayList<String> result = new ArrayList<String>();
		result.add("");
		result.add("Dieser Algorithmus dient dazu, auf Basis zweier deterministischer, endlicher Zustandsautomaten(DFA)");
		result.add("einen neuen DFA zu erstellen, der die Schnittmenge der beiden akzeptierten Sprachen der Ausgangs-");
		result.add("automaten akzeptiert.");
		result.add("");
		result.add("Dazu muss das zugrundeliegende Alphabet der beiden Ausgangsautomaten das gleiche sein, auf dem dann");
		result.add("auch der neue Automat beruht.");
		result.add("");
		result.add("Die Zustandsmenge ergibt sich aus dem Kreuzprodukt der beiden Zustandsmengen, der Startzustand ent-");
		result.add("steht aus der Kombination der beiden Startzustände, die Menge der akzeptierenden Zustände ebenfalls");
		result.add("aus dem Kreuzprodukt der beiden Mengen akzeptierender Zustände.");
		result.add("");
		result.add("Die Zustandsübergangsfunktion entsteht durch Kombination der beiden Zustandsübergangsfunktionen an-");
		result.add("hand der Zustände, die durch das Kreuzprodukt entstanden sind.");

		return result;

	}

	public String getCodeExample() {
		return "f&uuml;r jeden Zustand s1 des Automaten 1"
				+ "\n"
				+ "        f&uuml;r jeden Zustand s2 des Automaten 2"
				+ "\n"
				+ "                erstelle einen neuen Zustand sn"
				+ "\n"
				+ "                falls s1 und s2 beide akzeptierende Zust&auml;nde sind"
				+ "\n"
				+ "                        mache sn zu einem akzeptierenden Zustand"
				+ "\n"
				+ "                falls s1 und s2 beide Startzust&auml;nde sind"
				+ "\n"
				+ "                        mache sn zu einem Startzustand"
				+ "\n"
				+ "f&uuml;r jeden neuen Zustand sn"
				+ "\n"
				+ "        f&uuml;r jedes Element des Alphabets"
				+ "\n"
				+ "                erstelle einen entsprechenden Zustands&uuml;bergang"
				+ "\n";
	}

	public String getCodeExampleUTF8() {
		return "für jeden Zustand x1 des Automaten 1"
				+ "\n"
				+ "        für jeden Zustande x2 des Automaten 2"
				+ "\n"
				+ "                erstelle einen neuen Zustand sn"
				+ "\n"
				+ "                falls x1 und x2 beide akzeptierende Zustände sind"
				+ "\n"
				+ "                        mache sn zu einem akzeptierenden Zustand"
				+ "\n"
				+ "                falls x1 und x2 beide Startzustände sind"
				+ "\n"
				+ "                        mache sn zu einem Startzustand"
				+ "\n"
				+ "für jeden neuen Zustand xn"
				+ "\n"
				+ "        für jedes Element des Alphabets"
				+ "\n"
				+ "                erstelle einen entsprechenden Zustandsübergang"
				+ "\n";
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
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {

		int[][] transitions1 = (int[][]) arg1.get("transitions1");
		int startState1 = (int) arg1.get("startState1");
		int[] acceptingStates1 = (int[]) arg1.get("acceptingStates1");
		int[][] transitions2 = (int[][]) arg1.get("transitions2");
		int startState2 = (int) arg1.get("startState2");
		int[] acceptingStates2 = (int[]) arg1.get("acceptingStates2");

		boolean valid = true;

		if (transitions1.length != transitions1[0].length
				|| transitions2.length != transitions2[0].length) {
			JOptionPane.showMessageDialog(null,
					"Eine Zustandsübergangsfunktion muss quadratisch sein!",
					"ERROR", JOptionPane.ERROR_MESSAGE);
			valid = false;
		}

		if (!(startState1 < transitions1.length)
				|| !(startState2 < transitions2.length)) {
			JOptionPane
					.showMessageDialog(
							null,
							"Der Startzustand muss ein existierender Zustand, d.h. kleiner als die Zeilenzahl der Zustandsübergangsfunktion, sein!",
							"ERROR", JOptionPane.ERROR_MESSAGE);
			valid = false;
		}

		if (startState1 < 0 || startState2 < 0) {
			JOptionPane
					.showMessageDialog(
							null,
							"Der Startzustand muss ein existierender Zustand, d.h. positiv, sein!",
							"ERROR", JOptionPane.ERROR_MESSAGE);
			valid = false;
		}

		for (int i : acceptingStates1) {
			if (!(i < transitions1.length)) {
				JOptionPane
						.showMessageDialog(
								null,
								"Die akzeptierenden Zustände müssen existierende Zustände, d.h. kleiner als die Zeilenzahl der Zustandsübergangsfunktion, sein!",
								"ERROR", JOptionPane.ERROR_MESSAGE);
				valid = false;
			}
		}

		for (int i : acceptingStates2) {
			if (!(i < transitions2.length)) {
				JOptionPane
						.showMessageDialog(
								null,
								"Die akzeptierenden Zustände müssen existierende Zustände, d.h. kleiner als die Zeilenzahl der Zustandsübergangsfunktion, sein!",
								"ERROR", JOptionPane.ERROR_MESSAGE);
				valid = false;
			}
		}

		for (int i : acceptingStates1) {
			if (i < 0) {
				JOptionPane
						.showMessageDialog(
								null,
								"Die akzeptierenden Zustände müssen existierende Zustände, d.h. positiv, sein!",
								"ERROR", JOptionPane.ERROR_MESSAGE);
				valid = false;
			}
		}

		for (int i : acceptingStates2) {
			if (i < 0) {
				JOptionPane
						.showMessageDialog(
								null,
								"Die akzeptierenden Zustände müssen existierende Zustände, d.h. positiv, sein!",
								"ERROR", JOptionPane.ERROR_MESSAGE);
				valid = false;
			}
		}

		boolean question = false;

		for (int i = 0; i < transitions1.length && !question; i++) {
			for (int j = 0; j < transitions1[0].length && !question; j++) {
				if (transitions1[i][j] == 0) {
					question = true;
					int answer = JOptionPane
							.showConfirmDialog(
									null,
									"Eine Null in der Zustandsübergangsfunktion entspricht keinem Übergang. Fortfahren?",
									"Unexpected Input.",
									JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.NO_OPTION)
						valid = false;
				}
			}
		}

		for (int i = 0; i < transitions2.length && !question; i++) {
			for (int j = 0; j < transitions2[0].length && !question; j++) {
				if (transitions2[i][j] == 0) {
					question = true;
					int answer = JOptionPane
							.showConfirmDialog(
									null,
									"Eine Null in der Zustandsübergangsfunktion entspricht keinem Übergang. Fortfahren?",
									"Unexpected Input.",
									JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.NO_OPTION)
						valid = false;

				}
			}
		}

		return valid;
	}

	// -------------------------------------------------------------------------
	// Ablauf der Animation
	// -------------------------------------------------------------------------
	private void intersect() {

		preprocessData();

		introduction();

		showAutomata();

		// Gerüst für den neuen Automaten erstellen und positionieren
		newAutomaton = createNewAutomaton(graphs, "newAutomaton");
		newAutomaton.moveTo(null, null, new Offset(0, 25, headerRect,
				AnimalScript.DIRECTION_SW), Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		newAutomaton.moveBy(null, 900, 0, Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);

		// Startzustand und akzeptierende Zustände des neuen Automaten markieren
		startHighlight = createNewAutomaton(highlight2, "hStartAutomaton");
		startHighlight.moveTo(null, null, new Offset(0, 25, headerRect,
				AnimalScript.DIRECTION_SW), Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		startHighlight.moveBy(null, 900, 0, Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		accHighlight = createNewAutomaton(highlight1, "hAccAutomaton");
		accHighlight.moveTo(null, null, new Offset(0, 25, headerRect,
				AnimalScript.DIRECTION_SW), Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		accHighlight.moveBy(null, 900, 0, Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);

		showStateCreation();

		showAlphabetArray();

		showTransitionCreation();

		conclusion();
	}

	// -------------------------------------------------------------------------
	// Eingaben des Users in internes Format überführen
	// -------------------------------------------------------------------------
	private void preprocessData() {

		// Transitionstabellen in Graphen überführen und Markierung für Zustände
		automaton1 = setGraph(transitions1, "automaton1", graphs, false);
		automaton2 = setGraph(transitions2, "automaton2", graphs, false);
		startA1 = setGraph(transitions1, "startA1", highlight2, true);
		startA2 = setGraph(transitions2, "startA2", highlight2, true);
		accA1 = setGraph(transitions1, "accA1", highlight1, true);
		accA2 = setGraph(transitions2, "accA2", highlight1, true);

		acceptingIndices1 = new ArrayList<Integer>();
		acceptingIndices2 = new ArrayList<Integer>();

		// akzeptierende Zustände in ArrayLists sammeln
		for (int i = 0; i < acceptingStates1.length; i++) {
			acceptingIndices1.add(acceptingStates1[i]);
		}
		for (int i = 0; i < acceptingStates2.length; i++) {
			acceptingIndices2.add(acceptingStates2[i]);
		}

		// Größe des neuen Automaten berechnen
		newAutomatonSize = automaton1.getSize() * automaton2.getSize();

		setAlphabet();
	}

	// -------------------------------------------------------------------------
	// Transitionstabelle in Graphen überführen
	// hide gesetzt wenn der Graph nur zur Markierung bestimmter Zustände dient
	// -------------------------------------------------------------------------
	private Graph setGraph(int[][] transitions, String name,
			GraphProperties props, boolean hide) {

		Node[] graphNodes = createNodes(25, transitions.length);

		String[] labels = new String[transitions.length];
		for (int i = 0; i < transitions.length; i++) {
			labels[i] = "s" + i;
		}

		Graph g = lang.newGraph(name, transitions, graphNodes, labels, null,
				props);

		if (hide) {
			for (int i = 0; i < g.getSize(); i++) {
				g.hideNode(i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				for (int j = 0; j < g.getSize(); j++) {
					g.hideEdge(i, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				}
			}

		} else
			g.hide();

		return g;
	}

	// -------------------------------------------------------------------------
	// Berechnung kreisförmiger Koordinaten für Zustände
	// -------------------------------------------------------------------------
	private Node[] createNodes(int radiusFactor, int size) {

		Node[] nodeList = new Node[size];

		double angle = 2 * Math.PI / size;
		int radius = radiusFactor * size;

		for (int i = 0; i < size; i++) {

			int x = radius + (int) -(Math.cos(i * angle) * radius);
			int y = radius + (int) -(Math.sin(i * angle) * radius);

			nodeList[i] = new Coordinates(x, y);
		}
		return nodeList;
	}

	// -------------------------------------------------------------------------
	// liest alle Werte die in den Transitionstabellen auftauchen aus
	// -------------------------------------------------------------------------
	private void setAlphabet() {

		TreeSet<Integer> set = new TreeSet<Integer>();

		for (int[] array : automaton1.getAdjacencyMatrix()) {
			for (int elem : array) {
				if (elem != 0)
					set.add(elem);
			}
		}
		for (int[] array : automaton2.getAdjacencyMatrix()) {
			for (int elem : array) {
				if (elem != 0)
					set.add(elem);
			}
		}

		alphabet = new int[set.size()];
		Iterator<Integer> itr = set.iterator();
		int index = 0;
		while (itr.hasNext()) {
			alphabet[index] = itr.next();
			index++;
		}
	}

	// -------------------------------------------------------------------------
	// Anzeige von Überschrift, Beschreibung und Pseudocode
	// -------------------------------------------------------------------------
	private void introduction() {

		// Überschrift mit Rechteck
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) texts
				.get(AnimationPropertiesKeys.FONT_PROPERTY))
				.deriveFont((float) 24));
		headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				texts.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		header = lang.newText(new Coordinates(20, 30),
				"Schnittmengenautomat zweier DFA", "header", null, headerProps);

		headerRect = lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"),
				"hRect", null, rects);

		lang.nextStep("Beschreibung des Algorithmus");

		// Beschreibung des Algorithms
		ArrayList<Text> description = new ArrayList<Text>();
		ArrayList<String> strings = getDescriptionUTF8();

		for (int i = 0; i < strings.size(); i++)
			description.add(lang.newText(new Offset(0, 10 + i * 15, headerRect,
					AnimalScript.DIRECTION_SW), strings.get(i), "description"
					+ i, null, texts));

		lang.nextStep("Algorithmus in Pseudocode");

		// Pseudocode des Algorithmus
		for (Text t : description)
			t.hide();
		srcCode = lang.newSourceCode(new Offset(0, 25, headerRect,
				AnimalScript.DIRECTION_SW), "srcCode", null, code);
		srcCode.addMultilineCode(getCodeExampleUTF8(), "srcCodeText", null);

		lang.nextStep("Ausgangsautomaten");
	}

	// -------------------------------------------------------------------------
	// Anzeige Ausgangsautomaten mit Higlights und Legende
	// -------------------------------------------------------------------------
	private void showAutomata() {

		// Ursprungsautomaten und deren Highlightautomaten positionieren
		automaton1.moveTo(null, null, new Offset(0, 25, srcCode,
				AnimalScript.DIRECTION_SW), Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		automaton2.moveTo(null, null, new Offset(0, 25, srcCode,
				AnimalScript.DIRECTION_SW), Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		automaton2.moveBy(null, 450, 0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		startA1.moveTo(null, null, new Offset(0, 25, srcCode,
				AnimalScript.DIRECTION_SW), Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		startA2.moveTo(null, null, new Offset(0, 25, srcCode,
				AnimalScript.DIRECTION_SW), Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		startA2.moveBy(null, 450, 0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		accA1.moveTo(null, null, new Offset(0, 25, srcCode,
				AnimalScript.DIRECTION_SW), Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		accA2.moveTo(null, null, new Offset(0, 25, srcCode,
				AnimalScript.DIRECTION_SW), Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		accA2.moveBy(null, 450, 0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

		automaton1.show();
		automaton2.show();

		// Startzustand und akzeptierende Zustände der Automaten markieren
		for (int i = 0; i < automaton1.getSize(); i++) {
			if (acceptingIndices1.contains(i))
				accA1.showNode(i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			if (startState1 == i)
				startA1.showNode(i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		}
		for (int i = 0; i < automaton2.getSize(); i++) {
			if (acceptingIndices2.contains(i))
				accA2.showNode(i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			if (startState2 == i)
				startA2.showNode(i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		}

		TextProperties capProps = new TextProperties();
		capProps.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) texts
				.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(
				Font.BOLD, (float) 18));
		capProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				texts.get(AnimationPropertiesKeys.COLOR_PROPERTY));

		Text legend1 = lang.newText(new Offset(100, 0, srcCode,
				AnimalScript.DIRECTION_NE), "Startzustände:", "legend1", null,
				capProps);
		legend = lang.newText(new Offset(0, 25, legend1,
				AnimalScript.DIRECTION_SW), "akzeptierende Zustände:",
				"legend2", null, capProps);

		RectProperties colorSample = new RectProperties();
		colorSample.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		colorSample.set(AnimationPropertiesKeys.FILL_PROPERTY,
				highlight2.get(AnimationPropertiesKeys.FILL_PROPERTY));
		lang.newRect(new Offset(25, -50, legend, AnimalScript.DIRECTION_NE),
				new Offset(75, -25, legend, AnimalScript.DIRECTION_NE),
				"color1", null, colorSample);
		colorSample.set(AnimationPropertiesKeys.FILL_PROPERTY,
				highlight1.get(AnimationPropertiesKeys.FILL_PROPERTY));
		lang.newRect(new Offset(25, 0, legend, AnimalScript.DIRECTION_NE),
				new Offset(75, 25, legend, AnimalScript.DIRECTION_NE),
				"color1", null, colorSample);

		lang.nextStep("Zustandserstellung");
	}

	// -------------------------------------------------------------------------
	// Graph für den neuen Automaten mit versteckten Knoten erstellen
	// -------------------------------------------------------------------------
	private Graph createNewAutomaton(GraphProperties props, String name) {

		Graph automaton = lang.newGraph(name,
				new int[newAutomatonSize][newAutomatonSize],
				createNodes(20, newAutomatonSize), createNodeNames(), null,
				props);

		for (int i = 0; i < automaton.getSize(); i++) {
			automaton.hideNode(i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		}

		return automaton;
	}

	// -------------------------------------------------------------------------
	// Liste der neuen Zustandsnamen aus den alten Zustandsnamen zusammensetzen
	// -------------------------------------------------------------------------
	private String[] createNodeNames() {

		String[] nameList = new String[newAutomatonSize];

		for (int i = 0; i < newAutomatonSize; i++) {
			String name = automaton1.getNodeLabel((int) i
					/ automaton2.getSize());
			name += automaton2.getNodeLabel(i % automaton2.getSize());
			nameList[i] = name;
		}

		return nameList;
	}

	// -------------------------------------------------------------------------
	// Animation der Konstruktion der Zustände des neuen Automaten
	// -------------------------------------------------------------------------
	private void showStateCreation() {

		// Frage nach der Anzahl der neuen Zustände
		MultipleChoiceQuestionModel numberOfStates = new MultipleChoiceQuestionModel(
				"numberOfStates");
		numberOfStates.setPrompt("Wie viele Zustände wird der Automat haben?");
		numberOfStates.addAnswer("" + newAutomatonSize, 1,
				"Richtig! Zustände des 1.Automaten*Zustände des 2.Automaten");
		numberOfStates.addAnswer("" + automaton1.getSize(), 0,
				"Falsch! Zustände des 1.Automaten*Zustände des 2.Automaten");
		numberOfStates.addAnswer(
				"" + (automaton1.getSize() + automaton2.getSize()), 0,
				"Falsch! Zustände des 1.Automaten*Zustände des 2.Automaten");
		lang.addMCQuestion(numberOfStates);

		for (int i = 0; i < newAutomatonSize; i++) {

			srcCode.highlight(0);
			automaton1.highlightNode((int) i / automaton2.getSize(),
					Timing.INSTANTEOUS, new TicksTiming(50));
			startA1.highlightNode((int) i / automaton2.getSize(),
					Timing.INSTANTEOUS, new TicksTiming(50));
			accA1.highlightNode((int) i / automaton2.getSize(),
					Timing.INSTANTEOUS, new TicksTiming(50));

			lang.nextStep();

			srcCode.unhighlight(0);
			srcCode.highlight(1);
			automaton2.highlightNode(i % automaton2.getSize(),
					Timing.INSTANTEOUS, new TicksTiming(50));
			startA2.highlightNode(i % automaton2.getSize(), Timing.INSTANTEOUS,
					new TicksTiming(50));
			accA2.highlightNode(i % automaton2.getSize(), Timing.INSTANTEOUS,
					new TicksTiming(50));

			lang.nextStep();

			boolean acc = acceptingIndices1.contains((int) i
					/ automaton2.getSize())
					&& acceptingIndices2.contains(i % automaton2.getSize());
			boolean start = ((int) i / automaton2.getSize()) == startState1
					&& (i % automaton2.getSize()) == startState2;

			// Frage nach der Art des neuen Zustands
			TrueFalseQuestionModel startAccQ = new TrueFalseQuestionModel(
					"startAccQ" + i, (!start && !acc), 1);
			startAccQ
					.setPrompt("Dieser Zustand ist weder Startzustand, noch akzeptierend.");
			lang.addTFQuestion(startAccQ);

			srcCode.unhighlight(1);
			srcCode.highlight(2);
			newAutomaton.showNode(i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			newAutomaton.highlightNode(i, Timing.INSTANTEOUS, new TicksTiming(
					50));
			accHighlight.highlightNode(i, Timing.INSTANTEOUS, new TicksTiming(
					50));
			startHighlight.highlightNode(i, Timing.INSTANTEOUS,
					new TicksTiming(50));

			lang.nextStep();

			srcCode.unhighlight(2);
			srcCode.highlight(3);

			lang.nextStep();

			srcCode.unhighlight(3);

			// Sonderbehandlung akzeptierender Zustände
			if (acc) {
				srcCode.highlight(4);
				newAcceptingIndices = new ArrayList<Integer>();
				newAcceptingIndices.add(i);
				accHighlight.showNode(accHighlight.getNode(i),
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);

				lang.nextStep();

				srcCode.unhighlight(4);
			}
			srcCode.highlight(5);

			lang.nextStep();

			srcCode.unhighlight(5);

			// Sonderbehandlung des Startzustands
			if (start) {
				srcCode.highlight(6);
				newStartIndex = i;
				startHighlight.showNode(startHighlight.getNode(i),
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);

				lang.nextStep();

				srcCode.unhighlight(6);

			}
			automaton1.unhighlightNode((int) i / automaton2.getSize(),
					Timing.INSTANTEOUS, new TicksTiming(50));
			startA1.unhighlightNode((int) i / automaton2.getSize(),
					Timing.INSTANTEOUS, new TicksTiming(50));
			accA1.unhighlightNode((int) i / automaton2.getSize(),
					Timing.INSTANTEOUS, new TicksTiming(50));
			automaton2.unhighlightNode(i % automaton2.getSize(),
					Timing.INSTANTEOUS, new TicksTiming(50));
			startA2.unhighlightNode(i % automaton2.getSize(),
					Timing.INSTANTEOUS, new TicksTiming(50));
			accA2.unhighlightNode(i % automaton2.getSize(), Timing.INSTANTEOUS,
					new TicksTiming(50));
			newAutomaton.unhighlightNode(i, Timing.INSTANTEOUS,
					new TicksTiming(50));
			accHighlight.unhighlightNode(i, Timing.INSTANTEOUS,
					new TicksTiming(50));
			startHighlight.unhighlightNode(i, Timing.INSTANTEOUS,
					new TicksTiming(50));
		}
		lang.nextStep("Das Alphabet");
	}

	// -------------------------------------------------------------------------
	// Anzeige des Alphabets als Array für die Iteration über dessen Elemente
	// -------------------------------------------------------------------------
	private void showAlphabetArray() {

		TextProperties capProps = new TextProperties();
		capProps.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) texts
				.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(
				Font.BOLD, (float) 18));
		capProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				texts.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		Text caption = lang.newText(new Offset(0, 40, legend,
				AnimalScript.DIRECTION_SW), "Alphabet:", "caption", null,
				capProps);

		alphabetArray = lang.newIntArray(new Offset(0, 10, caption,
				AnimalScript.DIRECTION_SW), alphabet, "alphabet", null, arrays);

		lang.nextStep("Transitionserstellung");
	}

	// -------------------------------------------------------------------------
	// Animation der Erstellung der Zustandsübergänge des neuen Automaten
	// -------------------------------------------------------------------------
	private void showTransitionCreation() {

		for (int i = 0; i < newAutomatonSize; i++) {
			for (int j = 0; j < alphabet.length; j++) {

				srcCode.highlight(7);
				newAutomaton.highlightNode(i, Timing.INSTANTEOUS,
						new TicksTiming(50));
				accHighlight.highlightNode(i, Timing.INSTANTEOUS,
						new TicksTiming(50));
				startHighlight.highlightNode(i, Timing.INSTANTEOUS,
						new TicksTiming(50));

				lang.nextStep();

				srcCode.unhighlight(7);
				srcCode.highlight(8);
				alphabetArray.highlightCell(j, Timing.INSTANTEOUS,
						new TicksTiming(50));

				lang.nextStep();

				srcCode.unhighlight(8);
				srcCode.highlight(9);
				int target = findTarget(i, j);
				if (target != -1) {
					newAutomaton.setEdgeWeight(i, target, alphabet[j],
							Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					newAutomaton.highlightEdge(i, target, Timing.INSTANTEOUS,
							new TicksTiming(50));
				}

				lang.nextStep();

				newAutomaton.unhighlightNode(i, Timing.INSTANTEOUS,
						new TicksTiming(50));
				accHighlight.unhighlightNode(i, Timing.INSTANTEOUS,
						new TicksTiming(50));
				startHighlight.unhighlightNode(i, Timing.INSTANTEOUS,
						new TicksTiming(50));
				alphabetArray.unhighlightCell(j, Timing.INSTANTEOUS,
						new TicksTiming(50));
				srcCode.unhighlight(9);
				if (target != -1)
					newAutomaton.unhighlightEdge(i, target, Timing.INSTANTEOUS,
							new TicksTiming(50));
			}
		}
		lang.nextStep();
	}

	// -------------------------------------------------------------------------
	// Identifikation des Ziels für einen Übergang
	// -------------------------------------------------------------------------
	private int findTarget(int start, int alphabetIndex) {

		int state1 = -1;
		int state2 = -1;

		int[][] matrix = automaton1.getAdjacencyMatrix();

		for (int i = 0; i < matrix[0].length; i++) {
			if (matrix[(int) start / automaton2.getSize()][i] == alphabet[alphabetIndex])
				state1 = i;
		}
		matrix = automaton2.getAdjacencyMatrix();
		for (int i = 0; i < matrix[0].length; i++) {
			if (matrix[start % automaton2.getSize()][i] == alphabet[alphabetIndex])
				state2 = i;
		}
		if (state1 == -1 || state2 == -1)
			return -1;
		else
			return state1 * automaton2.getSize() + state2;
	}

	// -------------------------------------------------------------------------
	// Anzeige einer abschließenden Zusammenfassung
	// -------------------------------------------------------------------------
	private void conclusion() {
		lang.hideAllPrimitives();
		header.show();
		headerRect.show();

		ArrayList<Text> conclusion = new ArrayList<Text>();
		ArrayList<String> strings = new ArrayList<String>();

		strings.add("");
		strings.add("Der neue Automat hat " + newAutomatonSize + " Zustände ("
				+ transitions1.length + " * " + transitions2.length + ").");
		strings.add("Sein Anfangszustand ist "
				+ newAutomaton.getNodeLabel(newStartIndex) + ".");
		String acc = "";
		if (newAcceptingIndices.size() == 1) {
			acc = "Sein Endzustand ist "
					+ newAutomaton.getNodeLabel(newAcceptingIndices.get(0))
					+ ".";
		} else {
			acc = "Seine Endzustände sind: ";
			for (int i = 0; i < newAcceptingIndices.size(); i++) {
				acc = acc
						+ newAutomaton.getNodeLabel(newAcceptingIndices.get(i))
						+ " ";
			}
		}
		strings.add(acc);
		strings.add("");
		strings.add("Achtung: Der entstandene Automat ist nicht minimiert!");
		strings.add("");
		strings.add("Die Zustandsübergangstabelle sieht wie folgt aus:");

		for (int i = 0; i < strings.size(); i++)
			conclusion.add(lang.newText(new Offset(0, 10 + i * 15, headerRect,
					AnimalScript.DIRECTION_SW), strings.get(i), "conclusion"
					+ i + i, null, texts));

		int[][] matrix = newAutomaton.getAdjacencyMatrix();

		// Anzeige der neuen Zustandsübergangsfunktion als Matrix
		Rect[][] table = new Rect[matrix.length + 1][matrix.length + 1];
		for (int y = 0; y < matrix.length + 1; y++) {
			for (int x = 0; x < matrix.length + 1; x++) {

				table[x][y] = lang.newRect(
						new Offset(40 * x, 25 + 30 * y, conclusion
								.get(conclusion.size() - 1),
								AnimalScript.DIRECTION_SW),
						new Offset(40 * (x + 1), 30 * (y + 1) + 25, conclusion
								.get(conclusion.size() - 1),
								AnimalScript.DIRECTION_SW), x + "," + y, null,
						rects);
			}
		}

		Text[][] tableText = new Text[matrix.length + 1][matrix.length + 1];
		for (int y = 0; y < matrix.length + 1; y++) {
			for (int x = 0; x < matrix.length + 1; x++) {
				if (x * y == 0) {
					if (x != 0 || y != 0)
						tableText[x][y] = lang.newText(
								new Offset(40 * x + 5, 33 + 30 * y, conclusion
										.get(conclusion.size() - 1),
										AnimalScript.DIRECTION_SW),
								newAutomaton.getNodeLabel(x + y - 1) + "",
								"text" + x + "," + y, null, texts);

				} else if (matrix[y - 1][x - 1] != 0)
					tableText[x][y] = lang.newText(new Offset(40 * x + 15,
							33 + 30 * y, conclusion.get(conclusion.size() - 1),
							AnimalScript.DIRECTION_SW), matrix[y - 1][x - 1]
							+ "", "text" + x + "," + y, null, texts);
			}
		}
		lang.nextStep();
		newAutomaton.show();
		startHighlight.show();
		accHighlight.show();

	}
}
