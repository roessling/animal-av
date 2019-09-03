package generators.graph.helpers;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Encapsulates the functionality of a directed graph.
 * 
 * @author chollubetz
 * 
 */
public class DirectedGraph {
  Set<Node> nodes;
  Set<Edge> edges;

  public DirectedGraph() {
    nodes = new TreeSet<Node>();
    edges = new TreeSet<Edge>();
  }

  /**
   * Adds a (new) node to the graph.
   * 
   * @param node
   *          the node to add
   * @return the graph itself
   */
  public DirectedGraph addNode(Node node) {
    nodes.add(node);
    System.out.println(nodes.size());
    System.out.println(edges.size());
    return this;
  }

  /**
   * Adds a (new) node to the graph.
   * 
   * @param name
   *          the name of the node to add
   * @return the graph itself
   */
  public DirectedGraph addNode(String name) {
    nodes.add(new Node(name));
    System.out.println(nodes.size());
    System.out.println(edges.size());
    return this;
  }

  /**
   * Adds a (new) edge to the graph.
   * 
   * @param edge
   *          the edge to add
   * @return the graph itself
   */
  public DirectedGraph addEdge(Edge edge) {
    nodes.add(edge.getFromNode());
    nodes.add(edge.getToNode());
    edges.add(edge);
    System.out.println(nodes.size());
    System.out.println(edges.size());
    return this;
  }

  /**
   * Adds a (new) edge to the graph.
   * 
   * @param fromNode
   *          the source node
   * @param toNode
   *          the destination node
   * @param weight
   *          the weight of the edge
   * @return the graph itself
   */
  public DirectedGraph addEdge(Node fromNode, Node toNode, int weight) {
    nodes.add(fromNode);
    nodes.add(toNode);
    edges.add(new Edge(fromNode, toNode, 1));
    return this;
  }

  /**
   * Adds a (new) edge to the graph.
   * 
   * @param fromName
   *          the name of the source node
   * @param toName
   *          the name of the destination node
   * @param weight
   *          the weight of the edge
   * @return the graph itself
   */
  public DirectedGraph addEdge(String fromName, String toName, int weight) {
    Node n1 = new Node(fromName);
    Node n2 = new Node(toName);
    nodes.add(n1);
    nodes.add(n2);
    edges.add(new Edge(n1, n2, weight));
    return this;
  }

  /**
   * Returns the nodes.
   * 
   * @return the nodes of the graph
   */
  public Set<Node> getNodes() {
    return nodes;
  }

  /**
   * Returns the edges.
   * 
   * @return the edges of the graph
   */
  public Set<Edge> getEdges() {
    return edges;
  }

  /**
   * Determines the outgoing neighbors and the weight of the corresponding edges
   * of the given node.
   * 
   * @param fromNode
   *          the source node
   * @return the outgoing neighbors and theirs edge weights
   */
  public Set<Pair<Node, Integer>> getOutgoingNeighbors(Node fromNode) {
    Set<Pair<Node, Integer>> result = new TreeSet<Pair<Node, Integer>>();
    for (Edge currentEdge : edges)
      if (currentEdge.getFromNode().equals(fromNode))
        result.add(new Pair<Node, Integer>(currentEdge.getToNode(), currentEdge
            .getWeight()));
    return result;
  }

  /**
   * Determines the ingoing neighbors and the weight of the corresponding edges
   * to the given node.
   * 
   * @param toNode
   *          the destination node
   * @return the ingoing neighbors and theirs edge weights
   */
  public Set<Pair<Node, Integer>> getIngoingNeighbors(Node toNode) {
    Set<Pair<Node, Integer>> result = new TreeSet<Pair<Node, Integer>>();
    for (Edge currentEdge : edges)
      if (currentEdge.getToNode().equals(toNode))
        result.add(new Pair<Node, Integer>(currentEdge.getFromNode(),
            currentEdge.getWeight()));
    return result;
  }

  /**
   * Determines the indegree of the given node.
   * 
   * @param node
   *          the node
   * @return the indegree of the node
   */
  public int getIndegreeOf(Node node) {
    return getIngoingNeighbors(node).size();
  }

  /**
   * Determines the outdegree of the given node.
   * 
   * @param node
   *          the node
   * @return the outdegree of the node
   */
  public int getOutdegreeOf(Node node) {
    return getOutgoingNeighbors(node).size();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Edge currentEdge : edges)
      sb.append(currentEdge);
    return sb.toString();
  }

  /**
   * Checks, whether the directed graph has a cycle.
   * 
   * @return true if the graph contains a cycle, otherwise false
   */
  public boolean hasCycle() {
    TreeMap<Node, Boolean> visited = new TreeMap<Node, Boolean>();
    TreeMap<Node, Boolean> finished = new TreeMap<Node, Boolean>();

    for (Node currentNode : nodes) {
      visited.put(currentNode, false);
      finished.put(currentNode, false);
    }

    try {
      for (Node currentNode : nodes)
        DFS(currentNode, visited, finished);
    } catch (Exception e) {
      return true;
    }

    return false;
  }

  /**
   * Performs a depth-first-search with the given values on the current graph.
   * 
   * @param v
   *          the starting point
   * @param visited
   *          the visited nodes
   * @param finished
   *          the finished nodes
   * @throws Exception
   *           if there is a cycle detected
   */
  private void DFS(Node v, TreeMap<Node, Boolean> visited,
      TreeMap<Node, Boolean> finished) throws Exception {
    if (finished.get(v))
      return;
    if (visited.get(v))
      throw new Exception("Cycle detected.");
    visited.put(v, true);
    for (Pair<Node, Integer> neighborPair : getOutgoingNeighbors(v))
      DFS(neighborPair.getFirst(), visited, finished);
    finished.put(v, true);
  }

  /**
   * Checks, whether the directed graph has negative weighted edges.
   * 
   * @return true if the graph contains a negative weighted edge, otherwise
   *         false
   */
  public boolean hasNegativeEdgeWeights() {
    for (Edge currentEdge : edges)
      if (currentEdge.getWeight() < 0)
        return true;
    return false;
  }

}
