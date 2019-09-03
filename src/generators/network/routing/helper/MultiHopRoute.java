package generators.network.routing.helper;


import java.util.LinkedList;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * Represents a route where all hops from start to destination are known
 */
public class MultiHopRoute extends Route {
	private LinkedList<Router> via;
	
	/**
	 * Create a new route
	 * 
	 * @param via A list of routers the route follows or a Router if the route has only one hop
	 * @param cost The total cost of the route
	 */
	@SuppressWarnings("unchecked")
	public MultiHopRoute(Object via, int cost) {
		super(cost);
		// There occurs a warning but as this class should only be used 
		// within any other *Vector[Routing]* class we should be save.
		// Therefore the above annotation was added.
		if(via instanceof Router) {
			this.via = new LinkedList<Router>();
			this.via.add((Router)via);
		} else if(via instanceof LinkedList<?>) {
			this.via = (LinkedList<Router>)via;
		} else {
			throw new ClassCastException("MultiHopRoute: The first argument must be of type Router or LinkedList<Router>.");
		}
	}

	/**
	 * Get all hops along the route
	 * 
	 * @return The hops
	 */
	public LinkedList<Router> getVia() {
		return via;
	}
	
	@Override
	public Router getFirstHop() {
		return via.getFirst();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		// iterate over first to element before last
		for(Router thisRouter : via.subList(0, via.size() - 1)) {
			sb.append(thisRouter);
			sb.append(" → ");
		}
		// the last element does not need an arrow
		sb.append(via.getLast());
		
		return sb.toString();
	}
}