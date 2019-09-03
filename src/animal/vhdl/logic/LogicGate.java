package animal.vhdl.logic;
/** <br>
 * 
 *
 * @author Lu,Zheng
 * @version 1.0
 */
public class LogicGate extends LogicVHDL implements LogicVHDLGate{
	protected char logicResult;

	public char getLogicResult() {
		// TODO Auto-generated method stub
		return this.logicResult;
	}
	protected int[] findResult(char logicResult, char c) {
		// TODO Auto-generated method stub
		int coordinate[]=new int[2];
		for (int k=0;k<LOGIC_VALUES.length;k++){
			if(LOGIC_VALUES[k].equals(logicResult))
				coordinate[0]=k;
			if(LOGIC_VALUES[k].equals(c))
				coordinate[1]=k;
		}
		return coordinate;
	}

}
