package animal.exchange.animalscript;

import animal.animator.Animator;
import animal.animator.Swap;

public class SwapExporter extends TimedAnimatorExporter {
  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   *
   * @param animator the current Move object
   */
  public String getExportString(Animator animator) {
    StringBuilder sb = new StringBuilder(200);
    Swap swap = (Swap)animator;

    // 1. append the space to the output
    sb.append(exportUsedObjects(swap.getObjectNums()));
//    String objectIDString = exportObjectIDs(animator);
    sb.append("arraySwap on \"");
    // TODO Ensure the name of the object goes in here!
    int[] swapIndices = swap.getSwapElements();
    sb.append("\" position ").append(swapIndices[0]);
    sb.append(" with ").append(swapIndices[1]);

    // 2. write out the info contained in the ancestor
    // note: this ends without a space
    sb.append(super.getExportString(animator));
    return sb.toString();
  }
}
