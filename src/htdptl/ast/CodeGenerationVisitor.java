package htdptl.ast;



/**
 * This visitor generates the code of an AST 
 *
 */
public class CodeGenerationVisitor implements IVisitor {

	private String result = "";

	@Override
	public void visit(Expression expression) {
		result += expression.getLeftParenthesis();
		if (expression.getOperator() != null) {
			expression.getOperator().accept(this);
			result += " ";
			for (int i = 0; i < expression.numChildren(); i++) {
				expression.getChild(i).accept(this);
				if (i < expression.numChildren() - 1) {
					result += " ";
				}
			}
		}
		result += expression.getRightParenthesis();

	}

	@Override
	public void visit(Leaf leaf) {
		result += convert(leaf);

	}

	private String convert(Leaf leaf) {
		Object value = leaf.getValue();
		if (value instanceof Double) {
			Double result = (Double) value;
			if (result.equals(new Double(result.intValue()))) {
				return new Double(result.intValue()).toString();
			} else {
				return new Double(Math.round(result * 100.) / 100.).toString();
			}
		}
		if (value instanceof String) {
			if (value.toString().startsWith("'")) {
				return value.toString();
			} else {
				return "\"" + value.toString() + "\"";
			}
		}
		return value.toString();
	}

	public String getCode() {
		return result;
	}

}
