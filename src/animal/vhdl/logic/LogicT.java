package animal.vhdl.logic;

public class LogicT {
	private boolean t;

	private boolean sd;
	private boolean rd;

	private boolean clk;
	private boolean ce;

	private boolean q;

	public LogicT(boolean[] currentState) {
		if (currentState.length == 6) {
			t = currentState[0];
			sd = currentState[1];
			rd = currentState[2];
			clk = currentState[3];
			ce = currentState[4];
			q = currentState[5];
		}
	}

	public boolean getQValue() {
		// TODO Auto-generated method stub
		// asynchronous set: return 1
		if (sd)
			return true;
		// asynchronous reset: return 0
		if (rd)
			return false;
		// synchronous control not effective or no clock signal: value keeping
		if (!ce || !clk)
			return q;
		// t turn over the value of q 
		if (t)
			return !q;
		// otherwise: value keeping
		return q;
	}
}
