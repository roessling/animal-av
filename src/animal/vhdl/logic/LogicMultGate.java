package animal.vhdl.logic;
/** <br>
 * 
 *
 * @author Lu,Zheng
 * @version 1.0
 */
public class LogicMultGate implements LogicVHDLElementMult,LogicVHDLGate{
	protected String logicResults;

	public String getLogicResults() {
		// TODO Auto-generated method stub
		return this.logicResults;
	}

	public char getLogicResult(int index) {
		// TODO Auto-generated method stub
		return this.logicResults.charAt(index);
	}
	public boolean checkStringLength (String[] inputList){
		boolean equallength=true;
		int stringLength=inputList[0].length();
		for (int i=0;i<inputList.length;i++){
			if (inputList[i].length()!=stringLength){
				equallength=false;
				break;
			}
					
		}
		return equallength;
	}

	public char getLogicResult() {
		return getLogicResult(0);
	}
}
