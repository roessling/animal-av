package htdptl.animators;

import htdptl.ast.AST;
import htdptl.prettyPrinting.ExpressionPositions;
import htdptl.prettyPrinting.PrettyPrinter;
import htdptl.traces.TraceManager;
import htdptl.util.Util;

import java.util.ArrayList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * Animation of procedure calls of procedures that were defined in the HtDP-TL program
 *
 */
public class ProcedureAnimator extends AbstractAnimator {

	private Text closingParenthesis;
	private AST functionCall;
	private PrettyPrinter header;
	private Text text;
	private Text define;
	private PrettyPrinter body;
	private ArrayList<AST> arguments;
	private ArrayList<AST> expressionsRight;
	private AST definition;
  private AST signature;

	@Override
	public void animate(Language lang, TraceManager traceManager) {
		super.animate(lang, traceManager);

		functionCall = trace.getRedex();
		definition = trace.getDefinition();
		signature = definition.getChild(0);	
		arguments = functionCall.getChildren();

		/*
		 * 1. show the ast on the left side. add arguments and the function call
		 * as elements to highlight Use mode identic to prevent highlighting of
		 * other appearences of 'arguments' in the AST
		 */		
		ArrayList<AST> expressionsLeft = new ArrayList<AST>();
		expressionsLeft.addAll(arguments);
		expressionsLeft.add(functionCall.getOperator());
		prettyPrinter.print(trace.getAst(), trace.getRedex(), null, expressionsLeft);
		lang.nextStep();

		/*
		 * 2. left: highlight the redex (the function call) right: show the
		 * definition of the procedure add formal parameters as elements to
		 * highlight
		 */
		prettyPrinter.highlight(functionCall.getOperator(), true);

		showProcedureDefinition();
		header.highlight(0, 1);
		lang.nextStep();

		// high-light parameter and unhighlight function name
		prettyPrinter.highlight(functionCall.getOperator(), false);
		body.highlight(functionCall.getOperator(), false);
		prettyPrinter.highlight(arguments, true);
		header.highlight(signature.getChildren(), true);
		body.highlight(signature.getChildren(), true);
		lang.nextStep();

		/*
		 * 3. Substitute
		 */			
		body.print(trace.getResolvedBody(), null, null, trace.getSubstitutedExpressions());
		body.highlight(trace.getSubstitutedExpressions(), true);
		closingParenthesis.hide();
		showClosingParenthesis();		
		lang.nextStep();

		text.hide();
		define.hide();
		header.hide();
		closingParenthesis.hide();
		prettyPrinter.hide();
		body.hide();
		traceManager.next();

	}

	private void showProcedureDefinition() {
		Node textTarget = new Offset(100, 0, prettyPrinter.getSourceCode(),
				AnimalScript.DIRECTION_NE);
		String headerText = "Procedure " + functionCall.getOperator() + ":";
		text = lang.newText(textTarget, headerText, "", null);
		text.setFont(Util.getBoldFont(), null, null);

		define = lang.newText(new Offset(0, 5, text,
				AnimalScript.DIRECTION_SW), "(define ", "", null);
		define.setFont(Util.getFont(), null, null);

		
		expressionsRight = new ArrayList<AST>();
		expressionsRight.add(signature.getOperator());
		expressionsRight.addAll(signature.getChildren());

		// the header: (f a1 a2 ...)
		header = new PrettyPrinter(lang);
		header.setTarget(new Offset(0, 0, define, AnimalScript.DIRECTION_N));
		header
				.print(definition.getChild(0), null, null,
						expressionsRight);
		// the procedure body
		body = new PrettyPrinter(lang);
		body.setTarget(new Offset(0, 5, header.getSourceCode(),
				AnimalScript.DIRECTION_NW));

		body.print(definition.getChild(1), null, null, expressionsRight, ExpressionPositions.EQUALS);

		showClosingParenthesis();
	}

	private void showClosingParenthesis() {
		closingParenthesis = lang.newText(new Offset(-10, 0, body
				.getSourceCode(), AnimalScript.DIRECTION_SW), ")", "", null);
	}

}
