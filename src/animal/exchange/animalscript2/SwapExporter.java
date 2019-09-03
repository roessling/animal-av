package animal.exchange.animalscript2;

import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;
import animal.animator.Animator;
import animal.animator.Swap;

public class SwapExporter extends TimedAnimatorExporter {
//  /**
//   * Export this object in ASCII format to the PrintWriter passed in.
//   *
//   * @param animator the current Move object
//   */
//  public String getExportString(Animator animator) {
//    StringBuilder sb = new StringBuilder(200);
//    Swap swap = (Swap)animator;
//
//    // 1. append the space to the output
//    sb.append(exportUsedObjects(swap.getObjectNums()));
////    String objectIDString = exportObjectIDs(animator);
//    sb.append("arraySwap on \"");
//    // TODO Ensure the name of the object goes in here!
//    int[] swapIndices = swap.getSwapElements();
//    sb.append("\" position ").append(swapIndices[0]);
//    sb.append(" with ").append(swapIndices[1]);
//
//    // 2. write out the info contained in the ancestor
//    // note: this ends without a space
//    sb.append(super.getExportString(animator));
//    return sb.toString();
//  }
  @Override
  public void export(Language lang, Animator animator) {
    if (!(animator instanceof Swap))
        return;
    Swap swap = (Swap)animator;

    int baseArrayID = swap.getObjectNums()[0];
    Primitive primitive = PTGraphicObjectExporter.hasBeenExported.get(baseArrayID);
    if (primitive == null || !(primitive instanceof ArrayPrimitive)) {
      System.err.println("Have to export id " +baseArrayID);
    }
    ArrayPrimitive baseArray = (ArrayPrimitive)primitive;
    int[] swapElements = swap.getSwapElements();
    if (swapElements == null || swapElements.length != 2) {
      System.err.println("Cannot export swap, sorry -- swapElements: " + swapElements
          + ((swapElements == null) ? "null" : ("has " +swapElements.length + " elems")));
      return;
    }
    int firstCell = swapElements[0], secondCell = swapElements[1];
    baseArray.swap(firstCell, secondCell,
          createTiming(true, swap), createTiming(false, swap));
  }
}
