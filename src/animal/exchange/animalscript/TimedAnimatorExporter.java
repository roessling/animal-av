package animal.exchange.animalscript;

import animal.animator.Animator;
import animal.animator.TimedAnimator;

public class TimedAnimatorExporter extends AnimatorExporter
{
  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   *
   * @param animator the animator to export
   */
  public String getExportString(Animator animator)
  {
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
    return sb.toString();
  }
}
