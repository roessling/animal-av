package generators.network.graph;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.AbstractGraphGenerator;
import generators.network.graph.anim.DistanceView;
import generators.network.helper.ClassName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
 * Generate an animation showing the functionality of Prim's algorithm.
 * The algorithm searches for shortest path in a weighted graph with
 * no negative weighted edges. 
 */
public class DijkstraGenerator extends AbstractGraphGenerator {	
	
	/**
	 * Create a new generator with the default locale 'German'
	 */
	public DijkstraGenerator() {
		this(Locale.GERMANY);
	}
	
	/**
	 * Create a new generator
	 * 
	 * @param myLocale The locale setting to use
	 */
	public DijkstraGenerator(Locale myLocale) {
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
	private DijkstraGenerator(Language lang, Locale myLocale) {
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
		DijkstraGenerator anim = new DijkstraGenerator(l, locale);
		
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
		
		// distance from the source and predecessor for a specific node 
		Map<Node, Integer> dist = new HashMap<Node, Integer>();
		Map<Node, Node> pred = new HashMap<Node, Node>();
		
		// list of unoptimized nodes (all nodes for now)
		List<Node> remaining = new ArrayList<Node>();
		
		// source
		Node source = g.getStartNode();
		
		// init the variables
		c.toggleHighlight(0, 1);
		l.nextStep();
		c.unhighlight(1);
		for (int i = 0; i < g.getSize(); i++) {
			g.highlightNode(g.getNode(i), null, null);
			
			dist.put(g.getNode(i), Integer.MAX_VALUE);
			d.setDistance(i, Integer.MAX_VALUE, null, null);
			d.highlight(i, null, null);
			c.highlight(2);
			l.nextStep();
			
			d.unhighlight(i, null, null);
			
			pred.put(g.getNode(i), null);
			c.toggleHighlight(2, 3);
			l.nextStep();
			
			// add all nodes to the list of remaining nodes to visit
			remaining.add(g.getNode(i));
			
			c.toggleHighlight(3, 4);
			l.nextStep();
			
			c.unhighlight(4);
			g.unhighlightNode(g.getNode(i), null, null);
		}
		
		// set distance form the source to itself
		dist.put(source, 0);
		d.setDistance(source, 0, null, null);
		
		g.highlightNode(source, null, null);
		d.highlight(source, null, null);
		c.highlight(6);

		g.unhighlightNode(source, null, null);
		d.unhighlight(source, null, null);
		c.toggleHighlight(6, 7);
		l.nextStep();
		c.unhighlight(7);
		while(!remaining.isEmpty()) {
			// find the nearest node
			Node next = null;
			for(int i = 0; i < remaining.size(); i++) {
				if((next == null && dist.get(remaining.get(i)) < Integer.MAX_VALUE) || (next != null && dist.get(remaining.get(i)) < dist.get(next))) {
					next = remaining.get(i);
				}
			}
			c.highlight(8);
			g.highlightNode(next, null, null);
			l.nextStep(translator.translateMessage("LBL_EXAMINE_NODE", g.getNodeLabel(next)));
			
			// if there are no more nodes connected to the rest of the graph we can empty the list and finish
			c.toggleHighlight(8, 9);
			l.nextStep();
			if(next == null) {
				remaining.clear();
				c.toggleHighlight(9, 10);
				l.nextStep();
				c.unhighlight(10);
				l.nextStep();
			// otherwise we work on the next node
			} else {
				// remove node from list
				remaining.remove(next);
				c.toggleHighlight(9, 12);
				l.nextStep();
				
				// get the neighbors of the current node
				int[] nodeMatrix = g.getEdgesForNode(next);
				
				c.toggleHighlight(12, 13);
				l.nextStep();
				c.unhighlight(13);
				for(int  i = 0; i < nodeMatrix.length; i++) {
					// only work on the nodes which are actually connected and were not visited before
					if(nodeMatrix[i] > 0 && remaining.contains(g.getNode(i))) {
						// get the new distance for those nodes
						int newDist = dist.get(next) + nodeMatrix[i];
						c.highlight(14);
						g.highlightNode(i, null, null);
						g.highlightEdge(next, g.getNode(i), null, null);
						l.nextStep();
						
						// update distance if shorter path was found
						c.toggleHighlight(14, 15);
						l.nextStep();
						c.unhighlight(15);
						if(newDist < dist.get(g.getNode(i))) {
							dist.put(g.getNode(i), newDist);
							d.setDistance(i, newDist, null, null);
							d.highlight(i, null, null);
							c.highlight(16);
							l.nextStep();
				
							pred.put(g.getNode(i), next);
							d.unhighlight(i, null, null);
							c.toggleHighlight(16, 17);
							l.nextStep();

							c.toggleHighlight(17, 18);
							l.nextStep();
							c.unhighlight(18);
						}						
						g.unhighlightNode(i, null, null);
						g.unhighlightEdge(next, g.getNode(i), null, null);
					}
				}
				
				g.unhighlightNode(next, null, null);
				l.nextStep();
			}
		}
		c.highlight(23);
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
