package generators.network;

import generators.network.graph.anim.AdjacencyMatrixView;
import generators.network.graph.anim.DistanceView;

import java.util.Hashtable;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.CodeView;
import algoanim.animalscript.addons.bbcode.H3;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.util.Offset;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * This class provides advanced functionality for all generators implementing graph algorithms 
 * and any generators depending on graph input.
 *
 */
public abstract class AbstractGraphGenerator extends AbstractNetworkGenerator {
	
	/**
	 * Create a algoanim.primitives.Graph object from the primitives received from the UI.
	 * 
	 * @param primitives A table of name, primitive pairs
	 * @return a Graph object
	 */
	protected Graph getGraphFromPrimitives(Hashtable<String, Object> primitives) {
		Object graphPrim = primitives.get("graph");	
		return (Graph) graphPrim;
	}
	
	/**
	 * Create a view of the adjacency matrix.
	 * 
	 * @param g The graph of which the matrix should be displayed
	 * @return The adjacency matrix view
	 */
	protected AdjacencyMatrixView getMatrix(Graph g) {
		return new AdjacencyMatrixView(l, new Offset(20, 20, "header2", 
		    AnimalScript.DIRECTION_SW), s, g,  translator.translateMessage("ADJACENCY_MATRIX"), H3.BB_CODE);
	}
	
	/**
	 * Create a source code view. By convention all content is read from {textResource}_SOURCECODE unless a
	 * constant named SOURCECODE is present in the translator's text resource file. 
	 * 
	 * @return The source code primitive created
	 */
	protected SourceCode getCode() {
		// get the source code from a file
		SourceCode c = CodeView.primitiveFromFile(l, getResource("SOURCECODE", false), "sourceCode", new Offset(30, 0, "graph", AnimalScript.DIRECTION_NE), null, s); 
		return c;
	}
	
	/**
	 * Create a view of the distances from all nodes to a specific vertex in the graph.
	 * 
	 * @param g The graph to generate the view from
	 * @return The view of all distances
	 */
	protected DistanceView getDistanceView(Graph g) {
		return new DistanceView(l, new Offset(0, 70, "graph", AnimalScript.DIRECTION_SW), s, g,  translator.translateMessage("DISTANCE_FROM_NODE", g.getNodeLabel(g.getStartNode())), H3.BB_CODE);
	}
	
	/**
	 * Get the Graph object the algorithm should work on and create view of said graph.
	 * 
	 * @param g The graph to display
	 * @return The graph object as returned by the Language
	 */
	protected Graph getGraph(Graph g) {
		return getGraph(g, false);
	}
	
	/**
	 * Get the Graph object the algorithm should work on and create view of said graph.
	 * 
	 * @param g The graph to display
	 * @param fullMatrix If set to true and the graph is not directed the adjacency matrix is mirrored before graph creation.
	 * @return The graph object as returned by the Language
	 */
	protected Graph getGraph(Graph g, boolean fullMatrix) {
		// set the extended properties of the graph
		GraphProperties props = (GraphProperties) s.getProperties("graph");
		props.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, g.getProperties().get(AnimationPropertiesKeys.WEIGHTED_PROPERTY));
		props.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, g.getProperties().get(AnimationPropertiesKeys.DIRECTED_PROPERTY));

		// make sure we have a source and sink. Just to be on the save side
		if(g.getStartNode() == null) {
			g.setStartNode(g.getNode(0));
		}
		if(g.getTargetNode() == null) {
			g.setTargetNode(g.getNode(g.getSize() - 1));
		}
		
		Graph graph;
		if(fullMatrix && !(Boolean)g.getProperties().get(AnimationPropertiesKeys.DIRECTED_PROPERTY)) {
			// create full matrix
			int[][] matrix = mirrorMatrix(g.getAdjacencyMatrix());
			
			// set new adjacency matrix
			g.setAdjacencyMatrix(matrix);			
		}

		// add the graph to the animation
		graph = l.addGraph(g, g.getDisplayOptions(), props);				
		
		// position the graph
		try {
			graph.moveTo("SW", "Translate", new Offset(20, 20, "header2", AnimalScript.DIRECTION_SW), null, null);
		} catch (IllegalDirectionException e) {
			System.err.println("AbstractNetworkGraphGenerator.getGraph(): graph.moveTo() failed");
		}
		
		return graph;
	}
	
	/**
	 * Mirror a matrix on the first main diagonal
	 * 
	 * @param m The matrix to mirror
	 * @return The mirrored matrix
	 */
	private int[][] mirrorMatrix(int[][] m) {
		for(int i = 0; i < m.length; i++) {
			for(int j = i + 1; j <  m[i].length; j++) {
				m[j][i] = m[i][j];
			}
		}
		return m;
	}
}
