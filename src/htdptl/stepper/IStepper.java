package htdptl.stepper;

import htdptl.ast.AST;

import java.util.ArrayList;


public interface IStepper {

	void replaceRedex(AST value);
	
	AST getCurrentDefinition();

	AST getRedex();

	Object eval(Object sexp);

	void setResolvedBody(AST procedureBody);

	void setSubstitutedExpressions(ArrayList<AST> substitutedExpressions);

	

}
