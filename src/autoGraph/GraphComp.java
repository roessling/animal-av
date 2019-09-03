package autoGraph;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * @author Marian Hieke
 *
 */
public class GraphComp {

	private LinkedList<NodeComp> nodes = new LinkedList<NodeComp>(); // the
																		// nodes
																		// of
																		// the
																		// GraphComp
	private LinkedList<EdgeComp> edges = new LinkedList<EdgeComp>(); // the
																		// edges
																		// of
																		// the
																		// GraphComp
	private NodeComp start; // the
							// starting
							// node
							// of
							// the
							// GraphComp
	private NodeComp end; // the
							// end
							// node
							// of
							// the
							// GraphComp
	private double heightMax; // the
								// maximal
								// height
								// of
								// the
								// GraphComp;
	private double widthMax; // the
								// maximal
								// width
								// of
								// the
								// GraphComp;
	private double minEdgeLength; // the
									// minimal
									// edgelentgth;
	private double desiredEdgeLength; // the
										// desired
										// edge
										// length
	private double edgeLength; // the
								// used
								// edge
								// length
	private boolean raster = true;
	private int xEpsilon = 0; // the
								// epsilon
								// distance
								// in
								// which
								// to
								// nodes
								// get
								// the
								// same
								// x
								// value
	private int yEpsilon = 0; // the
								// epsilon
								// distance
								// in
								// which
								// to
								// nodes
								// get
								// the
								// same
								// y
								// value

	private int postProcessIt = 4;

	// private double nodeRadius = 0; // the radius used for the nodes
	private int iterations;
	private double temparature;
	private double radiusForNodes;
	private int[][] adjacencyMatrix = null;
	private String[] nodeLabels = null;

	public GraphComp() {

	}

	public GraphComp(NodeComp start) {
		this.start = start;
	}

	public GraphComp(NodeComp start, NodeComp end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * sets the desired edge length to the given value
	 * 
	 * @param lenght
	 */
	public void setDesiredEdgeLength(double lenght) {
		this.desiredEdgeLength = lenght;
	}

	/**
	 * sets the minimum edge length to the given value
	 * 
	 * @param lenght
	 */
	public void setMinEdgeLEngth(double lenght) {
		this.minEdgeLength = lenght;
	}

	/**
	 * sets the maximum height of the grtaph to the given value
	 * 
	 * @param height
	 */
	public void setHeightMax(double height) {
		this.heightMax = height;

	}

	/**
	 * sets the maximum width of the GraphComp to the given value
	 * 
	 * @param width
	 */
	public void setWidthMax(double width) {
		this.widthMax = width;
	}

	/**
	 * sets the start node to the given node
	 * 
	 * @param node
	 */
	public void setStart(NodeComp node) {
		this.start = node;

	}

	/**
	 * sets the eend node to the given node
	 * 
	 * @param node
	 */
	public void setEnd(NodeComp node) {
		this.end = node;

	}

	public NodeComp getStart() {
		return this.start;

	}

	public NodeComp getEnd() {
		return this.end;
	}

	/**
	 * adds the given node to the list of nodes
	 * 
	 * @param node
	 */
	public void addNode(NodeComp node) {
		this.nodes.add(node);
	}

	/**
	 * adds a node with the given radius and id
	 * 
	 * @param radius
	 * @param id
	 */
	public void addNode(double radius, int id) {
		NodeComp node = new NodeComp(radius, id);
		this.nodes.add(node);
	}

	/**
	 * adds a node with the given Vector, radius and id
	 * 
	 * @param pos
	 * @param radius
	 * @param id
	 */
	public void addNode(VectorComp pos, double radius, int id) {
		NodeComp node = new NodeComp(pos, radius, id);
		this.nodes.add(node);
	}

	/**
	 * adds a new edge to the GraphComp the edge connects nodeA and nodeB
	 * 
	 * @param nodeA
	 * @param nodeB
	 */
	public void addEdge(NodeComp nodeA, NodeComp nodeB) {
		EdgeComp edge = new EdgeComp(nodeA, nodeB);
		this.edges.add(edge);
	}

	/**
	 * returns all nodes of the GraphComp
	 * 
	 * @return
	 */
	public LinkedList<NodeComp> getAllNodes() {
		return this.nodes;
	}

	public void placeCicle() {

	}

	public NodeComp getNodeForId(int id) {
		NodeComp nc = null;
		for (NodeComp n : this.nodes) {
			if (id == n.getId()) {
				nc = n;
			}
		}

		return nc;
	}

	// nach GraphComp drawing by force directed placement fruchterman &reingold
	public void placeNodesForceDirected() {
		double area = this.heightMax * this.widthMax;

		double k = Math.sqrt(area / this.nodes.size());
		double t = this.temparature;
		VectorComp delta;
		VectorComp disp;
		VectorComp tmp;
		VectorComp point;
		VectorComp dir;
		VectorComp lotDir;
		double r;
		double c;
		double tmpX;
		double tmpY;
		double dirX;
		double dirY;
		double lotSX;
		double lotSY;
		double lotDirX;
		double lotDirY;

		double fac;
		this.assigneRandom();
		for (int i = 0; i < iterations; i++) {

			// calculate repulsive forces
			for (NodeComp n : this.nodes) {
				disp = new VectorComp(0, 0);
				n.setDisPosition(disp);

				for (NodeComp m : this.nodes) {
					if (n.getId() != m.getId()) {

						delta = n.getPosition().sub(m.getPosition());

						tmp = delta.div(delta.norm());
						tmp = tmp.mul(this.fr(delta.norm(), k));
						n.setDisPosition(n.getDisPosition().add(tmp));

					}
				}
			}

			// calculate attractive force
			for (EdgeComp e : this.edges) {

				delta = e.getNodeA().getPosition().sub(e.getNodeB().getPosition());

				tmp = delta.div(delta.norm());
				fac = this.fa(delta.norm(), k);
				tmp = tmp.mul(fac);
				e.getNodeA().setDisPosition(e.getNodeA().getDisPosition().sub(tmp));
				tmp = delta.div(delta.norm());
				tmp = tmp.mul(fac);
				e.getNodeB().setDisPosition(e.getNodeB().getDisPosition().add(tmp));

			}

			/*
			 * limit the maximum displacement to the temperature t and then prevent from
			 * being displaced outside the frame
			 */
			for (NodeComp n : this.nodes) {

				tmp = n.getDisPosition();
				tmp = n.getDisPosition();
				tmp = tmp.div(tmp.norm());
				fac = this.min(n.getDisPosition().norm(), t);

				tmp = tmp.mul(fac);
				n.setPosition(n.getPosition().add(tmp));
				n.getPosition().setX(this.min((this.widthMax / 2) - this.radiusForNodes,
						this.max(-(this.widthMax / 2) + this.radiusForNodes, n.getPosition().getX())));
				n.getPosition().setY(this.min((this.heightMax / 2) - this.radiusForNodes,
						this.max(-(this.heightMax / 2) + this.radiusForNodes, n.getPosition().getY())));

			}

			for (NodeComp n : this.nodes)
				for (NodeComp m : this.nodes)
					if (n.getId() != m.getId()) {
						VectorComp del = n.getPosition().sub(m.getPosition());
						if (del.norm() < this.minEdgeLength) {
							this.ensureMinEdgeLength(n, m);

						}
					}

			// reduce temperatur
			t = this.cooling(t, i);
		}
		for (int h = 0; h < this.postProcessIt; h++) {
			for (int i = 0; i < iterations / 10; i++) {
				for (NodeComp n : this.nodes) {
					for (EdgeComp e : this.edges)
						if (e.getNodeA().getId() != n.getId() && e.getNodeB().getId() != n.getId()) {
							// if the node is not part of the edge compute the distance
							// between the node and the edge

							lotDir = new VectorComp();
							point = n.getPosition();
							dir = e.getNodeB().getPosition().sub(e.getNodeA().getPosition());
							lotDir.setX(dir.getY());
							lotDir.setY(dir.mul(-1).getX());
							lotSX = point.getX();
							lotSY = point.getY();
							lotDirX = lotDir.getX();
							lotDirY = lotDir.getY();

							if (lotDirX != 0) {
								fac = lotDirY / lotDirX;
							} else {
								fac = lotDirY;
							}

							tmpX = e.getNodeA().getPosition().getX() * fac;

							dirX = dir.getX() * fac;
							lotSX = lotSX * fac;
							lotDirX = lotDirX * fac;

							tmpY = e.getNodeA().getPosition().getY() - tmpX;
							dirY = dir.getY() - dirX;
							lotSY = lotSY - lotSX;
							lotDirY = lotDirY - lotDirX;

							lotDir = lotDir.mul((-tmpX - ((-tmpY + lotSY) / dirY) * dirX + lotSX) / lotDirX);
							// if the difference between the edge is smaller than the radius
							// add a force
							if (lotDir.norm() < this.radiusForNodes) {
								lotDir = lotDir.div(lotDir.norm());
								lotDir = lotDir.mul(this.fer());
								if (e.intersects(n, this.minEdgeLength)) {
									lotDir = lotDir.mul(2);
									n.setPosition(n.getPosition().add(lotDir));
									break;
								}
							}
						}
					for (NodeComp m : this.nodes)
						if (n.getId() != m.getId()) {
							VectorComp del = n.getPosition().sub(m.getPosition());
							if (del.norm() < this.minEdgeLength)
								this.ensureMinEdgeLength(n, m);
						}
				}

			}
			this.placeEdges();
			VectorComp posA;
			VectorComp posB;

			for (int i = 0; i < 1; i++)
				for (EdgeComp e : this.edges)
					for (EdgeComp d : this.edges)
						if (e.isCrossing(d) && !e.equals(d))
							if (e.getNodeA().getId() != d.getNodeA().getId()
									&& e.getNodeB().getId() != d.getNodeB().getId()) {

								posA = e.getNodeA().getPosition();
								posB = d.getNodeA().getPosition();
								e.getNodeA().setPosition(posB);
								d.getNodeA().setPosition(posA);
								this.placeEdges();
							} else {
								if (e.getNodeA().getId() != d.getNodeB().getId()
										&& (e.getNodeB().getId() != d.getNodeA().getId())) {

									posA = e.getNodeA().getPosition();
									posB = d.getNodeB().getPosition();
									e.getNodeA().setPosition(posB);
									d.getNodeB().setPosition(posA);
									this.placeEdges();
								}
							}

			for (NodeComp n : this.nodes)
				for (NodeComp m : this.nodes)
					if (n.getId() != m.getId()) {

						VectorComp del = n.getPosition().sub(m.getPosition());

						if (del.norm() < this.minEdgeLength) {
							this.ensureMinEdgeLength(n, m);
						}
					}

		}
		if (this.raster) {
			this.rasterize(this.xEpsilon, this.yEpsilon);
			this.placeEdges();
		}
	}

	/**
	 * The cooling function for the simulated annealing
	 * 
	 * @param t
	 *            the temperatur
	 * @param i
	 *            the iteration of the main loop
	 * @return the new temperature
	 */
	private double cooling(double t, int i) {
		if (t > i) {
			t = t - i;
		} else {
			t = 0;
		}
		return t;
	}

	/**
	 * adds the given parameter to the x value for the rasterization( nodes that are
	 * more different in x value will get the same x value)
	 * 
	 * @param xEps
	 */
	public void setXEpsilon(int xEps) {
		this.xEpsilon = xEps;
	}

	public int getXEpsilon() {
		return this.xEpsilon;
	}

	/**
	 * adds the given parameter to the y value for the rasterization( nodes that are
	 * more different in y value will get the same y value)
	 * 
	 * @param xEps
	 */
	public void setYEpsilon(int yEps) {
		this.yEpsilon = yEps;
	}

	public int getYEpsilon() {
		return this.yEpsilon;
	}

	/**
	 * sets the iterations to the given value
	 * 
	 * @param iterations
	 */
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	/**
	 * sets the temperature to the given value
	 * 
	 * @param temperature
	 */
	public void setTemperature(double temperature) {
		this.temparature = temperature;
	}

	/**
	 * 
	 * @return the temperatue
	 */
	public double getTemeparature() {
		return this.temparature;
	}

	/**
	 * 
	 * @return thenumber iterations
	 */
	public int getIterations() {
		return this.iterations;
	}

	/**
	 * enables or diasbles rasterisation of the nodes
	 * 
	 * @param rasterize
	 */
	public void setRaster(boolean rasterize) {
		this.raster = rasterize;
	}

	/**
	 * 
	 * @return if the raasterisation of the nodes is enabled
	 */
	public boolean getRasterize() {
		return this.raster;
	}

	/**
	 * assignes the nodes of the GraphComp randomly
	 */
	public void assigneRandom() {
		double oneXStep = (this.widthMax / 2) / this.nodes.size();
		double oneYStep = (this.heightMax / 2) / this.nodes.size();
		double stepX = oneXStep;
		double stepY = oneYStep;
		double x = 0;
		double y = 0;
		VectorComp pos;
		LinkedList<NodeComp> copy = new LinkedList<NodeComp>();
		copy.addAll(this.nodes);
		NodeComp p = copy.getFirst();
		boolean found = false;
		NodeComp tmp;
		int changeSide = 1;
		boolean small = false;
		if (copy.size() <= 3) {
			small = true;
		}
		while (copy.size() > 0) {
			pos = new VectorComp(x, y);
			if (small) {
				if (copy.size() % 2 == 0) {
					pos.setY(y + y * 0.1);
				} else {
					pos.setY(y - y * 0.1);
				}
			}
			p.setPosition(pos);
			for (EdgeComp e : this.edges) {
				if (e.getNodeA().getId() == p.getId()) {

					for (Iterator it = copy.iterator(); it.hasNext();) {
						tmp = (NodeComp) it.next();
						if (p.getId() == tmp.getId()) {
							it.remove();

							break;
						}
					}
					for (NodeComp n : copy) {
						if (e.getNodeB().getId() == n.getId() && copy.contains(e.getNodeB())) {
							p = n;
							found = true;
							break;

						}
					}
					if (found) {
						break;
					}
				} else {
					if (e.getNodeB().getId() == p.getId()) {
						for (Iterator it = copy.iterator(); it.hasNext();) {
							tmp = (NodeComp) it.next();
							if (p.getId() == tmp.getId()) {
								it.remove();
								break;
							}
						}
						for (NodeComp n : copy) {
							if (e.getNodeA().getId() == n.getId() && copy.contains(e.getNodeA())) {
								p = n;
								found = true;
								break;
							}
						}

					}
					if (found) {
						break;
					}
				}
			}

			if (!found) {
				for (Iterator it = copy.iterator(); it.hasNext();) {
					tmp = (NodeComp) it.next();
					if (p.getId() == tmp.getId()) {
						it.remove();

						break;
					}
				}
				if (!copy.isEmpty()) {

					changeSide = changeSide * -1;
					p = copy.getFirst();

				}

			}

			y = 0 + stepY * changeSide;
			x = 0 - stepX * changeSide;
			stepX = stepX + oneXStep;
			stepY = stepY + oneYStep;
			found = false;
		}
	}

	public LinkedList<EdgeComp> getAllEdges() {
		return this.edges;
	}

	/**
	 * 
	 * @return the maximum width
	 */
	public double getWidthMax() {
		return this.widthMax;

	}

	/**
	 * 
	 * @return
	 */
	public double getHeightMax() {
		return this.heightMax;
	}

	/**
	 * sets the radius for the nodes to the given value
	 * 
	 * @param radius
	 */
	public void setRadiusForNode(double radius) {
		this.radiusForNodes = radius;
		for (NodeComp n : this.nodes) {
			n.setRadius(radius);
		}
	}

	public double fer() {
		return this.radiusForNodes;
	}

	public double fa(double x, double k) {
		double result = (x * x) / k;

		return result;
	}

	public double fr(double z, double k) {
		double result = -(k * k) / z;

		return result;
	}

	public double min(double a, double b) {

		if (a < b) {

			return a;
		} else {

			return b;
		}
	}

	public double max(double a, double b) {

		if (a > b) {
			return a;
		} else {
			return b;
		}
	}

	/**
	 * ensure that the minimum edge length is between nodeA and nodeB
	 * 
	 * @param nodeA
	 * @param nodeB
	 */
	public void ensureMinEdgeLength(NodeComp nodeA, NodeComp nodeB) {
		VectorComp delta = nodeA.getPosition().sub(nodeB.getPosition());
		VectorComp a0 = delta.div(delta.norm());
		if (delta.norm() == 0) {
			double one = Math.sqrt(1);
			a0 = new VectorComp(one, one);
		}
		a0 = a0.mul(this.minEdgeLength / 2);
		nodeA.setPosition(nodeA.getPosition().add(a0));
		nodeB.setPosition(nodeB.getPosition().sub(a0));
	}

	public void placeEdges() {
		for (EdgeComp e : this.edges) {
			e.computeposAandPosB();
		}
	}

	public void placeEdges(double zoom) {
		for (EdgeComp e : this.edges) {
			e.computeposAandPosB(zoom);
		}
	}

	public NodeComp getNode(int nodeNr) {
		if (nodeNr < this.nodes.size() && nodeNr >= 0) {
			return this.nodes.get(nodeNr);
		}
		return null;
	}

	public String getNodeLabel(int nodeNr) {
		if (nodeNr >= 0 && nodeNr < this.nodeLabels.length) {
			return this.nodeLabels[nodeNr];
		} else {
			return null;
		}
	}

	public void setPostProcessIt(int it) {
		this.postProcessIt = it;
	}

	public void rasterize(int xEpsilon, int yEpsilon) {
		LinkedList<NodeComp> nodesTmp = new LinkedList<NodeComp>();
		LinkedList<NodeComp> nodesTmp2 = new LinkedList<NodeComp>();
		nodesTmp.addAll(this.nodes);
		Iterator it1 = nodesTmp.listIterator();
		Iterator it2 = nodesTmp.listIterator();
		NodeComp tmp;
		NodeComp tmp2;

		for (Iterator it = it1; it.hasNext();) {
			tmp = (NodeComp) it.next();
			for (Iterator itj = nodesTmp.listIterator(); itj.hasNext();) {
				tmp2 = (NodeComp) itj.next();
				if (tmp != tmp2) {
					if (tmp.getPosition().getY() + (radiusForNodes + yEpsilon) > tmp2.getPosition().getY()
							&& tmp.getPosition().getY() < tmp2.getPosition().getY()
							&& tmp.getPosition().getY() != tmp2.getPosition().getY()
							|| tmp.getPosition().getY() - (radiusForNodes + yEpsilon) < tmp2.getPosition().getY()
									&& tmp.getPosition().getY() > tmp2.getPosition().getY()
									&& tmp.getPosition().getY() != tmp2.getPosition().getY()) {
						tmp2.setPosition(new VectorComp(tmp2.getPosition().getX(), tmp.getPosition().getY()));

					} else {
						if (tmp.getPosition().getX() + (radiusForNodes + xEpsilon) > tmp2.getPosition().getX()
								&& tmp.getPosition().getX() < tmp2.getPosition().getX()
								&& tmp.getPosition().getX() != tmp2.getPosition().getX()
								|| tmp.getPosition().getX() - (radiusForNodes + xEpsilon) < tmp2.getPosition().getX()
										&& tmp.getPosition().getX() > tmp2.getPosition().getX()
										&& tmp.getPosition().getX() != tmp2.getPosition().getX()) {
							tmp2.setPosition(new VectorComp(tmp.getPosition().getX(), tmp2.getPosition().getY()));

						}
					}

				}
			}

		}
	}

}