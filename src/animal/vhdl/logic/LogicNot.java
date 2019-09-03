package animal.vhdl.logic;
/** <br>
 * 
 *
 * @author Lu,Zheng
 * @version 1.0
 */
public class LogicNot extends LogicGate{

	public LogicNot(char [] inputList){
//		boolean result=false;
//		boolean allTrue=true;
		if (inputList.length!=1){
			Exception nis=new Exception("A Not gate cannot have more than one input");
			try {
				throw nis;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{	
			if (inputList[0]==LOGIC_STRONG_LOW || inputList[0]==LOGIC_WEAK_LOW )
				logicResult=LOGIC_STRONG_HIGH;
			else if (inputList[0]==LOGIC_STRONG_HIGH || inputList[0]==LOGIC_WEAK_HIGH)
				logicResult=LOGIC_STRONG_LOW;
			else if (inputList[0]==LOGIC_UNINITIALIZED)
				logicResult=LOGIC_UNINITIALIZED;
			else 
				logicResult=LOGIC_STRONG_UNKNOWN;	
		}
	}
}

