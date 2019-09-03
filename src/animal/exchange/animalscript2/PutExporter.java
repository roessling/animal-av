package animal.exchange.animalscript2;

import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.IntArray;
import algoanim.primitives.Primitive;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import animal.animator.Animator;
import animal.animator.Put;

public class PutExporter extends TimedAnimatorExporter {
//  /**
//   * Export this object in ASCII format to the PrintWriter passed in.
//   *
//   * @param animator the current Move object
//   */
//  public String getExportString(Animator animator) {
//  	StringBuilder sb = new StringBuilder(200);
//    Put put = (Put)animator;
//
//    // 1. append the space to the output
//    sb.append(exportUsedObjects(put.getObjectNums()));
////    String objectIDString = exportObjectIDs(animator);
//    sb.append("arrayPut \"");
//    sb.append(put.getContent());
//    sb.append("\" on \"");
//    // TODO Ensure the name of the object goes in here!
//    sb.append("\" position ").append(put.getCell());
//
//    // 2. write out the info contained in the ancestor
//    // note: this ends without a space
//    sb.append(super.getExportString(animator));
//    return sb.toString();
//  }
  @Override
  public void export(Language lang, Animator animator) {
    if (!(animator instanceof Put))
        return;
    Put put = (Put)animator;
    int baseArrayID = put.getObjectNums()[0];
    Primitive baseArray = PTGraphicObjectExporter.hasBeenExported.get(baseArrayID);
    if (baseArray == null || !(baseArray instanceof ArrayPrimitive)) {
      System.err.println("Have to export id " +baseArrayID);
    }
    if (baseArray instanceof IntArray) {
      ((IntArray)baseArray).put(put.getCell(), Integer.valueOf(put.getContent()),
          createTiming(true, put), createTiming(false, put));
    } else if (baseArray instanceof DoubleArray) {
      ((DoubleArray)baseArray).put(put.getCell(), Double.valueOf(put.getContent()),
          createTiming(true, put), createTiming(false, put));
    } else if (baseArray instanceof StringArray) {
      ((StringArray)baseArray).put(put.getCell(), put.getContent(),
          createTiming(true, put), createTiming(false, put));
    }
    else
      System.err.println("Could not cast " +baseArray.getClass().getName() 
          +" to I/D/S-Array");
  }
}
