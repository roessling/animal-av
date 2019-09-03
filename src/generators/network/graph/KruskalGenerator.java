package generators.network.graph;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.AbstractGraphGenerator;
import generators.network.graph.helper.Edge;
import generators.network.helper.ClassName;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import translator.Translator;
import algoanim.animalscript.addons.Slide;
import algoanim.animalscript.addons.bbcode.NetworkStyle;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * Generate an animation showing the functionality of Prim's algorithm.
 * The algorithm searches for the minimum spanning tree in an undirected weighted graph. 
 */
public class KruskalGenerator extends AbstractGraphGenerator {
	/**
	 * use quiz mode
	 */
	private static final boolean DEFAULT_QUIZ_MODE = true;
	private boolean quizMode;
	
	/**
	 * Create a new generator with the default locale 'German'
	 */
	public KruskalGenerator() {
		this(Locale.GERMANY);
	}

	/**
	 * Create a new generator
	 * 
	 * @param myLocale The locale setting to use
	 */
	public KruskalGenerator(Locale myLocale) {
		textResource = ClassName.getPackageAsPath(this) + "resources/" + ClassName.getClassNameOnly(this);
		locale = myLocale;
		translator = new Translator(textResource, locale);
	}
	
	/**
	 * Create a new generator using the given language object.
	 * This constructor is used internally to setup the animation after 
	 * the initial preparation of any primitives.
	 * 
	 * @param lang The Language object to use within the animation
	 * @param myLocale The locale setting to use
	 * @param quiz Use quiz mode
	 */
	private KruskalGenerator(Language lang, Locale myLocale, boolean quiz) {
		this(myLocale);
		
		s = new NetworkStyle();
		
		l = lang;
		l.setStepMode(true);
		
		quizMode = quiz;
	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		// get the graph created by the user
		Graph g = getGraphFromPrimitives(primitives); 
		
		// Create a new animation
		init();
		KruskalGenerator anim = new KruskalGenerator(l, locale, getQuizModeFromPrimitives(primitives));
		
		// build headline 
		anim.getHeader();
		l.nextStep(translator.translateMessage("LBL_TITLE_SLIDE"));
		
		// build title
		anim.getTitleSlide();
		
		// get the graph
		g = anim.getGraph(g);
		
		// display the source code
		SourceCode c = anim.getCode();
		
		l.nextStep(translator.translateMessage("LBL_ANIM_START"));
		anim.run(g, c);

		l.finalizeGeneration();
		
		return l.toString();
	}
	
	/**
	 * Run the actual algorithm and create the AnimalScript code to display an animation.
	 * 
	 * @param g The graph to analyze
	 * @param c The source code of the algorithm
	 */
	private void run(Graph g, SourceCode c) {
		// initialize a random number generator to present questions at random
		Random rand = new Random();
		
		c.highlight(0);

		// initial question
		FillInBlanksQuestionModel qsTotalWeight = new FillInBlanksQuestionModel("qs" + UUID.randomUUID().toString().replace("-", ""));
		qsTotalWeight.setPrompt(translator.translateMessage("QS_TOTAL_WEIGHT"));
		// only show question if in quiz mode
		if(quizMode) {
			l.nextStep(translator.translateMessage("LBL_QS_TOTAL_WEIGHT"));
			l.addFIBQuestion(qsTotalWeight);
			l.nextStep();
		}
		
		// adjacency matrix of the chosen edges
		boolean[][] chosenEdges = new boolean[g.getSize()][g.getSize()];
		c.toggleHighlight(0, 1);
		l.nextStep();
		
		// fill with default values
		for(int i = 0; i < chosenEdges.length; i++) {			
			for(int j = 0; j < chosenEdges[i].length; j++) {
				chosenEdges[i][j] = false;
			}
		}
		
		// get a sorted set of all edges in the graph
		SortedSet<Edge> availableEdges = new TreeSet<Edge>();
		
		// iterate over the whole adjacency matrix
		c.toggleHighlight(1, 2);
		l.nextStep();
		c.unhighlight(2);
		
		int[][] adjMatrix = g.getAdjacencyMatrix(); 
		for(int i = 0; i < adjMatrix.length; i++) {
			for(int j = 0; j < adjMatrix[i].length; j++) {
				if(adjMatrix[i][j] > 0) {
					availableEdges.add(new Edge(g.getNode(i), g.getNode(j), adjMatrix[i][j]));
					
					c.highlight(3);
					g.highlightNode(i, null, null);
					g.highlightNode(j, null, null);
					l.nextStep();
					c.unhighlight(3);
					g.unhighlightNode(i, null, null);
					g.unhighlightNode(j, null, null);
					l.nextStep();
				}
			}
		}
		
		// add the edges one by one according to their weight
		c.highlight(5);
		l.nextStep();
		c.toggleHighlight(5, 6);
		l.nextStep();
		c.toggleHighlight(6, 7);
		l.nextStep();
		c.unhighlight(7);

		
		// ask what edge to be added next
		// needs to be set up here because we might loop more than once
		// if a possible loop in the graph is detected
		MultipleChoiceQuestionModel qsAddEdge = new MultipleChoiceQuestionModel("qs" + UUID.randomUUID().toString().replace("-", ""));
		qsAddEdge.setPrompt(translator.translateMessage("QS_NEXT_EDGE"));
		Object[] edgeArr = availableEdges.toArray();
		
		// these variables are only used for the quiz mode
		boolean loopDetected = false;
		List<Edge> loopEdges = new LinkedList<Edge>();
		int totalWeight = 0;

		int edgeCount = 0;
		while (edgeCount < g.getSize() - 1) {
			// get edge with lowest weight and remove from list
			Edge currentEdge = availableEdges.first();
			
			// only show the question if the user wants the quiz mode
			// a new question is only needed if an edge was added in the last round
			// add a random value not to annoy the user
			if(quizMode && !loopDetected && rand.nextBoolean()) {
				l.nextStep(translator.translateMessage("LBL_QS_NEXT_EDGE"));				
				l.addMCQuestion(qsAddEdge);
				l.nextStep();
			}
			
			// remove the current edge (eg. the edge with the lowest weight) from the set
			availableEdges.remove(currentEdge);
			
			c.highlight(8);
			g.highlightNode(currentEdge.node1, null, null);
			g.highlightNode(currentEdge.node2, null, null);
			l.nextStep();
			
			// detect possible circle
			c.toggleHighlight(8, 9);
			l.nextStep();
			c.unhighlight(9);
			if (!pathExists(chosenEdges, g.getPositionForNode(currentEdge.node1), g.getPositionForNode(currentEdge.node2))) {
				// add edge to the chosen ones
				chosenEdges[g.getPositionForNode(currentEdge.node1)][g.getPositionForNode(currentEdge.node2)] = true;
				// also add the opposite direction
				chosenEdges[g.getPositionForNode(currentEdge.node2)][g.getPositionForNode(currentEdge.node1)] = true;
				
				g.highlightEdge(currentEdge.node1, currentEdge.node2, null, null);
				c.highlight(10);
				l.nextStep(translator.translateMessage("LBL_ADD_EDGE", g.getNodeLabel(currentEdge.node1), g.getNodeLabel(currentEdge.node2)));
				c.unhighlight(10);

				// count the edge and its weight
				edgeCount++;
				totalWeight += currentEdge.weight;
				
				// randomize the possible answers for the "next edge" questions
				// as we do not know the correct answer before we set up the answers down here
				int qsAnsCount = 0;
				while(qsAnsCount < edgeArr.length) {
					int pos = rand.nextInt(edgeArr.length);
					if(edgeArr[pos] != null) {
						Edge qsAnsEdge = (Edge)edgeArr[pos];
						if(qsAnsEdge.equals(currentEdge)) {
						  qsAddEdge.addAnswer(g.getNodeLabel(qsAnsEdge.node1) + " -> " + g.getNodeLabel(qsAnsEdge.node2), 1, 
						      translator.translateMessage("ANS_CORRECT") + ": " + translator.translateMessage("ANS_NEXT_EDGE", g.getNodeLabel(currentEdge.node1) + " -> " + g.getNodeLabel(currentEdge.node2), String.valueOf(currentEdge.weight)));
						} else {
							// a loop would be created
							if(loopEdges.contains(qsAnsEdge)) {
							  qsAddEdge.addAnswer(g.getNodeLabel(qsAnsEdge.node1) + " -> " + g.getNodeLabel(qsAnsEdge.node2), 0, 
							      translator.translateMessage("ANS_WRONG") + ": " + translator.translateMessage("ANS_NEXT_EDGE_LOOP_ERROR", g.getNodeLabel(currentEdge.node1) + " -> " + g.getNodeLabel(currentEdge.node2), String.valueOf(currentEdge.weight)));
							// the edge has a higher weight
							} else if(qsAnsEdge.weight > currentEdge.weight) {
								qsAddEdge.addAnswer(g.getNodeLabel(qsAnsEdge.node1) + " -> " + g.getNodeLabel(qsAnsEdge.node2), 0, 
								    translator.translateMessage("ANS_WRONG") + ": " + translator.translateMessage("ANS_NEXT_EDGE_WEIGHT_ERROR", g.getNodeLabel(currentEdge.node1) + " -> " + g.getNodeLabel(currentEdge.node2), String.valueOf(currentEdge.weight)));
							// the edge is not the next in alphabetical order
							} else if(qsAnsEdge.weight == currentEdge.weight) {
							  qsAddEdge.addAnswer(g.getNodeLabel(qsAnsEdge.node1) + " -> " + g.getNodeLabel(qsAnsEdge.node2), 0, 
							      translator.translateMessage("ANS_WRONG") + ": " + translator.translateMessage("ANS_NEXT_EDGE_SORT_ERROR", g.getNodeLabel(currentEdge.node1) + " -> " + g.getNodeLabel(currentEdge.node2), String.valueOf(currentEdge.weight)));
							// if none match just produce a generic error
							} else {
							  qsAddEdge.addAnswer(g.getNodeLabel(qsAnsEdge.node1) + " -> " + g.getNodeLabel(qsAnsEdge.node2), 0, 
							      translator.translateMessage("ANS_WRONG") + ": " + translator.translateMessage("ANS_NEXT_EDGE", g.getNodeLabel(currentEdge.node1) + " -> " + g.getNodeLabel(currentEdge.node2), String.valueOf(currentEdge.weight)));
							}
						}
						edgeArr[pos] = null;
						qsAnsCount++;
					}
				}
				// also set up the question section for the next round as we now have a definite answer
				qsAddEdge = new MultipleChoiceQuestionModel("qs" + UUID.randomUUID().toString().replace("-", ""));
				qsAddEdge.setPrompt(translator.translateMessage("QS_NEXT_EDGE"));
				edgeArr = availableEdges.toArray();
				
				// reset the loop detector and the edge list
				loopDetected = false;
				loopEdges = new LinkedList<Edge>();
			} else {
				// loop detected
				// no new question is needed for the next round as we did not adge an edge yet
				loopDetected = true;
				
				// to give correct feedback remember the edge
				loopEdges.add(currentEdge);
			}
			g.unhighlightNode(currentEdge.node1, null, null);
			g.unhighlightNode(currentEdge.node2, null, null);
			l.nextStep();
		}
		c.highlight(13);
		
		// add the answer to the questions
		qsTotalWeight.addAnswer(String.valueOf(totalWeight), 1, translator.translateMessage("ANS_TOTAL_WEIGHT", String.valueOf(totalWeight)));
	}
	
	/**
	 * Checks if a path exists between two nodes in a graph
	 * 
	 * @param matrix The path matrix
	 * @param source The id of the source node
	 * @param target The id of the target node
	 * @return If a path exists
	 */
	// TODO we need a better implementation
	private boolean pathExists(boolean[][] matrix, int source, int target) {
		boolean[] neighbors = matrix[source];
		boolean res = false;
		
		
		for(int i = 0; i < neighbors.length && !res; i++) {
			// edge exists
			if(neighbors[i]) {
				// check if current visited node is the target
				if(i == target) {
					res = true;
				} else {
					// make sure we do not go back
					matrix[source][i] = false;
					matrix[i][source] = false;
					// recursive search for a path
					res = pathExists(matrix, i, target);
					// and restore the values
					matrix[source][i] = true;
					matrix[i][source] = true;

				}
			}
		}
		return res;
	}

	/**
	 * Check the user wants to use the quiz mode
	 * 
	 * @param primitives The primitives received by the UI
	 * @return Show the matrix
	 */
	private boolean getQuizModeFromPrimitives(Hashtable<String, Object> primitives) {
		boolean show;
		Object showPrim = primitives.get("quizMode");
		if(showPrim instanceof Boolean) {
			show = (Boolean) showPrim;
		} else {
			show = DEFAULT_QUIZ_MODE;
		}
		return show;
	}
	
	/**
	 * Due to limitations in this algorithm it only works on undirected graphs.
	 */
	@Override
	public String getDescription() {
		String desc = Slide.getTeaser(getResource("TITLESLIDE"));
		return desc.concat("<p>" + translator.translateMessage("UNDIRECTED_GRAPH_MESSAGE") + "</p>");
	}
}
