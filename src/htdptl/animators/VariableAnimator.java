package htdptl.animators;

import htdptl.ast.AST;
import htdptl.prettyPrinting.PrettyPrinter;
import htdptl.traces.TraceManager;
import htdptl.util.Util;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * Animation of variables. The definition of the variable is shown next to the expression.   
 *
 */
public class VariableAnimator extends AbstractAnimator {

	@Override
	public void animate(Language lang, TraceManager traceManager) {
		super.animate(lang, traceManager);

		AST definition = trace.getDefinition();
		
		prettyPrinter.print(trace.getAst(), trace.getRedex(), null, null);
		lang.nextStep();
		prettyPrinter.highlightRedex();
		lang.nextStep();

		Node textTarget = new Offset(100, 0, prettyPrinter.getSourceCode(),
				AnimalScript.DIRECTION_NE);
		String definitionHeader = "Definition of "
			+ definition.getChild(0).getOperator() + ":";
		Text text = lang.newText(textTarget, definitionHeader, "", null);
		text.setFont(Util.getBoldFont(), null, null);
		Node rightTarget = new Offset(0, 5, text, AnimalScript.DIRECTION_W);
		PrettyPrinter right = new PrettyPrinter(lang);
		right.setTarget(rightTarget);		
		right.print(trace.getDefinition(), null, null, null);
		lang.nextStep();

		prettyPrinter.hide();
		right.hide();
		text.hide();
		prettyPrinter.print(trace.getAst(), trace.getRedex(), trace.getRedexValue(),
				null);
		prettyPrinter.highlightRedex();
		step();

		prettyPrinter.hide();
		
		

	}
}
