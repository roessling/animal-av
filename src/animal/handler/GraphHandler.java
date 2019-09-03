/*
 * GraphHandler.java
 * The handler for PTGraph.
 *
 * @author Pierre Villette
 */

package animal.handler;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.util.StringTokenizer;
import java.util.Vector;

import animal.graphics.PTGraph;
import animal.graphics.PTGraphicObject;
import animal.misc.MSMath;

/**
 * Handler for operations that can be performed on Graphs.
 */
public class GraphHandler extends GraphicObjectHandler {

	/**
	 * Get all the animation methods supported by a PTGraph
	 * 
	 * @param ptgo
	 *          a graphic object, i.e. a PTGraph
	 * @param obj
	 *          the type of the animation effect
	 */
	public Vector<String> getMethods(PTGraphicObject ptgo, Object obj) {

		Vector<String> result = new Vector<String>(12, 2);

		PTGraph graph = null;
		if (ptgo instanceof PTGraph)
			graph = (PTGraph) ptgo;
		else
			return result;

		if (obj instanceof Boolean) { // animation types for "Show" / "TimedShow"
																	// animators
			result.addElement("show"); // show the graph
			result.addElement("hide"); // hide the graph
			for (int i = 0; i < graph.getLength(); i++) {
				result.addElement("showNode " +i);
				result.addElement("hideNode " +i);
			}
		}	else if (obj instanceof String) {
			result.addElement("setEdgeWeight");
		} else if (obj instanceof Color) {// animation types for ColorChanger
			result.addElement("bgColor"); // change the graph's background color
			result.addElement("outlineColor"); // change the color of the cells'
																					// outline
			result.addElement("nodeFontColor"); // change the font color of the graph
			result.addElement("edgeFontColor");
			
			//TODO
			result.addElement("setNodeHighlightFillColor");
			result.addElement("setNodeHighlightTextColor");
			result.addElement("setEdgeHighlightTextColor");
			result.addElement("setEdgeHighlightPolyColor");
		} else if (obj instanceof Point) { // animation types for Move
			result.addElement("translate"); // move the graph
			for (int a = 0; a < graph.getSize(); a++)
				result.addElement("translate #" + (a + 1));
			result.addElement("translateNodes..."); // NEW
			result.addElement("translateWithFixedNodes..."); // NEW
		} else if (obj instanceof double[]) {// animation types for Highlight
			result.addElement("highlight node elements"); // highlight the cell
																										// content
			result.addElement("highlight nodes"); // highlight the cell background
			result.addElement("unhighlight node elements"); // unhighlight the cell
																											// content
			result.addElement("unhighlight nodes"); // unhighlight the cell background
			result.addElement("deactivate nodes"); // deactivate the cell
			result.addElement("activate nodes"); // activate the cell
			result.addElement("set nodes visible");
			result.addElement("set nodes invisible");
		} else if (obj instanceof double[][]) {
			result.addElement("highlight edge elements");
			result.addElement("highlight edges");
			result.addElement("unhighlight edge elements");
			result.addElement("unhighlight edges");
			result.addElement("deactivate edges");
			result.addElement("activate edges");
			result.addElement("set edges visible");
			result.addElement("set edges invisible");
			result.addElement("hide edge weight");
			result.addElement("show edge weight");			
		}

		// add extension methods provided in other classes
		addExtensionMethodsFor(ptgo, obj, result);

		// return the vector of animation types
		return result;
	}

	/**
	 * Transform the requested property change in method calls
	 * 
	 * @param ptgo
	 *          the graphical primitive to modify
	 * @param e
	 *          the PropertyChangeEvent that encodes the information which
	 *          property has to change how
	 */
	public void propertyChange(PTGraphicObject ptgo, PropertyChangeEvent e) {
		// only works if the passed object is a PTGraph!
		PTGraph graph = null;
		if (ptgo instanceof PTGraph) {
			graph = (PTGraph) ptgo; // convert to PTGraph
			String what = e.getPropertyName(); // retrieve property

			if (what.equalsIgnoreCase("bgColor")) // set background color
				graph.setBGColor((Color) e.getNewValue());
			else if (what.equalsIgnoreCase("nodeFontColor"))
				graph.setNodeFontColor((Color) e.getNewValue());
			else if (what.equalsIgnoreCase("edgeFontColor"))
				graph.setEdgeFontColor((Color) e.getNewValue());
			else if (what.equalsIgnoreCase("outlineColor"))
				graph.setOutlineColor((Color) e.getNewValue());
			else if (what.startsWith("setEdgeWeight")) {
				StringTokenizer stok = new StringTokenizer(what, "(),");
				stok.nextToken(); // swallow "setEdgeWeight"
//				token = stok.nextToken(); // '('
				int startNode = Integer.valueOf(stok.nextToken()).intValue();
//				token = stok.nextToken(); // ','
				int endNode = Integer.valueOf(stok.nextToken()).intValue();
//				token = stok.nextToken(); // ')'
				graph.enterValueEdge(startNode, endNode, (String)e.getNewValue());
			}
			else if (what.equalsIgnoreCase("translate")) {
				Point old = (Point) e.getOldValue(), now = (Point) e.getNewValue(), diff = MSMath
						.diff(now, old);
				graph.translate(diff.x, diff.y);
			} else if (what.length() > 11
					&& what.substring(0, 11).equalsIgnoreCase("translate #")) {
				// translate only one of the vertices.
				int num = Integer.parseInt(what.substring(11));
				Point old = (Point) e.getOldValue(), now = (Point) e.getNewValue(), diff = MSMath
						.diff(now, old);
        graph.translateNode(num, diff.x, diff.y);
			} else if (what.startsWith("translateNodes ")
					|| what.startsWith("translateWithFixedNodes ")) {
				boolean moveMode = (what.startsWith("translateNodes"));
				StringTokenizer stringTok = new StringTokenizer(what
						.substring((moveMode) ? 15 : 24));
				int nodeCount = graph.getSize();
				boolean[] map = new boolean[nodeCount];
				if (!moveMode)
					for (int i = 0; i < nodeCount; i++)
						map[i] = true;
				int currentNode = 0;
				while (stringTok.hasMoreTokens()) {
					currentNode = Integer.parseInt(stringTok.nextToken());
					if (currentNode >= 0 && currentNode < nodeCount)
						map[currentNode] = moveMode;
				}

        @SuppressWarnings("unused")
        Point old = (Point) e.getOldValue(), now = (Point) e.getNewValue(), diff = MSMath
            .diff(now, old);
//      graph.translateNodes(map, diff.x, diff.y);
        graph.translateNodesTo(map, now.x, now.y);
				
			}

			else if (what.contains("highlight") && what.contains("node")) {
				double[] states = (double[]) e.getNewValue();
				for (int i = 0; i < graph.length; i++) {
					// hopefully this should work without checking the 'states'
					// size as it should always be as large as the longest
					// graph that was chosen in the HighlightEditor
					if (what.startsWith("un")) {
						if ((states[i] >= 0) && graph.isElemHighlightedNode(i)) {
							graph.getPTTextNode(i).setColor(
									animal.animator.Highlight.fadeColor(graph
											.getElemHighlightColor(i), graph.getNodeFontColor(),
											states[i]));
							if (states[i] >= 0.99)
								graph.setElemHighlightedNode(i, false);
						}
						if ((states[i] >= 0) && graph.isHighlightedNode(i)) {
							graph.getNode(i).setFillColor(
									animal.animator.Highlight.fadeColor(graph
											.getHighlightColor(i), graph.getBGColor(), states[i]));
							if (states[i] >= 0.99)
								graph.setHighlightedNode(i, false);
						}
//						if (what.endsWith("elements")) {
//							if ((states[i] >= 0) && graph.isElemHighlightedNode(i)) {
//								graph.getPTTextNode(i).setColor(
//										animal.animator.Highlight.fadeColor(graph
//												.getElemHighlightColor(), graph.getNodeFontColor(),
//												states[i]));
//								if (states[i] >= 0.99)
//									graph.setElemHighlightedNode(i, false);
//							}
//						} else if (what.endsWith("nodes")) {
//							if ((states[i] >= 0) && graph.isHighlightedNode(i)) {
//								graph.getNode(i).setFillColor(
//										animal.animator.Highlight.fadeColor(graph
//												.getHighlightColor(i), graph.getBGColor(), states[i]));
//								if (states[i] >= 0.99)
//									graph.setHighlightedNode(i, false);
//							}
//						}
					} else {
						if ((states[i] >= 0) && !graph.isElemHighlightedNode(i)) {
							graph.getPTTextNode(i).setColor(
									animal.animator.Highlight.fadeColor(graph
											.getNodeFontColor(), graph.getElemHighlightColor(i),
											states[i]));
							if (states[i] >= 0.99)
								graph.setElemHighlightedNode(i, true);
						}
						if ((states[i] >= 0) && !graph.isHighlightedNode(i)) {
							graph.getNode(i).setFillColor(
									animal.animator.Highlight.fadeColor(graph.getBGColor(),
											graph.getHighlightColor(i), states[i]));
							if (states[i] >= 0.99)
								graph.setHighlightedNode(i, true);
						}
//						if (what.endsWith("elements")) {
//							if ((states[i] >= 0) && !graph.isElemHighlightedNode(i)) {
//								graph.getPTTextNode(i).setColor(
//										animal.animator.Highlight.fadeColor(graph
//												.getNodeFontColor(), graph.getElemHighlightColor(),
//												states[i]));
//								if (states[i] >= 0.99)
//									graph.setElemHighlightedNode(i, true);
//							}
//						} else if (what.endsWith("nodes")) {
//							if ((states[i] >= 0) && !graph.isHighlightedNode(i)) {
//								graph.getNode(i).setFillColor(
//										animal.animator.Highlight.fadeColor(graph.getBGColor(),
//												graph.getHighlightColor(i), states[i]));
//								if (states[i] >= 0.99)
//									graph.setHighlightedNode(i, true);
//							}
//						}
					}
				}
			} else if (what.contains("activate") && what.contains("node")) {
				double[] states = (double[]) e.getNewValue();
				for (int i = 0; i < graph.length; i++) {
					if (states[i] >= 0.5) {
						graph.setActivatedNode(i, !what.startsWith("de"));
						for (int j = 0; j < graph.length; j++) {
							graph.setActivatedEdge(i, j, !what.startsWith("de")
									&& graph.isActivatedNode(j));
							graph.setActivatedEdge(j, i, !what.startsWith("de")
									&& graph.isActivatedNode(j));
						}
					}
				}
			} else if (what.contains("visible") && what.contains("node")) {
				double[] states = (double[]) e.getNewValue();
				for (int i = 0; i < graph.length; i++) {
					if (states[i] >= 0.5) {
						graph.setVisibleNode(i, !what.contains("in"));
						for (int j = 0; j < graph.length; j++) {
							graph.setVisibleEdge(i, j, !what.contains("in")
									&& graph.isVisibleNode(j));
							graph.setVisibleEdge(j, i, !what.contains("in")
									&& graph.isVisibleNode(j));
						}
					}
				}
			} else if (what.contains("highlight") && what.contains("edge")) {
				double[][] states = (double[][]) e.getNewValue();
				for (int i = 0; i < graph.length; i++) {
					for (int j = 0; j < graph.length; j++) {
						// hopefully this should work without checking the 'states'
						// size as it should always be as large as the longest
						// graph that was chosen in the HighlightEditor
						if (what.startsWith("un")) {
							if ((states[i][j] >= 0) && graph.isElemHighlightedEdge(i, j)) {
								graph.getPTTextEdge(i, j).setColor(
										animal.animator.HighlightEdge.fadeColor(graph
												.getEdgeHighlightFontColor(i, j), graph.getEdgeFontColor(),
												states[i][j]));
								if (states[i][j] >= 0.99)
									graph.setElemHighlightedEdge(i, j, false);
							}
							if ((states[i][j] >= 0) && graph.isHighlightedEdge(i, j)) {
								if (i != j) {
									graph.getEdge(i, j).setColor(
											animal.animator.HighlightEdge.fadeColor(graph.getEdgeHighlightPolyColor(i, j), graph.getOutlineColor(),
													states[i][j]));
								} else {
									graph.getEdgeLoop(i).setColor(
											animal.animator.HighlightEdge.fadeColor(graph.getEdgeHighlightPolyColor(i, j), graph.getOutlineColor(),
													states[i][j]));
								}
								if (states[i][j] >= 0.99)
									graph.setHighlightedEdge(i, j, false);
							}
							graph.getPTTextEdge(i, j).setColor(graph.getEdgeTextColor(i, j));
							
//							if (what.endsWith("elements")) {
//								if ((states[i][j] >= 0) && graph.isElemHighlightedEdge(i, j)) {
//									graph.getPTTextEdge(i, j).setColor(
//											animal.animator.HighlightEdge.fadeColor(graph
//													.getElemHighlightColor(), graph.getEdgeFontColor(),
//													states[i][j]));
//									if (states[i][j] >= 0.99)
//										graph.setElemHighlightedEdge(i, j, false);
//								}
//							} else if (what.endsWith("edges")) {
//								if ((states[i][j] >= 0) && graph.isHighlightedEdge(i, j)) {
//									if (i != j) {
//										graph.getEdge(i, j).setColor(
//												animal.animator.HighlightEdge.fadeColor(graph
//														.getHighlightColor(), graph.getOutlineColor(),
//														states[i][j]));
//									} else {
//										graph.getEdgeLoop(i).setColor(
//												animal.animator.HighlightEdge.fadeColor(graph
//														.getHighlightColor(), graph.getOutlineColor(),
//														states[i][j]));
//									}
//									if (states[i][j] >= 0.99)
//										graph.setHighlightedEdge(i, j, false);
//								}
//							}
						} else {
							if ((states[i][j] >= 0) && !graph.isElemHighlightedEdge(i, j)) {
								graph.getPTTextEdge(i, j).setColor(
										animal.animator.HighlightEdge.fadeColor(graph
												.getEdgeFontColor(), graph.getEdgeHighlightFontColor(i, j),
												states[i][j]));
								if (states[i][j] >= 0.99)
									graph.setElemHighlightedEdge(i, j, true);
							}
							if ((states[i][j] >= 0) && !graph.isHighlightedEdge(i, j)) {
								if (i != j) {
									graph.getEdge(i, j).setColor(
											animal.animator.HighlightEdge.fadeColor(graph
													.getOutlineColor(), graph.getEdgeHighlightPolyColor(i, j),
													states[i][j]));
								} else {
									graph.getEdgeLoop(i).setColor(
											animal.animator.HighlightEdge.fadeColor(graph
													.getOutlineColor(), graph.getEdgeHighlightPolyColor(i, j),
													states[i][j]));
								}
								if (states[i][j] >= 0.99)
									graph.setHighlightedEdge(i, j, true);
							}
							graph.getPTTextEdge(i, j).setColor(graph.getEdgeTextColor(i, j));
							
//							if (what.endsWith("elements")) {
//								if ((states[i][j] >= 0) && !graph.isElemHighlightedEdge(i, j)) {
//									graph.getPTTextEdge(i, j).setColor(
//											animal.animator.HighlightEdge.fadeColor(graph
//													.getEdgeFontColor(), graph.getElemHighlightColor(),
//													states[i][j]));
//									if (states[i][j] >= 0.99)
//										graph.setElemHighlightedEdge(i, j, true);
//								}
//							} else if (what.endsWith("edges")) {
//								if ((states[i][j] >= 0) && !graph.isHighlightedEdge(i, j)) {
//									if (i != j) {
//										graph.getEdge(i, j).setColor(
//												animal.animator.HighlightEdge.fadeColor(graph
//														.getOutlineColor(), graph.getHighlightColor(),
//														states[i][j]));
//									} else {
//										graph.getEdgeLoop(i).setColor(
//												animal.animator.HighlightEdge.fadeColor(graph
//														.getOutlineColor(), graph.getHighlightColor(),
//														states[i][j]));
//									}
//									if (states[i][j] >= 0.99)
//										graph.setHighlightedEdge(i, j, true);
//								}
//							}
						}
					}
				}
			} else if (what.contains("activate") && what.contains("edge")) {
				double[][] states = (double[][]) e.getNewValue();
				for (int i = 0; i < graph.length; i++) {
					for (int j = 0; j < graph.length; j++) {
						if (states[i][j] >= 0.5) {
							graph.setActivatedEdge(i, j, !what.startsWith("de")
									&& graph.isActivatedNode(i) && graph.isActivatedNode(j));
						}
					}
				}
			} else if (what.contains("visible") && what.contains("edge")) {
				double[][] states = (double[][]) e.getNewValue();
				for (int i = 0; i < graph.length; i++) {
					for (int j = 0; j < graph.length; j++) {
						if (states[i][j] >= 0.5) {
							graph.setVisibleEdge(i, j, !what.contains("in")
									&& graph.isVisibleNode(i) && graph.isVisibleNode(j));
						}
					}
				}
			}
			else if (what.equalsIgnoreCase("show edge weight")
					|| what.equalsIgnoreCase("hide edge weight")) {
				boolean isShow = what.startsWith("show");
				double[][] states = (double[][]) e.getNewValue();
				
				for (int i = 0; i < graph.length; i++) {
					for (int j = 0; j < graph.length; j++) {
						if (states[i][j] >= 0.5) {
							graph.setEdgeWeightVisibility(i, j, 
									isShow && graph.isVisibleNode(i) && graph.isVisibleNode(j)
									&& graph.isVisibleEdge(i, j));
						}
					}
				}
			} else if (what.startsWith("hideNode") || what.startsWith("showNode")) {
				boolean isHideMode = what.startsWith("hide");
				StringTokenizer stok = new StringTokenizer(what);
				String elem = stok.nextToken(); // should be "show/hideNode"
				while (stok.hasMoreTokens()) {
					elem = stok.nextToken();
					int value = new Integer(elem).intValue();
					if (isHideMode)
						graph.hideNode(value);
					else
						graph.showNode(value);					
				}
			} else if (what.startsWith("hideEdge") || what.startsWith("showEdge")) {
					boolean isHideMode = what.startsWith("hide");
					StringTokenizer stok = new StringTokenizer(what,"(), ", false);
					String elem = stok.nextToken(); // should be "show/hideNode"
					while (stok.hasMoreTokens()) {
//						elem = stok.nextToken(); // '('
						elem = stok.nextToken(); // first node
						int startNode = new Integer(elem).intValue();
//						elem = stok.nextToken(); // ', '
						elem = stok.nextToken(); // second node
						int targetNode = new Integer(elem).intValue();
//						elem = stok.nextToken(); // ')'						
						if (isHideMode)
							graph.hideEdge(startNode, targetNode);
						else
							graph.showEdge(startNode, targetNode);					
					}
			} else if (what.startsWith("setNodeHighlightFillColor")) {
				Color color = (Color)e.getNewValue();
				@SuppressWarnings("unused")
				String method = what.split(" ")[0];
				Integer fromRange = Integer.valueOf(what.split(" ")[1]);
				Integer toRange = Integer.valueOf(what.split(" ")[2]);
				for (int i = fromRange; i <= toRange; i++) {
					graph.setHighlightColor(color, i);
				}
			} else if (what.startsWith("setNodeHighlightTextColor")) {
				Color color = (Color)e.getNewValue();
				@SuppressWarnings("unused")
				String method = what.split(" ")[0];
				Integer fromRange = Integer.valueOf(what.split(" ")[1]);
				Integer toRange = Integer.valueOf(what.split(" ")[2]);
				for (int i = fromRange; i <= toRange; i++) {
					graph.setElemHighlightColor(color, i);
				}
			} else if (what.startsWith("setEdgeHighlightTextColor")) {
				Color color = (Color)e.getNewValue();
				@SuppressWarnings("unused")
				String method = what.split(" ")[0];
				Integer fromNode = Integer.valueOf(what.split(" ")[1]);
				Integer toNode = Integer.valueOf(what.split(" ")[2]);
				graph.setEdgeHighlightFontColor(color, fromNode, toNode);
			} else if (what.startsWith("setEdgeHighlightPolyColor")) {
				Color color = (Color)e.getNewValue();
				@SuppressWarnings("unused")
				String method = what.split(" ")[0];
				Integer fromNode = Integer.valueOf(what.split(" ")[1]);
				Integer toNode = Integer.valueOf(what.split(" ")[2]);
				graph.setEdgeHighlightPolyColor(color, fromNode, toNode);
			} else if (what.startsWith("setText")) {
				String newContent = (String)e.getNewValue();
				@SuppressWarnings("unused")
				String method = what.split(" ")[0];
				@SuppressWarnings("unused")
				String node = what.split(" ")[1];//node="node"
				Integer nodeIndex = Integer.valueOf(what.split(" ")[2]);
				graph.enterValueNode(nodeIndex, newContent);
			} else if (what.startsWith("setRadius")) {
				String newContent = (String)e.getNewValue();
				Integer newRadius;
				if(newContent.equals("null")){
					newRadius = null;
				}else{
					newRadius = Integer.valueOf(newContent);
				}
				@SuppressWarnings("unused")
				String method = what.split(" ")[0];
				@SuppressWarnings("unused")
				String node = what.split(" ")[1];//node="node"
				Integer nodeIndex = Integer.valueOf(what.split(" ")[2]);
				graph.enterNodeRadius(nodeIndex, newRadius);
			} else { // not handled here; pass up to superclass
				super.propertyChange(ptgo, e);
			}
		}
	}
}