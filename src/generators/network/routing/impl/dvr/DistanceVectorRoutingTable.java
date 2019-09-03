package generators.network.routing.impl.dvr;

import generators.network.routing.helper.AbstractRoutingTable;
import generators.network.routing.helper.Router;
import generators.network.routing.helper.RoutingTableEntry;
import generators.network.routing.helper.SingleHopRoute;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * The routing table used by Distance Vector Routing
 */
public class DistanceVectorRoutingTable extends AbstractRoutingTable {
	
	/**
	 * Create new routing table
	 * 
	 * @param parent The router this table is associated to
	 */
	public DistanceVectorRoutingTable(Router parent) {
		super(parent);
	}

	@Override
	protected boolean update(Router source, Router target, int cost, boolean singleStep) {
		RoutingTableEntry entry;
		boolean update = false;
		boolean viewUpdate = false;
		
		int newCosts = parent.getLink(source).cost + cost;
		
		if(currentRoutes.containsKey(target)) {
			entry = currentRoutes.get(target);
			
			if(!entry.routeExistsVia(source) || newCosts < entry.getCostVia(source)) {
				entry.add(new SingleHopRoute(source, newCosts));
				// only set the update status if we really have a new shortest route
				if(newCosts <= this.getShortestRoute(target).getCost()) {
					entry.setUpdateStatus(true);
					update = true;
				} else {
					entry.setUpdateStatus(false);
					update = false;
					viewUpdate = true;
				}
			}
		} else {
			entry = new RoutingTableEntry(new SingleHopRoute(source, newCosts), true);
			update = true;
		}
		newRoutes.put(target, entry);

		// update route view if one exists
		if(view != null && (update || viewUpdate)) {
			view.updateView(target, source, newCosts, singleStep);
		}
		return update;	
	}
}