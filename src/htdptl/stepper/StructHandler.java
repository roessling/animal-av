package htdptl.stepper;

import htdptl.ast.AST;
import htdptl.ast.Leaf;
import htdptl.exceptions.StepException;
import htdptl.visitors.VisitorUtil;

import java.util.ArrayList;

/**
 * This IHandler evaluates the selectors and the predicate of a structure  
 *
 */
public class StructHandler implements IHandler {

	private String structName;
	private ArrayList<String> fields;

	public StructHandler(String structName, ArrayList<String> fields) {
		this.structName = structName;
		this.fields = fields;
	}

	@Override
	public void step(IStepper stepper) throws StepException {
		AST redex = stepper.getRedex();
		String operator = VisitorUtil.toCode(redex.getOperator());
		if (operator.equals(structName + "?")) {
			Boolean result = VisitorUtil.toCode(redex.getChild(0).getOperator()).equals("make-"
					+ structName) ? true : false;
			stepper.replaceRedex(new Leaf(result));
		} else {
			String field = operator.substring(operator.indexOf('-') + 1);
			for (int i = 0; i < fields.size(); i++) {
				if (fields.get(i).equals(field)) {
					stepper.replaceRedex(redex.getChild(0).getChild(i));
					return;
				}
			}
			throw new StepException("unknown field " + field + " for struct "
					+ structName);
		}

	}

}
