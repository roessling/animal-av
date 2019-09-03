/**
 * 
 */
package animal.vhdl.logic;

/**
 * @author p_li
 * 
 */
public class LogicRS {
	private boolean set;
	private boolean reset;

	private boolean sd;
	private boolean rd;

	private boolean clk;
	private boolean ce;

	private boolean q;
	private boolean revQ;

	public LogicRS(boolean[] currentState) {
		if (currentState.length == 8) {
			set = currentState[0];
			reset = currentState[1];
			sd = currentState[2];
			rd = currentState[3];
			clk = currentState[4];
			ce = currentState[5];
			q = currentState[6];
			revQ = currentState[7];
		}
	}

	public boolean getQValue() {
		// TODO Auto-generated method stub
		// asynchronous set: return 1
		if (sd)
			return true;
		// synchronous control not effective or no clock signal: value keeping
		if (!ce || !clk)
			return q;
		// set: return 1. (if set and reset are both 1, it is a invalid
		// situation: the q value and the value of the reverse q are both 1.
		if (set)
			return true;
		// otherwise: return 0
		return false;
	}

	public boolean getRevQValue() {
		// TODO Auto-generated method stub
		// asynchronous reset: return 1
		if (rd)
			return true;
		// synchronous control not effective or no clock signal: value keeping
		if (!ce || !clk)
			return revQ;
		// reset: return 1. (if set and reset are both 1, it is a invalid
		// situation: the q value and the value of the reverse q are both 1.
		if (reset)
			return true;
		// otherwise: return 0
		return false;
	}
	

}
