/**
 * 
 */
package animal.vhdl.logic;

import java.util.ArrayList;

/**
 * @author p_li
 * 
 */
public class LogicMux {
	private ArrayList<Boolean> inputs;
	private ArrayList<Boolean> selection;
	private Boolean output;

	public LogicMux(ArrayList<Boolean> in, ArrayList<Boolean> sel) {
		inputs = in;
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
		if (selectionValue < inputs.size()) {
			output = inputs.get(selectionValue);
		}
	}

	/**
	 * 
	 * 
	 * @return the output
	 */
	public boolean getOutput() {
		return output;
	}

}
