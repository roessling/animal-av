package algoanim.primitives.updater;

import algoanim.primitives.ArrayMarker;
import algoanim.util.Timing;
import algoanim.variables.Variable;
import algoanim.variables.VariableObserver;

public class ArrayMarkerUpdater implements VariableObserver {

	Timing t;
	Timing d;
	ArrayMarker am;
	Variable v;
	int maxPosition;
	
	public ArrayMarkerUpdater(ArrayMarker am, Timing t, Timing d, int maxPosition) {
		this.am = am;
		this.t = t;
		this.d = d;
		this.maxPosition = maxPosition;
		
		am.hide();
	}
	
	public void setVariable(Variable v) {
		this.v = v;
		v.addObserver(this);
		am.show();
	}
	
	@Override
	public void update() {
		int newPos = v.getValue(Integer.class);
		if(newPos < 0 || newPos > maxPosition)
			am.moveOutside(t, d);
		else
			am.move(newPos, t, d);
	}

}
