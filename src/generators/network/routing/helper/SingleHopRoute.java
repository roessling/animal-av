package generators.network.routing.helper;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * Represents a route where only the next hop is known
 */
public class SingleHopRoute extends Route {
	protected Router via;
	
	/**
	 * Create a new route
	 * 
	 * @param via The next hop
	 * @param cost The total cost of the route
	 */
	public SingleHopRoute(Object via, int cost) {
		super(cost);
		
		if(via instanceof Router) {
			this.via = (Router)via;
		} else {
			throw new ClassCastException("SingleHopRoute: The first argument must be of type Router.");
		}
	}
	
	@Override
	public Router getFirstHop() {
		return via;
	}
	
	@Override
	public String toString() {
		return via.toString();
	}
}