package animal.exchange.animalscript;

import animal.animator.Animator;
import animal.animator.TimedShow;

public class TimedShowExporter extends TimedAnimatorExporter {
	/**
	 * Export this object in ASCII format to the PrintWriter passed in.
	 * 
	 * @param animator
	 *          the current Animator object
	 */
	public String getExportString(Animator animator) {
		StringBuilder sb = new StringBuilder(200);
		TimedShow timedShow = (TimedShow) animator;
		sb.append(exportUsedObjects(timedShow.getObjectNums()));
		if (!timedShow.isShow()) {
			sb.append(timedShow.isShow() ? "show" : "hide");
			sb.append(exportObjectIDs(timedShow));
		}
		return sb.toString();
	}
}
