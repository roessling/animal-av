package animal.vhdl.logic;
/** <br>
 * 
 *
 * @author Lu,Zheng
 * @version 1.0
 */
public class LogicMultAnd extends LogicMultGate{
	public LogicMultAnd(String[] inputList){
		String temp="";
		logicResults="";
		if (checkStringLength(inputList)){
			for (int j=0;j<inputList[0].length();j++){
				for (int i=0;i<inputList.length;i++){
					temp=temp+inputList[i].charAt(j);
				}
				logicResults=logicResults+new LogicAnd(temp.toCharArray()).getLogicResult();
				temp="";
			}
		}
		else{
			Exception nis=new Exception("Length of the strings are not equally");
			try {
				throw nis;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	}
}
