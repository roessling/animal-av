package generators.searching.tabusearch;

import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

/**
 * This class is used to read and store the input.
 * 
 * @author Sebastian Ritzenhofen, Felix Rauterberg
 */
public class Input {

	/*
	 * PRIMITIVES
	 */

	private static int numberOfIterations; // number of iterations in the for-loop
	private static int numberOfNodes; // number of nodes in the graph
	private static int sizeOfTabuList; // number of max. possible tabu list entries
	private static int[][] adjacencyMatrix;// matrix to define the distance between nodes
	private static boolean useQuestion; // information if multiple choice questions will be used during the animation

	/*
	 * PROPERTIES
	 */

	private static ArrayProperties arrayProps; // user-defined properties for arrays
	private static SourceCodeProperties pseudoCodeProps; // user-defined properties for pseudocode
	private static TextProperties labelProps; // user-defined properties for labels

	/**
	 * @return number of for-loop iterations
	 */
	public static int getNumberOfIterations() {
		return numberOfIterations;
	}

	/**
	 * @return number of nodes for the TSP graph
	 */
	public static int getNumberOfNodes() {
		return numberOfNodes;
	}

	/**
	 * @return size of tabu list
	 */
	public static int getSizeOfTabuList() {
		return sizeOfTabuList;
	}

	/**
	 * @return adjacency matrix with egde weights for the TSP graph
	 */
	public static int[][] getAdjacencyMatrix() {
		Helper.initNodeNameToNumberMap(adjacencyMatrix.length);
		return adjacencyMatrix;
	}

	/**
	 * 
	 * @return information, if multiple choice questions will be used during the
	 *         animation
	 */
	public static boolean getUseQuestions() {
		return useQuestion;
	}

	/**
	 * 
	 * @return properties for arrays
	 */
	public static ArrayProperties getSolutionArrayProps() {
		return Input.arrayProps;
	}

	/**
	 * 
	 * @return properties for pseudo code
	 */
	public static SourceCodeProperties getPseudoCodeProps() {
		return Input.pseudoCodeProps;
	}

	/**
	 * 
	 * @return properties for labels
	 */
	public static TextProperties getLabelProps() {
		return Input.labelProps;
	}

	/**
	 * Set the number of iterations the algorithm will execute
	 * 
	 * @param numberOfIterations
	 *            Number of iterations the algorithm will execute
	 */
	public static void setNumberOfIterations(int numberOfIterations) {
		Input.numberOfIterations = numberOfIterations;
	}

	/**
	 * Set the number of nodes of the input graph
	 * 
	 * @param numberOfNodes
	 *            Number of nodes of the input graph
	 */
	public static void setNumberOfNodes(int numberOfNodes) {
		Input.numberOfNodes = numberOfNodes;
	}

	/**
	 * Set the size of the tabu list that will be used by the tabu search algorithm
	 * 
	 * @param sizeOfTabuList
	 *            number of elements the tabu list can take
	 */
	public static void setSizeOfTabuList(int sizeOfTabuList) {
		Input.sizeOfTabuList = sizeOfTabuList;
	}

	/**
	 * Set the adjacency matrix with the weights of the edges, that will be used as
	 * input graph
	 * 
	 * @param adjacencyMatrix
	 *            adjacency matrix of the input graph
	 */
	public static void setAdjacencyMatrix(int[][] adjacencyMatrix) {
		Input.adjacencyMatrix = adjacencyMatrix;
	}

	/**
	 * 
	 * @param useQuestions
	 *            Sets the information if multiple choice questions will be used
	 *            during the animation
	 */
	public static void setUseQuestions(boolean useQuestions) {
		Input.useQuestion = useQuestions;
	}

	/**
	 * Sets the user-defined array properties
	 * 
	 * @param props
	 *            solution array properties
	 */
	public static void setArrayProps(ArrayProperties props) {
		Input.arrayProps = props;
	}

	/**
	 * Sets the user-defined pseudo code properties
	 * 
	 * @param props
	 *            pseudo code properties
	 */
	public static void setPseudoCodeProps(SourceCodeProperties props) {
		Input.pseudoCodeProps = props;
	}

	/**
	 * Sets the user-defined label properties
	 * 
	 * @param props
	 *            label properties
	 */
	public static void setLabelProps(TextProperties props) {
		Input.labelProps = props;
	}
}
