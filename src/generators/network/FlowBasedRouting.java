/*
 * FlowBasedRouting.java
 * Claudia Lölkes, Verena Sieburger, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.network;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import org.apache.commons.math3.util.Pair;

import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleSelectionQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.Slide;
import algoanim.animalscript.addons.bbcode.DefaultStyle;

import java.awt.Color;
import java.awt.Font;

import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class FlowBasedRouting implements ValidatingGenerator {
	private Language lang;
	private SourceCodeProperties sourceCode;
	private int[][] utilisationMatrix;
	private Color Highlightcolor;
	private algoanim.primitives.Graph graph;

	public void init() {
		lang = new AnimalScript("Non-adaptive Flow Based Routing", "Claudia Lölkes, Verena Sieburger", 800, 600);
		lang.setStepMode(true);

	}

	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		graph = (algoanim.primitives.Graph) primitives.get("graph");
		utilisationMatrix = (int[][]) primitives.get("utilisationMatrix");
		for (int i = 0; i < utilisationMatrix.length; i++)
			if (utilisationMatrix[i].length != utilisationMatrix.length)
				return false;
		if (utilisationMatrix.length != graph.getSize())
			return false;
		for (int j = 0; j < utilisationMatrix.length; j++) {
			for (int k = 0; k < utilisationMatrix.length; k++) {
				if (utilisationMatrix[k][j] != utilisationMatrix[j][k] || utilisationMatrix[j][k] < 0)
					return false;
			}
		}
		for (int i = 0; i < graph.getAdjacencyMatrix().length; i++) {
			for (int j = 0; j < graph.getAdjacencyMatrix().length; j++)
				if (graph.getAdjacencyMatrix()[i][j] < 0)
					return false;
		}
		return true;
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
		utilisationMatrix = (int[][]) primitives.get("utilisationMatrix");

		Highlightcolor = (Color) primitives.get("Highlightcolor");

		graph = (algoanim.primitives.Graph) primitives.get("graph");

		GraphProperties gp = graph.getProperties();
		gp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Highlightcolor);
		gp.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.GREEN);

		graph = lang.addGraph(graph, null, gp);
		graph.hide();

		startFlowBasedRouting(graph, utilisationMatrix);
		return lang.toString();
	}

	/*
	 * 
	 */
	private void startFlowBasedRouting(algoanim.primitives.Graph graph2, int[][] utilisationMatrix) {
		showHeadline();
		showDescription();
		flowClaculator(graph2, utilisationMatrix);
		showEndText();

	}

	public String getName() {
		return "Non-adaptive Flow Based Routing";
	}

	public String getAlgorithmName() {
		return "Flow Based Routing";
	}

	public String getAnimationAuthor() {
		return "Claudia Lölkes, Verena Sieburger";
	}

	public String getDescription() {
		return "Flow Based Routing ist ein Algorithmus, der die Verbindungskapazitäten eines" + "\n"
				+ "Netzwerks ausnutzt.";
	}

	public String getCodeExample() {
		return "private void flowbasedRouting(Graph graph, Utilisation utilisation){" + "\n"
				+ "	for(edge e : graph.getAllEdges){" + "\n" + "   		int c = e.getCapacity();" + "\n" + "	}"
				+ "\n" + "	for(edge e : graph.getAllEdges){" + "\n"
				+ "		int u = utilisation(e.getNodeFrom(), e.getNodeTo());" + "\n" + "	}" + "\n"
				+ " 	for(edge e : graph.getAllEdges){" + "\n" + "		int avL = getAverageLoad(e);" + "\n"
				+ "	}" + "\n" + "	for(edge e : graph.getAllEdges){" + "\n"
				+ "		int tDel = (1/(c[e.getId()] – avL[e.getId()])) * 1000;" + "\n" + "	}" + "\n"
				+ "	for(edge e : graph.getAllEdges){" + "\n" + "		double weight = avL(e.getId()] / sum(avL);"
				+ "\n" + "	}" + "\n" + "	double avDel = 0; " + "\n" + "	for(edge e : graph.getAllEdges){" + "\n"
				+ "		avDel =+ weight[e.getId()] * tDel[e.getId()];" + "\n" + "	}" + "\n" + "}" + "\n";
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

	/**
	 * 
	 * @param visualGraph
	 * @param utilisation
	 */
	public void flowClaculator(algoanim.primitives.Graph visualGraph, int[][] utilisation) {

		GraphProperties gp = visualGraph.getProperties();
		gp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Highlightcolor);
		gp.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.GREEN);

		TextProperties textP = new TextProperties();

		visualGraph.show();

		Graphnode.resetId();

		Graph graph = toGraph(visualGraph);
		Text graphUnter = lang.newText(new Offset(0, 10, visualGraph, "SW"), "Kapazitätengraph (pkts)",
				"Graphunterschrift", null);

		Text beschreibung1 = lang.newText(new Coordinates(100, 50), "", "beschreibung1", null, textP);

		// InMatrix für die nutzung
		MatrixProperties mp = new MatrixProperties();
		mp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Highlightcolor);
		mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");

		PathStore[][] ps = bellmannford(graph);
		List<Edge> paths = new ArrayList<Edge>();
		String[][] nutzMatrix = new String[utilisation.length + 1][utilisation.length + 1];
		for (int i = 0; i < utilisation.length + 1; i++) {
			for (int j = 0; j < utilisation.length + 1; j++) {
				if (i == 0 && j == 0) {
					nutzMatrix[i][j] = "";
				} else if (i == 0 && !(i == j)) {
					nutzMatrix[i][j] = graph.getNode(j - 1).getLabel();
				} else if (j == 0 && !(i == j)) {
					nutzMatrix[i][j] = graph.getNode(i - 1).getLabel();
				} else {
					paths = ps[i - 1][j - 1].getPath();
					String pathString = getDisplayPath(graph.getNode(i - 1).getLabel(), graph.getNode(j - 1).getLabel(),
							paths);
					nutzMatrix[i][j] = utilisation[i - 1][j - 1] + (pathString.isEmpty() ? "" : ", ") + pathString;
				}
			}
		}
		StringMatrix utiliStrMatrix = lang.newStringMatrix(new Offset(50, 0, visualGraph, "NE"), nutzMatrix,
				"Kantennutzung", null, mp);
		Text utiliStrMatrixUnter = lang.newText(new Offset(0, 10, utiliStrMatrix, "SW"),
				"UtilisationMatrix (pkts, routingpfad)", "UtiliUnter", null);

		// inititalisieren von der Rechnungsmatrix
		String[][] calc = new String[graph.getEdges().size() + 1][6];

		for (int i = 0; i < graph.getEdges().size() + 1; i++) {
			for (int j = 0; j < 6; j++) {
				calc[i][j] = "";
			}
		}
		calc[0] = new String[] { "Edge", "Capacity (pkts/sec)", "Usage (pkts/sec)", "Avg. Usage (pkts/sec)",
				"Avg. Delay (msec/pkts)", "Weight" };
		for (int i = 0; i < graph.getEdges().size(); i++) {
			calc[i + 1][0] = graph.getEdges().get(i).getA().getLabel() + graph.getEdges().get(i).getB().getLabel();
		}
		StringMatrix calcMatrix = lang.newStringMatrix(new Offset(50, 0, utiliStrMatrix, "NE"), calc, "calcMatrix",
				null, mp);
		SourceCode sc = lang.newSourceCode(new Offset(10, 0, calcMatrix, "SW"), "sourceCode", null, sourceCode);

		sc.addCodeLine("public void flowCalculator(Graph graph, int[][] utilisation){ ", null, 0, null);
		sc.addCodeLine("for(edge e : allEdges){", null, 1, null);
		sc.addCodeLine("int c = e.getCapacity();", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("for(edge e : graph.getAllEdges){", null, 1, null);
		sc.addCodeLine("int u = utilisation(e.getNodeFrom(), e.getNodeTo());", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("for(edge e: graph.getAllEdges){", null, 1, null);
		sc.addCodeLine("int avL = e.getAverageLoad()", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("for(edge e : graph.getAllEdges){", null, 1, null);
		sc.addCodeLine("int tDel = (1/(c[e.getId()] – avL[e.getId()])) * 1000;", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("for(edge e : graph.getAllEdges){", null, 1, null);
		sc.addCodeLine("double weight = avL(e.getId()] / sum(avL);", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("double avDel = 0;", null, 1, null);
		sc.addCodeLine("for(edge e : graph.getAllEdges){", null, 1, null);
		sc.addCodeLine("avDel =+ weight[e.getId()] * tDel[e.getId()];", null, 2, null);
		sc.addCodeLine("{", null, 1, null);
		sc.addCodeLine("}", null, 0, null);

		MultipleSelectionQuestionModel q1 = new MultipleSelectionQuestionModel("MS");
		q1.setPrompt("Wofür stehen die Gewichte der Kanten im Graph");
		q1.addAnswer("Kapazität", 1, "Genau, die Zahl steht für die die Verbindungskapazität in Pakete/Sekunde ");
		q1.addAnswer("Pfadlänge", 0, "Falsch, Richtig wäre die Kapazität gewesen");
		q1.addAnswer("Auslastung", 0, "Falsch, Richtig wäre die Kapazität gewesen");
		lang.addMSQuestion(q1);

		lang.nextStep();
		beschreibung1.setText("- Lese die Kapazität im Graphen ab -", null, null);
		sc.highlight(0);
		lang.nextStep();
		sc.unhighlight(0);

		/*
		 * 1
		 */
		int[] capacity = new int[graph.getEdges().size()];
		for (int i = 0; i < graph.getEdges().size(); i++) {
			sc.highlight(1);
			lang.nextStep();
			sc.unhighlight(1);
			sc.highlight(2);
			capacity[i] = graph.getEdges().get(i).getWeight();
			calcMatrix.put(i + 1, 1, "" + capacity[i], null, null);
			visualGraph.highlightNode(graph.getEdges().get(i).getA().getId(), null, null);
			visualGraph.highlightNode(graph.getEdges().get(i).getB().getId(), null, null);
			visualGraph.highlightEdge(graph.getEdges().get(i).getA().getId(), graph.getEdges().get(i).getB().getId(),
					null, null);
			calcMatrix.highlightCell(i + 1, 1, null, null);
			lang.nextStep();
			visualGraph.unhighlightEdge(graph.getEdges().get(i).getA().getId(), graph.getEdges().get(i).getB().getId(),
					null, null);
			visualGraph.unhighlightNode(graph.getEdges().get(i).getA().getId(), null, null);
			visualGraph.unhighlightNode(graph.getEdges().get(i).getB().getId(), null, null);
			calcMatrix.unhighlightCell(i + 1, 1, null, null);
			sc.unhighlight(2);
		}

		/*
		 * 2
		 */
		beschreibung1.setText("- Lese die Utilisation aus der Utilisation Matrix ab -", null, null);
		int[] utili = new int[graph.getEdges().size()];
		for (int i = 0; i < graph.getEdges().size(); i++) {
			sc.highlight(4);
			lang.nextStep();
			sc.unhighlight(4);
			sc.highlight(5);
			utili[i] = utilisation[graph.getEdges().get(i).getA().getId()][graph.getEdges().get(i).getB().getId()];
			calcMatrix.put(i + 1, 2, "" + utili[i], null, null);
			calcMatrix.highlightCell(i + 1, 2, null, null);
			utiliStrMatrix.highlightCell(graph.getEdges().get(i).getA().getId() + 1,
					graph.getEdges().get(i).getB().getId() + 1, null, null);
			lang.nextStep();
			utiliStrMatrix.unhighlightCell(graph.getEdges().get(i).getA().getId() + 1,
					graph.getEdges().get(i).getB().getId() + 1, null, null);
			calcMatrix.unhighlightCell(i + 1, 2, null, null);
			sc.unhighlight(5);
		}
		/*
		 * 3
		 */
		beschreibung1.setText(
				"-suche in der UtilisationMatrix jeden Eintrag der die Kante enthält und addiere die Ergebnisse -",
				null, null);
		int[] averageLoad = new int[graph.getEdges().size()];
		int sum = 0;
		for (int i = 0; i < graph.getEdges().size(); i++) {
			sc.highlight(7);
			if (i == 3) {
				MultipleSelectionQuestionModel q2 = new MultipleSelectionQuestionModel("MS");
				q2.setPrompt("Welches ist die richtige durchschnittliche Nutzung der nächsten Kante");
				q2.addAnswer("15", 0,
						"Es müssen alle Pakete aus der Utilisationmatrix, die den Pfad enthalten zusammengezählt werden");
				q2.addAnswer("" + averageLoad[i], 1, "Super! Das ist die richtige Antwort");
				q2.addAnswer("0", 0, "Es müssen alle Pakete aus der Utilisationmatrix, die den Pfad enthalten zusammengezählt werden");
				lang.addMSQuestion(q2);
			}
			lang.nextStep();
			sc.unhighlight(7);
			sc.highlight(8);

			List<Pair<Integer, Integer>> usedEdges = getUsedEdges(graph, utilisation, ps,
					graph.getEdges().get(i).getA(), graph.getEdges().get(i).getB());

			averageLoad[i] = getAverageLoad(usedEdges, utilisation);
			sum = sum + averageLoad[i];
			calcMatrix.put(i + 1, 3, "" + averageLoad[i], null, null);
			calcMatrix.highlightCell(i + 1, 3, null, null);
			for (Pair<Integer, Integer> e : usedEdges) {
				if (!(e == null))
					utiliStrMatrix.highlightCell(e.getFirst() + 1, e.getSecond() + 1, null, null);
			}
			lang.nextStep();
			calcMatrix.unhighlightCell(i + 1, 3, null, null);
			for (Pair<Integer, Integer> e : usedEdges) {
				if (!(e == null))
					utiliStrMatrix.unhighlightCell(e.getFirst() + 1, e.getSecond() + 1, null, null);
			}
			sc.unhighlight(8);
		}

		/*
		 * 4
		 */
		beschreibung1.setText("-Bestimme die Zeitverzögerung anhand der Formel und der schon berechneten Werte -", null,
				null);
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		Text formel = lang.newText(new Offset(0, -15, calcMatrix, "NW"),
				"Avg. Delay = ( 1 / ( capacity - Avg. Load ) ) * 1000", "timeDelayFormel", null, tp);
		Font textFont = new Font("SansSerif", Font.BOLD, 16);
		formel.setFont(textFont, null, null);

		double[] timeDelay = new double[graph.getEdges().size()];
		for (int i = 0; i < graph.getEdges().size(); i++) {
			sc.highlight(10);
			lang.nextStep();
			sc.unhighlight(10);
			sc.highlight(11);
			timeDelay[i] = Math.round(((1.0 / (capacity[i] - averageLoad[i])) * 1000) * 1000) / 1000.0;
			calcMatrix.put(i + 1, 4, "" + timeDelay[i], null, null);
			calcMatrix.highlightCell(i + 1, 4, null, null);
			calcMatrix.highlightCell(i + 1, 1, null, null);
			calcMatrix.highlightCell(i + 1, 3, null, null);
			lang.nextStep();
			calcMatrix.unhighlightCell(i + 1, 4, null, null);
			calcMatrix.unhighlightCell(i + 1, 1, null, null);
			calcMatrix.unhighlightCell(i + 1, 3, null, null);
			sc.unhighlight(11);
		}

		formel.setText("Weight = Avg. Usage / Sum ( Avg. Usage )", null, null);

		lang.nextStep();
		beschreibung1.setText("- Bestimme die Summe der Durschnittlichen Nutzung -", null, null);
		for (int i = 0; i < calcMatrix.getNrRows(); i++) {
			calcMatrix.highlightCell(i, 3, null, null);
		}

		// calcMatrix.highlightElemRowRange(1, calcMatrix.getNrRows() - 1 , 3,
		// defaultDuration, defaultDuration);

		lang.nextStep();

		formel.setText("Weight = Avg. Usage / " + sum, null, null);

		lang.nextStep();
		beschreibung1.setText("- Berechne die Gewichte anhand der Formel und den schon ausgerechneten Werten -", null,
				null);

		for (int i = 0; i < calcMatrix.getNrRows(); i++) {
			calcMatrix.unhighlightCell(i, 3, null, null);
		}

		// calcMatrix.unhighlightElemRowRange(1, calcMatrix.getNrRows() -1 , 3,
		// defaultDuration, defaultDuration);

		/*
		 * 5
		 */
		double[] weight = new double[graph.getEdges().size()];
		for (int i = 0; i < graph.getEdges().size(); i++) {
			sc.highlight(13);
			lang.nextStep();
			sc.unhighlight(13);
			sc.highlight(14);
			weight[i] = Math.round(((double) averageLoad[i] / sum) * 1000) / 1000.0;
			calcMatrix.put(i + 1, 5, "" + weight[i], null, null);
			calcMatrix.highlightCell(i + 1, 3, null, null);
			calcMatrix.highlightCell(i + 1, 5, null, null);

			lang.nextStep();

			calcMatrix.unhighlightCell(i + 1, 3, null, null);
			calcMatrix.unhighlightCell(i + 1, 5, null, null);
			sc.unhighlight(14);
		}

		lang.nextStep();
		beschreibung1.setText("- Berechne die durschnittliche Verzögerung des ganzen Netzwerks mit der Formel -", null,
				null);

		formel.setText("totalAverageDelay = sum( weight[i] * timeDelay[i])", null, null);

		double totalAverageDelay = 0.0;
		for (int i = 0; i < graph.getEdges().size(); i++) {
			sc.highlight(17);
			lang.nextStep();
			sc.unhighlight(17);
			sc.highlight(18);
			calcMatrix.highlightCell(i + 1, 5, null, null);
			calcMatrix.highlightCell(i + 1, 4, null, null);
			totalAverageDelay = totalAverageDelay + (weight[i] * timeDelay[i]);
			formel.setText("totalAverageDelay = sum( weight[i] * timeDelay[i]) = " + totalAverageDelay, null, null);
			lang.nextStep();
			sc.unhighlight(18);
			calcMatrix.unhighlightCell(i + 1, 5, null, null);
			calcMatrix.unhighlightCell(i + 1, 4, null, null);
		}

		formel.setText("totalAverageDelay = " + totalAverageDelay, null, null);

		lang.nextStep();
		beschreibung1.hide();
		sc.hide();
		formel.hide();
		visualGraph.hide();
		graphUnter.hide();
		calcMatrix.hide();
		utiliStrMatrix.hide();
		utiliStrMatrixUnter.hide();
		lang.nextStep();

	}

	/**
	 * 
	 * @param graph
	 * @return
	 */
	public PathStore[][] bellmannford(Graph graph) {
		PathStore[][] pathStore = initPath(graph);

		// Anzahl der Iterationen nodes-1
		for (int iteration = 2; iteration < graph.getNodes().size() + 1; iteration++) {
			// durch pathStore durchgehen
			for (int von = 0; von < pathStore.length; von++) {
				for (int zu = 0; zu < pathStore.length; zu++) {
					if (von != zu) {
						// für jeden Nachbarknoten
						for (Graphnode n : graph.getNeighbors(graph.getNodes().get(von))) {

							if (pathStore[n.getId()][zu].getLength() < Integer.MAX_VALUE) {
								// speichere den Weg mit den wenigsten
								// übergängen

								if (pathStore[n.getId()][zu].getLength() + 1 < pathStore[von][zu].getLength()) {
									int newCapacity = pathStore[von][n.getId()].getCapacity()
											+ pathStore[n.getId()][zu].getCapacity();
									pathStore[von][zu].setCapacity(newCapacity);

									ArrayList<Edge> path = new ArrayList<Edge>();
									path.add(graph.getEdge(graph.getNode(von), n));
									path.addAll(pathStore[n.getId()][zu].getPath());

									pathStore[von][zu].setPath(path);

									pathStore[von][zu].setLength(pathStore[von][zu].getPath().size());

								}

								if (pathStore[n.getId()][zu].getLength() + 1 == pathStore[von][zu].getLength()) {

									int newCapacity = pathStore[von][n.getId()].getCapacity()
											+ pathStore[n.getId()][zu].getCapacity();
									if (pathStore[von][zu].getCapacity() < newCapacity) {
										pathStore[von][zu].setCapacity(newCapacity);
										ArrayList<Edge> path = new ArrayList<Edge>();
										path.add(graph.getEdge(graph.getNode(von), n));
										path.addAll(pathStore[n.getId()][zu].getPath());

										pathStore[von][zu].setPath(path);

										pathStore[von][zu].setLength(pathStore[von][zu].getPath().size());
									}

								}
							}
						}
					}
				}
			}

		}
		return pathStore;
	}

	/**
	 * 
	 */
	private void showDescription() {
		Slide slide = new Slide(lang, "resources/beschreibung.txt", "beschreibung", new DefaultStyle());
		slide.hide();
	}

	/**
	 * 
	 */
	private void showEndText() {
		Slide slide = new Slide(lang, "resources/endText.txt", "ende", new DefaultStyle());
		slide.hide();
	}

	/**
	 * 
	 * @param start
	 * @param destination
	 * @param path
	 * @return
	 */
	private String getDisplayPath(String start, String destination, List<Edge> path) {
		if (start.equals(destination))
			return "";

		List<String> result = new ArrayList<String>();
		result.add(start);

		List<Edge> visitedEdges = new ArrayList<Edge>();

		while (!result.get(result.size() - 1).equals(destination)) {
			String currentNode = result.get(result.size() - 1);

			for (Edge e : path) {
				if (visitedEdges.contains(e))
					continue;

				if (e.getA().getLabel().equals(currentNode)) {
					result.add(e.getB().getLabel());
					visitedEdges.add(e);
					break;
				}
				if (e.getB().getLabel().equals(currentNode)) {
					result.add(e.getA().getLabel());
					visitedEdges.add(e);
					break;
				}
			}
		}

		String finalString = "";
		for (String s : result)
			finalString += s;

		return finalString;
	}

	/**
	 * 
	 */
	private void showHeadline() {
		TextProperties tp = new TextProperties();

		Text title = lang.newText(new Coordinates(70, 30), "Flow Based Distance Routing", "title", null, tp);
		title.setFont(new Font("SansSerif", Font.BOLD, 30), null, null);
		title.show();
	}

	/**
	 * 
	 * @param graph
	 * @param utilisation
	 * @param a
	 * @param b
	 * @return
	 */
	public List<Pair<Integer, Integer>> getUsedEdges(Graph graph, int[][] utilisation, PathStore[][] pathStore,
			Graphnode a, Graphnode b) {
		// int result = 0;
		List<Pair<Integer, Integer>> usedEdges = new ArrayList<Pair<Integer, Integer>>();

		if (!(utilisation.length == pathStore.length)) {
			System.out.println("Exception weil utilisation nicht zum Graph passt");
			return null;
		}

		for (int i = 0; i < pathStore.length; i++) {
			for (int j = i + 1; j < pathStore.length; j++) {
				if (pathStore[i][j].getPath().contains(graph.getEdge(a, b))) {
					usedEdges.add(new Pair<>(i, j));
				}
			}
		}
		return usedEdges;
	}

	/**
	 * 
	 * @param usedEdges
	 * @param utilisation
	 * @return
	 */
	private int getAverageLoad(List<Pair<Integer, Integer>> usedEdges, int[][] utilisation) {
		int result = 0;
		for (Pair<Integer, Integer> e : usedEdges) {
			if (!(e == null)) {
				result = result + utilisation[e.getFirst()][e.getSecond()];
			} else {
				continue;
			}
		}
		return result;
	}

	/**
	 * Converts a algoanim.privitibes.Graph to a FlowBasedRoutingGraph
	 * 
	 * @param visualGraph
	 *            to be converted
	 * @return the FlowBasedRoutingGraph
	 */
	public Graph toGraph(algoanim.primitives.Graph visualGraph) {
		ArrayList<Graphnode> nodes = new ArrayList<Graphnode>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (int i = 0; i < visualGraph.getNodes().length; i++) {
			nodes.add(new Graphnode(visualGraph.getNodeLabel(visualGraph.getNodes()[i])));
		}
		for (int i = 0; i < visualGraph.getAdjacencyMatrix().length; i++) {
			for (int j = i; j < visualGraph.getAdjacencyMatrix().length; j++) {
				if (!(visualGraph.getAdjacencyMatrix()[i][j] == 0)) {
					edges.add(new Edge(nodes.get(i), nodes.get(j),
							visualGraph.getEdgeWeight(visualGraph.getNodeForIndex(i), visualGraph.getNodeForIndex(j))));
				}
			}
		}
		Graph graph = new Graph(edges, nodes);
		return graph;
	}

	/**
	 * @param graph,
	 *            the graph the
	 * @return
	 */
	public PathStore[][] initPath(Graph graph) {
		PathStore[][] myPathStore = new PathStore[graph.getNodes().size()][graph.getNodes().size()];
		for (int i = 0; i < graph.getNodes().size(); i++) {
			for (int j = i; j < graph.getNodes().size(); j++) {
				if (graph.getNodes().get(i).getId() == graph.getNodes().get(j).getId()) {
					PathStore ps = new PathStore();
					ArrayList<Edge> path = new ArrayList<Edge>();
					ps.setLength(0);
					ps.setCapacity(0);
					ps.setPath(path);
					myPathStore[graph.getNodes().get(i).getId()][graph.getNodes().get(j).getId()] = ps;
				} else if (graph.getNeighbors(graph.getNodes().get(i)).contains(graph.getNodes().get(j))) {
					PathStore ps = new PathStore();
					ArrayList<Edge> path = new ArrayList<Edge>();
					path.add(graph.getEdge(graph.getNodes().get(i), graph.getNodes().get(j)));
					int capacity = graph.getEdge(graph.getNodes().get(i), graph.getNodes().get(j)).getWeight();
					ps.setLength(1);
					ps.setCapacity(capacity);
					ps.setPath(path);
					myPathStore[graph.getNodes().get(i).getId()][graph.getNodes().get(j).getId()] = ps;
					myPathStore[graph.getNodes().get(j).getId()][graph.getNodes().get(i).getId()] = ps;
				} else {
					PathStore ps = new PathStore();
					ArrayList<Edge> path = new ArrayList<Edge>();
					ps.setLength(Integer.MAX_VALUE);
					ps.setCapacity(0);
					ps.setPath(path);
					myPathStore[graph.getNodes().get(i).getId()][graph.getNodes().get(j).getId()] = ps;
					myPathStore[graph.getNodes().get(j).getId()][graph.getNodes().get(i).getId()] = ps;
				}
			}
		}
		return myPathStore;
	}

}