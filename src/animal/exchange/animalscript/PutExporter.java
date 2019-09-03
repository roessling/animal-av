package animal.exchange.animalscript;

import animal.animator.Animator;
import animal.animator.Put;

public class PutExporter extends TimedAnimatorExporter {
  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   *
   * @param animator the current Move object
   */
  public String getExportString(Animator animator) {
  	StringBuilder sb = new StringBuilder(200);
    Put put = (Put)animator;

    // 1. append the space to the output
    sb.append(exportUsedObjects(put.getObjectNums()));
//    String objectIDString = exportObjectIDs(animator);
    sb.append("arrayPut \"");
    sb.append(put.getContent());
    sb.append("\" on \"");
    // TODO Ensure the name of the object goes in here!
    sb.append("\" position ").append(put.getCell());

    // 2. write out the info contained in the ancestor
    // note: this ends without a space
    sb.append(super.getExportString(animator));
    return sb.toString();
  }
}
