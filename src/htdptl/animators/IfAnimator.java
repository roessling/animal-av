package htdptl.animators;

import htdptl.ast.AST;
import htdptl.traces.TraceManager;
import htdptl.visitors.VisitorUtil;

import java.util.ArrayList;

import algoanim.primitives.generators.Language;

/**
 * Animation of if expressions. The condition and the corresponding branch are highlighted.
 * @author david
 *
 */
public class IfAnimator  extends AbstractAnimator {

	@Override
	public void animate(Language lang, TraceManager traceManager) {
		super.animate(lang,traceManager);
		
		// show ast
		ArrayList<AST> elements = new ArrayList<AST>();
		AST condition = trace.getRedex().getChild(0);
		elements.add(condition);
		int offset = VisitorUtil.toCode(condition).equals("true") ? 1 : 2 ;
		AST branch = trace.getRedex().getChild(offset);
		elements.add(branch);		
		
		
		prettyPrinter.print(trace.getAst(), trace.getRedex(), null, elements);
		lang.nextStep();
					
		
		prettyPrinter.highlight(condition, true);
		prettyPrinter.highlightBlock(prettyPrinter.getRedexPosition(),offset+1);
		
		lang.nextStep();
		
		
		prettyPrinter.hide();
		traceManager.next();
		
		
		
	}
	
	

	

}
