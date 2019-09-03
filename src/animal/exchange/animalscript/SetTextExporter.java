package animal.exchange.animalscript;

import animal.animator.Animator;
import animal.animator.SetText;

public class SetTextExporter extends TimedAnimatorExporter
{
  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   *
   * @param animator the current Move object
   */
  public String getExportString(Animator animator) {
    StringBuilder sb = new StringBuilder(200);
    SetText setText = (SetText)animator;

    // 1. append the space to the output
    sb.append(exportUsedObjects(setText.getObjectNums()));
//    sb.append(exportUsedObjects(new int[] { move.getMoveBaseNum() }, false));
    String objectIDString = exportObjectIDs(animator);
//    String movePathIDString = exportObjectIDs(move.getMoveBaseNum());
    sb.append("setText of").append(objectIDString);
    sb.append(" to \"").append(setText.getValue()).append("\"");

    // 2. write out the info contained in the ancestor
    // note: this ends without a space
    sb.append(super.getExportString(animator));
    return sb.toString();
  }
}
