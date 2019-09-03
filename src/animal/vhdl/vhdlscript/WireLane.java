package animal.vhdl.vhdlscript;

import animal.graphics.PTPoint;

public class WireLane {
	private PTPoint holder=null;
	
	public WireLane(PTPoint p){
		holder=p;
	}

	public PTPoint getHolder() {
		return holder;
	}

	public void setHolder(PTPoint holder) {
		this.holder = holder;
	}

}
