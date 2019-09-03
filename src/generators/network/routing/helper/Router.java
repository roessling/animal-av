package generators.network.routing.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import algoanim.animalscript.addons.bbcode.Style;
import algoanim.primitives.Graph;
import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * A router within the Network holding its RoutingTable
 */
public class Router implements Comparable<Router> {
	private String name;
	private Graph g;
	private int gId;
	private AbstractRoutingFactory method;
	private AbstractRoutingTable routes;
	private Map<Router, AbstractRoutingTable> updates;
	private Map<Router, Link> connections;

	/**
	 * Create a new router which uses the given method to create its table
	 * 
	 * @param method The routing method
	 */
	public Router(AbstractRoutingFactory method) {
		this(method, null, -1);
	}
	
	/**
	 * Create a new router which uses the given method to create its table
	 * 
	 * @param method The routing method
	 * @param graph The graph the network is derived from
	 * @param graphNodeId The node id which is represented by this router
	 */
	public Router(AbstractRoutingFactory method, Graph graph, int graphNodeId) {
		this(method, graph, graphNodeId, UUID.randomUUID().toString());
	}
	
	/**
	 * Create a new router which uses the given method to create its table
	 * 
	 * @param method The routing method
	 * @param graph The graph the network is derived from
	 * @param graphNodeId The node id which is represented by this router
	 * @param name The routers display name
	 */
	public Router(AbstractRoutingFactory method, Graph graph, int graphNodeId, String name) {
		this.name = name;
		this.g = graph;
		this.gId = graphNodeId;
		this.method = method;
		connections = new HashMap<Router, Link>();
		routes = method.getNewRoutingTable(this);
		updates = new HashMap<Router, AbstractRoutingTable>();
	}
	
	/**
	 * Add a new connection to the router
	 * 
	 * @param r The target router
	 * @param cost The cost of transmitting data via this link
	 */
	public void addLink(Router r, int cost) {
		connections.put(r, new Link(g, this, r, cost));
		routes.addRoute(r, method.getNewRoute(r, cost));
	}
	
	/**
	 * Get the connection to another router
	 * 
	 * @param router The target router
	 * @return The link connecting this and the target
	 */
	public Link getLink(Router router) {
		return connections.get(router);
	}
	
	/**
	 * Get all directly connected routers
	 * 
	 * @return A Set of neighbors
	 */
	public Set<Router> getNeighbors() {
		return connections.keySet();
	}
	
	/**
	 * Get the node's id in the graph
	 * 
	 * @return The node's id
	 */
	protected int getGraphNodeId() {
		return gId;
	}
	
	/**
	 * Send updates to another router (normally a neighbor)
	 * 
	 * @param receiver The receiver of the updates
	 */
	public void sendUpdates(Router receiver) {
		receiver.receiveUpdates(this, routes.getRouteMessage(receiver));
	}
	
	/**
	 * Receive updates (normally from a neighbor)
	 * 
	 * @param source The sneder
	 * @param table The updates
	 */
	public void receiveUpdates(Router source, AbstractRoutingTable table) {
		updates.put(source, table);
	}
	
	/**
	 * Get all updates sent out by the router in this round
	 * 
	 * @return The updates
	 */
	public AbstractRoutingTable getUpdates() {
		return routes.getRouteMessage(null);
	}
	
	/**
	 * Update the router's table (including the view)
	 * 
	 * @param singleStep Step through each update one by one during the animation
	 * @return The update status
	 */
	public boolean updateTable(boolean singleStep) {
		boolean update = false;
		for(Router thisRouter : updates.keySet()) {
			update = routes.update(thisRouter, updates.get(thisRouter), singleStep) || update;
		}
		return update;
	}
	
	/**
	 * Commit all changes in the routing table
	 */
	public void commitTable() {
		routes.commit();
	}
	
	/**
	 * Get all currently known routes
	 * 
	 * @return The known routes
	 */
	public AbstractRoutingTable getRoutes() {
		return routes;
	}
	
	/**
	 * Get the routing method used
	 * 
	 * @return The routing method
	 */
	public AbstractRoutingFactory getMethod() {
		return method;
	}

	/**
	 * Create a view for the routing table within the animation
	 * 
	 * @param l The language object to add the view to
	 * @param upperLeft The alignment within the animation frame
	 * @param display Any display options
	 * @param style The style to be used to create a consistent look throughout the animation
	 * @param headline The headline text for the view
	 * @param size The maximum size of the routing table
	 * @return A primitive representing the routing table
	 */
	public Primitive createView(Language l, Node upperLeft, DisplayOptions display, Style style, String headline, int size) {
		return routes.createAnimalView(l, upperLeft, display, style, headline, size).getView();
	}
	
	/**
	 * Highlight the node that represents this router
	 * 
	 * @param offset Animation delay
	 * @param duration Animation dureation
	 */
	public void highlight(Timing offset, Timing duration) {
		if(g != null) {
			g.highlightNode(gId, offset, duration);
		}
	}
	
	/**
	 * Remove the highlight from the node in the graph
	 * 
	 * @param offset Animation delay
	 * @param duration Animation duration
	 */
	public void unhighlight(Timing offset, Timing duration) {
		if(g != null) {
			routes.unhighlight(offset, duration);
			g.unhighlightNode(gId, offset, duration);
		}
	}
	
	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Router) {
			Router r = (Router)o;
			if(r.name.equals(this.name) && r.connections.equals(this.connections)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	/**
	 * Note: this class has a natural ordering that is inconsistent with equals.
	 */
	@Override
	public int compareTo(Router cmp) {
		if (!(cmp instanceof Router)) {
			throw new ClassCastException("A Router Object was expected.");
		} else {
			// simply check the routers names
			return this.toString().compareTo(((Router)cmp).toString());
		}
	}
}
