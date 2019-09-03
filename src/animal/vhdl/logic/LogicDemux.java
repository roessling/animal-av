/**
 * 
 */
package animal.vhdl.logic;

import java.util.ArrayList;

/**
 * @author p_li
 *
 */
public class LogicDemux {
	
	private ArrayList<Boolean> outputs;
	private ArrayList<Boolean> selection;
	private Boolean output;
	
	public LogicDemux(ArrayList<Boolean> outs, ArrayList<Boolean> sel) {
		outputs = outs;
		selection = sel;
		calculateOutput();
	}

	private void calculateOutput() {
		// selection value
		int selectionValue = 0;
		for (int i = 0; i < selection.size(); i++) {
			if (selection.get(i)) {
				selectionValue += Math.pow(2, i);
			}
		}
		if (selectionValue < outputs.size()) {
			output = outputs.get(selectionValue);
		}
	}

	/** 
	 * @return the output
	 */
	public boolean getOutput() {
		return output;
	}

}
