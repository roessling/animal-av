/*
 * local_search.java
 * Maximilian Müller, Tobias Neidig, 2015 for the Animal project at TU Darmstadt.
 */
package generators.searching;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;

public class LocalSearchTSP implements Generator {
	private Language lang;
	private int startNodeIndex;
	private Graph graphCurrentBest;
	private Graph graphComputation;
	private Text textCurrentBest;
	private Text textComputation;
	private Text textStepDescription;
	private Text textHeadline;
	private TextProperties textPropsH1;
	private TextProperties textPropsH2;
	private TextProperties textPropsParagraph;
	private SourceCodeProperties sourceCodePropsText;
	private SourceCode sourceCodePseudo1;
	private SourceCode sourceCodePseudo2;
	private ArrayProperties arrProps;
	private StringArray arrCurrentBest;
	private StringArray arrComputation;

	private Integer arrCurrentBest_i = 0;
	private Integer arrComputation_i = 0;

	protected Translator translator;
	protected Locale contentLocale = null;

	public LocalSearchTSP() {
		this("resources/LocalSearchTSP", Locale.GERMANY);
	}

	public LocalSearchTSP(String aResourceName, Locale aLocale) {
		translator = new Translator(aResourceName, aLocale);
		contentLocale = aLocale;

		init();
	}

	public void init() {
		lang = new AnimalScript(translator.translateMessage("animTitle"), "Maximilian Müller, Tobias Neidig", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		// count = (Integer)primitives.get("count");
		Graph graph = (Graph) primitives.get("graph");
		lang.setStepMode(true);

		// init text props
		textPropsH1 = (TextProperties) props.getPropertiesByName("Überschrift 1 Property");
		textPropsH1.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textPropsH1.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily(),
						Font.BOLD | Font.ITALIC, 20));
		textPropsH2 = (TextProperties) props.getPropertiesByName("Überschrift 2 Property");
		textPropsH2.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(((Font) textPropsH2.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily(), Font.BOLD, 15));
		textPropsParagraph = (TextProperties) props.getPropertiesByName("Text Property");
		textPropsParagraph.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				((Font) textPropsParagraph.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFamily(), Font.PLAIN, 12));
		sourceCodePropsText = (SourceCodeProperties) props.getPropertiesByName("Fließtext Property");

		arrProps = new ArrayProperties();
		arrProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		arrProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
		arrProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 13));
		arrProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

		Node[] nodes = new Node[graph.getSize()];
		String[] labels = new String[graph.getSize()];
		int[][] adja = graph.getAdjacencyMatrix();
		for (int i = 0; i < graph.getSize(); i++) {
			nodes[i] = graph.getNode(i);
			labels[i] = graph.getNodeLabel(i);
			// check if current node is start node
			if (graph.getStartNode() == graph.getNode(i)) {
				startNodeIndex = i;
			}
		}

		GraphProperties graphProps = (GraphProperties) props.getPropertiesByName("Current-Best Graph Properties");
		GraphProperties graphProps2 = (GraphProperties) props.getPropertiesByName("Computation Graph Properties");

		// addGraph & copy does not work correctly... so we create our new
		// graphs
		graphCurrentBest = lang.newGraph("graphCurrentBest", adja, nodes, labels, null, graphProps);
		graphCurrentBest.setStartNode(graph.getStartNode());
		graphCurrentBest.moveTo("SE", "Translate", new Coordinates(20, 180), null, null);
		graphCurrentBest.hide();

		graphComputation = lang.newGraph("graphComputation", adja, nodes, labels, null, graphProps2);
		graphComputation.setStartNode(graph.getStartNode());
		graphComputation.moveTo("SE", "Translate", new Coordinates(20, 180), null, null);
		graphComputation.hide();

		// textfields
		Color tmpColor = (Color) textPropsParagraph.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		textPropsParagraph.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				graphCurrentBest.getProperties().get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
		textCurrentBest = lang.newText(new Coordinates(20, 50), translator.translateMessage("textCurrentBest", "-"),
				"textCurrentBest", null, textPropsParagraph);
		textCurrentBest.hide();
		textPropsParagraph.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				graphComputation.getProperties().get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
		textComputation = lang.newText(new Coordinates(20, 80), translator.translateMessage("textComputation", "-"),
				"textComputation", null, textPropsParagraph);
		textComputation.hide();
		textPropsParagraph.set(AnimationPropertiesKeys.COLOR_PROPERTY, tmpColor);
		textStepDescription = lang.newText(new Coordinates(20, 110), "", "textStepDescription", null,
				textPropsParagraph);
		textStepDescription.hide();

		// Pseudo SourceCode
		sourceCodePseudo1 = lang.newSourceCode(new Offset(10, 80, graphCurrentBest, "NE"), "sourceCodePseudo1", null,
				(SourceCodeProperties) props.getPropertiesByName("Code Lokale Suche Property"));

		sourceCodePseudo1.addCodeLine(translator.translateMessageWithoutParameterExpansion("pseudoCodeLocalSearch_0"),
				null, 0, null);
		sourceCodePseudo1.addCodeLine(translator.translateMessageWithoutParameterExpansion("pseudoCodeLocalSearch_1"),
				null, 1, null);
		sourceCodePseudo1.addCodeLine(translator.translateMessageWithoutParameterExpansion("pseudoCodeLocalSearch_2"),
				null, 1, null);
		sourceCodePseudo1.addCodeLine(translator.translateMessageWithoutParameterExpansion("pseudoCodeLocalSearch_3"),
				null, 1, null);
		sourceCodePseudo1.addCodeLine(translator.translateMessageWithoutParameterExpansion("pseudoCodeLocalSearch_4"),
				null, 1, null);
		sourceCodePseudo1.addCodeLine(translator.translateMessageWithoutParameterExpansion("pseudoCodeLocalSearch_5"),
				null, 0, null);
		sourceCodePseudo1.hide();
		sourceCodePseudo2 = lang.newSourceCode(new Offset(0, 0, "sourceCodePseudo1", "SW"), "sourceCodePseudo2", null,
				(SourceCodeProperties) props.getPropertiesByName("Code Nachbarschaft Property"));
		sourceCodePseudo2.addCodeLine(
				translator.translateMessageWithoutParameterExpansion("pseudoCodeSearchInNeighbourhood_0"), null, 0,
				null);
		sourceCodePseudo2.addCodeLine(
				translator.translateMessageWithoutParameterExpansion("pseudoCodeSearchInNeighbourhood_1"), null, 1,
				null);
		sourceCodePseudo2.addCodeLine(
				translator.translateMessageWithoutParameterExpansion("pseudoCodeSearchInNeighbourhood_2"), null, 1,
				null);
		sourceCodePseudo2.addCodeLine(
				translator.translateMessageWithoutParameterExpansion("pseudoCodeSearchInNeighbourhood_3"), null, 2,
				null);
		sourceCodePseudo2.addCodeLine(
				translator.translateMessageWithoutParameterExpansion("pseudoCodeSearchInNeighbourhood_4"), null, 1,
				null);
		sourceCodePseudo2.addCodeLine(
				translator.translateMessageWithoutParameterExpansion("pseudoCodeSearchInNeighbourhood_5"), null, 2,
				null);
		sourceCodePseudo2.addCodeLine(
				translator.translateMessageWithoutParameterExpansion("pseudoCodeSearchInNeighbourhood_6"), null, 1,
				null);
		sourceCodePseudo2.addCodeLine(
				translator.translateMessageWithoutParameterExpansion("pseudoCodeSearchInNeighbourhood_7"), null, 0,
				null);
		sourceCodePseudo2.hide();

		// array fields
		arrCurrentBest = lang.newStringArray(new Coordinates(250, 50), labels, "arrCurrentBest" + arrCurrentBest_i,
				null, arrProps);
		arrCurrentBest.hide();
		arrComputation = lang.newStringArray(new Coordinates(250, 80), labels, "arrComputation" + arrComputation_i,
				null, arrProps);
		arrComputation.hide();

		// show headline
		textHeadline = lang.newText(new Coordinates(20, 20), translator.translateMessage("headline"), "textHeadline",
				null, textPropsH1);
		textHeadline.show();

		/* algoanim.primitives.Rect rectHeadline = */
		lang.newRect(new Coordinates(0, 40), new Coordinates(800, 40), "rectHeadline", null);

		lang.nextStep();
		// show intro
		intro();
		// run the algo
		TownList towns = new TownList(adja);
		try {
			run(towns);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println(e.toString());
		}
		
		// show outro
		outro();

		// exit...
		lang.finalizeGeneration();
		return lang.toString();
	}

	/**
	 * Zeigt dem Betrachter ein Intro an. In diesem wird ihm Allgemeines über
	 * Lokale Suche und TSP vermittelt.
	 */
	public void intro() {
		Text textLocalSearchHeadline = lang.newText(new Coordinates(20, 50),
				translator.translateMessage("introHeadlineLocalSearch"), "textLocalSearchHeadline", null, textPropsH2);
		// We have to use SourceCode object instead of text object,
		// because it
		// can handle multilines

		SourceCode sourceCodeTextLocalSearch = lang.newSourceCode(new Coordinates(20, 80), "sourceCodeTextLocalSearch",
				null, sourceCodePropsText);
		for (String line : translator.translateMessage("introLocalSearch").split("\n")) {
			sourceCodeTextLocalSearch.addCodeLine(line, null, 0, null);
		}
		lang.nextStep();

		Text textTSPHeadline = lang.newText(new Coordinates(20, 270), translator.translateMessage("introHeadlineTSP"),
				"textTSPHeadline", null, textPropsH2);
		SourceCode sourceCodeTextTSP = lang.newSourceCode(new Coordinates(20, 300), "sourceCodeTextLocalSearch", null,
				sourceCodePropsText);
		for (String line : translator.translateMessage("introTSP").split("\n")) {
			sourceCodeTextTSP.addCodeLine(line, null, 0, null);
		}
		lang.nextStep(translator.translateMessage("tableOfContentsIntro"));

		textLocalSearchHeadline.hide();
		sourceCodeTextLocalSearch.hide();
		textTSPHeadline.hide();
		sourceCodeTextTSP.hide();
	}

	/**
	 * Nach Abschluss der Animation zeige dem Betrachter weiterführende Infos
	 * über Lokale Suche an.
	 */
	public void outro() {
		/* Text textOutroHeadline = */
		lang.newText(new Coordinates(20, 50), translator.translateMessage("outroHeadline"), "textLocalSearchHeadline",
				null, textPropsH2);

		// We have to use SourceCode object instead of text object, because it
		// can handle multilines
		SourceCode sourceCodeTextOutro = lang.newSourceCode(new Coordinates(20, 80), "sourceCodeTextOutro", null,
				sourceCodePropsText);
		for (String line : translator.translateMessage("outroText").split("\n")) {
			sourceCodeTextOutro.addCodeLine(line, null, 0, null);
		}
		lang.nextStep(translator.translateMessage("tableOfContentsOutro"));
	}

	/**
	 * Führt die Lokale Suche anhand einer Startlösung aus, sucht solange
	 * bessere Nachbarn, bis es keine mehr gibt und gibt diesen dann zurück
	 * 
	 * @param solution
	 *            Zulässige Startlösung
	 * @return Zulässige finale Lösung
	 */
	public void run(TownList solution) {
		// show ui
		graphCurrentBest.show();
		textCurrentBest.show();
		textComputation.show();
		textStepDescription.show();
		sourceCodePseudo1.show();
		sourceCodePseudo2.show();
		sourceCodePseudo1.highlight(0);
		arrCurrentBest.show();
		arrComputation.show();
		if (startNodeIndex != 0) {
			// respect start node
			int t = startNodeIndex;
			while(t > 0){
				t--;
				for(int i = 0; i < solution.size() - 1; i++){
					solution.exchange(i, i+1);
					
					arrCurrentBest.hide();
					arrCurrentBest = lang.newStringArray(arrCurrentBest.getUpperLeft(), getLabelsFromArray(arrCurrentBest),
							arrCurrentBest.getName().replace(arrCurrentBest_i.toString(), (++arrCurrentBest_i).toString()),
							null, arrCurrentBest.getProperties());
					arrCurrentBest.swap(i, i+1, null, null);
					arrComputation.hide();
					arrComputation = lang.newStringArray(arrComputation.getUpperLeft(), getLabelsFromArray(arrComputation),
							arrComputation.getName().replace(arrComputation_i.toString(), (++arrComputation_i).toString()),
							null, arrComputation.getProperties());
					arrComputation.swap(i, i+1, null, null);
				}
				
			}
			/*solution.exchange(0, startNodeIndex);
			arrCurrentBest.hide();
			arrCurrentBest = lang.newStringArray(arrCurrentBest.getUpperLeft(), getLabelsFromArray(arrCurrentBest),
					arrCurrentBest.getName().replace(arrCurrentBest_i.toString(), (++arrCurrentBest_i).toString()),
					null, arrCurrentBest.getProperties());
			arrCurrentBest.swap(0, startNodeIndex, null, null);
			arrCurrentBest.hide();

			arrComputation.hide();
			arrComputation = lang.newStringArray(arrComputation.getUpperLeft(), getLabelsFromArray(arrComputation),
					arrComputation.getName().replace(arrComputation_i.toString(), (++arrComputation_i).toString()),
					null, arrComputation.getProperties());
			arrComputation.swap(0, startNodeIndex, null, null);
			arrComputation.hide();*/
		}
		lang.nextStep();

		// Bestimme eine Startlösung
		// Pseudo: solution = solution;
		sourceCodePseudo1.toggleHighlight(0, 1);

		TownList other_solution = solution;
		do {
			if (compare(solution, other_solution)) {
				textStepDescription.setText(translator.translateMessage("stepDescriptionNewBestSolutionFound"), null,
						null);
				sourceCodePseudo2.unhighlight(2);
				sourceCodePseudo2.unhighlight(3);
				sourceCodePseudo1.highlight(3);
				sourceCodePseudo1.highlight(4);
			}
			solution = other_solution;
			arrCurrentBest.hide();
			arrCurrentBest_i++;
			arrCurrentBest = lang.newStringArray(new Coordinates(250, 50), getLabelsFromArray(arrComputation),
					"arrCurrentBest" + arrCurrentBest_i, null, arrProps);
			textCurrentBest.setText(
					translator.translateMessage("textCurrentBest", Double.toString(solution.roundtrip())), null, null);
			solution.highlightInGraph(graphCurrentBest);
			lang.nextStep(translator.translateMessage("stepDescriptionNewBestSolution",
					Double.toString(solution.roundtrip())));
			sourceCodePseudo1.unhighlight(1);
			sourceCodePseudo1.unhighlight(3);
			sourceCodePseudo1.unhighlight(4);
			// Durchsuche Nachbarschaft nach besseren Lösungen
			other_solution = getMutation(solution);
			// Wenn bessere Lösung gefunden, setze als neue "Startlösung" und
			// starte neu
			// Wenn keine bessere gefunden sind wir fertig.
		} while (compare(solution, other_solution));

		//textStepDescription.setText(translator.translateMessage("stepDescriptionNoBetterSolution"), null, null);
		//sourceCodePseudo1.highlight(5);
		//lang.nextStep();
		//sourceCodePseudo1.unhighlight(5);

		// hide ui
		textComputation.hide();
		arrComputation.hide();
		textStepDescription.hide();
		textCurrentBest.setText(
				translator.translateMessage("textCurrentBestBest", Double.toString(solution.roundtrip())), null, null);
		lang.nextStep(translator.translateMessage("tableOfContentsBestSolution"));
		sourceCodePseudo1.hide();
		sourceCodePseudo2.hide();
		textCurrentBest.hide();
		graphCurrentBest.hide();
		arrCurrentBest.hide();
		return;
	}

	/**
	 * Bekomt zwei zulässige Lösungen und vergleicht deren Werte
	 * 
	 * @param solution_1
	 *            1. zulässige Lösung
	 * @param Lösung_2
	 *            2. zulässige Lösung
	 * @return true, wenn 2. zulässige Lösung besser ist, sonst false
	 */
	public boolean compare(TownList solution_1, TownList solution_2) {
		return solution_1.roundtrip() > solution_2.roundtrip();
	}

	/**
	 * Bekommt eine zulässige Lösung und versucht daraus einen zulässigen
	 * Nachbarn mit besserem Wert zu generieren, gibt bei Existenz diesen
	 * zurück, sonst sich selbst
	 * 
	 * @param solution
	 *            Zulässige Lösung
	 * @return Bessere Lösung als Nachbar von der ursprünglichen Lösung oder die
	 *         ursprüngliche Lösung
	 */
	public TownList getMutation(TownList solution) {
		TownList new_solution = new TownList(solution);
		double old_fitness = new_solution.roundtrip();
		new_solution.highlightInGraph(graphComputation);
		graphComputation.show();
		textStepDescription.setText(translator.translateMessage("stepDescriptionCalculateNeighbourhood"), null, null);
		sourceCodePseudo1.highlight(2);
		sourceCodePseudo2.highlight(0);
		lang.nextStep();


		sourceCodePseudo2.unhighlight(0);

		for (int i = 1; i < new_solution.size(); i++) {
			for (int j = 1; j < new_solution.size(); j++) {
				sourceCodePseudo2.unhighlight(4);
				sourceCodePseudo2.unhighlight(5);
				if (j <= i) {
					continue;
				}

				// Betrachte Nachbarn: Nachbar = i und j getauscht
				new_solution.exchange(i, j);
				arrComputation.hide();
				arrComputation = lang.newStringArray(arrComputation.getUpperLeft(), getLabelsFromArray(arrComputation),
						arrComputation.getName().replace(arrComputation_i.toString(), (++arrComputation_i).toString()),
						null, arrComputation.getProperties());
				arrComputation.swap(i, j, Timing.FAST, Timing.FAST);

				new_solution.highlightInGraph(graphComputation);
				textStepDescription.setText(translator.translateMessage("stepDescriptionCalculateExchange",
						arrComputation.getData(j), arrComputation.getData(i)), null, null);
				textComputation.setText(
						translator.translateMessage("textComputation", Double.toString(new_solution.roundtrip())), null,
						null);
				sourceCodePseudo2.highlight(1);
				lang.nextStep();
				sourceCodePseudo2.unhighlight(1);
				// Wenn bessere Lösung gefunden, gib diese zurück
				if (new_solution.roundtrip() < old_fitness) {
					sourceCodePseudo2.highlight(2);
					sourceCodePseudo2.highlight(3);
					lang.nextStep();
					sourceCodePseudo1.unhighlight(2);
					graphComputation.hide();
					return new_solution;
				} else {
					// sonst revidiere letzten Tausch und betrachte nächsten
					// Nachbarn
					new_solution.exchange(j, i);
					arrComputation.hide();
					arrComputation = lang.newStringArray(arrComputation.getUpperLeft(),
							getLabelsFromArray(arrComputation), arrComputation.getName()
									.replace(arrComputation_i.toString(), (++arrComputation_i).toString()),
							null, arrComputation.getProperties());
					arrComputation.swap(j, i, Timing.FAST, Timing.FAST);
					sourceCodePseudo2.highlight(4);
					sourceCodePseudo2.highlight(5);
					new_solution.highlightInGraph(graphComputation);
					textStepDescription.setText(translator.translateMessage("stepDescriptionRevise"), null, null);
					textComputation.setText(
							translator.translateMessage("textComputation", Double.toString(new_solution.roundtrip())),
							null, null);
					lang.nextStep();
				}
			}
		}
		sourceCodePseudo2.unhighlight(4);
		sourceCodePseudo2.unhighlight(5);
		sourceCodePseudo2.highlight(6);
		textStepDescription.setText(translator.translateMessage("stepDescriptionNoBetterSolution"), null, null);
		lang.nextStep();
		sourceCodePseudo1.unhighlight(2);
		sourceCodePseudo2.unhighlight(6);
		graphComputation.hide();
		return new_solution;
	}

	/**
	 * Gibt alle Labels eines StringArrays zurück
	 * 
	 * @param arr
	 * @return
	 */
	private String[] getLabelsFromArray(StringArray arr) {
		String[] labels = new String[arr.getLength()];
		for (int i = 0; i < arr.getLength(); i++) {
			labels[i] = arr.getData(i);
		}
		return labels;
	}

	/**
	 * A {@link TownList} represents an ordered list of towns and extends
	 * ArrayList with helper-methods.
	 */
	private class TownList extends ArrayList<Integer> {
		private static final long serialVersionUID = 1L;
		/**
		 * Die Matrxi des Graphen bzw der Städteliste
		 */
		private int[][] adjacencyMatrix;

		/**
		 * Repräsentiert eine Städteliste
		 * 
		 * @param adjacencyMatrix
		 *            Die Einträge der Matrix geben die Distanzen zwischen den
		 *            Städten an.
		 */
		public TownList(int[][] adjacencyMatrix) {
			this.adjacencyMatrix = adjacencyMatrix;
			for (int i = 0; i < this.adjacencyMatrix.length; i++) {
				// Add "town" to map
				this.add(i);
				// fill empty places in adjacency matrix
				for (int j = 0; j < this.adjacencyMatrix.length; j++) {
					if (this.adjacencyMatrix[i][j] == 0) {
						this.adjacencyMatrix[i][j] = this.adjacencyMatrix[j][i];
					}
				}
			}
		}

		/**
		 * Kopiert eine Städteliste
		 * 
		 * @param other
		 */
		public TownList(TownList other) {
			this.adjacencyMatrix = other.getAdjacencyMatrix();
			this.addAll(other);
		}

		/**
		 * Calculates the whole distance through all towns in ordered list.
		 * 
		 * @return distance between all towns
		 */
		public double roundtrip() {
			double roundtripLength = 0;
			for (int i = 0; i < this.size(); i++) {
				if (i == this.size() - 1) {
					roundtripLength += adjacencyMatrix[this.get(i)][this.get(0)];
				} else {
					roundtripLength += adjacencyMatrix[this.get(i)][this.get(i + 1)];
				}
			}

			return roundtripLength;
		}

		/**
		 * exchanges the town at first position and the town at second position
		 * 
		 * @param first
		 * @param second
		 */
		public boolean exchange(int first, int second) {
			// System.out.println("exchange " + first + " with " + second);
			if (first < 0 || second < 0 || first >= this.size() || second >= this.size()) {
				return false;
			}
			if (first == second) {
				// nothing to do
				return true;
			}
			Integer tmp = this.get(first);
			this.set(first, this.get(second));
			this.set(second, tmp);
			return true;
		}

		/**
		 * Stellt die Städte im Graph dar und highlighted die besuchten Kanten.
		 * 
		 * @param g
		 */
		public void highlightInGraph(Graph g) {
			for (int i = 0; i < this.size() - 1; i++) {
				g.unhighlightNode(i, null, null);
				int[] edges = g.getEdgesForNode(i);
				for (int j = 0; j < edges.length; j++) {
					g.unhighlightEdge(i, j, null, null);
				}
			}
			g.highlightNode(this.get(0), null, null);
			g.setStartNode(g.getNode(this.get(0)));
			for (int i = 0; i < this.size() - 1; i++) {
				g.highlightEdge(g.getNode(this.get(i)), g.getNode(this.get(i + 1)), null, null);
			}
			g.highlightEdge(g.getNode(this.get(this.size() - 1)), g.getNode(this.get(0)), null, null);
		}

		/**
		 * @return the adjacencyMatrix
		 */
		public int[][] getAdjacencyMatrix() {
			return adjacencyMatrix;
		}
	}

	/*
	 * Animal-Generator options
	 */

	public String getName() {
		return translator.translateMessage("animName");
	}

	public String getAlgorithmName() {
		return translator.translateMessage("animAlgoName");
	}

	public String getAnimationAuthor() {
		return "Maximilian Müller, Tobias Neidig";
	}

	public String getDescription() {
		return translator.translateMessage("animDescription");
	}

	public String getCodeExample() {
		return translator.translateMessage("animCodeExample");
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return contentLocale;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	/*
	 * End animal generator options
	 */
}