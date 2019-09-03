package animal.exchange.animalscript;

import animal.animator.Animator;
import animal.animator.Rotate;

public class RotateExporter extends TimedAnimatorExporter {
	/**
	 * Export this object in ASCII format to the PrintWriter passed in.
	 * 
	 * @param animator
	 *          the current Animator object
	 */
	public String getExportString(Animator animator) {
		// 1. write out the info contained in the ancestor
		// note: this ends without a space
		StringBuilder sb = new StringBuilder(200);
		Rotate rotate = (Rotate) animator;
		sb.append(exportUsedObjects(rotate.getObjectNums()));
		sb.append(exportUsedObjects(rotate.getCenterNum()));
		String objectIDString = exportObjectIDs(animator);
		String centerIDString = exportObjectIDs(rotate.getCenterNum());

		sb.append("rotate");
		sb.append(objectIDString);
		sb.append("around");
		sb.append(centerIDString);

		// 2. write out the degrees of the rotation
		sb.append(" degrees");
		sb.append(rotate.getDegrees());
		sb.append(super.getExportString(animator));
		return sb.toString();
	}
}
