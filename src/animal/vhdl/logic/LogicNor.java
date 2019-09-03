package animal.vhdl.logic;
/** <br>
 * 
 *
 * @author Lu,Zheng
 * @version 1.0
 */
public class LogicNor extends LogicGate{
	public LogicNor(char [] inputList){
		LogicOr step1=new LogicOr(inputList);
		char [] step1Result=new char[1];
		step1Result[0]=step1.getLogicResult();
		LogicNot step2=new LogicNot(step1Result);
		logicResult=step2.getLogicResult();
	}
}
