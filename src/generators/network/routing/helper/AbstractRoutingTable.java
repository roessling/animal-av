package generators.network.routing.helper;

import generators.network.routing.anim.RoutingView;
import generators.network.routing.impl.dvr.DistanceVectorFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import algoanim.animalscript.addons.bbcode.Style;
import algoanim.primitives.generators.Language;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * The routing table holds all routes to other routers known by this router at the current time.
 */
public abstract class AbstractRoutingTable {
	/**
	 * The currently known routes
	 */
	protected Map<Router, RoutingTableEntry> currentRoutes;
	/**
	 * The table entries to be modified after the current update round
	 */
	protected Map<Router, RoutingTableEntry> newRoutes;
	/**
	 * The router this table is associated with
	 */
	protected Router parent;
	/**
	 * The animal script view of this table
	 */
	protected RoutingView view;

	/**
	 * Create new routing table
	 * 
	 * @param parent The router this table is associated to
	 */
	public AbstractRoutingTable(Router parent) {
		this.parent = parent;
		currentRoutes = new HashMap<Router, RoutingTableEntry>();
		newRoutes = new HashMap<Router, RoutingTableEntry>();
	}
	
	/**
	 * Add a new route to the table
	 * 
	 * @param target The route's target
	 * @param route The route to be added
	 */
	public void addRoute(Router target, Route route) {
		RoutingTableEntry entry;
		if(currentRoutes.containsKey(target)) {
			entry = currentRoutes.get(target);
			entry.add(route);
		} else {
			entry = new RoutingTableEntry(route);
		}
		entry.setUpdateStatus(true);
		currentRoutes.put(target, entry);
	}

	/**
	 * Get all router's where a route is known to
	 * 
	 * @return A Set of routers
	 */
	public Set<Router> getTargets() {
		return currentRoutes.keySet();
	}
	
	/**
	 * Get all known routes to a specific router
	 * 
	 * @param target The target router
	 * @return A Set of routes
	 */
	public Set<Route> getRoutes(Router target) {
		return currentRoutes.get(target);
	}

	/**
	 * Get the shortest route to a specific router
	 * 
	 * @param target The target router
	 * @return The shortest route
	 */
	public Route getShortestRoute(Router target) {
		int lowestCost = Integer.MAX_VALUE;
		Route shortestRoute = parent.getMethod().getNewRoute(new Router(new DistanceVectorFactory()), lowestCost);
		for(Route thisRoute : currentRoutes.get(target)) {
			if(thisRoute.getCost() < lowestCost) {
				lowestCost = thisRoute.getCost();
				shortestRoute = thisRoute;
			}
		}
		return shortestRoute;
	}

	/**
	 * Update the routing table but do not activate the new routes
	 * 
	 * @param source The source of the update message
	 * @param table The update message
	 * @return If table was updated
	 */
	public boolean update(Router source, AbstractRoutingTable table) {
		return update(source, table, false);
	}
	
	/**
	 * Update the routing table but do not activate the new routes
	 * 
	 * @param source The source of the update message
	 * @param table The update message
	 * @param singleStep Step through each update one by one during the animation
	 * @return If table was updated
	 */
	public boolean update(Router source, AbstractRoutingTable table, boolean singleStep) {
		boolean update = false;
		// add route for each target
		for(Router thisTarget : table.getTargets()) {
			update = this.update(source, thisTarget, table.getRoutes(thisTarget), singleStep) || update;
		}
		return update;
	}

	/**
	 * Update the routing table but do not activate the new routes
	 * 
	 * @param source The source of the update message
	 * @param target The target router for this update
	 * @param route The new route to the target
	 * @param singleStep Step through each update one by one during the animation
	 * @return If table was updated
	 */
	private boolean update(Router source, Router target, Set<Route> route, boolean singleStep) {
		// no need to update any routes from/to ourselves or routes which have infinite length
		if(source == parent || target == parent || currentRoutes.get(source).getCostVia(source) == Integer.MAX_VALUE) {
			return false;
		}
		
		boolean update = false;
		// add each route
		for(Route thisRoute : route) {
			update = this.update(source, target, thisRoute.getCost(), singleStep) || update;
		}
		return update;
	}

	/**
	 * Update the routing table
	 * 
	 * @param source The source of the update message
	 * @param target The target router for this update
	 * @param cost The total costs of transmission for this route
	 * @param singleStep Step through each update one by one during the animation
	 * @return If table was updated
	 */
	protected abstract boolean update(Router source, Router target, int cost, boolean singleStep);

	/**
	 * Commit any changes made to the routing table during this round
	 */
	public void commit() {		
		// merge tables
		for(Router thisTarget : currentRoutes.keySet()) {
			if(!newRoutes.containsKey(thisTarget)) {
				RoutingTableEntry entry = currentRoutes.get(thisTarget);
				entry.setUpdateStatus(false);
				newRoutes.put(thisTarget, entry);
			}
		}
		// commit new table
		currentRoutes = newRoutes;
		
		// empty table
		newRoutes = new HashMap<Router, RoutingTableEntry>();
	}
	
	public AbstractRoutingTable getRouteMessage(Router receiver) {
		AbstractRoutingTable table = parent.getMethod().getNewRoutingTable(parent);
		for(Router thisTarget : currentRoutes.keySet()) {
			if(thisTarget != receiver && currentRoutes.get(thisTarget).getUpdateStatus()) {
				table.addRoute(thisTarget, this.getShortestRoute(thisTarget));
			}
		}
		return table;
	}

	/**
	 * Get the total number of routes in the routing table for any target
	 * 
	 * @return The size of the routing table
	 */
	public int size() {
		int size = 0;
		for(Router thisTarget : currentRoutes.keySet()) {
			size += currentRoutes.get(thisTarget).size();
		}
		
		return size;
	}
	
	/**
	 * Create new view in animal for this table
	 * 
	 * @param l The Language object to add the view to
	 * @param upperLeft The alignment in the animation frame
	 * @param display Any display options
	 * @param s The style to be used to create a consistent design throughout the animation
	 * @param headline The headline text for the view
	 * @param size The maximum size of the view
	 * @return The new view
	 */
	public RoutingView createAnimalView(Language l, Node upperLeft, DisplayOptions display, Style s, String headline, int size) {
		view = parent.getMethod().getNewView(l, upperLeft, display, s, headline, size, parent);
		return view;
	}

	/**
	 * Remove any highlights from the routing table's view
	 * 
	 * @param offset Animation delay
	 * @param duration Animation duration
	 */
	public void unhighlight(Timing offset, Timing duration) {
		if(view != null) {
			view.unhighlight(offset, duration);
		}
	}
}