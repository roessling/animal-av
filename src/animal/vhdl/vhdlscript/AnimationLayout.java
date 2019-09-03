package animal.vhdl.vhdlscript;

import java.awt.Point;
import java.util.ArrayList;

import animal.graphics.PTPoint;
import animal.vhdl.graphics.PTPin;
import animal.vhdl.graphics.PTVHDLElement;
import animal.vhdl.graphics.PTWire;


public class AnimationLayout {
	private ArrayList<PTVHDLElement> originalData;
	private ArrayList<PTWire> wires = new ArrayList<PTWire>();

	private AnimationTract[][] elementNet;

	public AnimationLayout(ArrayList<PTVHDLElement> elements) {
		originalData = elements;
		getElementStructure();
		for (PTWire wire : wires) {
			ArrayList<Point> route = getWireRoute(wire);
			wire.setRoute(route);
			setLaneHolder(route, wire);
		}
	}

	private void setLaneHolder(ArrayList<Point> route, PTWire wire) {
		if (route == null || route.size() < 1)
			return;
		if (route.size() == 1) {
			int laneNr = getFreeLane(route.get(0), wire.getNodes().get(0));
			setLaneHolder(route.get(0), wire.getNodes().get(0), laneNr);
			return;
		}
		for (int i = 1; i < route.size(); i++) {
			int laneNr = getFreeLane(route.get(i - 1), route.get(i), wire
					.getNodes().get(0));
			setLaneHolder(route.get(i - 1), route.get(i), wire.getNodes()
					.get(0), laneNr);
		}
	}

	private void setLaneHolder(Point singlePoint, PTPoint holder, int laneNr) {
		elementNet[singlePoint.x][singlePoint.y].setVerticalLanesHolder(laneNr,
				holder);
	}

	private void setLaneHolder(Point s, Point e, PTPoint holder, int laneNr) {
		if (s.x == e.x) {
			for (int i = Math.min(s.y, e.y); i <= Math.max(s.y, e.y); i++) {
				elementNet[s.x][i].setVerticalLanesHolder(laneNr, holder);
			}
		}
		if (s.y == e.y) {
			for (int i = Math.min(s.x, e.x); i <= Math.max(s.x, e.x); i++) {
				elementNet[i][s.y].setHorizontalLanesHolder(laneNr, holder);
			}
		}
	}

	private int getFreeLane(Point singlePoint, PTPoint holder) {
		for (int number = 0;; number++) {
			if (elementNet[singlePoint.x][singlePoint.y].isFreeInVerticalLanes(
					number, holder))
				return number;
		}
	}

	private int getFreeLane(Point s, Point e, PTPoint holder) {
		int number = 0;
		boolean isFound = false;
		if (s.x == e.x) {
			while (!isFound) {
				for (int i = Math.min(s.y, e.y); i <= Math.max(s.y, e.y); i++) {
					if (!elementNet[s.x][i].isFreeInVerticalLanes(number,
							holder)) {
						number++;
						isFound = false;
						break;
					}
					isFound = true;
				}
			}
		}
		if (s.y == e.y) {
			while (!isFound) {
				for (int i = Math.min(s.x, e.x); i <= Math.max(s.x, e.x); i++) {
					if (!elementNet[i][s.y].isFreeInHorizontalLanes(number,
							holder)) {
						number++;
						isFound = false;
						break;
					}
					isFound = true;
				}
			}
		}
		return number;
	}

	/**
	 * 
	 * return an array list of point as the coordinate of tract in tract net.
	 * 
	 * @param wire
	 * @return
	 */
	private ArrayList<Point> getWireRoute(PTWire wire) {
		// sx:startNodeX;
		// ex:endNodeX
		ArrayList<Point> route = new ArrayList<Point>();

		int bsx = wire.getStartElement().getAnimationLocation().x * 2 + 1;
		int bsy = wire.getStartElement().getAnimationLocation().y * 2 + 1;
		int bex = wire.getEndElement().getAnimationLocation().x * 2 + 1;
		int bey = wire.getEndElement().getAnimationLocation().y * 2 + 1;

		int sx = -1;
		int sy = -1;
		int ex = -1;
		int ey = -1;
		// coordinate of the start tract in the element net.
		// the originate of a wire is either an output or an in/output, and the
		// start direction can not be north or south.
		String startDirection = wire.getStartDirection();
		assert (startDirection == PTWire.DIRECTION_EAST || startDirection == PTWire.DIRECTION_WEST) : "output direction error";
		if (startDirection == PTWire.DIRECTION_EAST) {
			sx = bsx + 1;
			sy = bsy;
		}
		if (startDirection == PTWire.DIRECTION_WEST) {
			sx = bsx - 1;
			sy = bsy;
		}

		// coordinate of the end tract in the element net
		// the finality of a wire can be an input, an in/output, or a control.
		String endDirection = wire.getEndDirection();
		// finality with an input or with an in/output
		if (endDirection == PTWire.DIRECTION_EAST
				|| endDirection == PTWire.DIRECTION_WEST) {
			ex = bex - 1;
			ey = bey;
		}
		// finality with a control pin
		if (endDirection == PTWire.DIRECTION_NORTH) {
			ex = bex;
			ey = bsy + 1;
		}
		// finality with a control pin
		if (endDirection == PTWire.DIRECTION_SOUTH) {
			ex = bex;
			ey = bey - 1;
		}

		if (sx == -1 || sy == -1 || ex == -1 || ey == -1)
			try {
				throw (new Exception());
			} catch (Exception e) {
				System.out.println("coordinate error");
			}

		Point startTractLoc = new Point(sx, sy);
		Point endTractLoc = new Point(ex, ey);

		route.add(startTractLoc);

		// if the wire is started and ended in the same tract, route is found.
		if (startTractLoc.equals(endTractLoc))
			return route;
		// if the start tract and the end tract are on a line, route is the
		// tract pair
		if (startTractLoc.x == endTractLoc.x) {
			route.add(endTractLoc);
			return route;
		}

		// to find is the route reach the "last cross tract"

		// the last cross tract is a neighbor of the end tract
		Point lastCrossTract = null;
		{
			// situation 1: over or under the end tract
			if ((endDirection == PTWire.DIRECTION_EAST)
					|| endDirection == PTWire.DIRECTION_WEST) {
				if (sy < ey)
					lastCrossTract = new Point(endTractLoc.x, endTractLoc.y - 1);
				else
					lastCrossTract = new Point(endTractLoc.x, endTractLoc.y + 1);
			}
			// situation 2: in left or in right of the end tract
			if (endDirection == PTWire.DIRECTION_NORTH
					|| endDirection == PTWire.DIRECTION_SOUTH)
				if (sx < ex)
					lastCrossTract = new Point(endTractLoc.x - 1, endTractLoc.y);
				else
					lastCrossTract = new Point(endTractLoc.x + 1, endTractLoc.y);
		}

		if (lastCrossTract != null) {
			Point turningCrossTract = new Point(startTractLoc.x,
					lastCrossTract.y);
			route.add(turningCrossTract);
			if (!turningCrossTract.equals(lastCrossTract)) {
				route.add(lastCrossTract);
			}
		}
		route.add(endTractLoc);
		return route;
	}

	private void getElementStructure() {
		int elementsCounter = originalData.size();
		int[] longitude;
		int[] latitude;
		// get the relation of elements with each other
		// and generate the wires (only with the start node and end node)
		ArrayList<VHDLNode> dataGraph = new ArrayList<VHDLNode>();
		for (PTVHDLElement e : originalData)
			dataGraph.add(new VHDLNode(e));
		for (PTVHDLElement wireEndEle : originalData) {
			// all possible pins as the end of a wire: input, in-out and control
			ArrayList<PTPin> allInPinOfEndEle = new ArrayList<PTPin>();
			if (wireEndEle.getInputPins() != null)
				allInPinOfEndEle.addAll(wireEndEle.getInputPins());
			if (wireEndEle.getInoutPins() != null)
				allInPinOfEndEle.addAll(wireEndEle.getInoutPins());
			if (wireEndEle.getControlPins() != null)
				allInPinOfEndEle.addAll(wireEndEle.getControlPins());
			nextIn: for (PTPin wireEndPin : allInPinOfEndEle) {
				for (PTVHDLElement wireStartEle : originalData) {
					// all possible pins as the start of a wire output and
					// in-out
					ArrayList<PTPin> allOutPinOfStartEle = new ArrayList<PTPin>();
					if (wireStartEle.getOutputPins() != null)
						allOutPinOfStartEle.addAll(wireStartEle.getOutputPins());
					if (wireStartEle.getInoutPins() != null)
						allOutPinOfStartEle.addAll(wireStartEle.getInoutPins());
					for (PTPin wireStartPin : allOutPinOfStartEle) {
						String wireStartName = wireStartPin.getPinName();
						String wireEndName = wireEndPin.getPinName();
						if (wireStartName.equals(wireEndName)) {
							int endIndex = originalData.indexOf(wireEndEle);
							int startIndex = originalData.indexOf(wireStartEle);

							dataGraph.get(endIndex).getParent().add(
									dataGraph.get(startIndex));
							Point newWireStartNode = wireStartPin.getLastNode()
									.toPoint();
							Point newWireEndNode = wireEndPin.getFirstNode()
									.toPoint();

							PTWire newWire = new PTWire(newWireStartNode,
									newWireEndNode);
							newWire.setStartElement(wireStartEle);
							newWire.setEndElement(wireEndEle);
							newWire.setStartPin(wireStartPin);
							newWire.setEndPin(wireEndPin);

							// wire name: "wire" + start element name + pin Nr.
							// + end element name + pin Nr.
							/*
							 * newWire.setId("wire" +
							 * originalData.get(j).getScriptId() + l +
							 * originalData[i].getScriptId() + k);
							 */
							wires.add(newWire);
							// an input accept only one entrance
							continue nextIn;
						}
					}

				}
			}
		}

		// set the relative location of elements
		longitude = new int[elementsCounter];

		for (VHDLNode vhdlNode : dataGraph) {
			// if a Element have one of the "black box input"
			if (vhdlNode.getParent().size() < vhdlNode.getOriginalElement()
					.getInputPins().size()) {
				vhdlNode.leave = 0;
				vhdlNode.visited = true;
				continue;
			}
			vhdlNode.calculateLeave();
		}
		for (int i = 0; i < dataGraph.size(); i++) {
			longitude[i] = dataGraph.get(i).leave;
		}
		int counter = dataGraph.size();
		latitude = new int[elementsCounter];
		for (int i = 0; i < longitude.length; i++) {
			int lati = 0;
			for (int j = 0; j < longitude.length; j++) {
				if (longitude[j] == i) {
					latitude[j] = lati;
					lati++;
					counter--;
				}
			}
			if (counter == 0)
				break;
		}
		int horizontalNr = 0, verticalNr = 0;
		// store the location in every element
		// and get the width and length of net
		for (int i = 0; i < originalData.size(); i++) {
			originalData.get(i).setAnimationLocation(
					new Point(longitude[i], latitude[i]));
			horizontalNr = Math.max(horizontalNr, longitude[i] + 1);
			verticalNr = Math.max(verticalNr, latitude[i] + 1);
		}
		// element's net
		elementNet = new AnimationTract[horizontalNr * 2 + 1][verticalNr * 2 + 1];
		for (int i = 0; i < elementNet.length; i++)
			for (int j = 0; j < elementNet[i].length; j++){
				elementNet[i][j] = new AnimationTract();	
				if(i%2+j%2==2)
					elementNet[i][j].setForWire(false);
			}

	}

	public ArrayList<PTVHDLElement> getOriginalData() {
		return originalData;
	}

	public ArrayList<PTWire> getWires() {
		return wires;
	}

	public AnimationTract[][] getElementNet() {
		return elementNet;
	}

}
