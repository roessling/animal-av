package animal.exchange.animalscript;

import animal.animator.Animator;
import animal.animator.ColorChanger;
import animal.misc.ColorChoice;

public class ColorChangerExporter extends TimedAnimatorExporter {
	/**
	 * Export this object in ASCII format to the PrintWriter passed in.
	 * 
	 * @param animator
	 *          the current Animator object
	 */
	public String getExportString(Animator animator) {
		StringBuilder sb = new StringBuilder(200);
		sb.append(exportUsedObjects(animator.getObjectNums()));
		// 1. write out the info contained in the ancestor
		// note: this ends without a space
		String objectIDString = exportObjectIDs(animator);
		ColorChanger colorChanger = (ColorChanger) animator;
		sb.append("color"); // must be adapted to reflect 'type'
		sb.append(objectIDString);
		String colorChangeMethod = colorChanger.getMethod();
		if (!colorChangeMethod.equalsIgnoreCase("color"))
			sb.append(" type \"" + colorChangeMethod + "\"");
		sb.append(" ").append(ColorChoice.getColorName(colorChanger.getColor()));
		sb.append(super.getExportString(animator));
		return sb.toString();
	}
}
