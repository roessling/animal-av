package htdptl.animators;

import htdptl.animation.IAnimator;
import htdptl.prettyPrinting.PrettyPrinter;
import htdptl.traces.TraceManager;
import htdptl.traces.TraceStep;
import algoanim.primitives.generators.Language;

/**
 * Each Animator class uses the TraceManager to get the current step in the
 * Trace, the Language object to append a new step to the animation and the
 * PrettyPrinter object to add Expression to the animation.
 * 
 */
public abstract class AbstractAnimator implements IAnimator {

  protected TraceManager  traceManager;
  protected Language      lang;
  protected TraceStep     trace;
  protected PrettyPrinter prettyPrinter;

  public void animate(Language lang, TraceManager traceManager) {
    this.lang = lang;
    this.traceManager = traceManager;
    prettyPrinter = new PrettyPrinter(lang);
    trace = traceManager.getCurrentTrace();
  }

  protected void step() {
    lang.nextStep();
    traceManager.next();
    trace = traceManager.getCurrentTrace();
  }

}
