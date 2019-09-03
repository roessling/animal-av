/**
 * 
 */
package animal.vhdl.logic;

/**
 * @author p_li
 * 
 */
public class LogicD {
	private boolean d;

	private boolean sd;
	private boolean rd;

	private boolean clk;
	private boolean ce;

	private boolean q;

	public LogicD(boolean[] currentState) {
		if (currentState.length == 6) {
			d = currentState[0];
			sd = currentState[1];
			rd = currentState[2];
			clk = currentState[3];
			ce = currentState[4];
			q = currentState[5];
		}
	}
	
	

	/**
	 * return the Q value 
	 * 
	 * @return the q
	 */
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
		// q = d
		if (d)
			return true;
		// otherwise: return 0
		return false;
	}

}