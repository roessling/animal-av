package htdptl.ast;

import jsint.Pair;
import jsint.Symbol;
import jsint.U;

/**
 * Factory for converting expression representations of JScheme to AST objects.  
 *
 */
public class ASTFactory {

	private static boolean quote;

	public static AST build(Object expression) {
		return build(expression, false);
	}

	public static AST build(Object expression, boolean quote) {
		ASTFactory.quote = quote;
		if (expression instanceof Object[]) {
			return buildFromObjectArray((Object[])expression);
		}
		else {
			return buildFromList(expression);			
		}
		
	}

	private static AST buildFromObjectArray(Object[] expression) {
		
		if (expression[0].toString().equals("quote")) {
			AST result = new Leaf(expression[1]);
			return result;
		}
		else {
			Expression result = new Expression();
			result.addChild(new Leaf(expression[0]));
			for (int i=1; i<expression.length;i++) {
				if (expression[i] instanceof Object[]) {
					result.addChild(buildFromObjectArray((Object[]) expression[i]));
				}
				else {
					result.addChild(new Leaf(expression[i]));
				}
			}
			return result;
		}
		
		
	}

	private static AST buildFromList(Object expression) {
		// leaf
		if (!(expression instanceof Pair)) {
			Object operator = expression;
			AST result = new Leaf(operator);
			return result;
		// expression
		} else {
			if (expression == Pair.EMPTY) {
				return new Leaf(Symbol.intern("empty"));
			}

			Object operator = U.first(expression);
			Object children = U.rest(expression);
			
			if (operator.toString().equals("quote")) {
				Object second = U.second(expression);
				if (second instanceof Pair) {
					Expression result = new Expression();
					result.addChild(new Leaf(Symbol.intern("list")));
					children = U.first(children);
					while (children != Pair.EMPTY) {
						result.addChild(buildFromList(U.list("quote", U
								.first(children))));
						children = U.rest(children);
					}
					return result;
				} else {
					if (second instanceof Symbol) {
						second = "'" + second;
					}
					AST result = new Leaf(second);
					return result;

				}

			} else if (operator.toString().equals("define-struct")) {
				Object second = U.second(expression);
				Expression result = new Expression();
				result.addChild(new Leaf(Symbol.intern("define-struct")));
				result.addChild(new Leaf(second));
				Expression fields = new Expression();
				addChildren(fields, U.second(children));
				result.addChild(fields);
				return result;
				
			}
			// else if (operator.equals("define-struct")) {
			// AST result = new AST("define-struct");
			// String structName = U.first(arguments));
			// result.addChild(new AST(structName));
			// AST fields = new AST("");
			// addParameters(fields, U.first(U.rest(arguments)));
			// result.addChild(fields);
			// return result;
			// }
			else {
				Expression result = new Expression();
				result.addChild(ASTFactory.build(operator));
				addChildren(result, children);
				return result;
			}
		}

	}

	private static void addChildren(Expression ast, Object p) {
	  Object myP = p;
		while (myP != Pair.EMPTY) {
			Object first = U.first(myP);
			if (first instanceof Pair) {
				ast.addChild(buildFromList(first));
			} else {
				Object operator = U.first(myP);
				if (quote) {
					operator = "'" + operator;
				}
				ast.addChild(new Leaf(operator));
			}
			myP = U.rest(myP);
		}
	}

	public static void addConditions(Expression ast, Object conditions) {
	  Object myCond = conditions;
		while (myCond != Pair.EMPTY) {
			/*
			 * p is a list of conditions and corresponding branches ((c1 a1) (c2
			 * a2) ... )
			 */
			Object condition = U.first(myCond);
			Expression conditionAST = new Expression();
			conditionAST.addChild(buildFromList(U.first(condition)));
			conditionAST.addChild(buildFromList(U.first(U.rest(condition))));
			ast.addChild(conditionAST);
			myCond = U.rest(myCond);
		}

	}
}
