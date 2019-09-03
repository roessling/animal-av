package animal.exchange.animalscript2;

import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;
import animal.animator.Animator;
import animal.animator.Rotate;

public class RotateExporter extends TimedAnimatorExporter {
//	/**
//	 * Export this object in ASCII format to the PrintWriter passed in.
//	 * 
//	 * @param animator
//	 *          the current Animator object
//	 */
//	public String getExportString(Animator animator) {
//		// 1. write out the info contained in the ancestor
//		// note: this ends without a space
//		StringBuilder sb = new StringBuilder(200);
//		Rotate rotate = (Rotate) animator;
//		sb.append(exportUsedObjects(rotate.getObjectNums()));
//		sb.append(exportUsedObjects(rotate.getCenterNum()));
//		String objectIDString = exportObjectIDs(animator);
//		String centerIDString = exportObjectIDs(rotate.getCenterNum());
//
//		sb.append("rotate");
//		sb.append(objectIDString);
//		sb.append("around");
//		sb.append(centerIDString);
//
//		// 2. write out the degrees of the rotation
//		sb.append(" degrees");
//		sb.append(rotate.getDegrees());
//		sb.append(super.getExportString(animator));
//		return sb.toString();
//	}
	
  @Override
  public void export(Language lang, Animator animator) {
    if (!(animator instanceof Rotate))
        return;
    Rotate rotate = (Rotate)animator;
    Primitive rotationCenter = PTGraphicObjectExporter.hasBeenExported.get(rotate.getCenterNum());
    if (rotationCenter == null) {
      System.err.println("Have to export id " +rotate.getCenterNum());
//      PTGraphicObject moveBase = move.getMoveBaseNum()
    }    
    int[] oids = animator.getObjectNums();
    for (int oid : oids) {
      if (oid > -1) {
        Primitive currentPrimitive = PTGraphicObjectExporter.hasBeenExported.get(oid);
        if (currentPrimitive != null)
          currentPrimitive.rotate(rotationCenter, rotate.getDegrees(),
              createTiming(true, rotate),
              createTiming(false, rotate));
        else
          System.err.println("did not get information for ID " +oid);
      }
    }
  }


}
