package generators.searching.tabusearch;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import algoanim.primitives.Graph;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

/**
 * This class contains several methods that provide plain functionality and
 * calculations for the algorithm and the display which are necessary but
 * irritating and space-consuming for the readability of the relevant classes
 * (especially Algo-class). Therefore, these methods are outsourced to keep the
 * relevant classes clean and readable. In the relevant classes, the purpose of
 * these methods should (hopefully) be clear because of unambiguously chosen
 * method names and descriptions.
 * 
 * @author Sebastian Ritzenhofen, Felix Rauterberg
 *
 */
public class Helper {

	/**
	 * Reference on tabu search instance
	 */
	public static TabuSearchGenerator tabu;

	/**
	 * Labels for the graph and StringArray representation
	 */
	public static String[] nodeLabels = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };

	/**
	 * Converts node character description to integer index in adjacency matrix
	 */
	public static HashMap<String, Integer> NodeNameToNodeNumber;

	/**
	 * Counts iterations of for loop to display in table of contents
	 */
	public static int iterationCount;

	/**
	 * Initializes a map from node labels ("A", "B", etc.) to their indices.
	 * 
	 * @param numberNodes
	 *            the indices of the nodes
	 */
	public static void initNodeNameToNumberMap(int numberNodes) {
		if (NodeNameToNodeNumber != null)
			return;

		NodeNameToNodeNumber = new HashMap<String, Integer>();
		for (int i = 0; i < numberNodes; i++) {
			Helper.NodeNameToNodeNumber.put(nodeLabels[i], i);
		}
	}

	/**
	 * Calculates the cost of the given solution.
	 * 
	 * @param solution
	 *            roundtour for which the cost shall be calculated
	 * @return cost of solution ( = sum of edge weights on solution roundtrip)
	 */
	public static int getSolutionCosts(StringArray solution) {

		int cost = 0;
		int[][] matrix = Input.getAdjacencyMatrix();

		for (int i = 0; i < solution.getLength() - 1; i++) {

			String nodeA = solution.getData(i);
			String nodeB = solution.getData(i + 1);
			int nodeOne = NodeNameToNodeNumber.get(nodeA);
			int nodeTwo = NodeNameToNodeNumber.get(nodeB);

			cost += matrix[nodeOne][nodeTwo];
		}
		String nodeA = solution.getData(solution.getLength() - 1);
		String nodeB = solution.getData(0);
		int nodeOne = NodeNameToNodeNumber.get(nodeA);
		int nodeTwo = NodeNameToNodeNumber.get(nodeB);

		cost += matrix[nodeOne][nodeTwo];
		return cost;
	}

	/**
	 * Determines all neighbors for the current solution. The neighbors that will be
	 * forbidden due to the tabu list are still included here.
	 */
	public static void generateNeighborhood() {

		// reset used lists and sets
		tabu.currentNeighborLists = new ArrayList<StringArray>();
		tabu.currentNeighborCosts = new ArrayList<Text>();
		tabu.currentNeighborSwaps = new ArrayList<Text>();
		tabu.currentNeighbors = new ArrayList<Neighbor>();

		// define array properties
		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Input.getSolutionArrayProps().get(AnimationPropertiesKeys.FILL_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
				Input.getSolutionArrayProps().get(AnimationPropertiesKeys.FILLED_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Input.getSolutionArrayProps().get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);

		// define text properties
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		textProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
		textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);

		// create a deep copy of the initial solution
		String[] copy = new String[tabu.currentSolList.getLength()];
		for (int i = 0; i < tabu.currentSolList.getLength(); i++) {
			copy[i] = tabu.currentSolList.getData(i);
		}

		// create neighbors by swapping the elements
		StringArray lastNeighbor = null;
		for (int i = 1; i < tabu.currentSolList.getLength() - 1; i++) {
			for (int j = i + 1; j < tabu.currentSolList.getLength(); j++) {

				// get position for neighbor
				Node coord = getNextNeighborPos(lastNeighbor);

				// create neighbor by swapping
				StringArray neighbor = tabu.lang.newStringArray(coord, copy, "neighbor", null, arrayProps);
				neighbor.swap(i, j, null, null);
				lastNeighbor = neighbor;

				// build neighbor properties
				int cost = getSolutionCosts(neighbor);

				Text costText = tabu.lang.newText(new Offset(0, 25, neighbor, "SW"), "Kosten: " + cost, "costs", null,
						textProps);
				String swapString = "(" + tabu.currentSolList.getData(i) + "," + tabu.currentSolList.getData(j) + ")";
				Text swapText = tabu.lang.newText(new Offset(0, 40, neighbor, "SW"), "Tausch: " + swapString, "swap",
						null, textProps);

				// fill lists and create neighbor instance
				tabu.currentNeighborLists.add(neighbor);
				tabu.currentNeighborCosts.add(costText);
				tabu.currentNeighborSwaps.add(swapText);
				tabu.currentNeighbors.add(new Neighbor(cost, swapString, neighbor, costText, swapText));
			}
		}

		// ask the user a question
		if (Input.getUseQuestions()) {
			Helper.initBeforeNeighborQuestionModels();
			int index = Helper.getRandomQuestion(tabu.beforeNeighborQuestions);
			Helper.showQuestion(tabu.beforeNeighborQuestions.get(index));
		}

		// reset neighbor position index
		tabu.lastNeighborPos = null;
	}

	/**
	 * Calculates positions for the neighborhood lists on the display.
	 * 
	 * @return position for information of next neighbor in neighborhood
	 */
	public static Node getNextNeighborPos(StringArray neighbor) {
		if (neighbor == null)
			return new Coordinates(750, 400);
		return new Offset(20, 0, neighbor, "NE");
	}

	/**
	 * Adjusts graph to only show the edges given by the StringArray list.
	 * 
	 * @param graph
	 * @param s
	 *            StringArray that represents roundtrip
	 */
	public static void showEdges(Graph graph, StringArray s) {

		// hide all edges first
		for (int i = 0; i < s.getLength(); i++)
			for (int j = 0; j < s.getLength(); j++)
				graph.hideEdge(i, j, null, null);

		// show only wanted edges
		for (int i = 0; i < s.getLength() - 1; i++)
			graph.showEdge(NodeNameToNodeNumber.get(s.getData(i)), NodeNameToNodeNumber.get(s.getData(i + 1)), null,
					null);
		graph.showEdge(NodeNameToNodeNumber.get(s.getData(s.getLength() - 1)), NodeNameToNodeNumber.get(s.getData(0)),
				null, null);
	}

	/**
	 * Switches swap representation.
	 * 
	 * @param swap
	 * @return reversed swap representation
	 */
	public static String reverseSwap(String swap) {
		return "(" + swap.charAt(3) + "," + swap.charAt(1) + ")";
	}

	/**
	 * Finds and returns all neighbors that are excluded by the given tabu list
	 * element.
	 * 
	 * @param tabuSwap
	 *            an element from the tabu list
	 * @return neighbors excluded by this element
	 */
	public static Neighbor findTabuNeighbor(String tabuSwap) {

		// return null if given string is empty
		if (tabuSwap.length() < 5) {
			return null;
		}

		// for each neighbor
		for (Neighbor n : tabu.currentNeighbors) {

			// return this neighbor if it matches with given tabu
			if (n.swap.equals(tabuSwap)) {
				return n;
			}
		}

		// return null if no match found
		return null;
	}

	/**
	 * Determines best neighbor from neighborhood solutions (not considering the
	 * tabu neighbors)
	 * 
	 * @return best of all non-tabu neighbors
	 */
	public static Neighbor getBestNeighbor() {

		Neighbor best = tabu.currentNeighbors.get(0);
		for (Neighbor n : tabu.currentNeighbors)
			if (n.cost < best.cost)
				best = n;
		return best;
	}

	/**
	 * Checks if the current solution is better than the stored global best
	 * solution.
	 * 
	 * @return true if current solution is better than global best, false otherwise
	 */
	public static boolean currentIsBetterThanGlobalBest() {

		String currentCostString = tabu.currentSolCost.getText().substring(8, tabu.currentSolCost.getText().length());
		String bestCostString = tabu.bestSolCost.getText().substring(8, tabu.bestSolCost.getText().length());
		int currentCost = Integer.valueOf(currentCostString);
		int bestCost = Integer.valueOf(bestCostString);

		return (currentCost < bestCost) ? true : false;
	}

	/**
	 * Resets the tabu pseudo-code to adjust the index in the for-loop.
	 */
	public static void updateCode() {

		tabu.tabuCode.hide();
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 16));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Input.getPseudoCodeProps().get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Input.getPseudoCodeProps().get(AnimationPropertiesKeys.COLOR_PROPERTY));
		tabu.tabuCode = tabu.lang.newSourceCode(new Coordinates(720, 80), "tabuCode", null, scProps);
		tabu.tabuCode.addCodeLine("01. currentSol = GenerateRandomSol()", null, 0, null);
		tabu.tabuCode.addCodeLine("02. bestSol = currentSol", null, 0, null);
		tabu.tabuCode.addCodeLine(
				"03. for (int i=" + (tabu.iterationCount++) + "; i<" + Input.getNumberOfIterations() + "; i++) {", null,
				0, null);
		tabu.tabuCode.addCodeLine("04.     neighborhood = GenerateNeighborhood(currentSol)", null, 0, null);
		tabu.tabuCode.addCodeLine("05.     neighborhood = removeTabus(neighborhood)", null, 0, null);
		tabu.tabuCode.addCodeLine("06.     currentSol = getBestNeighbor(neighborhood)", null, 0, null);
		tabu.tabuCode.addCodeLine("07.     if (currentSol.betterThan(bestSol))", null, 0, null);
		tabu.tabuCode.addCodeLine("08.         bestSol = currentSol", null, 0, null);
		tabu.tabuCode.addCodeLine("09.     updateTabuList()", null, 0, null);
		tabu.tabuCode.addCodeLine("10. }", null, 0, null);
		tabu.tabuCode.addCodeLine("11. return bestSol", null, 0, null);
		tabu.tabuCode.addCodeLine("", null, 0, null);
	}

	/**
	 * Deletes/hides a given StringArray.
	 * 
	 * @param array
	 */
	public static void deleteStringArray(StringArray array) {
		array.setTextColor(0, Input.getNumberOfNodes() - 1, Color.WHITE, null, null);
		array.setFillColor(0, Input.getNumberOfNodes() - 1, Color.WHITE, null, null);
		array.unhighlightCell(0, Input.getNumberOfNodes() - 1, null, null);
		array.setBorderColor(0, Input.getNumberOfNodes() - 1, Color.WHITE, null, null);
		array.showIndices(false, null, null);
	}

	/**
	 * Deletes/hides a given text element.
	 * 
	 * @param textElement
	 */
	public static void deleteText(Text textElement) {
		textElement.setText("", null, null);
	}

	/**
	 * Deletes/hides a given graph.
	 * 
	 * @param graph
	 */
	public static void deleteGraph(Graph graph) {
		int size = graph.getSize();

		for (int i = 0; i < size; i++) {
			graph.hideNode(i, null, null);
		}
	}

	/**
	 * Gets the number of nodes in the TSP graph and returns the positions of the
	 * nodes on the display for the current solution graph.
	 * 
	 * @param numberOfNodes
	 * @return positions of graph nodes
	 */
	public static Point[] getCurrentSolNodeOrder(int numberOfNodes) {
		Point[] nodeOrder = new Point[] { new Point(200, 230), new Point(70, 270), new Point(330, 270),
				new Point(40, 350), new Point(360, 350), new Point(70, 430), new Point(330, 430), new Point(200, 470) };

		return getNodeOrder(nodeOrder, numberOfNodes);
	}

	/**
	 * Gets the number of nodes in the TSP graph and returns the positions of the
	 * nodes on the display for the previous solution graph.
	 * 
	 * @param numberOfNodes
	 * @return positions of graph nodes
	 */
	public static Point[] getPrevSolNodeOrder(int numberOfNodes) {
		Point[] nodeOrder = new Point[] { new Point(520, 140), new Point(460, 160), new Point(580, 160),
				new Point(440, 210), new Point(600, 210), new Point(460, 260), new Point(580, 260),
				new Point(520, 280) };

		return getNodeOrder(nodeOrder, numberOfNodes);
	}

	/**
	 * Gets the number of nodes in the TSP graph and returns the positions of the
	 * nodes on the display for the best solution graph.
	 * 
	 * @param numberOfNodes
	 * @return positions of graph nodes
	 */
	public static Point[] getBestSolNodeOrder(int numberOfNodes) {
		Point[] nodeOrder = new Point[] { new Point(520, 450), new Point(460, 470), new Point(580, 470),
				new Point(440, 520), new Point(600, 520), new Point(460, 570), new Point(580, 570),
				new Point(520, 590) };

		return getNodeOrder(nodeOrder, numberOfNodes);
	}

	/**
	 * For better representation, different node constellations are used for odd or
	 * even graph node numbers.
	 * 
	 * @param nodeOrder
	 * @param numberOfNodes
	 * @return the positions to be actually used for the graph representation
	 */
	private static Point[] getNodeOrder(Point[] nodeOrder, int numberOfNodes) {
		if (numberOfNodes == 8) {
			return nodeOrder;

		} else if (numberOfNodes % 2 == 0) {
			Point[] evenOrder = new Point[numberOfNodes];
			for (int i = 1; i <= numberOfNodes; i++) {
				evenOrder[i - 1] = nodeOrder[i];
			}
			return evenOrder;

		} else {
			Point[] oddOrder = new Point[numberOfNodes];

			for (int i = 0; i < numberOfNodes; i++) {
				oddOrder[i] = nodeOrder[i];
			}
			return oddOrder;
		}
	}

	/**
	 * Initialize general multiple choice question for the user of the animation
	 */
	public static void initGeneralQuestionModels() {
		tabu.generalQuestions = new ArrayList<QuestionModel>();

		// add general questions
		MultipleSelectionQuestionModel multQuest = new MultipleSelectionQuestionModel("g_termination");
		multQuest.setPrompt("Wann ist es denkbar, dass die Tabusuche terminiert?");
		multQuest.addAnswer("Nach einer festgelegten Anzahl von Iterationen", 1, "Richtig!");
		multQuest.addAnswer("Nach Finden einer besseren Lösung", 0,
				"Leider falsch! Nachdem eine bessere Lösung gefunden wurde, kann man im Allgemeinen nicht davon ausgehen, sich der global besten Lösung genähert zu haben.");
		multQuest.addAnswer("Nach einem festgeleten Zeitlimit", 1, "Korrekt!");
		multQuest.addAnswer("Nach einer bestimmten Anzahl an Iterationen ohne Finden einer besseren Lösung", 1,
				"Stimmt!");
		multQuest.setGroupID("general");
		multQuest.setNumberOfTries(2);
		tabu.generalQuestions.add(multQuest);

		MultipleChoiceQuestionModel choiceQuest = new MultipleChoiceQuestionModel("g_nxtNeighbor");
		choiceQuest.setPrompt("Von welcher Lösung aus wird in einem Iterationsschritt die Nachbarschaft erzeugt?");
		choiceQuest.addAnswer("Von der aktuellen Lösung ausgehend", 1, "Richtig!");
		choiceQuest.addAnswer("Von der vorherigen Lösung ausgehend", 0,
				"Leider falsch! Die aktuelle Lösung stellt einen Nachbar der vorherigen Lösung dar.");
		choiceQuest.addAnswer("Von der bisher besten gefunden Lösung ausgehend", 0,
				"Das stimmt leider nicht! Die bisher beste Lösung wird lediglich gespeichert, da während der Ausführung der Tabusuche auch schlechtere Lösungen gefunden werden können.");
		choiceQuest.setGroupID("general");
		choiceQuest.setNumberOfTries(2);
		tabu.generalQuestions.add(choiceQuest);

		choiceQuest = new MultipleChoiceQuestionModel("g_terminationSolution");
		choiceQuest.setPrompt(
				"Ist es möglich, dass der Algorithmus mit einer schlechteren Lösung terminiert als der Startlösung?");
		choiceQuest.addAnswer("Das ist richtig", 0,
				"Das ist nicht möglich, da die beste gefundene Lösung immer separat abgespeichert wird.");
		choiceQuest.addAnswer("Das ist nicht richtig", 1, "Korrekte Antwort!");
		choiceQuest.setGroupID("general");
		choiceQuest.setNumberOfTries(2);
		tabu.generalQuestions.add(choiceQuest);

		choiceQuest = new MultipleChoiceQuestionModel("g_solLocalSearch");
		choiceQuest.setPrompt(
				"Kann die Tabusuche Lösungen finden, die die lokale Suche nicht finden kann (unabhängig vom Beispiel)?");
		choiceQuest.addAnswer("Das ist richtig", 1, "Die Antwort ist richtig!");
		choiceQuest.addAnswer("Das ist nicht richtig", 0,
				"Falsch; die Tabusuche kann durch den Einsatz der Tabuliste lokale Optima überwinden, was die lokale Suche nicht kann.");
		choiceQuest.setGroupID("general");
		choiceQuest.setNumberOfTries(2);
		tabu.generalQuestions.add(choiceQuest);

		choiceQuest = new MultipleChoiceQuestionModel("g_fullTabuList");
		choiceQuest.setPrompt("Was passiert, wenn die Tabuliste voll ist?");
		choiceQuest.addAnswer("Die Tabuliste wird geleert und eine neue Liste wird angelegt", 0,
				"Leider falsch! Dadurch wäre die Wahrscheinlichkeit größer, in lokale Optima zu verfallen.");
		choiceQuest.addAnswer(
				"Das am längsten in der Liste stehende Element wird gelöscht und der aktuelle Tausch wird eingefügt", 1,
				"Korrekt!");
		choiceQuest.addAnswer("Es werden keine neuen Elemente mehr eingefügt und auch keine gelöscht", 0,
				"Das stimmt leider nicht! Dadurch würde sich die Wahrscheinlichkeit erhöhen in lokale Optima zu verfallen.");
		choiceQuest.setGroupID("general");
		choiceQuest.setNumberOfTries(2);
		tabu.generalQuestions.add(choiceQuest);

		choiceQuest = new MultipleChoiceQuestionModel("g_AimOfTabuList");
		choiceQuest.setPrompt("Was ist das hauptsächliche Ziel der Tabuliste?");
		choiceQuest.addAnswer("Das Vermeiden bzw. Überwinden von lokalen Optima", 1, "Das ist richtig!");
		choiceQuest.addAnswer("Steigerung der Effizienz des Algorithmus", 0,
				"Das ist falsch! Durch die Tabuliste wird verhindert, dass kürzliche Änderungen gleich wieder rückgängig gemacht werden, um lokale Optima zu überwinden.");
		choiceQuest.setGroupID("general");
		choiceQuest.setNumberOfTries(2);
		tabu.generalQuestions.add(choiceQuest);

		choiceQuest = new MultipleChoiceQuestionModel("g_tabuListIterationSolutions");
		choiceQuest.setPrompt(
				"Kann die Tabuliste dafür sorgen, dass während eines Iterationsschritts eine schlechtere als die aktuelle Lösung gewählt wird?");
		choiceQuest.addAnswer("Das ist richtig", 1, "Richtige Antwort!");
		choiceQuest.addAnswer("Das ist nicht richtig", 0,
				"Falsch; die Tabuliste vermeidet das 'Zurückfallen' in lokale Optima. Dafür ist es nötig, in einigen Iterationsschritten schlechtere Lösungen zu akzeptieren.");
		choiceQuest.setGroupID("general");
		choiceQuest.setNumberOfTries(2);
		tabu.generalQuestions.add(choiceQuest);
	}

	private static int neighborIDCounter = 0;

	/**
	 * Initialize neighborhood multiple choice questions for the user
	 */
	public static void initBeforeNeighborQuestionModels() {

		tabu.beforeNeighborQuestions = new ArrayList<QuestionModel>();

		/*
		 * CREATE VALID NEIGHBOR
		 */
		int index = (int) (Math.random() * (tabu.currentNeighborLists.size() - 1));
		StringArray neighbor = tabu.currentNeighborLists.get(index);

		// add question
		String neighborString = new String();
		for (int i = 0; i < neighbor.getLength(); i++) {
			neighborString += "[" + neighbor.getData(i) + "] ";
		}

		MultipleChoiceQuestionModel choiceQuest = new MultipleChoiceQuestionModel("goodNeighbor" + neighborIDCounter++);
		choiceQuest.setPrompt("Wird der Nachbar " + neighborString + " Teil der Nachbarschaft sein?");
		choiceQuest.addAnswer("Das ist richtig", 1, "Die Antwort stimmt!");
		choiceQuest.addAnswer("Das ist nicht richtig", 0,
				"Falsch; der Nachbar unterscheidet sich nur durch einen einzigen Tausch von der aktuellen Lösung und ist deshalb ein gültiger Nachbar.");
		choiceQuest.setGroupID("neighbor");
		choiceQuest.setNumberOfTries(2);
		tabu.beforeNeighborQuestions.add(choiceQuest);

		/*
		 * CREATE INVALID NEIGHBOR
		 */
		String[] falseNeighbor = new String[tabu.currentSolList.getLength()];
		for (int i = 0; i < tabu.currentSolList.getLength(); i++) {
			falseNeighbor[i] = tabu.currentSolList.getData(i);
		}

		// swap two elements to get an invalid neighbor
		String first = falseNeighbor[1];
		String second = falseNeighbor[2];

		falseNeighbor[1] = second;
		falseNeighbor[2] = first;

		// second swap
		first = falseNeighbor[2];
		second = falseNeighbor[3];

		falseNeighbor[2] = second;
		falseNeighbor[3] = first;

		neighborString = new String();
		for (int k = 0; k < falseNeighbor.length; k++) {
			neighborString += "[" + falseNeighbor[k] + "] ";
		}

		// add question
		choiceQuest = new MultipleChoiceQuestionModel("badNeighbor" + neighborIDCounter++);
		choiceQuest.setPrompt("Wird der Nachbar " + neighborString + " Teil der Nachbarschaft sein?");
		choiceQuest.addAnswer("Das ist nicht richtig", 1, "Das ist korrekt!");
		choiceQuest.addAnswer("Das ist richtig", 0,
				"Falsch; im Gegensatz zur aktuellen Lösung besitzt der Nachbar mehr als einen Tausch und ist deswegen kein gültiger Nachbar.");
		choiceQuest.setGroupID("neighbor");
		choiceQuest.setNumberOfTries(2);
		tabu.beforeNeighborQuestions.add(choiceQuest);
	}

	/**
	 * Selects an random question from the given question list
	 * 
	 * @param questionList
	 *            A list of QuestionModels
	 * @return A random index of the list, to select a question to ask
	 */
	public static int getRandomQuestion(List<QuestionModel> questionList) {
		return (int) (Math.random() * questionList.size());
	}

	/**
	 * Shows the prompt of the given question
	 * 
	 * @param question
	 *            Question to asks
	 */
	public static void showQuestion(QuestionModel question) {
		if (question instanceof MultipleSelectionQuestionModel) {
			tabu.lang.addMSQuestion((MultipleSelectionQuestionModel) question);
		} else if (question instanceof MultipleChoiceQuestionModel) {
			tabu.lang.addMCQuestion((MultipleChoiceQuestionModel) question);
		} else if (question instanceof TrueFalseQuestionModel) {
			tabu.lang.addTFQuestion((TrueFalseQuestionModel) question);
		}

	}

}
