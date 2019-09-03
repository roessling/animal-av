package generators.network.graph;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.AbstractGraphGenerator;
import generators.network.helper.ClassName;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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
 * The algorithm searches for the minimum spanning tree in an undirected weighted graph. 
 */
public class PrimGenerator extends AbstractGraphGenerator {
	
	/**
	 * Create a new generator with the default locale 'German'
	 */
	public PrimGenerator() {
		this(Locale.GERMANY);
	}
	
	/**
	 * Create a new generator
	 * 
	 * @param myLocale The locale setting to use
	 */
	public PrimGenerator(Locale myLocale) {
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
	private PrimGenerator(Language lang, Locale myLocale) {
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
		PrimGenerator anim = new PrimGenerator(l, locale);
		
		// build headline 
		anim.getHeader();
		l.nextStep(translator.translateMessage("LBL_TITLE_SLIDE"));
		
		// build title
		anim.getTitleSlide();
		
		// get the graph
		g = anim.getGraph(g, true);
		
		// display the source code
		SourceCode c = anim.getCode();

		l.nextStep(translator.translateMessage("LBL_ANIM_START"));
		anim.run(g, c);

		return l.toString();
	}
	
	/**
	 * Run the actual algorithm and create the AnimalScript code to display an animation.
	 * 
	 * @param g The graph to analyze
	 * @param c The source code of the algorithm
	 */
	private void run(Graph g, SourceCode c) {
		c.highlight(0);
		l.nextStep();
		
		// list of all nodes already visited nodes
		List<Node> visitedNodes = new ArrayList<Node>();
		
		c.toggleHighlight(0, 1);
		l.nextStep();
		
		// start node is chosen by user
		Node next = g.getStartNode();
		g.highlightNode(next, null, null);
		c.toggleHighlight(1, 2);
		l.nextStep();

		c.toggleHighlight(2, 3);
		l.nextStep();
		c.unhighlight(3);
		for(int i = 0; i < g.getSize() - 1; i++) {
			Node current = next;

			// add current node to list
			visitedNodes.add(current);
			c.highlight(4);
			g.highlightNode(current, null, null);
			l.nextStep();
			g.unhighlightNode(current, null, null);
			
			// get node with shortest distance to the current one
			int shortestDist = Integer.MAX_VALUE;
			c.toggleHighlight(4, 5);
			l.nextStep();
			c.toggleHighlight(5, 6);
			l.nextStep();
			c.toggleHighlight(6, 7);
			l.nextStep();

			c.toggleHighlight(7, 8);
			l.nextStep();
			c.unhighlight(8);
			for(Node thisNode : visitedNodes) {
				g.highlightNode(thisNode, null, null);
				// get the nearest unvisited neighbor for this node 
				int[] distance = g.getEdgesForNode(thisNode);
				c.highlight(9);
				l.nextStep(translator.translateMessage("LBL_SEARCH_SHORTEST_EDGE", g.getNodeLabel(thisNode)));
				
				c.toggleHighlight(9, 10);
				l.nextStep();
				c.unhighlight(10);
				for(int j = 0; j < distance.length; j++) {
					if(!visitedNodes.contains(g.getNode(j)) && distance[j] > 0) {
						c.highlight(11);
						g.highlightNode(j, null, null);
						l.nextStep();
						c.unhighlight(11);

						if(distance[j] < shortestDist) {
							current = thisNode;
							c.highlight(12);
							l.nextStep();

							next = g.getNode(j);
							c.toggleHighlight(12, 13);
							l.nextStep();

							shortestDist = distance[j];
							c.toggleHighlight(13, 14);
						}
						g.unhighlightNode(j, null, null);
					}
					c.unhighlight(14);
				}
				g.unhighlightNode(thisNode, null, null);
			}

			// check if current node is isolated
			if(next == null) {
				// choose the next node by random
				boolean found = false;
				Random r = new Random();
				while(!found) {
					int rnd = r.nextInt(g.getSize());
					if(!visitedNodes.contains(g.getNode(rnd))) {
						next = g.getNode(rnd);
						found = true;
					}
				}
			} else {
				c.highlight(18);
				g.highlightNode(current, null, null);
				g.highlightNode(next, null, null);
				l.nextStep();
				// add edge between nodes to list
				g.highlightEdge(current, next, null, null);
				c.toggleHighlight(18, 19);
				l.nextStep(translator.translateMessage("LBL_ADD_EDGE", g.getNodeLabel(current), g.getNodeLabel(next)));
				c.unhighlight(19);
				g.unhighlightNode(current, null, null);
				g.unhighlightNode(next, null, null);
			}
		}
		c.highlight(22);
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
