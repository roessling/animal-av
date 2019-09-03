package generators.network.graph;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.AbstractGraphGenerator;
import generators.network.graph.anim.DistanceView;
import generators.network.graph.helper.Edge;
import generators.network.helper.ClassName;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import translator.Translator;
import algoanim.animalscript.addons.Slide;
import algoanim.animalscript.addons.bbcode.NetworkStyle;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.util.Node;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * Generate an animation showing the functionality of the Bellman-Ford algorithm.
 * The algorithm searches for the shortest path in any weighted graph with 
 * no negative weighted edges.  
 */
public class BellmanFordGenerator extends AbstractGraphGenerator {	
	
	/**
	 * Create a new generator with the default locale 'German'
	 */
	public BellmanFordGenerator() {
		this(Locale.GERMANY);
	}

	/**
	 * Create a new generator
	 * 
	 * @param myLocale The locale setting to use
	 */
	public BellmanFordGenerator(Locale myLocale) {
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
	private BellmanFordGenerator(Language lang, Locale myLocale) {
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
		BellmanFordGenerator anim = new BellmanFordGenerator(l, locale);
		
		// build headline 
		anim.getHeader();
		l.nextStep(translator.translateMessage("LBL_TITLE_SLIDE"));

		// build title
		anim.getTitleSlide();
		
		// get the graph
		g = anim.getGraph(g, true);
		
		// display the source code
		SourceCode c = anim.getCode();

		// display the distance view
		DistanceView d = anim.getDistanceView(g);
		
		l.nextStep(translator.translateMessage("LBL_ANIM_START"));
		anim.run(g, d, c);

		return l.toString();
	}
	
	/**
	 * Run the actual algorithm and create the AnimalScript code to display an animation.
	 * 
	 * @param g The graph to analyze
	 * @param d The distance view to update with new values
	 * @param c The source code of the algorithm
	 */
	private void run(Graph g, DistanceView d, SourceCode c) {
		c.highlight(0);
		l.nextStep();
		
		Map<Node, Integer> dist = new HashMap<Node, Integer>();
		Map<Node, Node> pred = new HashMap<Node, Node>();
		Set<Edge> edges = new HashSet<Edge>();
		
		// Initialize the distance and the predecessor for each node
		c.toggleHighlight(0, 1);
		l.nextStep();
		c.unhighlight(1);
		for(int i = 0; i < g.getSize(); i++) {
			dist.put(g.getNode(i), Integer.MAX_VALUE);			
			pred.put(g.getNode(i), null);
			
			c.highlight(2);
			d.setDistance(i, Integer.MAX_VALUE, null, null);
			d.highlight(i, null, null);
			g.highlightNode(i, null, null);
			l.nextStep();
			d.unhighlight(i, null, null);
			c.toggleHighlight(2, 3);
			l.nextStep();
			c.unhighlight(3);
			g.unhighlightNode(i, null, null);
		}
		
		// set distance to start node
		dist.put(g.getStartNode(), 0);
		
		d.setDistance(g.getStartNode(), 0, null, null);
		d.highlight(g.getStartNode(), null, null);
		g.highlightNode(g.getStartNode(), null, null);
		c.highlight(5);
		l.nextStep();
		d.unhighlight(g.getStartNode(), null, null);
		g.unhighlightNode(g.getStartNode(), null, null);

		// build a set of all edges
		int[][] matrix = g.getAdjacencyMatrix();
		for(int i = 0; i < g.getSize(); i++) {
			for(int j = 0; j < g.getSize(); j++) {
				if(matrix[i][j] > 0) {
					Edge e = new Edge(g.getNode(i), g.getNode(j), matrix[i][j]);
					edges.add(e);
				}
			}
		}
		
		// relax edges
		c.toggleHighlight(5, 6);
		l.nextStep();
		c.unhighlight(6);
		for(int i = 0; i < g.getSize() - 1; i++) {
			c.highlight(7);
			l.nextStep();
			c.unhighlight(7);
			for(Edge e : edges) {
				Node u = e.node1;
				Node v = e.node2;
				
				g.highlightEdge(u, v, null, null);
				l.nextStep(translator.translateMessage("LBL_EXAMINE_EDGE", g.getNodeLabel(u), g.getNodeLabel(v)));
				
				c.highlight(8);
				g.highlightNode(u, null, null);
				l.nextStep();
				c.toggleHighlight(8, 9);
				g.highlightNode(v, null, null);
				l.nextStep();
				c.toggleHighlight(9, 10);
				l.nextStep();
				c.unhighlight(10);
				
				if(dist.get(u) < Integer.MAX_VALUE && dist.get(u) + e.weight < dist.get(v)) {
					dist.put(v, dist.get(u) + e.weight);
					pred.put(v, u);
					
					c.highlight(11);
					d.setDistance(v, dist.get(u) + e.weight, null, null);
					d.highlight(v, null, null);
					l.nextStep();
					d.unhighlight(v, null, null);
					c.toggleHighlight(11, 12);
					c.unhighlight(12);
				}
				
				g.unhighlightEdge(u, v, null, null);
				g.unhighlightNode(u, null, null);
				g.unhighlightNode(v, null, null);
			} 
		}
		
		// search for negative weight circles
		c.highlight(17);
		l.nextStep();
		c.unhighlight(17);
		for(Edge e : edges) {
			Node u = e.node1;
			Node v = e.node2;
			
			g.highlightEdge(u, v, null, null);
			l.nextStep();
			c.highlight(18);
			g.highlightNode(u, null, null);
			l.nextStep();
			c.toggleHighlight(18, 19);
			g.highlightNode(v, null, null);
			l.nextStep();
			c.toggleHighlight(19, 20);
			l.nextStep();
			c.unhighlight(20);
			
			if(dist.get(u) + e.weight < dist.get(v)) {
				System.err.println("Negative weight circle detected!");
				
				c.highlight(21);
				l.nextStep();
				c.unhighlight(21);
				
				break;
			}
			g.unhighlightEdge(u, v, null, null);
			g.unhighlightNode(u, null, null);
			g.unhighlightNode(v, null, null);
		}
		
		c.highlight(24);
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
