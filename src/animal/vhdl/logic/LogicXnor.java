package animal.vhdl.logic;
/** <br>
 * 
 *
 * @author Lu,Zheng
 * @version 1.0
 */
public class LogicXnor extends LogicGate{
	public LogicXnor(char [] inputList) {
		LogicXor step1=new LogicXor(inputList);
		char [] step1Result=new char[1];
		step1Result[0]=step1.getLogicResult();
		LogicNot step2=new LogicNot(step1Result);
		logicResult=step2.getLogicResult();
	}
}
