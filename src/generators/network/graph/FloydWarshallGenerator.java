package generators.network.graph;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.AbstractGraphGenerator;
import generators.network.graph.anim.AdjacencyMatrixView;
import generators.network.graph.anim.DistanceView;
import generators.network.helper.ClassName;

import java.util.Hashtable;
import java.util.Locale;

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
 * The algorithm searches for the shortest path in a weighted graph. 
 */
public class FloydWarshallGenerator extends AbstractGraphGenerator {
	/**
	 * defines whether the matrix is shown before the graph or not
	 */
	private static final boolean DEFAULT_SHOW_MATRIX = true;
	
	/**
	 * Create a new generator with the default locale 'German'
	 */
	public FloydWarshallGenerator() {
		this(Locale.GERMANY);
	}
	
	/**
	 * Create a new generator
	 * 
	 * @param myLocale The locale setting to use
	 */
	public FloydWarshallGenerator(Locale myLocale) {
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
	 */
	private FloydWarshallGenerator(Language lang, Locale myLocale) {
		this(myLocale);
		
		s = new NetworkStyle();
		
		l = lang;
		l.setStepMode(true);
	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		// get the graph created by the user
		Graph g = getGraphFromPrimitives(primitives);
		
		// Create a new animation
		init();
		FloydWarshallGenerator anim = new FloydWarshallGenerator(l, locale);
		
		// build headline 
		anim.getHeader();
		l.nextStep(translator.translateMessage("LBL_TITLE_SLIDE"));

		// build title
		anim.getTitleSlide();
		
		// get the graph
		g = anim.getGraph(g, true);
		
		// get the adjacency matrix (and show it first)
		AdjacencyMatrixView m = anim.getMatrix(g);
		if(getShowMatrixFromPrimitives(primitives)) {
			g.hide();
		} else {
			m.hide();
		}
		
		// display the source code
		SourceCode c = anim.getCode();
		
		// display the distance view
		DistanceView d = anim.getDistanceView(g);
		
		l.nextStep(translator.translateMessage("LBL_ANIM_START"));
		anim.run(m, g, c, d);

		return l.toString();
	}
	
	/**
	 * Run the actual algorithm and create the AnimalScript code to display an animation.
	 * 
	 * @param m
	 * @param g The graph to analyze
	 * @param c The source code of the algorithm
	 * @param d
	 */
	private void run(AdjacencyMatrixView m, Graph g, SourceCode c , DistanceView d) {
		int[][] dist = g.getAdjacencyMatrix();
		int[][] next = new int[dist.length][dist.length];
		
		c.highlight(0);
		l.nextStep();
		
		// as the adjacency matrix contains 0 for non existent edges
		// we have to update the matrix here
		for(int i = 0; i < dist.length; i++) {
			for(int j = 0; j < dist.length; j++) {
				// but leave the distance from a node to itself alone
				if(dist[i][j] == 0 && i != j) {
					dist[i][j] = Integer.MAX_VALUE;
				}
				next[i][j] = Integer.MAX_VALUE;
				
				// fill the distance view
				if(i == g.getPositionForNode(g.getStartNode())) {
					d.setDistance(j, dist[i][j], null, null);
					d.highlight(j, null, null);
					l.nextStep();
					d.unhighlight(j, null, null);
				}
			}
		}

		// set n as number of vertices in animation
		int n = dist.length;
		c.toggleHighlight(0, 1);
		l.nextStep();
		
		// fill the matrix with all distances (and the intermediate hops)
		c.toggleHighlight(1, 2);
		l.nextStep();
		c.unhighlight(2);
		for(int k = 0; k < n; k++) {
			c.highlight(3);
			l.nextStep();
			c.unhighlight(3);
			for(int i = 0; i < n; i++) {
				c.highlight(4);
				l.nextStep();
				c.unhighlight(4);
				for(int j = 0; j < n; j++) {
					c.highlight(5);
					m.highlightCell(i, k, null, null);
					m.highlightCell(k, j, null, null);
					g.highlightEdge(i, k, null, null);
					g.highlightEdge(k, j, null, null);
					l.nextStep(translator.translateMessage("LBL_EXAMINE_DISTANCE", g.getNodeLabel(i), g.getNodeLabel(j)));
					c.unhighlight(5);
					if(dist[i][k] < Integer.MAX_VALUE && dist[k][j] < Integer.MAX_VALUE && dist[i][k] + dist[k][j] < dist[i][j]) {
						dist[i][j] = dist[i][k] + dist[k][j];
						c.highlight(6);
						m.put(i, j, dist[i][j], null, null);
						m.highlightCell(i, j, null, null);
						g.highlightNode(i, null, null);
						g.highlightNode(j, null, null);
						if(i == g.getPositionForNode(g.getStartNode())) {
							d.setDistance(j, dist[i][j], null, null);
							d.highlight(j, null, null);
						}
						l.nextStep();
						
						next[i][j] = k;
						c.toggleHighlight(6, 7);
						g.highlightNode(k, null, null);
						
						l.nextStep();
						c.unhighlight(7);
					} else {
						l.nextStep();
					}
					d.unhighlight(j, null, null);
					m.unhighlightCell(i, k, null, null);
					m.unhighlightCell(k, j, null, null);
					m.unhighlightCell(i, j, null, null);
					g.unhighlightEdge(i, k, null, null);
					g.unhighlightEdge(k, j, null, null);
					g.unhighlightNode(i, null, null);
					g.unhighlightNode(j, null, null);
					g.unhighlightNode(k, null, null);
				}
			}
		}
		l.nextStep();
		c.highlight(11);
		
		// find a path from source to sink
		if(g.getStartNode() != null && g.getTargetNode() != null) {
			l.nextStep();
			m.hide();
			g.show();
			c.unhighlight(11);
			getPath(g, g.getPositionForNode(g.getStartNode()), g.getPositionForNode(g.getTargetNode()), dist, next, c);
		}
	}
	
	/**
	 * Find the path between two nodes on the given matrix
	 * 
	 * @param g The graph to work on (needed for highlights)
	 * @param source The source of the path
	 * @param sink The target of the path
	 * @param distance The distance matrix of the graph
	 * @param neighbors The neighborhood relation of the graph (next hop on path)
	 * @param c The source code to highlight
	 */
	private void getPath(Graph g, int source, int sink, int[][] distance, int[][] neighbors, SourceCode c) {
		c.highlight(13);
		g.highlightNode(g.getStartNode(), null, null);
		l.nextStep();
		c.toggleHighlight(13, 14);
		l.nextStep();
		c.unhighlight(14);
		if(distance[source][sink] == Integer.MAX_VALUE) {
			c.highlight(15);
			l.nextStep();
		} else {
			int intermediate = neighbors[source][sink];
			c.highlight(17);
			l.nextStep();
			c.toggleHighlight(17, 18);
			l.nextStep();
			c.unhighlight(18);
			
			if(intermediate == Integer.MAX_VALUE) {
				c.highlight(19);
				g.highlightNode(source, null, null);
				g.highlightEdge(source, sink, null, null);
				g.highlightNode(sink, null, null);
				
				l.nextStep(translator.translateMessage("LBL_ADD_EDGE", g.getNodeLabel(source), g.getNodeLabel(sink)));
				c.unhighlight(19);
			} else {
				c.highlight(21);
				l.nextStep();
				c.unhighlight(21);
				getPath(g, source, intermediate, distance, neighbors, c);
				getPath(g, intermediate, sink, distance, neighbors, c);
			}
		}
		c.highlight(23);
		l.nextStep();
		c.unhighlight(23);
	}
	
	/**
	 * Check the user input if the matrix should be shown before the graph
	 * 
	 * @param primitives The primitives received by the UI
	 * @return Show the matrix
	 */
	private boolean getShowMatrixFromPrimitives(Hashtable<String, Object> primitives) {
		boolean show;
		Object showPrim = primitives.get("showMatrix");
		if(showPrim instanceof Boolean) {
			show = (Boolean) showPrim;
		} else {
			show = DEFAULT_SHOW_MATRIX;
		}
		return show;
	}

	/**
	 * Due to limitations in Animal this algorithm only works on directed graphs.
	 */
	@Override
	public String getDescription() {
		String desc = Slide.getTeaser(getResource("TITLESLIDE"));
		return desc.concat("<p>" + translator.translateMessage("DIRECTED_GRAPH_MESSAGE") + "</p>");
	}
}
