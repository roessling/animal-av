package generators.searching.tabusearch;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import algoanim.primitives.Text;
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

/**
 * This class contains the setup and metawork like creating the description
 * slides and the general layout of the displayed headers, labels, graphs etc.
 * 
 * @author Sebastian Ritzenhofen, Felix Rauterberg
 *
 */
public class Setup {

	/**
	 * Reference on tabu search instance
	 */
	public static TabuSearchGenerator tabu;

	/**
	 * Generates the first slide with a general description of the tabu search
	 * algorithm.
	 */
	public static void generateTabuSearchDescription() {

		/*
		 * TABU SEARCH DESCRIPTION
		 */

		tabu.header = tabu.lang.newText(new Coordinates(20, 30), "Tabusuche", "header", null);
		tabu.header.setFont(TabuSearchGenerator.headerFont, null, null);

		// generate the description text
		tabu.algoDescs = new ArrayList<Text>();
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 20, "header", "SW"),
				"Die Tabusuche ist ein metaheuristisches Verfahren zur naeherungsweisen Loesung komplexer Optimierungsprobleme, die zugleich eine Art Erweiterung der lokalen Suche darstellt.",
				"algoDesc1", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc1", "SW"),
				"Fuer weitere Informationen bezueglich Metaheuristiken und lokaler Suche empfehlen wir die Wikipedia Artikel 'Metaheuristik' und 'Lokale Suche'.",
				"algoDesc2", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 15, "algoDesc2", "SW"),
				"Das Grundkonzept der Tabusuche besteht darin, ein lokales Suchverfahren mit zusaetzlicher Gedaechtnisstruktur auszufuehren, die helfen soll, das Verfahren auf intelligente Art und Weise",
				"algoDesc3", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc3", "SW"),
				"durch den Loesungsraum zu steuern. Waehrend einer Iteration wird dazu die beste Loesung aus den zur aktuellen Loesung benachbarten Loesungen ausgewaehlt, selbst wenn diese schlechter ist",
				"algoDesc4", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc4", "SW"),
				"als die bisher gefundene beste Loesung. Aus diesem Grund ist es bei der Tabusuche (im Gegensatz zur gewoehnlichen lokalen Suche) noetig, neben der aktuellen Loesung auch die beste",
				"algoDesc5", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc5", "SW"),
				"bisher gefundene Loesung zu vermerken. Die Strategie, immer die beste benachbarte Loesung zu waehlen, selbst wenn diese schlechter ist als die aktuelle Loesung, soll helfen, lokale Optima zu",
				"algoDesc6", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc6", "SW"),
				"ueberwinden und demzufolge Loesungen zu finden, die besser sind als das lokale Optimum.", "algoDesc7",
				null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc7", "SW"),
				"Im Gegensatz zur lokalen Suche kann die Tabusuche demnach nicht beendet werden, sobald kein besserer Nachbar mehr gefunden werden kann; ein neues Terminierungskriterium ist",
				"algoDesc8", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc8", "SW"),
				"notwendig. Sinnvolle Kriterien zum Abbruch der Suche sind beispielsweise die Anzahl der Iterationsschritte insgesamt, die Anzahl der Iterationsschritte nach dem Finden der bisher besten",
				"algoDesc9", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc9", "SW"),
				"Loesung oder eine zeitliche Begrenzung.", "algoDesc9.1", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 15, "algoDesc9.1", "SW"),
				"Doch was hat das alles mit Tabus und Gedaechtnisstrukturen zu tun?", "algoDesc10", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc10", "SW"),
				"Wurde ausgehend von einer aktuellen Loesung A eine Nachbarschaft erzeugt und daraus Nachbar B ausgewaehlt, so wird die Aenderung, die den Wechsel von Loesung A zu Loesung B herbei-",
				"algoDesc11", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc11", "SW"),
				"gefuehrt hat, fuer eine gewisse Anzahl an Iterationen auf eine Tabuliste gesetzt. Die Tabuliste soll bewirken, dass der Algorithmus bei Annehmen einer schlechteren Loesung nicht gleich wieder",
				"algoDesc12", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc12", "SW"),
				"zu der eigentlich besseren Loesung zurueckkehrt, um so lokale Optima ueberwinden zu koennen; um dies umzusetzen, werden die Loesungen einer Nachbarschaft, die sich aus einer auf der",
				"algoDesc13", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc13", "SW"),
				"Tabuliste vermerkten Aenderung ergeben, bei der Wahl der besten Nachbarloesung nicht beruecksichtigt.",
				"algoDesc14", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 15, "algoDesc14", "SW"),
				"In dieser Animation soll die Tabusuche anhand des Traveling Salesman Problems (TSP) dargestellt werden. Bei diesem Problem besteht die Eingabe aus einem Graphen mit gewichteten",
				"algoDesc15", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc15", "SW"),
				"Kanten; Ziel ist es, eine Reihenfolge fuer den Besuch aller Knoten des Graphen so zu waehlen, dass kein Knoten ausser der ersten mehr als einmal besucht wird, die gesamte 'Reisestrecke'",
				"algoDesc16", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc16", "SW"),
				"moeglichst kurz und die erste Station gleich der letzten Station ist.", "algoDesc17", null));

		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 15, "algoDesc17", "SW"),
				"Die Nachbarschaft einer Loesung sei hierbei ueber alle Loesungen definiert, die durch den Austausch zweier Knoten aus der aktuellen Loesung hervorgehen. Betrachtet man die aktuelle Loesung",
				"algoDesc18", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc18", "SW"),
				"als die Reihenfolge, in der die Knoten auf der Rundtour besucht werden, besteht die Nachbarschaft somit aus allen Knotenreihenfolgen, die sich durch das Tauschen zweier Knoten erzeugen",
				"algoDesc19", null));
		tabu.algoDescs.add(tabu.lang.newText(new Offset(0, 3, "algoDesc19", "SW"), "lassen.", "algoDesc20", null));

		// set font to description elements
		for (Text desc : tabu.algoDescs) {
			desc.setFont(TabuSearchGenerator.textFont, null, null);
		}

	}

	/**
	 * Generates the second slide with a more specific description and details about
	 * the following animation.
	 */
	public static void generateAnimationDescription() {

		/*
		 * CLEANUP
		 */

		tabu.lang.nextStep("Einleitung");
		tabu.lang.hideAllPrimitivesExcept(tabu.header);
		tabu.animationDescList = new ArrayList<Text>();

		/*
		 * DESCRIPTION PART 1
		 */

		tabu.animationDescList.add(tabu.lang.newText(new Offset(0, 20, "header", "SW"),
				"In der folgenden Animation zur Darstellung der Tabusuche fuer das TSP wird folgender Beispielgraph verwendet:",
				"animDesc1", null));

		/*
		 * EXAMPLE GRAPH 1
		 */

		GraphProperties graphProps = new GraphProperties();
		graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);
		graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);

		Node[] positions = new Node[5];
		positions[0] = Node.convertToNode(new Point(250, 100));
		positions[1] = Node.convertToNode(new Point(170, 170));
		positions[2] = Node.convertToNode(new Point(200, 260));
		positions[3] = Node.convertToNode(new Point(300, 260));
		positions[4] = Node.convertToNode(new Point(330, 170));

		String[] labels = new String[5];
		labels[0] = "A";
		labels[1] = "B";
		labels[2] = "C";
		labels[3] = "D";
		labels[4] = "E";

		int[][] matrix = new int[][] { { 0, 7, 10, 10, 7 }, { 7, 0, 7, 10, 10 }, { 10, 7, 0, 7, 10 },
				{ 10, 10, 7, 0, 7 }, { 7, 10, 10, 7, 0 } };

		tabu.exampleGraph1 = tabu.lang.newGraph("ExplanationGraph", matrix, positions, labels, new TicksTiming(20),
				graphProps);

		/*
		 * DESCRIPTION PART 2
		 */

		tabu.animationDescList.add(tabu.lang.newText(new Offset(0, 260, "header", "SW"),
				"Der obige Graph besitzt aus Gruenden der Uebersichtlichkeit keine gerichteten Kanten. Das bedeutet, dass sowohl die Hin- als auch die Rueckrichtung dieselben Kosten haben.",
				"animDesc2", null));
		tabu.animationDescList.add(tabu.lang.newText(new Offset(0, 3, "animDesc2", "SW"),
				"Des weiteren wird der Graph in den folgenden Schritten nicht mehr mit all seinen Kanten angezeigt, sondern nur noch mit denen, die Teil der dargestellten Rundtour sind, um",
				"animDesc3", null));
		tabu.animationDescList.add(tabu.lang.newText(new Offset(0, 3, "animDesc3", "SW"),
				"die Animation uebersichtlicher zu gestalten. Als Terminierungskriterium wird in diesem Beispiel eine Iterationsbegrenzung von vier Iterationen verwendet.",
				"animDesc4", null));

		tabu.animationDescList.add(tabu.lang.newText(new Offset(0, 15, "animDesc4", "SW"),
				"Bevor wir mit der Animation starten, werfen wir noch einen kurzen Blick auf den Aufbau der Tabuliste, die folgendermassen dargestellt wird:",
				"animDesc5", null));

		/*
		 * EXAMPLE TABU LIST
		 */

		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);

		tabu.exampleTabuList = tabu.lang.newStringArray(new Offset(150, 15, "animDesc5", "SW"),
				new String[] { "(B,A)", "(C,B)", "(D,C)" }, "tabulist", null, arrayProps);

		/*
		 * DESCRIPTION PART 3
		 */
		tabu.animationDescList.add(tabu.lang.newText(new Offset(0, 65, "animDesc5", "SW"),
				"Die Tabuliste kann als Queue interpretiert werden, die in diesem Beispiel eine Kapazitaet von drei Elementen hat. An Indexposition 2 steht das Element, das als erstes in die",
				"animDesc6", null));
		tabu.animationDescList.add(tabu.lang.newText(new Offset(0, 3, "animDesc6", "SW"),
				"Tabuliste eingefuegt wurde. Beim Einfuegen eines weiteren Elements in die Tabuliste wuerde das neue Element an Indexposition 0 gesetzt, alle bestehenden Elemente um einen",
				"animDesc7", null));
		tabu.animationDescList.add(tabu.lang.newText(new Offset(0, 3, "animDesc7", "SW"),
				"Schritt nach rechts verrueckt und das letzte Element (sofern die Queue voll ist) von der Tabuliste entfernt.",
				"animDesc8", null));

		tabu.animationDescList.add(tabu.lang.newText(new Offset(0, 15, "animDesc8", "SW"),
				"Beschreibt man eine Rundtour des TSP nicht mit einem Graphen, sondern in tabellarischer Form, so koennte eine gueltige Roundtour des obigen Graphen wie folgt aussehen:",
				"animDesc9", null));

		/*
		 * EXAMPLE SOLUTION LIST 1
		 */

		tabu.exampleSolutionList = tabu.lang.newStringArray(new Offset(150, 15, "animDesc9", "SW"),
				new String[] { "A", "C", "E", "B", "D" }, "solution", null, arrayProps);

		/*
		 * DESCRIPTION PART 4
		 */
		tabu.animationDescList.add(tabu.lang.newText(new Offset(0, 65, "animDesc9", "SW"),
				"Der Startknoten steht an Indexposition 0 und wird nicht mehr an letzter Stelle der Tabelle genannt, da die Rundtour definitionsgemaess auch wieder am Startknoten schliessen muss.",
				"animDesc10", null));
		tabu.animationDescList.add(tabu.lang.newText(new Offset(0, 3, "animDesc10", "SW"),
				"Der dazugehoerige Graph sieht folgendermassen aus:", "animDesc11", null));

		/*
		 * EXAMPLE GRAPH 2
		 */

		int[][] adjacencyMatrix = new int[5][5];
		adjacencyMatrix[0][2] = 10;
		adjacencyMatrix[2][4] = 10;
		adjacencyMatrix[4][1] = 10;
		adjacencyMatrix[1][3] = 10;
		adjacencyMatrix[3][0] = 10;

		positions = new Node[5];
		positions[0] = Node.convertToNode(new Point(250, 660));
		positions[1] = Node.convertToNode(new Point(170, 720));
		positions[2] = Node.convertToNode(new Point(200, 810));
		positions[3] = Node.convertToNode(new Point(300, 810));
		positions[4] = Node.convertToNode(new Point(330, 720));

		tabu.exampleGraph2 = tabu.lang.newGraph("ExampleGraph", adjacencyMatrix, positions, labels, new TicksTiming(20),
				graphProps);

		/*
		 * DESCRIPTION PART 5
		 */

		tabu.animationDescList.add(tabu.lang.newText(new Offset(0, 210, "animDesc11", "SW"),
				"Tauschen wir nun die Positionen der Elemente an den Indexpositionen 1 und 4, so dass sich folgende Rundtour ergibt:",
				"animDesc12", null));

		/*
		 * EXAMPLE SOLUTION LIST 2
		 */

		tabu.exampleSolutionList2 = tabu.lang.newStringArray(new Offset(150, 15, "animDesc12", "SW"),
				new String[] { "A", "D", "E", "B", "C" }, "solution", null, arrayProps);

		/*
		 * DESCRIPTION PART 6
		 */

		tabu.animationDescList.add(tabu.lang.newText(new Offset(0, 65, "animDesc12", "SW"),
				"Dann bezeichnen wir den stattgefundenen Tausch als (C,D), da der erstgenannte Knoten die Position des zweitgenannten eingenommen hat und umgekehrt. Auf der Tabuliste",
				"animDesc13", null));
		tabu.animationDescList.add(tabu.lang.newText(new Offset(0, 3, "animDesc13", "SW"),
				"wird ein solcher Tausch der Uebersicht halber direkt mit dem Gegentausch (D,C) vermerkt, der auftreten wuerde, wenn dieselben Knoten wieder von einem Tausch betroffen sind.",
				"animDesc14", null));

		tabu.animationDescList.add(tabu.lang.newText(new Offset(0, 15, "animDesc14", "SW"),
				"Hinweis: Als Eingabe kann diese Animation, aus Platzgruenden, nur Graphen verarbeiten, die zwischen vier und acht Knoten besitzen!",
				"animDesc15", null));

		// set the font of description elements
		for (Text desc : tabu.animationDescList) {
			desc.setFont(TabuSearchGenerator.textFont, null, null);
		}

		// ask an question to the user
		if (Input.getUseQuestions()) {
			int index = Helper.getRandomQuestion(tabu.generalQuestions);
			Helper.showQuestion(tabu.generalQuestions.get(index));
		}
	}

	/**
	 * Generates the layout and constantly put artifacts that are displayed
	 * throughout the animation (e.g. label, graph and list positions, header,
	 * pseudocode, ...)
	 */
	public static void generateTabuSearchPerspective() {

		/*
		 * CLEANUP
		 */

		tabu.lang.nextStep();
		tabu.exampleSolutionList.hide();
		tabu.exampleTabuList.hide();
		tabu.exampleSolutionList2.hide();
		tabu.lang.hideAllPrimitivesExcept(tabu.header);
		Helper.iterationCount = 0;

		/*
		 * ATTRUBUTES
		 */
		tabu.tabuListQueue = new ArrayBlockingQueue<String>(Input.getSizeOfTabuList());

		/*
		 * GENERAL GRAPH PROPERTIES
		 */

		GraphProperties graphProps = new GraphProperties();
		graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.BLACK);
		graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);

		/*
		 * CURRENT SOLUTION GRAPH
		 */

		Node[] positions = new Node[Input.getNumberOfNodes()];
		String[] labels = new String[Input.getNumberOfNodes()];

		for (int i = 0; i < positions.length; i++) {
			positions[i] = Node.convertToNode(Helper.getCurrentSolNodeOrder(positions.length)[i]);
			labels[i] = Helper.nodeLabels[i];
		}

		tabu.currentSolGraph = tabu.lang.newGraph("currentSolGraph", Input.getAdjacencyMatrix(), positions, labels,
				new TicksTiming(20), graphProps);

		/*
		 * PREV SOLUTION GRAPH
		 */

		positions = new Node[Input.getNumberOfNodes()];
		labels = new String[Input.getNumberOfNodes()];

		for (int i = 0; i < positions.length; i++) {
			positions[i] = Node.convertToNode(Helper.getPrevSolNodeOrder(positions.length)[i]);
			labels[i] = Helper.nodeLabels[i];
		}

		tabu.prevSolGraph = tabu.lang.newGraph("prevSolGraph", Input.getAdjacencyMatrix(), positions, labels,
				new TicksTiming(20), graphProps);

		/*
		 * BEST SOLUTION GRAPH
		 */

		positions = new Node[Input.getNumberOfNodes()];
		labels = new String[Input.getNumberOfNodes()];

		for (int i = 0; i < positions.length; i++) {
			positions[i] = Node.convertToNode(Helper.getBestSolNodeOrder(positions.length)[i]);
			labels[i] = Helper.nodeLabels[i];
		}

		tabu.bestSolGraph = tabu.lang.newGraph("bestSolGraph", Input.getAdjacencyMatrix(), positions, labels,
				new TicksTiming(20), graphProps);

		/*
		 * LISTS
		 */

		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Input.getSolutionArrayProps().get(AnimationPropertiesKeys.COLOR_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Input.getSolutionArrayProps().get(AnimationPropertiesKeys.FILL_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
				Input.getSolutionArrayProps().get(AnimationPropertiesKeys.FILLED_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Input.getSolutionArrayProps().get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Input.getSolutionArrayProps().get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Input.getSolutionArrayProps().get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));

		tabu.currentSolList = tabu.lang.newStringArray(new Offset(-55, -105, "currentSolGraph", "N"),
				new String[Input.getNumberOfNodes()], "currentSolList", null, arrayProps);
		tabu.prevSolList = tabu.lang.newStringArray(new Offset(-45, -80, "prevSolGraph", "N"),
				new String[Input.getNumberOfNodes()], "prevSolList", null, arrayProps);
		tabu.bestSolList = tabu.lang.newStringArray(new Offset(-45, -80, "bestSolGraph", "N"),
				new String[Input.getNumberOfNodes()], "bestSolList", null, arrayProps);
		tabu.tabuList = tabu.lang.newStringArray(new Offset(1130, 0, "header", "NE"),
				new String[Input.getSizeOfTabuList()], "tabuList", null, arrayProps);
		for (int i = 0; i < tabu.tabuList.getLength(); i++)
			tabu.tabuList.put(i, "( )", null, null);

		/*
		 * TABU SEARCH PSEUDO CODE
		 */

		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 16));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Input.getPseudoCodeProps().get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Input.getPseudoCodeProps().get(AnimationPropertiesKeys.COLOR_PROPERTY));

		tabu.iterationCount = 0;

		tabu.tabuCode = tabu.lang.newSourceCode(new Coordinates(720, 80), "tabuCode", null, scProps);
		tabu.tabuCode.addCodeLine("01. currentSol = GenerateRandomSol()", null, 0, null);
		tabu.tabuCode.addCodeLine("02. bestSol = currentSol", null, 0, null);
		tabu.tabuCode.addCodeLine(
				"03. for (int i=" + tabu.iterationCount + "; i<" + Input.getNumberOfIterations() + "; i++) {", null, 0,
				null);
		tabu.tabuCode.addCodeLine("04.     neighborhood = GenerateNeighborhood(currentSol)", null, 0, null);
		tabu.tabuCode.addCodeLine("05.     neighborhood = removeTabus(neighborhood)", null, 0, null);
		tabu.tabuCode.addCodeLine("06.     currentSol = getBestNeighbor(neighborhood)", null, 0, null);
		tabu.tabuCode.addCodeLine("07.     if (currentSol.betterThan(bestSol))", null, 0, null);
		tabu.tabuCode.addCodeLine("08.         bestSol = currentSol", null, 0, null);
		tabu.tabuCode.addCodeLine("09.     updateTabuList()", null, 0, null);
		tabu.tabuCode.addCodeLine("10. }", null, 0, null);
		tabu.tabuCode.addCodeLine("11. return bestSol", null, 0, null);
		tabu.tabuCode.addCodeLine("", null, 0, null);

		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		tabu.sourceRect = tabu.lang.newRect(new Offset(-5, -5, tabu.tabuCode, "NW"),
				new Offset(5, 5, tabu.tabuCode, "SE"), "sourceRect", null, rectProps);

		/*
		 * LABELS
		 */

		// define text properties
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		textProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
		textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);

		tabu.currentSolLabel = tabu.lang.newText(new Offset(120, 40, "header", "SW"), "Aktuelle Loesung", "", null,
				textProps);
		tabu.currentSolLabel.setFont(TabuSearchGenerator.textFont, null, null);

		tabu.prevSolLabel = tabu.lang.newText(new Offset(-55, -110, "prevSolGraph", "N"), "Vorherige Loesung", "", null,
				textProps);
		tabu.prevSolLabel.setFont(TabuSearchGenerator.textFont, null, null);

		tabu.bestSolLabel = tabu.lang.newText(new Offset(-55, -110, "bestSolGraph", "N"), "Bisher beste Loesung", "",
				null, textProps);
		tabu.bestSolLabel.setFont(TabuSearchGenerator.textFont, null, null);

		tabu.neighborhoodLabel = tabu.lang.newText(new Offset(30, 30, "tabuCode", "SW"), "Nachbarschaft", "", null,
				textProps);
		tabu.neighborhoodLabel.setFont(TabuSearchGenerator.textFont, null, null);

		tabu.currentSolCost = tabu.lang.newText(new Offset(5, 35, "currentSolList", "SW"), "Kosten: ", "SW", null,
				textProps);
		tabu.currentSolCost.setFont(TabuSearchGenerator.textFont, null, null);

		tabu.prevSolCost = tabu.lang.newText(new Offset(10, 30, "prevSolList", "SW"), "Kosten: ", "", null, textProps);
		tabu.prevSolCost.setFont(TabuSearchGenerator.textFont, null, null);

		tabu.bestSolCost = tabu.lang.newText(new Offset(10, 30, "bestSolList", "SW"), "Kosten: ", "", null, textProps);
		tabu.bestSolCost.setFont(TabuSearchGenerator.textFont, null, null);

		tabu.tabuListLabel = tabu.lang.newText(new Offset(1050, 0, "header", "NE"), "Tabuliste", "", null, textProps);
		tabu.tabuListLabel.setFont(TabuSearchGenerator.textFont, null, null);

		/*
		 * HIDE ELEMENTS
		 */

		tabu.currentSolGraph.hide();
		tabu.currentSolCost.hide();
		tabu.currentSolList.hide();
		tabu.prevSolGraph.hide();
		tabu.prevSolCost.hide();
		tabu.prevSolList.hide();
		tabu.bestSolGraph.hide();
		tabu.bestSolCost.hide();
		tabu.bestSolList.hide();
	}

	/**
	 * Generates the last slide with the outro text.
	 */
	public static void generateOutroText() {

		// step
		tabu.lang.nextStep();

		/*
		 * CLEANUP
		 */
		tabu.lang.hideAllPrimitivesExcept(tabu.header);
		Helper.deleteStringArray(tabu.prevSolList);
		Helper.deleteStringArray(tabu.bestSolList);
		Helper.deleteStringArray(tabu.currentSolList);
		Helper.deleteText(tabu.prevSolCost);
		Helper.deleteText(tabu.bestSolCost);
		Helper.deleteText(tabu.currentSolCost);
		Helper.deleteGraph(tabu.prevSolGraph);
		Helper.deleteGraph(tabu.bestSolGraph);
		Helper.deleteGraph(tabu.currentSolGraph);

		/*
		 * OUTRO TEXT
		 */
		tabu.outroDescs = new ArrayList<Text>();
		tabu.outroDescs.add(tabu.lang.newText(new Offset(0, 20, tabu.header, "SW"),
				"In der gezeigten Animation wurde eine optimale Loesung der gegebenen TSP-Instanz mithilfe des Tabusuche-Algorithmus gesucht.",
				"conc1", null));
		tabu.outroDescs.add(tabu.lang.newText(new Offset(0, 3, "conc1", "SW"),
				"Ob tatsaechlich eine optimale Loesung erreicht werden kann, haengt von den Eingabekriterien und der Guete der zufaellig bestimmten",
				"conc2", null));
		tabu.outroDescs.add(tabu.lang.newText(new Offset(0, 3, "conc2", "SW"),
				"Ausgangslage ab; bei den hier zur besseren Veranschaulichung recht klein gehaltenen Eingabemoeglichkeiten (insbesondere wegen",
				"conc3", null));
		tabu.outroDescs.add(tabu.lang.newText(new Offset(0, 3, "conc3", "SW"),
				"der Knotenlimitierung fuer den Eingabegraphen aus Platzgruenden) ist es allerdings sehr wahrscheinlich, dass die Tabusuche eine",
				"conc4", null));
		tabu.outroDescs
				.add(tabu.lang.newText(new Offset(0, 3, "conc4", "SW"), "optimale Loesung liefert.", "conc5", null));
		tabu.outroDescs.add(tabu.lang.newText(new Offset(0, 15, "conc5", "SW"),
				"Generell sind Optimierungsprobleme ein typisches Anwendungsgebiet der Tabusuche. Die wichtigste Voraussetzung zur Anwendung ist",
				"conc6", null));
		tabu.outroDescs.add(tabu.lang.newText(new Offset(0, 3, "conc6", "SW"),
				"dabei, dass eine moeglichst sinnvolle Nachbarschaft konstruiert werden kann. Da zu einer Loesung fuer gewoehnlich eine sehr grosse",
				"conc7", null));
		tabu.outroDescs.add(tabu.lang.newText(new Offset(0, 3, "conc7", "SW"),
				"Anzahl an Nachbarn besteht, kann das Finden einer besten Loesung in der Nachbarschaft allein aus Laufzeitgruenden schnell zum ",
				"conc8", null));
		tabu.outroDescs.add(tabu.lang.newText(new Offset(0, 3, "conc8", "SW"),
				"Problem werden. Deshalb muessen in der Regel einige Optimierungen bei der Suche vorgenommen werden, um den Nachbarschafts-",
				"conc9", null));
		tabu.outroDescs.add(tabu.lang.newText(new Offset(0, 3, "conc9", "SW"),
				"Suchraum systematisch einzuschraenken. Beispiele fuer solche Optimierungen koennten etwa Heuristiken sein, die schwach erscheinende",
				"conc10", null));
		tabu.outroDescs.add(tabu.lang.newText(new Offset(0, 3, "conc10", "SW"),
				"Nachbarloesungen gar nicht erst in der Suche beruecksichtigen.", "conc11", null));

		// set font to outroText
		for (Text text : tabu.outroDescs) {
			text.setFont(TabuSearchGenerator.textFont, null, null);
		}

		/*
		 * ASK THE USER A QUESTION
		 */
		if (Input.getUseQuestions()) {
			int index = Helper.getRandomQuestion(tabu.generalQuestions);
			Helper.showQuestion(tabu.generalQuestions.get(index));
		}

		// step
		tabu.lang.nextStep("Fazit");
		tabu.lang.hideAllPrimitivesExcept(tabu.header);

	}
}
