package htdptl.animators;

import htdptl.traces.TraceManager;
import algoanim.primitives.generators.Language;

/**
 * The default animation (primitive operations like +,-,*,/)
 * The expression is added to the animation, then the value of the evaluated redex is added (e.g. (+ 2 3) = 5)  
 *
 */
public class DefaultAnimator extends AbstractAnimator {

	@Override
	public void animate(Language lang, TraceManager traceManager) {
		super.animate(lang, traceManager);
		if (!traceManager.done()) {
			// show the expression and highlight the redex
			prettyPrinter.print(trace.getAst(), trace.getRedex(), null, null);
			prettyPrinter.highlightRedex();
			lang.nextStep();

			// Show the evaluated redex value
			prettyPrinter.print(trace.getAst(), trace.getRedex(), trace
					.getRedexValue(), null);
			prettyPrinter.highlightRedex();
			lang.nextStep();
			prettyPrinter.hide();
			traceManager.next();
		} else {
			
			prettyPrinter.print(trace.getAst(), trace.getRedex(), null, null);
			lang.nextStep();
			prettyPrinter.hide();
		}
	}
}
