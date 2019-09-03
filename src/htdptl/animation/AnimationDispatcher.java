package htdptl.animation;

import htdptl.traces.TraceManager;
import htdptl.traces.TraceStep;
import htdptl.visitors.VisitorUtil;

import java.util.HashMap;

/**
 * This class is used while creating the animation. If the next steps are
 * filtered the filteredTraceStepAnimator will be returned. If the next steps
 * are not filtered the animator class is assigned corresponding to the current
 */
public class AnimationDispatcher {

  static HashMap<String, IAnimator> animators = new HashMap<String, IAnimator>();
  private static IAnimator          defaultAnimator;
  private static IAnimator          filteredTraceStepAnimator;

  public static IAnimator getAnimator(TraceManager traceManager) {

    TraceStep trace = traceManager.getCurrentTrace();
    String operator = VisitorUtil.toCode(trace.getRedex().getOperator());
    if (traceManager.isNextStepFiltered()) {
      return filteredTraceStepAnimator;
    } else {
      IAnimator animator = animators.get(operator);
      return (animator == null) ? defaultAnimator : animator;
    }

  }

  public static void addAnimator(String operator, IAnimator animator) {
    animators.put(operator, animator);
  }

  public static void setDefaultAnimator(IAnimator animator) {
    defaultAnimator = animator;
  }

  public static void setFilteredTraceStepAnimator(IAnimator animator) {
    filteredTraceStepAnimator = animator;
  }

  public static void clean() {
    animators.clear();
  }

  public static IAnimator getDefaultAnimator() {
    return defaultAnimator;
  }

}
