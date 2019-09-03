package htdptl.stepHandlers;

import htdptl.ast.AST;
import htdptl.stepper.IHandler;
import htdptl.stepper.IStepper;
import htdptl.visitors.VisitorUtil;

import java.util.Iterator;


/**
 * This IHandler evaluates cond expressions.  
 *
 */
public class CondHandler implements IHandler {

	@Override
	public void step(IStepper stepper) {

		for (Iterator<AST> iterator = stepper.getRedex().getChildren().iterator(); iterator
				.hasNext();) {
			AST child = iterator.next();
			String condition = VisitorUtil.toCode(child.getOperator());
			if (condition.equals("true") || condition.equals("else")) {
				stepper.replaceRedex(child.getChild(0));
				break;
			}
		}

	}

}
