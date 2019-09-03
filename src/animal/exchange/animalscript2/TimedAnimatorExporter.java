package animal.exchange.animalscript2;

import algoanim.primitives.generators.Language;
import algoanim.util.MsTiming;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.animator.Animator;
import animal.animator.TimedAnimator;

public abstract class TimedAnimatorExporter extends AnimatorExporter {
  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   *
   * @param animator the animator to export
   */
  public void exportTiming(Language lang, Animator animator) {
    StringBuilder sb = new StringBuilder(200);
    TimedAnimator ta = (TimedAnimator)animator;
    int duration = ta.getDuration(), offset = ta.getOffset();
    boolean unitIsTicks = ta.isUnitIsTicks();

    if (offset != 0)
    {
      sb.append(" offset ");
      sb.append(offset);
      sb.append((unitIsTicks) ? " ticks" : " ms");
    }

    if (duration != 0)
    {
      sb.append(" within ");
      sb.append(duration);
      sb.append((unitIsTicks) ? " ticks" : " ms");
    }
//    return sb.toString();
  }
  protected Timing createTiming(boolean isOffset, TimedAnimator animator) {
    boolean isTicksBased = animator.isUnitIsTicks();
    if (isTicksBased)
      return new TicksTiming((isOffset) ? animator.getOffset(): animator.getDuration());
    return new MsTiming((isOffset) ? animator.getOffset() : animator.getDuration());
  }
}
