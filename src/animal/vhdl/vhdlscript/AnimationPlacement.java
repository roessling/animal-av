package animal.vhdl.vhdlscript;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Vector;

import animal.graphics.PTPoint;
import animal.graphics.PTPolyline;
import animal.graphics.PTRectangle;
import animal.vhdl.graphics.PTPin;
import animal.vhdl.graphics.PTVHDLElement;
import animal.vhdl.graphics.PTWire;

public class AnimationPlacement {
	private PTRectangle blackBox = new PTRectangle();
	private ArrayList<PTWire> blackWires = new ArrayList<PTWire>();
	private ArrayList<PTPolyline> blackPins = new ArrayList<PTPolyline>();
	private PTVHDLElement[] originalData;
	private ArrayList<PTWire> wires = new ArrayList<PTWire>();
	private int eleSize;
	private int eleGap;
	private final static int laneGap = 5;
	private final static Point firstNodeOfElements = new Point(70, 100);

	public AnimationPlacement(ArrayList<PTVHDLElement> elements) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		AnimationLayout layout = new AnimationLayout(elements);
		originalData = new PTVHDLElement[layout.getOriginalData().size()];
		layout.getOriginalData().toArray(originalData);
		wires = layout.getWires();

		int horizontalNr = layout.getElementNet().length / 2;
		int verticalNr = layout.getElementNet()[0].length / 2;

		int maxLaneNr = 0;
		for (AnimationTract[] ata : layout.getElementNet())
			for (AnimationTract at : ata)
				if (at.isForWire())
					maxLaneNr = Math.max(maxLaneNr, Math.max(at
							.getHorizontalLanesNr(), at.getVerticalLanesNr()));

		eleGap = (laneGap + 1) * maxLaneNr + laneGap;

		eleSize = Math.min(screenSize.width / horizontalNr - eleGap,
				screenSize.height / verticalNr - eleGap);
		eleSize = Math.max(eleSize, 60);
		eleSize = Math.min(eleSize, 150);

		// set tracts location
		for (int i = 0; i < layout.getElementNet().length; i++) {
			for (int j = 0; j < layout.getElementNet()[i].length; j++) {
				int locationX = firstNodeOfElements.x + i / 2
						* (eleSize + eleGap) + i % 2 * eleGap;
				int locationY = firstNodeOfElements.y + j / 2
						* (eleSize + eleGap) + j % 2 * eleGap;
				layout.getElementNet()[i][j].setLocation(new Point(locationX,
						locationY));

			}
		}

		// set elements' location and size
		for (int i = 0; i < originalData.length; i++) {

			originalData[i].setStartNode(firstNodeOfElements.x + eleGap
					+ originalData[i].getAnimationLocation().x
					* (eleGap + eleSize), firstNodeOfElements.y + eleGap
					+ originalData[i].getAnimationLocation().y
					* (eleGap + eleSize));
			originalData[i].setHeight(eleSize);
			originalData[i].setWidth(eleSize);
		}
		// set wires' nodes
		for (PTWire wire : wires) {
			{
				wire.getNodeAt(0).setLocation(
						wire.getStartPin().getLastNode().toPoint());
				wire.getNodeAt(1).setLocation(
						wire.getEndPin().getFirstNode().toPoint());
				//				
				// wire.setNode(0, wire.getStartPin().getLastNode().toPoint());
				// wire.setNode(1, wire.getEndPin().getFirstNode().toPoint());
			}
			PTPoint holder = wire.getNodes().get(0);
			if (wire.getRoute() != null && wire.getRoute().size() > 1) {
				ArrayList<Point> route = wire.getRoute();
				for (Point corner : route) {
					Vector<PTPoint> nodes = wire.getNodes();
					AnimationTract cornerTract = layout.getElementNet()[corner.x][corner.y];
					PTPoint newPoint = new PTPoint();
					PTPoint basePoint = nodes.get(nodes.size() - 2);
					newPoint.setX(basePoint.getX());
					newPoint.setY(basePoint.getY());

					ArrayList<WireLane> horizontalLanes = cornerTract
							.getHorizontalLanes();
					for (int i = 0; i < horizontalLanes.size(); i++) {
						if (holder == horizontalLanes.get(i).getHolder()) {
							newPoint.setY(cornerTract.getLocation().y + i
									* (1 + laneGap) + laneGap);
							break;
						}
					}

					ArrayList<WireLane> verticalLanes = cornerTract
							.getVerticalLanes();
					for (int i = 0; i < verticalLanes.size(); i++) {
						if (holder == verticalLanes.get(i).getHolder()) {
							newPoint.setX(cornerTract.getLocation().x + i
									* (1 + laneGap) + laneGap);
							break;
						}
					}
					wire.getNodes().insertElementAt(newPoint,
							wire.getNodes().size() - 1);
				}
			}
			// last corner
			if (wire.getRoute() != null) {
				Point corner = wire.getRoute().get(wire.getRoute().size() - 1);
				AnimationTract cornerTract = layout.getElementNet()[corner.x][corner.y];
				ArrayList<WireLane> horizontalLanes = cornerTract
						.getHorizontalLanes();
				PTPoint newPoint = new PTPoint();
				PTPoint basePoint = wire.getNodes().get(
						wire.getNodes().size() - 2);
				newPoint.setX(basePoint.getX());
				newPoint.setY(basePoint.getY());
				for (int i = 0; i < horizontalLanes.size(); i++) {
					if (holder == horizontalLanes.get(i).getHolder()) {
						newPoint.setX(wire.getNodes().get(
								wire.getNodes().size() - 1).getX());
						break;
					}
				}

				ArrayList<WireLane> verticalLanes = cornerTract
						.getVerticalLanes();
				for (int i = 0; i < verticalLanes.size(); i++) {
					if (holder == verticalLanes.get(i).getHolder()) {
						newPoint.setY(wire.getNodes().get(
								wire.getNodes().size() - 1).getY());
						break;
					}
				}
				if (wire.getRoute().size() > 1)
					wire.getNodes().setElementAt(newPoint,
							wire.getNodes().size() - 2);
				else
					wire.getNodes().insertElementAt(newPoint,
							wire.getNodes().size() - 1);
			}

		}

		// black box entity
		ArrayList<PTPin> blackInput = new ArrayList<PTPin>();
		ArrayList<PTPin> blackOutput = new ArrayList<PTPin>();
		ArrayList<PTPin> blackControl = new ArrayList<PTPin>();
		int bl = 0;

		for (PTVHDLElement ele : originalData) {
			in: for (PTPin input : ele.getInputPins()) {
				for (PTWire wire : wires) {
					if (wire.getEndPin() == input) {
						continue in;
					}
				}
				blackInput.add(input);
			}
			out: for (PTPin output : ele.getOutputPins()) {
				for (PTWire wire : wires) {
					if (wire.getStartPin().getPinName().equals(
							output.getPinName())) {
						continue out;
					}
				}
				blackOutput.add(output);
			}
			if (ele.getControlPins() != null)
				con: for (PTPin control : ele.getControlPins()) {
					for (PTWire wire : wires) {
						if (wire.getEndPin() == control) {
							continue con;
						}
					}
					blackControl.add(control);
				}
		}

		ArrayList<PTPin> pins = new ArrayList<PTPin>();

		in: for (PTPin pin : blackInput) {
			for (PTPin p : pins) {
				if (pin.getPinName().equals(p.getPinName())) {
					continue in;
				}
			}
			pins.add(pin);
		}
		int blackGap = laneGap * (pins.size() + 1);

		int x = layout.getElementNet().length - 1;
		int y = layout.getElementNet()[0].length - 1;

		Point lastEle = layout.getElementNet()[x][y].getLocation();
		Point lastNode = new Point(lastEle.x + eleGap, lastEle.y + eleGap);

		Point s = new Point(firstNodeOfElements.x - blackGap,
				firstNodeOfElements.y);
		blackBox = new PTRectangle(s, lastNode);
		blackBox.setFilled(false);
		blackBox.setColor(Color.black);

		for (PTPin p : pins) {
			bl += laneGap;
			blackInput.remove(p);
			int wsx = firstNodeOfElements.x - blackGap - 15;
			int wsy = p.getFirstNode().getY();
			Point ws = new Point(wsx, wsy);
			PTWire wire = new PTWire(ws, p.getFirstNode().toPoint());
			PTPolyline bp = new PTPolyline();
			bp.setNode(0, ws);
			bp.setNode(1, new Point(blackBox.getStartNode().x, wsy));
			bp.setColor(Color.black);
			bp.setFWArrow(false);
			blackPins.add(bp);
			blackWires.add(wire);
			for (PTPin pin : blackInput) {
				if (pin.getPinName().equals(p.getPinName())) {
					wire = new PTWire(p.getFirstNode().toPoint(), pin
							.getFirstNode().toPoint());
					int px = firstNodeOfElements.x - bl;
					int py = p.getFirstNode().getY();
					wire.getNodes().insertElementAt(new PTPoint(px, py), 1);
					py = pin.getFirstNode().getY();
					wire.getNodes().insertElementAt(new PTPoint(px, py), 2);
					blackWires.add(wire);
				}
			}

		}

		for (PTPin pin : blackOutput) {
			Point we = new Point(blackBox.getEndNode().x + 20, pin
					.getLastNode().toPoint().y);
			PTWire wire = new PTWire(pin.getLastNode().toPoint(), we);
			blackWires.add(wire);
			PTPolyline bp = new PTPolyline();
			bp.setNode(0, new Point(blackBox.getEndNode().x, we.y));
			bp.setNode(1, we);
			bp.setColor(Color.black);
			bp.setFWArrow(false);
			blackPins.add(bp);
		}
		if (blackControl != null) {

		}

	}

	public PTVHDLElement[] getOriginalData() {
		return originalData;
	}

	public ArrayList<PTWire> getWires() {
		return wires;
	}

	/**
	 * @return the blackBox
	 */
	public PTRectangle getBlackBox() {
		return blackBox;
	}

	/**
	 * @return the blackWires
	 */
	public ArrayList<PTWire> getBlackWires() {
		return blackWires;
	}

	/**
	 * @return the blackPins
	 */
	public ArrayList<PTPolyline> getBlackPins() {
		return blackPins;
	}

}
