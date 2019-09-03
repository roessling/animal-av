package generators.network.routing.helper;

import generators.network.routing.anim.RoutingView;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.primitives.generators.Language;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * Provides classes specific to different routing methods.
 */
public abstract class AbstractRoutingFactory {

	/**
	 * Get a routing table
	 * 
	 * @param myself The router to hold the table
	 * @return The routing table
	 */
	public abstract AbstractRoutingTable getNewRoutingTable(Router myself);

	/**
	 * Create a new route
	 * 
	 * @param via The intermediate(s)
	 * @param cost The total costs of the route
	 * @return A new route
	 */
	public abstract Route getNewRoute(Object via, int cost);

	/**
	 * Get the name of the routing method.
	 * To be consistent with the rest of animal try to add a 'Generator' at the end
	 * 
	 * @return The routing methods name
	 */
	public abstract String getName();
	
	/**
	 * Create a new routing table view 
	 * 
	 * @param l The Language object to add the view to
	 * @param upperLeft The view's alignment
	 * @param display Any display options
	 * @param s The style to be used to achieve a consistent look
	 * @param headline The headline text of the view
	 * @param size The total size of the view
	 * @param r The router associated with the view
	 * @return A new routing table view
	 */
	public abstract RoutingView getNewView(Language l, Node upperLeft, DisplayOptions display, Style s, String headline, int size, Router r);
}