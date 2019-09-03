package generators.network.routing.anim;

import generators.network.routing.helper.Router;

import java.util.List;
import java.util.UUID;
import java.util.Vector;

import algoanim.animalscript.addons.bbcode.H3;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.primitives.Primitive;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * Display routing information 
 */
public abstract class RoutingView {
	protected Language l;
	protected StringMatrix m;
	protected Text hl;
	protected int count;
	protected List<Router> knownRouters;
	protected Router myself;
	
	/**
	 * Create a new view
	 * 
	 * @param lang The Language object to add the route to
	 * @param upperLeft The anchor element on the animation frame
	 * @param display Any animal display options for the view
	 * @param style The style object used to create a consistent view
	 * @param headline The headline text of the view
	 * @param size The maximum size of the routing table
	 * @param router The router this view is created for 
	 */
	public RoutingView(Language lang, Node upperLeft, DisplayOptions display, Style style, String headline, int size, Router router) {
		l = lang;
		knownRouters = new Vector<Router>(size);
		myself = router;
		
		hl = l.newText(upperLeft, headline, UUID.randomUUID().toString(), display, (TextProperties)style.getProperties(H3.BB_CODE));
	}
	
	/**
	 * Get the primitive actually displaying the entries
	 * 
	 * @return A matrix which holds the entries
	 */
	public Primitive getView() {
		return m;
	}

	/**
	 * Update an entry
	 * 
	 * @param target The destination router
	 * @param via The next hop
	 * @param distance The new distance
	 */
	public void updateView(Router target, Router via, int distance) {
		updateView(target, via, distance, false);
	}

	/**
	 * Remove any highlights from the view
	 * 
	 * @param offset Animation delay
	 * @param duration Animation duration
	 */
	public void unhighlight(Timing offset, Timing duration) {
		for(int row = 1; row < m.getNrRows(); row++) {
			m.unhighlightCellColumnRange(row, 0, m.getNrCols() - 1, offset, duration);
		}
	}
	
	/**
	 * Update an entry
	 * 
	 * @param target The destination router
	 * @param via The next hop
	 * @param distance The new distance
	 * @param singleStep Create a single step for each update within the animation
	 */
	public abstract void updateView(Router target, Router via, int distance, boolean singleStep);
}
