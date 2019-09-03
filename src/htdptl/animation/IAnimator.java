package htdptl.animation;

import htdptl.traces.TraceManager;
import algoanim.primitives.generators.Language;

/**
 * An IAnimator adds the corresponding animation for the current step in the
 * TraceManager object to the animation using the Language object.
 * 
 */
public interface IAnimator {

  public void animate(Language lang, TraceManager traceManager);
}
