package htdptl.stepHandlers;

import htdptl.ast.AST;
import htdptl.ast.Expression;
import htdptl.stepper.IHandler;
import htdptl.stepper.IStepper;
import htdptl.visitors.VisitorUtil;

/**
 * This IHandler evaluates if expressions 
 *
 */
public class IfHandler implements IHandler {

	@Override
	public void step(IStepper stepper) {
		String condition = VisitorUtil.toCode(stepper.getRedex().getChild(0));
		AST branch = (condition.equals("true")) ? ((Expression) stepper.getRedex()).getChild(1)
				: ((Expression) stepper.getRedex()).getChild(2);
		stepper.replaceRedex(branch);
	
	}

}
