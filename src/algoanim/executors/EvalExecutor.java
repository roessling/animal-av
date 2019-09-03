package algoanim.executors;

import java.io.StringReader;
import java.util.Vector;

import algoanim.annotations.Annotation;
import algoanim.annotations.Executor;
import algoanim.executors.formulaparser.FormulaParser;
import algoanim.executors.formulaparser.FormulaParserTreeConstants;
import algoanim.executors.formulaparser.ParseException;
import algoanim.executors.formulaparser.SimpleNode;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;

public class EvalExecutor extends Executor {
	public EvalExecutor(Variables vars, SourceCode src) {
		super(vars, src);
	}

	@Override
	public boolean exec(Annotation anno)
	{
		if(anno.getName().equals(Annotation.EVAL)
		&& anno.getParameters().size() == 2) // var, expr
		{
			Vector<String> params = anno.getParameters();
			String targetKey = params.get(0);
			FormulaParser parser = new FormulaParser(new StringReader(params.get(1)));
			try {
				SimpleNode root = parser.query();
				Double result = eval((SimpleNode) root.jjtGetChild(0));
				vars.set(targetKey, result.toString());
			} catch (ParseException e) {
				System.out.println(e);
			}

			return true;
		}
		return false;
	}

	public Double eval(SimpleNode node)
	{
		int id = node.id;

		SimpleNode lhs = null;
		SimpleNode rhs = null;

		if(id != FormulaParserTreeConstants.JJTNUMBER
		&& id != FormulaParserTreeConstants.JJTIDENTIFIER) {
			lhs = (SimpleNode) node.jjtGetChild(0);
			rhs = (SimpleNode) node.jjtGetChild(1);
		}

		/*if (id == FormulaParserTreeConstants.JJTNUMBER) {
			System.out.println("eval(): " + node.getText());
		} else if (id == FormulaParserTreeConstants.JJTIDENTIFIER) {
			System.out.println("eval(): " + node.getText() + " = "
					+ vars.get(node.getText()));
		} else
			System.out.println("eval(): evaluating AST nodetype " + id + " ["
					+ FormulaParserTreeConstants.jjtNodeName[id] + "]");*/

		switch (id) {
		case FormulaParserTreeConstants.JJTPLUS:
			return eval(lhs) + eval(rhs);
		case FormulaParserTreeConstants.JJTMINUS:
			return eval(lhs) - eval(rhs);
		case FormulaParserTreeConstants.JJTMULT:
			return eval(lhs) * eval(rhs);
		case FormulaParserTreeConstants.JJTDIV:
			return eval(lhs) / eval(rhs);

		case FormulaParserTreeConstants.JJTNUMBER:
			return Double.parseDouble(node.getText());
		case FormulaParserTreeConstants.JJTIDENTIFIER:
			return Double.parseDouble(vars.get(node.getText()));

		default:
			throw new java.lang.IllegalArgumentException("unknown node id [" + id + "]");
		}
	}

}
