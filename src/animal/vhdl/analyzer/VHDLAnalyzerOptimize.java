package animal.vhdl.analyzer;

import animal.misc.MessageDisplay;
import animal.vhdl.optimization.QuineMcCluskey.Formula;

public class VHDLAnalyzerOptimize {
	public static String quineMcCluskey(String expression){
		String et=Formula.expressionTranslate(expression);
		if (et!=null){
			if (et.equals("Error 0")){
			MessageDisplay.errorMsg("Quine Mc Cluskey can't support Nand, Nor, Xor, Xnor", MessageDisplay.INFO);
			return expression;
			
			}
			else{
		        Formula f = Formula.read(et);
		        if (f!=null){
			        f.reduceToPrimeImplicants();
			        f.reducePrimeImplicantsToSubset();
					return f.resultTranslate();
				}
		        else
		        	return expression;
				}
		}
		else
			return expression;
		}
		
}
