package generators.misc.arithconvert.sugar;

import generators.misc.arithconvert.BinaryExpression;
import generators.misc.arithconvert.Expression;
import generators.misc.arithconvert.Operator;

/**
 * An expression which uses the Mul-Operator.
 * @author Jannis Weil, Hendrik Wuerz
 */
public class Mul extends BinaryExpression {
	
	/**
	 * Create an binary expression which uses the Mul-Operator.
	 * @param left the left expression
	 * @param right the right expression
	 */
	public Mul(Expression left, Expression right){
		super(left, right, Operator.Mul);
	}
}
