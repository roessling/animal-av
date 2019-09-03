package animal.vhdl.vhdlscript;

import java.util.ArrayList;

import animal.vhdl.graphics.PTVHDLElement;


public class VHDLNode {
	private ArrayList<VHDLNode> parent;
	int leave;
	boolean visited;
	private PTVHDLElement originalElement;

	public VHDLNode(PTVHDLElement ve) {
		originalElement = ve;
		parent = new ArrayList<VHDLNode>(ve.getInputPins().size());
		leave = -1;
		visited = false;
	}

	public ArrayList<VHDLNode> getParent() {
		return parent;
	}

	public void calculateLeave() {
		// TODO Auto-generated method stub
		visited = true;
		if (parent.size() == 0) {
			leave = 0;
			return;
		}
		for (int i = 0; i < parent.size(); i++) {
			if (!parent.get(i).visited)
				parent.get(i).calculateLeave();
			leave = Math.max(leave, parent.get(i).leave + 1);
		}
	}

	public PTVHDLElement getOriginalElement() {
		return originalElement;
	}

}
