package generators.network.graph.anim;

import java.util.UUID;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.primitives.Graph;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 */
public class DistanceView {
	private Text hl;
	private StringMatrix m;
	private Language l;
	private Graph g;
	
	/**
	 * Create a new distance view
	 * 
	 * @param lang The Language object to add the matrix to
	 * @param upperLeft The alignment position within the animation frame
	 * @param style The style to be used
	 * @param graph The graph to derive the matrix from
	 * @param headline The headline text
	 * @param headlineSize The headline size. There must be a corresponding style present in s.
	 */
	public DistanceView(Language lang, Node upperLeft, Style style, Graph graph, String headline, String headlineSize) {
		l = lang;
		g = graph;
		
		// create labels and initial distances
		String[][] dist = new String[2][g.getSize()];
		for(int i = 0; i < g.getSize(); i++) {
			dist[0][i] = g.getNodeLabel(i);
			dist[1][i] = "";
		}
		
		// create headline
		hl = l.newText(upperLeft, headline, UUID.randomUUID().toString(), null, (TextProperties) style.getProperties(headlineSize));
		
		// create new matrix
		m = l.newStringMatrix(new Offset(0, 7, hl.getName(), AnimalScript.DIRECTION_SW), dist, "distanceViewMatrix" + UUID.randomUUID().toString().replace("-", ""), null, (MatrixProperties) style.getProperties("matrix"));
	}
	
	/**
	 * Change the distance displayed for a node
	 * 
	 * @param nodeId The node's id
	 * @param dist The new distance
	 * @param delay Animation delay
	 * @param duration Animation duration
	 */
	public void setDistance(int nodeId, int dist, Timing delay, Timing duration) {
		if (dist == Integer.MAX_VALUE) {
			m.put(1, nodeId, "∞", null, null); 
		} else {
			m.put(1, nodeId, String.valueOf(dist), delay, duration);
		}
	}
	
	/**
	 * Change the distance displayed for a node
	 * 
	 * @param n The node
	 * @param dist The new distance
	 * @param delay Animation delay
	 * @param duration Animation duration
	 */
	public void setDistance(Node n, int dist, Timing delay, Timing duration) {
		this.setDistance(g.getPositionForNode(n), dist, delay, duration);
	}
	
	/**
	 * Highlight an entry in the view
	 * 
	 * @param nodeId The graph node's id
	 * @param offset Animation delay
	 * @param duration Animation duration
	 */
	public void highlight(int nodeId, Timing offset, Timing duration) {
		m.highlightCell(1, nodeId, offset, duration);
	}

	/**
	 * Highlight an entry in the view
	 * 
	 * @param node The graph node represented by the entry
	 * @param offset Animation delay
	 * @param duration Animation duration
	 */
	public void highlight(Node node, Timing offset, Timing duration) {
		this.highlight(g.getPositionForNode(node), offset, duration);
	}

	/**
	 * Remove the highlight from an entry in the view
	 * 
	 * @param nodeId The graph node's id
	 * @param offset Animation delay
	 * @param duration Animation duration
	 */
	public void unhighlight(int nodeId, Timing offset, Timing duration) {
		m.unhighlightCell(1, nodeId, offset, duration);
	}
	
	/**
	 * Remove the highlight from an entry in the view
	 * 
	 * @param node The graph node represented by the entry
	 * @param offset Animation delay
	 * @param duration Animation duration
	 */
	public void unhighlight(Node node, Timing offset, Timing duration) {
		this.unhighlight(g.getPositionForNode(node), offset, duration);
	}
	
	/**
	 * Show the matrix in the animation
	 */
	public void show() {
		this.show(null);
	}
	
	/**
	 * Show the matrix in the animation
	 * 
	 * @param timing The animation delay
	 */
	public void show(Timing timing) {
		hl.show(timing);
		m.show(timing);		
	}
	
	/**
	 * Hide the matrix in the animation
	 */
	public void hide() {
		this.hide(null);
	}
	
	/**
	 * Hide the matrix in the animation
	 * 
	 * @param timing The animation delay
	 */
	public void hide(Timing timing) {
		hl.hide(timing);
		m.hide(timing);		
	}
}