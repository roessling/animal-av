package animal.vhdl.graphics;

import java.awt.Point;
import java.util.ArrayList;

import animal.graphics.PTPoint;
import animal.graphics.PTPolyline;

public class PTWire extends PTPolyline {
	public static final String DIRECTION_EAST = "EAST";
	public static final String DIRECTION_NORTH = "NORTH";
	public static final String DIRECTION_WEST = "WEST";
	public static final String DIRECTION_SOUTH = "SOUTH";

	private ArrayList<Point> route;
	private PTPin startPin;
	private PTPin endPin;
	private PTVHDLElement startElement;
	private PTVHDLElement endElement;
	private int walkSpeed;
	public static final String WIRE_TYPE = "Wire";

	public PTWire() {
		this(new Point(0, 0), new Point(10, 0));
	}

	public PTWire(Point startNode, Point endNode) {
		super();
		nodes.add(new PTPoint(startNode));
		nodes.add(new PTPoint(endNode));
		walkSpeed = 150;
	}

	public PTVHDLElement getStartElement() {
		return startElement;
	}

	public void setStartElement(PTVHDLElement startEle) {
		this.startElement = startEle;
	}

	public PTVHDLElement getEndElement() {
		return endElement;
	}

	public void setEndElement(PTVHDLElement endEle) {
		this.endElement = endEle;
	}

	public void setRoute(ArrayList<Point> route) {
		this.route = route;
		// TODO Auto-generated method stub

	}

	/**
	 * @return the startPin
	 */
	public PTPin getStartPin() {
		return startPin;

	}

	/**
	 * @param startPin
	 *            the startPin to set
	 */
	public void setStartPin(PTPin startPin) {
		this.startPin = startPin;
	}

	/**
	 * @return the endPin
	 */
	public PTPin getEndPin() {
		return endPin;
	}

	/**
	 * @param endPin
	 *            the endPin to set
	 */
	public void setEndPin(PTPin endPin) {
		this.endPin = endPin;
	}

	/**
	 * @return the startDirection
	 */
	public String getStartDirection() {
		PTPin pin = getStartPin();
		Point fn = pin.getFirstNode().toPoint();
		Point en = pin.getLastNode().toPoint();
		int fx = fn.x;
		int fy = fn.y;
		int ex = en.x;
		int ey = en.y;
		if (fx == ex && fy < ey)
			return DIRECTION_SOUTH;
		if (fx == ex && fy > ey)
			return DIRECTION_NORTH;
		if (fx < ex && fy == ey)
			return DIRECTION_EAST;
		if (fx > ex && fy == ey)
			return DIRECTION_WEST;
		return null;
	}

	/**
	 * @return the endDirection
	 */
	public String getEndDirection() {
		PTPin pin = getEndPin();
		Point fn = pin.getFirstNode().toPoint();
		Point en = pin.getLastNode().toPoint();
		int fx = fn.x;
		int fy = fn.y;
		int ex = en.x;
		int ey = en.y;
		if (fx == ex && fy < ey)
			return DIRECTION_SOUTH;
		if (fx == ex && fy > ey)
			return DIRECTION_NORTH;
		if (fx < ex && fy == ey)
			return DIRECTION_EAST;
		if (fx > ex && fy == ey)
			return DIRECTION_WEST;
		return null;
	}

	public ArrayList<Point> getRoute() {
		return route;
	}

	public String toScript() {
		String scr = "wire \"" + getObjectName() + "\" ";
		for (PTPoint p : getNodes()) {
			String pstring = "( " + p.getX() + "," + p.getY() + ") ";
			scr += pstring;
		}

		return scr;
	}

	/**
	 * @return the walkSpeed
	 */
	public int getWalkSpeed() {
		return walkSpeed;
	}

	/**
	 * @param walkSpeed
	 *            the walkSpeed to set
	 */
	public void setWalkSpeed(int walkSpeed) {
		this.walkSpeed = walkSpeed;
	}

	public String getObjectName() {
		String on = "wire_";
		if (getStartElement() != null && getStartPin() != null
				&& getEndElement() != null && getEndPin() != null) {
			on += getStartElement().getObjectName() + "-";
			on += getStartPin().getObjectName() + "->";
			on += getEndElement().getObjectName() + "-";
			on += getEndPin().getObjectName();
		}else
			on+="black";

		return on;
	}
	  /**
	 * returns the type of this object
	 * 
	 * @return the type of the object
	 */
  public String getType() {
    return PTWire.WIRE_TYPE;
  }

}
