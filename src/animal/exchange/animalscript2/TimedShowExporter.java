package animal.exchange.animalscript2;

import algoanim.primitives.generators.Language;
import animal.animator.Animator;
import animal.animator.TimedShow;

public class TimedShowExporter extends TimedAnimatorExporter {
//	/**
//	 * Export this object in ASCII format to the PrintWriter passed in.
//	 * 
//	 * @param animator
//	 *          the current Animator object
//	 */
//	public String getExportString(Animator animator) {
//		StringBuilder sb = new StringBuilder(200);
//		TimedShow timedShow = (TimedShow) animator;
//		sb.append(exportUsedObjects(timedShow.getObjectNums()));
//		if (!timedShow.isShow()) {
//			sb.append(timedShow.isShow() ? "show" : "hide");
//			sb.append(exportObjectIDs(timedShow));
//		}
//		return sb.toString();
//	}

	@Override
	public void export(Language lang, Animator animator) {
		TimedShow timedShow = (TimedShow) animator;
		// export all objects used that were not exported before...
		exportUsedObjects(lang, timedShow.getObjectNums(), timedShow.isShow(),
				timedShow.getOffset(), timedShow.getDuration(), timedShow.isUnitIsTicks());
//		TimedShow timedShow = 
//		
//		if (!timedShow.isShow()) {
//			sb.append(timedShow.isShow() ? "show" : "hide");
//			sb.append(exportObjectIDs(timedShow));
//		}
	}
}
