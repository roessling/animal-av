package htdptl.animators;

import htdptl.ast.AST;
import htdptl.prettyPrinting.PrettyPrinter;
import htdptl.traces.TraceManager;
import htdptl.util.Util;
import htdptl.visitors.VisitorUtil;

import java.util.ArrayList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * Animation of Structures.  
 *
 */
public class StructAnimator extends AbstractAnimator {

	@Override
	public void animate(Language lang, TraceManager traceManager) {
		super.animate(lang, traceManager);

		String operator = VisitorUtil.toCode(trace.getRedex().getOperator());

		// struct-field
		if (operator.contains("-")) {

			// 1. (point-x (make-point 3.0 4.0))
			// 2. (define-struct point (x y))
			// 3. 3.0

			String f = operator.substring(operator.indexOf('-') + 1);			
			ArrayList<AST> fields = trace.getDefinition().getChild(1).getExpressions();
			
			int index;
			for (index=0; index<fields.size();index++) {
				if (VisitorUtil.toCode(fields.get(index).getOperator()).equals(f)) {
					break;
				}
			}
			
			
			AST field = fields.get(index);

			// 1. (point-x (make-point 3.0 4.0))
			// show the current expression as a tree on the left side and
			// highlight the redex
			AST call = trace.getRedex();
			ArrayList<AST> elements = new ArrayList<AST>();
			AST selectedField = call.getChild(0).getChild(index);
			elements.add(selectedField);
			elements.add(call.getOperator());

			prettyPrinter.print(trace.getAst(), trace.getRedex(), null, elements);			
			prettyPrinter.highlight(call.getOperator(), true);
			lang.nextStep();

			// 2. (define-struct point (x y))
			// show the struct definition on the right
			Node textTarget = new Offset(100, 0, prettyPrinter.getSourceCode(),
					AnimalScript.DIRECTION_NE);

			String definitionHeader = "Struct " + trace.getDefinition().getChild(0)
					+ ":";
			Text text = lang.newText(textTarget, definitionHeader, "", null);
			text.setFont(Util.getFont(), null, null);

			Node rightTarget = new Offset(0, 5, text, AnimalScript.DIRECTION_SW);
			PrettyPrinter right = new PrettyPrinter(lang);
			right.setTarget(rightTarget);
			ArrayList<AST> elements2 = new ArrayList<AST>();
			elements2.add(field);
			right.print(trace.getDefinition(), null, null, elements2);
			lang.nextStep();

			// highlight the field (right) and the corresponding expression
			// (left)
			prettyPrinter.highlight(selectedField, true);
			right.highlight(field, true);
			step();

			// hide struct definition 
			right.hide();
			text.hide();
			prettyPrinter.hide();
			
			// and show resulting value
			// astDrawer.draw(trace.getAst(), trace.getRedex(), Util.toString(trace.getResolvedBody()), null);
			


		}
		// struct?
		else {

			prettyPrinter.print(trace.getAst(), trace.getRedex(), null, null);
			lang.nextStep();

			prettyPrinter.highlightRedex();
			lang.nextStep();

			prettyPrinter.hide();
			prettyPrinter.print(trace.getAst(), trace.getRedex(), trace.getRedexValue(), null);
			prettyPrinter.highlightRedex();
			step();

			prettyPrinter.hide();

		}

	}

}
