package animal.exchange.animalscript2;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;
import animal.animator.Animator;
import animal.animator.Move;

public class MoveExporter extends TimedAnimatorExporter {
//  /**
//   * Export this object in ASCII format to the PrintWriter passed in.
//   * 
//   * @param animator
//   *          the current Move object
//   */
//  public String getExportString(Animator animator) {
//    StringBuilder sb = new StringBuilder(200);
//    Move move = (Move) animator;
//
//    // 1. append the space to the output
//    sb.append(exportUsedObjects(move.getObjectNums()));
//    sb.append(exportUsedObjects(new int[] { move.getMoveBaseNum() }, false));
//    String objectIDString = exportObjectIDs(animator);
//    String movePathIDString = exportObjectIDs(move.getMoveBaseNum());
//    sb.append("move");
//    sb.append(objectIDString);
//    sb.append(" via");
//    sb.append(movePathIDString);
//
//    // 2. write out the info contained in the ancestor
//    // note: this ends without a space
//    sb.append(super.getExportString(animator));
//    return sb.toString();
//  }

  @Override
  public void export(Language lang, Animator animator) {
    if (!(animator instanceof Move))
        return;
    Move move = (Move)animator;
    Primitive moveBase = PTGraphicObjectExporter.hasBeenExported.get(move.getMoveBaseNum());
    if (moveBase == null) {
      System.err.println("Have to export id " +move.getMoveBaseNum());
//      PTGraphicObject moveBase = move.getMoveBaseNum()
    }    
    int[] oids = animator.getObjectNums();
    for (int oid : oids) {
      if (oid > -1) {
        Primitive currentPrimitive = PTGraphicObjectExporter.hasBeenExported.get(oid);
        if (currentPrimitive != null)
          try {
          currentPrimitive.moveVia("S", move.getType(), moveBase,
              createTiming(true, move),
              createTiming(false, move));
          } catch(IllegalDirectionException ide) {
            System.err.println("Ouch");
          }
        else
          System.err.println("did not get information for ID " +oid);
      }
    }
  }

}
