package animal.exchange.animalscript2;

import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;
import animal.animator.Animator;
import animal.animator.ColorChanger;

public class ColorChangerExporter extends TimedAnimatorExporter {
//	/**
//	 * Export this object in ASCII format to the PrintWriter passed in.
//	 * 
//	 * @param animator
//	 *          the current Animator object
//	 */
//	public String getExportString(Animator animator) {
//		StringBuilder sb = new StringBuilder(200);
//		sb.append(exportUsedObjects(animator.getObjectNums()));
//		// 1. write out the info contained in the ancestor
//		// note: this ends without a space
//		String objectIDString = exportObjectIDs(animator);
//		ColorChanger colorChanger = (ColorChanger) animator;
//		sb.append("color"); // must be adapted to reflect 'type'
//		sb.append(objectIDString);
//		String colorChangeMethod = colorChanger.getMethod();
//		if (!colorChangeMethod.equalsIgnoreCase("color"))
//			sb.append(" type \"" + colorChangeMethod + "\"");
//		sb.append(" ").append(ColorChoice.getColorName(colorChanger.getColor()));
//		sb.append(super.getExportString(animator));
//		return sb.toString();
//	}

  @Override
  public void export(Language lang, Animator animator) {
    if (!(animator instanceof ColorChanger))
        return;
    ColorChanger colorChanger = (ColorChanger)animator;
    
    int[] oids = animator.getObjectNums();
    for (int oid : oids) {
      if (oid > -1) {
        Primitive currentPrimitive = PTGraphicObjectExporter.hasBeenExported.get(oid);
        if (currentPrimitive != null)
          currentPrimitive.changeColor(colorChanger.getType(), 
              colorChanger.getColor(), createTiming(true, colorChanger),
              createTiming(false, colorChanger));
        else
          System.err.println("did not get information for ID " +oid);
      }
    }
  }
}
