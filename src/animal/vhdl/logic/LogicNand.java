package animal.vhdl.logic;
/** <br>
 * 
 *
 * @author Lu,Zheng
 * @version 1.0
 */
public class LogicNand extends LogicGate{
	public LogicNand(char [] inputList){
		LogicAnd step1=new LogicAnd(inputList);
		char [] step1Result=new char[1];
		step1Result[0]=step1.getLogicResult();
		LogicNot step2=new LogicNot(step1Result);
		logicResult=step2.getLogicResult();
	}
}
