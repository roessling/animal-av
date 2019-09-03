package generators.network.routing.impl.pvr;

import generators.network.routing.helper.MultiHopRoute;
import generators.network.routing.helper.Router;
import generators.network.routing.helper.AbstractRoutingTable;
import generators.network.routing.helper.RoutingTableEntry;

import java.util.LinkedList;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * The routing table used by Path Vector Routing
 */
public class PathVectorRoutingTable extends AbstractRoutingTable {
	
	/**
	 * Create new routing table
	 * 
	 * @param parent The router this table is associated to
	 */
	public PathVectorRoutingTable(Router parent) {
		super(parent);
	}

	@Override	
	protected boolean update(Router source, Router target, int cost, boolean singleStep) {
		RoutingTableEntry entry;
		boolean update = false;
		
		int newCost =  this.getShortestRoute(source).getCost() + cost;

		if(!currentRoutes.containsKey(target) || newCost < this.getShortestRoute(target).getCost()) {
			MultiHopRoute newRoute = ((MultiHopRoute)source.getRoutes().getShortestRoute(target));
			LinkedList<Router> newPath = new LinkedList<Router>(newRoute.getVia());
			newPath.addFirst(source);
			entry = new RoutingTableEntry(new MultiHopRoute(newPath, newCost), true);
			
			newRoutes.put(target, entry);
			
			update = true;
		} 

		// update route view if one exists
		if(view != null && update) {
			view.updateView(target, source, newCost, singleStep);
		}
		return update;
		
	}
}