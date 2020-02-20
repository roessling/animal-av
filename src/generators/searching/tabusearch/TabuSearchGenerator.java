/*
 * TabuSearchGenerator.java
 * Felix Rauterberg, Sebastian Ritzenhofen, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.searching.tabusearch;

import java.awt.Font;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.QuestionModel;

public class TabuSearchGenerator implements ValidatingGenerator {

	// Animation Attributes
	public Language lang;
	public Text header;
	public List<Text> algoDescs;
	public List<Text> outroDescs;
	public List<Text> animationDescList;
	public Coordinates lastNeighborPos;
	public StringArray exampleTabuList;
	public Graph exampleGraph1;
	public Graph exampleGraph2;
	public StringArray exampleSolutionList;
	public StringArray exampleSolutionList2;
	public Text currentSolLabel;
	public Text prevSolLabel;
	public Text bestSolLabel;
	public Text neighborhoodLabel;
	public Text tabuListLabel;
	public Text currentSolCost;
	public Text prevSolCost;
	public Text bestSolCost;
	public StringArray currentSolList;
	public StringArray prevSolList;
	public StringArray bestSolList;
	public StringArray tabuList;
	public Graph currentSolGraph;
	public Graph prevSolGraph;
	public Graph bestSolGraph;
	public ArrayBlockingQueue<String> tabuListQueue;
	public SourceCode tabuCode;
	public Rect sourceRect;
	public List<StringArray> currentNeighborLists;
	public List<Text> currentNeighborCosts;
	public List<Text> currentNeighborSwaps;
	public List<Neighbor> currentNeighbors;
	public List<QuestionModel> beforeNeighborQuestions;
	public List<QuestionModel> generalQuestions;
	public int iterationCount;

	// Constants
	public static final Font textFont = new Font("SansSerif", Font.PLAIN, 16);
	public static final Font headerFont = new Font("SansSerif", Font.BOLD, 24);

	public void init() {
		// init animation
		lang = new AnimalScript("Tabusuche am Beispiel des TSP", "Felix Rauterberg, Sebastian Ritzenhofen", 800, 600);
		lang.setStepMode(true);

		// init used classes
		Setup.tabu = this;
		Helper.tabu = this;
		Algo.tabu = this;

		// init user interaction
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		Helper.initGeneralQuestionModels();
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

		// read input
		Input.setAdjacencyMatrix((int[][]) primitives.get("adjacencyMatrix"));
		Input.setNumberOfIterations((Integer) primitives.get("numberOfIterations"));
		Input.setNumberOfNodes((Integer) primitives.get("numberOfNodes"));
		Input.setSizeOfTabuList((Integer) primitives.get("sizeOfTabuList"));
		Input.setUseQuestions((Boolean) primitives.get("useQuestions"));
		Input.setArrayProps((ArrayProperties) props.get(0));
		Input.setPseudoCodeProps((SourceCodeProperties) props.get(1));
		Input.setLabelProps((TextProperties) props.get(2));

		// show tabu search description
		Setup.generateTabuSearchDescription();

		// show description about the animation
		Setup.generateAnimationDescription();

		// generate tabu search perspective
		Setup.generateTabuSearchPerspective();

		// execute tabu search
		this.tabuSearch(Input.getAdjacencyMatrix());

		// show outro text
		Setup.generateOutroText();

		lang.finalizeGeneration();
		return lang.toString();
	}

	public String getName() {
		return "Tabusuche am Beispiel des TSP";
	}

	public String getAlgorithmName() {
		return "Tabusuche";
	}

	public String getAnimationAuthor() {
		return "Felix Rauterberg, Sebastian Ritzenhofen";
	}

	public String getDescription() {
		return "Für die meisten Instanzen NP-schwerer Probleme können exakte Algorithmen keine Lösung in" + "\n"
				+ "annehmbarer Zeit berechnen. Daher existieren verschiedene Alternativen, die nicht versuchen, die"
				+ "\n"
				+ "beste Lösung einer gegebenen Probleminstanz zu finden, sondern nur eine, die ausreichend gut ist"
				+ "\n" + "(dafür aber in annehmbarer Zeit)." + "\n"
				+ "Eine solche Alternative ist die Tabusuche. Sie stellt eine Art Erweiterung der lokalen Suche dar und"
				+ "\n" + "ist dadurch in der Lage, lokale Optima zu überwinden.";
	}

	public void tabuSearch(int[][] adjacencyMatrix) {

		// pick random solution, set best solution to current solution
		Algo.getRandomSolution();
		Algo.setBestToCurrentSol(true);

		// for given number of iterations
		for (int i = 0; i < Input.getNumberOfIterations(); i++) {

			Algo.getNeighborhood();
			Algo.removeTabus();
			Algo.getBestNeighbor();
			if (Algo.currentBetterThanBest()) {
				Algo.setBestToCurrentSol(false);
			}
			Algo.updateTabuList();
		}
		Algo.returnResult();
	}

	public String getCodeExample() {
		return "01. currentSol = GenerateRandomSol()" + "\n" + "02. bestSol = currentSol" + "\n"
				+ "03. for (int i=0; i<4; i++) " + "\n" + "04.     neighborhood = GenerateNeighborhood(currentSol)"
				+ "\n" + "05.     neighborhood = removeTabus(neighborhood)" + "\n"
				+ "06.     currentSol = getBestNeighbor(neighborhood)" + "\n"
				+ "07.     if (currentSol.betterThan(bestSol))" + "\n" + "08.         bestSol = currentSol" + "\n"
				+ "09.     updateTabuList()" + "\n" + "10. }" + "\n" + "11. return bestSol";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {

		int[][] adjacnecyMatrix = (int[][]) primitives.get("adjacencyMatrix");
		int numberOfIterations = (Integer) primitives.get("numberOfIterations");
		int numberOfNodes = (Integer) primitives.get("numberOfNodes");
		int tabuListSize = (Integer) primitives.get("sizeOfTabuList");

		if (tabuListSize <= 0) {
			return false;
		}

		if (numberOfNodes != adjacnecyMatrix.length) {
			return false;
		}

		if (numberOfNodes <= 3 || numberOfNodes >= 9) {
			return false;
		}

		if (numberOfIterations <= 0) {
			return false;
		}

		if (adjacnecyMatrix.length != adjacnecyMatrix[0].length) {
			return false;
		}

		return true;
	}

}