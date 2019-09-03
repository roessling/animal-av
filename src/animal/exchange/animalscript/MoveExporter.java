package animal.exchange.animalscript;

import animal.animator.Animator;
import animal.animator.Move;

public class MoveExporter extends TimedAnimatorExporter
{
  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   *
   * @param animator the current Move object
   */
  public String getExportString(Animator animator)
  {
    StringBuilder sb = new StringBuilder(200);
    Move move = (Move)animator;

    // 1. append the space to the output
    sb.append(exportUsedObjects(move.getObjectNums()));
    sb.append(exportUsedObjects(new int[] { move.getMoveBaseNum() }, false));
    String objectIDString = exportObjectIDs(animator);
    String movePathIDString = exportObjectIDs(move.getMoveBaseNum());
    sb.append("move");
    sb.append(objectIDString);
    sb.append(" via");
    sb.append(movePathIDString);

    // 2. write out the info contained in the ancestor
    // note: this ends without a space
    sb.append(super.getExportString(animator));
    return sb.toString();
  }
}
