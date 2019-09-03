package htdptl.animators;

import htdptl.prettyPrinting.PrettyPrinter;
import htdptl.traces.TraceManager;
import htdptl.util.Util;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * Animation of filtered steps in the trace.
 * The step before and after the removed steps is shown.
 * Between the steps "..." is printed to indicate that steps have been removed  
 *
 */
public class FilteredTraceStepsAnimator extends AbstractAnimator {

	@Override
	public void animate(Language lang, TraceManager traceManager) {	
		super.animate(lang, traceManager);
		
		int step = traceManager.getStep();
		prettyPrinter.print(trace.getAst(), trace.getRedex(), null, null);
		prettyPrinter.highlightRedex();
		step();
		
			
		
		PrettyPrinter right = new PrettyPrinter(lang);
		Node rightTarget = new Offset(120,-17,prettyPrinter.getSourceCode(),AnimalScript.DIRECTION_NE);
		right.setTarget(rightTarget);
		right.print(trace.getAst(), trace.getRedex(), null, null);
		
		Node textTarget = new Offset(0, -30, right.getSourceCode(),
				AnimalScript.DIRECTION_NW);	
		String theText = "steps "+(step+1)+" - "+(traceManager.getStep()-1)+" are filtered. Step "+traceManager.getStep()+" is:";
		Text text = lang.newText(textTarget, theText, "", null);
		text.setFont(Util.getBoldFont(), null, null);
		
		lang.nextStep();
		
		prettyPrinter.hide();
		right.hide();
		text.hide();
		

		
	}
	
}
