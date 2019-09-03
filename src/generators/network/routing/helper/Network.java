package generators.network.routing.helper;

import java.util.LinkedHashSet;
import java.util.Set;

import algoanim.primitives.Graph;
import algoanim.properties.AnimationPropertiesKeys;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * A network of Routers connected by Links.
 */
public class Network {
	private Set<Router> router;
	private AbstractRoutingFactory method;
	private boolean directed;
	
	/**
	 * Create new network with bidirectional links (ie. undirected edges)
	 * 
	 * @param method The routing method to be used within the network
	 */
	public Network(AbstractRoutingFactory method) {
		this(false, method);
	}
	
	/**
	 * Create a new network directly from a animal graph primitive
	 * 
	 * @param graph The initial graph
	 * @param method The routing method to be used within the network
	 */
	public Network(Graph graph, AbstractRoutingFactory method) {
		this((Boolean)graph.getProperties().get(AnimationPropertiesKeys.DIRECTED_PROPERTY), method);
		createFromGraph(graph);
	}
	
	/**
	 * Create a new network
	 * 
	 * @param directed Links are unidirectional (ie. edges are directed)
	 * @param method The routing method to be used within the network
	 */
	public Network(boolean directed, AbstractRoutingFactory method) {
		this.directed = directed;
		this.method = method;
		router = new LinkedHashSet<Router>();
	}
	
	/**
	 * Add a router to the network
	 * 
	 * @param r The router to be added
	 */
	public void addRouter(Router r) {
		router.add(r);
	}
	
	/**
	 * Get a Set of all routers in the network
	 * 
	 * @return The Set of routers
	 */
	public Set<Router> getRouters() {
		return router;
	}
	
	/**
	 * Add a link (ie. an edge) to the network
	 * 
	 * @param start The start router
	 * @param end The desitination router
	 * @param cost The cost of transmitting data over the link
	 * @throws UnknownRouterException
	 */
	public void addLink(Router start, Router end, int cost) throws UnknownRouterException {
		if(router.contains(start) && router.contains(end)) {
			start.addLink(end, cost);
			
			// if we have an undirected network we also add a (backward) link from end to start
			if(!directed) {
				end.addLink(start, cost);
			}
		} else {
			throw new UnknownRouterException();
		}
	}
	
	/**
	 * Initialize the whole network from an Animal graph primitive
	 * 
	 * @param graph The initial graph
	 */
	private void createFromGraph(Graph graph) {		
		// mapping of router to Node ID in the graph
		Router[] routerId = new Router[graph.getSize()]; 
		
		// add each node as router
		for(int i = 0; i < graph.getSize(); i++) {
			Router r = new Router(method, graph, i, graph.getNodeLabel(i));
			this.addRouter(r);
			// remember the ID for later
			routerId[i] = r;
		}
		
		// add edges from the adjacency matrix
		int[][] matrix = graph.getAdjacencyMatrix();
		for(int i = 0; i < graph.getSize(); i++) {
			for(int j = 0; j < graph.getSize(); j++) {
				if(matrix[i][j] > 0) {
					try {
						this.addLink(routerId[i], routerId[j], matrix[i][j]);
					} catch (UnknownRouterException e) {
						// silently ignore the error
					}
				}
			}
		}
	}
}
