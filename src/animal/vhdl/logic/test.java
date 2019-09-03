package animal.vhdl.logic;

public class test {
	public static void main(String[] args){
		char [] a={'z','x'};
		String[] b={"1","1","0"};
		//LogicAnd test=new LogicAnd(a);
//		LogicNand test3=new LogicNand(a);
//		LogicOr test1=new LogicOr(a);
		LogicXor test4=new LogicXor(a);
		LogicXnor test5=new LogicXnor(a);
		LogicMultAnd test6=new LogicMultAnd(b);
		//LogicNot test2=new LogicNot(a);
		//System.out.println(test.getLogicResult());
		System.out.println(test4.getLogicResult());
		System.out.println(test5.getLogicResult());
		System.out.println(test6.getLogicResults());
		//System.out.println(test2.getLogicResult());
	}
	
}
