package htdptl.filter;

import htdptl.ast.AST;
import htdptl.stepper.Stepper;
import htdptl.visitors.NormalformVisitor;

/**
 * This class is used to follow a sub expression of the current expression that is evaluated.   
 *
 */
public class ExpressionObserver {
	
	private AST parent;
	private int index;
	private NormalformVisitor pv;
	private String operator;
	private Stepper stepper;
	
	public ExpressionObserver(AST parent, int index, NormalformVisitor pv, String operator) {
		this.parent = parent;
		this.index = index;
		this.pv = pv;
		this.operator = operator;		
	}
	
	public ExpressionObserver(Stepper stepper, NormalformVisitor pv,
			String operator) {
		this.stepper = stepper;
		this.pv = pv;
		this.operator = operator;	
	}
	
	public AST getNode() {
		if (parent==null) {
			return stepper.getAST();
		}
		else {
			return parent.getChild(index);
		}
	}

	public boolean isPrimitive() {
		getNode().accept(pv);
		return pv.isPrimitive();
	}
	
	public String getOperator() {
		return operator;
	}
	
}
