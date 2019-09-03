package generators.network.routing.helper;


import java.util.HashSet;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * An entry in a routing table. Can hold one or more routes.
 */
public class RoutingTableEntry extends HashSet<Route> {
	private static final long serialVersionUID = -8917876820032868738L;
	
	/**
	 * The entry was updated in the last round
	 */
	protected boolean updated;

	/**
	 * Create a new entry with an initial route. 
	 * 
	 * @param route The initial route to be added
	 */
	public RoutingTableEntry(Route route) {
		this(route, true);
	}
	
	/**
	 * Create a new entry with an initial route
	 * 
	 * @param route The initial route to be added
	 * @param updated The update status of the entry
	 */
	public RoutingTableEntry(Route route, boolean updated) {
		this.add(route);
		this.updated = updated;
	}
	
	/**
	 * Check if a route exists via the given router as the first hop
	 * 
	 * @param router The first hop
	 * @return The route exists
	 */
	public boolean routeExistsVia(Router router) {
		for(Route route : this) {
			if(route.getFirstHop() == router) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the total costs for a route via a given first hop
	 * 
	 * @param router The first hop
	 * @return The total costs
	 */
	public int getCostVia(Router router) {
		int lowestCost = Integer.MAX_VALUE; 
		for(Route route : this) {
			if(route.getFirstHop() == router && route.getCost() < lowestCost) {
				lowestCost = route.getCost();
			}
		}
		return lowestCost;
	}

	/**
	 * Set the update status for this entry
	 * 
	 * @param status The new status
	 */
	public void setUpdateStatus(boolean status) {
		this.updated = status;
	}

	/**
	 * Get the update status for this route
	 * 
	 * @return The status
	 */
	public boolean getUpdateStatus() {
		return this.updated;
	}
}
