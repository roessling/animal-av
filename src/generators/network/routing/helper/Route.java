package generators.network.routing.helper;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * A start to destination connection in a network
 */
public abstract class Route {
	/**
	 * The costs of using the connection
	 */
	protected int cost;
	
	/**
	 * Create a new connection
	 * 
	 * @param cost The total costs of the connection
	 */
	public Route(int cost) {
		this.cost = cost;
	}
	
	/**
	 * Get the total costs of the connection
	 * 
	 * @return The costs
	 */
	public int getCost() {
		return this.cost;
	}
	
	/**
	 * Get the first hop along the route
	 * 
	 * @return The first hop
	 */
	public abstract Router getFirstHop();
}
