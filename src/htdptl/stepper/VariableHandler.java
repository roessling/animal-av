package htdptl.stepper;

import htdptl.ast.AST;
import htdptl.exceptions.StepException;

/**
 * This IHandler resolves a variable using its definition.
 *
 */
public class VariableHandler implements IHandler {

	@Override
	public void step(IStepper stepper) throws StepException {
		AST symbolDefinition = stepper.getCurrentDefinition();
		AST value = symbolDefinition.getChild(1);
		stepper.replaceRedex(value);
	}

}
