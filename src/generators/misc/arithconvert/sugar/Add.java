package generators.misc.arithconvert.sugar;

import generators.misc.arithconvert.BinaryExpression;
import generators.misc.arithconvert.Expression;
import generators.misc.arithconvert.Operator;

/**
 * An expression which uses the Add-Operator.
 * @author Jannis Weil, Hendrik Wuerz
 */
public class Add extends BinaryExpression {
	
	/**
	 * Create an binary expression which uses the Add-Operator.
	 * @param left the left expression
	 * @param right the right expression
	 */
	public Add(Expression left, Expression right){
		super(left, right, Operator.Add);
	}
}
