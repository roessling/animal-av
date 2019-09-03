/*
 * Created on 30.11.2004
 */
package de.ahrgr.animal.kohnert.asugen;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author ek
 */
public class PolyLine extends AnimalObject {

	public static final int ARROW_NONE = 0;

	public static final int ARROW_FORWARD = 1;

	public static final int ARROW_BACKWARD = 2;

	protected ArrayList<EKNode> nodes;

	protected int arrowType = ARROW_NONE;

	public PolyLine(AnimalScriptWriter writer) {
		super(writer);
		name = "polyline" + instance_index;
		nodes = new ArrayList<EKNode>();
	}

	public void addNode(EKNode node) {
		if (!registered) {
			nodes.add(node);
		}
	}

	public void addNodeRel(int dx, int dy) {
		if (nodes.size() < 1)
			nodes.add(new AbsoluteNode(scriptwriter, dx, dy));
		else {
			EKNode last = nodes.get(nodes.size() - 1);
			nodes.add(last.createOffset(dx, dy));
		}
	}

	public void setArrow(int arrow_type) {
		if (!registered)
			this.arrowType = arrow_type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see animalobjects.AnimalObject#register()
	 */
	public void register() {
		super.register();
		if (!registered) {
			out.print("polyLine \"");
			out.print(name);
			out.print("\"");
			Iterator<EKNode> i = nodes.iterator();
			while (i.hasNext()) {
				EKNode n = i.next();
				n.print();
			}
			switch (arrowType) {
			case ARROW_FORWARD:
				out.print(" fwArrow");
				break;
			case ARROW_BACKWARD:
				out.print("bwArrow");
				break;
			default:
			}
			printDisplayOptions();
			out.println();
			registered = true;
		}

	}

}
