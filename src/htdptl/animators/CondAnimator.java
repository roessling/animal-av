package htdptl.animators;

import htdptl.ast.AST;
import htdptl.traces.TraceManager;
import htdptl.visitors.VisitorUtil;
import algoanim.primitives.generators.Language;

/**
 * Animation of cond expressions
 * The first cond clause with a test equal to "true" or "else" will be highlighted.  
 *
 */
public class CondAnimator extends AbstractAnimator {

	private int offset = 0;

	@Override
	public void animate(Language lang, TraceManager traceManager) {
		super.animate(lang, traceManager);

		searchCondition();

		prettyPrinter.print(trace.getAst(), trace.getRedex(), null, null);
		lang.nextStep();

		// highlight the condition block to which this expression resolves
		prettyPrinter.highlightBlock(prettyPrinter.getRedexPosition(), offset);
		lang.nextStep();

		prettyPrinter.hide();
		traceManager.next();

	}

	/**
	 * Current redex is a cond-expression. Since the cond expression is
	 * reducible, all condition-clauses are evaluated, i.e.: (cond [false ...]
	 * [false ...] ... [true ...] or [else ...] This method searches for the
	 * first condition branch containing "true" or "else".
	 * 
	 */
	private void searchCondition() {
		AST cond = trace.getRedex();
		for (int i = 0; i < cond.getChildren().size(); i++) {
			if (isConditionTrue(cond, i)) {
				offset = i + 1;
				break;
			}
		}
	}

	/**
	 * Assume this is an cond expression, search first condition with test true
	 * or false
	 * 
	 * @param i
	 * @return
	 */
	private boolean isConditionTrue(AST ast, int i) {
		String condition = VisitorUtil.toCode(ast.getChild(i).getOperator());
		return condition.equals("true") || condition.equals("else");
	}

}
